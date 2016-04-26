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

package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.gui.QColor;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.utils.XmlColor;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.AbstractMetricSettings;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.CorrelationMetricSettings;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.MetricSettings;
import org.radixware.schemas.monitoringSettings.MonitoringDiagram;
import org.radixware.schemas.monitoringSettings.MonitoringMetricSettings;


public class CorrelationMetricView extends AbstaractMetricView{    
   
    CorrelationMetricView(AbstractMetricSettings metricSettings, MetricHistWidget parent,MetricHistWidget.MetricPageWidget page, final IClientEnvironment environment,final MetricHistWidget.IdsGetter idsGetter,int index){
        super(metricSettings, parent, page,  environment, idsGetter);
        diagramType=MetricHistWidget.EDiagramType.CORRELATION;
        String diagramTitle=metricSettings!=null? metricSettings.getTitle():"";
        diagramPanel=new DiagramPanel(parent, diagramTitle, diagramType, environment, false,index);
    }   
    
    @Override
    public List<Long> getMetricStateId(){
        List<Long> metricStatePids=new ArrayList<>();
        //CorrelationMetricSettings correlMetricSettings=(CorrelationMetricSettings)metrisSettings;
        for(MetricSettings ms: getMetricSettings().getMetricSettingsList()){
            metricStatePids.add((Long) ms.getMetricState().getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID)).getValueObject());
            //metricStatePids.add(Long.getLong(ms.getMetricStateId().toString()));
        }
        return metricStatePids;
    }
    
    @Override
    protected List<MetricView.MetricValue> createValues() throws InterruptedException {
        List<MetricView.MetricValue> vals = new LinkedList<>();
        CorrelationMetricSettings correlMetricSettings=(CorrelationMetricSettings)metrisSettings;
        for(MetricSettings ms:correlMetricSettings.getMetricSettingsList()){
            String kind = (String) ms.getMetricState().getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_KIND)).getValueObject();             
            MetricHistWidget.EDiagramType metricKind=getDiagramType(kind); 
            BigDecimal val;
            QColor color=ms.getNormColor();
            if(metricKind==MetricHistWidget.EDiagramType.STATISTIC)
                val=(BigDecimal)ms.getMetricState().getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_AVG_VAL)).getValueObject();
            else
                val=(BigDecimal)ms.getMetricState().getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_END_VAL)).getValueObject();
            Double[] errorArea=calcErrorArea(ms.getMetricState());
            Double[] warnArea=calcWarnArea(ms.getMetricState());
            if(isInArea(errorArea,val.doubleValue())){
                color=ms.getErrorColor();
            }else if(isInArea(warnArea,val.doubleValue())){
                color=ms.getWarningColor();
            }
            vals.add(new MetricView.CorrelationValue(ms.getTitle(), val.doubleValue(),color));
        }
        return vals; 
    }
    
    private boolean isInArea(Double[] area,Double val){
        return area!= null && val!=null && ((area[0]!= null && val <= area[0]) || (area[1] != null && val > area[1]));
    }
    
     @Override
    public CorrelationMetricSettings getMetricSettings(){
        return (CorrelationMetricSettings)metrisSettings;
    }
    
     @Override
    public void updateAsinc(GroupModel metricsGroup,/*Timestamp begTime,Timestamp endTime,*/boolean isLast) {
        super.updateAsinc(metricsGroup,/* begTime, endTime,*/isLast);
        try {
            for(MetricSettings ms:getMetricSettings().getMetricSettingsList()){            
                Pid pidState=new Pid(metricsGroup.getSelectorPresentationDef().getTableId(), ms.getMetricState().getPid());
                int indexstate=metricsGroup.readToEntity(pidState);
                EntityModel metricState=metricsGroup.getEntity(indexstate);
                ms.setMetricState(metricState);
            }
            updateDiagram(true);
        } catch (BrokenEntityObjectException ex) {
            Logger.getLogger(CorrelationMetricView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
        } catch (ServiceClientException ex) {
            metricsGroup.showException(ex);
            //Logger.getLogger(CorrelationMetricView.class.getName()).log(Level.SEVERE, null, ex);
        }finally{         
        /*if(histModel!=null){
                histModel.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.SINCE)).setValueObject(begTime);
                histModel.reset();
                histModel.readMoreAsync().addListener(new MetricView.MetricResponseListener());
            }else*/ if(isLast){
                    page.setUpdatesEnabled(true);
                }
        }
    }

    @Override
    public void update(/*Timestamp begTime, Timestamp endTime,*/AbstractMetricSettings metricSettings) {
        setMetricSettings(metricSettings);
        diagramPanel.setTitle(metricSettings.getTitle());
        updateDiagram(true);
    }
    
    @Override
     public void appendTo(MonitoringDiagram xMetricDiagram){
        xMetricDiagram.setIsPie(true);
        xMetricDiagram.setName(diagramPanel.getTitle());
         
        CorrelationMetricSettings metricSettings=getMetricSettings();
        xMetricDiagram.setIsShowVals(metricSettings.isShowValues());
        xMetricDiagram.setIsPercent(metricSettings.isPercent());
        for(MetricSettings ms:metricSettings.getMetricSettingsList()){
            MonitoringMetricSettings xMetricSettings=xMetricDiagram.addNewMetricSettings();
            xMetricSettings.setTitle(ms.getTitle());
            xMetricSettings.setMetricName(ms.getMetricName());
            Long stateId=(Long) ms.getMetricState().getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID)).getValueObject();
            xMetricSettings.setMetricStateId(stateId);
            xMetricSettings.setMetricStatePid(ms.getMetricState().getPid().toString());
            
            xMetricSettings.setNormalColor(XmlColor.mergeColor(new Color(ms.getNormColor().rgb())));
            xMetricSettings.setWarningColor(XmlColor.mergeColor(new Color(ms.getWarningColor().rgb())));
            xMetricSettings.setErrorColor(XmlColor.mergeColor(new Color(ms.getErrorColor().rgb())));
        }         
     }
}
