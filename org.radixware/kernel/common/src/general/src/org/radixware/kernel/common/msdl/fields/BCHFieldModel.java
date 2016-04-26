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

package org.radixware.kernel.common.msdl.fields;

import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldBCH;
import org.radixware.schemas.msdl.BCHField;


public class BCHFieldModel extends SimpleFieldModel {

    public BCHFieldModel(MsdlField container, BCHField field) {
        super(container, field);
    }

    @Override
    public BCHField getField() {
        return (BCHField)super.getField();
    }

    @Override
    public EFieldType getType() {
        return EFieldType.BCH;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldBCH(this);
        }
        return parser;
    }

}
