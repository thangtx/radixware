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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Set;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartAxis;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EReportChartAxisLabelPositions;
import org.radixware.kernel.common.enums.EReportChartLegendPosition;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.reports.CellValueFormatter;
import org.radixware.kernel.server.reports.ChartDataSet;
import org.radixware.kernel.server.types.Report;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public abstract class ChartBuilder {

    private static final double INCH2MM = 25.4;

    private static final double DPMM = 1.0 * FopReportGenerator.DEFAULT_REPORT_DPI / INCH2MM; // dot per millimeter

    public File file;
    private final Report report;
    private final Id reportId;
    protected final Set<ChartDataSet> chartDataSet;
    protected final AdsReportChartCell cell;

    public static final class Factory {

        public static ChartBuilder newInstance(final AdsReportChartCell cell, final Report report, final Set<ChartDataSet> chartDataSet) {
            switch (cell.getChartType()) {
                case CATEGORY:
                    return new CategoryChartBuilder(cell, report, chartDataSet);
                case XY:
                    return new XYChartBuilder(cell, report, chartDataSet);
                case PIE:
                    return new PieChartBuilder(cell, report, chartDataSet);
            }
            return null;
        }
    }

    protected ChartBuilder(final AdsReportChartCell cell, final Report report, final Set<ChartDataSet> chartDataSet) {
        this.report = report;
        this.chartDataSet = chartDataSet;
        this.reportId = Id.Factory.loadFrom(report.getClass().getSimpleName());
        this.cell = cell;
    }

    public void buildChart() {
        JFreeChart chart = createChart();
        if (chart != null) {
            String chartTitle = calcTitle(cell.getTitleId());
            if (chartTitle != null) {
                chart.setTitle(chartTitle);
                Font newFont = createFont(cell.getTitleFont());
                chart.getTitle().setFont(newFont);
            }
            EReportChartLegendPosition legendPosition = cell.getLegendPosition();
            Font newFont = createFont(cell.getLegendFont());
            chart.getLegend().setItemFont(newFont);
            if (legendPosition != EReportChartLegendPosition.BOTTOM) {
                switch (legendPosition) {
                    case NO_LEGEND:
                        chart.removeLegend();
                        break;
                    case LEFT:
                        setLegendPosition(chart, RectangleEdge.LEFT);
                        break;
                    case RIGHT:
                        setLegendPosition(chart, RectangleEdge.RIGHT);
                        break;
                    case TOP:
                        setLegendPosition(chart, RectangleEdge.TOP);
                        break;
                }
            }
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument(null, "svg", null);
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            svgGenerator.setSVGCanvasSize(new Dimension(mm2px(cell.getWidthMm()), mm2px(cell.getHeightMm())));
            //svgGenerator.getGeneratorContext().setPrecision(6);
            ChartRenderingInfo info = new ChartRenderingInfo();
            chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, mm2px(cell.getWidthMm()), mm2px(cell.getHeightMm())), info);
            PlotRenderingInfo plotInfo = info.getPlotInfo();
            if (cell.getPlotMinSizeEnable()) {
                if (plotInfo.getPlotArea() != null) {
                    double delta = cell.getMinPlotHeightMm() - plotInfo.getPlotArea().getHeight();
                    cell.setAdjustHeightMm(delta > 0 ? cell.getHeightMm() + delta : cell.getHeightMm());
                    delta = plotInfo.getPlotArea() == null ? cell.getMinPlotWidthMm() : cell.getMinPlotWidthMm() - plotInfo.getPlotArea().getWidth();
                    cell.setAdjustWidthMm(delta > 0 ? cell.getWidthMm() + delta : cell.getWidthMm());
                } else {
                    if (plotInfo.getDataArea() != null) {
                        AdsReportForm ownerForm = cell.getOwnerForm();
                        double pageWidth = ownerForm.getPageWidthMm() - ownerForm.getMargin().getLeftMm() - ownerForm.getMargin().getRightMm();
                        double width = Math.max(cell.getMinPlotWidthMm(), cell.getWidthMm()) + plotInfo.getDataArea().getWidth();
                        cell.setAdjustWidthMm(width > pageWidth ? pageWidth : width);
                        cell.setAdjustHeightMm(Math.max(cell.getMinPlotHeightMm(), cell.getHeightMm()) + plotInfo.getDataArea().getHeight());
                    } else {
                        cell.setAdjustHeightMm(Math.max(cell.getMinPlotHeightMm(), cell.getHeightMm()));
                        cell.setAdjustWidthMm(Math.max(cell.getMinPlotWidthMm(), cell.getWidthMm()));
                    }
                }
                svgGenerator.setSVGCanvasSize(new Dimension(mm2px(cell.getAdjustWidthMm()), mm2px(cell.getAdjustHeightMm())));
                chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, mm2px(cell.getAdjustWidthMm()), mm2px(cell.getAdjustHeightMm())));
            }

            boolean useCSS = true;
            try {
                file = File.createTempFile(cell.getName(), ".svg");
                file.deleteOnExit();
                Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                try {
                    svgGenerator.stream(out, useCSS);
                } finally {
                    out.close();
                }
            } catch (IOException ex) {
                throw new RadixError("Unable to create svg file for report.", ex);
            } finally {
                if (file != null) {
                    FileUtils.deleteFile(file);
                    file = null;
                }
            }
        }
    }

    private void setLegendPosition(final JFreeChart chart, RectangleEdge position) {
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setPosition(position);
        }
    }

    protected CategoryAxis createCategoryAxis(AdsReportChartAxis axis, AdsReportAbstractAppearance.Font font) {
        String axisName = calcTitle(axis.getTitleId());
        CategoryAxis categoryAxis = axisName == null ? new CategoryAxis() : new CategoryAxis(axisName);
        Font newFont = createFont(font);
        categoryAxis.setLabelFont(newFont);
        categoryAxis.setTickLabelFont(newFont);
        if (axis.getAxisLabelPosition() != EReportChartAxisLabelPositions.STANDARD) {
            categoryAxis.setCategoryLabelPositions(calcLabelPosition(axis.getAxisLabelPosition()));
        }
        return categoryAxis;
    }

    protected ValueAxis createValueAxis(AdsReportChartAxis axis, AdsReportAbstractAppearance.Font font, EValType type) {
        String axisName = calcTitle(axis.getTitleId());
        ValueAxis domainAxis;
        if (axis.isDateAxis()) {
            domainAxis = axisName == null ? new DateAxis() : new DateAxis(axisName);
            if (type != null) {
                DateFormat df = (DateFormat) CellValueFormatter.reportFormatToJavaFormat(axis.getFormat(), report.getArte().getClientLocale(), type);
                ((DateAxis) domainAxis).setDateFormatOverride(df);
            }
        } else {
            domainAxis = axisName == null ? new NumberAxis() : new NumberAxis(axisName);
            ((NumberAxis) domainAxis).setAutoRangeIncludesZero(axis.isAutoRangeIncludeZero());
            if (type != null) {
                NumberFormat nf = (NumberFormat) CellValueFormatter.reportFormatToJavaFormat(axis.getFormat(), report.getArte().getClientLocale(), type);
                ((NumberAxis) domainAxis).setNumberFormatOverride(nf);
            }
        }
        Font newFont = createFont(font);
        domainAxis.setLabelFont(newFont);
        domainAxis.setTickLabelFont(newFont);
        if (axis.getAxisLabelPosition() != EReportChartAxisLabelPositions.STANDARD) {
            domainAxis.setVerticalTickLabels(true);
        }
        return domainAxis;
    }

    protected Number getValue(Object val, EValType valtype) {
        Number res = null;
        if (val instanceof Number/*==EValType.NUM) || (valtype==EValType.INT)*/) {
            res = (Number) val;
        }
        //if(valtype==EValType.NUM){
        //    res=  (Double)val;
        //}else if(valtype==EValType.INT){
        //    res=  (Long)val;
        //}
        return res;
    }

    protected final Font createFont(AdsReportAbstractAppearance.Font f) {
        if (f == null) {
            f = cell.getFont();
        }
        int style = (f.getBold() ? Font.BOLD : 0) | (f.getItalic() ? Font.ITALIC : 0);
        return new Font(f.getName(), style, mm2px(f.getSizeMm()));
    }

    protected String calcSeriesTitle(Object series, ChartDataSet dataset) {
        String seriesName;
        if (series != null) {
            seriesName = series.toString();
        } else {
            seriesName = calcTitle(dataset.getSeriesTitleId());
            seriesName = seriesName == null ? dataset.getSeriesName() : seriesName;
        }
        return seriesName;
    }

    protected void calcAxisTitle(RadPropDef prop, Axis axis) {
        if ((axis.getLabel() == null || axis.getLabel().equals("")) && (prop != null)) {
            String axisName = prop.getTitle();
            if (axisName == null) {
                axisName = prop.getName();
            }
            axis.setLabel(axisName);
        }
    }

    protected final String calcTitle(Id titleId) {
        return titleId != null
                ? org.radixware.kernel.common.types.MultilingualString.get(report.getArte(), report.getId(), titleId) : null;
    }

    private CategoryLabelPositions calcLabelPosition(EReportChartAxisLabelPositions lebelPosition) {
        switch (lebelPosition) {
            case DOWN_45:
                return CategoryLabelPositions.DOWN_45;
            case DOWN_90:
                return CategoryLabelPositions.DOWN_90;
            case UP_45:
                return CategoryLabelPositions.UP_45;
            case UP_90:
                return CategoryLabelPositions.UP_90;
        }
        return CategoryLabelPositions.STANDARD;
    }

    public static int mm2px(double mm) {
        return (int) (mm * DPMM * 1.0);
    }

    protected JFreeChart createChart() {
        JFreeChart chart = null;
        Plot plot = createPlot();
        if (plot != null) {
            chart = new JFreeChart(plot);
        }
        return chart;
    }

    protected abstract Plot createPlot();

    protected AbstractRenderer createRenderer(ChartDataSet chartDataSet) {
        switch (chartDataSet.getSeriesType()) {
            case BAR:
                BarRenderer renderer = new BarRenderer();
                renderer.setDrawBarOutline(false);
                //renderer.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL));
                return renderer;
            case BAR_3D:
                BarRenderer3D renderer3D = new BarRenderer3D();
                renderer3D.setDrawBarOutline(false);
                return renderer3D;
            case BAR_STACKED:
                return new StackedBarRenderer();
            case BAR_STACKED_3D:
                return new StackedBarRenderer3D();
            case LINE:
                return new LineAndShapeRenderer();
            case LINE_3D:
                return new LineRenderer3D();
            case AREA:
                return new AreaRenderer();
            case AREA_STACKED:
                return new StackedAreaRenderer();

            case XY_BAR:
                return new XYBarRenderer();
            case XY_BAR_STACKED:
                return new StackedXYBarRenderer();
            case XY_LINE:
                return new XYLineAndShapeRenderer();
            case XY_AREA:
                return new XYAreaRenderer();
            case XY_AREA_STACKED:
                return new StackedXYAreaRenderer2();
        }
        return new BarRenderer();
    }

}
