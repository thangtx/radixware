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

package org.radixware.kernel.common.svn.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


/*We can't include starter.jar as dependency for this module, so we must copy XMLReader*/

class XMLReader {

    private final String fileName;
    private final XMLStreamReader reader;

    public XMLReader(String fileName, byte[] fileData) throws XMLStreamException {
        this.fileName = fileName;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(new ByteArrayInputStream(fileData));
    }

    public void checkTag(String name) throws IOException {
        if (!reader.getLocalName().equals(name)) {
            throw new IOException("File " + fileName + " format error - expected tag is " + name + " not " + reader.getLocalName());
        }
    }

    public String nextTag() throws XMLStreamException, IOException {
        for (;;) {
            int s = reader.next();
            if (s == XMLStreamConstants.START_ELEMENT) {
                return reader.getLocalName();
            }
            if (s == XMLStreamConstants.END_ELEMENT) {
                return null;
            }
            if (s == XMLStreamConstants.END_DOCUMENT) {
                throw new IOException("File " + fileName + " format error - unexpected EOF");
            }
        }
    }

    public boolean nextTag(String name) throws XMLStreamException, IOException {
        for (;;) {
            int s = reader.next();
            if (s == XMLStreamConstants.START_ELEMENT) {
                checkTag(name);
                return true;
            }
            if (s == XMLStreamConstants.END_ELEMENT) {
                return false;
            }
            if (s == XMLStreamConstants.END_DOCUMENT) {
                throw new IOException("File " + fileName + " format error - unexpected EOF");
            }
        }
    }

    public String getTag() throws IOException, XMLStreamException {
        String name = nextTag();
        if (name == null) {
            throw new IOException("File " + fileName + " format error - unexpected EOF");
        }
        return name;
    }

    public void getTag(String name) throws IOException, XMLStreamException {
        if (!nextTag(name)) {
            throw new IOException("File " + fileName + " format error - unexpected EOF");
        }
    }

    public void skipTag() throws XMLStreamException, IOException {
        for (;;) {
            int s = reader.next();
            if (s == XMLStreamConstants.END_ELEMENT) {
                return;
            }
            if (s == XMLStreamConstants.START_ELEMENT) {
                skipTag();
            }
            if (s == XMLStreamConstants.END_DOCUMENT) {
                throw new IOException("File " + fileName + " format error - unexpected EOF");
            }
        }
    }

    public Map<QName, String> getAttributValues() {
        final Map<QName, String> attributeValues = new HashMap<>();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            attributeValues.put(reader.getAttributeName(i), reader.getAttributeValue(i));
        }
        return attributeValues;
    }

    public String findAttribute(String attr) throws IOException {
        return reader.getAttributeValue(null, attr);
    }

    public String getAttribute(String attr) throws IOException {
        String val = reader.getAttributeValue(null, attr);
        if (val == null) {
            throw new IOException("File " + fileName + " format error - no attribute " + attr + " in the tag " + reader.getLocalName());
        }
        return val;
    }

    public boolean isEndElement() {
        return reader.isEndElement();
    }

    public boolean hasNext() throws XMLStreamException {
        return reader.hasNext();
    }
}
