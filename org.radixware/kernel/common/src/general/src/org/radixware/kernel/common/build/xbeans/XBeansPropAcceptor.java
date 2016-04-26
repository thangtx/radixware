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

public interface XBeansPropAcceptor {

    void beginProperty(QName qName, String propertyName, String arrayName, String type, String xtype, boolean isAttr, boolean isDeprecated);

    void endProperty();

    /**
     * Called when getter for singleton property is required (getXXX() method)
     */
    void acceptSingletonPropGetter(boolean several) throws IOException;

    /**
     * Called when setter for singleton property is required (setXXX() method)
     */
    void acceptSingletonPropSetter(boolean several) throws IOException;

    /**
     * Called when x-getter for singleton property is required (xgetXXX()
     * method)
     */
    void acceptSingletonPropXmlGetter(boolean several) throws IOException;

    /**
     * Called when x-setter for singleton property is required (xsetXXX()
     * method)
     */
    void acceptSingletonPropXmlSetter(boolean several) throws IOException;

    /**
     * Called when isNill() for singleton property is required
     */
    void acceptSingletonPropNullCheck(boolean several) throws IOException;

    /**
     * Called when x-type addNew() method is required
     */
    void acceptSingletonXmlCreation() throws IOException;

    /**
     * Called when setNill() method is required for singleton property
     */
    void acceptSingletonPropSetNull(boolean several) throws IOException;

    /**
     * Called when unset() method is required for singleton property
     */
    void acceptOptionalPropUnset(boolean several) throws IOException;

    /**
     * Called when isSet() for optional property is required
     */
    void acceptOptionalPropExistanceCheck(boolean several) throws IOException;

    /**
     * Called when List<wrappedType> getXXX() method is required;
     */
    void acceptSeveralPropListGetter(String wrappedType) throws IOException;

    /**
     * Called when type[] getXXX() method is required - access of array of
     * elements;
     */
    void acceptSeveralPropArrayGetter() throws IOException;

    /**
     * Called when setXXX(type[] ) method is required - write of array of
     * elements;
     */
    void acceptSeveralPropArraySetter() throws IOException;

    /**
     * Called when assignXXX(java.util.List<type>) method is required - write of
     * list of elements;
     */
    void acceptSeveralPropListAssignment() throws IOException;

    /**
     * Called when type getXXX(integer index) method is required - access of
     * array element at position index;
     */
    void acceptSeveralPropArrayElementGetter() throws IOException;

    /**
     * Called when type setXXX(integer index) method is required - write of
     * array element at position index;
     */
    void acceptSeveralPropArrayElementSetter() throws IOException;

    /**
     * Called when type setNill(integer index) method is required - write null
     * of array element at position index;
     */
    void acceptSeveralPropArrayElementSetNull() throws IOException;

    /**
     * Called when List<xtupe>getXXX() method is required - access of list of
     * XML elements;
     */
    void acceptSeveralPropListXmlGetter() throws IOException;

    /**
     * Called when x-type[] getXXX() method is required - access of array of XML
     * elements;
     */
    void acceptSeveralPropArrayXmlGetter() throws IOException;

    /**
     * Called when xsetXXX(x-type[]) method is required - write of array of XML
     * elements;
     */
    void acceptSeveralPropArrayXmlSetter() throws IOException;

    /**
     * Called when x-type getXXX(integer index) method is required - access of
     * XML array element at position index;
     */
    void acceptSeveralPropArrayElementXmlGetter() throws IOException;

    /**
     * Called when xsetXXX(integer index,x-type) method is required - write of
     * XML array element at position index;
     */
    void acceptSeveralPropArrayElementXmlSetter() throws IOException;

    /**
     * Called when xsetXXX(integer index,x-type) method is required - write of
     * XML array element at position index;
     */
    void acceptSeveralPropArrayElementInsertion() throws IOException;

    void acceptSeveralPropArrayElementAddition() throws IOException;

    void acceptSeveralPropArrayNewElementInsertion() throws IOException;

    void acceptSeveralPropArrayNewElementAddition() throws IOException;

    void acceptSeveralPropArrayElementRemoving() throws IOException;

    /**
     * Called when null check for several property is required
     */
    void acceptSeveralPropNullCheck() throws IOException;

    /**
     * Called when size access of several property is required
     */
    void acceptSeveralPropSizeAccess() throws IOException;
}
