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
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.EReportCellHAlign;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportCellVAlign;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlColor;

public abstract class AdsReportCell extends AdsReportWidget implements IProfileable {

    public static final Double DEFAULT_MARGIN = 1.0;
    public static final Double DEFAULT_LINE_SPACING = 1.0;
    public static final Double MINIMAL_WIDTH = 10.0;
    private static final String STR_ON_ADDING = "OnAdding";
    //
    private EReportCellHAlign hAlign = EReportCellHAlign.LEFT;
    private EReportCellVAlign vAlign = EReportCellVAlign.TOP;
    private boolean fontInherited = true;
    private double marginTopMm = DEFAULT_MARGIN;
    private double marginBottomMm = DEFAULT_MARGIN;
    private double marginLeftMm = DEFAULT_MARGIN;
    private double marginRightMm = DEFAULT_MARGIN;

    private int marginTopRows = 0;
    private int marginBottomRows = 0;
    private int marginLeftCols = 0;
    private int marginRightCols = 0;
    private boolean ignoreZebraSettings;

    private double lineSpacingMm = DEFAULT_LINE_SPACING;
    private boolean wrapWord = false;
    private boolean clipContent = false;
    private boolean snapTopEdge = false;
    private boolean snapBottomEdge = false;
    private final Jml jmlOnAdding;
    private AdsReportForm.Mode preferredMode = null;

    // for server
    private String runTimeContent = "";
    private boolean visible = true;

    protected AdsReportCell() {
        jmlOnAdding = Jml.Factory.newInstance(this, STR_ON_ADDING);
    }

