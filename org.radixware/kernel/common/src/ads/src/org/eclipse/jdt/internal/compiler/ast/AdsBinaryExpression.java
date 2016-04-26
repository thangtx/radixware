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

package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_JavaLangString;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_boolean;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_byte;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_char;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_double;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_float;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_int;
import static org.eclipse.jdt.internal.compiler.lookup.TypeIds.T_long;


public class AdsBinaryExpression extends BinaryExpression {

    private Expression substutution = null;

    public AdsBinaryExpression(BinaryExpression expression) {
        super(expression);
    }

    public Expression getSubstitution() {
        if (substutution == null) {
            return this;
        } else {
            return substutution;
        }
    }

    private boolean computeSubstitution(int operator, BlockScope scope) {
        char[] substututionSelector;

        switch (operator) {
            case OperatorIds.PLUS:
                substututionSelector = "add".toCharArray();
                break;
            case OperatorIds.MINUS:
                substututionSelector = "subtract".toCharArray();
                break;
            case OperatorIds.DIVIDE:
                substututionSelector = "divide".toCharArray();
                break;
            case OperatorIds.MULTIPLY:
                substututionSelector = "multiply".toCharArray();
                break;
            case OperatorIds.GREATER:
            case OperatorIds.GREATER_EQUAL:
            case OperatorIds.LESS:
            case OperatorIds.LESS_EQUAL:
                substututionSelector = "compare".toCharArray();
                break;
            default:
                return false;
        }
        final MessageSend ms = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), substututionSelector);
        ms.arguments = new Expression[]{
            this.left, this.right
        };
        //compute expression type
        switch (operator) {
            case OperatorIds.PLUS:
            case OperatorIds.MINUS:
            case OperatorIds.DIVIDE:
            case OperatorIds.MULTIPLY:
                this.resolvedType = getBigDecimal(scope);
                this.substutution = ms;
                break;
            case OperatorIds.GREATER:
            case OperatorIds.GREATER_EQUAL:
            case OperatorIds.LESS:
            case OperatorIds.LESS_EQUAL:
                substutution = new BinaryExpression(ms, BaseGenerator.createIntConstant(0), operator);
                this.resolvedType = BaseTypeBinding.BOOLEAN;
                break;
            default:
                break;
        }
        return true;
    }

    private TypeBinding getBigDecimal(BlockScope scope) {
        CompilationUnitScope unitScope = scope.compilationUnitScope();
        return unitScope.environment.getResolvedType(BaseGenerator.JAVAMATHBIGDECIMAL_TYPE_NAME, scope);
    }

    public TypeBinding resolveType(BlockScope scope) {
        // keep implementation in sync with CombinedBinaryExpression#resolveType
        // and nonRecursiveResolveTypeUpwards
        boolean leftIsCast, rightIsCast;
        if ((leftIsCast = this.left instanceof CastExpression) == true) {
            this.left.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on
        }
        TypeBinding leftType = this.left.resolveType(scope);

        if ((rightIsCast = this.right instanceof CastExpression) == true) {
            this.right.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on
        }
        TypeBinding rightType = this.right.resolveType(scope);

        // use the id of the type to navigate into the table
        if (leftType == null || rightType == null) {
            this.constant = Constant.NotAConstant;
            return null;
        }

        int leftTypeID = leftType.id;
        int rightTypeID = rightType.id;

        // autoboxing support
        final boolean use15specifics = scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5;
        if (use15specifics) {
            if (!leftType.isBaseType() && rightTypeID != TypeIds.T_JavaLangString && rightTypeID != TypeIds.T_null) {
                leftTypeID = scope.environment().computeBoxingType(leftType).id;
            }
            if (!rightType.isBaseType() && leftTypeID != TypeIds.T_JavaLangString && leftTypeID != TypeIds.T_null) {
                rightTypeID = scope.environment().computeBoxingType(rightType).id;
            }
        }

        if ((leftTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal && rightTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal)
                || (leftTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal && rightTypeID <= 15)
                || (leftTypeID <= 15 && rightTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal)) {
            final int operator = (this.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
            this.constant = Constant.NotAConstant;
            if (!computeSubstitution(operator, scope)) {
                scope.problemReporter().invalidOperator(this, leftType, rightType);
                return null;
            }
        } else {
            if (leftTypeID > 15
                    || rightTypeID > 15) { // must convert String + Object || Object + String
                if (leftTypeID == TypeIds.T_JavaLangString) {
                    rightTypeID = TypeIds.T_JavaLangObject;
                } else if (rightTypeID == TypeIds.T_JavaLangString) {
                    leftTypeID = TypeIds.T_JavaLangObject;
                } else {
                    this.constant = Constant.NotAConstant;
                    scope.problemReporter().invalidOperator(this, leftType, rightType);
                    return null;
                }
            }
            if (((this.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT) == OperatorIds.PLUS) {
                if (leftTypeID == TypeIds.T_JavaLangString) {
                    this.left.computeConversion(scope, leftType, leftType);
                    if (rightType.isArrayType() && ((ArrayBinding) rightType).elementsType() == TypeBinding.CHAR) {
                        scope.problemReporter().signalNoImplicitStringConversionForCharArrayExpression(this.right);
                    }
                }
                if (rightTypeID == TypeIds.T_JavaLangString) {
                    this.right.computeConversion(scope, rightType, rightType);
                    if (leftType.isArrayType() && ((ArrayBinding) leftType).elementsType() == TypeBinding.CHAR) {
                        scope.problemReporter().signalNoImplicitStringConversionForCharArrayExpression(this.left);
                    }
                }
            }

            // the code is an int
            // (cast)  left   Op (cast)  right --> result
            //  0000   0000       0000   0000      0000
            //  <<16   <<12       <<8    <<4       <<0

            // Don't test for result = 0. If it is zero, some more work is done.
            // On the one hand when it is not zero (correct code) we avoid doing the test
            int operator = (this.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
            int operatorSignature = OperatorExpression.OperatorSignatures[operator][(leftTypeID << 4) + rightTypeID];

            this.left.computeConversion(scope, TypeBinding.wellKnownType(scope, (operatorSignature >>> 16) & 0x0000F), leftType);
            this.right.computeConversion(scope, TypeBinding.wellKnownType(scope, (operatorSignature >>> 8) & 0x0000F), rightType);
            this.bits |= operatorSignature & 0xF;
            switch (operatorSignature & 0xF) { // record the current ReturnTypeID
                // only switch on possible result type.....
                case T_boolean:
                    this.resolvedType = TypeBinding.BOOLEAN;
                    break;
                case T_byte:
                    this.resolvedType = TypeBinding.BYTE;
                    break;
                case T_char:
                    this.resolvedType = TypeBinding.CHAR;
                    break;
                case T_double:
                    this.resolvedType = TypeBinding.DOUBLE;
                    break;
                case T_float:
                    this.resolvedType = TypeBinding.FLOAT;
                    break;
                case T_int:
                    this.resolvedType = TypeBinding.INT;
                    break;
                case T_long:
                    this.resolvedType = TypeBinding.LONG;
                    break;
                case T_JavaLangString:
                    this.resolvedType = scope.getJavaLangString();
                    break;
                default: //error........
                    this.constant = Constant.NotAConstant;
                    scope.problemReporter().invalidOperator(this, leftType, rightType);
                    return null;
            }

            // check need for operand cast
            if (leftIsCast || rightIsCast) {
                CastExpression.checkNeedForArgumentCasts(scope, operator, operatorSignature, this.left, leftTypeID, leftIsCast, this.right, rightTypeID, rightIsCast);
            }
            // compute the constant when valid
            computeConstant(scope, leftTypeID, rightTypeID);
        }
        return this.resolvedType;
    }
    /*
     public TypeBinding resolveType(BlockScope scope) {
     // keep implementation in sync with CombinedBinaryExpression#resolveType
     // and nonRecursiveResolveTypeUpwards
     boolean leftIsCast, rightIsCast;
     if ((leftIsCast = this.left instanceof CastExpression) == true) {
     this.left.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on
     }
     TypeBinding leftType = this.left.resolveType(scope);

     if ((rightIsCast = this.right instanceof CastExpression) == true) {
     this.right.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on
     }
     TypeBinding rightType = this.right.resolveType(scope);

     // use the id of the type to navigate into the table
     if (leftType == null || rightType == null) {
     this.constant = Constant.NotAConstant;
     return null;
     }

     int leftTypeID = leftType.id;
     int rightTypeID = rightType.id;

     // autoboxing support
     boolean use15specifics = scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5;
     if (use15specifics) {
     if (!leftType.isBaseType() && rightTypeID != TypeIds.T_JavaLangString && rightTypeID != TypeIds.T_null) {
     leftTypeID = scope.environment().computeBoxingType(leftType).id;
     }
     if (!rightType.isBaseType() && leftTypeID != TypeIds.T_JavaLangString && leftTypeID != TypeIds.T_null) {
     rightTypeID = scope.environment().computeBoxingType(rightType).id;
     }
     }
     if (leftTypeID > 15
     || rightTypeID > 15) { // must convert String + Object || Object + String
     if (leftTypeID == TypeIds.T_JavaLangString) {
     rightTypeID = TypeIds.T_JavaLangObject;
     } else if (rightTypeID == TypeIds.T_JavaLangString) {
     leftTypeID = TypeIds.T_JavaLangObject;
     } else {
     this.constant = Constant.NotAConstant;
     scope.problemReporter().invalidOperator(this, leftType, rightType);
     return null;
     }
     }
     if (((this.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT) == OperatorIds.PLUS) {
     if (leftTypeID == TypeIds.T_JavaLangString) {
     this.left.computeConversion(scope, leftType, leftType);
     if (rightType.isArrayType() && ((ArrayBinding) rightType).elementsType() == TypeBinding.CHAR) {
     scope.problemReporter().signalNoImplicitStringConversionForCharArrayExpression(this.right);
     }
     }
     if (rightTypeID == TypeIds.T_JavaLangString) {
     this.right.computeConversion(scope, rightType, rightType);
     if (leftType.isArrayType() && ((ArrayBinding) leftType).elementsType() == TypeBinding.CHAR) {
     scope.problemReporter().signalNoImplicitStringConversionForCharArrayExpression(this.left);
     }
     }
     }

     // the code is an int
     // (cast)  left   Op (cast)  right --> result
     //  0000   0000       0000   0000      0000
     //  <<16   <<12       <<8    <<4       <<0

     // Don't test for result = 0. If it is zero, some more work is done.
     // On the one hand when it is not zero (correct code) we avoid doing the test
     int operator = (this.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;

     try {
     int a = OperatorExpression.OperatorSignatures[operator][(leftTypeID << 4) + rightTypeID];
     } catch (Exception e) {
     e.printStackTrace();
     }
     int operatorSignature = OperatorExpression.OperatorSignatures[operator][(leftTypeID << 4) + rightTypeID];

     this.left.computeConversion(scope, TypeBinding.wellKnownType(scope, (operatorSignature >>> 16) & 0x0000F), leftType);
     this.right.computeConversion(scope, TypeBinding.wellKnownType(scope, (operatorSignature >>> 8) & 0x0000F), rightType);
     this.bits |= operatorSignature & 0xF;

     switch (operatorSignature & 0xF) { // record the current ReturnTypeID
     // only switch on possible result type.....
     case T_boolean:
     this.resolvedType = TypeBinding.BOOLEAN;
     break;
     case T_byte:
     this.resolvedType = TypeBinding.BYTE;
     break;
     case T_char:
     this.resolvedType = TypeBinding.CHAR;
     break;
     case T_double:
     this.resolvedType = TypeBinding.DOUBLE;
     break;
     case T_float:
     this.resolvedType = TypeBinding.FLOAT;
     break;
     case T_int:
     this.resolvedType = TypeBinding.INT;
     break;
     case T_long:
     this.resolvedType = TypeBinding.LONG;
     break;
     case T_JavaLangString:
     this.resolvedType = scope.getJavaLangString();
     break;
     default: //error........
     this.constant = Constant.NotAConstant;
     scope.problemReporter().invalidOperator(this, leftType, rightType);
     return null;
     }

     // check need for operand cast
     if (leftIsCast || rightIsCast) {
     CastExpression.checkNeedForArgumentCasts(scope, operator, operatorSignature, this.left, leftTypeID, leftIsCast, this.right, rightTypeID, rightIsCast);
     }
     // compute the constant when valid
     computeConstant(scope, leftTypeID, rightTypeID);
     return this.resolvedType;
     }*/
}
