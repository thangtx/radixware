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

import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginMm;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginTxt;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlColor;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.Report;
import org.radixware.schemas.adsdef.ReportBand;

public class AdsReportForm extends RadixObject implements IReportNavigatorObject{
    public final static double DEFAULT_GRID_SIZE_MM = 2.5;
    
    private Color bgColor = Color.WHITE;
    private Color fgColor = Color.BLACK;
    private final Margin margin = new Margin();
    private final MarginTxt marginTxt = new MarginTxt();
    private int pageWidthMm = 210;
    private int pageHeightMm = 297;
    private int pageWidthColumns = 80;
    private int pageHeightRows = 40;
    private Mode mode = Mode.GRAPHICS;
    private boolean textModeAccepted = false;
    private int multifileGroupLevel = -1;
    private boolean repeatGroupHeadersOnNewPage;

    private boolean showGrid = true;
    private boolean snapToGrid = true;
    private double gridSizeMm = DEFAULT_GRID_SIZE_MM;

    //
    public class Bands extends RadixObjects<AdsReportBand> {

        private final EReportBandType type;
        private volatile boolean loading = false;

        public Bands(EReportBandType type) {
            super(AdsReportForm.this);
            this.type = type;
        }

        public void loadFrom(Report.Form.Bands xBands) {
            assert xBands.getType() == type;
            loading = true;
            try {
                for (ReportBand xBand : xBands.getBandList()) {
                    AdsReportBand band = new AdsReportBand(xBand);
                    add(band);
                }
            } finally {
                loading = false;
            }
        }

        void load(AdsReportBand band) {
            loading = true;
            try {
                add(band);
            } finally {
                loading = false;
            }
        }

        public EReportBandType getType() {
            return type;
        }

        /**
         * all values assigned to fonts are caused by RADIX-9979
         */
        @Override
        protected void onAdd(AdsReportBand object) {
            super.onAdd(object);
            if (!loading) {
                switch (type) {
                    case PAGE_HEADER:
                        object.getFont().setSizeMm(3.0);
                        break;
                    case TITLE:
                        object.getFont().setSizeMm(5.62);
                        object.getFont().setBold(true);
                        break;
                    case GROUP_HEADER:
                        object.getFont().setSizeMm(3.51);
                        break;
                    case COLUMN_HEADER:
                        object.getFont().setSizeMm(2.81);
                        object.setBgColorInherited(true);
                        break;
                    case DETAIL:
                        object.getFont().setSizeMm(2.81);
                        break;
                    case SUMMARY:
                        object.getFont().setSizeMm(3.53);
                        break;
                    case PAGE_FOOTER:
                        object.getFont().setSizeMm(3.0);
                        break;
                }
                if (object.isNewStyle()) {
                    AdsReportDefaultStyle.setDefaultBandStyle(object);
                }
                AdsReportForm.this.changeSupport.fireEvent(new ChangedEvent(this, ChangedEvent.ChangeEventType.ADD));
            }
        }

        @Override
        protected void onRemove(AdsReportBand object) {
            super.onRemove(object);
            if (!loading) {
                AdsReportForm.this.changeSupport.fireEvent(new ChangedEvent(this, ChangedEvent.ChangeEventType.REMOVE));
            }
        }

        @Override
        protected void onClear() {
            super.onClear();
            if (!loading) {
                AdsReportForm.this.changeSupport.fireEvent(new ChangedEvent(this));
            }
        }

