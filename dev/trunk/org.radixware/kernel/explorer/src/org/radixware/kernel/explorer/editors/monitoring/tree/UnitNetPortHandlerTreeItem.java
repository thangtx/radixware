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

package org.radixware.kernel.explorer.editors.monitoring.tree;

import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.schemas.monitoringcommand.SysMonitoringRq;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs.Channel;



public class UnitNetPortHandlerTreeItem extends UnitTreeItem {
    private final List<Long> loadedNetChannelIds = new ArrayList<>();
    private boolean isCalculated = false;
    private boolean sortByName = false; 

    public UnitNetPortHandlerTreeItem(UnitsWidget.IdsGetter idsGetter,  MetricInfoGetter.UnitInfo unitInfo, final boolean isInstanceStarted,final Model groupModel, QTreeWidgetItem parent) {
        super(idsGetter, unitInfo, isInstanceStarted, groupModel, parent);        
    }
  
    public void addNetChannels(final SysMonitoringRs res,final MetricInfoGetter metricInfo,final Model groupModel,final List<MonitoringTreeItemDecorator> decorators ) {
        for (Channel netChannel : res.getChannelList()) {
            if(netChannel.getUnitId().equals(getId())){
                NetChanelTreeItem netItem = new NetChanelTreeItem(idsGetter, metricInfo.createChannelInfo(netChannel), groupModel, this);
                netItem.updateMetricDate(metricInfo);
                this.addChild(netItem);
            }
        }
    }
    
    public List<Long> getLoadedNetChannelIds(){
        return loadedNetChannelIds;
    }
    
    @Override
    protected UnitsTree.ExpandCommandEvent getExpandEvent(SysMonitoringRq sysMonitoringRq) {
        SysMonitoringRq.ExpandedInstances.Instance expInst = sysMonitoringRq.addNewExpandedInstances().addNewInstance();
        expInst.setId(((InstanceTreeItem) parent()).getId());
        SysMonitoringRq.ExpandedInstances.Instance.ExpandedUnits.Unit unit = expInst.addNewExpandedUnits().addNewUnit();
        unit.setId(getId());
        return new UnitsTree.ExpandCommandEvent((InstanceTreeItem) parent(), sysMonitoringRq);
    }
    
    public boolean isCalculated() {
        return isCalculated;
    }

    protected void setIsCalculated(boolean isCalc) {
        isCalculated = isCalc;
    }
    
    public void setSortByName(boolean sortByName) {
        this.sortByName = sortByName;
    }
    
    public boolean isSortByName() {
        return sortByName;
    }
}
    