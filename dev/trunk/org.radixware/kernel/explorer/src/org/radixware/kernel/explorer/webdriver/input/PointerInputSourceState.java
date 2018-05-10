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

final class PointerInputSourceState extends InputSourceState{

    private int x;
    private int y;
    private Qt.MouseButtons buttons = new Qt.MouseButtons(Qt.MouseButton.NoButton);
    
    public PointerInputSourceState(){        
    }

    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public Qt.MouseButtons getButtons(){
        return new Qt.MouseButtons(buttons.value());
    }
    
    public boolean isPressed(final Qt.MouseButton button){
        return buttons.isSet(button);
    }
    
    public void setButton(final Qt.MouseButton button){
        buttons.set(button);
    }
    
    public void clearButton(final Qt.MouseButton button){
        buttons.clear(button);
    }
    
    public void setPos(final int x, final int y){
        this.x = x;
        this.y = y;
    }      
}
