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

package org.radixware.kernel.explorer.env.progress;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QApplication;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IProgressHandle.Cancellable;
import org.radixware.kernel.common.exceptions.IllegalUsageError;


public class TaskWaiter extends QObject implements ITaskWaiter {
    
    private static final class ExecutionCompleteEvent extends QEvent{        
        public ExecutionCompleteEvent(){
            super(QEvent.Type.User);
        }
    }
    
    private static final class InternalThreadFactory implements ThreadFactory{
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r, "TaskWaiter thread");
        }
    }
    
    private static final class CancelHandler implements IProgressHandle.Cancellable{
        
        private final ICancellableTask task;
        
        public CancelHandler(final ICancellableTask cancellableTask){
            task = cancellableTask;
        }

        @Override
        public void onCancel() {
            task.cancel();
        }
        
    }
    
    private final class InternalThreadPoolExecutor extends ThreadPoolExecutor{
                
        public InternalThreadPoolExecutor(){
            super(1, 1, 0L, TimeUnit.MILLISECONDS,
                  new LinkedBlockingQueue<Runnable>(),
                  new InternalThreadFactory());
        }

        @Override
        protected void afterExecute(final Runnable r, final Throwable t) {
            QApplication.postEvent(TaskWaiter.this, new ExecutionCompleteEvent());            
        }                
    }

    private final ExecutorService executor = new InternalThreadPoolExecutor();
    private final QEventLoop localEventLoop = new QEventLoop(this);
    
    private String title;

    public boolean canBeCanceled() {
        return canBeCanceled;
    }

    public void setCanBeCanceled(final boolean canBeCanceled) {
        this.canBeCanceled = canBeCanceled;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(final String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
    
    private String message;
    private boolean canBeCanceled;
    private final IClientEnvironment environment;
    private Object currentTask;
    private IProgressHandle handle;
    private int timer;
    private int lastMaxValue, lastCurrentValue;
    private String lastTitle;

    public TaskWaiter(final IClientEnvironment environment) {
        this(environment,(QObject)environment.getMainWindow());
    }
    
    public TaskWaiter(final IClientEnvironment environment, final QObject parent) {
        super(parent);
        if (QApplication.instance().thread()!=Thread.currentThread()){
            environment.getTracer().error(new IllegalUsageError("Creating TaskWaiter instance in wrong thread"));
        }
        this.environment = environment;
    }    

    @Override
    public <T> T runAndWait(final Callable<T> task) throws InterruptedException, ExecutionException {
        handle = startProgress(task);
        try {
            final Future<T> future = executor.submit(task);
            localEventLoop.exec();
            return future.get();
        } catch (CancellationException ex) {
            throw new InterruptedException("execution of task was interrupted");//NOPMD
        } finally {
            if (timer!=0){
                killTimer(timer);
            }
            handle.finishProgress();
            currentTask = null;
            handle = null;
            
        }
    }

    @Override
    public void runAndWait(final Runnable task) throws InterruptedException {
        handle = startProgress(task);        
        try {
            final Future future = executor.submit(task);
            localEventLoop.exec();
            if (future.isCancelled()) {
                throw new InterruptedException("execution of task was interrupted");
            }
        } finally {
            if (timer!=0){
                killTimer(timer);
            }
            handle.finishProgress();
            currentTask = null;
            handle = null;
        }
    }

    private IProgressHandle createProgressHandle(final Cancellable cancellable) {
        final IProgressHandle resultHandle;
        if (cancellable==null){
            resultHandle = environment.getProgressHandleManager().newProgressHandle();
        }else{
            resultHandle = environment.getProgressHandleManager().newProgressHandle(cancellable);
        }
        if (title != null) {
            resultHandle.setTitle(title);
        }
        return resultHandle;
    }
    
    private IProgressHandle startProgress(final Object task){
        final IProgressHandle resultHandle;
        if (task instanceof ICancellableTask){
            resultHandle = createProgressHandle(new CancelHandler((ICancellableTask)task));
            canBeCanceled = true;            
        }else{
            resultHandle = createProgressHandle(null);
        }
        if (task instanceof IMeasurableTask || task instanceof ITitledTask){
            lastCurrentValue = 0;
            lastMaxValue = 0;
            lastTitle = message;
            currentTask = task;            
            timer = startTimer(100);
        }
        resultHandle.startProgress(message, canBeCanceled);
        return resultHandle;                
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (event.timerId()==timer){
            event.accept();
            if (currentTask instanceof IMeasurableTask && handle!=null){
                final IMeasurableTask measurableTask = (IMeasurableTask)currentTask;
                final int maxValue = measurableTask.getProgressMaxValue();
                final int curValue = measurableTask.getProgressCurValue();
                if (lastMaxValue!=maxValue){
                    lastMaxValue = maxValue;
                    handle.setMaximumValue(maxValue);
                }
                if (lastCurrentValue!=curValue){
                    lastCurrentValue = curValue;
                    handle.setValue(curValue);
                }
            }if (currentTask instanceof ITitledTask && handle!=null){            
                final ITitledTask titledTask = (ITitledTask)currentTask;
                final String newTitle = titledTask.getTitle();
                if (!Objects.equals(lastTitle, newTitle)){
                    lastTitle = newTitle;
                    handle.setText(newTitle);
                }
            }
        }else{
            super.timerEvent(event);
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ExecutionCompleteEvent){
            event.accept();
            localEventLoop.exit();
        }else{
            super.customEvent(event);
        }
    }
    
    

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
