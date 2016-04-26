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

import java.util.Calendar;
import org.radixware.kernel.common.client.models.EntityModel;


public class HistoricalMetricSettings extends MetricSettings{
    private double[] valScale=null;
    private boolean isAutoRange;
    private boolean isHistogram;
    private boolean isShowChangeRange=true;
    /*private MetricHistWidget.EDiagramType diagramType;*/
    private int period=1;
    private int periodUnit=Calendar.HOUR;
    //private double visiblePeriod;
    
    //parent.setTimeSettings(choosePeriod.getPeriod(),choosePeriod.getPeriodUnit());
    
    public HistoricalMetricSettings(EntityModel metricState){
        super(metricState);
    }  
    
    public HistoricalMetricSettings(){
        super();
    }
  
    public void setValueScale(double minVal,double maxVal){
       if(valScale==null){
           valScale=new double[]{minVal,maxVal};
       }else{
           valScale[0]=minVal;
           valScale[1]=maxVal;
       }
    }
    
    public double[] getValueScale(){
        return valScale;
    }
    
    public void setAutoValueRange(boolean isAutoRange) {
        this.isAutoRange=isAutoRange;
        //valScale=null;
    }
    
    public boolean  isAutoValueRange() {
        //return valScale==null;
        return isAutoRange;
    }
    
    public void setIsHistogram(boolean isHistogram) {
        this.isHistogram=isHistogram;
    }
    
    public boolean  isHistogram() {
        return isHistogram;
    }  
    
    public void setIsShowChangeRange(boolean isShowChangeRange) {
        this.isShowChangeRange=isShowChangeRange;
    }
    
    public boolean  isShowChangeRange() {
        return isShowChangeRange;
    } 
    
//    public void setDiagramType( MetricHistWidget.EDiagramType diagramType) {
//        this.diagramType=diagramType;
//    }
    
//    public  MetricHistWidget.EDiagramType  getDiagramType() {
//        return diagramType;
//    }
    
    //private void setPeriod(double res){
    //   setPeriod( res, periodUnit);        
    //}
    
    public void setPeriod(double res,int timeUnit){
        if(res!= Math.round(res)){
           if(timeUnit==Calendar.HOUR){
              period=(int)(res * 60 * 60 * 1000);
           }else if(timeUnit==Calendar.MINUTE){
              period=(int)(res * 60 * 1000);
           }else if(timeUnit==Calendar.SECOND){
              period=(int)(res * 1000);
           }
           periodUnit=Calendar.MILLISECOND;           
        } else{
           period=(int)res;
           periodUnit=timeUnit;
        }       
    }
    
    public int getPeriod(){
        return period;
    }
    
    public int getPeriodUnit(){
        return periodUnit;
    }
}