        public void appendTo(Report.Form xForm, ESaveMode saveMode) {
            if (isEmpty()) {
                return;
            }
            if (size() == 1) {
                ReportBand xBand;
                switch (type) {
                    case PAGE_HEADER:
                        xBand = xForm.addNewPageHeader();
                        break;
                    case TITLE:
                        xBand = xForm.addNewTitle();
                        break;
                    case COLUMN_HEADER:
                        xBand = xForm.addNewColumnsHeaders();
                        break;
                    case DETAIL:
                        xBand = xForm.addNewDetails();
                        break;
                    case SUMMARY:
                        xBand = xForm.addNewSummary();
                        break;
                    case PAGE_FOOTER:
                        xBand = xForm.addNewPageFooter();
                        break;
                    default:
                        throw new IllegalStateException("Unsupported band type");
                }
                get(0).appendTo(xBand, saveMode);
            } else {
                Report.Form.Bands xBands = xForm.addNewBands();
                xBands.setType(type);
                for (AdsReportBand band : this) {
                    band.appendTo(xBands.addNewBand(), saveMode);
                }
            }
        }
    }

    private Bands pageHeaderBand = new Bands(EReportBandType.PAGE_HEADER);
    private Bands titleBand = new Bands(EReportBandType.TITLE);
    private Bands columnHeaderBand = new Bands(EReportBandType.COLUMN_HEADER);
    private Bands detailBand = new Bands(EReportBandType.DETAIL);
    private Bands summaryBand = new Bands(EReportBandType.SUMMARY);
    private Bands pageFooterBand = new Bands(EReportBandType.PAGE_FOOTER);
    private final AdsReportGroups groups = new AdsReportGroups(this);
    private boolean columnsHeaderForEachGroupDisplayed = false;
    private boolean isNewStyle = false;

    public enum Mode {

        GRAPHICS,
        TEXT
    }
    //
    private final RadixEventSource<IChangeListener, ChangedEvent> changeSupport = new RadixEventSource<IChangeListener, ChangedEvent>();

    public static class Factory {

        private Factory() {
        }

        // for server
        public static AdsReportForm newInstance() {
            return new AdsReportForm();
        }
    }

    /**
     * Creates a new ReportForm.
     */
    protected AdsReportForm() {
    }

    @SuppressWarnings("LeakingThisInConstructor")
    protected AdsReportForm(AdsReportClassDef ownerReport) {
        setContainer(ownerReport);
        isNewStyle = true;
        setPageHeaderBandUsed(true);
        setTitleBandUsed(true);
        setColumnHeaderBandUsed(true);
        setDetailBandUsed(true);
        setSummaryBandUsed(true);
        setPageFooterBandUsed(true);
        AdsReportDefaultStyle.setDefaultFormStyle(this);
    }

    protected AdsReportForm(AdsReportClassDef ownerReport, org.radixware.schemas.adsdef.Report.Form xForm) {
        setContainer(ownerReport);
        doLoadFrom(xForm);
    }

    public void loadFrom(org.radixware.schemas.adsdef.Report.Form xForm) {
        doLoadFrom(xForm);
        setEditState(EEditState.MODIFIED);
    }

