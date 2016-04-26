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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class CorrelationDiagramEditor extends BaseDiagramEditor {

    private QListWidget metricListWidget;
    private QPushButton btnDelete;
    private QPushButton btnEdit;
    private QRadioButton rbShowAbsoluteValues;
    private QRadioButton rbShowPersentValues;
    private boolean isShowVals = false;
    private boolean isPercent = false;
    private final QColor[] defaultColors = new QColor[]{QColor.blue, QColor.cyan, QColor.green, QColor.yellow,
        QColor.red, QColor.gray, QColor.magenta, QColor.darkRed,
        QColor.darkBlue, QColor.darkGreen, QColor.magenta, QColor.white,
        QColor.darkGray, QColor.darkCyan, QColor.lightGray, QColor.black,
        QColor.darkMagenta, QColor.darkYellow};
    private MonitoringConfigHelper helper;
    private QCheckBox isUpdateEnabledCheckBox;
    private QCheckBox isAlertsSuppressedCheckBox;

    public CorrelationDiagramEditor(DiagramSettings metricSettings, /*MetricHistWidget*/ MonitoringConfigHelper helper) {
        super(helper.getAdsBridge().getDashboardView()/*,metricSettings*/);
        this.setWindowTitle(Application.translate("SystemMonitoring", "Diagram Editor"));
        this.parent = helper.getAdsBridge().getDashboardView();
        this.helper = helper;
        edTitle = new QLineEdit();

        createUI(metricSettings);
    }

    private void createUI(DiagramSettings metricSettings) {
        QVBoxLayout generalLayout = new QVBoxLayout();
        QGridLayout titleLayout = new QGridLayout();
        if (metricSettings != null) {
            edTitle.setText(metricSettings.getTitle());
        }
        String lbText = Application.translate("SystemMonitoring", "Name");
        QLabel lb = new QLabel(lbText + ":", this);
        titleLayout.addWidget(lb, 1, 0);
        titleLayout.addWidget(edTitle, 1, 1);

        QHBoxLayout metricsLayout = new QHBoxLayout();
        QLabel lbMetricList = new QLabel(Application.translate("SystemMonitoring", "Metric List") + ":");
        metricListWidget = new QListWidget(this);
        metricListWidget.currentItemChanged.connect(this, "onItemChanged(QListWidgetItem,QListWidgetItem)");
        QVBoxLayout metricBtnLayout = new QVBoxLayout();
        QPushButton btnAdd = new QPushButton(this);
        btnAdd.setObjectName("btnAddMetric");
        btnAdd.setParent(this);
        btnAdd.setToolTip(Application.translate("SystemMonitoring", "Add Metric"));
        btnAdd.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.CREATE));
        btnAdd.setIconSize(new QSize(22, 22));
        btnAdd.clicked.connect(this, "addMetric()");
        fillList(metricSettings);

        btnDelete = new QPushButton(this);
        btnDelete.setObjectName("btnDeleteMetric");
        btnDelete.setParent(this);
        btnDelete.setToolTip(Application.translate("SystemMonitoring", "Delete Metric"));
        btnDelete.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.DELETE));
        btnDelete.setIconSize(new QSize(22, 22));
        btnDelete.clicked.connect(this, "deleteMetric()");
        btnDelete.setEnabled(false);

        btnEdit = new QPushButton(this);
        btnEdit.setObjectName("btnEditMetric");
        btnEdit.setParent(this);
        btnEdit.setToolTip(Application.translate("SystemMonitoring", "Edit Metric"));
        btnEdit.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.EDIT));
        btnEdit.setIconSize(new QSize(22, 22));
        btnEdit.clicked.connect(this, "editMetric()");
        btnEdit.setEnabled(false);
        metricBtnLayout.addWidget(btnAdd/*, 0, Qt.AlignmentFlag.AlignTop*/);
        metricBtnLayout.addWidget(btnDelete/*, 0, Qt.AlignmentFlag.AlignTop*/);
        metricBtnLayout.addWidget(btnEdit, 0, Qt.AlignmentFlag.AlignTop);

        metricsLayout.addWidget(metricListWidget);
        metricsLayout.addLayout(metricBtnLayout);

        generalLayout.addLayout(titleLayout);
        generalLayout.addWidget(lbMetricList);
        generalLayout.addLayout(metricsLayout);
        generalLayout.addWidget(createShowValuesPanel(metricSettings));
        if (metricSettings != null) {
            isAlertsSuppressedCheckBox = new QCheckBox(Application.translate("SystemMonitoring", "Alerts suppressed"));
            isAlertsSuppressedCheckBox.setChecked(metricSettings.getIsAlertsSuppressed());
            generalLayout.addWidget(isAlertsSuppressedCheckBox);
            
            isUpdateEnabledCheckBox = new QCheckBox(Application.translate("SystemMonitoring", "Update enabled"));
            isUpdateEnabledCheckBox.setChecked(metricSettings.getIsUpdateEnabled());
            generalLayout.addWidget(isUpdateEnabledCheckBox);
        }
        this.setLayout(generalLayout);
        setBtnEnable();
        metricSet.emit();
    }

    private QGroupBox createShowValuesPanel(DiagramSettings metricSettings) {
        QGroupBox showValsGroupBox = new QGroupBox(Application.translate("SystemMonitoring", "Show values as"));
        showValsGroupBox.setCheckable(true);
        showValsGroupBox.toggled.connect(this, "showValuesChange(boolean)");
        QVBoxLayout layout = new QVBoxLayout();
        rbShowAbsoluteValues = new QRadioButton(Application.translate("SystemMonitoring", "Absolute"));
        rbShowAbsoluteValues.toggled.connect(this, "changeValType(boolean)");

        rbShowPersentValues = new QRadioButton(Application.translate("SystemMonitoring", "Percent"));
        rbShowPersentValues.toggled.connect(this, "changeValType(boolean)");

        if (metricSettings != null && metricSettings.isHistorical == false) {
            isPercent = metricSettings.getCorrSettings().isPercent();
            showValuesChange(metricSettings.getCorrSettings().isShowValues());
        }
        showValsGroupBox.setChecked(isShowVals);

        layout.addWidget(rbShowAbsoluteValues);
        layout.addWidget(rbShowPersentValues);

        showValsGroupBox.setLayout(layout);
        showValsGroupBox.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Maximum);
        return showValsGroupBox;
    }

    @SuppressWarnings("unused")
    private void changeValType(boolean b) {
        if (rbShowAbsoluteValues.isChecked()) {
            isPercent = false;
            rbShowPersentValues.setChecked(false);
        } else {
            isPercent = true;
            rbShowAbsoluteValues.setChecked(false);
        }
    }

    @SuppressWarnings("unused")
    private void showValuesChange(boolean showValues) {
        isShowVals = showValues;
        if (isShowVals) {
            rbShowPersentValues.setChecked(isPercent);
            rbShowAbsoluteValues.setChecked(!isPercent);
        } //else {
        //   rbShowPersentValues.setChecked(false);
        //    rbShowAbsoluteValues.setChecked(false);
        //}
    }

    private void fillList(DiagramSettings metricSettings) {
        if (metricSettings != null && metricSettings.isHistorical == false) {
            /*CorrelationMetricSettings correlationMetricSettings = (CorrelationMetricSettings) metricSettings;*/
            List<DiagramSettings> msList = metricSettings.getCorrSettings().getMetricSettingsList();
            for (DiagramSettings ms : msList) {
//                MetricSettings newMs = new MetricSettings(ms);
//                MetricListItem item = new MetricListItem(newMs);
//                item.setText(newMs.getTitle());
//                metricListWidget.addItem(item);
                MetricListItem item = new MetricListItem(ms);
                item.setText(ms.getTitle());
                metricListWidget.addItem(item);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onItemChanged(QListWidgetItem item, QListWidgetItem item1) {
        setBtnEnable();
    }

    private void setBtnEnable() {
        boolean enable = metricListWidget.currentItem() != null;
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    @Override
    public /*AbstractMetricSettings*/ DiagramSettings getMetricSettings() {
        DiagramSettings correlationMetricSettings = new DiagramSettings(false);
        correlationMetricSettings.setTitle(edTitle.text());
        correlationMetricSettings.setDiagramType(DiagramSettings.EDiagramType.CORRELATION);
        correlationMetricSettings.getCorrSettings().setShowValues(isShowVals);
        correlationMetricSettings.getCorrSettings().setPercent(isPercent);
        if (isAlertsSuppressedCheckBox != null) {
            correlationMetricSettings.setIsAlertsSuppressed(isAlertsSuppressedCheckBox.isChecked());
        }
        if (isUpdateEnabledCheckBox != null) {
            correlationMetricSettings.setIsUpdateEnabled(isUpdateEnabledCheckBox.isChecked());
        }
        for (int i = 0; i < metricListWidget.count(); i++) {
            MetricListItem item = (MetricListItem) metricListWidget.item(i);
            if (item.getMetricSettings() != null) {
                DiagramSettings ms = item.getMetricSettings();
                correlationMetricSettings.getCorrSettings().getMetricSettingsList().add(ms);
            }
        }
        return correlationMetricSettings;
    }

    private Set<Long> getIds() {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < metricListWidget.count(); i++) {
            MetricListItem item = (MetricListItem) metricListWidget.item(i);
            if (item.getMetricSettings() != null && item.getMetricSettings().getMetricState() != null) {
                Id propId = helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID);
                ids.add((Long) item.getMetricSettings().getMetricState().getProperty(propId).getValueObject());
            }
        }
        return ids;
    }

    @SuppressWarnings("unused")
    private void addMetric() {
        EntityModel metricState = helper.chooseMetricState(getIds());
        if (metricState != null) {
            EntityModel metricViewModel = helper.getMetricViewByMetricState(metricState, true);
            if (metricViewModel != null) {
                String title = (String) metricState.getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_SENSOR_TITLE)).getValueObject();
                title = title == null || title.isEmpty() ? "" : ", " + title;
                String typeTitle = (String) metricViewModel.getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_TYPE_TITLE)).getValueObject();
                title = typeTitle + title;
                MetricListItem item = new MetricListItem(metricViewModel);
                DiagramSettings itemMs = item.getMetricSettings();
                itemMs.setMetricName(title);
                itemMs.setTitle(title);
                itemMs.setKind((String) itemMs.getMetricState().getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_KIND)).getValueObject());
                itemMs.setStateId((long) itemMs.getMetricState().getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID)).getValueObject());
                itemMs.setDiagramType(helper.getDiagramType(itemMs.getKind()));
                if (defaultColors.length >= metricListWidget.count()) {
                    item.getMetricSettings().setNormColor(new Color(defaultColors[metricListWidget.count()].rgb()));
                }
                item.setText(title);
                metricListWidget.addItem(item);
                metricSet.emit();
            }
        }
    }

    @SuppressWarnings("unused")
    private void deleteMetric() {
        String message = Application.translate("SystemMonitoring", "Do you really want to delete metric?");
        if (Application.messageConfirmation(message)) {
            MetricListItem item = (MetricListItem) metricListWidget.currentItem();
            int row = metricListWidget.row(item);
            metricListWidget.takeItem(row);
            metricSet.emit();
        }
    }

    @SuppressWarnings("unused")
    private void editMetric() {
        MetricListItem metricListItem = (MetricListItem) metricListWidget.currentItem();
        DiagramEditorDialog chooseInst = new DiagramEditorDialog(metricListItem.getMetricSettings(), helper, getIds());
        this.setMinimumSize(250, 300);
        if (chooseInst.exec() == QDialog.DialogCode.Accepted.value()) {
            MetricListItem item = (MetricListItem) metricListWidget.currentItem();
            DiagramSettings ms = chooseInst.getMetricSettings();
            item.setText(ms.getTitle());
            item.setMetricSettings(ms);
        }
    }

    @Override
    public boolean isComplete() {
        return metricListWidget.count() > 1;
    }

    class MetricListItem extends QListWidgetItem {

        private /*MetricSettings*/ DiagramSettings metricSettings;

        MetricListItem(EntityModel metricState) {
            super();
            metricSettings = new DiagramSettings(metricState);
        }

        MetricListItem(/*MetricSettings*/DiagramSettings metricSettings) {
            super();
            this.metricSettings = metricSettings;
        }

        DiagramSettings getMetricSettings() {
            return metricSettings;
        }

        void setMetricSettings(DiagramSettings metricSettings) {
            this.metricSettings = metricSettings;
        }
    }
}
