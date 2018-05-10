/*
* Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.server.arte;

import java.util.Arrays;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

public class ArteTransactionParams {

    private final Long version;
    private final Long easSessionId;
    private final String easRequestName;
    private final String userName;
    private final String stationName;
    private final EIsoLanguage clientLanguage;
    private final EIsoCountry clientCountry;
    private final ERuntimeEnvironmentType clientEnvironment;
    private final byte[] sessionKey;
    private final String aasClassName;
    private final String aasMethodName;

    public ArteTransactionParams(final Long version_,
                                 final Long easSessionId_,
                                 final String easRequestName,
                                 final String userName_,
                                 final String stationName_,
                                 final EIsoLanguage clientLanguage_,
                                 final EIsoCountry clientCountry_,
                                 final ERuntimeEnvironmentType clientEnvironment_,
                                 final byte[] sessionKey_,
                                 final String aasClassName,
                                 final String aasMethodName){
        version = version_;
        easSessionId = easSessionId_;
        this.easRequestName = easRequestName;
        userName = userName_;
        stationName = stationName_;
        clientLanguage = clientLanguage_;
        clientCountry = clientCountry_;
        clientEnvironment = clientEnvironment_;
        sessionKey = sessionKey_==null ? null : Arrays.copyOf(sessionKey_, sessionKey_.length);
        this.aasClassName = aasClassName;
        this.aasMethodName = aasMethodName;
    }

    public Long getVersion() {
        return version;
    }

    public Long getEasSessionId() {
        return easSessionId;
    }

    public String getEasRequestName() {
        return easRequestName;
    }

    public String getUserName() {
        return userName;
    }

    public String getStationName() {
        return stationName;
    }

    public EIsoLanguage getClientLanguage() {
        return clientLanguage;
    }

    public EIsoCountry getClientCountry() {
        return clientCountry;
    }

    public ERuntimeEnvironmentType getClientEnvironment() {
        return clientEnvironment;
    }
    
    public boolean isSessionKeyAccessible(){
        return sessionKey!=null && sessionKey.length>0;
    }

    public byte[] getSessionKey() {
        return sessionKey==null ? null : Arrays.copyOf(sessionKey, sessionKey.length);
    }

    public String getAasClassName() {
        return aasClassName;
    }

    public String getAasMethodName() {
        return aasMethodName;
    }
}
