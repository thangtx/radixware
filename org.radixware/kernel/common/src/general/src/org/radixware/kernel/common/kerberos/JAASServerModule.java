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

import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.*;


public abstract class JAASServerModule extends JAASModule {

    private GSSContext context;

    public JAASServerModule(String moduleName, String servicePrincipalName) {
        super(moduleName, null);
        setParamValue(JAASModule.KRB_IS_INITIATOR, "false");
        setParamValue(JAASModule.KRB_KEY_TAB, "true");
        setParamValue(JAASModule.KRB_STORE_KEY, "true");
        setParamValue(JAASModule.KRB_PRINCIPAL, servicePrincipalName);
    }

    @Override
    public char[] getPassword() {
        throw new IllegalStateException("Password must not be entered for service principal (service never knows it's password)");
    }

    @Override
    public String getPrincipalName() {
        return getParamValue(JAASModule.KRB_PRINCIPAL);
    }

    public void initGSSContext(final byte[] initToken) throws GSSException, LoginException {
        if (context != null) {
            context.dispose();
        }
        final GSSException[] innerException = new GSSException[1];
        context = Subject.doAs(getSubject(), new PrivilegedAction<GSSContext>() {

            @Override
            public GSSContext run() {
                try {
                    final Oid krb5Oid = createKrbOid();

                    GSSManager manager = GSSManager.getInstance();
                    GSSName serverName =
                            manager.createName(getPrincipalName(),
                            GSSName.NT_USER_NAME);
                    GSSCredential serverCreds =
                            manager.createCredential(serverName,/*
                             * serverName,
                             */
                            GSSCredential.INDEFINITE_LIFETIME,
                            new Oid[]{krb5Oid},
                            GSSCredential.ACCEPT_ONLY);
                    GSSContext secContext = manager.createContext(serverCreds);


                    byte[] outToken = secContext.acceptSecContext(initToken, 0, initToken.length);
                    if (outToken != null) {
                        writeServerToken(outToken);
                    }
                    while (!secContext.isEstablished()) {
                        byte[] inToken = readClientToken();
                        outToken = secContext.acceptSecContext(inToken, 0, initToken.length);

                        if (outToken != null) {
                            writeServerToken(outToken);
                        }
                    }
                    return secContext;
                } catch (GSSException ex) {
                    innerException[0] = ex;
                    return null;
                }
            }
        });
        if (innerException[0] != null) {
            throw innerException[0];
        }
    }

    protected abstract byte[] readClientToken();

    protected abstract void writeServerToken(byte[] bytes);
}
