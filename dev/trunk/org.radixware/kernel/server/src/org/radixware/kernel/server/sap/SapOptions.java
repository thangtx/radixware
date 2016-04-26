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

package org.radixware.kernel.server.sap;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.sc.SoapServiceOptions;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SocketServerChannel;
import org.radixware.kernel.server.arte.services.eas.ExplorerAccessService;
import org.radixware.kernel.server.utils.OptionsGroup;


public final class SapOptions {

    private final long sapId;
    private final SapAddress address;
    private final String serviceUri;
    private final EPortSecurityProtocol securityProtocol;
    private final List<String> serverKeyAliases;
    private final List<String> clientCertAliases;
    private final List<String> cipherSuites;
    private final EClientAuthentication checkClientCert;
    private final EClientAuthentication easKrbAuth;
    private final long keystoreModificationTime;
    private final SoapServiceOptions soapServiceOptions;
    private final Map<String, String> attrs;

    public SapOptions(final long sapId,
            final SapAddress address,
            final String serviceUri,
            final EPortSecurityProtocol securityProtocol,
            final List<String> serverKeyAliases,
            final List<String> clientCertAliases,
            final EClientAuthentication checkClientCert,
            final List<String> cipherSuites,
            final EClientAuthentication easKrbAuth,
            final SoapServiceOptions soapServiceOptions,
            final Map<String, String> attrs) {
        this.sapId = sapId;
        this.serviceUri = serviceUri;
        this.address = address;
        this.securityProtocol = securityProtocol;
        this.serverKeyAliases = serverKeyAliases;
        this.clientCertAliases = clientCertAliases;
        this.checkClientCert = checkClientCert;
        this.keystoreModificationTime = KeystoreController.getServerKeystoreModificationTime();
        this.cipherSuites = cipherSuites;
        this.easKrbAuth = easKrbAuth;
        this.soapServiceOptions = soapServiceOptions;
        this.attrs = Collections.unmodifiableMap(attrs == null ? new HashMap<String, String>() : new HashMap<>(attrs));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.sapId ^ (this.sapId >>> 32));
        hash = 97 * hash + Objects.hashCode(this.address);
        hash = 97 * hash + Objects.hashCode(this.serviceUri);
        hash = 97 * hash + Objects.hashCode(this.securityProtocol);
        hash = 97 * hash + Objects.hashCode(this.serverKeyAliases);
        hash = 97 * hash + Objects.hashCode(this.clientCertAliases);
        hash = 97 * hash + Objects.hashCode(this.cipherSuites);
        hash = 97 * hash + Objects.hashCode(this.checkClientCert);
        hash = 97 * hash + Objects.hashCode(this.easKrbAuth);
        hash = 97 * hash + (int) (this.keystoreModificationTime ^ (this.keystoreModificationTime >>> 32));
        hash = 97 * hash + Objects.hashCode(this.soapServiceOptions);
        hash = 97 * hash + Objects.hashCode(this.attrs);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SapOptions other = (SapOptions) obj;
        if (this.sapId != other.sapId) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.serviceUri, other.serviceUri)) {
            return false;
        }
        if (this.securityProtocol != other.securityProtocol) {
            return false;
        }
        if (!Objects.equals(this.serverKeyAliases, other.serverKeyAliases)) {
            return false;
        }
        if (!Objects.equals(this.clientCertAliases, other.clientCertAliases)) {
            return false;
        }
        if (!Objects.equals(this.cipherSuites, other.cipherSuites)) {
            return false;
        }
        if (this.checkClientCert != other.checkClientCert) {
            return false;
        }
        if (this.easKrbAuth != other.easKrbAuth) {
            return false;
        }
        if (this.keystoreModificationTime != other.keystoreModificationTime) {
            return false;
        }
        if (!Objects.equals(this.soapServiceOptions, other.soapServiceOptions)) {
            return false;
        }
        if (!Objects.equals(this.attrs, other.attrs)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final OptionsGroup og = new OptionsGroup();
        og.add(Messages.SERVICE_URI, getServiceUri())
                .add(Messages.SAP, getSapId())
                .add(Messages.ADDRESS, getAddress())
                .add(Messages.SECURITY_PROTOCOL, getSecurityProtocol() == EPortSecurityProtocol.SSL ? "secured connection" : "plaintext connection");
        
          if (getSecurityProtocol() == EPortSecurityProtocol.SSL) {
            og.add(Messages.SERVER_KEY_ALIASES, getServerKeyAliases() == null ? Messages.ANY : getServerKeyAliases().toString());
            og.add(Messages.CLIENT_CERT_ALIASES, getClientCertAliases() == null ? Messages.ANY : getClientCertAliases().toString());
            og.add(Messages.CHECK_CLIENT_CERT, getCheckClientCert());
            
            if (getKeystoreModificationTime() > 0) {
                og.add(Messages.CIPHER_SUITES, getCipherSuites() == SocketServerChannel.SUITE_ANY_STRONG ? Messages.ANY_STRONG : (getCipherSuites() == SocketServerChannel.SUITE_ANY ? Messages.ANY : getCipherSuites()));
                og.add(Messages.SERVER_KEYSTORE_MODIFICATION_TIME, new Date(getKeystoreModificationTime()).toString());
            }
        }      

        if (ExplorerAccessService.SERVICE_WSDL.equals(getServiceUri())) {
            og.add(Messages.EAS_KRB_AUTH, getEasKerberosAuthPolicy());
        }
        
        og.add(Messages.SOAP_SERVICE_OPTIONS, soapServiceOptions);

        if (getAdditionalAttrs() != null && !getAdditionalAttrs().isEmpty()) {
            OptionsGroup additionalAttrs = og.addGroup(Messages.ADDITIONAL_ATTRIBUTES);
            for (Map.Entry<String, String> entry : getAdditionalAttrs().entrySet()) {
                additionalAttrs.add(entry.getKey(), entry.getValue());
            }
        }

        return og.toString();
    }

    public SoapServiceOptions getSoapServiceOptions() {
        return soapServiceOptions;
    }

    public Map<String, String> getAdditionalAttrs() {
        return attrs;
    }

    /**
     * @return the sapId
     */
    public long getSapId() {
        return sapId;
    }

    /**
     * @return the address
     */
    public SapAddress getAddress() {
        return address;
    }

    /**
     * @return the serviceUri
     */
    public String getServiceUri() {
        return serviceUri;
    }

    /**
     * @return the securityProtocol
     */
    public EPortSecurityProtocol getSecurityProtocol() {
        return securityProtocol;
    }

    /**
     * @return the serverKeyAliases
     */
    public List<String> getServerKeyAliases() {
        return serverKeyAliases;
    }

    /**
     * @return the clientCertAliases
     */
    public List<String> getClientCertAliases() {
        return clientCertAliases;
    }

    /**
     * @return the checkClientCert
     */
    public EClientAuthentication getCheckClientCert() {
        return checkClientCert;
    }

    /**
     * @return the keystoreModificationTime
     */
    public long getKeystoreModificationTime() {
        return keystoreModificationTime;
    }

    public List<String> getCipherSuites() {
        return cipherSuites;
    }

    public EClientAuthentication getEasKerberosAuthPolicy() {
        return easKrbAuth;
    }

    public String getShortDescription() {
        return "Sap#" + sapId;
    }
}
