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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import java.util.EnumSet;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IModelFinder;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.ISelectEntityDialog;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;

public class SelectEntityDialog extends ExplorerDialog implements ISelectEntityDialog {
    
    private static class UpdateButtonStateEvent extends QEvent{
        public UpdateButtonStateEvent(){
            super(QEvent.Type.User);
        }
    }

    public EntityModel selectedEntity = null;
    private GroupModel groupModel;
    private final Selector selector;
    private boolean selectorIsOpened;
    private boolean needToRestoreEditorRestriction;
    private boolean needToRestoreMultiSelectRestriction;
    private final ISelector.CurrentEntityHandler currentEntityHandler = new ISelector.CurrentEntityHandler() {

        @Override
        public void onSetCurrentEntity(final EntityModel entity) {
            SelectEntityDialog.this.refreshUi();
        }

        @Override
        public void onLeaveCurrentEntity() {
            Application.processEventWhenEasSessionReady(SelectEntityDialog.this, new UpdateButtonStateEvent());
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

    @SuppressWarnings("LeakingThisInConstructor")
    public SelectEntityDialog(final GroupModel groupModel, final boolean canBeNull){
        super(groupModel.getEnvironment(),
              Application.getMainWindow(),
              groupModel.getConfigStoreGroupName(),
              groupModel.getSelectorPresentationDef().getDefaultDialogWidth(),
              groupModel.getSelectorPresentationDef().getDefaultDialogHeight()
              );
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        this.groupModel = groupModel;
        setWindowTitle(groupModel.getWindowTitle());        
        selector = (Selector) groupModel.createView();
        selector.setParent(this);
        selector.setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.MinimumExpanding);        
        selector.setObjectName("groupView");
        layout().addWidget(selector);
        initButtonBox(canBeNull);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }
    
    public void openSelector() throws InterruptedException{
        final IView nearestView = findNearestView(groupModel);
        if (nearestView!=null){
            selector.getRestrictions().add(nearestView.getRestrictions());
        }
        final ExplorerMenu selectorMenu = new ExplorerMenu(Application.translate("MainMenu", "&Selector"));
        selector.setMenu(selectorMenu, null);
        selector.entityActivated.connect(this, "onEntityActivated(EntityModel)");

        beforeOpenSelector(groupModel);
        try {
            selector.open(groupModel);
        } catch (CantOpenSelectorError error) {
            if (error.getCause() instanceof InterruptedException) {
                throw (InterruptedException) error.getCause();
            } else {
                throw error;
            }
        }
        afterOpenSelector(selector,groupModel);
        selectorIsOpened = true;
        refreshUi();
        selector.setFocus();
    }

    private void initButtonBox(final boolean canClean) {
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),false);
        if (canClean) {
            final QPushButton cleanButton = (QPushButton)addButton(EDialogButtonType.APPLY);
            cleanButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR));
            cleanButton.setText(Application.translate("SelectEntityDialog", "Clear"));
            cleanButton.clicked.connect(this, "onClearClicked()");
        }
        setCenterButtons(true);
        rejectButtonClick.connect(this, "reject()");
        acceptButtonClick.connect(this, "onOkClicked()");        
    }

    @Override
    protected void keyPressEvent(final QKeyEvent keyEvent) {
        if ((keyEvent.key() == Qt.Key.Key_Return.value() || keyEvent.key() == Qt.Key.Key_Enter.value())
            && keyEvent.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value()){
            onCtrlEnter();
        }else{
            super.keyPressEvent(keyEvent);
        }
    }
    
    protected void onOkClicked() {
        final EntityModel entity = selector.getCurrentEntity();
        selectedEntity = entity;
        accept();
    }
    
    protected void onCtrlEnter(){
        if (isEntityCanBeChoosed(selector.getCurrentEntity())){
            onOkClicked();
        }
    }
    
    protected void onClearClicked(){
        accept();
    }

    protected final boolean isEntityCanBeChoosed(final EntityModel entity) {
        if (entity == null || !selector.getGroupModel().getEntitySelectionController().isEntityChoosable(entity)) {
            return false;
        }
        return selector.getGroupModel() == selector.getModel()
                || ((GroupModel) selector.getModel()).getEntitySelectionController().isEntityChoosable(entity);
    }
    
    protected void onEntityActivated(final EntityModel entity) {
        if (isEntityCanBeChoosed(entity)) {
            selectedEntity = entity;
            accept();
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof UpdateButtonStateEvent){
            event.accept();
            refreshUi();
        }else{
            super.customEvent(event);
        }
    }
       
    @Override
    public int exec() {
        if (!selectorIsOpened){
            boolean isOpened = false;
            try{
                openSelector();
                isOpened = true;
            }catch(InterruptedException exception){
                return QtDialog.DialogCode.Rejected.value();
            }finally{
                groupModel = null;
                if (!isOpened){
                    disposeLater();
                }                
            }
        }
        return super.exec();
    }

    @Override
    public void done(final int result) {
        final GroupModel group = (GroupModel)selector.getModel();
        if (selector.close(false)) {            
            selectorIsOpened = false;
            afterCloseSelector(group);
            super.done(result);
        }
    }

    @Override
    public EntityModel getSelectedEntity() {
        return selectedEntity;
    }
    
    protected final Selector getSelector(){
        return selector;
    }    
    
    protected void beforeOpenSelector(final GroupModel group){
        addEditorRestriction(group);
        addMultiSelectRestriction(group);
    }
    
    protected final void addEditorRestriction(final GroupModel group){
        if (!group.getRestrictions().getIsEditorRestricted()){
            needToRestoreEditorRestriction = true;
            group.getRestrictions().setEditorRestricted(true);
        }else{
            needToRestoreEditorRestriction = false;
        }
    }
    
    protected final void addMultiSelectRestriction(final GroupModel group){
        if (!group.getRestrictions().getIsMultipleSelectionRestricted()){
            needToRestoreMultiSelectRestriction = true;
            group.getRestrictions().setMultipleSelectionRestricted(true);
        }else{
            needToRestoreMultiSelectRestriction = false;
        }
    }    
    
    protected final void removeEditorRestriction(final GroupModel group){
        if (group!=null && needToRestoreEditorRestriction && 
            group.getRestrictions().canBeAllowed(ERestriction.EDITOR)){
            group.getRestrictions().setEditorRestricted(false);
            needToRestoreEditorRestriction = false;
        }        
    }
    
    protected final void removeMultiselectRestriction(final GroupModel group){
        if (group!=null && needToRestoreMultiSelectRestriction && 
            group.getRestrictions().canBeAllowed(ERestriction.MULTIPLE_SELECTION)){
            group.getRestrictions().setMultipleDeleteRestricted(false);
            needToRestoreMultiSelectRestriction = false;
        }        
    }
    
    protected void afterOpenSelector(final Selector selector, final GroupModel group){
        selector.addCurrentEntityHandler(currentEntityHandler);        
    }
    
    protected void afterCloseSelector(final GroupModel group){
        selector.removeCurrentEntityHandler(currentEntityHandler);
        removeEditorRestriction(group);
        removeMultiselectRestriction(group);
    }
    
    protected boolean isOkButtonEnabled(){
        return isEntityCanBeChoosed(selector.getCurrentEntity());
    }
    
    protected void refreshUi(){
        if (getButton(EDialogButtonType.OK) != null){
            getButton(EDialogButtonType.OK).setEnabled(isOkButtonEnabled());
        }        
    }
    
}