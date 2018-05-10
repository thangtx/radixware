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


public class StrangeTest {

    @Test 
    public void testmathOperationUsage() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testmathOperationUsage"));
    }

    @Test 
    public void testInnerCreation() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testInnerCreation"));
    }

    @Test 
    public void testBooleanUnboxingMethod() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testBooleanUnboxingMethod"));
    }

    @Test 
    public void testReturnCompare() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testReturnCompare"));
    }

    @Test 
    public void testGenericAccess() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testGenericAccess"));
    }

    @Test 
    public void testInheritedPropertySetter() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testInheritedPropertySetter"));
    }

    @Test 
    public void testNumInvokeAndCompareWithZero() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testNumInvokeAndCompareWithZero"));
    }

    @Test 
    public void testAbstractInstantiation() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testAbstractInstantiation"));
    }

    @Test 
    public void testMemberInitialization() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testMemberInitialization"));
    }

    @Test 
    public void testPropertyOfInnerClass() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testPropertyOfInnerClass"));
    }

    @Test 
    public void testIntArgsMethodCall() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "StrangeTest_testIntArgsMethodCall"));
    }

    @Test 
    public void testForeachProp() {
        assertTrue("Compilation falied", TextCompilerTest.compileCodeByResourceName("Test", "StrangeTest_testForeachProp", new TextCompilerTest.InvocationTest("test")));
    }

    @Test 
    public void testA() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testA"));
    }

    @Test 
    public void testCallCompare2() {
        assertEquals("Compilation falied", 2, TextCompilerTest.compileErrorneousCode("Test", "StrangeTest_testCallCompare2"));
    }

    @Test 
    public void testUnsafeCast() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testUnsafeCast"));
    }

    @Test 
    public void testCallCompare() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testCallCompare"));
    }

    @Test 
    public void testIndicator() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testIndicator"));
    }

    @Test 
    public void testProperty() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testProperty"));
    }

    @Test 
    public void testCallCompare3() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testCallCompare3"));
    }

    @Test 
    public void testCallCompare4() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testCallCompare4"));
    }

    @Test 
    public void testDateTime() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testDateTime"));
    }

    @Test 
    public void testCallCompare5() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testCallCompare5"));
    }

    @Test 
    public void testWildcard() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testWildcard"));
    }

    @Test 
    public void testEmptySwitch() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testEmptySwitch"));
    }

    @Test 
    public void testPropertyInitializationWithInnerClass() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "StrangeTest_testPropertyInitializationWithInnerClass"));
    }
}