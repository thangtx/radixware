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

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.radixware.kernel.common.compiler.TextCompilerTest;
import org.radixware.kernel.common.compiler.CompilerTestUtils;
import org.radixware.kernel.common.compiler.TextCompilerTest.InvocationTest;


public class ClinitTestInvoke {
    
    
    @Test
    public void testEnumAccess() {
	
	InvocationTest invoker = new InvocationTest("a", true) {
	    
	    @Override
	    public boolean beforeInvoke(Class clazz) {
		return clazz != null;
	    }
	    
	};
	
	String jmpSampleSource = "ClinitTest_testEnumAccess";
	
	boolean result = TextCompilerTest.compileCodeByResourceName("Test", jmpSampleSource, invoker);
	
	assertTrue("Code execution failed", result);
	
    }
    
}
