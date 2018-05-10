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

import com.trolltech.qt.QNoSuchEnumValueException;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;
import java.awt.event.InputEvent;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

abstract class PointerButtonAction extends InputAction<PointerInputSource> {
    
    private final static QPoint CLICK_POINT = new QPoint();
    private final Qt.MouseButton button;    
    
    public PointerButtonAction(final PointerInputSource inputSource, final int button){
        super(inputSource);
        Qt.MouseButton qtButton;
        try{
            qtButton = null;
            /* http://www.w3.org/TR/uievents/#dom-mouseevent-button */
            if(button == 0) {
                // 0 MUST indicate the primary button of the device (in general, the left button or the only button on single-button devices, used to activate a user interface control or select text) or the un-initialized value.
                qtButton = Qt.MouseButton.LeftButton;
            }
            else if(button == 1) {
                // 1 MUST indicate the auxiliary button (in general, the middle button, often combined with a mouse wheel).
                qtButton = Qt.MouseButton.MidButton;
            }
            else if(button == 2) {
                // 2 MUST indicate the secondary button (in general, the right button, often used to display a context menu).
                qtButton = Qt.MouseButton.RightButton;
            }
        }catch(QNoSuchEnumValueException exception){
            qtButton = null;
        }
        this.button = qtButton;
    }
    
    protected final Qt.MouseButton getButton(){
        return button;
    }
    
    protected final QMouseEvent createEvent(final QEvent.Type type,
                                            final QWidget target,
                                            final WebDrvInputSourceManager manager){
        final PointerInputSourceState state = getInputSource().getState();        
        CLICK_POINT.setX(state.getX());
        CLICK_POINT.setY(state.getY());        
        return new QMouseEvent(type, target.mapFromGlobal(CLICK_POINT), button, state.getButtons(), manager.getKeyboardModifiers());
    }
}
