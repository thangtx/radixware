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

package org.radixware.kernel.common.client.models.groupsettings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;

import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyReference;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;


public class FilterSettingsStorage {

    public FilterSettingsStorage() {
    }

    protected final static class FilterParameters {

        private final Map<Id, ValAsStr> valuesByParamId = new HashMap<>(8);
        private final Map<Id, Reference> refsByParamId = new HashMap<>(4);
        private final Map<Id, ArrRef> arrRefsByParamId = new HashMap<>(4);

        protected FilterParameters(FilterModel filter) {
            saveParameters(filter);
        }

        public final void saveParameters(final FilterModel filter) {
            valuesByParamId.clear();
            final Collection<Property> properties = filter.getActiveProperties();
            Object value;
            for (Property property : properties) {                
                value = property.getValueObject();
                if (value instanceof IKernelEnum) {
                    value = ((IKernelEnum) value).getValue();
                }
                final EValType valType = ValueConverter.serverValType2ClientValType(property.getType());
                switch(valType){
                    case PARENT_REF:{
                        if (value instanceof Reference || value==null){
                            refsByParamId.put(property.getId(), (Reference)value);
                        }else{
                            final String messageTemplate = 
                                filter.getEnvironment().getMessageProvider().translate("TraceMessage", "Value of filter parameter %1$s (#%2$s) has unexpected type %3$s");
                            final String message = 
                                String.format(messageTemplate, property.getDefinition().getName(), property.getId().toString(), value.getClass().getName());
                            filter.getEnvironment().getTracer().error(message);
                        }
                        break;
                    }
                    case ARR_REF:{
                        if (value instanceof ArrRef || value==null){
                            arrRefsByParamId.put(property.getId(), (ArrRef)value);
                        }else{
                            final String messageTemplate = 
                                filter.getEnvironment().getMessageProvider().translate("TraceMessage", "Value of filter parameter %1$s (#%2$s) has unexpected type %3$s");
                            final String message = 
                                String.format(messageTemplate, property.getDefinition().getName(), property.getId().toString(), value.getClass().getName());
                            filter.getEnvironment().getTracer().error(message);                            
                        }
                        break;
                    }
                    default:
                        valuesByParamId.put(property.getId(), ValAsStr.Factory.newInstance(value, valType));
                }
            }
        }

        public final boolean hasValue(final Id parameterId) {
            return valuesByParamId.containsKey(parameterId) || 
                   refsByParamId.containsKey(parameterId) || 
                   arrRefsByParamId.containsKey(parameterId);
        }

        public final Object getValue(final Id parameterId, final EValType valType) {
            if (valType==EValType.PARENT_REF){
                return refsByParamId.get(parameterId);
            }
            else if (valType==EValType.ARR_REF){
                return arrRefsByParamId.get(parameterId);
            }            
            return valuesByParamId.get(parameterId)==null ? null : valuesByParamId.get(parameterId).toObject(valType);
        }
    }

    public final static class FilterSettings {
        
        private final static String SAVE_LAST_FILTER_OPTION_KEY = 
            SettingNames.SYSTEM+"/"+SettingNames.SELECTOR_GROUP+"/"+ SettingNames.Selector.COMMON_GROUP+"/"+SettingNames.Selector.Common.SAVE_FILTER;

        private Id lastFilterId;
        private Id lastSortingId;
        private boolean lastFilterApplyed;
        private boolean filterParamsCollapsed;
        private int filterToolBarHeight;
        private final Map<Id, FilterParameters> parametersByFilterId = new HashMap<>(8);
        private final IClientEnvironment environment;

        protected FilterSettings(IClientEnvironment environment) {
            this.environment = environment;
        }
        
        private boolean keepLastFilter(){
            return environment.getConfigStore().readBoolean(SAVE_LAST_FILTER_OPTION_KEY, true);
        }

        public void saveLastFilter(final Id filterId, 
                                              final Id sortingId, 
                                              final boolean wasApplyed,
                                              final boolean wasCollapsed,
                                              final int filterToolBarHeight) {
            if (keepLastFilter()){
                lastFilterId = filterId;
                lastSortingId = sortingId;
                lastFilterApplyed = wasApplyed;
                filterParamsCollapsed = lastFilterApplyed && wasCollapsed;
                this.filterToolBarHeight = filterParamsCollapsed ? -1 : filterToolBarHeight;
            }
        }

        public void saveFilterParameters(FilterModel filter) {
            final FilterParameters filterParameters = parametersByFilterId.get(filter.getId());
            if (filterParameters == null) {
                parametersByFilterId.put(filter.getId(), new FilterParameters(filter));
            } else {
                filterParameters.saveParameters(filter);
            }
        }

        @SuppressWarnings("unchecked")
        public boolean restoreFilterParameter(final Id filterId, final Property parameter) {
            final FilterParameters filterParameters = parametersByFilterId.get(filterId);
            if (filterParameters != null && filterParameters.hasValue(parameter.getId())) {
                try {
                    final EValType valType = ValueConverter.serverValType2ClientValType(parameter.getType());
                    final Object value = filterParameters.getValue(parameter.getId(), valType);
                    if (parameter.isLocal()){
                        if (parameter instanceof SimpleProperty){
                            ((SimpleProperty)parameter).setInitialValue(value);
                        }else if (parameter instanceof PropertyReference){
                            ((PropertyReference)parameter).setInitialValue((Reference)value);
                        }
                    }else{
                        parameter.setServerValue(new PropertyValue(parameter.getDefinition(), value));
                    }                    
                } catch (Exception ex) {
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Can't restore value of filter parameter %s:\n%s");
                    environment.getTracer().warning(String.format(message, parameter.getDefinition().toString(), ClientException.exceptionStackToString(ex)));
                    return false;
                }
                return true;
            }
            return false;
        }

        public final Id getLastFilterId() {
            return keepLastFilter() ? lastFilterId : null;            
        }
        
        public final Id getLastSortingId() {
            return lastSortingId;
        }

        public final boolean lastFilterWasApplyed() {
            return lastFilterApplyed;
        }
        
        public final boolean filterParamsWasCollapsed(){
            return filterParamsCollapsed;
        }
        
        public final int getFilterToolBarHeight(){
            return filterToolBarHeight;
        }

        public final void clear() {
            parametersByFilterId.clear();
        }
    }
    
    private final Map<String, FilterSettings> settings = new HashMap<>(128);

    public FilterSettings getFilterSettings(final GroupModel group) {
        synchronized (settings) {
            final String key = group.getConfigStoreGroupName();
            FilterSettings filterSettings = settings.get(key);
            if (filterSettings == null) {
                filterSettings = new FilterSettings(group.getEnvironment());
                settings.put(key, filterSettings);
            }
            return filterSettings;
        }
    }

    public void clearFilterSettings(final GroupModel group) {
        synchronized (settings) {
            final String key = group.getConfigStoreGroupName();
            settings.remove(key);
        }
    }

    public boolean isFilterSettingsStored(final GroupModel group) {
        synchronized (settings) {
            final String key = group.getConfigStoreGroupName();
            return settings.containsKey(key);
        }
    }

    public void clear() {
        synchronized (settings) {
            for (FilterSettings filterSettings : settings.values()) {
                filterSettings.clear();
            }
            settings.clear();
        }
    }
}
