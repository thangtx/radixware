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

package org.radixware.kernel.common.client.models.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.eas.resources.IMessageDialogResource;
import org.radixware.kernel.common.client.errors.WrongDefinitionFormatError;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPresentationCommandDef;
import org.radixware.kernel.common.client.models.*;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.widgets.ICommandPushButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.schemas.eas.CommandRs;
import org.radixware.schemas.eas.ContextlessCommandRs;

public class Command extends ModelItem {

    private final RadCommandDef def;
    
    private boolean visible;
    private boolean enabled;
    private boolean serverEnabled = true;//команда разрешена на сервере
    private String title;
    private Icon icon;
    private boolean rereadAfterExecute;
    private final Collection<Id> disabledForProperties = new ArrayList<>();
    private final Collection<Id> invisibleForProperties = new ArrayList<>();
    private Command synchronizedCommand = null;

    public Command(final Model owner, final RadCommandDef def) {
        super(owner, def.getId());
        this.def = def;
        visible = def.isVisible();
        if (owner instanceof EntityModel) {
            enabled = !((EntityModel) owner).getRestrictions().getIsCommandRestricted(def.getId());
        } else if (owner instanceof GroupModel) {
            enabled = !((GroupModel) owner).getRestrictions().getIsCommandRestricted(def.getId());
        } else {
            enabled = true;
        }
        if (enabled && def.getId().getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
            enabled = getEnvironment().isContextlessCommandAccessible(def.getId());
        }
        title = def.getTitle();
        icon = def.getIcon();
    }
    
    public final RadCommandDef getDefinition(){
        return def;
    }
    
    @Override
    public final Model getOwner() {//Overrided to make final
        return super.getOwner();
    }    

    public void execute() {
        execute(null);
    }

    public void execute(final Id propertyId) {//default command handler
        if (def.getNature() == ECommandNature.FORM_IN_OUT) {
            if (def.getStartFormId() == null) {
                final String message = getEnvironment().getMessageProvider().translate("Command", "Form was not defined for command '%s'");
                processException(new WrongDefinitionFormatError(def.getId(), String.format(message, def.getDescription())));
                return;
            }
            final FormModel form = FormModel.create(def.getStartFormId(), new IContext.Form(this, propertyId));
            if (form == null) //в методе afterInit произошло закрытие модели
            {
                return;
            }
            form.exec(null);
        } else {//command handler was not created
//			throw new AbstractMethodError();
        }
    }

    /*
     * //генерируемый метод public <XmlOut> send(<XmlIn> parameter) throws
     * org.radixware.kernel.common.exceptions.ServiceClientException,
     * InterruptedException{ XmlObject response =
     * org.radixware.kernel.explorer.env.Environment.session.executeCommand(owner,
     * null, def.getId(), parameter); return
     * org.radixware.kernel.explorer.utils.XmlObjectProcessor.castToXmlClass(processResponse(response),
     * <XmlOut>.class); }
     */
    protected final XmlObject processResponse(XmlObject response, Id propertyId) {

        final FormModel form = processResponse(response, null, propertyId);
        if (form != null) {
            try {
                form.exec(null);
            } catch (Exception ex) {
                getEnvironment().processException(ex);
            }
            return null;
        }
        final XmlObject output;
        if (def instanceof RadPresentationCommandDef) {
            output = ((CommandRs) response).getOutput();
        } else {
            output = ((ContextlessCommandRs) response).getOutput();
        }
        return getCommandOutput(output);
    }
    
    private static XmlObject getCommandOutput(final XmlObject output){
        return XmlObjectProcessor.hasChild(output) ? XmlObjectProcessor.getXmlObjectFirstChild(output) : null;
    }

