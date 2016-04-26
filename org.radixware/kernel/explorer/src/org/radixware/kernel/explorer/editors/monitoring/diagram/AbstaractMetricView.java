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
import java.sql.Timestamp;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.MetricPageWidget;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.AbstractMetricSettings;
import org.radixware.schemas.monitoringSettings.MonitoringDiagram;


public abstract  class AbstaractMetricView {
    protected final MetricHistWidget.IdsGetter idsGetter;
    protected DiagramPanel diagramPanel;
    protected final MetricHistWidget parent;
    protected final MetricPageWidget page;
    protected MetricHistWidget.EDiagramType diagramType;
    protected AbstractMetricSettings metrisSettings;
    private RadEnumPresentationDef metricKindEnum = null;
    protected Timestamp endTime;
    protected Timestamp beginTime;
    protected final IClientEnvironment environment;
    
    AbstaractMetricView( final AbstractMetricSettings metrisSettings,final MetricHistWidget parent,final MetricPageWidget page, final IClientEnvironment environment,final MetricHistWidget.IdsGetter idsGetter){
       this.environment=environment;
       this.idsGetter=idsGetter;
       this.parent=parent;
       this.page=page;
       this.metrisSettings=metrisSettings;
       if (idsGetter.getMetricKindEnumId() != null) {
            metricKindEnum = environment.getApplication().getDefManager().getEnumPresentationDef(idsGetter.getMetricKindEnumId());
       }
    }
    
    private boolean isLast=false;
    protected abstract List<MetricView.MetricValue> createValues() throws InterruptedException;
    //public abstract void changeMetric(/*EntityModel metricType,*/ EntityModel metricState);
    public abstract void update(/*Timestamp begTime,Timestamp endTime,*/AbstractMetricSettings metricSettings);
    public abstract List<Long> getMetricStateId();
    public abstract void appendTo(MonitoringDiagram xMetricDiagram);
    
    public void updateAsinc(final GroupModel metricsGroup,/*Timestamp begTime,Timestamp endTime,*/final boolean isLast){
        this.isLast=isLast;
    }     
     
    public AbstractMetricSettings getMetricSettings(){
        return metrisSettings;
    }
    
    public void  setMetricSettings(final AbstractMetricSettings  metrisSettings){
        this.metrisSettings= metrisSettings;
    }
    
    /*private String getSensorTitle(final EntityModel metricState) {
        return (String)metricState.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_STATE_SENSOR_TITLE)).getValueObject();         
    }
    
    public Long getMetricTypePid(){
        return (Long) metricType.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.METRIC_ID)).getValueObject();
    }*/   
    
