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

public class WiaDeviceDescription {
    
    private final String deviceId;
    private final WiaRootItem rootItem;
    
    private WiaDeviceDescription(final String deviceId, final long rootItemPointer){
        this.deviceId = deviceId;
        rootItem = rootItemPointer==0 ? null : new WiaRootItem(rootItemPointer);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public WiaRootItem getRootItem() {
        return rootItem;
    }            
}
