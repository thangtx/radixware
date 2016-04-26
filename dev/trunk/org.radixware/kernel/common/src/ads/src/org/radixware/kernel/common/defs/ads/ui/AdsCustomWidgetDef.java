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
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.ui.Signal;


public class AdsCustomWidgetDef extends AdsCustomDialogDef {

    public static final class Factory {

        public static final AdsCustomWidgetDef newInstance() {
            return new AdsCustomWidgetDef(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_WIDGET), "NewWidget");
        }

        public static final AdsCustomWidgetDef loadFrom(AbstractDialogDefinition xDef) {
            return new AdsCustomWidgetDef(xDef);
        }
    }

    public class Signals extends AdsDefinitions<AdsUISignalDef> {

        private Signals() {
            super(AdsCustomWidgetDef.this);
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
    private final Signals signals = new Signals();
    private final UiProperties properties = new UiProperties(this);

    protected AdsCustomWidgetDef(AbstractDialogDefinition xDef) {
        super(xDef);
        if (xDef.getUi() != null) {
            signals.loadFrom(xDef.getUi().getSignals());
            properties.loadFrom(xDef.getUi().getProperties().getPropertyList());
        }
    }

    protected AdsCustomWidgetDef(Id id, String name) {
        super(id, name);
//        properties.add(new AdsUIProperty.IntProperty("testInt", 55));
//        properties.add(new AdsUIProperty.StringProperty("testStr", "test"));
//        properties.add(new AdsUIProperty.LocalizedStringRefProperty("testLocStr", null));

//        signals.add(new AdsUISignalDef("signalTest1"));
//        signals.add(new AdsUISignalDef("signalTest2", Arrays.asList(
//                AdsTypeDeclaration.Factory.newInstance(EValType.INT),
//                AdsTypeDeclaration.Factory.newInstance(EValType.STR)
//                )));
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_WIDGET_DEF;
    }

    public Signals getSignals() {
        return signals;
    }

    public UiProperties getProperties() {
        return properties;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        signals.visit(visitor, provider);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        AbstractDialogDefinition xDef = xDefRoot.addNewAdsCustomWidgetDefinition();
        super.appendTo(xDef, saveMode);
        signals.appendTo(xDef.getUi().addNewSignals());
        xDef.getUi().addNewProperties().setPropertyArray(properties.toXml());
        getModelClass().appendTo(xDef.addNewEmbeddedModel(), saveMode);
    }

    private class AdsCustomWidgetClipboardSupport extends AdsClipboardSupport<AdsCustomWidgetDef> {

        public AdsCustomWidgetClipboardSupport() {
            super(AdsCustomWidgetDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
            AdsCustomWidgetDef.this.appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);
            return xDef.getAdsDefinition().getAdsCustomWidgetDefinition();
        }

        @Override
        protected AdsCustomWidgetDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsCustomWidgetDef.Factory.loadFrom(xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsCustomWidgetDef> getClipboardSupport() {
        return new AdsCustomWidgetClipboardSupport();
    }
}