    protected AdsReportCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);

        if (xCell.isSetName()) {
            final String name = xCell.getName();
            if (name != null && !name.isEmpty()) {
                setName(name);
            }
        }
        fontInherited = !xCell.isSetFont();

        hAlign = xCell.getHorzAlign();
        vAlign = xCell.getVertAlign();

        if (xCell.isSetMargin()) {
            marginTopMm = xCell.getMargin();
            marginLeftMm = xCell.getMargin();
            marginRightMm = xCell.getMargin();
            marginBottomMm = xCell.getMargin();
        }

        if (xCell.isSetMarginTxt()) {
            marginTopRows = xCell.getMarginTxt();
            marginBottomRows = xCell.getMarginTxt();
            marginLeftCols = xCell.getMarginTxt();
            marginRightCols = xCell.getMarginTxt();
        }

        if (xCell.isSetTopMargin()) {
            marginTopMm = xCell.getTopMargin();
        }
        if (xCell.isSetLeftMargin()) {
            marginLeftMm = xCell.getLeftMargin();
        }
        if (xCell.isSetRightMargin()) {
            marginRightMm = xCell.getRightMargin();
        }
        if (xCell.isSetBottomMargin()) {
            marginBottomMm = xCell.getBottomMargin();
        }

        if (xCell.isSetTopMarginRows()) {
            marginTopRows = xCell.getTopMarginRows();
        }

        if (xCell.isSetBottomMarginRows()) {
            marginBottomRows = xCell.getBottomMarginRows();
        }

        if (xCell.isSetLeftMarginCols()) {
            marginLeftCols = xCell.getLeftMarginCols();
        }
        if (xCell.isSetRightMarginCols()) {
            marginLeftCols = xCell.getRightMarginCols();
        }

        if (xCell.isSetLineSpacing()) {
            lineSpacingMm = xCell.getLineSpacing();
        }

        if (xCell.isSetClipContent()) {
            clipContent = xCell.getClipContent();
        }

        if (xCell.isSetWordWrap()) {
            wrapWord = xCell.getWordWrap();
        }

        if (xCell.isSetSnapTopEdge()) {
            snapTopEdge = xCell.getSnapTopEdge();
        }

        if (xCell.isSetSnapBottomEdge()) {
            snapBottomEdge = xCell.getSnapBottomEdge();
        }
        if (xCell.isSetMode()) {
            if (AdsReportForm.Mode.GRAPHICS.name().equals(xCell.getMode())) {
                preferredMode = AdsReportForm.Mode.GRAPHICS;
            } else if (AdsReportForm.Mode.TEXT.name().equals(xCell.getMode())) {
                preferredMode = AdsReportForm.Mode.TEXT;
            }
        }
        /*if (xCell.isSetAdjustHeight()) {
         adjustHeight = xCell.getAdjustHeight();
         }

         if (xCell.isSetAdjustWidth()) {
         adjustWidth = xCell.getAdjustWidth();
         }*/

        if (xCell.isSetOnAdding()) {
            jmlOnAdding = Jml.Factory.loadFrom(this, xCell.getOnAdding(), STR_ON_ADDING);
        } else {
            jmlOnAdding = Jml.Factory.newInstance(this, STR_ON_ADDING);
        }
        if (xCell.isSetIgnoreParentZebra()) {
            this.ignoreZebraSettings = xCell.getIgnoreParentZebra();
        }
        /*leftMm = xCell.getLeft();
         topMm = xCell.getTop();
         widthMm = xCell.getWidth();
         heightMm = xCell.getHeight();*/
    }

    protected AdsReportCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);

        if (xCell.isSetName()) {
            final String name = xCell.getName();
            if (name != null && !name.isEmpty()) {
                setName(name);
            }
        }
        fontInherited = !xCell.isSetFont();

        hAlign = xCell.getHorzAlign();
        vAlign = xCell.getVertAlign();
        if (vAlign == EReportCellVAlign.ADJUST) {
            adjustHeight = true;
        }

        if (xCell.isSetMargin()) {
            marginTopMm = xCell.getMargin();
            marginLeftMm = xCell.getMargin();
            marginRightMm = xCell.getMargin();
            marginBottomMm = xCell.getMargin();
        }

        if (xCell.isSetMarginTxt()) {
            marginTopRows = xCell.getMarginTxt();
            marginBottomRows = xCell.getMarginTxt();
            marginLeftCols = xCell.getMarginTxt();
            marginRightCols = xCell.getMarginTxt();
        }

        if (xCell.isSetTopMargin()) {
            marginTopMm = xCell.getTopMargin();
        }
        if (xCell.isSetLeftMargin()) {
            marginLeftMm = xCell.getLeftMargin();
        }
        if (xCell.isSetRightMargin()) {
            marginRightMm = xCell.getRightMargin();
        }
        if (xCell.isSetBottomMargin()) {
            marginBottomMm = xCell.getBottomMargin();
        }

        if (xCell.isSetTopMarginRows()) {
            marginTopRows = xCell.getTopMarginRows();
        }

        if (xCell.isSetBottomMarginRows()) {
            marginBottomRows = xCell.getBottomMarginRows();
        }

        if (xCell.isSetLeftMarginCols()) {
            marginLeftCols = xCell.getLeftMarginCols();
        }
        if (xCell.isSetRightMarginCols()) {
            marginLeftCols = xCell.getRightMarginCols();
        }

        if (xCell.isSetLeftMargin()) {
            marginLeftMm = xCell.getLeftMargin();
        }
        if (xCell.isSetRightMargin()) {
            marginRightMm = xCell.getRightMargin();
        }
        if (xCell.isSetBottomMargin()) {
            marginBottomMm = xCell.getBottomMargin();
        }

        if (xCell.isSetLineSpacing()) {
            lineSpacingMm = xCell.getLineSpacing();
        }

        if (xCell.isSetClipContent()) {
            clipContent = xCell.getClipContent();
        }

        if (xCell.isSetWordWrap()) {
            wrapWord = xCell.getWordWrap();
        }

        if (xCell.isSetSnapTopEdge()) {
            snapTopEdge = xCell.getSnapTopEdge();
        }

        if (xCell.isSetSnapBottomEdge()) {
            snapBottomEdge = xCell.getSnapBottomEdge();
        }

        if (xCell.isSetMode()) {
            if (AdsReportForm.Mode.GRAPHICS.name().equals(xCell.getMode())) {
                preferredMode = AdsReportForm.Mode.GRAPHICS;
            } else if (AdsReportForm.Mode.TEXT.name().equals(xCell.getMode())) {
                preferredMode = AdsReportForm.Mode.TEXT;
            }
        }

        if (xCell.isSetOnAdding()) {
            jmlOnAdding = Jml.Factory.loadFrom(this, xCell.getOnAdding(), STR_ON_ADDING);
        } else {
            jmlOnAdding = Jml.Factory.newInstance(this, STR_ON_ADDING);
        }
        if (xCell.isSetIgnoreParentZebra()) {
            this.ignoreZebraSettings = xCell.getIgnoreParentZebra();
        }
    }

    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell);
        xCell.setType(getCellType());

        if (preferredMode != null) {
            xCell.setMode(preferredMode.name());
        }

        final String name = getName();
        if (name != null && !name.isEmpty()) {
            xCell.setName(name);
        }

        final Border border = getBorder();
        if (border.isDisplayed()) {
            border.appendTo(xCell.addNewBorder(), saveMode);
        }

        if (!isFontInherited()) {
            final Font font = getFont();
            font.appendTo(xCell.addNewFont(), saveMode);
        }

        if (!isBgColorInherited()) {
            xCell.setBgColor(XmlColor.mergeColor(getBgColor()));
        }

        if (!isFgColorInherited()) {
            xCell.setFgColor(XmlColor.mergeColor(getFgColor()));
        }

        xCell.setHorzAlign(hAlign);
        xCell.setVertAlign(vAlign);

        if (!Utils.equals(marginTopMm, DEFAULT_MARGIN)) {
            xCell.setTopMargin(marginTopMm);
        }
        if (!Utils.equals(marginLeftMm, DEFAULT_MARGIN)) {
            xCell.setLeftMargin(marginLeftMm);
        }
        if (!Utils.equals(marginRightMm, DEFAULT_MARGIN)) {
            xCell.setRightMargin(marginRightMm);
        }
        if (!Utils.equals(marginBottomMm, DEFAULT_MARGIN)) {
            xCell.setBottomMargin(marginBottomMm);
        }

        if (marginBottomRows != 0) {
            xCell.setTopMarginRows(marginTopRows);
        }
        if (marginBottomRows != 0) {
            xCell.setTopMarginRows(marginTopRows);
        }
        if (marginLeftCols != 0) {
            xCell.setLeftMarginCols(marginLeftCols);
        }
        if (marginRightCols != 0) {
            xCell.setRightMarginCols(marginRightCols);
        }

        if (!Utils.equals(lineSpacingMm, DEFAULT_LINE_SPACING)) {
            xCell.setLineSpacing(lineSpacingMm);
        }

        if (clipContent) {
            xCell.setClipContent(clipContent);
        }

        if (wrapWord) {
            xCell.setWordWrap(wrapWord);
        }

        if (snapTopEdge) {
            xCell.setSnapTopEdge(snapTopEdge);
        }

        if (snapBottomEdge) {
            xCell.setSnapBottomEdge(snapBottomEdge);
        }

        /*if (adjustHeight) {
         xCell.setAdjustHeight(adjustHeight);
         }

         if (adjustWidth) {
         xCell.setAdjustWidth(adjustWidth);
         }*/
        if (!jmlOnAdding.getItems().isEmpty()) {
            jmlOnAdding.appendTo(xCell.addNewOnAdding(), saveMode);
        }
        if (ignoreZebraSettings) {
            xCell.setIgnoreParentZebra(true);
        }
        /*xCell.setLeft(leftMm);
         xCell.setTop(topMm);
         xCell.setWidth(widthMm);
         xCell.setHeight(heightMm);*/
    }

    public abstract EReportCellType getCellType();

    /**
     * @return cell background color or band background color if inherited.
     */
    @Override
    public Color getBgColor() {
        if (isBgColorInherited()) {
            final AdsReportBand ownerBand = getOwnerBand();
            if (ownerBand != null) {
                return ownerBand.getBgColor();
            }
        }
        return super.getBgColor();
    }

    /**
     * @return cell text color or band text color if inherited.
     */
    @Override
    public Color getFgColor() {
        if (isFgColorInherited()) {
            final AdsReportBand ownerBand = getOwnerBand();
            if (ownerBand != null) {
                return ownerBand.getFgColor();
            }
        }
        return super.getFgColor();
    }

    @Override
    public Color getAltBgColor() {
        if (ignoreZebraSettings) {
            return null;
        } else {
            final AdsReportBand ownerBand = getOwnerBand();
            if (ownerBand != null) {
                return ownerBand.getAltBgColor();
            }
        }
        return super.getAltBgColor();
    }

    public boolean isIgnoreAltBgColor() {
        return ignoreZebraSettings;
    }

    public void setIgnoreAltBgColor(boolean ignore) {
        if (ignoreZebraSettings != ignore) {
            ignoreZebraSettings = ignore;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return cell font information or band font information if inherited.
     */
    @Override
    public Font getFont() {
        if (isFontInherited()) {
            final AdsReportBand ownerBand = getOwnerBand();
            if (ownerBand != null) {
                return ownerBand.getFont();
            }
        }
        return super.getFont();
    }

    /**
     * @return true if font inherited from band, false otherwise
     */
    public boolean isFontInherited() {
        return fontInherited;
    }

    public boolean getFontInherited() { // for jmlOnAdding
        return fontInherited;
    }

    public void setFontInherited(boolean fontInherited) {
        if (getFontInherited() != fontInherited) {
            this.fontInherited = fontInherited;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return text margin from edges in millimeters.
     */
    public double getMarginTopMm() {
        return marginTopMm;
    }

    public int getMarginTopRows() {
        return marginTopRows;
    }

    public void setMarginTopRows(int rows) {
        if (getMarginTopRows() != rows) {
            marginTopRows = rows;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setMarginBottomRows(int rows) {
        if (getMarginBottomRows() != rows) {
            marginBottomRows = rows;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setMarginLeftCols(int cols) {
        if (getMarginLeftCols() != cols) {
            marginLeftCols = cols;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setMarginRightCols(int cols) {
        if (getMarginRightCols() != cols) {
            marginRightCols = cols;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setMarginTopMm(double marginMm) {
        if (getMarginTopMm() != marginMm) {
            marginTopMm = marginMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    public double getMarginBottomMm() {
        return marginBottomMm;
    }

    public int getMarginBottomRows() {
        return marginBottomRows;
    }

    public void setMarginBottomMm(double marginMm) {
        if (getMarginBottomMm() != marginMm) {
            marginBottomMm = marginMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    public double getMarginLeftMm() {
        return marginLeftMm;
    }

    public int getMarginLeftCols() {
        return marginLeftCols;
    }

    public void setMarginLeftMm(double marginMm) {
        if (getMarginLeftMm() != marginMm) {
            marginLeftMm = marginMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    public double getMarginRightMm() {
        return marginRightMm;
    }

    public int getMarginRightCols() {
        return marginRightCols;
    }

    public void setMarginRightMm(double marginMm) {
        if (getMarginRightMm() != marginMm) {
            marginRightMm = marginMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return space in millimeters between text lines.
     */
    public double getLineSpacingMm() {
        return lineSpacingMm;
    }

    public void setLineSpacingMm(double lineSpacingMm) {
        if (this.lineSpacingMm != lineSpacingMm) {
            this.lineSpacingMm = lineSpacingMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if text words are wrap, false otherwise.
     */
    public boolean isWrapWord() {
        return wrapWord;
    }

    public boolean getWrapWord() { // for jmlOnAdding
        return wrapWord;
    }

    public void setWrapWord(boolean wrapWord) {
        if (this.wrapWord != wrapWord) {
            this.wrapWord = wrapWord;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if text is clipped withind the cell area, false otherwise.
     */
    public boolean isClipContent() {
        return clipContent;
    }

    public boolean getClipContent() { // for jmlOnAdding
        return clipContent;
    }

    public void setClipContent(boolean clipContent) {
        if (this.clipContent != clipContent) {
            this.clipContent = clipContent;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return text horizontal align.
     */
    public EReportCellHAlign getHAlign() {
        return hAlign;
    }

    public void setHAlign(EReportCellHAlign hAlign) {
        assert (hAlign != null);
        if (!Utils.equals(this.hAlign, hAlign)) {
            this.hAlign = hAlign;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return text vertical align.
     */
    public EReportCellVAlign getVAlign() {
        return vAlign;
    }

    public void setVAlign(EReportCellVAlign vAlign) {
        assert (vAlign != null);
        if (!Utils.equals(this.vAlign, vAlign)) {
            this.vAlign = vAlign;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Jml getOnAdding() {
        return jmlOnAdding;
    }

    /**
     * Get indent from band left edge.
     *
     * @return cell left position in millimeters.
     */
    /*@Override
     public double getLeftMm() {
     return widgetAppearance.getLeftMm();
     }

     public void setLeftMm(double leftMm) {
     if (!Utils.equals(widgetAppearance.getLeftMm(), leftMm)) {
     widgetAppearance.setLeftMm(leftMm);
     setEditState(EEditState.MODIFIED);
     }
     }*/
    /**
     * Get indent from band top edge.
     *
     * @return cell top position in millimeters.
     */
    /*@Override
     public double getTopMm() {
     return widgetAppearance.getTopMm();
     }

     public void setTopMm(double topMm) {
     if (!Utils.equals(widgetAppearance.getTopMm(), topMm)) {
     widgetAppearance.setTopMm(topMm);
     setEditState(EEditState.MODIFIED);
     }
     }*/
    /**
     * @return cell width in millimeters
     */
    /*public double getWidthMm() {
     return widgetAppearance.getWidthMm();
     }

     public void setWidthMm(double widthMm) {
     if (!Utils.equals(widgetAppearance.getWidthMm(), widthMm)) {
     widgetAppearance.setWidthMm(widthMm);
     setEditState(EEditState.MODIFIED);
     }
     }*/
    /**
     * @return cell height in millimeters
     */
    /* public double getHeightMm() {
     return widgetAppearance.getHeightMm();
     }

     public void setHeightMm(double heightMm) {
     if (!Utils.equals(widgetAppearance.getHeightMm(), heightMm)) {
     widgetAppearance.setHeightMm(heightMm);
     setEditState(EEditState.MODIFIED);
     }
     }*/
    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        jmlOnAdding.visit(visitor, provider);
    }

    private static class AdsReportCellClipboardSupport extends AdsClipboardSupport<AdsReportCell> {

        private static class AdsReportCellTransfer extends AdsTransfer<AdsReportCell> {

            public AdsReportCellTransfer(AdsReportCell cell) {
                super(cell);
            }

            @Override
            protected void afterDuplicate() {
                super.afterDuplicate();
                final AdsReportCell cell = getObject();
                cell.setLeftMm(cell.getLeftMm() + AdsReportBand.GRID_SIZE_MM);
                cell.setTopMm(cell.getTopMm() + AdsReportBand.GRID_SIZE_MM);
            }
        }

        public AdsReportCellClipboardSupport(AdsReportCell cell) {
            super(cell);
        }

        @Override
        protected XmlObject copyToXml() {
            final org.radixware.schemas.adsdef.ReportCell xCell = org.radixware.schemas.adsdef.ReportCell.Factory.newInstance();
            radixObject.appendTo(xCell, ESaveMode.NORMAL);
            return xCell;
        }

        @Override
        protected AdsReportCell loadFrom(XmlObject xmlObject) {
            final org.radixware.schemas.adsdef.ReportCell xCell = (org.radixware.schemas.adsdef.ReportCell) xmlObject;
            return AdsReportCellFactory.loadFrom(xCell);
        }

        @Override
        protected AdsReportCellTransfer newTransferInstance() {
            return new AdsReportCellTransfer(radixObject);
        }
    }

    @Override
    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return new AdsReportCellClipboardSupport(this);
    }

    // TODO: refactor: move to server class
    // for server
    public String getRunTimeContent() {
        return runTimeContent;
    }

    // TODO: refactor: move to server class
    // for server
    public void setRunTimeContent(String runTimeContent) {
        this.runTimeContent = runTimeContent;
    }

    // for codegen, see AdsReportClassWriter
    public void onAdding() {
        // NOTHING BY DEFAULT
    }

    public AdsReportForm.Mode getPreferredMode() {
        return preferredMode;
    }

    public void setPreferredMode(AdsReportForm.Mode mode) {
        if (this.preferredMode != mode) {
            this.preferredMode = mode;
            setEditState(EEditState.MODIFIED);
        }
    }

    private AdsReportForm.Mode getDiagramMode() {
        AdsReportForm form = getOwnerForm();
        return form == null ? null : form.getMode();
    }

    public boolean isDiagramModeSupported(AdsReportForm.Mode mode) {
        if (mode == null) {
            return true;
        } else {
            return isModeSupported(mode) && (getPreferredMode() == null || getPreferredMode() == mode);
        }
    }

    // for server
    public boolean isVisible() {
        return visible && isDiagramModeSupported(getDiagramMode());
    }

    public boolean getVisible() { // for jmlOnAdding
        return isVisible();
    }

    // for server
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    protected void onModified() {
        final AdsReportBand band = this.getOwnerBand();
        if (band != null) {
            band.onModified();
        }
    }

    public boolean isSnapTopEdge() {
        return snapTopEdge;
    }

    public void setSnapTopEdge(boolean snapTopEdge) {
        if (this.snapTopEdge != snapTopEdge) {
            this.snapTopEdge = snapTopEdge;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isSnapBottomEdge() {
        return snapBottomEdge;
    }

    public void setSnapBottomEdge(boolean snapBottomEdge) {
        if (this.snapBottomEdge != snapBottomEdge) {
            this.snapBottomEdge = snapBottomEdge;
            setEditState(EEditState.MODIFIED);
        }
    }

    /* public boolean isAdjustHeight() {
     return adjustHeight;
     }

     public void setAdjustHeight(boolean adjustHeight) {
     if (this.adjustHeight != adjustHeight) {
     this.adjustHeight = adjustHeight;
     setEditState(EEditState.MODIFIED);
     }
     }

     public boolean isAdjustWidth() {
     return adjustWidth;
     }

     public void setAdjustWidth(boolean adjustWidth) {
     if (this.adjustWidth != adjustWidth) {
     this.adjustWidth = adjustWidth;
     setEditState(EEditState.MODIFIED);
     }
     }*/
    @Override
    public AdsProfileSupport getProfileSupport() {
        return new AdsProfileSupport(this);
    }

    @Override
    public boolean isProfileable() {
        return true;
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return ((AdsReportForm) getContainer().getContainer()).getOwnerReport();
    }

    @Override
    public boolean isReportContainer() {
        return false;
    }
}
