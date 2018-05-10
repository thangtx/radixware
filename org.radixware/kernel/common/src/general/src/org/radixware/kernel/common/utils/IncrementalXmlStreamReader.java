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

import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Incremental parser suitable for XMLBeans object parsing.
 *
 * This class is constructed with list of QNames from which the common part of
 * target documents consits.
 *
 * Consider we have the folowing xml:
 *
 * <code>
 * <Root>
 * <Child>
 * <GrandChild1/>
 * </Child>
 *
 * <Child>
 * <GrandChild2/>
 * </Child>
 * </Root>
 * </code>
 *
 * we can construct {@link IncrementalXmlStreamReader} via
 * {@code IncrementalXmlStreamReader(originStream, new QName("", "Root"), new QName("", "Child"))},
 * and get two documents: first with GrandChild1 and second with GrandChild2.
 */
public class IncrementalXmlStreamReader implements XMLStreamReader {

    private State state;
    private int currentDepth = -1;
    private int lastMatch = -1;
    private int currentType = -1;
    private int virtualDepth = -1;
    boolean process = false;
    private XMLStreamReader parent;
    private QName[] branch;
    private long parentEventNumber = -1;
    private long acceptedEventNumber = -1;

    private boolean accept(XMLStreamReader r) {
        if (acceptedEventNumber != parentEventNumber || acceptedEventNumber == -1) {
            acceptedEventNumber = parentEventNumber;
            switch (r.getEventType()) {
                case XMLStreamReader.START_ELEMENT:
                    ++currentDepth;

                    if (currentDepth < branch.length && currentDepth - lastMatch == 1) {
                        QName currentName = r.getName();
                        if (currentName.equals(branch[currentDepth])) {
                            ++lastMatch;
                            NodeCache c = new NodeCache();
                            nodeCache[currentDepth] = c;
                            c.attributes = new NodeCache.Attribute[parent.getAttributeCount()];
                            for (int i = 0; i < parent.getAttributeCount(); ++i) {
                                c.attributes[i] = c.new Attribute(parent.getAttributeName(i), parent.getAttributeType(i), parent.getAttributeValue(i));
                            }

                            c.namespaces = new NodeCache.Namespace[parent.getNamespaceCount()];
                            for (int i = 0; i < c.namespaces.length; i++) {
                                c.namespaces[i] = c.new Namespace(parent.getNamespacePrefix(i), parent.getNamespaceURI(i));
                            }
                        }
                    }
                    process = lastMatch == branch.length - 1;
                    break;
                case XMLStreamReader.END_ELEMENT:
                    process = lastMatch == branch.length - 1;
                    if (currentDepth == lastMatch) {
                        QName currentName = r.getName();
                        if (currentName.equals(branch[currentDepth])) {
                            --lastMatch;
                        }
                    }
                    --currentDepth;
                    break;
            }
        }

        return process;
    }

    private void init(XMLStreamReader parent) {
        this.parent = parent;
        this.state = State.AtTop;
        this.nodeCache = new NodeCache[this.branch.length];
    }

    /**
     * Construct branch filtered stream using original XML stream and list of
     * {@link QName}'s as branch path
     *
     * @param parent original stream
     * @param branch A branch path as a list of {@link QName}'s
     */
    public IncrementalXmlStreamReader(XMLStreamReader parent,
            List<QName> branch) {
        super();
        this.branch = branch.toArray(new QName[0]);
        init(parent);
    }

    /**
     * Construct branch filtered stream using original XML stream and any number
     * of {@link QName}'s as branch path
     *
     * @param parent original stream
     * @param branch A branch path as any number of {@link QName} arguments.
     * It's possible to pass {@link QName}[] instead of several arguments
     */
    public IncrementalXmlStreamReader(XMLStreamReader parent,
            QName... branch) {
        super();
        this.branch = branch;
        init(parent);
    }

    public IncrementalXmlStreamReader(XMLStreamReader parent, String branchAsXPath) {
        this(parent, XmlUtils.xPathToQNames(branchAsXPath));
    }
    
    private int parentNext() throws XMLStreamException {
        parentEventNumber++;
        return parent.next();
    }

    private int filteredNext() throws XMLStreamException {
        int ret = parentNext();
        while (ret != XMLStreamReader.END_DOCUMENT && !accept(parent)) {
            ret = parentNext();
        }
        return ret;
    }

    /**
     *
     * @return @throws XMLStreamException
     */
    public boolean hasNextItem() throws XMLStreamException {
        //if no events left, return false
        if (!hasNext()) {
            return false;
        }
        //skip all whitespaces, comments and processing instructions from current position
        while (isWhiteSpace() || parent.getEventType() == XMLStreamConstants.COMMENT || parent.getEventType() == XMLStreamConstants.SPACE || parent.getEventType() == XMLStreamConstants.PROCESSING_INSTRUCTION) {
            parentNext();
        }
        //update internal state of the IncrementalXmlStreamReader
        hasNext();
        //if there is no any input in parent reader, return false
        if (parent.getEventType() == XMLStreamConstants.END_DOCUMENT) {
            return false;
        }
        //return true
        return true;
    }

