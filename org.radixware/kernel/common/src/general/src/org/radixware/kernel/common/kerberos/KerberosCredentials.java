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

import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.*;
//import sun.security.jgss.GSSUtil;//NOPMD we need this class

/**
 * Идентификационные данные сервиса или пользователя, зарегистрированного в KDC
 *
 */
public abstract class KerberosCredentials {

    private Subject subject;
    private final CreateCredentialAction createAction;
    private final KerberosLoginConfiguration loginConfig;
    private GSSCredential credential;

    KerberosCredentials(final Subject subject, final GSSCredential credential) {
        this.subject = subject;
        this.credential = credential;
        createAction = null;
        loginConfig = null;
    }

    KerberosCredentials(final KerberosLoginConfiguration config,
            final CreateCredentialAction createCredentialAction) throws KerberosException {
        this.loginConfig = config;
        this.createAction = createCredentialAction;
        renew();
    }

    public final String getSelfPrincipalName() {
        final Set<Principal> principals = subject.getPrincipals();
        if (principals.isEmpty()) {
            return null;
        } else {
            return principals.iterator().next().getName();
        }
    }

    public abstract String getRemotePrincipalName();

    public final boolean isRenewable() {
        return createAction != null && loginConfig != null;
    }

    public final void renew() throws KerberosException {
        if (isRenewable()) {
            final Subject newSubject;
            try {
                newSubject = loginConfig.doLogin(true);
            } catch (LoginException exception) {
                throw new KerberosException(exception);
            }
            final GSSCredential newCredential;
            try {
                newCredential = Subject.doAs(newSubject, createAction);
            } catch (PrivilegedActionException exception) {
                throw new KerberosException(exception.getException());//NOPMD we do not need for stack of PrivilegedActionException
            }
            disposeCredential();
            subject = newSubject;
            credential = newCredential;
        }
    }

    protected final GSSCredential getGSSCredentials() {
        return credential;
    }

    final <T> T performAction(final PrivilegedExceptionAction<T> action) throws PrivilegedActionException {
        return Subject.doAs(subject, action);
    }

    public final byte[] getNextHandshakeToken(final GSSContext context, final byte[] token) throws KerberosException {
        try {
            return performAction(createHandshakeAction(context, token));
        } catch (PrivilegedActionException exception) {
            throw new KerberosException(exception.getException());//NOPMD we do not need for stack of PrivilegedActionException
        }
    }

    protected abstract PrivilegedExceptionAction<byte[]> createHandshakeAction(final GSSContext context, final byte[] token);

    public final void dispose() {
        disposeCredential();
        logout();
    }

    private void disposeCredential() {
        if (credential != null) {
            try {
                credential.dispose();
                credential = null;//NOPMD
            } catch (GSSException exception) {
                //do nothing here
                credential = null;//NOPMD
            }
        }
    }

    private void logout() {
        if (loginConfig != null) {
            try {
                loginConfig.doLogout();
            } catch (LoginException exception) {//NOPMD
                //do nothing here
            }
        }
    }

    public abstract GSSContext createSecurityContext() throws KerberosException;

    private static class ServiceCredentials extends KerberosCredentials {

        private static class HandshakeAction extends InitSecContextAction {

            public HandshakeAction(final GSSContext context, final byte[] inToken) {
                super(context, inToken);
            }

            @Override
            protected byte[] doAction(GSSContext context, byte[] inToken) throws GSSException {
                return context.acceptSecContext(inToken, 0, inToken.length);
            }
        }

        public ServiceCredentials(final KerberosLoginConfiguration config, final CreateCredentialAction createAction) throws KerberosException {
            super(config, createAction);
        }

        @Override
        public GSSContext createSecurityContext() throws KerberosException {
            try {
                return performAction(new PrivilegedExceptionAction<GSSContext>() {
                    @Override
                    public GSSContext run() throws GSSException {
                        return GSSManager.getInstance().createContext(getGSSCredentials());
                    }
                });
            } catch (PrivilegedActionException exception) {
                throw new KerberosException(exception.getException());//NOPMD we do not need for stack of PrivilegedActionException
            }
        }

        @Override
        protected PrivilegedExceptionAction<byte[]> createHandshakeAction(GSSContext context, byte[] token) {
            return new HandshakeAction(context, token);
        }

        @Override
        public String getRemotePrincipalName() {
            return null;
        }
    }

    private static class ClientCredentials extends KerberosCredentials {

        private static class HandshakeAction extends InitSecContextAction {

