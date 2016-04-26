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
package org.radixware.kernel.explorer.editors.xml.new_.view.editors;

import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;

final class DialogForCorrectValue extends ExplorerDialog {

    private final XmlValueEdit editor;

    public DialogForCorrectValue(final IClientEnvironment environment,
            final QWidget parent,
            final XmlModelItem xmlModel,
            final IXmlValueEditingOptionsProvider editingOptionsProvider) {
        super(environment, parent);
        final QFormLayout form = new QFormLayout();
        editor = new XmlValueEdit(environment, xmlModel, editingOptionsProvider, false);
        editor.asWidget().setParent(this);
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Input Correct Value"));
        final String name = xmlModel.getXmlNode().getLocalName();
        form.addRow(name + ":", editor.asWidget());
        dialogLayout().addLayout(form);
        addButtons(EnumSet.of(EDialogButtonType.CANCEL), true);
        addButton(EDialogButtonType.OK).setEnabled(false);
        acceptButtonClick.connect(this, "onAccept()");
        editor.addValueListener(new IXmlValueEditor.ValueListener() {
            @Override
            public void valueChanged(final String newValue) {
                if (newValue == null) {
                    getEnvironment().messageError(getEnvironment().getMessageProvider().translate("XmlEditor", "Empty Value"), getEnvironment().getMessageProvider().translate("XmlEditor", "Input correctly value"));
                }
                getButton(EDialogButtonType.OK).setEnabled(newValue != null);
            }
        });
    }

    @SuppressWarnings("unused")
    private void onAccept() {
        if (getValue() == null && editor.asWidget().getValidationResult() == ValidationResult.ACCEPTABLE) {
            getEnvironment().messageError(getEnvironment().getMessageProvider().translate("XmlEditor", "Wrong Value"), getEnvironment().getMessageProvider().translate("XmlEditor", "Input correctly value"));
            return;
        }
        if (editor.asWidget().getValidationResult() == ValidationResult.ACCEPTABLE) {
            accept();
        }
    }

    public Object getValue() {
        return editor.getValueObject();
    }
}
