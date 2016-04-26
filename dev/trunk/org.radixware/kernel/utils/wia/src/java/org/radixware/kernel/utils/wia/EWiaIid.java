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

public enum EWiaIid {

	IID_IWiaItem (0x4db1ad10339111d2l, 0x9a3300c04fa36145l),
	IID_IWiaDataTransfer (0xa6cef998a5b011d2l, 0xa08f00c04f72dc3cl),	
	IID_IEnumWIA_DEV_CAPS (0x1fcc4287aca611d2l, 0xa09300c04f72dc3cl),	
	IID_IWiaDevMgr	(0x5eb2502a8cf111d1l, 0xbf920060081ed811l),
	IID_IEnumWIA_DEV_INFO (0x5e38b83c8cf111d1l, 0xbf920060081ed811l),
	IID_IEnumWiaItem (0x5e8383fc339111d2l, 0x9a3300c04fa36145l),
	IID_IWiaPropertyStorage (0x98B5E8A029CC491al, 0xAAC0E6DB4FDCCEB6l),
	IID_IEnumSTATPROPSTG (0x0000013900000000l, 0xC000000000000046l);	   
	
	private final UUID guid;

    private EWiaIid(final long mostSigBits, final long leastSigBits) {
        guid = new UUID(mostSigBits,leastSigBits);
    }

    public UUID getGuid() {
        return guid;
    }
}
