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

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLORORExpression extends OR_OR_Expression implements IJMLExpression {

    private boolean wasResolved = false;

    public JMLORORExpression(Expression left, Expression right, int operator) {
        super(left, right, operator);
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return null;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;

        if (this.left instanceof IJMLExpression) {
            this.left.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.left).getSubstitution(scope);
            if (subst != null) {
                this.left = subst;
            }
        }
        if (this.right instanceof IJMLExpression) {
            this.right.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.right).getSubstitution(scope);
            if (subst != null) {
                this.right = subst;
            }
        }

        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
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
