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


public class JMLBoxingTest {

    @Test
    public void test2partBoxing() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_test2partBoxing"));
    }

    @Test
    public void testBoxing() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testBoxing"));
    }

    @Test
    public void testReplaceInt() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceInt"));
    }

    @Test
    public void testReplaceNum() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceNum"));
    }

    @Test
    public void testStr() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testStr"));
    }

    @Test
    public void testBoolean() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testBoolean"));
    }

    @Test
    public void testLongBoxing() {
        assertFalse("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testLongBoxing"));
    }

    @Test
    public void testIncompatibleArrAsParameter() {
        assertFalse("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testIncompatibleArrAsParameter"));
    }

    @Test
    public void testReplaceNumInStaticConstant() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceNumInStaticConstant"));
    }

    @Test
    public void testExtendedBigDecimalBoxing() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testExtendedBigDecimalBoxing"));
    }

    @Test
    public void testReplaceIntInCast() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceIntInCast"));
    }

    @Test
    public void testExtendedBoxingFromVariable() {
        assertFalse("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testExtendedBoxingFromVariable"));
    }

    @Test
    public void testReplaceNumInStaticContext() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceNumInStaticContext"));
    }

    @Test
    public void testReplaceStrInStaticContext() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceStrInStaticContext"));
    }

    @Test
    public void testExtendedBoxing() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testExtendedBoxing"));
    }

    @Test
    public void testReplaceStrInInstanceOf() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceStrInInstanceOf"));
    }

    @Test
    public void testArrNumAsParameter() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testArrNumAsParameter"));
    }

    @Test
    public void testBigDecimalInversion() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testBigDecimalInversion"));
    }

    @Test
    public void testBoolendWithIntByAnd() {
        assertFalse("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testBooleanWithIntByAnd"));
    }

    @Test
    public void testFloraTypesInArray() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testFloraTypesInArray"));
    }

    @Test
    public void testUnboxingRefused() {
        assertFalse("Compilation succeeded", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testUnboxingRefused"));
    }

    @Test
    public void testCompileErrors() {
        assertFalse("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testCompileErrors"));
    }

    @Test
    public void testReplaceArrNum() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceArrNum"));
    }

    @Test
    public void testFloraTypesInCast() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testFloraTypesInCast"));
    }

    @Test
    public void testPropertyVsFloraTypes() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testPropertyvsFloraTypes"));
    }

    @Test
    public void testArrIntAsParameter() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testArrIntAsParameter"));
    }

    @Test
    public void testReplaceStrInInstanceOfAndCast() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLBoxingTest_testReplaceStrInInstanceOfAndCast"));
    }
}