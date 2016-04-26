/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.enums;

import java.util.EnumSet;


public enum EReferencedObjectActions {
    ACCESS(0x1),
    VIEW(0x2),    
    MODIFY(0x4),
    DELETE(0x8);
    
    private final int val;
    
    private EReferencedObjectActions(final int bitVal){
        val = bitVal;
    }
    
    public static int toBitMask(final EnumSet<EReferencedObjectActions> actions){
        int result = 0;
        for (EReferencedObjectActions action : actions) {
            result |= action.val;
        }
        return result;
    }
    
    public static EnumSet<EReferencedObjectActions> fromBitMask(final int bitMask){
        final EnumSet<EReferencedObjectActions> enumSet = EnumSet.noneOf(EReferencedObjectActions.class);

        for (EReferencedObjectActions action : values()) {
            if ((bitMask & action.val) != 0) {
                enumSet.add(action);
            }
        }

        return enumSet;
    }        
}
