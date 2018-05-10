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

package org.radixware.kernel.explorer.dialogs;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.ISelectEntitiesDialog;
import org.radixware.kernel.common.client.widgets.selector.IMultiSelectionWidget;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.ServiceClientException;

import org.radixware.kernel.explorer.views.selector.Selector;

public final class SelectEntitiesDialog extends SelectEntityDialog implements ISelectEntitiesDialog {
    
    private boolean isMultiSelectionSupported;
    private boolean needToRestoreSelectAllRestriction;
    private boolean isSelectedEntityCanBeChoosen;
    private boolean cleanClicked;
    private final List<EntityModel> selection = new LinkedList<>();
    private final GroupModel.SelectionListener selectionListener = new GroupModel.SelectionListener() {

        @Override
        public void afterSelectionChanged(final EntityObjectsSelection selection) {
            SelectEntitiesDialog.this.refreshUi();
            SelectEntitiesDialog.this.afterChangeSelection(selection);
        }

        @Override
        public boolean beforeChangeSelection(final EntityObjectsSelection oldSelection, final EntityObjectsSelection newSelection) {
            return SelectEntitiesDialog.this.beforeChangeSelection(newSelection);
        }
    };     
    
    public SelectEntitiesDialog(final GroupModel groupModel, final boolean canBeNull){
        super(groupModel,canBeNull);
    }        
    
    private boolean beforeChangeSelection(final EntityObjectsSelection newSelection){       
        final List<EntityModel> selectedObjects = new LinkedList<>();
        switch(newSelection.getSelectionMode()){
            case EXCLUSION:
            {
                final GroupModel group = getGroupModel();
                if (group.hasMoreRows()){
                    return false;
                }else{
                    final GroupModelReader reader = new GroupModelReader(group,EnumSet.of(GroupModelReader.EReadingFlags.DONT_LOAD_MORE));
                    for (EntityModel entity: reader){
                        if (newSelection.isObjectSelected(entity) && !isEntityCanBeChoosed(entity)){
                            return false;
                        }else{
                            selectedObjects.add(entity);
                        }
                    }
                    selection.clear();
                    selection.addAll(selectedObjects);
                    return true;
                }             
            }
            case INCLUSION:
            {
                final GroupModel group = getGroupModel();
                for (Pid pid: newSelection.getSelectedObjects()){
                    final int row = group.findEntityByPid(pid);
                    if (row>=0){
                        final EntityModel entity;
                        try{
                            entity = group.getEntity(row);
                        }catch(ServiceClientException | InterruptedException | BrokenEntityObjectException excepion){
                            return false;
                        }
                        if (!isEntityCanBeChoosed(entity)){
                            return false;
                        }
                        selectedObjects.add(entity);
                    }
                }       
                selection.clear();
                selection.addAll(selectedObjects);                
                return true;                
            }
            default:
            {
                selection.clear();
                return true;
            }
        }
    }
    
    private void afterChangeSelection(final EntityObjectsSelection newSelection){
        final List<EntityModel> selectedObjects = new LinkedList<>();
        selection.clear();
        switch(newSelection.getSelectionMode()){
            case EXCLUSION:
            {
                final GroupModel group = getGroupModel();
                if (group.hasMoreRows()){
                    return;
                }else{
                    final GroupModelReader reader = new GroupModelReader(group,EnumSet.of(GroupModelReader.EReadingFlags.DONT_LOAD_MORE));
                    for (EntityModel entity: reader){
                        if (newSelection.isObjectSelected(entity) && isEntityCanBeChoosed(entity)){
                            selectedObjects.add(entity);
                        }
                    }
                    selection.addAll(selectedObjects);
                }
                break;
            }
            case INCLUSION:
            {
                final GroupModel group = getGroupModel();
                for (Pid pid: newSelection.getSelectedObjects()){
                    final int row = group.findEntityByPid(pid);
                    if (row>=0){
                        final EntityModel entity;
                        try{
                            entity = group.getEntity(row);
                        }catch(ServiceClientException | InterruptedException | BrokenEntityObjectException excepion){
                            continue;
                        }
                        if (!isEntityCanBeChoosed(entity)){
                            continue;
                        }
                        selectedObjects.add(entity);
                    }
                }
                selection.addAll(selectedObjects);
                break;
            }
        }
    }
    