    private void doLoadFrom(org.radixware.schemas.adsdef.Report.Form xForm) {
        this.bgColor = XmlColor.parseColor(xForm.getBgColor());
        this.fgColor = XmlColor.parseColor(xForm.getFgColor());
        this.margin.loadFrom(xForm);
        this.pageWidthMm = xForm.getWidth();
        this.pageHeightMm = xForm.getHeight();

        if (xForm.isSetRepeatGroupHeadersOnNewPage()) {
            repeatGroupHeadersOnNewPage = xForm.getRepeatGroupHeadersOnNewPage();
        }
        if (xForm.isSetWidthCols()) {
            this.pageWidthColumns = xForm.getWidthCols();
        }
        if (xForm.isSetHeightRows()) {
            this.pageHeightRows = xForm.getHeightRows();
        }
        if (xForm.isSetMultifileGroupLevel()) {
            this.multifileGroupLevel = xForm.getMultifileGroupLevel();
        }
        if (xForm.isSetGridSize()){
            this.gridSizeMm = xForm.getGridSize();
        }
        if (xForm.isSetShowGrid()){
            this.showGrid = xForm.getShowGrid();
        }
        if (xForm.isSetSnapToGrid()){
            this.snapToGrid = xForm.getSnapToGrid();
        }

        columnsHeaderForEachGroupDisplayed = xForm.getColumnsHeadersInGroups();

        if (xForm.isSetTextModeAccepted()) {
            textModeAccepted = xForm.getTextModeAccepted();
        }

        Map<EReportBandType, Report.Form.Bands> xmlBandsByType = new HashMap<>();
        if (xForm.getBandsList() != null) {
            for (Report.Form.Bands xBand : xForm.getBandsList()) {
                xmlBandsByType.put(xBand.getType(), xBand);
            }
        }

        pageHeaderBand.clear();
        if (xForm.isSetPageHeader()) {
            AdsReportBand band = new AdsReportBand(xForm.getPageHeader());
            pageHeaderBand.load(band);
        } else {
            Report.Form.Bands xBand = xmlBandsByType.get(EReportBandType.PAGE_HEADER);
            if (xBand != null) {
                pageHeaderBand.loadFrom(xBand);
            }
        }

        titleBand.clear();
        if (xForm.isSetTitle()) {
            AdsReportBand band = new AdsReportBand(xForm.getTitle());
            titleBand.load(band);
        } else {
            Report.Form.Bands xBand = xmlBandsByType.get(EReportBandType.TITLE);
            if (xBand != null) {
                titleBand.loadFrom(xBand);
            }
        }

        columnHeaderBand.clear();
        if (xForm.isSetColumnsHeaders()) {
            AdsReportBand band = new AdsReportBand(xForm.getColumnsHeaders());
            columnHeaderBand.load(band);
        } else {
            Report.Form.Bands xBand = xmlBandsByType.get(EReportBandType.COLUMN_HEADER);
            if (xBand != null) {
                columnHeaderBand.loadFrom(xBand);
            }
        }
        detailBand.clear();
        if (xForm.isSetDetails()) {
            AdsReportBand band = new AdsReportBand(xForm.getDetails());
            detailBand.load(band);
        } else {
            Report.Form.Bands xBand = xmlBandsByType.get(EReportBandType.DETAIL);
            if (xBand != null) {
                detailBand.loadFrom(xBand);
            }
        }
        summaryBand.clear();
        if (xForm.isSetSummary()) {
            AdsReportBand band = new AdsReportBand(xForm.getSummary());
            summaryBand.load(band);
        } else {
            Report.Form.Bands xBand = xmlBandsByType.get(EReportBandType.SUMMARY);
            if (xBand != null) {
                summaryBand.loadFrom(xBand);
            }
        }
        pageFooterBand.clear();
        if (xForm.isSetPageFooter()) {
            AdsReportBand band = new AdsReportBand(xForm.getPageFooter());
            pageFooterBand.load(band);
        } else {
            Report.Form.Bands xBand = xmlBandsByType.get(EReportBandType.PAGE_FOOTER);
            if (xBand != null) {
                pageFooterBand.loadFrom(xBand);
            }
        }

        this.getGroups().clear();
        if (xForm.isSetGroups()) {
            final List<org.radixware.schemas.adsdef.Report.Form.Groups.Group> xGroups = xForm.getGroups().getGroupList();
            for (org.radixware.schemas.adsdef.Report.Form.Groups.Group xGroup : xGroups) {
                final AdsReportGroup group = new AdsReportGroup(xGroup);
                this.getGroups().add(group);
            }
        }
        fireEvent(new ChangedEvent(this, ChangedEvent.ChangeEventType.LOADING));
    }

    public void appendTo(org.radixware.schemas.adsdef.Report.Form xForm) {
        appendTo(xForm, ESaveMode.NORMAL);
    }

