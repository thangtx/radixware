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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.enums.EChannelType;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.sc.SoapServiceOptions;
import org.radixware.kernel.common.sc.WsdlSource;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.net.JmsAddress;
import org.radixware.kernel.common.utils.io.pipe.PipeAddress;
import org.radixware.kernel.common.utils.net.JmsUtils;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SocketServerChannel;


public class ReadSapOptionsQuery {

    private PreparedStatement readOptsQry = null;
    private final Connection connection;

    public ReadSapOptionsQuery(final Connection connection) {
        this.connection = connection;
    }

    public SapOptions readOptions(final long sapId) throws SQLException {
        if (readOptsQry == null) {
            readOptsQry = connection.prepareStatement(
                    "select "
                    + "ap.address, "
                    + "ap.uri, "
                    + "ap.systemId, "
                    + "ap.securityprotocol, "
                    + "ap.serverkeyaliases, "
                    + "ap.clientcertaliases, "
                    + "ap.checkclientcert, "
                    + "ap.channelType, "
                    + "ap.cipherSuites, "
                    + "ap.easKrbAuth,"
                    + "ap.serverAttrs, "
                    + "ap.useWsSecurity, "
                    + "ap.serviceQName, "
                    + "ap.portQName, "
                    + "ap.serviceLastUpdateTime, "
                    + "nvl2(ap.serviceWsdl, 1, 0) wsdlPresent, "
                    + "dsm.uri dsmWsdlUri, "
                    + "dsm.lastUpdateTime dsmWsdlLastUpdateTime "
                    + "from "
                    + "rdx_sap ap, "
                    + "rdx_service svc left join rdx_sb_datascheme dsm on dsm.uri = svc.wsdlUri "
                    + "where ap.id = ? "
                    + "and svc.systemId = ap.systemId "
                    + "and svc.uri = ap.uri");
            readOptsQry.setFetchSize(1);
        }
        readOptsQry.setLong(1, sapId);
        final ResultSet rs = readOptsQry.executeQuery();
        try {
            if (rs.next()) {
                final EChannelType channelType = EChannelType.getForValue(rs.getString("channelType"));
                final SapAddress sapAddress;
                if (channelType == EChannelType.TCP) {
                    sapAddress = new SapAddress(ValueFormatter.parseCompositeInetSocketAddress(rs.getString("address")));
                } else if (channelType == EChannelType.INTERNAL_PIPE) {
                    sapAddress = new SapAddress(new PipeAddress(rs.getString("address")));
                } else if (channelType == EChannelType.JMS) {
                    sapAddress = new SapAddress(new JmsAddress(rs.getString("address")));
                } else {
                    throw new IllegalStateException("Unsupported channel type: " + channelType);
                }
                final long securityProtocolVal = rs.getLong("securityprotocol");
                final EPortSecurityProtocol securityProtocol = EPortSecurityProtocol.getForValue(securityProtocolVal);
                final String serverKeyAliasesVal = rs.getString("serverkeyaliases");
                final List<String> serverKeyAliases;
                if (serverKeyAliasesVal != null) {
                    serverKeyAliases = Collections.unmodifiableList(ArrStr.fromValAsStr(serverKeyAliasesVal));
                } else {
                    serverKeyAliases = null;
                }
                final String clientCertAliasesVal = rs.getString("clientcertaliases");
                final List<String> clientCertAliases;
                if (clientCertAliasesVal != null) {
                    clientCertAliases = Collections.unmodifiableList(ArrStr.fromValAsStr(clientCertAliasesVal));
                } else {
                    clientCertAliases = null;
                }
                final String cipherSuitesVal = rs.getString("cipherSuites");
                final List<String> cipherSuites;
                if (cipherSuitesVal == null) {
                    cipherSuites = SocketServerChannel.SUITE_ANY_STRONG;
                } else {
                    final List<String> suites = Collections.unmodifiableList(ArrStr.fromValAsStr(cipherSuitesVal));
                    if (suites.isEmpty()) {
                        cipherSuites = SocketServerChannel.SUITE_ANY;
                    } else {
                        cipherSuites = suites;
                    }
                }
                final long checkClientCertVal = rs.getLong("checkclientcert");
                final EClientAuthentication checkClientCert = EClientAuthentication.getForValue(checkClientCertVal);
                final long easKrbAuthVal = rs.getLong("easKrbAuth");
                final EClientAuthentication easKrbAuth = EClientAuthentication.getForValue(easKrbAuthVal);
                final String serviceUri = rs.getString("uri");
                SoapServiceOptions soapServiceOptions = null;
                if (rs.getBoolean("useWsSecurity")) {
                    if (rs.getInt("wsdlPresent") == 0) {
                        soapServiceOptions = new SoapServiceOptions(
                                new WsdlSource(rs.getString("dsmWsdlUri")),
                                qnameFromString(rs.getString("serviceQName")),
                                qnameFromString(rs.getString("portQName")),
                                rs.getTimestamp("dsmWsdlLastUpdateTime") == null ? 0 : rs.getTimestamp("dsmWsdlLastUpdateTime").getTime());
                    } else {
                        soapServiceOptions = new SoapServiceOptions(
                                new WsdlSource(sapId),
                                qnameFromString(rs.getString("serviceQName")),
                                qnameFromString(rs.getString("portQName")),
                                rs.getTimestamp("serviceLastUpdateTime") == null ? 0 : rs.getTimestamp("serviceLastUpdateTime").getTime());
                    }
                }
                final Map<String, String> attrs = JmsUtils.parseProps(rs.getString("serverAttrs"));
                return new SapOptions(
                        sapId,
                        sapAddress,
                        serviceUri,
                        securityProtocol,
                        serverKeyAliases,
                        clientCertAliases,
                        checkClientCert,
                        cipherSuites,
                        easKrbAuth,
                        soapServiceOptions,
                        attrs);
            } else {
                throw new IllegalUsageError("There is no sap with id #" + sapId);
            }
        } finally {
            rs.close();
        }
    }

    public void close() {
        if (readOptsQry != null) {
            try {
                readOptsQry.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            readOptsQry = null;
        }
    }

    public static QName qnameFromString(final String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return QName.valueOf(string);
    }
}
