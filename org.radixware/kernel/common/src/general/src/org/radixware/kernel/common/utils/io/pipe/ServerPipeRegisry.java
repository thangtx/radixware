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


final class ServerPipeRegisry {

    private static final Object SEM = new Object();
    private static final Map<PipeAddress, ServerPipe> SERVER_PIPES_BY_ADDRESS = new HashMap<PipeAddress, ServerPipe>();

    private ServerPipeRegisry() {
        //singletone
    }

    static void bind(final PipeAddress addr, final ServerPipe server) throws BindException {
        synchronized (SEM) {
            if (SERVER_PIPES_BY_ADDRESS.containsKey(addr)) {
                throw new BindException("Address is already used by other ServerPipe: " + addr);
            }
            SERVER_PIPES_BY_ADDRESS.put(addr, server);
        }
    }

    static void free(final PipeAddress addr) {
        synchronized (SEM) {
            SERVER_PIPES_BY_ADDRESS.remove(addr);
        }
    }

    static void connect(final PipeAddress addr, final BidirectionalPipe client) throws ConnectException {
        synchronized (SEM) {
            final ServerPipe srv = SERVER_PIPES_BY_ADDRESS.get(addr);
            if (srv == null) {
                throw new ConnectException("There is no ServerPipe listening: " + addr);
            }
            srv.connect(client);
        }
    }
}