    @Override
    public boolean hasNext() throws XMLStreamException {
        if (parent.hasNext()) {
            while (parent.hasNext()) {
                if (accept(parent)) {
                    return true;
                }
                parentNext();
            }
        }
        return false;
    }

    @Override
    public int next() throws XMLStreamException {

        switch (state) {
            case AtTop:
                hasNext();
                setState(State.Descent);
                virtualDepth = 0;
//                logger.fine("state = AtTop, virtualDepth = " + virtualDepth + ", returning getEventType()");
                return XMLStreamReader.START_ELEMENT;

            case Descent:
                virtualDepth++;
                if (virtualDepth == branch.length - 1) {
                    setState(State.AtBottom);
//                    logger.fine("state = Descent -> AtBottom, virtualDepth = " + virtualDepth + ", returning getEventType(): " + parent.getEventType());
                    return parent.getEventType(); // we stopped here at AtTop state
                } else {
//                    logger.fine("state = Descent, virtualDepth = " + virtualDepth + ", returning START_ELEMENT");
                    return XMLStreamReader.START_ELEMENT;
                }

            case AtBottom:
                int ret = filteredNext();
                if (currentDepth < branch.length - 1) {
                    setState(State.Pop);
//                    logger.fine("state = AtBottom -> Pop, currentDepth = " + currentDepth + ", returning filteredNext(): " + ret);
                }
//                else {
////                    logger.fine("state = AtBottom, currentDepth = " + currentDepth + ", returning filteredNext(): " + ret);
//                }
                return ret;

            case Pop:
//                logger.fine("state = Pop, virtualDepth = " + virtualDepth);
                if (virtualDepth == 0) {
                    setState(State.End);
                    return next();
                }
                virtualDepth--;
                return XMLStreamReader.END_ELEMENT;

            case End:
                setState(State.AtTop);
                filteredNext();
//                logger.fine("next(): state = End -> AtTop, returning END_DOCUMENT");
                return XMLStreamReader.END_DOCUMENT;

            default:
                throw new IllegalStateException("Invalid state for Incremental Reader: " + state);
        }
    }

    private void setState(State state) {
        if (this.state != state) {
//            logger.finest("Setting state '" + this.state + "' -> '" + state + "'");
            this.state = state;
        }
    }

    @Override
    public int getEventType() {
        switch (state) {
            case AtTop:
//                logger.fine("getEventType(): AtTop -> START_DOCUMENT");
                return XMLStreamReader.START_DOCUMENT;
            case Descent:
//                logger.fine("getEventType(): Descent -> START_ELEMENT");
                return XMLStreamReader.START_ELEMENT;
            case Pop:
//                logger.fine("getEventType(): Pop -> END_ELEMENT");
                return XMLStreamReader.END_ELEMENT;
            case End:
//                logger.fine("getEventType(): End -> END_DOCUMENT");
                return XMLStreamReader.END_DOCUMENT;
            default:
//                logger.fine("getEventType() -> from parent: " + parent.getEventType());
                return parent.getEventType();
        }
    }

    @Override
    public void close() throws XMLStreamException {
        parent.close();
    }

