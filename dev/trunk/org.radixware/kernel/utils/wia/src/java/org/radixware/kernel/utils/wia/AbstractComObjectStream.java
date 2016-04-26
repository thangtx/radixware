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

public abstract class AbstractComObjectStream {

	private long nativePointer;
	private final UUID objectIid;
	
	protected AbstractComObjectStream(final long pointer, final UUID iid){
		nativePointer = pointer;
		objectIid = iid;
	}
		
	public ComObject readObjectAndRelease() throws ComException{
		if (nativePointer==0){
			throw new IllegalStateException("This stream was released");
		}
		final long objPointer = readObject(nativePointer, "{"+objectIid.toString()+"}");
		if (objPointer==0){
			return null;
		}
		final ComObject result = wrapObject(objPointer, objectIid);	
		nativePointer = 0;
		return result;
	}
	
	protected abstract ComObject wrapObject(final long nativePointer, final UUID iid);
	

	private static native long readObject(final long pointer, final String iid) throws ComException;	
}