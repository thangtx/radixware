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

import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.AdsDefinitionScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsLookupOnScope;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class TaggedSingleNameReference extends SingleNameReference {

    public Definition referencedDefinition;
    public AdsTypeDeclaration referencedType;
    public Definition referenceContext;
    private boolean isMetaReference;

    public TaggedSingleNameReference(SingleNameReference src, Definition referenceContext, Definition referencedDefinition) {
        this(src, referenceContext, referencedDefinition, false);
    }

    public TaggedSingleNameReference(SingleNameReference src, Definition referenceContext, Definition referencedDefinition, boolean isInternalGetterAccess) {
        super(("`" + String.valueOf(src.token) + "`").toCharArray(), (long) src.sourceStart << 32 | src.sourceEnd);
        this.referencedDefinition = referencedDefinition;
        this.referenceContext = referenceContext;
    }

    public TaggedSingleNameReference(SingleNameReference src, Definition referenceContext, AdsTypeDeclaration referencedType) {
        super(("`" + String.valueOf(src.token) + "`").toCharArray(), (long) src.sourceStart << 32 | src.sourceEnd);
        this.referencedType = referencedType;
        this.referenceContext = referenceContext;
    }

    public TaggedSingleNameReference(Definition referenceContext, AdsTypeDeclaration referencedType) {
        super(("`" + referencedType.getQualifiedName() + "`").toCharArray(), 0);
        this.referencedType = referencedType;
        this.referenceContext = referenceContext;
    }

    public TaggedSingleNameReference(Definition referenceContext, Definition referencedDefinition, boolean meta) {
        super(("`" + referencedDefinition.getQualifiedName() + "`").toCharArray(), 0);
        this.referencedDefinition = referencedDefinition;
        this.isMetaReference = meta;
        this.referenceContext = referenceContext;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        if (referencedDefinition != null) {
            this.actualReceiverType = scope.enclosingSourceType();
            if (referencedDefinition instanceof AdsDefinition) {
                this.binding = AdsLookupOnScope.resolveAdsReference(scope.enclosingSourceType(), this, true, true, (AdsDefinitionScope) scope.classScope(), scope, referencedDefinition, isMetaReference);
                if (binding instanceof FieldBinding) {
                    checkFieldAccess(scope);
                    return this.resolvedType = ((FieldBinding) this.binding).type;
                } else {
                    if (this.binding instanceof TypeBinding) {
                        this.bits &= ~ASTNode.RestrictiveFlagMASK;  // clear bits
                        this.bits |= Binding.TYPE;
                        this.constant = Constant.NotAConstant;
                        return this.resolvedType = (TypeBinding) this.binding;
                    }
                    return null;
                }
            }
            return super.resolveType(scope);
        } else if (referencedType != null) {//fields can not be referenced against type declaration
            return ((AdsDefinitionScope) scope.classScope()).getType(referenceContext, referencedType);
        } else {
            return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public StringBuffer printExpression(int indent, StringBuffer output) {
        if (referencedDefinition != null) {
            output.append("`").append(referencedDefinition.getQualifiedName(referenceContext.getDefinition())).append("`");
        } else {
            output.append("`").append(referencedType.getQualifiedName(referenceContext.getDefinition())).append("`");
        }
        return output;
    }
}
