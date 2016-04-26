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

package org.radixware.kernel.common.client.views;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.CommonFilterNotFoundException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.exceptions.NoInstantiatableClassesException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.BatchDeleteResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.types.EntityRestrictions;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.ModelRestrictions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;

import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public interface ISelector extends IView {

    public abstract class Actions implements Iterable<Action>{

        private final Action insertAction;
        private final Action insertAndEditAction;
        private final Action insertWithReplaceAction;
        private final Action autoinsertAction;
        private final Action runEditorDialogAction;
        private final Action createAction;
        private final Action deleteAction;
        private final Action deleteAllAction;
        private final Action updateAction;
        private final Action cancelChangesAction;
        private final Action rereadAction;
        private final Action copyAction;
        private final Action copyAllAction;
        private final Action duplicateAction;
        private final Action pasteAction;
        private final Action hideEditorAction;
        private final Action viewAuditLogAction;
        private final Action showFilterAndOrderToolbar;
        private final Action copySelectorPresId;
        private final Action copyEditorPresId;
        
        private final List<Action> allActions = new LinkedList<>();
                
        private final String insertActionError;
        private final String createActionError;
        private final String duplicateActionError;
        private final String deleteActionError;
        private final String deleteAllActionError;
        final String runEditorActionError;
        final String updateActionError;
        final String rereadActionError;
        
        private boolean firstRefresh = true;
        private int selectionFlag=0;        

        public Action getAutoInsertAction() {
            return autoinsertAction;
        }

        public Action getInsertWithReplaceAction() {
            return insertWithReplaceAction;
        }

        public Action getInsertAndEditAction() {
            return insertWithReplaceAction;
        }

        public Action getRereadAction() {
            return rereadAction;
        }

        public Action getCreateAction() {
            return createAction;
        }

        public Action getInsertAction() {
            return insertAction;
        }

        public Action getUpdateAction() {
            return updateAction;
        }

        public Action getRunEditorDialogAction() {
            return runEditorDialogAction;
        }

        public Action getCancelChangesAction() {
            return cancelChangesAction;
        }

        public Action getCopyAction() {
            return copyAction;
        }

        public Action getCopyAllAction() {
            return copyAllAction;
        }

        public Action getDeleteAction() {
            return deleteAction;
        }

        public Action getDeleteAllAction() {
            return deleteAllAction;
        }

        public Action getDuplicateAction() {
            return duplicateAction;
        }

        public Action getPasteAction() {
            return pasteAction;
        }

        public Action getViewAuditLogAction() {
            return viewAuditLogAction;
        }

        public Action getShowFilterAndOrderToolBarAction() {
            return showFilterAndOrderToolbar;
        }
        
        Action getCopySelectorPresIdAction(){
            return copySelectorPresId;
        }
        
        Action getCopyEditorPresIdAction(){
            return copyEditorPresId;
        }
        
        private final IClientEnvironment environment;
        private final SelectorController controller;

        protected abstract Action createAction(ClientIcon icon, String title);

        private Action createActionImpl(ClientIcon icon, String title) {
            final Action action = createAction(icon, title);
            allActions.add(action);
            return action;
        }

        public Actions(SelectorController controller) {
            this.controller = controller;
            this.environment = controller.getEnvironment();

            runEditorActionError = environment.getMessageProvider().translate("Selector", "Failed to run object editor");
            insertActionError = environment.getMessageProvider().translate("Selector", "Failed to open object in tree");
            createActionError = environment.getMessageProvider().translate("Selector", "Failed to create object");
            duplicateActionError = environment.getMessageProvider().translate("Selector", "Failed to create copy");
            deleteActionError = environment.getMessageProvider().translate("Selector", "Failed to delete object");
            deleteAllActionError = environment.getMessageProvider().translate("Selector", "Failed to delete group");
            updateActionError = environment.getMessageProvider().translate("Selector", "Failed to apply changes");
            rereadActionError = environment.getMessageProvider().translate("Selector", "Error on Receiving Data");

            insertAction = createActionImpl(ClientIcon.Selector.INSERT_INTO_TREE, environment.getMessageProvider().translate("Selector", "Open in Tree"));
            insertAction.setToolTip(environment.getMessageProvider().translate("Selector", "Open Object in Tree"));
            insertAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    insertEntityAction();
                }
            });

            insertAndEditAction = createActionImpl(ClientIcon.Selector.INSERT_INTO_TREE_AND_EDIT,
                    environment.getMessageProvider().translate("Selector", "Open in Tree and Edit"));
            insertAndEditAction.setToolTip(environment.getMessageProvider().translate("Selector", "Open Object in Tree"));
            insertAndEditAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    insertEntityAndEditAction();
                }
            });

            insertWithReplaceAction = createActionImpl(ClientIcon.Selector.REPLACE_IN_TREE,
                    environment.getMessageProvider().translate("Selector", "Replace"));
            insertWithReplaceAction.setToolTip(environment.getMessageProvider().translate("Selector", "Open Object in Tree"));
            insertWithReplaceAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    insertEntityWithReplaceAction();
                }
            });



            autoinsertAction = createActionImpl(ClientIcon.Selector.AUTOINSERT_INTO_TREE,
                    environment.getMessageProvider().translate("Selector", "Auto Open in Tree"));
            autoinsertAction.setToolTip(environment.getMessageProvider().translate("Selector", "Automatically Open Current Object in Tree"));
            autoinsertAction.setCheckable(true);
            autoinsertAction.setChecked(false);
            autoinsertAction.addActionToggleListener(new Action.ActionToggleListener() {

                @Override
                public void toggled(Action action, boolean isChecked) {
                    Actions.this.controller.setAutoInsertEnabled(isChecked);
                }
            });


            runEditorDialogAction = createActionImpl(ClientIcon.Selector.OPEN_IN_EDITOR,
                    environment.getMessageProvider().translate("Selector", "Edit"));
            runEditorDialogAction.setToolTip(environment.getMessageProvider().translate("Selector", "Edit"));
            runEditorDialogAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    runEditorDialogAction();
                }
            });

            runEditorDialogAction.setEnabled(false);

            rereadAction = createActionImpl(ClientIcon.CommonOperations.REREAD,
                    environment.getMessageProvider().translate("Selector", "Refresh"));
            rereadAction.setToolTip(environment.getMessageProvider().translate("Selector", "Refresh"));
            rereadAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    rereadAction();
                }
            });

            createAction = createActionImpl(ClientIcon.CommonOperations.CREATE,
                    environment.getMessageProvider().translate("Selector", "Create Object..."));
            createAction.setToolTip(environment.getMessageProvider().translate("Selector", "Create Object"));
            createAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    createAction();
                }
            });

            pasteAction = createActionImpl(ClientIcon.CommonOperations.PASTE,
                    environment.getMessageProvider().translate("Selector", "Paste..."));
            pasteAction.setToolTip(environment.getMessageProvider().translate("Selector", "Paste"));
            pasteAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    Actions.this.controller.paste();
                }
            });

            copyAction = createActionImpl(ClientIcon.CommonOperations.COPY,
                    environment.getMessageProvider().translate("Editor", "Copy"));
            copyAction.setToolTip(environment.getMessageProvider().translate("Editor", "Copy"));
            copyAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    copyAction();
                }
            });


            copyAllAction = createActionImpl(ClientIcon.Selector.COPY_ALL,
                    environment.getMessageProvider().translate("Selector", "Copy All"));
            copyAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Copy All"));
            copyAllAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    Actions.this.controller.copyAll();
                }
            });


            duplicateAction = createActionImpl(ClientIcon.Selector.CLONE,
                    environment.getMessageProvider().translate("Selector", "Duplicate"));
            duplicateAction.setToolTip(environment.getMessageProvider().translate("Selector", "Duplicate"));
            duplicateAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    duplicateAction();
                }
            });


            deleteAction = createActionImpl(ClientIcon.CommonOperations.DELETE,
                    environment.getMessageProvider().translate("Editor", "Delete"));
            deleteAction.setToolTip(environment.getMessageProvider().translate("Editor", "Delete Object"));
            deleteAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    deleteAction();
                }
            });

            deleteAllAction = createActionImpl(ClientIcon.CommonOperations.DELETE_ALL,
                    environment.getMessageProvider().translate("Selector", "Delete All"));
            deleteAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Delete All Objects"));
            deleteAllAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    deleteAllAction();
                }
            });

            updateAction = createActionImpl(ClientIcon.CommonOperations.SAVE,
                    environment.getMessageProvider().translate("Editor", "Apply"));
            updateAction.setEnabled(false);
            updateAction.setToolTip(environment.getMessageProvider().translate("Editor", "Apply Changes"));
            updateAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    updateAction();
                }
            });

            cancelChangesAction = createActionImpl(ClientIcon.CommonOperations.CANCEL,
                    environment.getMessageProvider().translate("Editor", "Cancel Changes"));
            cancelChangesAction.setEnabled(false);
            cancelChangesAction.setToolTip(environment.getMessageProvider().translate("Editor", "Cancel Changes"));
            cancelChangesAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    cancelChangesAction();
                }
            });

            hideEditorAction = createActionImpl(null, "");
            hideEditorAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    hideEditorAction();
                }
            });

            viewAuditLogAction = createActionImpl(ClientIcon.Selector.AUDIT,
                    environment.getMessageProvider().translate("Editor", "Audit Log"));
            viewAuditLogAction.setToolTip(environment.getMessageProvider().translate("Editor", "Audit Log"));
            viewAuditLogAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    viewAuditLogAction();
                }
            });

            showFilterAndOrderToolbar = createActionImpl(ClientIcon.Selector.FILTER_AND_SORTING,
                                            environment.getMessageProvider().translate("Selector", "Filter and Sorting Panel"));
            showFilterAndOrderToolbar.setToolTip(environment.getMessageProvider().translate("Selector", "Show Filter and Sorting Panel"));
            showFilterAndOrderToolbar.setCheckable(true);
            showFilterAndOrderToolbar.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    showFilterAndOrderToolbarAction();
                    updateShowFilterAndOrderToolbar();
                }
            });

            copySelectorPresId = createActionImpl(ClientIcon.Definitions.SELECTOR_ID, 
                                    environment.getMessageProvider().translate("Selector", "Copy Selector Presentation Id"));
            copySelectorPresId.setToolTip(environment.getMessageProvider().translate("Selector", "Copy Selector Presentation Identifier to Clipboard"));
            copySelectorPresId.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(final Action action) {
                    Actions.this.controller.copySelectorPresId();
                }
            });
            
            copyEditorPresId = createActionImpl(ClientIcon.Definitions.EDITOR_ID, 
                                environment.getMessageProvider().translate("Editor", "Copy Editor Presentation Id"));
            copyEditorPresId.setToolTip(environment.getMessageProvider().translate("Editor", "Copy Editor Presentation Identifier to Clipboard"));
            copyEditorPresId.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    Actions.this.controller.copyEditorPresId();
                }
            });
            
        }

        @Override
        public Iterator<Action> iterator() {
            return allActions.iterator();
        }                

        /**
         * Установить доступность действий в соответсвии с
         * ограничениями группы
         */        
        public void refresh() {
            final GroupModel group = controller.getGroupModel();
            final MessageProvider mp = environment.getMessageProvider();            
            if (group == null) {
                return;//not opened
            }
            final GroupRestrictions groupRestrictions = group.getRestrictions();
            //rereadAction.setEnabled(!controller.isDisabled());
            final boolean isCreateEnabled = !groupRestrictions.getIsCreateRestricted();
            createAction.setEnabled(isCreateEnabled);
            if (firstRefresh){
                if (group.getSelectorPresentationDef().getClassPresentation().hasObjectTitle()){
                    final String groupTitle =  group.getSelectorPresentationDef().getClassPresentation().getObjectTitle();
                    final String createActionTitle = String.format(mp.translate("Selector", "Create \"%s\"..."), groupTitle);
                    final String createActionToolTip = String.format(mp.translate("Selector", "Create \"%s\""), groupTitle);                
                    createAction.setText(createActionTitle);
                    createAction.setToolTip(ClientValueFormatter.capitalizeIfNecessary(environment, createActionToolTip));
                }
                else{
                    createAction.setText(mp.translate("Selector", "Create Object..."));
                    createAction.setToolTip(mp.translate("Selector", "Create Object"));
                }
            }
            if (isCreateEnabled
                    && environment.getClipboard().size() > 0
                    && environment.getClipboard().isCompatibleWith(group)) {
                pasteAction.setEnabled(true);
                if (environment.getClipboard().size() == 1) {
                    final String title = environment.getClipboard().iterator().next().getTitle();
                    final String toolTipFormat = mp.translate("Selector", "Paste Object '%1$s'...");
                    final String finalToolTip = String.format(toolTipFormat, title);
                    pasteAction.setToolTip(finalToolTip.length() > 100 ? environment.getMessageProvider().translate("Selector", "Paste Object...") : finalToolTip);
                } else {
                    pasteAction.setToolTip(environment.getMessageProvider().translate("Selector", "Paste Objects..."));
                }
            } else {
                pasteAction.setEnabled(false);
                pasteAction.setToolTip(environment.getMessageProvider().translate("Selector", "Paste"));
            }

            refreshEntityModifiedState();
            final EntityModel currentEntity = controller.getCurrentEntity();
            final EntityRestrictions currentEntityRestrictions;
            if (group.isEmpty() || currentEntity == null || !currentEntity.canOpenEntityView()) {
                insertAction.setEnabled(false);
                insertAndEditAction.setEnabled(false);
                insertWithReplaceAction.setEnabled(false);
                autoinsertAction.setEnabled(false);
                runEditorDialogAction.setEnabled(false);
                deleteAction.setEnabled(false);
                copyAction.setEnabled(false);
                copyEditorPresId.setEnabled(false);
                currentEntityRestrictions = null;
            } else {
                currentEntityRestrictions = currentEntity.getRestrictions();
                boolean insertionRestricted = 
                    groupRestrictions.getIsInsertIntoTreeRestricted() || !currentEntity.canInsertIntoTreeFromSelector();                
                final List<GroupModel> parentGroups = controller.parentModels.getParentGroups();
                for (int i = parentGroups.size() - 1; i >= 0 && !insertionRestricted; i--) {
                    insertionRestricted = parentGroups.get(i).getRestrictions().getIsInsertIntoTreeRestricted();
                }
                final boolean canInsert;
                if (insertionRestricted){
                    canInsert = false;
                }else{
                    final IExplorerItemView nearestExplorerItemView = group.findNearestExplorerItemView();
                    if (nearestExplorerItemView==null){
                        canInsert = false;
                    }else if (nearestExplorerItemView.isChoosenObject()){//RADIX-7253
                        canInsert = 
                            !currentEntity.getPid().equals(nearestExplorerItemView.getChoosenEntityInfo().pid);
                    }else{
                        canInsert = true;
                    }
                }
                insertAction.setEnabled(canInsert);
                insertAndEditAction.setEnabled(canInsert);
                if (canInsert) {
                    final Id tableId = group.getSelectorPresentationDef().getTableId();
                    List<IExplorerItemView> inserted = group.findNearestExplorerItemView().getChoosenEntities(tableId);
                    insertWithReplaceAction.setEnabled(!(inserted.isEmpty()
                            || inserted.get(0).getChoosenEntityInfo().pid.equals(currentEntity.getPid())
                            || controller.isAutoInsertEnabled()
                            || //Если есть промежуточные группы, то вставлять с заменой нельзя
                            !controller.parentModels.getParentGroups().isEmpty()));
                } else {
                    insertWithReplaceAction.setEnabled(false);
                }
                autoinsertAction.setEnabled(canInsert && 
                        //Если есть промежуточные группы, то автовставка запрещена
                        controller.parentModels.getParentGroups().isEmpty());
                runEditorDialogAction.setEnabled(!groupRestrictions.getIsRunEditorRestricted());
                deleteAction.setEnabled(!currentEntityRestrictions.getIsDeleteRestricted());
                if (!currentEntityRestrictions.getIsCopyRestricted()
                        && currentEntity.isExists()) {
                    copyAction.setEnabled(true);
                } else {
                    copyAction.setEnabled(false);
                }      
                refreshCopyAllActionState(group);
                copyEditorPresId.setEnabled(true);
            }
            //delete all operation always applies to root group
            refreshDeleteAllActionState(group);
            refreshDuplicateActionState(group,groupRestrictions,currentEntity,currentEntityRestrictions);
            final boolean filtered = controller.state == SelectorController.SelectorState.NORMAL && group.getCurrentFilter()!=null;            

            viewAuditLogAction.setVisible(group.isAuditEnabled());

            //Check if  some filter or order choosen, but toolbar was not shown
            final ISelectorMainWindow selectorMainWindow = controller.getSelectorMainWindow();
            if (selectorMainWindow.isAnyFilter()
                    && !selectorMainWindow.isFilterAndOrderToolbarVisible()
                    && !showFilterAndOrderToolbar.isChecked()) {
                //trigger this action
                showFilterAndOrderToolbar.trigger();
            } else {
                updateShowFilterAndOrderToolbar();
            }

            if (selectorMainWindow.someGroupSettingIsMandatory()) {
                if (!selectorMainWindow.isFilterAndOrderToolbarVisible()) {
                    if (!showFilterAndOrderToolbar.isChecked()) {
                        showFilterAndOrderToolbar.trigger();
                    } else {
                        selectorMainWindow.updateFilterAndOrderToolbarVisible(true);
                    }
                }
                showFilterAndOrderToolbar.setEnabled(false);
            } else {
                showFilterAndOrderToolbar.setEnabled(true);
            }
            refreshSelectionToolTips(group.getSelection(),filtered);
            firstRefresh = false;
        }
        
        public void refreshAfterChangeSelection(){
            final GroupModel group = controller.getGroupModel();            
            refreshCopyAllActionState(group);
            refreshDeleteAllActionState(group);            
            final boolean filtered = controller.state == SelectorController.SelectorState.NORMAL && group.getCurrentFilter()!=null;            
            refreshSelectionToolTips(group.getSelection(),filtered);
            final EntityModel currentEntity = controller.getCurrentEntity();
            final EntityRestrictions currentEntityRestrictions = currentEntity==null || currentEntity.getRestrictions().getIsViewRestricted() ? null : currentEntity.getRestrictions();            
            refreshDuplicateActionState(group, group.getRestrictions(), currentEntity, currentEntityRestrictions);
        }
        
        private void refreshSelectionToolTips(final EntityObjectsSelection selection, final boolean filtered){
            final int currentSelectionFlag;
            if (selection.isEmpty()){
                currentSelectionFlag=0;
            }else if (selection.isSingleObjectSelected()){
                currentSelectionFlag=1;
            }else{
                currentSelectionFlag=2;
            }
            if (currentSelectionFlag!=selectionFlag){
                if (currentSelectionFlag==0){
                    copyAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Copy All"));
                    refreshDeleteAllActionToolTip(filtered);                    
                }else if (currentSelectionFlag==1){
                    deleteAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Delete Selected Object"));
                    copyAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Copy Selected Object"));                    
                }else{
                    deleteAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Delete Selected Objects"));
                    copyAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Copy Selected Objects"));
                }
                selectionFlag = currentSelectionFlag;
            }
        }
        
        private void refreshDeleteAllActionToolTip(final boolean filtered){
            if (filtered){
                deleteAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Delete All Filtered Objects"));
            }else{
                deleteAllAction.setToolTip(environment.getMessageProvider().translate("Selector", "Delete All Objects"));
            }            
        }
        
        private void refreshCopyAllActionState(final GroupModel groupModel){            
            final GroupRestrictions groupRestrictions = groupModel.getRestrictions();
            if (groupRestrictions.getIsCopyRestricted() || controller.state!=SelectorController.SelectorState.NORMAL || groupModel.isEmpty()){
                copyAllAction.setEnabled(false);
            }else{
                final EntityObjectsSelection selection = groupModel.getSelection();
                if (selection.isEmpty()){
                    copyAllAction.setEnabled(!groupRestrictions.getIsMultipleCopyRestricted());
                }else{                    
                    if (selection.getSelectionMode()==ESelectionMode.INCLUSION || !groupModel.hasMoreRows()){
                        copyAllAction.setEnabled(true);
                    }else{
                        copyAllAction.setEnabled(!groupRestrictions.getIsMultipleCopyRestricted());
                    }
                }
            }
        }
        
        private void refreshDeleteAllActionState(final GroupModel groupModel){            
            final GroupRestrictions groupRestrictions = groupModel.getRestrictions();
            if (groupRestrictions.getIsDeleteRestricted() || controller.state!=SelectorController.SelectorState.NORMAL || groupModel.isEmpty()){
                deleteAllAction.setEnabled(false);
            }else{
                final EntityObjectsSelection selection = groupModel.getSelection();
                if (selection.isEmpty()){
                    deleteAllAction.setEnabled(!groupRestrictions.getIsDeleteAllRestricted());
                }else if (selection.isSingleObjectSelected()){
                    deleteAllAction.setEnabled(canDeleteSomeSelectedObject(groupModel));
                }else if (groupRestrictions.getIsMultipleDeleteRestricted()){
                    deleteAllAction.setEnabled(false);
                }else{
                    if (selection.getSelectionMode()==ESelectionMode.INCLUSION || !groupModel.hasMoreRows()){
                        deleteAllAction.setEnabled(canDeleteSomeSelectedObject(groupModel));
                    }else{
                        deleteAllAction.setEnabled(!groupRestrictions.getIsDeleteAllRestricted());
                    }
                }                
            }
        }
        
        private void refreshDuplicateActionState(final GroupModel group, final GroupRestrictions groupRestrictions, final EntityModel currentEntity, final EntityRestrictions currentEntityRestrictions) {
            if (group.getSelection().isSingleObjectSelected()){
                //duplicate single object                
                final EntityModel sourceEntity = controller.getSindleSelectedObject();
                if (sourceEntity==null){
                    duplicateAction.setEnabled(false);
                    duplicateAction.setToolTip(environment.getMessageProvider().translate("Selector", "Duplicate"));
                }else{
                    final ModelRestrictions srcEntityRestrictions = sourceEntity.getRestrictions();
                    final String toolTip = environment.getMessageProvider().translate("Selector", "Duplicate Selected Object \'%1$s\'...");
                    duplicateAction.setToolTip(String.format(toolTip, sourceEntity.getTitle()));
                    duplicateAction.setEnabled(!groupRestrictions.getIsCreateRestricted()
                                               && !srcEntityRestrictions.getIsCopyRestricted());                    
                }
            }else if (!group.getSelection().isEmpty() && !groupRestrictions.getIsMultipleCopyRestricted()){
                    final String toolTip = environment.getMessageProvider().translate("Selector", "Duplicate Selected Objects...");
                    duplicateAction.setToolTip(toolTip);
                    duplicateAction.setEnabled(!groupRestrictions.getIsCreateRestricted());
            }else if (currentEntity!=null && currentEntityRestrictions!=null){
                final String toolTip = environment.getMessageProvider().translate("Selector", "Duplicate Object \'%1$s\'...");
                duplicateAction.setToolTip(String.format(toolTip, currentEntity.getTitle()));
                duplicateAction.setEnabled(!groupRestrictions.getIsCreateRestricted()
                                           && !currentEntityRestrictions.getIsCopyRestricted());                
            }else{
                duplicateAction.setEnabled(false);
                duplicateAction.setToolTip(environment.getMessageProvider().translate("Selector", "Duplicate"));                
            }
        }
        
        private boolean canDeleteSomeSelectedObject(final GroupModel group){
            final EnumSet<GroupModelReader.EReadingFlags> readingFlags = 
                EnumSet.of(GroupModelReader.EReadingFlags.RESPECT_SELECTION,GroupModelReader.EReadingFlags.DONT_LOAD_MORE);
            for (EntityModel entityObject: new GroupModelReader(group, readingFlags)){
                if (!entityObject.getRestrictions().getIsDeleteRestricted()){
                    return true;
                }
            }
            return false;
        }
        
        protected void refreshEntityModifiedState() {
            final EntityModel currentEntity = controller.getCurrentEntity();
            if (currentEntity == null || currentEntity.getRestrictions().getIsUpdateRestricted()) {
                updateAction.setEnabled(false);
                cancelChangesAction.setEnabled(false);
            } else if (currentEntity != null) {
                setEntityModifiedState(currentEntity.isEdited());
            }
        }

        protected void setEntityModifiedState(final boolean modified) {
            final EntityModel currentEntity = controller.getCurrentEntity();
            if (currentEntity != null && !currentEntity.getRestrictions().getIsUpdateRestricted() && updateAction.isEnabled() != modified) {
                updateAction.setIcon(environment.getApplication().getImageManager().getIcon(modified ? ClientIcon.Editor.NEED_FOR_SAVE : ClientIcon.Editor.SAVE));
                cancelChangesAction.setEnabled(modified);
                updateAction.setEnabled(modified);
                controller.getSelectorListener().modifiedStateChanged(modified);
            }
        }

        private void insertEntityAction() {
            try {
                controller.getModel().finishEdit();
                controller.insertEntity();
            } catch (Exception ex) {
                controller.getModel().showException(insertActionError, ex);
            }
        }

        private void insertEntityWithReplaceAction() {
            try {
                controller.getModel().finishEdit();
                controller.insertEntityWithReplace();
            } catch (Exception ex) {
                controller.getModel().showException(insertActionError, ex);
            }
        }

        private void insertEntityAndEditAction() {
            try {
                controller.getModel().finishEdit();
                IExplorerItemView item = controller.insertEntityImpl(false, false, false);
                if (item != null) {
                    item.setCurrent();
                }
            } catch (Exception ex) {
                controller.getModel().showException(insertActionError, ex);
            }
        }

        private void runEditorDialogAction() {
            try {
                controller.getModel().finishEdit();
                controller.runEditorDialog();
            } catch (Exception ex) {
                controller.getModel().showException(runEditorActionError, ex);
            }
        }

        private void rereadAction() {
            try {
                controller.getModel().finishEdit();
                controller.reread();
            } catch (CommonFilterNotFoundException exception) {
                controller.getModel().showException(rereadActionError, exception);
                final FilterModel defaultFilter = controller.group.getFilters().getDefaultFilter();
                try {
                    if (defaultFilter == null || !defaultFilter.canApply() || !applyFilter(defaultFilter)) {
                        applyFilter(null);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            } catch (Exception ex) {
                controller.getModel().showException(rereadActionError, ex);
            }
        }

        private boolean applyFilter(final FilterModel filter) throws InterruptedException {
            try {
                controller.group.setFilter(filter);
                return true;
            } catch (PropertyIsMandatoryException | InvalidPropertyValueException exception) {
                return false;
            } catch (ServiceClientException exception) {
                controller.group.getEnvironment().getTracer().error(exception);
                return false;
            }
        }

        private void createAction() {
            try {
                controller.getModel().finishEdit();
                controller.create();
            } catch (NoInstantiatableClassesException ex) {
                controller.getEnvironment().getTracer().error(ex.getMessage()
                        + " (" + controller.getGroupModel().getSelectorPresentationDef().getDescription() + ")");
                environment.messageError(createActionError, ex.getMessage());
            } catch (Exception ex) {
                controller.getModel().showException(createActionError, ex);
            }

        }

        private void duplicateAction() {
            try {
                controller.getModel().finishEdit();
                 if (controller.group.getSelection().isEmpty()){
                    controller.duplicate();
                 }else{
                     controller.duplicateSelected();
                 }
            } catch (Exception ex) {
                controller.getModel().showException(duplicateActionError, ex);
            }
        }

        private void deleteAllAction() {
            try {
                controller.getModel().finishEdit();
                if (controller.group.getSelection().isEmpty()){
                    controller.deleteAll();
                }else{
                    controller.deleteSelected();
                }                 
            } catch (Exception ex) {
                controller.getModel().showException(deleteAllActionError, ex);
            }
        }

        private void deleteAction() {
            try {
                controller.getModel().finishEdit();
                if (controller.getCurrentEntity() != null
                        && controller.getCurrentEntity().delete(false)) {
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } catch (Exception ex) {
                controller.getModel().showException(deleteActionError, ex);
            }
        }

        private void updateAction() {
            try {
                controller.update();
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } catch (Exception ex) {
                controller.getModel().showException(updateActionError, ex);
            }
        }

        private void cancelChangesAction() {
            if (controller.getCurrentEntity() != null) {
                controller.getModel().finishEdit();
                if (!controller.isCurrentEntityModified() || confirmToCancelChanges()){
                    controller.getCurrentEntity().cancelChanges();
                    controller.cancelChanges();
                }
            }
        }
        
        private boolean confirmToCancelChanges(){
            final MessageProvider messageProvider = controller.group.getEnvironment().getMessageProvider();
            final String title = messageProvider.translate("Editor", "Confirm to Cancel Changes");
            final String message = messageProvider.translate("Editor", "Your changes will be lost. Do you want to continue?");
            return controller.group.getEnvironment().messageConfirmation(title, message);
        }        

        private void copyAction() {
            if (controller.getCurrentEntity() != null) {
                controller.getCurrentEntity().getEnvironment().getClipboard().push(controller.getCurrentEntity());
                refresh();
            }
        }

        private void viewAuditLogAction() {
            controller.viewAuditLog();
        }

        private void hideEditorAction() {
        }

        private void showFilterAndOrderToolbarAction() {
            if (!showFilterAndOrderToolbar.isChecked() && (controller.group.getCurrentFilter() != null || controller.isDisabled())) {
                try {                    
                    final RadSortingDef choosedSorting = controller.group.getCurrentSorting(), newSorting;
                    if (choosedSorting!=null && !controller.group.getSortings().isAcceptable(choosedSorting, null)){
                        newSorting = controller.group.getSortings().getDefaultSorting(null);
                    }else{
                        newSorting = choosedSorting;//keep current sorting
                    }                    
                    controller.group.applySettings(null, newSorting);
                } catch (InterruptedException e) {
                    return;
                } catch (Exception ex) {
                    controller.getModel().showException(updateActionError, ex);
                    return;
                }
            }
            controller.getSelectorMainWindow().updateFilterAndOrderToolbarVisible(showFilterAndOrderToolbar.isChecked());
        }

        private void updateShowFilterAndOrderToolbar() {
            if (showFilterAndOrderToolbar.isChecked()) {
                if (controller.group.getCurrentFilter() != null || controller.isDisabled()) {
                    //showFilterAndOrderToolbar.setToolTip(environment.getMessageProvider().translate("Selector", "Cancel Filtering"));
                    showFilterAndOrderToolbar.setToolTip(environment.getMessageProvider().translate("Selector", "Remove Filter and Hide Filter and Sorting Panel"));
                } else {
                    showFilterAndOrderToolbar.setToolTip(environment.getMessageProvider().translate("Selector", "Hide Filter and Sorting Panel"));
//                    showFilterAndOrderToolbar.setToolTip(ClientValueFormatter.capitalizeIfNecessary(environment, showFilterAndOrderToolbar.getText()));
                }
            } else {
                showFilterAndOrderToolbar.setToolTip(environment.getMessageProvider().translate("Selector", "Show Filter and Sorting Panel"));
//                showFilterAndOrderToolbar.setToolTip(ClientValueFormatter.capitalizeIfNecessary(environment, showFilterAndOrderToolbar.getText()));
            }
        }

        public void close() {
            for (Action action: allActions){
                action.close();
            }
            allActions.clear();
        }
    }

    public interface IEditorSpace {

        boolean isHidden();

        void setVisible(boolean isVisible);

        void setHidden(boolean hidden);
        
        void showException(Throwable exception);
        
        void hideException();
        
        boolean isExceptionShown();
    }

    public interface ISelectorMainWindow {

        public boolean isFilterAndOrderToolbarVisible();

        public boolean isAnyFilter();
        
        public void applyFilterAndOrderChanges();

        public void updateFilterAndOrderToolbarVisible(final boolean isVisible);

        public boolean someGroupSettingIsMandatory();

        public boolean setupInitialFilterAndSorting() throws InterruptedException;

        public void refreshFilterAndOrderToolbar();

        boolean isInitialFilterNeedToBeApplyed();

        void addToolBar(IToolBar toolBar);

        void addToolBarBreak();

        void removeToolBarBreak(IToolBar toolBar);

        void setUpdatesEnabled(boolean enabled);

        void clear();
    }

    public interface IToolBarsManager {

        public void close();

        public boolean isToolBarsHaveCustomPositions();

        public void correctToolBarsPosition();

        public void sheduleCorrectToolBarsPosition();
    }

    public abstract class SelectorListener {
        
        public void opened(final IWidget content){};

        public void entityRemoved(){};

        public void entityUpdated(){};

        public void entitiesCreated(final List<EntityModel> entities){};

        public void modifiedStateChanged(final boolean modified){};

        public void insertedIntoTree(final IExplorerItemView explorerItemView){};

        public void onDeleteAll(){};
                
        public void onDeleteSelected(final BatchDeleteResult deleteResult){};

        public void afterReread(final Pid pid){};
        
        public void onShowException(final Throwable exception){};        

        public void closed(){};
    }

    public void addSelectorListener(SelectorListener listener);

    public void removeSelectorListener(SelectorListener listener);

    public Actions getActions();

    public void refresh();

    public boolean isUpdatesEnabled();

    public boolean canChangeCurrentEntity(boolean force);          

    public EntityModel getCurrentEntity();

    public void setCurrentEntity(EntityModel entity);

    public void setCurrentFilter(final FilterModel filter, final boolean apply);

    public void entityRemoved(Pid pid);

    public void repaint();

    //public void reread() throws ServiceClientException;
    public void reread(Pid pid) throws ServiceClientException;

    public boolean disable();

    public boolean leaveCurrentEntity(boolean force);

    public void setToolBarHidden(boolean hidden);
    public void setEditorToolBarHidden(boolean hidden);
    public void setCommandBarHidden(boolean hidden);
    public void setEditorCommandBarHidden(boolean hidden);

    public void setSelectorWidget(ISelectorWidget widget);

    public ISelectorWidget getSelectorWidget();

    public GroupModel getGroupModel();

    public interface CurrentEntityHandler {

        public void onSetCurrentEntity(EntityModel entity);

        public void onLeaveCurrentEntity();
    }

    public void addCurrentEntityHandler(CurrentEntityHandler handler);

    public void removeCurrentEntityHandler(CurrentEntityHandler handler);

    public void setAutoInsertEnabled(final boolean enabled);

    public List<EntityModel> paste();

    public boolean isAutoInsertEnabled();

    public ExplorerItemView insertEntity();

    public ExplorerItemView insertEntityWithReplace();

    public EntityModel create() throws ServiceClientException;

    public EntityModel duplicate() throws ServiceClientException;

    public void runEditorDialog() throws ServiceClientException;
    
    public boolean applyChanges() throws ServiceClientException, ModelException;
    
    public void cancelChanges();

    public void blockRedraw();

    public void unblockRedraw();

    public boolean deleteAll() throws ServiceClientException;
    
    public BatchDeleteResult deleteSelected() throws ServiceClientException;
    
    public BatchCopyResult duplicateSelected();

    public void copyAll();
    
    public void showException(final Throwable exception);
    
    public void hideException();
}
