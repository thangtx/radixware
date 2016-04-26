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

import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;

public class JMLBlock extends Block {

    public JMLBlock(Block src) {
        super(src.explicitDeclarations);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statements = src.statements;
        this.bits = src.bits;
    }

    @Override
    public void resolve(BlockScope upperScope) {
        super.resolve(upperScope); //To change body of generated methods, choose Tools | Templates.
        afterResolve(scope);
    }

    @Override
    public void resolveUsing(BlockScope givenScope) {
        super.resolveUsing(givenScope);
        afterResolve(scope);
    }

    private void afterResolve(BlockScope scope) {
        if (statements != null) {
            for (int i = 0; i < statements.length; i++) {
                if (statements[i] instanceof IJMLExpression) {
                    Expression subst = ((IJMLExpression) statements[i]).getSubstitution(scope);
                    if (subst != null) {
                        statements[i] = subst;
                    }
                }
            }
        }
    }
}
