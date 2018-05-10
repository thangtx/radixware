/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance;

import java.util.concurrent.Callable;
import org.radixware.kernel.common.utils.net.ServerChannel;

public class ServerChannelResourceItem extends SimpleResourceRegistryItem {

    private final ServerChannel serverChannel;

    public ServerChannelResourceItem(String key, ServerChannel serverChannel, final String description, final Callable<Boolean> holderAliveChecker) {
        super(key, serverChannel, description, holderAliveChecker);
        this.serverChannel = serverChannel;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + "[opened=" + serverChannel.isOpened() + "]";
    }

    @Override
    public boolean isClosed() {
        return !serverChannel.isOpened();
    }

    @Override
    public Object getTarget() {
        return serverChannel;
    }
    
}
