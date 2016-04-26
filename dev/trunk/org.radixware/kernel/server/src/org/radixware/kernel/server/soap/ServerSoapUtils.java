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

package org.radixware.kernel.server.soap;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.BusFactory;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.wsdl.WSDLManager;
import org.apache.ws.security.components.crypto.Crypto;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.ESoapOption;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.SoapServiceOptions;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.sap.SapQueries;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public class ServerSoapUtils {

    public static final String HTTP_URL_PREFIX = "http://";

    public static String getDispatchKey(final SapClientOptions sap) {
        String serviceName = "null";
        if (sap.getSoapServiceOptions().getServiceName() != null) {
            serviceName = sap.getSoapServiceOptions().getServiceName().toString();
        }
        String portName = "null";
        if (sap.getSoapServiceOptions().getPortName() != null) {
            portName = sap.getSoapServiceOptions().getPortName().toString();
        }

        return StringUtils.join(new Object[]{ServerSoapUtils.getWsdlKey(sap.getSoapServiceOptions()), serviceName, portName}, "~");
    }

    public static String getWsdlKey(final SoapServiceOptions serviceOptions) {
        if (serviceOptions == null) {
            return null;
        }
        return StringUtils.join(new Object[]{serviceOptions.getWsdlSource().toString(), serviceOptions.getLastUpdateTimeMillis()}, "~");
    }

    /**
     * Get WSDL url from global cache (key is wsdl source and last modification
     * time) or load it from database (ant put it in the cache).
     */
    public static URL getWsdlUrl(final SoapServiceOptions soapServiceOptions, final Connection dbConnection, final LocalTracer tracer) throws IOException {
        final String wsdlKey = getWsdlKey(soapServiceOptions);
        File file = RadixLoader.getInstance().getTempFile(wsdlKey);
        if (file == null) {
            byte[] data;
            try {
                data = SapQueries.readWsdlData(soapServiceOptions.getWsdlSource(), dbConnection);
                if (data == null) {
                    throw new IOException("There is no wsdl data for source " + soapServiceOptions.getWsdlSource().toString());
                }
                file = RadixLoader.getInstance().getOrCreateTempFile(wsdlKey, data);
                if (tracer != null) {
                    tracer.debug("Wsdl data for " + wsdlKey + " has been loaded from database", false);
                }
            } catch (SQLException ex) {
                throw new IOException(ex);
            }

        }
        return file.toURI().toURL();
    }

    public static QName[] getServiceAndPortName(final SoapServiceOptions soapServiceOptions, final URL wsdlUrl) throws WSDLException {
        QName serviceName = null;
        QName portName = null;
        if (soapServiceOptions != null) {
            serviceName = soapServiceOptions.getServiceName();
            portName = soapServiceOptions.getPortName();
        }

        if (wsdlUrl != null && (serviceName == null || portName == null)) {
            final WSDLManager wsdlManager = BusFactory.getThreadDefaultBus().getExtension(WSDLManager.class);
            final Definition wsdlDefinition = wsdlManager.getDefinition(wsdlUrl);
            if (serviceName == null) {
                for (Object serviceObj : wsdlDefinition.getAllServices().values()) {
                    if (serviceObj instanceof javax.wsdl.Service) {
                        javax.wsdl.Service service = (javax.wsdl.Service) serviceObj;
                        serviceName = service.getQName();
                        if (service.getPorts().size() > 0) {
                            Object portObj = service.getPorts().values().iterator().next();
                            if (portObj instanceof javax.wsdl.Port) {
                                portName = new QName(serviceName.getNamespaceURI(), ((javax.wsdl.Port) portObj).getName());
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (serviceName == null) {
            throw new WSDLException("", "Unable to determine service name");
        }
        if (portName == null) {
            throw new WSDLException("", "Unable to determine port name");
        }
        return new QName[]{serviceName, portName};
    }

    public static void configureWsSecurity(final Map<String, Object> requestContext, final Map<String, String> additionalAttrs, final LocalTracer tracer, final String sapInfo) {
        try {
            List<String> trustedCertAliases = null;
            if (additionalAttrs != null) {
                String aliasesAsStr = additionalAttrs.get(ESoapOption.RDX_WS_TRUSTED_CERT_ALIASES.getValue());
                if (aliasesAsStr != null) {
                    trustedCertAliases = new ArrayList<>();
                    trustedCertAliases.addAll(ArrStr.fromValAsStr(aliasesAsStr));
                }
            }
            final Crypto radixCrypto = new RadixCrypto(trustedCertAliases);
            final Map<String, Object> ctx = new HashMap<>();
            ctx.put("ws-security.signature.crypto", radixCrypto);
            ctx.put("ws-security.encryption.crypto", radixCrypto);
            ctx.put("ws-security.callback-handler", "org.radixware.kernel.server.soap.RadixPasswordCallback");
            requestContext.putAll(ctx);
        } catch (KeystoreControllerException ex) {
            if (tracer != null) {
                tracer.debug("Unable to init crypto engine for " + sapInfo + ": " + ExceptionTextFormatter.throwableToString(ex), true);
            }
        }
    }

    public static void setupRequestPath(final EndpointReferenceType endpointInfo, final Map<String, String> attrs) {
        if (attrs == null) {
            return;
        }
        if (endpointInfo != null && endpointInfo.getAddress() != null && endpointInfo.getAddress().getValue() != null) {
            String address = endpointInfo.getAddress().getValue();

            if (address.startsWith(HTTP_URL_PREFIX)) {
                final int hostEndSlashIdx = address.indexOf('/', HTTP_URL_PREFIX.length());
                if (hostEndSlashIdx != -1) {
                    attrs.put(EHttpParameter.HTTP_REQ_PATH_PARAM.getValue(), address.substring(hostEndSlashIdx));
                }
            }
        }
    }
}
