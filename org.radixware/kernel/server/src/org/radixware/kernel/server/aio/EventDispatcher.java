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
package org.radixware.kernel.server.aio;

import org.radixware.kernel.common.utils.net.AioUtils;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.net.RadixServerSelector;

public class EventDispatcher {
    //Attention: never use SelectableChannel.keyFor(selector) to obtain
    //the key for the channel inside of the EventDispatcher, because 
    //RadixServerSelector is a virtual selector that incapsulates pipe selector 
    //and default selector, so actual channel has no knowledge about it and will
    //return null. Use selector.keyFor(SelectableChannel channel) instead. 

//	---------------------------------------- EVENTS -------------------------------------------------------------------	
    static public final class TimerEvent extends Event {

        @Override
        public Object getSource() {
            return null;
        }

        @Override
        public boolean matchs(Object o) {
            throw new RadixError("Abstract");
        }
    }

    static public abstract class ChannelEvent extends Event {

        SelectableChannel source;

        public ChannelEvent(SelectableChannel source) {
            this.source = source;
        }

        @Override
        public SelectableChannel getSource() {
            return source;
        }

        @Override
        public boolean matchs(Object o) {
            return o.getClass() == getClass() && source == ((ChannelEvent) o).source;
        }
    }

    static public final class AcceptEvent extends ChannelEvent {

        public AcceptEvent(final SelectableChannel source) {
            super(source);
        }
    }

    static public final class ConnectEvent extends ChannelEvent {

        public ConnectEvent(final SelectableChannel source) {
            super(source);
        }
    }

    static public final class ReadEvent extends ChannelEvent {

        private Socket sourceSocket = null;

        /**
         * @param source SocketChannel which will generate ReadEvent
         * @param sourceSocket Socket associated with event's source, is used
         * with ssl connections. Provide <code>null</code> if it is not needed.
         */
        public ReadEvent(SelectableChannel source, Socket sourceSocket) {
            super(source);
            this.sourceSocket = sourceSocket;
        }

        public Socket getSourceSocket() {
            return sourceSocket;
        }
    }

    static public final class WriteEvent extends ChannelEvent {

        public WriteEvent(SelectableChannel source) {
            super(source);
        }
    }

//	------------------------------------ Public -----------------------------------------------------	
    public EventDispatcher() throws IOException {
        this(AioUtils.openSelector());
    }

    public EventDispatcher(final RadixServerSelector selector) throws IOException {
        timerSubscribers = new PriorityQueue<>();
        this.selector = selector;
    }

    public void process() throws IOException {
        TimerSubscription ts = timerSubscribers.peek();
        final long time = System.currentTimeMillis();
        int res;
        if (ts == null) {
            res = selector.select();
        } else if (ts.time > time) {
            res = selector.select(ts.time - time);
        } else {
            res = selector.selectNow();
        }
        while (res > 0) {
            processSelectedKeys();
            ts = timerSubscribers.peek();
            if (ts != null && ts.time <= System.currentTimeMillis() && ts.iter != iteration) {//if immediate timer event was scheduled in this iteration, perform one more select() before processing it to cleanup closed sockets, etc.
                break;
            }
            res = selector.selectNow();

        }
        while (true) {
            ts = timerSubscribers.peek();
            if (ts == null || ts.time > System.currentTimeMillis()) {
                break;
            }
            timerSubscribers.poll();
            if (ts.eventSubscription != null) { //timeout
                eventSubscriptions.remove(ts.eventSubscription);
                ts.eventSubscription.event.isExpired = true;
                ts.eventSubscription.handler.onEvent(ts.eventSubscription.event);
            } else {
                ts.handler.onEvent(new TimerEvent());
            }
        }
    }

    public boolean hasEventSubscriptions(final Object source) {
        return eventSubscriptions.getSubsriptionsCountForSource(source) > 0;
    }

    /**
     * Time of the nearest timer subscription for this handler, or -1 if there
     * is no such.
     *
     * @param handler
     * @return
     */
    public long getNearestTimerSubscription(final EventHandler handler) {
        if (handler == null) {
            return -1;
        }
        long result = -1;
        for (TimerSubscription ts : timerSubscribers) {
            if (ts.eventSubscription == null && ts.handler == handler) {
                if (result == -1) {
                    result = ts.time;
                } else {
                    result = Math.min(result, ts.time);
                }
            }
        }
        return result;
    }

