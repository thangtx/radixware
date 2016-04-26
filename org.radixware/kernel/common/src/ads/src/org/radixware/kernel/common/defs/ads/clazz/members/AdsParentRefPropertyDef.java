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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public abstract class AdsParentRefPropertyDef extends AdsTablePropertyDef implements ParentRefProperty {

    protected final ParentReferenceInfo parentReferenceInfo;

    protected AdsParentRefPropertyDef(DdsReferenceDef parentRef) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.DDS_COLUMN), parentRef == null ? "newReferenceProperty" : parentRef.getName());
        this.parentReferenceInfo = new ParentReferenceInfo(this, parentRef);
        DdsTableDef table = parentRef == null ? null : parentRef.findParentTable(parentRef);
        getValue().setType(table == null ? AdsTypeDeclaration.Factory.newInstance(EValType.PARENT_REF) : AdsTypeDeclaration.Factory.newParentRef(table));
        this.getPresentationSupport().checkPresentation();

    }

    protected AdsParentRefPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        this.parentReferenceInfo = new ParentReferenceInfo(this, xProp);
    }

    protected AdsParentRefPropertyDef(AdsParentRefPropertyDef source, boolean forOverride) {
        super(source, forOverride);
        this.parentReferenceInfo = new ParentReferenceInfo(this, source.parentReferenceInfo);
    }

    @Override
    public ParentReferenceInfo getParentReferenceInfo() {
        return parentReferenceInfo;
    }

    public AdsEntityObjectClassDef findReferencedEntityClass() {
        AdsType type = getValue().getType().resolve(this).get();
        if (type instanceof ParentRefType) {
            return ((ParentRefType) type).getSource();
        } else {
            return null;
        }
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        parentReferenceInfo.appendTo(xDef);
    }

    /**
     * Tries to find entity class handling parent table of reference associated with property
     * If class succsessfully determined corrects property type declaration as parent reference to class found
     * else makes type of property as simple parent reference to platform emtity handler 
     */
    protected void updateReferenceClass() {
        DdsReferenceDef ref = parentReferenceInfo.findParentReference();
        AdsTypeDeclaration typeDecl = null;
        if (ref != null) {
            DdsTableDef parentTable = ref.findParentTable(this);
            if (parentTable != null) {
                AdsEntityClassDef classDef = AdsSearcher.Factory.newEntityClassSearcher(getModule()).findEntityClass(parentTable).get();
                if (classDef != null) {
                    typeDecl = AdsTypeDeclaration.Factory.newParentRef(classDef);
                }
            }
        }
        if (typeDecl != null) {
            value.setType(typeDecl);
        } else {
            value.setType(AdsTypeDeclaration.Factory.newInstance(EValType.PARENT_REF));
        }
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        DdsReferenceDef ref = this.parentReferenceInfo.findParentReference();
        if (ref != null) {
            list.add(ref);
        }
    }
}
