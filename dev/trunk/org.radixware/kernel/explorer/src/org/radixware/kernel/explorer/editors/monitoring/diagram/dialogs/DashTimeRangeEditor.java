/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Properties;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dashboard.DashPropContext;
import org.radixware.kernel.common.client.dashboard.DashTimeRangeProp;
import org.radixware.kernel.common.client.dashboard.EDashPropSource;
import org.radixware.kernel.common.client.dashboard.HistoricalDiagramSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.editors.valeditors.ValDateTimeEditor;

/**
 *
 * @author npopov
 */
public class DashTimeRangeEditor extends QGroupBox {

    private DashTimeRangeProp timeRange;
    private QSpinBox sbPeriod;
    private QComboBox cbPeriodUnit;
    private QComboBox cbPropSource;
    private QRadioButton rbIntervalTime;
    private QRadioButton rbLastPeriodTime;
    private QPushButton applyCfgBtn;
    private ValDateTimeEditor timeToEditor;
    private ValDateTimeEditor timeFromEditor;
    private QWidget intervalTimeWidget;
    private QWidget lastPeriodTimeWidget;
    private QWidget savedCfgWidget;
    private final IClientEnvironment env;
    private final MessageProvider mp;
    private HistoricalDiagramSettings.TimeRange timeRangeVal;
    private final EnumSet<EDashPropSource> availablePropSrc;
    private final DashPropContext context;
    private final MonitoringConfigHelper.AdsBridge adsBridge;
    private EDashPropSource currPropSrc;
   
    private static final String PREV_SAVED_CFG_KEY = SettingNames.SYSTEM + "/" + "system_monitor_time_range_prop";

    public DashTimeRangeEditor(QWidget parent, IClientEnvironment env, DashPropContext ctx, DashTimeRangeProp timeRange, MonitoringConfigHelper.AdsBridge adsBridge) {
        super(parent);
        setTitle(env.getMessageProvider().translate("SystemMonitoring", "Time Range"));
        this.env = env;
        this.mp = env.getMessageProvider();
        this.timeRange = timeRange;
        this.context = ctx;
        this.availablePropSrc = getAvailableSrcByContext(context.getContext());
        this.currPropSrc = timeRange.getPropSource();
        this.timeRangeVal = timeRange.getTimeRange();
        this.adsBridge = adsBridge;
        init();
    }

    private EnumSet<EDashPropSource> getAvailableSrcByContext(DashPropContext.EContext ctx) {
        switch (ctx) {
            case DASHBOARD:
                return EnumSet.of(EDashPropSource.OWN);
            case DASHBOARD_REF:
                return EnumSet.of(EDashPropSource.OWN, EDashPropSource.COMMON_REF);
            case WIDGET:
                return EnumSet.of(EDashPropSource.OWN, EDashPropSource.DASHBOARD);
            case WIDGET_REF:
                return EnumSet.of(EDashPropSource.OWN, EDashPropSource.DASHBOARD, EDashPropSource.COMMON_REF);
            default:
                return EnumSet.of(EDashPropSource.OWN);
        }
    }
    
    private String getPropSourceLocalazied(EDashPropSource src) {
        switch(src) {
            case OWN:
                return mp.translate("SystemMonitoring", "Individual settings");
            case DASHBOARD:
                return mp.translate("SystemMonitoring", "Dashboard");
            case COMMON_REF:
                return mp.translate("SystemMonitoring", "Common configuration");
            default:
                return mp.translate("SystemMonitoring", "Undefined source");
        }
    }

