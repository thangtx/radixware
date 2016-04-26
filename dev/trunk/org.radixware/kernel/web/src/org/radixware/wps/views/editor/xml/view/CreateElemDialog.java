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
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.ICreateElementDialog;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;


final class CreateElemDialog extends Dialog implements ICreateElementDialog {

    private InputBox<String> nameEditor;
    private final XmlModelItem parentModelItem;
    private final XmlValueEditorFactory<? extends UIObject> valueEditorFactory;
    private IXmlValueEditor<? extends UIObject> elementValue;
    private ValListEditorController<String> listEditorController;
    private final Map<String, XmlSchemaElementItem> schemaItemsByName;
    final FormBox form = new FormBox();
    final IXmlValueEditingOptionsProvider editingOptionsProvider;

    public CreateElemDialog(final IClientEnvironment environment,
            final XmlValueEditorFactory<? extends UIObject> valueEditorFactory,
            final IXmlValueEditingOptionsProvider editingOptionsProvider,
            final XmlModelItem parentModelItem,
            final List<XmlSchemaElementItem> allowedItems) {
        setWindowTitle(environment.getMessageProvider().translate("XmlEditor", "Creating Element"));
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
            form.addLabledEditor(environment.getMessageProvider().translate("XmlEditor", "Element name:"), nameEditor);
            elementValue = createValueEditor(null, editingOptionsProvider);
            form.addLabledEditor(environment.getMessageProvider().translate("XmlEditor", "Element value:"), elementValue.asWidget());
            schemaItemsByName = null;
        } else {
            schemaItemsByName = new HashMap<>();
            EditMaskList editMask = new EditMaskList();
            listEditorController = new ValListEditorController<>(environment, editMask);
            listEditorController.setMandatory(true);
            final UIObject comboBox = (UIObject) listEditorController.getValEditor();
            for (XmlSchemaElementItem item : allowedItems) {
                schemaItemsByName.put(item.getLocalName(), item);
                editMask.addItem(item.getLocalName(), item.getLocalName());
            }
            listEditorController.setEditMask(editMask);
            listEditorController.setValue(allowedItems.get(0).getLocalName());
            add(comboBox);
            form.addLabledEditor(environment.getMessageProvider().translate("XmlEditor", "Element name:"), comboBox);
            onChangeSchemaElement();
            listEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {
                @Override
                public void onValueChanged(String oldValue, String newValue) {
                    onChangeSchemaElement();
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
            final String title = getEnvironment().getMessageProvider().translate("XmlEditor", "Wrong Name");
            final String message1 = getEnvironment().getMessageProvider().translate("XmlEditor", "Input correctly name");
            final String message2 = getEnvironment().getMessageProvider().translate("XmlEditor", "Choose name");
            if (nameEditor != null) {
                if (nameEditor.getValue() == null || !nameEditor.getValue().matches("[_a-zA-Z][_a-zA-Z0-9.-]*")) {
                    getEnvironment().messageError(title, message1);
                    return null;
                } else {
                    return super.onClose(action, actionResult);
                }
            } else if (listEditorController != null && listEditorController.getValue() != null) {
                return super.onClose(action, actionResult);
            } else {
                getEnvironment().messageError(title, message2);
                return null;
            }
        } else {
            return super.onClose(action, actionResult);
        }
    }

    private IXmlValueEditor<? extends UIObject> createValueEditor(final XmlSchemaElementItem elemItem, final IXmlValueEditingOptionsProvider editingOptionsProvider) {
        final XmlModelItem modelItem = new XmlModelItem(null, elemItem, parentModelItem);
        if (modelItem.getValueEditingOptions(editingOptionsProvider) == null) {
            return null;
        }
        return valueEditorFactory.createEditor(getEnvironment(), editingOptionsProvider, modelItem);
    }

    private void onChangeSchemaElement() {
        if (elementValue != null) {
            form.removeEditor(elementValue.asWidget());
        }
        final XmlSchemaElementItem currentSchemaItem =
                schemaItemsByName.get(listEditorController.getValue());
        elementValue = createValueEditor(currentSchemaItem, editingOptionsProvider);
        if (elementValue != null) {
            form.addLabledEditor(getEnvironment().getMessageProvider().translate("XmlEditor", "Element value:"), elementValue.asWidget());
        }
    }

    @Override
    public QName getChildName() {
        if (nameEditor != null) {
            return new QName(nameEditor.getValue());
        } else {
            final XmlSchemaElementItem childSchemaItem;
            childSchemaItem = schemaItemsByName.get(listEditorController.getValue());
            return childSchemaItem.getFullName();
        }
    }

    @Override
    public String getChildText() {
        if (elementValue != null) {
            return elementValue.getValue();
        } else {
            return null;
        }
    }
}
