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
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlColor;

public class AdsReportBand extends AdsReportAbstractAppearance implements IProfileable, IReportWidgetContainer {

    public static final double GRID_SIZE_MM = 2.5;
    public static final int GRID_SIZE_SYMBOLS = 1;
    private double heightMm = 20.0;
    private int heightRows = 3;
    private boolean startOnNewPage = false;
    private boolean lastOnPage = false;
    private boolean multiPage = false;
    private boolean wrapCells = false;
    private boolean autoHeight = false;
    private final AdsReportContainer reportContainer;
    private final Jml jmlOnAdding;
    private final RadixObjects<AdsSubReport> preReports = new SubReports(this);
    private final RadixObjects<AdsSubReport> postReports = new SubReports(this);
    private Color zebraColor = null;
    // for server
    private boolean visible = true;

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
        return ((AdsReportForm) getContainer()).getOwnerReport();
    }

    private static class SubReports extends RadixObjects<AdsSubReport> {

        public SubReports(AdsReportBand ownerBand) {
            super(ownerBand);
        }

        public AdsReportBand getOwnerBand() {
            return (AdsReportBand) getContainer();
        }

        @Override
        protected void onModified() {
            final AdsReportBand band = getOwnerBand();
            if (band != null) {
                band.onModified();
            }
        }
    }

    public AdsReportBand() {
        super();
        reportContainer = new AdsReportContainer(this);
        jmlOnAdding = Jml.Factory.newInstance(this, "OnAdding");
    }

    protected AdsReportBand(final org.radixware.schemas.adsdef.ReportBand xBand) {
        super(xBand);

        heightMm = xBand.getHeight();
        heightRows = xBand.getHeightRows();

        if (xBand.getReportWidgetContainer() != null) {
            reportContainer = new AdsReportContainer(xBand.getReportWidgetContainer(), this);
        } else {
            reportContainer = new AdsReportContainer(this);
        }

        if (xBand.isSetNewPage()) {
            startOnNewPage = xBand.getNewPage();
        }

        if (xBand.isSetLastOnPage()) {
            lastOnPage = xBand.getLastOnPage();
        }

        if (xBand.isSetMultiPage()) {
            multiPage = xBand.getMultiPage();
        }
        if (xBand.isSetWrapCells()) {
            wrapCells = xBand.getWrapCells();
        }

        if (xBand.isSetAutoHeight()) {
            autoHeight = xBand.getAutoHeight();
        }

        if (xBand.isSetOnAdding()) {
            jmlOnAdding = Jml.Factory.loadFrom(this, xBand.getOnAdding(), "OnAdding");
        } else {
            jmlOnAdding = Jml.Factory.newInstance(this, "OnAdding");
        }

        if (xBand.isSetCells()) {
            for (org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell : xBand.getCells().getCellList()) {
                final AdsReportCell cell = AdsReportCellFactory.loadFrom(xCell);
                reportContainer.getCells().add(cell);
            }
        }

        if (xBand.isSetPreReports()) {
            for (org.radixware.schemas.adsdef.SubReport xPreReport : xBand.getPreReports().getSubReportList()) {
                final AdsSubReport preReport = AdsSubReport.Factory.loadFrom(xPreReport);
                preReports.add(preReport);
            }
        }

        if (xBand.isSetPostReports()) {
            for (org.radixware.schemas.adsdef.SubReport xPostReport : xBand.getPostReports().getSubReportList()) {
                final AdsSubReport postReport = AdsSubReport.Factory.loadFrom(xPostReport);
                postReports.add(postReport);
            }
        }
        if (xBand.isSetZebraColor()) {
            this.zebraColor = XmlColor.parseColor(xBand.getZebraColor());
        }
    }

    protected boolean isCurrentModeSupported() {
        AdsReportForm form = getOwnerForm();
        return form == null || isModeSupported(form.getMode());
    }

    @Override
    protected boolean isModeSupported(AdsReportForm.Mode mode) {
        return true;
    }

    @Override
    public Color getAltBgColor() {
        return zebraColor;
    }

    public void setAltBgColor(Color fgColor) {
        if (!Utils.equals(this.zebraColor, fgColor)) {
            this.zebraColor = fgColor;
            setEditState(EEditState.MODIFIED);
        }
    }

    protected void appendTo(final org.radixware.schemas.adsdef.ReportBand xBand, final ESaveMode saveMode) {
        //if(reportContainer.canSaveInOldFormat()){
        //    reportContainer.appendTo(xBand,saveMode);
        //}else{
        reportContainer.appendTo(xBand.addNewReportWidgetContainer(), saveMode);
        //}
        getFont().appendTo(xBand.addNewFont(), saveMode);

        final Border border = getBorder();
        if (border.isDisplayed()) {
            border.appendTo(xBand.addNewBorder(), saveMode);
        }

        if (!isBgColorInherited()) {
            xBand.setBgColor(XmlColor.mergeColor(getBgColor()));
        }

        if (!isFgColorInherited()) {
            xBand.setFgColor(XmlColor.mergeColor(getFgColor()));
        }

        xBand.setHeight(heightMm);
        xBand.setHeightRows(heightRows);

        if (startOnNewPage) {
            xBand.setNewPage(startOnNewPage);
        }

        if (lastOnPage) {
            xBand.setLastOnPage(lastOnPage);
        }

        if (multiPage) {
            xBand.setMultiPage(multiPage);
        }
        if (wrapCells) {
            xBand.setWrapCells(wrapCells);
        }

        if (autoHeight) {
            xBand.setAutoHeight(autoHeight);
        }

        if (!jmlOnAdding.getItems().isEmpty()) {
            jmlOnAdding.appendTo(xBand.addNewOnAdding(), saveMode);
        }

        if (zebraColor != null) {
            xBand.setZebraColor(XmlColor.mergeColor(zebraColor));
        }
        /* if (!reportContainer.getCells().isEmpty()) {
         org.radixware.schemas.adsdef.ReportBand.Cells xCells = xBand.addNewCells();
         for (AdsReportCell cell : cells) {
         cell.appendTo(xCells.addNewCell(), saveMode);
         }
         }*/
        if (!preReports.isEmpty()) {
            final org.radixware.schemas.adsdef.ReportBand.PreReports xPreReports = xBand.addNewPreReports();
            for (AdsSubReport preReport : preReports) {
                if (preReport.getReportId() != null) {
                    preReport.appendTo(xPreReports.addNewSubReport(), saveMode);
                }
            }
        }

        if (!postReports.isEmpty()) {
            final org.radixware.schemas.adsdef.ReportBand.PostReports xPostReports = xBand.addNewPostReports();
            for (AdsSubReport postReport : postReports) {
                if (postReport.getReportId() != null) {
                    postReport.appendTo(xPostReports.addNewSubReport(), saveMode);
                }
            }
        }
    }

    @Override
    public String getName() {
        final AdsReportForm.Bands ownerBands = getOwnerBands();
        String res = "Band";

        if (ownerBands == null) {
            return "Form Band";
        } else {
            int index = ownerBands.indexOf(this);
            StringBuilder sb = new StringBuilder();
            switch (ownerBands.getType()) {
                case PAGE_HEADER:
                    sb.append("Page Header");
                    break;
                case TITLE:
                    sb.append("Title");
                    break;
                case COLUMN_HEADER:
                    sb.append("Column Header");
                    break;
                case DETAIL:
                    sb.append("Detail");
                    break;
                case SUMMARY:
                    sb.append("Summary");
                    break;
                case PAGE_FOOTER:
                    sb.append("Page Footer");
                    break;
                default:
                    throw new IllegalStateException();
            }
            if (index > 0) {
                return sb.append(" ").append(index).toString();
            } else {
                return sb.toString();
            }
        }

        //}
        // see override in AdsReportGroupBand
    }

    @Override
    public boolean setName(String name) {
        throw new IllegalStateException("Attempt to set report band name");
    }

    @Override
    public RadixObjects<AdsReportWidget> getWidgets() {
        return reportContainer.getCells();
    }

    @Override
    public EReportLayout getLayout() {
        return reportContainer.getLayout();
    }

    @Override
    public void setLayout(EReportLayout layout) {
        if (reportContainer.getLayout() != layout) {
            reportContainer.setLayout(layout);
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return band background color or form background color if inherited.
     */
    @Override
    public Color getBgColor() {
        Color bgColor = null;
        if (isBgColorInherited()) {
            final AdsReportForm ownerForm = getOwnerForm();
            if (ownerForm != null) {
                bgColor = ownerForm.getBgColor();
            }
        }
        if (bgColor == null) {
            bgColor = super.getBgColor();
        }
        return bgColor;
    }

    /**
     * @return band text color or form text color if inherited.
     */
    @Override
    public Color getFgColor() {
        Color fgColor = null;
        if (isFgColorInherited()) {
            final AdsReportForm ownerForm = getOwnerForm();
            if (ownerForm != null) {
                fgColor = ownerForm.getFgColor();
            }
        }
        if (fgColor == null) {
            fgColor = super.getFgColor();
        }
        return fgColor;
    }

    /**
     * @return band height in millimeters.
     */
    public double getHeightMm() {
        return heightMm;
    }

    public void setHeightMm(double heightMm) {
        if (this.heightMm != heightMm) {
            this.heightMm = heightMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getHeightRows() {
        return heightRows;
    }

    public void setHeightRows(int heightMm) {
        if (this.heightRows != heightMm) {
            this.heightRows = heightMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isCellWrappingEnabled() {
        return wrapCells;
    }

    public boolean getCellWrappingEnabled() {
        return wrapCells;
    }

    public void setCellWrappingEnabled(boolean disabled) {
        if (wrapCells != disabled) {
            wrapCells = disabled;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if band must be started on new page on report generation.
     */
    public boolean isStartOnNewPage() {
        return startOnNewPage;
    }

    public boolean getStartOnNewPage() { // for jmlOnAdding
        return startOnNewPage;
    }

    public void setStartOnNewPage(boolean startOnNewPage) {
        if (this.startOnNewPage != startOnNewPage) {
            this.startOnNewPage = startOnNewPage;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if new page mast be started for the following band on report
     * generation.
     */
    public boolean isLastOnPage() {
        return lastOnPage;
    }

    public boolean getLastOnPage() { // for jmlOnAdding
        return lastOnPage;
    }

    public void setLastOnPage(boolean lastOnPage) {
        if (this.lastOnPage != lastOnPage) {
            this.lastOnPage = lastOnPage;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if band is large and can be displayed on several pages.
     */
    public boolean isMultiPage() {
        return multiPage;
    }

    public boolean getMultiPage() { // for jmlOnAdding
        return multiPage;
    }

    public void setMultiPage(boolean multiPage) {
        if (this.multiPage != multiPage) {
            this.multiPage = multiPage;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if height of band adjusted to most bottom edge of cells
     * (except cells that has adjusted height).
     */
    public boolean isAutoHeight() {
        return autoHeight;
    }

    public boolean getAutoHeight() { // for jmlOnAdding
        return autoHeight;
    }

    public void setAutoHeight(boolean autoHeight) {
        if (this.autoHeight != autoHeight) {
            this.autoHeight = autoHeight;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Jml getOnAdding() {
        return jmlOnAdding;
    }

    /**
     * @return owner font or null if band instance is not in form.
     */
    @Override
    public AdsReportForm getOwnerForm() {
        AdsReportForm ownerForm = null;
        for (RadixObject container = this.getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsReportForm) {
                ownerForm = (AdsReportForm) container;
            }
        }
        return ownerForm;
    }

    /*private static class Cells extends RadixObjects<AdsReportCell> {

     protected Cells(AdsReportBand ownerBand) {
     super(ownerBand);
     }

     @Override
     public ClipboardSupport.CanPasteResult canPaste(List<Transfer> transfers, ClipboardSupport.DuplicationResolver resolver) {
     if (isReadOnly()) {
     return ClipboardSupport.CanPasteResult.NO;
     }

     for (Transfer transfer : transfers) {
     final RadixObject objectInClipboard = transfer.getObject();
     if (!(objectInClipboard instanceof AdsReportCell)) {
     return ClipboardSupport.CanPasteResult.NO;
     }
     }
     return ClipboardSupport.CanPasteResult.YES;
     }

     public AdsReportBand getOwnerBand() {
     return (AdsReportBand) getContainer();
     }

     @Override
     protected void onModified() {
     final AdsReportBand band = getOwnerBand();
     if (band != null) {
     band.onModified();
     }
     }
     }

     public RadixObjects<AdsReportCell> getCells() {
     return cells;
     }*/
    @Override // switch to visibility in current package
    protected void setContainer(RadixObject container) {
        super.setContainer(container);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        reportContainer.visit(visitor, provider);
        jmlOnAdding.visit(visitor, provider);
        preReports.visit(visitor, provider);
        postReports.visit(visitor, provider);
    }

    @Override
    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return new ClipboardSupport<AdsReportBand>(this) {
            @Override
            public boolean canCopy() {
                return false;
            }

            @Override
            public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
                return reportContainer.getClipboardSupport().canPaste(transfers, resolver);
            }

            @Override
            public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
                reportContainer.getClipboardSupport().paste(transfers, resolver);
            }
        };
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_BAND;
    }

    // for codegen, see AdsReportClassWriter
    public void onAdding() {
        // NOTHING BY DEFAULT
    }

    // for server
    public boolean isVisible() {
        return visible && isCurrentModeSupported();
    }

    // for server
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public RadixObjects<AdsSubReport> getPreReports() {
        return preReports;
    }

    public RadixObjects<AdsSubReport> getPostReports() {
        return postReports;
    }

    @Override
    protected void onModified() {
        final AdsReportForm form = this.getOwnerForm();
        if (form != null) {
            form.onModified();
        }
    }

    public AdsReportForm.Bands getOwnerBands() {
        for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof AdsReportForm.Bands) {
                return (AdsReportForm.Bands) obj;
            }
        }
        return null;
    }

    // overrided in AdsReportGroupBand
    public EReportBandType getType() {
        EReportBandType type;

        final AdsReportForm.Bands bands = getOwnerBands();
        if (bands == null) {
            throw new IllegalStateException();
        }
        return bands.getType();
//        if (bands.getPageHeaderBand() == this) {
//            type = EReportBandType.PAGE_HEADER;
//        } else if (bands.getTitleBand() == this) {
//            type = EReportBandType.TITLE;
//        } else if (bands.getColumnHeaderBand() == this) {
//            type = EReportBandType.COLUMN_HEADER;
//        } else if (bands.getDetailBand() == this) {
//            type = EReportBandType.DETAIL;
//        } else if (bands.getSummaryBand() == this) {
//            type = EReportBandType.SUMMARY;
//        } else if (bands.getPageFooterBand() == this) {
//            type = EReportBandType.PAGE_FOOTER;
//        } else {
//            throw new IllegalStateException();
//        }
//        return type;
    }

    public void convertToTextMode() {
        final double CONVERSION_Y = 5;
        this.heightRows = (int) Math.round(this.heightMm / CONVERSION_Y);
        for (AdsReportWidget w : this.getWidgets()) {
            w.convertToTextMode();
        }
    }
}
