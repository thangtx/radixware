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

package org.radixware.kernel.common.utils.net;

import java.io.IOException;
import java.net.ProtocolFamily;
//import java.net.ProtocolFamily;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.IllegalSelectorException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.io.pipe.PipeSelector;

/**
 * Selector that can handle both network channels and internal pipe channels
 */
public class RadixServerSelector extends AbstractSelector {

    private final PipeSelector pipeSelector;
    private final Selector defaultSelector;

    public RadixServerSelector(final PipeSelector pipeSelector, final Selector networkSelector) {
        super(PROVIDER);
        this.pipeSelector = pipeSelector;
        this.defaultSelector = networkSelector;
        pipeSelector.addListener(new PipeSelector.PipeSelectorListener() {

            @Override
            public void onDataAvailable() {
                defaultSelector.wakeup();
            }
        });
    }

    @Override
    protected void implCloseSelector() throws IOException {
        defaultSelector.close();
        pipeSelector.close();
    }

    @Override
    protected SelectionKey register(final AbstractSelectableChannel ch, final int ops, final Object att) {
        final Selector selector = getSelectorForChannel(ch);
        if (selector == null) {
            throw new IllegalSelectorException();
        }
        try {
            return ch.register(selector, ops, att);
        } catch (ClosedChannelException ex) {
            throw new RadixError("Error while registering channel", ex);
        }
    }

    @Override
    public Set<SelectionKey> keys() {
        final HashSet<SelectionKey> result = new HashSet<SelectionKey>(defaultSelector.keys());
        result.addAll(pipeSelector.keys());
        return Collections.unmodifiableSet(result);
    }

    @Override
    public Set<SelectionKey> selectedKeys() {
        return new UngrowableProxySet(defaultSelector.selectedKeys(), pipeSelector.selectedKeys());

    }

    @Override
    public int selectNow() throws IOException {
        return defaultSelector.selectNow() + pipeSelector.selectNow();
    }

    @Override
    public int select(final long timeout) throws IOException {
        int availableNow = pipeSelector.selectNow();
        if (availableNow > 0) {
            return availableNow + defaultSelector.selectNow();
        } else {
            final int defaultSelectorResult = defaultSelector.select(timeout);
            return defaultSelectorResult + pipeSelector.selectNow();
        }
    }

    @Override
    public int select() throws IOException {
        final int defaultResult = defaultSelector.select();
        return defaultResult + pipeSelector.selectNow();
    }

    @Override
    public Selector wakeup() {
        defaultSelector.wakeup();
        return this;
    }

    private Selector getSelectorForChannel(final AbstractSelectableChannel channel) {
        if (channel.provider() == pipeSelector.provider()) {
            return pipeSelector;
        } else if (channel.provider() == defaultSelector.provider()) {
            return defaultSelector;
        }
        return null;
    }

    public SelectionKey keyFor(final SelectableChannel channel) {
        if (channel.provider() == defaultSelector.provider()) {
            return channel.keyFor(defaultSelector);
        } else if (channel.provider() == pipeSelector.provider()) {
            return channel.keyFor(pipeSelector);
        }
        return null;
    }

    private static class UngrowableProxySet implements Set<SelectionKey> {

        private final Set<SelectionKey> setA;
        private final Set<SelectionKey> setB;

        public UngrowableProxySet(final Set<SelectionKey> setA, final Set<SelectionKey> setB) {
            this.setA = setA;
            this.setB = setB;
        }

        @Override
        public int size() {
            return setA.size() + setB.size();
        }

        @Override
        public boolean isEmpty() {
            return setA.isEmpty() && setB.isEmpty();
        }

        @Override
        public boolean contains(final Object o) {
            return setA.contains(o) || setB.contains(o);
        }

        @Override
        public Iterator<SelectionKey> iterator() {
            return new Iterator<SelectionKey>() {

                private final Iterator<SelectionKey> iteratorA = setA.iterator();
                private final Iterator<SelectionKey> iteratorB = setB.iterator();
                private Iterator<SelectionKey> currentIterator = null;

                @Override
                public boolean hasNext() {
                    return iteratorA.hasNext() || iteratorB.hasNext();
                }

                @Override
                public SelectionKey next() {
                    if (!hasNext()) {
                        throw new IllegalStateException("There is no next element");
                    }
                    if (iteratorA.hasNext()) {
                        currentIterator = iteratorA;
                        return iteratorA.next();
                    }
                    if (iteratorB.hasNext()) {
                        currentIterator = iteratorB;
                        return iteratorB.next();
                    }
                    throw new IllegalStateException("There is no next element");
                }

                @Override
                public void remove() {
                    if (currentIterator == null) {
                        throw new IllegalStateException("There is no element to remove");
                    }
                    currentIterator.remove();
                }
            };
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> T[] toArray(final T[] a) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean add(final SelectionKey e) {
            throw new UnsupportedOperationException("Adding elements to this set is prohibited");
        }

        @Override
        public boolean remove(final Object o) {
            if (!setA.remove(o)) {
                return setB.remove(o);
            }
            return true;
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            for (Object object : c) {
                if (!setA.contains(object) && !setB.contains(object)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(final Collection<? extends SelectionKey> c) {
            throw new UnsupportedOperationException("Adding elements to this set is prohibited");
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            boolean result = false;
            for (Object object : c) {
                result = setA.remove(object) || setB.remove(object);
            }
            return result;
        }

        @Override
        public void clear() {
            setA.clear();
            setB.clear();
        }
    }

    /**
     * Needed only for super() constructor
     */
    private static class VirtualSelectorProvider extends SelectorProvider {

        @Override
        public DatagramChannel openDatagramChannel() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Pipe openPipe() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public AbstractSelector openSelector() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ServerSocketChannel openServerSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public SocketChannel openSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
//
        @Override
        public DatagramChannel openDatagramChannel(ProtocolFamily arg0) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    private static final SelectorProvider PROVIDER = new VirtualSelectorProvider();
}
