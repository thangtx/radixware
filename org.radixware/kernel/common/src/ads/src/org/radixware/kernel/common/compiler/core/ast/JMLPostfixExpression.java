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
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.PrefixExpression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import static org.radixware.kernel.common.compiler.core.ast.JMLCompoundAssignment.MISSING_SETTER;

public class JMLPostfixExpression extends PostfixExpression implements IJMLExpression {

    private Expression substitution;
    private boolean wasResolved;

    public JMLPostfixExpression(PostfixExpression src) {
        super(src.lhs, IntLiteral.buildIntLiteral("1".toCharArray(), src.expression.sourceStart, src.expression.sourceEnd), src.operator, 0);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;

        this.substitution = JMLCompoundAssignment.computeSubsritution(this, scope);
        if (substitution != null) {
            return this.resolvedType = this.substitution.resolveType(scope);
        } else if (substitution == MISSING_SETTER) {
            return this.resolvedType = TypeBinding.VOID;
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
