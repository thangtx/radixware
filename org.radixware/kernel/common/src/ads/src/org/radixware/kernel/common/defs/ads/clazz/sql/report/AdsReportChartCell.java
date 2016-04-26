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

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportChartAxisType;
import org.radixware.kernel.common.enums.EReportChartCellType;
import org.radixware.kernel.common.enums.EReportChartLegendPosition;
import org.radixware.kernel.common.enums.EReportChartSeriesType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.MinPlotSize;

public class AdsReportChartCell extends AdsReportCell implements ILocalizedDef {

    private final ChartSeries series;
    private final AdsReportChartAxes domainAxes;
    private final AdsReportChartAxes rangeAxes;
    private EReportChartCellType chartType = EReportChartCellType.CATEGORY;
    private EReportChartLegendPosition legendPosition = EReportChartLegendPosition.BOTTOM;
    private Id titleId = null;
    private boolean x_AxisGrid = true;
    private boolean y_AxisGrid = true;
    private boolean horizontalOrientation = false;
    private boolean autoAxisSpace = true;
    private int leftAxisSpacePx = 0, topAxisSpacePx = 0, rightAxisSpacePx = 0, bottomAxisSpacePx = 0;
    private boolean titleFontInherited = true, axesFontInherited = true, legendFontInherited = true;
    private final Font titleFont, axesFont, legendFont;
    private double foregroundAlpha = 1;
    private double minPlotWidthMm = 100.0, minPlotHeightMm = 100.0, adjustWidthMm = 0.0, adjustHeightMm = 0.0;
    private boolean plotMinSizeEnable = false;

    protected AdsReportChartCell() {
        super();
        this.titleFont = new Font();
        this.axesFont = new Font();
        this.legendFont = new Font();
        series = new ChartSeries();
        domainAxes = new AdsReportChartAxes();
        rangeAxes = new AdsReportChartAxes();
        setName("Chart");
    }

