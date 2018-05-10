/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.rwt.events;

import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;


public abstract class InputEvent extends HtmlEvent{
    
    private final EnumSet<EKeyboardModifier> modifiers; 
    private final int button;
    
    protected InputEvent(final int button, final EnumSet<EKeyboardModifier> keyboardModifiers){
        this.button = button;
        modifiers = keyboardModifiers==null ? EnumSet.noneOf(EKeyboardModifier.class) : EnumSet.copyOf(keyboardModifiers);
    }
    
    public final int getButton(){
        return button;
    }
    
    public EnumSet<EKeyboardModifier> getKeyboardModifiers(){
        return EnumSet.copyOf(modifiers);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Button is ").append(button);
        sb.append(", modifiers are ").append(getKeyboardModifiers().toString());
        return sb.toString();
    }    
}
