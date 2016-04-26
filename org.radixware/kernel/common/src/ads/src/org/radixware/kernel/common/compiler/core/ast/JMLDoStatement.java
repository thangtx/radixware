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

import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;


public class JMLDoStatement extends DoStatement {

    public JMLDoStatement(DoStatement src) {
        super(src.condition, src.action, src.sourceStart, src.sourceEnd);
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
        if (this.action instanceof IJMLExpression) {
            this.action.resolve(scope);
            final Expression subst = ((IJMLExpression) this.action).getSubstitution(scope);
            if (subst != null) {
                this.action = subst;
            }
        }
        super.resolve(scope); //To change body of generated methods, choose Tools | Templates.
    }

}
