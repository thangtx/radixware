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
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.radixware.kernel.common.compiler.TextCompilerTest;


public class JMLBigDecimalTest {

    @Test
    public void testBigDecimalConstantRef() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalConstantRef"));
    }

    @Test
    public void testBigDecimalCompoundIncrement() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalCompoundIncrement"));
    }

    @Test
    public void testBigDecimalCompoundDivide() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalCompoundDivide"));
    }

    @Test
    public void testBigDecimalDivide() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalDivide"));
    }

    @Test
    public void testBigDecimalNegate() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalNegate"));
    }

    @Test
    public void testBigDecimalComplexExpression() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalComplexExpression"));
    }

    @Test
    public void testBigDecimalEqBigDecimal() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalEqBigDecimal"));
    }

    @Test
    public void testBigDecimalZeroCompare() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalZeroCompare"));
    }

    @Test
    public void testBigDecimalWithDifferentTypes() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalWithDifferentTypes"));
    }

    @Test
    public void testBigDecimalGeqBigDecimal() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalGeqBigDecimal"));
    }

    @Test
    public void testBigDecimalMultiplication() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalMultiplication"));
    }

    @Test
    public void testBigDecimalAddition() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testBigDecimalAddition"));
    }

    @Test
    public void testCastToNum() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testCastToNum"));
    }

    @Test
    public void testPassNegativeIntegerToNumArgument() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testPassNegativeIntegerToNumArgument"));
    }

    @Test
    public void testAssignNegativeNumVariableToFinalNumVariable() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test", "JMLBigDecimalTest_testAssignNegativeNumVariableToFinalNumVariable"));
    }
}