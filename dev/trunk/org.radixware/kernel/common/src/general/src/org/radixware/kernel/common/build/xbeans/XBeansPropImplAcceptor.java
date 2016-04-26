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

package org.radixware.kernel.common.build.xbeans;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;


public interface XBeansPropImplAcceptor {

    void beginProperty(SchemaProperty prop, QName qName, String propertyName, String arrayName, String listName, String type, String xtype, boolean isAttr, boolean several, boolean isDeprecated);

    void endProperty();

    void acceptSingletonPropGetter(SchemaProperty prop, String jtargetType, String identifier, String setIdentifier, int javaType, boolean isRadix) throws IOException;

    void acceptSingletonPropXmlGetter(SchemaProperty prop, String identifier, String setIdentifier) throws IOException;

    void acceptSingletonPropNullCheck(String identifier, String setIdentifier) throws IOException;

    void acceptOptionalPropExistanceCheck(String identifier, String setIdentifier) throws IOException;

    void acceptSeveralPropArrayAccess(SchemaProperty prop, String setIdentifier, int javaType) throws IOException;

    void acceptSeveralPropArrayElementAccess(SchemaProperty prop, String identifier, String setIdentifier, String jtargetType) throws IOException;

    void acceptSeveralPropArrayXmlGetter(String setIdentifier) throws IOException;

    void acceptSeveralPropArrayElementXmlGetter(String identifier, String setIdentifier) throws IOException;

    void acceptSeveralPropNullCheck(String identifier, String setIdentifier) throws IOException;

    void acceptSeveralPropSizeAccess(String setIdentifier) throws IOException;

    void acceptListGetter15GetList(boolean xget, String wrappedType, String parentThis, boolean xmltype) throws IOException;

    void acceptListSetter15AssignList(SchemaType sType, String identifier, String setIdentifier, boolean isobj, boolean isSubstGroup) throws IOException;

    void acceptSingletonPropSetter(SchemaType sType, SchemaProperty prop, String jtargetType, int javaType, String identifier, String setIdentifier, boolean isRadix, boolean xmltype, String jSet, boolean optional, boolean nillable) throws IOException;

    void acceptSingletonPropXmlSetter(SchemaType sType, String identifier, String setIdentifier) throws IOException;

    void acceptSingletonPropCreation(SchemaType sType, String identifier) throws IOException;

    void acceptSingletonPropSetNull(SchemaType sType, String identifier, String setIdentifier) throws IOException;

    void acceptOptionalPropUnset(SchemaType sType, String identifier, String setIdentifier) throws IOException;

    void acceptSeveralPropArraySetter(SchemaType sType, String identifier, String setIdentifier, boolean isobj, boolean isSubstGroup) throws IOException;

    void acceptSeveralPropArrayElementSetter(SchemaType sType, SchemaProperty prop, String identifier, String setIdentifier, String jtargetType, String jSet, int javaType) throws IOException;

    void acceptSeveralPropArrayXmlSetter(SchemaType sType, String identifier) throws IOException;

    void acceptSeveralPropArrayElementXmlSetter(SchemaType sType, String identifier, String setIdentifier) throws IOException;

    void acceptSeveralPropSetNull(SchemaType sType, String identifier, String setIdentifier) throws IOException;

    void acceptSeveralPropElementInsertion(SchemaType sType, String identifier, String setIdentifier, String jtargetType, boolean isSubstGroup, String jSet, int javaType) throws IOException;

    void acceptSeveralPropElementAddition(SchemaType sType, String identifier, String jtargetType, int javaType, String jSet) throws IOException;

    void acceptSeveralPropNewElementInsertion(SchemaType sType, String identifier, String setIdentifier, boolean isSubstGroup) throws IOException;

    void acceptSeveralPropNewElementAddition(SchemaType sType, String identifier) throws IOException;

    void acceptSeveralPropElementRemove(SchemaType sType, String identifier, String setIdentifier) throws IOException;
}
