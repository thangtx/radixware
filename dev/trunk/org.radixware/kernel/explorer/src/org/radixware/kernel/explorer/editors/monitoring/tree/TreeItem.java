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

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.List;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.kernel.explorer.editors.monitoring.tree.UnitsTree.ExpandCommandEvent;
import org.radixware.schemas.monitoringcommand.SysMonitoringRq;

public abstract class TreeItem extends QTreeWidgetItem {

    private final MetricInfoGetter.TreeItemInfo metricInfo;
    protected Model eventLogContextModel = null;
    protected final Model groupModel;
    protected final EntityModel entity = null;
    protected final UnitsWidget.IdsGetter idsGetter;
    //private List<MonitoringTreeItemDecorator> decorators=new ArrayList<>();    

    /* protected void setEventLogContextModel(Id explorerItemId){

     if(getEntityModel()!=null){
     try {
     eventLogContextModel = getEntityModel().getChildModel( explorerItemId);
     } catch (ServiceClientException ex) {
     eventLogContextModel.showException(ex);
     } catch (InterruptedException ex) {                
     }
     }
     }
     
     public Model getEventLogContextModel(){
     return groupModel;
     }*/
    @Override
    public boolean operator_less(QTreeWidgetItem qtwi) {
        if (qtwi instanceof TreeItem) {
            return metricInfo.getId() > ((TreeItem) qtwi).getId();
        }
        return super.operator_less(qtwi);
    }

    protected TreeItem(UnitsWidget.IdsGetter idsGetter, final MetricInfoGetter.TreeItemInfo metricInfo, final Model groupModel, QTreeWidgetItem parent) {
        super(parent);
        this.metricInfo = metricInfo;
        this.groupModel = groupModel;
        this.idsGetter = idsGetter;
    }
    
    public boolean isCalculated() {
        return true;
    }

    protected void setIsCalculated(boolean isCalc) {
    }

    public MetricInfoGetter.TreeItemInfo getMetricInfo() {
        return metricInfo;
    }

    public String getTitle() {
        return metricInfo == null ? null : metricInfo.getTitle();
    }

    public String getClassGUID() {
        return metricInfo == null ? null : metricInfo.getClassGUID();
    }

    public EntityModel getEntityModel() {
        if (entity == null && groupModel != null) {
            try {
                Pid pid = new Pid(getTableId(), metricInfo.getId());
                Id ownerClassId = ((GroupModel) groupModel).getSelectorPresentationDef().getOwnerClassId();
                List<Id> editorPresentationIds = ((GroupModel) groupModel).getSelectorPresentationDef().getEditorPresentationIds();
                return EntityModel.openContextlessModel(groupModel.getEnvironment(), pid, ownerClassId, editorPresentationIds);
            } catch (ServiceClientException ex) {
                groupModel.showException(ex);
            } catch (InterruptedException ex) {
            }
        }
        return entity;
    }

    protected abstract Id getTableId();

    public Long getId() {
        return metricInfo == null ? null : metricInfo.getId();
    }

    /* public QIcon getIcon() {
     // public static final ClientIcon INHERITED = new ClientIcon("classpath:images/go_to_parent.svg", true);
     QIcon icon = ExplorerIcon.getQIcon(new RdxIcon(metricInfo.getIcon()));
     return  icon;
     }*/
    protected abstract void updateMetricDate(MetricInfoGetter metricInfo);
    protected ExpandCommandEvent getExpandEvent(SysMonitoringRq sysMonitoringRq) {
        return null;
    }

    protected void updateText(int index) {
        String text = this.text(index);
        this.setText(index, text + " ");
        this.setText(index, text);
    }

    protected abstract QIcon getStateIcon();

    public abstract EEventContextType getContextType();

    /*public Object getPropValueById(Id propId, EValType type){
     return ValAsStr.fromStr(metricInfo.getPropValueById(propId), type);
     //return metricInfo.getPropValueById(propId);
     }
    
     public MetricInfo getMetricValueByKind(String kind){
     return metricInfo.getMetricValueByKind(kind);
     }
    
     void addDecorator(MonitoringTreeItemDecorator decorator){
     if(!decorators.contains(decorator))
     decorators.add(decorator);
     }
    
     List<MonitoringTreeItemDecorator> getDecorators(){
     return decorators;
     }*/
}
