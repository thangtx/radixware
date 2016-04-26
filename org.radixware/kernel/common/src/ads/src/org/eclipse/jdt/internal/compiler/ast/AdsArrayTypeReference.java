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

import org.eclipse.jdt.internal.compiler.lookup.AdsDefinitionScope;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class AdsArrayTypeReference extends ArrayTypeReference {

    private AdsTypeDeclaration referencedType;
    private Definition referenceContext;

    public AdsArrayTypeReference(ArrayTypeReference src, Definition referenceContext, AdsTypeDeclaration token) {
        super(("`" + String.valueOf(token.getQualifiedName(referenceContext.getDefinition())) + "`").toCharArray(), src.dimensions, (long) src.sourceStart << 32 | src.sourceEnd);
        this.referencedType = token;
        this.referenceContext = referenceContext;
    }

    public AdsArrayTypeReference(Definition referenceContext, AdsTypeDeclaration token) {
        super(("`" + String.valueOf(token.getQualifiedName(referenceContext.getDefinition())) + "`").toCharArray(), token.getArrayDimensionCount(), 0);
        this.referencedType = token.getArrayItemType();
        this.referenceContext = referenceContext;
    }

    @Override
    protected TypeBinding getTypeBinding(Scope scope) {
        if (this.resolvedType != null) {
            return this.resolvedType;
        }
        if (this.dimensions > 255) {
            scope.problemReporter().tooManyDimensions(this);
        }

        TypeBinding leafComponentType = ((AdsDefinitionScope) scope.classScope()).getType(referenceContext, referencedType);

        return scope.createArrayType(leafComponentType, this.dimensions);
    }
}
