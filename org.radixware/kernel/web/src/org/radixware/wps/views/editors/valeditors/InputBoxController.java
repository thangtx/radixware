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

package org.radixware.wps.views.editors.valeditors;

import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.EditingHistoryException;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.ButtonBase;
import org.radixware.wps.rwt.DropDownEditHistoryDelegate;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputFormat;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;


public abstract class InputBoxController<T, V extends EditMask> extends ValEditorController<T, V> {

    private static class InternalInputBox<V, M extends EditMask> extends InputBox<V> implements IValEditor<V, M> {

        private final IClientEnvironment env;
        private InputBoxController<V, M> editorController;
        private V initialValue;

        private InternalInputBox(IClientEnvironment environment, DisplayController<V> controller, ValueController<V> valueController, InputBoxController<V, M> editorController) {
            super(controller, valueController);
            env = environment;
            this.editorController = editorController;
        }

        @Override
        public void addButton(IButton button) {
            if (button instanceof ButtonBase) {
                addCustomButton((ButtonBase) button);
            }
        }

        @Override
        public void setValidationResult(ValidationResult validationResult) {
            if (ValidationResult.ACCEPTABLE == validationResult) {
                setValidationMessage(null);
            } else {
                final String comment = validationResult.getInvalidValueReason().toString();
                if (comment == null || comment.isEmpty()) {
                    setValidationMessage(env.getMessageProvider().translate("Value", "Current value is invalid"));
                } else {
                    setValidationMessage(comment);
                }
            }
        }

        @Override
        public ValidationResult getValidationResult() {
            final String message = getValidationMessage();
            if (message == null || message.isEmpty()) {
                return ValidationResult.ACCEPTABLE;
            } else {
                return ValidationResult.Factory.newInvalidResult(message);
            }
        }

        @Override
        public void refresh() {
            updateDisplayString();
        }

        @Override
        public ValEditorController<V, M> getController() {
            return editorController;
        }

        @Override
        protected Label createLabel() {

            Label label = ((InputBoxController) editorController).createLabel();
            if (label != null) {
                return label;
            }

            return super.createLabel();
        }

        @Override
        public void setReadOnly(final boolean readOnly) {
            super.setReadOnly(readOnly);
            editorController.afterChangeReadOnly();
        }

        @Override
        public void setInitialValue(final V value) {
            initialValue = value;
            setInitialText(editorController.calcInitialText(value, editorController.getEditMask()));
        }

        @Override
        public V getInitialValue() {
            return initialValue;
        }

        @Override
        protected IClientApplication getApplication() {
            return env.getApplication();
        }
    }

    private class EditMaskDisplayController implements InputBox.DisplayController<T> {

        @Override
        public String getDisplayValue(T value, boolean isFocused, boolean isReadOnly) {
            if (editMask != null) {
                return isFocused ? calcFocusedText(value, editMask) : editMask.toStr(getEnvironment(), value);
            } else {
                return value == null ? getEnvironment().getMessageProvider().translate("Value", "<not defined>") : value.toString();
            }
        }
    }

    protected abstract InputBox.ValueController<T> createValueController();
    protected IEditingHistory editingHistory = null;
    private DropDownEditHistoryDelegate<T> dropDownHistoryDelegate;
    private EValType valType;
    private boolean isEnabled = true;

    public InputBoxController(IClientEnvironment env) {
        super(env);
        getValEditor();
    }

    protected void setupValEditor(InputBox<T> inputBox) {
        if (!isMandatory()) {
            inputBox.setClearController(new InputBox.ClearController<T>() {
                @Override
                public T clear() {
                    return null;
                }
            });
        } else {
            inputBox.setClearController(null);
        }
    }

    protected final InputBox<T> getInputBox() {
        if (getValEditor() instanceof InputBox){
            return (InputBox<T>) getValEditor();
        }else{
            return null;
        }        
    }

    public void setLabelVisible(boolean visible) {
        final InputBox box = getInputBox();
        if (box!=null){
            box.setLabelVisible(visible);
        }
    }

    public boolean isLabelVisible() {
        final InputBox box = getInputBox();
        return box==null ? false : box.isLabelVisible();
    }

    public boolean getLabelVisible() {
        return isLabelVisible();
    }

    public String getLabel() {
        final InputBox box = getInputBox();
        return box==null ? null : box.getLabel();        
    }

    public void setLabel(String label) {
        final InputBox box = getInputBox();
        if (box!=null){
            box.setLabel(label);
        }
    }

