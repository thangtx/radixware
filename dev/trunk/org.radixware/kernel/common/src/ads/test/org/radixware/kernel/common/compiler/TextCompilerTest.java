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

package org.radixware.kernel.common.compiler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import static org.junit.Assert.*;
import org.junit.Test;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.IJavaSourceProvider;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;


public class TextCompilerTest {

    private static Branch branch;

    static {
        try {
            branch = new BranchLoader().getBranch();
        } catch (IOException ex) {
            Logger.getLogger(TextCompilerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Branch getBranch() {
        return branch;
    }

    public static class GenericSrcDef extends AdsDefinition implements IJavaSourceProvider {

        private char[] src;

        public GenericSrcDef(String name, String source) {
            super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_USER_PROP), name);
            src = source.toCharArray();
        }

        @Override
        public EDefType getDefinitionType() {
            return EDefType.GENERIC;
        }

        @Override
        public char[] getJavaSource(String sourceId) {
            return src;
        }

        @Override
        public String[] getSourceIds() {
            return new String[]{getName()};
        }

        @Override
        public boolean isSaveable() {
            return true;
        }
    }

    public static boolean compileCode(String name, String srcName) {
        return compileErrorneousCode(name, srcName) == 0;
    }

    public static int compileErrorneousCode(String name, String srcName) {
        System.out.println("------------------------ " + srcName + " ------------------------>");
        try {
            return compileErrorneousCode(name, CompilerTestUtils.getJmlSampleSource(srcName), null);
        } finally {
            System.out.println("<------------------------ " + srcName + " ------------------------");
        }
    }

    public static boolean compileCodeByResourceName(String name, String srcName) {
        return compileCodeByResourceName(name, srcName, null);
    }

    public static boolean compileCodeByResourceName(String name, String srcName, InvocationTest tester) {
        System.out.println("------------------------ " + srcName + " ------------------------>");
        try {
            return compileErrorneousCode(name, CompilerTestUtils.getJmlSampleSource(srcName), tester) == 0;
        } finally {
            System.out.println("<------------------------ " + srcName + " ------------------------");
        }
    }

    public static boolean compileCode(String name, String src, InvocationTest tester) {

        try {
            return compileErrorneousCode(name, src, tester) == 0;
        } finally {
        }
    }

    public static int compileErrorneousCode(String name, String src, InvocationTest tester) {
        final GenericSrcDef def = new GenericSrcDef(name, src);
        try {
            final BranchLoader loader = new BranchLoader();
            final Layer l = loader.getBranch().getLayers().findByURI("org.radixware");
            final AdsModule module = (AdsModule) l.getAds().getModules().findById(Id.Factory.loadFrom("mdlUKQP7T5DCVBZVAIAPKIR7XGD7A"));
            module.getDefinitions().add(def);
            return compileCode((GenericSrcDef) module.getTopContainer().findById(def.getId()), tester);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            return 1;
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            return 1;
        } finally {
            def.delete();
        }
    }

    public static class InvocationTest {

        public OutputStream outputStream;
        public boolean isStatic;
        public String methodName;
        public Object[] arguments;
        public Class[] argumentTypes;

        public InvocationTest() {//NOPMD
            this(null, false, null, null);
        }

        public InvocationTest(String methodName) {//NOPMD
            this(methodName, false, null, null);
        }

        public InvocationTest(String methodName, boolean isStatic) {
            this(methodName, isStatic, null, null);
        }

        public InvocationTest(String methodName, boolean isStatic, Class[] argumentTypes, Object[] arguments) {//NOPMD
            this.isStatic = isStatic;
            this.methodName = methodName;
            this.arguments = arguments;
            this.argumentTypes = argumentTypes;
        }

        public boolean beforeInvoke(Class clazz) {
            return true;
        }

        public boolean invoke(Class clazz) {
            try {
                boolean beforeResult = beforeInvoke(clazz);
                if (methodName == null) {
                    return beforeResult;
                }
                Method method = clazz.getMethod(methodName, argumentTypes);
                Object result;
                method.setAccessible(true);
                if (isStatic) {
                    result = method.invoke(null, arguments);
                } else {
                    Constructor c = clazz.getDeclaredConstructor(null);
                    c.setAccessible(true);
                    result = method.invoke(c.newInstance(), arguments);
                }
                return afterInvoke(result);
            } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                Logger.getLogger(TextCompilerTest.class.getName()).log(Level.SEVERE, null, ex);
                fail();
                return false;
            } catch (IllegalAccessException ex) {
                Logger.getLogger(TextCompilerTest.class.getName()).log(Level.SEVERE, null, ex);
                fail();
                return false;
            }
        }

        public boolean afterInvoke(Object result) {
            return true;
        }
    }

    public static int compileCode(GenericSrcDef def, InvocationTest tester) {
        final int[] errorCount = new int[]{0};
        //long t = System.currentTimeMillis();
        System.out.println("<<======================== code ========================");
        System.out.println(def.src);
        System.out.println(">>======================== code ========================");
        final CompilationResult result = new org.radixware.kernel.common.compiler.Compiler(new IProblemHandler() {
            @Override
            public void accept(RadixProblem problem) {
                if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                    errorCount[0]++;
                }
                System.out.println(problem.getMessage());
            }
        }).compile(def, ERuntimeEnvironmentType.COMMON, def.getName());
        //System.out.println("Compilation time: " + (System.currentTimeMillis() - t));
        //try{
        if (result == null || result.hasErrors()) {
            int c = 0;
            if (result != null) {

                for (int i = 0; i < result.problemCount; i++) {
                    final IProblem p = result.problems[i];
                    if (p.isError()) {
                        c++;
                        System.err.println(p.getMessage());
                    }

                }
                assertEquals(errorCount[0], c);
            }
            return errorCount[0];
        } else {
            TestClassLoader classLoader = new TestClassLoader(def.getBranch().getDirectory(), result.getClassFiles());
            try {
                String className = JavaSourceSupport.getPackageName(def, JavaSourceSupport.UsagePurpose.COMMON_EXECUTABLE, false) + "." + def.getName();
                Class c = classLoader.loadClass(className);
                classLoader.resolve(c);
                if (tester != null) {
                    return tester.invoke(c) ? 0 : 1;
                } else {
                    return 0;
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TextCompilerTest.class
                        .getName()).log(Level.SEVERE, null, ex);
                fail();
            }
            return 1;
        } /*}finally{
         System.out.println("Total time: " + (System.currentTimeMillis() - t));
         } */
    }

    @Test
    public void testEmptyClass() {
        assertTrue("Compilation failed", compileCode("A", "public class A{"
                + "     public int a(){return 11;}"
                + "}", new InvocationTest("a", false, null, null) {
            @Override
            public boolean afterInvoke(Object result) {
                return result instanceof Integer && ((Integer) result).intValue() == 11;
            }
        }));
    }
}
