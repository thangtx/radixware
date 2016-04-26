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

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.enums.EMetricKind;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.env.Application;


final class ChooseMetricTypeWidget extends QWidget {
    private final ChooseMetricWizard parent_;
    private QListWidget listWidget;
    private GroupModel metricsGroup;
    private final MonitoringConfigHelper.AdsBridge  adsBridge;
    private Set<Long> ids_;
    
    public ChooseMetricTypeWidget(final ChooseMetricWizard parent, final GroupModel metricsGroup, final Set<Long> ids,final MonitoringConfigHelper.AdsBridge  adsBridge) {
        parent_=parent;
        ids_=ids;
        this.adsBridge=adsBridge;
        this.metricsGroup = metricsGroup;
        createUI();
    }

    private void createUI() {
        QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        this.setMinimumSize(200, 200);
        String lbText= Application.translate("SystemMonitoring" ,"Metric Type List");
        QLabel lb = new QLabel(lbText+":", this);
        listWidget = new QListWidget(this);       
        listWidget.itemActivated.connect(this, "onItemClick(QListWidgetItem)");
        listWidget.itemClicked.connect(this, "onItemClick(QListWidgetItem)");
        listWidget.itemDoubleClicked.connect(this,"onItemDoubleClick(QListWidgetItem)");
        createMetricTypeList();

        layout.addWidget(lb);
        layout.addWidget(listWidget);
        this.setLayout(layout);
    }
    
    @SuppressWarnings("unused")
    protected void onItemDoubleClick(QListWidgetItem item){
        if(item instanceof MetricWidgetItem){
            boolean isMetricDefined=((MetricWidgetItem)item).isMetricDefined();
            parent_.setType(((MetricWidgetItem)item).getEntity(),true,isMetricDefined);
        }
    }
    
    public void reopen(){
        QListWidgetItem item=listWidget.currentItem();
        if(item!=null){
            onItemClick(item);
        }
    }
    
    @SuppressWarnings("unused")
    protected void onItemClick(QListWidgetItem item){
        EntityModel entityModel=null;
        boolean isMetricDefined=false;
        if(item!=null && item instanceof MetricWidgetItem){
            entityModel=/*((MetricWidgetItem)item).getStateCount()>0 ?*/ ((MetricWidgetItem)item).getEntity();// : null;
            isMetricDefined= ((MetricWidgetItem)item).isMetricDefined();
        }           
        parent_.setType(entityModel,false,isMetricDefined);        
    }
    
    public void  setModel( final GroupModel metricsGroup){
        this.metricsGroup = metricsGroup;
        createMetricTypeList();        
    }
    
    public void createMetricTypeList() {
        listWidget.clear();
        try{
            if(metricsGroup!=null){
               final GroupModelReader metricsReader = 
                     new GroupModelReader(metricsGroup,EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
               for (EntityModel metricModel: metricsReader) {
                    String kind = (String) metricModel.getProperty(adsBridge.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_KIND)).getValueObject();
                    boolean isUserMetric=kind.equals(EMetricKind.USER.getValue());
                    int stateCount=getStateCount(metricModel,isUserMetric);
                    if(stateCount!=0){
                        String title=metricModel.getTitle();
                        if(title==null || title.isEmpty()){
                             Long id = (Long) metricModel.getProperty(adsBridge.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_ID)).getValueObject();
                             title=id.toString();
                        }
                        boolean isMetricDefined=isUserMetric||stateCount==1;
                        metricModel= stateCount<=0 /*&& !isUserMetric*/ ? null : metricModel; //RADIX-9103: Metric color is darkGray if there are no availible states.
                        MetricWidgetItem item = new MetricWidgetItem( title,metricModel,isMetricDefined, stateCount);                        
                        listWidget.addItem(item);
                        if(metricModel==null){
                            item.setForeground(new QBrush(QColor.darkGray));
                        }
                    }
               }
               if (metricsReader.wasInterrupted()){
                    throw new InterruptedException();
               }
               if(listWidget.count()>0){
                   listWidget.setCurrentRow(0);
                   onItemClick(listWidget.item(0));
               }else{
                   onItemClick(null);
               }
            }
        } catch (InterruptedException ex) { 
        }
    }
    
   /* public void createMetricTypeList() {
        listWidget.clear();
        try{
            if(metricsGroup!=null){
               final GroupModelReader metricsReader = 
                     new GroupModelReader(metricsGroup,EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
               for (EntityModel metricModel: metricsReader) {
                    String kind = (String) metricModel.getProperty(idsGetter_.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_KIND)).getValueObject();
                    boolean isUserMetric=kind.equals(EMetricKind.USER.getValue());
                    int stateCount=getStateCount(metricModel,isUserMetric);
                    if(stateCount>0){
                        String title=metricModel.getTitle();
                        if(title==null || title.isEmpty()){
                             Long id = (Long) metricModel.getProperty(idsGetter_.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_ID)).getValueObject();
                             title=id.toString();
                        }
                        boolean isMetricDefined=isUserMetric||stateCount==1;
                        MetricWidgetItem item = new MetricWidgetItem( title,metricModel,isMetricDefined);
                        listWidget.addItem(item);
                   }
               }
               if (metricsReader.wasInterrupted()){
                    throw new InterruptedException();
               }
               if(listWidget.count()>0){
                   listWidget.setCurrentRow(0);
                   onItemClick(listWidget.item(0));
               }else{
                   onItemClick(null);
               }
            }
        } catch (InterruptedException ex) { 
        }
    }*/
    
    private int getStateCount(final EntityModel metricModel,final boolean isUserMetric){       
        int stateCnt=0;
        try {
            GroupModel modelStateGroup = (GroupModel)metricModel.getChildModel(adsBridge.getMetricStateChildId());
            final GroupModelReader metricsReader = 
            new GroupModelReader(modelStateGroup,EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION)); 
            boolean hasMetricState=false;
            for (EntityModel stateModel : metricsReader) {
                Long id = (Long) stateModel.getProperty(adsBridge.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_ID)).getValueObject();
                if(isUserMetric){
                    return !ids_.contains(id)? 1:0;
                }                    
                if(!ids_.contains(id)){
                    stateCnt++;
                }
                hasMetricState=true;
            }
            stateCnt= !hasMetricState?-1:stateCnt;
            if (metricsReader.wasInterrupted()){
                throw new InterruptedException();
            }
        } catch (ServiceClientException ex) {
            metricModel.showException(ex);
        } catch (InterruptedException ex) {
        }
        return stateCnt;
    }

    /*public EntityModel getSelectedMetric() {
        MetricWidgetItem cutItem = (MetricWidgetItem) listWidget.currentItem();
        if (cutItem != null && cutItem.getStateCount()>0) {
            return cutItem.getEntity();
        }
        return null;
    }*/

    private class MetricWidgetItem extends QListWidgetItem {
        private final EntityModel entity;
        private final boolean isMetricDefined_;
        private final int stateCount;

        MetricWidgetItem(String title, final EntityModel entity,final boolean isMetricDefined,int stateCount) {
            this.entity = entity;
            this.setText(title );
            isMetricDefined_=isMetricDefined;
            this.stateCount=stateCount;
        }

        public EntityModel getEntity() {
            return entity;
        }
        
        public int getStateCount() {
            return stateCount;
        }
        
        public boolean isMetricDefined() {
            return isMetricDefined_;
        }
    }
}
