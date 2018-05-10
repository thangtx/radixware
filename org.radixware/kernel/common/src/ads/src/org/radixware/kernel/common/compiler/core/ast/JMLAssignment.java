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
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.IsStrictlyAssigned;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;

public class JMLAssignment extends Assignment implements IJMLExpression {

    private Expression substitution;
    private boolean wasResolved = false;

    public JMLAssignment(Expression lhs, Expression expression, int sourceEnd) {
        super(lhs, expression, sourceEnd);
        this.lhs.bits |= IsStrictlyAssigned;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;

        if (this.lhs instanceof IJMLExpression) {

            this.lhs.bits |= ASTNode.IsStrictlyAssigned;

            final AdsProblemReporter.InvalidMethodHandler handler = ((AdsProblemReporter) scope.problemReporter()).pushIvalidMethodHandler();
            this.lhs.resolveType(scope);

            final Expression subst = ((IJMLExpression) this.lhs).getSubstitution(scope);

            if (subst != null) {
                if (subst instanceof JMLMessageSend) {
                    JMLMessageSend setter = (JMLMessageSend) subst;
                    setter = setter.convertToSetter(scope);
                    if (setter != null) {
                        handler.reject();
                        setter.arguments = new Expression[]{this.expression};
                        this.substitution = setter;
                        return this.resolvedType = this.substitution.resolveType(scope);
                    } else {
                        handler.post();
                        return TypeBinding.VOID;
                    }
                } else {
                    handler.post();
                    this.lhs = subst;
                }
            } else {
                handler.post();
            }
        }
        if (expression != null) {
            final TypeBinding lhsType = this.lhs.resolveType(scope);
            if (this.expression instanceof IJMLExpression) {
                this.expression.setExpectedType(resolvedType);
                this.expression.resolve(scope);
                final Expression subst = ((IJMLExpression) this.expression).getSubstitution(scope);
                if (subst != null) {
                    this.expression = subst;
                }
            }

            final Expression mathOper = JMLMathOperations.computeConversionToReferenceNumeric(lhsType, expression, scope);
            if (mathOper != null) {
                this.expression = mathOper;
            }
        }

        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public TypeBinding resolveTypeExpecting(BlockScope scope, TypeBinding expectedType) {
        TypeBinding rhsType = this.expression.resolvedType;
        if (rhsType == null) {
            return null;
        }
        return super.resolveTypeExpecting(scope, expectedType);
    }
    
    
}
