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
package org.radixware.kernel.server.monitoring;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 *
 * @author dsafonov
 */
public class ChainStat {

    private final int maxChainSize;
    private final int maxStoreMillis;
    private RegisteredItem<Long> lastMin;
    private RegisteredItem<Long> lastMax;
    private long sum;
    private final ArrayDeque<RegisteredItem<Long>> buffer;

    public ChainStat(int maxChainSize, int maxStoreMillis) {
        this.maxChainSize = maxChainSize;
        this.maxStoreMillis = maxStoreMillis;
        buffer = new ArrayDeque<>(maxChainSize);
        if (maxChainSize <= 0) {
            throw new IllegalArgumentException("Chain size should be >= 0 (actual " + maxChainSize + ")");
        }
    }

    public void append(long value) {
        append(new RegisteredItem<>(System.currentTimeMillis(), value));
    }

    public void append(RegisteredItem<Long> item) {
        cleanOld();
        if (buffer.size() > maxChainSize) {
            removeHead();
        }
        buffer.add(item);
        updateMinMax(item);
        sum += item.val;
    }

    private void cleanOld() {
        final long curMillis = System.currentTimeMillis();
        while (buffer.peek() != null && buffer.peek().regTimeMillis < curMillis - maxStoreMillis) {
            removeHead();
        }
    }

    private void removeHead() {
        final RegisteredItem<Long> outItem = buffer.remove();
        if (outItem == lastMax || outItem == lastMin) {
            lastMax = null;
            lastMin = null;
            final Iterator<RegisteredItem<Long>> it = buffer.iterator();
            while (it.hasNext()) {
                updateMinMax(it.next());
            }
        }
        sum -= outItem.val;
    }

    private void updateMinMax(final RegisteredItem<Long> item) {
        if (lastMax == null || item.val >= lastMax.val) {
            lastMax = item;
        }
        if (lastMin == null || item.val <= lastMin.val) {
            lastMin = item;
        }
    }

    public IStatValue getStat() {
        cleanOld();
        return new StatValue(lastMin == null ? 0 : lastMin.val.doubleValue(),
                lastMax == null ? 0 : lastMax.val.doubleValue(),
                buffer.isEmpty() ? 0 : sum / buffer.size());
    }

    public int getMaxChainSize() {
        return maxChainSize;
    }

    public int getMaxStoreMillis() {
        return maxStoreMillis;
    }

}
