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

import com.trolltech.qt.QFlags;
import com.trolltech.qt.QtEnumerator;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class QFlagsPropertyEditor extends ValEditor<String> implements IPropertyEditor {

    private QtEnumerator[] flags;
    private SelectFlagsDialog selectFlagsDlg = null;
    private WidgetProperty currentWidgetProp;
    private final QToolButton chooseBtn;
    private LinkedList<String> currSelectedFlags;
    private final ValueListenerController valListenerController = new ValueListenerController();

    public QFlagsPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskStr(), true, true);
        chooseBtn = new QToolButton(parent);
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
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setPropertyValue(WidgetProperty property) {
        this.currentWidgetProp = property;
            valListenerController.notifyListener(property.getValue());

        Class persistentClass = (Class) ((ParameterizedType) ((QFlags) property.getValue()).getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        currSelectedFlags = new LinkedList<>();
        this.flags = (QtEnumerator[]) persistentClass.getEnumConstants();
        for (Object flag : persistentClass.getEnumConstants()) {
            if (((QFlags) property.getValue()).isSet((QtEnumerator) flag)) {
                currSelectedFlags.add(flag.toString());
            }
            this.setValue(flag.toString());
        }
        setStringValue();
        return true;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @SuppressWarnings("unused")
    private void chooseActionSlot() {
        selectFlagsDlg = new SelectFlagsDialog(super.getEnvironment(), this, currentWidgetProp.getName(), flags);
        selectFlagsDlg.setAutoSize(true);
        selectFlagsDlg.setWindowTitle(currentWidgetProp.getName());
        selectFlagsDlg.show();
        selectFlagsDlg.accepted.connect(this, "acceptedFlagsDialogSlot()");
    }

    @SuppressWarnings("unused")
    private void acceptedFlagsDialogSlot() {
        ArrayList<Object> selectedFlags = selectFlagsDlg.getFlags();
        currSelectedFlags = new LinkedList<>();
        if (!selectedFlags.isEmpty()) {
            try {
                Object arr = java.lang.reflect.Array.newInstance(selectedFlags.get(0).getClass(), selectedFlags.size());
                for (int i = 0; i < selectedFlags.size(); i++) {
                    java.lang.reflect.Array.set(arr, i, selectedFlags.get(i));
                }
                Constructor<?>[] constructArray = currentWidgetProp.getValue().getClass().getConstructors();
                for (Constructor<?> constructor : constructArray) {
                    if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].equals(java.lang.reflect.Array.newInstance(selectedFlags.get(0).getClass(), 0).getClass()) && constructor.getParameterTypes()[0].isArray()) {
                        this.setValue(null);
                        for (Object flag : selectedFlags) {
                            currSelectedFlags.add(flag.toString());
                        }
                        valListenerController.notifyListener(constructor.newInstance(arr));
                        setStringValue();
                    }
                }
            } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
                super.getEnvironment().getTracer().error(ex);
            }
        }
    }

    private void setStringValue() {
        StringBuilder sb = new StringBuilder();
        for (String flag : currSelectedFlags) {
            if (sb.toString().isEmpty()) {
                sb.append(flag);
            } else {
                sb.append(", ").append(flag);
            }
        }
        this.setValue(sb.toString());
    }

    class SelectFlagsDialog extends ExplorerDialog {

        private final ArrayList<Object> checkedFlags = new ArrayList<>();
        private final QGroupBox checkBoxGroup = new QGroupBox(this);

        SelectFlagsDialog(final IClientEnvironment environment, final QWidget parent, final String dlgName, final QtEnumerator[] flags) {
            super(environment, parent, dlgName);
            QVBoxLayout checkBoxLayout = new QVBoxLayout();
            checkBoxGroup.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            for (Object flag : flags) {
                QCheckBox checkBox = new QCheckBox(this);
                checkBox.setText(flag.toString());
                checkBox.setProperty("flag", flag);
                for (String currFlag : currSelectedFlags) {
                    if (checkBox.text().equals(currFlag)) {
                        checkBox.setCheckState(Qt.CheckState.Checked);
                    }
                }
                checkBoxLayout.addWidget(checkBox);
            }

            checkBoxGroup.setLayout(checkBoxLayout);
            this.dialogLayout().addWidget(checkBoxGroup);
            this.dialogLayout().addSpacerItem(new QSpacerItem(20, 40, QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Expanding));
            addButton(EDialogButtonType.OK);
            addButton(EDialogButtonType.CANCEL);
            acceptButtonClick.connect(this, "dialogAcceptedSlot()");
            rejectButtonClick.connect(this, "dialogRejectedSlot()");
            this.setWindowModality(Qt.WindowModality.WindowModal);
        }

        @SuppressWarnings(value = "unused")
        private void dialogAcceptedSlot() {
            for (QObject qCheckBox : checkBoxGroup.findChildren(QCheckBox.class)) {
                if (((QAbstractButton) qCheckBox).isChecked()) {
                    this.checkedFlags.add(qCheckBox.property("flag"));
                }
            }
            this.accept();
            this.close();
        }

        @SuppressWarnings(value = "unused")
        private void dialogRejectedSlot() {
            this.reject();
            this.close();
        }

        public ArrayList<Object> getFlags() {
            return checkedFlags;
        }
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        chooseBtn.setDisabled(readOnly);
    }

}
