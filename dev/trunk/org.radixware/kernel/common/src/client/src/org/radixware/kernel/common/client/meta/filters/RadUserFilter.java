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

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.meta.editorpages.RadUserFilterEditorPages;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.groupsettings.CustomFilter;
import org.radixware.schemas.sqmlexpression.SqmlExpression;
import org.radixware.schemas.sqmlexpression.SqmlExpressionDocument;


public class RadUserFilter extends RadFilterDef {

    public final Id baseFilterId, presentationId, tableId;
    private final RadUserFilterEditorPages editorPages;
    private final IClientEnvironment environment;
    private RadFilterDef baseFilter;
    private org.radixware.schemas.xscml.Sqml actualizedCondition;
    private long changeTimestamp = System.currentTimeMillis();
    private boolean conditionWasActualized;
    private boolean wasModified;
    private String title;
    private final Icon icon;

    public final static class Factory {

        private Factory() {
        }

        public static RadUserFilter newInstance(final IClientEnvironment environment, final String name, final RadSelectorPresentationDef presentation, final Id baseFilterId) {
            return new RadUserFilter(environment, presentation,
                    Id.Factory.newInstance(EDefinitionIdPrefix.FILTER),
                    name,
                    baseFilterId,
                    null,//condition
                    null//xml
                    );
        }

        public static RadUserFilter newInstance(final IClientEnvironment environment, final String name, final RadSelectorPresentationDef presentation) {
            return newInstance(environment, name, presentation, null);
        }

        public static RadUserFilter loadFromString(final IClientEnvironment environment, final String xmlAsString, final RadSelectorPresentationDef presentation, final boolean keepId) throws XmlException {
            try{
                return loadFromFilterDocument(environment, xmlAsString, presentation, keepId);
            }catch(XmlException firstChanceException){
                try{
                    return loadFromSqmlExpression(environment, xmlAsString, presentation);
                }catch(XmlException exception){
                    throw firstChanceException;
                }
            }
        }
        
        private static RadUserFilter loadFromFilterDocument(final IClientEnvironment environment, final String xmlAsString, final RadSelectorPresentationDef presentation, final boolean keepId) throws XmlException {
            final org.radixware.schemas.groupsettings.FilterDocument document = 
                    org.radixware.schemas.groupsettings.FilterDocument.Factory.parse(xmlAsString);
            return loadFromXml(environment, document.getFilter(), presentation, keepId);
        }
        
        private static RadUserFilter loadFromSqmlExpression(final IClientEnvironment environment, final String xmlAsString, final RadSelectorPresentationDef presentation) throws XmlException {
            final SqmlExpressionDocument document = 
                   SqmlExpressionDocument.Factory.parse(xmlAsString);
            return loadFromXml(environment, document.getSqmlExpression(), presentation);
        }
                
        public static RadUserFilter loadFromXml(final IClientEnvironment environment, 
                                                final org.radixware.schemas.groupsettings.CustomFilter xml,
                                                final RadSelectorPresentationDef presentation, 
                                                final boolean keepId){
            final Id tableId = xml.getTableId();
            if (tableId != null && !tableId.equals(presentation.getTableId())) {
                final String message = environment.getMessageProvider().translate("SelectorAddons", "This filter cannot be used for '%s'");
                throw new DefinitionError(String.format(message, presentation.getTitle()));
            }
            final org.radixware.schemas.xscml.SqmlDocument condition =
                    org.radixware.schemas.xscml.SqmlDocument.Factory.newInstance();
            if (xml.getCondition() != null) {
                condition.setSqml(xml.getCondition());
            }            
            final RadUserFilter filter = new RadUserFilter(environment, presentation,
                    keepId ? xml.getId() : Id.Factory.newInstance(EDefinitionIdPrefix.FILTER),
                    xml.getName(),
                    xml.getBaseFilterId(),
                    condition.xmlText(),
                    xml);
            return filter;            
        }
        
