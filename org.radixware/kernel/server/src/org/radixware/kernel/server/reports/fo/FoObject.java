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

package org.radixware.kernel.server.reports.fo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.radixware.kernel.common.utils.XmlUtils;

abstract class FoObject {

    protected final XMLStreamWriter stream;
    protected static final String XML_NS = "http://www.w3.org/1999/XSL/Format";

    protected FoObject(XMLStreamWriter stream) {
        this.stream = stream;
    }

    protected FoObject(FoObject owner) {
        this(owner.stream);
    }

    protected abstract String getName();

    public void begin() throws XMLStreamException {
        String name = this.getName();
        stream.writeStartElement("fo", name, XML_NS);
    }

    public void end() throws XMLStreamException {
        stream.writeEndElement();
    }

    protected void writeColor(final String name, final String rgbColor) throws XMLStreamException {
        // for example, convert "#00CCFF" to "rgb(0,204,255)"
        String foColor;
        if (rgbColor.length() == 7) {
            final int HEX = 16;
            int r = Integer.parseInt(rgbColor.substring(1, 3), HEX);
            int g = Integer.parseInt(rgbColor.substring(3, 5), HEX);
            int b = Integer.parseInt(rgbColor.substring(5, 7), HEX);
            foColor = "rgb(" + Integer.toString(r) + "," + Integer.toString(g) + "," + Integer.toString(b) + ")";
        } else {
            foColor = "rgb(255,255,255)";
        }

        writeAttribute(name, foColor);
    }

    public static String toFopMm(final double size) {
        return Double.toString(java.lang.Math.round(size * 100) / 100.0) + "mm";
    }

    protected void writeMm(final String name, final double size) throws XMLStreamException {
        String foValue = toFopMm(size);
        writeAttribute(name, foValue);
    }

    protected void writeAttribute(final String name, final String value) throws XMLStreamException {
        stream.writeAttribute(name, value);
    }

    private static List<String> stringToFopString(String s) {
        List<String> result = new ArrayList<>();
        
        if (s == null || s.isEmpty()) {
            result.add(s);
            return result;
        }
        
        // replace unsupported by XML properties by '?'
        s = XmlUtils.stringToXmlString(s);

        // replace unsupported by FOP characters by '?', otherwise - IndexOutOfBoundException :-(
        final char[] chars = s.toCharArray();
        final StringBuilder fopStringBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            
            if (org.apache.fop.text.linebreak.LineBreakUtils.getLineBreakProperty(c) == 0) {
                fopStringBuilder.append('?');
                continue;
            }
            
            if (i > 1) {
                if (chars[i - 1] == ']' && chars[i - 2] == ']' && c == '>') {
                    result.add(fopStringBuilder.toString());
                    fopStringBuilder.setLength(0);
                }
            }
            
            fopStringBuilder.append(c);
        }
        
        if (fopStringBuilder.length() != 0) {
            result.add(fopStringBuilder.toString());
        }
        
        return result;
    }

    protected void writeText(final String value) throws XMLStreamException {
        for (String fopString : stringToFopString(value)) {
            stream.writeCData(fopString);
        }
    }
}
