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
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;


public class JMLAndAndExpression extends AND_AND_Expression implements IJMLExpression {

    private boolean wasResolved;
    private Expression substitution;

    public JMLAndAndExpression(Expression left, Expression right, int operator) {
        super(left, right, operator);
    }

    @Override
    public Expression getSubstitution(BlockScope scope) {
        return substitution;
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
        
        computeArguments(scope);
        
        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void computeArguments(BlockScope scope){
        if (left instanceof NullLiteral || right instanceof NullLiteral) {
            return;
        }
        final TypeBinding leftType = left.resolveType(scope);
        final TypeBinding rightType = right.resolveType(scope);

        if (leftType == null || rightType == null) {
            return;
        }

        final int leftTypeID = leftType.id;
        final int rightTypeID = rightType.id;

        if ((leftTypeID == TypeIds.T_JavaLangString || rightTypeID == TypeIds.T_JavaLangString)) {
            return;
        }
        
        if (leftTypeID == TypeIds.T_JavaLangBoolean){
            this.left = getBool(left);
            this.left.resolveType(scope);
        }
        
        if (rightTypeID == TypeIds.T_JavaLangBoolean){
            this.right = getBool(right);
            this.right.resolveType(scope);
        }
    }
    
    private Expression getBool(Expression argument){
        final MessageSend ms = BaseGenerator.createMessageSend(BaseGenerator.createQualifiedName(BaseGenerator.RADIX_MATHOPERATIONS_TYPE_NAME), "boolNvl".toCharArray(), new Expression[]{argument});
        return ms;
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
