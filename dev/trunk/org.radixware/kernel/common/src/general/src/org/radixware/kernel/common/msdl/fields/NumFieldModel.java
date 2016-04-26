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
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldNum;
import org.radixware.schemas.msdl.NumField;
import org.radixware.schemas.msdl.Structure;


public class NumFieldModel extends SimpleFieldModel {

    public NumFieldModel(MsdlField container, NumField field) {
        super(container, field);
    }

    @Override
    public NumField getField() {
        return (NumField) super.getField();
    }

    @Override
    public EFieldType getType() {
        return EFieldType.NUM;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldNum(this);
        }
        return parser;
    }

    @Override
    public String getEncoding() { 
        if (getField().isSetEncoding()) {
            return getField().getEncoding();
        } else {
            final String[] result = new String[]{null};
            iterateOverParents(true, new ParentAcceptor() {
                @Override
                public boolean accept(MsdlField field, Structure cur) {
                    if (cur.isSetDefaultIntNumEncoding()) {
                        result[0] = cur.getDefaultIntNumEncoding();
                        return false;
                    }
                    if ((isRootMsdlSchemeDirectChild() || field.getModel().isRootMsdlScheme()) && cur.isSetDefaultEncoding()) {
                        String encoding = cur.getDefaultEncoding();
                        if (isAcceptableEncoding(EEncoding.getInstance(encoding))) {
                            result[0] = encoding;
                            return false;
                        }
                    }
                    return true;
                }
            });
            return result[0];
        }
    }

    @Override
    public String getPadChar() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetDefaultIntNumPadChar()) {
                return cur.getDefaultIntNumPadChar();
            }
            if (cur.isSetPadChar()) {
                return cur.getPadChar();
            }
        }
        return null;
    }

    @Override
    public DBFField getDBFField() {
        DBFField f = new DBFField();
        f.setName(getName());
        f.setDataType(DBFField.FIELD_TYPE_F);
        f.setFieldLength(100);
        return f;
    }
}
