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
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.radixware.kernel.common.build.xbeans.XBeansPropAcceptor;
import org.radixware.kernel.common.build.xbeans.XBeansTypeAcceptor;


class XBeansDefTypeAcceptor implements XBeansTypeAcceptor {

    private XBeansInterface iface;
    private XBeansInterface outer;
    private final XBeansTypeSystem ts;

    XBeansDefTypeAcceptor(XBeansTypeSystem ts) {
        this.ts = ts;
        this.outer = null;
    }

    XBeansDefTypeAcceptor(XBeansInterface outer) {
        this.outer = outer;
        this.ts = null;
    }

    @Override
    public void beginType(SchemaType sType, SchemaTypeSystem system) throws IOException {
    }

    @Override
    public void endType() throws IOException {
        if (outer != null) {
            outer.addInnerInterface(iface);
        } else {
            ts.addInterface(iface);
        }
    }

    @Override
    public XBeansTypeAcceptor createInnerType() {
        return new XBeansDefTypeAcceptor(iface);
    }

    @Override
    public XBeansPropAcceptor createPropAcceptor() {
        return new XBeansDefPropAcceptor(iface);
    }

    @Override
    public void acceptTypeJavaDoc() throws IOException {
        //DO NOTHING
    }

    @Override
    public void acceptInterfaceStart(String shortName, String baseInterface, String[] extensionInterfaces, boolean isDeprecated, boolean isDocType) throws IOException {
        iface = new XBeansInterface(ts, outer, shortName, baseInterface, extensionInterfaces, isDeprecated, isDocType);
    }

    @Override
    public void acceptStaticDeclaration() throws IOException {
    }

    @Override
    public void acceptStringEnumeration(SchemaType baseEnumType, String baseEnumClass) throws IOException {
        iface.setContent(new XBeansInterface.SimpleContent(iface, baseEnumClass));
    }

    @Override
    public void acceptStringEnumerationItem(String constName, String enumValue) throws IOException {
        XBeansInterface.SimpleContent content = (XBeansInterface.SimpleContent) iface.getContent();
        content.addEnumField(new XBeansInterface.SimpleContent.EnumField(constName, enumValue));
    }

    @Override
    public void acceptStringEnumerationItemAsInt(String constName) throws IOException {
        XBeansInterface.SimpleContent content = (XBeansInterface.SimpleContent) iface.getContent();
        content.addIndexField(constName);
    }

    @Override
    public void acceptStringEnumerationClassWithTable(SchemaStringEnumEntry[] entries) throws IOException {
        XBeansInterface.SimpleContent content = (XBeansInterface.SimpleContent) iface.getContent();
        for (SchemaStringEnumEntry e : entries) {
            content.addElement(new XBeansInterface.SimpleContent.EnumerationElement(e.getEnumName(), e.getString(), e.getIntValue()));
        }
    }

    @Override
    public void acceptSpecializedDecimalAccessor(int size) throws IOException {
        iface.setDecimalAccType(size);
    }

    @Override
    public void acceptSpecializedObjectAccessor() throws IOException {
        iface.setHasObjectAccessor(true);
    }

    @Override
    public void acceptSpecializedListAccessor(String wildcard) throws IOException {
        iface.setListAccessorWildcard(wildcard);
    }

    @Override
    public void acceptTypeFactory(String fullName, boolean fullFactory, boolean isSimpleType, boolean isAbstract) throws IOException {
        iface.setFactoryInfo(new XBeansInterface.FactoryInfo(fullFactory, fullName, isAbstract, isSimpleType));
    }
}
