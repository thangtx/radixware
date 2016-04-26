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

import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLArrayAllocationExpression extends ArrayAllocationExpression {

    private boolean wasResolved = false;

    public JMLArrayAllocationExpression(ArrayAllocationExpression src) {
        this.dimensions = src.dimensions;
        this.initializer = src.initializer;
        this.sourceEnd = src.sourceEnd;
        this.sourceStart = src.sourceStart;
        this.statementEnd = src.statementEnd;
        this.type = src.type;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        if (this.dimensions != null) {
            for (int i = 0; i < this.dimensions.length; i++) {
                final Expression dim = this.dimensions[i];
                if (dim instanceof IJMLExpression) {
                    dim.resolveType(scope);
                    final Expression subst = ((IJMLExpression) dim).getSubstitution(scope);
                    if (subst != null) {
                        this.dimensions[i] = subst;
                    }
                }
            }
        }
        /*if (this.initializer instanceof IJMLExpression) {
         this.initializer.resolveType(scope);
         final Expression subst = ((IJMLExpression) this.initializer).getSubstitution(scope);
         if (subst != null) {
         this.initializer = (ArrayInitializer) subst;
         }
         }*/
        return super.resolveType(scope);
    }

}