    protected Double[] calcErrorArea(final EntityModel metric){
        final BigDecimal lowErrorVal=(BigDecimal)metric.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.LOW_ERROR_VAL)).getValueObject(); 
        final BigDecimal hightErrorVal=(BigDecimal)metric.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.HIGH_ERROR_VAL)).getValueObject();  
        final Double[] resVals=new Double[] { lowErrorVal   == null ? null : lowErrorVal.doubleValue(),
                                        hightErrorVal == null ? null : hightErrorVal.doubleValue() };
        return resVals;
    }
    
    protected Double[] calcWarnArea(final EntityModel metric){
        final BigDecimal lowWarnVal=(BigDecimal)metric.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.LOW_WARN_VAL)).getValueObject(); 
        final BigDecimal hightWarnVal=(BigDecimal)metric.getProperty(idsGetter.getPropIdByName(MetricHistWidget.EPropertyName.HIGH_WARN_VAL)).getValueObject();  
        final Double[] resVals=new Double[]{lowWarnVal==null?null:lowWarnVal.doubleValue(),hightWarnVal==null?null:hightWarnVal.doubleValue()};
        return resVals;
    }
    
    public void remove(){
        //parent.deleteDiagram(this);
        if(page.widget()!=null && page.widget().layout()!=null && page.widget().layout() instanceof GridLayout){
            final GridLayout layout=((GridLayout)page.widget().layout());
            layout.deleteItem(getDiagram());
            layout.removeWidget(getDiagram());
            getDiagram().setParent(null);   
            page.getMetricViews().remove(this);
       }
    }
    
    void updateDiagram(final boolean isAsinc){
        try {
            final List<MetricView.MetricValue> vals=createValues(); 
            diagramPanel.update(vals,beginTime,endTime,metrisSettings,isAsinc);
            if(isAsinc  && isLast)
                page.setUpdatesEnabled(true);
        } catch (InterruptedException ex) {
            page.setUpdatesEnabled(true);
        }
    }
    
    public void reread(final GroupModel metricsGroup) throws BrokenEntityObjectException, ServiceClientException, InterruptedException{       
        //int index=metricsGroup.readToEntity(new Pid(tableId,getMetricTypePid().toString()));
        //metricType= metricsGroup.getEntity(index);
        //Double[] errorVals= calcErrorArea(metricType);
        //Double[] warnVals= calcWarnArea(metricType);
        
        /*int index=metricsGroup.readToEntity(new Pid(tableId, getMetricStateId().toString()));
        metrisSettings.setMetricState(metricsGroup.getEntity(index));
        Double[] errorVals= calcErrorArea(metrisSettings.getMetricState());
        Double[] warnVals= calcWarnArea(metrisSettings.getMetricState());
        diagramPanel.setErrorWarnArea( errorVals[0], errorVals[1], warnVals[0], warnVals[1]);*/
    }
    
    public void changeTimeScale(final Timestamp begTime,final Timestamp endTime){        
        diagramPanel.changeTimeScale(begTime, endTime);
    }
    
    public DiagramPanel getDiagram(){
        return diagramPanel;
    }  
    
     protected MetricHistWidget.EDiagramType getDiagramType(final String kind){
       final RadEnumPresentationDef.Item kindItem=  metricKindEnum.findItemByValue(kind);
       if(kindItem.getConstant().isInDomain(idsGetter.getMetricStatisticDomainId())){
          return MetricHistWidget.EDiagramType.STATISTIC;
       }else if(kindItem.getConstant().isInDomain(idsGetter.getMetricPointDomainId())){
          return MetricHistWidget.EDiagramType.DOT;
       }else if(kindItem.getConstant().isInDomain(idsGetter.getMetricEventDomainId())){
          return MetricHistWidget.EDiagramType.STEP;
       }
       /*if(kind.equals(EMetricKind.INST_ARTE_INSTCNT.getValue()) || kind.equals(EMetricKind.INST_STOP.getValue())||
          kind.equals(EMetricKind.UNIT_STOP.getValue()) || kind.equals(EMetricKind.UNIT_HANG.getValue())||
          kind.equals(EMetricKind.UNIT_ISO8583_CONNECT.getValue())||
          kind.equals(EMetricKind.UNIT_ISO8583_LOGON.getValue()) || kind.equals(EMetricKind.NET_CLIENT_CONNECT.getValue()))
         return MetricHistWidget.EDiagramType.STEP;
       if(kind.equals(EMetricKind.PROFILING_CNT.getValue())|| kind.equals(EMetricKind.PROFILING_DURATION.getValue())||
          kind.equals(EMetricKind.PROFILING_FREQ.getValue())|| kind.equals(EMetricKind.PROFILING_PERCENT_CPU.getValue())||
          kind.equals(EMetricKind.PROFILING_PERCENT_DB.getValue())|| kind.equals(EMetricKind.PROFILING_PERCENT_EXT.getValue())||
          kind.equals(EMetricKind.INST_ARTE_SESSCNT.getValue()) || kind.equals(EMetricKind.INST_SERVICE_SESSCNT.getValue())||
          kind.equals(EMetricKind.UNIT_ARTE_SESSCNT.getValue()) || kind.equals(EMetricKind.UNIT_JOB_EXECCNT.getValue()) ||
          kind.equals(EMetricKind.UNIT_JOB_WAITCNT.getValue()) ||kind.equals(EMetricKind.NET_SERVER_CONNECT.getValue()) || 
          kind.equals(EMetricKind.INST_CPU_USAGE.getValue()) || kind.equals(EMetricKind.INST_MEMORY_CODE_CACHE.getValue()) ||
          kind.equals(EMetricKind.INST_MEMORY_PERM_GEN.getValue()) || kind.equals(EMetricKind.INST_MEMORY_HEAP.getValue())
               || kind.equals(EMetricKind.NET_CLIENT_CONNECT_TIME_PERCENT.getValue()))
         return MetricHistWidget.EDiagramType.STATISTIC;*/ 
        throw new IllegalArgumentError("Metric kind "+kind+" is not supported");
    }
    
     public static class MetricValue{
        protected long time;
        protected double val;
        
        public MetricValue(final long time,final double val){
            this.time=time;
            this.val=val;
        }
        
        public double getVal(){
            return val;
        }
        public long getTime(){
             return time;
        }        
    }
     
     public static class StepMetricValue extends MetricValue{
        private final double startVal;
        private double maxVal;
        private double minVal;
        private double avgVal;
        private boolean isIntermediate=false;
        
        public StepMetricValue(final long time,final double startVal,final double val){
            super(time,val);
            this.startVal=startVal;            
        }
        
        public StepMetricValue(final long time,final double startVal,final double val,final double maxVal,final double minVal,final double avgVal){
            super(time,val);
            this.startVal=startVal;
            this.maxVal=maxVal;
            this.minVal=minVal;
            this.avgVal=avgVal;
            this.isIntermediate=true;
        }
        
        public double getStartVal(){
            return startVal;
        }
        
        public double getMinVal(){
            return minVal;
        }
        
        public double getMaxVal(){
            return maxVal;
        }  
        
        public double getAvgVal(){
            return avgVal;
        } 
        
        public boolean isIntermediate(){
            return isIntermediate;
        } 
    }
     
     public static class DotMetricValue extends MetricValue{
        private final long startTime;
        private final long endTime;
        
        public DotMetricValue(final long beg_time,final long end_time,final double val){
            super( beg_time+((end_time-beg_time)/2), val);
            startTime=beg_time;
            endTime=end_time;
        }
        
        public long getStartTime(){            
            return startTime;
        }       
        
        public long getEndTime(){            
            return endTime;
        }        
    }
     
    public static class CorrelationValue extends MetricValue{
        protected String name;
        protected Color color;
        
        public CorrelationValue(final String name,final double val,final QColor color){
            super(0,val);
            this.name=name;
            this.color=new Color(color.rgb());
        }

        public String getName(){
             return name;
        } 
        
        public Color getColor(){
             return color;
        }
    }
    
    public static class StatistMetricValue extends DotMetricValue{
        //private double stdVal;
        private final Double maxVal;
        private final Double minVal;
        
        public StatistMetricValue(final long beg_time,final long end_time,final double avgVal,final Double maxVal,final Double minVal){
            super(beg_time, end_time, avgVal);
            this.minVal=minVal;
            this.maxVal=maxVal;
            //this.stdVal=maxVal-avgVal;
        }
        
        //public double getStdVal(){
        //    return stdVal;
        //}       
        
        public Double getMaxVal(){
             return maxVal;
        }
        
        public Double getMinVal(){
             return minVal;
        }
    }   
}
