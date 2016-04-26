/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.compiler.core.ast;

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.LabeledStatement;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;

/**
 *
 * @author akrylov
 */
public class JMLLabeledStatement extends LabeledStatement {

    private boolean wasResolved = false;

    public JMLLabeledStatement(LabeledStatement src) {
        super(src.label, src.statement, 0, src.sourceEnd);
        this.sourceStart = src.sourceStart;
        this.labelEnd = src.labelEnd;
        this.sourceEnd = src.sourceEnd;
    }

    @Override
    public void resolve(BlockScope scope) {
        if (wasResolved) {
            return;
        }
        wasResolved = true;
        if (this.statement instanceof IJMLExpression) {
            this.statement.resolve(scope);
            Expression subst = ((IJMLExpression) this.statement).getSubstitution(scope);
            if (subst != null) {
                this.statement = subst;
            }
        }
        super.resolve(scope);
    }

}
