/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
 
package org.radixware.kernel.utils.wia;

import java.util.UUID;

public abstract class ComObject implements AutoCloseable {

    private final long nativePointer;
	private final UUID iid;

    public ComObject(final long pointer, final UUID iid) {
        nativePointer = pointer;
		this.iid=iid;
    }
    
    protected final long getNativePointer(){
        return nativePointer;
    }
	
	public UUID getIid(){
		return iid;
	}
    
    @Override
    public void close() throws ComException {
        release(nativePointer);
    }
	
	protected final long marshalObject() throws ComException{
		return writeToStream(nativePointer, "{"+iid.toString()+"}");
	}
		
	public abstract IComObjectStream<? extends ComObject> writeToStream() throws ComException;

	private static native void release(final long pointer) throws ComException;
	private static native long writeToStream(final long pointer, final String iid) throws ComException;	
}
