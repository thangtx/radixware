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

package org.radixware.kernel.common.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Replacment for {@link IncrementalXmlStreamWriter}. It has better support of
 * namespaces and more clear implementation. You can use
 * {@link IncrementalDataWriter#forceNSDecl(java.lang.String, java.lang.String)}
 * for an explicit declaration of namespaces at the root level of the document
 * to avoid many unnecessary namespace declarations in child elements.<br/>
 *
 * General way of using this class is to pass many different documents with the
 * same root elements so IncrementalDataWriter could merge them.<br/>
 *
 * For example, consider you have to input documents:<br/><br/>
 *
 * <pre>
 * &lt;Element1&gt;
 *   &lt;Element2&gt;
 *     &lt;Element3&gt;Element3_Content_1&lt;/Element3&gt;
 *   &lt;/Element2&gt;
 * &lt;/Element1&gt;
 *
 * and
 *
 * &lt;Element1&gt;
 *   &lt;Element2&gt;
 *     &lt;Element3&gt;Element3_Content_2&lt;/Element3&gt;
 *   &lt;/Element2&gt;
 * &lt;/Element1&gt;
 * </pre>
 *
 * If you will consrtuct IncrementalDataWriter via
 * {@code new IncrementalDataWriter(writer, "Element1", "Element2");}
 *
 * and than call
 *
 * <pre>
 * dataWriter.forseStartDocument();
 * dataWriter.parse(document1.getReader());
 * dataWriter.parse(document2.getReader());
 * dataWriter.forceEndDocument();
 * </pre>
 *
 * you will get the folowing result:
 *
 * <pre>
 * &lt;Element1&gt;
 *   &lt;Element2&gt;
 *     &lt;Element3&gt;Element3_Content_1&lt;/Element3&gt;
 *     &lt;Element3&gt;Element3_Content_2&lt;/Element3&gt;
 *   &lt;/Element2&gt;
 * &lt;/Element1&gt;
 * </pre>
 *
 * Note that you should call {@link IncrementalDataWriter#forceStartDocument()}
 * before writing any documents and
 * {@link IncrementalDataWriter#forceEndDocument()} when all writing operations
 * are completed.
 *
 */
public class IncrementalDataWriter extends DataWriter {

    private final List<QName> commonElements;
    private boolean process;
    private boolean inGoodBranch;
    private boolean isFirstPass;
    private boolean started = false;
    private int depth;
    private final QName uniqueElement;
    private Stack<OpenElement> openElements = new Stack<>();

    public IncrementalDataWriter(Writer writer) {
        this(writer, new QName[]{});
    }

    public IncrementalDataWriter(Writer writer, QName... branch) {
        super(writer);
        try {
            setParent(XMLReaderFactory.createXMLReader());
        } catch (SAXException ex) {
            throw new IllegalStateException("Can not create parent reader", ex);
        }
        commonElements = new ArrayList<>(Arrays.asList(branch));
        if (commonElements.size() > 0) {
            uniqueElement = commonElements.remove(commonElements.size() - 1);
        } else {
            uniqueElement = null;
        }
        init();
    }

    public IncrementalDataWriter(Writer writer, String branchAsXPath) {
        this(writer, XmlUtils.xPathToQNames(branchAsXPath));
    }

    public IncrementalDataWriter(Writer writer, List<QName> branch) {
        this(writer, branch.toArray(new QName[0]));
    }

    @SuppressWarnings("unchecked")
    private void init() {
        this.process = false;
        depth = -1;
        isFirstPass = true;
        if (commonElements.isEmpty()) {
            inGoodBranch = true;
        }
    }

    private void checkStartElement(String uri, String localName, final String qName, Attributes attrs) throws SAXException {
        depth++;
        if (depth < commonElements.size()) {
            if (commonElements.get(depth).getNamespaceURI().equals(uri)
                    && commonElements.get(depth).getLocalPart().equals(localName)) {
                inGoodBranch = true;
                process = isFirstPass;

                if (depth < openElements.size() && !isFirstPass && !attributesEquals(attrs, openElements.get(depth).attributes)) {
                    //check read attributes for equility with chached attributes
                    //if not equals, move to new branch
                    moveToNewBranch(uri, localName, qName, attrs);
                }
                if (depth == commonElements.size() - 1) {
                    isFirstPass = false;
                }
            } else {
                inGoodBranch = false;
            }
        } else if (depth == commonElements.size() && uniqueElement != null && !uniqueElement.equals(new QName(uri, localName))) {
            throw new SAXException("Attempt to write unexpected data: " + uri + ":" + localName);
        } else {
            process = inGoodBranch;
        }
    }

    private boolean attributesEquals(final Attributes a, final Attributes b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.getLength() != b.getLength()) {
            return false;
        }
        int equalAttributesCount = 0;
        final int count = a.getLength();
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (Utils.equals(a.getURI(i), b.getURI(j))
                        && Utils.equals(a.getLocalName(i), b.getLocalName(j))
                        && Utils.equals(a.getValue(i), b.getValue(j))) {
                    equalAttributesCount++;
                    break;
                }
            }
        }
        return equalAttributesCount == count;
    }

    private void checkEndElement() {
        if (depth < commonElements.size()) {
            process = false;
        }
        depth--;
    }

    void moveToNewBranch(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        for (int i = depth; i < commonElements.size(); i++) {
            final OpenElement element = openElements.pop();
            super.endElement(element.uri, element.localName, element.qName);
        }

        isFirstPass = true;
        process = true;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    @Override
    public void emptyElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        super.emptyElement(uri, localName, qName, atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        checkEndElement();
        if (process) {
            super.endElement(uri, localName, qName);
            openElements.pop();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        checkStartElement(uri, localName, qName, atts);
        if (process) {
            super.startElement(uri, localName, qName, atts);
            openElements.push(new OpenElement(uri, localName, qName, atts));
        }
    }

    public void startElement(final XmlObject xmlObject) throws SAXException {
        final Node domNode = xmlObject.getDomNode();
        commonElements.add(new QName(domNode.getNamespaceURI(), domNode.getLocalName()));
        //expanding commonElements array invalidates state of the isFirstPass
        isFirstPass = true;
        startElement(
                domNode.getNamespaceURI(),
                domNode.getLocalName(),
                getPrefixedName(domNode),
                getAttributes(domNode));
    }

    public void endElement(final XmlObject xmlObject) throws SAXException {
        if (commonElements.isEmpty()) {
            throw new SAXException("Unexpected endElement (none expected): " + getTopNodeQName(xmlObject));
        }
        final QName endQName = getTopNodeQName(xmlObject);
        if (!commonElements.get(commonElements.size() - 1).equals(getTopNodeQName(xmlObject))) {
            throw new SAXException("Unexpected endElement (" + commonElements.get(commonElements.size() - 1) + " expected): " + endQName);
        }
        commonElements.remove(commonElements.size() - 1);
        endElement(endQName.getNamespaceURI(), endQName.getLocalPart(), getPrefixedName(xmlObject.getDomNode()));
    }

    private QName getTopNodeQName(final XmlObject xmlObject) {
        if (xmlObject == null || xmlObject.getDomNode() == null) {
            return null;
        }
        return new QName(xmlObject.getDomNode().getNamespaceURI(), xmlObject.getDomNode().getLocalName());
    }

    public void write(final XmlObject xmlObject) throws SAXException {
        try {
            XmlOptions options = new XmlOptions();
            options.setSaveOuter();
            parse(new InputSource(xmlObject.newInputStream(options)));
        } catch (IOException ex) {
            throw new SAXException(ex);
        }
    }

    private Attributes getAttributes(final Node domNode) {
        AttributesImpl attributes = new AttributesImpl();
        final NamedNodeMap attrMap = domNode.getAttributes();
        if (attrMap != null) {
            for (int i = 0; i < attrMap.getLength(); i++) {
                final Node attrNode = attrMap.item(i);
                attributes.addAttribute(
                        attrNode.getNamespaceURI(),
                        attrNode.getLocalName(),
                        getPrefixedName(attrNode),
                        "CDATA",
                        attrNode.getNodeValue());
            }
        }
        return attributes;
    }

    private String getPrefixedName(final Node domNode) {
        return domNode.getPrefix() == null || domNode.getPrefix().isEmpty() ? domNode.getLocalName() : domNode.getPrefix() + ":" + domNode.getLocalName();
    }

    public void forceEndDocument() throws SAXException {
        if (!started) {
            throw new IllegalStateException("An attemt to end document that was not started");
        }
        while (!openElements.empty()) {
            OpenElement element = openElements.pop();
            super.endElement(element.uri, element.localName, element.qName);
        }
        super.endDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        //do actual logic in forceEndDocument()
    }

    @Override
    public void startDocument() throws SAXException {
        //do actual logic in forceStartDocument
    }

    public void forceStartDocument() throws SAXException {
        if (!started) {
            super.startDocument();
            started = true;
        }
    }

    private static class OpenElement {

        private final String uri;
        private final String localName;
        private final Attributes attributes;
        private final String qName;

        public OpenElement(String uri, String localName, final String qName, final Attributes attributes) {
            this.uri = uri;
            this.localName = localName;
            this.qName = qName;
            this.attributes = new AttributesImpl(attributes);
        }
    }
}
