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
        encodingSet.add(EEncoding.EBCDIC_CP1047.getValue());
        encodingSet.add(EEncoding.UTF8.getValue());
    }

    @Override
    public EFieldType getType() {
        return EFieldType.DATETIME;
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
    public String calcEncoding() {
        return calcEncoding(true);
    }

    @Override
    public String calcEncoding(boolean inclusive) {
        if (inclusive && getField().isSetEncoding()) {
            return getField().getEncoding();
        } else {
            for (Structure cur : getParentList(true)) {
                if (cur.isSetDefaultDateTimeEncoding()) {
                    return cur.getDefaultDateTimeEncoding();
                }
                final String defEnc = getAcceptableEncoding(cur.getDefaultEncoding());
                if (defEnc != null) {
                    return defEnc;
                }
            }
            return null;
        }
    }
    
    @Override
    public String getUnit(boolean inclusive, MsdlUnitContext ctx) {
        if (ctx.getContextType() == MsdlUnitContext.EContext.EMBEDDED_LEN) {
            return LenUnitDef.CHAR.toString();
        }
        return super.getUnit(inclusive, ctx);
    }
}
