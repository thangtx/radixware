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

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class DetailReferenceInfo {

    private Id detailReferenceId;
    private AdsTablePropertyDef property;

    DetailReferenceInfo(AdsTablePropertyDef property, DdsReferenceDef detailRef) {
        this.property = property;
        this.detailReferenceId = detailRef == null ? null : detailRef.getId();
    }

    DetailReferenceInfo(AdsTablePropertyDef property, DetailReferenceInfo source) {
        this.property = property;
        this.detailReferenceId = source.detailReferenceId;
    }

    DetailReferenceInfo(AdsTablePropertyDef property, AbstractPropertyDefinition xProp) {
        this.property = property;
        if (xProp instanceof PropertyDefinition) {
            this.detailReferenceId = ((PropertyDefinition) xProp).getDetailRefId();
        } else {
            this.detailReferenceId = null;
        }
    }

    public DdsReferenceDef findDetailReference() {

        return AdsSearcher.Factory.newDdsReferenceSearcher(property).findById(detailReferenceId).get();

    }

    void appendTo(PropertyDefinition xProp) {
        if (detailReferenceId != null) {
            xProp.setDetailRefId(detailReferenceId);
        }
    }

    boolean associateWith(DdsReferenceDef ref) {
        if (ref == null) {
            return false;
        }
        if (ref.getType() != DdsReferenceDef.EType.MASTER_DETAIL) {
            return false;
        }
        this.detailReferenceId = ref.getId();

        this.property.setEditState(EEditState.MODIFIED);

        return true;
    }

    public boolean setDetailReferenceId(Id id) {
        this.detailReferenceId = id;
        this.property.setEditState(EEditState.MODIFIED);
        return true;
    }

    public Id getDetailReferenceId() {
        return detailReferenceId;
    }

    protected void appendAdditionalToolTip(StringBuilder sb) {
        DdsReferenceDef ref = findDetailReference();
        if (ref != null) {
            Utils.appendReferenceToolTipHtml(ref, "Detail reference", sb);
        } else {
            sb.append("<b><font color=\"#FF0000\">Detail reference is not found</font></b>");
        }
    }
}
