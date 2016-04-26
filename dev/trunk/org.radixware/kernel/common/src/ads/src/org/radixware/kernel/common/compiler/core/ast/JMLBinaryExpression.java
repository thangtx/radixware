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
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.RadixObject;


public class JMLBinaryExpression extends BinaryExpression implements IJMLExpression {

    private Expression substitution;
    private boolean wasResolved = false;

    public JMLBinaryExpression(Expression left, Expression right, int operator) {
        super(left, right, operator);
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;
        if (this.left instanceof IJMLExpression) {
            this.left.resolveType(scope);
            final Expression s = ((IJMLExpression) this.left).getSubstitution(scope);
            if (s != null) {
                this.left = s;
            }
        }

        if (this.right instanceof IJMLExpression) {
            this.right.resolveType(scope);
            final Expression s = ((IJMLExpression) this.right).getSubstitution(scope);
            if (s != null) {
                this.right = s;
            }
        }
        final Expression mathOper = JMLMathOperations.computeExpression(scope, left, right, (bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT);
        if (mathOper != null) {
            this.constant = Constant.NotAConstant;
            this.substitution = mathOper;
            return this.resolvedType = this.substitution.resolveType(scope);
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
    public RadixObjectLocation getRadixObject(int index) {
        return null;
    }
}