        public static RadUserFilter loadFromXml(final IClientEnvironment environment, 
                                                final SqmlExpression xml,
                                                final RadSelectorPresentationDef presentation){
            final Id tableId = xml.getTableId();
            if (tableId != null && !tableId.equals(presentation.getTableId())) {
                final String message = environment.getMessageProvider().translate("SelectorAddons", "This filter cannot be used for '%s'");
                throw new DefinitionError(String.format(message, presentation.getTitle()));
            }
            final org.radixware.schemas.xscml.SqmlDocument condition =
                    org.radixware.schemas.xscml.SqmlDocument.Factory.newInstance();
            if (xml.getSqml() != null) {
                condition.setSqml(xml.getSqml());
            }            
            final RadUserFilter filter = new RadUserFilter(environment, presentation,
                    Id.Factory.newInstance(EDefinitionIdPrefix.FILTER),                    
                    environment.getMessageProvider().translate("SelectorAddons", "New Filter"),
                    null,
                    condition.xmlText(),
                    xml.getParameters(),
                    null);
            return filter;
        }
        
    }
    
    private static void actualizeSqml(final org.radixware.schemas.xscml.Sqml sqml, final ISqmlDefinitions sqmlDefinitions){
        if (sqml!=null && sqml.getItemList()!=null && !sqml.getItemList().isEmpty()){
            for(org.radixware.schemas.xscml.Sqml.Item sqmlItem: sqml.getItemList()){
                if (sqmlItem.isSetPropSqlName()){   
                    final org.radixware.schemas.xscml.Sqml.Item.PropSqlName propName = sqmlItem.getPropSqlName();
                    final Id propOwnerId = propName.getTableId();                        
                    final ISqmlTableDef propOwner = propOwnerId==null ? null : sqmlDefinitions.findTableById(propOwnerId);
                    if (propOwner!=null){
                        final Id propId = propName.getPropId();                            
                        final ISqmlColumnDef column = propId==null ? null : propOwner.getColumns().getColumnById(propId);
                        if (column!=null && !column.hasProperty() && !propOwnerId.equals(propOwner.getTableId())){
                            propName.setTableId(propOwner.getTableId());
                        }
                    }                        
                }
            }
        }        
    }    

    protected RadUserFilter(final IClientEnvironment environment, final RadSelectorPresentationDef presentation,
            final Id id,
            final String name,
            final Id baseFilterId,
            final String condition,
            final org.radixware.schemas.groupsettings.CustomFilter xml) {
        this( environment, 
               presentation, 
               id, 
               name, 
               baseFilterId, 
               condition, 
               xml==null ?  null : xml.getParameters(),
               xml==null ? null : xml.getPersistentValues());
    }
    
    protected RadUserFilter(final IClientEnvironment environment, final RadSelectorPresentationDef presentation,
            final Id id,
            final String name,
            final Id baseFilterId,
            final String condition,
            final XmlObject xmlParameters,
            final CustomFilter.PersistentValues persistentValues) {
        super(id,
              name,
              environment.getApplication().getRuntimeEnvironmentType(),
              presentation.getOwnerClassId(),//classId
              null,//titleId
              condition,//not linked own condition
              presentation.getAcceptableBaseSortingIds(),//enabledSortings
              presentation.isCustomSortingEnabled(),//isAnyCustomSortingEnabled
              presentation.isAnySortingAcceptable(),//isAnyBaseSortingEnabled
              presentation.getDefaultSortingId(),//baseSortingId
              null,//parameters
              null,//pages
              null,//pageOrder
              null//contextlessCommandsOrder
              );        
        this.baseFilterId = baseFilterId;
        this.environment = environment;
        presentationId = presentation.getId();
        tableId = presentation.getTableId();

        if (baseFilterId == null) {
            baseFilter = null;
        } else {
            if (presentation.isFilterExists(baseFilterId)) {
                baseFilter = presentation.getFilterDefById(baseFilterId);
            } else {
                baseFilter = null;
            }            
        }
        parameters = new RadFilterParameters(environment, xmlParameters, persistentValues, baseFilter, presentation.getOwnerClassId());
        editorPages = baseFilter == null ? null : new RadUserFilterEditorPages(baseFilter, parameters);
        title = name;
        icon = environment.getApplication().getImageManager().getIcon(ClientIcon.Definitions.USER_FILTER);
    }    

    public RadFilterDef getBaseFilter() {
        return baseFilter;
    }

