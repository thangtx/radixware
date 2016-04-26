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

package org.radixware.kernel.common.client.eas;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.IEventLoop;


final class DummyEventLoop implements IEventLoop{        
            
    private final AtomicBoolean isInProgress = new AtomicBoolean(false);    
    private final List<Runnable> scheduledTasks = new LinkedList<>();
    private final Object semaphore = new Object();
    private volatile CountDownLatch latch;
    
    @Override
    public void scheduleTask(final Runnable task) {
        if (task!=null){
            scheduledTasks.add(task);
        }
    }

    @Override
    @SuppressWarnings("WaitWhileNotSynced")
    public void start() {
        synchronized(semaphore){
            isInProgress.set(true);
            latch = new CountDownLatch(1);
        }
        try {
            for (Runnable task : scheduledTasks) {
                try {
                    task.run();
                } catch (RuntimeException exception) {
                    Logger.getLogger(DummyEventLoop.class.getName()).log(Level.SEVERE, null, exception);
                }
            }            
            scheduledTasks.clear();            
            try{
                latch.await();
            }catch(InterruptedException exception){
                Logger.getLogger(DummyEventLoop.class.getName()).log(Level.SEVERE, null, exception);
            }
        } finally {
            isInProgress.set(false);
        }
    }

    @Override
    @SuppressWarnings("NotifyWhileNotSynced")
    public void stop() {
        synchronized(semaphore){
            latch.countDown();
        }
    }

    @Override
    public boolean isInProgress() {
        return isInProgress.get();
    }    
}
