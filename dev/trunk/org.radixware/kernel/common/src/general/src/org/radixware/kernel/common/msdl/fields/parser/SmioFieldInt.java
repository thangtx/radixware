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

package org.radixware.kernel.common.msdl.fields.parser;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.msdl.EncodingDef;
import org.radixware.schemas.msdl.IntField;
import org.radixware.schemas.types.Int;


public class SmioFieldInt extends SmioFieldSigned {

    private final Int defaultVal;

    public SmioFieldInt(final IntFieldModel model) throws SmioError {
        super(model);
        if (getField().isSetDefaultVal()) {
            defaultVal = Int.Factory.newInstance();
            defaultVal.setLongValue(getField().getDefaultVal());
        } else {
            defaultVal = null;
        }
    }

    @Override
    public boolean getIsBSD() throws SmioException {
        return getCoder().encoding == EncodingDef.BCD;
    }

    @Override
    public final IntField getField() {
        return (IntField)getModel().getField();
    }

    @Override
    public boolean getIsHex() throws SmioException {
        return getCoder() != null && (getCoder().encoding == EncodingDef.HEX || getCoder().encoding == EncodingDef.HEX_EBCDIC);
    }

    @Override
    public void parseField(final XmlObject obj, final IDataSource ids, final boolean containsOddEl) throws SmioException, IOException {
        final String strVal = parseToString(ids);
        if (strVal == null || strVal.isEmpty()) {
            obj.setNil();
            return;
        }
        final String signedStr = parseSign(strVal);
        final BigInteger bgVal = new BigInteger(signedStr, getRadix());
        final Int value = Int.Factory.newInstance();
        value.setBigIntegerValue(bgVal);
        obj.set(value);
    }

    private int getRadix() throws SmioException {
        return getIsHex() ? 16 : 10;
    }

    @Override
    public int getMinFieldLen() {
        return 1;
    }

    @Override
    public ByteBuffer mergeField(final XmlObject obj) throws SmioException {
        Int v = null;
        try {
            v = Int.Factory.parse(obj.getDomNode());
        } catch (XmlException ex) {
            throw new SmioException("Wrong field format",ex);
        }
        final BigInteger val = v.getBigIntegerValue();
        final String valStr = val.toString(getRadix()).toUpperCase();
        sign = null;
        if (minusSign != null && minusSign.length() > 0 && val.compareTo(BigInteger.ZERO) == -1) {
            sign = getCoder().encode(minusSign)[0];
            return mergeFromString(valStr.substring(1));
        }
        if (plusSign != null && plusSign.length() > 0 && ((val.compareTo(BigInteger.ZERO) == 1)||(val.compareTo(BigInteger.ZERO) == 0))) {
            sign = getCoder().encode(plusSign)[0];
            return mergeFromString(valStr);
        }
        return mergeFromString(valStr);
    }

    @Override
    public XmlObject getDefaultVal() {
        return defaultVal;
    }
}
