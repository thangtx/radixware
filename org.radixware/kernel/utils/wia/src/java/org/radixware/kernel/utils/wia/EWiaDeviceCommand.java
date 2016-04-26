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

public enum EWiaDeviceCommand {

	WIA_CMD_SYNCHRONIZE(0x9b26b7b2acad11d2l, 0xa09300c04f72dc3cl),
	WIA_CMD_TAKE_PICTURE(0xaf933cacacad11d2l, 0xa09300c04f72dc3cl),
	WIA_CMD_DELETE_ALL_ITEMS(0xe208c170acad11d2l, 0xa09300c04f72dc3cl),
	WIA_CMD_CHANGE_DOCUMENT(0x04e725b0acae11d2l, 0xa09300c04f72dc3cl),
	WIA_CMD_UNLOAD_DOCUMENT(0x1f3b3d8eacae11d2l, 0xa09300c04f72dc3cl),
	WIA_CMD_DIAGNOSTIC(0x10ff52f5de044cf0l, 0xa5ad691f8dce0141l),
	WIA_CMD_FORMAT(0xc3a693aaf7884d34l, 0xa5b0be7190759a24l),
	UNKNOWN();
	
	private final UUID guid;
	
	private EWiaDeviceCommand(){
		guid = null;
	}
	
	private EWiaDeviceCommand(final long mostSigBits, final long leastSigBits) {
		guid = new UUID(mostSigBits, leastSigBits);
	}
	
    public UUID getGuid() {
        return guid;
    }

    public static EWiaDeviceCommand fromGuid(final UUID guid) {
        for (EWiaDeviceCommand command : EWiaDeviceCommand.values()) {
            if (command.guid!=null && command.guid.equals(guid)) {
                return command;
            }
        }
        return UNKNOWN;
    }
}