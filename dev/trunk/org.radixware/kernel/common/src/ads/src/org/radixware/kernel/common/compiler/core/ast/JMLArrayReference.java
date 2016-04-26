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

import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class JMLArrayReference extends ArrayReference {

    private boolean wasResolved = false;

    public JMLArrayReference(ArrayReference src) {
        super(src.receiver, src.position);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statementEnd = src.statementEnd;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (wasResolved) {
            return this.resolvedType;
        }
        wasResolved = true;
        if (this.position instanceof IJMLExpression) {
            this.position.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.position).getSubstitution(scope);
            if (subst != null) {
                this.position = subst;
            }
        }
        if (this.receiver instanceof IJMLExpression) {
            this.receiver.resolveType(scope);
            final Expression subst = ((IJMLExpression) this.receiver).getSubstitution(scope);
            if (subst != null) {
                this.receiver = subst;
            }
        }
        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
    }
}
