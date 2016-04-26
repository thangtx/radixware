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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


/**
 * Use {@linkplain IncrementalDataWriter} instead
 */
@Deprecated
public class IncrementalXmlStreamWriter implements XMLStreamWriter {

    private XMLStreamWriter parent;
    private QName[] branch;
    private boolean process;
    private boolean inGoodBranch;
    private boolean isFirstPass;
    private int depth;
    private Map<QName, String>[] attrCache;
    private Map<QName, String> attrsRead;

    @SuppressWarnings("unchecked")
    private void init(XMLStreamWriter parent) {
        this.parent = parent;
        this.process = false;
        depth = -1;
        attrsRead = new HashMap<QName, String>();
        attrCache = new Map[branch.length - 1];
        for (int i = 0; i < attrCache.length; ++i) {
            attrCache[i] = new HashMap<QName, String>();
        }
    }

    private void checkStartElement(String uri, String localName) {
        depth++;
        attrsRead.clear();
        if (depth < branch.length - 1) {
            if (branch[depth].getNamespaceURI().equals(uri)
                    && branch[depth].getLocalPart().equals(localName)) {
                inGoodBranch = true;
                process = isFirstPass;

                if (isFirstPass && depth < attrCache.length) {
                    attrCache[depth].clear();
                }
            } else {
                inGoodBranch = false;
            }
        } else {
            process = inGoodBranch;
        }
    }

    private void checkAttribute(QName name, String value) throws XMLStreamException {
        if (isFirstPass) {
            if (depth < attrCache.length) {
                attrCache[depth].put(name, value);
            }
        } else if (depth < attrCache.length
                && (!attrCache[depth].containsKey(name) || !attrCache[depth].get(name).equals(value))) {
            moveToNewBranch();
            attrCache[depth].put(name, value);
        }
        attrsRead.put(name, value);
    }

    private void checkStartElementFinished() throws XMLStreamException {
        if (!isFirstPass && inGoodBranch && depth < attrCache.length
                && !attrsRead.equals(attrCache[depth])) {
            moveToNewBranch();
        }
    }

    private void checkEndElement() {
        if (depth >= branch.length - 1 && process) {
            isFirstPass = false;
        }

        if (depth < branch.length - 1) {
            process = false;
        }
        depth--;
    }

    void moveToNewBranch() throws XMLStreamException {
        for (int i = depth; i < branch.length - 1; ++i) {
            parent.writeEndElement();
        }
        parent.writeStartElement(branch[depth].getNamespaceURI(), branch[depth].getLocalPart());
        attrCache[depth].clear();
        for (Map.Entry<QName, String> i : attrsRead.entrySet()) {
            attrCache[depth].put(i.getKey(), i.getValue());
            if (!i.getKey().getPrefix().isEmpty()) {
                assert !i.getKey().getNamespaceURI().isEmpty();
                parent.writeAttribute(i.getKey().getPrefix(),
                        i.getKey().getNamespaceURI(),
                        i.getKey().getLocalPart(),
                        i.getValue());
            } else if (!i.getKey().getNamespaceURI().isEmpty()) {
                parent.writeAttribute(i.getKey().getNamespaceURI(),
                        i.getKey().getLocalPart(),
                        i.getValue());
            } else {
                parent.writeAttribute(i.getKey().getLocalPart(),
                        i.getValue());
            }
        }
        isFirstPass = true;
        process = true;
    }

    @Override
    public void writeEndDocument() throws XMLStreamException {
        for (int i = 0; i < branch.length - 1; ++i) {
            parent.writeEndElement();
        }
        parent.writeEndDocument();
    }

    /**
     * Construct branch filtered stream using original XML stream and list of
     * {@link QName}'s as branch path
     *
     * @param parent original stream
     * @param branch A branch path as a list of {@link QName}'s
     */
    public IncrementalXmlStreamWriter(XMLStreamWriter parent, List<QName> branch) {
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
    public IncrementalXmlStreamWriter(XMLStreamWriter parent, QName... branch) {
        super();
        this.branch = branch;
        init(parent);
    }

    @Override
    public void close() throws XMLStreamException {
        parent.close();
    }

    @Override
    public void flush() throws XMLStreamException {
        parent.flush();
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        return parent.getNamespaceContext();
    }

    @Override
    public String getPrefix(String uri) throws XMLStreamException {
        return parent.getPrefix(uri);
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return parent.getProperty(name);
    }

    @Override
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        parent.setDefaultNamespace(uri);
    }

    @Override
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        parent.setNamespaceContext(context);
    }

    @Override
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        parent.setPrefix(prefix, uri);
    }

    @Override
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
            throws XMLStreamException {
        checkAttribute(new QName(namespaceURI, localName), value);
        if (process) {
            parent.writeAttribute(prefix, namespaceURI, localName, value);
        }
    }

    @Override
    public void writeAttribute(String namespaceURI, String localName, String value)
            throws XMLStreamException {
        checkAttribute(new QName(namespaceURI, localName), value);
        if (process) {
            parent.writeAttribute(namespaceURI, localName, value);
        }
    }

    @Override
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        checkAttribute(new QName(localName), value);
        if (process) {
            parent.writeAttribute(localName, value);
        }
    }

    @Override
    public void writeCData(String data) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeCData(data);
        }
    }

    @Override
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeCharacters(text, start, len);
        }
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeCharacters(text);
        }
    }

    @Override
    public void writeComment(String data) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeComment(data);
        }
    }

    @Override
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        checkAttribute(new QName("xmlns"), namespaceURI);
        if (process) {
            parent.writeDefaultNamespace(namespaceURI);
        }
    }

    @Override
    public void writeDTD(String dtd) throws XMLStreamException {
        if (process) {
            parent.writeDTD(dtd);
        }
    }

    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI)
            throws XMLStreamException {
        checkStartElement(namespaceURI, localName);
        if (process) {
            parent.writeEmptyElement(prefix, localName, namespaceURI);
        }
        checkEndElement();
    }

    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        checkStartElement(namespaceURI, localName);
        if (process) {
            parent.writeEmptyElement(namespaceURI, localName);
        }
        checkEndElement();
    }

    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException {
        checkStartElement("", localName);
        if (process) {
            parent.writeEmptyElement(localName);
        }
        checkEndElement();
    }

    @Override
    public void writeEndElement() throws XMLStreamException {
        checkEndElement();
        if (process) {
            parent.writeEndElement();
        }
    }

    @Override
    public void writeEntityRef(String name) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeEntityRef(name);
        }
    }

    @Override
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeNamespace(prefix, namespaceURI);
        }
    }

    @Override
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeProcessingInstruction(target, data);
        }
    }

    @Override
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        checkStartElementFinished();
        if (process) {
            parent.writeProcessingInstruction(target);
        }
    }

    @Override
    public void writeStartDocument() throws XMLStreamException {
        isFirstPass = true;
        parent.writeStartDocument();
    }

    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        isFirstPass = true;
        parent.writeStartDocument(encoding, version);
    }

    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
        isFirstPass = true;
        parent.writeStartDocument(version);
    }

    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI)
            throws XMLStreamException {
        checkStartElement(namespaceURI, localName);
        if (process) {
            parent.writeStartElement(prefix, localName, namespaceURI);
        }
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        checkStartElement(namespaceURI, localName);
        if (process) {
            parent.writeStartElement(namespaceURI, localName);
        }
    }

    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        checkStartElement("", localName);
        if (process) {
            parent.writeStartElement(localName);
        }
    }
}
