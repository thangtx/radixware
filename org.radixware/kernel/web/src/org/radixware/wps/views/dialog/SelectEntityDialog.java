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

 package org.radixware.wps.views.dialog;

import org.radixware.kernel.common.client.dialogs.IMessageBox.StandardButton;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IModelFinder;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.ISelectEntityDialog;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.views.selector.RwtSelector;


public class SelectEntityDialog extends Dialog implements ISelectEntityDialog {

    public EntityModel selectedEntity = null;
    private boolean needToRestoreEditorRestriction;
    private GroupModel groupModel;
    private final RwtSelector selector;    
    private final String selectAction;
    private final String cancelAction;
    private final String cleanAction;
    private boolean selectorIsOpened;
    private final ISelector.CurrentEntityHandler currentEntityHandler = new ISelector.CurrentEntityHandler() {

        @Override
        public void onSetCurrentEntity(final EntityModel entity) {
            SelectEntityDialog.this.onEntitySelected(entity);
        }

        @Override
        public void onLeaveCurrentEntity() {
            SelectEntityDialog.this.onEntitySelected(null);
        }
    };
    
    private IView findNearestView(final GroupModel model){
        final Model nearestModelWithView = model.findOwner(new IModelFinder() {
            @Override
            public boolean isTarget(final Model model) {
                return model.getView()!=null;
            }
        });
        return nearestModelWithView==null ? null : nearestModelWithView.getView();
    }

    public SelectEntityDialog(final IDialogDisplayer displayer, final GroupModel groupModel, final boolean canBeNull){
        super(displayer, ClientValueFormatter.capitalizeIfNecessary(groupModel.getEnvironment(), groupModel.getTitle()));
        final RadSelectorPresentationDef presentation = groupModel.getSelectorPresentationDef();
        setWidth(presentation.getDefaultDialogWidth()>0 ? presentation.getDefaultDialogWidth() : 400);
        setHeight(presentation.getDefaultDialogHeight()>0 ? presentation.getDefaultDialogHeight() : 300);
        selectAction = getEnvironment().getMessageProvider().translate("SelectEntityDialog", "Select");
        cleanAction = getEnvironment().getMessageProvider().translate("SelectEntityDialog", "Clear");
        cancelAction = StandardButton.getTitle(EDialogButtonType.CANCEL, getEnvironment());
        html.setAttr("dlgId",
                "selectEntityDialog");
        this.groupModel = groupModel;
        selector = (RwtSelector) groupModel.createView();
        add(selector);
        selector.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        selector.setObjectName("groupView");
        initButtonBox(canBeNull);
    }
    
    public void openSelector() throws InterruptedException{
        final IView nearestView = findNearestView(groupModel);
        if (nearestView!=null){
            selector.getRestrictions().add(nearestView.getRestrictions());
        }        
        if (groupModel.getRestrictions().getIsEditorRestricted()){
            needToRestoreEditorRestriction = false;
        }else{
            needToRestoreEditorRestriction = true;
            groupModel.getRestrictions().setEditorRestricted(true);            
        }
        try {
            selector.open(groupModel);
        } catch (CantOpenSelectorError error) {
            if (error.getCause() instanceof InterruptedException) {
                throw (InterruptedException) error.getCause();
            } else {
                throw error;
            }
        }
        selector.addCurrentEntityHandler(currentEntityHandler);
        selectorIsOpened = false;
        onEntitySelected(selector.getCurrentEntity());
    }

    private boolean isEntityCanBeChoosed(final EntityModel entity) {
        if (entity == null || !selector.getGroupModel().getEntitySelectionController().isEntityChoosable(entity)) {
            return false;
        }
        return selector.getGroupModel() == selector.getModel()
                || ((GroupModel) selector.getModel()).getEntitySelectionController().isEntityChoosable(entity);
    }

    private void onEntitySelected(final EntityModel entity) {
        setActionEnabled(selectAction, isEntityCanBeChoosed(entity));
    }

    private void initButtonBox(final boolean canClean) {
        this.addCloseAction(selectAction, DialogResult.ACCEPTED);
        this.addCloseAction(cancelAction, DialogResult.REJECTED);
        if (canClean) {
            this.addCloseAction(cleanAction, DialogResult.ACCEPTED);
        }
        setActionEnabled(selectAction, false);
    }
    
    @Override
    public DialogResult execDialog(final IWidget parentWidget) {
        if (!selectorIsOpened){
            try{
                openSelector();
            }catch(InterruptedException exception){
                return DialogResult.REJECTED;
            }finally{
                groupModel = null;
            }               
        }
        return super.execDialog(parentWidget);
    }      

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            if (cleanAction.equals(action)) {
                selectedEntity = null;
            } else {
                selectedEntity = selector.getCurrentEntity();
            }
        } else {
            selectedEntity = null;
        }
        final GroupModel group = (GroupModel)selector.getModel();
        if (selector.close(false)) {
            selector.removeCurrentEntityHandler(currentEntityHandler);
            selectorIsOpened = false;
            if (group!=null && needToRestoreEditorRestriction && 
                group.getRestrictions().canBeAllowed(ERestriction.EDITOR)){
                group.getRestrictions().setEditorRestricted(false);
            }
            return super.onClose(action, actionResult);
        }
        return null;
    }

    @Override
    public EntityModel getSelectedEntity() {
        return selectedEntity;
    }
}
