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
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class QPalettePropertyEditor extends ValEditor<String> implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();
    private QPalette pal;
    private EditPaletteDlg editPaletteDialog = null;

    public QPalettePropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskStr(), true, true);
        QToolButton chooseBtn = new QToolButton(this);
        chooseBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
        chooseBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonIconOnly);
        chooseBtn.clicked.connect(this, "chooseActionSlot()");
        this.addButton(chooseBtn);
        this.setMinimumHeight(20);
        this.setValue("");
    }

    @Override
    public void addValueListener(ValueListener listener) {
        valListenerController.add(listener);
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        valListenerController.notifyListener(property.getValue());
        this.pal = (QPalette) property.getValue();
        this.setValue(pal.toString());
        return true;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @SuppressWarnings("unused")
    private void chooseActionSlot() {
        editPaletteDialog = new EditPaletteDlg(pal, super.getEnvironment(), this, "Edit palette");
        editPaletteDialog.setWindowModality(Qt.WindowModality.WindowModal);
        editPaletteDialog.show();
        editPaletteDialog.accepted.connect(this, "editPaletteDialogSlot()");
    }

    @SuppressWarnings("unused")
    private void editPaletteDialogSlot() {
        valListenerController.notifyListener(editPaletteDialog.getQPalette());
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        if (editPaletteDialog != null) {
            editPaletteDialog.setDisabled(readOnly);
        }
    }

}
