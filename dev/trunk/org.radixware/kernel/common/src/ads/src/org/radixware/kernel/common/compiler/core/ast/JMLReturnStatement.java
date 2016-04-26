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

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLReturnStatement extends ReturnStatement {

    public JMLReturnStatement(ReturnStatement src) {
        super(src.expression, src.sourceStart, src.sourceEnd);
    }

    public JMLReturnStatement(Expression expression) {
        super(expression, 0, 0);
    }

    @Override
    public void resolve(BlockScope scope) {
        MethodScope methodScope = scope.methodScope();
        MethodBinding methodBinding;
        TypeBinding methodType =
                (methodScope.referenceContext instanceof AbstractMethodDeclaration)
                ? ((methodBinding = ((AbstractMethodDeclaration) methodScope.referenceContext).binding) == null
                ? null
                : methodBinding.returnType)
                : TypeBinding.VOID;
        TypeBinding expressionType;
        if (methodType == TypeBinding.VOID) {
            // the expression should be null
            if (this.expression == null) {
                return;
            }
            if ((expressionType = this.expression.resolveType(scope)) != null) {
                scope.problemReporter().attemptToReturnNonVoidExpression(this, expressionType);
            }
            return;
        }
        if (this.expression == null) {
            if (methodType != null) {
                scope.problemReporter().shouldReturn(methodType, this);
            }
            return;
        }

        if (this.expression instanceof IJMLExpression) {
            this.expression.setExpectedType(methodType);
            this.expression.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.expression).getSubstitution(scope);
            if (subst != null) {
                this.expression = subst;
            }

        }

        this.expression = JMLMathOperations.computeConversionToReferenceNumeric(methodType, expression, scope);

        this.expression.setExpectedType(methodType); // needed in case of generic method invocation
        if ((expressionType = this.expression.resolveType(scope)) == null) {
            return;
        }
        if (expressionType == TypeBinding.VOID) {
            scope.problemReporter().attemptToReturnVoidValue(this);
            return;
        }
        if (methodType == null) {
            return;
        }

        if (methodType != expressionType) {
            scope.compilationUnitScope().recordTypeConversion(methodType, expressionType);
        }
        if (this.expression.isConstantValueOfTypeAssignableToType(expressionType, methodType)
                || expressionType.isCompatibleWith(methodType)) {

            this.expression.computeConversion(scope, methodType, expressionType);
            if (expressionType.needsUncheckedConversion(methodType)) {
                scope.problemReporter().unsafeTypeConversion(this.expression, expressionType, methodType);
            }
            if (this.expression instanceof CastExpression
                    && (this.expression.bits & (ASTNode.UnnecessaryCast | ASTNode.DisableUnnecessaryCastCheck)) == 0) {
                CastExpression.checkNeedForAssignedCast(scope, methodType, (CastExpression) this.expression);
            }
            return;
        } else if (isBoxingCompatible(expressionType, methodType, this.expression, scope)) {
            this.expression.computeConversion(scope, methodType, expressionType);
            if (this.expression instanceof CastExpression
                    && (this.expression.bits & (ASTNode.UnnecessaryCast | ASTNode.DisableUnnecessaryCastCheck)) == 0) {
                CastExpression.checkNeedForAssignedCast(scope, methodType, (CastExpression) this.expression);
            }
            return;
        }
        if ((methodType.tagBits & TagBits.HasMissingType) == 0) {
            // no need to complain if return type was missing (avoid secondary error : 220967)
            scope.problemReporter().typeMismatchError(expressionType, methodType, this.expression, null);
        }
    }
}
