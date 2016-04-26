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
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.ads.ui.ICustomDialog;
import org.radixware.kernel.common.defs.ads.ui.enums.EStandardButton;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.ui.Signal;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;

public class AdsRwtCustomDialogDef extends AdsRwtUIDef implements ICustomDialog {

    public static class CloseSignals extends AdsDefinitions<AdsUISignalDef> {

        public CloseSignals(RadixObject container) {
            super(container);
        }

        private void loadFrom(org.radixware.schemas.ui.Signals xSignals) {
            if (xSignals != null) {
                for (Signal xSignal : xSignals.getSignalList()) {
                    add(new AdsUISignalDef(xSignal));
                }
            }
        }

        private void appendTo(org.radixware.schemas.ui.Signals xSignals) {
            if (xSignals != null) {
                for (AdsUISignalDef signal : this) {
                    signal.appendTo(xSignals.addNewSignal());
                }
            }
        }
    }
    private static final char[] PLATFORM_CLASS_NAME = "org.radixware.wps.views.dialog.RwtDialog".toCharArray();

    public static final class Factory {

        private Factory() {
        }

        public static AdsRwtCustomDialogDef newInstance() {
            return new AdsRwtCustomDialogDef("newCustomDialog");
        }

        public static AdsRwtCustomDialogDef loadFrom(AbstractDialogDefinition xDef) {
            return new AdsRwtCustomDialogDef(xDef);
        }
    }
    protected AdsDialogModelClassDef model;
    private final CloseSignals closeSignals = new CloseSignals(this);

    private AdsRwtCustomDialogDef(String name) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_DIALOG), name);
    }

    public CloseSignals getCloseSignals() {
        return closeSignals;
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        this.model.afterOverwrite();
    }

    protected AdsRwtCustomDialogDef(AbstractDialogDefinition xDef) {
        super(xDef.getId(), xDef);
        if (xDef.isSetEmbeddedModel()) {
            this.model = AdsDialogModelClassDef.Factory.loadFrom(this, xDef.getEmbeddedModel());
        } else {
            this.model = AdsDialogModelClassDef.Factory.newInstance(this);
        }
        if (xDef != null && xDef.getUi() != null) {
            closeSignals.loadFrom(xDef.getUi().getSignals());
        }
    }

    protected AdsRwtCustomDialogDef(Id id, String name) {
        super(id, name);
        this.model = AdsDialogModelClassDef.Factory.newInstance(this);

        if (!AdsRwtCustomWidgetDef.PLATFORM_CLASS_NAME_STR.equals(AdsUIUtil.getQtClassName(getWidget()))) {
            this.getWidget().getProperties().add(new AdsUIProperty.SetProperty("standardButtons", EStandardButton.Ok.getValue() + "|" + EStandardButton.Cancel.getValue()));
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_DIALOG;
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        appendTo(xDefRoot.addNewAdsWebCustomDialogDefinition(), saveMode);
    }

    @Override
    public void appendTo(AbstractDialogDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        model.appendTo(xDef.addNewEmbeddedModel(), saveMode);
        if (!closeSignals.isEmpty()) {
            closeSignals.appendTo(xDef.getUi().addNewSignals());
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public AdsDefinition getDialogDef() {
        return this;
    }

    @Override
    public AdsDialogModelClassDef getModelClass() {
        return model;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CUSTOM_DIALOG;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        model.visit(visitor, provider);
    }

    @Override
    public char[] getSuperClassName() {
        return PLATFORM_CLASS_NAME;
    }

    private class AdsRwtCustomDialogClipboardSupport extends AdsClipboardSupport<AdsRwtCustomDialogDef> {

        public AdsRwtCustomDialogClipboardSupport() {
            super(AdsRwtCustomDialogDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
            AdsRwtCustomDialogDef.this.appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);
            return xDef.getAdsDefinition().getAdsWebCustomDialogDefinition();
        }

        @Override
        protected AdsRwtCustomDialogDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsRwtCustomDialogDef.Factory.loadFrom(xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsRwtCustomDialogDef> getClipboardSupport() {
        return new AdsRwtCustomDialogClipboardSupport();
    }

    @Override
    public String getTypeTitle() {
        return "Dialog Form for Web";
    }
}
