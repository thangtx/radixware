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
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;


public class ReenterableSingleNameReference extends SingleNameReference {

    private boolean needResolve = true;

    public ReenterableSingleNameReference(SingleNameReference src) {
        super(src.token, 0);
        sourceStart = src.sourceStart;
        sourceEnd = src.sourceEnd;
        bits = src.bits;
    }
    public ReenterableSingleNameReference(char[] token) {
        super(token, 0);
        sourceStart = 0;
        sourceEnd = 0;        
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (needResolve) {
            needResolve = false;
            return super.resolveType(scope);
        }
        return resolvedType;

    }

    @Override
    public TypeBinding resolveType(ClassScope scope) {
        if (needResolve) {
            needResolve = false;
            return super.resolveType(scope);
        }
        return resolvedType;

    }
}
