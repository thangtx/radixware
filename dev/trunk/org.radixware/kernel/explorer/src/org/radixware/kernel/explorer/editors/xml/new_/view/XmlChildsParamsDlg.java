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
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QRegExpValidator;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.ICreateElementDialog;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;


public class XmlChildsParamsDlg extends ExplorerDialog implements ICreateElementDialog {

    private QLineEdit childName;
    private QComboBox allowedNames;
    private final XmlModelItem parentModelItem;
    private IXmlValueEditor<? extends QWidget> childValue;
    private final XmlValueEditorFactory<? extends QWidget> valueEditorFactory;
    private final QFormLayout formLayout = new QFormLayout();
    private final QLabel lbValue = new QLabel(getEnvironment().getMessageProvider().translate("XmlEditor", "Element value:"), this);
    final IXmlValueEditingOptionsProvider editingOptionsProvider;

    public XmlChildsParamsDlg(final IClientEnvironment environment,
            final QWidget parent,
            final XmlValueEditorFactory<? extends QWidget> valueEditorFactory,
            final IXmlValueEditingOptionsProvider editingOptionsProvider,
            final XmlModelItem parentModelItem,
            final List<XmlSchemaElementItem> possibleItems) {
        super(environment, parent);
        this.parentModelItem = parentModelItem;
        this.valueEditorFactory = valueEditorFactory;
        this.editingOptionsProvider = editingOptionsProvider;
        if (possibleItems == null) {
            childName = new QLineEdit(this);
            childName.textEdited.connect(this, "createChildN()");
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Element name:"), childName);
            final QRegExp re = new QRegExp("[_a-zA-Z][_a-zA-Z0-9.-]*");
            final QRegExpValidator v = new QRegExpValidator(re, null);
            childName.setValidator(v);
            childValue = createValueEditor(null, editingOptionsProvider);
            formLayout.addRow(lbValue, childValue.asWidget());
        } else {
            allowedNames = new QComboBox(this);
            for (XmlSchemaElementItem childItem : possibleItems) {
                allowedNames.addItem(childItem.getLocalName(), childItem);
            }
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Element name:"), allowedNames);
            allowedNames.setCurrentIndex(0);
            formLayout.addRow(lbValue, (QWidget) null);
            onChangeSchemaElement();
            allowedNames.currentIndexChanged.connect(this, "onChangeSchemaElement()");
        }
        dialogLayout().addLayout(formLayout);
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Creating Element"));
        if (childName != null) {
            addButton(EDialogButtonType.OK).setEnabled(false);
        } else {
            addButton(EDialogButtonType.OK).setEnabled(true);
        }
        addButton(EDialogButtonType.CANCEL);
        acceptButtonClick.connect(this, "accept()");
        rejectButtonClick.connect(this, "reject()");
    }

    private void createChildN() {        
        if (childName == null || (!childName.text().isEmpty() && childName.hasAcceptableInput())) {
            getButton(EDialogButtonType.OK).setEnabled(true);
        } else {
            getButton(EDialogButtonType.OK).setEnabled(false);
        }
    }

    @Override
    public QName getChildName() {
        if (childName != null) {
            return new QName(childName.text());
        } else {
            final XmlSchemaElementItem childSchemaItem;
            final int currentIndex = allowedNames.currentIndex();
            if (currentIndex >= 0) {
                childSchemaItem =
                        (XmlSchemaElementItem) allowedNames.itemData(currentIndex, Qt.ItemDataRole.UserRole);
            } else {
                childSchemaItem = null;
            }
            if (childSchemaItem == null) {
                return new QName(allowedNames.currentText());
            } else {
                return childSchemaItem.getFullName();
            }
        }
    }
    
    private IXmlValueEditor<? extends QWidget> createValueEditor(final XmlSchemaElementItem elemItem, final IXmlValueEditingOptionsProvider editingOptionsProvider) {
        final XmlModelItem modelItem = new XmlModelItem(null, elemItem, parentModelItem);
        if (modelItem.getValueEditingOptions(editingOptionsProvider) == null) {
            return null;
        }
        final IXmlValueEditor<? extends QWidget> editor =
                valueEditorFactory.createEditor(getEnvironment(), editingOptionsProvider, modelItem, false);
        editor.addValueListener(new IXmlValueEditor.ValueListener() {
            @Override
            public void valueChanged(final String newValue) {
                createChildN();
            }
        });
        editor.asWidget().setParent(this);
        return editor;
    }

    private void onChangeSchemaElement() {
        if (childValue != null) {
            formLayout.takeAt(3);
            childValue.asWidget().setVisible(false);
            childValue.asWidget().close();
        }
        final int currentIndex = allowedNames.currentIndex();
        final XmlSchemaElementItem currentSchemaItem =
                (XmlSchemaElementItem) allowedNames.itemData(currentIndex, Qt.ItemDataRole.UserRole);
        childValue = createValueEditor(currentSchemaItem, editingOptionsProvider);
        if (childValue == null) {
            formLayout.setWidget(1, QFormLayout.ItemRole.LabelRole, null);
            lbValue.setVisible(false);
        } else {
            formLayout.setWidget(1, QFormLayout.ItemRole.LabelRole, lbValue);
            lbValue.setVisible(true);
            formLayout.setWidget(1, QFormLayout.ItemRole.FieldRole, childValue.asWidget());
        }
    }

    @Override
    public String getChildText() {
        if (childValue != null) {
            return childValue.getValue();
        } else {
            return null;
        }
    }
}
