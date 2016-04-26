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

package org.radixware.kernel.common.defs.ads.platform;

import java.util.*;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;


public class RadixPlatformClass extends TypeSignature {

    public static abstract class ClassMember extends TypeSignature {

        private final String name;
        private final EAccess access;
        private final boolean isFinal;
        private final boolean isStatic;
        private final char[] signature;

        ClassMember(char[] name, char[] signature, int modifiers) {
            this.name = String.valueOf(name);
            this.signature = signature;

            isFinal = Flags.isFinal(modifiers);
            isStatic = Flags.isStatic(modifiers);

            if (Flags.isPublic(modifiers)) {
                access = EAccess.PUBLIC;
            } else if (Flags.isProtected(modifiers)) {
                access = EAccess.PROTECTED;
            } else if (Flags.isPrivate(modifiers)) {
                access = EAccess.PRIVATE;
            } else {
                access = EAccess.DEFAULT;
            }

            if (signature != null && signature.length > 0) {
                CharOperation.replace(this.signature, '/', '.');
                CharOperation.replace(this.signature, '$', '.');
            }
        }

        public final boolean isFinal() {
            return isFinal;
        }

        public final boolean isStatic() {
            return isStatic;
        }

        public final EAccess getAccess() {
            return access;
        }

        public final String getName() {
            return name;
        }

        public final char[] getSignature() {
            return signature;
        }

        public char[] getRadixSignature() {
            return CharOperation.concat(getName().toCharArray(), getSignature());
        }

        public abstract boolean isAbstract();
    }

    public static class Method extends ClassMember {

        private final AdsTypeDeclaration returnType;
        private final AdsTypeDeclaration[] parameterTypes;
        private String[] parameterName;
        private final AdsTypeDeclaration[] exceptions;
        private final boolean isConstructor;
        private final boolean isAbstract;
        private final boolean isVarargs;
        private final boolean isSynthetic;
        private final boolean isOverriden;

        Method(MethodBinding binding, IBinaryMethod methodInfo, boolean isOverriden) {
            super(binding.selector, binding.genericSignature() == null ? binding.signature() : binding.genericSignature(), binding.modifiers);
            isAbstract = Flags.isAbstract(binding.modifiers);
            this.isSynthetic = Flags.isSynthetic(binding.modifiers);

            if (binding.parameters != null && binding.parameters.length > 0) {
                char[][] argumentName = null;
                if (methodInfo != null){
                    argumentName = methodInfo.getArgumentNames();
                    if (argumentName != null && argumentName.length > 0){
                        parameterName = new String[argumentName.length];
                    }
                }
                parameterTypes = new AdsTypeDeclaration[binding.parameters.length];
                for (int i = 0; i < binding.parameters.length; i++) {
                    parameterTypes[i] = AdsTypeDeclaration.Factory.newInstance(binding.parameters[i]);
                    if (argumentName != null && i < argumentName.length){
                        parameterName[i] = new String(argumentName[i]);
                    }
                }
            } else {
                parameterTypes = new AdsTypeDeclaration[0];
            }
            if (binding.returnType != null) {
                returnType = AdsTypeDeclaration.Factory.newInstance(binding.returnType);
            } else {
                returnType = AdsTypeDeclaration.VOID;
            }
            

            if (binding.thrownExceptions != null && binding.thrownExceptions.length > 0) {
                this.exceptions = new AdsTypeDeclaration[binding.thrownExceptions.length];
                for (int i = 0; i < this.exceptions.length; i++) {
                    this.exceptions[i] = AdsTypeDeclaration.Factory.newInstance(binding.thrownExceptions[i]);
                }
            } else {
                this.exceptions = null;
            }

            isVarargs = Flags.isVarargs(binding.modifiers);
            isConstructor = binding.isConstructor();
            this.isOverriden = isOverriden;
        }

        @Override
        public final boolean isAbstract() {
            return isAbstract;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            builder.append('(');
            boolean first = true;
            for (AdsTypeDeclaration adsTypeDeclaration : parameterTypes) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(adsTypeDeclaration.toString());
            }
            builder.append(')');

            return "Method: " + getReturnType().toString() + " " + getName() + builder.toString();
        }

        public String[] getParameterName() {
            return parameterName;
        }

        public AdsTypeDeclaration[] getExceptions() {
            return exceptions;
        }

        public boolean isConstructor() {
            return isConstructor;
        }

        public boolean isSynthetic() {
            return isSynthetic;
        }

