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

package org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.sql.Timestamp;
import org.radixware.kernel.common.client.dashboard.DashPropContext;
import org.radixware.kernel.common.client.dashboard.DashTimeRangeProp;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.common.client.dashboard.EDashPropSource;
import org.radixware.kernel.common.client.dashboard.HistoricalDiagramSettings;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
//import org.radixware.kernel.explorer.env.Application;


public class HistoricalDiagramEditor extends CorrelationMetricEditor {

    private QRadioButton rbLine;
    private QRadioButton rbHystogram;
    private QDoubleSpinBox spinBoxMin;
    private QDoubleSpinBox spinBoxMax;
    private QCheckBox cbShowChangeRange = null;
    private QLabel lbMin;
    private QLabel lbMax;
    private boolean isAutoRange = true;
    private DiagramSettings.EDiagramType diagramType;
    public Signal0 rangeChanged = new Signal0();
    private QCheckBox isUpdateEnabledCheckBox;
    private QCheckBox isAlertsSuppressedCheckBox;
    private DashTimeRangeEditor timeRangeWidget;

    public HistoricalDiagramEditor(DiagramSettings metricSettings, MonitoringConfigHelper helper) {
        this(metricSettings, new DashPropContext(DashPropContext.EContext.WIDGET), helper);
    }
    
    public HistoricalDiagramEditor(DiagramSettings metricSettings, DashPropContext ctx, MonitoringConfigHelper helper) {
        super(helper, null);
        createUI(metricSettings, ctx);
    }

    private void createUI(/*AbstractMetricSettings*/DiagramSettings metricSettings, DashPropContext ctx) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        QVBoxLayout generalLayout = new QVBoxLayout();
        
        QFrame metricFrame = new QFrame(this);
        QGridLayout metricLayout = new QGridLayout();
        metricLayout.setMargin(0);
        metricFrame.setLayout(metricLayout);
        metricFrame.setFrameShape(QFrame.Shape.NoFrame);
        String lbText = mp.translate("SystemMonitoring", "Metric");
        QLabel lb = new QLabel(lbText + ":", this);
        final QAction action = new QAction(this);
        action.triggered.connect(this, "actChooseMetric()");
        edMetric.addButton("...", action);
        metricLayout.addWidget(lb, 0, 0);
        metricLayout.addWidget(edMetric, 0, 1);

        double minVal = 0, maxVal = 0;
        boolean isHystogram = false;
        if (metricSettings != null && metricSettings.isHistorical == true /*instanceof HistoricalMetricSettings*/) {
            /*HistoricalMetricSettings  histMetricSettings=(HistoricalMetricSettings)metricSettings;*/
            metricStateView = metricSettings.getMetricState();
            edMetric.setValue(metricSettings.getMetricName());
            edTitle.setText(metricSettings.getTitle());
            isHystogram = metricSettings.getHistSettings().isHistogram();
            double[] valScale = metricSettings.getHistSettings().getValueScale();
            isAutoRange = metricSettings.getHistSettings().isAutoValueRange();
            //if(!isAutoRange){
            if(valScale != null) {
                minVal = valScale[0];
                maxVal = valScale[1];
            }
            
            diagramType = metricSettings.getDiagramType();
            if (metricSettings.getDiagramType() == DiagramSettings.EDiagramType.STATISTIC) {
                cbShowChangeRange = new QCheckBox(this);
                cbShowChangeRange.setText(mp.translate("SystemMonitoring", "Show range of changes"));
                Qt.CheckState checkedState = metricSettings.getHistSettings().isShowChangeRange() ? Qt.CheckState.Checked : Qt.CheckState.Unchecked;
                cbShowChangeRange.setCheckState(checkedState);
            }
        }
        lbText = mp.translate("SystemMonitoring", "Name");
        lb = new QLabel(lbText + ":", this);
        metricLayout.addWidget(lb, 1, 0);
        metricLayout.addWidget(edTitle, 1, 1);

        QGroupBox rangeGroupBox = createRangePanel(minVal, maxVal);
        QHBoxLayout colorsAndTypeLayout=  new QHBoxLayout();
        QGroupBox colorGroupBox = createColorsPanel(metricSettings);
        QGroupBox diagramTypeGroupBox = createDiagramTypePanel(isHystogram);
        colorsAndTypeLayout.addWidget(colorGroupBox);
        colorsAndTypeLayout.addWidget(diagramTypeGroupBox);
        rangeGroupBox.setChecked(isAutoRange);
        autoRangeChange(isAutoRange);
        QFrame colorsAndTypeFrame = new QFrame(this);
        colorsAndTypeFrame.setFrameShape(QFrame.Shape.NoFrame);
        colorsAndTypeLayout.setMargin(0);
        colorsAndTypeFrame.setLayout(colorsAndTypeLayout);

