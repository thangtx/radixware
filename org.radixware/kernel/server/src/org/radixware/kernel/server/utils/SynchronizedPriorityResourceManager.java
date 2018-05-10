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
import java.util.concurrent.TimeUnit;
import org.radixware.kernel.common.enums.EPriority;

public class SynchronizedPriorityResourceManager implements IPriorityResourceManager {

    private static final EPriority[] PRIORITIES = new EPriority[]{EPriority.NORMAL, EPriority.ABOVE_NORMAL, EPriority.HIGH, EPriority.VERY_HIGH, EPriority.CRITICAL};
    private static final long CHECK_HOLDERS_FOR_DEATH_PERIOD_MILLIS = 1000;
    //
    private final Map<Integer, List<Ticket>> priority2Tickets = new HashMap<>();
    //Guarded by this
    private final Queue<TicketWaitRequest> waitingList = new PriorityQueue<>();
    //Guarded by this
    private Options options;
    private long lastCheckHoldersForDeathMillis = 0;

    public SynchronizedPriorityResourceManager() {
        for (EPriority p : PRIORITIES) {
            priority2Tickets.put(p.getValue().intValue(), new ArrayList<Ticket>());
        }
    }

    @Override
    public int getCapturedTicketsCount(int priority) {
        return priority2Tickets.get(Ticket.normalizePriority(priority)).size();
    }

    @Override
    public Ticket requestTicket(int priority, long timeoutMillis) throws InterruptedException {
        return doRequestTicket(priority, timeoutMillis, 0);
    }

    /**
     *
     * @param timeoutMillis pass -1 to wait forever, 0 for no wait (the same as
     * {@linkplain  requestTicket(long)})
     * @return ticket or null in case timeout expires
     */
    private Ticket doRequestTicket(int priority, long timeoutMillis, final long queueOrderingKey) throws InterruptedException {
        final int normalizedPriority = Ticket.normalizePriority(priority);
        final TicketWaitRequest waitRequest = new TicketWaitRequest(normalizedPriority, queueOrderingKey);
        synchronized (this) {
            Ticket result = requestTicketNow(priority);
            if (result != null || timeoutMillis == 0) {
                return result;
            } else {
                getWaitList().add(waitRequest);
            }
        }

        if (timeoutMillis == -1) {
            return waitRequest.queue.take();
        } else {
            final Ticket result = waitRequest.queue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            if (result == null) {
                synchronized (this) {
                    getWaitList().remove(waitRequest);
                    return waitRequest.queue.poll();
                }
            } else {
                return result;
            }
        }
    }

    private Queue<TicketWaitRequest> getWaitList() {
        return waitingList;
    }

    /**
     * @param priority
     * @return Ticket of null if there is no ticket available at the moment
     */
    @Override
    public synchronized Ticket requestTicketNow(int priority) {
        maybeReleaseTicketsFromDeadHolders();
        priority = Ticket.normalizePriority(priority);
        if (hasTicketForPriority(priority)) {
            final Ticket t = new Ticket(priority);
            getTickets(priority).add(t);
            return t;
        }
        return null;
    }

    @Override
    public synchronized boolean releaseTicket(final Ticket ticket) {
        if (ticket == null) {
            return false;
        }
        priority2Tickets.get(ticket.getPriority()).remove(ticket);
        final boolean result = ticket.release();
        feedWaiters();
        return result;
    }

    //should be called under synchronization by this
    private void feedWaiters() {
        final Iterator<TicketWaitRequest> it = getWaitList().iterator();
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
            return options.getNormalCount();
        }
        if (priority <= EPriority.ABOVE_NORMAL.getValue()) {
            return options.getAboveNormalCount();
        }
        if (priority <= EPriority.HIGH.getValue()) {
            return options.getHighCount();
        }
        if (priority <= EPriority.VERY_HIGH.getValue()) {
            return options.getVeryHighCount();
        }
        return options.getCriticalCount();
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
        return getTickets(priority.getValue().intValue());
    }

    private List<Ticket> getTickets(final int priority) {
        return priority2Tickets.get(priority);
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
        public final int normalizedPriority;
        public final long orderingKey;
        public final long createTimeMillis = System.currentTimeMillis();

        public TicketWaitRequest(final int normalizedPriority, final long queueOrderingKey) {
            this.normalizedPriority = normalizedPriority;
            this.orderingKey = queueOrderingKey;
        }

        @Override
        public int compareTo(Object o) {
            long result = orderingKey - ((TicketWaitRequest) o).orderingKey;
            if (result == 0) {
                result = this.createTimeMillis - ((TicketWaitRequest) o).createTimeMillis;
            }
            return result < 0 ? -1 : result == 0 ? 0 : 1;
        }
    }

}
