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

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.common.utils.FileUtils;

class FoFlow extends FoObject {

    public FoFlow(FoPageSequence page) {
        super(page);
    }

    public FoFlow() throws XMLStreamException {
        super(XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        }, FileUtils.XML_ENCODING));
        
    }

    @Override
    protected String getName() {
        return "flow";
    }

    public void setFlowName(final String value) throws XMLStreamException {
        writeAttribute("flow-name", value);
    }

    public FoBlockContainer addNewBlockContainer() {
        return new FoBlockContainer(this);
    }
}
