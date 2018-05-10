/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.radixware.kernel.common.enums.EEventSeverity;

/**
 *
 * @author npopov
 */
class ClassCheckVisitor extends ClassVisitor {

    private final Class<?> checkedClass;
    private final String name;
    private final NoizyVerifier logger;
    private final ClassLoader cl;
    private final Set<String> notFoundClasses;
    private String userFuncMethodId;
    private int line;
    private boolean hadErrors = false;
    private List<IClassCheckProblem> problems;

    private static final Class<?>[] EMPTY_CLASSES = new Class<?>[]{};
    private static final Map<String, Class<?>> PRIMITIVE_CLASSES;

    static {
        final HashMap typesMap = new HashMap();
        typesMap.put("B", byte.class);
        typesMap.put("C", char.class);
        typesMap.put("D", double.class);
        typesMap.put("F", float.class);
        typesMap.put("I", int.class);
        typesMap.put("J", long.class);
        typesMap.put("S", short.class);
        typesMap.put("Z", boolean.class);
        typesMap.put("V", Void.TYPE);
        PRIMITIVE_CLASSES = Collections.unmodifiableMap(typesMap);
    }

    public ClassCheckVisitor(String name, Class<?> clazz, Set<String> notFoundClasses, ClassLoader cl, NoizyVerifier logger) {
        super(Opcodes.ASM5);
        this.name = name;
        this.checkedClass = clazz;
        this.notFoundClasses = notFoundClasses != null ? notFoundClasses : new HashSet<String>();
        this.logger = logger;
        this.cl = cl;
    }

