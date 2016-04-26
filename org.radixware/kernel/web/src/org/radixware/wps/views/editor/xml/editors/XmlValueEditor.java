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
package org.radixware.wps.views.editor.xml.editors;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlValueConverter;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlValueEditingOptions;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.editors.valeditors.ValEditorController;

public class XmlValueEditor implements IXmlValueEditor<UIObject> {

    final private XmlModelItem model;
    final private IClientEnvironment environment;
    final private IXmlValueEditingOptionsProvider editingOptionsProvider;
    private ValEditorController valEditor;
    final private SchemaType schemaType;
    private InputBox<String> wrongValueBox;
    private ToolButton but;
    private boolean wrongValueMode;
    private boolean isReadOnly;
    DialogForCorrectValue dtd;
    private List<IXmlValueEditor.ValueListener> listeners = new LinkedList<>();
    private List<IXmlValueEditor.WidgetListener<UIObject>> widgetListeners = new LinkedList<>();
    private final ValueEditor.ValueChangeListener valueListener = new ValueEditor.ValueChangeListener() {
        @Override
        public void onValueChanged(Object oldValue, Object newValue) {
            XmlValueEditor.this.onValueChanged(newValue);
        }
    };

    public XmlValueEditor(final IClientEnvironment environment, final XmlModelItem modelItem, final IXmlValueEditingOptionsProvider editingOptionsProvider) {
        this.environment = environment;
        this.editingOptionsProvider = editingOptionsProvider;
        final XmlValueEditingOptions editingOptions = modelItem.getValueEditingOptions(editingOptionsProvider);
        if (editingOptions == null) {
            throw new IllegalArgumentException(environment.getMessageProvider().translate("XmlEditor", "Item is not editable"));
        }
        schemaType = editingOptions.getSchemaType();
        model = modelItem;
    }

