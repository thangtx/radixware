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
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsDetailRefPropertyDef extends AdsParentRefPropertyDef implements DetailProperty {

    public static final class Factory {

        public static AdsDetailRefPropertyDef newInstance(DdsReferenceDef detailRef, DdsReferenceDef parentRef) {
            return new AdsDetailRefPropertyDef(detailRef, parentRef);
        }

        public static AdsDetailRefPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsDetailRefPropertyDef prop = newInstance(null, null);
            prop.setContainer(context);
            return prop;
        }
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsDetailRefPropertyDef(this, forOverride);
    }
    DetailReferenceInfo detailReferenceInfo;

    AdsDetailRefPropertyDef(DdsReferenceDef detailRef, DdsReferenceDef parentRef) {
        super(parentRef);
        this.detailReferenceInfo = new DetailReferenceInfo(this, detailRef);
    }

    AdsDetailRefPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        this.detailReferenceInfo = new DetailReferenceInfo(this, xProp);
    }

    AdsDetailRefPropertyDef(AdsDetailRefPropertyDef source, boolean forOverride) {
        super(source, forOverride);
        this.detailReferenceInfo = new DetailReferenceInfo(this, source.detailReferenceInfo);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.DETAIL_PROP;
    }

    @Override
    public DetailReferenceInfo getDetailReferenceInfo() {
        return detailReferenceInfo;
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        detailReferenceInfo.appendTo(xDef);
    }

//    /**
//     * Associates the property with reference to parent table of child table of given detail reference
//     */
//    public boolean associateWith(DdsReferenceDef detailRef, DdsReferenceDef parentRef) {
//        if (detailReferenceInfo.associateWith(detailRef)) {
//            if (parentReferenceInfo.associateWith(parentRef)) {
//                updateReferenceClass();
//                return true;
//            }
//        }
//        return false;
//    }
    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        DdsReferenceDef ref = this.detailReferenceInfo.findDetailReference();
        if (ref != null) {
            list.add(ref);
        }
    }

    @Override
    public String getTypeTitle() {
        return "Detail Reference";
    }

    @Override
    public String getTypesTitle() {
        return "Detail References";
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        getDetailReferenceInfo().appendAdditionalToolTip(sb);
        getParentReferenceInfo().appendAdditionalToolTip(sb);
    }
}