        DashTimeRangeProp timeRangeProp;
        if (metricSettings != null) {
            timeRangeProp = metricSettings.getHistSettings().getTimeRangeProp();
        } else {
            timeRangeProp = new DashTimeRangeProp(EDashPropSource.OWN, 
                    HistoricalDiagramSettings.TimeRange.LAST_PERIOD,
                    null, null, 1, 10);
        }
        timeRangeWidget = new DashTimeRangeEditor(this, getEnvironment(), 
                ctx, timeRangeProp, helper.getAdsBridge());

        generalLayout.addWidget(metricFrame);
        generalLayout.addWidget(rangeGroupBox);
        generalLayout.addWidget(timeRangeWidget);
        generalLayout.addWidget(colorsAndTypeFrame);
        if (cbShowChangeRange != null) {
            generalLayout.addWidget(cbShowChangeRange);
        }
        if (metricSettings != null) {
            isAlertsSuppressedCheckBox = new QCheckBox(mp.translate("SystemMonitoring", "Alerts suppressed"));
            isAlertsSuppressedCheckBox.setChecked(metricSettings.getIsAlertsSuppressed());
            generalLayout.addWidget(isAlertsSuppressedCheckBox);

            isUpdateEnabledCheckBox = new QCheckBox(mp.translate("SystemMonitoring", "Update enabled"));
            isUpdateEnabledCheckBox.setChecked(metricSettings.getIsUpdateEnabled());
            generalLayout.addWidget(isUpdateEnabledCheckBox);
        }
        this.setLayout(generalLayout);
        
