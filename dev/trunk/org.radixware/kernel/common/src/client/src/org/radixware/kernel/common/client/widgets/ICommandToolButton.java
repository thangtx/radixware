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

package org.radixware.kernel.common.client.widgets;

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
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.types.Id;


public interface ICommandToolButton extends IToolButton, IModelWidget {

    public boolean close();

    public static class CommandController {

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
                    newEntity = 
                        original.onChangePresentation(rawData, newPresentationClassId, newPresentationId);
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
        private Command cmd;
        private Property property;

        public void open(Command cmd, Property prop) {
            this.cmd = cmd;
            this.property = prop;

        }

        public Command getCommand() {
            return cmd;
        }
        
        public boolean isRestrictedInView(){
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
                    return restrictions.getIsCommandRestricted(cmd.getDefinition());
                }
            }
            return false;            
        }

        public Property getProperty() {
            return property;
        }

        public void executeCommand() {
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
                    final ISelector selector = ((GroupModel) cmd.getOwner()).getGroupView();
                    entity = selector != null ? selector.getCurrentEntity() : null;
                }
                final IEditor editor = entity==null ? null : entity.getEntityView();
                final boolean isModified = (editor !=null && editor.getActions().getUpdateAction().isEnabled())
                                                       || (entity!=null && entity.isEdited());                
                if (isModified) {
                    final String title = cmd.getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Apply Changes"),
                            msg = cmd.getEnvironment().getMessageProvider().translate("ExplorerMessage", "You must save your data before execute this command.\n Do you want to save your changes now?");
                    final PresentationChangedHandler handler = new PresentationChangedHandler(entity);
                    ((IContext.Entity) entity.getContext()).setPresentationChangedHandler(handler);
                    try {
                        if (!cmd.getEnvironment().messageConfirmation(title, msg)) {
                            return;
                        }
                        if (editor==null){
                            entity.update();
                        }else{
                            editor.getActions().getUpdateAction().trigger();
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
                if (!cmd.getEnvironment().messageConfirmation(title, String.format(msg, cmd.getTitle()))) {
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
    }
}
