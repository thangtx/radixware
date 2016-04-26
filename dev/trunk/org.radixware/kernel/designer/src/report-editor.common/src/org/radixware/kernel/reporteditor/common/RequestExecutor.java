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

package org.radixware.kernel.reporteditor.common;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.progress.ProgressUtils;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.environment.IEnvironmentAccessor;
import org.radixware.kernel.common.environment.IRadixEnvironment;


public abstract class RequestExecutor {
    
    private final Queue<ExplorerAction> actionQueue = new LinkedList<>();
    private volatile ExplorerSafeThread processor;
    
    public interface ExplorerAction {

        public void execute(IClientEnvironment env);
    }

    public interface ExplorerActionWithWait extends ExplorerAction {

        public void waitUntilDone() throws InterruptedException;

        public void done();
    }

    public static abstract class ExplorerActionWithWaitImpl implements ExplorerActionWithWait {

        private final CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void waitUntilDone() throws InterruptedException {
            latch.await();
        }

        @Override
        public void done() {
            latch.countDown();
        }
    }
    
    protected abstract IClientEnvironment getEnvironment();

    private static class ExplorerSafeThread extends Thread implements IEnvironmentAccessor, IContextEnvironment {

        private final IClientApplication app;
        private final IClientEnvironment env;

        public ExplorerSafeThread(IClientEnvironment env, Runnable run) {
            super(run);
            this.env = env;
            setName("ExplorerSafeThread");
            this.app = env.getApplication();
        }

        @Override
        public IRadixEnvironment getEnvironment() {
            return app;
        }

        @Override
        public IClientEnvironment getClientEnvironment() {
            return env;
        }
    }
    
    public void submitAction(final ExplorerAction action) {
       
        if (Thread.currentThread() == processor) {
            executeAction(action);
            return;
        }
        if (processor == null) {
            processor = new ExplorerSafeThread(getEnvironment(), new Runnable() {

                @Override
                public void run() {

                    processActions();
                }
            });
            processor.setDaemon(true);
            processor.start();

        }
        enqueueAction(action);
        
        if (SwingUtilities.isEventDispatchThread()) {
            ProgressUtils.runOffEventDispatchThread(new Runnable() {
                @Override
                public void run() {
                    if (action instanceof ExplorerActionWithWait) {
                        try {
                            ((ExplorerActionWithWait) action).waitUntilDone();
                        } catch (InterruptedException ex) {
                            //ignore
                        }
                    }
                }
            }, "Please wait...", new AtomicBoolean(false), false, 0, 400);
        } else {
            if (action instanceof ExplorerActionWithWait) {
                try {
                    ((ExplorerActionWithWait) action).waitUntilDone();
                } catch (InterruptedException ex) {
                    //ignore
                }
            }
        }
    }

    private void enqueueAction(ExplorerAction action) {
        synchronized (actionQueue) {
            actionQueue.add(action);
            actionQueue.notifyAll();
        }
    }

    private ExplorerAction getNextAction() {
        synchronized (actionQueue) {
            while (true) {
                if (actionQueue.isEmpty()) {
                    try {
                        actionQueue.wait();
                    } catch (InterruptedException ex) {
                        return null;
                    }
                } else {
                    return actionQueue.remove();
                }
            }
        }
    }

    private void processActions() {
        while (true) {
            ExplorerAction action = getNextAction();
            if (action != null) {
                executeAction(action);
            } else {
                return;
            }
        }
    }

    private void executeAction(ExplorerAction action) {
        try {
            action.execute(getEnvironment());
        } catch (Throwable e) {
            getEnvironment().processException(e);

        } finally {
            if(action instanceof ExplorerActionWithWait) {
                ((ExplorerActionWithWait) action).done();
            }
        }
    }

    void stop() {
        if (processor != null) {
            processor.interrupt();
        }
    }
}
