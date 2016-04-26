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
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;


abstract class InitSecContextAction implements PrivilegedExceptionAction<byte[]> {
    
    private final GSSContext context;
    private final byte[] inToken;    

    public InitSecContextAction(final GSSContext context, final byte[] inToken){        
        this.context = context;
        this.inToken = inToken;
    }

    @Override
    public byte[] run() throws Exception {
        return doAction(context, inToken);
    }   
    
    protected abstract byte[] doAction(final GSSContext context, final byte[] inToken) throws GSSException;
}
