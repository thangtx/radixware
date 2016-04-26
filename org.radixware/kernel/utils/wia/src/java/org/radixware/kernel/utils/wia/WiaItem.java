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
import java.util.EnumSet;
import java.util.UUID;

public class WiaItem extends ComObject{

	protected static abstract class AbstractWiaItemStream extends AbstractComObjectStream{
	
		private final EnumSet<EWiaItemType> itemType;		
		private final boolean isRoot;
	
		public AbstractWiaItemStream(final long pointer, final WiaItem item){
			super(pointer,EWiaIid.IID_IWiaItem.getGuid());
			itemType = item.type;
			isRoot = item instanceof WiaRootItem;
		}			
	
		@Override
		protected WiaItem wrapObject(final long nativePointer, final UUID iid){
			final WiaItem item;
			if (isRoot){
				item = new WiaRootItem(nativePointer);
			}else{
				item = new WiaItem(nativePointer);
			}
			item.type = itemType;
			return item;
		}
	}
	
	private static final class WiaItemStream extends AbstractWiaItemStream implements IComObjectStream<WiaItem>{
		public WiaItemStream(final long pointer, final  WiaItem item){
			super(pointer, item);
		}
		
		@Override		
		public WiaItem readObjectAndRelease() throws ComException{
			return (WiaItem)super.readObjectAndRelease();
		}
	}	

	private EnumSet<EWiaItemType> type;
    
    protected WiaItem(final long pointer){
        super(pointer, EWiaIid.IID_IWiaItem.getGuid());
    }    
	
    public WiaPropertyStorage openPropertyStorage() throws ComException{
        return getPropertyStorage(getNativePointer());
    }
    
    public WiaDataTransfer createDataTransfer() throws ComException{
		final long pointer = createDataTransfer(getNativePointer());
		return pointer==0 ? null : new WiaDataTransfer(pointer);
	}
	
	public WiaItems loadChildItems() throws ComException{
		final long pointer = enumChildItems(getNativePointer());
		return new WiaItems(pointer);
	}
	
	public WiaItems loadChildItems(final int bufferSize) throws ComException{
		final long pointer = enumChildItems(getNativePointer());
		return new WiaItems(pointer, bufferSize);
	}	
	
	public EnumSet<EWiaItemType> getType() throws ComException{
		if (type==null){
			final long typeMask = getType(getNativePointer());
			type = EWiaItemType.getFromBitMask(typeMask);
		}
		return type;
	}
	
	public void analyze() throws ComException{
		analyze(getNativePointer());
	}
	
	@Override
	public IComObjectStream<WiaItem> writeToStream() throws ComException{
		final long pointer = marshalObject();
		return pointer==0 ? null : new WiaItemStream(pointer, this);
	}
    
    private static native WiaPropertyStorage getPropertyStorage(long pointer) throws ComException;
	private static native long createDataTransfer(long pointer) throws ComException;
    private static native long enumChildItems(long pointer) throws ComException;	
	private static native long getType(long pointer) throws ComException;
	private static native void analyze(long pointer) throws ComException;
}