    protected Label createLabel() {
        return null;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (getValEditor() instanceof UIObject){
            ((UIObject)getValEditor()).setEnabled(isEnabled);
        }else{
            final InputBox box = getInputBox();
            if (box!=null){
                box.setEnabled(isEnabled);
            }
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    protected InputFormat createInputFormat() {
        return InputFormat.Factory.inputFormatFromEditMask(getEditMask(), getEnvironment().getLocale());
    }

    protected void applyEditMask(InputBox box) {
        if (box!=null){
            box.setInputFormat(createInputFormat());
            if (box.hasAcceptableInput()){
                box.setFocusedText(calcFocusedText(getValue(), editMask));
            }
            box.setInitialText(calcInitialText(getInitialValue(), editMask));
            box.refreshTextOptions();
        }
    }

    protected InputBox.DisplayController<T> createDisplayController() {
        return new EditMaskDisplayController();
    }

    @Override
    protected IValEditor<T, V> createValEditor() {
        InternalInputBox<T, V> box = new InternalInputBox<>(getEnvironment(), createDisplayController(), createValueController(), this);
        box.addValueChangeListener(new ValueChangeListener<T>() {
            @Override
            public void onValueChanged(T oldValue, T newValue) {
                InputBoxController.this.valueEdited(newValue);
            }
        });
        setupValEditor(box);
        if (editMask != null) {
            applyEditMask(box);
        }
        return box;
    }

    protected String calcFocusedText(final T value, final V editMask) {
        if (editMask == null) {
            return value == null ? "" : null;
        } else {
            return editMask.isSpecialValue(value) ? "" : null;
        }
    }

    protected String calcInitialText(final T initialValue, final V editMask) {
        if (editMask == null) {
            return null;
        }
        final String focusedText = calcFocusedText(initialValue, editMask);
        if (focusedText == null) {
            if (editMask.isSpecialValue(initialValue)) {
                return "";
            } else {
                return editMask.toStr(getEnvironment(), initialValue);
            }
        } else {
            return focusedText;
        }
    }

    @Override
    protected ValidationResult calcValidationResult(T value) {
        ValidationResult result = super.calcValidationResult(value);        
        if (result == ValidationResult.ACCEPTABLE && !isReadOnly()) {
            final InputBox box = getInputBox();
            if (box!=null && !box.isPrintable()){
                final InvalidValueReason reason = InvalidValueReason.Factory.createForValueWithNonprintableChars(getEnvironment());
                result = ValidationResult.Factory.newInvalidResult(reason);
            }
        }
        return result;
    }

    @Override
    public void setEditMask(V editMask) {
        super.setEditMask(editMask);
        applyEditMask(getInputBox());
    }

    @Override
    public void close() {
        try {
            if (editingHistory != null) {
                editingHistory.flush();
            }
        } catch (EditingHistoryException ex) {
            getEnvironment().getTracer().error(ex);
        } finally {
            super.close();
        }

    }

    @Override
    protected void valueEdited(T value) {
        super.valueEdited(value);
        final InputBox<T> box = getInputBox();
        if (box!=null){
            box.setFocusedText(calcFocusedText(value, getEditMask()));
        }
        updateHistory();
    }

    public void setEditHistory(final IEditingHistory history, EValType type) {
        this.valType = type;
        if (history == null) {
            try {
                if (editingHistory != null) {
                    editingHistory.flush();
                }
            } catch (EditingHistoryException ex) {
                getEnvironment().getTracer().error(ex);
            }
            this.editingHistory = null;
        } else {
            this.editingHistory = history;
        }
        final InputBox<T> box = getInputBox();
        if (box!=null) {
            if (dropDownHistoryDelegate == null) {
                dropDownHistoryDelegate = new DropDownEditHistoryDelegate<>(getInputBox(), history, type);
                box.addDropDownDelegate(dropDownHistoryDelegate);
            } else {
                dropDownHistoryDelegate.updateItems(box, history, type);
            }
            updateHistory();
        }
    }

    public IEditingHistory getEditHistory() {
        return editingHistory;
    }

    public void updateHistory() {
        if (getValue() != null && dropDownHistoryDelegate != null && getEditHistory() != null && valType != null) {
            if (!dropDownHistoryDelegate.getItems().contains(getValue())) {
                String text = calcFocusedText(getValue(), editMask);

                if (text == null && editMask != null) {
                    text = editMask.toStr(getEnvironment(), getValue());
                }
                editingHistory.addEntry(text);                
                dropDownHistoryDelegate.updateItems(getInputBox(), editingHistory, valType);
            }
            try {
                editingHistory.flush();
            } catch (EditingHistoryException ex) {
                getEnvironment().getTracer().error(ex);
            }
        }
    }

    @Override
    public void setMandatory(final boolean mandatory) {
        if (isMandatory()!=mandatory){
            super.setMandatory(mandatory);
            final InputBox<T> box = getInputBox();
            if (box!=null){
                if (!isMandatory()) {
                    box.setClearController(new InputBox.ClearController<T>() {
                        @Override
                        public T clear() {
                            return null;
                        }
                    });
                } else {
                    box.setClearController(null);
                }
            }
        }
    }

    @Override
    public final void setReadOnly(final boolean readOnly) {//use afterChangeReadOnly
        super.setReadOnly(readOnly);
    }

    protected void afterChangeReadOnly() {
    }

    public final InputBox.ValueController<T> getValueController() {
        final InputBox<T> box = getInputBox();
        return box == null ? null : box.getValueController();
    }

    @Override
    public ValEditorController<T, V> getController() {
        return this;
    }
}
