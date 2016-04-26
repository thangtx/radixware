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
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.IdsGetter;
import org.radixware.kernel.explorer.env.Application;


public class ChooseMetricStateWidget extends QWidget {

    private final ChooseMetricWizard parent_;
    private QTableWidget tableWidget;
    private GroupModel metricsGroup;
    private final MonitoringConfigHelper.AdsBridge adsBridge;
    private Set<Long> ids_;

    public ChooseMetricStateWidget(final ChooseMetricWizard parent, final Set<Long> ids, final MonitoringConfigHelper.AdsBridge adsBridge) {
        parent_ = parent;
        this.adsBridge = adsBridge;
        ids_ = ids;
        createUI();
    }

    private void createUI() {
        QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        this.setMinimumSize(200, 200);
        String lbText = Application.translate("SystemMonitoring", "Sensor List");
        QLabel lb = new QLabel(lbText + ":", this);
        tableWidget = new QTableWidget(this);
        tableWidget.itemActivated.connect(this, "onItemClick(QTableWidgetItem)");
        tableWidget.itemClicked.connect(this, "onItemClick(QTableWidgetItem)");
        tableWidget.itemDoubleClicked.connect(this, "onItemDoubleClick(QTableWidgetItem)");
        tableWidget.setSelectionMode(SelectionMode.SingleSelection);
        tableWidget.setSelectionBehavior(SelectionBehavior.SelectRows);
        tableWidget.setSortingEnabled(true);
        List<String> columnLabels = new ArrayList<String>();
        columnLabels.add(Application.translate("SystemMonitoring", "ID"));
        columnLabels.add(Application.translate("SystemMonitoring", "Name"));
        tableWidget.setColumnCount(columnLabels.size());
        tableWidget.setHorizontalHeaderLabels(columnLabels);
        for (int i = 0; i < columnLabels.size(); i++) {
            tableWidget.horizontalHeaderItem(i).setTextAlignment(Qt.AlignmentFlag.AlignCenter.value());
        }
        tableWidget.horizontalHeader().setResizeMode(0, ResizeMode.ResizeToContents);
        tableWidget.horizontalHeader().setResizeMode(1, ResizeMode.Stretch);
        tableWidget.verticalHeader().setHidden(true);
        createInstanceList();

        layout.addWidget(lb);
        layout.addWidget(tableWidget);
        this.setLayout(layout);
    }

    @SuppressWarnings("unused")
    protected void onItemDoubleClick(QTableWidgetItem item) {
        if (item.column() != 0) {
            item = tableWidget.item(item.row(), 0);
        }
        EntityModel entityModel = ((MetricWidgetItem) item).getMetricModel();
        parent_.setState(entityModel, true);
    }

    @SuppressWarnings("unused")
    protected void onItemClick(QTableWidgetItem item) {
        EntityModel entityModel = null;
        if (item != null) {
            if (item.column() != 0) {
                item = tableWidget.item(item.row(), 0);
            }
            entityModel = ((MetricWidgetItem) item).getMetricModel();
        }
        parent_.setState(entityModel, false);
    }

    public void setMetricModel(final GroupModel metricsGroup) {
        this.metricsGroup = metricsGroup;
        createInstanceList();
    }

    private void createInstanceList() {
        tableWidget.clearContents();
        tableWidget.setRowCount(0);
        try {
            if (metricsGroup != null) {
                final GroupModelReader metricsReader =
                        new GroupModelReader(metricsGroup, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
                int rowCount = 0;
                for (EntityModel metricModel : metricsReader) {
                    if (ids_ != null) {//sensors
                        Long id = (Long) metricModel.getProperty(adsBridge.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_ID)).getValueObject();
                        if (!ids_.contains(id)) {
                            String title = (String) metricModel.getProperty(adsBridge.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_SENSOR_TITLE)).getValueObject();
                            MetricWidgetItem itemId = new MetricWidgetItem(id.toString(), title, metricModel);
                            //itemId.setData(Role.UserRole.value(), metricModel);

                            //QTableWidgetItem itemSensor = new QTableWidgetItem();
                            //itemSensor.setText(title);
                            //itemSensor.setFlags(new ItemFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable)); 

                            tableWidget.insertRow(rowCount);
                            tableWidget.setItem(rowCount, 0, itemId);
                            rowCount++;
                        }
                    }
                }
                if (metricsReader.wasInterrupted()) {
                    throw new InterruptedException();
                }
                tableWidget.sortByColumn(0, Qt.SortOrder.AscendingOrder);
                for (int i = 0; i < tableWidget.rowCount(); i++) {
                    QTableWidgetItem itemSensor = new QTableWidgetItem();
                    itemSensor.setText(((MetricWidgetItem) tableWidget.item(i, 0)).getSensorTitle());
                    tableWidget.setItem(i, 1, itemSensor);
                }
                if (tableWidget.rowCount() > 0) {
                    QTableWidgetItem curitem = tableWidget.item(0, 0);
                    tableWidget.setCurrentItem(curitem);
                    onItemClick(curitem);
                } else {
                    onItemClick(null);
                }
            }
        } catch (InterruptedException ex) {
        }
    }

    public EntityModel getSelectedMetric() {
        MetricWidgetItem cutItem = (MetricWidgetItem) tableWidget.item(tableWidget.currentRow(), 0);
        if (cutItem != null) {
            return cutItem.getMetricModel();
        }
        return null;
    }

    private class MetricWidgetItem extends QTableWidgetItem {

        private final EntityModel metricModel_;
        private final String sensorTitle_;

        @Override
        public boolean operator_less(QTableWidgetItem qtwi) {
            Long val1 = Long.valueOf(super.text());
            Long val2 = Long.valueOf(qtwi.text());
            return val1 < val2;
        }

        MetricWidgetItem(final String title, final String sensorTitle, final EntityModel metricModel) {
            this.setText(title);
            sensorTitle_ = sensorTitle;
            metricModel_ = metricModel;
        }

        EntityModel getMetricModel() {
            return metricModel_;
        }

        String getSensorTitle() {
            return sensorTitle_;
        }
    }
}
