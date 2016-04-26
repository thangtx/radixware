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
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EMetricKind;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.env.Application;


public class ChooseMetricDialog  extends ExplorerDialog{
    private QTreeWidget treeWidget;
    private final MetricHistWidget.IdsGetter  idsGetter;
    private List<Long> ids;
    private List<Long> typeIds;
    private EntityModel selectedMetric=null;
    
    
    public ChooseMetricDialog( QWidget parent,final GroupModel stateViewModel,final MetricHistWidget.IdsGetter  idsGetter,List<Long> ids){
         super(stateViewModel.getEnvironment(),parent, "ChooseMetricDialog");
         this.idsGetter=idsGetter;
         this.ids=ids;
         this.setWindowTitle(Application.translate("SystemMonitoring", "Select Metric"));
         treeWidget=new QTreeWidget(this);
         typeIds=new ArrayList<>();
         createUi(stateViewModel);
    }
    
     private void createUi(final GroupModel stateViewModel){
        this.setMinimumSize(200, 20);
        
        treeWidget.setObjectName("ChooseMetricTreeWidget"); 
        treeWidget.currentItemChanged.connect(this, "onItemChanged(QTreeWidgetItem,QTreeWidgetItem)");        
        
        dialogLayout().addWidget(treeWidget);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        this.setWindowModality(Qt.WindowModality.WindowModal);        
        this.setVisible(true);  
        fillTree(stateViewModel);
     }   

    @Override
     public void reject(){
         selectedMetric=null;
         super.reject();
     }
     
     private void onItemChanged(QTreeWidgetItem item1,QTreeWidgetItem item2){
         QTreeWidgetItem item=treeWidget.currentItem();
         if(item!=null && item instanceof MetricTreeItem){
            selectedMetric=((MetricTreeItem)treeWidget.currentItem()).getMetric(); 
         }else{
            selectedMetric=null;
         }
         getButton(EDialogButtonType.OK).setEnabled(selectedMetric!=null);
     }
     
     public EntityModel getSelectedMetric(){
         return selectedMetric;
     }
     
     private void fillTree(final GroupModel stateViewModel){
         try{
            if(stateViewModel!=null){
               final GroupModelReader metricsReader = 
                     new GroupModelReader(stateViewModel,EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
                 for (EntityModel metricModel: metricsReader) {
                    Long stateId = (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID)).getValueObject();                    
                    if(ids==null || !ids.contains(stateId)){
                        Long typeId = (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_TYPE_ID)).getValueObject();                    

                        if(!typeIds.contains(typeId)){ 
                            String kind = (String) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_KIND)).getValueObject();
                            boolean isUserMetric=kind.equals(EMetricKind.USER.getValue());

                            Long typeInstanceId = isUserMetric ? null : (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_INSTANCE_ID)).getValueObject();
                            Long typeUnitId = isUserMetric ? null : (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_UNIT_ID)).getValueObject();
                            String typeServiceUri = isUserMetric ? null : (String) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_SERVICE_URI)).getValueObject();
                            String typeTimingSection = isUserMetric ? null : (String) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_TIMING_SECTION)).getValueObject();
                            Long typeNetChannelId = isUserMetric ? null : (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_NETCHENNEL_ID)).getValueObject();

                            boolean isSetMetricSensor=isUserMetric || typeInstanceId!=null || typeUnitId!=null || typeServiceUri!=null ||
                                    typeTimingSection!=null || typeNetChannelId!=null;
                            String typeTitle = (String) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_TYPE_TITLE)).getValueObject();
                            String title=typeId+")"+typeTitle;
                            if(isSetMetricSensor){
                                QTreeWidgetItem item=new MetricTreeItem(metricModel,typeId);
                                item.setText(0,title+" "+metricModel.getTitle());                       
                                treeWidget.addTopLevelItem(item);
                            }else{                                
                                typeIds.add(typeId);
                                QTreeWidgetItem item=new MetricTreeItem(null,typeId);
                                item.setText(0,title);                       
                                treeWidget.addTopLevelItem(item);

                                QTreeWidgetItem childItem=new MetricTreeItem(metricModel,typeId);
                                childItem.setText(0,metricModel.getTitle());                       
                                item.addChild(childItem);
                            }                       
                        }else{
                            QTreeWidgetItem item=findItem(typeId);
                            if(item!=null){
                                QTreeWidgetItem childItem=new QTreeWidgetItem();
                                childItem.setText(0,metricModel.getTitle());
                                item.addChild(childItem);
                            }else{
                                throw new IllegalStateException("Can't find metric type item for state "+metricModel.getTitle());//!!!!!
                            }
                        }  
                    }
                    
               }
               if (metricsReader.wasInterrupted()){
                    throw new InterruptedException();
               }
               treeWidget.sortByColumn(0, Qt.SortOrder.AscendingOrder);
               //for(int i=0;i<tableWidget.rowCount();i++){               
               //   QTableWidgetItem itemSensor = new QTableWidgetItem();
               //   itemSensor.setText(((ChooseMetricStateWidget.MetricWidgetItem)tableWidget.item(i, 0)).getSensorTitle());
               //   tableWidget.setItem(i, 1, itemSensor);
               //}
               if(treeWidget.topLevelItemCount()>0){
                   QTreeWidgetItem curitem=treeWidget.topLevelItem(0);
                   treeWidget.setCurrentItem(curitem);
               }
               onItemChanged(null,null);
            }
        } catch (InterruptedException ex) { 
        }         
     }
     
     private MetricTreeItem findItem(Long typeId){
         for(int i=0;i<treeWidget.topLevelItemCount();i++){
             MetricTreeItem item=(MetricTreeItem)treeWidget.topLevelItem(i);
             if(item.getMetricType().equals(typeId))
                 return item;
         }
         return null;
     }
     
   /*  Long typeInstanceId = (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_INSTANCE_ID)).getValueObject();
                        Long typeUnitId = (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_UNIT_ID)).getValueObject();
                        String typeServiceUri = (String) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_SERVICE_URI)).getValueObject();
                        String typeTimingSection = (String) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_TIMING_SECTION)).getValueObject();
                        Long typeNetChannelId = (Long) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_TYPE_NETCHENNEL_ID)).getValueObject();
                        String kind = (String) metricModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_KIND)).getValueObject();

                        boolean isExtandable=typeInstanceId!=null || typeUnitId!=null || typeServiceUri!=null ||
                                typeTimingSection!=null || typeNetChannelId!=null;*/
     
     class MetricTreeItem extends QTreeWidgetItem{
         private EntityModel entityModel;
         private Long metricType;
         
         MetricTreeItem(EntityModel entityModel,Long metricType){
             this.entityModel=entityModel;
             this.metricType=metricType;
         }
         
         Long getMetricType(){
             return metricType;
         }
         
         EntityModel getMetric(){
             return entityModel;
         }
         
     }
    
}
