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
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.eas.RequestHandle;


final class QtEventsDispatcher extends QObject{
    
    private final static class ScheduledEvent{
        
        private static int counter = 0;
        
        private final long id;
        private final QObject receiver;
        private final QEvent event;        
        
        public ScheduledEvent(final QObject receiver, final QEvent event){
            id = ++counter;
            this.receiver = receiver;
            this.event = event;
        }

        public QObject getReceiver() {
            return receiver;
        }

        public QEvent getSourceEvent() {
            return event;
        }                
        
        public long getId(){
            return id;
        }
    }    
    
    private final static class ProcessScheduledEvents extends QEvent{     
        public ProcessScheduledEvents(){
            super(QEvent.Type.User);
        }
    }        
    
    private final List<ScheduledEvent> scheduledEvents = new LinkedList<>();
    private final Map<Long, ScheduledEvent> scheduledEventsById = new HashMap<>();
    
    private IEasSession.Listener sessionListener;    
    private boolean eventProcessingScheduled;
    private boolean blocked;
    private final IEasSession session;
    private final Object semaphore = new Object();
    
    public QtEventsDispatcher(final QObject parent, final IEasSession easSession){
        super(parent);
        session = easSession;
    }
    
    public long scheduleEvent(final QObject receiver, final QEvent event){        
        if (event==null){
            throw new NullPointerException("event cannot be null");                
        }
        synchronized(semaphore){
            if (blocked){
                return 0;
            }
            final ScheduledEvent scheduledEvent = new ScheduledEvent(receiver, event);
            scheduledEvents.add(scheduledEvent);
            scheduledEventsById.put(scheduledEvent.getId(), scheduledEvent);
            if (!eventProcessingScheduled){
                QApplication.postEvent(this, new ProcessScheduledEvents());
                eventProcessingScheduled = true;
            }
            return scheduledEvent.getId();
        }
    }
    
    public boolean dropEvent(final long eventId){
        synchronized(semaphore){
            final ScheduledEvent scheduledEvent = scheduledEventsById.get(eventId);
            if (scheduledEvent!=null){
                scheduledEvents.remove(scheduledEvent);
                scheduledEventsById.remove(eventId);
                if (scheduledEvents.isEmpty()){
                    eventProcessingScheduled = false;
                }
                return true;
            }
            return false;
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ProcessScheduledEvents){
            event.accept();
            synchronized(semaphore){
                if (eventProcessingScheduled){
                    eventProcessingScheduled = false;
                    final List<ScheduledEvent> localCopy = new LinkedList<>(scheduledEvents);
                    for (ScheduledEvent scheduledEvent: localCopy){
                        if (session.isBusy()){
                            if (sessionListener==null){
                                sessionListener = new IEasSession.Listener() {
                                    @Override
                                    public void afterSyncRequestProcessed(final RequestHandle handle) {
                                        onSessionReady();
                                    }
                                };
                                session.addListener(sessionListener);
                            }
                            break;
                        }
                        final long eventId = scheduledEvent.getId();
                        if (scheduledEventsById.containsKey(eventId)){//still scheduled                        
                            if (scheduledEvent.getReceiver().nativeId()!=0){
                                QApplication.sendEvent(scheduledEvent.getReceiver(), scheduledEvent.getSourceEvent());
                            }
                            dropEvent(eventId);
                        }
                    }
                }
            }
        }
        super.customEvent(event);
    }
    
    private void onSessionReady(){
        synchronized(semaphore){
            session.removeListener(sessionListener);
            sessionListener = null;
            if (!eventProcessingScheduled && !scheduledEvents.isEmpty()){
                QApplication.postEvent(this, new ProcessScheduledEvents());
                eventProcessingScheduled = true;            
            }
        }
    }
    
    public void reset(){
        synchronized(semaphore){
            eventProcessingScheduled = false;
            scheduledEvents.clear();
            scheduledEventsById.clear();
        }        
    }
    
    public void block(){
        synchronized(semaphore){
            reset();
            blocked = true;
        }        
    }
    
    public void unblock(){
        synchronized(semaphore){
            blocked = false;
        }
    }
}
