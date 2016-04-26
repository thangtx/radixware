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
package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import java.awt.Color;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlColor;

public abstract class AdsReportAbstractAppearance extends RadixObject {

    private Color bgColor = Color.WHITE;
    private Color fgColor = Color.BLACK;
    private boolean bgColorInherited = true;
    private boolean fgColorInherited = true;
    private final Font font;
    private final Border border;

    protected AdsReportAbstractAppearance() {
        this.font = new Font();
        this.border = new Border();
    }

    protected AdsReportAbstractAppearance(org.radixware.schemas.adsdef.ReportBand xBand) {
        this.font = new Font(xBand.getFont());

        if (xBand.isSetBorder()) {
            this.border = new Border(xBand.getBorder());
        } else {
            this.border = new Border();
        }

        if (xBand.isSetBgColor()) {
            bgColorInherited = false;
            bgColor = XmlColor.parseColor(xBand.getBgColor());
        } else {
            bgColorInherited = true;
        }

        if (xBand.isSetFgColor()) {
            fgColorInherited = false;
            fgColor = XmlColor.parseColor(xBand.getFgColor());
        } else {
            fgColorInherited = true;
        }
    }

    protected AdsReportAbstractAppearance(org.radixware.schemas.adsdef.ReportCell xCell) {
        if (xCell.isSetFont()) {
            this.font = new Font(xCell.getFont());
        } else {
            this.font = new Font();
        }

        if (xCell.isSetBorder()) {
            this.border = new Border(xCell.getBorder());
        } else {
            this.border = new Border();
        }

        if (xCell.isSetBgColor()) {
            bgColorInherited = false;
            bgColor = XmlColor.parseColor(xCell.getBgColor());
        } else {
            bgColorInherited = true;
        }

        if (xCell.isSetFgColor()) {
            fgColorInherited = false;
            fgColor = XmlColor.parseColor(xCell.getFgColor());
        } else {
            fgColorInherited = true;
        }
    }

    protected AdsReportAbstractAppearance(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        if (xCell.isSetFont()) {
            this.font = new Font(xCell.getFont());
        } else {
            this.font = new Font();
        }

        if (xCell.isSetBorder()) {
            this.border = new Border(xCell.getBorder());
        } else {
            this.border = new Border();
        }

        if (xCell.isSetBgColor()) {
            bgColorInherited = false;
            bgColor = XmlColor.parseColor(xCell.getBgColor());
        } else {
            bgColorInherited = true;
        }

        if (xCell.isSetFgColor()) {
            fgColorInherited = false;
            fgColor = XmlColor.parseColor(xCell.getFgColor());
        } else {
            fgColorInherited = true;
        }
    }

    // overwrited in used classes to search inherited.
    public Color getBgColor() {
        return bgColor;
    }

    public Color getAltBgColor() {
        return null;
    }

    protected abstract boolean isModeSupported(AdsReportForm.Mode mode);