    private void init() {
        lastPeriodTimeWidget = new QWidget(this);
        intervalTimeWidget = new QWidget(this);

        savedCfgWidget = new QWidget(this);
        QHBoxLayout savedCfgLayout = new QHBoxLayout(this);
        savedCfgLayout.setAlignment(new Alignment(Qt.AlignmentFlag.AlignLeft));
        savedCfgLayout.setMargin(0);
        savedCfgWidget.setLayout(savedCfgLayout);
        final QPushButton saveCfgBtn = new QPushButton(mp.translate("SystemMonitoring", "Copy Range Parameters"));
        saveCfgBtn.clicked.connect(this, "onSaveCfgBtnClick()");
        savedCfgWidget.layout().addWidget(saveCfgBtn);
        applyCfgBtn = new QPushButton(mp.translate("SystemMonitoring", "Apply Copied Parameters"));
        applyCfgBtn.clicked.connect(this, "onApplyCfgBtnClick()");
        savedCfgWidget.layout().addWidget(applyCfgBtn);
        if (getPrevSavedCfgFromSettings() == null) {
            applyCfgBtn.setEnabled(false);
        }
        savedCfgWidget.setVisible(currPropSrc == EDashPropSource.OWN);

        QLabel lbPropSource = new QLabel(mp.translate("SystemMonitoring", "Configuration source"), this);
        cbPropSource = new QComboBox(this);
        for (EDashPropSource src : availablePropSrc) {
            cbPropSource.addItem(getPropSourceLocalazied(src), src);
        }
        int index = cbPropSource.findData(timeRange.getPropSource());
        cbPropSource.setCurrentIndex(index);
        cbPropSource.currentIndexChanged.connect(this, "onPropSourceChanged(Integer)");
        QWidget srcPropWidget = new QWidget(this);
        srcPropWidget.setLayout(new QHBoxLayout(this));
        srcPropWidget.layout().setContentsMargins(0, 0, 0, 0);
        srcPropWidget.layout().addWidget(lbPropSource);
        srcPropWidget.layout().addWidget(cbPropSource);
        if (availablePropSrc.size() <= 1) {
            srcPropWidget.setHidden(true);
        } 

        QLabel lbSince = new QLabel(mp.translate("SystemMonitoring", "Show recent statistics for"), this);
        sbPeriod = new QSpinBox(this);
        sbPeriod.setMinimum(1);
        sbPeriod.setSingleStep(1);
        sbPeriod.setMaximum(999);
        sbPeriod.setValue(timeRange.getPeriod());
        
        cbPeriodUnit = new QComboBox(this);
        cbPeriodUnit.addItem(mp.translate("SystemMonitoring", "Second"), Calendar.SECOND);
        cbPeriodUnit.addItem(mp.translate("SystemMonitoring", "Minute"), Calendar.MINUTE);
        cbPeriodUnit.addItem(mp.translate("SystemMonitoring", "Hour"), Calendar.HOUR);
        int curIndex = cbPeriodUnit.findData(timeRange.getPeriodUnit());
        cbPeriodUnit.setCurrentIndex(curIndex);

        QLabel lbTimeFrom = new QLabel(mp.translate("SystemMonitoring", "From") + ":", this);
        timeFromEditor = new ValDateTimeEditor(getEnvironment(), this);
        timeFromEditor.setValue(timeRange.getTimeFrom());
        QLabel lbTimeTo = new QLabel(mp.translate("SystemMonitoring", "To") + ":", this);
        timeToEditor = new ValDateTimeEditor(getEnvironment(), this);
        timeToEditor.setValue(timeRange.getTimeTo());

        rbIntervalTime = new QRadioButton(mp.translate("SystemMonitoring", "Show statistics for interval"));
        rbLastPeriodTime = new QRadioButton(mp.translate("SystemMonitoring", "Show recent statistics"));
        rbIntervalTime.clicked.connect(this, "rbIntervalTimeClick(boolean)");
        rbLastPeriodTime.clicked.connect(this, "rbLastPeriodClick(boolean)");
        if (timeRange.getTimeRange() == HistoricalDiagramSettings.TimeRange.INTERVAL) {
            rbIntervalTime.setChecked(true);
            rbIntervalTimeClick(true);
        } else {
            rbLastPeriodTime.setChecked(true);
            rbLastPeriodClick(true);
        }

        lastPeriodTimeWidget.setLayout(new QHBoxLayout());
        lastPeriodTimeWidget.layout().addWidget(lbSince);
        lastPeriodTimeWidget.layout().addWidget(sbPeriod);
        lastPeriodTimeWidget.layout().addWidget(cbPeriodUnit);
        lastPeriodTimeWidget.layout().setContentsMargins(0, 0, 0, 0);

        QGridLayout layout = new QGridLayout();
        layout.addWidget(lbTimeFrom, 0, 0);
        layout.addWidget(timeFromEditor, 0, 1);
        layout.addWidget(lbTimeTo, 1, 0);
        layout.addWidget(timeToEditor, 1, 1);
        layout.setContentsMargins(0, 0, 0, 0);
        intervalTimeWidget.setLayout(layout);

        QVBoxLayout mainLayout = new QVBoxLayout(this);
        mainLayout.addWidget(srcPropWidget);
        mainLayout.addWidget(savedCfgWidget);
        mainLayout.addWidget(rbLastPeriodTime);
        mainLayout.addWidget(lastPeriodTimeWidget);
        mainLayout.addWidget(rbIntervalTime);
        mainLayout.addWidget(intervalTimeWidget);

        enableTimeRangePanel(timeRange.getPropSource());
        setLayout(mainLayout);
    }
    
