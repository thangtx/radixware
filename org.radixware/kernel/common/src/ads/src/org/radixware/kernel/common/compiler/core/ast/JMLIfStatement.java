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
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;


public class JMLIfStatement extends IfStatement {

    public JMLIfStatement(IfStatement src) {
        super(src.condition, src.thenStatement, src.elseStatement, src.sourceStart, src.sourceEnd);
    }

    @Override
    public void resolve(BlockScope scope) {
        if (this.condition instanceof IJMLExpression) {
            this.condition.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.condition).getSubstitution(scope);
            if (subst != null) {
                this.condition = subst;
            }
        }
        if (this.thenStatement instanceof IJMLExpression) {
            this.thenStatement.resolve(scope);
            final Expression subst = ((IJMLExpression) this.thenStatement).getSubstitution(scope);
            if (subst != null) {
                this.thenStatement = subst;
            }
        }
        if (this.elseStatement instanceof IJMLExpression) {
            this.elseStatement.resolve(scope);
            final Expression subst = ((IJMLExpression) this.elseStatement).getSubstitution(scope);
            if (subst != null) {
                this.elseStatement = subst;
            }
        }
        super.resolve(scope);
    }
}