            public HandshakeAction(final GSSContext context, final byte[] inToken) {
                super(context, inToken);
            }

            @Override
            protected byte[] doAction(GSSContext context, byte[] inToken) throws GSSException {
                try {
                    return context.initSecContext(inToken, 0, inToken.length);
                } catch (NullPointerException exception) {//NOPMD we do not need for stack of this exception
                    throw new GSSException(GSSException.DEFECTIVE_TOKEN);//NOPMD we do not need for stack of this exception
                }
            }
        }
        private final String servicePrincipal;

        public ClientCredentials(final KerberosLoginConfiguration config,
                final CreateCredentialAction createAction,
                final String servicePrincipal) throws KerberosException {
            super(config, createAction);
            this.servicePrincipal = servicePrincipal;
        }

        public ClientCredentials(final Subject subject,
                final GSSCredential credential,
                final String servicePrincipal) {
            super(subject, credential);
            this.servicePrincipal = servicePrincipal;
        }

        private static Oid getKerberosOid() {
            return KerberosUtils.KRB5_OID;
        }

        @Override
        public GSSContext createSecurityContext() throws KerberosException {
            try {
                return performAction(new PrivilegedExceptionAction<GSSContext>() {
                    @Override
                    public GSSContext run() throws GSSException {
                        final GSSManager manager = GSSManager.getInstance();
                        final GSSName name = manager.createName(servicePrincipal, GSSName.NT_USER_NAME);
                        final GSSContext context = manager.createContext(name,
                                getKerberosOid(),
                                getGSSCredentials(),
                                GSSContext.DEFAULT_LIFETIME);
                        context.requestMutualAuth(true);
                        return context;
                    }
                });
            } catch (PrivilegedActionException exception) {
                throw new KerberosException(exception.getException());//NOPMD we do not need for stack of this exception
            }
        }

        @Override
        protected PrivilegedExceptionAction<byte[]> createHandshakeAction(GSSContext context, byte[] token) {
            return new HandshakeAction(context, token);
        }

        @Override
        public String getRemotePrincipalName() {
            return servicePrincipal;
        }
    }

    public static class Factory {

        private Factory() {
        }

        private static class CreateServiceCredentialAction extends CreateCredentialAction {

            public CreateServiceCredentialAction(final String principalName) {
                super(principalName, GSSName.NT_USER_NAME, KerberosUtils.KRB5_OID, GSSCredential.INDEFINITE_LIFETIME, GSSCredential.ACCEPT_ONLY);
            }
        }

        private static class CreateSpnegoServiceCredentialAction extends CreateCredentialAction {

            public CreateSpnegoServiceCredentialAction(final String principalName) {
                super(principalName, GSSName.NT_USER_NAME, KerberosUtils.SPNEGO_OID, GSSCredential.INDEFINITE_LIFETIME, GSSCredential.ACCEPT_ONLY);
            }
        }

        private static class CreateClientCredentialAction extends CreateCredentialAction {

            public CreateClientCredentialAction(final String principalName, final int lifeTime) {
                super(principalName, GSSName.NT_USER_NAME, KerberosUtils.KRB5_OID, lifeTime, GSSCredential.INITIATE_ONLY);
            }
        }

        public static KerberosCredentials newServiceCredentials(final String principalName,
                final KerberosLoginConfiguration config,
                final boolean isSpnego)
                throws KerberosException {
            final CreateCredentialAction createAction;
            if (isSpnego) {
                createAction = new CreateSpnegoServiceCredentialAction(principalName);
            } else {
                createAction = new CreateServiceCredentialAction(principalName);
            }
            return new ServiceCredentials(config, createAction);
        }

        public static KerberosCredentials newClientCredentials(final String clientPrincipalName,
                final String servicePrincipalName,
                final KerberosLoginConfiguration config,
                final int lifeTimeInSeconds)
                throws KerberosException {
            final CreateCredentialAction createAction =
                    new CreateClientCredentialAction(clientPrincipalName, lifeTimeInSeconds);
            return new ClientCredentials(config, createAction, servicePrincipalName);
        }

        public static KerberosCredentials wrapDelegatedCredential(final String servicePrincipalName,
                final KerberosLoginConfiguration config,
                final GSSCredential delegCred)
                throws KerberosException {
            final Subject subject;
            try {
                subject = config.doLogin(true);
            } catch (LoginException exception) {
                throw new KerberosException(exception);
            }
            return new ClientCredentials(subject, delegCred, servicePrincipalName);
        }
    }
}