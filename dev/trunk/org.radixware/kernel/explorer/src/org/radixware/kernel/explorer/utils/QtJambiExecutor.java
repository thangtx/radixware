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

package org.radixware.kernel.explorer.utils;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


public final class QtJambiExecutor extends QObject {
    
    
//Должен быть сконструирован из потока qt
        
    private static class InvokeRunnable extends QEvent{
        
        private final Runnable task;
        
        public InvokeRunnable(final Runnable task){
            super(QEvent.Type.User);
            this.task = task;
        }
        
        public void invoke(){
            task.run();
        }
    }
    
    private static class InvokeCallable<T> extends QEvent{
        
        private final Callable<T> task;
        private T result = null;
        private ExecutionException exception = null;
        
        public InvokeCallable(final Callable<T> task){
            super(QEvent.Type.User);
            this.task = task;
        }        
        
        public T result() throws ExecutionException{
            if(exception == null) {
                return result;
            } else {
                throw exception;
            }
        }
        
        public void invoke() {
            try {
                result = task.call();
            } catch (ExecutionException ex) {
                exception = ex;
            } catch (Exception ex) {
                exception = new ExecutionException(ex);
            }
        } 
    } 
    
    private volatile boolean done;
    private final Object semaphore = new Object();
    
    public QtJambiExecutor() {
        this(QApplication.instance());
    }
    
    public QtJambiExecutor(final QObject parent) {
        super(parent);
    }
    
    public void invoke(final Runnable task) throws InterruptedException{//может быть вызван из любого потока        
        if (QApplication.instance().thread()==Thread.currentThread()){
            task.run();
        }else{
            synchronized(semaphore){
                done = false;
            }            
            final InvokeRunnable event = new InvokeRunnable(task);
            QApplication.postEvent(this, event);
            synchronized(event){
                while (!isDone()){
                    event.wait(); 
                }
            }
        }        
    }
    
    public <T> T invoke(Callable<T> task) throws InterruptedException, ExecutionException {//может быть вызван из любого потока        
        if (QApplication.instance().thread()==Thread.currentThread()){
            try{
                return task.call();
            }catch(Exception ex){
                throw new ExecutionException(ex);
            }
        }else{
            synchronized(semaphore){
                done = false;
            }
            final InvokeCallable<T> event = new InvokeCallable<>(task);
            QApplication.postEvent(this, event);
            synchronized(event){ 
                while (!isDone()){
                    event.wait(); 
                }
            }
            return event.result();
        }
    }

    private boolean isDone(){
        synchronized(semaphore){
            return done;
        }
    }
    
    
    @Override
    protected void customEvent(QEvent event) {
               
        if(event instanceof InvokeCallable) {
            InvokeCallable invoker = (InvokeCallable) event;
            invoker.invoke();
        } else if (event instanceof InvokeRunnable){
            ((InvokeRunnable)event).invoke();
        } else {
            super.customEvent(event);
        }
        synchronized(semaphore){
            done = true;
        }
        synchronized(event) { event.notify(); }//NOPMD
    }
}
