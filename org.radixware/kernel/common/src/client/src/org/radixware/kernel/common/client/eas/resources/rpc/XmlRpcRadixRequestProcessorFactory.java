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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.RequestProcessorFactory;
import org.radixware.kernel.common.client.IClientEnvironment;


class XmlRpcRadixRequestProcessorFactory implements RequestProcessorFactory {
    private final IClientEnvironment environment;
    private final Map<String, Object> resources = new HashMap<>(16);

    public XmlRpcRadixRequestProcessorFactory(final IClientEnvironment environment) {
        this.environment = environment;
    }

    @SuppressWarnings("unchecked")
    private Constructor findResourceConstructor(final Class resourceClass, final Class<?>[] types) {
        try {
            return resourceClass.getConstructor(types);
        } catch (NoSuchMethodException exception) {
            return null;
        } catch (SecurityException exception) {
            return null;
        }
    }

    @Override
    public Object getRequestProcessor(final XmlRpcRequest request) throws XmlRpcException {
        final RpcRequestInfo requestInfo = new RpcRequestInfo(request.getMethodName(), environment.getDefManager());
        Object resource = resources.get(requestInfo.getInstanceKey());
        if (resource == null) {
            try {
                final Class<?> resourceClass = environment.getDefManager().getClassLoader().loadClass(requestInfo.getClassName());
                Constructor constructor = findResourceConstructor(resourceClass, new Class<?>[]{IClientEnvironment.class});
                if (constructor == null) {
                    constructor = findResourceConstructor(resourceClass, new Class<?>[]{});
                    if (constructor == null) {
                        throw new XmlRpcException(String.format("Cannot create an instance of %s class: no suitable constructors found", resourceClass.getName()));
                    } else {
                        try {
                            resource = constructor.newInstance();
                        } catch (Exception ex) {
                            throw new XmlRpcException(String.format("Cannot create an instance of %s class", resourceClass.getName()), ex);
                        }
                    }
                } else {
                    try {
                        resource = constructor.newInstance(environment);
                    } catch (Exception ex) {
                        throw new XmlRpcException(String.format("Cannot create an instance of %s class", resourceClass.getName()), ex);
                    }
                }
            } catch (ClassNotFoundException ex) {
                throw new XmlRpcException("Requested resource was not found", ex);
            }
            resources.put(requestInfo.getInstanceKey(), resource);
        }
        return resource;
    }

    public void clearCahce() {
        resources.clear();
    }
    
}