    public final FormModel processResponse(XmlObject response, FormModel activeForm, Id propertyId) {
        final org.radixware.schemas.eas.PropertyList properties;
        final org.radixware.schemas.eas.NextDialogRequest nextDialog;
        
        final XmlObject output;
        if (def instanceof RadPresentationCommandDef) {
            final CommandRs rs = (CommandRs) response;
            if (rs.getCurrentData() != null) {
                properties = rs.getCurrentData().getProperties();
            } else {
                properties = null;
            }
            nextDialog = rs.getNextDialog();
            output = ((CommandRs) response).getOutput();
        } else {
            final ContextlessCommandRs rs = (ContextlessCommandRs) response;
            properties = null;
            nextDialog = rs.getNextDialog();
            output = ((ContextlessCommandRs) response).getOutput();
        }


        if (properties != null) {
            List<org.radixware.schemas.eas.PropertyList.Item> itemList = properties.getItemList();
            if (itemList != null) {
                Property property;
                for (org.radixware.schemas.eas.PropertyList.Item item : itemList) {
                    property = owner.getProperty(item.getId());
                    if (property.getDefinition().getType() == EValType.XML) {
                        final PropertyXml xmlProp = (PropertyXml) property;
                        final XmlObject value = xmlProp.castValue(ValueConverter.easPropXmlVal2ObjVal(item, EValType.XML, null));
                        property.setValueObject(value);
                    } else {
                        final Id valTableId;
                        if (property.getDefinition() instanceof RadParentRefPropertyDef) {
                            valTableId = ((RadParentRefPropertyDef) property.getDefinition()).getReferencedTableId();
                        } else {
                            valTableId = null;
                        }
                        property.setValueObject(ValueConverter.easPropXmlVal2ObjVal(item, property.getDefinition().getType(), valTableId));
                    }
                }
            }
        }

        final FormModel nextForm;
        if (nextDialog == null) {
            nextForm = null;
        }else{
            if (nextDialog.getMessageBox() != null) {
                final org.radixware.schemas.eas.NextDialogRequest.MessageBox messageBox = nextDialog.getMessageBox();
                final IMessageDialogResource messageDialog = getEnvironment().getResourceManager().openMessageDialogResource(messageBox);
                //IButton cancel = null;
                EDialogButtonType cancelButtonType = EDialogButtonType.CANCEL;
                if (messageBox.getCancelButtonType() != null) {
                    cancelButtonType = messageBox.getCancelButtonType();
                    //     cancel = messageDialog.getButton(cancelButton);
                    messageDialog.setEscapeButton(cancelButtonType);
                }

                if (activeForm != null && activeForm.getFormView() != null) {
                    activeForm.getFormView().hide();
                }

                messageDialog.exec();                
                if (messageDialog.getClickedButton() != null
                    && messageDialog.getClickedButton().getType()==cancelButtonType) {
                    afterExecute(activeForm, null, propertyId, getCommandOutput(output));
                    return null;
                }
            }

            if (nextDialog.getForm() == null) {
                nextForm = null;
            }else{
                final IContext.Form context;
                if (activeForm != null) {
                    context = new IContext.Form(activeForm);
                } else {
                    context = new IContext.Form(this, propertyId);
                }

                nextForm = FormModel.create(nextDialog.getForm(), context);                
            }
        }
        afterExecute(activeForm, nextForm, propertyId, getCommandOutput(output));
        return nextForm;
    }
    
    protected void afterExecute(final FormModel previousForm, final FormModel nextForm, final Id propertyId, final XmlObject response){
        
    }

    public final void startSynchronization(final Command command2) {
        if (synchronizedCommand == null) {
            synchronizedCommand = command2;    
            command2.synchronizedCommand = this;
            visible = synchronizedCommand.visible;
            enabled = synchronizedCommand.enabled;
            serverEnabled = synchronizedCommand.serverEnabled;
            title = synchronizedCommand.title;
            icon = synchronizedCommand.icon;
            disabledForProperties.clear();
            disabledForProperties.addAll(synchronizedCommand.disabledForProperties);
            invisibleForProperties.clear();
            invisibleForProperties.addAll(synchronizedCommand.invisibleForProperties);
        }
    }

    public final void stopSynchronization() {
        if (synchronizedCommand != null) {
            synchronizedCommand.synchronizedCommand = null;
            synchronizedCommand = null;
        }
    }
    
    public final boolean isSynchronized(){
        return synchronizedCommand!=null;
    }

    public Icon getIcon() {
        return icon;
    }

    public final void setIcon(final Icon icon) {
        this.icon = icon;
        afterModify();
        if (synchronizedCommand != null) {
            synchronizedCommand.icon = icon;
            synchronizedCommand.afterModify();
        }
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
        afterModify();
        if (synchronizedCommand != null) {
            synchronizedCommand.title = title;
            synchronizedCommand.afterModify();
        }
    }

    public boolean isEnabled() {
        final boolean isCommandRegistered = owner.getAccessibleCommandIds().contains(getId())
                || ((owner instanceof FormModel) && (def.getId().getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND));
        if (!isCommandRegistered || !serverEnabled || !enabled || !owner.isCommandEnabled(def)) {
            return false;
        }
        if (def.getAccessibility() == ECommandAccessibility.ALWAYS || !(owner instanceof EntityModel)) {
            return true;
        }
        EntityModel e = (EntityModel) owner;
        switch (def.getAccessibility()) {
            case ONLY_FOR_EXISTENT:
            case ONLY_FOR_FIXED:
                return !e.isNew();
            case ONLY_FOR_NEW:
                return e.isNew();
        }
        return true;
    }

