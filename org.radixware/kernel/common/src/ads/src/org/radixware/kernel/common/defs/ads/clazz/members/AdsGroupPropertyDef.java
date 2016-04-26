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
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public class AdsGroupPropertyDef extends AdsTransmittablePropertyDef {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsGroupPropertyDef newInstance() {
            return new AdsGroupPropertyDef("newGroupParameter");
        }

        public static AdsGroupPropertyDef newTemporaryInstance(final AdsPropertyGroup context) {
            final AdsGroupPropertyDef prop = newInstance();
            prop.setContainer(context);
            return prop;
        }
    }

    AdsGroupPropertyDef(final AbstractPropertyDefinition xProp) {
        super(xProp);
    }

    AdsGroupPropertyDef(final String name) {
        super(EDefinitionIdPrefix.ADS_GROUP_PROP, name);
    }

    AdsGroupPropertyDef(final AdsGroupPropertyDef source, final boolean forOverride) {
        super(source, forOverride);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.GROUP_PROPERTY;
    }

    @Override
    protected AdsPropertyDef createOvr(final boolean forOverride) {
        return new AdsGroupPropertyDef(this, forOverride);
    }

    @Override
    public boolean isTransferable(ERuntimeEnvironmentType env) {
        return isSuperTransferable(env);
    }

    @Override
    public String getTypeTitle() {
        return "Entity Group Parameter";
    }

    @Override
    public String getTypesTitle() {
        return "Entity Group Parameters";
    }

}
