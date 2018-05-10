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
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.radixware.kernel.common.compiler.CompilerTestUtils;
import org.radixware.kernel.common.compiler.TextCompilerTest;


public class ComparisonsTestInvoke {
	
	@Test
	public void testBoolBooleanAnd() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testBoolBooleanAnd";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareBigDecimalAndDouble() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareBigDecimalAndDouble";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareBigDecimalAndInt() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareBigDecimalAndInt";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareBigDecimalAndInteger() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareBigDecimalAndInteger";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareBigDecimals() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareBigDecimals";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareBool() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareBool";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareBoolAndInt() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareBoolAndInt";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareIntandint() {
		//FIXME
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareIntandint";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareIntegerAndLong() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareIntegerAndLong";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareIntegerAndLongLess() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareIntegerAndLongLess";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareIntegers() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareIntegers";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareIntegersAndCharacter() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareIntegersAndCharacter";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareIntegersLess() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareIntegersLess";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareLongs() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareLongs";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareNums() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareNums";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareStringAndNull() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareStringAndNull";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareStrings() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareStrings";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareUnknownTypes() {
		//FIXME
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testCompareUnknownTypes";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompareWithBase() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("true");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompareWithBase";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompoundWithBaseBoolean() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("2");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompoundWithBaseBoolean";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testCompoundWithHeapBoolean() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("2");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testCompoundWithHeapBoolean";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testEqualBaseBooleans() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("2");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testEqualBaseBooleans";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testEqualHeapAndBaseBooleans() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("2");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testEqualHeapAndBaseBooleans";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testFloraTypesInReturnStatement() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testFloraTypesInReturnStatement";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testIfWithHeapBoolean() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("1");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testIfWithHeapBoolean";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testIntegerAndLongAdd() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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
                    return baos.toString().equals("3");
                }
            };


            String jmpSampleSource = "JMLComparisonsTest_testIntegerAndLongAdd";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testInvalidCompare() {
		//FIXME
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testInvalidCompare";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testInvalidLessBooleans() {
		//FIXME
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testInvalidLessBooleans";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testInvalidLessCompare() {
		//FIXME
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testInvalidLessCompare";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testKnownAndUnknown() {
		//FIXME
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testKnownAndUnknown";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testStringDivision() {
		//FIXME
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("main", true, new Class[]{String[].class}, new Object[]{new String[]{}}) {
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


            String jmpSampleSource = "JMLComparisonsTest_testStringDivision";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
}
