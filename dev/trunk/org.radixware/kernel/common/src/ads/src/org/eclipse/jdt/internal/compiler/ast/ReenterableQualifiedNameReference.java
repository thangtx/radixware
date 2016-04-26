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

package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class ReenterableQualifiedNameReference extends QualifiedNameReference {

    private boolean needResolve = true;

    public ReenterableQualifiedNameReference(char[][] tokens, long[] positions, int sourceStart, int sourceEnd) {
        super(tokens, positions, sourceStart, sourceEnd);
    }

    public ReenterableQualifiedNameReference(QualifiedNameReference src) {
        super(src.tokens, src.sourcePositions, src.sourceStart, src.sourceEnd);
        this.bits = src.bits;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (needResolve) {
            needResolve = false;
            resolveTypeImpl(scope);
        }
        return resolvedType;
    }

    protected TypeBinding resolveTypeImpl(BlockScope scope) {
        return super.resolveType(scope);
    }
}
