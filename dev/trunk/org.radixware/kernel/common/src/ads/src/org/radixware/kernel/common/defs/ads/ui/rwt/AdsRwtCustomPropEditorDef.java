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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.ui.AdsPropEditorModelClassDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;


public class AdsRwtCustomPropEditorDef extends AdsRwtCustomDialogDef {

    private static final char[] PLATFORM_CLASS_NAME = "org.radixware.wps.views.dialog.RwtPropEditorDialog".toCharArray();

    public static final class Factory {

        private Factory() {
        }

        public static AdsRwtCustomPropEditorDef newInstance() {
            return new AdsRwtCustomPropEditorDef("newCustomPropEditor");
        }

        public static AdsRwtCustomPropEditorDef loadFrom(AbstractDialogDefinition xDef) {
            return new AdsRwtCustomPropEditorDef(xDef);
        }
    }

    private AdsRwtCustomPropEditorDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_PROP_EDITOR), name);
    }

    private AdsRwtCustomPropEditorDef(AbstractDialogDefinition xDef) {
        super(xDef);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        appendTo(xDefRoot.addNewAdsWebCustomPropEditorDefinition(), saveMode);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CUSTOM_PROP_EDITOR;
    }

    @Override
    public AdsPropEditorModelClassDef getModelClass() {
        return (AdsPropEditorModelClassDef) super.getModelClass();
    }

    @Override
    public char[] getSuperClassName() {
        return PLATFORM_CLASS_NAME;
    }

    @Override
    public String getTypeTitle() {
        return "Property Editor Form for Web";
    }

    private class AdsRwtCustomPropEditorClipboardSupport extends AdsClipboardSupport<AdsRwtCustomPropEditorDef> {

        public AdsRwtCustomPropEditorClipboardSupport() {
            super(AdsRwtCustomPropEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
            AdsRwtCustomPropEditorDef.this.appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);
            return xDef.getAdsDefinition().getAdsWebCustomPropEditorDefinition();
        }

        @Override
        protected AdsRwtCustomPropEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsRwtCustomPropEditorDef.Factory.loadFrom(xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsRwtCustomDialogDef> getClipboardSupport() {
        return new AdsRwtCustomPropEditorClipboardSupport();
    }
}
