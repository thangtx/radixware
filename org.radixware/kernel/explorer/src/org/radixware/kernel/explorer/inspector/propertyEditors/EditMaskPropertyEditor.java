/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector.propertyEditors;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.editmask.consteditor.ConstSetEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.listeditor.ListEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class EditMaskPropertyEditor extends ValEditor<String> implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();
    EditMask editMask;
    EditMaskDlg editMaskDialog;
    IClientEnvironment environment;

    public EditMaskPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskStr(), true, true);
        this.environment = environment;
        QToolButton chooseBtn = new QToolButton(this);
        chooseBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
        chooseBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonIconOnly);
        chooseBtn.clicked.connect(this, "chooseActionSlot()");
        this.addButton(chooseBtn);
        this.setMinimumHeight(20);
    }

    @Override
    public void addValueListener(ValueListener listener) {
        valListenerController.add(listener);
    }

    @Override
    public void registerChildPropEditor(WidgetProperty parentWdgtProperty, WidgetProperty childWdgtProperty, IPropertyEditor childPropEditor) {
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        this.editMask = (EditMask) property.getValue();
        if (editMask instanceof EditMaskNone) {
            this.setEnabled(false);
        }
        this.setValue(editMask.toString());
        return true;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @SuppressWarnings("unused")
    private void chooseActionSlot() {
        IEditMaskEditor editMaskEditor = null;
        EEditMaskType editMaskType = this.editMask.getType();
        if (editMaskType == EEditMaskType.LIST) {
            editMaskEditor = new ListEditMaskEditorWidget(environment, this, EValType.STR); 
        } else if (editMaskType == EEditMaskType.ENUM) {
            Id enumId =  ((EditMaskConstSet)this.editMask).getEnumId();
            RadEnumPresentationDef radEnumPresentationDef = getEnvironment().getDefManager().getEnumPresentationDef(enumId);
            editMaskEditor = new ConstSetEditMaskEditorWidget(environment, this, radEnumPresentationDef); 
        }//ValConstEditor
        else {
            editMaskEditor = environment.getApplication().getEditMaskEditorFactory().newEditMaskEditor(environment, this, this.editMask.getType());
        }
        
        editMaskEditor.setEditMask(this.editMask);
        editMaskDialog = new EditMaskDlg(super.getEnvironment(), this, editMaskEditor);
        editMaskDialog.setWindowTitle(environment.getMessageProvider().translate("EditMask", "Edit Input Mask"));
        int result = editMaskDialog.exec();
        if (result == QDialog.DialogCode.Accepted.value()) {
            EditMask acceptedEditMask = editMaskDialog.getEditMask();
            valListenerController.notifyListener(acceptedEditMask);
        }
    }
}
