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

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QWidget;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.CorrelationValue;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.MetricValue;
import org.radixware.schemas.monitoringcommand.DiagramRs;
import org.radixware.schemas.monitoringcommand.MetricRecord;
import org.radixware.schemas.monitoringcommand.MetricValueRanges;


public class CorrelationDiagramWidget extends DiagramWidget {

    public CorrelationDiagramWidget(final IClientEnvironment env, final QWidget parent) {
        super(env, parent);
        chart = ChartFactory.createPieChart(
                null, // chart title
                null, // data
                true, // include legend
                true,
                false);
        //TextTitle title = chart.getTitle();
        //title.setToolTipText("A title tooltip!");

        PiePlot plot = (PiePlot) chart.getPlot();
        //plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        //plot.setLabelGenerator(null);
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);

    }

    private static PieDataset createDataset(List<MetricValue> vals, PiePlot plot) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (MetricValue val : vals) {
            dataset.setValue(((CorrelationValue) val).getName(), val.getVal());
            plot.setSectionPaint(((CorrelationValue) val).getName(), ((CorrelationValue) val).getColor());
        }
        return dataset;
    }

    @Override
    protected void createHorizontalAxisWidget(int posAxisX, QColor colorBackground) {
        //Не используется для данного типа диаграм 
    }
    
    @Override
    public void update(/*List<MetricValue>*/DiagramRs diagRs, /*Timestamp begTime, Timestamp endTime,*/ DiagramSettings metricSettings, boolean isAsinc) {
        PiePlot plot = (PiePlot) chart.getPlot();
        if (metricSettings != null && metricSettings.isHistorical == false) {
            if (metricSettings.getCorrSettings().isShowValues()) {
                plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                         metricSettings.getCorrSettings().isPercent() ? "{2}" : "{1}"));
            } else {
                plot.setLabelGenerator(null);
            }
        }
        List<DiagramRs.CorrellationRs.CorrRecord> listFromXml = diagRs.getCorrellationRs().getCorrRecordList();//getRecords().getRecordList();
        List<MetricView.MetricValue> listOfVals = new LinkedList<>();
        /*CorrelationMetricSettings correlMetricSettings = (CorrelationMetricSettings) metricSettings;*/

        for (DiagramSettings ms : metricSettings.getCorrSettings().getMetricSettingsList()) {        
            DiagramSettings.EDiagramType metricKind = ms.getDiagramType();
            BigDecimal val;
            QColor color = new QColor(ms.getNormColor().getRGB());
            MetricRecord rec = null;
            MetricValueRanges ranges = null;
            for (DiagramRs.CorrellationRs.CorrRecord cr : listFromXml) {
                if (cr.getStateId().equals(ms.getStateId())) {
                    rec = cr.getCurrentState();
                    ranges = cr.getValueRanges();
                    break;
                }
            }
            
            if (rec != null && ranges != null) {
                if (metricKind == DiagramSettings.EDiagramType.STATISTIC) {
                    val = rec.getAvgVal();           
                } else {
                    val = rec.getEndVal();
                }

                Double[] errorArea = {ranges.getLowErrorVal() == null ? null : ranges.getLowErrorVal().doubleValue(), ranges.getHighErrorVal() == null ? null : ranges.getHighErrorVal().doubleValue()};
                Double[] warnArea = {ranges.getLowWarnVal() == null ? null : ranges.getLowWarnVal().doubleValue(), ranges.getHighWarnVal() == null ? null : ranges.getHighWarnVal().doubleValue()};
                if (isInArea(errorArea, val.doubleValue())) {
                    color = new QColor(ms.getErrorColor().getRGB());
                } else if (isInArea(warnArea, val.doubleValue())) {
                    color = new QColor(ms.getWarningColor().getRGB());
                }
                listOfVals.add(new MetricView.CorrelationValue(ms.getTitle(), val.doubleValue(), color));
            }
        }
        PieDataset data = createDataset(listOfVals, plot);
        plot.setDataset(data);
        drawDiagram(data);
    }

    private boolean isInArea(Double[] area, Double val) {
        return area != null && val != null && ((area[0] != null && val <= area[0]) || (area[1] != null && val > area[1]));
    }

    private void drawDiagram(PieDataset data) {
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDataset(data);
        setDiagram(parent.width(), parent.height() / 3);
    }

    @Override
    public void clearMarkers() {
    }

    @Override
    public void setTimeScale(double begTime, double endTime) {
    }
}
