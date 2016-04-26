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

import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;

/**
 * Property wrope for database reference
 */
public class AdsInnateRefPropertyDef extends AdsParentRefPropertyDef implements ParentRefProperty {

    public static final class Factory extends AdsPropertyDef.Factory {

        public static final AdsInnateRefPropertyDef newInstance(DdsReferenceDef ref) {
            return new AdsInnateRefPropertyDef(ref);
        }

        public static final AdsInnateRefPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsInnateRefPropertyDef prop = newInstance(null);
            prop.setContainer(context);
            return prop;
        }
    }

    protected AdsInnateRefPropertyDef(DdsReferenceDef ref) {
        super(ref);
    }

    protected AdsInnateRefPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
    }

    protected AdsInnateRefPropertyDef(AdsInnateRefPropertyDef source, boolean forOverride) {
        super(source, forOverride);
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsInnateRefPropertyDef(this, forOverride);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.INNATE;
    }

    /**
     * Associates the property with given parent reference
     */
    public boolean associateWith(DdsReferenceDef ref) {
        if (parentReferenceInfo.associateWith(ref)) {
            updateReferenceClass();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getTypeTitle() {
        return "Parent Reference";
    }

    @Override
    public String getTypesTitle() {
        return "Parent References";
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        getParentReferenceInfo().appendAdditionalToolTip(sb);
    }
}
