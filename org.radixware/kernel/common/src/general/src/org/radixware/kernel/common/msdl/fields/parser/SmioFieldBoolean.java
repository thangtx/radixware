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
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.BooleanFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.types.Bool;


public class SmioFieldBoolean extends SmioField {

    Bool value;
    public SmioFieldBoolean(BooleanFieldModel model) {
        super(model);
        value = Bool.Factory.newInstance();
    }

    @Override
    public void readAsDbfObject(XmlObject obj, Object object)  throws SmioException, IOException{
        try {
            value.setBooleanValue((Boolean)object);
            obj.set(value);
        } catch (Throwable e) {
            throw new SmioException(readError, e, getModel().getName());
        }
    }

    @Override
    public Object writeAsDbfObject(final XmlObject o)  throws SmioException, IOException{
        try {
            if (o == null)
                return null;
            else {
                value.set(o);
                return value.getBooleanValue();
            }
        } catch (Throwable e) {
            throw new SmioException(writeError, e, getModel().getName());
        }
    }

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
