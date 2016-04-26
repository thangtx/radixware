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

package org.radixware.kernel.common.client.meta.filters;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.meta.editorpages.RadUserFilterEditorPages;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.groupsettings.CustomFilter;


public class RadCommonFilter extends RadFilterDef{
    
    private RadUserFilterEditorPages editorPages;
    private RadFilterDef baseFilter;    
    private String title;
    private final IClientEnvironment environment;
    private final Id classId;
    private long lastUpdateTime;
    private Id baseFilterId;
    private String parametersAsStr;
    private org.radixware.schemas.xscml.Sqml condition;
    private boolean exists = true;
    private final Icon icon;
    
    public static class Factory{
        private Factory(){
            
        }
        
        public static RadCommonFilter loadFrom(final IClientEnvironment environment, 
                                               final RadSelectorPresentationDef presentation,
                                               final org.radixware.schemas.eas.CommonFilter filterXml){            
            return new RadCommonFilter(environment,
                                       presentation,
                                       filterXml.getId(),
                                       filterXml.getBaseFilterId(), 
                                       filterXml.getTitle(), 
                                       filterXml.getLastUpdateTime(),
                                       filterXml.getCondition(),
                                       filterXml.getParameters());
        }
        
        public static RadCommonFilter loadFrom(final IClientEnvironment environment, 
                                               final RadSelectorPresentationDef presentation,
                                               final CustomFilter filterXml){            
            return new RadCommonFilter(environment,
                                       presentation,
                                       filterXml.getId(),
                                       filterXml.getBaseFilterId(), 
                                       filterXml.getName(), 
                                       0,
                                       filterXml.getCondition(),
                                       filterXml.getParameters());
        }
    }
    
    
    private RadCommonFilter(final IClientEnvironment environment,
                            final RadSelectorPresentationDef presentation,
                            final Id filterId,
                            final Id baseFilterId, 
                            final String filterTitle, 
                            final long timestamp,
                            final org.radixware.schemas.xscml.Sqml condition,
                            final org.radixware.schemas.groupsettings.FilterParameters parameters){
        super(filterId,
              filterTitle,
              environment.getApplication().getRuntimeEnvironmentType(),
              presentation.getOwnerClassId(),//classId
              null,//titleId
              null,//not linked own condition
              presentation.getAcceptableBaseSortingIds(),//enabledSortings
              presentation.isCustomSortingEnabled(),//isAnyCustomSortingEnabled
              presentation.isAnySortingAcceptable(),//isAnyBaseSortingEnabled
              presentation.getDefaultSortingId(),//baseSortingId              
              null,//parameters
              null,//pages
              null,//pageOrder
              null//contextlessCommandsOrder
             );        
        classId = presentation.getOwnerClassId();
        if (baseFilterId != null) {
            final RadClassPresentationDef classDef = environment.getDefManager().getClassPresentationDef(classId);
            baseFilter = classDef.getFilterDefById(baseFilterId);
            this.baseFilterId = baseFilterId;
        } else {
            baseFilter = null;
            this.baseFilterId = null;
        }        
        this.environment = environment;        
        this.parameters = RadFilterParameters.Factory.loadFrom(environment, parameters, baseFilter, classId);
        parametersAsStr = getParametersAsStr(parameters);
        editorPages = baseFilter == null ? null : new RadUserFilterEditorPages(baseFilter, this.parameters);
        title = filterTitle;
        lastUpdateTime = timestamp;
        this.condition = condition;
        icon = environment.getApplication().getImageManager().getIcon(ClientIcon.Definitions.COMMON_FILTER);
    }
    
    public void actualize(final Id baseFilterId, 
                       final String title, 
                       final org.radixware.schemas.xscml.Sqml condition,
                       final org.radixware.schemas.groupsettings.FilterParameters parameters,
                       final long lastUpdateTime){
        boolean needToRecreateEditorPages = false;
        if (!Utils.equals(this.baseFilterId, baseFilterId)){
            if (baseFilterId != null) {
                final RadClassPresentationDef classDef = environment.getDefManager().getClassPresentationDef(classId);
                baseFilter = classDef.getFilterDefById(baseFilterId);
                this.baseFilterId = baseFilterId;
            } else {
                baseFilter = null;
                this.baseFilterId = null;
            }
            needToRecreateEditorPages = true;
        }
        
        this.title = title;
        this.lastUpdateTime = lastUpdateTime;        
        final String newParametersAsStr = getParametersAsStr(parameters);
        if (!Utils.equals(parametersAsStr, newParametersAsStr)){
            this.parameters = RadFilterParameters.Factory.loadFrom(environment, parameters, baseFilter, classId);
            parametersAsStr = newParametersAsStr;
            needToRecreateEditorPages = true;            
        }
        
        if (needToRecreateEditorPages){
            editorPages = baseFilter == null ? null : new RadUserFilterEditorPages(baseFilter, this.parameters);
        }
        this.condition = condition;
    }
    
