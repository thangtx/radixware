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


public class JMLPropertyTest {

    @Test 
    public void testPropertyBoolAndAnd2() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("A", "JMLPropertyTest_testPropertyBoolAndAnd2"));
    }

    @Test 
    public void testPropertyInArrayInitializer() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInArrayInitializer"));
    }

    @Test 
    public void testTwiceSimpleCast() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testTwiceSimpleCast"));
    }

    @Test 
    public void testPropertyBoolAndAnd() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("A", "JMLPropertyTest_testPropertyBoolAndAnd"));
    }

    @Test 
    public void testPropertyOfMethodResult() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("C", "JMLPropertyTest_testPropertyOfMethodResult"));
    }

    @Test 
    public void testStaticSetterWithNum() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testStaticSetterWithNum"));
    }

    @Test 
    public void testPropertyArrayGetAccess() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyArrayGetAccess"));
    }

    @Test 
    public void testStaticProperty() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("B", "JMLPropertyTest_testStaticProperty"));
    }

    @Test 
    public void testPropertyWithinCondition() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyWithinCondition"));
    }

    @Test 
    public void testSetterWithTypeCast() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterWithTypeCast"));
    }

    @Test 
    public void testMethodsInGenericBase() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testMethodsInGenericBase"));
    }

    @Test 
    public void testPropertyNegateSimpleType() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyNegateSimpleType"));
    }

    @Test 
    public void testStaticPropertyLongQualified() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("B", "JMLPropertyTest_testStaticPropertyLongQualified"));
    }

    @Test 
    public void testPropertyNegateBigDecimal() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyNegateBigDecimal"));
    }

    @Test 
    public void testFieldInGenericBase() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testFieldInGenericBase"));
    }

    @Test 
    public void testBoxingInSwitch() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testBoxingInSwitch"));
    }

    @Test 
    public void testInnerInGenericBase() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testInnerInGenericBase"));
    }

    @Test 
    public void testPropertyInThrow() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInThrow"));
    }

    @Test 
    public void testPropertyArraySetAccess() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyArraySetAccess"));
    }

    @Test 
    public void testPropertyInGenericBase() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInGenericBase"));
    }

    @Test 
    public void testPropertyIndirectBoxing() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyIndirectBoxing"));
    }

    @Test 
    public void testStaticPropertyQualified() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("B", "JMLPropertyTest_testStaticPropertyQualified"));
    }

    @Test 
    public void testCompareProperty() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("B", "JMLPropertyTest_testCompareProperty"));
    }

    @Test 
    public void testPropertyAnonumousClassAssign() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyAnonumousClassAssign"));
    }

    @Test 
    public void testPropertySet_int() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertySet_int"));
    }

    @Test 
    public void testPropertyInForAction() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInForAction"));
    }

    @Test 
    public void testPropertyOnLongQualifiedName() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyOnLongQualifiedName"));
    }

    @Test 
    public void testPropertyInForIncrement() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInForIncrement"));
    }

    @Test 
    public void testGetterPropertyInvalidType() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testGetterPropertyInvalidType"));
    }

    @Test 
    public void testPropertyEquals() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyEquals"));
    }

    @Test 
    public void testPropertyCompare() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyCompare"));
    }

    @Test 
    public void testGetterPropertyInSwitch() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyInSwitch"));
    }

    @Test 
    public void testSetterFieldOverrideProperty() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterFieldOverrideProperty"));
    }

    @Test 
    public void testPropertyEqualsUnknownTypes() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyEqualsUnknownTypes"));
    }

    @Test 
    public void testPropertyInAnd() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInAnd"));
    }

    @Test 
    public void testPropertyInInterface() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInInterface"));
    }

    @Test 
    public void testSetterPropertyComplexAssign() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyComplexAssign"));
    }

    @Test 
    public void testPropertyInForCondition() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInForCondition"));
    }

    @Test 
    public void testSelfReference() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSelfReference"));
    }

    @Test 
    public void testGetterPropertyOnAssign() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyOnAssign"));
    }

    @Test 
    public void testPropertyPostfixOperator() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyPostfixOperator"));
    }

    @Test 
    public void testPropertyPrefixOperator() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyPrefixOperator"));
    }

    @Test 
    public void testGetterPropertyInIfCondition() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyInIfCondition"));
    }

    @Test 
    public void testSetterBeforeGetter() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterBeforeGetter"));
    }

    @Test 
    public void testSetterPropertyOnAssign() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyOnAssign"));
    }

    @Test 
    public void testPropertyPrivateAccess() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testPropertyPrivateAccess"));
    }

    @Test 
    public void testGetterPropertyAsExplicitThis() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyAsExplicitThis"));
    }

    @Test 
    public void testPropertyAddAssign() {
        assertEquals("Compilation falied", 2, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testPropertyAddAssign"));
    }

    @Test 
    public void testGetterPropertyAsImplicitThis() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyAsImplicitThis"));
    }

    @Test 
    public void testSetterPropertyNoSetter() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testSetterPropertyNoSetter"));
    }

    @Test 
    public void testPropertyInForInit() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInForInit"));
    }

    @Test 
    public void testPropertyAddTimestamp() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyAddTimestamp"));
    }

    @Test 
    public void testGetterPropertyOnFunctionCall() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyOnFunctionCall"));
    }

    @Test 
    public void testSetterPropertyInvalidType() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testSetterPropertyInvalidType"));
    }

    @Test 
    public void testPropertyInTry() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInTry"));
    }

    @Test 
    public void testPropertyPublicAccess() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyPublicAccess"));
    }

    @Test 
    public void testPropertyInConstructor() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInConstructor"));
    }

    @Test 
    public void testPropertyOnQualifiedName() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyOnQualifiedName"));
    }

    @Test 
    public void testGetterPropertyOnInit() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyOnInit"));
    }

    @Test 
    public void testPropertyCompare1() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyCompare1"));
    }

    @Test 
    public void testSetterPropertyNoSetterWithExplicitParent() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyNoSetterWithExplicitParent"));
    }

    @Test 
    public void testPropertyOnLongQualifiedSetterName() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyOnLongQualifiedSetterName"));
    }

    @Test 
    public void testSetterExplicitParentFieldOverridesProperty() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterExplicitParentFieldOverridesProperty"));
    }

