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
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.CantOpenEditorError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IModelFinder;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModifiedEntityModelFinder;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IEditor.EditorListener;
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.views.ModificationsList;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.views.editor.Editor;
import org.radixware.wps.views.editor.ErrorView;

public final class EntityEditorDialog extends Dialog implements IPresentationChangedHandler, IEntityEditorDialog {

    private EntityModel model;
    private org.radixware.wps.views.editor.Editor editor;
    private boolean closing, editorIsOpened, toolBarIsHidden, allowUpdateOnClosing;
    private boolean wasChanged;
    private IPresentationChangedHandler oldPCH;
    private final WpsEnvironment environment;

    private static DialogGeometry getDefaultGeometry(EntityModel entityModel){
        final RadEditorPresentationDef presentation = entityModel.getEditorPresentationDef();
        int w = 400,h=300;
        if(presentation.getDefaultDialogWidth()>0){
            w = presentation.getDefaultDialogWidth();
        }
        if(presentation.getDefaultDialogHeight()>0){
            h = presentation.getDefaultDialogHeight();
        }
        return new DialogGeometry(w, h, 0, 0);
    }
    public EntityEditorDialog(final EntityModel entityModel){
        super(((WpsEnvironment) entityModel.getEnvironment()).getDialogDisplayer(), entityModel.getWindowTitle(),true,getDefaultGeometry(entityModel));        
        html.setAttr("dlgId", "ee" + entityModel.getClassId().toString());
        this.environment = (WpsEnvironment) entityModel.getEnvironment();
        model = entityModel;
    }

    private void setupUi(){
        setWindowTitle(model.getWindowTitle());
        model.addTitleListener(new Model.ITitleListener() {
            @Override
            public void titleChanged(final String oldTitle, final String newTitle) {
                setWindowTitle(model.getWindowTitle());
            }
        });
        editor = createEditor(model);
        editor.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        editor.setParent(this);
        editor.setObjectName("entityView");
        add(editor);
        editorIsOpened = true;
        if (!model.isNew() && editor.getEntityModel().getRestrictions().getIsUpdateRestricted()) {
            addCloseAction(EDialogButtonType.CLOSE).setDefault(true);
        } else {
            final PushButton btn = new PushButton(StandardButton.getTitle(EDialogButtonType.OK, getEnvironment()));
            btn.setObjectName(StandardButton.getTitle(EDialogButtonType.OK, getEnvironment()));
            btn.setIcon(getEnvironment().getApplication().getImageManager().getIcon(StandardButton.getIcon(EDialogButtonType.OK)));
            btn.setDefault(true);
            addCloseAction(btn, DialogResult.APPLY);
            addCloseAction(EDialogButtonType.CANCEL);
        }
        
        editor.setFocus();
    }
    
    public void openEditor() throws ServiceClientException, InterruptedException{
        oldPCH = ((IContext.Entity) model.getContext()).getPresentationChangedHandler();
        ((IContext.Entity) model.getContext()).setPresentationChangedHandler(this);
        if (model.isExists() && !model.wasRead()) {
            if (model.isEdited()){//TWRBS-2060
                model.activateAllProperties();
            }
            else{
                model.read();
            }
        }    
        if (model.isExists() && 
            !model.getRestrictions().getIsUpdateRestricted() &&
            detectReenterableModification()//RADIX-425
           ){
            model.getRestrictions().setUpdateRestricted(true);
            allowUpdateOnClosing = true;
        }else{
            allowUpdateOnClosing = false;
        }        
        //model.read invoke may cause editor presentation change and call of setupUi from 
        //onChangePresentation handler, so we need check this
        if (!editorIsOpened){
            setupUi();
        }
    }
    
    private boolean detectReenterableModification(){
        return model.findOwner(new ModifiedEntityModelFinder(model.getPid()))!=null;
    }    

    @Override
    public void setToolBarHidden(final boolean isHidden) {
        toolBarIsHidden = isHidden;
        if (editorIsOpened){
            editor.setToolBarHidden(isHidden);
        }
    }        
    
