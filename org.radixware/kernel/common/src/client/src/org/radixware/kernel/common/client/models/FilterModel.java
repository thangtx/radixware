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

package org.radixware.kernel.common.client.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.models.groupsettings.FilterSettingsStorage;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IFilterEditorDialog;
import org.radixware.kernel.common.client.views.IFilterParametersView;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;


public class FilterModel extends ModelWithPages implements IGroupSetting {

    private final RadFilterDef filter;
    private RadFilterDef baseFilter = null;
    private org.radixware.schemas.xscml.Sqml userCondition = null;
    private org.radixware.schemas.xscml.Sqml finalCondition = null;
    private boolean userConditionWasReceived;
    private boolean conditionWasCalculated;
    private boolean isEdited, propertiesWasActivated, doDisableView;

    public FilterModel(final IClientEnvironment environment,final RadFilterDef def) {
        super(environment,def);
        filter = def;
        invalidateContition();    
    }

    public final void invalidate() {
        clean();
        if (isUserDefined()) {
            userCondition = null;
            userConditionWasReceived = false;
        }
        invalidateContition();
    }
    
    private void invalidateContition(){
        conditionWasCalculated = false;
        finalCondition = null;        
    }
    
    @Override
    public void setView(final IView view_) {
        super.setView(view_);
        if (view_ == null) {
            cleanPages();
       }
    }

    @Override
    public void clean() {
        //save parameter values
        if (propertiesWasActivated) {
            final GroupModel ownerGroup = getFilterContext().ownerGroup;
            final FilterSettingsStorage.FilterSettings settings = 
                    getEnvironment().getFilterSettingsStorage().getFilterSettings(ownerGroup);
            settings.saveFilterParameters(this);
        }

        propertiesWasActivated = false;
        super.clean();
    }

    public RadFilterDef getFilterDef() {
        return filter;
    }

    public IContext.Filter getFilterContext() {
        return getContext() != null ? (IContext.Filter) getContext() : null;
    }

    public IFilterParametersView getFilterView() {
        return (IFilterParametersView) getView();
    }

