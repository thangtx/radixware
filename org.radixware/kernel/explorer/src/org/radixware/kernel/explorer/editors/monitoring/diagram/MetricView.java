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

import com.trolltech.qt.QNoNativeResourcesException;
import java.awt.Color;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.IResponseListener;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.utils.XmlColor;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.EDiagramType;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.EPropertyName;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.IdsGetter;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.MetricPageWidget;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.AbstractMetricSettings;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.HistoricalMetricSettings;
import org.radixware.schemas.monitoringSettings.MonitoringDiagram;
import org.radixware.schemas.monitoringSettings.MonitoringMetricSettings;
import org.radixware.schemas.monitoringSettings.MonitoringTimeRange;
import org.radixware.schemas.monitoringSettings.MonitoringValueRange;


public class MetricView extends AbstaractMetricView{
    private GroupModel histModel=null;
    
    MetricView(AbstractMetricSettings metricSettings, MetricHistWidget parent,MetricPageWidget page, final IClientEnvironment environment,final IdsGetter idsGetter,int index){
       super(metricSettings, parent, page,  environment, idsGetter);
       String diagramTitle="";
       if(metricSettings!=null  && (metricSettings instanceof HistoricalMetricSettings)){
           HistoricalMetricSettings hms= (HistoricalMetricSettings)metricSettings;
           if(hms.getMetricState()!=null){
                String kind = (String) hms.getMetricState().getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_KIND)).getValueObject();             
                diagramType=getDiagramType(kind);      
                histModel=getHistModel(hms.getMetricState());
                hms.setDiagramType(diagramType);
           }
           diagramTitle=metricSettings.getTitle();
           calcTimeRange();
       }
       diagramPanel=new DiagramPanel(parent, diagramTitle, diagramType, environment, true,index);       
    }
    
    private void changeMetric(EntityModel metricState){
       String kind = (String) metricState.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_KIND)).getValueObject();             
       diagramType=getDiagramType(kind);      
       histModel=getHistModel(metricState);
       diagramPanel.setDiagram(diagramType);
    }    
    
    @Override
    public void update(AbstractMetricSettings metricSettings){   
        if(metricSettings!=null){
            HistoricalMetricSettings hms=(HistoricalMetricSettings)metricSettings; 
            if(hms.isAutoValueRange()){
                diagramPanel.setAutoValueRange(); 
            }else if(hms.getValueScale()!=null){
                double[] valRange=hms.getValueScale();
                diagramPanel.setValueScale(valRange[0], valRange[1]); 
            }
            diagramPanel.setNormalColor(new Color(hms.getNormColor().rgb()));
            diagramPanel.setWarningColor(new Color(hms.getWarningColor().rgb()));
            diagramPanel.setErrorColor(new Color(hms.getErrorColor().rgb()));
            if(!hms.getMetricState().getPid().equals(getMetricSettings().getMetricState().getPid())){
                changeMetric(hms.getMetricState());
            }
            Double[] errorVals= calcErrorArea(hms.getMetricState());
            Double[] warnVals = calcWarnArea(hms.getMetricState());            
            diagramPanel.setErrorWarnArea( errorVals[0], errorVals[1], warnVals[0], warnVals[1]);
            diagramPanel.setTitle(metrisSettings.getTitle());
            setMetricSettings(metricSettings);
        }
        
        if(histModel!=null){
            histModel.getProperty(idsGetter.getPropIdByName(EPropertyName.SINCE)).setValueObject(beginTime);
            try {
                histModel.reread();
                updateDiagram(false);
            } catch (ServiceClientException ex) {
                histModel.showException(ex);
            }
        }
    }    
    
     @Override
    public HistoricalMetricSettings getMetricSettings(){
        return (HistoricalMetricSettings)metrisSettings;
    }
    
    @Override
    public List<Long> getMetricStateId(){
        List<Long> metricStatePids=new ArrayList<>();
        metricStatePids.add((Long) getMetricSettings().getMetricState().getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_VIEW_STATE_ID)).getValueObject());
        return metricStatePids;
    }
    
    private boolean updating=false;
    @Override
    public void updateAsinc(GroupModel metricsGroup,boolean isLast) {
        if(updating) return;
        updating=true;
        super.updateAsinc(metricsGroup, isLast);
        try{
            Pid pidState=new Pid(metricsGroup.getSelectorPresentationDef().getTableId(), getMetricSettings().getMetricState().getPid());
            int indexstate=metricsGroup.readToEntity(pidState);
            EntityModel metricState=metricsGroup.getEntity(indexstate);
            if(metricState!=null){
                getMetricSettings().setMetricState(metricState); 
                Double[] errorVals= calcErrorArea(metricState);
                Double[] warnVals = calcWarnArea(metricState);
                diagramPanel.setErrorWarnArea( errorVals[0], errorVals[1], warnVals[0], warnVals[1]);
                diagramPanel.setTitle(metrisSettings.getTitle());
            }else{
                page.setUpdatesEnabled(true); 
            }
        } catch (BrokenEntityObjectException | ServiceClientException ex) {
             page.setUpdatesEnabled(true);
             metricsGroup.showException(ex);
        } catch (InterruptedException ex) {
             page.setUpdatesEnabled(true);
        }
        if(histModel!=null){
            calcTimeRange();
            histModel.getProperty(idsGetter.getPropIdByName(EPropertyName.SINCE)).setValueObject(beginTime);           
            histModel.reset();
            histModel.readMoreAsync().addListener(new MetricResponseListener());
        }else if(isLast){
            page.setUpdatesEnabled(true);
        }
     }
    
    private void calcTimeRange(){ 
        endTime = environment.getCurrentServerTime();
        Calendar cal = Calendar.getInstance(); 
        cal.setTime(endTime);
        cal.add(getMetricSettings().getPeriodUnit(),-getMetricSettings().getPeriod());        
        beginTime=new Timestamp(cal.getTimeInMillis());
    }
    
    private GroupModel getHistModel(EntityModel metricState){
       try {
            if(metricState!=null){
                Model metricHistModel = metricState.getChildModel(idsGetter.getMetricHistChildId());
                if (metricHistModel instanceof GroupModel){
                    ((GroupModel)metricHistModel).setReadPageSize(500);
                    return (GroupModel)metricHistModel;
                }
            }
       } catch (ServiceClientException ex) {
           metricState.showException(ex);
       } catch (InterruptedException ex) {
       }            
       return null;
    }
    
    private List<MetricValue> createStatistValues() throws InterruptedException {
        List<MetricValue> vals = new LinkedList<>();
        final GroupModelReader historyReader = 
           new GroupModelReader(histModel, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
        for (EntityModel historyEntry: historyReader) {
           Timestamp endTime = (Timestamp) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.END_TIME)).getValueObject();
           Timestamp begTime = (Timestamp) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.BEG_TIME)).getValueObject();
           BigDecimal avgVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.AVG_VAL)).getValueObject();
           BigDecimal maxVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.MAX_VAL)).getValueObject();
           BigDecimal minVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.MIN_VAL)).getValueObject();
           if(endTime!=null && begTime!=null && avgVal!=null){
              Double min= minVal!=null ? minVal.doubleValue():null;
              Double max= maxVal!=null ? maxVal.doubleValue():null;
              vals.add(new StatistMetricValue(begTime.getTime(),endTime.getTime(), avgVal.doubleValue(),max,min));
           }
        }
        if(historyReader.wasInterrupted()){
            throw new InterruptedException();
        }
        return vals;
    }    
    
   protected List<MetricValue> createDotValues()throws InterruptedException {
        List<MetricValue> vals = new LinkedList<>();
        final GroupModelReader historyReader = 
           new GroupModelReader(histModel, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
        for (EntityModel historyEntry: historyReader) {
           Timestamp endTime = (Timestamp) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.END_TIME)).getValueObject();
           BigDecimal endVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.END_VAL)).getValueObject();
           Timestamp begTime = (Timestamp) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.BEG_TIME)).getValueObject();
           if( endTime!=null && endVal!=null){
               long endTimeMs=endTime.getTime();
               long startTimeMs=endTimeMs;
               if( begTime!=null)
                    startTimeMs=begTime.getTime();
               vals.add(new DotMetricValue(startTimeMs, endTimeMs, endVal.doubleValue()));
           }
        }
        if(historyReader.wasInterrupted()){
            throw new InterruptedException();
        }
        return vals;
   }
    
    @Override
    protected List<MetricValue> createValues() throws InterruptedException {
        List<MetricValue> vals = new LinkedList<>();
        if(diagramType==EDiagramType.STATISTIC) 
            vals=createStatistValues();
        else if(diagramType==EDiagramType.STEP)
            vals=createStepValues(); 
        else if(diagramType==EDiagramType.DOT)
            vals=createDotValues();
        else{
            page.setUpdatesEnabled(true);
            throw new IllegalStateException("Unknown diagram type");
        }
        return vals;      
    }
    
    private List<MetricValue> createStepValues() throws InterruptedException {
        List<MetricValue> vals = new LinkedList<>();        
        final GroupModelReader historyReader = 
            new GroupModelReader(histModel, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));               
        for (EntityModel historyEntry: historyReader) {
           Timestamp endTime = (Timestamp) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.END_TIME)).getValueObject();
           BigDecimal startVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.BEG_VAL)).getValueObject();         
           BigDecimal endVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.END_VAL)).getValueObject(); 
           BigDecimal avgVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.AVG_VAL)).getValueObject();           
           if(avgVal!=null ){               
               BigDecimal maxVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.MAX_VAL)).getValueObject();
               BigDecimal minVal = (BigDecimal) historyEntry.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.MIN_VAL)).getValueObject();
               MetricValue val=new StepMetricValue(endTime.getTime(), startVal.doubleValue(), endVal.doubleValue(), maxVal.doubleValue(), minVal.doubleValue(),avgVal.doubleValue());
               vals.add(val);               
           }else if( endTime!=null && endVal!=null){
               MetricValue val=new StepMetricValue(endTime.getTime(),startVal.doubleValue(), endVal.doubleValue());
               vals.add(val);
           }           
        }
        if(historyReader.wasInterrupted()){
            throw new InterruptedException();
        }
        return vals;
    }
    
    @Override
     public void appendTo(MonitoringDiagram xMetricDiagram){
        MonitoringMetricSettings xMetricSettings=xMetricDiagram.addNewMetricSettings();
        xMetricSettings.setMetricStateId(getMetricStateId().get(0).intValue());
        xMetricSettings.setMetricStatePid(getMetricSettings().getMetricState().getPid().toString());

        xMetricSettings.setTitle(diagramPanel.getTitle());
        xMetricSettings.setMetricName(getMetricSettings().getMetricName());
        xMetricDiagram.setIsPie( false);
        xMetricDiagram.setIsHistogram( getMetricSettings().isHistogram());
        xMetricDiagram.setIsShowChangeRange(getMetricSettings().isShowChangeRange());
        MonitoringTimeRange xTimeRange=xMetricDiagram.addNewTimeRange();
        xTimeRange.setTimePeriod(getMetricSettings().getPeriod());
        xTimeRange.setPeriodUnit(getMetricSettings().getPeriodUnit());
        
        double[] valScale=getMetricSettings().getValueScale();
        if(!getMetricSettings().isAutoValueRange() && valScale!=null){
            MonitoringValueRange xValRange=xMetricDiagram.addNewValueRange();
            xValRange.setMinValue(valScale[0]);
            xValRange.setMaxValue(valScale[1]);
        }      
        xMetricSettings.setNormalColor(XmlColor.mergeColor(new Color(getMetricSettings().getNormColor().rgb())));
        xMetricSettings.setWarningColor(XmlColor.mergeColor(new Color(getMetricSettings().getWarningColor().rgb())));
        xMetricSettings.setErrorColor(XmlColor.mergeColor(new Color(getMetricSettings().getErrorColor().rgb())));
     }
    
    class MetricResponseListener implements IResponseListener{
        
        MetricResponseListener(){
            super();
        }

        @Override
        public void registerRequestHandle(RequestHandle handle) {
        }

        @Override
        public void unregisterRequestHandle(RequestHandle handle) {
        }

        @Override
        public void onResponseReceived(XmlObject response, RequestHandle handle) {
            if(histModel.hasMoreRows()){
                histModel.readMoreAsync().addListener(new MetricResponseListener());                 
            }else{ 
                try{
                    updateDiagram(true);                    
                }catch (QNoNativeResourcesException ex){  
                }finally{
                     updating=false;
                }
            }
        }

        @Override
        public void onServiceClientException(ServiceClientException exception, RequestHandle handle) {
            updating=false;
            page.setUpdatesEnabled(true);             
        }

        @Override
        public void onRequestCancelled(XmlObject request, RequestHandle handler) {
            updating=false;
            page.setUpdatesEnabled(true);
        }        
    }
}
