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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.AccessRules.Transparence;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsTransparentPropertyDef extends AdsPropertyDef {

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        throw new UnsupportedOperationException("Not supported operation.");
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.DYNAMIC;
    }

    public static final class Factory {

        private Factory() {
        }

        public static AdsTransparentPropertyDef newInstance() {
            return new AdsTransparentPropertyDef("newTransparentProperty");
        }

        public static AdsTransparentPropertyDef newTemporaryInstance(RadixObject container) {
            AdsTransparentPropertyDef property = newInstance();
            property.setContainer(container);
            return property;
        }
    }


    private class AdsPropertyTransparence extends AdsTransparence {

        public AdsPropertyTransparence(AdsDefinition context, String publishedName, boolean isExtendable) {
            super(context, publishedName, isExtendable);
        }

        private AdsPropertyTransparence(AdsTransparentPropertyDef property, Transparence transparence) {
            super(property, transparence);
        }
    }

    private final AdsPropertyTransparence transparence;

    public AdsTransparentPropertyDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_DYNAMIC_PROP), name);

        transparence = new AdsPropertyTransparence(this, name, false);
    }

    public AdsTransparentPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);

        transparence = new AdsPropertyTransparence(this, xProp.getAccessRules().getTransparence());
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (getTransparence() != null) {
            getTransparence().appendTo(xDef.getAccessRules().addNewTransparence());
        }
    }
    @Override
    public AdsTransparence getTransparence() {
        return transparence;
    }

    public char[] getPublishedPropertyName() {
        return transparence.getPublishedName().toCharArray();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
    @Override
    public String getTypeTitle() {
        return "Platform Field Wrapper";
    }
}
