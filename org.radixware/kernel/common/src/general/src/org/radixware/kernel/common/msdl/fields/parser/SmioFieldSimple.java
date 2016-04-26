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
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.SimpleFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.msdl.EncodingDef;
import org.radixware.schemas.msdl.SimpleField;

public abstract class SmioFieldSimple extends SmioField {

    private byte[] nullIndicator;
    private SmioCoder coder = null;
    protected boolean nullable;
    private EncodingDef.Enum encodingDef = null;

    public SmioFieldSimple(SimpleFieldModel model) throws SmioError {
        super(model);
        try {
            if (!(this instanceof SmioFieldBCH)) {
                String encoding = getField().isSetEncoding() ? getField().getEncoding() : getModel().getEncoding();
                if (encoding != null) {
                    //isHex = encoding.equalsIgnoreCase(EncodingDef.HEX.toString());
                    coder = new SmioCoder(encoding);
                }
            }
            nullIndicator = getField().isSetNullIndicator() ? getField().getNullIndicator() : getModel().getNullIndicator(true);
            nullable = getField().isSetIsNilable() && getField().getIsNilable();
        }
        catch (Throwable e) {
            throw new SmioError(initError, e, getModel().getName());
        }
    }

    public abstract XmlObject getDefaultVal();

    protected ByteBuffer getNullValue() throws SmioException {
        
        ByteBuffer row;
        XmlObject defaultVal = getDefaultVal();
        
        if(this instanceof SmioFieldSigned) {
            SmioFieldSigned signed = (SmioFieldSigned)this;
            signed.sign = null;
        }
        
        if (defaultVal == null) {
            if (nullIndicator == null)
                row = ByteBuffer.wrap(new byte[]{});
            else
                row = ByteBuffer.wrap(nullIndicator);
        }
        else
            row = mergeField(defaultVal);
        return row;
    }

    @Override
    protected ByteBuffer getFieldRowData(XmlObject obj) throws SmioException {
        XmlObject field = null;
        ByteBuffer row = null;
        if (obj == null) {
            row = getNullValue();
        }
        else {
            XmlObject[] arr = obj.selectChildren(namespace,elementName);
            if (arr.length > 0) {
                field = arr[0];
                if (field.isNil()) {
                    row = getNullValue();
                }
                else {
                    row = mergeField(field);
                }
            }
            else {
                row = getNullValue();
            }
        }
        return row;
    }

    public EncodingDef.Enum getEncoding() {
        if (encodingDef == null) {
            final String encoding = getField().isSetEncoding() ? getField().getEncoding() : getModel().getEncoding();
            encodingDef = EncodingDef.Enum.forString(encoding);
        }
        return encodingDef;
    }

    @Override
    public SimpleField getField() {
        return (SimpleField)getModel().getField();
    }

    public byte[] getNullIndicator() {
        return nullIndicator;
    }

    public SmioCoder getCoder() throws SmioException {
        if (coder == null) {
            if (!(this instanceof SmioFieldBCH)) {
                String encoding = getField().isSetEncoding() ? getField().getEncoding() : getModel().getEncoding();
                if (encoding != null) {
                    //isHex = encoding.equalsIgnoreCase(EncodingDef.HEX.toString());
                    coder = new SmioCoder(encoding);
                } else {
                    throw new SmioException("Field encoding is not defined");
                }
            }
        }
        return coder;
    }

    private boolean checkForNull(ByteBuffer bf) {
//        if (nullIndicator != null && nullIndicator.length > 0) {
        if (nullIndicator != null && nullIndicator.length == bf.limit()) {
            for (int i=0; i<nullIndicator.length; i++) {
                int idx = bf.position() + i;
                if (/*idx >= bf.limit() || */nullIndicator[i] != bf.get(idx))
                    return false;
            }
            return true;
        }
        return false;
    }

    protected String parseToString(IDataSource ids) throws SmioException, IOException {
        ByteBuffer bf = ids.getByteBuffer();
        if (checkForNull(bf))
            return null;
        return getCoder().decode(bf);
    }

    protected ByteBuffer mergeFromString(String string) throws SmioException {
        ByteBuffer bf = ByteBuffer.wrap(getCoder().encode(string));
        return bf;
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        try {
            getCoder();
        } catch (SmioException ex) {
            handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "error: '" + ex.getMessage() + "'"));
        }
    }
}
