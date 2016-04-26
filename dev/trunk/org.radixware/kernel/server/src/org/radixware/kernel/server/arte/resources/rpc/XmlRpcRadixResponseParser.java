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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.common.TypeFactory;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.apache.xmlrpc.parser.XmlRpcResponseParser;
import org.xml.sax.SAXException;


class XmlRpcRadixResponseParser extends XmlRpcResponseParser {

    private Throwable actualException;
    private final ClassLoader classLoader;

    public XmlRpcRadixResponseParser(final XmlRpcStreamRequestConfig config, final TypeFactory typeFactory, final ClassLoader adsClassLoader) {
        super(config, typeFactory);
        classLoader = adsClassLoader;
    }

    @Override
    protected void addResult(final Object result) throws SAXException {
        super.addResult(result); //exception will be deserialized with default class loader :(
        if (!isSuccess()) {
            //so if was fault then we must to fix exception
            final Object exception = result instanceof Map ? ((Map) result).get("faultCause") : null;
            if (exception != null) {
                try {
                    final byte[] bytes = (byte[]) exception;
                    final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    final ObjectInputStream ois = new ObjectInputStream(bais) {
                        @Override
                        protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
                            return Class.forName(osc.getName(), false, classLoader);
                        }
                    };
                    actualException = (Throwable) ois.readObject();
                    ois.close();
                    bais.close();
                } catch (Throwable ex) {
                    // Ignore me
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public Throwable getErrorCause() {
        return actualException == null ? super.getErrorCause() : actualException;
    }
}
