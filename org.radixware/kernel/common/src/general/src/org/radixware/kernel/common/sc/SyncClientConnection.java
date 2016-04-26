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

package org.radixware.kernel.common.sc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SelectableChannel;


public interface SyncClientConnection {

    public void connect() throws IOException;

    public void close() throws IOException;

    public OutputStream getOutputStream() throws IOException;

    public InputStream getInputStream() throws IOException;

    public void setReadTimeOut(int timeoutMillis) throws IOException;

    public SelectableChannel getSelectableChannel();
    
    public SapClientOptions getSapOptions();
    
}
