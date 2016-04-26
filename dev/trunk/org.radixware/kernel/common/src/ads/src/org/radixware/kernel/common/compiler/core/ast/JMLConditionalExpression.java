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

import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.RadixObject;


public class JMLConditionalExpression extends ConditionalExpression implements IJMLExpression {

    private boolean wasResolved = false;

    public JMLConditionalExpression(ConditionalExpression src) {
        super(src.condition, src.valueIfTrue, src.valueIfFalse);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.bits = src.bits;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        if (this.condition instanceof IJMLExpression) {
            this.condition.resolveType(scope);
            final Expression subst = ((IJMLExpression) condition).getSubstitution(scope);
            if (subst != null) {
                this.condition = subst;
            }
        }
        if (this.valueIfTrue instanceof IJMLExpression) {
            this.valueIfTrue.resolveType(scope);
            final Expression subst = ((IJMLExpression) valueIfTrue).getSubstitution(scope);
            if (subst != null) {
                this.valueIfTrue = subst;
            }
        }
        if (this.valueIfFalse instanceof IJMLExpression) {
            this.valueIfFalse.resolveType(scope);
            final Expression subst = ((IJMLExpression) valueIfFalse).getSubstitution(scope);
            if (subst != null) {
                this.valueIfFalse = subst;
            }
        }
        wasResolved = true;
        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return null;
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
