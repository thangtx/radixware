/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.client.enums;

import java.awt.Event;
import java.util.EnumSet;


public enum EKeyboardModifier {
    
    SHIFT(Event.SHIFT_MASK),CTRL(Event.CTRL_MASK),META(Event.META_MASK),ALT(Event.ALT_MASK);    
    
    private final int awtMask;
    
    private EKeyboardModifier(final int awtMask){
        this.awtMask = awtMask;                
    }
    
    public static EnumSet<EKeyboardModifier> fromAwtBitMask(final int bitMask){
        final EnumSet<EKeyboardModifier> modifiers = EnumSet.noneOf(EKeyboardModifier.class);
        for (EKeyboardModifier modifier: values()){
            if ((bitMask & modifier.awtMask)!=0){
                modifiers.add(modifier);
            }
        }
        return modifiers;
    }
}
