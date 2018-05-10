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

import com.trolltech.qt.core.QObject;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlValueEditingOptions;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueConverter;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.explorer.dialogs.DateTimeDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;

public class XmlValueEdit implements IXmlValueEditor<ValEditor> {

    final private XmlModelItem model;
    final private IClientEnvironment environment;
    final private IXmlValueEditingOptionsProvider editingOptionsProvider;
    private ValEditor valEditor;
    private ValStrEditor wrongValueEditor;
    private boolean wrongValueMode;
    private boolean isReadOnly;
    private boolean needToNotifyListeners;
    final private SchemaType schemaType;
    private DateTimeDialog dialog;
    QAction openDialog;
    DialogForCorrectValue dtd;
    private List<IXmlValueEditor.ValueListener> listeners = new LinkedList<>();
    private List<IXmlValueEditor.WidgetListener<ValEditor>> widgetListeners = new LinkedList<>();

    public XmlValueEdit(final IClientEnvironment environment, final XmlModelItem modelItem, final IXmlValueEditingOptionsProvider editingOptionsProvider, final boolean isReadOnly) {
        this.environment = environment;
        this.editingOptionsProvider = editingOptionsProvider;
        final XmlValueEditingOptions editingOptions = modelItem.getValueEditingOptions(editingOptionsProvider);
        if (editingOptions == null) {
            throw new IllegalArgumentException(environment.getMessageProvider().translate("XmlEditor", "item is not editable"));
        }
        schemaType = editingOptions.getSchemaType();
        model = modelItem;
        setReadOnly(isReadOnly);
    }

    @Override
    public void setValue(final String xmlValue) {
        final XmlValueEditingOptions editingOptions = model.getValueEditingOptions(editingOptionsProvider);
        final EditMask mask = editingOptions.getEditMask();
        final EEditMaskType maskType = mask.getType();
        final EValType valType = editingOptions.getValType();
        final Object valueObject;
        try {
            valueObject =
                    XmlValueConverter.getInstance().xmlStringToValue(xmlValue, maskType, valType, schemaType);
        } catch (WrongFormatError | IllegalArgumentError | NumberFormatException error) {
            getWrongValueEditor().setValue(xmlValue);
            if (!wrongValueMode) {
                valEditor = null;
                notifyWidgetChanged(getWrongValueEditor());
                wrongValueMode = true;
            }
            return;
        }
        setValueObject(valueObject);
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        final boolean prevReadOnly = this.isReadOnly;
        final XmlValueEditingOptions editingOptions = model.getValueEditingOptions(editingOptionsProvider);
        this.isReadOnly = isReadOnly || editingOptions.isReadOnly();
        if (this.isReadOnly != prevReadOnly && valEditor != null) {
            valEditor.setReadOnly(this.isReadOnly);
        }
    }

    @SuppressWarnings("unchecked")
    private void setValueObject(final Object valueObject) {
        getValEditor().blockSignals(true);
        try {
            getValEditor().setValue(valueObject);
        } finally {
            getValEditor().blockSignals(false);
        }
        if (wrongValueMode) {
            notifyWidgetChanged(getValEditor());
            wrongValueEditor = null;
            wrongValueMode = false;
        }
    }

    Object getValueObject() {
        return wrongValueMode ? getWrongValueEditor().getValue() : getValEditor().getValue();
    }

    @Override//need for QtJambi getter/setter types chacker
    public String getValue() {
        if (wrongValueMode) {
            return getWrongValueEditor().getValue();
        } else {
            final Object valueObject = getValEditor().getValue();
            final XmlValueEditingOptions editingOptions = model.getValueEditingOptions(editingOptionsProvider);
            final EditMask mask = editingOptions.getEditMask();
            return XmlValueConverter.getInstance().valueToXmlString(valueObject, mask, environment, schemaType);
        }
    }