    public void setBgColor(Color bgColor) {
        assert (bgColor != null);
        if (!Utils.equals(this.bgColor, bgColor)) {
            this.bgColor = bgColor;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isBgColorInherited() {
        return bgColorInherited;
    }

    public boolean getBgColorInherited() { // for onAding
        return bgColorInherited;
    }

    public void setBgColorInherited(boolean bgColorInherited) {
        if (this.bgColorInherited != bgColorInherited) {
            this.bgColorInherited = bgColorInherited;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Color getFgColor() {
        return fgColor;
    }

    public void setFgColor(Color fgColor) {
        assert (fgColor != null);
        if (!Utils.equals(this.fgColor, fgColor)) {
            this.fgColor = fgColor;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isFgColorInherited() {
        return fgColorInherited;
    }

    public boolean getFgColorInherited() {  // for onAdding
        return fgColorInherited;
    }

    public void setFgColorInherited(boolean fgColorInherited) {
        if (this.fgColorInherited != fgColorInherited) {
            this.fgColorInherited = fgColorInherited;
            setEditState(EEditState.MODIFIED);
        }
    }

    public final class Font {

        private String name = "Arial";
        private String verticalAlign = null;
        private boolean bold = false;
        private boolean italic = false;
        private boolean underline = false;
        private boolean lineThrough = false;
        private double sizeMm = 10.0;

        /**
         * Creates a new Font.
         */
        protected Font() {
        }

        public Font copy() {
            Font copyFont = new Font();
            copyFont.name = name;
            copyFont.sizeMm = sizeMm;
            copyFont.bold = bold;
            copyFont.italic = italic;
            copyFont.underline = underline;
            copyFont.lineThrough = lineThrough;
            copyFont.verticalAlign = verticalAlign;
            return copyFont;
        }

        protected Font(org.radixware.schemas.adsdef.ReportFont xFont) {
            this.name = xFont.getName();
            this.sizeMm = xFont.getSize();
            this.bold = (xFont.isSetBold() ? xFont.getBold() : false);
            this.italic = (xFont.isSetItalic() ? xFont.getItalic() : false);
        }

        protected void appendTo(org.radixware.schemas.adsdef.ReportFont xFont, ESaveMode saveMode) {
            xFont.setName(this.name);
            xFont.setSize(this.sizeMm);
            if (this.bold) {
                xFont.setBold(this.bold);
            }
            if (this.italic) {
                xFont.setItalic(this.italic);
            }
        }

        /**
         * @return font name, Arial for example.
         */
        public String getName() {
            return name;
        }

        public void setName(String name) {
            assert (name != null);
            if (!Utils.equals(this.name, name)) {
                this.name = name;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return font name, Arial for example.
         */
        public boolean isSetVerticalAlign() {
            return verticalAlign != null;
        }

        public String getVerticalAlign() {
            return verticalAlign;
        }

        public void setVerticalAlign(String name) {
            assert (name != null);
            if (!Utils.equals(this.verticalAlign, name)) {
                this.verticalAlign = name;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if font is bold, false otherwise.
         */
        public boolean isBold() {
            return bold;
        }

        public boolean getBold() { // for onAdding
            return bold;
        }

        public void setBold(boolean bold) {
            if (this.bold != bold) {
                this.bold = bold;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if font is italic, false otherwise.
         */
        public boolean isItalic() {
            return italic;
        }

        public boolean getItalic() { // for onAdding
            return italic;
        }

        public void setItalic(boolean italic) {
            if (this.italic != italic) {
                this.italic = italic;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if font is underline, false otherwise.
         */
        public boolean isUnderline() {
            return underline;
        }

        public boolean getUnderline() { // for onAdding
            return underline;
        }

        public void setUnderline(boolean bold) {
            if (this.underline != bold) {
                this.underline = bold;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if font is line-through, false otherwise.
         */
        public boolean isLineThrough() {
            return lineThrough;
        }

        public boolean getLineThrough() { // for onAdding
            return lineThrough;
        }

        public void setLineThrough(boolean lineThrough) {
            if (this.lineThrough != lineThrough) {
                this.lineThrough = lineThrough;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return font size in millimeters (without line spacing).
         */
        public double getSizeMm() {
            return sizeMm;
        }

        public double getSizePts() {
            return mm2pts(getSizeMm());
        }

        public void setSizePts(double pts) {
            setSizeMm(pts2mm(pts));
        }

        public void setSizeMm(double sizeMm) {
            if (this.sizeMm != sizeMm) {
                this.sizeMm = sizeMm;
                setEditState(EEditState.MODIFIED);
            }
        }

        private static final double MM2PTS_CONST = 25.4 / 72;

        private double mm2pts(double mm) {
            return mm / MM2PTS_CONST;
        }

        private double pts2mm(double pts) {
            return pts * MM2PTS_CONST;
        }
    }

    /**
     * @return information about font.
     */
    // overwrited in cell for search inherited
    public Font getFont() {
        return font;
    }

    public final class Border {

        public static final double DEFAULT_THICKNESS_MM = 0.1;
        private EReportBorderStyle style = EReportBorderStyle.SOLID;
        private Color color = Color.BLACK;
        private double thicknessMm = DEFAULT_THICKNESS_MM;
        private boolean onTop = false, onBottom = false, onLeft = false, onRight = false;

        /**
         * Creates a new Border.
         */
        protected Border() {
        }

        protected Border(org.radixware.schemas.adsdef.ReportBorder xBorder) {
            this.color = XmlColor.parseColor(xBorder.getColor());
            this.thicknessMm = xBorder.getThickness();
            this.style = xBorder.getStyle();
            this.onLeft = xBorder.getLeft();
            this.onRight = xBorder.getRight();
            this.onTop = xBorder.getTop();
            this.onBottom = xBorder.getBottom();
        }

        protected void appendTo(org.radixware.schemas.adsdef.ReportBorder xBorder, ESaveMode saveMode) {
            xBorder.setColor(XmlColor.mergeColor(this.color));
            xBorder.setThickness(this.thicknessMm);
            xBorder.setStyle(this.style);
            xBorder.setLeft(this.onLeft);
            xBorder.setRight(this.onRight);
            xBorder.setTop(this.onTop);
            xBorder.setBottom(this.onBottom);
        }

        /**
         * @return border style.
         */
        public EReportBorderStyle getStyle() {
            return style;
        }

        public void setStyle(EReportBorderStyle style) {
            assert (style != null);
            if (!Utils.equals(this.style, style)) {
                this.style = style;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return border color.
         */
        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            assert (color != null);
            if (!Utils.equals(this.color, color)) {
                this.color = color;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return border line thickness in millimeters.
         */
        public double getThicknessMm() {
            return thicknessMm;
        }

        public void setThicknessMm(double thicknessMm) {
            if (!Utils.equals(this.thicknessMm, thicknessMm)) {
                this.thicknessMm = thicknessMm;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if border line displayed on top, false otherwise.
         */
        public boolean isOnTop() {
            return onTop;
        }

        public boolean getOnTop() { // for onAdding
            return onTop;
        }

        public void setOnTop(boolean onTop) {
            if (this.onTop != onTop) {
                this.onTop = onTop;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if border line displayed on bottom, false otherwise.
         */
        public boolean isOnBottom() {
            return onBottom;
        }

        public boolean getOnBottom() { // for onAdding
            return onBottom;
        }

        public void setOnBottom(boolean onBottom) {
            if (this.onBottom != onBottom) {
                this.onBottom = onBottom;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if border line displayed on left, false otherwise.
         */
        public boolean isOnLeft() {
            return onLeft;
        }

        public boolean getOnLeft() { // for onAdding
            return onLeft;
        }

        public void setOnLeft(boolean onLeft) {
            if (this.onLeft != onLeft) {
                this.onLeft = onLeft;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return true if border line displayed on right, false otherwise.
         */
        public boolean isOnRight() {
            return onRight;
        }

        public boolean getOnRight() { // for onAdding
            return onRight;
        }

        public void setOnRight(boolean onRight) {
            if (this.onRight != onRight) {
                this.onRight = onRight;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean isDisplayed() {
            return onLeft || onRight || onTop || onBottom;
        }
    }

    /**
     * @return information about border.
     */
    public Border getBorder() {
        return border;
    }
}
