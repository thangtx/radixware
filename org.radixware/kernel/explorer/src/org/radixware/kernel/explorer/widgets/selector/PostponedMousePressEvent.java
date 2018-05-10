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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QMouseEvent;
import java.util.Objects;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.explorer.env.Application;


final class PostponedMousePressEvent<T extends QAbstractItemView> extends QEvent{
    
    public static interface IndexInfo<T extends QAbstractItemView>{
        QModelIndex getIndex(T view);
    }
    
    private static final class MouseEventInfo{
        
        private final QEvent.Type type;
        private final QPoint pos;
        private final QPoint globalPos;
        private final Qt.MouseButton mouseButton;
        private final Qt.MouseButtons mouseButtons;
        private final Qt.KeyboardModifiers keyModifiers;
        
        public MouseEventInfo(final QMouseEvent event){
            type = event.type();
            pos = new QPoint(event.x(), event.y());
            globalPos = new QPoint(event.globalX(), event.globalY());
            mouseButtons = new Qt.MouseButtons(event.buttons().value());
            mouseButton = event.button();
            keyModifiers = new Qt.KeyboardModifiers(event.modifiers().value());            
        }
        
        public QMouseEvent getMouseEvent(){
            return new QMouseEvent(type, pos, globalPos, mouseButton, mouseButtons, keyModifiers);
        }        
    }    
    
    private final MouseEventInfo originalEventInfo;
    private final IndexInfo<T> indexInfo;
    private final int timerId;
    private final Pid pid;        
    private MouseEventInfo editEventInfo;

    public PostponedMousePressEvent(final QMouseEvent event, final IndexInfo<T> indexInfo, final Pid pid, final int timerId){
        super(QEvent.Type.User);
        originalEventInfo = new MouseEventInfo(event);
        this.indexInfo = indexInfo;
        this.pid = pid;
        this.timerId = timerId;
    }

    public QMouseEvent getOriginalEvent(){
        return originalEventInfo.getMouseEvent();
    }

    public QMouseEvent getEditEvent(){
        return editEventInfo==null ? null : editEventInfo.getMouseEvent();
    }

    public QModelIndex getIndex(final T view){
        return indexInfo.getIndex(view);
    }
    
    public boolean sameIndex(final IndexInfo<T> otherIndexInfo){
        return Objects.equals(indexInfo, otherIndexInfo);
    }

    public Pid getPid(){
        return pid;
    }

    public int getClickTimerId(){
        return timerId;
    }

    public boolean isEdit() {
        return editEventInfo!=null;
    }

    public void setEdit(final QMouseEvent event) {
        editEventInfo = event == null ? null : new MouseEventInfo(event);
    }                

    public long post(final QObject receiver){
        return Application.processEventWhenEasSessionReady(receiver, this);
    }    
}
