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

import com.linuxense.javadbf.DBFField;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldBoolean;
import org.radixware.schemas.msdl.BooleanField;


public class BooleanFieldModel extends AbstractFieldModel {

    public BooleanFieldModel(MsdlField container, BooleanField aBoolean) {
        super(container, aBoolean);
    }

    @Override
    public EFieldType getType() {
        return EFieldType.BOOLEAN;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldBoolean(this);
        }
        return parser;
    }

    @Override
    public DBFField getDBFField() {
        DBFField f = new DBFField();
        f.setName(getName());
        f.setDataType(DBFField.FIELD_TYPE_L);
        f.setFieldLength(100);
        return f;
    }
}
