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

package org.radixware.kernel.designer.ads.localization.source;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMultilingualStringKind;


public class FilterSettings { 

    private boolean showOnlyPulished = false;
    private boolean showEmpty = true;
    private boolean showNotEmpty = true;
    private boolean showOnlyDifferentVersions = false;
    private boolean isShowCommentedSrc = true;
    private boolean isShowNotChecked = true;
    
    private AuthorFilter createBy = null;
    private TimeFilter creationTimeFilter = null;
    private AuthorFilter modifiedBy = null;
    private TimeFilter modifiedTimeFilter = null;
    private TimeFilter statusChangeTimeFilter = null;
    


    private String searchText=null;
    private EIsoLanguage notTranslatedOn=null;
    private boolean isNotTranslatedOnAll=false;

    private List<EMultilingualStringKind> stringTypes = new ArrayList<>();
    
    public enum GroupLangsEnum{ 
        SRC_LANGS("Source languages"), TRANSL_LANGS("Target languages"), ALL_LANGS("All languages");
        
        private final String name;
        GroupLangsEnum(final String name) {
          this.name = name;
        }
    
        @Override
        public String toString(){
            return name;
        }
    };
  
    public FilterSettings() {
        init();
    }
    
    public boolean isEmpty(){
        boolean timeFilterUse = statusChangeTimeFilter.isEmpty() && 
                                creationTimeFilter.isEmpty() && 
                                modifiedTimeFilter.isEmpty();
        boolean authorFilterUse = createBy.isEmpty() && modifiedBy.isEmpty();
        return !isShowOnlyPulished() && isShowEmpty() && isShowNotEmpty() && isShowCommentedSrc() && isShowNotChecked() && !isShowOnlyDifferentVersions()
                && (searchText == null || searchText.isEmpty())
                && notTranslatedOn == null && !isNotTranslatedOnAll
                && timeFilterUse && authorFilterUse && 
                (stringTypes != null && stringTypes.size() == EMultilingualStringKind.values().length);
    }
    
    public void init(){
        searchText=null;
        
        showOnlyPulished = false;
        showEmpty = true;
        showNotEmpty = true;
        isShowCommentedSrc = true;
        isShowNotChecked = true;
        searchText = null;
        notTranslatedOn = null;
        isNotTranslatedOnAll = false;
        
        creationTimeFilter = new TimeFilter();
        modifiedTimeFilter = new TimeFilter();
        statusChangeTimeFilter = new TimeFilter();
        createBy = new AuthorFilter();
        modifiedBy = new AuthorFilter();
        stringTypes = new ArrayList<>(Arrays.asList(EMultilingualStringKind.values()));
    }
    
    public void setShowEmpty(final boolean isShowEmpty) {
        this.showEmpty = isShowEmpty;
    }
    
    public boolean isShowEmpty() {
        return showEmpty;
    }

    public boolean isShowNotEmpty() {
        return showNotEmpty;
    }

    public void setShowNotEmpty(boolean isShowNotEmpty) {
        this.showNotEmpty = isShowNotEmpty;
    }
    
    public void setShowCommentedSrc(final boolean isShowComment){
        this.isShowCommentedSrc=isShowComment;
    }
    
    public void setShowNotChecked(final boolean isShowNotChecked){
        this.isShowNotChecked=isShowNotChecked;
    }

    
    public void setSerchText(final String searchText){
        this.searchText=searchText;
    }
    
    public void setNotTranslatedOn(final EIsoLanguage notTranslatedOn){
        this.notTranslatedOn=notTranslatedOn;
    }
    
    public void setNotTranslatedOnAll(final boolean isNotTranslOnAll){
        this.isNotTranslatedOnAll=isNotTranslOnAll;
    }
    
    public boolean isShowNotChecked(){
        return isShowNotChecked;
    }
    
    public boolean isShowCommentedSrc(){
        return isShowCommentedSrc;
    }
     
    
    public String getSerchText(){
        return searchText;
    }
    
    public EIsoLanguage getNotTranslatedOn(){
        return notTranslatedOn;
    }
    
    public boolean isNotTranslatedOnAll( ){
        return isNotTranslatedOnAll;
    }

    public TimeFilter getModifiedTimeFilter() {
        return modifiedTimeFilter;
    }

    public TimeFilter getCreationTimeFilter() {
        return creationTimeFilter;
    }

    public TimeFilter getStatusChangeTimeFilter() {
        return statusChangeTimeFilter;
    }

    public AuthorFilter getCreateBy() {
        return createBy;
    }

    public AuthorFilter getModifiedBy() {
        return modifiedBy;
    }

    public boolean isShowOnlyPulished() {
        return showOnlyPulished;
    }

    public void setShowOnlyPulished(boolean isShowOnlyPulished) {
        this.showOnlyPulished = isShowOnlyPulished;
    }

    public List<EMultilingualStringKind> getStringTypes() {
        return stringTypes;
    }

    public void setStringTypes(List<EMultilingualStringKind> stringTypes) {
        this.stringTypes = stringTypes;
    }

    public boolean isShowOnlyDifferentVersions() {
        return showOnlyDifferentVersions;
    }

    public void setShowOnlyUnversion(boolean showOnlyUnversion) {
        this.showOnlyDifferentVersions = showOnlyUnversion;
    }
}
