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

import java.util.EnumSet;


public enum EWiaDataCallbackStatus {

    UNKNOWN(0x0,""), 
	TRANSFERFROMDEVICE(0x1,"Data is currently being transferred from the WIA device."),
	PROCESSINGDATA(0x2,"Data is currently being processed."),
	TRANSFERTOCLIENT(0x4,"Data is currently being transferred to the client's data buffer."),
	MASK(0x7,"");
    
    private final long status;
	private final String description;
    
    private EWiaDataCallbackStatus(final long stat, final String desc){
        status = stat;
		this.description = desc;
    }

    public long getStatus() {
        return status;
    }
    
	public String getDescription(){
		return description;
	}
    
    public static EWiaDataCallbackStatus fromLong(final long formatIdx) {
        for (EWiaDataCallbackStatus format : EWiaDataCallbackStatus.values()) {
            if (format.status == formatIdx) {
                return format;
            }
        }
        return UNKNOWN;
    }
    
}
