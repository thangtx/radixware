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

package org.radixware.kernel.common.defs.ads.src.xml;

import java.io.IOException;
import org.apache.xmlbeans.InterfaceExtension.MethodSignature;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.build.xbeans.XBeansPropImplAcceptor;
import org.radixware.kernel.common.build.xbeans.XBeansTypeImplAcceptor;


class XBeansDefTypeImplAcceptor implements XBeansTypeImplAcceptor {

    private XBeansClass clazz;
    private final XBeansClass outer;
    private final XBeansTypeSystem ts;

    XBeansDefTypeImplAcceptor(XBeansTypeSystem ts) {
        this.ts = ts;
        this.outer = null;
    }

    XBeansDefTypeImplAcceptor(XBeansClass outer) {
        this.outer = outer;
        this.ts = null;
    }

    @Override
    public void beginType(SchemaType sType, String shortName, boolean isInner) throws IOException {
        clazz = new XBeansClass(shortName, isInner);
    }

    @Override
    public XBeansTypeImplAcceptor createInnerType() {
        return new XBeansDefTypeImplAcceptor(clazz);
    }

    @Override
    public XBeansPropImplAcceptor createPropAcceptor() {
        return new XBeansDefPropImplAcceptor(clazz);
    }

    @Override
    public void endType() throws IOException {
        if (outer != null) {
            outer.addInnerClass(clazz);
        } else {
            ts.addInterface(clazz);
        }
    }

    @Override
    public void acceptTypeJavaDoc() throws IOException {
        //do nothing
    }

    @Override
    public void acceptClassStart(String baseClass, String[] interfaces) throws IOException {
        clazz.setBaseClass(baseClass);
        clazz.setInterfaces(interfaces);
    }

    @Override
    public void acceptConstructor() throws IOException {
        clazz.setHasConstructor(true);
    }

    @Override
    public void acceptExtensionImplMethodDecl(String handler, MethodSignature method) throws IOException {
        clazz.addExtensionMethod(new XBeansClass.ExtensionMethod(handler, method));
    }

    @Override
    public void acceptExtensionImplMethodImpl(String handler, MethodSignature method) throws IOException {
        //do nothing
    }
}
