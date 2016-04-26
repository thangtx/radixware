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
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLForStatement extends ForStatement {

    public JMLForStatement(Statement[] initializations,
            Expression condition,
            Statement[] increments,
            Statement action,
            boolean neededScope) {
        super(initializations, condition, increments, action, neededScope, 0, 0);
    }

    public JMLForStatement(ForStatement src) {
        super(src.initializations, src.condition, src.increments, src.action, (src.bits & ASTNode.NeededScope) > 0, 0, 0);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
    }

    @Override
    public void resolve(BlockScope upperScope) {
        // use the scope that will hold the init declarations
        this.scope = (this.bits & ASTNode.NeededScope) != 0 ? new BlockScope(upperScope) : upperScope;
        if (this.initializations != null) {
            for (int i = 0, length = this.initializations.length; i < length; i++) {
                this.initializations[i].resolve(this.scope);
                if (this.initializations[i] instanceof IJMLExpression) {
                    final Expression subst = ((IJMLExpression) this.initializations[i]).getSubstitution(scope);
                    if (subst != null) {
                        this.initializations[i] = subst;
                    }
                }
            }
        }
        if (this.condition != null) {
            if (this.condition instanceof IJMLExpression) {
                this.condition.resolveType(scope);
                final Expression subst = ((IJMLExpression) this.condition).getSubstitution(scope);
                if (subst != null) {
                    this.condition = subst;
                }
            }
            final TypeBinding type = this.condition.resolveTypeExpecting(this.scope, TypeBinding.BOOLEAN);
            this.condition.computeConversion(this.scope, type, type);
        }
        if (this.increments != null) {
            for (int i = 0, length = this.increments.length; i < length; i++) {
                this.increments[i].resolve(this.scope);
                if (this.increments[i] instanceof IJMLExpression) {
                    final Expression subst = ((IJMLExpression) this.increments[i]).getSubstitution(scope);
                    if (subst != null) {
                        this.increments[i] = subst;
                    }
                }
            }
        }
        if (this.action != null) {
            this.action.resolve(this.scope);
            if (this.action instanceof IJMLExpression) {
                final Expression subst = ((IJMLExpression) this.action).getSubstitution(scope);
                if (subst != null) {
                    this.action = subst;
                }
            }
        }
    }
}
