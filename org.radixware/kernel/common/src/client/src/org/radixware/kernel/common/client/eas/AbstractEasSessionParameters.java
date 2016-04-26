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

package org.radixware.kernel.common.client.eas;

import java.security.cert.X509Certificate;
import org.radixware.kernel.common.enums.EAuthType;


abstract class AbstractEasSessionParameters {
    
    private final String stationName;
    private final String userName;
    
    public AbstractEasSessionParameters(final String userName, final String stationName){
        this.userName = userName;
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

    public String getUserName() {
        return userName;
    }   
    
    public abstract EAuthType getAuthType();
    
    public abstract boolean hasUserCerts();
    
    public abstract X509Certificate[] getUserCertificates();
    
    public abstract AbstractEasSessionParameters createCopy(final String newUserName);
}
