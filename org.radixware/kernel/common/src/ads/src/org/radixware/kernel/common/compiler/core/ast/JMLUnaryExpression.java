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
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLUnaryExpression extends UnaryExpression implements IJMLExpression {

    private boolean wasResolved = false;
    private Expression substitution;

    public JMLUnaryExpression(Expression expression, int operator) {
        super(expression, operator);
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;

        if (expression instanceof IJMLExpression) {
            expression.resolveType(scope);
            final Expression subst = ((IJMLExpression) expression).getSubstitution(scope);
            if (subst != null) {
                expression = subst;
            }
        }

        final Expression mathOper = JMLMathOperations.computeExpression(scope, expression, (bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT);
        if (mathOper != null) {
            this.constant = Constant.NotAConstant;
            this.substitution = mathOper;
            return this.resolvedType = mathOper.resolveType(scope);
        }

        return super.resolveType(scope);
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return substitution;
    }

    @Override
    public Constant resolveCase(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
        return JmlStatementUtils.resolveCase(this, scope, testType, switchStatement);
    }

    @Override
    public Constant resolveCaseOriginal(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
        return super.resolveCase(scope, testType, switchStatement);
    }

    @Override
    public IJMLExpression.RadixObjectLocation getRadixObject(int index) {
        return null;
    }
}
