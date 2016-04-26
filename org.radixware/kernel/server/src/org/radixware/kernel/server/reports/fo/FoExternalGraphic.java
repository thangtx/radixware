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
import org.radixware.kernel.common.enums.EReportCellHAlign;
import org.radixware.kernel.common.enums.EReportCellVAlign;

class FoExternalGraphic extends FoObject {

    public FoExternalGraphic(FoBlock owner) {
        super(owner);
    }

    @Override
    protected String getName() {
        return "external-graphic";
    }

    public void setSrc(final String src) throws XMLStreamException {
        writeAttribute("src", src);
    }

    public void setWidthMm(final double widthMm) throws XMLStreamException {
        writeMm("width", widthMm);
    }

    public void setHeightMm(final double heightMm) throws XMLStreamException {
        writeMm("height", heightMm);
    }

    public static final String SCALE_TO_FIT = "scale-to-fit";
    public static final String ALIGN_MIDDLE = "middle";
    public static final String ALIGN_CENTER = "center";
    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_RIGHT = "right";
    public static final String ALIGN_TOP = "top";
    public static final String ALIGN_BOTTOM = "bottom";

    public void setContentWidth(final String contentWidth) throws XMLStreamException {
        writeAttribute("content-width", contentWidth);
    }

    public void setContentHeight(final String contentHeight) throws XMLStreamException {
        writeAttribute("content-height", contentHeight);
    }

    public void setVerticalAlign(EReportCellVAlign align) throws XMLStreamException {

        String strAlign;
        switch (align) {
            case TOP:
                strAlign = ALIGN_TOP;
                break;
            case BOTTOM:
                strAlign = ALIGN_BOTTOM;
                break;
            default:
                strAlign = ALIGN_MIDDLE;
                break;
        }
        writeAttribute("vertical-align", strAlign);
    }    

    public void setHorizontalAlign(EReportCellHAlign align) throws XMLStreamException {
        String strAlign;
        switch (align) {
            case LEFT:
                strAlign = ALIGN_LEFT;
                break;
            case RIGHT:
                strAlign = ALIGN_RIGHT;
                break;
            default:
                strAlign = ALIGN_CENTER;
                break;
        }
        writeAttribute("text-align", strAlign);
    }

    //content-width="scale-to-fit" content-height="scale-to-fit"
}
