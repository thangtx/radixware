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

package org.radixware.wps.views.editor.property;

import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.wps.rwt.ToolButton;


public class PropObjectEditor extends PropReferenceEditor {

    private ToolButton deleteObjectButton;
    protected final ToolButton copyObjectButton;
    protected final ToolButton pasteObjectButton;
    
    private final Clipboard.ChangeListener clipboardChangeListener = new Clipboard.ChangeListener(){
	  @Override
	  public void stateChanged() {
	      updatePasteObjectButton();
	  }        
    };

    public PropObjectEditor(PropertyObject property) {
        super(property);
        getEnvironment().getClipboard().addChangeListener(clipboardChangeListener,property.getOwner());
        deleteObjectButton = new ToolButton();
        deleteObjectButton.setObjectName("tbDeleteObject");
        deleteObjectButton.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Delete Object"));
        deleteObjectButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.DELETE));
        deleteObjectButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                deleteObject();
            }
        });
        copyObjectButton = new ToolButton();
        copyObjectButton.setObjectName("tbCopyObject");
        copyObjectButton.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Copy Object"));
        copyObjectButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.COPY));
        copyObjectButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                copyObject();
            }
        });
        pasteObjectButton = new ToolButton();
        pasteObjectButton.setObjectName("tbPasteObject");
        pasteObjectButton.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Paste Object"));
        pasteObjectButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.PASTE));
        pasteObjectButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                pasteObject();
            }
        });
    }

    @Override
    protected void selectEntity() {
        final PropertyObject property = (PropertyObject) getProperty();
        if (property.getValueObject() == null) {
            try {
                property.create();
                //After PropertyObject.create property have its actual value
                //p.setValueObject(new Reference(model.getPid(), model.getTitle()));// commented by yremizov
            } catch (InterruptedException ex) {
            } catch (Exception ex) {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to create object for \'%s\': \n%s");
                final String message = String.format(msg, property.getTitle(), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex));
                controller.processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Creating Object"), message);
            }
        }
        this.refresh(getProperty());
    }

    private void deleteObject() {
        final PropertyObject property = (PropertyObject) getProperty();
        if (property.getValueObject() != null) {
            if (property.getInheritableValue() != null) {
                property.setValueObject(null);
            } else {
                try {
                    final EntityModel entity = property.openEntityModel();
                    if (entity.delete(false)) {
                        final PropertyValue serverValue = new PropertyValue(
                                property.getDefinition(),
                                null,//value
                                false,//isOwn
                                false,//isDefined
                                false//isReadonly
                                );
                        property.setServerValue(serverValue);
                        ((EntityModel) property.getOwner()).afterChangePropertyObject(property);
                    }
                } catch (InterruptedException ex) {
                } catch (Exception ex) {
                    final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't remove value for \'%s\': \n%s");
                    final String message = String.format(msg, property.getTitle(), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex));
                    processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Removing Value"), message);
                }
            }
        }
        this.refresh(getProperty());
    }
    
    @SuppressWarnings("unused")
    private void pasteObject(){
        final PropertyObject property = (PropertyObject) getProperty();
        try {
            property.create(getEnvironment().getClipboard().iterator().next());
        } catch (InterruptedException ex) {
        } catch (Exception ex) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to create object for \'%s\': \n%s");
            final String message = String.format(msg, property.getTitle(), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex));
            processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Creating Object"), message);
        }
        //getValEditor().getLineEdit().setText(property.getValueAsString());
        //getValEditor().getLineEdit().selectAll();
        this.refresh(getProperty());
    }
    
    @SuppressWarnings("unused")
    private void copyObject() {
        final PropertyObject property = (PropertyObject) getProperty();
        if (property.getValueObject() != null) {
            try {
                final EntityModel entity = property.openEntityModel();                
                entity.getEnvironment().getClipboard().push(entity);
            } catch (InterruptedException ex) {
            } catch (Exception ex) {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to copy value of \'%s\': \n%s");
                final String message = String.format(msg, property.getTitle(), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex));
                processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Coping Value"), message);
            }
        }
    }

    @Override
    public void refresh(ModelItem item) {
        super.refresh(item);
        final PropertyObject property = (PropertyObject) getProperty();
        if (property.getValueObject() == null) {
            deleteObjectButton.setVisible(false);
            copyObjectButton.setVisible(false);
        } else {
            deleteObjectButton.setVisible(!controller.isReadonly() && property.canDelete());
            copyObjectButton.setVisible(!controller.isReadonly()
                    && ((EntityModel) property.getOwner()).isExists()
                    && !property.getVal().isBroken());
        }
        updatePasteObjectButton();
    }
    
    private void updatePasteObjectButton(){
        final PropertyObject property = (PropertyObject) getProperty();
        final Clipboard clipboard = getEnvironment().getClipboard();
        final boolean isVisible = property.getValueObject()==null 
                                  && !controller.isReadonly() 
                                  && property.canCreate()
                                  && clipboard.isCompatibleWith(property);
        pasteObjectButton.setVisible(isVisible);
    }

    @Override
    protected boolean isSelectButtonVisible() {
        final PropertyObject property = (PropertyObject) getProperty();
        if (property.getValueObject() == null) {
            return !controller.isReadonly() && property.canCreate();
        } else {
            return false;
        }
    }

    @Override
    protected String getSelectButtonToolTip() {
        return getEnvironment().getMessageProvider().translate("PropertyEditor", "Create Object");
    }

    @Override
    protected ClientIcon getSelectButtonIcon() {
        return ClientIcon.CommonOperations.CREATE;
    }

    @Override
    protected void closeEditor() {
        getEnvironment().getClipboard().removeChangeListener(clipboardChangeListener);
        super.closeEditor();
    }       
}
