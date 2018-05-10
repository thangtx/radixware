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

package org.radixware.kernel.common.client.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.types.Id;

/**
 * Класс-обертка над ServiceCallFault для случая
 * когда getFaultString().equals(ExceptionEnum.UNIQUE_CONSTRAINT_VIOLATION.toString())
 * Содержит человеко-читаемое сообщение об ошибке.
 * Позволяет определить относится ли данное исключение к заданной модели сущности.
 */
public class UniqueConstraintViolationError extends ServiceCallFault {

    private static final long serialVersionUID = -6022555731607131303L;

    private final Id classId;
    private final List<Id> propertyIds = new ArrayList<>();
    private final Map<Id,Set<Id>> parentRefPropIds = new HashMap<>();
    private String message;
    private final IClientEnvironment environment;

    public UniqueConstraintViolationError(IClientEnvironment environment, final ServiceCallFault source) {
        super(source.getFaultCode(), source.getFaultString(), source.getDetail());
        this.environment = environment;
        final StringTokenizer lines = new StringTokenizer(source.getMessage(), "\n");
        final Id id = Id.Factory.loadFrom(lines.nextToken());
        if (id.getPrefix()==EDefinitionIdPrefix.DDS_TABLE){
            classId = Id.Factory.changePrefix(id, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        }else{                    
            classId = id;
        }
        while (lines.hasMoreTokens()) {
            final String[] ids = lines.nextToken().split(" ");
            if (ids.length>0){
                final Id propertyId = Id.Factory.loadFrom(ids[0]);
                propertyIds.add(propertyId);
                if (ids.length>1){
                    final Set<Id> propertyRefIds = new HashSet<>();
                    for (int i=1; i<ids.length; i++){
                        propertyRefIds.add(Id.Factory.loadFrom(ids[i]));
                    }
                    parentRefPropIds.put(propertyId,propertyRefIds);
                }else{
                    parentRefPropIds.put(propertyId, Collections.<Id>emptySet());                   
                }
            }
        }
    }

    @Override
    public String getMessage() {
        if (message == null) {
            message = getContextMessage((EntityModel)null);
        }
        return message;
    }
    
    public String getMessage(final EntityModel contextEntityModel) {
        return getContextMessage(contextEntityModel);
    }    
    
    public String getMessage(final GroupModel groupModel){
        return getContextMessage(groupModel.getSelectorPresentationDef().getClassPresentation());
    }
    
    private String getContextMessage(final EntityModel contextEntityModel){
        final boolean isInContext = contextEntityModel!=null && inContextOf(contextEntityModel) && contextEntityModel.getView()!=null;
        final String translatedMessage = environment.getMessageProvider().translate("ExplorerError", "Unique constraint violation for objects '%s' by key: %s");
        final RadClassPresentationDef classDef;
        try {
            classDef = environment.getDefManager().getClassPresentationDef(classId);
        } catch (DefinitionError err) {
            final StringBuilder propertyIdsAsStr = new StringBuilder();
            for (int i = 0; i < propertyIds.size(); i++) {
                if (i > 0) {
                    propertyIdsAsStr.append(", ");
                }
                propertyIdsAsStr.append(propertyIds.get(i).toString());
            }
            return String.format(translatedMessage, classId.toString(), propertyIdsAsStr.toString());
        }
        final String tableTitle;
        if (classDef.hasGroupTitle()) {
            tableTitle = classDef.getGroupTitle();
        } else if (classDef.hasObjectTitle()) {
            tableTitle = classDef.getObjectTitle();
        } else {
            tableTitle = classDef.getName();
        }
        final StringBuilder propertyTitles = new StringBuilder();
        final List<Id> printedPropertyRefId = new LinkedList<>();
        Id propertyId;
        for (int i = 0; i < propertyIds.size(); i++) {
            if (i > 0) {
                propertyTitles.append(", ");
            }
            propertyId = propertyIds.get(i);
            if (isInContext){
                Property property = findVisibleProperty(contextEntityModel, propertyId);
                if (property==null){
                    final Set<Id> parentRefIds = parentRefPropIds.get(propertyId);
                    for (Id parentRefId: parentRefIds){
                        property = findVisibleProperty(contextEntityModel, parentRefId);
                        if (property!=null){
                            break;
                        }
                    }
                    if (property!=null){
                        if (!printedPropertyRefId.contains(property.getId())){
                            printedPropertyRefId.add(property.getId());
                            propertyTitles.append(property.getTitle());
                        }
                        continue;                        
                    }
                }else{
                    propertyTitles.append(property.getTitle());
                    continue;                    
                }
            }
            if (!printProperty(classDef, propertyId, propertyTitles, environment)){
                final Set<Id> parentRefIds = parentRefPropIds.get(propertyId);
                for (Id parentRefId: parentRefIds){
                    if (printedPropertyRefId.contains(parentRefId)){
                        break;
                    }
                    if (printProperty(classDef, parentRefId, propertyTitles, environment)){
                        printedPropertyRefId.add(parentRefId);
                        propertyId = null;
                        break;
                    }
                }
                if (propertyId!=null){
                    propertyTitles.append(propertyId.toString());
                }
            }            
        }
        return String.format(translatedMessage, tableTitle, propertyTitles.toString());        
    }
    
    private String getContextMessage(final RadClassPresentationDef classDef){
        final boolean isInContext = classDef!=null && inContextOf(classDef);
        final String translatedMessage = environment.getMessageProvider().translate("ExplorerError", "Unique constraint violation for objects '%s' by key: %s");
        final RadClassPresentationDef contextClassDef;
        try {
            contextClassDef = environment.getDefManager().getClassPresentationDef(classId);
        } catch (DefinitionError err) {
            final StringBuilder propertyIdsAsStr = new StringBuilder();
            for (int i = 0; i < propertyIds.size(); i++) {
                if (i > 0) {
                    propertyIdsAsStr.append(", ");
                }
                propertyIdsAsStr.append(propertyIds.get(i).toString());
            }
            return String.format(translatedMessage, classId.toString(), propertyIdsAsStr.toString());
        }
        final String tableTitle;
        if (classDef.hasGroupTitle()) {
            tableTitle =classDef.getGroupTitle();
        } else if (classDef.hasObjectTitle()) {
            tableTitle = classDef.getObjectTitle();
        } else {
            tableTitle = classDef.getName();
        }
        final StringBuilder propertyTitles = new StringBuilder();
        final List<Id> printedPropertyRefId = new LinkedList<>();
        Id propertyId;
        for (int i = 0; i < propertyIds.size(); i++) {
            if (i > 0) {
                propertyTitles.append(", ");
            }
            propertyId = propertyIds.get(i);
            if (isInContext){
                RadPropertyDef propertyDef = 
                    classDef.isPropertyDefExistsById(propertyId) ? classDef.getPropertyDefById(propertyId) : null;
                if (propertyDef==null){
                    final Set<Id> parentRefIds = parentRefPropIds.get(propertyId);
                    for (Id parentRefId: parentRefIds){
                        propertyDef = 
                            classDef.isPropertyDefExistsById(propertyId) ? classDef.getPropertyDefById(parentRefId) : null;
                        if (propertyDef!=null){
                            break;
                        }
                    }
                    if (propertyDef!=null){
                        if (!printedPropertyRefId.contains(propertyDef.getId())){
                            printedPropertyRefId.add(propertyDef.getId());
                            propertyTitles.append(propertyDef.getTitle(environment));
                        }
                        continue;                        
                    }
                }else{
                    propertyTitles.append(propertyDef.getTitle(environment));
                    continue;                    
                }
            }
            if (!printProperty(contextClassDef, propertyId, propertyTitles, environment)){
                final Set<Id> parentRefIds = parentRefPropIds.get(propertyId);
                for (Id parentRefId: parentRefIds){
                    if (printedPropertyRefId.contains(parentRefId)){
                        break;
                    }
                    if (printProperty(contextClassDef, parentRefId, propertyTitles, environment)){
                        printedPropertyRefId.add(parentRefId);
                        propertyId = null;
                        break;
                    }
                }
                if (propertyId!=null){
                    propertyTitles.append(propertyId.toString());
                }
            }            
        }
        return String.format(translatedMessage, tableTitle, propertyTitles.toString());        
    }    
    
    private static boolean printProperty(final RadClassPresentationDef classDef, 
                                                          final Id propertyId, 
                                                          final StringBuilder writeTo,
                                                          final IClientEnvironment environment){
        if (classDef.isPropertyDefExistsById(propertyId)) {
            final  RadPropertyDef property = classDef.getPropertyDefById(propertyId);
            if (property.hasTitle()) {
                writeTo.append(property.getTitle(environment));
                return true;
            } else if (property.getName() != null && !property.getName().isEmpty()) {
                writeTo.append(property.getName());
                return true;
            }       
        }
        return false;
    }

    private static Property findVisibleProperty(final EntityModel entityModel, final Id propertyId){
        for (Property property: entityModel.getActiveProperties()){
            if (property.getId().equals(propertyId)){
                if (property.isActivated() && property.isVisible() 
                    && (property.hasSubscriber() || isPropertyOnEditorPage(entityModel.getEditorPresentationDef(), propertyId))){
                    return property;
                }
                break;
            }
        }
        return null;
    }
    
    private static boolean isPropertyOnEditorPage(final RadEditorPresentationDef presentation, final Id propertyId){
        final Stack<RadEditorPageDef> pagesStack = new Stack<>();
        {
            final RadEditorPages pages = presentation.getEditorPages();
            for (int i = pages.getTopLevelPages().size() - 1; i >= 0; i--) {
                pagesStack.push(pages.getTopLevelPages().get(i));
            }
        }
        RadEditorPageDef page;
        List<RadEditorPageDef> childPages;
        while (!pagesStack.isEmpty()) {
            page = pagesStack.pop();
            if (page instanceof RadStandardEditorPageDef) {
                final RadStandardEditorPageDef.PropertiesGroup propGroup = ((RadStandardEditorPageDef) page).getRootPropertiesGroup();
                if (findPropertyInGroup(presentation, ((RadStandardEditorPageDef) page), propGroup, propertyId)){
                    return true;
                }
            }
            childPages = page.getSubPages();
            for (int i = childPages.size() - 1; i >= 0; i--) {
                pagesStack.push(childPages.get(i));
            }            
        }
        return false;
    }
    
    private static boolean findPropertyInGroup(final RadEditorPresentationDef presentation, final RadStandardEditorPageDef pageDef, final RadStandardEditorPageDef.PropertiesGroup propGroup, final Id propertyId) {
        final List<RadStandardEditorPageDef.PageItem> items = propGroup.getPageItems();
        Id itemId;
        for (RadStandardEditorPageDef.PageItem item : items) {
            itemId = item.getItemId();
            if (itemId.getPrefix() == EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP) {
                if (findPropertyInGroup(presentation,pageDef, pageDef.getPropertiesGroup(itemId), propertyId)){
                    return true;
                }
            } else if (presentation.isPropertyDefExistsById(itemId)) {
                return true;
            }
        }
        return false;
    }
    

    public boolean inContextOf(final EntityModel entity) {
        return inContextOf(entity.getClassPresentationDef());
    }
    
    public boolean inContextOf(final GroupModel groupModel){
        return inContextOf(groupModel.getSelectorPresentationDef().getClassPresentation());
    }
    
    private boolean inContextOf(final RadClassPresentationDef classDef){
        if (classDef.getId().equals(classId)){
            return true;
        }else{            
            final RadClassPresentationDef contextClassDef;
            try{
                contextClassDef = 
                    environment.getDefManager().getClassPresentationDef(classId);
            }catch(DefinitionError error){
                return false;
            }
            return classDef.isDerivedFrom(contextClassDef);
        }
    }
    

    public List<Id> findVisiblePropertyIds(final EntityModel contextEntityModel) {
        if (!inContextOf(contextEntityModel)){
            return Collections.emptyList();
        }
        final List<Id> result = new LinkedList<>();
        Id propertyId;
        for (int i = 0; i < propertyIds.size(); i++) {
            propertyId = propertyIds.get(i);
            if (findVisibleProperty(contextEntityModel, propertyId)==null){
                final Set<Id> parentRefIds = parentRefPropIds.get(propertyId);
                for (Id parentRefId: parentRefIds){
                    if (findVisibleProperty(contextEntityModel, parentRefId)!=null){
                        result.add(parentRefId);
                        break;
                    }
                }
            }else{
                result.add(propertyId);
            }
        }
        return result;
    }
}
