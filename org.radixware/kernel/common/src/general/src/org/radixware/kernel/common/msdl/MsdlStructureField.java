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
import org.radixware.schemas.msdl.Structure;


public class MsdlStructureField extends MsdlField {

    public MsdlStructureField(Structure.Field field) {
        super(field);
    }

    @Override
    public Structure.Field getFullField() {
        Structure.Field res = (Structure.Field)getField().copy();
        fillAnyField(res,getModel().getFullField());
        return res;
    }

    @Override
    public Structure.Field getField() {
        return (Structure.Field)super.getField();
    }

    private class MsdlFieldModelClipboardSupport extends ClipboardSupport<MsdlStructureField> {

        public MsdlFieldModelClipboardSupport() {
            super(MsdlStructureField.this);
        }

        @Override
        public XmlObject copyToXml() {
            return getFullField().copy();
        }

        @Override
        public MsdlStructureField loadFrom(XmlObject xmlObject) {
            return new MsdlStructureField((Structure.Field) xmlObject);
        }
    }

    @Override
    public ClipboardSupport<? extends MsdlStructureField> getClipboardSupport() {
        return new MsdlFieldModelClipboardSupport();
    }

}