    public final void setEnabled(final boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        if (!serverEnabled) {
            return;
        }
        if (!owner.getAccessibleCommandIds().contains(getId())) {
            return;
        }
        this.enabled = enabled;
        afterModify();
        if (synchronizedCommand != null) {
            synchronizedCommand.enabled = enabled;
            synchronizedCommand.afterModify();
        }
    }

    public final void enableForProperty(final Id propertyId) {//RADIX-1393
        if (!(def instanceof RadPresentationCommandDef)) {
            throw new IllegalUsageError("Contextless command is not applicable to property");
        }
        final RadPresentationCommandDef commandDef = (RadPresentationCommandDef) def;
        if (!commandDef.isApplicableForProperty(propertyId)) {
            throw new IllegalUsageError("This command is not applicable to property #" + propertyId.toString());
        }
        if (disabledForProperties.contains(propertyId)) {
            disabledForProperties.remove(propertyId);
            if (synchronizedCommand != null) {
                synchronizedCommand.disabledForProperties.remove(propertyId);
            }
            if (isEnabledForProperty(propertyId)) {
                refreshWidgets();                
            }
        }
    }

    public final void disableForProperty(final Id propertyId) {//RADIX-1393
        if (!disabledForProperties.contains(propertyId)) {
            final boolean wasEnabled = isEnabledForProperty(propertyId);
            disabledForProperties.add(propertyId);
            if (synchronizedCommand != null) {
                synchronizedCommand.disabledForProperties.add(propertyId);
            }
            if (wasEnabled) {
                refreshWidgets();
            }
        }
    }

    public boolean isEnabledForProperty(final Id propertyId) {//RADIX-1393
        if (def instanceof RadPresentationCommandDef) {
            final RadPresentationCommandDef commandDef =
                    (RadPresentationCommandDef) def;
            return isEnabled() && commandDef.isApplicableForProperty(propertyId)
                    && !disabledForProperties.contains(propertyId);

        }
        return false;
    }
        
    public final void setVisibleForProperty(final Id propertyId, final boolean isVisible){
        final boolean currentVisibility = isVisibleForProperty(propertyId);
        final boolean isVisibleForProperty = !invisibleForProperties.contains(propertyId);
        if (isVisible && !isVisibleForProperty){
            invisibleForProperties.remove(propertyId);
            if (synchronizedCommand != null){
                synchronizedCommand.invisibleForProperties.remove(propertyId);
            }
            if (!currentVisibility){
                refreshWidgets();
            }
        }else if (!isVisible && isVisibleForProperty){
            invisibleForProperties.add(propertyId);
            if (synchronizedCommand != null){
                synchronizedCommand.invisibleForProperties.add(propertyId);
            }
            if (currentVisibility){
                refreshWidgets();
            }            
        }
    };
    
    private void refreshWidgets(){
        afterModify();
        if (synchronizedCommand!=null){
            synchronizedCommand.afterModify();
        }        
    }
    
    public boolean isVisibleForProperty(final Id propertyId){
        if (def instanceof RadPresentationCommandDef){
            final RadPresentationCommandDef commandDef = (RadPresentationCommandDef) def;
            return isVisible() && commandDef.isApplicableForProperty(propertyId) &&
                    !invisibleForProperties.contains(propertyId);
        }
        return false;
    }

    public final void setServerEnabled(final boolean flag) {
        if (serverEnabled == flag) {
            return;
        }
        serverEnabled = flag;
        afterModify();
        if (synchronizedCommand != null) {
            synchronizedCommand.serverEnabled = flag;
            synchronizedCommand.afterModify();
        }        
    }

    public boolean isVisible() {
        return visible;
    }

    public final void setVisible(final boolean visible) {
        if (this.visible == visible) {
            return;
        }
        this.visible = visible;
        afterModify();
        if (synchronizedCommand != null) {
            synchronizedCommand.visible = visible;
            synchronizedCommand.afterModify();
        }
    }

    public ICommandPushButton createPushButton() {
        return getEnvironment().getApplication().getWidgetFactory().newCommandPushButton(this);
    }

    public ICommandToolButton createToolButton() {
        return getEnvironment().getApplication().getWidgetFactory().newCommandToolButton(this);
    }
    
    public ICommandToolButton createToolButton(final Id propertyId){
        return getEnvironment().getApplication().getWidgetFactory().newCommandToolButton(this, owner.getProperty(propertyId));
    }

    public void processException(final Throwable ex) {
        owner.showException(getEnvironment().getMessageProvider().translate("Command", "Can't Execute Command"), ex);
    }
    
    public final void setRereadOwnerAfterExecute(final boolean reread){
        rereadAfterExecute = reread;
    }
    
    public boolean needToRereadOwnerAfterExecute(){
        return rereadAfterExecute;
    }
}