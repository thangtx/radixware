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

import org.radixware.kernel.common.client.dashboard.FastMetricRecordIterator;
import com.trolltech.qt.gui.QWidget;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.YIntervalRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.MetricValue;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.StatistMetricValue;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.monitoringcommand.DiagramRs;
import org.radixware.schemas.monitoringcommand.MetricRecord;


public class StatisticalDiagramWidget extends DiagramWidget {

    private boolean showMaxMin = true;

    public StatisticalDiagramWidget(final IClientEnvironment env, final QWidget parent) {
        super(env, parent);
        //isHistogram=histogram;
        //this.period=period;
        String sTime = Application.translate("SystemMonitoring", null);
        String sValue = Application.translate("SystemMonitoring", "Value");
        chart = ChartFactory.createTimeSeriesChart(null, sTime, sValue, null, false, false, false);

        XYPlot plot = chart.getXYPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlineStroke(new BasicStroke(0.8f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 2.0f}, 0.0f));
        plot.setRangeGridlineStroke(new BasicStroke(0.8f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 2.0f}, 0.0f));
        //AxisSpace space=new AxisSpace();
        //space.setTop(0);
        //space.setLeft(DiagramWidget.leftSpace);
        //space.setBottom(DiagramWidget.bottomSpace);
        //plot.setFixedRangeAxisSpace(space);
        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setUpperMargin(0);

        //updateRenderers(plot,);

        YIntervalRenderer intervalRend = new YIntervalRenderer();
        intervalRend.setSeriesPaint(0, Color.GRAY);
        intervalRend.setSeriesStroke(0, new BasicStroke(1f));
        plot.setRenderer(1, intervalRend);
    }

    @Override
    public void update(/*List<MetricValue>*/DiagramRs diagRs, /*Timestamp begTime, Timestamp endTime,*/ DiagramSettings metricSettings, boolean isAsinc) {
        super.update(diagRs, metricSettings, isAsinc);
        /*HistoricalMetricSettings histMetricSettings = (HistoricalMetricSettings) metricSettings;*/
        showMaxMin = metricSettings.getHistSettings().isShowChangeRange();
        if (!isAsinc) {
            updateRenderers(chart.getXYPlot(), metricSettings.getHistSettings().isHistogram());
        }

        List<MetricValue> listOfVals = new ArrayList<>();
//        if (diagRs.getHistoryRs().isSetPrevRecord()) {
//            MetricRecord rec = diagRs.getHistoryRs().getPrevRecord();
//            if (rec.getEndTime() != null && rec.getBegTime() != null && rec.getAvgVal() != null) {
//                Double min = rec.getMinVal() != null ? rec.getMinVal().doubleValue() : null;
//                Double max = rec.getMaxVal() != null ? rec.getMaxVal().doubleValue() : null;
//                listOfVals.add(new StatistMetricValue(rec.getBegTime().getTime(), rec.getEndTime().getTime(), rec.getAvgVal().doubleValue(), max, min));
//            }
//        } else {
        try (final FastMetricRecordIterator iter = new FastMetricRecordIterator(diagRs.getHistoryRs().getRecords())) {
            while (iter.hasNext()) {
                final MetricRecord rec = iter.next();
                if (rec.getEndTime() != null && rec.getBegTime() != null && rec.getAvgVal() != null) {
                    Double min = rec.getMinVal() != null ? rec.getMinVal().doubleValue() : null;
                    Double max = rec.getMaxVal() != null ? rec.getMaxVal().doubleValue() : null;
                    listOfVals.add(new StatistMetricValue(rec.getBegTime().getTime(), rec.getEndTime().getTime(), rec.getAvgVal().doubleValue(), max, min));
                }
            }
        }
        
        begTime = diagRs.getHistoryRs().getTimeFrom();
        endTime = diagRs.getHistoryRs().getTimeTo();
        updateDataset(listOfVals, begTime, endTime, metricSettings);
        metricSettings.getHistSettings().setValueScale(getMinVal(), getMaxVal());
    }

    private void updateRenderers(XYPlot plot, boolean isHistogram) {
        if (isHistogram) {
            XYBarRenderer rend = new XYBarRenderer();
            rend.setDrawBarOutline(false);
            rend.setSeriesPaint(0, getNormalColor());
            plot.setRenderer(rend);
            plot.setForegroundAlpha(0.8f);
        } else {
            XYLineAndShapeRenderer rend = new XYLineAndShapeRenderer(true, false);
            rend.setSeriesPaint(0, getNormalColor());
            rend.setSeriesStroke(0, new BasicStroke(2f));
            rend.setSeriesShape(0, new Rectangle2D.Double(-3.0, -3.0, 6.0, 6.0));
            plot.setRenderer(rend);
            plot.setForegroundAlpha(1f);

        }
    }

    private void updateDataset(List<MetricValue> vals, Timestamp begTime, Timestamp endTime, DiagramSettings metricSettings) {
        if (metricSettings.getHistSettings().isHistogram()) {
            TimeTableXYDataset data = new TimeTableXYDataset();
            List<StatistMetricValue> maxMinVals = new ArrayList<>();
            for (MetricValue val : vals) {
                StatistMetricValue statistVal = (StatistMetricValue) val;
                Date dateStart = new Date(statistVal.getStartTime());
                Date dateEnd = new Date(statistVal.getEndTime());
                TimePeriod tp = new SimpleTimePeriod(dateStart, dateEnd);
                data.add(tp, statistVal.getVal(), "");
                if (statistVal.getMaxVal() != null && statistVal.getMinVal() != null) {
                    maxMinVals.add(statistVal);
                }
            }
            DefaultIntervalXYDataset dataInterval = createIntervalDataSet(maxMinVals);//new DefaultIntervalXYDataset();
            drawDiagram(data, dataInterval, begTime, endTime, metricSettings.getHistSettings().isAutoValueRange() ? null : metricSettings.getHistSettings().getValueScale());
        } else {
            TimeTableXYDataset data;
            data = new TimeTableXYDataset();
            List<StatistMetricValue> maxMinVals = new ArrayList<>();
            for (MetricValue val : vals) {
                StatistMetricValue statistVal = (StatistMetricValue) val;
                Date date = new Date(val.getTime());
                data.add(new Millisecond(date), val.getVal(), "avg");
                if (statistVal.getMaxVal() != null && statistVal.getMinVal() != null) {
                    maxMinVals.add(statistVal);
                }
            }
            DefaultIntervalXYDataset dataInterval = createIntervalDataSet(maxMinVals);
            drawDiagram(data, dataInterval, begTime, endTime, metricSettings.getHistSettings().isAutoValueRange() ? null : metricSettings.getHistSettings().getValueScale());
        }
    }

    /*private void fillDataSets(List<MetricValue> vals,TimeTableXYDataset data){        
     List<StatistMetricValue> maxMinVals=new ArrayList<>();
     for(MetricValue val:vals){
     StatistMetricValue statistVal=(StatistMetricValue)val;    
     Date date=new Date(val.getTime());
     data.add(new Millisecond(date), val.getVal(),"avg");
     if(statistVal.getMaxVal()!=null && statistVal.getMinVal()!=null){
     maxMinVals.add(statistVal);
     }
     } 
     int i=0;
     double[] y=new double[vals.size()];
     double[] x=new double[vals.size()];
     double[] yMax=new double[vals.size()];
     double[] yMin=new double[vals.size()];
     for(MetricValue val:maxMinVals){
     StatistMetricValue statistVal=(StatistMetricValue)val;  
     x[i]=statistVal.getTime();
     y[i]=statistVal.getVal();
     yMax[i]=statistVal.getMaxVal();
     yMin[i]=statistVal.getMinVal();
     i++;
     }     
        
     if(showMaxMin){
     double[][] serias=new double[][] {x, x, x, y,  yMin,yMax};
     dataInterval.addSeries("int", serias);
     }
     }*/
    DefaultIntervalXYDataset createIntervalDataSet(List<StatistMetricValue> maxMinVals) {
        DefaultIntervalXYDataset dataInterval = new DefaultIntervalXYDataset();
        if (showMaxMin) {
            int i = 0;
            double[] y = new double[maxMinVals.size()];
            double[] x = new double[maxMinVals.size()];
            double[] yMax = new double[maxMinVals.size()];
            double[] yMin = new double[maxMinVals.size()];
            for (MetricValue val : maxMinVals) {
                StatistMetricValue statistVal = (StatistMetricValue) val;
                x[i] = statistVal.getTime();
                y[i] = statistVal.getVal();
                yMax[i] = statistVal.getMaxVal();
                yMin[i] = statistVal.getMinVal();
                i++;
            }

            double[][] serias = new double[][]{x, x, x, y, yMin, yMax};
            dataInterval.addSeries("int", serias);
        }
        return dataInterval;
    }

    private void drawDiagram(TimeTableXYDataset data, XYDataset intervaldata, Timestamp begTime, Timestamp endTime, double[] valScale) {
        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setRange(begTime.getTime(), endTime.getTime());
        plot.setDataset(0, data);
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