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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormattedCell.ChangedEvent;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.CellFormat;


public class AdsReportFormat {
    private int precission=3;    
    private String decimalDelimiter = null;
    private String triadDelimeter = null;
    private ETriadDelimeterType triadDelimeterType=ETriadDelimeterType.DEFAULT;
    
    private EDateTimeStyle dataStyle=null;
    private EDateTimeStyle timeStyle=null;
    private String pattern = "";
    private boolean useDefaultFormat=true;
    
    private final RadixEventSource<IRadixEventListener<ChangedEvent>,ChangedEvent> changeSupport = new RadixEventSource<IRadixEventListener<ChangedEvent>, ChangedEvent>();
    
    public void addChangeListener(IRadixEventListener<ChangedEvent> l) {
        changeSupport.addEventListener(l);
    }
    
    /**
    * Creates a new AdsReportFormat.
    */
    public AdsReportFormat() {        
    } 
    
    public AdsReportFormat(AdsReportFormat src) {
        if(src!=null){
            pattern = src.getPattern();
            precission=src.getPrecission();
            decimalDelimiter=src.getDesimalDelimeter();
            triadDelimeter=src.getTriadDelimeter();
            triadDelimeterType=src.getTriadDelimeterType();

            dataStyle=src.getDateStyle();
            timeStyle=src.getTimeStyle();
            useDefaultFormat=src.getUseDefaultFormat();
        }
       /* if(src.getisSetGroupSeparator()){
            useDefaultFormat=false;
            triadDelimeterType=ETriadDelimeterType.SPECIFIED;
            triadDelimeter=src.getGroupSeparator();
        } */      
    }
    
    protected AdsReportFormat(CellFormat xFormat) {
        if(xFormat.isSetPattern())
            pattern = xFormat.getPattern();
        if(xFormat.isSetPrecission())
            precission=xFormat.getPrecission();
        if(xFormat.isSetDecimalDelimiter())
            decimalDelimiter=xFormat.getDecimalDelimiter();
        if(xFormat.isSetTriadDelimeter())
            triadDelimeter=xFormat.getTriadDelimeter();
        if(xFormat.isSetTriadDelimeterType())
            triadDelimeterType=xFormat.getTriadDelimeterType();

        if(xFormat.isSetDateStyle())
            dataStyle=xFormat.getDateStyle();
        if(xFormat.isSetTimeStyle())
            timeStyle=xFormat.getTimeStyle();
        if(xFormat.isSetUseDefaultFormat())
            useDefaultFormat=xFormat.getUseDefaultFormat();

        if(xFormat.isSetGroupSeparator()){
            useDefaultFormat=false;
            triadDelimeterType=ETriadDelimeterType.SPECIFIED;
            triadDelimeter=xFormat.getGroupSeparator();
        }        
    }
    
    protected void appendTo(CellFormat xFormat, RadixObject.EEditState editState) {
        xFormat.setUseDefaultFormat(useDefaultFormat);
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
            if(editState==RadixObject.EEditState.MODIFIED){
                pattern=null;
                xFormat.setPattern(pattern);
            }
        }
    }
    

    public String getStrValue(EValType type){        
        if(useDefaultFormat){
            return "<default>";
        }
        StringBuilder res = new StringBuilder();
        if(type==EValType.NUM){
            if(decimalDelimiter!=null){
                res.append("Decimal delimiter: ").append(decimalDelimiter).append("; ");//+="Decimal delimiter: "+decimalDelimiter+"; ";
            }
            res.append("Precission: ").append(precission);//+=+precission;

        }
        if(type==EValType.NUM ||type==EValType.INT){
            if(triadDelimeterType!=null){
                if(!res.toString().isEmpty()){
                    res.append("; ");
                }
                res.append("Triad delimeter: ");
                if(triadDelimeterType==ETriadDelimeterType.SPECIFIED){
                    res.append(triadDelimeter);
                }else{
                    res.append(triadDelimeterType.getName()); 
                }
            }
        }else if(type==EValType.DATE_TIME){
            if(!res.toString().isEmpty()){
                 res.append("; ");
            } 
            if(dataStyle==null){
                res.append("Date: <default>");
            }else if(dataStyle==EDateTimeStyle.CUSTOM){
                res.append("DateTime: ").append(pattern);
                return res.toString();
            }else{
                res.append("Date: ").append(dataStyle.getName()); 
            }

            if(!res.toString().isEmpty() && !res.toString().endsWith("; ")){
                 res.append("; ");
            } 
            res.append("Time: ");
            if(timeStyle==null){
                res.append("<default>");
            }else{
                res.append(timeStyle.getName());
            } 
        }
        return res.toString();
    }
    
    public int getPrecission() {
        return precission;
    }
    
    public void setPrecission(int precission) {
        if (this.precission!=precission) {
            this.precission = precission;
            ChangedEvent e=new ChangedEvent();
            changeSupport.fireEvent(e);
        }
    }
    
    public boolean getUseDefaultFormat() {
        return useDefaultFormat;
    }
    
    public void setUseDefaultFormat(boolean useDefaultFormat) {
         if (this.useDefaultFormat!=useDefaultFormat) {
            this.useDefaultFormat = useDefaultFormat;
            ChangedEvent e=new ChangedEvent();
            changeSupport.fireEvent(e);
         }

    }
    
    public ETriadDelimeterType getTriadDelimeterType() {
        return triadDelimeterType;
    }    

    public void setTriadDelimeterType(ETriadDelimeterType triadDelimeterType) {
         if (!Utils.equals(this.triadDelimeterType, triadDelimeterType)) {
             this.triadDelimeterType = triadDelimeterType;
             ChangedEvent e=new ChangedEvent();
             changeSupport.fireEvent(e);
         }
    } 
    
    

    public String getTriadDelimeter() {
        return triadDelimeter;
    }    

    public void setTriadDelimeter(String triadDelimeter) {
        if (!Utils.equals(this.triadDelimeter , triadDelimeter)) {
            this.triadDelimeter = triadDelimeter;
            ChangedEvent e=new ChangedEvent();
            changeSupport.fireEvent(e);
        }
    }  
    
    public String getDesimalDelimeter() {
        return decimalDelimiter;
    }    

    public void setDesimalDelimeter(String decimalDelimiter) {
        if (!Utils.equals( this.decimalDelimiter, decimalDelimiter)) {
            this.decimalDelimiter = decimalDelimiter;
            ChangedEvent e=new ChangedEvent();
            changeSupport.fireEvent(e);
        }

    }
    
    public EDateTimeStyle getDateStyle() {
        return dataStyle;
    }    

    public void setDateStyle(EDateTimeStyle dataStyle) {
        if (!Utils.equals(this.dataStyle , dataStyle)) {
            this.dataStyle = dataStyle;
            ChangedEvent e=new ChangedEvent();
            changeSupport.fireEvent(e);
        }
    }
    
    public EDateTimeStyle getTimeStyle() {
        return timeStyle;
    }    

    public void setTimeStyle(EDateTimeStyle timeStyle) {
        if (!Utils.equals(this.timeStyle, timeStyle)) {
            this.timeStyle = timeStyle;
            ChangedEvent e=new ChangedEvent();
            changeSupport.fireEvent(e);
        }
    }        

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        if (!Utils.equals(this.pattern, pattern)) {
            this.pattern = pattern;
            ChangedEvent e=new ChangedEvent();
            changeSupport.fireEvent(e);
         }
    } 
}
