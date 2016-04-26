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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QResizeEvent;
import java.util.LinkedList;
import java.util.List;


final class QEventsScheduler {
    
    private List<QEvent> scheduledEvents = new LinkedList<>();
    
    public QEventsScheduler(){        
    }
    
    public void scheduleEvent(final QEvent event){
        final boolean isTimerEvent = event instanceof QTimerEvent;
        final int timerId = event instanceof QTimerEvent ? ((QTimerEvent)event).timerId() : 0;        
        final QSize newSize = event instanceof QResizeEvent ? ((QResizeEvent)event).size() : null;        
        for (int i=0,count=scheduledEvents.size(); i<count; i++){
            final QEvent scheduledEvent = scheduledEvents.get(i);
            if (isTimerEvent){
                if (scheduledEvent instanceof QTimerEvent 
                    && ((QTimerEvent)scheduledEvent).timerId()==timerId){
                    return;
                }
            }else if (newSize!=null){
                if (scheduledEvent instanceof QResizeEvent){
                    final QResizeEvent resizeEvent = (QResizeEvent)scheduledEvent;
                    if (newSize.equals(resizeEvent.size())){
                        return;
                    }
                    scheduledEvents.set(i, new QResizeEvent(newSize, resizeEvent.oldSize()));
                }
            } else if (scheduledEvent.getClass()==event.getClass()){//already scheduled                
                return;
            }
        }
        scheduledEvents.add(event);        
    }
    
    public void postScheduledEvents(final QObject targed){
        for (QEvent event: scheduledEvents){
            QApplication.postEvent(targed, event);
        }
        scheduledEvents.clear();        
    }    
}
