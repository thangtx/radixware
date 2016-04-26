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

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.apache.xmlrpc.common.TypeConverterFactory;
import org.apache.xmlrpc.common.XmlRpcStreamRequestProcessor;
import org.radixware.kernel.common.rpc.XmlRpcRadixTypeConverterFactory;
import org.radixware.kernel.common.rpc.XmlRpcRadixTypeFactory;
import org.radixware.kernel.server.arte.Arte;


class RadixRpcClient {    
    
    private static class XmlRpcRadixTransportFactory implements XmlRpcTransportFactory{
        
       private final XmlRpcTransport transport;
        
        public XmlRpcRadixTransportFactory (final XmlRpcClient client, final XmlRpcStreamRequestProcessor processor, final ClassLoader adsClassLoader){
            transport = new XmlRpcRadixTransport(client, processor,adsClassLoader);
        }

        @Override
        public XmlRpcTransport getTransport() {
            return transport;
        }                
    }    
        
    private final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    private final TypeConverterFactory typeConverterFactory = new XmlRpcRadixTypeConverterFactory();
    
    private RadixRpcClient(){
        config.setEnabledForExtensions(true);
        config.setEnabledForExceptions(true);
    }
    
    private static RadixRpcClient INSTANCE = new RadixRpcClient();
    
    public static RadixRpcClient getInstance(){        
        return INSTANCE;
    }   
    
    private ClientFactory clientFactory(final Arte arte){
        final XmlRpcClient rpcClient = new XmlRpcClient();
        rpcClient.setConfig(config);
        final XmlRpcEasRequestProcessor processor = new XmlRpcEasRequestProcessor(arte);
        rpcClient.setTypeFactory(new XmlRpcRadixTypeFactory(rpcClient,arte.getDefManager().getClassLoader()));
        final XmlRpcRadixTransportFactory transportFactory = 
                new XmlRpcRadixTransportFactory(rpcClient, processor, arte.getDefManager().getClassLoader());
        rpcClient.setTransportFactory(transportFactory);
        return new ClientFactory(rpcClient,typeConverterFactory);
    }
    
    public final Object newClientClassRemoteInstance(final Arte arte, final Class _interface, final String className){
        final String targetClassName = className==null ? _interface.getName() : className;
        final long targetInstanceIndex = System.nanoTime();
        final String targetName = targetClassName+"#"+Long.toString(targetInstanceIndex,16);
        final ClassLoader cl = arte.getDefManager().getClassLoader();
        return clientFactory(arte).newInstance(cl, _interface, targetName);        
    }
    
    public final Object newClientClassRemoteInstance(final Arte arte, final Class _interface){
        return newClientClassRemoteInstance(arte, _interface, null);
    }
}
