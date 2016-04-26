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

package org.radixware.kernel.explorer.widgets.commands;

import org.radixware.kernel.common.client.errors.ModelError;
import org.radixware.kernel.common.client.meta.RadPresentationCommandDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.explorer.views.selector.Selector;

class CommandButton {

    private final Command cmd;
    private final Property property;

    private final static class PresentationChangedHandler implements IPresentationChangedHandler {

        final private IPresentationChangedHandler original;
        private boolean wasChanged;
        final private EntityModel model;
        private EntityModel newEntity = null;

        public PresentationChangedHandler(final EntityModel entity) {
            model = entity;
            original = ((IContext.Entity) entity.getContext()).getPresentationChangedHandler();
        }

        @Override
        public EntityModel onChangePresentation(RawEntityModelData rawData,
                                                Id newPresentationClassId, 
                                                Id newPresentationId) {
            wasChanged = true;
            if (original != null) {
                newEntity = original.onChangePresentation(rawData,
                                                          newPresentationClassId, 
                                                          newPresentationId);
                return newEntity;
            } else {
                final String errorMessage = model.getEnvironment().getMessageProvider().translate("ExplorerException", " from #%s (#%s) to #%s (#%s)");
                throw new ModelError(ModelError.ErrorType.CANT_CHANGE_PRESENTATION, model, String.format(errorMessage, model.getTitle(), model.getDefinition().getId(), model.getDefinition().getOwnerClassId(), newPresentationId, newPresentationClassId));
            }
        }

        public boolean presentationWasChanged() {
            return wasChanged;
        }

        public EntityModel getNewEntityModel() {
            return newEntity;
        }

        public IPresentationChangedHandler getOriginal() {
            return original;
        }
    }

    public CommandButton(Command command) {
        this.cmd = command;
        //this.setSizePolicy(Policy.Expanding, Policy.Expanding);
        //this.clicked.connect(this, "onExecute()");
        property = null;
    }

    public CommandButton(Command command, final Property property) {
        this.cmd = command;
        //this.setSizePolicy(Policy.Expanding, Policy.Expanding);
        //this.clicked.connect(this, "onExecute()");
        this.property = property;
    }

    public Command getCommand() {
        return cmd;
    }

    public void subscribe(IModelWidget w) {
        if (cmd == null) {
            throw new IllegalStateException("command was not defined");
        }
        cmd.subscribe(w);
        w.refresh(cmd);
    }

    public void unsubscribe(IModelWidget w) {
        if (cmd != null) {
            cmd.unsubscribe(w);
        }
    }
    
    public boolean isRestricted(){
        if (cmd!=null){
            final Model owner = cmd.getOwner();
            final Restrictions restrictions;
            if (owner instanceof EntityModel){                
                restrictions = ((EntityModel)owner).getRestrictions();
            }else if (owner instanceof GroupModel){
                restrictions = ((GroupModel)owner).getRestrictions();
            }else if (owner.getView()!=null){
                restrictions = owner.getView().getRestrictions();
            }else{
                restrictions = null;
            }
            if (restrictions!=null){
                return restrictions.getIsCommandRestricted(cmd.getId())
                       || (!cmd.getDefinition().isReadOnly() && restrictions.getIsNotReadOnlyCommandsRestricted());
            }
        }
        return false;
    }

    public RdxIcon loadIcon(final Id iconId) {
        Icon icon = null;
        try {
            icon = cmd.getEnvironment().getApplication().getDefManager().getImage(iconId);
        } catch (DefinitionError err) {
            final String mess = cmd.getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot get icon #%s for %s");
            cmd.getEnvironment().getTracer().error(String.format(mess, iconId, cmd.getDefinition().getDescription()), err);
        }
        return (RdxIcon) icon;
    }

