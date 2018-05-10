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

package org.radixware.kernel.explorer.webdriver.input;

import com.trolltech.qt.core.Qt;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.webdriver.WebDrvKeyEvents;

final class KeyInputSourceState extends InputSourceState{

    private final List<Qt.Key> pressedKeys = new ArrayList<>();
    private final Qt.KeyboardModifiers modifiers = new Qt.KeyboardModifiers(Qt.KeyboardModifier.NoModifier);
    
    public KeyInputSourceState(){        
    }
    
    public boolean isPressed(final Qt.Key key){
        return pressedKeys.contains(key);
    }
    
    public void addPressedKey(final Qt.Key key){
        pressedKeys.add(key);
        final Qt.KeyboardModifier modifier = WebDrvKeyEvents.getModifierForKey(key);
        if (modifier!=null){
            modifiers.set(modifier);
        }
    }
    
    public void removePressedKey(final Qt.Key key){
        for (int i=pressedKeys.size()-1; i>=0; i--){
            if (key.value()==pressedKeys.get(i).value()){
                pressedKeys.remove(i);
                break;
            }
        }
        final Qt.KeyboardModifier modifier = WebDrvKeyEvents.getModifierForKey(key);
        if (modifier!=null){
            modifiers.clear(modifier);
        }        
    }
    
    public Qt.KeyboardModifiers getModifiers(){
        return new Qt.KeyboardModifiers(modifiers.value());
    }     
}
