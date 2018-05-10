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


public class SeparatedFieldListStart implements ISeparatedFieldList {

    private final byte separatorStart;
    private final Byte shield;

    public SeparatedFieldListStart(byte separator, Byte shield) {
        this.separatorStart = separator;
        this.shield = shield;
    }

    @Override
    public void mergeField(ExtByteBuffer bf, ByteBuffer field) {
        bf.extPut(separatorStart);
        
        while (field.hasRemaining()) {
            byte b = field.get();
            if (b == separatorStart && shield != null)
                bf.extPut(shield);
            bf.extPut(b);
        }
    }

    @Override
    public IDataSource parseField(IDataSource ids) throws IOException {
        ExtByteBuffer exbf = new ExtByteBuffer();
        while (ids.hasAvailable()) {
            if (ids.get() == separatorStart) {
                break;
            }
        }
        
        while (ids.hasAvailable()) {
            byte b  = ids.get();
            if (b == separatorStart) {
                ids.prepend(new DataSourceByteBuffer(new byte[] {separatorStart}));
                break;
            }
            else {
                if (shield != null && b == shield) {
                    b = ids.get();
                    if (b!=separatorStart) {
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
        return new byte[] {separatorStart};
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
            if (b == separatorStart) {
                return true;
            }
        }
        return false;
    }
}
