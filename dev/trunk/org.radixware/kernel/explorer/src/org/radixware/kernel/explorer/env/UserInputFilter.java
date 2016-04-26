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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMouseEvent;
import java.util.EnumSet;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;


public final class UserInputFilter extends QEventFilter{
    
    private final static EnumSet<QEvent.Type> MOUSE_EVENTS = EnumSet.of(QEvent.Type.MouseButtonPress, 
                                                                        QEvent.Type.MouseButtonDblClick, 
                                                                        QEvent.Type.MouseButtonRelease);
    private final static EnumSet<QEvent.Type> PROHIBITED_EVENTS = EnumSet.of(QEvent.Type.Shortcut,
                                                                             QEvent.Type.ShortcutOverride,
                                                                             QEvent.Type.Wheel,
                                                                             QEvent.Type.KeyPress,
                                                                             QEvent.Type.KeyRelease,
                                                                             QEvent.Type.Enter,
                                                                             QEvent.Type.Leave);
    
    public UserInputFilter(final QObject parent){
        this(parent,false,true);
    }
    
    public UserInputFilter(final QObject parent, final boolean strongInputFilter, final boolean filterCloseEvent){
        super(parent);
        final EnumSet<QEvent.Type> prohibitedEvents = EnumSet.copyOf(PROHIBITED_EVENTS);
        final EnumSet<QEvent.Type> processableEvents = EnumSet.noneOf(QEvent.Type.class);
        if (strongInputFilter){
            prohibitedEvents.addAll(MOUSE_EVENTS);
        }else{
            processableEvents.addAll(MOUSE_EVENTS);
        }
        if (filterCloseEvent){
            processableEvents.add(QEvent.Type.Close);
        }        
        setProhibitedEventTypes(prohibitedEvents);
        if (!processableEvents.isEmpty()){
            setProcessableEventTypes(processableEvents);
        }
    }

    @Override
    @SuppressWarnings({"null", "ConstantConditions"})
    public boolean eventFilter(final QObject target, final QEvent event) {
        final QEvent.Type eventType = event==null ? null : event.type();
        final boolean isCloseDialogEvent = eventType == QEvent.Type.Close && target instanceof ExplorerDialog;
        if (isCloseDialogEvent){
            event.ignore();
            return true;
        }
        if (event instanceof QMouseEvent && target!=null){//RADIX-7253
            final QMouseEvent mouseEvent = (QMouseEvent)event;
            final boolean isLeftButton = mouseEvent.buttons().isSet(Qt.MouseButton.LeftButton);
            if ((eventType==QEvent.Type.MouseButtonPress || eventType==QEvent.Type.MouseButtonDblClick) && isLeftButton){
                QApplication.postEvent(target, new FilteredMouseEvent(eventType));                
            }else if (eventType==QEvent.Type.MouseButtonRelease){
                return !(target instanceof com.trolltech.qt.gui.QScrollBar);//RADIX-4783
            }
            return true;
        }
        return false;
    }
}