    @Override
    public void setContext(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");            
        }
        if (!(context instanceof IContext.Filter)) {
            throw new IllegalArgumentException("\"" + context.getClass().getName()
                    + "\" is not suitable context for filter model.");
        }
        super.setContext(context);
        if (isUserDefined()) {
            final Id baseFilterId = ((RadUserFilter) filter).baseFilterId;
            if (baseFilterId != null) {
                final RadSelectorPresentationDef presentation = ((IContext.Filter) context).ownerGroup.getSelectorPresentationDef();
                if (presentation.isFilterExists(baseFilterId)) {
                    baseFilter = presentation.getFilterDefById(baseFilterId);
                    conditionWasCalculated = false;
                } else {
                    final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "can't find predefined filter #%s in presentation %s");
                    getEnvironment().getTracer().put(EEventSeverity.ERROR, String.format(message, baseFilterId, presentation.toString()), EEventSource.EXPLORER);
                }
            }
        }
        properties = new HashMap<>();
    }

    @Override
    public Collection<Property> getActiveProperties() {
        if (!propertiesWasActivated) {
            propertiesWasActivated = true;
            activateProperties();
        }
        return super.getActiveProperties();
    }

    public boolean hasParameters() {
        return !getFilterDef().getParameters().isEmpty() || getCustomViewId() != null;
    }

    private void activateProperties() {
        for (ISqmlParameter parameter : filter.getParameters().getAll()) {
            if (properties == null || !properties.containsKey(parameter.getId())) {
                getProperty(parameter.getId());//activate property
            }
        }
    }

    @Override
    protected Property activateProperty(final Id propertyId) {
        final Property property = super.activateProperty(propertyId);
        final GroupModel ownerGroup = getFilterContext().ownerGroup;
        final ISqmlParameter parameter = property.getDefinition() instanceof ISqmlParameter ? (ISqmlParameter) property.getDefinition() : null;
        final FilterSettingsStorage filterSettings = getEnvironment().getFilterSettingsStorage();
        if (parameter != null && parameter.canHavePersistentValue() && parameter.getPersistentValue() != null && parameter.getPersistentValue().isValid(getEnvironment())) {
            try {
                property.setServerValue(new PropertyValue(property.getDefinition(), parameter.getPersistentValue().getValObject()));
            } catch (Exception ex) {
                final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Can't set persistent value of filter parameter %s:\n%s");
                getEnvironment().getTracer().warning(String.format(message, property.getDefinition().toString(), ClientException.exceptionStackToString(ex)));
            }
        } else if (filterSettings.isFilterSettingsStored(ownerGroup)) {
            //try to restore saved parameter value
            final FilterSettingsStorage.FilterSettings settings = filterSettings.getFilterSettings(ownerGroup);
            if (!settings.restoreFilterParameter(getId(), property)) {
                final RadPropertyDef paramDef = property.getDefinition();
                Object val = null;
                try {
                    final EValType valType = ValueConverter.serverValType2ClientValType(paramDef.getType());
                    val = paramDef.getInitialVal() != null ? paramDef.getInitialVal().toObject(valType) : null;
                } catch (WrongFormatError error) {
                    final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Can't set initial value of filter parameter %s:\n%s");
                    getEnvironment().getTracer().warning(String.format(message, paramDef.toString(), ClientException.exceptionStackToString(error)));
                }
                try {
                    property.setServerValue(new PropertyValue(paramDef, val));
                } catch (Exception ex) {
                    final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Can't set initial value of filter parameter %s:\n%s");
                    getEnvironment().getTracer().warning(String.format(message, paramDef.toString(), ClientException.exceptionStackToString(ex)));
                }
            }
        } else {
            final RadPropertyDef paramDef = property.getDefinition();
            Object val = null;
            try {
                final EValType valType = ValueConverter.serverValType2ClientValType(paramDef.getType());
                val = paramDef.getInitialVal() != null ? paramDef.getInitialVal().toObject(valType) : null;
            } catch (WrongFormatError error) {
                final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Can't set initial value of filter parameter %s:\n%s");
                getEnvironment().getTracer().warning(String.format(message, paramDef.toString(), ClientException.exceptionStackToString(error)));
            }
            try {
                property.setServerValue(new PropertyValue(paramDef, val));
            } catch (Exception ex) {
                final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Can't set initial value of filter parameter %s:\n%s");
                getEnvironment().getTracer().warning(String.format(message, paramDef.toString(), ClientException.exceptionStackToString(ex)));
            }
        }
        return property;
    }

    protected final Property createGroupProperty(final Id propertyId) {
        final GroupModel ownerGroup = getFilterContext().ownerGroup;
        return ownerGroup.createProperty(getPropertyDef(propertyId));
    }

    @Override
    protected RadPropertyDef getPropertyDef(Id propertyId) {
        final GroupModel ownerGroup = getFilterContext() == null ? null : getFilterContext().ownerGroup;
        if (ownerGroup != null && ownerGroup.getSelectorPresentationDef().isPropertyDefExistsById(propertyId)) {
            return ownerGroup.getSelectorPresentationDef().getPropertyDefById(propertyId);
        } else {
            return super.getPropertyDef(propertyId);
        }
    }

    public boolean canApply() {
        if (!isValid()) {
            return false;
        }
        for (Property property : getActiveProperties()) {
            if (!property.hasValidMandatoryValue())//RADIX-4803
            {
                return false;
            }
        }
        return true;
    }

    public org.radixware.schemas.xscml.Sqml getUserCondition() {
        if (isUserDefined()){
            if (!userConditionWasReceived){
                userCondition = filter.getCondition();
                userConditionWasReceived = true;
            }
            return userCondition != null ? (org.radixware.schemas.xscml.Sqml) userCondition.copy() : null;
        }else{
            return null;
        }
    }

    public void setUserCondition(org.radixware.schemas.xscml.Sqml condition) {
        if (!isUserDefined()) {
            throw new IllegalUsageError("Cannot change condition of predefined filter " + filter.toString());
        }
        if (getContext() == null) {
            throw new IllegalStateException("context is not defined");
        }
        userCondition = (org.radixware.schemas.xscml.Sqml) condition.copy();
        userConditionWasReceived = true;
        invalidateContition();
    }

    public void setUserCondition(SqmlExpression condition) {
        if (!isUserDefined()) {
            throw new IllegalUsageError("Cannot change condition of predefined filter " + filter.toString());
        }
        if (getContext() == null) {
            throw new IllegalStateException("context is not defined");
        }
        userCondition = condition != null ? condition.asXsqml() : null;
        userConditionWasReceived = true;
        invalidateContition();
    }

    public org.radixware.schemas.xscml.Sqml getFinalCondition() {
        if (!conditionWasCalculated){
            finalCondition = calcFinalCondition();
            conditionWasCalculated = true;
        }
        return finalCondition != null ? (org.radixware.schemas.xscml.Sqml) finalCondition.copy() : null;
    }

    @Override
    public final boolean isUserDefined() {
        return filter instanceof RadUserFilter;
    }
    
    public final boolean isCommon(){
        return filter instanceof RadCommonFilter;
    }

    @Override
    public final boolean isValid() {
        if (isUserDefined()) {
            return ((RadUserFilter) filter).isValid();
        }
        else if (isCommon()){
            return ((RadCommonFilter) filter).isExists();
        }
        return true;
    }

    @Override
    public boolean hasAncestor() {
        return isUserDefined() && ((RadUserFilter) filter).getBaseFilter() != null;
    }

    @Override
    public boolean wasModified() {
        return isUserDefined() ? ((RadUserFilter) filter).wasModified() : false;
    }

    @Override
    public String getName() {
        return filter.getTitle();
    }

    @Override
    public void setName(final String name) {
        if (!isUserDefined()) {
            throw new IllegalUsageError("Cannot change name of predefined filter " + filter.toString());
        }
        ((RadUserFilter) filter).setTitle(name);
    }

    @Override
    public boolean hasTitle() {
        return !isUserDefined() && filter.hasTitle();
    }

    @Override
    public String getTitle() {
        return filter.getTitle();
    }

    @Override
    public Icon getIcon() {
        if (super.getIcon()==null && !isUserDefined() && !isCommon()){
            return getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Definitions.FILTER);
        }
        return super.getIcon();
    }
        
    @Override
    public Id getId() {
        return filter.getId();
    }

    private org.radixware.schemas.xscml.Sqml calcFinalCondition() {
        if (!isUserDefined()) {
            return filter.getCondition();
        }

        if (!isValid()) {
            return getUserCondition();
        }

        if (baseFilter == null) {
            return getUserCondition();
        } else {
            return SqmlExpression.mergeConditions(baseFilter.getCondition(), getUserCondition());
        }
    }

    public boolean isPropertyValueEdited() {
        return isEdited;
    }

    public void setIsPropertyValueEdited(boolean edited) {
        if (edited == isEdited) {
            return;
        }
        final ISelector selector = getFilterContext().ownerGroup.getGroupView();
        if (edited &&  selector!= null && !doDisableView){            
            //onStartEditPropertyValue is not invoked in web-server mode
            doDisableView = true;//to avoid reсursive call of getGroupView().disable()
            selector.blockRedraw();
            try{
                isEdited = true;
                final List<Property> editableProperties = new ArrayList<>(getActiveProperties());
                for (int i=editableProperties.size()-1;i>=0;i--){
                    final Property property = editableProperties.get(i);
                    if (!property.isVisible() 
                        || !property.isEnabled()
                        || !property.hasSubscriber() 
                        || property.isReadonly()){
                        editableProperties.remove(i);
                    }
                }
                if (selector.disable() && editableProperties.size()==1 && canApply()){                    
                    final Property property = editableProperties.get(0);
                    final boolean canApplyNow = property.getType()==EValType.PARENT_REF ||
                                      property.isCustomEditOnly() ||
                                      property.getEditMask() instanceof EditMaskConstSet ||
                                      property.getEditMask() instanceof EditMaskList;
                    if (canApplyNow){
                        try{
                            selector.reread();
                        }catch(ServiceClientException exception){
                            getEnvironment().getTracer().error(exception);
                        }
                    }
                }
            }finally{
                doDisableView = false;
                selector.unblockRedraw();
            }
        }else{
            isEdited = edited;
        }
    }

