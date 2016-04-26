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

package org.radixware.kernel.common.client.eas;

import org.radixware.schemas.eas.CreateSessionRs;


public final class DatabaseInfo {
    
    final static DatabaseInfo EMPTY_INSTANCE = new DatabaseInfo(null);

    private final String productName;
    private final String productVersion;
    private final String driverName;
    private final String driverVersion;
            
    DatabaseInfo(final CreateSessionRs.DatabaseInfo dbInfo){
        if (dbInfo==null){
            productName = null;
            productVersion = null;
            driverName = null;
            driverVersion = null; 
        }else{
            productName = dbInfo.getProductName();
            productVersion = dbInfo.getProductVersion();
            driverName = dbInfo.getDriverName();
            driverVersion = dbInfo.getDriverVersion();            
        }
    }
    
    public boolean isEmpty(){
        return (productName==null || productName.isEmpty())
               && (productVersion==null || productVersion.isEmpty())
               && (driverName==null || driverName.isEmpty())
               && (driverVersion==null || driverVersion.isEmpty());
    }

    public String getProductName() {
        return productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }            
}
