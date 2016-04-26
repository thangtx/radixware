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
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.ParagraphDefinition;


public class AdsRwtCustomParagEditorDef extends AdsRwtUIDef {

    public static final String PLATFORM_CLASS_NAME_STR = "org.radixware.wps.views.paragraph.CustomParagEditor";
    private static final char[] PLATFORM_CLASS_NAME = PLATFORM_CLASS_NAME_STR.toCharArray();

    public static class Factory {

        public static final AdsRwtCustomParagEditorDef loadFrom(AdsParagraphExplorerItemDef context, AbstractDialogDefinition xDef) {
            return new AdsRwtCustomParagEditorDef(context, xDef);
        }

        public static final AdsRwtCustomParagEditorDef newInstance(AdsParagraphExplorerItemDef context) {
            return new AdsRwtCustomParagEditorDef(context);
        }
    }

    public AdsParagraphExplorerItemDef getOwnerParagraph() {
        return (AdsParagraphExplorerItemDef) getOwnerDef();
    }

    protected AdsRwtCustomParagEditorDef(AdsParagraphExplorerItemDef context) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR), "WebView");
        setContainer(context);
    }

    protected AdsRwtCustomParagEditorDef(AdsParagraphExplorerItemDef context, AbstractDialogDefinition xDef) {
        super(xDef.getId(), "WebView", xDef);
        setContainer(context);
    }

    public void appendTo(ParagraphDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewWebView(), saveMode);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_PARAG_EDITOR;
    }

    public AdsClassDef getOwnerClass() {
        AdsParagraphExplorerItemDef ownerParag = getOwnerParagraph();
        if (ownerParag != null && ownerParag.findOwnerExplorerRoot() == null) {
            AdsEditorPresentationDef ep = ownerParag.findOwnerEditorPresentation();
            if (ep != null) {
                return ep.getOwnerClass();
            }
        }
        return null;
    }

    private class AdsCustomParagEditorClipboardSupport extends AdsClipboardSupport<AdsRwtCustomParagEditorDef> {

        public AdsCustomParagEditorClipboardSupport() {
            super(AdsRwtCustomParagEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            ParagraphDefinition xDef = ParagraphDefinition.Factory.newInstance();
            AdsRwtCustomParagEditorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AdsRwtCustomParagEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsRwtCustomParagEditorDef.Factory.loadFrom(getOwnerParagraph(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsRwtCustomParagEditorDef> getClipboardSupport() {
        return new AdsCustomParagEditorClipboardSupport();
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