    @Override
    public boolean clearButtonWasClicked(){
        return cleanClicked;
    }    

    @Override
    public List<EntityModel> getSelectedEntities() {
        if (selection.isEmpty() && isSelectedEntityCanBeChoosen){
            selection.add(getSelectedEntity());
        }
        return Collections.unmodifiableList(selection);
    }        
    
    private GroupModel getGroupModel(){
        return getSelector().getGroupModel();
    }        
    
    private void addSelectAllRestriction(final GroupModel group){
        if (!group.getRestrictions().getIsSelectAllRestricted()){
            needToRestoreSelectAllRestriction = true;
            group.getRestrictions().setSelectAllRestricted(true);
        }else{
            needToRestoreSelectAllRestriction = false;
        }
    }    
    
    private void removeSelectAllRestriction(final GroupModel group){
        if (group!=null && needToRestoreSelectAllRestriction && 
            group.getRestrictions().canBeAllowed(ERestriction.SELECT_ALL)){
            group.getRestrictions().setSelectAllRestricted(false);
            needToRestoreSelectAllRestriction = false;
        }        
    }    

    @Override
    protected void refreshUi() {
        if (isMultiSelectionSupported){
            final EntityObjectsSelection selection = getGroupModel().getSelection().getNormalized();
            final String groupModelTitle = getSelector().getModel().getWindowTitle();
            final int selectedObjects = selection.getSelectedObjects().size();
            final String titleTemplate = 
                getEnvironment().getMessageProvider().translate("SelectEntityDialog", "%1$s. Number of Selected Objects: %2$s");
            setWindowTitle(String.format(titleTemplate,groupModelTitle,String.valueOf(selectedObjects)));            
        }
        super.refreshUi();
    }

    @Override
    protected boolean isOkButtonEnabled() {
        return super.isOkButtonEnabled() || !selection.isEmpty();
    }        

    @Override
    protected void beforeOpenSelector(final GroupModel group) {
        addEditorRestriction(group);
    }

    @Override
    protected void afterOpenSelector(final Selector selector, final GroupModel group) {
        isMultiSelectionSupported = selector.getSelectorWidget() instanceof IMultiSelectionWidget
                                    && !group.getRestrictions().getIsMultipleSelectionRestricted();
        if (isMultiSelectionSupported){
            addSelectAllRestriction(group);
            group.addSelectionListener(selectionListener);
            ((IMultiSelectionWidget)selector.getSelectorWidget()).setMultiSelectionEnabled(true);
        }else{
            addMultiSelectRestriction(group);
        }     
        super.afterOpenSelector(selector,group);
    }

    @Override
    protected void afterCloseSelector(final GroupModel group) {
        if (isMultiSelectionSupported){
            group.removeSelectionListener(selectionListener);
            removeSelectAllRestriction(group);            
        }
        final EntityModel entityModel = getSelectedEntity();
        isSelectedEntityCanBeChoosen = 
            entityModel!=null && group.getEntitySelectionController().isEntityChoosable(entityModel);
        super.afterCloseSelector(group);        
    }

    @Override
    protected void onEntityActivated(final EntityModel entity) {        
        if (!isMultiSelectionSupported){
            super.onEntityActivated(entity);
        }
    }

    @Override
    protected void onClearClicked() {
        cleanClicked = true;
        super.onClearClicked();
    }

    @Override
    protected void onCtrlEnter() {
        if (!isMultiSelectionSupported){
            super.onCtrlEnter();
        }
    }                    
}