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
package org.radixware.kernel.common.compiler.core.ast;

import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding;
import static org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal;
import static org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding.TypeId_JavaSqlTimestamp;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

public class JMLMathOperations {

    private static boolean isMathOperNeeded(int leftTypeID, int rightTypeID, int operator) {
        switch (operator) {
            case OperatorIds.PLUS:
            case OperatorIds.MINUS:
            case OperatorIds.DIVIDE:
            case OperatorIds.MULTIPLY:
            case OperatorIds.GREATER:
            case OperatorIds.GREATER_EQUAL:
            case OperatorIds.LESS:
            case OperatorIds.LESS_EQUAL:
                if (leftTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal) {
                    return rightTypeID <= 15 || (rightTypeID >= TypeIds.T_JavaLangByte && rightTypeID <= TypeIds.T_JavaLangDouble) || rightTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal;
                }
                if (rightTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal) {
                    return leftTypeID <= 15 || (leftTypeID >= TypeIds.T_JavaLangByte && leftTypeID <= TypeIds.T_JavaLangDouble) || leftTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal;
                }

                return false;
            case OperatorIds.NOT_EQUAL:
            case OperatorIds.EQUAL_EQUAL:
                return shouldUseEqualsMathOper(leftTypeID, rightTypeID);
            case OperatorIds.AND_AND:
                return leftTypeID == TypeIds.T_JavaLangBoolean && (rightTypeID == TypeIds.T_JavaLangBoolean || rightTypeID == TypeIds.T_boolean)
                        || rightTypeID == TypeIds.T_JavaLangBoolean && (leftTypeID == TypeIds.T_JavaLangBoolean || leftTypeID == TypeIds.T_boolean);
            default:
                return false;
        }
    }