    private void updateEditor(DashTimeRangeProp timeRange_) {
        if (timeRange_ == null) {
            return;
        }
        this.timeRange = timeRange_;
        this.timeRangeVal = timeRange_.getTimeRange();
        
        setPeriod(timeRange_.getPeriod(), timeRange_.getPeriodUnit());
        
        timeFromEditor.setValue(timeRange_.getTimeFrom());
        timeToEditor.setValue(timeRange_.getTimeTo());
        if (timeRange_.getTimeRange() == HistoricalDiagramSettings.TimeRange.INTERVAL) {
            rbIntervalTime.setChecked(true);
            rbIntervalTimeClick(true);
        } else {
            rbLastPeriodTime.setChecked(true);
            rbLastPeriodClick(true);
        }
    }
    
    @SuppressWarnings("unused")
    private void onSaveCfgBtnClick() {
        try {
            StringWriter writer = new StringWriter();
            getDashTimeRangeProp().store().store(writer, "");
            getEnvironment().getConfigStore().writeString(PREV_SAVED_CFG_KEY, writer.getBuffer().toString());
            applyCfgBtn.setEnabled(true);
        } catch (IOException ex) {
            getEnvironment().getTracer().error("SystemMonitor. Error on store time range value.", ex);
        }
    }
    
    @SuppressWarnings("unused")
    private void onApplyCfgBtnClick() {
        updateEditor(getPrevSavedCfgFromSettings());
    }
    
