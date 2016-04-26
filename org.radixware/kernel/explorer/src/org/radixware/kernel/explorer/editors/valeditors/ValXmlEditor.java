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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.explorer.dialogs.XmlEditorDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.common.client.utils.ClientValueFormatter;


public class ValXmlEditor extends ValEditor<XmlObject> {           

    private QToolButton editBtn;    
    private Id schemaId;
    private boolean canCreateNew;
    private Class<?> valClass;
    private String dialogTitle, schemaStr;
    private XmlEditorDialog xmlEditorDialog;

    public ValXmlEditor(final IClientEnvironment environment, final QWidget parent, final boolean mandatory,
            final boolean readOnly, final boolean showEditButton) {
        super(environment, parent, new EditMaskNone(), mandatory, readOnly);        
        {
            if (showEditButton) {
                initEditBtn();
            } else {
                editBtn = null;
            }
        }
        getLineEdit().setReadOnly(true);
    }

    public ValXmlEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent,true,false,true);
    }

    public void setValClass(final Class<?> xmlValClass) {
        valClass = xmlValClass;
        if (valClass != null) {
            try {
                final XmlObject xml = XmlObjectProcessor.createNewInstance((ClassLoader) getEnvironment().getApplication().getDefManager().getClassLoader(), valClass);
                canCreateNew = xml.schemaType().isDocumentType();
            } catch (AppError error) {
                final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Invalid Xml-class '%s': %s\n%s");
                final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), error);
                final String stack = ClientException.exceptionStackToString(error);
                getEnvironment().getTracer().error(String.format(message, valClass.getName(), reason, stack));
                canCreateNew = false;
            }
        } else {
            canCreateNew = false;
        }
        refresh();
    }

    public void setSchemaId(final Id schemaId) {
        this.schemaId = schemaId;
    }

    public Id getSchemaId() {
        return schemaId;
    }

    public void setSchemaStr(final String schemaAsStr) {
        schemaStr = schemaAsStr;
        refresh();
    }

    public String getSchemaStr() {
        return schemaStr;
    }

    public String getEditorDialogTitle() {
        return dialogTitle;
    }

    public void setEditorDialogTitle(final String title) {
        dialogTitle = title;
    }

    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    protected boolean eq(final XmlObject value1, final XmlObject value2) {
        if (value1 == null) {
            return value2 == null;
        }
        return value2 != null && (value1 == value2 || value1.xmlText().equals(value2.xmlText()));
    }
    
    private XmlEditorDialog getXmlEditorDialog(){
        if (xmlEditorDialog==null){
            xmlEditorDialog = new XmlEditorDialog(getEnvironment(),this);
            xmlEditorDialog.accepted.connect(this, "onAccept()");
        }                
        return xmlEditorDialog;
    }

    public void openEditor() {
        final XmlObject xml;
        if (dialogTitle != null) {
            getXmlEditorDialog().setWindowTitle(ClientValueFormatter.capitalizeIfNecessary(getEnvironment(),dialogTitle));
        }
        if (schemaStr != null && !schemaStr.isEmpty()) {
            xml = getXmlEditorDialog().edit(getValue(), schemaStr, isReadOnly());
        } else if (getValue() != null) {
            xml = getXmlEditorDialog().edit(getValue(), schemaId, isReadOnly());
        } else if (canCreateNew) {
            xml = getXmlEditorDialog().create(valClass, schemaId);
        } else {
            xml = null;
        }
        if (xml != null) {
            setValue(xml);
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        final XmlObject xml = getValue();        
        if (xml!=null){//RADIX-7032
            getLineEdit().setText(getEnvironment().getMessageProvider().translate("Value", "<value defined>"));
        }
        if (editBtn != null) {
            editBtn.setVisible(isEditButtonVisible());
            if (isReadOnly()) {
                if (xml != null) {
                    editBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "View Value"));
                    editBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.ValueModification.VIEW));
                }
            } else {
                editBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Edit Value"));
                final ClientIcon buttonIcon = 
                    xml==null ? ClientIcon.ValueModification.CREATE  : ClientIcon.ValueModification.EDIT;
                editBtn.setIcon(ExplorerIcon.getQIcon(buttonIcon));
            }
        }
    }
    
    private boolean isEditButtonVisible(){
        final XmlObject xml = getValue();
        final boolean canParse;
        if (schemaStr != null) {
            canParse = true;
        } else {
            canParse = xml == null || xml.schemaType() == null ? canCreateNew : xml.schemaType().isDocumentType();
        }
        return canParse && (!isReadOnly() || xml != null);        
    }
    
    public void setEditButtonVisible(final boolean isVisible){
        if (editBtn==null){
            if (isVisible){
                initEditBtn();
                refresh();
            }
        }else{
            editBtn.setVisible(isEditButtonVisible() && isVisible);
        }
    }
    
    private void initEditBtn(){
        final QAction action = new QAction(this);
        action.triggered.connect(this, "openEditor()");                
        editBtn = addButton(null, action);
        getLayout().setAlignment(editBtn, new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter));
        getLayout().setStretchFactor(editBtn, 0);
        editBtn.setObjectName("btnXmlEditor");        
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        refresh();
    }

    @SuppressWarnings("unused")
    private void onAccept() {
        editingFinished.emit(getValue());
    }
    
    @Override
    public void setPredefinedValues(final List<XmlObject> predefValues) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public List<XmlObject> getPredefinedValues() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }
    
    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    protected boolean isButtonCanChangeValue(final QAbstractButton button) {
        return super.isButtonCanChangeValue(button) && (button!=editBtn || !isReadOnly());
    }        
}
