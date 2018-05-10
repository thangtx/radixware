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
import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.IEventLoop;


final class ExplorerEventLoop extends QObject implements IEventLoop{
            
    private static class StopEventLoop extends QEvent{        
        public StopEventLoop(){
            super(QEvent.Type.User);
        }        
    }
    
    private static class ScheduledTask extends QEvent{
        
        private final Runnable task;
        
        public ScheduledTask(final Runnable task){
            super(QEvent.Type.User);
            this.task = task;
        }
        
        public void execute(){
            try{
                task.run();
            }catch(RuntimeException exception){
                Logger.getLogger(ExplorerEventLoop.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    private final AtomicBoolean isInProgress = new AtomicBoolean(false);    
    private final QEventLoop eventLoop = new QEventLoop(this);    
    private final List<Runnable> scheduledTasks = new LinkedList<>();
    private final Object semaphore = new Object();
    private volatile CountDownLatch latch;
    
    public ExplorerEventLoop(final QObject parent){
        super(parent);
    }        
    
    private boolean isGuiThread(){
        return thread()==Thread.currentThread();
    }

    @Override
    public void scheduleTask(final Runnable task) {
        if (task!=null){
            if (isInProgress()){
                QApplication.postEvent(this, new ScheduledTask(task));
            }else{
                scheduledTasks.add(task);
            }
        }
    }    

    @Override
    public void start() {
        isInProgress.set(true);
        for (Runnable task: scheduledTasks){
            QApplication.postEvent(this, new ScheduledTask(task));
        }
        scheduledTasks.clear();
        if (isGuiThread()){
            eventLoop.exec();
        }else{
            synchronized(semaphore){
                latch = new CountDownLatch(1);
            }
            try{
                latch.await();
            }catch(InterruptedException exception){
                Logger.getLogger(ExplorerEventLoop.class.getName()).log(Level.SEVERE, null, exception);
            }            
        }
    }

    @Override
    public void stop() {
        synchronized(semaphore){
            if (latch!=null){
                latch.countDown();
                latch = null;
                isInProgress.set(false);
                return;
            }
        }
        QApplication.postEvent(this, new StopEventLoop());        
    }

    @Override
    public boolean isInProgress() {
        return isInProgress.get();
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof StopEventLoop){
            event.accept();
            isInProgress.set(false);
            eventLoop.exit();
        }else if (event instanceof ScheduledTask){
            event.accept();
            ((ScheduledTask)event).execute();
        }else{
            super.customEvent(event);
        }
    }        
}
