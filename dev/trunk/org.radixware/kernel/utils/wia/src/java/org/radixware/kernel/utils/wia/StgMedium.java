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

public abstract class StgMedium implements AutoCloseable{

	public static final class Factory{
	
		private Factory(){
		}
		
		public static FileStorage createFileStorage(final String fileName){
			final long pointer = StgMedium.createFileStorage(fileName!=null && fileName.isEmpty() ? null : fileName);
			return new FileStorage(pointer,fileName);
		}
		
		public static FileStorage createFileStorage(final boolean deleteOnClose){
			final long pointer = StgMedium.createFileStorage(null);
			return new FileStorage(pointer,deleteOnClose);
		}
	}

	private long nativePointer;
	private final boolean releaseStorage;
	private final boolean deleteFileNameString;
	
	StgMedium(final long pointer, final boolean releaseStorage, final boolean deleteFileNameString){
		nativePointer = pointer;
		this.releaseStorage = releaseStorage;
		this.deleteFileNameString = deleteFileNameString;
	}
		
	@Override
	public void close(){
		if (nativePointer!=0){
			release(nativePointer, releaseStorage, deleteFileNameString);
			nativePointer = 0;
		}
	}
	
	long getNativePointer(){
		return nativePointer;
	}
	
	private static native long createFileStorage(final String fileName);
	private static native void release(final long pointer, final boolean releaseStorage, final boolean deleteFileNameString);
	static native String getFileName(final long pointer);
}