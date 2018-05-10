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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.text.DecimalFormat;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.kernel.explorer.editors.monitoring.tree.MetricInfoGetter.InstanceInfo;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.monitoringcommand.SysMonitoringRq;


public class InstanceTreeItem extends TreeItem {
    private EState state;
    private boolean isCalculated = false;
    private boolean sortByName = false; 

    private static enum EState {
        STARTED, STOPPED
    }
    //id - instance id
    public InstanceTreeItem(UnitsWidget.IdsGetter idsGetter,MetricInfoGetter.InstanceInfo instanceInfo,final Model groupModel) {
        super(idsGetter, instanceInfo, groupModel, null);      
    }
    
    public void setSortByName(boolean sortByName) {
        this.sortByName = sortByName;
    }
    
    public boolean isSortByName() {
        return sortByName;
    }
    
    @Override
    public EEventContextType getContextType() {
        return EEventContextType.SYSTEM_INSTANCE;
    }

    private EState calcState(boolean isStarted) {
        if (isStarted) {
            return EState.STARTED;
        }
        return EState.STOPPED;
    }
    
    @Override
    public boolean operator_less(QTreeWidgetItem qtwi) { 
        return super.operator_less(qtwi);
    }
    
    @Override
    protected void updateMetricDate(MetricInfoGetter metricInfo){
        InstanceInfo instanceInfo=metricInfo.getInstanceInfo(getId());

        if(instanceInfo!=null){
            MetricData md0 = (MetricData) this.data(0, ItemDataRole.UserRole);
            Boolean isStarted =instanceInfo.isStarted();
            EState newState = calcState(isStarted);
            if (md0 == null) {                
                state = newState;
                md0 = new MetricData(MetricData.Type.TEXT);
                md0.addIcon(getStateIcon());
                md0.addIcon(instanceInfo.getIcon());
                this.setData(0, ItemDataRole.UserRole, md0);
                this.setText(0, getTitle());
                String toolTip = state == EState.STARTED ? Application.translate("SystemMonitoring", "Started")
                        : Application.translate("SystemMonitoring", "Stopped");
                this.setToolTip(0, toolTip);
            }else{            
                if (newState != state) {
                    this.state = newState;
                    md0.getIconList().remove(0);
                    md0.addIcon(0, getStateIcon());
                    updateText(0);
                }
            }
                
            MetricData md = (MetricData) this.data(1, ItemDataRole.UserRole);
            Long maxArteInstCnt = instanceInfo.getMaxArteInstCnt();
            Long arteInstCnt = instanceInfo.getArteInstCnt();
            if(md==null){
                md = new MetricData(MetricData.Type.DIAGRAM);
                this.setData(1, ItemDataRole.UserRole, md);
                String toolTip = Application.translate("SystemMonitoring", "The relation between the number of active ARTEs and the maximum number of ARTEs");//"Количество активных ARTE по отношению к максимальному количеству ARTE" 
                this.setToolTip(1, toolTip);
            }
            Double val=instanceInfo.getInstArteSessionCnt();
            if (val != null && maxArteInstCnt != 0) {
                md.setVal(val / maxArteInstCnt * 100);
                this.setText(1, "");
            }else{
                this.setText(1, "");
                this.setTextAlignment(1, Qt.AlignmentFlag.AlignCenter.value());
            } 
            updateText(1);
            if(isStarted){
                MetricData md1 = (MetricData) this.data(2, ItemDataRole.UserRole);
                DecimalFormat f = new DecimalFormat("0.00");
                String s= val!=null ? f.format(val) :"-";
                String strActiveArteInst = Application.translate("SystemMonitoring", "Active ARTEs")+" = ";
                String strArteInst = Application.translate("SystemMonitoring", "ARTEs")+" = ";
                String strMaxArteInst = Application.translate("SystemMonitoring", "Max ARTEs")+" = ";
                String text=strActiveArteInst + s + ";  " + strArteInst + arteInstCnt + ";  " + strMaxArteInst + maxArteInstCnt;
                if(md1==null){              
                    md1 = new MetricData(MetricData.Type.TEXT);
                    this.setData(2, ItemDataRole.UserRole, md1);
                    //String toolTip = Application.translate("SystemMonitoring", "Active ARTEs / Number of ARTEs / Maximum number of ARTEs");
                    //this.setToolTip(2, toolTip);
                }
                this.setText(2, text);
                updateText(2);
            }
        }        
    }

    @Override
    protected UnitsTree.ExpandCommandEvent getExpandEvent(SysMonitoringRq sysMonitoringRq) {
        SysMonitoringRq.ExpandedInstances expInst = sysMonitoringRq.addNewExpandedInstances();
        SysMonitoringRq.ExpandedInstances.Instance instance = SysMonitoringRq.ExpandedInstances.Instance.Factory.newInstance();
        instance.setId(getId());
        expInst.getInstanceList().add(instance);
        return new UnitsTree.ExpandCommandEvent(this, sysMonitoringRq);
    }
    
    @Override
    protected QIcon getStateIcon() {
        if (state == EState.STARTED) {
            return ExplorerIcon.getQIcon(UnitsWidget.UnitsWidgetIcons.STARTED);
        }
        if (state == EState.STOPPED) {
            return ExplorerIcon.getQIcon(UnitsWidget.UnitsWidgetIcons.STOPPED);
        }
        return null;
    }

    public boolean isStarted() {
        return state == EState.STARTED;
    }

    public boolean isDiagramm() {
        return true;
    }

    @Override
    public boolean isCalculated() {
        return isCalculated;
    }

    @Override
    protected void setIsCalculated(boolean isCalc) {
        isCalculated = isCalc;
    }
    
    @Override
    protected Id getTableId(){
        return idsGetter.getInstanceTableId();
    }
}
