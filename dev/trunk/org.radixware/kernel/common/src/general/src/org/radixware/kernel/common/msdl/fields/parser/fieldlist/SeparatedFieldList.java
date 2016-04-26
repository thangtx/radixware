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

package org.radixware.kernel.common.msdl.fields.parser.fieldlist;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;


public class SeparatedFieldList implements IFieldList {

    byte separator;
    Byte shield;

    public SeparatedFieldList(byte separator, Byte shield) {
        this.separator = separator;
        this.shield = shield;
    }

    @Override
    public void mergeField(ExtByteBuffer bf, ByteBuffer field) {
        while (field.hasRemaining()) {
            byte b = field.get();
            if (b == separator && shield != null)
                bf.extPut(shield);
            bf.extPut(b);
        }
        bf.extPut(separator);
    }

    @Override
    public IDataSource parseField(IDataSource ids) throws IOException {
        ExtByteBuffer exbf = new ExtByteBuffer();
        while (ids.available() > 0) {
            byte b  = ids.get();
            if (b == separator) {
                break;
            }
            else {
                if (shield != null && b == shield) {
                    b = ids.get();
                    if (b!=separator) {
                        exbf.extPut(shield);
                    }
                }
                exbf.extPut(b);
            }
        }
        return new DataSourceByteBuffer(exbf.flip());
    }
    
    public byte getSeparator() {
        return separator;
    }
    
    public Byte getShield() {
        return shield;
    }
}
