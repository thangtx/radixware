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
package org.radixware.kernel.explorer.editors.xml.new_.view;

import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlEditorDialog;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.StandardXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.XmlEditorOperation;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.xml.new_.XmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class XmlEditorDlg extends ExplorerDialog implements IXmlEditorDialog {

    private final XmlEditor editor;
    private final IClientEnvironment environment;

    @SuppressWarnings("LeakingThisInConstructor")
    public XmlEditorDlg(final IClientEnvironment environment, final String xmlSchemaAsStr, final boolean isReadOnly, final QWidget parent) throws InstantiationException, IllegalAccessException {
        super(environment, parent);
        this.environment = environment;
        final IXmlSchemaProvider provider = new StandardXmlSchemaProvider(environment);
        editor = new XmlEditor(environment, xmlSchemaAsStr, provider, null, null, isReadOnly);
        editor.setParent(this);
        dialogLayout().addWidget(editor);
        addButton(EDialogButtonType.OK);
        addButton(EDialogButtonType.CANCEL);
        if (xmlSchemaAsStr != null) {
            acceptButtonClick.connect(this, "onOkButtonClick()");
        } else {
            acceptButtonClick.connect(this, "accept()");
        }
        rejectButtonClick.connect(this, "reject()");
        setMinimumWidth(600);
        setMinimumHeight(350);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.ValueTypes.XML));
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Xml Editor"));
    }

    @SuppressWarnings("unused")
    private void onOkButtonClick() {
        if (editor.valueWasModified() && !isReadOnly()) {
            final List<XmlError> validateList = validate(false);
            if (validateList != null
                    && !validateList.isEmpty()
                    && !environment.messageConfirmation(environment.getMessageProvider().translate("XmlEditor", "Confirm Changes"), environment.getMessageProvider().translate("XmlEditor", "The document contains errors. Do you really want to continue?"))) {
                return;
            }
        }
        accept();
    }

    @Override
    public void setValue(final XmlObject value) {
        editor.setValue(value);
    }

    @Override
    public XmlObject getValue() {
        return editor.getValue();
    }

    @Override
    public void setReadOnly(final boolean isReadOnly) {
        editor.setReadOnly(isReadOnly);
    }

    @Override
    public boolean isReadOnly() {
        return editor.isReadOnly();
    }

    @Override
    public void setDisabledOperations(final EnumSet<XmlEditorOperation> operations) {
        editor.setDisabledOperations(operations);
    }

    @Override
    public boolean isOperationDisabled(final XmlEditorOperation operation) {
        return editor.isOperationDisabled(operation);
    }

    @Override
    public boolean valueWasModified() {
        return editor.valueWasModified();
    }

    @Override
    public List<XmlError> validate(final boolean isVisible) {
        return editor.validate(isVisible);
    }
}
