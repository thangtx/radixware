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

class FoBlockContainer extends FoObject {

    public enum BorderStyle {

        NONE, DASHED, DOTTED, SOLID
    }

    public enum OverflowType {

        HIDDEN, VISIBLE
    }

    public enum DisplayAlignType {

        BEFORE, CENTER, AFTER
    }

    public enum TextAlignType {

        START, CENTER, END
    }

    public FoBlockContainer(FoFlow owner) {
        super(owner);
    }

    public FoBlockContainer(FoBlockContainer owner) {
        super(owner);
    }

    @Override
    protected String getName() {
        return "block-container";
    }

    public FoBlock addNewBlock() {
        return new FoBlock(this);
    }

    public void addEmptyBlock() throws XMLStreamException {
        FoBlock block = addNewBlock();
        block.begin();
        block.end();
    }

    public FoBlockContainer addNewBlockContainer() {
        return new FoBlockContainer(this);
    }

    public void setAbsolutePositionFixed() throws XMLStreamException {
        writeAttribute("absolute-position", "fixed");
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

    public void setFontStyleNormal() throws XMLStreamException {
        writeAttribute("font-style", "normal");
    }

    public void setFontWeightBold() throws XMLStreamException {
        writeAttribute("font-weight", "bold");
    }

    public void setFontWeightNormal() throws XMLStreamException {
        writeAttribute("font-weight", "normal");
    }

    public void setLeft(double valueMm) throws XMLStreamException {
        writeMm("left", valueMm);
    }

    public void setTop(double valueMm) throws XMLStreamException {
        writeMm("top", valueMm);
    }

    public void setWidth(double valueMm) throws XMLStreamException {
        writeMm("width", valueMm);
    }

    public void setHeight(double valueMm) throws XMLStreamException {
        writeMm("height", valueMm);
    }

    protected void setBorder(String name, BorderStyle style, String rgbColor, double thicknessMm) throws XMLStreamException {
        //String value = "";
        final StringBuilder sb = new StringBuilder();
        switch (style) {
            case DASHED:
                sb.append("dashed");
                //value = "dashed";
                break;
            case DOTTED:
                sb.append("dotted");
                //value = "dotted";
                break;
            case NONE:
                sb.append("none");
                //value = "none";
                break;
            case SOLID:
                sb.append("solid");
                //value = "solid";
                break;
        }

        // RADIX-3646
        // to correctly display border in Acrobat Reader, it width must be 0.5pt.
        // 1pt = 1mm*2.83464567  (from fop sources)
        // 0.1mm - default border size in radix
        // 5.0 / 2.83464567 - coefficient to convert 0.1mm into 0.5pt
        //value += " 0.5pt";//+toFopMm(thicknessMm * 5.0 / 2.83464567);
        sb.append(" 0.5pt");
        if (rgbColor != null) {
            sb.append(" ").append(rgbColor);
            //value += " " + rgbColor;  // w/o convert
        }
        writeAttribute(name, sb.toString());
    }

    public void setBorder(BorderStyle style, String rgbColor, double thicknessMm) throws XMLStreamException {
        setBorder("border", style, rgbColor, thicknessMm);
    }

    public void setBorderLeft(BorderStyle style, String rgbColor, double thicknessMm) throws XMLStreamException {
        setBorder("border-left", style, rgbColor, thicknessMm);
    }

    public void setBorderRight(BorderStyle style, String rgbColor, double thicknessMm) throws XMLStreamException {
        setBorder("border-right", style, rgbColor, thicknessMm);
    }

    public void setBorderTop(BorderStyle style, String rgbColor, double thicknessMm) throws XMLStreamException {
        setBorder("border-top", style, rgbColor, thicknessMm);
    }

    public void setBorderBottom(BorderStyle style, String rgbColor, double thicknessMm) throws XMLStreamException {
        setBorder("border-bottom", style, rgbColor, thicknessMm);
    }

    public void setMargin(double marginMm) throws XMLStreamException {
        String foValue = toFopMm(marginMm);
        writeAttribute("margin", foValue);
    }

    public void setLeftMargin(double marginMm) throws XMLStreamException {
        String foValue = toFopMm(marginMm);
        writeAttribute("margin-left", foValue);
    }

    public void setRightMargin(double marginMm) throws XMLStreamException {
        String foValue = toFopMm(marginMm);
        writeAttribute("margin-right", foValue);
    }

    public void setLineHeight(double valueMm) throws XMLStreamException {
        writeMm("line-height", valueMm);
    }

    public void setOverflow(OverflowType overflowType) throws XMLStreamException {
        String value = "";
        switch (overflowType) {
            case HIDDEN:
                value = "hidden";
                break;
            case VISIBLE:
                value = "visible";
                break;
        }
        writeAttribute("overflow", value);
    }

    public void setDisplayAlign(DisplayAlignType displayAlignType) throws XMLStreamException {
        String value = "";
        switch (displayAlignType) {
            case BEFORE:
                value = "before";
                break;
            case CENTER:
                value = "center";
                break;
            case AFTER:
                value = "after";
                break;
        }
        writeAttribute("display-align", value);
    }

    public void setTextAlign(TextAlignType textAlignType) throws XMLStreamException {
        String value = "";
        switch (textAlignType) {
            case START:
                value = "start";
                break;
            case CENTER:
                value = "center";
                break;
            case END:
                value = "end";
                break;
        }
        writeAttribute("text-align", value);
    }

    public void setTextJustify() throws XMLStreamException {
        writeAttribute("text-align-last", "justify");
    }
}
