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
import java.util.Objects;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;


public class SeparatedFieldListEnd implements ISeparatedFieldList {

    private final byte separatorEnd;
    private final Byte shield;

    public SeparatedFieldListEnd(byte separator, Byte shield) {
        this.separatorEnd = separator;
        this.shield = shield;
    }

    @Override
    public void mergeField(ExtByteBuffer bf, ByteBuffer field) {
        while (field.hasRemaining()) {
            byte b = field.get();
            if (b == separatorEnd && shield != null)
                bf.extPut(shield);
            bf.extPut(b);
        }
        bf.extPut(separatorEnd);
    }

    @Override
    public IDataSource parseField(IDataSource ids) throws IOException {
        ExtByteBuffer exbf = new ExtByteBuffer();
        while (ids.hasAvailable()) {
            byte b  = ids.get();
            if (b == separatorEnd) {
                break;
            }
            else {
                if (shield != null && b == shield) {
                    b = ids.get();
                    if (b!=separatorEnd) {
                        exbf.extPut(shield);
                    }
                }
                exbf.extPut(b);
            }
        }
        return new DataSourceByteBuffer(exbf.flip());
    }
    
    @Override
    public byte[] getSeparators() {
        return new byte[] {separatorEnd};
    }
    
    @Override
    public Byte getShield() {
        return shield;
    }
    
    @Override
    public boolean hasSameShield(ISeparatedFieldList other) {
        return other != null && Objects.equals(shield, other.getShield());
    }
    
    @Override
    public boolean hasSameSeparators(ISeparatedFieldList other) {
        if (other == null) {
            return false;
        }
        for (byte b : other.getSeparators()) {
            if (b == separatorEnd) {
                return true;
            }
        }
        return false;
    }
}
