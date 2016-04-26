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

class FoBlock extends FoObject {

    public FoBlock(FoBlockContainer owner) {
        super(owner);
    }

    @Override
    protected String getName() {
        return "block";
    }

    public FoInline addNewInline() {
        return new FoInline(this);
    }

    public void setWrapOptionNoWrap() throws XMLStreamException {
        writeAttribute("wrap-option", "no-wrap");
    }

    public void setNoWhiteSpaceCollapse() throws XMLStreamException {
        writeAttribute("white-space-collapse", "false");
    }

    public void setWhiteSpaceTreatment() throws XMLStreamException {
        writeAttribute("white-space-treatment", "ignore-if-before-linefeed"/*preserve"*/);
    }

    public void setText(final String value) throws XMLStreamException {
        writeText(value);
    }

    public FoExternalGraphic addNewExternalGraphic() throws XMLStreamException {
        return new FoExternalGraphic(this);
    }

    public void setTopMargin(double marginMm) throws XMLStreamException {
        String foValue = toFopMm(marginMm);
        writeAttribute("margin-top", foValue);
    }

    public void setBottomMargin(double marginMm) throws XMLStreamException {
        String foValue = toFopMm(marginMm);
        writeAttribute("margin-bottom", foValue);
    }

    public void setNoTextJustify() throws XMLStreamException {
        writeAttribute("text-align-last", "start");
    }

    public void setTextJustify() throws XMLStreamException {
        writeAttribute("text-align", "justify");
    }

}