    private IView findNearestView(final EntityModel model){
        final Model nearestModelWithView = model.findOwner(new IModelFinder() {
            @Override
            public boolean isTarget(final Model model) {
                return model.getView()!=null;
            }
        });
        return nearestModelWithView==null ? null : nearestModelWithView.getView();
    }    

    private Editor createEditor(final EntityModel entityModel) {
        final Editor result = (Editor) entityModel.createView();
        final IView nearestView = findNearestView(entityModel);
        if (nearestView!=null){
            result.getRestrictions().add(nearestView.getRestrictions());
        }        
        result.open(entityModel);
        result.getActions().getDeleteAction().setVisible(false);
        if (entityModel.isNew() || toolBarIsHidden) {
            result.setToolBarHidden(true);
        }
        result.addEditorListener(new EditorListener() {

            @Override
            public void entityUpdated() {
                onUpdated();
            }

            @Override
            public void entityRemoved() {
            }
        });
        return result;
    }

    @Override
    protected void dialogButtonAction(String name) {
        if (StandardButton.getTitle(EDialogButtonType.OK, getEnvironment()).equals(name)) {
            onOkClicked();
        } else {
            super.dialogButtonAction(name);
        }
    }

    private void onOkClicked() {
        closing = true;
        try {
            model.finishEdit();
            if (model.isNew()) {
                if (model.getContext() instanceof IContext.ObjectPropCreating) {
                    final IContext.ObjectPropCreating context = (IContext.ObjectPropCreating) model.getContext();
                    final Property property = context.propOwner.getProperty(context.propId);
                    if (property.getValueObject() != null && property.hasOwnValue()) {
                        final String caption = environment.getMessageProvider().translate("EntityEditorDialog", "Confirm to Change Value of Property");
                        final String message = environment.getMessageProvider().translate("EntityEditorDialog", "Old value of property \'%s\'\n will be lost. Do you really want to continue?");
                        if (!environment.messageConfirmation(caption, String.format(message, property.getDefinition().getTitle()))) {
                            return;
                        }
                    }
                }
                final EEntityCreationResult creationResult = model.create();
                if (creationResult==EEntityCreationResult.SUCCESS) {
                    this.acceptDialog();
                }else if (creationResult==EEntityCreationResult.CANCELED_BY_SERVER){
                    close(DialogResult.NONE);
                }
            } else {
                final boolean changesWasApplied;
                if (editor == null) {
                    changesWasApplied = true;
                } else {
                    final ModificationsList modifications = new ModificationsList(editor);
                    changesWasApplied =
                            modifications.applyChangesOnClosingView(getEnvironment(), this, modifications.getModifiedObjectsList());
                }
                if (changesWasApplied && model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) {
                    this.acceptDialog();
                }
            }
        } catch (InterruptedException ex) {
        } catch (Exception ex) {
            final String errorMessageTitle;
            if (model.isNew()) {
                if (model.getSrcPid() != null) {
                    errorMessageTitle = environment.getMessageProvider().translate("Editor", "Failed to create copy");
                } else {
                    errorMessageTitle = environment.getMessageProvider().translate("Editor", "Failed to create entity");
                }
            } else {
                errorMessageTitle = environment.getMessageProvider().translate("Editor", "Failed to apply changes");
            }
            model.showException(errorMessageTitle, ex);
        } finally {
            closing = false;
        }
    }

    @Override
    public DialogResult execDialog(IWidget parentWidget) {
        if (!editorIsOpened){
            try{
                openEditor();
            }catch(ObjectNotFoundError exception){
                if(exception.inKnownContext()){
                    throw new CantOpenEditorError(model, exception);
                }else{
                    processServiceClientException(exception);
                }
            }catch(ServiceClientException exception){
                processServiceClientException(exception);
            }catch(InterruptedException exception){
                return DialogResult.REJECTED;
            }
        }
        return super.execDialog(parentWidget);
    }
    
