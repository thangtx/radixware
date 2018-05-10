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


public class OverrideTestInvoke {
	
	@Test
	public void testOverride() {
		
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
                    return baos.toString().equals("a invocked");
                }
            };


            String jmpSampleSource = "OverrideTest_testOverrideInterfaceMethodWithAnnotation";

            boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);

            assertTrue("Code execution failed", result);
        } finally {
            System.setErr(stdErr);
        }
		
	}
	
}
