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

package org.radixware.kernel.server.arte.resources.rpc;

import java.io.IOException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.common.ServerStreamConnection;
import org.apache.xmlrpc.common.TypeConverterFactory;
import org.apache.xmlrpc.common.TypeConverterFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.apache.xmlrpc.common.XmlRpcStreamRequestProcessor;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.resources.Resource;
import org.radixware.schemas.eas.ClientMethodInvocationMess;
import org.radixware.schemas.eas.ClientMethodInvocationRq;
import org.radixware.schemas.easWsdl.ClientMethodInvocationDocument;

/**
 * Tunneling XmlRpc through EAS protocol
 */
class XmlRpcEasRequestProcessor implements XmlRpcStreamRequestProcessor {    
    private final Arte arte;

    public XmlRpcEasRequestProcessor(final Arte arte) {
        this.arte = arte;
    }
    private static final TypeConverterFactory TYPE_CONVERTER_FACTORY = new TypeConverterFactoryImpl();

    @Override
    public void execute(final XmlRpcStreamRequestConfig config, final ServerStreamConnection xmlRpcConnection) throws XmlRpcException {
        final ClientMethodInvocationDocument document = ClientMethodInvocationDocument.Factory.newInstance();
        final ClientMethodInvocationRq request = document.addNewClientMethodInvocation().addNewClientMethodInvocationRq();
        try {
            request.setRpcRequest(XmlObject.Factory.parse(xmlRpcConnection.newInputStream()));
        } catch (XmlException exception) {
            throw new XmlRpcException("Can't parse Xml-Rpc request", exception);
        } catch (IOException exception) {
            throw new XmlRpcException("Can't parse Xml-Rpc request", exception);
        }
        final ClientMethodInvocationMess responseMess;
        try {
            responseMess = (ClientMethodInvocationMess) arte.getArteSocket().invokeResource(document, ClientMethodInvocationMess.class, Resource.DEFAULT_TIMEOUT / 1000);
        } catch (ResourceUsageException ex) {
            throw new XmlRpcException("Can't execute client callback request", ex);
        } catch (ResourceUsageTimeout ex) {
            return;
        } catch (InterruptedException ex) {
            return;
        }
        final XmlObject response = responseMess.getClientMethodInvocationRs().getRpcResponse();
        if (response != null) {
            try {
                response.save(xmlRpcConnection.newOutputStream());
            } catch (IOException exception) {
                throw new XmlRpcException("Can't write Xml-Rpc response", exception);
            }
        }
    }

    /**
     * Is never called in XmlRpcStreamRequestProcessor
     * @param xrr
     * @return
     * @throws XmlRpcException 
     */
    @Override
    public Object execute(final XmlRpcRequest xrr) throws XmlRpcException {
        return null;
    }

    @Override
    public TypeConverterFactory getTypeConverterFactory() {
        return TYPE_CONVERTER_FACTORY;
    }
    
}
