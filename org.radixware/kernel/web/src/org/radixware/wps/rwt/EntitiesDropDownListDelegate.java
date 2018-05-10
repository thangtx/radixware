/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.rwt;

import java.awt.Color;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IEntitySelectionController;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.wps.text.WpsTextOptions;


public class EntitiesDropDownListDelegate extends LazyDropDownListDelegate<Reference> {
    
    private static class EntityModelListItem extends DropDownListItem<Reference>{
        public EntityModelListItem(final EntityModel entityModel){
            super(entityModel.getTitle(), new Reference(entityModel));
        }
    }
    
    private static class ErrorListItem extends DropDownListItem<Reference>{
        public ErrorListItem(final String title){
            super(title, null);
            setTextOptions(WpsTextOptions.Factory.getOptions(null, ETextAlignment.CENTER, Color.red, null));
            setEnabled(false);
        }        
    }
    
    private GroupModel groupModel;
    private boolean wasException;
    
    public EntitiesDropDownListDelegate(){
        this(null);
    }
    
    public EntitiesDropDownListDelegate(final GroupModel group){
        super();
        groupModel = group;        
    }
    
    public final Reference getReference(final int index){
        if (index>=0){
            final List<DropDownListItem<Reference>> items = getItems();
            return index<items.size() ? items.get(index).getValue() : null;
        }else{
            return null;
        }
    }
    
    private IClientEnvironment getEnvironment(){
        if (groupModel==null){
            throw new IllegalStateException("Group model was not defined");
        }        
        return groupModel.getEnvironment();
    }

    @Override
    protected List<DropDownListItem<Reference>> loadMoreItems(final int oldCount) {
        final List<DropDownListItem<Reference>> newItems = new LinkedList<>();
        final List<EntityModel> newEntities = loadNewEntities(oldCount); 
        for (EntityModel entityModel: newEntities){
            newItems.add(new EntityModelListItem(entityModel));
        }
        if (wasException){
            final String errorTitle = getEnvironment().getMessageProvider().translate("Selector", "Error on Receiving Data");
            if (oldCount==0){
                for (int i=1; i<=9; i++){
                    newItems.add(new ErrorListItem(i==5 ? errorTitle : ""));
                }
            }else{
                newItems.add(new ErrorListItem(errorTitle));
            }
        }
        return newItems;
    }
    
    private List<EntityModel> loadNewEntities(final int oldCount){
        if (groupModel==null){
            throw new IllegalStateException("Group model was not defined");
        }
        int lastEntityIndex;
        if (oldCount==0){
            lastEntityIndex = -1;
        }else{
            lastEntityIndex = groupModel.findEntityByPid(getReference(oldCount-1).getPid());
        }
        final List<EntityModel> newEntities = new LinkedList<>();        
        final IEntitySelectionController selectionController = groupModel.getEntitySelectionController();        
        for (int i=lastEntityIndex+1; i<groupModel.getEntitiesCount() || (hasMoreItems() && newEntities.isEmpty()); i++){
            try{
                final EntityModel entityModel = groupModel.getEntity(i);
                if (entityModel!=null && selectionController!=null && selectionController.isEntityChoosable(entityModel)){
                    newEntities.add(entityModel);
                }
            }catch(ServiceClientException exception){
                //do not expect this exceptions here
                processServiceClientException(exception);
            }catch(BrokenEntityObjectException exception){
                //ignoring broken entity
            }catch(InterruptedException exception){
                break;
            }
        }
        
        return newEntities;        
    }
    
    private void processServiceClientException(final ServiceClientException exception) {
        wasException = true;
        final String message = 
            getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
        getEnvironment().getTracer().error(message, exception);        
    }

    @Override
    protected boolean hasMoreItems() {
        return !wasException && groupModel!=null && groupModel.hasMoreRows();
    }        
    
    @Override
    public final void reset(){
        if (groupModel!=null){
            groupModel.reset();
        }
        wasException = false;
        super.reset();
    }

    public GroupModel getGroupModel() {
        return groupModel;
    }

    public void setGroupModel(final GroupModel groupModel) {
        if (groupModel!=this.getGroupModel()){
            this.groupModel = groupModel;
            wasException = false;            
        }        
        super.reset();
    }    
}

