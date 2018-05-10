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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.Sqml;


public final class RadContextFilter {
    
    public static class Factory{
        
        private Factory(){            
        }
        
        public static RadContextFilter newInstance(final RadFilterDef filter, 
                                                                        final Collection<RadFilterParamValue> paramValues, 
                                                                        final Id explorerItemId){
            final Sqml condition;
            final Id predefinedFilterId;
            if (filter instanceof RadUserFilter){
                predefinedFilterId = null;
            }else{
                predefinedFilterId = filter.getId();
            }
            condition = filter.getConditionFrom()==null ? filter.getCondition() : null; //cant use condition if from part is defined
            final Map<Id,RadFilterParamValue> paramValuesById = new HashMap<>();
            final Map<Id,RadFilterParamValue> paramValuesByUniqueId = new HashMap<>();
            if (paramValues!=null){
                for (RadFilterParamValue paramValue: paramValues){
                    paramValuesById.put(paramValue.getParamId(), paramValue);
                }
            }
            final String idPostfix = "-"+explorerItemId.toString();
            final List<Sqml.Item> sqml = condition==null ? Collections.<Sqml.Item>emptyList() : condition.getItemList();
            for (Sqml.Item item : sqml) {
                if (item.isSetParameter()) {
                    final Id initialId = item.getParameter().getParamId();
                    final Id uniqueId = Id.Factory.append(initialId, idPostfix);
                    if (!paramValuesById.containsKey(initialId)) {
                        throw new IllegalArgumentException("Filter parameter #" + initialId + " is not defined");
                    }
                    item.getParameter().setParamId(uniqueId);
                    if (!paramValuesByUniqueId.containsKey(uniqueId)){
                        final RadFilterParamValue paramValue = paramValuesById.get(initialId);
                        paramValuesByUniqueId.put(uniqueId, paramValue.changeParamId(uniqueId));
                    }
                }else if (item.isSetEntityRefParameter()){
                    final Id initialId = item.getEntityRefParameter().getParamId();
                    final Id uniqueId = Id.Factory.append(initialId, idPostfix);
                    if (!paramValuesById.containsKey(initialId)) {
                        throw new IllegalArgumentException("Filter parameter #" + initialId + " is not defined");
                    }
                    item.getEntityRefParameter().setParamId(uniqueId);
                    if (!paramValuesByUniqueId.containsKey(uniqueId)){
                        final RadFilterParamValue paramValue = paramValuesById.get(initialId);
                        paramValuesByUniqueId.put(uniqueId, paramValue.changeParamId(uniqueId));
                    }                    
                } else if (item.isSetParamValCount()) {
                    final Id initialId = item.getParamValCount().getParamId();
                    final Id uniqueId = Id.Factory.append(initialId, idPostfix);
                    if (!paramValuesById.containsKey(initialId)) {
                        throw new IllegalArgumentException("Filter parameter #" + initialId + " is not defined");
                    }
                    item.getParamValCount().setParamId(uniqueId);
                    if (!paramValuesByUniqueId.containsKey(uniqueId)) {
                        final RadFilterParamValue paramValue = paramValuesById.get(initialId);
                        paramValuesByUniqueId.put(uniqueId, paramValue.changeParamId(uniqueId));
                    }
                } else if (item.isSetIfParam()){
                    final Id initialId = item.getIfParam().getParamId();
                    final Id uniqueId = Id.Factory.append(initialId, idPostfix);
                    if (!paramValuesById.containsKey(initialId)) {
                        throw new IllegalArgumentException("Filter parameter #" + initialId + " is not defined");
                    }
                    item.getIfParam().setParamId(uniqueId);
                    if (!paramValuesByUniqueId.containsKey(uniqueId)) {
                        final RadFilterParamValue paramValue = paramValuesById.get(initialId);
                        paramValuesByUniqueId.put(uniqueId, paramValue.changeParamId(uniqueId));
                    }
                }
            }
            for (RadFilterParamValue paramValue: paramValues){
                final Id initialId = paramValue.getParamId();
                final Id uniqueId = Id.Factory.append(initialId, idPostfix);
                if (!paramValuesByUniqueId.containsKey(uniqueId)) {
                    paramValuesByUniqueId.put(uniqueId, paramValue.changeParamId(uniqueId));
                }
            }
            return new RadContextFilter(predefinedFilterId, explorerItemId, condition, paramValuesByUniqueId);
        }
        
