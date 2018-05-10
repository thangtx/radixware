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

package org.radixware.kernel.server.instance.arte;

import java.io.InputStream;
import java.util.Map;
import org.radixware.kernel.server.units.arte.ArteUnit;
import org.radixware.kernel.common.utils.net.RequestChannel;
import org.radixware.kernel.server.sap.SapOptions;
import org.radixware.kernel.server.utils.IPriorityResourceManager;


public interface IArteRequest {

    public ArteUnit getUnit();

    public RequestChannel getRequestChannel();

    public IArteRequestCallback getCallback();

    /**
     * @return String that somehow identifies client. It is used to find the
     * most suitable ARTE instance for request handling (preferably, the same
     * instance that was handling this client last time). It should be the same
     * for all requests of one given client (i.e., it shouldn't contain client
     * port number, because it can change between requests). Example - client IP.
     */
    public String getClientId();

    public SapOptions getSapOptions();
    
    public int getRadixPriority();
    
    public InputStream getOverriddenInput();
    
    public IPriorityResourceManager.Ticket getCountTicket();
    
    public void setCountTicket(IPriorityResourceManager.Ticket ticket);
    
    public IPriorityResourceManager.Ticket getActiveTicket();
    
    public void setActiveTicket(IPriorityResourceManager.Ticket ticket);
    
    public long getCreateTimeMillis();
    
    /**
     * 
     * @return -1 if particulate version is not strictly required
     */
    public long getVersion();
    
    public Map<String, String> getHeaders();
}
