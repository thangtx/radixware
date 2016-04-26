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

package org.radixware.kernel.common.types;

import java.nio.ByteBuffer;

import java.util.Arrays;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.utils.Base64;

public final class Bin {
    private ByteBuffer byteBuffer;

    public Bin(final byte[] b ) throws IllegalArgumentError {
        if (b == null)
            throw new IllegalArgumentError( "Invalid Bin source "  );
        byteBuffer = ByteBuffer.allocate( b.length );
        byteBuffer.put(b);
        byteBuffer = byteBuffer.asReadOnlyBuffer();
    }
    public Bin(final ByteBuffer b ) throws IllegalArgumentError {
        if (b == null)
            throw new IllegalArgumentError( "Invalid Bin source "  );
        byteBuffer = ByteBuffer.allocate( b.limit() );
        byteBuffer.put(b);
        byteBuffer = byteBuffer.asReadOnlyBuffer();
    }
    public Bin(final String dataInBase64 ) throws IllegalArgumentError {
        if (dataInBase64 == null)
            throw new IllegalArgumentError( "Invalid Bin source "  );
        byte b[] = Base64.decode( dataInBase64 );
        byteBuffer = ByteBuffer.allocate( b.length );
        byteBuffer.put( b );                   
        byteBuffer = byteBuffer.asReadOnlyBuffer();
    }
    

    private Bin() {}
    public static Bin wrap(final byte[] b ) {
        Bin bin = new Bin();
        bin.byteBuffer = ByteBuffer.wrap( b );
        bin.byteBuffer.position(b.length);
        bin.byteBuffer = bin.byteBuffer.asReadOnlyBuffer();
        return bin;
    }
    
    /**
     * @return ����� ����� �������, ���������� ������������� 
     */
    public final byte[] get() {
        byte[] b = new byte[ byteBuffer.limit() ];
        byteBuffer.rewind();
        byteBuffer.get( b );
        byteBuffer.position(b.length); 
        return b;
    }     
    public final byte getByte(final int index) {
        return byteBuffer.get( index );        
    }    
     /**
     * @return ����� ����� �������, ���������� ������������� ��� ������ � Bas64 
     */
    public final String getAsBase64() {
        return Base64.encode( get() );
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Bin other = (Bin) obj;
		return Arrays.equals(this.get(), other.get());
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + (this.byteBuffer != null ? this.byteBuffer.hashCode() : 0);
		return hash;
	}

}
