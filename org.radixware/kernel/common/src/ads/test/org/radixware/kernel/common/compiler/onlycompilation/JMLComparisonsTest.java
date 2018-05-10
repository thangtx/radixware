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


public class JMLComparisonsTest {

    @Test @Ignore
    public void testIfWithHeapBoolean() {
        assertEquals("Compilation failed", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testIfWithHeapBoolean"));
    }

    @Test @Ignore
    public void testInvalidLessBooleans() {
        assertEquals("Compilation must fail", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testInvalidLessBooleans"));
    }

    @Test @Ignore
    public void testCompareLongs() {
        Long a = 2l, b = 3l;
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareLongs"));
    }

    @Test @Ignore
    public void testCompareLongAndInt() {
        Long a = 2l, b = 3l;
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareLongAndInt"));
    }

    @Test @Ignore
    public void testCompareBool() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareBool"));
    }

    @Test @Ignore
    public void testCompareNums() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareNums"));
    }

    @Test @Ignore
    public void testCompoundWithHeapBoolean() {
        assertEquals("Compilation must fail", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testCompoundWithHeapBoolean"));
    }

    @Test @Ignore
    public void testCompareStringAndNull() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareStringAndNull"));
    }

    @Test @Ignore
    public void testCompoundWithBaseBoolean() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompoundWithBaseBoolean"));
    }

    @Test @Ignore
    public void testCompareUnknownTypes() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareUnknownTypes"));
    }

    @Test @Ignore
    public void testKnownAndUnknown() {
        assertEquals("Compilation must fail", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testKnownAndUnknown"));
    }

    @Test @Ignore
    public void testCompareWithBase() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareWithBase"));
    }

    @Test @Ignore
    public void testInvalidLessCompare() {
        assertFalse("Compilation must fail", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testInvalidLessCompare"));
    }

    @Test @Ignore
    public void testEqualBaseBooleans() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testEqualBaseBooleans"));
    }

    @Test @Ignore
    public void testCompareBigDecimalAndInt() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareBigDecimalAndInt"));
    }

    @Test @Ignore
    public void testInvalidCompare() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testInvalidCompare"));
    }

    @Test @Ignore
    public void testEqualHeapAndBaseBooleans() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testEqualHeapAndBaseBooleans"));
    }

    @Test @Ignore
    public void testIntegerAndLongAdd() {
        assertFalse("Compilation nust fail", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testIntegerAndLongAdd"));
    }

    @Test @Ignore
    public void testStringDivision() {
        assertFalse("Compilation must fail", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testStringDivision"));
    }

    @Test @Ignore
    public void testIntegerAndLongAdd1() {
        assertFalse("Compilation nust fail", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testIntegerAndLongAdd1"));
    }

    @Test @Ignore
    public void testCompareBigDecimalAndDouble() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareBigDecimalAndDouble"));
    }

    @Test @Ignore
    public void testCompareBigDecimalAndInteger() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareBigDecimalAndInteger"));
    }

    @Test @Ignore
    public void testCompareStrings() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareStrings"));
    }

    @Test @Ignore
    public void testCompareBoolAndInt() {
        assertEquals("Compilation failed", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testCompareBoolAndInt"));
    }

    @Test @Ignore
    public void testCompareBigDecimals() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareBigDecimals"));
    }

    @Test @Ignore
    public void testCompareIntegerAndLong() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareIntegerAndLong"));
    }

    @Test @Ignore
    public void testCompareIntegersLess() {
        assertEquals("Compilation failed", 2, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testCompareIntegersLess"));
    }

    @Test @Ignore
    public void testCompareIntegerAndLongLess() {
        assertEquals("Compilation failed", 2, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testCompareIntegerAndLongLess"));
    }

    @Test @Ignore
    public void testCompareIntegers() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareIntegers"));
    }

    @Test @Ignore
    public void testCompareIntegersAndCharacter() {
        assertEquals("Compilation failed", 2, TextCompilerTest.compileErrorneousCode("Test", "JMLComparisonsTest_testCompareIntegersAndCharacter"));
    }

    @Test @Ignore
    public void testCompareIntandint() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareIntandint"));
    }

    @Test @Ignore
    public void testBoolBooleanAnd() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testBoolBooleanAnd"));
    }

    @Test @Ignore
    public void testFloraTypesInReturnStatement() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testFloraTypesInReturnStatement"));
    }
    @Test 
    public void testCompareDateTime() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLComparisonsTest_testCompareDateTimes"));
    }
    
}