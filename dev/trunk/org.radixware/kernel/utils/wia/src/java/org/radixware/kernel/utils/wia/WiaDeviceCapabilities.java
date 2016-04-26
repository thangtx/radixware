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

public final class WiaDeviceCapabilities extends ComEnumeration<WiaDeviceCapability>{

	private static final class WiaDeviceCapabilitiesStream extends AbstractComObjectStream implements IComObjectStream<WiaDeviceCapabilities>{
		
		private final int bufferSize;
		
		public WiaDeviceCapabilitiesStream(final long pointer, final int bufferSize){
			super(pointer, EWiaIid.IID_IEnumWIA_DEV_CAPS.getGuid());
			this.bufferSize = bufferSize;
		}
		
		@Override
		public WiaDeviceCapabilities readObjectAndRelease() throws ComException{
			return (WiaDeviceCapabilities)super.readObjectAndRelease();
		}
		
		@Override
		protected WiaDeviceCapabilities wrapObject(final long pointer, final UUID iid){
			return new WiaDeviceCapabilities(pointer,bufferSize);
		}	
	}

	WiaDeviceCapabilities(final long pointer){
		super(pointer, EWiaIid.IID_IEnumWIA_DEV_CAPS.getGuid());
	}
	
	WiaDeviceCapabilities(final long pointer, final int bufferSize){
		super(pointer, EWiaIid.IID_IEnumWIA_DEV_CAPS.getGuid(), bufferSize);
	}
	
	@Override
	public ComEnumeration<WiaDeviceCapability> copy() throws ComException{
		final long propertiesPointer = clone(getNativePointer());
		return new WiaDeviceCapabilities(propertiesPointer, getBufferSize());
	}
	
	public long getCount() throws ComException{
		return count(getNativePointer());
	}
	
	@Override
	public WiaDeviceCapability[] next(int count) throws ComException{
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
	public IComObjectStream<WiaDeviceCapabilities> writeToStream() throws ComException{
		final long pointer = marshalObject();
		return pointer==0 ? null : new WiaDeviceCapabilitiesStream(pointer, getBufferSize());
	}	

	private static native long clone(long nativePointer) throws ComException;
	private static native long count(long nativePointer) throws ComException;
	private static native WiaDeviceCapability[] next(long nativePointer, int count) throws ComException;
	private static native void reset(long nativePointer) throws ComException;
	private static native void skip(long nativePointer, long count) throws ComException;
}