    private void processServiceClientException(final ServiceClientException exception){
        final ErrorView error = new ErrorView();
        error.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        add(error);
        final String errorTitle = 
            getEnvironment().getMessageProvider().translate("ExplorerError", "Failed to open editor of '%1$s'");
        error.setError(errorTitle, exception);
        setWindowTitle(model.getWindowTitle());
        setWindowIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ERROR));
        addCloseAction(EDialogButtonType.CLOSE).setDefault(true);        
    }

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
        closing = true;
        try {
            if (editor != null) {
                if (actionResult == DialogResult.REJECTED) {
                    model.finishEdit();
                    final ModificationsList modifications;
                    final boolean isModified;
                    if (model.isNew()){                  
                        modifications = ModificationsList.EMPTY_LIST;
                        isModified = !model.getEditedProperties().isEmpty();
                    }else{
                        modifications = new ModificationsList(editor);
                        isModified = !modifications.isEmpty();
                    }
                    if (isModified){
                        final String message = getEnvironment().getMessageProvider().translate("EntityEditorDialog", "Do you really want to close editor without saving your changes?");
                        final String title = getEnvironment().getMessageProvider().translate("EntityEditorDialog", "Confirm to Close Editor");
                        if (!getEnvironment().messageConfirmation(title, message)) {
                            return null;
                        } else if (!model.isNew()) {
                            modifications.cancelChanges();
                        }
                    }
                    if (!model.isNew() && !model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) {
                        return null;
                    }
                }
                final Editor closingEditor = editor;
                editor = null;
                closingEditor.close(true);
                remove(editor);
                editorIsOpened = false;                
            }
        } finally {
            closing = false;
        }

        if (model != null) {
            ((IContext.Entity) model.getContext()).setPresentationChangedHandler(oldPCH);
            if (allowUpdateOnClosing){
                model.getRestrictions().setUpdateRestricted(false);
            }            
        }
        return super.onClose(action, actionResult == DialogResult.NONE ? DialogResult.REJECTED : actionResult);
    }

    @Override
    public EntityModel getEntityModel() {
        return model;
    }
    
    private void respawnModel(final Id presentationId, final IContext.Entity context){
        final RadEditorPresentationDef presentation = 
            getEnvironment().getApplication().getDefManager().getEditorPresentationDef(presentationId);
        model = presentation.createModel(context);        
    }    

    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawData,
                                           final Id newPresentationClassId, 
                                           final Id newPresentationId) {
        final IContext.Entity context = (IContext.Entity) model.getContext();
        model.clean();
        if (editor != null) {
            remove(editor);
            editorIsOpened = false;
        }
        clearCloseActions();
        if (context instanceof IContext.InSelectorEditing) {
            IContext.InSelectorEditing ctx = (IContext.InSelectorEditing) context;
            IContext.Entity entityContext = ctx.getSelectorRowContext();
            if (entityContext==null || entityContext.getPresentationChangedHandler()==null){
                respawnModel(newPresentationId, context);
            }else{
                final EntityModel row = 
                    entityContext.getPresentationChangedHandler().onChangePresentation(rawData, 
                                                                                   newPresentationClassId, 
                                                                                   newPresentationId);
                model = row.openInSelectorEditModel();
                ((IContext.Entity) model.getContext()).setPresentationChangedHandler(this);            
            }
        } else {
            respawnModel(newPresentationId, context);
        }
        model.activate(rawData);
        if (editor != null) {
            setupUi();
        }
        return model;
    }

    @SuppressWarnings("unused")
    private void onUpdated() {
        wasChanged = true;
    }    

    @Override
    public boolean entityWasChanged() {
        return wasChanged;
    }

    @Override
    public boolean dialogClosing() {
        return closing;
    }

    public void forceClose() {
        if (editor != null && model.isExists()) {
            new ModificationsList(editor).cancelChanges();
        }
    }
}
