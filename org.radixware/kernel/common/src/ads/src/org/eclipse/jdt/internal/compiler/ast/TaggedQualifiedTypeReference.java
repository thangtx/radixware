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

import org.eclipse.jdt.internal.compiler.lookup.IAdsScope;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class TaggedQualifiedTypeReference extends QualifiedTypeReference {

    public AdsTypeDeclaration[] referencedTypes;
    private Definition referenceContext;

    public TaggedQualifiedTypeReference(QualifiedTypeReference src, Definition referenceContext, AdsTypeDeclaration[] tags) {
        super(src.tokens, src.sourcePositions);
        this.referenceContext = referenceContext;
        this.referencedTypes = tags;
    }

    @Override
    protected TypeBinding findNextTypeBinding(int tokenIndex, Scope scope, PackageBinding packageBinding) {
        if (referencedTypes[tokenIndex] != null) {
            return this.resolvedType = findAdsScope(scope).getType(referenceContext, referencedTypes[tokenIndex]);
        }
        return super.findNextTypeBinding(tokenIndex, scope, packageBinding); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TypeBinding getTypeBinding(Scope scope) {
        return super.getTypeBinding(scope); //To change body of generated methods, choose Tools | Templates.
    }

    private IAdsScope findAdsScope(Scope scope) {
        while (scope != null) {
            if (scope instanceof IAdsScope) {
                return (IAdsScope) scope;
            }
            scope = scope.parent;
        }
        return null;
    }
}
