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
import org.junit.Ignore;
import static org.junit.Assert.*;
import org.radixware.kernel.common.compiler.TextCompilerTest;


public class JMLSwithTest {

    @Test
    public void testSwithWithEnumRefMix() {
        assertEquals("Compilation falied", 2, TextCompilerTest.compileErrorneousCode("Test", "JMLSwithTest_testSwithWithEnumRefMix"));
    }

    @Test
    public void testSwithWithDuplicateCase() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLSwithTest_testSwithWithDuplicateCase"));
    }

    @Test
    public void testSwithWithQualifiedEnumRef() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLSwithTest_testSwithWithQualifiedEnumRef"));
    }

    @Test
    public void testLocalVariableInCase() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLSwithTest_testLocalVariableInCase"));
    }
}