    @Override
    public MethodVisitor visitMethod(int access, final String name, final String desc, String signature, String[] exceptions) {
        //first visited method - constructor, we ignore him
        if (!name.startsWith("<")) {
            userFuncMethodId = name;
        }
        return new MethodVisitor(Opcodes.ASM5) {

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                check(owner, name + desc);
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                if (shouldBeIgnored(type)) {
                    return;
                }
                Class<?> sigs = getClassByName(type);
                if (sigs == null) {
                    error(type, null);
                } else if (isDeprecated(sigs)) {
                    message(EEventSeverity.WARNING, type, null, "Is deprecated", null);
                }
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                check(owner, name + '#' + desc);
            }

            @Override
            public void visitLineNumber(int line, Label start) {
                ClassCheckVisitor.this.line = line;
            }

            private void check(String owner, String sig) {
                if (shouldBeIgnored(owner)) {
                    return;
                }
                if (find(getClassByName(owner), sig, true)) {
                    return;
                }
                error(owner, sig);
            }

            private boolean shouldBeIgnored(String type) {
                if (type.charAt(0) == '[') {
                    return true;
                }
                return false;
            }
        };
    }

    private boolean find(Class<?> c, String sig, boolean baseFind) {
        if (c == null) {
            return false;
        }

        if (getMemberBySignature(c, sig) != null) {
            return true;
        }

        if (sig.startsWith("<")) // constructor and static initializer shouldn't go up the inheritance hierarchy
        {
            return false;
        }

        if (find(c.getSuperclass(), sig, false)) {
            return true;
        }

        if (c.getInterfaces() != null) {
            final Class<?>[] interfaces = c.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (find(interfaces[i], sig, false)) {
                    return true;
                }
            }
        }

        // This is a rare case and quite expensive, so moving it to the end of this method and only execute it from
        // first find-call.
        if (baseFind) {
            // MANIMALSNIFFER-49
            Pattern returnTypePattern = Pattern.compile("(.+\\))L(.+);");
            Matcher returnTypeMatcher = returnTypePattern.matcher(sig);
            if (returnTypeMatcher.matches()) {
                String method = returnTypeMatcher.group(1);
                String returnType = returnTypeMatcher.group(2);

                Class<?> returnClass = getClassByName(returnType);

                if (returnClass != null && returnClass.getSuperclass() != null) {
                    String oldSignature = method + 'L' + returnClass.getSuperclass().getName() + ';';
                    if (find(c, oldSignature, false)) {
                        logger.message(name + (line > 0 ? ":" + line : "")
                                + ": Covariant return type change detected: "
                                + Utils.toSourceForm(c.getName(), oldSignature) + " has been changed to "
                                + Utils.toSourceForm(c.getName(), sig));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Class<?> getClassByName(String className) {
        try {
            return findClassByName(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private Class<?> findClassByName(String className) throws ClassNotFoundException {
        if (notFoundClasses.contains(className)) {
            return null;
        }
        try {
            return Class.forName(className.replace('/', '.'), false, cl);
        } catch (ClassNotFoundException ex) {
            notFoundClasses.add(className);
            throw ex;
        }
    }

    private Member getMemberBySignature(Class<?> clazz, String sign) {
        if (clazz == null) {
            return null;
        }
        
        Member result = null;
        if (sign.startsWith("<")) {
            try {
                result = clazz.getDeclaredConstructor(getParameterTypes(sign));
            } catch (NoSuchMethodException ex) {
                //ignore
            } catch (Throwable ex) {
                error(name, sign, ex);
            }
        } else if (sign.indexOf('(') != -1) {
            try {
                final Method candidate = clazz.getDeclaredMethod(sign.substring(0, sign.indexOf('(')), getParameterTypes(sign));
                final Type expected = Type.getReturnType(sign);
                if (checkTypes(expected, candidate.getReturnType())) {
                    result = candidate;
                }
            } catch (NoSuchMethodException ex) {
                //ignore
            } catch (Throwable ex) {
                error(name, sign, ex);
            }
        } else if (sign.indexOf('#') != -1) {
            try {
                final String[] nameAndType = sign.split("#");
                final Field candidate = clazz.getDeclaredField(nameAndType[0]);
                final Type expected = Type.getType(nameAndType[1]);
                if (checkTypes(expected, candidate.getType())) {
                    result = candidate;
                }
            } catch (NoSuchFieldException ex) {
                //ignore
            } catch (Throwable ex) {
                error(name, sign, ex);
            }
        }
        
        if (result != null) {
            if (!checkAccess(clazz, result.getModifiers())) {
                error(name, sign, "Has " + getAccessModifier(result.getModifiers()) + " access");
            }
            if (result instanceof AnnotatedElement) {
                final AnnotatedElement annElem = (AnnotatedElement) result;
                if (isDeprecated(annElem)) {
                    message(EEventSeverity.WARNING, name, sign, "Is deprecated", null);
                }
            }
        }
        return result;
    }
    
    private boolean isDeprecated(AnnotatedElement annElem) {
        for (Annotation annotation : annElem.getDeclaredAnnotations()) {
            if (Deprecated.class.equals(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }
    
    private String getAccessModifier(final int mod) {
        if (Modifier.isPublic(mod)) {
            return "public";
        } else if (Modifier.isPrivate(mod)) {
            return "private";
        } else if (Modifier.isProtected(mod)) {
            return "protected";
        } else {
            return "package";
        }
    }
        
    private boolean checkAccess(final Class<?> ownerClazz, final int modifiers) {
        if (Modifier.isPublic(modifiers)) {
            return true;
        } else if (Modifier.isPrivate(modifiers)) {
            if (checkedClass == ownerClazz) {
                return true;
            } else {
                Class<?> enclosingCl = ownerClazz.getEnclosingClass();
                while (enclosingCl != null) {
                    if (checkedClass == enclosingCl) {
                        return true;
                    }
                    enclosingCl = enclosingCl.getEnclosingClass();
                }
                enclosingCl = checkedClass.getEnclosingClass();
                while (enclosingCl != null) {
                    if (ownerClazz == enclosingCl) {
                        return true;
                    }
                    enclosingCl = enclosingCl.getEnclosingClass();
                }
                return false;
            }
        } else if (Modifier.isProtected(modifiers)) {
            if (checkedClass.getPackage() == ownerClazz.getPackage()) {
                return true;
            }
            Class<?> baseClass = checkedClass.getSuperclass();
            while (baseClass != null) {
                if (baseClass == ownerClazz) {
                    return true;
                }
                baseClass = baseClass.getSuperclass();
            }
            return false;
        } else {
            return checkedClass.getPackage() == ownerClazz.getPackage();
        }
    }
    
    private boolean checkTypes(Type expected, Class<?> actual) throws ClassNotFoundException {
        return getClassFromType(expected) == actual;
    }

    private Class<?>[] getParameterTypes(String sign) throws ClassNotFoundException {
        if (sign.indexOf('(') == -1) {
            throw new IllegalArgumentException("Not a method signature: " + sign);
        }

        final Type[] types = Type.getArgumentTypes(sign.substring(sign.indexOf('(')));
        if (types.length > 0) {
            Class<?>[] classes = new Class<?>[types.length];
            for (int i = 0; i < types.length; i++) {
                classes[i] = getClassFromType(types[i]);
            }
            return classes;
        } else {
            return EMPTY_CLASSES;
        }
    }

    private Class<?> getClassFromType(Type t) throws ClassNotFoundException {
        final Class<?> res;
        final String desc = t.getDescriptor();
        if (PRIMITIVE_CLASSES.containsKey(desc)) {
            res = PRIMITIVE_CLASSES.get(desc);
        } else {
            if (t.getSort() == Type.ARRAY) {
                if (t.getElementType().getSort() == Type.OBJECT) {
                    res = findClassByName(desc.replace('/', '.'));
                } else {
                    res = findClassByName(desc);
                }
            } else {
                res = findClassByName(t.getClassName());
            }
        }
        return res;
    }
    
    private void error(String type, String sig, String mess) {
        message(EEventSeverity.ERROR, type, sig, mess, null);
    }
    
    private void error(String type, String sig) {
        message(EEventSeverity.ERROR, type, sig, "Undefined reference", null);
    }
    
    private void error(String type, String sig, Throwable ex) {
        message(EEventSeverity.ERROR, type, sig, null, ex);
    }

    private void message(EEventSeverity sev, String type, String sig, String mess, Throwable ex) {
        if (!hadErrors) {
            hadErrors = sev.getValue() >= EEventSeverity.ERROR.getValue();
        }
        CheckProblem p = new CheckProblem(sev, name, userFuncMethodId, line, type, sig, mess, ex);
        if (problems == null) {
            problems = new ArrayList<>();
        }
        problems.add(p);
    }
    
    public boolean thereAreErrors() {
        return hadErrors;
    }
    
    public List<IClassCheckProblem> getProblems() {
        return problems != null ? problems : Collections.<IClassCheckProblem>emptyList();
    }

    
    private static final class CheckProblem implements IClassCheckProblem {

        private final EEventSeverity sev;
        private final String className;
        private final String methodName;
        private final int line;
        private final String type;
        private final String signature;
        private final String message;
        private final Throwable ex;

        public CheckProblem(String className, String methodName, int line, String type, String signature, String message, Throwable ex) {
            this(EEventSeverity.ERROR, className, methodName, line, type, signature, message, ex);
        }
        
        public CheckProblem(EEventSeverity sev, String className, String methodName, int line, String type, String signature, String message, Throwable ex) {
            this.sev = sev;
            this.className = className;
            this.methodName = methodName;
            this.line = line;
            this.type = type;
            this.signature = signature;
            this.message = message;
            this.ex = ex;
        }
        
        @Override
        public EEventSeverity getSeverity() {
            return sev;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public String getMethodName() {
            return methodName;
        }

        @Override
        public String getClassName() {
            return className;
        }
        
        public String getSimpleClassName() {
            if (className.lastIndexOf('.') != -1) {
                return className.substring(className.lastIndexOf('.') + 1);
            }
            return className;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getSignature() {
            return signature;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public Throwable getException() {
            return ex;
        }

        @Override
        public String getPrettySignature() {
            return Utils.toSourceForm(type, signature);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (line > 0) {
                sb.append(String.format("%s.java:%d", getSimpleClassName(), line));
            } else {
                sb.append(getSimpleClassName());
            }
            sb.append(" : ").append(getPrettySignature());
            
            if (message != null) {
                sb.append(" : ").append(message);
            }
            if (ex != null) {
                sb.append(" : ").append(ex.getClass().getSimpleName() + ": "  + ex.getMessage());
            }
            return sb.toString();
        }

    }
}
