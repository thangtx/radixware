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

import com.trolltech.qt.gui.QDialog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.dashboard.DashTimeRangeProp;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.common.client.dashboard.EDashPropSource;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.schemas.xscml.Sqml;


public class MonitoringConfigHelper {

    private final AdsBridge adsBridge;
    private GroupModel metricsGroup = null;

    public MonitoringConfigHelper(final AdsBridge adsBridge) {
        this.adsBridge = adsBridge;
        metricsGroup = adsBridge.createStateViewGroup();
    }

    public AdsBridge getAdsBridge() {
        return adsBridge;
    }

    public EntityModel chooseMetricState(Set<Long> ids) {
        EntityModel metricModels = null;

        if (ids == null) {
            ids = new HashSet<>();
            ids.addAll(ids);
        }
        GroupModel metricType = adsBridge.createMetricTypeGroup();
        ChooseMetricWizard chooseInst = new ChooseMetricWizard(this, metricType, ids);
        if (chooseInst.exec() == QDialog.DialogCode.Accepted.value()) {
            metricModels = chooseInst.getSelectedMetricState();
        }

        return metricModels;
    }

    public EntityModel getMetricViewByMetricState(final EntityModel metricModels, boolean interactive) {
        EntityModel metricView = null;
        try {
            Long stateId = (Long) metricModels.getProperty(adsBridge.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_ID)).getValueObject();
            List<Long> stateIds = new ArrayList<>();
            stateIds.add(stateId);
            Sqml sqml = createCondition(stateIds);
            metricsGroup.reset();
            metricsGroup.removeCondition();
            metricsGroup.setCondition(sqml);
            metricsGroup.reread();
            metricView = metricsGroup.getEntity(0);
        } catch (BrokenEntityObjectException | ServiceClientException ex) {
            if (interactive) {
                adsBridge.getDashboardView().getEnvironment().processException(ex);
            } else {
                Logger.getLogger(MetricHistWidget.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
        }
        return metricView;
    }

    private Sqml createCondition(List<Long> ids) {
        Id propId = adsBridge.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID);
        Id tableId = metricsGroup.getSelectorPresentationDef().getTableId();
        Sqml xSqml = Sqml.Factory.newInstance();
        Sqml.Item.PropSqlName propName = xSqml.addNewItem().addNewPropSqlName();
        propName.setOwner(Sqml.Item.PropSqlName.Owner.THIS);
        propName.setPropId(propId);
        propName.setTableId(tableId);
        xSqml.addNewItem().setSql(" in( ");
        for (int i = 0, size = ids.size(); i < size; i++) {
            Sqml.Item.TypifiedValue typifiedValue = xSqml.addNewItem().addNewTypifiedValue();
            typifiedValue.setPropId(propId);
            typifiedValue.setTableId(tableId);
            typifiedValue.setValue(ids.get(i).toString());
            if (i < size - 1) {
                xSqml.addNewItem().setSql(", ");
            }
        }
        xSqml.addNewItem().setSql(" ) ");
        return xSqml;
    }

    protected DiagramSettings.EDiagramType getDiagramType(String kind) {
        RadEnumPresentationDef metricKindEnum = null;       
        if (getAdsBridge().getMetricKindEnumId() != null) {
            metricKindEnum = getAdsBridge().getDashboardView().getEnvironment().getApplication().getDefManager().getEnumPresentationDef(getAdsBridge().getMetricKindEnumId());

            RadEnumPresentationDef.Item kindItem = metricKindEnum.findItemByValue(kind);
            if (kindItem.getConstant().isInDomain(getAdsBridge().getMetricStatisticDomainId())) {
                return DiagramSettings.EDiagramType.STATISTIC;
            } else if (kindItem.getConstant().isInDomain(getAdsBridge().getMetricPointDomainId())) {
                return DiagramSettings.EDiagramType.DOT;
            } else if (kindItem.getConstant().isInDomain(getAdsBridge().getMetricEventDomainId())) {
                return DiagramSettings.EDiagramType.STEP;
            } else {
                return DiagramSettings.EDiagramType.CORRELATION;
            }
        } else {
            throw new IllegalArgumentError("Metric kind " + kind + " is not supported");
        }
    }

    public interface AdsBridge {

        public ExplorerWidget getDashboardView();

        public Set<Long> getStateIdsForExistingDiargarms();

        public GroupModel createMetricTypeGroup();

        public GroupModel createStateViewGroup();

        public Id getMetricTypeChildId();

        public Id getMetricStateChildId();

        public Id getMetricHistChildId();

        public Id getStateViewChildId();

        public Id getPropIdByName(MetricHistWidget.EPropertyName propName);

        public Id getMetricEventDomainId();

        public Id getMetricStatisticDomainId();

        public Id getMetricPointDomainId();

        public Id getMetricKindEnumId();
        
        public DashTimeRangeProp getDashboardTimeRange(EDashPropSource src);
        
        public DashTimeRangeProp getRefWidgetTimeRange(String dashGuid, String widgetGuid);
    }
}
