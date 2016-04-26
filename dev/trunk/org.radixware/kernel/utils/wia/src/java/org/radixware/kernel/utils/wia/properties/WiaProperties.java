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
 
package org.radixware.kernel.utils.wia.properties;

import org.radixware.kernel.utils.wia.ComException;
import org.radixware.kernel.utils.wia.ComEnumeration;
import org.radixware.kernel.utils.wia.AbstractComObjectStream;
import org.radixware.kernel.utils.wia.IComObjectStream;
import org.radixware.kernel.utils.wia.EWiaIid;
import java.util.UUID;

public final class WiaProperties extends ComEnumeration<ComProperty>{	

	private static final class WiaPropertiesStream extends AbstractComObjectStream implements IComObjectStream<WiaProperties>{
		
		private final int bufferSize;
		
		public WiaPropertiesStream(final long pointer, final int bufferSize){
			super(pointer, EWiaIid.IID_IEnumSTATPROPSTG.getGuid());
			this.bufferSize = bufferSize;
		}
		
		@Override
		public WiaProperties readObjectAndRelease() throws ComException{
			return (WiaProperties)super.readObjectAndRelease();
		}
		
		@Override
		protected WiaProperties wrapObject(final long pointer, final UUID iid){
			return new WiaProperties(pointer,bufferSize);
		}	
	}
		
	WiaProperties(final long pointer){
		super(pointer,EWiaIid.IID_IEnumSTATPROPSTG.getGuid());
	}
	
	WiaProperties(final long pointer, final int bufferSize){
		super(pointer, EWiaIid.IID_IEnumSTATPROPSTG.getGuid(), bufferSize);
	}
	
	@Override
	public ComEnumeration<ComProperty> copy() throws ComException{
		final long propertiesPointer = clone(getNativePointer());
		return new WiaProperties(propertiesPointer, getBufferSize());
	}
	
	@Override
	public ComProperty[] next(int count) throws ComException{
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
	
	public IComObjectStream<WiaProperties> writeToStream() throws ComException{
		final long pointer = marshalObject();
		return pointer==0 ? null : new WiaPropertiesStream(pointer, getBufferSize());
	}

	private static native long clone(long nativePointer) throws ComException;
	private static native ComProperty[] next(long nativePointer, int count) throws ComException;
	private static native void reset(long nativePointer) throws ComException;
	private static native void skip(long nativePointer, long count) throws ComException;
}