    @Override
    public int getAttributeCount() {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].attributes.length;
        }
        return parent.getAttributeCount();
    }

    @Override
    public String getAttributeLocalName(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].attributes[i].name.getLocalPart();
        }
        return parent.getAttributeLocalName(i);
    }

    @Override
    public QName getAttributeName(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].attributes[i].name;
        }
        return parent.getAttributeName(i);
    }

    @Override
    public String getAttributeNamespace(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].attributes[i].name.getNamespaceURI();
        }
        return parent.getAttributeNamespace(i);
    }

    @Override
    public String getAttributePrefix(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].attributes[i].name.getPrefix();
        }
        return parent.getAttributePrefix(i);
    }

    @Override
    public String getAttributeType(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].attributes[i].type;
        }
        return parent.getAttributeType(i);
    }

    @Override
    public String getAttributeValue(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].attributes[i].value;
        }
        return parent.getAttributeValue(i);
    }

    @Override
    public String getAttributeValue(String namespaceURI, String name) {
        if (state == State.Descent || state == State.Pop) {
            for (NodeCache.Attribute c : nodeCache[virtualDepth].attributes) {
                if (c.name.equals(new QName(namespaceURI, name))) {
                    return c.value;
                }
            }
            return null;
        }
        return parent.getAttributeValue(namespaceURI, name);
    }

    @Override
    public String getCharacterEncodingScheme() {
        return parent.getCharacterEncodingScheme();
    }

    @Override
    public String getElementText() throws XMLStreamException {
        return parent.getElementText();
    }

    @Override
    public String getEncoding() {
        return parent.getEncoding();
    }

    @Override
    public String getLocalName() {
        if (state == State.Descent || state == State.Pop) {
            return branch[virtualDepth].getLocalPart();
        }
        return parent.getLocalName();
    }

    @Override
    public Location getLocation() {
        return parent.getLocation();
    }

    @Override
    public QName getName() {
        if (state == State.Descent || state == State.Pop) {
            return branch[virtualDepth];
        }
        return parent.getName();
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        return parent.getNamespaceContext();
    }

    @Override
    public int getNamespaceCount() {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].namespaces.length;
        }
        return parent.getNamespaceCount();
    }

    @Override
    public String getNamespacePrefix(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].namespaces[i].prefix;
        }
        return parent.getNamespacePrefix(i);
    }

    @Override
    public String getNamespaceURI() {
        if (state == State.Descent || state == State.Pop) {
            return branch[virtualDepth].getNamespaceURI();
        }
        return parent.getNamespaceURI();
    }

    @Override
    public String getNamespaceURI(int i) {
        if (state == State.Descent || state == State.Pop) {
            return nodeCache[virtualDepth].namespaces[i].uri;
        }
        return parent.getNamespaceURI(i);
    }

    @Override
    public String getNamespaceURI(String prefix) {
        if (state == State.Descent || state == State.Pop) {
            if (prefix == null) {
                throw new IllegalArgumentException("Null prefix passed to getNamespaceURI");
            }

            for (NodeCache.Namespace ns : nodeCache[virtualDepth].namespaces) {
                if (ns.prefix.equals(prefix)) {
                    return ns.uri;
                }
            }
            return null;
        }
        return parent.getNamespaceURI(prefix);
    }

    @Override
    public String getPIData() {
        return parent.getPIData();
    }

    @Override
    public String getPITarget() {
        return parent.getPITarget();
    }

    @Override
    public String getPrefix() {
        return parent.getPrefix();
    }

    @Override
    public Object getProperty(String arg0) throws IllegalArgumentException {
        return parent.getProperty(arg0);
    }

    @Override
    public String getText() {
        return parent.getText();
    }

    @Override
    public char[] getTextCharacters() {
        return parent.getTextCharacters();
    }

    @Override
    public int getTextCharacters(int arg0, char[] arg1, int arg2, int arg3)
            throws XMLStreamException {
        return parent.getTextCharacters(arg0, arg1, arg2, arg3);
    }

    @Override
    public int getTextLength() {
        return parent.getTextLength();
    }

    @Override
    public int getTextStart() {
        return parent.getTextStart();
    }

    @Override
    public String getVersion() {
        return parent.getVersion();
    }

    @Override
    public boolean hasName() {
        return parent.hasName();
    }

    @Override
    public boolean hasText() {
        return parent.hasText();
    }

    @Override
    public boolean isAttributeSpecified(int arg0) {
        return parent.isAttributeSpecified(arg0);
    }

    @Override
    public boolean isCharacters() {
        return parent.isCharacters();
    }

    @Override
    public boolean isEndElement() {
        return parent.isEndElement();
    }

    @Override
    public boolean isStandalone() {
        return parent.isStandalone();
    }

    @Override
    public boolean isStartElement() {
        return parent.isStartElement();
    }

    @Override
    public boolean isWhiteSpace() {
        return parent.isWhiteSpace();
    }

    @Override
    public int nextTag() throws XMLStreamException {
        return parent.nextTag();
    }

    @Override
    public void require(int arg0, String arg1, String arg2) throws XMLStreamException {
        parent.require(arg0, arg1, arg2);
    }

    @Override
    public boolean standaloneSet() {
        return parent.standaloneSet();
    }

    private class NodeCache {

        private class Attribute {

            QName name;
            String type;
            String value;

            public Attribute(QName name, String type, String value) {
                super();
                this.name = name;
                this.type = type;
                this.value = value;
            }
        }
        private Attribute[] attributes;

        private class Namespace {

            String prefix;
            String uri;

            public Namespace(String prefix, String uri) {
                super();
                this.prefix = prefix;
                this.uri = uri;
            }
        }
        private Namespace[] namespaces;
    }
    private NodeCache[] nodeCache;

    /**
     * States our parser can be in
     *
     */
    private enum State {

        /**
         * Generate fake START_DOCUMENT events
         */
        AtTop,
        /**
         * Generate fake START_ELEMENT events
         */
        Descent,
        /**
         * Return real nodes below the branch
         */
        AtBottom,
        /**
         * Generate fake END_ELEMENT events
         */
        Pop,
        /**
         * Generate fake END_DOCUMENT event
         */
        End
    }
}
