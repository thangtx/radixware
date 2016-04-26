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

class ReportXmlRoot extends ReportXmlObject {

    private static final String XML_NS = "http://schemas.radixware.org/reports.xsd";

    public ReportXmlRoot(XMLStreamWriter stream) {
        super(stream);
    }

    @Override
    protected String getName() {
        return "Report";
    }

    public ReportXmlBand addNewBand() {
        return new ReportXmlBand(this);
    }

    @Override
    public void begin() throws XMLStreamException {
        stream.writeStartDocument("utf-8", "1.0");
        super.begin();
        stream.writeDefaultNamespace(XML_NS);
    }

    @Override
    public void end() throws XMLStreamException {
        super.end();
        stream.writeEndDocument();
    }
}