    @Override
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
    public void setValue(final String value) {
        final XmlValueEditingOptions editingOptions = model.getValueEditingOptions(editingOptionsProvider);
        final EditMask mask = editingOptions.getEditMask();
        final EEditMaskType maskType = mask.getType();
        final EValType valType = editingOptions.getValType();
        final Object valueObject;
        try {
            valueObject =
                    XmlValueConverter.getInstance().xmlStringToValue(value, maskType, valType, schemaType);
        } catch (WrongFormatError | IllegalArgumentError | NumberFormatException error) {
            getWrongValueEditor().setValue(value);
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
        if (this.isReadOnly != prevReadOnly) {
            if (valEditor != null) {
                valEditor.setReadOnly(this.isReadOnly);
            } else if (wrongValueBox != null) {
                wrongValueBox.setReadOnly(isReadOnly);
                ITextOptions textOptions = model.getValueTextOptions(environment, this.isReadOnly);
                if (isReadOnly) {
                    wrongValueBox.setValidationMessage(null);
                } else {
                    final String message =
                            InvalidValueReason.NO_REASON.getMessage(environment.getMessageProvider(), InvalidValueReason.EMessageType.Value);
                    wrongValueBox.setValidationMessage(message);
                    textOptions = textOptions.changeForegroundColor(Color.red);
                }
                wrongValueBox.setTextOptions((WpsTextOptions) textOptions);
                if (but != null) {
                    but.setVisible(!isReadOnly);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setValueObject(final Object valueObject) {
        getValEditor().removeValueChangeListener(valueListener);
        try {
            getValEditor().setValue(valueObject);
        } finally {
            getValEditor().addValueChangeListener(valueListener);
        }
        if (wrongValueMode) {
            notifyWidgetChanged((UIObject) getValEditor().getValEditor());
            wrongValueBox = null;
            but = null;
            wrongValueMode = false;
        }
    }

    Object getValueObject() {
        return wrongValueMode ? getWrongValueEditor().getValue() : getValEditor().getValue();
    }

    private InputBox<String> getWrongValueEditor() {
        if (wrongValueBox == null) {
            wrongValueBox = new InputBox<String>() {
                @Override
                public void setParent(final UIObject parent) {
                    super.setParent(parent);
                    if (parent != null) {
                        final String message =
                                InvalidValueReason.NO_REASON.getMessage(environment.getMessageProvider(), InvalidValueReason.EMessageType.Value);
                        setValidationMessage(message);
                    }
                }
            };
            but = new ToolButton();
            but.setText("...");
            but.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    openDialog();
                }
            });
            wrongValueBox.addCustomButton(but);
            wrongValueBox.setValueController(null);
            if (isReadOnly) {
                but.setVisible(false);
                wrongValueBox.setReadOnly(true);
                ITextOptions textOptions = model.getValueTextOptions(environment, this.isReadOnly);
                wrongValueBox.setTextOptions((WpsTextOptions) textOptions);
            }
        }
        return wrongValueBox;
    }

    private void openDialog() {
        dtd = new DialogForCorrectValue(environment, model, editingOptionsProvider);
        if (dtd.execDialog() == IDialog.DialogResult.ACCEPTED) {
            setValueObject(dtd.getValue());
            onValueChanged(getValueObject());
        }
    }

    private void onValueChanged(final Object newValue) {
        for (IXmlValueEditor.ValueListener listener : listeners) {
            listener.valueChanged(getValue());
        }
    }

    private void notifyWidgetChanged(final UIObject widget) {
        for (IXmlValueEditor.WidgetListener<UIObject> widgetListener : widgetListeners) {
            widgetListener.widgetChanged(widget);
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
    public UIObject asWidget() {
        return wrongValueMode ? getWrongValueEditor() : (UIObject) getValEditor().getValEditor();
    }

    @Override
    public XmlModelItem getModelItem() {
        return model;
    }

    @Override
    public void addWidgetListener(final WidgetListener<UIObject> listener) {
        final WidgetListener<UIObject> widgetListener = new WidgetListener<UIObject>() {
            @Override
            public void widgetChanged(UIObject newWidget) {
                listener.widgetChanged(newWidget);
            }
        };
        widgetListeners.add(widgetListener);
    }

    @Override
    public void removeWidgetListener(WidgetListener<UIObject> listener) {
        widgetListeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    private ValEditorController getValEditor() {
        if (valEditor == null) {
            final XmlValueEditingOptions editingOptions = model.getValueEditingOptions(editingOptionsProvider);
            final EditMask mask = editingOptions.getEditMask();
            final EEditMaskType maskType = mask.getType();
            if (editingOptions.getValType().isArrayType()) {
                valEditor = new XmlValArrEditorController(environment, editingOptions.getValType(), null, schemaType);
                valEditor.setEditMask(mask);
            } else if (editingOptions.getValType() == EValType.BIN) {
                valEditor = new XmlValBinEditorController(environment, schemaType);
            } else {
                switch (maskType) {
                    case BOOL:
                        valEditor = new XmlValBoolEditorController(environment, schemaType);
                        valEditor.setEditMask(mask);
                        break;
                    case DATE_TIME:
                        valEditor = new XmlValDateTimeEditorController(environment, schemaType);
                        valEditor.setEditMask(mask);
                        break;
                    case INT:
                        valEditor = new XmlValIntEditorController(environment, schemaType);
                        valEditor.setEditMask(mask);
                        break;
                    case LIST:
                        valEditor = new XmlValListEditorController(environment, schemaType);
                        valEditor.setEditMask(mask);
                        break;
                    case NUM:
                        valEditor = new XmlValNumEditorController(environment, schemaType);
                        valEditor.setEditMask(mask);
                        if (((XmlValNumEditorController)valEditor).getEditMask().getPrecision() == 0){
                            valEditor.setReadOnly(true);
                        }
                        break;
                    case STR:
                        valEditor = new XmlValStrEditorController(environment, schemaType);
                        valEditor.setEditMask(mask);
                        break;
                    case ENUM:
                        valEditor = new XmlValConstSetEditorController(environment, (EditMaskConstSet) mask, schemaType);
                        break;
                    case FILE_PATH:
                    case TIME_INTERVAL:
                    //TODO
                    default:
                        throw new IllegalArgumentException(environment.getMessageProvider().translate("XmlEditor", "Mask is not suitable"));
                }
            }
            final boolean isMandatory = editingOptions.isNotNull();
            valEditor.setReadOnly(editingOptions.isReadOnly() || isReadOnly);
            valEditor.setMandatory(isMandatory);
            valEditor.addValueChangeListener(valueListener);
        }
        return valEditor;
    }

    @Override
    public void clearValueListeners() {
        listeners.clear();
    }

    @Override
    public void clearWidgetListeners() {
        widgetListeners.clear();
    }
}