        public AdsTypeDeclaration[] getParameterTypes() {
            return parameterTypes;
        }

        public AdsTypeDeclaration getReturnType() {
            return returnType;
        }

        public boolean isVarargs() {
            return isVarargs;
        }

        public boolean isOverriden() {
            return isOverriden;
        }

    }

    public static class Field extends ClassMember {

        private final AdsTypeDeclaration valueType;

        Field(FieldBinding field) {
            super(field.name, field.genericSignature() == null ? field.type.signature() : field.genericSignature(), field.modifiers);
            valueType = AdsTypeDeclaration.Factory.newInstance(field.type);
        }

        @Override
        public final boolean isAbstract() {
            return false;
        }

        @Override
        public String toString() {
            return "Field: " + getValueType().toString() + " " + getName();
        }

        public final AdsTypeDeclaration getValueType() {
            return valueType;
        }
    }
    final boolean isGenericType;
    AdsTypeDeclaration declaration;
    AdsTypeDeclaration superclass;
    AdsTypeDeclaration[] superInterfaces;
    Method[] methods;
    Field[] fields;
    static final Method[] NO_METHODS = new Method[0];
    static final Field[] NO_FIELDS = new Field[0];
    private ReferenceBinding src;
    private PlatformLib context;

    protected RadixPlatformClass(PlatformLib context, ReferenceBinding src) {
      //  System.out.println(String.valueOf(CharOperation.concatWith(src.compoundName,'.')));
        this.context = context;
        this.declaration = AdsTypeDeclaration.Factory.newInstance(src);
        this.isGenericType = declaration.getGenericArguments() != null;
        this.src = src;
    }

    public boolean isFinal() {
        return Flags.isFinal(src.modifiers);
    }

    public boolean hasGenericArguments() {
        return isGenericType;
    }

    public AdsTypeDeclaration getDeclaration() {
        return declaration;
    }

    public boolean isInterface() {
        return Flags.isInterface(src.modifiers);
    }

    public boolean isAbstract() {
        return Flags.isAbstract(src.modifiers);
    }
    private static final String THROWABLE_CLASS_NAME = Throwable.class.getName();

    public boolean isException() {
        ReferenceBinding ref = src;
        while (ref != null) {
            if (THROWABLE_CLASS_NAME.equals(String.valueOf(CharOperation.concatWith(ref.compoundName, '.')))) {
                return true;
            }
            ref = ref.superclass();
        }
        return false;
    }

    public Method[] getMethods() {
        if (methods == null) {
            buildFieldsAndMethods();
        }
        return methods;
    }

    public Field[] getFields() {
        if (fields == null) {
            buildFieldsAndMethods();
        }
        return fields;
    }

    private void buildFieldsAndMethods() {
        final MethodBinding[] methods = src.methods();
        if (methods != null) {
            List<Method> mths = new ArrayList<>();         
            for (int i = 0; i < methods.length; i++) {
                MethodBinding method = methods[i];
                if ((method.isConstructor() && Flags.isStatic(method.getAccessFlags())) || Flags.isSynthetic(method.getAccessFlags())) {
                    continue;
                }
                char[] genericSignature = method.genericSignature();
                if (method.isDefault()){
                    continue;
                }
                IBinaryMethod methodInfo = null;
                boolean isOverriden = false;
                if (src instanceof AdsBinaryTypeBinding){
                        AdsBinaryTypeBinding srcClass = (AdsBinaryTypeBinding) src;
                        methodInfo = srcClass.findMethodInfo(method);
                        MethodBinding superMethod = srcClass.getHighestOverridenMethod(method);
                        if (superMethod != method){
                            isOverriden = true;
                        }
                }
                mths.add(new RadixPlatformClass.Method(method,methodInfo,isOverriden));
            }
            this.methods = mths.toArray(new Method[mths.size()]);
        } else {
            this.methods = NO_METHODS;
        }

        // get information about fields
        final FieldBinding[] fields = src.fields();
        if (fields != null) {
            ArrayList<RadixPlatformClass.Field> flds = new ArrayList<>();
            for (int i = 0; i < fields.length; i++) {
                FieldBinding field = fields[i];

                if (Flags.isSynthetic(field.getAccessFlags())) {
                    continue;
                }

                flds.add(new RadixPlatformClass.Field(field));
            }
            this.fields = flds.toArray(new Field[flds.size()]);
        }
    }
    
    public AdsTypeDeclaration[] getSuperInterfaces() {
        if (superInterfaces == null) {
            ReferenceBinding[] si = src.superInterfaces();
            if (si != null && si.length > 0) {
                superInterfaces = new AdsTypeDeclaration[si.length];
                for (int i = 0; i < superInterfaces.length; i++) {
                    superInterfaces[i] = AdsTypeDeclaration.Factory.newInstance(src.superInterfaces()[i]);
                }
            } else {
                superInterfaces = new AdsTypeDeclaration[0];
            }
        }
        return superInterfaces;
    }

    public AdsTypeDeclaration getSuperclass() {
        if (superclass == null) {
            if (src.superclass() != null) {
                superclass = AdsTypeDeclaration.Factory.newInstance(src.superclass());
            } else {
                superclass = AdsTypeDeclaration.UNDEFINED;
            }
        }
        return superclass == AdsTypeDeclaration.UNDEFINED ? null : superclass;
    }

    public RadixPlatformClass getSuperClass() {
        ReferenceBinding sc = src.superclass();
        if (sc != null) {
            return context.findPlatformClass(String.valueOf(src.superclass().qualifiedSourceName()));
        } else {
            return null;
        }
    }

    private Method findMethodBySignature(char[] signature) {
        for (final Method method : getMethods()) {
            if (CharOperation.equals(signature, method.getRadixSignature())) {
                return method;
            }

        }
        return null;
    }

    public Field findFieldByProfile(AdsPropertyDef prop) {
        if (prop == null) {
            return null;
        }

        String publishedName = prop instanceof AdsTransparentPropertyDef
                ? String.valueOf(((AdsTransparentPropertyDef) prop).getPublishedPropertyName())
                : prop.getName();

        AdsTypeDeclaration typeDeclaration = prop.getTypedObject().getType();

        for (Field field : fields) {
            if (field.getName().equals(publishedName) && field.getValueType().equalsTo(prop, typeDeclaration)) {
                return field;
            }
        }
        return null;
    }

    public Method findMethodByProfile(AdsMethodDef method) {

        String publishedName = method instanceof AdsTransparentMethodDef ? new String(((AdsTransparentMethodDef) method).getPublishedMethodName()) : method.getName();
        AdsTypeDeclaration[] normalizedProfile = method.getProfile().getNormalizedProfile();
        final List<ThrowsListItem> throwsList = method.getProfile().getThrowsList().list();

        final AdsTypeDeclaration[] throwsItems = new AdsTypeDeclaration[throwsList.size()];
        for (int i = 0; i < throwsList.size(); ++i) {
            throwsItems[i] = throwsList.get(i).getException();
        }

        mainLoop:
        for (Method m : getMethods()) {
            if (!m.getName().equals(publishedName)) {
                continue;
            }

            int profileSize = m.parameterTypes.length + 1;
            if (profileSize != normalizedProfile.length) {
                continue;
            }

            if (throwsItems.length > 0) {
                if (m.exceptions == null || m.exceptions.length != throwsItems.length) {
                    continue;
                }

                Comparator<AdsTypeDeclaration> typeComparator = new Comparator<AdsTypeDeclaration>() {
                    @Override
                    public int compare(AdsTypeDeclaration o1, AdsTypeDeclaration o2) {
                        return o1.getQualifiedName().compareTo(o2.getQualifiedName());
                    }
                };

                Arrays.sort(m.exceptions, typeComparator);
                Arrays.sort(throwsItems, typeComparator);
            }

            AdsTypeDeclaration[] profile = new AdsTypeDeclaration[profileSize];
            System.arraycopy(m.parameterTypes, 0, profile, 0, m.parameterTypes.length);
            profile[profileSize - 1] = m.returnType;

            for (int i = 0; i < profileSize; i++) {
                if (!normalizedProfile[i].equalsTo(method, profile[i])) {
                    continue mainLoop;
                }
            }

            for (int i = 0; i < throwsItems.length; i++) {
                if (!throwsItems[i].equalsTo(method, m.exceptions[i])) {
                    continue mainLoop;
                }
            }
            return m;//NOPMD
        }

        return null;
    }

    public Collection<Method> findMethodsByName(String name) {
        final ArrayList<Method> findMethods = new ArrayList<>();
        for (final Method m : getMethods()) {
            if (m.getName().equals(name)) {
                findMethods.add(m);
            }
        }

        return findMethods;
    }

    public Field findFieldByName(String name) {
        for (final Field field : getFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }
}
