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

package org.radixware.kernel.common.compiler.onlycompilation;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.radixware.kernel.common.compiler.TextCompilerTest;


public class JMLEnumTest {

    @Test 
    public void testStaticInitializer() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLEnumTest_testStaticInitializer"));
    }

    @Test 
    public void testEmptyEnum() {
        assertEquals("Compilation failed", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLEnumTest_testEmptyEnum"));
    }
}