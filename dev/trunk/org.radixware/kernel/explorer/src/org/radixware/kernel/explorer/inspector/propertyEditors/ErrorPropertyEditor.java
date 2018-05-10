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
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class ErrorPropertyEditor extends ValStrEditor implements IPropertyEditor {

    private String wdgtPropName;
    private Exception ex;

    public ErrorPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent);
        this.setValue("Error");
        this.getLineEdit().setStyleSheet("color: red");
        final QToolButton chooseBtn = new QToolButton(this);
        chooseBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.TraceLevel.ERROR));
        chooseBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonIconOnly);
        chooseBtn.clicked.connect(this, "errorDialogSlot()");
        this.addButton(chooseBtn);
        this.setMinimumHeight(20);
    }

    @SuppressWarnings("unused")
    private void errorDialogSlot() {
        super.getEnvironment().processException(super.getEnvironment().getMessageProvider().translate("inspector", "Fail to get property ") + wdgtPropName, ex);
    }

    @Override
    public void addValueListener(ValueListener listener) {
//        this class doesn't have listener
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, WidgetProperty childWdgtProperty, IPropertyEditor childPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        this.wdgtPropName = property.getName();
        this.ex = (Exception) property.getValue();
        return true;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }
}
