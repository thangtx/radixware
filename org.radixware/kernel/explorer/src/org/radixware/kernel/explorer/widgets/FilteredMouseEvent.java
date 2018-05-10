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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;


public final class FilteredMouseEvent extends QEvent{
    
    private final QEvent.Type eventType;
    private final QPoint pos;
    private final QPoint globalPos;
    private final Qt.MouseButton button;
    private final Qt.MouseButtons buttons;
    private final Qt.KeyboardModifiers modifiers;
    
    public FilteredMouseEvent(final QMouseEvent event){        
        super(QEvent.Type.User);        
        eventType = event.type();
        pos = event.pos();
        globalPos = event.globalPos();
        button = event.button();
        buttons = new Qt.MouseButtons(event.buttons().value());
        modifiers = new Qt.KeyboardModifiers(event.modifiers().value());
    }    
    
    public FilteredMouseEvent(final FilteredMouseEvent copy){
        super(QEvent.Type.User);        
        eventType = copy.eventType;
        pos = copy.pos;
        globalPos = copy.globalPos;
        button = copy.button;
        buttons = new Qt.MouseButtons(copy.buttons.value());
        modifiers = new Qt.KeyboardModifiers(copy.modifiers.value());
    }
    
    public FilteredMouseEvent(final FilteredMouseEvent copy, final QWidget delegate){
        super(QEvent.Type.User);        
        eventType = copy.eventType;
        pos = delegate.mapFromGlobal(copy.globalPos);
        globalPos = copy.globalPos;
        button = copy.button;
        buttons = new Qt.MouseButtons(copy.buttons.value());
        modifiers = new Qt.KeyboardModifiers(copy.modifiers.value());
    }    
    
    public QMouseEvent createFilteredEvent(){
        return new QMouseEvent(eventType, pos, button, buttons, modifiers);
    }
    
    public Qt.MouseButton getButton(){
        return button;
    }
    
    public QPoint getPos(){
        return pos;
    }
    
    public QPoint getGlobalPos(){
        return globalPos;
    }
    
    public QEvent.Type getFilteredEventType(){
        return eventType;                
    }
}
