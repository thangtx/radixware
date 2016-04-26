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
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.types.InstantiatableClasses;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.ObjectList;
import org.radixware.schemas.eas.SelectRs;


class SelectResponseParser {
    
    private final IContext.SelectorRow rowContext;
    private final Id tableId;
    private final IClientEnvironment env;
    private final RadSelectorPresentationDef selectorPresentation;
    
    public SelectResponseParser(final GroupModel groupModel){
        rowContext = new IContext.SelectorRow(groupModel);
        rowContext.setPresentationChangedHandler(groupModel);
        selectorPresentation = groupModel.getSelectorPresentationDef();
        tableId = selectorPresentation.getTableId();
        env = groupModel.getEnvironment();
    }
    
    private RadSelectorPresentationDef getSelectorPresentationDef() {
        return selectorPresentation;
    }
    
    public GroupModelData parse(final SelectRs response) {
        final List<EntityModel> entityModels = new ArrayList<>();
        
        //ObjectList.Rows rows = response.getDataSet().getRows();
        if (response.getDataSet() != null && response.getDataSet().getRows() != null && response.getDataSet().getRows().getItemList() != null) {
            for (ObjectList.Rows.Item data : response.getDataSet().getRows().getItemList()) {
                if (data.isSetObject()){
                    final ObjectList.Rows.Item.Object objectInSelector = data.getObject();
                    final Pid entityPid = new Pid(tableId, objectInSelector.getPID());
                    final ESelectorRowStyle rowStyle = objectInSelector.getRowStyle();
                    final EntityModel entity;
                    try{
                        final RadEditorPresentationDef epd = env.getDefManager().getEditorPresentationDef(objectInSelector.getPresentation().getId());
                        entity = epd.createModel(rowContext);                        
                        final RawEntityModelData rawData = 
                            new RawEntityModelData(objectInSelector, 
                                                   objectInSelector.getDisabledActions() != null ? objectInSelector.getDisabledActions().getItemList() : null,
                                                   objectInSelector.getEnabledEditorPages(),
                                                   objectInSelector.getAccessibleExplorerItems(),
                                                   rowStyle);
                        entity.activate(rawData, false);
                    }
                    catch(Throwable exception){
                        env.getTracer().error(exception);
                        entityModels.add(new BrokenEntityModel(env, getSelectorPresentationDef(), entityPid, exception));
                        continue;
                    }
                    entityModels.add(entity);
                }
                else if (data.isSetException()){
                    final BrokenEntityModel brokenEntity = 
                        new BrokenEntityModel(env, getSelectorPresentationDef(), data.getException());                    
                    if (brokenEntity.getPid()==null){
                        final String message = env.getMessageProvider().translate("TraceMessage", "Broken entity object with unknown PID detected.\nException message: %1$s.\nException stack:\n%2$s");
                        env.getTracer().warning(String.format(message, brokenEntity.getExceptionMessage(), brokenEntity.getExceptionStack()));
                    }
                    else{
                        final String message = env.getMessageProvider().translate("TraceMessage", "Broken entity object with PID '%1$s' detected.\nException message: %2$s.\nException stack:\n%3$s");
                        env.getTracer().warning(String.format(message, brokenEntity.getPid().toString(), brokenEntity.getExceptionMessage(), brokenEntity.getExceptionStack()));
                    }
                    entityModels.add(brokenEntity);
                }
            }
        }
        
        final EnumSet<ERestriction> srvRestrictions = EnumSet.noneOf(ERestriction.class);
        final List<Id> srvDisabledCommands = new ArrayList<>();
        if (response.getDisabledActions() != null && response.getDisabledActions().getItemList() != null) {
            for (org.radixware.schemas.eas.Actions.Item item : response.getDisabledActions().getItemList()) {
                if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.CREATE) {
                    srvRestrictions.add(ERestriction.CREATE);
                } else if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.DELETE) {
                    srvRestrictions.add(ERestriction.DELETE);
                } else if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.DELETE_ALL) {
                    srvRestrictions.add(ERestriction.DELETE_ALL);
                } else if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.COMMAND) {
                    srvDisabledCommands.add(item.getId());
                }
            }
        }

        return new GroupModelData(entityModels, 
                                  response.getCommonFilters(), 
                                  response.getFilter() == null ? null : response.getFilter().getId(), 
                                  response.getSorting() == null ? null : response.getSorting().getId(), 
                                  srvRestrictions, 
                                  srvDisabledCommands, 
                                  response.getInstantiatableClasses()==null ? null : InstantiatableClasses.parseClasses(response.getInstantiatableClasses(), env),
                                  response.getMore());
    }
    
}
