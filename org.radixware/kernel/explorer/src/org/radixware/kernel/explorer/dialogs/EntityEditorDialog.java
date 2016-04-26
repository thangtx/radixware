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

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
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
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.views.ModificationsList;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.explorer.views.Editor;
import org.radixware.kernel.explorer.views.ErrorView;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;

public final class EntityEditorDialog extends ExplorerDialog implements IPresentationChangedHandler, IEntityEditorDialog {
    
    private EntityModel model;
    private Editor editor;
    private boolean closing,forceClose,editorIsOpened,toolBarIsHidden,allowUpdateOnClosing;
    private IPresentationChangedHandler oldPCH;

    public EntityEditorDialog(final EntityModel entityModel){
        super(entityModel.getEnvironment(),
              (QWidget) entityModel.getEnvironment().getMainWindow(),//parentWidget
              getGeometryConfigPrefix(entityModel),//dlgName
              entityModel.getEditorPresentationDef().getDefaultDialogWidth(),
              entityModel.getEditorPresentationDef().getDefaultDialogHeight()
              );
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        model = entityModel;
    }
    
    private static String getGeometryConfigPrefix(final EntityModel entityModel){
        if (entityModel.isNew()){
            return entityModel.getConfigStoreGroupName()+"_creating";
        }else{
            return entityModel.getConfigStoreGroupName();
        }
    }