//    @Test 
//    public void testGetterPropertyOnLocalDeclaration1() {
//        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyOnLocalDeclaration1"));
//    }

    @Test 
    public void testPropertyOnLongQualifiedSetterFieldName() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyOnLongQualifiedSetterFieldName"));
    }

    @Test 
    public void testPropertyIndirectBoxingImplicitThis() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyIndirectBoxingImplicitThis"));
    }

    @Test 
    public void testPropertyInNeighbourConstructorCall() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInNeighbourConstructorCall"));
    }

    @Test 
    public void testGetterPropertyEqualLenWithOtherFunction() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyEqualLenWithOtherFunction"));
    }

    @Test 
    public void testPropertyPrefixOperatorReturnType() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testPropertyPrefixOperatorReturnType"));
    }

    @Test 
    public void testSetterPropertyNoSetterWithParent() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyNoSetterWithParent"));
    }

    @Test 
    public void testBoolPropertyJavaBoxingImplicitThis() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testBoolPropertyJavaBoxingImplicitThis"));
    }

    @Test 
    public void testGetterPropertyOnLocalDeclaration() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyOnLocalDeclaration"));
    }

    @Test 
    public void testStaticPropertySetterQualified() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("B", "JMLPropertyTest_testStaticPropertySetterQualified"));
    }

    @Test 
    public void testLongPropertyExtendedBoxingImplicitThis() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testLongPropertyExtendedBoxingImplicitThis"));
    }

    @Test 
    public void testSetterPropertyComplexAssignBigDecimal() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyComplexAssignBigDecimal"));
    }

    @Test 
    public void testPropertyOnLongQualifiedNameInside() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyOnLongQualifiedNameInside"));
    }

    @Test 
    public void testSetterPropertyOnAssignToImplicitThis() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyOnAssignToImplicitThis"));
    }

    @Test 
    public void testSetterPropertyOverrideParentField() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyOverrideParentField"));
    }

    @Test 
    public void testPropertyOnLongQualifiedSetterRHS() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyOnLongQualifiedSetterRHS"));
    }

    @Test 
    public void testPropertyOnLongQualifiedSetterPropRHS() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyOnLongQualifiedSetterPropRHS"));
    }

    @Test 
    public void testGetterPropertyInWhileCondition() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testGetterPropertyInWhileCondition"));
    }

    @Test 
    public void testStaticPropertySetterLongQualified() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("B", "JMLPropertyTest_testStaticPropertySetterLongQualified"));
    }

    @Test 
    public void testSetterPropertyOnAssignToExplicitThis() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyOnAssignToExplicitThis"));
    }

    @Test 
    public void testSimpleCast() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSimpleCast"));
    }

    @Test 
    public void testStaticSetter() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testStaticSetter"));
    }

    @Test 
    public void testStaticFields() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testStaticFields"));
    }

    @Test 
    public void testPropertyInOr() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInOr"));
    }

    @Test 
    public void testStaticGetter() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testStaticGetter"));
    }

    @Test 
    public void testForeach() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testForeach"));
    }

    @Test 
    public void testInner() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testInner"));
    }

    @Test 
    public void testPropertyInIf() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testPropertyInIf"));
    }

    @Test 
    public void testStrangeBug() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testStrangeBug"));
    }

    @Test 
    public void testInnerClass() {
        assertEquals("Compilation falied", 1, TextCompilerTest.compileErrorneousCode("Test", "JMLPropertyTest_testInnerClass"));
    }

    @Test 
    public void testSetterPropertyNoSetterWithExplicitParentAndDifferentType() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyNoSetterWithExplicitParentAndDifferentType"));
    }

    @Test 
    public void testSetterPropertyNoSetterWithParentAndDifferentType() {
        assertTrue("Compilation falied", TextCompilerTest.compileCode("Test", "JMLPropertyTest_testSetterPropertyNoSetterWithParentAndDifferentType"));
    }
}