    @Override
    public RadEditorPages getEditorPages() {
        return editorPages == null ? super.getEditorPages() : editorPages;
    }

    @Override
    public RadEditorPageDef findEditorPageById(final Id pageId) {
        final RadEditorPageDef pageDef = super.findEditorPageById(pageId);
        if (pageDef == null && editorPages != null) {
            return editorPages.getPageById(pageId);
        } else {
            return null;
        }
    }

    @Override
    public boolean isValid() {
        return (baseFilterId == null || baseFilter != null) && parameters.isValid();
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

    @Override
    public org.radixware.schemas.xscml.Sqml getCondition() {
        if (!conditionWasActualized){            
            actualizedCondition = super.getCondition();
            if (actualizedCondition!=null){
                actualizeSqml(actualizedCondition, environment.getSqmlDefinitions());
            }
            conditionWasActualized = true;
        }
        return actualizedCondition==null ? null : (org.radixware.schemas.xscml.Sqml)actualizedCondition.copy();
    }

    @Override
    public boolean isBaseSortingEnabledById(final Id srtId) {
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

    public boolean wasModified() {
        return wasModified || parameters.wasModified();
    }

    public void setTitle(final String newTitle) {
        if (!Utils.equals(title, newTitle)) {
            title = newTitle;
            wasModified = true;
        }
    }

    @Override
    protected Model createModelImpl(final IClientEnvironment environment) {
        if (baseFilter==null){
            return new FilterModel(environment, this);
        }else{
            return createModelImpl(environment, baseFilter.getId(), this);
        }
    }

    public void setCondition(final org.radixware.schemas.xscml.Sqml newCondition) {
        actualizedCondition = newCondition == null ? null : (org.radixware.schemas.xscml.Sqml) newCondition.copy();
        changeTimestamp = System.currentTimeMillis();
        conditionWasActualized = true;
        wasModified = true;
    }
    
    public org.radixware.schemas.groupsettings.CustomFilter writeToXml(final org.radixware.schemas.groupsettings.CustomFilter xml){        
        final org.radixware.schemas.groupsettings.CustomFilter customFilter;
        if (xml==null){
            customFilter = org.radixware.schemas.groupsettings.CustomFilter.Factory.newInstance();
        }else{
            customFilter = xml;
        }        
        customFilter.setId(getId());
        customFilter.setTableId(tableId);
        customFilter.setName(title);
        customFilter.setDefinitionType(EDefType.FILTER);
        if (baseFilterId != null) {
            customFilter.setBaseFilterId(baseFilterId);
        }
        final org.radixware.schemas.xscml.Sqml condition = getCondition();
        if (condition != null) {
            customFilter.setCondition(condition);
        }
        boolean customParameters=false,persistentValues=false;
        for (ISqmlParameter parameter: parameters.getAll()){
            if (parameter instanceof RadFilterUserParamDef){
                customParameters = true;
                if (persistentValues){
                    break;
                }
            }
            if (parameter.getPersistentValue()!=null){
                persistentValues = true;
                if (customParameters){
                    break;
                }
            }
        }
        final org.radixware.schemas.groupsettings.FilterParameters xmlParams;
        if (customParameters){
            xmlParams =parameters.writeCustomParametersToXml(customFilter.addNewParameters());
        }else {
            xmlParams = null;
        }
        if (persistentValues){            
            final org.radixware.schemas.groupsettings.FilterParameters.Values xmlValues =
                xmlParams==null ? customFilter.addNewParameters().addNewValues() : xmlParams.addNewValues();
            parameters.writePersistentValuesToXml(xmlValues);
        }        
        return customFilter;
    }

    public String saveToString() {
        final org.radixware.schemas.groupsettings.FilterDocument document =
                org.radixware.schemas.groupsettings.FilterDocument.Factory.newInstance();        
        writeToXml(document.addNewFilter());
        return document.xmlText();
    }
    
    public final void switchToFixedState(){
        parameters.switchToFixedState();
        wasModified = false;
    }
    
    public final long getChangeTimestamp(){
        return Math.max(changeTimestamp,parameters.getChangeValueTimestamp());
    }
}