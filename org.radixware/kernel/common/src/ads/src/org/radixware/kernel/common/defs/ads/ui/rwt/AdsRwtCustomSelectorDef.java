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
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.SelectorPresentationDefinition;


public class AdsRwtCustomSelectorDef extends AdsRwtUIDef {

    private static final char[] PLATFORM_CLASS_NAME = "org.radixware.wps.views.selector.RwtSelector".toCharArray();

    public static class Factory {

        public static final AdsRwtCustomSelectorDef loadFrom(AdsSelectorPresentationDef context, AbstractDialogDefinition xDef) {
            return new AdsRwtCustomSelectorDef(context, xDef);
        }

        public static final AdsRwtCustomSelectorDef newInstance(AdsSelectorPresentationDef context) {
            return new AdsRwtCustomSelectorDef(context);
        }
    }

    protected AdsRwtCustomSelectorDef(AdsSelectorPresentationDef context) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_SELECTOR), "WebView");
        setContainer(context);
    }

    protected AdsRwtCustomSelectorDef(AdsSelectorPresentationDef context, AbstractDialogDefinition xDef) {
        super(xDef.getId(), "WebView", xDef);
        setContainer(context);
    }

    public void appendTo(SelectorPresentationDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewWebView(), saveMode);
    }

    public AdsSelectorPresentationDef getOwnerSelectorPresentation() {
        return (AdsSelectorPresentationDef) getOwnerDef();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_SELECTOR;
    }

    public AdsEntityObjectClassDef getOwnerClass() {
        AdsSelectorPresentationDef spr = getOwnerSelectorPresentation();
        if (spr != null) {
            return spr.getOwnerClass();
        } else {
            return null;
        }
    }

    private class AdsCustomSelectorClipboardSupport extends AdsClipboardSupport<AdsRwtCustomSelectorDef> {

        public AdsCustomSelectorClipboardSupport() {
            super(AdsRwtCustomSelectorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            SelectorPresentationDefinition xDef = SelectorPresentationDefinition.Factory.newInstance();
            AdsRwtCustomSelectorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AdsRwtCustomSelectorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsRwtCustomSelectorDef.Factory.loadFrom(getOwnerSelectorPresentation(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsRwtCustomSelectorDef> getClipboardSupport() {
        return new AdsCustomSelectorClipboardSupport();
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        //do not use
    }

    @Override
    public char[] getSuperClassName() {
        return PLATFORM_CLASS_NAME;
    }
}
