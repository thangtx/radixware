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

package org.radixware.kernel.designer.common.dialogs.results;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;


public abstract class RadixObjectsLookuper {

    protected abstract String getProcessName();

    protected abstract String getPreparingProcessName();

    protected abstract void process(RadixObject radixObject);

    private static class Counter implements IVisitor {

        int count = 0;

        @Override
        public void accept(RadixObject radixObject) {
            count++;
        }
    }

    private class Visitor implements IVisitor {

        int processedCount = 0;
        final int step;
        final ProgressHandle progressHandle;
        final ExecutorService executor;
        final CountDownLatch cl;

        public Visitor(int totalCount, ProgressHandle progressHandle, ExecutorService executor, CountDownLatch cl) {
            this.step = (totalCount < 100 ? 1 : totalCount / 100); // to prevent devision by zero below
            this.progressHandle = progressHandle;
            this.executor = executor;
            this.cl = cl;
        }

        @Override
        public void accept(final RadixObject radixObject) {
            if (executor != null && cl != null) {
                executor.submit(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            process(radixObject);
                            incProgress();
                        } finally {
                            cl.countDown();
                        }
                    }
                });
            } else {
                process(radixObject);
                incProgress();
            }
        }

        private void incProgress() {
            synchronized (progressHandle) {
                processedCount++;
                if (processedCount % step == 0) { // for optimization
                    progressHandle.progress(processedCount);
                }
            }
        }
    }

    private static int getTotalCount(final Collection<? extends RadixObject> contexts, final VisitorProvider visitorProvider) {
        final Counter counter = new Counter();
        for (RadixObject context : contexts) {
            context.visit(counter, visitorProvider);
        }
        return counter.count;
    }

    protected final void lookup(final Collection<? extends RadixObject> contexts, final VisitorProvider visitorProvider) {
        lookup(contexts, visitorProvider, false);
    }
    private static final ExecutorService lookupExec = Executors.newFixedThreadPool(2, new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("RadixWareLookuperThread");
            return t;
        }
    });

    protected final void lookup(final Collection<? extends RadixObject> contexts, final VisitorProvider visitorProvider, boolean multithread) {
        final String preparingProcessName = getPreparingProcessName();
        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle(preparingProcessName, new Cancellable() {

            @Override
            public boolean cancel() {
                visitorProvider.cancel();
                return false;
            }
        });

        try {
            progressHandle.start();

            final int totalCount = getTotalCount(contexts, visitorProvider);

            final String processName = getProcessName();
            progressHandle.setDisplayName(processName);
            progressHandle.switchToDeterminate(totalCount);

            CountDownLatch cl = null;
            ExecutorService exec = null;

            if (multithread) {
                cl = new CountDownLatch(totalCount);
                exec = lookupExec;
            }

            final Visitor visitor = new Visitor(totalCount, progressHandle, exec, cl);

            for (RadixObject context : contexts) {
                context.visit(visitor, visitorProvider);
            }
            if (multithread) {
                try {
                    cl.await();
                } catch (InterruptedException ex) {
                    //ignore
                }
            }
        } finally {
            progressHandle.finish();
        }
    }
}
