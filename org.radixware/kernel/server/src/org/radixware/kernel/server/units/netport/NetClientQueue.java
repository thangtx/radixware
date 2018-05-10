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

package org.radixware.kernel.server.units.netport;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.utils.SystemPropUtils;

/**
 *
 * @author dsafonov
 */
public class NetClientQueue {
    
    private static final int MAX_QUEUE_SIZE = SystemPropUtils.getIntSystemProp("rdx.net.client.queue.max.size", 100000);
    
    private final Set<NetClientQueueItem> queue = new LinkedHashSet<>();
    
    public boolean add(final NetClientQueueItem request) {
        if (queue.size() >= MAX_QUEUE_SIZE) {
            return false;
        }
        register(request);
        return true;
    }
    
    public void clear() {
        queue.clear();
    }
    
    
    public int size() {
        return queue.size();
    }
    
    public boolean remove(final NetClientQueueItem item, final String reason) {
        return unregister(item, reason);
    }
    
    public List<NetClientQueueItem> removeTimedOut() {
        final List<NetClientQueueItem> result = new ArrayList<>();
        final long curTimeMillis = System.currentTimeMillis();
        for (NetClientQueueItem item : queue) {
            if (item.isTimedOut(curTimeMillis)) {
                result.add(item);
            }
        }
        for (NetClientQueueItem rq : result) {
            unregister(rq, "timed out");
        }
        return result;
    }
    
    public List<NetClientQueueItem> listQueue() {
        return new ArrayList<>(queue);
    }
    
    private void register(final NetClientQueueItem item) {
        queue.add(item);
        item.getNetClient().trace("Registered " + item);
    }
    
    private boolean unregister(final NetClientQueueItem item, final String reason) {
        item.getNetClient().trace("Unregistered " + item + " (reason: " + reason + ")");
        return queue.remove(item);
    }

}
