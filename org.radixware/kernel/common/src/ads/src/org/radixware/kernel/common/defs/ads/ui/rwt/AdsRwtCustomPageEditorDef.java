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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;

import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.EditorPage;


public class AdsRwtCustomPageEditorDef extends AdsRwtUIDef {

    public static final String PLATFORM_CLASS_NAME_STR = "org.radixware.wps.views.editor.EditorPageView";
    public static final char[] PLATFORM_CLASS_NAME = PLATFORM_CLASS_NAME_STR.toCharArray();

    public static class Factory {

        public static final AdsRwtCustomPageEditorDef loadFrom(AdsEditorPageDef page, AbstractDialogDefinition xDef) {
            return new AdsRwtCustomPageEditorDef(page, xDef);
        }

        public static final AdsRwtCustomPageEditorDef newInstance(AdsEditorPageDef page) {
            return new AdsRwtCustomPageEditorDef(page);
        }
    }

    protected AdsRwtCustomPageEditorDef(AdsEditorPageDef page) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE), "WebView");
        setContainer(page);
    }

    protected AdsRwtCustomPageEditorDef(AdsEditorPageDef page, AbstractDialogDefinition xDef) {
        super(xDef.getId(), "WebView", xDef);
        setContainer(page);
    }

    public void appendTo(EditorPage xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewWebView(), saveMode);
    }

    public AdsEditorPageDef getOwnerEditorPage() {
        return (AdsEditorPageDef) getOwnerDef();
    }

    public AdsClassDef getOwnerClass() {
        AdsEditorPageDef pageDef = getOwnerEditorPage();
        if (pageDef != null) {
            return pageDef.getOwnerClass();
        } else {
            return null;
        }
    }

//    public void udpateIdDueToPageInheritanceBugFix(Id newId) {
//        RadixObject c = getContainer();
//        setContainerNoFire(null);
//        setId(newId);
//        setContainerNoFire(c);
//        setEditState(EEditState.MODIFIED);
//        getWidget().className = getClassName();
//        getWidget().setEditState(EEditState.MODIFIED);
//    }
    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_PAGE_EDITOR;
    }

    private class AdsCustomPageEditorClipboardSupport extends AdsClipboardSupport<AdsRwtCustomPageEditorDef> {

        public AdsCustomPageEditorClipboardSupport() {
            super(AdsRwtCustomPageEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            EditorPage xDef = EditorPage.Factory.newInstance();
            AdsRwtCustomPageEditorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getWebView();
        }

        @Override
        protected AdsRwtCustomPageEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsRwtCustomPageEditorDef.Factory.loadFrom(AdsRwtCustomPageEditorDef.this.getOwnerEditorPage(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsRwtCustomPageEditorDef> getClipboardSupport() {
        return new AdsCustomPageEditorClipboardSupport();
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