    public boolean hasSameParameters(final org.radixware.schemas.groupsettings.FilterParameters parameters){
        return Utils.equals(parametersAsStr, getParametersAsStr(parameters));
    }
    
    private static String getParametersAsStr(final org.radixware.schemas.groupsettings.FilterParameters parameters){
        if (parameters==null){
            return null;
        }
        else{
            //Creating a separate xml instance to avoid comparing with a part of another xml
            final org.radixware.schemas.groupsettings.FilterParameters tmpParameters = 
                    org.radixware.schemas.groupsettings.FilterParameters.Factory.newInstance();
            if (parameters.getParameterList()!=null){
                for (org.radixware.schemas.groupsettings.FilterParameters.Parameter parameter: parameters.getParameterList()){
                    tmpParameters.addNewParameter().set(parameter);
                }
            }            
            String textToCompare = tmpParameters.xmlText().replace(" xmlns:eas=\"http://schemas.radixware.org/eas.xsd\"", "");
            textToCompare = textToCompare.replace(" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"", "");
            textToCompare = textToCompare.replace(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            return textToCompare;
        }        
    }
    
    public RadFilterDef getBaseFilter() {
        return baseFilter;
    }

    @Override
    public RadEditorPages getEditorPages() {
        return editorPages == null ? super.getEditorPages() : editorPages;
    }

    @Override
    public RadEditorPageDef findEditorPageById(Id pageId) {
        final RadEditorPageDef pageDef = super.findEditorPageById(pageId);
        if (pageDef == null && editorPages != null) {
            return editorPages.getPageById(pageId);
        } else {
            return null;
        }
    }

    @Override
    public boolean hasTitle() {
        return title != null && !title.isEmpty();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }
    
    public boolean isExists(){
        return exists;
    }
    
    @Override
    public org.radixware.schemas.xscml.Sqml getCondition() {
        return condition==null ? null : (org.radixware.schemas.xscml.Sqml)condition.copy();
    }

    @Override
    public boolean isBaseSortingEnabledById(Id srtId) {
        if (baseFilter != null) {
            return baseFilter.isBaseSortingEnabledById(srtId);
        }
        return super.isBaseSortingEnabledById(srtId);
    }

    @Override
    public boolean isAnyCustomSortingEnabled() {
        if (baseFilter != null) {
            return baseFilter.isAnyCustomSortingEnabled();
        }
        return super.isAnyCustomSortingEnabled();
    }

    @Override
    protected Model createModelImpl(IClientEnvironment environment) {
        return new FilterModel(environment, this);
    }
    
    @Override
    public Long getLastUpdateTime(){
        return lastUpdateTime;
    }        
    
    public void setWasRemoved(){
        exists = false;
    }
    
    @Override
    public boolean isValid(){
        return isExists() && parameters.isValid();
    }
    
    public org.radixware.schemas.eas.CommonFilter writeToXml(final org.radixware.schemas.eas.CommonFilter xml){
        final org.radixware.schemas.eas.CommonFilter filterXml;
        if (xml==null){
            filterXml = org.radixware.schemas.eas.CommonFilter.Factory.newInstance();
        }else{
            filterXml = xml;            
        }
        filterXml.setId(getId());
        filterXml.setBaseFilterId(baseFilterId);
        filterXml.setTitle(title);
        filterXml.setLastUpdateTime(lastUpdateTime);        
        parameters.writeCustomParametersToXml(filterXml.addNewParameters());
        return filterXml;
    }
    
    public RadUserFilter convertToUserFilter(final IClientEnvironment environment, final RadSelectorPresentationDef presentation){
        final org.radixware.schemas.eas.CommonFilter xml = writeToXml(null);
        final org.radixware.schemas.groupsettings.CustomFilter filterAsXml = 
            org.radixware.schemas.groupsettings.CustomFilter.Factory.newInstance();
        filterAsXml.setCondition(getCondition());
        filterAsXml.setParameters(xml.getParameters());
        filterAsXml.setBaseFilterId(xml.getBaseFilterId());
        filterAsXml.setName(xml.getTitle());
        return RadUserFilter.Factory.loadFromXml(environment, filterAsXml, presentation, false);        
    }
}
