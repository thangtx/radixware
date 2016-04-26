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

public enum EComPropValType {

    VT_I4(3,"4-byte signed integer"), 
	VT_R4(4,"32-bit IEEE floating point"), 
	VT_BSTR(8,"null-terminated Unicode string"), 
	VT_UI2(18,"2-byte unsigned integer"), 
	VT_CLSID(72,"globally unique identifier");
	
    private final int code;
	private final String description;

    private EComPropValType(final int code, final String desc) {
        this.code = code;
		description = desc;
    }

    public int getCode() {
        return code;
    }
	
	public String getDescription(){
		return description;
	}
}
