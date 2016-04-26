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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

class FoRoot extends FoObject {

    public FoRoot(XMLStreamWriter stream) {
        super(stream);
    }

    @Override
    protected String getName() {
        return "root";
    }

    public FoPageSequence addNewPageSequence() {
        return new FoPageSequence(this);
    }

    public void setColor(String rgbColor) throws XMLStreamException {
        this.writeColor("color", rgbColor);
    }

    public void setMasterReference(final String value) throws XMLStreamException {
        writeAttribute("master-reference", value);
    }

    public FoLayoutMasterSet addNewLayoutMasterSet() {
        return new FoLayoutMasterSet(this);
    }

    @Override
    public void begin() throws XMLStreamException {
        stream.writeStartDocument("utf-8", "1.0");
        super.begin();
        stream.writeNamespace("fo", FoObject.XML_NS);
        //xmlns:fo="http://www.w3.org/1999/XSL/Format"

    }

    @Override
    public void end() throws XMLStreamException {
        super.end();
        stream.writeEndDocument();
    }
}
