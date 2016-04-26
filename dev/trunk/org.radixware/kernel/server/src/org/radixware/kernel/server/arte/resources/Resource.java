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

package org.radixware.kernel.server.arte.resources;


public class Resource {    
    public final static int DEFAULT_TIMEOUT = 60; // 1 минута 
    public final static int DEFAULT_BLOCK_SIZE = 200 * 1024; // байт
    
    protected int timeout   = DEFAULT_TIMEOUT;
    protected int blockSize = DEFAULT_BLOCK_SIZE;
}
