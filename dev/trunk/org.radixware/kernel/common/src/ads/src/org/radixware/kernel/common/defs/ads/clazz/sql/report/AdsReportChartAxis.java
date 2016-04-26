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

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EReportChartAxisLabelPositions;
import org.radixware.kernel.common.enums.EReportChartAxisType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.CellFormat;


public class AdsReportChartAxis extends  RadixObject  implements ILocalizedDef {    
    private Id titleId = null;
    private EReportChartAxisType axisType;
    private EReportChartAxisLabelPositions labelPosition=EReportChartAxisLabelPositions.STANDARD;
    private boolean autoRangeIncludeZero = false;
    private boolean rightOrTop = false;
    private boolean dateAxis = false;
    private boolean visible = true;
    private AdsReportFormat format=new AdsReportFormat();
    
    public AdsReportChartAxis(final AdsReportChartCell chartCell,EReportChartAxisType axisType,org.radixware.schemas.adsdef.ChartAxis xAxis){
        super();
        setContainer(chartCell);
        if(xAxis.isSetAutoRangeIncludeZero()){
            this.autoRangeIncludeZero=xAxis.getAutoRangeIncludeZero();
        }
        if(xAxis.isSetRightOrTop()){
            this.rightOrTop=xAxis.getRightOrTop();
        }
        if(xAxis.isSetDateAxis()){
            this.dateAxis=xAxis.getDateAxis();
        }
        if(xAxis.isSetVisible()){
            this.visible=xAxis.getVisible();
        }

        if(xAxis.getName()!=null)
            setName(xAxis.getName());
        if (xAxis.isSetTitleId()) {
            this.titleId = xAxis.getTitleId();
        }
        if (xAxis.isSetAxisLabelPosition()) {
            this.labelPosition = xAxis.getAxisLabelPosition();
        }
        if(xAxis.isSetFormat()){
            this.format=new AdsReportFormat(xAxis.getFormat());
        }
        this.axisType=axisType;
    }
        
    public AdsReportChartAxis(final AdsReportChartCell chartCell,String name,EReportChartAxisType axisType){
        super(name);
        setContainer(chartCell);
        this.axisType=axisType;
    } 
        
    public EReportChartAxisType getAxisType(){
        return axisType;
    }
    
    protected void appendTo(org.radixware.schemas.adsdef.ChartAxis xAxis){
        xAxis.setName(getName());
        xAxis.setAutoRangeIncludeZero(autoRangeIncludeZero);
        xAxis.setRightOrTop(rightOrTop);
        xAxis.setDateAxis(dateAxis);
        xAxis.setVisible(visible);
        xAxis.setAxisLabelPosition(labelPosition);
        CellFormat xFormat=xAxis.addNewFormat();
        format.appendTo(xFormat, getEditState());
        if(titleId!=null)
            xAxis.setTitleId(titleId);       
    } 
    
    public EReportChartAxisLabelPositions getAxisLabelPosition(){
        return labelPosition;
    }
    
    public void setAxisLabelPosition(EReportChartAxisLabelPositions labelPosition){
        if (!Utils.equals(this.labelPosition, labelPosition)) {
            this.labelPosition=labelPosition;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public boolean isAutoRangeIncludeZero(){
        return autoRangeIncludeZero;
    }
    
    public void setIsAutoRangeIncludeZero(boolean isAutoRangeIncludeZero){
        if (!Utils.equals(this.autoRangeIncludeZero, isAutoRangeIncludeZero)) {
            this.autoRangeIncludeZero=isAutoRangeIncludeZero;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public boolean isRightOrTop(){
        return rightOrTop;
    }
    
    public void setIsRightOrTop(boolean isRightOrTop){
        if (!Utils.equals(this.rightOrTop, isRightOrTop)) {
            this.rightOrTop=isRightOrTop;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public boolean isDateAxis(){
        return dateAxis;
    }
    
    public void setIsDateAxis(boolean isDateAxis){
        if (!Utils.equals(this.dateAxis, isDateAxis)) {
            this.dateAxis=isDateAxis;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public boolean isVisible(){
        return visible;
    }
    
    public void setIsVisible(boolean isVisible){
        if (!Utils.equals(this.visible, isVisible)) {
            this.visible=isVisible;
            setEditState(EEditState.MODIFIED);
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
    
    @Override
    public void collectDependences(List<Definition> list) {       
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
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(AdsReportChartAxis.this) {

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
                return "Report Chart Axis";
            }

            @Override
            public boolean isPublished() {
                return false;
            }
        });
    }
    
    public int getIndex() {
        final AdsReportChartCell cell = (AdsReportChartCell)getContainer();
        if (cell != null) {
            if(axisType==EReportChartAxisType.DOMAIN_AXIS){
                return cell.getDomainAxes().indexOf(this);
            }else{
                return cell.getRangeAxes().indexOf(this);
            }
        } else {
            return -1;
        }
    }
    
    public AdsReportFormat getFormat(){
        return format;
    }
    
}