    @Override
    public void addValueListener(final ValueListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeValueListener(final ValueListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void clearValueListeners() {
        listeners.clear();
    }

    @Override
    public void addWidgetListener(WidgetListener<ValEditor> listener) {
        widgetListeners.add(listener);
    }

    @Override
    public void removeWidgetListener(WidgetListener<ValEditor> listener) {
        widgetListeners.remove(listener);
    }

    @Override
    public void clearWidgetListeners() {
        widgetListeners.clear();
    }

    private void notifyWidgetChanged(ValEditor widget) {
        for (IXmlValueEditor.WidgetListener<ValEditor> widgetListener : widgetListeners) {
            widgetListener.widgetChanged(widget);
        }
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        final String value = getValue();
        if (QObject.signalSender()==valEditor){//NOPMD            
            if ((value == null && valEditor.isMandatory()) || valEditor.getValidationResult() != ValidationResult.ACCEPTABLE){
                needToNotifyListeners = true;
                return;
            }
        }
        notifyListeners(value);
    }
    
    @SuppressWarnings("unused")
    private void onFinishEdit(){
        if (needToNotifyListeners){
            notifyListeners(getValue());
        }
    }
    
    private void notifyListeners(final String value){
        needToNotifyListeners = false;
        for (IXmlValueEditor.ValueListener listener : listeners) {
            listener.valueChanged(value);
        }        
    }

    @Override
    public XmlModelItem getModelItem() {
        return model;
    }

    @Override
    public ValEditor asWidget() {
        return wrongValueMode ? getWrongValueEditor() : getValEditor();
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    private ValStrEditor getWrongValueEditor() {
        if (wrongValueEditor == null) {
            wrongValueEditor = new ValStrEditor(environment, null);
            wrongValueEditor.setReadOnly(isReadOnly);
            wrongValueEditor.changeStateForGrid();
            wrongValueEditor.getLineEdit().setReadOnly(true);
            wrongValueEditor.setValidationResult(ValidationResult.Factory.newInvalidResult(InvalidValueReason.WRONG_FORMAT));
            openDialog = new QAction(null);
            openDialog.triggered.connect(this, "openDialog()");
            openDialog.setObjectName("open_dialog");
            if (!isReadOnly) {
                wrongValueEditor.addButton("...", openDialog);
            }
        }
        return wrongValueEditor;
    }

    private ValEditor getValEditor() {
        if (valEditor == null) {
            final XmlValueEditingOptions editingOptions = model.getValueEditingOptions(editingOptionsProvider);                        
            final EditMask mask = editingOptions.getEditMask();
            final EEditMaskType maskType = mask.getType();
            final boolean isMandatory = editingOptions.isNotNull();
            final boolean readOnly = editingOptions.isReadOnly() || this.isReadOnly;
            if (editingOptions.getValType().isArrayType()) {
                valEditor = new XmlValArrEditor(environment, editingOptions.getValType(), isMandatory, readOnly, schemaType);
                valEditor.setEditMask(mask);
            } else if (editingOptions.getValType() == EValType.BIN) {
                valEditor = new XmlValBinEditor(environment, (EditMaskNone) mask, isMandatory, readOnly, schemaType);
            } else {
                switch (maskType) {
                    case BOOL:
                        valEditor = new XmlValBoolEditor(environment, mask, isMandatory, readOnly, schemaType);
                        break;
                    case DATE_TIME:
                        valEditor = new XmlValDateTimeEditor(environment, (EditMaskDateTime) mask, isMandatory, readOnly, schemaType);
                        break;
                    case INT:
                        valEditor = new XmlValIntEditor(environment, (EditMaskInt) mask, isMandatory, readOnly, schemaType);
                        break;
                    case LIST:
                        valEditor = new XmlValListEditor(environment, (EditMaskList) mask, isMandatory, readOnly, schemaType);
                        break;
                    case NUM:
                        valEditor = new XmlValNumEditor(environment, (EditMaskNum) mask, isMandatory, readOnly, schemaType);
                        break;
                    case STR:
                        valEditor = new XmlValStrEditor(environment, (EditMaskStr) mask, isMandatory, readOnly, schemaType);
                        break;
                    case ENUM:
                        valEditor = new XmlValConstSetEditor(environment, (EditMaskConstSet) mask, isMandatory, readOnly, schemaType);
                        break;
                    case FILE_PATH:
                    case TIME_INTERVAL:
                    case OBJECT_REFERENCE:
                    //TODO
                    default:
                        throw new IllegalArgumentException(environment.getMessageProvider().translate("XmlEditor", "mask is not suitable"));
                }
            }
            if (model.getXmlNode() != null) {
                valEditor.changeStateForGrid();
            }
            valEditor.valueChanged.connect(this, "onValueChanged()");
            valEditor.editingFinished.connect(this, "onFinishEdit()");
        }
        return valEditor;
    }

    void openDialog() {
        dtd = new DialogForCorrectValue(environment, wrongValueEditor, model, editingOptionsProvider);
        if (schemaType.getBuiltinTypeCode() != SchemaType.BTC_DATE
                && schemaType.getBuiltinTypeCode() != SchemaType.BTC_DATE_TIME) {
            if (dtd.exec() == QDialog.DialogCode.Accepted.value()) {
                setValueObject(dtd.getValue());
                onValueChanged();
            }
        } else {
            dialog = new DateTimeDialog(environment, wrongValueEditor);
            if (schemaType.getBuiltinTypeCode() == SchemaType.BTC_DATE) {
                dialog.setTimeFieldVisible(false);
            }
            dialog.setMandatory(true);
            if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                setValueObject(dialog.getCurrentDateTime());
                onValueChanged();
            }
        }
    }
}