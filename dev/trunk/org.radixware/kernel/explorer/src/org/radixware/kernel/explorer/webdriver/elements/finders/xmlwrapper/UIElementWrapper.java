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

import java.util.List;
import java.util.Stack;
import org.radixware.kernel.explorer.webdriver.elements.UIElementPropertySet;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

abstract class UIElementWrapper extends BaseNodeWrapper implements org.w3c.dom.Element{
    
    private final BaseNodeWrapper parentNode;
    private final NodeListWrapper childNodes;
    private final Document ownerDocument;
    private final AttributesWrapper attrs;
    private final String name;
    
    protected UIElementWrapper(final String name, final BaseNodeWrapper parent, final Document document){
        this.name = name;
        parentNode = parent;
        ownerDocument = document;
        childNodes = new NodeListWrapper(this);
        attrs = new AttributesWrapper(this);
    }        
    
    @Override
    public String getNodeName() {
        return name;
    }

    @Override
    public String getTagName() {
        return name;
    }    

    @Override
    public String getNodeValue() throws DOMException {
        return null;
    }

    @Override
    public short getNodeType() {
        return Node.ELEMENT_NODE;
    }

    @Override
    public Node getParentNode() {
        return parentNode;
    }

    @Override
    public NodeList getChildNodes() {
        return childNodes;
    }

    @Override
    public Node getFirstChild() {
        return childNodes.getLength()>0 ? childNodes.item(0) : null;
    }

    @Override
    public Node getLastChild() {
        final int count = childNodes.getLength();
        return count>0 ? childNodes.item(count-1) : null;
    }

    @Override
    public Node getPreviousSibling() {
        final NodeList children = parentNode.getChildNodes();
        if (children instanceof NodeListWrapper){
            final int index = ((NodeListWrapper)children).getIndexOf(this);
            if (index<=0){
                return null;
            }
            return children.item(index-1);            
        }else{
            return null;
        }
    }

    @Override
    public Node getNextSibling() {
        final NodeList children = parentNode.getChildNodes();
        if (children instanceof NodeListWrapper){
            final int index = ((NodeListWrapper)children).getIndexOf(this);
            final int count = children.getLength();
            if (index<0 || index>count-2){
                return null;
            }
            return children.item(index+1);            
        }else{
            return null;
        }
    }

    @Override
    public NamedNodeMap getAttributes() {
        return attrs;
    }

    @Override
    public Document getOwnerDocument() {
        return ownerDocument;
    }

    @Override
    public boolean hasChildNodes() {
        return childNodes.getLength()>0;
    }

    @Override
    public String getLocalName() {
        return this.getTagName();
    }

    @Override
    public boolean hasAttributes() {
        return true;
    }

    @Override
    public String getAttribute(final String name) {
        return attrs.getNamedItem(name).getNodeValue();
    }

    @Override
    public void setAttribute(String name, String value) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public void removeAttribute(String name) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Attr getAttributeNode(final String name) {
        return (Attr)attrs.getNamedItem(name);
    }

    @Override
    public Attr setAttributeNode(final Attr newAttr) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Attr removeAttributeNode(final Attr oldAttr) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public NodeList getElementsByTagName(final String name) {
        final Stack<UIElementWrapper> stack = new Stack<>();
        stack.push(this);
        final NodeListImpl result = new NodeListImpl();
        UIElementWrapper element;
        while(!stack.isEmpty()){
            element = stack.pop();
            result.expand(element.childNodes.getNodesByName(name));
            for (int i=0, count=element.childNodes.getLength(); i<count; i++){
                stack.push(element.childNodes.item(i));
            }
        }
        return result;
    }

    @Override
    public String getAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        return null;
    }

    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public void removeAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Attr getAttributeNodeNS(final String namespaceURI, final String localName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Attr setAttributeNodeNS(final Attr newAttr) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public NodeList getElementsByTagNameNS(final String namespaceURI, final String localName) throws DOMException {
        return getElementsByTagName(localName);
    }

    @Override
    public boolean hasAttribute(final String name) {
        return attrs.getPropertySet().containsProperty(name);
    }

    @Override
    public boolean hasAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        return false;
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
        return VoidTypeInfo.INSTANCE;
    }

    @Override
    public void setIdAttribute(final String name, final boolean isId) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public void setIdAttributeNS(final String namespaceURI, final String localName, final boolean isId) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public void setIdAttributeNode(final Attr idAttr, final boolean isId) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }
    
    public abstract List<UIElementWrapper> getChildElements();

    public abstract UIElementPropertySet getPropertySet();
    
    public abstract UIElementReference getElementReference(final WebDrvUIElementsManager manager);
}
