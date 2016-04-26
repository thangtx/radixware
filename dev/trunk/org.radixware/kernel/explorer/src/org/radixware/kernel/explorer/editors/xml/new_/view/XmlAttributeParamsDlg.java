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
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QRegExpValidator;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaAttributeItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.ICreateAttributeDialog;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;


public class XmlAttributeParamsDlg extends ExplorerDialog implements ICreateAttributeDialog {

    private QLineEdit attributeName;
    private final XmlModelItem parentModelItem;
    private final XmlValueEditorFactory<? extends QWidget> valueEditorFactory;
    private IXmlValueEditor<? extends QWidget> attributeValue;
    private final List<QName> namesAttr;
    private QComboBox allowedNames;
    private final QFormLayout formLayout = new QFormLayout();
    final IXmlValueEditingOptionsProvider editingOptionsProvider;

    public XmlAttributeParamsDlg(final IClientEnvironment environment,
            final QWidget parent,
            final XmlValueEditorFactory<? extends QWidget> valueEditorFactory,
            final IXmlValueEditingOptionsProvider editingOptionsProvider,
            final XmlModelItem parentModelItem,
            final List<XmlSchemaAttributeItem> possibleItems,
            final List<QName> restrictedNames) {
        super(environment, parent);
        namesAttr = restrictedNames;
        this.parentModelItem = parentModelItem;
        this.valueEditorFactory = valueEditorFactory;
        this.editingOptionsProvider = editingOptionsProvider;
        if (possibleItems == null) {
            attributeName = new QLineEdit(this);
            attributeName.textEdited.connect(this, "createAttrVandN()");
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Attribute name:"), attributeName);
            final QRegExp re = new QRegExp("[_a-zA-Z][_a-zA-Z0-9.-]*");
            final QRegExpValidator v = new QRegExpValidator(re, null);
            attributeName.setValidator(v);
            attributeValue = createValueEditor(null, editingOptionsProvider);
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Attribute value:"), attributeValue.asWidget());
        } else {
            allowedNames = new QComboBox(this);
            for (XmlSchemaAttributeItem attributeItem : possibleItems) {
                allowedNames.addItem(attributeItem.getLocalName(), attributeItem);
            }
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Attribute name:"), allowedNames);
            allowedNames.setCurrentIndex(0);
            formLayout.addRow(environment.getMessageProvider().translate("XmlEditor", "Attribute value:"), (QWidget) null);
            onChangeSchemaAttribute();
            allowedNames.currentIndexChanged.connect(this, "onChangeSchemaAttribute()");
        }
        dialogLayout().addLayout(formLayout);
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Creating Attribute"));
        addButton(EDialogButtonType.OK).setEnabled(false);
        addButton(EDialogButtonType.CANCEL);
        if (possibleItems == null) {
            acceptButtonClick.connect(this, "checkNameAttr()");
        } else {
            acceptButtonClick.connect(this, "accept()");
        }
        rejectButtonClick.connect(this, "reject()");
    }

    private void createAttrVandN() {
        if ((attributeName != null && (attributeName.text().isEmpty() || !attributeName.hasAcceptableInput()))
                || (attributeValue.getValue() == null || attributeValue.getValue().isEmpty() || attributeValue.getValue().startsWith(" "))) {
            getButton(EDialogButtonType.OK).setEnabled(false);
        } else if (attributeName == null && (attributeValue.getValue() == null
                || attributeValue.getValue().isEmpty() || attributeValue.getValue().startsWith(" "))) {
            getButton(EDialogButtonType.OK).setEnabled(false);
        } else {
            getButton(EDialogButtonType.OK).setEnabled(true);
        }
    }

    private IXmlValueEditor<? extends QWidget> createValueEditor(final XmlSchemaAttributeItem attrItem, final IXmlValueEditingOptionsProvider editingOptionsProvider) {
        final IXmlValueEditor<? extends QWidget> editor =
                valueEditorFactory.createEditor(getEnvironment(), editingOptionsProvider, new XmlModelItem(null, attrItem, parentModelItem), false);
        editor.addValueListener(new IXmlValueEditor.ValueListener() {
            @Override
            public void valueChanged(final String newValue) {
                createAttrVandN();
            }
        });
        editor.asWidget().setParent(this);
        return editor;
    }

    private void onChangeSchemaAttribute() {
        if (attributeValue != null) {
            formLayout.takeAt(3);
            attributeValue.asWidget().setVisible(false);
            attributeValue.asWidget().close();
            getButton(EDialogButtonType.OK).setEnabled(false);
        }
        final int currentIndex = allowedNames.currentIndex();
        final XmlSchemaAttributeItem currentSchemaItem =
                (XmlSchemaAttributeItem) allowedNames.itemData(currentIndex, Qt.ItemDataRole.UserRole);
        attributeValue = createValueEditor(currentSchemaItem, editingOptionsProvider);
        formLayout.setWidget(1, QFormLayout.ItemRole.FieldRole, attributeValue.asWidget());
    }

    public void checkNameAttr() {
        if (namesAttr.contains(new QName(attributeName.text()))) {
            final String title = getEnvironment().getMessageProvider().translate("XmlEditor", "Wrong Name");
            final String message = getEnvironment().getMessageProvider().translate("XmlEditor", "Attribute with such name already exist! Please, rename this element.");
            getEnvironment().messageInformation(title, message);
            attributeName.setFocus();
        } else {
            accept();
        }
    }

    @Override
    public QName getAttrName() {
        if (attributeName != null) {
            return new QName(attributeName.text());
        } else {
            final XmlSchemaAttributeItem childSchemaItem;
            final int currentIndex = allowedNames.currentIndex();
            if (currentIndex >= 0) {
                childSchemaItem =
                        (XmlSchemaAttributeItem) allowedNames.itemData(currentIndex, Qt.ItemDataRole.UserRole);
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

    @Override
    public String getAttrValue() {
        return attributeValue.getValue();
    }
}
