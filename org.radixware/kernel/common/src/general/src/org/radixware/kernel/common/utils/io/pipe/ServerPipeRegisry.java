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

import java.net.BindException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ServerPipeRegisry {

    private final static ServerPipeRegisry INSTANCE = new ServerPipeRegisry();

    private final Object SEM = new Object();
    private final Map<PipeAddress, ServerPipe> SERVER_PIPES_BY_ADDRESS = new HashMap<PipeAddress, ServerPipe>();
    private final Map<Integer, IRemotePipeManager> remotePipeManagers = new ConcurrentHashMap<>();

    private ServerPipeRegisry() {
        //singletone
    }
    
    public static void registerRemotePipeManager(IRemotePipeManager remotePipeManager) {
        INSTANCE.internalSetRemotePipeManager(remotePipeManager);
    }
    
    private void internalSetRemotePipeManager(IRemotePipeManager remotePipeManager) {
        this.remotePipeManagers.put(remotePipeManager.getInstanceId(), remotePipeManager);
    }

    static void bind(final PipeAddress addr, final ServerPipe server) throws BindException {
    	INSTANCE.internalBind(addr, server);
    }
    private void internalBind(final PipeAddress addr, final ServerPipe server) throws BindException {
        if (addr.getRemotePort()>0) {
            // For remote pipes bind is illegal
            throw new BindException("ServerPipe cannot be binded to remote pipe address: " + addr);
        } else {
            // Local pipe
            synchronized (SEM) {
                if (SERVER_PIPES_BY_ADDRESS.containsKey(addr)) {
                    throw new BindException("Address is already used by other ServerPipe: " + addr);
                }
                SERVER_PIPES_BY_ADDRESS.put(addr, server);
            }
            
        }
    }

    static void free(final PipeAddress addr) {
    	INSTANCE.internalFree(addr);
    }
    
    private void internalFree(final PipeAddress addr) {
        if (addr.getRemotePort()>0) {
            // Remote pipe
            IRemotePipeManager remotePipeManager = remotePipeManagers.get(addr.getLocalInstanceId());
            if (remotePipeManager!=null) {
                remotePipeManager.free(addr);
            }
        } else {
            // Local pipe
            synchronized (SEM) {
                SERVER_PIPES_BY_ADDRESS.remove(addr);
            }
        }
    }

    public static void connect(final PipeAddress addr, final BidirectionalPipe client) throws ConnectException {
    	INSTANCE.internalConnect(addr, client);
    }
    
    private void internalConnect(final PipeAddress addr, final BidirectionalPipe client) throws ConnectException {
        if (addr.getRemotePort()>0) {
            // Remote pipe
            IRemotePipeManager remotePipeManager = remotePipeManagers.get(addr.getLocalInstanceId());
            if (remotePipeManager!=null) {
                remotePipeManager.connect(addr, client);
            }
        } else {
            // Local pipe
            synchronized (SEM) {
                final ServerPipe srv = SERVER_PIPES_BY_ADDRESS.get(addr);
                if (srv == null) {
                    throw new ConnectException("There is no ServerPipe listening: " + addr);
                }
                srv.connect(client);
            }
        }
    }
}
