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

import java.util.Iterator;
import java.util.UUID;

public abstract class ComEnumeration<T> extends ComObject implements Iterable<T>{
	
	private final class ComEnumerationIterator implements Iterator<T>{	
	
		private final int bufferSize;
		private T[] buffer;
		private int pos = -1;
		private boolean hasMore = true;
	
		public ComEnumerationIterator(final int bufferSize){		
			this.bufferSize = bufferSize;
		}
		
		private boolean readNext(){
			try{
				buffer = ComEnumeration.this.next(bufferSize);
				hasMore = buffer.length==bufferSize;
				pos = buffer.length>0 ? 0 : -1;
				return buffer.length>0;
			}catch(ComException exception){
			    buffer = null;
				hasMore = false;				
				pos = -1;
			}
			return false;
		}
	
		@Override
		public boolean hasNext(){
			return (pos>-1 && pos<buffer.length) || (hasMore && readNext());
		}
		
		@Override
		public T next(){
			if ( (pos>-1 && pos<buffer.length) || (hasMore && readNext()) ){
				return buffer[pos++];
			}
			throw new java.util.NoSuchElementException();
		}
		
		@Override
		public void remove(){
			throw new UnsupportedOperationException("remove operation is not supported");
		}
	}
	
	private final int bufferSize;
	
	protected ComEnumeration(final long nativePointer, final UUID iid, final int bufferSize){
		super(nativePointer, iid);
		this.bufferSize = bufferSize;
	}
	
	protected ComEnumeration(final long nativePointer, final UUID iid){
		this(nativePointer, iid, 10);
	}
	
	protected final int getBufferSize(){
		return bufferSize;
	}
	
	@Override
	public final ComEnumerationIterator iterator(){
		return new ComEnumerationIterator(bufferSize);
	}
	
	public abstract ComEnumeration<T> copy() throws ComException;
	public abstract T[] next(int count) throws ComException; 
	public abstract void reset() throws ComException;
	public abstract void skip(long count) throws ComException;
}