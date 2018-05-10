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

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DocumentWrapper extends BaseNodeWrapper implements org.w3c.dom.Document{
    
    private final RootElement root;
    
    public DocumentWrapper(){
        this(null);
    }
    
    public DocumentWrapper(final QWidget widget){
        root = new RootElement(widget, this);
    }

    @Override
    public String getNodeName() {
        return "#document";
    }

    @Override
    public String getNodeValue() throws DOMException {
        return null;
    }

    @Override
    public short getNodeType() {
        return Node.DOCUMENT_NODE;
    }

    @Override
    public Node getParentNode() {
        return null;
    }

    @Override
    public NodeList getChildNodes() {
        return new SingletonNodeListWrapper(root);
    }

    @Override
    public Node getFirstChild() {
        return root;
    }

    @Override
    public Node getLastChild() {
        return root;
    }

    @Override
    public Node getPreviousSibling() {
        return null;
    }

    @Override
    public Node getNextSibling() {
        return null;
    }

    @Override
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override
    public Document getOwnerDocument() {
        return null;
    }

    @Override
    public boolean hasChildNodes() {
        return true;
    }

    @Override
    public String getLocalName() {
        return "#document";
    }

    @Override
    public boolean hasAttributes() {
        return false;
    }

    @Override
    public DocumentType getDoctype() {
        return null;
    }

    @Override
    public DOMImplementation getImplementation() {
        return null;
    }

    @Override
    public Element getDocumentElement() {
        return root;
    }

    @Override
    public Element createElement(String tagName) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public DocumentFragment createDocumentFragment() {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Text createTextNode(String data) {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Comment createComment(String data) {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public CDATASection createCDATASection(String data) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Attr createAttribute(String name) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public EntityReference createEntityReference(String name) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override    
    public NodeList getElementsByTagName(final String tagname) {
        final NodeListImpl list = (NodeListImpl)root.getElementsByTagName(tagname);
        if (root.getTagName().equals(tagname) || "*".equals(tagname)){
            list.insert(root);
        }
        return list;
    }

    @Override
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return getElementsByTagName(localName);
    }

    @Override
    public Element getElementById(final String elementId) {
        return null;
    }

    @Override
    public String getInputEncoding() {
        return "UTF-8";
    }

    @Override
    public String getXmlEncoding() {
        return "UTF-8";
    }

    @Override
    public boolean getXmlStandalone() {
        return true;
    }

    @Override
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public String getXmlVersion() {
        return "1.0";
    }

    @Override
    public void setXmlVersion(String xmlVersion) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public boolean getStrictErrorChecking() {
        return false;
    }

    @Override
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public String getDocumentURI() {
        return null;
    }

    @Override
    public void setDocumentURI(String documentURI) {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node adoptNode(Node source) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public DOMConfiguration getDomConfig() {
        return null;
    }

    @Override
    public void normalizeDocument() {       
    }

    @Override
    public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }
    
    public static UIElementReference getElementReference(final Node node, final WebDrvUIElementsManager manager){
        if (node instanceof UIElementWrapper){
            return ((UIElementWrapper)node).getElementReference(manager);
        }else{
            return null;
        }
    }
}