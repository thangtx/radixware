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

import java.util.EnumSet;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsUIType;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;


public class AdsRwtCustomFilterDialogDef extends AdsRwtUIDef implements IAdsTypeSource{

    public static final String PLATFORM_CLASS_NAME_STR = "org.radixware.wps.views.selector.FilterParameters";
    private static final char[] PLATFORM_CLASS_NAME = PLATFORM_CLASS_NAME_STR.toCharArray();

    public static class Factory {

        public static final AdsRwtCustomFilterDialogDef loadFrom(AdsFilterDef context, AbstractDialogDefinition xDef) {
            return new AdsRwtCustomFilterDialogDef(context, xDef);
        }

        public static AdsRwtCustomFilterDialogDef newInstance(AdsFilterDef context) {
            return new AdsRwtCustomFilterDialogDef(context, "WebView");
        }
    }

    protected AdsRwtCustomFilterDialogDef(AdsFilterDef context, AbstractDialogDefinition xDef) {
        super(xDef.getId(), xDef.getName(), xDef);
        setContainer(context);
    }

    protected AdsRwtCustomFilterDialogDef(AdsFilterDef context, String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_FILTER_DIALOG), name);
        setContainer(context);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        AbstractDialogDefinition xDef = xDefRoot.addNewAdsWebCustomDialogDefinition();
        super.appendTo(xDef, saveMode);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_FILTER_DIALOG;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.WEB);
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return new AdsUIType(this);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    public AdsFilterDef getOwnerFilterDef() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsFilterDef) {
                return (AdsFilterDef) container;
            }
        }
        return null;
    }

    private class AdsCustomDialogClipboardSupport extends AdsClipboardSupport<AdsRwtCustomFilterDialogDef> {

        public AdsCustomDialogClipboardSupport() {
            super(AdsRwtCustomFilterDialogDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
            AdsRwtCustomFilterDialogDef.this.appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);
            return xDef.getAdsDefinition().getAdsCustomDialogDefinition();
        }

        @Override
        protected AdsRwtCustomFilterDialogDef loadFrom(XmlObject xmlObject) {
            AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AdsRwtCustomFilterDialogDef.Factory.loadFrom(getOwnerFilterDef(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsRwtCustomFilterDialogDef> getClipboardSupport() {
        return new AdsCustomDialogClipboardSupport();
    }

    @Override
    public char[] getSuperClassName() {
        return PLATFORM_CLASS_NAME;
    }
}