        public static RadContextFilter loadFromXml(final org.radixware.schemas.userexploreritem.ContextFilter xmlFilter){
            final Map<Id,RadFilterParamValue> paramValues;
            if (xmlFilter.getParamValues()==null || xmlFilter.getParamValues().getItemList()==null){
                paramValues = null;
            }else{
                paramValues = new HashMap<>();
                for (org.radixware.schemas.userexploreritem.FilterParameterValue value: xmlFilter.getParamValues().getItemList()){
                    paramValues.put(value.getId(), new RadFilterParamValue(value.getId(), value.getValType(), ValAsStr.Factory.loadFrom(value.getValue())));
                }
            }
            return new RadContextFilter(xmlFilter.getPredefinedFilterId(), 
                                                        xmlFilter.getExplorerItemId(), 
                                                        xmlFilter.getCondition(), 
                                                        paramValues);
        }
        
        public static List<RadContextFilter> splitUserFilter(final RadUserFilter userFilter,
                                                                                    final Collection<RadFilterParamValue> paramValues, 
                                                                                    final Id explorerItemId){
            final RadFilterDef baseFilter = userFilter.getBaseFilter();
            if (baseFilter==null){
                final RadContextFilter filter = RadContextFilter.Factory.newInstance(userFilter, paramValues, explorerItemId);
                return Collections.<RadContextFilter>singletonList(filter);
            }else{
                final List<RadFilterParamValue> baseFilterParamValues = new LinkedList<>();
                final List<RadFilterParamValue> userFilterParamValues = new LinkedList<>();                
                final List<ISqmlParameter> baseFilterParameters = baseFilter.getParameters().getAll();
                if (paramValues!=null){
                    for (RadFilterParamValue paramValue: paramValues){
                        if (containsParameter(baseFilterParameters, paramValue.getParamId())){
                            baseFilterParamValues.add(paramValue);
                        }else{
                            userFilterParamValues.add(paramValue);
                        }
                    }
                }
                final List<RadContextFilter> contextFilters = new LinkedList<>();
                contextFilters.add(RadContextFilter.Factory.newInstance(baseFilter, baseFilterParamValues, explorerItemId));
                contextFilters.add(RadContextFilter.Factory.newInstance(userFilter, userFilterParamValues, explorerItemId));
                return contextFilters;
            }            
        }
        
