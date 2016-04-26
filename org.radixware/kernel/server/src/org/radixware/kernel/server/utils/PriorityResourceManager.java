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
package org.radixware.kernel.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.radixware.kernel.common.enums.EPriority;

public class PriorityResourceManager {

    public static enum EQueuePriority {

        FIRST,
        SECOND;
    }
    private static final EPriority[] PRIORITIES = new EPriority[]{EPriority.NORMAL, EPriority.ABOVE_NORMAL, EPriority.HIGH, EPriority.VERY_HIGH, EPriority.CRITICAL};
    private static final long CHECK_HOLDERS_FOR_DEATH_PERIOD_MILLIS = 1000;
    //
    private final Map<Long, List<Ticket>> priority2Tickets = new HashMap<>();
    //Guarded by this
    private final Queue<TicketWaitRequest> firstWaitingList = new PriorityQueue<>();
    //Guarded by this
    private final Queue<TicketWaitRequest> secondWaitingList = new PriorityQueue<>();
    private Options options;
    private long lastCheckHoldersForDeathMillis = 0;

    public PriorityResourceManager() {
        for (EPriority p : PRIORITIES) {
            priority2Tickets.put(p.getValue(), new ArrayList<Ticket>());
        }
    }

    public Ticket requestTicket(long priority, long timeoutMillis, final EQueuePriority queuePriority) throws InterruptedException {
        return requestTicket(priority, timeoutMillis, queuePriority, 0);
    }

    /**
     *
     * @param timeoutMillis pass -1 to wait forever, 0 for no wait (the same as
     * {@linkplain  requestTicket(long)})
     * @return ticket or null in case timeout expires
     */
    public Ticket requestTicket(long priority, long timeoutMillis, final EQueuePriority queuePriority, final long queueOrderingKey) throws InterruptedException {
        final long normalizedPriority = normalizePriority(priority);
        final TicketWaitRequest waitRequest = new TicketWaitRequest(normalizedPriority, queueOrderingKey);
        synchronized (this) {
            Ticket result = requestTicketNow(priority);
            if (result != null || timeoutMillis == 0) {
                return result;
            } else {
                getWaitList(queuePriority).add(waitRequest);
            }
        }

        if (timeoutMillis == -1) {
            return waitRequest.queue.take();
        } else {
            final Ticket result = waitRequest.queue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            if (result == null) {
                synchronized (this) {
                    getWaitList(queuePriority).remove(waitRequest);
                    return waitRequest.queue.poll();
                }
            } else {
                return result;
            }
        }
    }

    private Queue<TicketWaitRequest> getWaitList(final EQueuePriority queuePriority) {
        switch (queuePriority) {
            case FIRST:
                return firstWaitingList;
            case SECOND:
                return secondWaitingList;
            default:
                throw new IllegalArgumentException("illegal priority: " + queuePriority);
        }
    }

    /**
     * @return Ticket of null if there is no ticket available at the moment
     */
    public synchronized Ticket requestTicketNow(long priority) {
        maybeReleaseTicketsFromDeadHolders();
        priority = normalizePriority(priority);
        if (hasTicketForPriority(priority)) {
            final Ticket t = new Ticket(priority);
            getTickets(priority).add(t);
            return t;
        }
        return null;
    }

    public synchronized void releaseTicket(final Ticket ticket) {
        if (ticket == null) {
            return;
        }
        priority2Tickets.get(ticket.getPriority()).remove(ticket);
        ticket.release();
        feedWaiters();
    }

    //should be called under synchronization by this
    private void feedWaiters() {
        for (EQueuePriority priority : EQueuePriority.values()) {
            final Iterator<TicketWaitRequest> it = getWaitList(priority).iterator();
            while (it.hasNext()) {
                final TicketWaitRequest rq = it.next();
                final Ticket result = requestTicketNow(rq.normalizedPriority);
                if (result != null) {
                    rq.queue.add(result);
                    it.remove();
                    return;
                }
            }
        }
    }

    public synchronized void setOptions(Options options) {
        this.options = options;
        feedWaiters();
    }

    private boolean hasTicketForPriority(long priority) {
        return getAvailableForPriority(priority) > getBusyForPriority(priority);
    }

    private long getAvailableForPriority(final long priority) {
        int available = 0;
        for (EPriority p : PRIORITIES) {
            if (p.getValue() <= priority) {
                available += getDeltaForPriority(p.getValue());
            }
        }
        return available;
    }

    private long getDeltaForPriority(final long priority) {
        if (options == null) {
            return Integer.MAX_VALUE;
        }
        if (priority <= EPriority.NORMAL.getValue()) {
            return options.normalCount;
        }
        if (priority <= EPriority.ABOVE_NORMAL.getValue()) {
            return options.aboveNormalCount;
        }
        if (priority <= EPriority.HIGH.getValue()) {
            return options.highCount;
        }
        if (priority <= EPriority.VERY_HIGH.getValue()) {
            return options.veryHighCount;
        }
        return options.criticalCount;
    }

    private long getBusyForPriority(final long priority) {
        long priorityToCheck = priority < EPriority.NORMAL.getValue() ? EPriority.NORMAL.getValue() : priority;
        long overloadedFromHigherPriority = 0;
        int busyFromThisOrLowerPriority = 0;
        for (int i = PRIORITIES.length - 1; i >= 0; i--) {
            if (PRIORITIES[i].getValue() > priorityToCheck) {
                overloadedFromHigherPriority = Math.max(0, getTickets(PRIORITIES[i]).size() + overloadedFromHigherPriority - getDeltaForPriority(PRIORITIES[i].getValue()));
            } else {
                busyFromThisOrLowerPriority += getTickets(PRIORITIES[i]).size();
            }
        }
        return busyFromThisOrLowerPriority + overloadedFromHigherPriority;
    }

