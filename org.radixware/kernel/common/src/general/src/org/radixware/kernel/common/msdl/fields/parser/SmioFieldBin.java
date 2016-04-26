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
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.BinFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.schemas.msdl.BinField;
import org.radixware.schemas.msdl.EncodingDef;
import org.radixware.schemas.types.BinHex;

public final class SmioFieldBin extends SmioFieldSimple {

    private BinHex defaultVal;

    public SmioFieldBin(BinFieldModel model) throws SmioError {
        super(model);
        try {
            defaultVal = null;
            if (getField().isSetDefaultVal()) {
                defaultVal = BinHex.Factory.newInstance();
                defaultVal.setByteArrayValue(getField().getDefaultVal());
            }
        } catch (Throwable e) {
            throw new SmioError(initError, e, getModel().getName());
        }
    }

    @Override
    public boolean getIsHex() throws SmioException {
        return getCoder().encoding == EncodingDef.HEX;
    }

    @Override
    public BinField getField() {
        return (BinField) getModel().getField();
    }

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        BinHex value = BinHex.Factory.newInstance();
        if (getCoder().encoding == EncodingDef.BIN) {
            value.setByteArrayValue(ids.getAll());
        } else {
            if (getCoder().encoding == EncodingDef.DECIMAL) {
                String strValue = parseToString(ids);
                if (strValue != null && !strValue.isEmpty()) {
                    if (strValue.length() % 3 != 0) {
                        throw new SmioException("Invalid field length");
                    }
                    int len = strValue.length() / 3;
                    byte[] valueArr = new byte[len];
                    for (int i = 0; i < len; i++) {
                        byte b = (byte) Long.parseLong(strValue.substring(i * 3, i * 3 + 3));
                        valueArr[i] = b;
                    }
                    value.setByteArrayValue(valueArr);
                } else {
                    value.setNil();
                }
            } else {
                String strValue = parseToString(ids);
                if (strValue != null && !strValue.isEmpty()) {
                    if (strValue.length() % 2 != 0) {
                        strValue = "0" + strValue;
                    }
                    byte[] valueArr = new byte[strValue.length() / 2];
                    for (int i = 0; i < valueArr.length; i++) {
                        byte b = (byte) Long.parseLong(strValue.substring(i * 2, i * 2 + 2), 16);
                        valueArr[i] = b;
                    }
                    value.setByteArrayValue(valueArr);
                } else {
                    value.setNil();
                }
            }
        }
        obj.set(value);
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        BinHex value = null;
        try {
            value = BinHex.Factory.parse(obj.getDomNode());
        } catch (XmlException ex) {
            Logger.getLogger(SmioFieldBin.class.getName()).log(Level.SEVERE, null, ex);
        }
        ExtByteBuffer exbf = new ExtByteBuffer();
        if (getCoder().encoding == EncodingDef.BIN) {
            exbf.extPut(ByteBuffer.wrap(value.getByteArrayValue()));
        } else {
            if (getCoder().encoding == EncodingDef.DECIMAL) {
                StringBuilder sb1 = new StringBuilder();
                byte[] valueArr1 = value.getByteArrayValue();
                for (int i = 0; i < valueArr1.length; i++) {
                    long l = valueArr1[i];
                    if (l < 0) {
                        l += 256;
                    }
                    String cur = Long.toString(l).toUpperCase();
                    if (cur.length() == 2) {
                        cur = "0" + cur;
                    }
                    if (cur.length() == 1) {
                        cur = "00" + cur;
                    }
                    sb1.append(cur);
                }
                try {
                    exbf.extPut(ByteBuffer.wrap(sb1.toString().getBytes("US-ASCII")));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(SmioFieldBin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                byte[] valueArr = value.getByteArrayValue();
                for (int i = 0; i < valueArr.length; i++) {
                    String cur = Long.toHexString(valueArr[i]).toUpperCase();
                    if (cur.length() > 2) {
                        cur = cur.substring(cur.length() - 2, cur.length());
                    }
                    if (cur.length() == 1) {
                        cur = "0" + cur;
                    }
                    sb.append(cur);
                }
                try {
                    exbf.extPut(ByteBuffer.wrap(sb.toString().getBytes("US-ASCII")));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(SmioFieldBin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return exbf.flip();
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        try {
            if (getCoder() != null && getCoder().encoding != EncodingDef.BIN && getCoder().encoding != EncodingDef.HEX && getCoder().encoding != EncodingDef.HEX_EBCDIC && getCoder().encoding != EncodingDef.DECIMAL) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' error: 'Wrong encoding'"));
            }
        } catch (Throwable ex) {
            handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "error: '" + ex.getMessage() + "'"));
        }
    }

    @Override
    public XmlObject getDefaultVal() {
        return defaultVal;
    }
}
