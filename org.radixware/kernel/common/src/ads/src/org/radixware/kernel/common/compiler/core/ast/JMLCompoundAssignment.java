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

import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.RadixObject;


public class JMLCompoundAssignment extends CompoundAssignment implements IJMLExpression {

    private boolean wasResolved = false;
    private Expression substitution;

    public JMLCompoundAssignment(Expression lhs, Expression expression, int operator, int sourceEnd) {
        super(lhs, expression, operator, sourceEnd);
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;
        this.substitution = computeSubsritution(this, scope);
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
    protected static final Expression MISSING_SETTER = new Expression() {
        @Override
        public StringBuffer printExpression(int indent, StringBuffer output) {
            return output;
        }
    };

    static Expression computeSubsritution(CompoundAssignment assignment, BlockScope scope) {
        if (assignment.expression instanceof IJMLExpression) {
            assignment.expression.resolveType(scope);
            final Expression subst = ((IJMLExpression) assignment.expression).getSubstitution(scope);
            if (subst != null) {
                assignment.expression = subst;
            }
        }

        if (assignment.lhs instanceof IJMLExpression) {
            assignment.lhs.resolveType(scope);
            final Expression subst = ((IJMLExpression) assignment.lhs).getSubstitution(scope);
            if (subst instanceof JMLMessageSend) {
                final JMLMessageSend getter = (JMLMessageSend) subst;
                final JMLMessageSend setter = getter.convertToSetter(scope);
                if (setter != null) {
                    final JMLBinaryExpression setterArgument = new JMLBinaryExpression(getter, assignment.expression, assignment.operator);
                    setter.arguments = new Expression[]{setterArgument};
                    return setter;
                } else {
                    return MISSING_SETTER;
                }
            }
        }
        final Expression mathOper = JMLMathOperations.computeExpression(scope, assignment.lhs, assignment.expression, assignment.operator);
        if (mathOper != null) {
            return new JMLAssignment(assignment.lhs, mathOper, assignment.sourceEnd);
        }
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
