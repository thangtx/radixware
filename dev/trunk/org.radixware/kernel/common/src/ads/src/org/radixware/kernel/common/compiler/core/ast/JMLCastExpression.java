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

import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLCastExpression extends CastExpression {

    private boolean wasResolved = false;

    public JMLCastExpression(Expression expression, TypeReference type) {
        super(expression, type);
    }

    public JMLCastExpression(CastExpression src) {
        super(src.expression, src.type);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return resolvedType;
        }
        wasResolved = true;

        if (this.type instanceof IJMLExpression) {
            this.type.resolveType(scope);
            final TypeReference substitution = (TypeReference) ((IJMLExpression) this.type).getSubstitution(scope);
            if (substitution != null) {
                this.type = substitution;
            }
        }
        if (expression instanceof IJMLExpression) {
            expression.resolveType(scope);
            final Expression substitution = ((IJMLExpression) this.expression).getSubstitution(scope);
            if (substitution != null) {
                this.expression = substitution;
            }
        }
        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
    }
}
