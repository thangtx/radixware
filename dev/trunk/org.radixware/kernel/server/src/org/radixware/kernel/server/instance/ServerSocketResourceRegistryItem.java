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

import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Callable;

/**
 *
 * @author dsafonov
 */
public class ServerSocketResourceRegistryItem extends SimpleResourceRegistryItem {

    private final ServerSocketChannel serverChannel;

    public ServerSocketResourceRegistryItem(String key, ServerSocketChannel socket, String description, Callable<Boolean> holderAliveChecker) {
        super(key, socket, description, holderAliveChecker);
        this.serverChannel = socket;
    }

    @Override
    public boolean isClosed() {
        try {
            if (serverChannel.socket().isClosed()) {
                return true;
            }
        } catch (Exception ex) {
            return true;
        }
        return false;
    }

    @Override
    public Object getTarget() {
        return serverChannel;
    }

}
