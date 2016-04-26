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
import org.eclipse.jdt.internal.compiler.ast.CombinedBinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.RadixObject;


public class JMLCombinedBinaryExpression extends CombinedBinaryExpression implements IJMLExpression {

    private boolean wasResolved = false;
    private Expression substitution;

    public JMLCombinedBinaryExpression(CombinedBinaryExpression src) {
        super(src);
        this.referencesTable = src.referencesTable;
        this.arity = src.arity;
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
        if (this.referencesTable != null) {
            for (int i = 0; i < this.referencesTable.length; i++) {
                if (this.referencesTable[i] instanceof IJMLExpression) {
                    this.referencesTable[i].resolveType(scope);
                    final Expression substitution = ((IJMLExpression) this.referencesTable[i]).getSubstitution(scope);
                    if (substitution instanceof BinaryExpression) {
                        this.referencesTable[i] = (BinaryExpression) substitution;
                    } else if (substitution != null) {
                        scope.problemReporter().illegalVoidExpression(this.referencesTable[i]);
                    }
                }
            }
        }

        wasResolved = true;
        if (this.left instanceof IJMLExpression) {
            this.left.resolveType(scope);
            Expression substitution = ((IJMLExpression) this.left).getSubstitution(scope);
            if (substitution != null) {
                this.left = substitution;
            }
        }
        if (this.right instanceof IJMLExpression) {
            this.right.resolveType(scope);
            Expression substitution = ((IJMLExpression) this.right).getSubstitution(scope);
            if (substitution != null) {
                this.right = substitution;
            }
        }
        Expression substitution = JMLMathOperations.computeExpression(scope, this.left, this.right, (bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT);

        if (substitution != null) {
            this.substitution = substitution;
            return this.resolvedType = substitution.resolveType(scope);
        } else {
            return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
        }
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
