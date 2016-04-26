/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.localization.source;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.designer.ads.localization.RowString;


public class FilterUtils {

    public static String NOT_DEFINED = "<Not Defined>";
    
    public static boolean rowFilter(RowString mlString,final FilterSettings filterSettings,List<EIsoLanguage> sourceLangs,List<EIsoLanguage> translLangs){
        return (FilterUtils.filterSearch(sourceLangs,mlString, filterSettings.getSerchText()))
                    && filterByPublishedContext(mlString,filterSettings) 
                    && filterByEmptyStr(filterSettings,mlString,sourceLangs,translLangs) 
                    && filterByCommentedContext(mlString,filterSettings)
                    && filterByShowMode(mlString, filterSettings.getStringTypes())
                    && filterByNotCheckedSrc(sourceLangs, mlString, filterSettings)
                    
                    && (FilterUtils.filterNotTranslStr( translLangs, mlString, filterSettings.getNotTranslatedOn(),filterSettings.isNotTranslatedOnAll()))
                    && filterParameters(mlString, filterSettings, translLangs);
    }
    
    public static List<RowString> getFiltredData(List<RowString> mlStrings,final FilterSettings filterSettings,List<EIsoLanguage> sourceLangs,List<EIsoLanguage> translLangs){
        if (mlStrings == null || filterSettings == null || filterSettings.isEmpty()) return mlStrings;
        
        List<RowString> filtredDeta = new ArrayList<>();
        for (RowString mlString : mlStrings) {
            if (rowFilter(mlString, filterSettings, sourceLangs, translLangs)) {
                filtredDeta.add(mlString);
            }
        }
        return filtredDeta;
    }
    
    private static boolean filterParameters(final RowString mlString,final FilterSettings filterSettings,List<EIsoLanguage> translLangs){
        boolean filterByTime = filterByCreationTime(mlString, filterSettings.getCreationTimeFilter()) && 
                    filterByLastModifiedTime(mlString, filterSettings.getModifiedTimeFilter()) &&
                    filterByStatusChangeTime(mlString, filterSettings.getStatusChangeTimeFilter());
        boolean filterByPeople = filterByCreator(mlString, filterSettings.getCreateBy())
                && filterByAuthor(mlString, filterSettings.getModifiedBy());
         return filterByTime && filterByPeople;
    }
    
    public static boolean filterByShowMode(final RowString mlString, final List<EMultilingualStringKind> allowsType){
        if (allowsType == null){
            return true;
        }
        EMultilingualStringKind strKind = mlString.getMultilingualStringKind();
        return allowsType.contains(strKind);
    }    
     

     public static boolean filterByNotCheckedSrc(List<EIsoLanguage> sourceLangs,final RowString mlString, final FilterSettings filterSettings) {
        return filterSettings.isShowNotChecked() || mlString.hasCheckedTranslation(sourceLangs);
        
    }

    private static boolean filterByPublishedContext(RowString mlString, final FilterSettings filterSettings) {
        return !filterSettings.isShowOnlyPulished() || mlString.isContextPublished();
    }
    
    private static boolean filterByEmptyStr(final FilterSettings filterSettings,RowString mlString,List<EIsoLanguage> sourceLangs,List<EIsoLanguage> translLangs){
        if (filterSettings.isShowEmpty() ^ filterSettings.isShowNotEmpty()) {
            return filterSettings.isShowEmpty() ? mlString.isEmpty(sourceLangs, translLangs) : !mlString.isEmpty(sourceLangs, translLangs);
        }
        return filterSettings.isShowEmpty();
    }
    
    private static boolean filterByCommentedContext(RowString mlString, final FilterSettings filterSettings){
        return filterSettings.isShowCommentedSrc() || !mlString.isStrInComment();
    }
    
    public static boolean filterNotTranslStr(List<EIsoLanguage> translLangs,final RowString mlString, final EIsoLanguage lang, final boolean notTranslOnAllLangs) {
        if(lang==null){
            if(notTranslOnAllLangs){
                for(EIsoLanguage l:translLangs)
                    if(!mlString.needsCheck(l))
                        return false;
            }
            return true;
        }else if(mlString.needsCheck(lang)){
            return true;
        }
        return false;
    }

    public static boolean filterSearch(List<EIsoLanguage> sourceLangs,final RowString mlString, final String searchingText) {
        if(searchingText==null || searchingText.isEmpty())return true;
        
        for(EIsoLanguage lang:sourceLangs){
            String value= mlString.getValue(lang);
            value= value==null ? "" : value;
            if(value.toLowerCase().contains(searchingText.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    
    public static boolean filterByCreator(final RowString mlString, AuthorFilter authorFilter){
        if (authorFilter.isEmpty()) return true;
        String author = mlString.getAuthor();
        
        if (author == null){
            return false;
        }
        return authorFilter.getName().equals(author.toLowerCase());
    }
    
    public static boolean filterByAuthor(final RowString mlString, AuthorFilter authorFilter){
        if(authorFilter.isEmpty()) return true;  
        
        String name = authorFilter.getName();
        
        for(EIsoLanguage lang:authorFilter.getLanguages()){
            
            String lastChangeAuthor= mlString.getLastChangeAuthor(lang);
            
            if(lastChangeAuthor != null && name.equals(lastChangeAuthor.toLowerCase())){
                return true;
            }
        }

        return false;
    }
    
    public static boolean filterByLastModifiedTime(final RowString mlString, final TimeFilter timeFilter) {
        if(timeFilter.isEmpty()) return true;  
        
        Timestamp timeFrom = timeFilter.getTimeFrom();
        Timestamp timeTo = timeFilter.getTimeTo();
        
        boolean res=false;
        for(EIsoLanguage lang:timeFilter.getLanguages()){
            
            Timestamp lastChangeTime= mlString.getLastChangeTime(lang);
            lastChangeTime=lastChangeTime==null? new Timestamp(-1):lastChangeTime;
            if((timeFrom == null || lastChangeTime.after(timeFrom)) &&
                (timeTo == null || lastChangeTime.before(timeTo))){
                res=true;
                break;
            }
        }

        return res;
    }
    
    public static boolean filterByCreationTime(final RowString mlString, final TimeFilter timeFilter) {
        if(timeFilter.isEmpty()) return true;
        
        Timestamp timeFrom = timeFilter.getTimeFrom();
        Timestamp timeTo = timeFilter.getTimeTo();
        Timestamp time = mlString.getCreationDate();
        
        return (timeFrom == null || time.after(timeFrom)) &&
                (timeTo == null || time.before(timeTo));
    }
    
    public static boolean filterByStatusChangeTime(final RowString mlString, final TimeFilter timeFilter) {
        if (timeFilter.isEmpty()) {
            return true;
        }

        Timestamp timeFrom = timeFilter.getTimeFrom();
        Timestamp timeTo = timeFilter.getTimeTo();

        boolean res = false;
        for (EIsoLanguage lang : timeFilter.getLanguages()) {

            Timestamp lastChangeTime = mlString.getChangeStatusTime(lang);
            lastChangeTime = lastChangeTime == null ? new Timestamp(-1) : lastChangeTime;
            if ((timeFrom == null || lastChangeTime.after(timeFrom))
                    && (timeTo == null || lastChangeTime.before(timeTo))) {
                res = true;
                break;
            }
        }

        return res;
    }
}
