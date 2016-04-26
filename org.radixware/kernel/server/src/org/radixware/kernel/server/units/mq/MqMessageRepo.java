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
package org.radixware.kernel.server.units.mq;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueManipulator;

public class MqMessageRepo<M extends MqMessage<?, P>, P> implements Closeable {

    private static final int QUEUE_HIWATER = 10;
    private static final int QUEUE_LOWATER = 2;
    private static final int QUEUE_HITOTAL = 1000;
    private static final int QUEUE_LOTOTAL = 20;

    private final IMqQueueManipulator<P> EMPTY_MANIPULATOR = new IMqQueueManipulator<P>() {
        @Override
        public void pause(P parameter) {
        }

        @Override
        public void resume(P parameter) {
        }

        @Override
        public void pausePolling() {
        }

        @Override
        public void resumePolling() {
        }
    };

    private final Map<P, List<M>> repo = new HashMap<>();
    private final Map<P, Long> inProcess = new HashMap<>();
    private final IMqQueueManipulator<P> manipulator;
    private final Set<P> paused = new HashSet<>();
    private final int hiWater, loWater, loTotal, hiTotal;
    private boolean pollable = true;

    public MqMessageRepo(final IMqQueueManipulator<P> manipulator) {
        this(manipulator, QUEUE_LOWATER, QUEUE_HIWATER, QUEUE_LOTOTAL, QUEUE_HITOTAL);
    }

    public MqMessageRepo(final IMqQueueManipulator<P> manipulator, final int loWater, final int hiWater, final int loTotal, final int hiTotal) {
        if (loWater <= 0 || hiWater <= 0) {
            throw new IllegalArgumentException("LoWater [" + loWater + "] and hiWater [" + hiWater + "] need to be positive!");
        } else if (loWater >= hiWater) {
            throw new IllegalArgumentException("LoWater [" + loWater + "] need to be less than hiWater [" + hiWater + "]");
        } else if (loTotal <= 0 || hiTotal <= 0) {
            throw new IllegalArgumentException("LoTotal [" + loTotal + "] and hiTotal [" + hiTotal + "] need to be positive!");
        } else if (loTotal >= hiTotal) {
            throw new IllegalArgumentException("LoTotal [" + loTotal + "] need to be less than hiTotal [" + hiTotal + "]");
        } else if (loWater > loTotal) {
            throw new IllegalArgumentException("LoWater [" + loWater + "] need not to be great than loTotal [" + loTotal + "]");
        } else if (hiWater > hiTotal) {
            throw new IllegalArgumentException("HiWater [" + hiWater + "] need to be great than hiTotal [" + hiTotal + "]");
        } else {
            this.manipulator = manipulator == null ? EMPTY_MANIPULATOR : manipulator;
            this.hiWater = hiWater;
            this.loWater = loWater;
            this.hiTotal = hiTotal;
            this.loTotal = loTotal;
        }
    }

    @Override
    public void close() throws IOException {
    }

    public boolean hasMessagesInProcess() {
        final long actualTimestamp = System.currentTimeMillis();
        int count = 0;

        for (Map.Entry<P, Long> item : inProcess.entrySet()) {
            if (item.getValue() >= actualTimestamp) {
                count++;
            }
        }

        return count > 0;
    }

    public void storeMessage(final M msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Message can't be null");
        } else {
            if (!repo.containsKey(msg.getPartitionId())) {
                repo.put((P) msg.getPartitionId(), new ArrayList<M>());
            }
            repo.get(msg.getPartitionId()).add(msg);

            if (repo.get(msg.getPartitionId()).size() == hiWater && !paused.contains(msg.getPartitionId())) {   // Prevent multiple pausing!
                paused.add((P) msg.getPartitionId());
                manipulator.pause((P) msg.getPartitionId());
            }

            int commonMessages = 0;
            for (Map.Entry<P, List<M>> item : repo.entrySet()) {
                commonMessages += item.getValue().size();
            }

            if (commonMessages == hiTotal && pollable) {   // Prevent multiple pausing!
                manipulator.pausePolling();
                pollable = false;
            }
        }
    }

    public M getAnyMessage() {
        final long actualTimestamp = System.currentTimeMillis();

        for (Map.Entry<P, List<M>> item : repo.entrySet()) {
            if (item.getValue().size() > 0 && (!inProcess.containsKey(item.getKey()) || inProcess.get(item.getKey()) < actualTimestamp)) {
                inProcess.put((P) item.getKey(), Long.MAX_VALUE);
                return (M) item.getValue().get(0);
            }
        }
        return null;
    }

    public void remove(final M msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Message can't be null");
        } else {
            repo.get(msg.getPartitionId()).remove(msg);
            if (repo.get(msg.getPartitionId()).size() == loWater && paused.contains(msg.getPartitionId())) {   // Prevent multiple resuming!
                paused.remove(msg.getPartitionId());
                manipulator.resume((P) msg.getPartitionId());
            }

            int commonMessages = 0;
            for (Map.Entry<P, List<M>> item : repo.entrySet()) {
                commonMessages += item.getValue().size();
            }

            if (commonMessages == loTotal && !pollable) {   // Prevent multiple pausing!
                manipulator.resumePolling();
                pollable = true;
            }
        }
    }

    public void releaseQueue(final M msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Message can't be null");
        } else {
            inProcess.remove(msg.getPartitionId());
        }
    }

    public void releaseQueueAfter(final M msg, final long delta) {
        if (msg == null) {
            throw new IllegalArgumentException("Message can't be null");
        } else {
            inProcess.put(msg.getPartitionId(), System.currentTimeMillis() + delta);
        }
    }
}
