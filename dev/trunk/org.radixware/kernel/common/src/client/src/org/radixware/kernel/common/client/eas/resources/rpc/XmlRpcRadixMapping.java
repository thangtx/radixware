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

import java.util.LinkedList;
import java.util.List;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.common.TypeConverterFactory;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.radixware.kernel.common.client.IClientEnvironment;


class XmlRpcRadixMapping extends PropertyHandlerMapping {
    private final IClientEnvironment environment;
    private final List<String> knownClasses = new LinkedList<String>();

    public XmlRpcRadixMapping(final IClientEnvironment environment, final TypeConverterFactory typeConverterFactory) {
        this.environment = environment;
        setTypeConverterFactory(typeConverterFactory);
    }

    @Override
    public XmlRpcHandler getHandler(final String pHandlerName) throws XmlRpcNoSuchHandlerException, XmlRpcException {
        final RpcRequestInfo requestInfo = new RpcRequestInfo(pHandlerName, environment.getDefManager());
        final String handlerClassName = requestInfo.getClassName();
        if (!knownClasses.contains(handlerClassName)) {
            final Class handlerClass = newHandlerClass(null, handlerClassName);
            addHandler(handlerClassName, handlerClass);
        }
        if (requestInfo.getMethodName().isEmpty()) {
            return super.getHandler(handlerClassName);
        } else {
            return super.getHandler(handlerClassName + "." + requestInfo.getMethodName());
        }
    }

    @Override
    public void addHandler(String pKey, Class pClass) throws XmlRpcException {
        super.addHandler(pKey, pClass);
        knownClasses.add(pKey);
    }

    @Override
    protected Class newHandlerClass(ClassLoader pClassLoader, String pClassName) throws XmlRpcException {
        return super.newHandlerClass(environment.getDefManager().getClassLoader(), pClassName);
    }
    
}
