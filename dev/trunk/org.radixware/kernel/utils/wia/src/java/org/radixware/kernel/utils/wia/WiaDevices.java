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

import org.radixware.kernel.utils.wia.properties.WiaPropertyStorage;
import java.util.UUID;

public final class WiaDevices extends ComEnumeration<WiaPropertyStorage>{

	private static final class WiaDevicesStream extends AbstractComObjectStream implements IComObjectStream<WiaDevices>{
		
		private final int bufferSize;
		
		public WiaDevicesStream(final long pointer, final int bufferSize){
			super(pointer, EWiaIid.IID_IEnumWIA_DEV_INFO.getGuid());
			this.bufferSize = bufferSize;
		}
		
		@Override
		public WiaDevices readObjectAndRelease() throws ComException{
			return (WiaDevices)super.readObjectAndRelease();
		}
		
		@Override
		protected WiaDevices wrapObject(final long pointer, final UUID iid){
			return new WiaDevices(pointer,bufferSize);
		}	
	}

	WiaDevices(final long pointer){
		super(pointer, EWiaIid.IID_IEnumWIA_DEV_INFO.getGuid());
	}
	
	WiaDevices(final long pointer, final int bufferSize){
		super(pointer, EWiaIid.IID_IEnumWIA_DEV_INFO.getGuid(), bufferSize);
	}
	
	@Override
	public ComEnumeration<WiaPropertyStorage> copy() throws ComException{
		final long propertiesPointer = clone(getNativePointer());
		return new WiaDevices(propertiesPointer, getBufferSize());
	}
	
	public long getCount() throws ComException{
		return count(getNativePointer());
	}
	
	@Override
	public WiaPropertyStorage[] next(int count) throws ComException{
		if (count<0){
			throw new IllegalArgumentException("count cannot be negative value");
		}
		return next(getNativePointer(), count);
	}
	
	@Override
	public void reset() throws ComException{
		reset(getNativePointer());
	}
	
	@Override
	public void skip(long count) throws ComException{
		if (count<0){
			throw new IllegalArgumentException("count cannot be negative value");
		}	
		skip(getNativePointer(), count);
	}
	
	@Override
	public IComObjectStream<WiaDevices> writeToStream() throws ComException{
		final long pointer = marshalObject();
		return pointer==0 ? null : new WiaDevicesStream(pointer, getBufferSize());
	}	

	private static native long clone(long nativePointer) throws ComException;
	private static native long count(long nativePointer) throws ComException;
	private static native WiaPropertyStorage[] next(long nativePointer, int count) throws ComException;
	private static native void reset(long nativePointer) throws ComException;
	private static native void skip(long nativePointer, long count) throws ComException;	

}