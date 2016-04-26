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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.Format;
import java.util.Locale;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.INT;


public class CellValueFormatter {
    
    public static String formatValue(Object object, AdsReportFormat formattedCell,Locale locale){        
        if(object instanceof Long){            
            return formatIntValue( object, formattedCell, locale);
        }else if(object instanceof BigDecimal){            
            return formatNumValue( object, formattedCell, locale);
        }else if(object instanceof Timestamp){            
            return  formatDateTimeValue( object, formattedCell, locale);           
        }
        return String.valueOf(object);        
    }
    
    public static Format reportFormatToJavaFormat(AdsReportFormat formattedCell,Locale locale, EValType type){        
        switch(type){ 
            case INT:
                return createIntFormat(formattedCell, locale);
            case NUM:           
                return createNumFormat(formattedCell, locale);     
            case DATE_TIME:           
                 return createDateTimeFormat(formattedCell, locale);
            default:
                 return null;      
        } 
        /*if(idDateTime){
            return createDateTimeFormat(formattedCell, locale);
        }else{
            return createNumFormat(formattedCell, locale);  
        }*/
    }
    
    private static String formatIntValue(Object object,AdsReportFormat formattedCell,Locale locale){
         final NumberFormat intFormat= createIntFormat(formattedCell, locale);
         return intFormat.format(object);
    } 
    
    private static NumberFormat createIntFormat(AdsReportFormat format,Locale locale){
         final NumberFormat intFormat= locale==null? DecimalFormat.getIntegerInstance(): DecimalFormat.getIntegerInstance(locale);
         if(!format.getUseDefaultFormat() && (intFormat instanceof DecimalFormat)){
             final java.text.DecimalFormatSymbols unusualSymbols = new java.text.DecimalFormatSymbols(locale); 
             setTriadDelimeter(intFormat, format, unusualSymbols);
             ((DecimalFormat)intFormat).setDecimalFormatSymbols(unusualSymbols);
         }
         return intFormat;
    } 
    
    private static String formatNumValue(Object object,AdsReportFormat formattedCell,Locale locale){
         final NumberFormat numberFormat = createNumFormat(formattedCell, locale);          
         return numberFormat.format(object);
    }
    
    private static NumberFormat createNumFormat(AdsReportFormat formattedCell,Locale locale){
         final NumberFormat numberFormat= locale==null ? DecimalFormat.getNumberInstance() : DecimalFormat.getNumberInstance(locale);
         if(!formattedCell.getUseDefaultFormat() && (numberFormat instanceof DecimalFormat)){ 
             final DecimalFormat decimalFormat=(DecimalFormat)numberFormat;
             final String decimalDelimeter=formattedCell.getDesimalDelimeter();
             final int precission=formattedCell.getPrecission();

             final java.text.DecimalFormatSymbols unusualSymbols = new java.text.DecimalFormatSymbols(locale); 
             setTriadDelimeter(decimalFormat, formattedCell,unusualSymbols);                      
             if((decimalDelimeter!=null && !decimalDelimeter.isEmpty()) )
                unusualSymbols.setDecimalSeparator(decimalDelimeter.charAt(0));
             decimalFormat.setDecimalFormatSymbols(unusualSymbols);
             decimalFormat.setMaximumFractionDigits(precission);
             decimalFormat.setMinimumFractionDigits(precission);
         }            
         return numberFormat;
    }
    
    private static String formatDateTimeValue(Object object,AdsReportFormat formattedCell,Locale locale){
        final DateFormat dateTimeFormat = createDateTimeFormat(formattedCell, locale);
        return  dateTimeFormat.format(object);
    }
    
    private static DateFormat createDateTimeFormat(AdsReportFormat formattedCell,Locale locale){
        DateFormat dateTimeFormat=locale==null ? DateFormat.getDateTimeInstance() : DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT,locale); 
        if(!formattedCell.getUseDefaultFormat()){                             
            final EDateTimeStyle dateStyle=formattedCell.getDateStyle()==null ? 
                    EDateTimeStyle.DEFAULT : formattedCell.getDateStyle();
            final EDateTimeStyle timeStyle=formattedCell.getTimeStyle()==null ? 
                    EDateTimeStyle.DEFAULT : formattedCell.getTimeStyle();
            final String pattern=formattedCell.getPattern();
            if(dateStyle==EDateTimeStyle.CUSTOM && timeStyle==EDateTimeStyle.CUSTOM ){
               if (pattern != null && !pattern.isEmpty()){
                   dateTimeFormat =new SimpleDateFormat(pattern,locale);
               }        
            }else{
               final int intTimeStyle= getDataTimeStyle(timeStyle);
               final int intDateStyle= getDataTimeStyle(dateStyle);
               if(intTimeStyle==-1 && intDateStyle!=-1){
                   dateTimeFormat= DateFormat.getDateInstance(intDateStyle,locale);      
               }
               if(intTimeStyle!=-1 && intDateStyle==-1){
                   dateTimeFormat= DateFormat.getTimeInstance(intTimeStyle,locale); 
               }
               if(intTimeStyle!=-1 && intDateStyle!=-1){
                   dateTimeFormat= DateFormat.getDateTimeInstance(intDateStyle,intTimeStyle,locale);
               }
            }
        }
        return  dateTimeFormat;
    }
    
    private static void setTriadDelimeter(NumberFormat format,AdsReportFormat formattedCell,
            java.text.DecimalFormatSymbols unusualSymbols ){
        final ETriadDelimeterType triadDelimeterType=formattedCell.getTriadDelimeterType();
        final String triadSeparator=formattedCell.getTriadDelimeter();         
          
        if(triadDelimeterType!=null){
            if(triadDelimeterType==ETriadDelimeterType.NONE){
                format.setGroupingUsed(false);
            }
            if (triadDelimeterType==ETriadDelimeterType.SPECIFIED && triadSeparator!=null ){ 
                format.setGroupingUsed(true);
                unusualSymbols.setGroupingSeparator(triadSeparator.charAt(0));                
            }
        }
    }    
    
    private static int getDataTimeStyle(EDateTimeStyle style){
        switch(style){
           case FULL:
               return DateFormat.FULL; 
           case LONG:
               return DateFormat.LONG; 
           case MEDIUM:
               return DateFormat.MEDIUM; 
           case SHORT:
               return DateFormat.SHORT;
           case DEFAULT:
               return DateFormat.DEFAULT; 
       }
       return -1;         
    }
    
}
