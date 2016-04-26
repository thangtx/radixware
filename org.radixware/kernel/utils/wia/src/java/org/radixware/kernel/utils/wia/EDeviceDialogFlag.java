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

public enum EDeviceDialogFlag {

    DEFAULT(0), FORCE_SHOW(1), SINGLE_IMAGE(2), COMMON_UI(4);
	
    private final int value;
	
    private EDeviceDialogFlag(final int val){
        value = val;
    }
    
    public int getValue(){
        return value;
    }
    
    public static int toBitMask(final EnumSet<EDeviceDialogFlag> flags) {
        int result = 0;
        if (flags != null && !flags.isEmpty()) {
            for (EDeviceDialogFlag flag : flags) {
                result |= flag.getValue();
            }
        }
        return result;
    }	
}