        private static boolean containsParameter(final List<ISqmlParameter> parameters, final Id paramId ){
            if (parameters==null || paramId==null){
                return false;
            }else{
                for (ISqmlParameter parameter: parameters){
                    if (paramId.equals(parameter.getId())){
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private final Sqml condition;
    private final Id explorerItemId;
    private final Id predefinedFilterId;    
    private final Map<Id,RadFilterParamValue> parameterValuesById;
    
    private RadContextFilter(final Id predefinedFilterId,
                                          final Id explorerItemId,
                                          final Sqml filterCondition,
                                          final Map<Id,RadFilterParamValue> params){
        this.predefinedFilterId = predefinedFilterId;
        this.explorerItemId = explorerItemId;
        condition = filterCondition==null ? null :  (Sqml)filterCondition.copy();
        if (params==null){
            parameterValuesById = Collections.emptyMap();
        }else{
            parameterValuesById = params;
        }
    }    
    
    public org.radixware.schemas.userexploreritem.ContextFilter writeToXml(final org.radixware.schemas.userexploreritem.ContextFilter xml){
        final org.radixware.schemas.userexploreritem.ContextFilter xmlFilter;
        if (xml==null){
            xmlFilter = org.radixware.schemas.userexploreritem.ContextFilter.Factory.newInstance();
        }else{
            xmlFilter = xml;            
        }
        if (condition!=null){
            xmlFilter.setCondition(condition);
        }
        if (predefinedFilterId!=null){
            xmlFilter.setPredefinedFilterId(predefinedFilterId);
        }
        if (explorerItemId!=null){
            xmlFilter.setExplorerItemId(explorerItemId);
        }
        if (!parameterValuesById.isEmpty()){
            final org.radixware.schemas.userexploreritem.ContextFilter.ParamValues paramValues = xmlFilter.addNewParamValues();                    
            for (RadFilterParamValue paramValue: parameterValuesById.values()){
                final org.radixware.schemas.userexploreritem.FilterParameterValue filterParamValue = paramValues.addNewItem();                    
                filterParamValue.setId(paramValue.getParamId());
                filterParamValue.setValType(paramValue.getType());
                if (paramValue.getValue()==null){
                    filterParamValue.setValue(null);
                }else{
                    filterParamValue.setValue(paramValue.getValue().toString());
                }
            }
        }
        return xmlFilter;
    }
    
    public Id getPredefinedFilterId(){
        return predefinedFilterId;
    }
    
    public Id getExplorerItemId(){
        return explorerItemId;
    }

    public Sqml getCondition() {
        return condition==null ? null : (Sqml)condition.copy();
    }

    public Collection<RadFilterParamValue> getParameterValues() {
        return parameterValuesById.values();
    }
    
    public String validate(final IClientEnvironment environment,
                                    final RadSelectorPresentationDef contextPresentation){
        if (condition==null){
            if (predefinedFilterId==null){
                return environment.getMessageProvider().translate("ExplorerMessage", "No condition or filter identifier defined");
            }else{
                return validatePredefinedFilter(environment, contextPresentation);
            }
        }else{            
            if (contextPresentation.isCustomFiltersEnabled()){
                return null;
            }else{
                if (predefinedFilterId==null){
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("ExplorerMessage", "Custom filters is not accessible in selector presentation %1$s (#%2$s)");
                    return String.format(messageTemplate, contextPresentation.getName(), contextPresentation.getId().toString());
                }else{
                    return validatePredefinedFilter(environment, contextPresentation);
                }
            }
        }
    }
    
    private String validatePredefinedFilter(final IClientEnvironment environment,
                                                              final RadSelectorPresentationDef contextPresentation){
        if (contextPresentation.isFilterExists(predefinedFilterId)){
            if (explorerItemId==null){
                return environment.getMessageProvider().translate("ExplorerMessage", "Explorer item identifier was not defined");
            }
            final RadFilterDef filterDef = contextPresentation.getFilterDefById(predefinedFilterId);
            final String idPostfix = "-"+explorerItemId.toString();
            final List<ISqmlParameter> filterParameters = filterDef.getParameters().getAll();
            Id paramId;
            for (ISqmlParameter parameter: filterParameters){
                paramId = idPostfix==null ? parameter.getId() : Id.Factory.append(parameter.getId(), idPostfix);
                if (!parameterValuesById.containsKey(paramId)){
                    if (parameter.isMandatory()){
                        final String messageTemplate = 
                            environment.getMessageProvider().translate("ExplorerMessage", "Value of parameter %1$s (#%2$s) was not defined");
                        return String.format(messageTemplate, parameter.getFullName(), parameter.getId().toString());
                    }else{
                        parameterValuesById.put(paramId, new RadFilterParamValue(paramId, parameter.getType(), parameter.getInitialVal()));
                    }
                }
            }
            return null;
        }else{
            final String messageTemplate = 
                environment.getMessageProvider().translate("ExplorerMessage", "Filter #%1$s is not accessible in selector presentation %2$s (#%3$s)");
            return String.format(messageTemplate, predefinedFilterId, contextPresentation.getName(), contextPresentation.getId().toString());
        }        
    }
}
