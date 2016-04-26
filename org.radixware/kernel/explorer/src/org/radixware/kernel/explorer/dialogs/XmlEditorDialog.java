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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.gui.QCloseEvent;
import org.apache.xmlbeans.XmlObject;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.explorer.editors.xml.XmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.exceptions.CancelXmlDocumentCreationException;
import org.radixware.kernel.explorer.exceptions.XmlEditorException;

public class XmlEditorDialog extends ExplorerDialog {

    private final XmlEditor editor;

    public XmlEditorDialog(final IClientEnvironment environment, final QWidget parent) {
        this(environment, parent, Application.translate("XmlEditor", "Xml Editor"));
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public XmlEditorDialog(final IClientEnvironment environment, final QWidget parent, final String windowTitle) {
        super(environment, parent, null);
        editor = new XmlEditor(environment);
        editor.setParent(this);
        setupUi();
        setWindowTitle(windowTitle);
    }

    private void setupUi() {
        layout().addWidget(editor);
        editor.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        addButton(EDialogButtonType.OK);
        addButton(EDialogButtonType.CANCEL);
        addButton(EDialogButtonType.CLOSE);
        acceptButtonClick.connect(this, "onAccept()");
        rejectButtonClick.connect(this, "reject()");
        setMaximumSize(QApplication.desktop().size());
        resize(750, 550);
    }

    public XmlObject edit(final XmlObject document, final boolean readonly) {
        return edit(document, (Id) null, readonly);
    }

    public XmlObject edit(final XmlObject document, final Id schemaId, final boolean readonly) {        
        setupDialogButtons(readonly);
        try {
            editor.edit(document, readonly, schemaId);
        } catch (CancelXmlDocumentCreationException exception) {
            return null;
        } catch (XmlEditorException exception) {
            getEnvironment().messageError(exception.getTitle(getEnvironment().getMessageProvider()), exception.getLocalizedMessage(getEnvironment().getMessageProvider()));
            return null;
        }
        setVisible(true);
        editor.setVisible(true);
        if (exec() == DialogCode.Accepted.value()) {
            return getResult();
        }
        return null;
    }

    public XmlObject edit(final XmlObject document, final String schemaDocStr, final boolean readonly) {
        setupDialogButtons(readonly);
        try {
            editor.edit(document, schemaDocStr, readonly);
        } catch (CancelXmlDocumentCreationException exception) {
            return null;
        } catch (XmlEditorException exception) {
            Application.messageError(exception.getTitle(getEnvironment().getMessageProvider()), exception.getLocalizedMessage(getEnvironment().getMessageProvider()));
            return null;
        }
        setVisible(true);
        editor.setVisible(true);
        if (exec() == DialogCode.Accepted.value()) {
            return getResult();
        }
        return null;
    }

    public XmlObject create(final Class<?> documentClass, final Id schemaId) {
        setupDialogButtons(false);
        final XmlObject newObject = XmlObjectProcessor.createNewInstance((ClassLoader) getEnvironment().getApplication().getDefManager().getClassLoader(), documentClass);//NOPMD
        try {
            editor.createDoc(newObject, schemaId);
        } catch (CancelXmlDocumentCreationException exception) {
            return null;
        } catch (XmlEditorException exception) {
            Application.messageError(exception.getTitle(getEnvironment().getMessageProvider()), exception.getLocalizedMessage(getEnvironment().getMessageProvider()));
            return null;
        }
        setVisible(true);
        editor.setVisible(true);
        if (exec() == DialogCode.Accepted.value()) {
            return getResult();
        }
        return null;
    }
    
    private void setupDialogButtons(final boolean readonly){
        getButton(EDialogButtonType.OK).setVisible(!readonly);
        getButton(EDialogButtonType.CANCEL).setVisible(!readonly);
        getButton(EDialogButtonType.CLOSE).setVisible(readonly);        
    }

    @SuppressWarnings("unused")
    private void onAccept() {
        final String confirmTitle = Application.translate("XmlEditor", "Confirm Changes");
        final String confirmMessage = Application.translate("XmlEditor", "This document contains errors.\nDo you want to continue?");
        final XmlObject result = editor.getCurrentDocument();
        if (result != null) {
            if (XmlObjectProcessor.validate(result) == null || Application.messageConfirmation(confirmTitle, confirmMessage)) {
                accept();
            } else {
                editor.validate();
            }
        }

    }

    private XmlObject getResult() {
        return editor.getCurrentDocument();
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        editor.close();
        super.closeEvent(event);
    }
}
