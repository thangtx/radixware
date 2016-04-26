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


class ReportXmlBand extends ReportXmlObject {

    public ReportXmlBand(ReportXmlRoot owner) {
        super(owner);
    }

    protected ReportXmlBand(XMLStreamWriter stream) {
        super(stream);
    }

    public ReportXmlCell addNewCell() {
        return new ReportXmlCell(this);
    }

    @Override
    protected String getName() {
        return "Band";
    }

    public void setType(final String type) throws XMLStreamException {
        writeAttribute("Type", type);
    }

    public void setGroupLevel(final int groupLevel) throws XMLStreamException {
        writeAttribute("GroupLevel", String.valueOf(groupLevel));
    }
}
