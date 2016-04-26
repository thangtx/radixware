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

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QRegExpValidator;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.view.IRenameDialog;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;


public class XmlRenameItem extends ExplorerDialog implements IRenameDialog {

    private QLineEdit reName;
    private final List<QName> namesAttr;
    private QComboBox allowedNames;
    Map<String, String> namesSpaceByURI = new HashMap<>();

    public XmlRenameItem(final IClientEnvironment environment,
            final QWidget parent,
            final List<QName> possibleNames,
            final List<QName> restrictedNames) {
        super(environment, parent);
        namesAttr = restrictedNames;
        QFormLayout formLayout = new QFormLayout();
        if (possibleNames == null) {
            reName = new QLineEdit(this);
            reName.textEdited.connect(this, "reName()");
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Element name:"), reName);
            QRegExp re = new QRegExp("[_a-zA-Z][_a-zA-Z0-9.-]*");
            QRegExpValidator v = new QRegExpValidator(re, null);
            reName.setValidator(v);
        } else {
            allowedNames = new QComboBox(this);
            for (QName name : possibleNames) {
                allowedNames.addItem(name.getLocalPart());
                if (name.getNamespaceURI() != null && !name.getNamespaceURI().isEmpty()) {
                    namesSpaceByURI.put(name.getLocalPart(), name.getNamespaceURI());
                }
            }
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Element name:"), allowedNames);
        }
        dialogLayout().addLayout(formLayout);
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Rename Item"));
        addButton(EDialogButtonType.CANCEL);
        if (possibleNames == null) {
            acceptButtonClick.connect(this, "checkNameAttr()");
            addButton(EDialogButtonType.OK).setEnabled(false);
        } else {
            acceptButtonClick.connect(this, "accept()");
            addButton(EDialogButtonType.OK).setEnabled(true);
        }
        rejectButtonClick.connect(this, "reject()");
    }

    @SuppressWarnings("unused")
    private void reName() {
        if (reName.text() == null || reName.text().isEmpty() || !reName.hasAcceptableInput() ) {
            getButton(EDialogButtonType.OK).setEnabled(false);
        } else {
            getButton(EDialogButtonType.OK).setEnabled(true);
        }
    }

    public void checkNameAttr() {
        if (namesAttr.contains(new QName(reName.text()))) {
            final String title = getEnvironment().getMessageProvider().translate("XmlEditor", "Wrong Name");
            final String message = getEnvironment().getMessageProvider().translate("XmlEditor", "Attribute with such name already exist! Please, rename this element.");
            getEnvironment().messageInformation(title, message);
            reName.setFocus();
        } else {
            accept();
        }
    }

    @Override
    public QName getNewName() {
        if (reName != null) {
            return new QName(reName.text());
        } else {
            if (!namesSpaceByURI.isEmpty() && namesSpaceByURI.containsKey(allowedNames.currentText())) {
                return new QName(namesSpaceByURI.get(allowedNames.currentText()), allowedNames.currentText());
            } else {
                return new QName(allowedNames.currentText());
            }
        }
    }
}
