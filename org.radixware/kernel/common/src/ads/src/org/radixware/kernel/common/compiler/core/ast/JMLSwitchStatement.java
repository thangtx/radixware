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

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;

public class JMLSwitchStatement extends SwitchStatement {

    public CategorizedProblem missingCaseProblem = null;
    
    public JMLSwitchStatement(SwitchStatement src) {
        this.breakLabel = src.breakLabel;
        this.cases = src.cases;
        this.defaultCase = src.defaultCase;
        this.expression = src.expression;
        this.statements = src.statements;
    }

    @Override
    public void resolve(BlockScope upperScope) {
        if (this.expression instanceof IJMLExpression) {
            this.expression.resolveType(upperScope);
            final Expression subst = ((IJMLExpression) this.expression).getSubstitution(upperScope);
            if (subst != null) {
                this.expression = subst;
            }
        }
        super.resolve(upperScope);
    }

}
