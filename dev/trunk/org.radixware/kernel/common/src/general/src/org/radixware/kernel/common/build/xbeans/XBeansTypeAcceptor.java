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
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;


public interface XBeansTypeAcceptor {

    void beginType(SchemaType sType, SchemaTypeSystem system) throws IOException;

    void endType() throws IOException;

    XBeansTypeAcceptor createInnerType();

    XBeansPropAcceptor createPropAcceptor();

    void acceptTypeJavaDoc() throws IOException;

    void acceptInterfaceStart(String shortName, String baseInterface, String[] extensionInterfaces, boolean isDeprecated, boolean isDocumentType) throws IOException;

    void acceptStaticDeclaration() throws IOException;

    void acceptStringEnumeration(SchemaType baseEnumType, String baseEnumClass) throws IOException;

    void acceptStringEnumerationItem(String constName, String enumValue) throws IOException;

    void acceptStringEnumerationItemAsInt(String constName) throws IOException;

    void acceptStringEnumerationClassWithTable(SchemaStringEnumEntry[] entries) throws IOException;

    /**
     * getIntValue(),getBigDecimalValue(),etc...
     */
    void acceptSpecializedDecimalAccessor(int size) throws IOException;

    /**
     * getObjectValue() setObjectValue() methods
     */
    void acceptSpecializedObjectAccessor() throws IOException;

    /**
     * List<....> getListValue() setObjectValue() methods
     */
    void acceptSpecializedListAccessor(String wildcard) throws IOException;

    void acceptTypeFactory(String fullName, boolean fullFactory, boolean isSimpleType, boolean isAbstract) throws IOException;
}
