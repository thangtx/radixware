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
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.Form;


public abstract class AbstractCustomFormDialogDef extends AdsUIDef {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AbstractCustomFormDialogDef loadFrom(final AbstractFormPresentations context, final AbstractDialogDefinition xDef) {
            switch (context.getOwnerClass().getClassDefType()) {
                case FORM_HANDLER:
                    return new AdsCustomFormDialogDef(context, xDef);
                case REPORT:
                    return new AdsCustomReportDialogDef(context, xDef);
                default:
                    throw new RadixError("Invalid owner for form dialog");
            }

        }

        public static AbstractCustomFormDialogDef newInstance(final AbstractFormPresentations context) {
            switch (context.getOwnerClass().getClassDefType()) {
                case FORM_HANDLER:
                    return new AdsCustomFormDialogDef(context);
                case REPORT:
                    return new AdsCustomReportDialogDef(context);
                default:
                    throw new RadixError("Invalid owner for form dialog");
            }
        }
    }

    protected AbstractCustomFormDialogDef(final AbstractFormPresentations context) {
        super(Id.Factory.newInstance(context.getCustomViewIdPrefix()), "View");
        setContainer(context);
    }

    protected AbstractCustomFormDialogDef(final AbstractFormPresentations context, final AbstractDialogDefinition xDef) {
        super(xDef.getId(), "View", xDef);
        setContainer(context);
    }

    public void appendTo(final Form xDef, final ESaveMode saveMode) {
        super.appendTo(xDef.addNewView(), saveMode);
    }

    public AdsClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsClassDef && owner instanceof IAdsFormPresentableClass) {
                return (AdsClassDef) owner;
            }
        }
        return null;
    }

    private class AdsCustomFormDialogClipboardSupport extends AdsClipboardSupport<AbstractCustomFormDialogDef> {

        public AdsCustomFormDialogClipboardSupport() {
            super(AbstractCustomFormDialogDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            final Form xDef = Form.Factory.newInstance();
            AbstractCustomFormDialogDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef.getView();
        }

        @Override
        protected AbstractCustomFormDialogDef loadFrom(final XmlObject xmlObject) {
            final AbstractDialogDefinition xDef = (AbstractDialogDefinition) xmlObject;
            return AbstractCustomFormDialogDef.Factory.loadFrom(((IAdsFormPresentableClass) AbstractCustomFormDialogDef.this.getOwnerClass()).getPresentations(), xDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AbstractCustomFormDialogDef> getClipboardSupport() {
        return new AdsCustomFormDialogClipboardSupport();
    }
}