    protected AdsReportChartCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);

        series = new ChartSeries();
        domainAxes = new AdsReportChartAxes();
        rangeAxes = new AdsReportChartAxes();
        if (!xCell.isSetName()) {
            setName("Chart");
        }
        if (xCell.getChartSeriesList() != null && !xCell.getChartSeriesList().isEmpty()) {
            for (org.radixware.schemas.adsdef.ChartSeries xSeries : xCell.getChartSeriesList()) {
                AdsReportChartSeries chartSeries = new AdsReportChartSeries(this, xSeries);
                series.add(chartSeries);
            }
        }
        if (xCell.getChartDomainAxisList() != null && !xCell.getChartDomainAxisList().isEmpty()) {
            for (org.radixware.schemas.adsdef.ChartAxis xAxis : xCell.getChartDomainAxisList()) {
                AdsReportChartAxis chartAxis = new AdsReportChartAxis(this, EReportChartAxisType.DOMAIN_AXIS, xAxis);
                domainAxes.add(chartAxis);
            }
        }
        if (xCell.getChartRangeAxisList() != null && !xCell.getChartRangeAxisList().isEmpty()) {
            for (org.radixware.schemas.adsdef.ChartAxis xAxis : xCell.getChartRangeAxisList()) {
                AdsReportChartAxis chartAxis = new AdsReportChartAxis(this, EReportChartAxisType.RANGE_AXIS, xAxis);
                rangeAxes.add(chartAxis);
            }
        }
        if (xCell.isSetChartType()) {
            chartType = xCell.getChartType();
        }
        if (xCell.isSetChartLegendPosition()) {
            legendPosition = xCell.getChartLegendPosition();
        }
        if (xCell.isSetTextId()) {
            this.titleId = xCell.getTextId();
        }
        if (xCell.isSetXAxisGrid()) {
            this.x_AxisGrid = xCell.getXAxisGrid();
        }
        if (xCell.isSetYAxisGrid()) {
            this.y_AxisGrid = xCell.getYAxisGrid();
        }
        if (xCell.isSetHorizontalOrientation()) {
            this.horizontalOrientation = xCell.getHorizontalOrientation();
        }

        if (xCell.isSetAutoAxisSpace()) {
            this.autoAxisSpace = xCell.getAutoAxisSpace();
        }
        if (xCell.isSetBottomAxisSpace()) {
            this.bottomAxisSpacePx = xCell.getBottomAxisSpace();
        }
        if (xCell.isSetLeftAxisSpace()) {
            this.leftAxisSpacePx = xCell.getLeftAxisSpace();
        }
        if (xCell.isSetRightAxisSpace()) {
            this.rightAxisSpacePx = xCell.getRightAxisSpace();
        }
        if (xCell.isSetTopAxisSpace()) {
            this.topAxisSpacePx = xCell.getTopAxisSpace();
        }
        //set font
        titleFontInherited = !xCell.isSetChartTitleFont();
        if (xCell.isSetChartTitleFont()) {
            this.titleFont = new Font(xCell.getChartTitleFont());
        } else {
            this.titleFont = new Font();
        }
        axesFontInherited = !xCell.isSetChartAxisFont();
        if (xCell.isSetChartAxisFont()) {
            this.axesFont = new Font(xCell.getChartAxisFont());
        } else {
            this.axesFont = new Font();
        }
        legendFontInherited = !xCell.isSetChartLegendFont();
        if (xCell.isSetChartLegendFont()) {
            this.legendFont = new Font(xCell.getChartLegendFont());
        } else {
            this.legendFont = new Font();
        }
        if (xCell.isSetForegroundAlpha()) {
            this.foregroundAlpha = xCell.getForegroundAlpha();
        }
        plotMinSizeEnable = xCell.isSetMinPlotSize();
        if (plotMinSizeEnable) {
            this.minPlotHeightMm = xCell.getMinPlotSize().getMinPlotHeight();
            this.minPlotWidthMm = xCell.getMinPlotSize().getMinPlotWidth();
        }
    }

    protected AdsReportChartCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);

        series = new ChartSeries();
        domainAxes = new AdsReportChartAxes();
        rangeAxes = new AdsReportChartAxes();
        if (!xCell.isSetName()) {
            setName("Chart");
        }
        if (xCell.getChartSeriesList() != null && !xCell.getChartSeriesList().isEmpty()) {
            for (org.radixware.schemas.adsdef.ChartSeries xSeries : xCell.getChartSeriesList()) {
                AdsReportChartSeries chartSeries = new AdsReportChartSeries(this, xSeries);
                series.add(chartSeries);
            }
        }
        if (xCell.getChartDomainAxisList() != null && !xCell.getChartDomainAxisList().isEmpty()) {
            for (org.radixware.schemas.adsdef.ChartAxis xAxis : xCell.getChartDomainAxisList()) {
                AdsReportChartAxis chartAxis = new AdsReportChartAxis(this, EReportChartAxisType.DOMAIN_AXIS, xAxis);
                domainAxes.add(chartAxis);
            }
        }
        if (xCell.getChartRangeAxisList() != null && !xCell.getChartRangeAxisList().isEmpty()) {
            for (org.radixware.schemas.adsdef.ChartAxis xAxis : xCell.getChartRangeAxisList()) {
                AdsReportChartAxis chartAxis = new AdsReportChartAxis(this, EReportChartAxisType.RANGE_AXIS, xAxis);
                rangeAxes.add(chartAxis);
            }
        }
        if (xCell.isSetChartType()) {
            chartType = xCell.getChartType();
        }
        if (xCell.isSetChartLegendPosition()) {
            legendPosition = xCell.getChartLegendPosition();
        }
        if (xCell.isSetTextId()) {
            this.titleId = xCell.getTextId();
        }
        if (xCell.isSetXAxisGrid()) {
            this.x_AxisGrid = xCell.getXAxisGrid();
        }
        if (xCell.isSetYAxisGrid()) {
            this.y_AxisGrid = xCell.getYAxisGrid();
        }
        if (xCell.isSetHorizontalOrientation()) {
            this.horizontalOrientation = xCell.getHorizontalOrientation();
        }

        if (xCell.isSetAutoAxisSpace()) {
            this.autoAxisSpace = xCell.getAutoAxisSpace();
        }
        if (xCell.isSetBottomAxisSpace()) {
            this.bottomAxisSpacePx = xCell.getBottomAxisSpace();
        }
        if (xCell.isSetLeftAxisSpace()) {
            this.leftAxisSpacePx = xCell.getLeftAxisSpace();
        }
        if (xCell.isSetRightAxisSpace()) {
            this.rightAxisSpacePx = xCell.getRightAxisSpace();
        }
        if (xCell.isSetTopAxisSpace()) {
            this.topAxisSpacePx = xCell.getTopAxisSpace();
        }
        //set font
        titleFontInherited = !xCell.isSetChartTitleFont();
        if (xCell.isSetChartTitleFont()) {
            this.titleFont = new Font(xCell.getChartTitleFont());
        } else {
            this.titleFont = new Font();
        }
        axesFontInherited = !xCell.isSetChartAxisFont();
        if (xCell.isSetChartAxisFont()) {
            this.axesFont = new Font(xCell.getChartAxisFont());
        } else {
            this.axesFont = new Font();
        }
        legendFontInherited = !xCell.isSetChartLegendFont();
        if (xCell.isSetChartLegendFont()) {
            this.legendFont = new Font(xCell.getChartLegendFont());
        } else {
            this.legendFont = new Font();
        }
        if (xCell.isSetForegroundAlpha()) {
            this.foregroundAlpha = xCell.getForegroundAlpha();
        }
        plotMinSizeEnable = xCell.isSetMinPlotSize();
        if (plotMinSizeEnable) {
            this.minPlotHeightMm = xCell.getMinPlotSize().getMinPlotHeight();
            this.minPlotWidthMm = xCell.getMinPlotSize().getMinPlotWidth();
        }
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.CHART;
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);
        if (series != null) {
            series.appendTo(xCell);
        }
        if (domainAxes != null) {
            domainAxes.appendTo(xCell);
        }
        if (rangeAxes != null) {
            rangeAxes.appendTo(xCell);
        }
        if (titleId != null) {
            xCell.setTextId(titleId);
        }
        xCell.setChartType(chartType);
        xCell.setChartLegendPosition(legendPosition);
        xCell.setXAxisGrid(x_AxisGrid);
        xCell.setYAxisGrid(y_AxisGrid);
        xCell.setHorizontalOrientation(horizontalOrientation);

        xCell.setAutoAxisSpace(autoAxisSpace);
        xCell.setBottomAxisSpace(bottomAxisSpacePx);
        xCell.setLeftAxisSpace(leftAxisSpacePx);
        xCell.setRightAxisSpace(rightAxisSpacePx);
        xCell.setTopAxisSpace(topAxisSpacePx);

        if (!isTitleFontInherited()) {
            titleFont.appendTo(xCell.addNewChartTitleFont(), saveMode);
        }
        if (!isAxesFontInherited()) {
            axesFont.appendTo(xCell.addNewChartAxisFont(), saveMode);
        }
        if (!isLegendFontInherited()) {
            legendFont.appendTo(xCell.addNewChartLegendFont(), saveMode);
        }
        if (foregroundAlpha < 1) {
            xCell.setForegroundAlpha(foregroundAlpha);
        }
        if (plotMinSizeEnable) {
            MinPlotSize xMinPlotSize = xCell.addNewMinPlotSize();
            xMinPlotSize.setMinPlotHeight(minPlotHeightMm);
            xMinPlotSize.setMinPlotWidth(minPlotWidthMm);
        }
    }

    public Id getTitleId() {
        return titleId;
    }

    public void setTitleId(Id titleId) {
        if (!Utils.equals(this.titleId, titleId)) {
            this.titleId = titleId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public double getForegroundAlpha() {
        return foregroundAlpha;
    }

    public void setForegroundAlpha(double foregroundAlpha) {
        if (!Utils.equals(this.foregroundAlpha, foregroundAlpha)) {
            this.foregroundAlpha = foregroundAlpha;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isXAxisGridVisible() {
        return x_AxisGrid;
    }

    public void setXAxisGridVisible(boolean visible) {
        if (!Utils.equals(this.x_AxisGrid, visible)) {
            this.x_AxisGrid = visible;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isYAxisGridVisible() {
        return y_AxisGrid;
    }

    public void setYAxisGridVisible(boolean visible) {
        if (!Utils.equals(this.y_AxisGrid, visible)) {
            this.y_AxisGrid = visible;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isHorizontalOrientation() {
        return horizontalOrientation;
    }

    public void setIsHorizontalOrientation(boolean isHorizontalOrientation) {
        if (!Utils.equals(this.horizontalOrientation, isHorizontalOrientation)) {
            this.horizontalOrientation = isHorizontalOrientation;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isAutoAxisSpace() {
        return autoAxisSpace;
    }

    public void setAutoAxisSpace(boolean autoAxisSpace) {
        if (!Utils.equals(this.autoAxisSpace, autoAxisSpace)) {
            this.autoAxisSpace = autoAxisSpace;
            if (autoAxisSpace) {
                bottomAxisSpacePx = 0;
                leftAxisSpacePx = 0;
                topAxisSpacePx = 0;
                rightAxisSpacePx = 0;
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getBottomAxisSpacePx() {
        return bottomAxisSpacePx;
    }

    public void setBottomAxisSpacePx(int bottomAxisSpacePx) {
        if (!Utils.equals(this.bottomAxisSpacePx, bottomAxisSpacePx)) {
            this.bottomAxisSpacePx = bottomAxisSpacePx;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getLeftAxisSpacePx() {
        return leftAxisSpacePx;
    }

    public void setLeftAxisSpacePx(int leftAxisSpacePx) {
        if (!Utils.equals(this.leftAxisSpacePx, leftAxisSpacePx)) {
            this.leftAxisSpacePx = leftAxisSpacePx;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getTopAxisSpacePx() {
        return topAxisSpacePx;
    }

    public void setTopAxisSpacePx(int topAxisSpacePx) {
        if (!Utils.equals(this.topAxisSpacePx, topAxisSpacePx)) {
            this.topAxisSpacePx = topAxisSpacePx;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getRightAxisSpacePx() {
        return rightAxisSpacePx;
    }

    public void setRightAxisSpacePx(int rightAxisSpacePx) {
        if (!Utils.equals(this.rightAxisSpacePx, rightAxisSpacePx)) {
            this.rightAxisSpacePx = rightAxisSpacePx;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_CHART_CELL;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (series != null && !series.isEmpty()) {
            series.collectDependences(list);
        }
        if (domainAxes != null && !domainAxes.isEmpty()) {
            domainAxes.collectDependences(list);
        }
        if (rangeAxes != null && !rangeAxes.isEmpty()) {
            rangeAxes.collectDependences(list);
        }
        final AdsMultilingualStringDef title = findTitle();
        if (title != null) {
            list.add(title);
        }
    }

    public AdsReportChartAxes getDomainAxes() {
        return domainAxes;
    }

    public AdsReportChartAxes getRangeAxes() {
        return rangeAxes;
    }

    public ChartSeries getChartSeries() {
        return series;
    }

    public EReportChartCellType getChartType() {
        return chartType;
    }

    public void setChartType(EReportChartCellType chartType) {
        this.chartType = chartType;
    }

    public double getAdjustHeightMm() {
        return adjustHeightMm;
    }

    public void setAdjustHeightMm(double adjustHeightMm) {
        this.adjustHeightMm = adjustHeightMm;
    }

    public double getAdjustWidthMm() {
        return adjustWidthMm;
    }

    public void setAdjustWidthMm(double adjustWidthMm) {
        this.adjustWidthMm = adjustWidthMm;
    }

    public EReportChartLegendPosition getLegendPosition() {
        return legendPosition;
    }

    public void setLegendPosition(EReportChartLegendPosition legendPosition) {
        if (!Utils.equals(this.legendPosition, legendPosition)) {
            this.legendPosition = legendPosition;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return title chart font information or cell font information if
     * inherited.
     */
    public Font getTitleFont() {
        if (isTitleFontInherited()) {
            return getFont();
        }
        return titleFont;
    }

    /**
     * @return axes chart font information or cell font information if
     * inherited.
     */
    public Font getAxesFont() {
        if (isAxesFontInherited()) {
            return getFont();
        }
        return axesFont;
    }

    /**
     * @return axes chart font information or cell font information if
     * inherited.
     */
    public Font getLegendFont() {
        if (isLegendFontInherited()) {
            return getFont();
        }
        return legendFont;
    }

    /**
     * @return true if title font inherited from cell, false otherwise
     */
    public boolean isTitleFontInherited() {
        return titleFontInherited;
    }

    public boolean getTitleFontInherited() { // for onAdding
        return titleFontInherited;
    }

    public void setTitleFontInherited(boolean fontInherited) {
        if (this.titleFontInherited != fontInherited) {
            this.titleFontInherited = fontInherited;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if axes font inherited from cell, false otherwise
     */
    public boolean isAxesFontInherited() {
        return axesFontInherited;
    }

    public boolean getAxesFontInherited() { // for onAdding
        return axesFontInherited;
    }

    public void setAxesFontInherited(boolean fontInherited) {
        if (this.axesFontInherited != fontInherited) {
            this.axesFontInherited = fontInherited;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if legend font inherited from band, false otherwise
     */
    public boolean isLegendFontInherited() {
        return legendFontInherited;
    }

    public boolean getLegendFontInherited() { // for onAdding
        return legendFontInherited;
    }

    public void setLegendFontInherited(boolean fontInherited) {
        if (this.legendFontInherited != fontInherited) {
            this.legendFontInherited = fontInherited;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if plot has min size
     */
    public boolean isPlotMinSizeEnable() {
        return plotMinSizeEnable;
    }

    public boolean getPlotMinSizeEnable() { // for onAdding
        return plotMinSizeEnable;
    }

    public void setPlotMinSizeEnable(boolean plotMinSizeEnable) {
        if (this.plotMinSizeEnable != plotMinSizeEnable) {
            this.plotMinSizeEnable = plotMinSizeEnable;
            setEditState(EEditState.MODIFIED);
        }
    }

    public double getMinPlotHeightMm() {
        return minPlotHeightMm;
    }

    public void setMinPlotHeightMm(double minPlotHeightMm) {
        if (!Utils.equals(this.minPlotHeightMm, minPlotHeightMm)) {
            this.minPlotHeightMm = minPlotHeightMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    public double getMinPlotWidthMm() {
        return minPlotWidthMm;
    }

    public void setMinPlotWidthMm(double minPlotWidthMm) {
        if (!Utils.equals(this.minPlotWidthMm, minPlotWidthMm)) {
            this.minPlotWidthMm = minPlotWidthMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        series.visit(visitor, provider);
        rangeAxes.visit(visitor, provider);
        domainAxes.visit(visitor, provider);
    }

    @Override
    public boolean isModeSupported(AdsReportForm.Mode mode) {
        return mode == AdsReportForm.Mode.GRAPHICS;
    }

    public AdsMultilingualStringDef findTitle() {
        return findLocalizedString(titleId);
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        if (stringId != null) {
            final AdsReportClassDef report = getOwnerReport();
            if (report != null) {
                return report.findLocalizedString(stringId);
            }
        }
        return null;
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(AdsReportChartCell.this) {
            @Override
            public Id getId() {
                return titleId;
            }

            @Override
            public void updateId(Id newId) {
                titleId = newId;
                setEditState(EEditState.MODIFIED);
            }

            @Override
            public EAccess getAccess() {
                return EAccess.PRIVATE;
            }

            @Override
            public String getContextDescription() {
                return "Report Chart Series";
            }

            @Override
            public boolean isPublished() {
                return false;
            }
        });
    }

    public static class ChartSeries extends RadixObjects<AdsReportChartSeries> {

        public boolean is3D() {
            if (!isEmpty()) {
                AdsReportChartSeries s = this.get(0);
                return s.getSeriesType() == EReportChartSeriesType.BAR_3D
                        || s.getSeriesType() == EReportChartSeriesType.LINE_3D
                        || s.getSeriesType() == EReportChartSeriesType.BAR_STACKED_3D
                        || s.getSeriesType() == EReportChartSeriesType.PIE_3D;
            }
            return true;
        }

        void appendTo(org.radixware.schemas.adsdef.ReportCell xCell) {
            if (!isEmpty()) {
                for (AdsReportChartSeries s : this) {
                    org.radixware.schemas.adsdef.ChartSeries xSeries = xCell.addNewChartSeries();
                    s.appendTo(xSeries);
                }
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            if (!isEmpty()) {
                for (AdsReportChartSeries s : this) {
                    s.collectDependences(list);
                }
            }
        }
    }

    public static class AdsReportChartAxes extends RadixObjects<AdsReportChartAxis> {

        void appendTo(org.radixware.schemas.adsdef.ReportCell xCell) {
            if (!isEmpty()) {
                for (AdsReportChartAxis axis : this) {
                    org.radixware.schemas.adsdef.ChartAxis xAxis = axis.getAxisType() == EReportChartAxisType.DOMAIN_AXIS
                            ? xCell.addNewChartDomainAxis() : xCell.addNewChartRangeAxis();
                    axis.appendTo(xAxis);
                }
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            if (!isEmpty()) {
                for (AdsReportChartAxis axis : this) {
                    axis.collectDependences(list);
                }
            }
        }
    }
}