    public void appendTo(org.radixware.schemas.adsdef.Report.Form xForm, ESaveMode saveMode) {
        xForm.setBgColor(XmlColor.mergeColor(bgColor));
        xForm.setFgColor(XmlColor.mergeColor(fgColor));
        margin.appendTo(xForm, saveMode);
        xForm.setColumnsHeadersInGroups(columnsHeaderForEachGroupDisplayed);
        xForm.setWidth(pageWidthMm);
        xForm.setHeight(pageHeightMm);
        
        if (saveMode == ESaveMode.API) {
            //in API mode save only required attributes
            return;
        }
        
        xForm.setWidthCols(pageWidthColumns);
        xForm.setHeightRows(pageHeightRows);

        if (isPageHeaderBandUsed()) {
            pageHeaderBand.appendTo(xForm, saveMode);
        }

        if (textModeAccepted) {
            xForm.setTextModeAccepted(true);
        }

        if (isTitleBandUsed()) {
            titleBand.appendTo(xForm, saveMode);
        }

        if (isColumnHeaderBandUsed()) {
            columnHeaderBand.appendTo(xForm, saveMode);
        }

        if (isDetailBandUsed()) {
            detailBand.appendTo(xForm, saveMode);
        }

        if (isSummaryBandUsed()) {
            summaryBand.appendTo(xForm, saveMode);
        }

        if (isPageFooterBandUsed()) {
            pageFooterBand.appendTo(xForm, saveMode);
        }

        if (!groups.isEmpty()) {
            final org.radixware.schemas.adsdef.Report.Form.Groups xGroups = xForm.addNewGroups();
            for (AdsReportGroup group : groups) {
                final org.radixware.schemas.adsdef.Report.Form.Groups.Group xGroup = xGroups.addNewGroup();
                group.appendTo(xGroup, saveMode);
            }
        }
        if (multifileGroupLevel >= 0 && !groups.isEmpty()) {
            xForm.setMultifileGroupLevel(multifileGroupLevel);
        }
        if (repeatGroupHeadersOnNewPage) {
            xForm.setRepeatGroupHeadersOnNewPage(true);
        }
        
        if (!showGrid){
            xForm.setShowGrid(showGrid);
        }
        
        if (!snapToGrid){
            xForm.setSnapToGrid(snapToGrid);
        }
        
        if (gridSizeMm != DEFAULT_GRID_SIZE_MM && gridSizeMm > 0){
            xForm.setGridSize(gridSizeMm);
        }
    }

    public int getMultifileGroupLevel() {
        return multifileGroupLevel;
    }

