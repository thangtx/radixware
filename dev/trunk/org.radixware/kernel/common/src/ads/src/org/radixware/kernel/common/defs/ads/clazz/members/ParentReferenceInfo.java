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

import java.util.Collection;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class ParentReferenceInfo {

    private Id parentReferenceId;
    private AdsTablePropertyDef property;

    ParentReferenceInfo(AdsTablePropertyDef property, DdsReferenceDef ref) {
        this(property);
        this.parentReferenceId = ref == null ? null : ref.getId();
        updateType(ref);
    }

    private DdsTableDef findMaster(DdsTableDef table) {
        Collection<DdsReferenceDef> refs = table.collectOutgoingReferences();
        for (DdsReferenceDef ref : refs) {
            if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {//this is detail table
                DdsTableDef parent = ref.findParentTable(property);
                if (parent == null) {
                    return null;
                } else {
                    return findMaster(parent);
                }
            }
        }
        return table;
    }

    private void updateType(DdsReferenceDef ref) {
        if (ref != null) {
            DdsTableDef table = ref.findParentTable(property);
            if (table != null) {
                table = findMaster(table);
            }
            if (table != null) {
                property.getValue().setType(AdsTypeDeclaration.Factory.newParentRef(table));
            } else {
                property.getValue().setType(AdsTypeDeclaration.Factory.newInstance(EValType.PARENT_REF));
            }
        } else {
            property.getValue().setType(AdsTypeDeclaration.Factory.newInstance(EValType.PARENT_REF));
        }
    }

    private ParentReferenceInfo(AdsTablePropertyDef property) {
        this.property = property;
    }

    ParentReferenceInfo(AdsTablePropertyDef property, AbstractPropertyDefinition xProp) {
        this(property);
        if (xProp instanceof PropertyDefinition) {
            this.parentReferenceId = ((PropertyDefinition) xProp).getParentRefId();
        } else {
            this.parentReferenceId = null;
        }
    }

    ParentReferenceInfo(AdsTablePropertyDef property, ParentReferenceInfo source) {
        this(property);
        this.parentReferenceId = source.parentReferenceId;
    }

    public void setParentReferenceId(Id id) {
        this.parentReferenceId = id;
        DdsReferenceDef ref = findParentReference();
        if (ref != null) {
            updateType(ref);
        }
    }

    public Id getParentReferenceId() {
        return this.parentReferenceId;
    }

    public DdsReferenceDef findParentReference() {
        return AdsSearcher.Factory.newDdsReferenceSearcher(property).findById(parentReferenceId).get();
    }

    boolean associateWith(DdsReferenceDef ref) {
        if (ref == null) {
            return false;
        }

        if (ref.getType() != DdsReferenceDef.EType.LINK) {
            return false;
        }

        this.parentReferenceId = ref.getId();

        property.setEditState(EEditState.MODIFIED);

        return true;
    }

    void appendTo(PropertyDefinition xProp) {
        if (parentReferenceId != null) {
            xProp.setParentRefId(parentReferenceId);
        }
    }

    protected void appendAdditionalToolTip(StringBuilder sb) {
        DdsReferenceDef ref = findParentReference();
        if (ref != null) {
            Utils.appendReferenceToolTipHtml(ref, "Parent reference", sb);
        } else {
            sb.append("<b><font color=\"#FF0000\">Parent reference is not found</font></b>");
        }
    }
}
