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

package org.radixware.kernel.common.client.exceptions;

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadFormDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadReportPresentationDef;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;


public abstract class ModelPropertyException extends ModelException {

    private final IModelDefinition propertyOwner;
    private final Id propertyId, propertyOwnerId;
    private final boolean ownProperty;

    protected ModelPropertyException(final Model model, final Id ownerId, final Id propertyId) {
        super(model);
        this.propertyId = propertyId;
        ownProperty = isPropertyDefined(model, propertyId);
        
        if (ownProperty) {
            propertyOwner = model.getDefinition();
            propertyOwnerId = model.getDefinition().getId();
        } else {
            propertyOwner = null;
            propertyOwnerId = ownerId;
        }
    }

    private static RadPropertyDef findPropertyDef(IClientEnvironment environment, final Id ownerId, final Id propertyId) {
        final Id entityClassId;
        if (ownerId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            entityClassId = Id.Factory.changePrefix(ownerId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        } else if (ownerId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS
                || ownerId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            entityClassId = ownerId;
        } else {
            entityClassId = null;
        }
        try {
            if (entityClassId != null) {
                final RadClassPresentationDef classDef = environment.getApplication().getDefManager().getClassPresentationDef(entityClassId);
                if (classDef.isPropertyDefExistsById(propertyId)) {
                    return classDef.getPropertyDefById(propertyId);
                }
            } else if (ownerId.getPrefix() == EDefinitionIdPrefix.EDITOR_PRESENTATION) {
                final RadEditorPresentationDef presentation = environment.getApplication().getDefManager().getEditorPresentationDef(ownerId);
                if (presentation.isPropertyDefExistsById(propertyId)) {
                    return presentation.getPropertyDefById(propertyId);
                }
            } else if (ownerId.getPrefix() == EDefinitionIdPrefix.ADS_FORM_HANDLER_CLASS) {
                final RadFormDef form = environment.getApplication().getDefManager().getFormDef(ownerId);
                if (form.isPropertyDefExistsById(propertyId)) {
                    return form.getPropertyDefById(propertyId);
                }
            } else if (ownerId.getPrefix() == EDefinitionIdPrefix.REPORT || ownerId.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
                final RadReportPresentationDef reportDef = environment.getApplication().getDefManager().getReportPresentationDef(ownerId);
                if (reportDef.isPropertyDefExistsById(propertyId)) {
                    return reportDef.getPropertyDefById(propertyId);
                }
            }
        } catch (DefinitionError err) {
            return null;
        }
        return null;
    }

    private static String getDefinitionTitle(IClientEnvironment environment, final Id defId) {
        final Id entityClassId;
        if (defId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            entityClassId = Id.Factory.changePrefix(defId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        } else if (defId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS
                || defId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            entityClassId = defId;
        } else {
            entityClassId = null;
        }
        try {
            if (entityClassId != null) {
                final RadClassPresentationDef classDef = environment.getApplication().getDefManager().getClassPresentationDef(entityClassId);
                if (classDef.hasGroupTitle()) {
                    return classDef.getGroupTitle();
                } else if (!classDef.getClassTitle().isEmpty()) {
                    return classDef.getClassTitle();
                } else {
                    return classDef.getName();
                }
            } else {
                final TitledDefinition titledDefinition;
                if (defId.getPrefix() == EDefinitionIdPrefix.EDITOR_PRESENTATION) {
                    titledDefinition = environment.getApplication().getDefManager().getEditorPresentationDef(defId);
                } else if (defId.getPrefix() == EDefinitionIdPrefix.ADS_FORM_HANDLER_CLASS) {
                    titledDefinition = environment.getApplication().getDefManager().getFormDef(defId);
                } else if (defId.getPrefix() == EDefinitionIdPrefix.REPORT || defId.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
                    titledDefinition = environment.getApplication().getDefManager().getReportPresentationDef(defId);
                } else {
                    return "#" + defId.toString();
                }
                return titledDefinition.hasTitle() ? titledDefinition.getTitle() : titledDefinition.getName();
            }
        } catch (DefinitionError err) {
            return "#" + defId.toString();
        }
    }
    
    protected static String getPropertyTitle(final Model model, final Id propId){
        if (isPropertyDefined(model,propId)){
            try{
                final Property property = model.getProperty(propId);
                final String title = property.getTitle();                
                if (title==null || title.isEmpty()){
                    final RadPropertyDef propDef = property.getDefinition();
                    return propDef.hasTitle() ? propDef.getTitle(model.getEnvironment()) : propDef.getName();
                }else{
                    return title;
                }
            }catch(Exception exception){
                return null;
            }
        }else{
            return null;
        }
    }
    
    protected static String getPropertyTitle(final Model model, final RadPropertyDef prop){
        final String title = getPropertyTitle(model, prop.getId());
        if (title==null || title.isEmpty()){
            return prop.hasTitle() ? prop.getTitle(model.getEnvironment()) : prop.getName();
        }else{
            return title;
        }
    }

    public String getPropertyTitle() {
        final RadPropertyDef propertyDef;
        if (propertyOwner != null) {
            try {
                propertyDef = propertyOwner.getPropertyDefById(propertyId);
            } catch (DefinitionError err) {
                return "#" + propertyId.toString();
            }
        } else {
            propertyDef = findPropertyDef(environment, propertyOwnerId, propertyId);
        }

        if (propertyDef == null) {
            return "#" + propertyId.toString();
        } else if (propertyDef.hasTitle()) {
            return propertyDef.getTitle(environment);
        } else {
            return propertyDef.getName();
        }
    }
    
    private boolean isParameter(){
       return getPropertyId().getPrefix() == EDefinitionIdPrefix.PREDEFINED_FILTER_PARAMETER
                || getPropertyId().getPrefix() == EDefinitionIdPrefix.PARAMETER
                || getPropertyId().getPrefix() == EDefinitionIdPrefix.USER_FILTER_PARAMETER;
    }    

    protected String getOwnerTitle() {
        if (propertyOwner == null) {
            return getDefinitionTitle(environment, propertyOwnerId);
        } else if (propertyOwner instanceof TitledDefinition) {
            if (((TitledDefinition) propertyOwner).hasTitle()) {
                return ((TitledDefinition) propertyOwner).getTitle();
            } else {
                return ((TitledDefinition) propertyOwner).getName();
            }
        } else {
            return "#" + propertyOwnerId.toString();
        }
    }

    public final Id getPropertyId() {
        return propertyId;
    }
    
    public final Id getPropertyOwnerDefinitionId(){
        return propertyOwnerId;
    }
    
    public final boolean isSourceModel(final Model model){
        return model.getDefinition().isPropertyDefExistsById(propertyId);
    }

    public final boolean isOwnProperty() {
        return ownProperty;
    }
    
    protected final String getReasonAsString(final MessageProvider mp, final InvalidValueReason reason, final String propertyTitle){
        if (isOwnProperty()){
            final String reasonMessage;
            if (isParameter()){
                reasonMessage = reason.getMessage(mp, InvalidValueReason.EMessageType.ThisParameterValue);
            }
            else {
                reasonMessage = reason.getMessage(mp, InvalidValueReason.EMessageType.ThisPropertyValue);
            }                
            return String.format(reasonMessage, propertyTitle);
        }
        else{
            final String reasonMessage;
            if (isParameter()){
                reasonMessage = reason.getMessage(mp, InvalidValueReason.EMessageType.ParameterValue);
            }
            else {
                reasonMessage = reason.getMessage(mp, InvalidValueReason.EMessageType.PropertyValue);
            }
            return String.format(reasonMessage, propertyTitle, getOwnerTitle());
        }            
    }
    
    protected final String getMessageForMandatoryProperties(final MessageProvider mp, final List<String> propertyTitles){
        if (isOwnProperty()) {
            if (propertyTitles.size()>1){
                final StringBuilder messageBuilder = new StringBuilder();
                if (isParameter()) {
                    messageBuilder.append(mp.translate("ExplorerException", "The values of the following parameters must be defined: "));
                }
                else{
                    messageBuilder.append(mp.translate("ExplorerException", "The values of the following properties must be defined: "));
                }
                for (String propertyTitle: propertyTitles){
                    messageBuilder.append("\n");
                    messageBuilder.append(propertyTitle);
                }
                return messageBuilder.toString();                
            }
        }
        return getReasonAsString(mp, InvalidValueReason.NOT_DEFINED, propertyTitles.get(0));
    }
    
    private static boolean isPropertyDefined(final Model model, final Id propertyId){
        if (model.getDefinition().isPropertyDefExistsById(propertyId)){
            return true;
        }else{            
            for (Property property: model.getLocalProperties()){
                if (property.getId().equals(propertyId)){
                    return true;
                }
            }
            return false;
        }         
    }
    
    public void goToProblem(final Model model){
        if (isPropertyDefined(model, getPropertyId()) && model.getView() != null) {
            model.getView().setFocus();
            model.getProperty(getPropertyId()).setFocused();
        }
    }
}