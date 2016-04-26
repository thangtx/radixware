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

package org.eclipse.jdt.internal.compiler.lookup;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.enumeration.IPlatformEnumItem;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import static org.radixware.kernel.common.enums.EDefType.CLASS_PROPERTY;
import static org.radixware.kernel.common.enums.EDefType.ENUM_ITEM;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.types.Id;


public final class AdsLookupOnScope {

    private AdsLookupOnScope() {
    }

    public static FieldBinding getField(BlockScope scope, Definition contextDef, TypeBinding receiverType, Jml.Tag tag, InvocationSite invocationSite) {
        if (tag instanceof JmlTagInvocation) {
            final Definition def = ((JmlTagInvocation) tag).resolve(contextDef.getDefinition());
            final AdsDefinitionScope ads = (AdsDefinitionScope) scope.classScope();
            final Binding binding = resolveAdsReference(receiverType, invocationSite, true, true, ads, scope, def, false);
            if (binding instanceof FieldBinding) {
                return (FieldBinding) binding;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static ReferenceBinding findMemberType(Definition referenceContext, TypeBinding recieverType, org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration declaration, BlockScope scope) {
        if (recieverType instanceof AdsTypeBinding) {
            final AdsTypeBinding adsType = (AdsTypeBinding) recieverType;
            if (adsType.definition instanceof AdsClassDef) {
                final AdsClassDef clazz = (AdsClassDef) adsType.definition;
                final AdsType type = declaration.resolve(referenceContext).get();
                if (type instanceof AdsClassType) {
                    final AdsClassDef reference = ((AdsClassType) type).getSource();
                    final Id[] parentPath = clazz.getIdPath();
                    final Id[] childPath = reference.getIdPath();
                    if (childPath.length <= parentPath.length) {
                        return null;
                    }
                    ReferenceBinding targetMemberType = null;
                    ReferenceBinding recvType = adsType;
                    for (int i = 0; i < childPath.length; i++) {
                        if (i < parentPath.length) {
                            //TODO: optimize
                        } else {
                            targetMemberType = recvType.getMemberType(childPath[i].toCharArray());
                            if (targetMemberType == null) {
                                return null;
                            }
                            recvType = targetMemberType;
                        }
                    }
                    return targetMemberType;
                }
            }
        }
        return null;
    }

    public static Binding resolveAdsReference(TypeBinding receiverType, InvocationSite invocationSite, boolean needResolve, boolean invisibleFieldsOk, AdsDefinitionScope adsScope, BlockScope blockScope, Definition referencedDef, boolean meta) {
        if (referencedDef instanceof AdsDefinition) {
            AdsDefinition def = (AdsDefinition) referencedDef;
            Binding binding = null;
            switch (def.getDefinitionType()) {
                case ENUM_ITEM:
                    AdsEnumItemDef referencedEnum = (AdsEnumItemDef) def;
                    TypeBinding enumClassBinding = adsScope.getType(referencedEnum.getOwnerEnum(), false);
                    if (enumClassBinding != null) {
                        if (enumClassBinding instanceof AdsEnumTypeBinding) {
                            binding = ((AdsEnumTypeBinding) enumClassBinding).getField(referencedEnum, true);
                        } else if (enumClassBinding instanceof AdsBinaryTypeBinding && def instanceof IPlatformEnumItem) {
                            binding = ((AdsBinaryTypeBinding) enumClassBinding).getField(((IPlatformEnumItem) def).getPlatformItemName().toCharArray(), true);
                        }
                    }
                    break;
                case CLASS_PROPERTY:
                    binding = blockScope.findField(receiverType, def.getId().toCharArray(), invocationSite, needResolve, invisibleFieldsOk);
                    break;
                default:
                    binding = adsScope.getType(referencedDef, meta);
                    break;
            }
            return binding;
        } else {
            return null;
        }
    }
}
