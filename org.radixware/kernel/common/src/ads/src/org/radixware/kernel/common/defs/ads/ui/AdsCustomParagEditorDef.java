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
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.ParagraphDefinition;


public class AdsCustomParagEditorDef extends AdsUIDef {

    public static class Factory {

        public static final AdsCustomParagEditorDef loadFrom(AdsParagraphExplorerItemDef context, AbstractDialogDefinition xDef) {
            return new AdsCustomParagEditorDef(context, xDef);
        }

        public static final AdsCustomParagEditorDef newInstance(AdsParagraphExplorerItemDef context) {
            return new AdsCustomParagEditorDef(context);
        }
    }

    public AdsParagraphExplorerItemDef getOwnerParagraph() {
        return (AdsParagraphExplorerItemDef) getOwnerDef();
    }


    protected AdsCustomParagEditorDef(AdsParagraphExplorerItemDef context) {
        super(context.getCustomViewId(), "View");
        setContainer(context);
    }

    protected AdsCustomParagEditorDef(AdsParagraphExplorerItemDef context, AbstractDialogDefinition xDef) {
        super(context.getCustomViewId(), "View", xDef);
        setContainer(context);
    }

    public void appendTo(ParagraphDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewView(), saveMode);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_PARAG_EDITOR;
    }

    public AdsClassDef getOwnerClass() {
        AdsParagraphExplorerItemDef ownerParag = getOwnerParagraph();
        if (ownerParag != null && ownerParag.findOwnerExplorerRoot() == null) {
            AdsEditorPresentationDef ep = ownerParag.findOwnerEditorPresentation();
            if (ep != null)
                return ep.getOwnerClass();
        }
        return null;
    }

    private class AdsCustomParagEditorClipboardSupport extends AdsClipboardSupport<AdsCustomParagEditorDef> {

        public AdsCustomParagEditorClipboardSupport() {
            super(AdsCustomParagEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            ParagraphDefinition xDef = ParagraphDefinition.Factory.newInstance();
            AdsCustomParagEditorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AdsCustomParagEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition)xmlObject;
            return AdsCustomParagEditorDef.Factory.loadFrom(getOwnerParagraph(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsCustomParagEditorDef> getClipboardSupport() {
        return new AdsCustomParagEditorClipboardSupport();
    }

}
