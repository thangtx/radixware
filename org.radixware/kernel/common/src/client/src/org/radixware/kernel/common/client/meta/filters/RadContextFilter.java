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
import java.util.Map;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.Sqml;


public final class RadContextFilter {
    
    public static class Factory{
        
        private Factory(){            
        }
        
        public static RadContextFilter newInstance(final RadFilterDef filter, final Collection<RadFilterParamValue> paramValues, final Id explorerItemId){
            final Sqml condition;                        
            if (filter instanceof RadUserFilter){
                final RadUserFilter userFilter = (RadUserFilter)filter;
                final Sqml mergedCondition;
                if (userFilter.getBaseFilter()==null){
                    mergedCondition = userFilter.getCondition();
                }else{
                    mergedCondition = 
                        SqmlExpression.mergeConditions(userFilter.getBaseFilter().getCondition(), userFilter.getCondition());
                }
                condition = mergedCondition;                
            }else{
                condition = filter.getCondition();                
            }
            final Map<Id,RadFilterParamValue> paramValuesById = new HashMap<>();
            final Map<Id,RadFilterParamValue> paramValuesByUniqueId = new HashMap<>();
            if (paramValues!=null){
                for (RadFilterParamValue paramValue: paramValues){
                    paramValuesById.put(paramValue.getParamId(), paramValue);
                }
            }
            final String idPostfix = "-"+explorerItemId.toString();            
            for (Sqml.Item item : condition.getItemList()) {
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
                }
            }
            return new RadContextFilter(condition, paramValuesByUniqueId.values());
        }                
        
        public static RadContextFilter loadFromXml(final org.radixware.schemas.userexploreritem.ContextFilter xmlFilter){
            final Collection<RadFilterParamValue> paramValues;
            if (xmlFilter.getParamValues()==null || xmlFilter.getParamValues().getItemList()==null){
                paramValues = null;
            }else{
                paramValues = new LinkedList<>();
                for (org.radixware.schemas.userexploreritem.FilterParameterValue value: xmlFilter.getParamValues().getItemList()){
                    paramValues.add(new RadFilterParamValue(value.getId(), value.getValType(), ValAsStr.Factory.loadFrom(value.getValue())));
                }
            }
            return new RadContextFilter(xmlFilter.getCondition(), paramValues);
        }
        
        public static RadContextFilter merge(final RadContextFilter base, final RadContextFilter addintional){
            if (base==null){
                return addintional;
            }
            if (addintional==null){
                return base;
            }
            final Sqml condition = 
                SqmlExpression.mergeConditions(base.getCondition(), addintional.getCondition());
            final Collection<RadFilterParamValue> parameterValues = 
                new LinkedList<>(base.getParameterValues());
            parameterValues.addAll(addintional.getParameterValues());
            return new RadContextFilter(condition, parameterValues);
        }
        
    }

    private final Sqml condition;
    private final Collection<RadFilterParamValue> parameterValues;
    
    private RadContextFilter(final Sqml filterCondition, final Collection<RadFilterParamValue> paramValues){
        condition = (Sqml)filterCondition.copy();
        if (paramValues==null){
            parameterValues = Collections.<RadFilterParamValue>emptyList();
        }else{
            parameterValues = Collections.unmodifiableCollection(paramValues);
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
        if (!parameterValues.isEmpty()){
            final org.radixware.schemas.userexploreritem.ContextFilter.ParamValues paramValues = xmlFilter.addNewParamValues();                               
            for (RadFilterParamValue paramValue: parameterValues){
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

    public Sqml getCondition() {
        return condition==null ? null : (Sqml)condition.copy();
    }

    public Collection<RadFilterParamValue> getParameterValues() {
        return parameterValues;
    }
}
