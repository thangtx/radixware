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
import javax.xml.namespace.QName;
import org.radixware.kernel.common.build.xbeans.XBeansPropAcceptor;


class XBeansDefPropAcceptor implements XBeansPropAcceptor {

    private XBeansInterface iface;
    private XBeansIfaceProp prop = null;

    XBeansDefPropAcceptor(XBeansInterface iface) {
        this.iface = iface;
    }

    @Override
    public void beginProperty(QName qName, String propertyName, String arrayName, String type, String xtype, boolean isAttr, boolean isDeprecated) {
        prop = new XBeansIfaceProp(iface, propertyName, isDeprecated, isAttr, type, xtype);
    }

    @Override
    public void endProperty() {
        XBeansInterface.ComplexContent content = (XBeansInterface.ComplexContent) iface.getContent();
        if (content == null) {
            content = new XBeansInterface.ComplexContent(iface);
            iface.setContent(content);
        }
        content.addProperty(prop);
    }

    @Override
    public void acceptSingletonPropGetter(boolean several) throws IOException {
        prop.setHasSingletonGetter(true);
    }

    @Override
    public void acceptSingletonPropSetter(boolean several) throws IOException {
        prop.setHasSingletonSetter(true);
    }

    @Override
    public void acceptSingletonPropXmlGetter(boolean several) throws IOException {
        prop.setHasSingletonGetterXml(true);
    }

    @Override
    public void acceptSingletonPropXmlSetter(boolean several) throws IOException {
        prop.setHasSingletonSetterXml(true);
    }

    @Override
    public void acceptSingletonPropNullCheck(boolean several) throws IOException {
        prop.setHasSingletonNullCheck(true);
    }

    @Override
    public void acceptSingletonXmlCreation() throws IOException {
        prop.setHasSingletonCreator(true);
    }

    @Override
    public void acceptSingletonPropSetNull(boolean several) throws IOException {
        prop.setHasSingletonUnset(true);
    }

    @Override
    public void acceptOptionalPropUnset(boolean several) throws IOException {
        prop.setHasOptionalUnset(true);
    }

    @Override
    public void acceptOptionalPropExistanceCheck(boolean several) throws IOException {
        prop.setHasOptionalExistanceCheck(true);
    }

    @Override
    public void acceptSeveralPropListGetter(String wrappedType) throws IOException {
        prop.setHasSeveralPropListGetter(true, wrappedType);
    }

    @Override
    public void acceptSeveralPropArrayGetter() throws IOException {
        prop.setHasSeveralPropArrayGetter(true);
    }

    @Override
    public void acceptSeveralPropArraySetter() throws IOException {
        prop.setHasSeveralPropArraySetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementGetter() throws IOException {
        prop.setHasSeveralPropArrayElementGetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementSetter() throws IOException {
        prop.setHasSeveralPropArrayElementSetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementSetNull() throws IOException {
        prop.setHasSeveralPropArrayElementSetNull(true);
    }

    @Override
    public void acceptSeveralPropListXmlGetter() throws IOException {
        prop.setHasSeveralPropListXmlGetter(true);
    }

    @Override
    public void acceptSeveralPropArrayXmlGetter() throws IOException {
        prop.setHasSeveralPropArrayXmlGetter(true);
    }

    @Override
    public void acceptSeveralPropArrayXmlSetter() throws IOException {
        prop.setHasSeveralPropArrayXmlSetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementXmlGetter() throws IOException {
        prop.setHasSeveralPropArrayElementXmlGetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementXmlSetter() throws IOException {
        prop.setHasSeveralPropArrayElementXmlSetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementInsertion() throws IOException {
        prop.setHasSeveralPropArrayElementInsertion(true);
    }

    @Override
    public void acceptSeveralPropArrayElementAddition() throws IOException {
        prop.setHasSeveralPropArrayElementAddition(true);
    }

    @Override
    public void acceptSeveralPropArrayNewElementInsertion() throws IOException {
        prop.setHasSeveralPropArrayNewElementInsertion(true);
    }

    @Override
    public void acceptSeveralPropArrayNewElementAddition() throws IOException {
        prop.setHasSeveralPropArrayNewElementAddition(true);
    }

    @Override
    public void acceptSeveralPropArrayElementRemoving() throws IOException {
        prop.setHasSeveralPropArrayElementRemoving(true);
    }

    @Override
    public void acceptSeveralPropNullCheck() throws IOException {
        prop.setHasSeveralPropNullCheck(true);
    }

    @Override
    public void acceptSeveralPropSizeAccess() throws IOException {
        prop.setHasSeveralPropSizeAccess(true);
    }

    @Override
    public void acceptSeveralPropListAssignment() throws IOException {
        prop.setHasSeveralPropListAssignment(true);
    }

    @Override
    public void acceptSingletonPropGetterDateTimeWithTimezone(boolean several) throws IOException {
        prop.setHasSingletonGetterDateTimeWithTimezone(true);
    }

    @Override
    public void acceptSingletonPropSetterDateTimeWithTimezone(boolean several) throws IOException {
        prop.setHasSingletonSetterDateTimeWithTimezone(true);
    }

    @Override
    public void acceptSeveralPropListGetterDateTimeWithTimezone(String wrappedType) throws IOException {
        prop.setHasSeveralPropListGetterDateTimeWithTimezone(true);
    }
}
