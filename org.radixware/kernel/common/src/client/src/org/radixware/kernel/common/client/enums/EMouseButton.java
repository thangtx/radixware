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

import java.awt.event.InputEvent;
import java.util.EnumSet;


public enum EMouseButton {
    
    LEFT(InputEvent.BUTTON1_DOWN_MASK),MIDDLE(InputEvent.BUTTON2_DOWN_MASK),RIGHT(InputEvent.BUTTON3_DOWN_MASK);
    
    private final int awtMask;
    
    private EMouseButton(final int awtMask){
        this.awtMask = awtMask;
    }
    
    public static EnumSet<EMouseButton> fromAwtBitMask(final int bitMask){
        final EnumSet<EMouseButton> modifiers = EnumSet.noneOf(EMouseButton.class);
        for (EMouseButton modifier: values()){
            if ((bitMask & modifier.awtMask)!=0){
                modifiers.add(modifier);
            }
        }
        return modifiers;
    }
}
