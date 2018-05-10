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


public class SwitchTestInvoke {
	
	//@Test
	public void testLocalVariableInCase() {
		
		final int testValue = -4;
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("f", true, new Class[]{int.class}, new Object[]{ testValue }) {
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
                    return baos.toString().equals(testValue + " 5 switch end");
                }
            };


            String jmpSampleSource = "JMLSwithTest_testLocalVariableInCase_1";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	//@Test
	public void testSwitchWithDuplicateCase() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("f", true) {
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
                    return baos.toString().equals("A");
                }
            };


            String jmpSampleSource = "JMLSwithTest_testSwithWithDuplicateCase";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
	@Test
	public void testSwithWithEnumRefMix() {
		
		PrintStream stdErr = System.err;
        try {
            TextCompilerTest.InvocationTest invoker = new TextCompilerTest.InvocationTest("f", true) {
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
                    return baos.toString().equals("A");
                }
            };


            String jmpSampleSource = "JMLSwithTest_testSwithWithEnumRefMix";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
}
