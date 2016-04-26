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

package org.radixware.kernel.server.reports.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.radixware.kernel.common.utils.XmlUtils;


abstract class ReportXmlObject {

    protected final XMLStreamWriter stream;

    protected ReportXmlObject(XMLStreamWriter stream) {
        this.stream = stream;
    }

    protected ReportXmlObject(ReportXmlObject owner) {
        this(owner.stream);
    }

    protected abstract String getName();

    public void begin() throws XMLStreamException {
        final String name = this.getName();
        stream.writeStartElement(name);
    }

    public void end() throws XMLStreamException {
        stream.writeEndElement();
    }

    protected void writeAttribute(final String name, final String value) throws XMLStreamException {
        stream.writeAttribute(name, XmlUtils.stringToXmlString(value));
    }

    protected void writeText(final String value) throws XMLStreamException {
        stream.writeCharacters(XmlUtils.stringToXmlString(value));
    }
}