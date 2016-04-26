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
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.NumFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.msdl.NumField;
import org.radixware.schemas.types.Num;

public final class SmioFieldNum extends SmioFieldSigned {

    private String fractionalPoint;
    private BigDecimal zero, val;
    private Num defaultVal;

    public SmioFieldNum(NumFieldModel model) throws SmioError {
        super(model);
        try {
            fractionalPoint = null;
            if (getField().isSetFractionalPoint())
                fractionalPoint = getField().getFractionalPoint().toString() ;
            else {
                Character ch = getModel().getFractionalPoint(false);
                if (ch != null)
                    fractionalPoint = ch.toString();
            }
            zero = new BigDecimal("0");
            defaultVal = null;
            if (getField().isSetDefaultVal()) {
                defaultVal = Num.Factory.newInstance();
                defaultVal.setBigDecimalValue(getField().getDefaultVal());
            }
        } catch (Throwable e) {
            throw new SmioError(initError, e, getModel().getName());
        }
    }

    @Override
    public NumField getField() {
        return (NumField)super.getField();
    }

    @Override
    public int getMinFieldLen() {
        return 1;
    }

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        String v = parseToString(ids);
        if (v == null || v.isEmpty()) {
            obj.setNil();
            return;
        }
        Num value = Num.Factory.newInstance();
        value.setStringValue(parseSign(v));
        obj.set(value);
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        Num v = null;
        try {
            v = Num.Factory.parse(obj.getDomNode());
        } catch (XmlException ex) {
            throw new SmioException("Wrong field format",ex);
        }
        val = v.getBigDecimalValue();
        sign = null;
        if (minusSign != null && minusSign.length() > 0 && val.compareTo(zero) == -1) {
            sign = getCoder().encode(minusSign)[0];
            return mergeFromString(v.getStringValue().substring(1));
        }
        if (plusSign != null && plusSign.length() > 0 && ((val.compareTo(zero) == 1)||(val.compareTo(zero) == 0))) {
            sign = getCoder().encode(plusSign)[0];
            return mergeFromString(v.getStringValue());
        }
        return mergeFromString(v.getStringValue());
    }

    @Override
    public XmlObject getDefaultVal() {
        return defaultVal;
    }

    @Override
    public void readAsDbfObject(XmlObject obj, Object object)  throws SmioException, IOException{
//        try {
//            Double d = (Double)object;
//            value.setStringValue(d.toString());
//            obj.set(value);
//        } catch (Throwable e) {
//            throw new SmioException(readError, e, getModel().getName());
//        }
    }

    @Override
    public Object writeAsDbfObject(final XmlObject o)  throws SmioException, IOException{
//        try {
//            if (o == null)
//                return null;
//            else {
//                value.set(o);
//                return Double.parseDouble(value.getStringValue());
//            }
//        } catch (Throwable e) {
//            throw new SmioException(writeError, e, getModel().getName());
//        }
        return null;
    }

}
