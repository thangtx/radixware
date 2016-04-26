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

class FoRegionBody extends FoObject {

    public FoRegionBody(FoSimplePageMaster owner) {
        super(owner);
    }

    @Override
    protected String getName() {
        return "region-body";
    }

    public void setMarginLeft(double marginLeftMm) throws XMLStreamException {
        writeMm("margin-left", marginLeftMm);
    }

    public void setMarginRight(double marginRightMm) throws XMLStreamException {
        writeMm("margin-right", marginRightMm);
    }

    public void setMarginTop(double marginTopMm) throws XMLStreamException {
        writeMm("margin-top", marginTopMm);
    }

    public void setMarginBottom(double marginBottomMm) throws XMLStreamException {
        writeMm("margin-bottom", marginBottomMm);
    }

    public void setBackgroundColor(String rgbColor) throws XMLStreamException {
        writeColor("background-color", rgbColor);
    }
}
