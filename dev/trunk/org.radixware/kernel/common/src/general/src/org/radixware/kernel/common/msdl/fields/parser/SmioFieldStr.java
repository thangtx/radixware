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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.StrFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.msdl.StrCharSetDef;
import org.radixware.schemas.msdl.StrField;
import org.radixware.schemas.types.Str;

public final class SmioFieldStr extends SmioFieldSimple {

    private Str defaultVal, value;

    public SmioFieldStr(StrFieldModel model) throws SmioError {
        super(model);
        try {
            defaultVal = null;
            if (getField().isSetDefaultVal()) {
                defaultVal = Str.Factory.newInstance();
                defaultVal.setStringValue(getField().getDefaultVal());
            }
            value = Str.Factory.newInstance();
        } catch (Throwable e) {
            throw new SmioError(initError, e, getModel().getName());
        }
    }

    @Override
    public StrField getField() {
        return (StrField)getModel().getField();
    }

    public StrCharSetDef.Enum getCharSet() {
        StrCharSetDef.Enum charSet;
        if (getField().isSetCharSetType()) {
            charSet = StrCharSetDef.Enum.forString(getField().getCharSetType());
        } else {
            final String cs = getModel().getCharSetType();
            if (cs != null) {
                charSet = StrCharSetDef.Enum.forString(cs);
            } else {
                charSet = StrCharSetDef.NONE;
            }
        }
        return charSet;
    }

    public String getCharSetExp() {
        String exp;
        StrCharSetDef.Enum cs = getCharSet();
        if (cs == StrCharSetDef.USER) {
            if (getField().isSetCharSetType()) {
                exp = getField().getCharSetExp();
            } else {
                exp = getModel().getCharSetExp();
            }
        } else {
            exp = null;
        }
        return exp;
    }

    private void checkCharSet(String val) throws SmioException {
        if (val != null) {
            final StrCharSetDef.Enum cs = getCharSet();
            if (cs == StrCharSetDef.USER) {
                final String exp = getCharSetExp();
                if (exp != null && !exp.isEmpty() && !val.matches(exp)) {
                    throw new SmioException("Value char set doesn't match expression '" + exp + "'");
                }
            }
            if (cs == StrCharSetDef.XML) {
                for (int i = 0; i < val.length(); i++) {
                    final char c = val.charAt(i);
                    if (XmlUtils.isBadXmlChar(c)) {
                        throw new SmioException("Value char set doesn't match to xml format");
                    }
                }
            }
        }
    }

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        String val = parseToString(ids);
        if (val == null) {
            obj.setNil();
            return;
        }
        checkCharSet(val);
        Str str = Str.Factory.newInstance();
        str.setStringValue(val);
        obj.set(str);
    }

    @Override
    public void readAsDbfObject(XmlObject obj, Object object) throws SmioException, IOException{
        try {
            value.setStringValue((String)object);
            obj.set(value);
        } catch (Throwable e) {
            throw new SmioException(readError, e, getModel().getName());
        }
    }

    @Override
    public Object writeAsDbfObject(final XmlObject o) throws SmioException, IOException{
        try {
            if (o == null) {
                return null;
            } else {
                value.set(o);
                value = Str.Factory.parse(o.getDomNode());
                return value.getStringValue();
            }
        } catch (Throwable e) {
            throw new SmioException(writeError, e, getModel().getName());
        }
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        Str v = null;
        try {
            v = Str.Factory.parse(obj.getDomNode());
        } catch (XmlException ex) {
            Logger.getLogger(SmioFieldStr.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String val = v.getStringValue();
        checkCharSet(val);
        return mergeFromString(val);
    }

    @Override
    public XmlObject getDefaultVal() {
        return defaultVal;
    }

    @Override
    protected void setXmlObjectToNil(XmlObject obj) {
        XmlCursor c = obj.newCursor();
        c.setTextValue("");
        c.dispose();
    }
}