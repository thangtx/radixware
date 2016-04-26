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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.SelectorPresentationDefinition;


public class AdsCustomSelectorDef extends AdsUIDef {

    public static class Factory {

        public static final AdsCustomSelectorDef loadFrom(AdsSelectorPresentationDef context, AbstractDialogDefinition xDef) {
            return new AdsCustomSelectorDef(context, xDef);
        }

        public static final AdsCustomSelectorDef newInstance(AdsSelectorPresentationDef context) {
            return new AdsCustomSelectorDef(context);
        }
    }

    protected AdsCustomSelectorDef(AdsSelectorPresentationDef context) {
        super(Id.Factory.changePrefix(context.getId(), EDefinitionIdPrefix.CUSTOM_SELECTOR), "View");
        setContainer(context);
    }

    protected AdsCustomSelectorDef(AdsSelectorPresentationDef context, AbstractDialogDefinition xDef) {
        super(Id.Factory.changePrefix(context.getId(), EDefinitionIdPrefix.CUSTOM_SELECTOR), "View", xDef);
        setContainer(context);
    }

    public void appendTo(SelectorPresentationDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewView(), saveMode);
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

    private class AdsCustomSelectorClipboardSupport extends AdsClipboardSupport<AdsCustomSelectorDef> {

        public AdsCustomSelectorClipboardSupport() {
            super(AdsCustomSelectorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            SelectorPresentationDefinition xDef = SelectorPresentationDefinition.Factory.newInstance();
            AdsCustomSelectorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AdsCustomSelectorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition)xmlObject;
            return AdsCustomSelectorDef.Factory.loadFrom(getOwnerSelectorPresentation(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsCustomSelectorDef> getClipboardSupport() {
        return new AdsCustomSelectorClipboardSupport();
    }
}
