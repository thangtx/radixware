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

package org.radixware.wps.views.editor.xml.view;

import java.util.List;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.editors.xmleditor.view.IRenameDialog;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;


public class RenameDialog extends Dialog implements IRenameDialog {

    private InputBox<String> editor;
    private final List<QName> namesAttr;
    private final List<QName> allowedNames;
    private ValListEditorController<Long> listEditorController;

    public RenameDialog(final IClientEnvironment environment, final List<QName> allowedNames, final List<QName> restrictedNames) {
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Rename Item"));
        namesAttr = restrictedNames;
        this.allowedNames = allowedNames;
        if (allowedNames == null) {
            editor = new InputBox<>();
            editor.setLabelVisible(true);
            editor.setLabel(environment.getMessageProvider().translate("XmlEditor", "Element name:"));
            add(editor);
            editor.setValueController(new InputBox.ValueController<String>() {
                @Override
                public String getValue(String text) throws InputBox.InvalidStringValueException {
                    return text;
                }
            });
        } else {
            EditMaskList editMask = new EditMaskList();
            listEditorController = new ValListEditorController<>(environment, editMask);
            final UIObject comboBox = (UIObject) listEditorController.getValEditor();
            for (int i = 0; i < allowedNames.size(); i++) {
                editMask.addItem(allowedNames.get(i).getLocalPart(), Long.valueOf(i));
            }
            listEditorController.setEditMask(editMask);
            add(comboBox);
        }
        addCloseAction(EDialogButtonType.OK);
        addCloseAction(EDialogButtonType.CANCEL);
        setWidth(300);
        setHeight(110);
        setMinimumHeight(110);
        setMaxHeight(110);
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            final String title = getEnvironment().getMessageProvider().translate("XmlEditor", "Wrong Name");
            final String message1 = getEnvironment().getMessageProvider().translate("XmlEditor", "Input correctly name");
            final String message2 = getEnvironment().getMessageProvider().translate("XmlEditor", "Attribute with such name already exist! Please, rename this element.");
            final String message3 = getEnvironment().getMessageProvider().translate("XmlEditor", "Choose name");
            if (editor != null) {
                if (editor.getValue() == null || !editor.getValue().matches("[_a-zA-Z][_a-zA-Z0-9.-]*")) {
                    getEnvironment().messageError(title, message1);
                    return null;
                } else if (namesAttr.contains(new QName(editor.getValue()))) {
                    getEnvironment().messageError(title, message2);
                    return null;
                } else {
                    return super.onClose(action, actionResult);
                }
            } else if (listEditorController != null && listEditorController.getValue() != null) {
                return super.onClose(action, actionResult);
            } else {
                getEnvironment().messageError(title, message3);
                return null;
            }
        } else {
            return super.onClose(action, actionResult);
        }

    }

    @Override
    public QName getNewName() {
        if (editor != null) {
            return new QName(editor.getValue());
        } else {
            final Long index = listEditorController.getValue();
            if (index == null || index < 0 || index >= allowedNames.size()) {
                return null;
            } else {
                return allowedNames.get(index.intValue());
            }
        }
    }
}
