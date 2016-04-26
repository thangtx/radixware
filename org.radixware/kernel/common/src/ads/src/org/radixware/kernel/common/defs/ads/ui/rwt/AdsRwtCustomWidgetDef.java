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

package org.radixware.kernel.common.defs.ads.ui.rwt;

import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;


public class AdsRwtCustomWidgetDef extends AdsRwtCustomDialogDef {

    public static final class Factory {

        private Factory() {
        }

        public static AdsRwtCustomWidgetDef newInstance() {
            return new AdsRwtCustomWidgetDef("newWebCustomWidget");
        }

        public static AdsRwtCustomWidgetDef loadFrom(AbstractDialogDefinition xDef) {
            return new AdsRwtCustomWidgetDef(xDef);
        }
    }
    public static final String PLATFORM_CLASS_NAME_STR = "org.radixware.wps.views.dialog.RwtCustomWidget";

    private AdsRwtCustomWidgetDef(AbstractDialogDefinition xDef) {
        super(xDef);
    }

    private AdsRwtCustomWidgetDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_WIDGET), name);
    }

    @Override
    public AdsCustomWidgetModelClassDef getModelClass() {
        return (AdsCustomWidgetModelClassDef) super.getModelClass();
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        appendTo(xDefRoot.addNewAdsWebCustomWidgetDefinition(), saveMode);
    }

    @Override
    public char[] getSuperClassName() {
        return PLATFORM_CLASS_NAME_STR.toCharArray();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_WIDGET_DEF;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
}