//    public void wasModified
    public List<Property> getEditedProperties() {
        final ArrayList<Property> result = new ArrayList<>(properties != null ? properties.size() : 0);
        for (Property property : getActiveProperties()) {
            if (property.isValEdited()) {
                result.add(property);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public DialogResult openEditor(final IWidget parent) {
        final Filters filters = getFilterContext().ownerGroup.getFilters();
        final List<String> existingFilters = new ArrayList<>();
        existingFilters.addAll(filters.getAllSettingTitles());
        while (existingFilters.remove(getTitle())){/*do nothing*/};
        return openEditor(getEnvironment(), parent, existingFilters, false);
    }

    @Override
    public DialogResult openEditor(final IClientEnvironment environment, final IWidget parent, final Collection<String> restrictedNames, final boolean showApplyButton) {
        final IFilterEditorDialog dialog = environment.getApplication().getStandardViewsFactory().newFilterEditorDialog(this, restrictedNames, showApplyButton, parent);
        final DialogResult result = dialog.execDialog();
        if (result != DialogResult.REJECTED && wasModified()) {
            if (isUserDefined()) {      
                userCondition = null;
                userConditionWasReceived = false;
                invalidateContition();
            }
        }
        return result;
    }

    protected final void apply() throws PropertyIsMandatoryException, InvalidPropertyValueException, ServiceClientException, InterruptedException {
        getFilterContext().ownerGroup.setFilter(this);
    }

    public boolean beforeApply() {
        return true;
    }

    public void afterApply() {
    }

    @Override
    public void onStartEditPropertyValue(Property property) {
        //NEYVABANKRU-152
        super.onStartEditPropertyValue(property);
        if (getFilterContext().ownerGroup.getGroupView() != null && !doDisableView) {
            doDisableView = true;//to avoid reсursive call of getGroupView().disable()
            try{
                getFilterContext().ownerGroup.getGroupView().disable();
            }finally{
                doDisableView = false;
            }            
        }
    }

    @Override
    public boolean canInsertReferencedObjectIntoTree(Id propertyId) {        
        return false;
    }        
    
    final Long getChangeTimestamp(){
        return isUserDefined() ? Long.valueOf(((RadUserFilter)filter).getChangeTimestamp()) : null;
    }
}