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

import com.trolltech.qt.gui.*;
import java.awt.Color;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;


public class CorrelationMetricEditor extends BaseDiagramEditor {

    protected final ValEditor<String> edMetric;
    private ColorWidget normColorWidget;
    private ColorWidget warningColorWidget;
    private ColorWidget errorColorWidget;
    protected EntityModel metricStateView;
    private final Set<Long> ids;
    protected final MonitoringConfigHelper helper;
    
    CorrelationMetricEditor(/*MetricHistWidget*/MonitoringConfigHelper helper, Set<Long> ids) {
        super(helper.getAdsBridge().getDashboardView()/*,metricSettings*/);
        this.helper = helper;
        this.ids = ids;
        edMetric = new ValEditor<>(parent.getEnvironment(), this, new EditMaskNone(), false, true);
        edTitle = new QLineEdit();
        this.setWindowTitle(getEnvironment().getMessageProvider().translate("SystemMonitoring", "Diagram Editor"));
    }

    void open(DiagramSettings metricSettings) {
        createUI(metricSettings);
    }

    private void createUI(DiagramSettings metricSettings) {
        QVBoxLayout generalLayout = new QVBoxLayout();
        QGridLayout sensLayout = new QGridLayout();
        String lbText = Application.translate("SystemMonitoring", "Metric");
        QLabel lb = new QLabel(lbText + ":", this);
        final QAction action = new QAction(this);
        action.triggered.connect(this, "actChooseMetric()");
        edMetric.addButton("...", action);
        sensLayout.addWidget(lb, 0, 0);
        sensLayout.addWidget(edMetric, 0, 1);


        lbText = Application.translate("SystemMonitoring", "Name");
        lb = new QLabel(lbText + ":", this);
        sensLayout.addWidget(lb, 1, 0);
        sensLayout.addWidget(edTitle, 1, 1);

        if (metricSettings != null /*&& metricSettings instanceof MetricSettings*/) {
            /*MetricSettings ms = (MetricSettings) metricSettings;*/
            metricStateView = metricSettings.getMetricState();
            edMetric.setValue(metricSettings.getMetricName());
            edTitle.setText(metricSettings.getTitle());
        }

        QHBoxLayout colorLayout = new QHBoxLayout();
        QGroupBox colorGroupBox = createColorsPanel(metricSettings);
        colorLayout.addWidget(colorGroupBox);

        generalLayout.addLayout(sensLayout);
        generalLayout.addLayout(colorLayout);
        this.setLayout(generalLayout);
    }

    protected QGroupBox createColorsPanel(/*AbstractMetricSettings*/DiagramSettings metricSettings) {
        QGroupBox groupBox = new QGroupBox(Application.translate("SystemMonitoring", "Color Settings"));
        QGridLayout colorLayout = new QGridLayout();
        QColor color = metricSettings != null ? new QColor(metricSettings.getNormColor().getRGB()) : MetricHistWidget.defaultNornalColor;
        normColorWidget = new ColorWidget(this, SettingNames.SYSTEM, "", "" + "/" + "NORM_COLOR", Application.translate("SystemMonitoring", "Graph color"), color);
        normColorWidget.addToParent(0, colorLayout);

        color = metricSettings != null ? new QColor(metricSettings.getWarningColor().getRGB()) : MetricHistWidget.defaultWarningColor;
        warningColorWidget = new ColorWidget(this, SettingNames.SYSTEM, "", "" + "/" + "WARNING_COLOR", Application.translate("SystemMonitoring", "Warning color"), color);
        warningColorWidget.addToParent(1, colorLayout);

        color = metricSettings != null ? new QColor(metricSettings.getErrorColor().getRGB()) : MetricHistWidget.defaultErrorColor;
        errorColorWidget = new ColorWidget(this, SettingNames.SYSTEM, "", "" + "/" + "ERROR_COLOR", Application.translate("SystemMonitoring", "Error color"), color);
        errorColorWidget.addToParent(2, colorLayout);
        groupBox.setLayout(colorLayout);
        groupBox.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Preferred);
        return groupBox;
    }

    @SuppressWarnings("unused")
    protected void actChooseMetric() {
        EntityModel metricState = helper.chooseMetricState(ids);
        if (metricState != null) {
            EntityModel choice = helper.getMetricViewByMetricState(metricState, true);
            if (choice != null) {
                metricStateView = choice;
                String title = (String) metricState.getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_SENSOR_TITLE)).getValueObject();
                title = title == null ? "" : ", " + title;

                title = (String) metricStateView.getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_TYPE_TITLE)).getValueObject() + title;
                edMetric.setValue(title);
                edTitle.setText(title);
                metricSet.emit();
            } else {
            }
        }
    }

    @Override
    public /*AbstractMetricSettings*/ DiagramSettings getMetricSettings() {
        DiagramSettings metricSettings = new DiagramSettings(true);   //???
        updateSettings(metricSettings);
        metricSettings.setTitle(edTitle.text());
        metricSettings.setKind((String) metricSettings.getMetricState().getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_KIND)).getValueObject());
        metricSettings.setStateId((long) metricSettings.getMetricState().getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID)).getValueObject());
        metricSettings.setDiagramType(helper.getDiagramType(metricSettings.getKind()));
        return metricSettings;
    }

    protected void updateSettings(/*MetricSettings*/DiagramSettings metricSettings) {
        metricSettings.setMetricName(edMetric.getValue());
        metricSettings.setMetricState(metricStateView);
        metricSettings.setNormColor(new Color(normColorWidget.getColor().rgb()));
        metricSettings.setWarningColor(new Color(warningColorWidget.getColor().rgb()));
        metricSettings.setErrorColor(new Color(errorColorWidget.getColor().rgb()));
    }
    
    final protected IClientEnvironment getEnvironment(){
        return helper.getAdsBridge().getDashboardView().getEnvironment();
    }

    @Override
    public boolean isComplete() {
        return edMetric != null && edMetric.getValue() != null && !edMetric.getValue().isEmpty();
    }
}