    private void setupUi() {
        setWindowTitle(model.getWindowTitle());
        model.addTitleListener(new Model.ITitleListener() {
            @Override
            public void titleChanged(final String oldTitle, final String newTitle) {
                setWindowTitle(model.getWindowTitle());
            }
        });        
        editor = createEditor(model);
        editor.setSizePolicy(Policy.Expanding, Policy.Expanding);
        editor.setObjectName("entityView");
        layout().addWidget((QWidget) editor);
        if (!model.isNew() && editor.getEntityModel().getRestrictions().getIsUpdateRestricted()) {
            addButton(EDialogButtonType.CLOSE);
        } else {
            addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),false);
        }
        acceptButtonClick.connect(this, "onOkClicked()");
        rejectButtonClick.connect(this, "reject()");
        editorIsOpened = true;
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        editor.setFocus();        
    }
    
    public void openEditor() throws InterruptedException, ServiceClientException{
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
    public void setToolBarHidden(final boolean isHidden){
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
        result.setParent(this);
        final IView nearestView = findNearestView(entityModel);
        if (nearestView!=null){
            result.getRestrictions().add(nearestView.getRestrictions());
        }
        if (!entityModel.isNew()) {
            final IMenu editorMenu = new ExplorerMenu(getEnvironment().getMessageProvider().translate("MainMenu", "&Editor"));
            result.setMenu(editorMenu);
        }
        result.open(entityModel);
        result.getActions().getDeleteAction().setVisible(false);
        if (entityModel.isNew() || toolBarIsHidden) {
            result.setToolBarHidden(true);
        }
        result.entityUpdated.connect(this, "onUpdated()");
        return result;
    }
    
    @SuppressWarnings("unused")
    private void onOkClicked() {
        closing = true;
        try {
            model.finishEdit();
            if (model.isNew()) {
                if (model.getContext() instanceof IContext.ObjectPropCreating) {
                    final IContext.ObjectPropCreating context = (IContext.ObjectPropCreating) model.getContext();
                    final Property property = context.propOwner.getProperty(context.propId);
                    if (property.getValueObject() != null && property.hasOwnValue()) {
                        final String caption = getEnvironment().getMessageProvider().translate("EntityEditorDialog", "Confirm to Change Value of Property");
                        final String message = getEnvironment().getMessageProvider().translate("EntityEditorDialog", "Old value of property \'%s\'\n will be lost. Do you really want to continue?");
                        if (!Application.messageConfirmation(caption, String.format(message, property.getDefinition().getTitle()))) {
                            return;
                        }
                    }
                }
                final EEntityCreationResult creationResult = model.create();
                if (creationResult==EEntityCreationResult.SUCCESS) {
                    accept();
                }else if (creationResult==EEntityCreationResult.CANCELED_BY_SERVER){
                    done(-1);
                }
            } else {
                final boolean changesWasApplied;
                if (editor==null){
                    changesWasApplied = true;
                }
                else{
                    final ModificationsList modifications = new ModificationsList(editor);
                    changesWasApplied = 
                        modifications.applyChangesOnClosingView(getEnvironment(), this, modifications.getModifiedObjectsList());                    
                }
                if (changesWasApplied && model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)){
                    this.accept();
                }
            }
        } catch (InterruptedException ex) {
        } catch (Exception ex) {
            final String errorMessageTitle;
            if (model.isNew()) {
                if (model.getSrcPid() != null) {
                    errorMessageTitle = getEnvironment().getMessageProvider().translate("Editor", "Failed to create copy");
                } else {
                    errorMessageTitle = getEnvironment().getMessageProvider().translate("Editor", "Failed to create entity");
                }
            } else {
                errorMessageTitle = getEnvironment().getMessageProvider().translate("Editor", "Failed to apply changes");
            }
            model.showException(errorMessageTitle, ex);
        } finally {
            closing = false;
        }
    }

    @Override
    public int exec() {
        if (!editorIsOpened){
            boolean dialogWasOpened = false;
            try{
                openEditor();
                dialogWasOpened = true;
            }catch(ObjectNotFoundError exception){
                if (exception.inKnownContext()){
                    throw new CantOpenEditorError(model, exception);
                }else{
                    processServiceClientException(exception);
                    dialogWasOpened = true;
                }
            }catch(ServiceClientException exception){
                processServiceClientException(exception);
                dialogWasOpened = true;
            }catch(InterruptedException exception){
                return QtDialog.DialogCode.Rejected.value();
            }finally{
                if (!dialogWasOpened){
                    model = null;
                    disposeLater();
                }
            }
        }
        return super.exec();
    }
    
    private void processServiceClientException(final ServiceClientException exception){
        final ErrorView errorView = new ErrorView(getEnvironment(), this);                
        errorView.setObjectName("errorView");
        layout().addWidget(errorView);
        final String errorTitle = 
            getEnvironment().getMessageProvider().translate("ExplorerError", "Failed to open editor of '%1$s'");
        errorView.setError(String.format(errorTitle, model.getTitle()), exception);
        setWindowTitle(model.getWindowTitle());
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.TraceLevel.ERROR));
        addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);        
    }

    @Override
    public void done(final int result) {
        closing = true;
        try {
            if (editor != null) {
                if (result == QDialog.DialogCode.Rejected.value() &&!forceClose) {
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
                        if (!getEnvironment().messageConfirmation(title, message)){
                            return;
                        }
                        else if (!model.isNew()){
                            modifications.cancelChanges();
                        }
                    }
                    if (!model.isNew() && !model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)){
                        return;
                    }
                }
                final Editor closingEditor = editor;
                editor = null;
                closingEditor.close(true);
                layout().removeWidget((QWidget) closingEditor);
                editorIsOpened = false;
                removeButtonBox();
            }
        }
        catch (Exception exception){
            getEnvironment().getTracer().error(exception);
        } finally {
            closing = false;
        }

        if (model != null) {
            ((IContext.Entity) model.getContext()).setPresentationChangedHandler(oldPCH);
            if (allowUpdateOnClosing){
                model.getRestrictions().setUpdateRestricted(false);
            }
        }
        super.done(result<0 ? QDialog.DialogCode.Rejected.value() : result);
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
            final Id newPresentationClassId, final Id newPresentationId) {
        setUpdatesEnabled(false);
        try {
            final IContext.Entity context = (IContext.Entity) model.getContext();
            model.clean();
            if (editor != null) {
                layout().removeWidget((QWidget) editor);
                editorIsOpened = false;
            }
            removeButtonBox();
            if (context instanceof IContext.InSelectorEditing) {
                final IContext.InSelectorEditing ctx = (IContext.InSelectorEditing) context;
                final IContext.Entity entityContext = ctx.getSelectorRowContext();
                if (entityContext==null || entityContext.getPresentationChangedHandler()==null){
                    respawnModel(newPresentationId, context);
                }else{
                    final EntityModel row = 
                        entityContext.getPresentationChangedHandler().onChangePresentation(rawData, newPresentationClassId, newPresentationId);
                    model = row.openInSelectorEditModel();
                    ((IContext.Entity) model.getContext()).setPresentationChangedHandler(this);
                }                
            } else {
                respawnModel(newPresentationId, context);
            }
            model.activate(rawData);
            setupUi();
            return model;
        } finally {
            setUpdatesEnabled(true);
        }
    }

    @SuppressWarnings("unused")
    private void onUpdated() {
        wasChanged = true;
    }
        
    private boolean wasChanged;

    @Override
    public boolean entityWasChanged() {
        return wasChanged;
    }

    @Override
    public boolean dialogClosing() {
        return closing;
    }

    @Override
    public void forceClose() {
        forceClose = true;
        super.forceClose();
    }
}
