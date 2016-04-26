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

package org.radixware.kernel.common.client.meta;

import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;


public final class RadUserSorting extends RadSortingDef{
    
    public final static class Factory {

        private Factory() {
        }

        public static RadUserSorting newInstance(IClientEnvironment environment, final Id classId, final String name) {
            return new RadUserSorting(environment,
                    Id.Factory.newInstance(EDefinitionIdPrefix.SORTING),
                    classId,
                    name,
                    null//columns
                    );
        }

        public static RadUserSorting loadFromString(IClientEnvironment environment, final Id classId, final String xmlAsString, final boolean keepId) throws XmlException {
            final org.radixware.schemas.groupsettings.SortingDocument document =
                    org.radixware.schemas.groupsettings.SortingDocument.Factory.parse(xmlAsString);
            final org.radixware.schemas.groupsettings.CustomSorting sorting = document.getSorting();                    
            final List<SortingItem> columns = new LinkedList<>();
            if (sorting.getOrderByList()!=null){
                for (org.radixware.schemas.groupsettings.CustomSorting.OrderBy orderBy: sorting.getOrderByList()){
                    columns.add(new RadSortingDef.SortingItem(orderBy.getPropId(), orderBy.getDesc()));
                }
            }
            return new RadUserSorting(environment, 
                                      keepId ? sorting.getId() : Id.Factory.newInstance(EDefinitionIdPrefix.SORTING),                     
                                      classId,
                                      sorting.getName(), 
                                      columns.toArray(new RadSortingDef.SortingItem[0])
                                    );
        }
    }
    
    private boolean wasModified;
    private String title;    
    private final Icon icon;
    private boolean isValid;

    
    private RadUserSorting(
            final IClientEnvironment environment,
            final Id id,
            final Id classId,
            final String name,
            final SortingItem[] columns) {
        super(id, name, classId, null, columns);
        icon = environment.getApplication().getImageManager().getIcon(ClientIcon.Definitions.USER_SORTING);
        title = name;
        isValid = validateColumns(environment);
    }
    
    private boolean validateColumns(final IClientEnvironment environment){
        if (getSortingColumns().isEmpty()){
            return false;
        }else{            
            final RadClassPresentationDef classPresentation;
            try{
                classPresentation = environment.getDefManager().getClassPresentationDef(getOwnerClassId());
            }catch(DefinitionError exception){                
                final String message = 
                    environment.getMessageProvider().translate("TraceMessage","Failed to load user sorting %1s: entity class #%2s was not found");
                environment.getTracer().put(EEventSeverity.DEBUG, String.format(message, getName(), String.valueOf(getOwnerClassId())), EEventSource.CLIENT_DEF_MANAGER);
                return false;
            }
            for (SortingItem column: getSortingColumns()){
                if (column.propId==null || !classPresentation.isPropertyDefExistsById(column.propId)){
                    final String message = 
                        environment.getMessageProvider().translate("TraceMessage","Failed to load user sorting %1s: property #%2s was not found in %3s");
                    environment.getTracer().put(EEventSeverity.DEBUG, String.format(message, getName(), String.valueOf(column.propId), classPresentation.getDescription()), EEventSource.CLIENT_DEF_MANAGER);
                    return false;
                }else if (!classPresentation.getPropertyDefById(column.propId).canBeUsedInSorting()){                    
                    final String message = 
                        environment.getMessageProvider().translate("TraceMessage","Failed to load user sorting %1s: property %2s cannot be used in sorting");
                    environment.getTracer().put(EEventSeverity.DEBUG, String.format(message, getName(), classPresentation.getPropertyDefById(column.propId).toString()), EEventSource.CLIENT_DEF_MANAGER);
                    return false;
                }
            }
            return true;
        }        
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public boolean isUserDefined() {
        return true;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void setName(final String name) {
        title = name;
        wasModified = true;
    }
    
    public void setSortingColumns(IClientEnvironment env,final List<RadSortingDef.SortingItem> columns){
        orderBy.clear();
        orderBy.addAll(columns);
        isValid = validateColumns(env);
        wasModified = true;
    }

    @Override
    public boolean wasModified() {
        return wasModified;
    }

    @Override
    protected String getTitle(final Id definitionId, final Id titleId) {
        return title;
    }

    @Override
    public String getName() {
        return title;
    }        

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean hasTitle() {
        return title!=null && !title.isEmpty();
    }            
    
    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "custom sorting %s");
        return String.format(desc, super.getDescription());
    }
        
    public String saveToString() {
        final org.radixware.schemas.groupsettings.SortingDocument document =
                org.radixware.schemas.groupsettings.SortingDocument.Factory.newInstance();
        final org.radixware.schemas.groupsettings.CustomSorting sorting = document.addNewSorting();
        sorting.setId(getId());
        sorting.setName(title);
        sorting.setDefinitionType(EDefType.SORTING);
        org.radixware.schemas.groupsettings.CustomSorting.OrderBy order;
        for (SortingItem column: orderBy){
            order = sorting.addNewOrderBy();
            order.setPropId(column.propId);
            order.setDesc(column.sortDesc);
        }
        return document.xmlText();
    }
    
    public final void switchToFixedState(){
        wasModified = false;
    }
}
