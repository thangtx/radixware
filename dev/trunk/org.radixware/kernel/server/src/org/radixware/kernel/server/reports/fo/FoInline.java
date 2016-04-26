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


class FoInline extends FoObject{
    
    public FoInline(FoBlock owner) {
        super(owner);
    }

    @Override
    protected String getName() {
        return "inline";
    }
    
    public void setColor(String rgbColor) throws XMLStreamException {
        writeColor("color", rgbColor);
    }

    public void setBackgroundColor(String rgbColor) throws XMLStreamException {
        writeColor("background-color", rgbColor);
    }

    public void setFontFamily(String value) throws XMLStreamException {
        writeAttribute("font-family", value);
    }

    public void setFontSize(double valueMm) throws XMLStreamException {
        writeMm("font-size", valueMm);
    }

    public void setFontStyleItalic() throws XMLStreamException {
        writeAttribute("font-style", "italic");
    }

    public void setFontWeightBold() throws XMLStreamException {
        writeAttribute("font-weight", "bold");
    }

    public void setUnderlineText() throws XMLStreamException {
        writeAttribute("text-decoration", "underline");
    }
    
    public void setLineThroughText() throws XMLStreamException {
        writeAttribute("text-decoration", "line-through");
    }
    
    public void setText(final String value) throws XMLStreamException {
        writeText(value);
    }
    
    public void setVerticalAlign(String value) throws XMLStreamException {
        /*String value = "";
        switch (textAlignType) {
            case TOP:
                value = "top";
                break;
            case MIDDLE:
                value = "middle";
                break;
            case BOTTOM:
                value = "bottom";
                break;                
        }*/
        writeAttribute("vertical-align", value);
    }
}
