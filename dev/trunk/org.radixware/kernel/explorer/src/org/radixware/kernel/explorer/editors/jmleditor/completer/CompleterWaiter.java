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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.trace.ClientTracer;

class CompleterWaiter {

    private final ExecutorService executor;
    private final ClientTracer tracer;
    private Future currentFuture = null;

    public CompleterWaiter(final ClientTracer tracer) {
        this.tracer = tracer;
        this.executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                Thread t = new Thread(r, "JML Editor CompleterWaiter Thread");
                t.setDaemon(true);
                return t;
            }
        });
    }
    
    public <T> void submitTask(final Callable<T> task) {
        try {
            if (currentFuture != null && !currentFuture.isDone()) {
                return;
            }
            final Callable<T> wrapperToHandleExceptions = new Callable<T>() {
                @Override
                public T call() throws Exception {
                    try {
                        return task.call();
                    } catch (InterruptedException th) {
                        throw th;
                    } catch (Throwable th) {
                        tracer.error("JmlEditor. Error on calculate completion", th);
                        throw th;
                    }
                }
            };
            currentFuture = executor.submit(wrapperToHandleExceptions);
        } catch (CancellationException ex) {
            Logger.getLogger(CompleterWaiter.class.getName()).log(Level.SEVERE, "Task was cancelled", ex);
        }
    }

    public boolean cancel() {
        if (currentFuture != null) {
            return currentFuture.cancel(true);
        }
        return false;
    }

    public void close() {
        executor.shutdown();
    }
}
