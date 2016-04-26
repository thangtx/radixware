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

import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizard;
import com.trolltech.qt.gui.QWizardPage;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.common.client.IClientEnvironment;


final class ChooseMetricWizard extends QWizard {

    private String key;
    private final ExplorerSettings settings;
    private final /*MetricHistWidget*/ MonitoringConfigHelper helper;
    private EntityModel type;
    private EntityModel state;
    private final MetricTypesPage metricTypePage;
    private final Set<Long> ids_;
    private IClientEnvironment environment;

    public ChooseMetricWizard(final /*MetricHistWidget*/ MonitoringConfigHelper helper, final GroupModel metricsGroup, final Set<Long> ids) {
        super(helper.getAdsBridge().getDashboardView());
        this.helper = helper;
        this.ids_ = ids;
        environment = metricsGroup.getEnvironment();
        this.settings = (ExplorerSettings) metricsGroup.getEnvironment().getConfigStore();
        String dlgName = "SystemMonitoring_ChooseMetricForDiagram";
        key =/* dlgName != null ?*/ SettingNames.SYSTEM + "/" + dlgName + "/wizard_geometry" /*: SettingNames.SYSTEM + "/" + getClass().getSimpleName() + "/wizard_geometry"*/;
        loadGeometryFromConfig();
        finished.connect(this, "saveGeometryToConfig()");

        metricTypePage = new MetricTypesPage(this, metricsGroup);
        addPage(metricTypePage);
        setWindowTitle(Application.translate("SystemMonitoring", "Select Metric"));
        metricTypePage.getPanel().reopen();
    }

    //public EntityModel getSelectedMetricType() {
    //    return type;
    //}
    public EntityModel getSelectedMetricState() {
        return state;
    }

    public void setType(EntityModel typeModel, final boolean isDoubleClick, final boolean isMetricDefined) {
        state = null;
        type = typeModel;
        if (metricTypePage != null) {
            if (type != null) {
                if (isMetricDefined) {
                    metricTypePage.setFinalPage(true);
                    GroupModel stateGroup;
                    try {
                        stateGroup = (GroupModel) type.getChildModel(helper.getAdsBridge().getMetricStateChildId());
                        GroupModelReader gropuReader = new GroupModelReader(stateGroup, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
                        for (EntityModel entity : gropuReader) {
                            Long id = (Long) entity.getProperty(helper.getAdsBridge().getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_ID)).getValueObject();
                            if (!ids_.contains(id)) {
                                state = entity;
                                break;
                            }
                        }
                    } catch (ServiceClientException ex) {
                        type.showException(ex);
                    } catch (InterruptedException ex) {
                    }
                    if (this.nextId() != -1) {
                        this.removePage(this.nextId());
                    }
                } else {
                    if (this.nextId() == -1) {
                        MetricStatePage metricStatePage = new MetricStatePage(this);
                        addPage(metricStatePage);
                    }
                    //this.button(WizardButton.NextButton).setVisible(true);
                    metricTypePage.setFinalPage(false);
                }
            } else {
                while (this.nextId() != -1) {
                    this.removePage(this.nextId());
                }
                metricTypePage.completeChanged.emit();
            }
        }
        if (isDoubleClick) {
            if (type == null) {
                accept();
            } else {
                this.button(WizardButton.NextButton).clicked.emit(Boolean.TRUE);
            }

        }
    }

    public void setState(EntityModel entityModel, final boolean isDoubleClick) {
        state = entityModel;
        if (isDoubleClick) {
            accept();
        }
    }

    @Override
    public void accept() {
        if (type == null) {
            String title = Application.translate("SystemMonitoring", "Can't select metric");
            String msg = Application.translate("SystemMonitoring", "This metric does not have data.");
            environment.messageInformation(title, msg);
        } else {
            super.accept();
        }
    }

    @SuppressWarnings("unused")
    protected void saveGeometryToConfig() {
        settings.writeQByteArray(key, saveGeometry());
    }

    protected void loadGeometryFromConfig() {
        if (settings.contains(key)) {
            restoreGeometry(settings.readQByteArray(key));
        }
    }

    private class MetricTypesPage extends QWizardPage {

        private final ChooseMetricTypeWidget metricTypePanel;

        public MetricTypesPage(ChooseMetricWizard parent, final GroupModel metricsGroup) {
            super(parent);
            this.setObjectName("MetricTypesPage");
            QVBoxLayout layout = new QVBoxLayout();
            metricTypePanel = new ChooseMetricTypeWidget(parent, metricsGroup, ids_, helper.getAdsBridge());
            metricTypePanel.setObjectName("ChooseDefinitionPanel");
            layout.addWidget(metricTypePanel);
            this.setLayout(layout);
        }

        @Override
        public void initializePage() {
            metricTypePanel.reopen();
        }

        ChooseMetricTypeWidget getPanel() {
            return metricTypePanel;
        }

        @Override
        public boolean isComplete() {
            return true;//ChooseMetricWizard.this.type!=null;
        }
    }

    private class MetricStatePage extends QWizardPage {

        private ChooseMetricStateWidget metricStatePanel;

        public MetricStatePage(ChooseMetricWizard parent) {
            super(parent);
            //this.setTitle(Application.translate("SystemMonitoring","Choose Metric State"));
            this.setObjectName("MetricStatePage");
            QVBoxLayout layout = new QVBoxLayout();
            metricStatePanel = new ChooseMetricStateWidget(parent, ids_, helper.getAdsBridge());
            metricStatePanel.setObjectName("ChooseDefinitionPanel");
            layout.addWidget(metricStatePanel);
            this.setLayout(layout);

        }

        @Override
        public void initializePage() {
            try {
                GroupModel model = (GroupModel) type.getChildModel(helper.getAdsBridge().getMetricStateChildId());
                metricStatePanel.setMetricModel(model);
            } catch (ServiceClientException ex) {
                type.showException(ex);
            } catch (InterruptedException ex) {
            }
        }

        @Override
        public boolean isComplete() {
            return ChooseMetricWizard.this.state != null;
        }
    }
}