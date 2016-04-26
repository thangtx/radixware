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

package org.radixware.kernel.server.reports;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;
import org.radixware.kernel.common.enums.EReportChartSeriesType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;


public class ChartDataSet {
     private final int groupIndex;
     private final RadPropDef seriesPropDef;
     private final RadPropDef domainPropDef;
     private final RadPropDef valuePropDef;
     private final Map<Object,List<DataSetItem>> dataSetMap;
     private final AdsReportChartSeries series;
     private int domainAxisIndex;
     private int rangeAxisIndex;
     
     ChartDataSet(int groupIndex, RadPropDef seriesPropDef, RadPropDef categoryPropDef,
             RadPropDef valuePropDef,final AdsReportChartSeries series, int domainAxisIndex, int rangeAxisIndex){
         this.groupIndex=groupIndex;
         this.seriesPropDef=seriesPropDef;
         this.domainPropDef=categoryPropDef;
         this.valuePropDef=valuePropDef;
         this.series=series;
         dataSetMap=new HashMap<>();
         this.domainAxisIndex=domainAxisIndex;
         this.rangeAxisIndex=rangeAxisIndex;
     } 
     
     public int getRangeAxisIndex(){
         return rangeAxisIndex;
     }
     
     public int getDomainAxisIndex(){
         return domainAxisIndex;
     }
     
     public int getGroupIndex(){
         return groupIndex;
     }
     
     RadPropDef getSeriesPropDef(){
         return seriesPropDef;
     }
     
     public RadPropDef getDomainPropDef(){
         return domainPropDef;
     }
     
     public RadPropDef getRangePropDef(){
         return valuePropDef;
     }
     
     void addDataSetItem(Object seriesVal,Object categoryVal,Object valueVal){         
         if(!dataSetMap.containsKey(seriesVal)){
            dataSetMap.put(seriesVal, new LinkedList<DataSetItem>());
         }
         List<DataSetItem> dataSet=dataSetMap.get(seriesVal);
         dataSet.add(new DataSetItem(categoryVal,valueVal));
         
     }
     
    public Map<Object,List<DataSetItem>> getDataSetMap(){
         return dataSetMap;
    }
    
    public EReportChartSeriesType getSeriesType(){
        return series.getSeriesType();
    }
    
    public AdsReportChartCell getOwnerCell(){
        if(series.getContainer() instanceof AdsReportChartCell)
            return (AdsReportChartCell)series.getContainer();
        return null;
    }
    
     public Id getSeriesTitleId(){
        return series.getTitleId();
    }
             
     public String getSeriesName(){
        return series.getName();
    }
     
    public boolean getIsSeriesItemLabelShow(){
        return series.isShowItemLabel();
    }
     
     public static class DataSetItem{
         private Object x;
         private Object y;
         
         DataSetItem(Object x,Object y){
             this.x=x;
             this.y=y;
         }
         
         public Object getX(){
             return x;  
         }
         
         public Object getY(){
             return y; 
         }        
     }
     
    /* public static class CategoryComporator implements Comparator<DataSetItem>{

        @Override
        public int compare(DataSetItem o1, DataSetItem o2) {
            String x1=(String)o1.x.toString();
            String x2=(String)o2.x.toString();
            return x1.compareTo(x2);
        }
         
     }*/
}
