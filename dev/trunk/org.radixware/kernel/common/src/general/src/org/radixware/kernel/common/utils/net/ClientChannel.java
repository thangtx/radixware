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

/**
 * Client side of the data transmission channel. Is able to connect to the
 * specified destination.
 *
 */
public interface ClientChannel extends RadixChannel {

    /**
     * Connect to the specified sap address. Blocking mode of the connection
     * depends on the {@code timeoutMillis} value.
     *
     * @param sapAddress address to connect
     * @param timeoutMillis timeout for connection. If timeoutMillis == 0, then
     * the channel will be opened in non-blocking mode. If timeoutMillis == -1,
     * then the call will block until connection will be established. Positive
     * timeoutMillis defines maximum amount of time to wait.
     *
     * @return true if connection is established
     *
     * @throws IOException
     */
    boolean connect(SapAddress sapAddress, int timeoutMillis) throws IOException;

    void finishConnect() throws IOException;
}