    public String loadTitle(final Id titleId) {
        String title = "";
        if (cmd.getOwner() != null) {
            try {
                title = cmd.getEnvironment().getApplication().getDefManager().getMlStringValue(cmd.getOwner().getDefinition().getOwnerClassId(), titleId);
            } catch (DefinitionError err) {
                final String mess = cmd.getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot get title #%s for %s");
                cmd.getEnvironment().getTracer().error(String.format(mess, titleId, cmd.getDefinition().getDescription()), err);
            }
        } else if (cmd.getId().getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
            try {
                title = cmd.getEnvironment().getApplication().getDefManager().getMlStringValue(cmd.getId(), titleId);
            } catch (DefinitionError err) {
                final String mess = cmd.getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot get title #%s for %s");
                cmd.getEnvironment().getTracer().error(String.format(mess, titleId, cmd.getDefinition().getDescription()), err);
            }
        }
        return title;
    }

    //@SuppressWarnings("unused")//slot for clicked signal
    void onExecute() {
        cmd.getOwner().finishEdit();
        if (!cmd.getOwner().checkInputAcceptable()){
            return;
        }        
        EntityModel entity = null;
        final Id propertyId = property == null ? null : property.getId();
        if (cmd.getDefinition().getAccessibility() == ECommandAccessibility.ONLY_FOR_FIXED) {
            if (cmd.getDefinition() instanceof RadPresentationCommandDef){
                cmd.setRereadOwnerAfterExecute(((RadPresentationCommandDef)cmd.getDefinition()).needToRereadOwnerAfterExecuteCommand());
            }
            if (cmd.getOwner() instanceof EntityModel) {
                entity = (EntityModel) cmd.getOwner();
            } else if (cmd.getOwner() instanceof GroupModel) {
                final Selector selector = (Selector) ((GroupModel) cmd.getOwner()).getGroupView();
                entity = selector != null ? selector.getCurrentEntity() : null;
            }
            if (entity != null && entity.isEdited()) {
                final String title = cmd.getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Apply Changes"),
                        msg = cmd.getEnvironment().getMessageProvider().translate("ExplorerMessage", "You must save your data before execute this command.\n Do you want to save your changes now?");
                final PresentationChangedHandler handler = new PresentationChangedHandler(entity);
                ((IContext.Entity) entity.getContext()).setPresentationChangedHandler(handler);
                try {
                    if (!Application.messageConfirmation(title, msg) || !entity.update()) {
                        return;
                    }
                } catch (InterruptedException ex) {
                    return;
                } catch (Exception ex) {
                    entity.showException(ex);
                    return;
                } finally {
                    ((IContext.Entity) entity.getContext()).setPresentationChangedHandler(handler.getOriginal());
                }
                if (handler.presentationWasChanged()) {
                    final EntityModel newEntityModel = handler.getNewEntityModel();
                    if (newEntityModel != null && newEntityModel.getAccessibleCommandIds().contains(cmd.getId())) {
                        executeCommandImpl(newEntityModel.getCommand(cmd.getId()), propertyId, newEntityModel);
                    }
                    return;
                }
            }
        }
        executeCommandImpl(cmd, propertyId, entity);
    }

    private static void executeCommandImpl(final Command cmd, final Id propertyId, final EntityModel entity) {
        if (cmd.getDefinition().needForConfirmation()) {
            final String title = cmd.getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Executing Command"),
                    msg = cmd.getEnvironment().getMessageProvider().translate("ExplorerMessage", "Do you really want to execute command \'%s\'?");
            if (!Application.messageConfirmation(title, String.format(msg, cmd.getTitle()))) {
                return;
            }
        }

        final PresentationChangedHandler handler;
        if (entity == null) {
            handler = null;
        } else {
            handler = new PresentationChangedHandler(entity);
            ((IContext.Entity) entity.getContext()).setPresentationChangedHandler(handler);
        }

        try {
            cmd.execute(propertyId);
            if (cmd.needToRereadOwnerAfterExecute() && entity != null && !handler.presentationWasChanged()) {
                entity.read();
            }
        } catch (Exception ex) {
            cmd.processException(ex);
        } finally {
            if (entity != null && !handler.presentationWasChanged()) {
                ((IContext.Entity) entity.getContext()).setPresentationChangedHandler(handler.getOriginal());
            }
        }
    }
    /*
    @Override
    protected void closeEvent(QCloseEvent event) {
    cmd.unsubscribe(this);
    super.closeEvent(event);
    }	*/
}
