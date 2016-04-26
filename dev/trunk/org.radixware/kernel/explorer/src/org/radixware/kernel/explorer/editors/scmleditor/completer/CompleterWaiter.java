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

package org.radixware.kernel.explorer.editors.scmleditor.completer;

import com.trolltech.qt.gui.QApplication;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;


public class CompleterWaiter{
    private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "SCML Editor CompleterWaiter Thread");
        }
    });
    private boolean isCanceled=false;

    public<T> T runAndWait(Callable<T> task) throws InterruptedException, ExecutionException{
        try{
            final Future<T> future = executor.submit(task);
            do{
                Thread.sleep(10);
                QApplication.processEvents();
                if (isCanceled){
                    future.cancel(true);
                    return null;
                }
            }
            while(!future.isDone());
            return future.get();
        }
        catch(CancellationException ex){
            throw new InterruptedException("execution of task was interrupted");
        }
    }

    public void cancel(){
        isCanceled=true;
    }

    public boolean wasCanceled(){
        return isCanceled;
    }

    public void close(){
        executor.shutdownNow();
    }
}
