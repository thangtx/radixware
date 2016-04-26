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

public final class WiaDataTransfer extends ComObject {

	private static final class WiaDataTransferStream extends AbstractComObjectStream implements IComObjectStream<WiaDataTransfer>{
		public WiaDataTransferStream(final long pointer){
			super(pointer, EWiaIid.IID_IWiaDataTransfer.getGuid());
		}
		
		@Override
		public WiaDataTransfer readObjectAndRelease() throws ComException{
			return (WiaDataTransfer)super.readObjectAndRelease();
		}
		
		@Override
		protected WiaDataTransfer wrapObject(final long pointer, final UUID iid){
			return new WiaDataTransfer(pointer);
		}	
	}

    WiaDataTransfer(final long nativePointer) {
        super(nativePointer, EWiaIid.IID_IWiaDataTransfer.getGuid());
    }
	
	public void idtGetData(final StgMedium stgMedium, final WiaDataCallback callback) throws ComException{
		idtGetData(getNativePointer(), stgMedium.getNativePointer(), callback);
	}
	
	public void idtGetBandedData(final long bufferSize, final boolean doubleBuffer, final WiaDataCallback callback) throws ComException{
		idtGetBandedData(getNativePointer(), bufferSize, doubleBuffer, callback);
	}
	
	@Override
	public IComObjectStream<WiaDataTransfer> writeToStream() throws ComException{
		final long pointer = marshalObject();
		return pointer==0 ? null : new WiaDataTransferStream(pointer);
	}
	
	private static native void idtGetData(long thisPointer, long stgMediumPointer, WiaDataCallback callback) throws ComException;
	private static native void idtGetBandedData(long thisPointer, long bufferSize, boolean doubleBuffer, WiaDataCallback callback) throws ComException;
}
