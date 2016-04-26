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

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.exceptions.IllegalUsageError;

/**
 * @NotThreadsafe
 */
public class SocketsDisconnectWatcher implements Closeable {

    private Selector selector = null;
    private Map<SelectableChannel, SelectionKey> watches;
    private Set<SelectableChannel> disconnected;
    private final ByteBuffer buffer = ByteBuffer.allocate(1);

    /**
     * Switches socket channel to asynchronous mode
     * @param channel
     * @throws IOException
     */
    public void startDisconnectWatch(final SelectableChannel channel) throws IOException {
        if (selector == null) {
            selector = AioUtils.openSelector();
            watches = new HashMap<SelectableChannel, SelectionKey>();
            disconnected = new HashSet<SelectableChannel>();
        } else {
            processWatches();// to clear canceled keys
        }
        channel.configureBlocking(false);
        final SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
        watches.put(channel, selectionKey);
    }

    /**
     * Switches socket channel to synchronous mode
     * @param channel
     * @throws IOException
     */
    public void finishDisconnectWatch(final SelectableChannel channel) throws IOException {
        if (selector == null) {
            return;
        }
        final SelectionKey selectionKey = watches.get(channel);
        if (selectionKey == null) {
            return;
        }
        selectionKey.cancel();
        channel.configureBlocking(true);
        watches.remove(channel);
        disconnected.remove(channel);
        //TODO ????
        //selector.selectNow();//почистим отмененный ключ
    }

    public boolean isDisconnected(final SelectableChannel channel) throws IOException {
        if (selector == null) {
            throw new IllegalUsageError("Socket disconnect watch is not started");
        }
        if (disconnected.contains(channel)) {
            return true;
        }
        final SelectionKey selectionKey = watches.get(channel);
        if (selectionKey == null) {
            throw new IllegalUsageError("Socket disconnect watch is not started");
        }
        processWatches();
        return disconnected.contains(channel);
    }

    @Override
    public void close() throws IOException {
        if (selector != null) {
            selector.close();
            watches.clear();
        }
    }

    private void processWatches() throws IOException {
        if (selector.selectNow() == 0) {
            return;
        }
        final Set<SelectionKey> readyKeys = selector.selectedKeys();
        final Iterator<SelectionKey> readyIter = readyKeys.iterator();
        while (readyIter.hasNext()) {
            final SelectionKey key = readyIter.next();
            readyIter.remove();
            if (!key.isValid()) {
                continue;
            }
            while (true) {
                final SelectableChannel channel = key.channel();
                if (((ReadableByteChannel) channel).read(buffer) < 0) { //shutdown
                    disconnected.add(channel);
                    break;
                }
                buffer.clear();  //skip
            }
        }
    }

    public boolean isWatched(final SelectableChannel channel) {
        return watches != null && watches.containsKey(channel);
    }
}
