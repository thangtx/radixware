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

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsUIType;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;

public class AdsCustomDialogDef extends AdsUIDef implements IAdsTypeSource, ICustomDialog {

    @Override
    public AdsDefinition getDialogDef() {
        return this;
    }

    public static class Factory {

        public static AdsCustomDialogDef loadFrom(AbstractDialogDefinition xDef) {
            return new AdsCustomDialogDef(xDef);
        }

        public static AdsCustomDialogDef newInstance() {
            return new AdsCustomDialogDef("newDialogForm");
        }
    }
    private final AdsDialogModelClassDef modelClass;

    protected AdsCustomDialogDef(AbstractDialogDefinition xDef) {
        super(xDef.getId(), xDef.getName(), xDef);
        this.modelClass = AdsDialogModelClassDef.Factory.loadFrom(this, xDef.getEmbeddedModel());
    }

    protected AdsCustomDialogDef(String name) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_DIALOG), name);
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        this.modelClass.afterOverwrite();
    }

    protected AdsCustomDialogDef(Id id, String name) {
        super(id, name);
        this.modelClass = AdsDialogModelClassDef.Factory.newInstance(this);
    }

    public AdsDialogModelClassDef getModelClass() {
        return modelClass;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.modelClass.visit(visitor, provider);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        AbstractDialogDefinition xDef = xDefRoot.addNewAdsCustomDialogDefinition();
        super.appendTo(xDef, saveMode);
        getModelClass().appendTo(xDef.addNewEmbeddedModel(), saveMode);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_DIALOG;
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return new AdsUIType(this);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    private class AdsCustomDialogClipboardSupport extends AdsClipboardSupport<AdsCustomDialogDef> {

        public AdsCustomDialogClipboardSupport() {
            super(AdsCustomDialogDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
            AdsCustomDialogDef.this.appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);
            return xDef.getAdsDefinition().getAdsCustomDialogDefinition();
        }

        @Override
        protected AdsCustomDialogDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsCustomDialogDef.Factory.loadFrom(xDef);
        }

        @Override
        public boolean isEncodedFormatSupported() {
            return true;
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return AdsCustomDialogDef.Factory.class.getDeclaredMethod("loadFrom", AbstractDialogDefinition.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends AdsCustomDialogDef> getClipboardSupport() {
        return new AdsCustomDialogClipboardSupport();
    }

    @Override
    public String getTypeTitle() {
        return "Dialog Form for Explorer";
    }
}
