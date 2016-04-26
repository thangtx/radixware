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

package org.radixware.kernel.explorer.editors.jmleditor.completer;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;


public class CompleterWaiter{
        
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            Thread t = new Thread(r, "JML Editor CompleterWaiter Thread");
            t.setDaemon(true);
            return t;
        }
    });

    private Future currentFuture = null;
    private final Object currentFutureSem = new Object();

    public <T> void submitTask(final Callable<T> task) throws InterruptedException, ExecutionException {
        try {
            synchronized (currentFutureSem) {
                if (currentFuture != null && !currentFuture.isDone()) {
                    return;
                }
                currentFuture = executor.submit(task);
            }

        } catch (CancellationException ex) {
            throw new InterruptedException("Execution of task was interrupted");
        }
    }

    public boolean cancel(){
        synchronized(currentFutureSem) {
            if(currentFuture != null) {
                return currentFuture.cancel(true);
            }
        }
        return false;
    }

    public void close(){
        executor.shutdownNow();
    }
}
