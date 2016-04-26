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

package org.radixware.kernel.common.utils.io.pipe;

import java.io.IOException;
import java.net.ProtocolFamily;
//import java.net.ProtocolFamily;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.IllegalSelectorException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelectionKey;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


public class PipeSelector extends AbstractSelector {

    private static final SelectorProvider PROVIDER = new Provider();
    private final Set<SelectionKey> keys = new CopyOnWriteArraySet();
    private final List<PipeSelectorListener> listeners = new CopyOnWriteArrayList<PipeSelectorListener>();
    private volatile Set<SelectionKey> selectedKeys = new ConcurrentSkipListSet<SelectionKey>();
    private final Object readySem = new Object();

    public PipeSelector() {
        super(selectorProvider());
    }

    @Override
    protected void implCloseSelector() throws IOException {
        for (SelectionKey k : keys) {
            deregisterKey(k);
        }
        cancelledKeys().clear();
        keys.clear();
        selectedKeys.clear();
    }

    @Override
    protected SelectionKey register(
            final AbstractSelectableChannel ch,
            final int ops,
            final Object att) {
        if (!(ch instanceof PipeSelectorChannel)) {
            throw new IllegalSelectorException();
        }
        final PipeSelectorChannel pch = (PipeSelectorChannel) ch;
        final SelectionKey k = new SelectionKeyImpl(ch, ops, 0);
        pch.registerSelector(this);
        keys.add(k);
        return k;
    }

    public void addListener(final PipeSelectorListener listener) {
        listeners.add(listener);
    }

    @Override
    public Set<SelectionKey> keys() {
        return Collections.unmodifiableSet(keys);
    }

    @Override
    public Set<SelectionKey> selectedKeys() {
        return selectedKeys;
    }

    @Override
    public int selectNow() throws IOException {
        return doSelect(0);
    }

    @Override
    public int select(final long timeout) throws IOException {
        return doSelect(timeout);
    }

    @Override
    public int select() throws IOException {
        return doSelect(-1);
    }

    private void deregisterKey(final SelectionKey key) {
        ((PipeSelectorChannel) key.channel()).unregisterSelector(this);
        deregister((AbstractSelectionKey) key);
    }

    private void processCancelledKeys() {
        for (SelectionKey key : cancelledKeys()) {
            deregisterKey(key);
            keys.remove(key);
            selectedKeys.remove(key);
        }
        cancelledKeys().clear();
    }

    private int doSelect(final long timeoutMillis) throws ClosedByInterruptException {
        processCancelledKeys();
        selectReadyKeys();
        if (selectedKeys.isEmpty() && timeoutMillis != 0) {
            if (timeoutMillis < 0) {
                synchronized (readySem) {
                    selectReadyKeys();
                    while (selectedKeys.isEmpty()) {
                        try {
                            readySem.wait();
                            selectReadyKeys();
                        } catch (InterruptedException ex) {
                            throw new ClosedByInterruptException();
                        }
                    }
                }
            } else {
                final Timeout timeout = new Timeout(timeoutMillis);
                try {
                    long millisLeft = timeout.millisLeft();
                    synchronized (readySem) {
                        selectReadyKeys();
                        while (selectedKeys.isEmpty() && millisLeft > 0) {
                            readySem.wait(millisLeft);
                            selectReadyKeys();
                            millisLeft = timeout.millisLeft();
                        }
                    }
                } catch (InterruptedException ex) {
                    throw new ClosedByInterruptException();
                }
            }
        }
        processCancelledKeys();
        return selectedKeys.size();
    }

    private int selectReadyKeys() {
        int updatedCount = 0;
        for (SelectionKey key : keys) {
            final int readyOps = ((PipeSelectorChannel) key.channel()).readyOps();
            final int selectedOps = key.interestOps() & readyOps;
            if (selectedOps > 0) {
                if (!selectedKeys.contains(key) || key.readyOps() != selectedOps) {
                    updatedCount++;
                }
                selectedKeys.add(key);
            }
            ((SelectionKeyImpl) key).setReadyOps(selectedOps);
        }
        return updatedCount;
    }

    @Override
    public PipeSelector wakeup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void notify(final SelectionKey k, final int readyOps) {
        if (k.isValid() && (k.interestOps() & readyOps) > 0) {
            notifyListeners();
            synchronized (readySem) {
                readySem.notifyAll();
            }
        }
    }

    private void notifyListeners() {
        for (PipeSelectorListener listener : listeners) {
            listener.onDataAvailable();
        }
    }

    private class SelectionKeyImpl extends AbstractSelectionKey implements Comparable<SelectionKey> {

        private final SelectableChannel channel;
        private volatile int interestOps;
        private volatile int readyOps;

        public SelectionKeyImpl(final SelectableChannel channel, final int interestOps, final int readyOps) {
            this.channel = channel;
            this.interestOps = interestOps;
            this.readyOps = readyOps;
        }

        @Override
        public SelectableChannel channel() {
            return channel;
        }

        @Override
        public Selector selector() {
            return PipeSelector.this;
        }

        @Override
        public int interestOps() {
            return interestOps;
        }

        @Override
        public SelectionKey interestOps(final int ops) {
            interestOps = ops;
            return this;
        }

        @Override
        public int readyOps() {
            return readyOps;
        }

        public void setReadyOps(final int readyOps) {
            this.readyOps = readyOps;
        }

        @Override
        public int compareTo(final SelectionKey o) {
            if (o == null) {
                return 1;
            }
            if (this == o) {
                return 0;
            }
            return hashCode() - o.hashCode();//FIXME ???
        }
    }

    static SelectorProvider selectorProvider() {
        return PROVIDER;
    }

    private static class Provider extends SelectorProvider {

        @Override
        public DatagramChannel openDatagramChannel() throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Pipe openPipe() throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public AbstractSelector openSelector() throws IOException {
            return new PipeSelector();
        }

        @Override
        public ServerSocketChannel openServerSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public SocketChannel openSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }
        @Override
        public DatagramChannel openDatagramChannel(ProtocolFamily arg0) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static interface PipeSelectorListener {

        public void onDataAvailable();
    }
}
