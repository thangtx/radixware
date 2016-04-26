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


class ReportXmlCell extends ReportXmlObject {

    public ReportXmlCell(ReportXmlBand owner) {
        super(owner);
    }

    @Override
    protected String getName() {
        return "Cell";
    }

    public void setName(final String name) throws XMLStreamException {
        writeAttribute("Name", name);
    }

    public void setValue(final String value) throws XMLStreamException {
        final ReportXmlObject obj = new ReportXmlObject(this) {

            @Override
            protected String getName() {
                return "Value";
            }
        };
        obj.writeText(value);
    }
}
