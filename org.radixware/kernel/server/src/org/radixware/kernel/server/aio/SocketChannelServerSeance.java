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

import java.nio.channels.SocketChannel;

public class SocketChannelServerSeance extends SocketChannelPort {

    public SocketChannelServerSeance(SocketChannelServer parent, SocketChannel acceptedSocket) {
        super(parent.dispatcher, parent.tracer, acceptedSocket, parent.recvFrameFormat, parent.sendFrameFormat);
        this.parent = parent;
    }

    @Override
    public void close() {
        super.close();
        parent.seances.remove(this);
    }
    private final SocketChannelServer parent;
}
