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

public enum ETymed{
    
	HGLOBAL(1),
	FILE(2),
	ISTREAM(4),
	ISTORAGE(8),
	GDI(16),
	MFPICT(32),
	ENHMF(64),
	CALLBACK(128),
	MULTIPAGE_FILE(256),
	TYMED_MULTIPAGE_CALLBACK(512),
	NULL(0);
	    
    
    private final int value;
	
    private ETymed(final int val){
        value = val;
    }
    
    public int getValue(){
        return value;
    }
}
