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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.eas.AbstractEasSession;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.utils.ThreadDumper;


final class ResponseNotificationScheduler extends QObject implements AbstractEasSession.IResponseNotificationScheduler{
    
    private static class RunNotificationTaskEvent extends QEvent{
        
        private final Runnable task;
        
        public RunNotificationTaskEvent(final Runnable task){
            super(QEvent.Type.User);
            this.task = task;
        }
        
        public Runnable getTask(){
            return task;
        }        
    }
    
    private final Object semaphore = new Object();
    private final List<Runnable> scheduledTasks = new LinkedList<>();
    private final Environment environment;    
    private final IEasSession.Listener sessionListener = new IEasSession.Listener() {
            @Override
            public void beforeProcessSyncRequest(final RequestHandle handle) {
                if (inResponseProcessing){
                    final String traceMessage = environment.getMessageProvider().translate("TraceMessage", "Sending sync request in async response handler.");
                    if (isInDevelopmentMode){
                        environment.getTracer().debug(traceMessage+"\n"+ThreadDumper.dumpSync());
                    }else{
                        environment.getTracer().debug(traceMessage);
                    }
                }
            }
    };
    private final boolean isInDevelopmentMode;
    private boolean inResponseProcessing;
    private boolean isListenerAdded;
    private int blockCounter;
    
    public ResponseNotificationScheduler(final Environment env){        
        super((QObject)env.getApplication());
        isInDevelopmentMode = RunParams.isDevelopmentMode();        
        this.environment = env;
    }

    @Override
    public void block() {
        synchronized(semaphore){
            blockCounter++;
        }
    }

    @Override
    public void unblock() {
        synchronized(semaphore){
            blockCounter--;
            if (blockCounter==0){
                for (Runnable task: scheduledTasks){
                    QApplication.postEvent(this, new RunNotificationTaskEvent(task));
                }
                scheduledTasks.clear();
            }            
        }
    }

    @Override
    public void scheduleNotificationTask(final Runnable task) {
        synchronized(semaphore){
            if (!isListenerAdded){
                environment.getEasSession().addListener(sessionListener);
                isListenerAdded = true;
            }
            if (blockCounter>0){
                scheduledTasks.add(task);
            }else{
                QApplication.postEvent(this, new RunNotificationTaskEvent(task));
            }
        }
    }
    
    private void runTask(final Runnable task){
        inResponseProcessing = true;
        try{
            task.run();
        }catch (RuntimeException ex) {
            final String message = environment.getMessageProvider().translate("ExplorerError", "Unhandled exception on request result processing:\n%s");
            final String exceptionStr = ClientException.getExceptionReason(environment.getMessageProvider(), ex) + "\n" + ClientException.exceptionStackToString(ex);
            environment.getTracer().error(String.format(message, exceptionStr));
        }finally{
            inResponseProcessing = false;
        }
    }
    
    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof RunNotificationTaskEvent){
            event.accept();
            final Runnable task = ((RunNotificationTaskEvent)event).getTask();
            synchronized(semaphore){
                if (blockCounter>0){
                    scheduledTasks.add(task);
                }else{
                    runTask(task);
                }
            }
        }else{
            super.customEvent(event);
        }
    }        
}
