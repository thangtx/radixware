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

import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.schemas.adsdef.CellFormat;

public class AdsReportFormattedCell extends AdsReportCell {

    private final AdsReportFormat format;
    /* private int precission=3;    
     private String decimalDelimiter = null;
     private String triadDelimeter = null;
     private ETriadDelimeterType triadDelimeterType=ETriadDelimeterType.DEFAULT;
    
     private EDateTimeStyle dataStyle=null;
     private EDateTimeStyle timeStyle=null;
     private String pattern = "";
     private boolean useDefaultFormat=true;*/
    private final IRadixEventListener<ChangedEvent> changeListener = new IRadixEventListener<ChangedEvent>() {
        @Override
        public void onEvent(ChangedEvent e) {
            setEditState(EEditState.MODIFIED);
        }
    };

    public static class ChangedEvent extends RadixEvent {

        /**
         * Creates a new ChangedEvent.
         */
        ChangedEvent() {
        }
    }

    protected AdsReportFormattedCell() {
        super();
        format = new AdsReportFormat();
    }

    protected AdsReportFormattedCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);
        if (xCell.isSetFormat()) {
            CellFormat xFormat = xCell.getFormat();
            format = new AdsReportFormat(xFormat);
            format.addChangeListener(changeListener);
        } else {
            format = new AdsReportFormat();
        }
    }

    protected AdsReportFormattedCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);
        if (xCell.isSetFormat()) {
            CellFormat xFormat = xCell.getFormat();
            format = new AdsReportFormat(xFormat);
            format.addChangeListener(changeListener);
        } else {
            format = new AdsReportFormat();
        }
    }

    public AdsReportFormat getFormat() {
        return format;
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);
        CellFormat xFormat = xCell.addNewFormat();
        if (format != null) {
            format.appendTo(xFormat, getEditState());
        }
        /*xFormat.setUseDefaultFormat(useDefaultFormat);
         if(!useDefaultFormat){
         if(dataStyle!=null ){
         xFormat.setDateStyle(dataStyle);                            
         }
         if(timeStyle!=null){
         xFormat.setTimeStyle(timeStyle);
         }
         if(dataStyle==EDateTimeStyle.CUSTOM || timeStyle==EDateTimeStyle.CUSTOM){
         xFormat.setPattern(pattern==null ? "" : pattern); 
         }
         if(triadDelimeterType!=null){
         xFormat.setTriadDelimeterType(triadDelimeterType);
         if(triadDelimeterType==ETriadDelimeterType.SPECIFIED){
         xFormat.setTriadDelimeter(triadDelimeter);
         }                     
         }
         if(decimalDelimiter!=null){
         xFormat.setDecimalDelimiter(decimalDelimiter);
         }
         if(precission!=-1){
         xFormat.setPrecission(precission);
         }
         }else{
         if(getEditState()==EEditState.MODIFIED){
         pattern=null;
         xFormat.setPattern(pattern);
         }
         }*/
    }

    @Override
    public EReportCellType getCellType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPrecission() {
        return format.getPrecission();
    }

    public void setPrecission(int precission) {
        if (format.getPrecission() != precission) {
            format.setPrecission(precission);
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean getUseDefaultFormat() {
        return format.getUseDefaultFormat();
    }

    public void setUseDefaultFormat(boolean useDefaultFormat) {
        if (format.getUseDefaultFormat() != useDefaultFormat) {
            format.setUseDefaultFormat(useDefaultFormat);
            setEditState(EEditState.MODIFIED);
        }
    }

    public ETriadDelimeterType getTriadDelimeterType() {
        return format.getTriadDelimeterType();
    }

    public void setTriadDelimeterType(ETriadDelimeterType triadDelimeterType) {
        if (!Utils.equals(format.getTriadDelimeterType(), triadDelimeterType)) {
            format.setTriadDelimeterType(triadDelimeterType);
            setEditState(EEditState.MODIFIED);
        }
    }

    public String getTriadDelimeter() {
        return format.getTriadDelimeter();
    }

    public void setTriadDelimeter(String triadDelimeter) {
        if (!Utils.equals(format.getTriadDelimeter(), triadDelimeter)) {
            format.setTriadDelimeter(triadDelimeter);
            setEditState(EEditState.MODIFIED);
        }
    }

    public String getDesimalDelimeter() {
        return format.getDesimalDelimeter();
    }

    public void setDesimalDelimeter(String decimalDelimiter) {
        if (!Utils.equals(format.getDesimalDelimeter(), decimalDelimiter)) {
            format.setDesimalDelimeter(decimalDelimiter);
            setEditState(EEditState.MODIFIED);
        }
    }

    public EDateTimeStyle getDateStyle() {
        return format.getDateStyle();
    }

    public void setDateStyle(EDateTimeStyle dataStyle) {
        if (!Utils.equals(format.getDateStyle(), dataStyle)) {
            format.setDateStyle(dataStyle);
            setEditState(EEditState.MODIFIED);
        }
    }

    public EDateTimeStyle getTimeStyle() {
        return format.getTimeStyle();
    }

    public void setTimeStyle(EDateTimeStyle timeStyle) {
        if (!Utils.equals(format.getTimeStyle(), timeStyle)) {
            format.setTimeStyle(timeStyle);
            setEditState(EEditState.MODIFIED);
        }
    }

    public String getPattern() {
        return format.getPattern();
    }

    public void setPattern(String pattern) {
        if (!Utils.equals(format.getPattern(), pattern)) {
            format.setPattern(pattern);
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean isModeSupported(AdsReportForm.Mode mode) {
        return true;
    }
}