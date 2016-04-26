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

package org.radixware.kernel.designer.common.dialogs.components.valstreditor;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public abstract class NullEditor<T> extends JTextField {
    
    protected abstract boolean isValidChar(char c);
    
    protected abstract T processString(String str);
    protected final ValueEditor<T> editor;
    
    public NullEditor(ValueEditor<T> editor) {
        super(editor.getNullIndicator());
        this.editor = editor;
    }
    
    @Override
    protected void processKeyEvent(KeyEvent ev) {
        
        int keyCode = ev.getKeyCode();
        char keyChar = ev.getKeyChar();
        T value = null;
        if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
            value = processString("");
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN
                || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT
                || keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE
                || ev.isControlDown()) { //FIXED: RADIX-2958
            super.processKeyEvent(ev);
            //игнорирую S чтобы после нажатия на Ctrl+S не вводилось S
        } else if (isValidChar(keyChar) && !(keyChar == 's' || keyChar == 'S')) {
            value = processString(String.valueOf(keyChar));            
         //  System.out.println(keyChar);
        }
        ev.consume();
        editor.setValue(value);               
    }
}
