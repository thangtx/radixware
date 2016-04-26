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
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomFormDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomReportDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.enums.EStandardButton;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.Form;


public abstract class AbstractRwtCustomFormDialogDef extends AdsRwtUIDef {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AbstractRwtCustomFormDialogDef loadFrom(final AbstractFormPresentations context, final AbstractDialogDefinition xDef) {
            switch (context.getOwnerClass().getClassDefType()) {
                case FORM_HANDLER:
                    return new AdsRwtCustomFormDialogDef(context, xDef);
                case REPORT:
                    return new AdsRwtCustomReportDialogDef(context, xDef);
                default:
                    throw new RadixError("Invalid owner for form dialog");
            }

        }

        public static AbstractRwtCustomFormDialogDef newInstance(final AbstractFormPresentations context) {
            switch (context.getOwnerClass().getClassDefType()) {
                case FORM_HANDLER:
                    return new AdsRwtCustomFormDialogDef(context);
                case REPORT:
                    return new AdsRwtCustomReportDialogDef(context);
                default:
                    throw new RadixError("Invalid owner for form dialog");
            }
        }
    }

    protected AbstractRwtCustomFormDialogDef(final AbstractFormPresentations context) {
        super(Id.Factory.newInstance(context.getCustomViewIdPrefix()), "WebView");
        setContainer(context);
        this.getWidget().getProperties().add(new AdsUIProperty.SetProperty("standardButtons", EStandardButton.Ok.getValue() + "|" + EStandardButton.Cancel.getValue()));
    }

    protected AbstractRwtCustomFormDialogDef(final AbstractFormPresentations context, final AbstractDialogDefinition xDef) {
        super(xDef.getId(), "WebView", xDef);
        setContainer(context);
    }

    public void appendTo(final Form xDef, final ESaveMode saveMode) {
        super.appendTo(xDef.addNewWebView(), saveMode);
    }

    public AdsClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsClassDef && owner instanceof IAdsFormPresentableClass) {
                return (AdsClassDef) owner;
            }
        }
        return null;
    }

    private class AdsCustomFormDialogClipboardSupport extends AdsClipboardSupport<AbstractRwtCustomFormDialogDef> {

        public AdsCustomFormDialogClipboardSupport() {
            super(AbstractRwtCustomFormDialogDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            final Form xDef = Form.Factory.newInstance();
            AbstractRwtCustomFormDialogDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getWebView();
        }

        @Override
        protected AbstractRwtCustomFormDialogDef loadFrom(final XmlObject xmlObject) {
            final AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AbstractRwtCustomFormDialogDef.Factory.loadFrom(((IAdsFormPresentableClass) AbstractRwtCustomFormDialogDef.this.getOwnerClass()).getPresentations(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AbstractRwtCustomFormDialogDef> getClipboardSupport() {
        return new AdsCustomFormDialogClipboardSupport();
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        //do not use
    }
}
