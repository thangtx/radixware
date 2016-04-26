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

package org.radixware.kernel.common.check.spelling;

import java.util.Timer;
import java.util.TimerTask;


public class DataController {

    private final WordCollection collection;
    private boolean deallocateEnabled;
    private long delay = 1000 * 60 * 1;
    
    Timer timer = new Timer();
    TimerTask deallocateTask;
    TimerTask sheduleTask;
    
    private volatile boolean isSheduling = false;

    public DataController(WordCollection collection, boolean deallocateEnable) {

        if (collection == null) {
            throw new NullPointerException("Collection is null");
        }
        this.collection = collection;
        this.deallocateEnabled = deallocateEnable;
    }

    public final long getDelay() {
        return delay;
    }

    public final void setDelay(long delay) {
        this.delay = delay;
    }

    public WordCollection getInitCollection() {
        request();
        return getCollection();
    }

    public void request() {
        if (!getCollection().isReady()) {
            collection.init();
        }

        if (!isSheduling) {
            cancelDeallocateTask();
            
            if (sheduleTask != null) {
                sheduleTask.cancel();
            }

            sheduleTask = new TimerTask() {

                @Override
                public void run() {
                    scheduleDeallocateTask();
                    isSheduling = false;
                    sheduleTask = null;
                }
            };

            int sheduleDelay = 1000 * 60 * 1; // one minute

            timer.purge();
            timer.schedule(sheduleTask, sheduleDelay);
            
            isSheduling = true;
        }
    }

    public void idle() {
        collection.deallocate();
        deallocateTask = null;
    }

    public void setDeallocateEnable(boolean enable) {

        if (!enable) {
            cancelDeallocateTask();
        } else if (!deallocateEnabled) {
            scheduleDeallocateTask();
        }

        deallocateEnabled = enable;
    }

    public boolean isDeallocateEnabled() {
        return deallocateEnabled;
    }
    
    public final WordCollection getCollection() {
        return collection;
    }

    private void cancelDeallocateTask() {
        if (deallocateTask != null) {
            deallocateTask.cancel();
            deallocateTask = null;
        }
    }

    private void scheduleDeallocateTask() {
        if (isDeallocateEnabled()) {

            // cancel previous task
            cancelDeallocateTask();

            deallocateTask = new DeallocateTask();
            timer.schedule(deallocateTask, delay);
        }
    }

    private final class DeallocateTask extends TimerTask {

        @Override
        public void run() {
            idle();
        }
    }
}