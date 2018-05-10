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
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class QFontPropertyEditor extends ValEditor<String> implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();
    private QFont font;
    QFontDialog selectFontDlg = null;

    public QFontPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskStr(), true, true);
        QToolButton chooseBtn = new QToolButton(this);
        chooseBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
        chooseBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonIconOnly);
        chooseBtn.clicked.connect(this, "chooseFontSlot()");
        this.addButton(chooseBtn);
        this.setMinimumHeight(20);
    }

    @Override
    public void addValueListener(ValueListener listener) {
            valListenerController.add(listener);
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        valListenerController.notifyListener(property.getValue());
        this.setValue(property.getValue().toString());
        this.font = (QFont) property.getValue();
        return true;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @SuppressWarnings("unused")
    private void chooseFontSlot() {
        if (this.font != null) {
            selectFontDlg = new QFontDialog(this.font, this);
        } else {
            selectFontDlg = new QFontDialog();
        }
        selectFontDlg.show();
        selectFontDlg.fontSelected.connect(this, "fontSelected(QFont)");
    }

    @SuppressWarnings("unused")
    private void fontSelected(QFont qFont) {
        this.setValue(qFont.toString());
        valListenerController.notifyListener(qFont);
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        super.setReadOnly(isReadOnly);
        if (selectFontDlg != null) {
            selectFontDlg.setDisabled(isReadOnly);
        }
    }
}
