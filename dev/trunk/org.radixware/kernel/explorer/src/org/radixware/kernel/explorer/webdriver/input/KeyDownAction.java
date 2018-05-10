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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;

final class KeyDownAction extends KeyAction {
    
    public KeyDownAction(final KeyInputSource inputSource, final char key){
        super(inputSource,key);
    }        

    @Override
    public void dispatch(final WebDrvSession session, int tickDuration, long tickStart) {
        final QWidget targetWidget = getTargetWidget();
        if (targetWidget!=null && getQtKey()!=Qt.Key.Key_unknown){
            final KeyInputSourceState state = getInputSource().getState();
            final boolean pressed = state.isPressed(getQtKey());
            state.addPressedKey(getQtKey());
            session.getInputSourceManager().addCancelAction(new KeyUpAction(getInputSource(), getKeyChar()));
            final QKeyEvent event = 
                new QKeyEvent(QEvent.Type.KeyPress, getQtKey().value(), state.getModifiers(), getKeySymbol(), pressed);
            QApplication.sendEvent(targetWidget, event);
        }  
    }
}
