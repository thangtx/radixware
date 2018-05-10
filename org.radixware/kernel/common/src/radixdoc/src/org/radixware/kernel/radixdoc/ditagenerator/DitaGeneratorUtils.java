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
package org.radixware.kernel.radixdoc.ditagenerator;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.radixware.kernel.radixdoc.enums.EDitaDocumentType;
import static org.radixware.kernel.radixdoc.enums.EDitaDocumentType.MAP;
import static org.radixware.kernel.radixdoc.enums.EDitaDocumentType.TOPIC;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DitaGeneratorUtils {

    // DEBUG METHODS
    public static String getXmlTree(Document doc) {
        StringBuilder result = new StringBuilder();
        appendNode(doc, result);
        return result.toString();
    }

    public static Element createLinesWithXref(final Document document, final String firstTextPart, final String secondTextPart, final String refText, final String href) {
        Element result = document.createElement("lines");
        Node firstTextNode = document.createTextNode(firstTextPart == null ? "" : firstTextPart);
        Element xref = createXRef(document, refText, href);
        Node secondTextNode = document.createTextNode(secondTextPart == null ? "" : secondTextPart);
        result.appendChild(firstTextNode);
        result.appendChild(xref);
        result.appendChild(secondTextNode);
        return result;
    }

    public static Element createTableEntry(final Document document, final String entryValue) {
        Element result = document.createElement("entry");
        result.setAttribute("valign", "middle");
        result.appendChild(createElement(document, "lines", entryValue));
        return result;
    }

    public static void appendNode(Node node, StringBuilder builder) {
        builder.append(node.getNodeName() + ":" + node.getNodeValue() + "\n");
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            appendNode(child, builder);
        }
    }

    public static Element createTableTitleEntry(final Document document, final String entryValue) {
        Element result = document.createElement("entry");
        result.setAttribute("valign", "middle");
        result.setAttribute("align", "center");
        Element lines = document.createElement("lines");
        result.appendChild(lines);
        lines.appendChild(createElement(document, "b", entryValue));
        return result;
    }

    public static Element createElement(final Document document, final String tagName, final String textValue) {
        Element result = document.createElement(tagName);
        result.appendChild(document.createTextNode(textValue == null ? "" : textValue));
        return result;
    }

    public static Element createXRef(final Document document, final String text, final String href) {
        Element xref = createElement(document, "xref", text);
        xref.setAttribute("href", href);
        return xref;
    }

    public static String documentToString(Document document, EDitaDocumentType ditaDocType) {
        try {
            final String[] TOPIC_DECLARATION_PARTS = {"topic", "-//OASIS//DTD DITA Topic//EN", "topic.dtd"};
            final String[] MAP_DECLARATION_PARTS = {"map", "-//OASIS//DTD DITA Map//EN", "map.dtd"};

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMImplementation domImplementation = document.getImplementation();

            DocumentType docTypeDeclaration;
            switch (ditaDocType) {
                case TOPIC:
                    docTypeDeclaration = domImplementation.createDocumentType(TOPIC_DECLARATION_PARTS[0], TOPIC_DECLARATION_PARTS[1], TOPIC_DECLARATION_PARTS[2]);
                    break;
                case MAP:
                    docTypeDeclaration = domImplementation.createDocumentType(MAP_DECLARATION_PARTS[0], MAP_DECLARATION_PARTS[1], MAP_DECLARATION_PARTS[2]);
                    break;
                default:
                    docTypeDeclaration = domImplementation.createDocumentType(TOPIC_DECLARATION_PARTS[0], TOPIC_DECLARATION_PARTS[1], TOPIC_DECLARATION_PARTS[2]);
            }

            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, docTypeDeclaration.getPublicId());
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docTypeDeclaration.getSystemId());

            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        } catch (DOMException | IllegalArgumentException | TransformerException ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

}
