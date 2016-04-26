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

package org.radixware.kernel.common.kerberos;

import java.security.PrivilegedExceptionAction;
import org.ietf.jgss.*;


abstract class CreateCredentialAction implements PrivilegedExceptionAction<GSSCredential> {    
    private final String principalName;
    private final Oid nameType;
    private final Oid mechType;
    private final int lifeTime;
    private final int credentialsUsage;

    /*public CreateCredentialAction(final String principalName, final Oid nameType, final int credentialsLifeTimeInSec, final int credentialsUsage) {
        this.principalName = principalName;
        this.nameType = nameType;
        this.lifeTime = credentialsLifeTimeInSec;
        this.credentialsUsage = credentialsUsage;        
    }*/
    
    public CreateCredentialAction(final String principalName, final Oid nameType, final Oid mechType, final int credentialsLifeTimeInSec, final int credentialsUsage) {
        this.principalName = principalName;
        this.nameType = nameType;
        this.mechType = mechType;
        this.lifeTime = credentialsLifeTimeInSec;
        this.credentialsUsage = credentialsUsage;        
    }    

    @Override
    public GSSCredential run() throws GSSException {
        final GSSManager manager = GSSManager.getInstance();
        GSSName name = manager.createName(principalName, nameType);
        return manager.createCredential(name, lifeTime, new Oid[]{mechType}, credentialsUsage);
    }    
}
