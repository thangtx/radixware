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
import java.io.InterruptedIOException;
import java.net.BindException;
import java.net.ConnectException;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;


public class ServerPipe extends AbstractSelectableChannel implements PipeSelectorChannel {

    private final PipeAddress addr;
    private final BlockingQueue<BidirectionalPipe> acceptAwaitingClients = new ArrayBlockingQueue<BidirectionalPipe>(1024, false);
    private final Set<PipeSelector> selectors = new CopyOnWriteArraySet();
    private volatile boolean closed = false;

    /**
     * Initializes a new instance of this class.
     */
    protected ServerPipe(final PipeAddress addr) throws BindException {
        super(PipeSelector.selectorProvider());
        this.addr = addr;
    }

    public static ServerPipe open(final PipeAddress addr) throws BindException {
        final ServerPipe pipe = new ServerPipe(addr);
        ServerPipeRegisry.bind(addr, pipe);
        return pipe;
    }

    /**
     * Returns an operation set identifying this channel's supported operations.
     *
     * <p> Server-socket channels only support the accepting of new connections,
     * so this method returns {@link SelectionKey#OP_ACCEPT}. </p>
     *
     * @return The valid-operation set
     */
    @Override
    public final int validOps() {
        return SelectionKey.OP_ACCEPT;
    }

    /**
     *
     * @param timeoutMillis - used when mode is blocking, -1 - wait forever
     * @return
     * @throws InterruptedException
     */
    public BidirectionalPipe accept(final long timeoutMillis) throws IOException {
        BidirectionalPipe client;
        final long waitEndMillis = System.currentTimeMillis() + timeoutMillis;
        if (isBlocking() && timeoutMillis != 0) {
            try {
                if (timeoutMillis < 0) {
                    client = acceptAwaitingClients.take();
                    while (client != null && !client.isOpen()) {
                        client = acceptAwaitingClients.take();//skip disconnected clients
                    }
                } else {
                    client = acceptAwaitingClients.poll(timeoutMillis, TimeUnit.MILLISECONDS);
                    while (client != null && !client.isOpen()) {
                        final long timeLeftMillis = waitEndMillis - System.currentTimeMillis();
                        if (timeLeftMillis > 0) {
                            client = acceptAwaitingClients.poll(timeLeftMillis, TimeUnit.MILLISECONDS);
                        }
                    }
                    if (client == null) {
                        throw new IOException("Accept timeout exceeded");
                    }
                }
            } catch (InterruptedException ex) {
                throw new InterruptedIOException(ex.getMessage());
            }
        } else {
            client = acceptAwaitingClients.poll();
            while (client != null && !client.isOpen()) {
                client = acceptAwaitingClients.poll();//skip disconnected clients (e.g. by timeout)
            }
        }
        if (client == null) {
            return null;
        }
        final BidirectionalPipe acceptedPipe = new AcceptedBidirectionalPipe(client);
        client.onConnect();
        return acceptedPipe;
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        closed = true;
        ServerPipeRegisry.free(addr);
        while (!acceptAwaitingClients.isEmpty()) {
            acceptAwaitingClients.poll().close();
        }
    }

    @Override
    protected void implConfigureBlocking(final boolean block) throws IOException {
        //do nothing
    }

    //protected by ServerPipeRegistry.sem
    void connect(final BidirectionalPipe client) throws ConnectException {
        if (closed) {
            throw new ConnectException("ServerPipe is already closed: " + addr);
        }
        if (!acceptAwaitingClients.offer(client)) {
            throw new ConnectException("ServerPipe's accept awaiting client queue limit exceeded: " + addr);
        }
        for (PipeSelector s : selectors) {
            final SelectionKey k = keyFor(s);
            if (k != null) {
                if (k.isValid()
                        && (k.interestOps() & SelectionKey.OP_ACCEPT) != 0) {
                    s.notify(k, SelectionKey.OP_ACCEPT);
                }
            }
        }

    }

    @Override
    public void registerSelector(final PipeSelector sel) {
        selectors.add(sel);
    }

    @Override
    public int readyOps() {
        if (!acceptAwaitingClients.isEmpty()) {
            return SelectionKey.OP_ACCEPT;
        } else {
            return 0;
        }
    }

    @Override
    public void unregisterSelector(final PipeSelector sel) {
        selectors.remove(sel);
    }
}
