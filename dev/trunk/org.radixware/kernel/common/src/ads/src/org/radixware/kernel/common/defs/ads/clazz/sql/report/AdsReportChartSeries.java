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

package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;

import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EReportChartSeriesType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsReportChartSeries extends  RadixObject  implements ILocalizedDef {
    private final AdsChartDataInfo seriesData;
    private final AdsChartDataInfo categoryData;
    private final AdsChartDataInfo valueData;
    private EReportChartSeriesType seriesType;
    private int groupIndex=-1;  
    private Id titleId = null;
    private boolean showItemLabel=false;
    
    public AdsReportChartSeries(final AdsReportChartCell chartCell,org.radixware.schemas.adsdef.ChartSeries xSeries){
        super();
        setContainer(chartCell);
        this.seriesType=xSeries.getSeriesType(); 
        this.groupIndex=xSeries.getGroupIndex(); 
        
        org.radixware.schemas.adsdef.ChartDataInfo xSeriesDataInfo=xSeries.getSeriesDataInfo();
        if(xSeriesDataInfo!=null){
            seriesData=new AdsChartDataInfo(xSeriesDataInfo,chartCell);
        }  else{
            seriesData=new AdsChartDataInfo(chartCell);
        } 
        org.radixware.schemas.adsdef.ChartDataInfo xCategoryDataInfo=xSeries.getCategoryDataInfo();
        if(xCategoryDataInfo!=null){
            categoryData=new AdsChartDataInfo(xCategoryDataInfo,chartCell);
        }else{
            categoryData=new AdsChartDataInfo(chartCell);
        }
        org.radixware.schemas.adsdef.ChartDataInfo xValueDataInfo=xSeries.getValueDataInfo();
        if(xValueDataInfo!=null){
            valueData=new AdsChartDataInfo(xValueDataInfo,chartCell);
        }else{
            valueData=new AdsChartDataInfo(chartCell);
        }
        if(xSeries.getName()!=null)
            setName(xSeries.getName());
        if (xSeries.isSetTitleId()) {
            this.titleId = xSeries.getTitleId();
        }
        if (xSeries.getIsShowItemLabel()) {
            this.showItemLabel = xSeries.getIsShowItemLabel();
        }
    }
    
    public AdsReportChartSeries(final AdsReportChartCell chartCell,String name){
        super();
        setContainer(chartCell);
        seriesData=new AdsChartDataInfo(chartCell);
        categoryData=new AdsChartDataInfo(chartCell);
        valueData=new AdsChartDataInfo(chartCell);
        setName(name);
     }
    
     public AdsReportChartSeries(final AdsReportChartCell chartCell,EReportChartSeriesType seriesType,String name){
        this(chartCell,name);
        this.seriesType=seriesType;
     }
    
    protected void appendTo(org.radixware.schemas.adsdef.ChartSeries xSeries){
        xSeries.setGroupIndex(groupIndex);
        xSeries.setName(getName());
        xSeries.setIsShowItemLabel(showItemLabel);
        if(seriesType!=null)
            xSeries.setSeriesType(seriesType);       
                
        if(seriesData!=null)
            seriesData.appendTo(xSeries.addNewSeriesDataInfo());
                
        if(categoryData!=null)
            categoryData.appendTo(xSeries.addNewCategoryDataInfo());
                
        if(valueData!=null)
            valueData.appendTo(xSeries.addNewValueDataInfo()); 
        
        if (titleId != null) {
            xSeries.setTitleId(titleId);
        }
    }  
    
    public Id getTitleId() {
        return titleId;
    }

    public void setTitleId(Id titleId) {
        if (!Utils.equals(this.titleId, titleId)) {
            this.titleId = titleId;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public void setSeriesType(EReportChartSeriesType seriesType){
        if (!Utils.equals(this.seriesType, seriesType)) {
            this.seriesType=seriesType;
            getContainer().setEditState(EEditState.MODIFIED);
        }        
    }
    
    public void  setGroupIndex(final int groupIndex){
        if (!Utils.equals(this.groupIndex, groupIndex)) {
            this.groupIndex=groupIndex;
            getContainer().setEditState(EEditState.MODIFIED);
        }        
    }
    
    public boolean isShowItemLabel() {
        return showItemLabel;
    }
    
    public void setIsShowItemLabel(boolean isShowItemLabel) {
        if (!Utils.equals(this.showItemLabel, isShowItemLabel)) {
            this.showItemLabel = isShowItemLabel;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public AdsChartDataInfo getSeriesData(){
        return seriesData;
    }
    
    public AdsChartDataInfo getDomainData(){
        return categoryData;
    }
    
    public AdsChartDataInfo getRangeData(){
        return valueData;
    }  
    
    public EReportChartSeriesType getSeriesType(){
        return seriesType;
    }    
        
    public int getGroupIndex(){
        return groupIndex; 
    }
    
    @Override
    public void collectDependences(List<Definition> list) {
         AdsPropertyDef prop =categoryData.findProperty();
         if (prop != null) {
            list.add(prop);
         }
         prop=valueData.findProperty();
         if (prop != null) {
            list.add(prop);
         }
         prop=seriesData.findProperty();
         if (prop != null) {
            list.add(prop);
         }
         
         final AdsMultilingualStringDef title = findTitle();
         if (title != null) {
            list.add(title);
         }
    }
     
    public AdsMultilingualStringDef findTitle() {
        return findLocalizedString(titleId);
    }
     
    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        if (stringId != null && getContainer()!=null) {
            final AdsReportClassDef report = ((AdsReportChartCell)getContainer()).getOwnerReport();
            if (report != null) {
                return report.findLocalizedString(stringId);
            }
        }
        return null;
    }
     
     @Override
    protected void onModified() {
        final AdsReportChartCell cell = ((AdsReportChartCell)getContainer());
        if (cell != null) {
            cell.onModified();
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(AdsReportChartSeries.this) {

            @Override
            public Id getId() {
                return titleId;
            }

            @Override
            public void updateId(Id newId) {
                titleId = newId;
                setEditState(EEditState.MODIFIED);
            }

            @Override
            public EAccess getAccess() {
                return EAccess.PRIVATE;
            }

            @Override
            public String getContextDescription() {
                return "Report Chart Series";
            }

            @Override
            public boolean isPublished() {
                return false;
            }
        });
    }
}