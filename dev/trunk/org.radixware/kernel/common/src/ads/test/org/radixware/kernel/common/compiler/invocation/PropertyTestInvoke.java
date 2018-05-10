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


public class PropertyTestInvoke {
	
	//@Test
	public void testBoolPropertyJavaBoxingImplicitThis() { //FAIL: NULLPOINTER
		
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
					String buf = baos.toString();
                    return buf.equals("setProp invocked");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testBoolPropertyJavaBoxingImplicitThis";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testBoxingInSwitch() {	//FAIL
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("f", false) {
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
                    return buf.equals("setProperty invocked");
                }
            };
			
            String jmpSampleSource = "JMLPropertyTest_testBoxingInSwitch";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testCompareProperty() { // FAIL
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("f", false) {
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
                    return buf.equals("setProp invocked");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testCompareProperty";

            boolean result = TextCompilerTest.compileCodeByResourceName("B", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testFieldInGenericBase() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("64846489");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testFieldInGenericBase";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testForeach() { //FAIL: CLASS CAST org.eclipse.jdt.internal.compiler.lookup.MethodBinding cannot be cast to org.eclipse.jdt.internal.compiler.lookup.FieldBinding
		
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
					String buf = baos.toString();
                    return buf.equals("qwerty");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testForeach";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyAsExplicitThis() { //FAIL: NULLPOINTER
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyAsExplicitThis";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyAsImplicitThis() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyAsImplicitThis";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}

	//@Test
	public void testGetterPropertyEqualLenWithOtherFunction() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyEqualLenWithOtherFunction";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyInIfCondition() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("ok");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyInIfCondition";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyInSwitch() { //FAIL: CLASS CAST org.eclipse.jdt.internal.compiler.lookup.MethodBinding cannot be cast to org.eclipse.jdt.internal.compiler.lookup.FieldBinding
		
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
					String buf = baos.toString();
                    return buf.equals("2");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyInSwitch";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyInWhileCondition() { //FAIL CLASS CAST
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyInWhileCondition";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyInvalidType() { // ??? HOW TO HANDLE EXPECTED COMPILATION ERROR?
		
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
					String buf = baos.toString();
                    return buf.equals("");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyInvalidType";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyOnAssign() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyOnAssign";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyOnFunctionCall() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyOnFunctionCall";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyOnInit() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyOnInit";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testGetterPropertyOnLocalDeclaration() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("11");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testGetterPropertyOnLocalDeclaration";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testInner() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("f invocked");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testInner";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testInnerClass() { //FAIL
		
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
					String buf = baos.toString();
                    return buf.equals("ok");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testInnerClass";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testInnerInGenericBase() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testInnerInGenericBase";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testLongPropertyExtendedBoxingImplicitThis() { //FAIL NULL POINTER
		
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
					String buf = baos.toString();
                    return buf.equals("1");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testLongPropertyExtendedBoxingImplicitThis";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testMethodsInGenericBase() { //OK
		
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
					String buf = baos.toString();
                    return buf.equals("null");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testMethodsInGenericBase";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testPropertyAddAssign() { //FAIL Prop cannot be resolved to a variabl
		
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
					String buf = baos.toString();
                    return buf.equals("null");
                }
            };

            String jmpSampleSource = "JMLPropertyTest_testPropertyAddAssign";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
}
