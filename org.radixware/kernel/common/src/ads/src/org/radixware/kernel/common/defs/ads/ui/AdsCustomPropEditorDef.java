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

package org.radixware.kernel.common.defs.ads.ui;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;


public class AdsCustomPropEditorDef extends AdsCustomDialogDef {

    public static class Factory {

        public static final AdsCustomPropEditorDef loadFrom(AbstractDialogDefinition xDef) {
            return new AdsCustomPropEditorDef(xDef);
        }

        public static final AdsCustomPropEditorDef newInstance() {
            return new AdsCustomPropEditorDef("NewPropertyEditor");
        }
    }

    protected AdsCustomPropEditorDef(AbstractDialogDefinition xDef) {
        super(xDef);
    }

    protected AdsCustomPropEditorDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_PROP_EDITOR), name);
    }

    @Override
    public AdsPropEditorModelClassDef getModelClass() {
        return (AdsPropEditorModelClassDef) super.getModelClass();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_PROP_EDITOR;
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        AbstractDialogDefinition xDef = xDefRoot.addNewAdsCustomPropEditorDefinition();
        super.appendTo(xDef, saveMode);
        getModelClass().appendTo(xDef.addNewEmbeddedModel(), saveMode);
    }

    private class AdsCustomPropEditorClipboardSupport extends AdsClipboardSupport<AdsCustomPropEditorDef> {

        public AdsCustomPropEditorClipboardSupport() {
            super(AdsCustomPropEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
            AdsCustomPropEditorDef.this.appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);
            return xDef.getAdsDefinition().getAdsCustomPropEditorDefinition();
        }

        @Override
        protected AdsCustomPropEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsCustomPropEditorDef.Factory.loadFrom(xDef);
        }

        @Override
        public boolean isEncodedFormatSupported() {
            return true;
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return AdsCustomPropEditorDef.Factory.class.getDeclaredMethod("loadFrom", AbstractDialogDefinition.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends AdsCustomPropEditorDef> getClipboardSupport() {
        return new AdsCustomPropEditorClipboardSupport();
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public String getTypeTitle() {
        return "Property Editor Form for Explorer";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CUSTOM_PROP_EDITOR;
    }
}
