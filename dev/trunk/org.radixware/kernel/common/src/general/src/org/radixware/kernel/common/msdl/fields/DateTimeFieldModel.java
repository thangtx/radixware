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
import org.radixware.kernel.common.msdl.MsdlUnitContext;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldDateTime;
import org.radixware.schemas.msdl.DateTimeField;
import org.radixware.schemas.msdl.LenUnitDef;
import org.radixware.schemas.msdl.Structure;


public class DateTimeFieldModel extends SimpleFieldModel {

    public DateTimeFieldModel(MsdlField container, DateTimeField field) {
        super(container, field);
        encodingSet.add(EEncoding.NONE.getValue());
        encodingSet.add(EEncoding.ASCII.getValue());
        encodingSet.add(EEncoding.BCD.getValue());
        encodingSet.add(EEncoding.CP1251.getValue());
        encodingSet.add(EEncoding.CP1252.getValue());
        encodingSet.add(EEncoding.CP866.getValue());
        encodingSet.add(EEncoding.EBCDIC.getValue());
        encodingSet.add(EEncoding.UTF8.getValue());
    }

    @Override
    public EFieldType getType() {
        return EFieldType.DATETIME;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldDateTime(this);
        }
        return parser;
    }

    @Override
    public DBFField getDBFField() {
        DBFField f = new DBFField();
        f.setName(getName());
        f.setDataType(DBFField.FIELD_TYPE_D);
        f.setFieldLength(100);
        return f;
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
                    if (cur.isSetDefaultDateTimeEncoding()) {
                        result[0] = cur.getDefaultDateTimeEncoding();
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
    public String getUnit(boolean inclusive, MsdlUnitContext ctx) {
        if (ctx.getContextType() == MsdlUnitContext.EContext.FIXED_LEN) {
            return super.getUnit(inclusive, ctx);
        } else if (ctx.getContextType() == MsdlUnitContext.EContext.EMBEDDED_LEN) {
            return LenUnitDef.CHAR.toString();
        }
        return null;
    }
}
