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

package org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

final class AttrWrapper extends BaseNodeWrapper implements org.w3c.dom.Attr{        
    
    private final AttributesWrapper attrs;
    private final String name;
    
    public AttrWrapper(final String name, final AttributesWrapper attrs){
        this.name = name;
        this.attrs = attrs;
    }

    @Override
    public String getNodeName() {
        return name;
    }

    @Override
    public String getNodeValue() throws DOMException {
        return getValue();
    }

    @Override
    public short getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    @Override
    public Node getParentNode() {
        return attrs.getOwner();
    }

    @Override
    public NodeList getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    @Override
    public Node getFirstChild() {
        return null;
    }

    @Override
    public Node getLastChild() {
        return null;
    }

    @Override
    public Node getPreviousSibling() {
        final List<String> orderedNames = attrs.getPropertySet().getOrderedNames();
        final int index = orderedNames.indexOf(name);
        if (index<=0){
            return null;
        }
        return attrs.getNamedItem(orderedNames.get(index-1));
    }

    @Override
    public Node getNextSibling() {
        final List<String> orderedNames = attrs.getPropertySet().getOrderedNames();
        final int index = orderedNames.indexOf(name);
        if (index<0 || index>orderedNames.size()-2){
            return null;
        }
        return attrs.getNamedItem(orderedNames.get(index+1));
    }

    @Override
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override
    public Document getOwnerDocument() {
        return attrs.getOwner().getOwnerDocument();
    }

    @Override
    public boolean hasChildNodes() {
        return false;
    }

    @Override
    public String getLocalName() {
        return name;
    }

    @Override
    public boolean hasAttributes() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean getSpecified() {
        return true;
    }

    @Override
    public String getValue() {
        try{
            return attrs.getPropertySet().getValue(name);
        }catch(IllegalAccessException | IllegalArgumentException exception){
            throw new DOMException(DOMException.INVALID_ACCESS_ERR, exception.getMessage());
        }catch(InvocationTargetException exception){
            return "null";
        }
    }

    @Override
    public void setValue(final String value) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Element getOwnerElement() {
        return attrs.getOwner();
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
        return StringTypeInfo.INSTANCE;
    }

    @Override
    public boolean isId() {
        return false;
    }

}
