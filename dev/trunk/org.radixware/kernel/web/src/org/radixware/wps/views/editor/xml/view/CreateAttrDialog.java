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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaAttributeItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.ICreateAttributeDialog;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;


final class CreateAttrDialog extends Dialog implements ICreateAttributeDialog {

    private InputBox<String> nameEditor;
    private final XmlModelItem parentModelItem;
    private final XmlValueEditorFactory<? extends UIObject> valueEditorFactory;
    private IXmlValueEditor<? extends UIObject> attributeValue;
    private ValListEditorController<String> listEditorController;
    private final Map<String, XmlSchemaAttributeItem> schemaItemsByName;
    private final List<QName> namesAttr;
    final FormBox form = new FormBox();
    final IXmlValueEditingOptionsProvider editingOptionsProvider;

    public CreateAttrDialog(final IClientEnvironment environment,
            final XmlValueEditorFactory<? extends UIObject> valueEditorFactory,
            final IXmlValueEditingOptionsProvider editingOptionsProvider,
            final XmlModelItem parentModelItem,
            final List<XmlSchemaAttributeItem> allowedItems,
            final List<QName> restrictedNames) {
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Creating Attribute"));
        namesAttr = restrictedNames;
        this.parentModelItem = parentModelItem;
        this.valueEditorFactory = valueEditorFactory;
        this.editingOptionsProvider = editingOptionsProvider;
        this.add(form);
        if (allowedItems == null) {
            nameEditor = new InputBox<>();
            nameEditor.setValueController(new InputBox.ValueController<String>() {
                @Override
                public String getValue(String text) throws InputBox.InvalidStringValueException {
                    return text;
                }
            });
            form.addLabledEditor(environment.getMessageProvider().translate("XmlEditor", "Attribute name:"), nameEditor);
            attributeValue = createValueEditor(null, editingOptionsProvider);
            form.addLabledEditor(environment.getMessageProvider().translate("XmlEditor", "Attribute value:"), attributeValue.asWidget());
            schemaItemsByName = null;
        } else {
            schemaItemsByName = new HashMap<>();
            EditMaskList editMask = new EditMaskList();
            listEditorController = new ValListEditorController<>(environment, editMask);
            listEditorController.setMandatory(true);
            final UIObject comboBox = (UIObject) listEditorController.getValEditor();
            for (XmlSchemaAttributeItem item : allowedItems) {
                schemaItemsByName.put(item.getLocalName(), item);
                editMask.addItem(item.getLocalName(), item.getLocalName());
            }
            listEditorController.setEditMask(editMask);
            listEditorController.setValue(allowedItems.get(0).getLocalName());
            add(comboBox);
            form.addLabledEditor(environment.getMessageProvider().translate("XmlEditor", "Attribute name:"), comboBox);
            onChangeSchemaAttribute();
            listEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {
                @Override
                public void onValueChanged(String oldValue, String newValue) {
                    onChangeSchemaAttribute();
                }
            });
        }
        addCloseAction(EDialogButtonType.OK);
        addCloseAction(EDialogButtonType.CANCEL);
        setWidth(300);
        setHeight(130);
        setMinimumHeight(130);
        setMaxHeight(130);
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            final String title1 = getEnvironment().getMessageProvider().translate("XmlEditor", "Empty Value");
            final String title2 = getEnvironment().getMessageProvider().translate("XmlEditor", "Wrong Name");
            final String message1 = getEnvironment().getMessageProvider().translate("XmlEditor", "Input value of attribute");
            final String message2 = getEnvironment().getMessageProvider().translate("XmlEditor", "Input correctly name");
            final String message3 = getEnvironment().getMessageProvider().translate("XmlEditor", "Attribute with such name already exist! Please, rename this element.");
            final String message4 = getEnvironment().getMessageProvider().translate("XmlEditor", "Choose name");
            if (nameEditor != null) {
                if (attributeValue.getValue() == null || attributeValue.getValue().isEmpty()
                        || attributeValue.getValue().startsWith(" ")) {
                    getEnvironment().messageError(title1, message1);
                    return null;
                } else if (nameEditor.getValue() == null || !nameEditor.getValue().matches("[_a-zA-Z][_a-zA-Z0-9.-]*")) {
                    getEnvironment().messageError(title2, message2);
                    return null;
                } else if (namesAttr.contains(new QName(nameEditor.getValue()))) {
                    getEnvironment().messageError(title2, message3);
                    return null;
                } else {
                    return super.onClose(action, actionResult);
                }
            } else if (listEditorController != null && listEditorController.getValue() != null) {
                if (attributeValue.getValue() != null && !attributeValue.getValue().isEmpty()
                        && !attributeValue.getValue().startsWith(" ")) {
                    return super.onClose(action, actionResult);
                } else {
                    getEnvironment().messageError(title1, message1);
                    return null;
                }
            } else {
                getEnvironment().messageError(title2, message4);
                return null;
            }
        } else {
            return super.onClose(action, actionResult);
        }
    }

    private IXmlValueEditor<? extends UIObject> createValueEditor(final XmlSchemaAttributeItem attrItem, final IXmlValueEditingOptionsProvider editingOptionsProvider) {
        return valueEditorFactory.createEditor(getEnvironment(), editingOptionsProvider, new XmlModelItem(null, attrItem, parentModelItem));                        
    }

    private void onChangeSchemaAttribute() {
        if (attributeValue != null) {
            form.removeEditor(attributeValue.asWidget());
        }
        final XmlSchemaAttributeItem currentSchemaItem =
                schemaItemsByName.get(listEditorController.getValue());
        attributeValue = createValueEditor(currentSchemaItem, editingOptionsProvider);
        form.addLabledEditor(getEnvironment().getMessageProvider().translate("XmlEditor", "Attribute value:"), attributeValue.asWidget());
    }

    @Override
    public QName getAttrName() {
        if (nameEditor != null) {
            return new QName(nameEditor.getValue());
        } else {
            return new QName(listEditorController.getValue());
        }
    }

    @Override
    public String getAttrValue() {
        return attributeValue.getValue();
    }
}
