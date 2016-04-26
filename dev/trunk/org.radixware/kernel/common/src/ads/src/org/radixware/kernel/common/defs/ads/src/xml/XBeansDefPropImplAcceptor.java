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
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.build.xbeans.XBeansPropImplAcceptor;


class XBeansDefPropImplAcceptor implements XBeansPropImplAcceptor {

    private XBeansClassProp prop;
    private final XBeansClass clazz;

    XBeansDefPropImplAcceptor(XBeansClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public void beginProperty(SchemaProperty prop, QName qName, String propertyName, String arrayName, String listName, String type, String xtype, boolean isAttr, boolean several,boolean isDeprecated) {
        this.prop = new XBeansClassProp(propertyName, type, xtype, isAttr);
    }

    @Override
    public void endProperty() {
        clazz.addProperty(prop);
    }

    @Override
    public void acceptSingletonPropGetter(SchemaProperty prop, String jtargetType, String identifier, String setIdentifier, int javaType, boolean isRadix) throws IOException {
        this.prop.setHasSingletonPropGetter(true);
    }

    @Override
    public void acceptSingletonPropXmlGetter(SchemaProperty prop, String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSingletonPropXmlGetter(true);
    }

    @Override
    public void acceptSingletonPropNullCheck(String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSingletonPropNullCheck(true);
    }

    @Override
    public void acceptOptionalPropExistanceCheck(String identifier, String setIdentifier) throws IOException {
        this.prop.setHasOptionalPropExistanceCheck(true);
    }

    @Override
    public void acceptSeveralPropArrayAccess(SchemaProperty prop, String setIdentifier, int javaType) throws IOException {
        this.prop.setHasSeveralPropArrayAccess(true);
    }

    @Override
    public void acceptSeveralPropArrayElementAccess(SchemaProperty prop, String identifier, String setIdentifier, String jtargetType) throws IOException {
        this.prop.setHasSeveralPropArrayElementAccess(true);
    }

    @Override
    public void acceptSeveralPropArrayXmlGetter(String setIdentifier) throws IOException {
        this.prop.setHasSeveralPropArrayXmlGetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementXmlGetter(String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSeveralPropArrayElementXmlGetter(true);
    }

    @Override
    public void acceptSeveralPropNullCheck(String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSeveralPropNullCheck(true);
    }

    @Override
    public void acceptSeveralPropSizeAccess(String setIdentifier) throws IOException {
        this.prop.setHasSeveralPropSizeAccess(true);
    }

    @Override
    public void acceptListGetter15GetList(boolean xget, String wrappedType, String parentThis, boolean xmltype) throws IOException {
        this.prop.setHasListGetter15GetList(true);
    }

    @Override
    public void acceptListSetter15AssignList(SchemaType sType, String identifier, String setIdentifier, boolean isobj, boolean isSubstGroup) throws IOException {
        this.prop.setHasSeveralPropListAssignment(true);
    }

    @Override
    public void acceptSingletonPropSetter(SchemaType sType, SchemaProperty prop, String jtargetType, int javaType, String identifier, String setIdentifier, boolean isRadix, boolean xmltype, String jSet, boolean optional, boolean nillable) throws IOException {
        this.prop.setHasSingletonPropSetter(true);
    }

    @Override
    public void acceptSingletonPropXmlSetter(SchemaType sType, String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSingletonPropXmlSetter(true);
    }

    @Override
    public void acceptSingletonPropCreation(SchemaType sType, String identifier) throws IOException {
        this.prop.setHasSingletonPropCreation(true);
    }

    @Override
    public void acceptSingletonPropSetNull(SchemaType sType, String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSingletonPropSetNull(true);
    }

    @Override
    public void acceptOptionalPropUnset(SchemaType sType, String identifier, String setIdentifier) throws IOException {
        this.prop.setHasOptionalPropUnset(true);
    }

    @Override
    public void acceptSeveralPropArraySetter(SchemaType sType, String identifier, String setIdentifier, boolean isobj, boolean isSubstGroup) throws IOException {
        this.prop.setHasSeveralPropArraySetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementSetter(SchemaType sType, SchemaProperty prop, String identifier, String setIdentifier, String jtargetType, String jSet, int javaType) throws IOException {
        this.prop.setHasSeveralPropArrayElementSetter(true);
    }

    @Override
    public void acceptSeveralPropArrayXmlSetter(SchemaType sType, String identifier) throws IOException {
        this.prop.setHasSeveralPropArrayXmlSetter(true);
    }

    @Override
    public void acceptSeveralPropArrayElementXmlSetter(SchemaType sType, String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSeveralPropArrayElementXmlSetter(true);
    }

    @Override
    public void acceptSeveralPropSetNull(SchemaType sType, String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSeveralPropSetNull(true);
    }

    @Override
    public void acceptSeveralPropElementInsertion(SchemaType sType, String identifier, String setIdentifier, String jtargetType, boolean isSubstGroup, String jSet, int javaType) throws IOException {
        this.prop.setHasSeveralPropElementInsertion(true);
    }

    @Override
    public void acceptSeveralPropElementAddition(SchemaType sType, String identifier, String jtargetType, int javaType, String jSet) throws IOException {
        this.prop.setHasSeveralPropElementAddition(true);
    }

    @Override
    public void acceptSeveralPropNewElementInsertion(SchemaType sType, String identifier, String setIdentifier, boolean isSubstGroup) throws IOException {
        this.prop.setHasSeveralPropNewElementInsertion(true);
    }

    @Override
    public void acceptSeveralPropNewElementAddition(SchemaType sType, String identifier) throws IOException {
        this.prop.setHasSeveralPropNewElementAddition(true);
    }

    @Override
    public void acceptSeveralPropElementRemove(SchemaType sType, String identifier, String setIdentifier) throws IOException {
        this.prop.setHasSeveralPropElementRemove(true);
    }
}
