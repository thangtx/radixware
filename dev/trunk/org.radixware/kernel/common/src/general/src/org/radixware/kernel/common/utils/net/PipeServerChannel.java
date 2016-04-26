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
import java.nio.channels.SelectableChannel;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.common.utils.io.pipe.ServerPipe;


public class PipeServerChannel implements ServerChannel {

    private volatile ServerPipe serverPipe;

    @Override
    public boolean isOpened() {
        return serverPipe != null;
    }

    @Override
    public void open(SapAddress sapAddress) throws IOException {
        serverPipe = ServerPipe.open(sapAddress.getPipeAddress());
    }

    @Override
    public void close() throws IOException {
        if (serverPipe != null) {
            try {
                serverPipe.close();
            } finally {
                serverPipe = null;
            }
        }
    }

    @Override
    public RequestChannel acceptRequest() throws IOException {
        final BidirectionalPipe acceptedPipe = serverPipe.accept(-1);
        if (acceptedPipe == null) {
            return null;
        }
        return new PipeRequestChannel(acceptedPipe);
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return serverPipe;
    }
}
