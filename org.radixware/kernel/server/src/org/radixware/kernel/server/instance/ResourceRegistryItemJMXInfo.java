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

/**
 *
 * @author dsafonov
 */
public class ResourceRegistryItemJMXInfo {

    private final String key;
    private final String description;
    private final boolean closed;

    public ResourceRegistryItemJMXInfo(String key, String description, boolean closed) {
        this.key = key;
        this.description = description;
        this.closed = closed;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClosed() {
        return closed;
    }

}