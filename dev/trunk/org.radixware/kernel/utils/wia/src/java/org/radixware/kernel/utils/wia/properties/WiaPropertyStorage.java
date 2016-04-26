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

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import org.radixware.kernel.utils.wia.ComException;
import org.radixware.kernel.utils.wia.ComObject;
import org.radixware.kernel.utils.wia.AbstractComObjectStream;
import org.radixware.kernel.utils.wia.IComObjectStream;
import org.radixware.kernel.utils.wia.EWiaIid;

public class WiaPropertyStorage extends ComObject {

	public static enum ECommitFlag{
	
		STGC_DEFAULT(0),
		STGC_OVERWRITE(1),
		STGC_ONLYIFCURRENT(2);
		
		private final int value;
		private ECommitFlag(final int val){
			this.value = val;
		}
		
		public int getValue(){
			return value;
		}
	}
	
	private static final class WiaPropertyStorageStream extends AbstractComObjectStream implements IComObjectStream<WiaPropertyStorage>{
	
		public WiaPropertyStorageStream(final long pointer){
			super(pointer, EWiaIid.IID_IWiaPropertyStorage.getGuid());
		}
		
		@Override
		public WiaPropertyStorage readObjectAndRelease() throws ComException{
			return (WiaPropertyStorage)super.readObjectAndRelease();
		}
		
		@Override
		protected WiaPropertyStorage wrapObject(final long pointer, final UUID iid){
			return new WiaPropertyStorage(pointer);
		}
	}	

    private WiaPropertyStorage(final long pointer) {
        super(pointer, EWiaIid.IID_IWiaPropertyStorage.getGuid());
    }
	
	private static ComProperty[] asArray(final Iterable<ComProperty> properties){
		if (properties==null){
			return null;
		}else{
			final List<ComProperty> propertiesList = new ArrayList<>();
			for (ComProperty property: properties){
				propertiesList.add(property);
			}
			final ComProperty[] arrProps = new ComProperty[propertiesList.size()];
			for (int i = 0, count = arrProps.length; i < count; i++) {
				arrProps[i] = propertiesList.get(i);
			}
			return arrProps;
		}		
	}

    public void writeMultiple(final Iterable<ComProperty> properties) throws ComException {
		ComProperty[] arrProps = asArray(properties);
        if (arrProps != null && arrProps.length>0) {
            writeMultiple(getNativePointer(), arrProps);
        }
    }
	
    public boolean readMultiple(final Iterable<ComProperty> properties) throws ComException {
		ComProperty[] arrProps = asArray(properties);
		if (arrProps != null && arrProps.length>0) {
			return readMultiple(getNativePointer(), arrProps);
		}else{
			return false;
		}
    }
	
	public WiaProperties loadProperties() throws ComException {
		final long propertiesNativePointer = getProperties(getNativePointer());
		return new WiaProperties(propertiesNativePointer,50);
	}
	
	public WiaProperties loadProperties(final int bufferSize) throws ComException {
		final long propertiesNativePointer = getProperties(getNativePointer());
		return new WiaProperties(propertiesNativePointer,bufferSize);
	}	
	
	public int getPropertiesCount() throws ComException{
		return getCount(getNativePointer());
	}
	
	public void commit(final ECommitFlag commitFlag) throws ComException{
		commit(getNativePointer(), commitFlag.getValue());
	}
	
	public void revert() throws ComException{
		revert(getNativePointer());
	}
	
	@Override
	public IComObjectStream<WiaPropertyStorage> writeToStream() throws ComException{
		final long pointer = marshalObject();
		return pointer==0 ? null : new WiaPropertyStorageStream(pointer);
	}	
		
	private static native void writeMultiple(final long pointer, ComProperty[] props) throws ComException;
	private static native boolean readMultiple(final long pointer, ComProperty[] props) throws ComException;
	private static native long getProperties(final long pointer) throws ComException;
	private static native int getCount(final long pointer) throws ComException;
	private static native void commit(final long pointer, final int commitFlag) throws ComException;
	private static native void revert(final long pointer) throws ComException;
	
}