    private List<Ticket> getTickets(final EPriority priority) {
        return getTickets(priority.getValue());
    }

    private List<Ticket> getTickets(final long priority) {
        return priority2Tickets.get(priority);
    }

    private long normalizePriority(final long priority) {
        if (priority < 1 || priority > 9) {
            return EPriority.NORMAL.getValue();
        }
        if (priority <= EPriority.NORMAL.getValue()) {
            return EPriority.NORMAL.getValue();
        }
        if (priority <= EPriority.ABOVE_NORMAL.getValue()) {
            return EPriority.ABOVE_NORMAL.getValue();
        }
        if (priority <= EPriority.HIGH.getValue()) {
            return EPriority.HIGH.getValue();
        }
        if (priority <= EPriority.VERY_HIGH.getValue()) {
            return EPriority.VERY_HIGH.getValue();
        }
        return EPriority.CRITICAL.getValue();
    }

    /**
     * Should be called under synchronized(this)
     */
    private void maybeReleaseTicketsFromDeadHolders() {
        if (System.currentTimeMillis() - lastCheckHoldersForDeathMillis > CHECK_HOLDERS_FOR_DEATH_PERIOD_MILLIS) {
            releaseTicketsFromDeadHolders();
            lastCheckHoldersForDeathMillis = System.currentTimeMillis();
        }
    }

    /**
     * Should be called under synchronized(this)
     */
    private void releaseTicketsFromDeadHolders() {
        for (List<Ticket> tickets : priority2Tickets.values()) {
            List<Ticket> toRelease = new ArrayList<>();
            for (Ticket t : tickets) {
                if (!t.isHolderAlive()) {
                    toRelease.add(t);
                }
            }
            tickets.removeAll(toRelease);
            for (Ticket ticket : toRelease) {
                ticket.release();
            }
        }
    }

    private static class TicketWaitRequest implements Comparable<Object> {

        public final ArrayBlockingQueue<Ticket> queue = new ArrayBlockingQueue<>(1);
        public final long normalizedPriority;
        public final long orderingKey;

        public TicketWaitRequest(final long normalizedPriority, final long queueOrderingKey) {
            this.normalizedPriority = normalizedPriority;
            this.orderingKey = queueOrderingKey;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof TicketWaitRequest) {
                long result = orderingKey - ((TicketWaitRequest) o).orderingKey;
                return result < 0 ? -1 : result == 0 ? 0 : 1;
            } else {
                return (int) (orderingKey - System.identityHashCode(o));
            }
        }
    }

    public static class Ticket {

        private volatile Callable<Boolean> holderAliveChecker;
        private final long priority;
        private volatile boolean released = false;

        private Ticket(final long priority) {
            this.priority = priority;
        }

        public long getPriority() {
            return priority;
        }

        public void setHolderAliveChecker(Callable<Boolean> holderAliveChecker) {
            this.holderAliveChecker = holderAliveChecker;
        }

        public boolean isHolderAlive() {
            Callable<Boolean> checkerSnapshot = holderAliveChecker;
            if (checkerSnapshot != null) {
                try {
                    return checkerSnapshot.call();
                } catch (Exception ex) {
                    //assume it is alive
                }
            }
            return true;
        }

        protected void release() {
            released = true;
        }

        public boolean isReleased() {
            return released;
        }

    }

    public static class Options {

        private final int normalCount;
        private final int aboveNormalCount;
        private final int highCount;
        private final int veryHighCount;
        private final int criticalCount;

        public Options(int normalCount, int aboveNormalCount, int highCount, int veryHighCount, int criticalCount) {
            this.normalCount = normalCount;
            this.aboveNormalCount = aboveNormalCount;
            this.highCount = highCount;
            this.veryHighCount = veryHighCount;
            this.criticalCount = criticalCount;
        }

        public int getNormalCount() {
            return normalCount;
        }

        public int getAboveNormalCount() {
            return aboveNormalCount;
        }

        public int getHighCount() {
            return highCount;
        }

        public int getVeryHighCount() {
            return veryHighCount;
        }

        public int getCriticalCount() {
            return criticalCount;
        }

        @Override
        public String toString() {
            return "{" + "normalCount=" + normalCount + ", aboveNormalCount=" + aboveNormalCount + ", highCount=" + highCount + ", veryHighCount=" + veryHighCount + ", criticalCount=" + criticalCount + '}';
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 19 * hash + this.normalCount;
            hash = 19 * hash + this.aboveNormalCount;
            hash = 19 * hash + this.highCount;
            hash = 19 * hash + this.veryHighCount;
            hash = 19 * hash + this.criticalCount;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Options other = (Options) obj;
            if (this.normalCount != other.normalCount) {
                return false;
            }
            if (this.aboveNormalCount != other.aboveNormalCount) {
                return false;
            }
            if (this.highCount != other.highCount) {
                return false;
            }
            if (this.veryHighCount != other.veryHighCount) {
                return false;
            }
            if (this.criticalCount != other.criticalCount) {
                return false;
            }
            return true;
        }
    }
}
