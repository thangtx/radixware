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
import java.io.InputStream;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcLocalStreamTransport;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.apache.xmlrpc.common.XmlRpcStreamRequestProcessor;
import org.apache.xmlrpc.parser.XmlRpcResponseParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Overriding standard class to be able use XmlRpcRadixResponseParser
 */
class XmlRpcRadixTransport extends XmlRpcLocalStreamTransport {
    private final ClassLoader classLoader;

    public XmlRpcRadixTransport(final XmlRpcClient client, final XmlRpcStreamRequestProcessor processor, final ClassLoader adsClassLoader) {
        super(client,processor);
        classLoader = adsClassLoader;
    }
    
    /**
     * Based on XmlRpcLocalStreamTransport.readResponse source code.
     * @param config
     * @param stream
     * @return
     * @throws XmlRpcException 
     */
    @Override
    protected Object readResponse(final XmlRpcStreamRequestConfig config, final InputStream stream) throws XmlRpcException {
        final InputSource isource = new InputSource(stream);
        final XMLReader xr = newXMLReader();
        final XmlRpcResponseParser xp;
        try {
            xp = new XmlRpcRadixResponseParser(config, getClient().getTypeFactory(), classLoader);
            xr.setContentHandler(xp);
            xr.parse(isource);
        } catch (SAXException e) {
            throw new XmlRpcClientException("Failed to parse server's response: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new XmlRpcClientException("Failed to read server's response: " + e.getMessage(), e);
        }
        if (xp.isSuccess()) {
            return xp.getResult();
        }
        Throwable t = xp.getErrorCause();
        if (t == null) {
            throw new XmlRpcException(xp.getErrorCode(), xp.getErrorMessage());
        }
        if (t instanceof XmlRpcException) {
            throw (XmlRpcException) t;
        }
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        throw new XmlRpcException(xp.getErrorCode(), xp.getErrorMessage(), t);
    }
    
}