    private DashTimeRangeProp getPrevSavedCfgFromSettings() {
        String prevSavedCfgAsStr = getEnvironment().getConfigStore().readString(PREV_SAVED_CFG_KEY, "");
        if (prevSavedCfgAsStr != null && !prevSavedCfgAsStr.isEmpty()) {
            try (InputStream is = new ByteArrayInputStream(prevSavedCfgAsStr.getBytes(StandardCharsets.UTF_8))) {
                Properties prop = new Properties();
                prop.load(is);
                return DashTimeRangeProp.load(prop);
            } catch (Exception ex) {
                getEnvironment().getTracer().error("SystemMonitor. Error on load saved time range value.", ex);
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private void onPropSourceChanged(Integer index) {
        EDashPropSource newPropSrc = (EDashPropSource) cbPropSource.itemData(index);
        if (newPropSrc == currPropSrc) {
            return;
        }
        
        DashTimeRangeProp timeRangeProp = null;
        switch (newPropSrc) {
            case OWN:
                timeRangeProp = this.timeRange;
                break;
            case DASHBOARD:
                timeRangeProp = adsBridge.getDashboardTimeRange(EDashPropSource.DASHBOARD);
                break;
            case COMMON_REF:
                switch (context.getContext()) {
                    case WIDGET_REF:
                        if (context.getDashGuid() != null && context.getWidgetGuid() != null) {
                            timeRangeProp = adsBridge.getRefWidgetTimeRange(context.getDashGuid(), context.getWidgetGuid());
                        }
                        break;
                    case DASHBOARD_REF:
                        timeRangeProp = adsBridge.getDashboardTimeRange(EDashPropSource.COMMON_REF);
                        break;
                    default:
                        break;
                }
                break;
        }

        if (timeRangeProp != null) {
            updateEditor(timeRangeProp);
        } else {
            env.messageError(env.getMessageProvider().translate("SystemMonitoring", "Time range is not defined in this property source."));
            cbPropSource.setCurrentIndex(cbPropSource.findText(currPropSrc.toString()));
            return;
        }
        
        currPropSrc = newPropSrc;
        enableTimeRangePanel(newPropSrc);
    }
    
    private void enableTimeRangePanel(EDashPropSource src) {
        boolean isTimePanelEnabled = src == EDashPropSource.OWN;
        if (!isTimePanelEnabled) {
            intervalTimeWidget.setEnabled(isTimePanelEnabled);
            lastPeriodTimeWidget.setEnabled(isTimePanelEnabled);
        }
        savedCfgWidget.setVisible(isTimePanelEnabled);
        rbIntervalTime.setEnabled(isTimePanelEnabled);
        rbLastPeriodTime.setEnabled(isTimePanelEnabled);
    }

    @SuppressWarnings("unused")
    private void rbIntervalTimeClick(boolean isChecked) {
        rbIntervalTime.setChecked(isChecked);
        timeRangeVal = HistoricalDiagramSettings.TimeRange.INTERVAL;
        intervalTimeWidget.setEnabled(isChecked);
        lastPeriodTimeWidget.setDisabled(isChecked);
    }

    @SuppressWarnings("unused")
    private void rbLastPeriodClick(boolean isChecked) {
        rbLastPeriodTime.setChecked(isChecked);
        timeRangeVal = HistoricalDiagramSettings.TimeRange.LAST_PERIOD;
        lastPeriodTimeWidget.setEnabled(isChecked);
        intervalTimeWidget.setDisabled(isChecked);
    }

    public boolean isDateIntervalCorrect() {
        if (rbIntervalTime.isChecked()) {
            if (!checkDateTimeInput(timeFromEditor) || !checkDateTimeInput(timeToEditor)) {
                return false;
            }
            if (timeToEditor.getValue().after(timeFromEditor.getValue())) {
                return true;
            } else {
                timeFromEditor.setFocus();
                return false;
            }
        }
        return true;
    }

    private boolean checkDateTimeInput(final ValDateTimeEditor editor) {
        final MessageProvider messageProvider = getEnvironment().getMessageProvider();
        final String title
                = messageProvider.translate("SystemMonitoring", "Error in date interval");
        return editor.checkInput(title, null);
    }

    public double getPeriod() {
        return sbPeriod.value();
    }

    public int getPeriodUnit() {
        return ((Integer) cbPeriodUnit.itemData(cbPeriodUnit.currentIndex())).intValue();
    }

    public void setPeriod(double value, int periodUnit) {
        if(periodUnit == Calendar.MILLISECOND) {
            updateEditorIfUnitIsMillis(Math.round(value));
        } else {
            sbPeriod.setValue((int) value);
            cbPeriodUnit.setCurrentIndex(cbPeriodUnit.findData(periodUnit));
        }
    }
    
    private void updateEditorIfUnitIsMillis(long period) {
        final int[] periodAndUnit = DashTimeRangeProp.getPeriodAndUnitForMillis(period);
        sbPeriod.setValue(periodAndUnit[0]);
        cbPeriodUnit.setCurrentIndex(cbPeriodUnit.findData(periodAndUnit[1]));
    }

    public void setTimeTo(Timestamp timeTo) {
        timeToEditor.setValue(timeTo);
    }

    public void setTimeFrom(Timestamp timeFrom) {
        timeFromEditor.setValue(timeFrom);
    }

    public void setTimeRange(HistoricalDiagramSettings.TimeRange timeRange) {
        if (timeRange.equals(HistoricalDiagramSettings.TimeRange.INTERVAL)) {
            rbIntervalTimeClick(true);
        } else {
            rbLastPeriodClick(true);
        }
    }

    public DashTimeRangeProp getDashTimeRangeProp() {
        return new DashTimeRangeProp((EDashPropSource) cbPropSource.itemData(cbPropSource.currentIndex()),
                timeRangeVal, timeFromEditor.getValue(), timeToEditor.getValue(),
                sbPeriod.value(), ((Integer) cbPeriodUnit.itemData(cbPeriodUnit.currentIndex())).intValue());
    }

    public IClientEnvironment getEnvironment() {
        return env;
    }
}
