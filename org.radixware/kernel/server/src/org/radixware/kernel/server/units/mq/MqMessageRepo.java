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
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueManipulator;

public class MqMessageRepo<M extends MqMessage<?, P>, P> implements Closeable {

    private static final int CACHED_MESSAGES_IN_PARTITION_HIWATER = SystemPropUtils.getIntSystemProp("rdx.mq.cached.messages.in.partition.hiwater", 10);
    private static final int NON_EMPTY_PARTITIONS_HIWATER = SystemPropUtils.getIntSystemProp("rdx.mq.non.empty.partitions.hiwater", -1);
    private static final int TOTAL_CACHED_MESSAGES_HIGHWATER = SystemPropUtils.getIntSystemProp("rdx.mq.total.messages.hiwater", 1000);

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

        @Override
        public boolean canPausePartition() {
            return false;
        }

        @Override
        public boolean canPausePolling() {
            return false;
        }

        @Override
        public boolean canRestoreOffset() {
            return false;
        }

        @Override
        public void rememberOffset(MqMessage mess) {
        }

        @Override
        public boolean isOffsetRemembered(MqMessage mess) {
            return false;
        }
    };

    private final Map<P, List<M>> repo = new HashMap<>();
    private final Map<P, Long> inProcess = new HashMap<>();
    private final IMqQueueManipulator<P> manipulator;
    private final Set<P> paused = new HashSet<>();
    private boolean pollable = true;
    private final MqUnit unit;
    private final LocalTracer tracer;
    private final int cachedMessagesHighwater;
    private int totalMessages = 0;

    public MqMessageRepo(final IMqQueueManipulator<P> manipulator, MqUnit unit) {
        this(manipulator, unit, TOTAL_CACHED_MESSAGES_HIGHWATER);
    }

    public MqMessageRepo(final IMqQueueManipulator<P> manipulator, MqUnit unit, int cachedMessagesHighWater) {
        this.manipulator = manipulator == null ? EMPTY_MANIPULATOR : manipulator;
        this.unit = unit;
        this.tracer = unit.createTracer();
        this.cachedMessagesHighwater = cachedMessagesHighWater;
    }

    @Override
    public void close() throws IOException {
    }

    public int getCachedMessagesHighwater() {
        return cachedMessagesHighwater;
    }

    public void storeMessage(final M msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Message can't be null");
        } else {
            if (!repo.containsKey(msg.getPartitionId())) {
                repo.put((P) msg.getPartitionId(), new ArrayList<M>());
            }
            
            final boolean limitsExceeded = (totalMessages + 1) > TOTAL_CACHED_MESSAGES_HIGHWATER || (repo.get(msg.getPartitionId()).size() + 1) > CACHED_MESSAGES_IN_PARTITION_HIWATER;
            if (canStoreMessage(msg, limitsExceeded)) {
                repo.get(msg.getPartitionId()).add(msg);
                totalMessages++;
            }
            if (limitsExceeded) {
                if (manipulator.canRestoreOffset() && !manipulator.isOffsetRemembered(msg)) {
                    manipulator.rememberOffset(msg);
                }
                
                if (!repo.get(msg.getPartitionId()).isEmpty() && manipulator.canPausePartition() && !paused.contains(msg.getPartitionId())) {   // Prevent multiple pausing!
                    paused.add((P) msg.getPartitionId());
                    manipulator.pause((P) msg.getPartitionId());
                    tracer.put(EEventSeverity.DEBUG, "Pausing partition '" + String.valueOf(msg.getPartitionId()), null, null, false);
                }
            }

            if (manipulator.canPausePolling() && !canPollOverall() && pollable) {   // Prevent multiple pausing!
                manipulator.pausePolling();
                pollable = false;
                tracer.put(EEventSeverity.DEBUG, "Pausing overall polling", null, null, false);
            }
        }
    }
    
    private boolean canStoreMessage(final M msg, final boolean limitsExceeded) {
        if (!limitsExceeded) {
            if (manipulator.canRestoreOffset()) {
                return !manipulator.isOffsetRemembered(msg);
            } else {
                return true;
            }
        } else {
            return !manipulator.canRestoreOffset();
        }
    }

    private boolean canPollOverall() {
        return (cachedMessagesHighwater < 0 || totalMessages < cachedMessagesHighwater)
                && (NON_EMPTY_PARTITIONS_HIWATER < 0 || getNonEmptyPartitionsCount() < NON_EMPTY_PARTITIONS_HIWATER);
    }

    public M getAnyMessage() {
        final long curMillis = System.currentTimeMillis();

        final List<Map.Entry<P, List<M>>> bestPartitions = new ArrayList<>();

        for (Map.Entry<P, List<M>> item : repo.entrySet()) {
            if (!item.getValue().isEmpty() && (!inProcess.containsKey(item.getKey()) || inProcess.get(item.getKey()) < curMillis)) {
                if (bestPartitions.isEmpty() || item.getValue().get(0).getRegTimeMillis() <= bestPartitions.get(0).getValue().get(0).getRegTimeMillis()) {
                    if (!bestPartitions.isEmpty() && item.getValue().get(0).getRegTimeMillis() < bestPartitions.get(0).getValue().get(0).getRegTimeMillis()) {
                        bestPartitions.clear();
                    }
                    bestPartitions.add(item);
                }
            }
        }
        if (!bestPartitions.isEmpty()) {
            Map.Entry<P, List<M>> bestPartition = bestPartitions.get((int) (bestPartitions.size() * Math.random()));
            inProcess.put((P) bestPartition.getKey(), Long.MAX_VALUE);
            return (M) bestPartition.getValue().get(0);
        }
        return null;
    }

    public void remove(final M msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Message can't be null");
        } else {
            repo.get(msg.getPartitionId()).remove(msg);
            if (repo.get(msg.getPartitionId()).isEmpty() && paused.contains(msg.getPartitionId())) {   // Prevent multiple resuming!
                paused.remove(msg.getPartitionId());
                manipulator.resume((P) msg.getPartitionId());
                tracer.put(EEventSeverity.DEBUG, "Resuming partition '" + String.valueOf(msg.getPartitionId()), null, null, false);
            }

            totalMessages--;

            if (canPollOverall() && !pollable) {   // Prevent multiple pausing!
                manipulator.resumePolling();
                pollable = true;
                tracer.put(EEventSeverity.DEBUG, "Resuming overall polling", null, null, false);
            }
        }
    }

    private int getNonEmptyPartitionsCount() {
        int nonEmptyParts = 0;
        for (Map.Entry<P, List<M>> item : repo.entrySet()) {
            if (!item.getValue().isEmpty()) {
                nonEmptyParts++;
            }
        }
        return nonEmptyParts;
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