    public void setMultifileGroupLevel(int multifileGroupLevel) {
        if (this.multifileGroupLevel != multifileGroupLevel) {
            if (multifileGroupLevel >= 0) {
                if (!groups.isEmpty()) {
                    this.multifileGroupLevel = multifileGroupLevel;
                    setEditState(EEditState.MODIFIED);
                }
            } else {
                this.multifileGroupLevel = -1;
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    @Override
    public String getName() {
        return "Form";
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        if (this.mode != mode){
            this.mode = mode;
            fireEvent(new ChangedEvent(this, ChangedEvent.ChangeEventType.MODE));
        }
    }

    @Override
    public boolean setName(String name) {
        throw new IllegalStateException("Attempt to set report form name");
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        assert (bgColor != null);
        if (!Utils.equals(this.bgColor, bgColor)) {
            this.bgColor = bgColor;
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

    public final class Margin extends AdsReportMarginMm{
        public static final double  DEFAULT_MARGIN = 10.0;
        /**
         * Creates a new Margin.
         */
        protected Margin() {
            super(AdsReportForm.this, DEFAULT_MARGIN);
        }

        protected Margin(org.radixware.schemas.adsdef.Report.Form xForm) {
            loadFrom(xForm);
        }

        protected void loadFrom(org.radixware.schemas.adsdef.Report.Form xForm) {
            if (xForm.isSetTopMargin()) {
                setTopMm(xForm.getTopMargin(), false);
            } else {
                setTopMm(DEFAULT_MARGIN, false);
            }
            if (xForm.isSetLeftMargin()) {
                setLeftMm(xForm.getLeftMargin(), false);
            } else {
                setLeftMm(DEFAULT_MARGIN, false);
            }
            if (xForm.isSetRightMargin()) {
                setRightMm(xForm.getRightMargin(), false);
            } else {
                setRightMm(DEFAULT_MARGIN, false);
            }
            if (xForm.isSetBottomMargin()) {
                setBottomMm(xForm.getBottomMargin(), false);
            } else {
                setBottomMm(DEFAULT_MARGIN, false);
            }
        }
        
        protected void appendTo(org.radixware.schemas.adsdef.Report.Form xForm, ESaveMode saveMode) {
            xForm.setTopMargin(getTopMm());
            xForm.setBottomMargin(getBottomMm());
            xForm.setLeftMargin(getLeftMm());
            xForm.setRightMargin(getRightMm());
        }
    }
    
    public final class MarginTxt extends AdsReportMarginTxt{
        public static final int  DEFAULT_MARGIN = 0;
        /**
         * Creates a new Margin.
         */
        protected MarginTxt() {
            super(AdsReportForm.this, DEFAULT_MARGIN);
        }

        protected MarginTxt(org.radixware.schemas.adsdef.Report.Form xForm) {
            loadFrom(xForm);
        }

        protected void loadFrom(org.radixware.schemas.adsdef.Report.Form xForm) {
            if (xForm.isSetTopMarginRows()) {
                setTopRows(xForm.getTopMarginRows(), false);
            } else {
                setTopRows(DEFAULT_MARGIN, false);
            }
            if (xForm.isSetLeftMarginCols()) {
                setLeftCols(xForm.getLeftMarginCols(), false);
            } else {
                setLeftCols(DEFAULT_MARGIN, false);
            }
            if (xForm.isSetRightMargin()) {
                setRightCols(xForm.getRightMarginCols(), false);
            } else {
                setRightCols(DEFAULT_MARGIN, false);
            }
            if (xForm.isSetBottomMargin()) {
                setBottomRows(xForm.getRightMarginCols(), false);
            } else {
                setBottomRows(DEFAULT_MARGIN, false);
            }
        }

        protected void appendTo(org.radixware.schemas.adsdef.Report.Form xForm, ESaveMode saveMode) {
            xForm.setTopMarginRows(getTopRows());
            xForm.setBottomMarginRows(getBottomRows());
            xForm.setLeftMarginCols(getLeftCols());
            xForm.setRightMarginCols(getRightCols());
        }
    }

    /**
     * @return margin from page sides.
     */
    public Margin getMargin() {
        return margin;
    }
    
    public MarginTxt getMarginTxt() {
        return marginTxt;
    }

    /**
     * @return page width in millimeters.
     */
    public int getPageWidthMm() {
        return pageWidthMm;
    }

    public void setPageWidthMm(int widthMm) {
        if (this.pageWidthMm != widthMm) {
            this.pageWidthMm = widthMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return page height in millimeters.
     */
    public int getPageHeightMm() {
        return pageHeightMm;
    }

    public void setPageHeightMm(int heightMm) {
        if (this.pageHeightMm != heightMm) {
            this.pageHeightMm = heightMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getPageWidthCols() {
        return pageWidthColumns;
    }

    public void setPageWidthCols(int widthMm) {
        if (this.pageWidthColumns != widthMm) {
            this.pageWidthColumns = widthMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return page height in millimeters.
     */
    public int getPageHeightRows() {
        return pageHeightRows;
    }

    public void setPageHeightRows(int heightMm) {
        if (this.pageHeightRows != heightMm) {
            this.pageHeightRows = heightMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Get page header band, displayed on each page on top.
     *
     * @return page header band or null if not used.
     */
    public Bands getPageHeaderBands() {
        return pageHeaderBand;
    }

    /**
     * Get title band, displayed one time.
     *
     * @return title band or null if not used.
     */
    public Bands getTitleBands() {
        return titleBand;
    }

    /**
     * Get columns headers band, displayed before details.
     *
     * @return column header band or null if not used.
     */
    public Bands getColumnHeaderBands() {
        return columnHeaderBand;
    }

    /**
     * Get details band, displayed for each record.
     *
     * @return details band or null if not used.
     */
    public Bands getDetailBands() {
        return detailBand;
    }

    /**
     * Get summary band, displayed after details.
     *
     * @return summary band or null if not used.
     */
    public Bands getSummaryBands() {
        return summaryBand;
    }

    /**
     * Get page footer band, displayed on each page on bottom.
     *
     * @return page footer band or null if not used.
     */
    public Bands getPageFooterBands() {
        return pageFooterBand;
    }

//    /**
//     * Get page header band, displayed on each page on top.
//     *
//     * @return page header band or null if not used.
//     */
//    public AdsReportBand getPageHeaderBand() {
//        return pageHeaderBand;
//    }
//
//    /**
//     * Get title band, displayed one time.
//     *
//     * @return title band or null if not used.
//     */
//    public AdsReportBand getTitleBand() {
//        return titleBand;
//    }
//
//    /**
//     * Get columns headers band, displayed before details.
//     *
//     * @return column header band or null if not used.
//     */
//    public AdsReportBand getColumnHeaderBand() {
//        return columnHeaderBand;
//    }
//
//    /**
//     * Get details band, displayed for each record.
//     *
//     * @return details band or null if not used.
//     */
//    public AdsReportBand getDetailBand() {
//        return detailBand;
//    }
//
//    /**
//     * Get summary band, displayed after details.
//     *
//     * @return summary band or null if not used.
//     */
//    public AdsReportBand getSummaryBand() {
//        return summaryBand;
//    }
//
//    /**
//     * Get page footer band, displayed on each page on bottom.
//     *
//     * @return page footer band or null if not used.
//     */
//    public AdsReportBand getPageFooterBand() {
//        return pageFooterBand;
//    }
    /**
     * @return true if header band displayed, false otherwise.
     */
    public boolean isPageHeaderBandUsed() {
        return !pageHeaderBand.isEmpty();
    }
    
    public void setPageHeaderBandUsed(boolean used) {
        if (used) {
            if (this.pageHeaderBand.isEmpty()) {
                AdsReportBand band = isNewStyle? AdsReportBand.createNewStyleBand() : new AdsReportBand();
                this.pageHeaderBand.add(band);
            }
        } else {
            this.pageHeaderBand.clear();
        }
    }

    /**
     * Set or remove page header band.
     */
    @Deprecated
    public void setPageHeaderBand(AdsReportBand band) {
        pageHeaderBand.clear();
        pageHeaderBand.add(band);
    }

    /**
     * @return true if title band displayed, false otherwise.
     */
    public boolean isTitleBandUsed() {
        return !titleBand.isEmpty();
    }

    /**
     * Create or remove title band.
     */
    public void setTitleBandUsed(boolean used) {
        if (used) {
            if (this.titleBand.isEmpty()) {
                AdsReportBand band = isNewStyle? AdsReportBand.createNewStyleBand() : new AdsReportBand();
                this.titleBand.add(band);
            }
        } else {
            this.titleBand.clear();
        }
    }

    /**
     * Set or remove title band.
     */
    @Deprecated
    public void setTitleBand(AdsReportBand band) {
        titleBand.clear();
        titleBand.add(band);
    }

    /**
     * @return true if columns headers band displayed, false otherwise.
     */
    public boolean isColumnHeaderBandUsed() {
        return !columnHeaderBand.isEmpty();
    }

    /**
     * Create or remove column header band.
     */
    public void setColumnHeaderBandUsed(boolean used) {
        if (used) {
            if (this.columnHeaderBand.isEmpty()) {
                AdsReportBand band = isNewStyle? AdsReportBand.createNewStyleBand() : new AdsReportBand();
                this.columnHeaderBand.add(band);
            }
        } else {
            this.columnHeaderBand.clear();
        }
    }

    /**
     * Set or remove column header band.
     */
    @Deprecated
    public void setColumnHeaderBand(AdsReportBand band) {
        columnHeaderBand.clear();
        columnHeaderBand.add(band);
    }

    /**
     * @return true if detail band displayed, false otherwise.
     */
    public boolean isDetailBandUsed() {
        return !detailBand.isEmpty();
    }

    /**
     * Create or remove detail band.
     */
    public void setDetailBandUsed(boolean used) {
        if (used) {
            if (this.detailBand.isEmpty()) {
                AdsReportBand band = isNewStyle? AdsReportBand.createNewStyleBand() : new AdsReportBand();
                this.detailBand.add(band);
            }
        } else {
            this.detailBand.clear();
        }
    }

    /**
     * Set or remove detail band.
     */
    @Deprecated
    public void setDetailBand(AdsReportBand band) {
        detailBand.clear();
        detailBand.add(band);
    }

    /**
     * @return true if summary band displayed, false otherwise.
     */
    public boolean isSummaryBandUsed() {
        return !summaryBand.isEmpty();
    }

    /**
     * Create or remove summary band.
     */
    public void setSummaryBandUsed(boolean used) {
        if (used) {
            if (this.summaryBand.isEmpty()) {
                AdsReportBand band = isNewStyle? AdsReportBand.createNewStyleBand() : new AdsReportBand();
                this.summaryBand.add(band);
            }
        } else {
            this.summaryBand.clear();
        }
    }

    /**
     * Set or remove summary band.
     */
    @Deprecated
    public void setSummaryBand(AdsReportBand band) {
        summaryBand.clear();
        summaryBand.add(band);
    }

    /**
     * @return true if page footer band displayed, false otherwise.
     */
    public boolean isPageFooterBandUsed() {
        return !pageFooterBand.isEmpty();
    }

    /**
     * Create or remove page footer band.
     */
    public void setPageFooterBandUsed(boolean used) {
        if (used) {
            if (this.pageFooterBand.isEmpty()) {
                AdsReportBand band = isNewStyle? AdsReportBand.createNewStyleBand() : new AdsReportBand();
                this.pageFooterBand.add(band);
            }
        } else {
            this.pageFooterBand.clear();
        }
    }

    /**
     * Set or remove page footer band.
     */
    @Deprecated
    public void setPageFooterBand(AdsReportBand band) {
        pageFooterBand.clear();
        pageFooterBand.add(band);
    }

    /**
     * @return report form groups.
     */
    public AdsReportGroups getGroups() {
        return groups;
    }

    public boolean isColumnsHeaderForEachGroupDisplayed() {
        return columnsHeaderForEachGroupDisplayed;
    }

    public void setColumnsHeaderForEachGroupDisplayed(boolean columnsHeaderForEachGroupDisplayed) {
        if (this.columnsHeaderForEachGroupDisplayed != columnsHeaderForEachGroupDisplayed) {
            this.columnsHeaderForEachGroupDisplayed = columnsHeaderForEachGroupDisplayed;
            setEditState(EEditState.MODIFIED);
        }
    }

    public AdsReportClassDef getOwnerReport() {
        return (AdsReportClassDef) getContainer();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);

        if (pageHeaderBand != null) {
            pageHeaderBand.visit(visitor, provider);
        }
        if (titleBand != null) {
            titleBand.visit(visitor, provider);
        }
        if (columnHeaderBand != null) {
            columnHeaderBand.visit(visitor, provider);
        }
        if (detailBand != null) {
            detailBand.visit(visitor, provider);
        }

        groups.visit(visitor, provider);

        if (summaryBand != null) {
            summaryBand.visit(visitor, provider);
        }
        if (pageFooterBand != null) {
            pageFooterBand.visit(visitor, provider);
        }
    }

    /**
     * @return list of displayed bands in order.
     */
    public List<AdsReportBand> getBands() {
        final List<AdsReportBand> bands = new ArrayList<AdsReportBand>();

        bands.addAll(pageHeaderBand.list());

        bands.addAll(titleBand.list());

        for (AdsReportGroup group : groups) {
            if (group.isHeaderBandUsed()) {
                bands.add(group.getHeaderBand());
            }
        }

        bands.addAll(columnHeaderBand.list());

        bands.addAll(detailBand.list());

        for (int i = groups.size() - 1; i >= 0; i--) {
            final AdsReportGroup group = groups.get(i);
            if (group.isFooterBandUsed()) {
                bands.add(group.getFooterBand());
            }
        }

        bands.addAll(summaryBand.list());

        bands.addAll(pageFooterBand.list());
        return bands;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_FORM;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    public boolean isRepeatGroupHeadersOnNewPage() {
        return repeatGroupHeadersOnNewPage;
    }
    
    public void setRepeatGroupHeadersOnNewPage(boolean repeatGroupHeadersOnNewPage) {
        if (this.repeatGroupHeadersOnNewPage != repeatGroupHeadersOnNewPage) {
            this.repeatGroupHeadersOnNewPage = repeatGroupHeadersOnNewPage;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setTextModeAccepted(boolean accepted) {
        if (textModeAccepted != accepted) {
            textModeAccepted = accepted;
            convertToTextMode();
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public void convertToTextMode() {
        if (textModeAccepted) {
            if (isReadOnly()) {
                return;
            }
            for (AdsReportBand band : getBands()) {
                band.convertToTextMode();
            }
            setEditState(EEditState.MODIFIED);
        } else {
            if (getMode() == Mode.TEXT) {
                setMode(Mode.GRAPHICS);
            }
        }
    }

    public boolean isSupportsTxt() {
        return textModeAccepted;
    }

    @Override
    protected void onModified() {
        final ChangedEvent changedEvent = new ChangedEvent(this);
        changeSupport.fireEvent(changedEvent);
    }

    public static class ChangedEvent extends RadixEvent {
        public enum ChangeEventType {
            ADD, REMOVE, NONE, MODE, LOADING
        }
        
        public final RadixObject radixObject;
        public final ChangeEventType type;

        protected ChangedEvent(RadixObject radixObject) {
            this(radixObject, ChangeEventType.NONE);
        }

        public ChangedEvent(RadixObject radixObject, ChangeEventType type) {
            this.radixObject = radixObject;
            this.type = type;
        }
    }

    public interface IChangeListener extends IRadixEventListener<ChangedEvent> {
    }

    public void addChangeListener(IChangeListener l) {
        changeSupport.addEventListener(l);
    }

    public void removeChangeListener(IChangeListener l) {
        changeSupport.removeEventListener(l);
    }
    
    void fireEvent(ChangedEvent changedEvent){
       changeSupport.fireEvent(changedEvent);
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        if (this.showGrid != showGrid) {
            this.showGrid = showGrid;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isSnapToGrid() {
        return snapToGrid;
    }

    public void setSnapToGrid(boolean snapToGrid) {
        if (this.snapToGrid != snapToGrid) {
            this.snapToGrid = snapToGrid;
            setEditState(EEditState.MODIFIED);
        }
    }

    public double getGridSizeMm() {
        return gridSizeMm;
    }

    public void setGridSizeMm(double gridSizeMm) {
        if (this.gridSizeMm != gridSizeMm){
            this.gridSizeMm = gridSizeMm;
            setEditState(EEditState.MODIFIED);
        }
    }

     @Override
    public RadixObject getParent() {
        return null;
    }

    @Override
    public List getChildren() {
        return getBands();
    }

    @Override
    public boolean isDiagramModeSupported(Mode mode) {
        return true;
    }
}
