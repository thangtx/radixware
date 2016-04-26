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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterFactory;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.FilterParameterPersistentValue;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IParameterCreationWizard;
import org.radixware.kernel.common.client.views.IParameterEditorDialog;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public final class RadFilterParameters implements ISqmlParameters {

    private final List<ISqmlParameter> parameters = new ArrayList<>();
    private final List<Id> customParameterIds = new ArrayList<>();
    private final Id classId;
    private final Map<Id, Object> persistentValues = new HashMap<>();
    private final boolean allowPersistentValues;
    
    private final static ISqmlParameterFactory PERSONAL_FILTER_PARAMETER_FACTORY = new ISqmlParameterFactory() {

        @Override
        public ISqmlModifiableParameter createModifableParameter(EValType valType) {
            return RadFilterUserParamDef.Factory.newInstance(valType,true);
        }

        @Override
        public ISqmlModifiableParameter createModifableParameter(ISqmlColumnDef column) {
            return RadFilterUserParamDef.Factory.newInstance(column,true);
        }

        @Override
        public ISqmlModifiableParameter createModifableParameter(RadPropertyDef targetProperty) {
            return RadFilterUserParamDef.Factory.newInstance(targetProperty,true);
        }
    };

    private final static ISqmlParameterFactory COMMON_FILTER_PARAMETER_FACTORY = new ISqmlParameterFactory() {

        @Override
        public ISqmlModifiableParameter createModifableParameter(EValType valType) {
            return RadFilterUserParamDef.Factory.newInstance(valType,false);
        }

        @Override
        public ISqmlModifiableParameter createModifableParameter(ISqmlColumnDef column) {
            return RadFilterUserParamDef.Factory.newInstance(column,false);
        }

        @Override
        public ISqmlModifiableParameter createModifableParameter(RadPropertyDef targetProperty) {
            return RadFilterUserParamDef.Factory.newInstance(targetProperty,false);
        }
    };
    
    public final static class Factory {

        private Factory() {
        }

        public static RadFilterParameters loadFrom(final IClientEnvironment environment, org.radixware.schemas.groupsettings.FilterParameters params, final RadFilterDef baseFilter, final Id ownerTableId) {
            return new RadFilterParameters(environment, params, baseFilter, ownerTableId);
        }                
    }

    //Конструктор для предопределенного фильтра
    RadFilterParameters(final RadFilterParamDef[] parameters, final Id ownerClassId) {
        classId = ownerClassId;
        if (parameters != null) {
            this.parameters.addAll(Arrays.asList(parameters));
        }
        allowPersistentValues = true;
    }

    //Конструктор для пользовательского фильтра
    RadFilterParameters(final IClientEnvironment environment, final org.radixware.schemas.groupsettings.CustomFilter xmlFilter, final RadFilterDef baseFilter, final Id ownerClassId) {
        classId = ownerClassId;
        if (baseFilter != null) {
            for (ISqmlParameter parameter : baseFilter.getParameters().getAll()) {
                parameters.add(new RadFilterInheritedParamDef((RadFilterParamDef) parameter));
            }
        }
        if (xmlFilter != null) {
            final org.radixware.schemas.groupsettings.FilterParameters params =
                    xmlFilter.getParameters();
            if (params != null && params.getParameterList() != null) {
                RadFilterUserParamDef customParameter;
                for (org.radixware.schemas.groupsettings.FilterParameters.Parameter parameter : params.getParameterList()) {
                    customParameter = RadFilterUserParamDef.Factory.loadFromXml(environment, parameter, true);
                    customParameterIds.add(customParameter.getId());
                    parameters.add(customParameter);
                }
            }
            final org.radixware.schemas.groupsettings.CustomFilter.PersistentValues persistentVals =
                    xmlFilter.getPersistentValues();
            if (persistentVals != null && persistentVals.getPersistentValueList() != null) {
                final List<org.radixware.schemas.groupsettings.CustomFilter.PersistentValues.PersistentValue> valuesList =
                        persistentVals.getPersistentValueList();
                Id parameterId;
                ISqmlParameter parameter;
                for (org.radixware.schemas.groupsettings.CustomFilter.PersistentValues.PersistentValue paramValue : valuesList) {
                    parameterId = paramValue.getParamId();
                    parameter = getParameterById(parameterId);
                    if (parameter != null && 
                        (parameter instanceof RadFilterUserParamDef==false || ((RadFilterUserParamDef)parameter).isValid())
                       ) {
                        final FilterParameterPersistentValue value =
                                FilterParameterPersistentValue.loadFromXml(environment, parameter, paramValue);
                        if (value != null) {
                            parameter.setPersistentValue(value);
                            persistentValues.put(parameter.getId(), value);
                        }
                    }
                }
            }
        }
        allowPersistentValues = true;
    }
    
    //Конструктор для общего фильтра
    private RadFilterParameters(final IClientEnvironment environment, final org.radixware.schemas.groupsettings.FilterParameters params, final RadFilterDef baseFilter, final Id ownerTableId) {
        classId = ownerTableId;
        if (baseFilter != null) {
            for (ISqmlParameter parameter : baseFilter.getParameters().getAll()) {
                parameters.add((RadFilterParamDef) parameter);
            }
        }
        if (params != null && params.getParameterList() != null) {
            RadFilterUserParamDef customParameter;
            for (org.radixware.schemas.groupsettings.FilterParameters.Parameter parameter : params.getParameterList()) {
                customParameter = RadFilterUserParamDef.Factory.loadFromXml(environment, parameter, false);
                customParameterIds.add(customParameter.getId());
                parameters.add(customParameter);
            }
        }
        allowPersistentValues = false;
    }

    @Override
    public ISqmlParameter getParameterById(Id parameterId) {
        for (ISqmlParameter parameter : parameters) {
            if (parameter.getId().equals(parameterId)) {
                return parameter;
            }
        }
        return null;
    }

    @Override
    public int addParameter(final ISqmlModifiableParameter parameter) {
        parameters.add(parameter);        
        return parameters.size() - 1;
    }    
    
    @Override
    public boolean swapParameters(final int index1, final int index2){
        if ((parameters.get(index1) instanceof ISqmlModifiableParameter) && 
            (parameters.get(index2) instanceof ISqmlModifiableParameter)) {
            Collections.swap(parameters, index1, index2);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeParameter(final int idx) {
        if (idx >= 0 && idx < parameters.size() && (parameters.get(idx) instanceof ISqmlModifiableParameter)) {
            parameters.remove(idx);
            return true;
        }
        return false;
    }

    @Override
    public List<ISqmlParameter> getAll() {
        return Collections.unmodifiableList(parameters);
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public org.radixware.schemas.groupsettings.FilterParameters writeCustomParametersToXml(final org.radixware.schemas.groupsettings.FilterParameters xml) {
        final org.radixware.schemas.groupsettings.FilterParameters xmlParameters;
        if (xml==null){
            xmlParameters = org.radixware.schemas.groupsettings.FilterParameters.Factory.newInstance();
        }else{
            xmlParameters = xml;
        }
        customParameterIds.clear();
        for (ISqmlParameter parameter : getAll()) {
            if (parameter instanceof RadFilterUserParamDef) {
                if (parameter instanceof ISqmlModifiableParameter) {
                    ((RadFilterUserParamDef) parameter).writeToXml(xmlParameters);
                    customParameterIds.add(parameter.getId());
                }
            }
        }
        return xmlParameters;
    }
    
    public org.radixware.schemas.groupsettings.CustomFilter.PersistentValues writePersistentValuesToXml(org.radixware.schemas.groupsettings.CustomFilter.PersistentValues xml){
        final org.radixware.schemas.groupsettings.CustomFilter.PersistentValues xmlParamValues;
        if (xml==null){
            xmlParamValues = org.radixware.schemas.groupsettings.CustomFilter.PersistentValues.Factory.newInstance();
        }else{
            xmlParamValues = xml;
        }
        org.radixware.schemas.groupsettings.CustomFilter.PersistentValues.PersistentValue xmlParamValue;
        for (ISqmlParameter parameter : getAll()) {
            if (parameter.getPersistentValue() != null) {
                xmlParamValue = xmlParamValues.addNewPersistentValue();
                xmlParamValue.setParamId(parameter.getId());
                final String valAsStr = parameter.getPersistentValue().getValAsStr();
                if (valAsStr != null) {
                    xmlParamValue.setValAsStr(valAsStr);
                }
                if (parameter.getPersistentValue().getEditorPresentationId() != null) {
                    xmlParamValue.setEditorPresentationId(parameter.getPersistentValue().getEditorPresentationId());
                }
            }
        }
        return xmlParamValues;
    }

    public final boolean wasModified() {
        for (ISqmlParameter parameter : getAll()) {
            if ((parameter instanceof RadFilterUserParamDef)
                    && (((RadFilterUserParamDef) parameter).wasModified() || !customParameterIds.contains(parameter.getId()))) {
                return true;
            }
            if (parameter.getPersistentValue() != null
                    && (!persistentValues.containsKey(parameter.getId())
                    || !Utils.equals(parameter.getPersistentValue().getValObject(), persistentValues.get(parameter.getId())))) {
                return true;
            }
            if (parameter.getPersistentValue() == null && persistentValues.containsKey(parameter.getId())) {
                return true;
            }
        }
        for (Id paramId : customParameterIds) {
            if (getParameterById(paramId) == null) {
                return true;
            }
        }
        return false;
    }
    
    final void switchToFixedState(){
        customParameterIds.clear();
        persistentValues.clear();
        for (ISqmlParameter parameter : getAll()) {
            if (parameter instanceof RadFilterUserParamDef){
                ((RadFilterUserParamDef)parameter).switchToFixedState();
                customParameterIds.add(parameter.getId());
            }
            if (parameter.getPersistentValue()!=null){
                persistentValues.put(parameter.getId(), parameter.getPersistentValue());
            }
        }
    }
    
    private final List<ISqmlParameter> saveParameters = new ArrayList<>();

    public void saveState() {
        saveParameters.clear();
        for (ISqmlParameter parameter : getAll()) {
            if (parameter instanceof RadFilterUserParamDef) {
                saveParameters.add(((RadFilterUserParamDef) parameter).copy());
            } else if (parameter instanceof RadFilterInheritedParamDef) {
                saveParameters.add(((RadFilterInheritedParamDef) parameter).copy());
            } else {
                saveParameters.add(parameter);
            }
        }
    }

    public void restoreState() {
        parameters.clear();
        parameters.addAll(saveParameters);
    }

    @Override
    public int size() {
        return parameters.size();
    }

    public int customParametersCount() {
        int count = 0;
        for (ISqmlParameter parameter : parameters) {
            if (parameter instanceof ISqmlModifiableParameter) {
                count++;
            }
        }
        return count;
    }

    @Override
    public ISqmlParameter get(int index) {
        return parameters.get(index);
    }

    private List<String> getParameterNames() {
        final List<String> parameterNames = new ArrayList<>();
        for (ISqmlParameter parameter : parameters) {
            parameterNames.add(parameter.getTitle());
        }
        return parameterNames;
    }

    private ISqmlTableDef getContext(IClientEnvironment environment) {
        final ISqmlDefinitions definitions = environment.getSqmlDefinitions();
        return definitions.findTableById(classId);
    }

    @Override
    public ISqmlModifiableParameter createNewParameter(final IClientEnvironment environment, final IWidget parent) {
        final ISqmlTableDef table = getContext(environment);
        final List<String> parameterNames = getParameterNames();        
        final ISqmlParameterFactory parameterFactory;
        if (allowPersistentValues){
            parameterFactory = PERSONAL_FILTER_PARAMETER_FACTORY;
        }
        else{
            parameterFactory = COMMON_FILTER_PARAMETER_FACTORY;
        }
        final IParameterCreationWizard wizard = 
            environment.getApplication().getStandardViewsFactory().newParameterCreationWizard(environment, table, parameterFactory, parameterNames, parent);
        if (wizard.execDialog() == DialogResult.ACCEPTED) {
            return wizard.getParameter();
        } else {
            return null;
        }
    }

    @Override
    public DialogResult openParameterEditor(final IClientEnvironment environment, final ISqmlParameter parameter, final boolean readonly, final IWidget parent) {
        final ISqmlTableDef table = getContext(environment);
        final List<String> parameterNames = getParameterNames();
        parameterNames.remove(parameter.getTitle());
        final IParameterEditorDialog dialog = environment.getApplication().getStandardViewsFactory().newParameterEditorDialog(environment, parameter, table, parameterNames, readonly, parent);
        return dialog.execDialog();
    }

    public boolean allParametersPersistent(IClientEnvironment environment) {
        for (ISqmlParameter parameter : parameters) {
            if (parameter.getPersistentValue() == null || !parameter.getPersistentValue().isValid(environment)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isValid(){
        for (ISqmlParameter parameter : getAll()) {
            if (parameter instanceof RadFilterUserParamDef && !((RadFilterUserParamDef)parameter).isValid()){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isReadonly() {
        return false;
    }
    
    final long getChangeValueTimestamp(){
        long max = Long.MIN_VALUE;
        for (ISqmlParameter parameter : parameters) {
            if (parameter instanceof RadFilterParamDef){
                max = Math.max(max,((RadFilterParamDef)parameter).getChangeValueTimestamp());
            }
        }
        return max;
    }
}