    private void processSelectedKeys() throws RadixError {
        iteration++;
        final Set<SelectionKey> readyKeys = selector.selectedKeys();
        final Iterator<SelectionKey> readyIter = readyKeys.iterator();
        while (readyIter.hasNext()) {
            final SelectionKey key = readyIter.next();
            final SelectableChannel channel = key.channel();
            try {
                if (key.isValid()) {
                    final int readyOps = key.readyOps();
                    if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
                        final AcceptEvent acceptEvent = new AcceptEvent(channel);
                        if (!notify(acceptEvent)) {
                            throw new RadixError("Unexpected OP_ACCEPT");
                        } else {
                            if (eventSubscriptions.getByEvent(acceptEvent) == null) { // no other subscribers
                                removeInterestOpt(channel, SelectionKey.OP_ACCEPT);
                            }
                        }
                    }
                    if ((readyOps & SelectionKey.OP_CONNECT) != 0) {
                        final ConnectEvent connectEvent = new ConnectEvent(channel);
                        if (!notify(connectEvent)) {
                            throw new RadixError("Unexpected OP_CONNECT");
                        } else {
                            if (eventSubscriptions.getByEvent(connectEvent) == null) { // no other subscribers
                                removeInterestOpt(channel, SelectionKey.OP_CONNECT);
                            }
                        }
                    }
                    if ((readyOps & SelectionKey.OP_READ) != 0) {
                        if (!notify(new ReadEvent(channel, null))) {
                            throw new RadixError("Unexpected OP_READ");
                        }
                    }
                    //OP_READ processing could close the channel, so additional key.isValid() check required
                    if (key.isValid() && ((readyOps & SelectionKey.OP_WRITE) != 0)) {
                        final WriteEvent writeEvent = new WriteEvent(channel);
                        if (!notify(writeEvent)) {
                            throw new RadixError("Unexpected OP_WRITE");
                        } else {
                            if (eventSubscriptions.getByEvent(writeEvent) == null) { // no other subscribers
                                removeInterestOpt(channel, SelectionKey.OP_WRITE);
                            }
                        }

                    }
                } else {
                    key.cancel();
                }
            } catch (CancelledKeyException e) {
                continue;
            } finally {
                readyIter.remove();
            }
        }
    }

    private void removeInterestOpt(final SelectableChannel socketChannel, final int opt) {
        // no other subscribers
        final SelectionKey selectKey = selector.keyFor(socketChannel);
        //unsubscribe from OP_WRITE to prevent an infinitive loop
        //see http://forums.oracle.com/forums/thread.jspa?messageID=9220480&#9220480
        selectKey.interestOps(selectKey.interestOps() & ~opt);
    }

    public void waitEvent(final Event event, final EventHandler handler, final long finishTime) {
        try {
            if (event.getClass() == AcceptEvent.class) {
                addInterestOpt(((AcceptEvent) event).source, SelectionKey.OP_ACCEPT);
            } else if (event.getClass() == ConnectEvent.class) {
                addInterestOpt(((ConnectEvent) event).source, SelectionKey.OP_CONNECT);
            } else if (event.getClass() == ReadEvent.class) {
                addInterestOpt(((ReadEvent) event).source, SelectionKey.OP_READ);
            } else if (event.getClass() == WriteEvent.class) {
                addInterestOpt(((WriteEvent) event).source, SelectionKey.OP_WRITE);
            } else if (event.getClass() == TimerEvent.class) {
                final TimerSubscription ts = new TimerSubscription(finishTime, handler, null, iteration);
                timerSubscribers.add(ts);
            }
        } catch (ClosedChannelException e) {
            throw new RadixError(e.getMessage(), e);
        }

        if (event.getClass() != TimerEvent.class) {
            final EventSubscription es = new EventSubscription();
            es.event = event;
            es.handler = handler;
            if (!eventSubscriptions.add(es)) {
                throw new RadixError("The event (class: " + event.getClass().getName() + ", source: " + event.getSource() + ") is already waited");
            }
            if (finishTime > 0) {
                final TimerSubscription ts = new TimerSubscription(finishTime, handler, es, iteration);
                timerSubscribers.add(ts);
                es.timeoutSubscription = ts;
            }
        }
    }

    private void addInterestOpt(final SelectableChannel channel, final int opt) throws ClosedChannelException {
        final SelectionKey keyFor = selector.keyFor(channel);
        final int curOpt = keyFor == null ? 0 : keyFor.interestOps();
        channel.register(keyFor == null ? selector : keyFor.selector(), curOpt | opt);
    }

    public boolean notify(final Event event) {
        final EventSubscription es = eventSubscriptions.getByEventAndRemove(event);
        if (es == null) {
            return false;
        }
        if (event instanceof ReadEvent) //fill the generated event with a socket associated with it's source
        {
            ((ReadEvent) event).sourceSocket = ((ReadEvent) es.event).getSourceSocket();
        }
        if (es.timeoutSubscription != null) {
            timerSubscribers.remove(es.timeoutSubscription);
        }
        es.handler.onEvent(event);
        return true;
    }

    public boolean unsubscribe(final Event event) {
        if (event != null && event.getClass() == TimerEvent.class) {
            final IllegalArgumentException exception = new IllegalArgumentException();
            LogFactory.getLog(EventDispatcher.class).warn("EventDispatcher.unsubscribe(TimerEvent) has no effect", exception);
        }
        final EventSubscription es = eventSubscriptions.getByEventAndRemove(event);
        if (es == null) {
            return false;
        }
        if (es.timeoutSubscription != null) {
            timerSubscribers.remove(es.timeoutSubscription);
        }
        return true;
    }

    public void unsubscribeFromTimerEvents(final EventHandler handler) {
        if (handler == null) {
            return;
        }

        Iterator<TimerSubscription> it = timerSubscribers.iterator();
        while (it.hasNext()) {
            final TimerSubscription ts = it.next();
            if (ts.handler == handler && ts.eventSubscription == null) {
                it.remove();
            }
        }
    }

    public void unsubscribe(final SelectableChannel channel) {
        unsubscribeFromSource(channel);
    }

    public void unsubscribeFromSource(final Object source) {
        unsubscribeFromSource(source, true);
    }

    public void unsubscribeFromSource(final Object source, boolean cancelKey) {
        if (source instanceof SelectableChannel && cancelKey) {
            final SelectionKey key = selector.keyFor((SelectableChannel) source);
            if (key != null) {
                key.cancel();
            }
        }
        final List<EventSubscription> list = eventSubscriptions.removeSourceSubsriptions(source);
        if (list != null) {
            for (EventSubscription es : list) {
                if (es.event.getSource() == source || es.event.getSource().equals(source)) {
                    if (es.timeoutSubscription != null) { //RADIX-3151
                        timerSubscribers.remove(es.timeoutSubscription);
                    }
                }
            }
        }
    }

    /**
     * Causes the first process operation that has not yet returned to return
     * immediately.
     */
    public void wakeup() {
        selector.wakeup();
    }
