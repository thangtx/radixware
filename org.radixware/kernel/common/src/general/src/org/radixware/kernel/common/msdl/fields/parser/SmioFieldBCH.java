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
import java.nio.ByteBuffer;
import org.apache.xmlbeans.XmlCursor;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.BCHFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.msdl.AlignDef;
import org.radixware.schemas.msdl.BCHField;
import org.radixware.schemas.types.Str;

public class SmioFieldBCH extends SmioFieldSimple {

    private AlignDef.Enum align;
    private final String pad;
    private final Str defaultVal, value;
    private boolean oddElementLen;

    public SmioFieldBCH(BCHFieldModel model) throws SmioError {
        super(model);
        align = null;
        if (getField().isSetAlign()) {
            align = AlignDef.Enum.forString(getField().getAlign());
        } else {
            String a = getModel().getBCHAlign(false);
            if (a != null) {
                align = AlignDef.Enum.forString(a);
            }
        }
        pad = getField().isSetPadChar() ? getField().getPadChar() : getModel().getBCHPad(false);
        Str defVal = null;
        if (getField().isSetDefaultVal()) {
            defVal = Str.Factory.newInstance();
            defVal.setStringValue(getField().getDefaultVal());
        }
        defaultVal = defVal;
        value = Str.Factory.newInstance();
    }

    @Override
    public BCHField getField() {
        return (BCHField) getModel().getField();
    }

    @Override
    public boolean getIsBCH() {
        return true;
    }

    @Override
    public void parseField(final XmlObject obj, final IDataSource ids, final boolean containsOddEl) throws SmioException, IOException {
        byte[] arr = ids.getAll();
        if (arr == null || arr.length == 0) {
            obj.setNil();
            return;
        }
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            int curByte = arr[i];
            if (curByte < 0) {
                curByte += 256;
            }
            int digit1 = (curByte >> 4) & 15;
            r = r.append(Integer.toString(digit1, 16));
            int digit2 = curByte & 15;
            r = r.append(Integer.toString(digit2, 16));
        }
        String result = r.toString();
        if (r.length() > 0 && containsOddEl) {
            if (align == AlignDef.LEFT) {
                result = r.substring(0, r.length() - 1);
            }
            if (align == AlignDef.RIGHT) {
                result = r.substring(1, r.length());
            }
        }
        value.setStringValue(result);
        obj.set(value);
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        XmlCursor c = obj.newCursor();
        String from = c.getTextValue();
        c.dispose();
        String param = from;
        oddElementLen = false;
        if (param.length() % 2 != 0) {
            if (align == AlignDef.LEFT) {
                param = param + pad;
            } else if (align == AlignDef.RIGHT) {
                param = pad + param;
            } else {
                throw new SmioException("Align is not defined for a BCH field and its text value length is " + param.length());
            }
            oddElementLen = true;
        }
        byte res[] = new byte[param.length() / 2];
        for (int j = param.length() - 1; j >= 0; j--) {
            if (j % 2 == 0) {
                res[j / 2] |= (byte) ((Integer.parseInt(param.substring(j, j + 1), 16) << 4));
            } else {
                res[j / 2] |= (byte) Integer.parseInt(param.substring(j, j + 1), 16);
            }
        }
        return ByteBuffer.wrap(res);
    }

    @Override
    public boolean needOddElementLen() {
        return oddElementLen;
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        try {
            if (align == null) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' error: 'Align not defined'"));
            }
            if (pad == null) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' error: 'Pad not defined'"));
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
