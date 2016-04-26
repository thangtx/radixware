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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.EditorPresentationDefinition;


public class AdsRwtCustomEditorDef extends AdsRwtUIDef {

    private static final char[] PLATFORM_CLASS_NAME = "org.radixware.wps.views.editor.CustomEditor".toCharArray();

    public static class Factory {

        public static final AdsRwtCustomEditorDef loadFrom(AdsEditorPresentationDef context, AbstractDialogDefinition xDef) {
            return new AdsRwtCustomEditorDef(context, xDef);
        }

        public static final AdsRwtCustomEditorDef newInstance(AdsEditorPresentationDef context) {
            return new AdsRwtCustomEditorDef(context);
        }
    }

    protected AdsRwtCustomEditorDef(AdsEditorPresentationDef context) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_EDITOR), "WebView");
        setContainer(context);
    }

    protected AdsRwtCustomEditorDef(AdsEditorPresentationDef context, AbstractDialogDefinition xDef) {
        super(xDef.getId(), "WebView", xDef);
        setContainer(context);
    }

    public void appendTo(EditorPresentationDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewWebView(), saveMode);
    }

    public AdsEditorPresentationDef getOwnerEditorPresentation() {
        return (AdsEditorPresentationDef) getOwnerDef();
    }

    public AdsEntityObjectClassDef getOwnerClass() {
        AdsEditorPresentationDef epr = getOwnerEditorPresentation();
        if (epr != null) {
            return epr.getOwnerClass();
        } else {
            return null;
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_EDITOR;
    }

    private class AdsCustomEditorClipboardSupport extends AdsClipboardSupport<AdsRwtCustomEditorDef> {

        public AdsCustomEditorClipboardSupport() {
            super(AdsRwtCustomEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            EditorPresentationDefinition xDef = EditorPresentationDefinition.Factory.newInstance();
            AdsRwtCustomEditorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AdsRwtCustomEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsRwtCustomEditorDef.Factory.loadFrom(AdsRwtCustomEditorDef.this.getOwnerEditorPresentation(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsRwtCustomEditorDef> getClipboardSupport() {
        return new AdsCustomEditorClipboardSupport();
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        //dont use
    }

    @Override
    public char[] getSuperClassName() {
        return PLATFORM_CLASS_NAME;
    }
}
