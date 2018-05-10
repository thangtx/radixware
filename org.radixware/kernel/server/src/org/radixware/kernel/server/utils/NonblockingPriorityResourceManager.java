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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.radixware.kernel.common.enums.EPriority;
import org.radixware.kernel.common.utils.SystemPropUtils;

public class NonblockingPriorityResourceManager implements IPriorityResourceManager {

    private static final int FEED_WAITERS_CANDIDATES = SystemPropUtils.getIntSystemProp("rdx.feed.waiters.candidates", 1);
    private static final int TICKET_WAIT_ITERATION_SLEEP_TIME = SystemPropUtils.getIntSystemProp("rdx.ticket.wait.iteration.sleep.time", 500);
    //
    private static final EPriority[] PRIORITIES = new EPriority[]{EPriority.NORMAL, EPriority.ABOVE_NORMAL, EPriority.HIGH, EPriority.VERY_HIGH, EPriority.CRITICAL};
    private static final long CHECK_HOLDERS_FOR_DEATH_PERIOD_MILLIS = 1000;
    private final Map<Integer, Queue<Ticket>> priority2Tickets = new HashMap<>();
    private final Map<Integer, AtomicInteger> priority2Counters = new HashMap<>();
    private final Queue<TicketWaitRequest> waitingList = new ConcurrentLinkedQueue<>();
    private volatile Options options;
    private final AtomicLong lastCheckHoldersForDeathMillis = new AtomicLong();

    public NonblockingPriorityResourceManager() {
        for (EPriority p : PRIORITIES) {
            priority2Tickets.put(p.getValue().intValue(), new ConcurrentLinkedQueue<Ticket>());
            priority2Counters.put(p.getValue().intValue(), new AtomicInteger(0));
        }
    }

    @Override
    public int getCapturedTicketsCount(int priority) {
        return getCounter(Ticket.normalizePriority(priority)).get();
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

        Ticket result = requestTicketNow(priority);

        if (result != null || timeoutMillis == 0) {
//            System.out.println(result + " served to " + Thread.currentThread().getName());
            return result;
        }

        final TicketWaitRequest waitRequest = new TicketWaitRequest(normalizedPriority, queueOrderingKey);

        getWaitList().add(waitRequest);

        long iterations = timeoutMillis < 0 ? Integer.MAX_VALUE : timeoutMillis / TICKET_WAIT_ITERATION_SLEEP_TIME;
        if (iterations == 0) {
            iterations++;
        }
        for (long i = 0; i < iterations; i++) {
            result = waitRequest.exchange.poll(TICKET_WAIT_ITERATION_SLEEP_TIME, TimeUnit.MILLISECONDS);
            if (result == null && i < iterations - 1) {
                feedWaiters();
            }
            if (result != null) {
                return result;
            }
        }
        getWaitList().remove(waitRequest);
        return waitRequest.exchange.poll();
    }

    private Queue<TicketWaitRequest> getWaitList() {
        return waitingList;
    }

    /**
     * @return Ticket of null if there is no ticket available at the moment
     */
    @Override
    public Ticket requestTicketNow(int priority) {
        maybeReleaseTicketsFromDeadHolders();
        final Options optsSnapshot = options;
        if (options == null) {
            return null;
        }
        for (int p = Ticket.normalizePriority(priority); p >= EPriority.NORMAL.getValue(); p--) {
            final AtomicInteger counter = getCounter(p);
            if (counter.incrementAndGet() <= optsSnapshot.getDeltaForPriority(p)) {
                final Ticket t = new Ticket(p);
                getTickets(p).add(t);
//                System.out.println(t + " allocated by " + Thread.currentThread().getName());
                return t;
            } else {
                counter.decrementAndGet();
            }
        }
        return null;
    }

    @Override
    public boolean releaseTicket(final Ticket ticket) {
        boolean result = doReleaseTicket(ticket);
        feedWaiters();
        return result;
    }

