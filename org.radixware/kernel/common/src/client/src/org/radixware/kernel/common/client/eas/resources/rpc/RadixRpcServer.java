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

package org.radixware.kernel.common.client.eas.resources.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.TimeZone;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.common.ServerStreamConnection;
import org.apache.xmlrpc.common.TypeConverterFactory;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.RequestProcessorFactory;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.server.XmlRpcErrorLogger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.rpc.XmlRpcRadixTypeConverterFactory;
import org.radixware.kernel.common.rpc.XmlRpcRadixTypeFactory;
import org.radixware.schemas.eas.ClientMethodInvocationRq;
import org.radixware.schemas.eas.ClientMethodInvocationRs;


public final class RadixRpcServer {
    
    private static class RadixRpcConnection implements ServerStreamConnection{
        
        private final XmlObject request;
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        public RadixRpcConnection(final XmlObject request){
            this.request = request;
        }

        @Override
        public InputStream newInputStream() throws IOException {
            return request.newInputStream();
        }

        @Override
        public OutputStream newOutputStream() throws IOException {
            outputStream.reset();
            return outputStream;
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }
        
        public XmlObject getResponce() throws UnsupportedEncodingException, XmlException{
            return XmlObject.Factory.parse(outputStream.toString("UTF-8"));
        }
    }
    
    private final static XmlRpcStreamRequestConfig XML_RPC_CONFIG = new XmlRpcStreamRequestConfig() {

        @Override
        public boolean isGzipCompressing() {
            return false;
        }

        @Override
        public boolean isGzipRequesting() {
            return false;
        }

        @Override
        public boolean isEnabledForExceptions() {
            return true;
        }

        @Override
        public String getEncoding() {
            return "UTF-8";
        }

        @Override
        public boolean isEnabledForExtensions() {
            return true;
        }

        @Override
        public TimeZone getTimeZone() {
            return TimeZone.getDefault();
        }
    };        
    
    private final  static TypeConverterFactory TYPE_CONVERTER_FACTORY = new XmlRpcRadixTypeConverterFactory();
    
    private final static class RpcLogger extends XmlRpcErrorLogger{
        
        private final ClientTracer tracer;
        
        public RpcLogger(final ClientTracer clientTracer){
            super();
            tracer = clientTracer;
        }

        @Override
        public void log(final String pMessage) {
            tracer.debug(pMessage);
        }

        @Override
        public void log(final String pMessage, final Throwable pThrowable) {
            tracer.debug(pMessage, pThrowable);
        }        
    }
    
    private final IClientEnvironment environment;
    private final XmlRpcStreamServer server;
    private final XmlRpcRadixRequestProcessorFactory requestProcessorFactory;
    private XmlRpcRadixMapping mapping;
    
    public RadixRpcServer(final IClientEnvironment env){
        environment = env;        
        requestProcessorFactory = new XmlRpcRadixRequestProcessorFactory(environment);
        server = new XmlRpcStreamServer() {};        
        server.setTypeFactory(new XmlRpcRadixTypeFactory(server,environment.getDefManager().getClassLoader()));
        server.setTypeConverterFactory(TYPE_CONVERTER_FACTORY);
        server.setErrorLogger(new RpcLogger(env.getTracer()));
        initHandlerMapping();
    }
    
    private void initHandlerMapping(){
        mapping = new XmlRpcRadixMapping(environment, TYPE_CONVERTER_FACTORY);
        mapping.setRequestProcessorFactoryFactory(new RequestProcessorFactoryFactory(){
                @Override
                public RequestProcessorFactory getRequestProcessorFactory(final Class type) throws XmlRpcException {
                    return requestProcessorFactory;
                }
            
            });
        mapping.setVoidMethodEnabled(true);
        server.setHandlerMapping(mapping);
    }
    
    public ClientMethodInvocationRs processRequest(final ClientMethodInvocationRq request) throws XmlRpcException, UnsupportedEncodingException, XmlException{        
        final RadixRpcConnection connection = new RadixRpcConnection(request.getRpcRequest());
        server.execute(XML_RPC_CONFIG, connection);
        final XmlObject rpcResponse = connection.getResponce();
        final ClientMethodInvocationRs response = ClientMethodInvocationRs.Factory.newInstance();
        response.addNewRpcResponse().set(rpcResponse);
        return response;
    }
}
