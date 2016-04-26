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

public final class FileStorage extends StgMedium{
	
	private String cachedFileName;
	
	
	FileStorage(final long pointer, final String fileName){
		super(pointer, false, fileName!=null && !fileName.isEmpty());
		cachedFileName = fileName;
	}
	
	FileStorage(final long pointer, final boolean deleteOnClose){
		super(pointer, deleteOnClose, false);
	}
	
	public String getFileName(){
		if (cachedFileName==null){
			cachedFileName = getFileName(getNativePointer());
		}
		return cachedFileName;
	}
}