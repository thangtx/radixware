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

package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QToolButton;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class PropObjectEditor extends PropReferenceEditor {    
    
    protected final QToolButton deleteObjectButton;
    protected final QToolButton copyObjectButton;
    protected final QToolButton pasteObjectButton;    
    
    private final Clipboard.ChangeListener clipboardChangeListener = new Clipboard.ChangeListener(){
	  @Override
	  public void stateChanged() {              
                updatePasteObjectButton();
	  }        
    };

    public PropObjectEditor(final Property property) {
        super(property);
        deleteObjectButton = initButton("onDeleteObjectClick()",
                null, //text on button
                getEnvironment().getMessageProvider().translate("PropertyEditor", "Delete Object"),
                ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE),
                "tbDeleteObject");
        
        copyObjectButton = initButton("onCopyObjectClick()",
                null, //text on button
                getEnvironment().getMessageProvider().translate("PropertyEditor", "Copy Object"),
                ExplorerIcon.getQIcon(ClientIcon.CommonOperations.COPY),
                "tbCopyObject");
        
        pasteObjectButton = initButton("onPasteObjectClick()",
                null, //text on button
                getEnvironment().getMessageProvider().translate("PropertyEditor", "Paste Object"),
                ExplorerIcon.getQIcon(ClientIcon.CommonOperations.PASTE),
                "tbPasteObject");

        changeReferenceButton.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Create Object"));
        changeReferenceButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE));        
        //setProperty method does not call in constructor
        getEnvironment().getClipboard().addChangeListener(clipboardChangeListener, property.getOwner());
    }

    @Override
    public void setProperty(final Property property) {
        super.setProperty(property);
        if (property==null){
            getEnvironment().getClipboard().removeChangeListener(clipboardChangeListener);
        }else{
            getEnvironment().getClipboard().addChangeListener(clipboardChangeListener, property.getOwner());
        }        
    }        

    @Override
    public void refresh(final ModelItem changedItem) {
        if (getProperty()!=null){
            super.refresh(changedItem);
            final PropertyObject property = (PropertyObject) getProperty();
            if (property.getValueObject() == null) {                
                changeReferenceButton.setVisible(!isReadonly() && !controller.isInheritedValue() && property.canCreate());
                deleteObjectButton.setVisible(false);
                copyObjectButton.setVisible(false);
            } else { 
                changeReferenceButton.setVisible(false);
                deleteObjectButton.setVisible(!isReadonly() && property.canDelete());
                copyObjectButton.setVisible(!isReadonly() && property.canCopy());
            }
            updatePasteObjectButton();
        }
    }
    
    private void updatePasteObjectButton(){       
        final PropertyObject property = (PropertyObject) getProperty();
        final Clipboard clipboard = getEnvironment().getClipboard();
        final boolean isVisible = property.getValueObject()==null 
                                  && !isReadonly() 
                                  && property.canCreate()
                                  && clipboard.isCompatibleWith(property);
        pasteObjectButton.setVisible(isVisible);
    }
    

    @SuppressWarnings("unused")
    private void onPasteObjectClick(){
        final PropertyObject property = (PropertyObject) getProperty();
        try {
            property.create(getEnvironment().getClipboard().iterator().next());
        } catch (InterruptedException ex) {
        } catch (Exception ex) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to create object for \'%s\': \n%s");
            final String message = String.format(msg, property.getTitle(), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex));
            processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Creating Object"), message);
        }
        getValEditor().getLineEdit().setText(property.getValueAsString());
        getValEditor().getLineEdit().selectAll();
        this.refresh(getProperty());
    }
    
    @SuppressWarnings("unused")
    private void onCopyObjectClick() {
        final PropertyObject property = (PropertyObject) getProperty();
        if (property.getValueObject() != null) {
            try {
                final EntityModel entity = property.openEntityModel();  
                //List<Id> presentations=new ArrayList( entity.getClassPresentationDef().getEditorPresentationIds()) ;
                //EntityModel copyEntityModel=EntityModel.openPrepareCreateModel(entity.getEditorPresentationDef(), entity.getClassId(), entity, presentations,  new IContext.ContextlessCreating(getEnvironment()));
                entity.getEnvironment().getClipboard().push(entity);
            } catch (InterruptedException ex) {
            } catch (Exception ex) {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to copy value of \'%s\': \n%s");
                final String message = String.format(msg, property.getTitle(), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex));
                processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Coping Value"), message);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onDeleteObjectClick() {
        final PropertyObject property = (PropertyObject) getProperty();
        try{
            property.delete();
        } catch (InterruptedException ex) {
        } catch (Exception ex) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't remove value for \'%s\': \n%s");
            final String message = String.format(msg, property.getTitle(), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex));
            processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Removing Value"), message);
        }
        if (getProperty()!=null){
            getValEditor().getLineEdit().setText(property.getValueAsString());
            getValEditor().getLineEdit().selectAll();
            this.refresh(getProperty());
        }        
    }

    @Override
     public void onChangeReferenceButtonClick() {
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
                processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Creating Object"), message);
            }
        }
        if (getProperty()!=null){
            getValEditor().getLineEdit().setText(property.getValueAsString());
            getValEditor().getLineEdit().selectAll();        
            this.refresh(getProperty());
        }
    }

    @Override
    protected void updateEditor(Object value, PropEditorOptions options) {
        if (!(value instanceof Reference) || !((Reference) value).isBroken()) {
            options.setMandatory(true);//TWRBS-1624 очищать нельзя, можно только удалить.
        }
        super.updateEditor(value,options);
    }    
    
    @Override
    protected void closeEvent(QCloseEvent event) {
         super.closeEvent(event);
         getEnvironment().getClipboard().removeChangeListener(clipboardChangeListener);	
    }        
    
    @Override
    public void setVisible(boolean bln) {
        final boolean previous = isVisible();
        super.setVisible(bln);
        if (previous!=isVisible()){
            if(isVisible()){
                getEnvironment().getClipboard().addChangeListener(clipboardChangeListener, getProperty().getOwner());
                updatePasteObjectButton();
            }else{
                getEnvironment().getClipboard().removeChangeListener(clipboardChangeListener);
            }
        }
    }
    
    @Override
    public final EWidgetMarker getWidgetMarker() {
        return EWidgetMarker.OBJECT_PROP_EDITOR;
    }
}