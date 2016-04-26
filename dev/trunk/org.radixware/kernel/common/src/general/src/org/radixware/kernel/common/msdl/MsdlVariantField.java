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

package org.radixware.kernel.common.msdl;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.schemas.msdl.ChoiceFieldVariant;


public class MsdlVariantField extends MsdlField {

    private final ChoiceFieldVariant variant;

    public MsdlVariantField(ChoiceFieldVariant variant) {
        super(variant.getField());
        this.variant = (ChoiceFieldVariant)variant.copy();
    }

    public ChoiceFieldVariant getVariant() {
        return variant;
    }

    public ChoiceFieldVariant getFullVariant() {
        ChoiceFieldVariant res = (ChoiceFieldVariant)variant.copy();
        res.setField((ChoiceFieldVariant.Field)getFullField());
        return res;
    }

    private class MsdlFieldModelClipboardSupport extends ClipboardSupport<MsdlVariantField> {

        public MsdlFieldModelClipboardSupport() {
            super(MsdlVariantField.this);
        }

        @Override
        public XmlObject copyToXml() {
            return getFullVariant().copy();
        }

        @Override
        public MsdlVariantField loadFrom(XmlObject xmlObject) {
            return new MsdlVariantField((ChoiceFieldVariant) xmlObject);
        }
    }

    @Override
    public ClipboardSupport<? extends MsdlVariantField> getClipboardSupport() {
        return new MsdlFieldModelClipboardSupport();
    }

}
