package org.radixware.kernel.common.utils;

/**
 * *
 *
 * Copyright 2004 Protique Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 */
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * *
 * Utility methods for working with an XMLStreamWriter. Maybe push this back
 * into stax-utils project.
 *
 * @version $Revision: 1.14 $
 */
public class XmlStreamHelper implements XMLStreamConstants {

    private static Attributes emptyAttributes = new AttributesImpl();

    public static String getQualifiedName(QName qname) {
        String prefix = qname.getPrefix();
        String localPart = qname.getLocalPart();
        if (prefix != null && prefix.length() > 0) {
            return prefix + ":" + localPart;
        }
        return localPart;
    }

    /**
     * *********************************************************************
     * Returns true if currently at the start of an element, otherwise move
     * forwards to the next element start and return true, otherwise false is
     * returned if the end of the stream is reached.
     */
    public static boolean skipToStartOfElement(XMLStreamReader in) throws XMLStreamException {
        for (int code = in.getEventType(); code != END_DOCUMENT; code = in.next()) {
            if (code == START_ELEMENT) {
                return true;
            }
        }
        return false;
    }

    public static void writeStartElement(QName qname, ContentHandler handler) throws SAXException {
        handler.startElement(qname.getNamespaceURI(), qname.getLocalPart(), getQualifiedName(qname), emptyAttributes);
    }

    public static void writeEndElement(QName qname, ContentHandler handler) throws SAXException {
        handler.endElement(qname.getNamespaceURI(), qname.getLocalPart(), getQualifiedName(qname));
    }

    /**
     * *
     * Copies the current element and its conetnt to the output
     */
    public static void copy(XMLStreamReader in, XMLStreamWriter out, boolean repairing) throws XMLStreamException {
        int elementCount = 0;
        for (int code = in.getEventType(); in.hasNext(); code = in.next()) {
            switch (code) {
                case START_ELEMENT:
                    elementCount++;
                    writeStartElementAndAttributes(out, in, repairing);
                    break;

                case END_ELEMENT:
                    if (--elementCount < 0) {
                        return;
                    }
                    out.writeEndElement();
                    break;

                case CDATA:
                    out.writeCData(in.getText());
                    break;

                case CHARACTERS:
                    out.writeCharacters(in.getText());
                    break;
                default:
                    continue;
            }
        }
        while (elementCount-- > 0) {
            out.writeEndElement();
        }
    }

    public static void writeStartElement(XMLStreamWriter out, String prefix, String uri, String localName, boolean repairing) throws XMLStreamException {
        boolean map = isPrefixNotMappedToUri(out, prefix, uri);
        if (prefix != null && prefix.length() > 0) {
            if (map) {
                out.setPrefix(prefix, uri);
            }
            out.writeStartElement(prefix, localName, uri);
            if (map && !repairing) {
                out.writeNamespace(prefix, uri);
            }
        } else {
            boolean hasURI = uri != null && uri.length() > 0;
            if (map && hasURI) {
                out.setDefaultNamespace(uri);
            }
            out.writeStartElement(uri, localName);
            if (map && !repairing && hasURI) {
                out.writeDefaultNamespace(uri);
            }
        }
    }

    public static void writeStartElement(XMLStreamWriter out, QName envelopeName, boolean repairing) throws XMLStreamException {
        writeStartElement(out, envelopeName.getPrefix(), envelopeName.getNamespaceURI(), envelopeName.getLocalPart(), repairing);
    }

    public static void writeStartElement(XMLStreamWriter out, XMLStreamReader in, boolean repairing) throws XMLStreamException {
        String prefix = in.getPrefix();

        // we can avoid this step if in repairing mode
        int count = in.getNamespaceCount();
        for (int i = 0; i < count; i++) {
            String aPrefix = in.getNamespacePrefix(i);
            if (prefix == aPrefix || (prefix != null && prefix.equals(aPrefix))) {
                continue;
            }
            String uri = in.getNamespaceURI(i);
            if (isPrefixNotMappedToUri(out, aPrefix, uri)) {
                if (aPrefix != null && aPrefix.length() > 0) {
                    out.setPrefix(aPrefix, uri);
                } else {
                    out.setDefaultNamespace(uri);
                }
            }
        }
        String localName = in.getLocalName();
        String uri = in.getNamespaceURI();
        writeStartElement(out, prefix, uri, localName, repairing);
    }

    public static void writeStartElementAndAttributes(XMLStreamWriter out, XMLStreamReader in, boolean repairing) throws XMLStreamException {
        writeStartElement(out, in, repairing);
        if (!repairing) {
            writeNamespaces(out, in, in.getPrefix());
        }
        writeAttributes(out, in);
    }

    public static void writeAttributes(XMLStreamWriter out, XMLStreamReader in) throws XMLStreamException {
        int count = in.getAttributeCount();
        for (int i = 0; i < count; i++) {
            out.writeAttribute(in.getAttributePrefix(i),
                    in.getAttributeNamespace(i),
                    in.getAttributeLocalName(i),
                    in.getAttributeValue(i));
        }
    }

    public static void writeNamespaces(XMLStreamWriter out, XMLStreamReader in, String prefixOfCurrentElement) throws XMLStreamException {
        int count = in.getNamespaceCount();
        for (int i = 0; i < count; i++) {
            String prefix = in.getNamespacePrefix(i);
            String uri = in.getNamespaceURI(i);
            if (prefixOfCurrentElement != null && prefixOfCurrentElement.equals(prefix)) {
                continue;
            }
            if (isPrefixNotMappedToUri(out, prefix, uri)) {
                out.writeNamespace(prefix, uri);
            }
        }
    }

    public static void writeNamespacesExcludingPrefixAndNamespace(XMLStreamWriter out, XMLStreamReader in, String ignorePrefix, String ignoreNamespace) throws XMLStreamException {
        int count = in.getNamespaceCount();
        for (int i = 0; i < count; i++) {
            String prefix = in.getNamespacePrefix(i);
            if (!ignorePrefix.equals(prefix)) {
                String uri = in.getNamespaceURI(i);
                if (!ignoreNamespace.equals(uri)) {
                    out.writeNamespace(prefix, uri);
                }
            }
        }
    }

    public static void writeAttribute(XMLStreamWriter out, QName name, String attributeValue) throws XMLStreamException {
        writeAttribute(out, name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attributeValue);
    }

    public static void writeAttribute(XMLStreamWriter out, String prefix, String namespaceURI, String localPart, String attributeValue) throws XMLStreamException {
        out.writeAttribute(prefix, namespaceURI, localPart, attributeValue);
    }

    protected static boolean isPrefixNotMappedToUri(XMLStreamWriter out, String prefix, String uri) {
        if (prefix == null) {
            prefix = "";
        }
        NamespaceContext context = out.getNamespaceContext();
        if (context == null) {
            return false;
        }
        String mappedPrefix = context.getPrefix(uri);
        boolean map = (mappedPrefix == null || !mappedPrefix.equals(prefix));
        return map;
    }
}