    public static Expression computeExpression(BlockScope scope, Expression left, Expression right, int operator) {
        if (left instanceof NullLiteral || right instanceof NullLiteral) {
            return null;
        }
        final TypeBinding leftType = left.resolveType(scope);
        final TypeBinding rightType = right.resolveType(scope);

        if (leftType == null || rightType == null) {
            return null;
        }

        final int leftTypeID = leftType.id;
        final int rightTypeID = rightType.id;

        if ((leftTypeID == TypeIds.T_JavaLangString || rightTypeID == TypeIds.T_JavaLangString) && !(operator == OperatorIds.EQUAL_EQUAL || operator == OperatorIds.NOT_EQUAL)) {
            return null;
        }
        if (isMathOperNeeded(leftTypeID, rightTypeID, operator)) {
            char[] substututionSelector;
            Expression substitution = null;

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
                case OperatorIds.NOT_EQUAL:
                case OperatorIds.EQUAL_EQUAL:
                    substututionSelector = "equals".toCharArray();
                    break;
                case OperatorIds.AND_AND:
                    substututionSelector = "and".toCharArray();
                    break;
                default:
                    return null;
            }
            final MessageSend ms = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), substututionSelector);
            ms.arguments = new Expression[]{
                left,
                right
            };
            //compute expression type
            switch (operator) {
                case OperatorIds.PLUS:
                case OperatorIds.MINUS:
                case OperatorIds.DIVIDE:
                case OperatorIds.MULTIPLY:
                case OperatorIds.AND_AND:
                    substitution = ms;
                    break;
                case OperatorIds.GREATER:
                case OperatorIds.GREATER_EQUAL:
                case OperatorIds.LESS:
                case OperatorIds.LESS_EQUAL:
                    substitution = new BinaryExpression(ms, BaseGenerator.createIntConstant(0), operator);
                    break;
                case OperatorIds.EQUAL_EQUAL:
                    substitution = ms;
                    break;
                case OperatorIds.NOT_EQUAL:
                    substitution = new UnaryExpression(ms, OperatorIds.NOT);
                    break;
                default:
                    break;
            }
            return substitution;
        } else {
            return null;
        }
    }

    public static Expression computeExpression(BlockScope scope, Expression expression, int operator) {
        final TypeBinding type = expression.resolveType(scope);

        if (type == null) {
            return null;
        }
        final int leftTypeID = type.id;

        if (leftTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal) {

            char[] substututionSelector;
            Expression substitution = null;

            switch (operator) {
                case OperatorIds.MINUS:
                    substututionSelector = "negate".toCharArray();
                    break;
                default:
                    return null;
            }
            final MessageSend ms = BaseGenerator.createMessageSend(expression, substututionSelector);

            //compute expression type
            switch (operator) {
                case OperatorIds.MINUS:
                    substitution = ms;
                    break;
                default:
                    break;
            }
            return substitution;
        } else {
            return null;
        }
    }

    public static Expression computeConversionToReferenceNumeric(TypeBinding targetType, Expression expression, BlockScope scope) {
        if (expression instanceof NullLiteral) {
            return expression;
        }
        if (targetType != null) {
            if (expression != null) {
                expression.setExpectedType(targetType);
            }
            final TypeBinding expressionType = expression == null ? null : expression.resolveType(scope);
            if (expressionType != null) {
                MessageSend converter = null;
                switch (targetType.id) {
                    case AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_JavaLangLong:
                            case TypeIds.T_JavaLangFloat:
                            case TypeIds.T_JavaLangDouble:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                            case TypeIds.T_long:
                            case TypeIds.T_float:
                            case TypeIds.T_double:
                                converter = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), "extendedBoxToNum".toCharArray());
                                break;
                        }
                        break;
                    case TypeIds.T_JavaLangInteger:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                                converter = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), "extendedBoxToInteger".toCharArray());
                                break;
                        }
                        break;
                    case TypeIds.T_JavaLangLong:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                                converter = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), "extendedBoxToLong".toCharArray());
                                break;
                        }
                        break;
                    case TypeIds.T_JavaLangFloat:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_JavaLangLong:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                            case TypeIds.T_long:
                                converter = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), "extendedBoxToFloat".toCharArray());
                                break;
                        }
                        break;
                    case TypeIds.T_JavaLangDouble:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_JavaLangLong:
                            case TypeIds.T_JavaLangFloat:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                            case TypeIds.T_long:
                            case TypeIds.T_float:
                                converter = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), "extendedBoxToDouble".toCharArray());
                                break;
                        }
                        break;

                }
                if (converter != null) {
                    converter.arguments = new Expression[]{expression};
                    expression.implicitConversion = 0;
                    converter.sourceStart = expression.sourceStart;
                    converter.sourceEnd = expression.sourceEnd;
                    converter.resolveType(scope);
                    return converter;
                }
            }
        }
        return expression;
    }

    public static TypeBinding computeConversionToReferenceNumeric(TypeBinding targetType, TypeBinding expressionType, BlockScope scope) {

        if (targetType != null) {
            if (expressionType != null) {
                MessageSend converter = null;
                switch (targetType.id) {
                    case AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_JavaLangLong:
                            case TypeIds.T_JavaLangFloat:
                            case TypeIds.T_JavaLangDouble:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                            case TypeIds.T_long:
                            case TypeIds.T_float:
                            case TypeIds.T_double:
                                return scope.environment().getType(BaseGenerator.JAVAMATHBIGDECIMAL_TYPE_NAME);
                        }
                        break;
                    case TypeIds.T_JavaLangInteger:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                                return scope.environment().getType(TypeConstants.JAVA_LANG_INTEGER);
                        }
                        break;
                    case TypeIds.T_JavaLangLong:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                                return scope.environment().getType(TypeConstants.JAVA_LANG_LONG);
                        }
                        break;
                    case TypeIds.T_JavaLangFloat:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_JavaLangLong:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                            case TypeIds.T_long:
                                return scope.environment().getType(TypeConstants.JAVA_LANG_FLOAT);
                        }
                        break;
                    case TypeIds.T_JavaLangDouble:
                        switch (expressionType.id) {
                            case TypeIds.T_JavaLangByte:
                            case TypeIds.T_JavaLangShort:
                            case TypeIds.T_JavaLangInteger:
                            case TypeIds.T_JavaLangLong:
                            case TypeIds.T_JavaLangFloat:
                            case TypeIds.T_byte:
                            case TypeIds.T_short:
                            case TypeIds.T_int:
                            case TypeIds.T_long:
                            case TypeIds.T_float:
                                return scope.environment().getType(TypeConstants.JAVA_LANG_DOUBLE);
                        }
                }
            }
        }
        return null;
    }

    private static boolean shouldUseEqualsMathOper(int leftTypeId, int rightTypeId) {

        if (leftTypeId == TypeIds.T_JavaLangBoolean && (rightTypeId == TypeIds.T_JavaLangBoolean || rightTypeId == TypeIds.T_boolean)) {
            return true;
        }
        if (rightTypeId == TypeIds.T_JavaLangBoolean && (leftTypeId == TypeIds.T_JavaLangBoolean || leftTypeId == TypeIds.T_boolean)) {
            return true;
        }

        if (leftTypeId == TypeIds.T_JavaLangString && rightTypeId == TypeIds.T_JavaLangString) {
            return true;
        }
        if (leftTypeId == TypeId_JavaSqlTimestamp && rightTypeId == TypeId_JavaSqlTimestamp) {
            return true;
        }

        boolean leftIsObjectNumeric = false;
        boolean leftIsNumeric = false;
        switch (leftTypeId) {
            case TypeIds.T_JavaLangByte:
            case TypeIds.T_JavaLangCharacter:
            case TypeIds.T_JavaLangShort:
            case TypeIds.T_JavaLangInteger:
            case TypeIds.T_JavaLangLong:
            case TypeIds.T_JavaLangFloat:
            case TypeIds.T_JavaLangDouble:
            case TypeId_JavaMathBigDecimal:
                leftIsObjectNumeric = true;
                break;
            case TypeIds.T_byte:
            case TypeIds.T_char:
            case TypeIds.T_short:
            case TypeIds.T_int:
            case TypeIds.T_long:
            case TypeIds.T_float:
            case TypeIds.T_double:
                leftIsNumeric = true;
                break;
        }
        boolean rightIsObjectNumeric = false;
        boolean rightIsNumeric = false;
        switch (rightTypeId) {
            case TypeIds.T_JavaLangByte:
            case TypeIds.T_JavaLangCharacter:
            case TypeIds.T_JavaLangShort:
            case TypeIds.T_JavaLangInteger:
            case TypeIds.T_JavaLangLong:
            case TypeIds.T_JavaLangFloat:
            case TypeIds.T_JavaLangDouble:
            case TypeId_JavaMathBigDecimal:
                rightIsObjectNumeric = true;
                break;
            case TypeIds.T_byte:
            case TypeIds.T_char:
            case TypeIds.T_short:
            case TypeIds.T_int:
            case TypeIds.T_long:
            case TypeIds.T_float:
            case TypeIds.T_double:
                rightIsNumeric = true;
                break;
        }

        return (leftIsObjectNumeric && rightIsObjectNumeric) || (leftIsNumeric && rightIsObjectNumeric) || (leftIsObjectNumeric && rightIsNumeric);
    }
}
