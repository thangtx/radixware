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


package org.radixware.kernel.common.compiler.invocation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.radixware.kernel.common.compiler.TextCompilerTest;
import org.radixware.kernel.common.compiler.CompilerTestUtils;
import org.radixware.kernel.common.compiler.TextCompilerTest.InvocationTest;


public class BigDecimalTestInvoke {

    @Test
    public void testBigDecimalEqBigDecimal() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setOut(printStream);
                    return true;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    return baos.toString().equals("false");
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalEqBigDecimal", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalGeqBigDecimal() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("test", true) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    // 1 < 2 ? true
                    return (boolean) result == true;
                }
            };

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalGeqBigDecimal", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalAddition() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    return (java.math.BigDecimal) result == java.math.BigDecimal.valueOf(1);
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalAddition", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalMultiplication() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    // don't understand a test logic
                    return true;
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalMultiplication", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalDivide() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setOut(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    return baos.toString().equals("1");
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalDivide", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalNegate() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setOut(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    String strResult = baos.toString();
                    System.out.println("Test output:");
                    System.out.println(strResult);
                    return strResult.equals("-8");
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalNegate", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalZeroCompare() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setErr(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    String strResult = baos.toString();
                    return ("AAA" + System.getProperty("line.separator")).equals(strResult);
                }
            };

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalZeroCompare", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalComplexExpression() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setErr(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    return baos.toString().equals("-6");
                }
            };

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalComplexExpression", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalCompoundDivide() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setOut(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    String buf = baos.toString();
                    return baos.toString().equals("0.5");
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalCompoundDivide", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalCompoundIncrement() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    java.math.BigDecimal answer = (java.math.BigDecimal) result;

                    return true;
                }
            };

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalCompoundIncrement", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalWithDifferentTypes() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setErr(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    return baos.toString().equals("answer = 2");
                }
            };

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalWithDifferentTypes", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    @Ignore
    public void testCastToNum() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("test", true, new Class[]{Object.class}, new Object[]{new Object()}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setErr(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    return baos.toString().equals("cast err") == true ? true : false;
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testCastToNum_1", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testPassNegativeIntegerToNumArgument() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("test2", true) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    return clazz != null;
                }
            };

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testPassNegativeIntegerToNumArgument", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testAssignNegativeNumVariableToFinalNumVariable() {
        PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("test", true) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setErr(printStream);
                    return clazz != null;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                    return baos.toString().equals("-1234");
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testAssignNegativeNumVariableToFinalNumVariable", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }

    @Test
    public void testBigDecimalConstantRef() {
        PrintStream stdErr = System.err;
        try {
            int expected = 5;

            InvocationTest invoker = new InvocationTest("method", false, new Class[]{java.math.BigDecimal.class}, new Object[]{new java.math.BigDecimal(expected)}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    return clazz != null;
                }
            };


            boolean result = TextCompilerTest.compileCodeByResourceName("Test", "JMLBigDecimalTest_testBigDecimalConstantRef", invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
    }
}
