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

package org.radixware.wps.dialogs;

import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlEditorDialog;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.StandardXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.XmlEditorOperation;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editor.xml.view.XmlEditor;


public class XmlEditorDialog extends Dialog implements IXmlEditorDialog {

    private final XmlEditor editor;

    public XmlEditorDialog(final WpsEnvironment env, final String xmlSchemaAsStr, final boolean isReadOnly) {
        super(env, "", false);
        final IXmlSchemaProvider provider = new StandardXmlSchemaProvider(env);
        editor = new XmlEditor(env, xmlSchemaAsStr, provider, null, isReadOnly);
        setupUi(isReadOnly);
    }

    public XmlEditorDialog(final WpsEnvironment env, final SchemaTypeSystem schemaTypeSystem, final boolean isReadOnly) {
        super(env, "", false);
        final IXmlSchemaProvider provider = new StandardXmlSchemaProvider(env);
        editor = new XmlEditor(env, schemaTypeSystem, provider, null, isReadOnly);
        setupUi(isReadOnly);
    }

    private void setupUi(final boolean readOnly) {
        add(editor);
        editor.setTop(1);
        editor.setLeft(1);
        editor.getAnchors().setBottom(new UIObject.Anchors.Anchor(1, -1));
        editor.getAnchors().setRight(new UIObject.Anchors.Anchor(1, -1));
        editor.setReadOnly(readOnly);
        editor.setFocused(true);
        updateDialogButtons(readOnly);
        setMinimumWidth(600);
        setMinimumHeight(350);
        editor.getHtml().setCss("min-width", null);
        editor.getHtml().setCss("min-height", null);
    }

    private void updateDialogButtons(final boolean readOnly) {
        clearCloseActions();
        if (readOnly) {
            addCloseAction(EDialogButtonType.CLOSE).setDefault(true);
        } else {
            addCloseAction(EDialogButtonType.OK).setDefault(true);
            addCloseAction(EDialogButtonType.CANCEL);
        }
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
    public boolean isReadOnly() {
        return editor.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        editor.setReadOnly(readOnly);
        updateDialogButtons(readOnly);
    }

    @Override
    public void setDisabledOperations(final EnumSet<XmlEditorOperation> operations) {
        editor.setDisabledOperations(operations);
    }

    @Override
    public boolean isOperationDisabled(XmlEditorOperation operation) {
        return editor.isOperationDisabled(operation);
    }
        

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED && editor.valueWasModified() && !isReadOnly()) {
            final List<XmlError> validate = editor.validate(false);
            if (validate != null && !validate.isEmpty()
                    && !getEnvironment().messageConfirmation(getEnvironment().getMessageProvider().translate("XmlEditor", "Confirm Changes"), getEnvironment().getMessageProvider().translate("XmlEditor", "The document contains errors. Do you really want to continue?"))) {
                return null;
            }
        }
        editor.close();
        return super.onClose(action, actionResult);
    }

    @Override
    public boolean close() {
        rejectDialog();
        return true;
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
