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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.EditorPresentationDefinition;


public class AdsCustomEditorDef extends AdsUIDef {

    public static class Factory {

        public static final AdsCustomEditorDef loadFrom(AdsEditorPresentationDef context, AbstractDialogDefinition xDef) {
            return new AdsCustomEditorDef(context, xDef);
        }

        public static final AdsCustomEditorDef newInstance(AdsEditorPresentationDef context) {
            return new AdsCustomEditorDef(context);
        }
    }

    protected AdsCustomEditorDef(AdsEditorPresentationDef context) {
        super(Id.Factory.changePrefix(context.getId(), EDefinitionIdPrefix.CUSTOM_EDITOR), "View");
        setContainer(context);
    }

    protected AdsCustomEditorDef(AdsEditorPresentationDef context, AbstractDialogDefinition xDef) {
        super(Id.Factory.changePrefix(context.getId(), EDefinitionIdPrefix.CUSTOM_EDITOR), "View", xDef);
        setContainer(context);
    }

    public void appendTo(EditorPresentationDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewView(), saveMode);
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

    private class AdsCustomEditorClipboardSupport extends AdsClipboardSupport<AdsCustomEditorDef> {

        public AdsCustomEditorClipboardSupport() {
            super(AdsCustomEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            EditorPresentationDefinition xDef = EditorPresentationDefinition.Factory.newInstance();
            AdsCustomEditorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AdsCustomEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition)xmlObject;
            return AdsCustomEditorDef.Factory.loadFrom(AdsCustomEditorDef.this.getOwnerEditorPresentation(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsCustomEditorDef> getClipboardSupport() {
        return new AdsCustomEditorClipboardSupport();
    }

}
