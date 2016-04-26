/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.sc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;

/**
 *
 * @author dsafonov
 */
public abstract class AasClientPool<T extends AasInvokeItem> {

    private static final int MAX_QUEUE_SIZE = 1000000;
    private AasClientPoolOptions options = new AasClientPoolOptions(1);
    private final List<T> queue = new LinkedList<>();
    private final List<SingleSeanceAasClient<T>> clients = new ArrayList<>();
    private final LocalTracer tracer;

    public AasClientPool(final LocalTracer tracer) {
        this.tracer = tracer;
    }
    
    private int getMaxQueueSize() {
        return MAX_QUEUE_SIZE;
    }

    public boolean canPlaceInvoke() {
        return queue.size() < getMaxQueueSize();
    }
    
    public void invoke(T item) {
        if (canPlaceInvoke()) {
            queue.add(item);
            tryInvoke();
        } else {
            onQueueOverflow(item);
        }
    }

    public void setOptions(AasClientPoolOptions options) {
        if (options == null) {
            throw new NullPointerException("Options should not be null");
        }
        this.options = options;
    }

    public AasClientPoolOptions getOptions() {
        return options;
    }
    
    protected void onQueueOverflow(final AasInvokeItem item) {
        throw new IllegalStateException("Invoke queue oveflow");
    }

    protected void onTimeoutInQueue(final AasInvokeItem item) {
        tracer.putFloodControlled(getClass().getName(), EEventSeverity.WARNING, "There were timed out itmes in AAS invoke queue", null, null, false);
    }

    public void tryInvoke() {
        if (!queue.isEmpty()) {
            for (SingleSeanceAasClient client : clients) {
                if (!client.busy()) {
                    client.invoke(queue.remove(0));
                    if (queue.isEmpty()) {
                        return;
                    }
                }
            }
            while (!queue.isEmpty() && clients.size() < options.getMaxSeancesCount()) {
                final SingleSeanceAasClient client = createAasClient();
                clients.add(client);
                client.invoke(queue.remove(0));
            }
        }
    }

    public void maintenance() {
        adjustPoolSize();
        removeTimedOutQueueItems();
    }

    private void removeTimedOutQueueItems() {
        final Iterator<T> it = queue.iterator();
        final List<AasInvokeItem> removed = new ArrayList<>();
        while (it.hasNext()) {
            final AasInvokeItem item = it.next();
            if (item.getCreateTimeMillis() + item.getTimeoutMillis() < System.currentTimeMillis()) {
                it.remove();
                removed.add(item);
            }
        }
        for (AasInvokeItem item : removed) {
            onTimeoutInQueue(item);
        }
    }

    private void adjustPoolSize() {
        int removed = 0;
        int needToRemove = clients.size() - options.getMaxSeancesCount();
        if (needToRemove > 0) {
            final Iterator<SingleSeanceAasClient<T>> it = clients.iterator();
            while (it.hasNext() && removed < needToRemove) {
                final SingleSeanceAasClient client = it.next();
                if (!client.busy()) {
                    client.closeActiveSeances();
                    it.remove();
                    removed++;
                }
            }
        }
    }

    public void closeAll() {
        for (SingleSeanceAasClient client : clients) {
            client.closeActiveSeances();
        }
        clients.clear();
    }
    
    public int getBusyCount() {
        int result = 0;
        for (SingleSeanceAasClient client : clients) {
            if (client.busy()) {
                result++;
            }
        }
        return result;
    }
    
    public int getFreeCount() {
        return Math.max(0, getOptions().getMaxSeancesCount() - getBusyCount());
    }

    protected abstract SingleSeanceAasClient<T> createAasClient();

    public static class AasClientPoolOptions {

        private final int maxSeancesCount;

        public AasClientPoolOptions(int maxSeancesCount) {
            this.maxSeancesCount = maxSeancesCount;
        }

        public int getMaxSeancesCount() {
            return maxSeancesCount;
        }

    }

}
