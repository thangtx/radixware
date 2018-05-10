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
import java.awt.geom.Ellipse2D;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.DotMetricValue;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.MetricValue;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.monitoringcommand.DiagramRs;
import org.radixware.schemas.monitoringcommand.MetricRecord;


public class DotDiagramWidget extends DiagramWidget {

    public DotDiagramWidget(final IClientEnvironment env, final QWidget parent) {
        super(env, parent);
        String sTime = Application.translate("SystemMonitoring", null);
        String sValue = Application.translate("SystemMonitoring", "Value");
        chart = ChartFactory.createTimeSeriesChart(null, sTime, sValue, null, false, false, false);

        XYPlot plot = chart.getXYPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlineStroke(new BasicStroke(0.8f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 2.0f}, 0.0f));
        plot.setRangeGridlineStroke(new BasicStroke(0.8f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 2.0f}, 0.0f));
        //AxisSpace space=new AxisSpace();
        //space.setLeft(DiagramWidget.leftSpace);
        //space.setBottom(DiagramWidget.bottomSpace);
        //plot.setFixedRangeAxisSpace(space);
        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setUpperMargin(0);
    }

    @Override
    public void update(/*List<MetricValue>*/DiagramRs diagRs, /*Timestamp begTime, Timestamp endTime,*/ DiagramSettings metricSettings, boolean isAsinc) {
        super.update(diagRs, metricSettings, isAsinc);
        /*HistoricalMetricSettings histMetricSettings = (HistoricalMetricSettings) metricSettings;*/
        if (!isAsinc) {
            updateRenderers(chart.getXYPlot(), metricSettings.getHistSettings().isHistogram());
        }    
        List<MetricValue> resultList = new ArrayList<>();
        try (FastMetricRecordIterator iter = new FastMetricRecordIterator(diagRs.getHistoryRs().getRecords())) {
            while (iter.hasNext()) {
                MetricRecord rec = iter.next();
                if (rec.getEndTime() != null && rec.getEndVal() != null) {
                    long endTimeMs = rec.getEndTime().getTime();
                    long startTimeMs = endTimeMs;
                    if (rec.getBegTime() != null) {
                        startTimeMs = rec.getBegTime().getTime();
                    }
                    resultList.add(new DotMetricValue(startTimeMs, endTimeMs, rec.getEndVal().doubleValue()));
                }
            }
        }
        begTime = diagRs.getHistoryRs().getTimeFrom();
        endTime = diagRs.getHistoryRs().getTimeTo();
        updateDataset(resultList, begTime, endTime, metricSettings);
        metricSettings.getHistSettings().setValueScale(getMinVal(), getMaxVal());
    }

    private void updateRenderers(XYPlot plot, boolean isHistogram) {
        if (isHistogram) {// &&  plot.getRenderer() instanceof XYLineAndShapeRenderer){
            XYBarRenderer rend = new XYBarRenderer();
            rend.setDrawBarOutline(false);
            rend.setSeriesPaint(0, getNormalColor());
            plot.setRenderer(rend);
            plot.setForegroundAlpha(0.8f);
        } else {// if(!isHistogram && plot.getRenderer() instanceof XYBarRenderer){
            XYLineAndShapeRenderer rend = new XYLineAndShapeRenderer(true, true);
            rend.setSeriesShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
            //rend.setBaseShape( new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            rend.setSeriesPaint(0, getNormalColor());
            rend.setSeriesStroke(0, new BasicStroke(2f));
            plot.setRenderer(rend);
            plot.setForegroundAlpha(1f);
        }
    }

    private void updateDataset(List<MetricValue> vals, Timestamp begTime, Timestamp endTime, /*HistoricalMetricSettings*/DiagramSettings histMetricSettings) {
        if (histMetricSettings.getHistSettings().isHistogram()) {
            TimeTableXYDataset data = new TimeTableXYDataset();
            for (MetricValue val : vals) {
                DotMetricValue dotVal = (DotMetricValue) val;
                Date dateStart = new Date(dotVal.getStartTime());
                Date dateEnd = new Date(dotVal.getEndTime());
                TimePeriod tp = new SimpleTimePeriod(dateStart, dateEnd);
                data.add(tp, val.getVal(), "");
            }
            drawDiagram(data, begTime, endTime, histMetricSettings.getHistSettings().isAutoValueRange() ? null : histMetricSettings.getHistSettings().getValueScale());
        } else {
            TimeTableXYDataset data = new TimeTableXYDataset();
            for (MetricValue val : vals) {
                DotMetricValue dotVal = (DotMetricValue) val;
                Date date = new Date(dotVal.getEndTime());
                data.add(new Millisecond(date), val.getVal(), "");
            }
            drawDiagram(data, begTime, endTime, histMetricSettings.getHistSettings().isAutoValueRange() ? null : histMetricSettings.getHistSettings().getValueScale());
        }
    }

    private void drawDiagram(XYDataset data, Timestamp begTime, Timestamp endTime, double[] valScale) {
        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setRange(begTime.getTime(), endTime.getTime());
        plot.setDataset(data);
        if (valScale != null) {
            plot.getRangeAxis().setRange(valScale[0], valScale[1]);
        } else {
//            autoValScale = new double[]{0, plot.getRangeAxis().getUpperBound()};
            plot.getRangeAxis().setAutoRange(true); //RADIX-8595 (7)
        }
        plot.getDomainAxis().setRange(begTime.getTime(), endTime.getTime());
        setDiagram(parent.width(), parent.height() / 3);
    }
    /* @Override
     public void setValueScale(double minVal,double maxVal){
     final XYPlot plot = chart.getXYPlot();
     plot.getRangeAxis().setAutoRange(false);       
     if(valScale==null){
     valScale=new double[]{minVal,maxVal};
     }else{
     valScale[0]=minVal;
     valScale[1]=maxVal;
     }
     plot.getRangeAxis().setRange(minVal, maxVal);
     setDiagram(parent.width(), parent.height()/3);
     }
   
     @Override
     public double getMaxVal(){
     final XYPlot plot = chart.getXYPlot();
     return plot.getRangeAxis().getRange().getUpperBound();
     }
    
     @Override
     public double getMinVal(){
     final XYPlot plot = chart.getXYPlot();
     return plot.getRangeAxis().getRange().getLowerBound();
     } */
}
