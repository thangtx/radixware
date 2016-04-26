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
package org.radixware.kernel.server.units.netport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceConnectTimeout;
import org.radixware.schemas.aasWsdl.InvokeDocument;

class NetPortAasClientPool {

    private static final int MAX_QUEUE_SIZE = 1000000;
    private final NetPortHandlerUnit unit;
    private final List<NetPortInvokeQueueItem> queue = new LinkedList<>();
    private final List<NetPortAasClient> clients = new ArrayList<>();

    public NetPortAasClientPool(final NetPortHandlerUnit unit) {
        this.unit = unit;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public void invoke(final Seance seance, final InvokeDocument invokeXml, final Map<String, String> headers, final boolean keepConnect) {
        if (queue.size() < MAX_QUEUE_SIZE) {
            final NetPortInvokeQueueItem item = new NetPortInvokeQueueItem(seance, invokeXml, headers, keepConnect);
            queue.add(item);
            unit.onQueuePut();
            logAdd(item);
            tryInvoke();
        } else {
            seance.onAasInvokeException(new ServiceConnectTimeout("AAS invocation queue overflow"));
        }
    }

    public void tryInvoke() {
        if (!queue.isEmpty()) {
            for (NetPortAasClient client : clients) {
                if (!client.busy()) {
                    final NetPortInvokeQueueItem item = queue.remove(0);
                    unit.onQueueRemove();
                    logInvoke(item);
                    client.invoke(item);
                    if (queue.isEmpty()) {
                        return;
                    }
                }
            }
            while (!queue.isEmpty() && clients.size() < unit.getOptions().maxAasSeancesCount) {
                final NetPortAasClient client = new NetPortAasClient(unit);
                clients.add(client);
                final NetPortInvokeQueueItem item = queue.remove(0);
                unit.onQueueRemove();
                logInvoke(item);
                client.invoke(item);
            }
        }
    }

    private void logAdd(final NetPortInvokeQueueItem item) {
        unit.getTrace().debug("Adding item " + item.debugTitle + " , queue size: " + queue.size() + ", waited: " + (System.currentTimeMillis() - item.createTimeMillis) + " ms", EEventSource.NET_PORT_HANDLER, false);
    }

    private void logInvoke(final NetPortInvokeQueueItem item) {
        unit.getTrace().debug("Invoking item " + item.debugTitle + " , queue size: " + queue.size() + ", waited: " + (System.currentTimeMillis() - item.createTimeMillis) + " ms", EEventSource.NET_PORT_HANDLER, false);
    }

    public void maintenance() {
        adjustPoolSize();
        removeTimedOutQueueItems();
    }

    private void removeTimedOutQueueItems() {
        final Iterator<NetPortInvokeQueueItem> it = queue.iterator();
        final List<NetPortInvokeQueueItem> toNotify = new ArrayList<>();
        while (it.hasNext()) {
            final NetPortInvokeQueueItem item = it.next();
            if (item.createTimeMillis + NetPortAasClient.AAS_INVOKE_TIMEOUT_MILLIS < System.currentTimeMillis()) {
                it.remove();
                toNotify.add(item);
            } else if (item.seance.state == Seance.State.CLOSED) {
                it.remove();
            }
        }
        for (NetPortInvokeQueueItem item : toNotify) {
            item.seance.onAasInvokeException(new ServiceConnectTimeout("Timeout during waiting in queue"));
        }
    }

    private void adjustPoolSize() {
        int removed = 0;
        int needToRemove = clients.size() - unit.getOptions().maxAasSeancesCount;
        if (needToRemove > 0) {
            final Iterator<NetPortAasClient> it = clients.iterator();
            while (it.hasNext() && removed < needToRemove) {
                final NetPortAasClient client = it.next();
                if (!client.busy()) {
                    client.closeActiveSeances();
                    it.remove();
                    removed++;
                }
            }
        }
    }

    public void closeAll() {
        for (NetPortAasClient client : clients) {
            client.closeActiveSeances();
        }
        clients.clear();
    }
}