    private boolean doReleaseTicket(final Ticket ticket) {
        if (ticket == null) {
            return false;
        }
        priority2Tickets.get(ticket.getPriority()).remove(ticket);
        if (ticket.release()) {
//            System.out.println(Thread.currentThread().getName() + " released " + ticket);
            getCounter(ticket.getPriority()).decrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * Try to feed waiters
     */
    private void feedWaiters() {
        final PriorityQueue<TicketWaitRequest> candidates = new PriorityQueue<>(FEED_WAITERS_CANDIDATES + 1, new Comparator<TicketWaitRequest>() {

            @Override
            public int compare(TicketWaitRequest o1, TicketWaitRequest o2) {
                return o2.compareTo(o1);
            }
        });

        for (TicketWaitRequest waitRequest : getWaitList()) {
            candidates.add(waitRequest);
            if (candidates.size() > FEED_WAITERS_CANDIDATES) {
                candidates.remove();
            }
        }

        Ticket ticket = null;
        TicketWaitRequest[] candidatesArr = candidates.toArray(new TicketWaitRequest[0]);
        for (int i = candidatesArr.length - 1; i >= 0; i--) {
            final TicketWaitRequest waitRequest = candidatesArr[i];
            if (ticket == null || ticket.getPriority() != waitRequest.normalizedPriority) {
                if (ticket != null) {
                    doReleaseTicket(ticket);
                }
                ticket = requestTicketNow(waitRequest.normalizedPriority);
            }
            if (ticket != null && waitRequest.serve(ticket)) {
                getWaitList().remove(waitRequest);
                return;
            }
        }
        if (ticket != null) {
            doReleaseTicket(ticket);
        }
    }

    @Override
    public void setOptions(Options options) {
        this.options = options;
    }

    private Queue<Ticket> getTickets(final int priority) {
        return priority2Tickets.get(priority);
    }

    private void maybeReleaseTicketsFromDeadHolders() {
        final long lastCheckMillis = lastCheckHoldersForDeathMillis.get();
        final long curTimeMillis = System.currentTimeMillis();
        if (Math.abs(lastCheckMillis - curTimeMillis) > CHECK_HOLDERS_FOR_DEATH_PERIOD_MILLIS) {
            if (lastCheckHoldersForDeathMillis.compareAndSet(lastCheckMillis, curTimeMillis)) {
                releaseTicketsFromDeadHolders();
            }
        }
    }

    private void releaseTicketsFromDeadHolders() {
        for (Queue<Ticket> tickets : priority2Tickets.values()) {
            List<Ticket> toRelease = new ArrayList<>();
            for (Ticket t : tickets) {
                if (!t.isHolderAlive()) {
                    toRelease.add(t);
                }
            }
            tickets.removeAll(toRelease);
            for (Ticket ticket : toRelease) {
                if (ticket.release()) {
                    final AtomicInteger counter = getCounter(ticket.getPriority());
                    if (counter != null) {
                        counter.decrementAndGet();
                    }
                }
            }
        }
    }

    private AtomicInteger getCounter(final int priority) {
        return priority2Counters.get(priority);
    }

    private static class TicketWaitRequest implements Comparable<Object> {

        public final ArrayBlockingQueue<Ticket> exchange = new ArrayBlockingQueue<>(1);
        public final int normalizedPriority;
        public final long orderingKey;
        public final AtomicBoolean served = new AtomicBoolean(false);
        public final Thread creator;
        public final long createTimeMillis = System.currentTimeMillis();

        public TicketWaitRequest(final int normalizedPriority, final long queueOrderingKey) {
            this.normalizedPriority = normalizedPriority;
            this.orderingKey = queueOrderingKey;
            this.creator = Thread.currentThread();
        }

        public boolean serve(final Ticket ticket) {
            if (!served.getAndSet(true)) {
//                System.out.println(ticket + " served to " + creator.getName());
                exchange.add(ticket);
                return true;
            }
            return false;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof TicketWaitRequest) {
                TicketWaitRequest other = (TicketWaitRequest) o;
                long result;
                if (this.normalizedPriority != other.normalizedPriority) {
                    result = other.normalizedPriority - this.normalizedPriority;
                } else if (this.orderingKey != other.orderingKey) {
                    result = this.orderingKey - other.orderingKey;
                } else {
                    result = this.createTimeMillis - other.createTimeMillis;
                }
                return result < 0 ? -1 : result == 0 ? 0 : 1;
            } else {
                throw new IllegalArgumentException("Attemt to compare TicketWaitRequest with " + (o == null ? "null" : o.getClass().getName()));
            }
        }
    }

}
