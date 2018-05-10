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
import org.radixware.kernel.common.compiler.TextCompilerTest;
import org.radixware.kernel.common.compiler.CompilerTestUtils;
import org.radixware.kernel.common.compiler.TextCompilerTest.InvocationTest;


public class BoxingTestInvoke {
    
    @Test
    public void test2partBoxing() {
	
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
                    return baos.toString().equals("-2147483648" + System.getProperty("line.separator"));
                }
            };


            String jmpSampleSource = "JMLBoxingTest_test2partBoxing";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
	
    }
    
    
    @Test
    public void testArrIntAsParameter() {
	
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
                    return baos.toString().equals("null");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testArrIntAsParameter";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
	
    }
	
	@Test
    public void testArrNumAsParameter() {
	
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
                    return baos.toString().equals("");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testArrNumAsParameter";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
	
    }
	
	
	@Test
    public void testBigDecimalInversion() {
	
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
					String buf = baos.toString();
                    return buf.equals("-5");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testBigDecimalInversion";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
	
    }
	
	@Test
	public void testBoolean() {
		
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
					String buf = baos.toString();
                    return buf.equals("");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testBoolean";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testBooleanWithIntByAnd() {
		
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
					String buf = baos.toString();
                    return buf.equals("true");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testBooleanWithIntByAnd";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testBoxing() {
		
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
					String buf = baos.toString();
                    return buf.equals("5");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testBoxing";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompileErrors() {
		
		PrintStream stdErr = System.err;
        try {
            InvocationTest invoker = new InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
                @Override
                public boolean beforeInvoke(Class clazz) {
                    outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true);
                    System.setErr(printStream);
                    return true;
                }

                @Override
                public boolean afterInvoke(Object result) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
					String buf = baos.toString();
                    return buf.equals(	"error:   UnknownType cannot be resolved to a type"			+ System.getProperty("line.separator") + 
										"error:   Incompatible operand types UnknownType and int"	+ System.getProperty("line.separator")
									 );
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testCompileErrors";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testExtendedBigDecimalBoxing() {
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testExtendedBigDecimalBoxing";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testExtendedBoxing() {
		
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
					String buf = baos.toString();
                    return buf.equals("5");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testExtendedBoxing";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testExtendedBoxingFromVariable() {
		
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
					String buf = baos.toString();
                    return buf.equals("3");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testExtendedBoxingFromVariable";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testFloraTypesInArray() {
		
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
					String buf = baos.toString();
                    return buf.equals("1100");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testFloraTypesInArray";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testFloraTypesInCast() {
		
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
					String buf = baos.toString();
                    return buf.equals("abcd");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testFloraTypesInCast";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testLongBoxing() {
		
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
					String buf = baos.toString();
                    return buf.equals("2");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testLongBoxing";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceInt() {
		
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
					String buf = baos.toString();
                    return buf.equals("3");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceInt";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceArrNum() {
		
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
					String buf = baos.toString();
                    return buf.equals("123");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceArrNum";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceIntInCast() {
		
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
					String buf = baos.toString();
                    return buf.equals("null");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceIntInCast";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceNum() {
		
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
					String buf = baos.toString();
                    return buf.equals("123");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceNum";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}

	@Test
	public void testReplaceNumInStaticConstant() {
		
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
					String buf = baos.toString();
                    return buf.equals("0");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceNumInStaticConstant";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceNumInStaticContext() {
		
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
					String buf = baos.toString();
                    return buf.equals("1.0");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceNumInStaticContext";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceStrInInstanceOf() {
		
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
					String buf = baos.toString();
                    return buf.equals("false");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceStrInInstanceOf";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceStrInInstanceOfAndCast() {
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceStrInInstanceOfAndCast";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testReplaceStrInStaticContext() {
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testReplaceStrInStaticContext";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testStr() {
		
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
					String buf = baos.toString();
                    return buf.equals("abcd");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testStr";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testUnboxingRefused() {
		
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
					String buf = baos.toString();
                    return buf.equals("3");
                }
            };


            String jmpSampleSource = "JMLBoxingTest_testUnboxingRefused";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
}