        boolean isRefContext = ctx.getContext() == DashPropContext.EContext.WIDGET_REF;
        if (isRefContext) {
            metricFrame.setEnabled(false);
            colorsAndTypeFrame.setEnabled(false);
            rangeGroupBox.setEnabled(false);
            if (cbShowChangeRange != null) {
                cbShowChangeRange.setEnabled(false);
            }
            if (metricSettings != null) {
                isAlertsSuppressedCheckBox.setEnabled(false);
                isUpdateEnabledCheckBox.setEnabled(false);
            }
        }
    }
        
    public boolean isDateIntervalCorrect() {
        return timeRangeWidget.isDateIntervalCorrect();
    }

    private QGroupBox createRangePanel(double minVal, double maxVal) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        QGroupBox rangeGroupBox = new QGroupBox(mp.translate("SystemMonitoring", "Auto scale"));
        rangeGroupBox.setCheckable(true);
        rangeGroupBox.toggled.connect(this, "autoRangeChange(boolean)");
        QGridLayout rangeLayout = new QGridLayout();
        lbMin = new QLabel(mp.translate("SystemMonitoring", "Minimum value") + ":", this);
        lbMax = new QLabel(mp.translate("SystemMonitoring", "Maximum value") + ":", this);
        spinBoxMin = createSpinBox(minVal);
        //spinBoxMin.setMaximum(Double.MAX_VALUE);
        //spinBoxMin.setDecimals(2);
        spinBoxMin.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Maximum);
        spinBoxMin.valueChanged.connect(this, "diagramRangeChanged()");

        spinBoxMax = createSpinBox(maxVal);
        //spinBoxMax.setMaximum(Double.MAX_VALUE);
        //spinBoxMax.setDecimals(2);
        spinBoxMax.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Maximum);
        spinBoxMax.valueChanged.connect(this, "diagramRangeChanged()");
        rangeLayout.addWidget(lbMin, 0, 0);
        rangeLayout.addWidget(spinBoxMin, 0, 1);
        rangeLayout.addWidget(lbMax, 1, 0);
        rangeLayout.addWidget(spinBoxMax, 1, 1);

        rangeGroupBox.setLayout(rangeLayout);
        rangeGroupBox.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Maximum);
        return rangeGroupBox;
    }

    private void diagramRangeChanged() {
        //boolean b=spinBoxMin.value()<spinBoxMax.value();
        rangeChanged.emit();
    }

    private QGroupBox createDiagramTypePanel(boolean isHystogram) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        QGroupBox groupBox = new QGroupBox(mp.translate("SystemMonitoring", "Diagram Type"));
        QVBoxLayout layout = new QVBoxLayout();
        rbLine = new QRadioButton(mp.translate("SystemMonitoring", "Line"), groupBox);
        rbLine.toggled.connect(this, "changeDiagramType(boolean)");
        rbHystogram = new QRadioButton(mp.translate("SystemMonitoring", "Histogram"), groupBox);
        rbHystogram.toggled.connect(this, "changeDiagramType(boolean)");
        if (isHystogram) {
            rbHystogram.setChecked(true);
        } else {
            rbLine.setChecked(true);
        }
        layout.addWidget(rbLine);
        layout.addWidget(rbHystogram);
        groupBox.setLayout(layout);
        groupBox.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Preferred);
        return groupBox;
    }

    @Override
    public DiagramSettings getMetricSettings() {
        DiagramSettings metricSettings = new DiagramSettings(true);
        updateSettings(metricSettings);
        metricSettings.setKind((String) metricSettings.getMetricState().getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_KIND)).getValueObject());
        metricSettings.setStateId((long) metricSettings.getMetricState().getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID)).getValueObject());
        metricSettings.setDiagramType(helper.getDiagramType(metricSettings.getKind()));
        metricSettings.setTitle(edTitle.text());
        metricSettings.getHistSettings().setIsHistogram(rbHystogram.isChecked());
        metricSettings.getHistSettings().setTimeRangeProp(timeRangeWidget.getDashTimeRangeProp());
        if (isAlertsSuppressedCheckBox != null) {
            metricSettings.setIsAlertsSuppressed(isAlertsSuppressedCheckBox.isChecked());
        }
        if (isUpdateEnabledCheckBox != null) {
            metricSettings.setIsUpdateEnabled(isUpdateEnabledCheckBox.isChecked());
        }
        if (cbShowChangeRange != null) {
            metricSettings.getHistSettings().setIsShowChangeRange(cbShowChangeRange.isChecked());
        }
        //if(isAutoRange){
        metricSettings.getHistSettings().setAutoValueRange(isAutoRange);
        if (!isAutoRange) {
            metricSettings.getHistSettings().setValueScale(spinBoxMin.value(), spinBoxMax.value());
        }
        return metricSettings;
    }

    @SuppressWarnings("unused")
    private void changeDiagramType(boolean b) {
        if (rbLine.isChecked()) {
            rbHystogram.setChecked(false);
        } else {
            rbLine.setChecked(false);
        }
        //if(metricSettings!=null && metricSettings instanceof HistoricalMetricSettings){
        //    ((HistoricalMetricSettings)metricSettings).setIsHistogram(rbHystogram.isChecked());
        //}
    }

    @Override
    public boolean isComplete() {
        return spinBoxMin.value() <= spinBoxMax.value() && edMetric.getValue() != null && !edMetric.getValue().isEmpty();
    }

    private QDoubleSpinBox createSpinBox(double val) {
        QDoubleSpinBox spinBox = new QDoubleSpinBox(this);
        spinBox.setMinimum(0);
        spinBox.setMaximum(Double.MAX_VALUE);
        spinBox.setDecimals(2);
        spinBox.setValue(val);
        return spinBox;
    }

    @SuppressWarnings("unused")
    private void autoRangeChange(boolean isChecked) {
        boolean isEnable = !isChecked;
        isAutoRange = isChecked;
        spinBoxMin.setEnabled(isEnable);
        spinBoxMax.setEnabled(isEnable);
        lbMin.setEnabled(isEnable);
        lbMax.setEnabled(isEnable);
        //if(metricSettings!=null && metricSettings instanceof HistoricalMetricSettings){
        //   ((HistoricalMetricSettings)metricSettings).setValueScale(minVal, maxVal);
    }
    
    public void setPeriod(double value, int periodUnit) {
        timeRangeWidget.setPeriod(value, periodUnit);
    }

    public void setTimeTo(Timestamp timeTo) {
        timeRangeWidget.setTimeTo(timeTo);
    }

    public void setTimeFrom(Timestamp timeFrom) {
        timeRangeWidget.setTimeFrom(timeFrom);
    }

    public void setTimeRange(HistoricalDiagramSettings.TimeRange timeRange) {
        timeRangeWidget.setTimeRange(timeRange);
    }
}