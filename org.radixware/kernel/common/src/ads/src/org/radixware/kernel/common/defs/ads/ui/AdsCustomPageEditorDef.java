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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.EditorPage;


public class AdsCustomPageEditorDef extends AdsUIDef {

    public static class Factory {

        public static final AdsCustomPageEditorDef loadFrom(AdsEditorPageDef page, AbstractDialogDefinition xDef) {
            return new AdsCustomPageEditorDef(page, xDef);
        }

        public static final AdsCustomPageEditorDef newInstance(AdsEditorPageDef page) {
            return new AdsCustomPageEditorDef(page);
        }
    }

    protected AdsCustomPageEditorDef(AdsEditorPageDef page) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE), "View");
        setContainer(page);
    }

    protected AdsCustomPageEditorDef(AdsEditorPageDef page, AbstractDialogDefinition xDef) {
        super(xDef.getId(), "View", xDef);
        setContainer(page);
    }

    public void appendTo(EditorPage xDef, ESaveMode saveMode) {
        super.appendTo(xDef.addNewView(), saveMode);
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

    public void udpateIdDueToPageInheritanceBugFix(Id newId) {
        RadixObject c = getContainer();
        setContainerNoFire(null);
        setId(newId);
        setContainerNoFire(c);
        setEditState(EEditState.MODIFIED);
        getWidget().className = getClassName();
        getWidget().setEditState(EEditState.MODIFIED);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_PAGE_EDITOR;
    }

    private class AdsCustomPageEditorClipboardSupport extends AdsClipboardSupport<AdsCustomPageEditorDef> {

        public AdsCustomPageEditorClipboardSupport() {
            super(AdsCustomPageEditorDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            EditorPage xDef = EditorPage.Factory.newInstance();
            AdsCustomPageEditorDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AdsCustomPageEditorDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsCustomPageEditorDef.Factory.loadFrom(AdsCustomPageEditorDef.this.getOwnerEditorPage(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsCustomPageEditorDef> getClipboardSupport() {
        return new AdsCustomPageEditorClipboardSupport();
    }
}
