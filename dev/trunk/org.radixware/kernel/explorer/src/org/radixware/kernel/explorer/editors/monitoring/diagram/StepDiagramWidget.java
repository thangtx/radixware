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

package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.gui.QWidget;
import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.renderer.xy.YIntervalRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.MetricValue;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.monitoringcommand.DiagramRs;
import org.radixware.schemas.monitoringcommand.MetricRecord;


public class StepDiagramWidget extends DiagramWidget {

    private XYItemRenderer renderer;

    public StepDiagramWidget(final QWidget parent/*,boolean hystogram*/) {
        super(parent);
        String sTime = Application.translate("SystemMonitoring", "Time");
        String sValue = Application.translate("SystemMonitoring", "Value");
        chart = ChartFactory.createTimeSeriesChart(null, sTime, sValue, null, false, false, false);
        final XYPlot plot = chart.getXYPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlineStroke(new BasicStroke(0.8f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 2.0f}, 0.0f));
        plot.setRangeGridlineStroke(new BasicStroke(0.8f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 2.0f}, 0.0f));
        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setUpperMargin(0);

        YIntervalRenderer intervalRend = new YIntervalRenderer();
        intervalRend.setSeriesPaint(0, Color.GRAY);
        intervalRend.setSeriesStroke(0, new BasicStroke(1f));
        plot.setRenderer(1, intervalRend);

        renderer = new XYStepRenderer();
        plot.setRenderer(2, renderer);
    }

    @Override
    public void update(/*List<MetricValue>*/DiagramRs diagRs, /*Timestamp begTime, Timestamp endTime,*/ DiagramSettings metricSettings, boolean isAsinc) {
        super.update(diagRs, metricSettings, isAsinc);
        /*XYSeriesCollection data;
         final XYSeries s1;
         data= new XYSeriesCollection();
         s1 = new XYSeries("Norm", true, true);      
         for(MetricValue val:vals){
         s1.add(val.getTime(), val.getVal());
         }
         data.addSeries(s1);
         drawDiagram(data,begTime,endTime,((HistoricalMetricSettings)metricSettings).getValueScale());*/
        /*HistoricalMetricSettings histMetricSettings = (HistoricalMetricSettings) metricSettings;*/
        if (!isAsinc) {
            updateRenderers(chart.getXYPlot(), metricSettings.getHistSettings().isHistogram());
        }

        List<MetricValue> listOfVals = new ArrayList<>();
        if (!diagRs.getHistoryRs().getRecords().getRecordList().isEmpty()) {
            for (MetricRecord rec : diagRs.getHistoryRs().getRecords().getRecordList()) {

                if (rec.getAvgVal() != null) {
                    listOfVals.add(new AbstaractMetricView.StepMetricValue(rec.getEndTime().getTime(), rec.getBegVal().doubleValue(), rec.getEndVal().doubleValue(), rec.getMaxVal().doubleValue(), rec.getMinVal().doubleValue(), rec.getAvgVal().doubleValue()));
                } else if (rec.getEndTime() != null && rec.getEndVal() != null) {
                    listOfVals.add(new AbstaractMetricView.StepMetricValue(rec.getEndTime().getTime(), rec.getBegVal().doubleValue(), rec.getEndVal().doubleValue()));
                }
            }
        } else {
            MetricRecord rec = diagRs.getHistoryRs().getPrevRecord();
            if (rec != null) {
                if (rec.getAvgVal() != null) {
                    listOfVals.add(new AbstaractMetricView.StepMetricValue(rec.getEndTime().getTime(), rec.getEndVal().doubleValue(), rec.getEndVal().doubleValue(), rec.getMaxVal().doubleValue(), rec.getMinVal().doubleValue(), rec.getAvgVal().doubleValue()));
                } else if (rec.getEndTime() != null && rec.getEndVal() != null) {
                    listOfVals.add(new AbstaractMetricView.StepMetricValue(rec.getEndTime().getTime(), rec.getEndVal().doubleValue(), rec.getEndVal().doubleValue()));
                }
            }
        }
        Timestamp begTime = diagRs.getHistoryRs().getTimeFrom();
        Timestamp endTime = diagRs.getHistoryRs().getTimeTo();
        updateDataset(listOfVals, begTime, endTime, metricSettings);
        metricSettings.getHistSettings().setValueScale(getMinVal(), getMaxVal());
    }

    private void updateRenderers(XYPlot plot, boolean isHistogram) {
        XYItemRenderer intervalRend = plot.getRenderer(1);
        if (isHistogram) {// &&  plot.getRenderer() instanceof XYLineAndShapeRenderer){
            XYBarRenderer rend = new XYBarRenderer();
            rend.setDrawBarOutline(false);
            rend.setSeriesPaint(0, getNormalColor());
            plot.setRenderer(2, rend);
            plot.setForegroundAlpha(0.8f);
            intervalRend.setSeriesPaint(0, Color.BLACK);
        } else {// if(!isHistogram && plot.getRenderer() instanceof XYBarRenderer){
            //XYLineAndShapeRenderer rend=new XYLineAndShapeRenderer(true,false); 
            //rend.setSeriesShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
            //rend.setBaseShape( new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesPaint(0, getNormalColor());
            renderer.setSeriesStroke(0, new BasicStroke(2f));
            plot.setRenderer(2, renderer);
            plot.setForegroundAlpha(1f);
            intervalRend.setSeriesPaint(0, Color.GRAY);
        }
    }

    private void updateDataset(List<MetricValue> vals, Timestamp begTime, Timestamp endTime, DiagramSettings histMetricSettings) {
        TimeTableXYDataset data = new TimeTableXYDataset();
        TimeTableXYDataset dataSeries = new TimeTableXYDataset();
        double[] y = new double[vals.size()];
        double[] x = new double[vals.size()];
        double[] yMax = new double[vals.size()];
        double[] yMin = new double[vals.size()];
        double newValInPrevPeriod = -1;
        int i = 0, stepSeriesIndex = 0;
        if (histMetricSettings.getHistSettings().isHistogram()) {
            Date dateStart = endTime;
            for (int j = 0; j < vals.size(); j++) {
                MetricValue val = vals.get(j);
                AbstaractMetricView.StepMetricValue stepVal = (AbstaractMetricView.StepMetricValue) val;
                Date dateEnd = new Date(stepVal.getTime());
                if (dateEnd.before(dateStart)) {
                    TimePeriod tp = new SimpleTimePeriod(dateEnd, dateStart);
                    if (newValInPrevPeriod != stepVal.getStartVal()) {
                        stepSeriesIndex++;
                    }
                    data.add(tp, val.getVal(), String.valueOf(stepSeriesIndex));
                    if (stepVal.isIntermediate() && (j + 1) < vals.size()) {
                        long prevDateMs = (j + 1) < vals.size() ? vals.get(j + 1).getTime() : begTime.getTime();
                        //long prevDateMs=vals.get(j+1).getTime();
                        Date intermediateDate = new Date(prevDateMs + (dateEnd.getTime() - prevDateMs) / 2);
                        Date prevDate = new Date(prevDateMs);
                        TimePeriod tp0 = new SimpleTimePeriod(intermediateDate, dateEnd);
                        data.add(tp0, stepVal.getAvgVal(), String.valueOf(stepSeriesIndex));
                        TimePeriod tp1 = new SimpleTimePeriod(prevDate, intermediateDate);
                        data.add(tp1, stepVal.getStartVal(), String.valueOf(stepSeriesIndex));
                        j++;
                        x[i] = intermediateDate.getTime();
                        y[i] = stepVal.getAvgVal();
                        yMax[i] = stepVal.getMaxVal();
                        yMin[i] = stepVal.getMinVal();
                        i++;
                        dateStart = prevDate;
                    } else {
                        dateStart = dateEnd;
                    }
                }
                newValInPrevPeriod = val.getVal();
            }
            if (vals.size() > 0) {
                AbstaractMetricView.StepMetricValue stepVal = ((AbstaractMetricView.StepMetricValue) vals.get(vals.size() - 1));
                TimePeriod tp = new SimpleTimePeriod(begTime, new Date(stepVal.getTime()));
                data.add(tp, stepVal.getStartVal(), "");
            }
        } else {
            if (!vals.isEmpty()) { 
                AbstaractMetricView.StepMetricValue stepVal = (AbstaractMetricView.StepMetricValue) vals.get(0);
                Date dateEnd = new Date(vals.get(0).getTime());
                data.add(new Millisecond(endTime), stepVal.getVal(), "");
                data.add(new Millisecond(dateEnd), stepVal.getVal(), "");
            }

            for (int j = 0; j < vals.size(); j++) {
                MetricValue val = vals.get(j);
                AbstaractMetricView.StepMetricValue stepVal = (AbstaractMetricView.StepMetricValue) val;
                Date date = new Date(stepVal.getTime());
                if (stepVal.isIntermediate()) {
                    data.add(new Millisecond(date), val.getVal(), String.valueOf(stepSeriesIndex));
                    stepSeriesIndex++;
                    long prevDate = (j + 1) < vals.size() ? vals.get(j + 1).getTime() : begTime.getTime();
                    Date intermediateDate = new Date(prevDate + (date.getTime() - prevDate) / 2);
                    dataSeries.add(new Millisecond(date), stepVal.getVal(), String.valueOf(i));
                    dataSeries.add(new Millisecond(intermediateDate), stepVal.getAvgVal(), String.valueOf(i));
                    dataSeries.add(new Millisecond(new Date(prevDate)), stepVal.getStartVal(), String.valueOf(i));

                    x[i] = intermediateDate.getTime();
                    y[i] = stepVal.getAvgVal();
                    yMax[i] = stepVal.getMaxVal();
                    yMin[i] = stepVal.getMinVal();
                    i++;
                } else {
                    if (newValInPrevPeriod != stepVal.getVal()) {
                        stepSeriesIndex++;
                    }
                    data.add(new Millisecond(date), stepVal.getStartVal(), String.valueOf(stepSeriesIndex));
                    data.add(new Millisecond(date), val.getVal(), String.valueOf(stepSeriesIndex));
                }
                newValInPrevPeriod = stepVal.getStartVal();
            }
            if (vals.size() > 0) {
                AbstaractMetricView.StepMetricValue stepVal = ((AbstaractMetricView.StepMetricValue) vals.get(vals.size() - 1));
                data.add(new Millisecond(begTime), stepVal.getStartVal(), String.valueOf(stepSeriesIndex));
                stepVal = ((AbstaractMetricView.StepMetricValue) vals.get(0));
                data.add(new Millisecond(endTime), stepVal.getVal(), String.valueOf(stepSeriesIndex)); //Draw line to the end of period
            }
        }
        DefaultIntervalXYDataset dataInterval = new DefaultIntervalXYDataset();
        double[][] serias = new double[][]{x, x, x, y, yMin, yMax};
        dataInterval.addSeries("int", serias);
        drawDiagram(dataSeries, data, dataInterval, begTime, endTime, histMetricSettings.getHistSettings().isAutoValueRange() ? null : histMetricSettings.getHistSettings().getValueScale());
    }

    private void drawDiagram(XYDataset dataset, XYDataset data, XYDataset intervaldata, Timestamp begTime, Timestamp endTime, double[] valScale) {
        final XYPlot plot = chart.getXYPlot();

        XYItemRenderer lineRenderer = plot.getRenderer(0);
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            lineRenderer.setSeriesPaint(i, getNormalColor());
            lineRenderer.setSeriesStroke(i, new BasicStroke(2f));
        }

        XYItemRenderer stepRenderer = plot.getRenderer(2);
        for (int i = 0; i < data.getSeriesCount(); i++) {
            stepRenderer.setSeriesPaint(i, getNormalColor());
            stepRenderer.setSeriesStroke(i, new BasicStroke(2f));
        }


        plot.getDomainAxis().setRange(begTime.getTime(), endTime.getTime());
        plot.setDataset(0, dataset);
        plot.setDataset(2, data);
        plot.setDataset(1, intervaldata);
        if (valScale != null) {
            plot.getRangeAxis().setRange(valScale[0], valScale[1]);
        } else {
//            autoValScale = new double[]{0, plot.getRangeAxis().getUpperBound()};
            plot.getRangeAxis().setAutoRange(true); //RADIX-8595 (7)
        }
        setDiagram(parent.width(), parent.height() / 3);
    }
}