//------------------------------------- Internal -------------------------------------------------------------

    static class TimerSubscription implements Comparable {

        private final long time;
        private final EventHandler handler;
        private final EventSubscription eventSubscription;
        private final long iter;

        public TimerSubscription(long time, EventHandler handler, EventSubscription eventSubscription, long iter) {
            this.time = time;
            this.handler = handler;
            this.eventSubscription = eventSubscription;
            this.iter = iter;
        }

        @Override
        public int compareTo(final Object o) {
            final TimerSubscription o2 = (TimerSubscription) o;
            return time == o2.time ? (iter == o2.iter ? 0 : (iter > o2.iter ? 1 : -1)) : (time > o2.time ? 1 : -1);
        }
    }

    static class EventSubscription {

        Event event;
        EventHandler handler;
        TimerSubscription timeoutSubscription;
    }
    private final RadixServerSelector selector;
    private final EventSubscriptions eventSubscriptions = new EventSubscriptions();
    private final PriorityQueue<TimerSubscription> timerSubscribers;
    protected long iteration;

    public final long getEventSubscribersCount() {
        return eventSubscriptions.size();
    }

    public final long getNonIgnorableOnStopEventSubscribersCount() {
        int ignorableOnStopCount = 0;
        for (EventSubscription subscription : eventSubscriptions.getAllSubscriptions()) {
            if (subscription.event != null && subscription.event.ignorableOnClose) {
                ignorableOnStopCount++;
            }
        }
        return getEventSubscribersCount() - ignorableOnStopCount;
    }

    /**
     * debug method
     *
     * @param channel
     */
    public final void printChannelKeys(final String comment, final SocketChannel channel) {
        final SelectionKey key = selector.keyFor(channel);
        System.out.println(comment + " : channel " + channel.toString() + " valid keys {\n");
        try {
            if (key.isValid()) {
                final int readyOps = key.readyOps();
                if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
                    System.out.println("OP_ACCEPT");
                }
                if ((readyOps & SelectionKey.OP_CONNECT) != 0) {
                    System.out.println("OP_CONNECT");
                }
                if ((readyOps & SelectionKey.OP_READ) != 0) {
                    System.out.println("OP_READ");
                }
                if ((readyOps & SelectionKey.OP_WRITE) != 0) {
                    System.out.println("OP_WRITE");
                }
            }
        } catch (CancelledKeyException e) {
            return;
        }
        System.out.println("}");
        System.out.flush();
    }

    public void close() {
        try {
            selector.close();
        } catch (IOException ex) {
            //do nothing
        }
    }

    private static class EventSubscriptions {

        private static final Object NULL_SOURCE = new Object();
        private final Map<Object, List<EventSubscription>> subscriptionsBySource = new HashMap<>();

        /**
         * @return true if event of this subscription was not waited and
         * subscription is actually added, false otherwise
         */
        public boolean add(EventSubscription s) {
            final Object sourceKey = maskNullSource(s.event.getSource());
            List<EventSubscription> list = subscriptionsBySource.get(sourceKey);
            if (list == null) {
                list = new ArrayList<>();
                subscriptionsBySource.put(sourceKey, list);
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).event.matchs(s.event)) {
                    return false;
                }
            }
            list.add(s);
            return true;
        }

        public EventSubscription getByEvent(final Event e) {
            return getByEventImpl(e, false);
        }

        public EventSubscription getByEventAndRemove(final Event e) {
            return getByEventImpl(e, true);
        }

        private EventSubscription getByEventImpl(final Event e, final boolean remove) {
            if (e == null) {
                return null;
            }
            final Object sourceKey = maskNullSource(e.getSource());
            final List<EventSubscription> list = subscriptionsBySource.get(sourceKey);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).event.matchs(e)) {
                        if (remove) {
                            final EventSubscription result = list.remove(i);
                            if (list.isEmpty()) {
                                subscriptionsBySource.remove(sourceKey);
                            }
                            return result;
                        }
                        return list.get(i);
                    }
                }
            }
            return null;
        }

        public int getSubsriptionsCountForSource(final Object source) {
            List<EventSubscription> list = subscriptionsBySource.get(maskNullSource(source));
            return list == null ? 0 : list.size();
        }

        public List<EventSubscription> getSubscriptionsForSource(final Object source) {
            return subscriptionsBySource.get(maskNullSource(source));
        }

        public List<EventSubscription> removeSourceSubsriptions(final Object source) {
            return subscriptionsBySource.remove(maskNullSource(source));
        }

        boolean remove(final EventSubscription es) {
            if (es == null || es.event == null) {
                return false;
            }
            final Object sourceKey = maskNullSource(es.event.getSource());
            final List<EventSubscription> list = subscriptionsBySource.get(sourceKey);
            if (list != null) {
                final boolean result = list.remove(es);
                if (list.isEmpty()) {
                    subscriptionsBySource.remove(sourceKey);
                }
                return result;
            }
            return false;

        }

        public int size() {
            int result = 0;
            for (List<EventSubscription> list : subscriptionsBySource.values()) {
                result += list.size();
            }
            return result;
        }

        /**
         * @return new List containing references to all subscriptions
         */
        public List<EventSubscription> getAllSubscriptions() {
            final List<EventSubscription> result = new ArrayList<>();
            for (List<EventSubscription> list : subscriptionsBySource.values()) {
                result.addAll(list);
            }
            return result;
        }

        private Object maskNullSource(final Object source) {
            return source == null ? NULL_SOURCE : source;
        }
    }
}
