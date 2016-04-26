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

public final class WiaDataCallbackHeader {

    private final UUID imageFormat;
    private final long bufferSize;
    private final long pageCount;
    
    private WiaDataCallbackHeader(final String guidAsStr, final long size, final long count){
        imageFormat = UUID.fromString(guidAsStr.substring(1, guidAsStr.length()-1));
        bufferSize = size;
        pageCount = count;
    }

    public UUID getImageFormatGUID() {
        return imageFormat;
    }

    public long getBufferSize() {
        return bufferSize;
    }
    
    public long getPageCount() {
        return pageCount;
    }
	
	@Override
	public String toString(){
        final StringBuilder builder = new StringBuilder();        
        builder.append("Page count: ");
        builder.append(String.valueOf(getPageCount()));
        builder.append(" Buffer size: ");
        builder.append(String.valueOf(getBufferSize()));
        builder.append(" Format GUID: ");
		final UUID formatGuid = getImageFormatGUID();
		if (formatGuid==null){
			builder.append(" Undefined");
		}else{
			builder.append(getImageFormatGUID().toString().toUpperCase());
			final EWiaImageFormat format = EWiaImageFormat.fromGuid(formatGuid);
			if (format!=null){
				builder.append(" (");
				builder.append(format.name());
				builder.append(')');
			}
		}
        return builder.toString();
	}
}