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

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;
import org.radixware.wps.text.WpsTextOptions;


public abstract class ValEditorController<T, V extends EditMask> implements IValEditor<T, V> {

    private static class DefaultValueChangeListener<T> implements ValueEditor.ValueChangeListener<T> {

        private List<ValueEditor.ValueChangeListener<T>> listeners = null;

        @Override
        public void onValueChanged(final T oldValue, final T newValue) {
            if (listeners != null) {
                final List<ValueEditor.ValueChangeListener<T>> listenersList = new LinkedList<>(listeners);
                for (ValueChangeListener<T> l : listenersList) {
                    l.onValueChanged(oldValue, newValue);
                }
            }
        }

        public void addValueChangeListener(final ValueEditor.ValueChangeListener<T> listener) {
            if (listeners == null) {
                listeners = new LinkedList<>();
                listeners.add(listener);
            } else if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        public void removeValueChangeListener(final ValueEditor.ValueChangeListener<T> listener) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    public static class ValidationResultChangeListener {
        private List<ValidationResultChangeListener> listeners = null;

        public void onValidationResultChange(ValidationResult newResult) {
            if (listeners != null) {
                final List<ValidationResultChangeListener> listenersList = new LinkedList<>(listeners);
                for (ValidationResultChangeListener l : listenersList) {
                    l.onValidationResultChange(newResult);
                }
            }
        }

        public void addValidationResultChangeListener(final ValidationResultChangeListener l) {
            if (listeners == null) {
                listeners = new LinkedList<>();
                listeners.add(l);
            } else if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }

        public void removeValidationResultChangeListener(final ValidationResultChangeListener l) {
            if (listeners != null) {
                listeners.remove(l);
            }
        }
    }

    private static class DefaultStartChangeValueListener implements ValueEditor.StartChangeValueListener {

        private List<ValueEditor.StartChangeValueListener> listeners = null;

        public void addListener(final ValueEditor.StartChangeValueListener listener) {
            if (listeners == null) {
                listeners = new LinkedList<>();
                listeners.add(listener);
            } else if (!listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }

        public void removeListener(final ValueEditor.StartChangeValueListener listener) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        @Override
        public void onStartChangeValue() {
            if (listeners != null) {
                final List<ValueEditor.StartChangeValueListener> listenersList = new LinkedList<>(listeners);
                for (ValueEditor.StartChangeValueListener listener : listenersList) {
                    listener.onStartChangeValue();
                }
            }
        }
    }

    private static class DefaultFinishChangeValueListener implements ValueEditor.FinishChangeValueListener {

        private List<ValueEditor.FinishChangeValueListener> listeners = null;

        public void addListener(final ValueEditor.FinishChangeValueListener listener) {
            if (listeners == null) {
                listeners = new LinkedList<>();
                listeners.add(listener);
            } else if (!listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }

        public void removeListener(final ValueEditor.FinishChangeValueListener listener) {
            if (listener != null) {
                listeners.remove(listener);
            }
        }

        @Override
        public void onFinishChangeValue(final boolean valueAccepted) {
            if (listeners != null) {
                final List<ValueEditor.FinishChangeValueListener> listenersList = new LinkedList<>(listeners);
                for (ValueEditor.FinishChangeValueListener listener : listenersList) {
                    listener.onFinishChangeValue(valueAccepted);
                }
            }
        }
    }
    
    private static class DefaultUnacceptableInputListener implements ValueEditor.UnacceptableInputListener{
        
        private List<ValueEditor.UnacceptableInputListener> listeners = null;
        
        public void addListener(final ValueEditor.UnacceptableInputListener listener){
            if (listeners == null){
                listeners = new LinkedList<>();
                listeners.add(listener);
            }else if (!listeners.contains(listener)){
                listeners.remove(listener);
            }
        }
        
        public void removeListener(final ValueEditor.UnacceptableInputListener listener){
            if (listeners != null){
                listeners.remove(listener);
            }
        }

        @Override
        public void onUnacceptableInputChanged(final UnacceptableInput previous, final UnacceptableInput current) {
            if (listeners != null ){
                final List<ValueEditor.UnacceptableInputListener> listenersList = new LinkedList<>(listeners);
                for (ValueEditor.UnacceptableInputListener listener : listenersList){
                    listener.onUnacceptableInputChanged(previous, current);
                }
            }
        }                        
    }    
    
    private IValEditor<T, V> valEditor;
    private T value;
    private boolean canBeNull = true;
    protected V editMask;
    private final IClientEnvironment env;
    private ValidationResult internalValueStatus = ValidationResult.ACCEPTABLE, externalValueStatus = ValidationResult.ACCEPTABLE;
    private boolean isAutoValidationEnabled = true;
    private final DefaultValueChangeListener<T> changeValueListener = new DefaultValueChangeListener<>();
    private final DefaultStartChangeValueListener startChangeListener =
            new DefaultStartChangeValueListener();
    private final DefaultFinishChangeValueListener finishChangeListener =
            new DefaultFinishChangeValueListener();
    private final ValidationResultChangeListener validationResultChangeListener = 
            new ValidationResultChangeListener();    
    private final DefaultUnacceptableInputListener unacceptableInputListener = 
            new DefaultUnacceptableInputListener();

    protected ValEditorController(final IClientEnvironment env) {
        this.env = env;
    }

    protected IClientEnvironment getEnvironment() {
        return env;
    }

    @Override
    public void addValueChangeListener(final ValueChangeListener<T> listener) {
        changeValueListener.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(final ValueChangeListener<T> listener) {
        changeValueListener.removeValueChangeListener(listener);
    }

    @Override
    public void addStartChangeValueListener(final StartChangeValueListener listener) {
        startChangeListener.addListener(listener);
    }

    @Override
    public void removeStartChangeValueListener(final StartChangeValueListener listener) {
        startChangeListener.removeListener(listener);
    }

    @Override
    public void addFinishChangeValueListener(final FinishChangeValueListener listener) {
        finishChangeListener.addListener(listener);
    }

    @Override
    public void removeFinishChangeValueListener(final FinishChangeValueListener listener) {
        finishChangeListener.removeListener(listener);
    }

    @Override
    public void addUnacceptableInputListener(final UnacceptableInputListener listener) {
        unacceptableInputListener.addListener(listener);
    }

    @Override
    public void removeUnacceptableInputListener(final UnacceptableInputListener listener) {
        unacceptableInputListener.removeListener(listener);
    }        
    
    public void addValidationResultChangeListener(final ValidationResultChangeListener listener) {
        validationResultChangeListener.addValidationResultChangeListener(listener);
    }

    public void removeValidationResultChangeListener(final ValidationResultChangeListener listener) {
        validationResultChangeListener.removeValidationResultChangeListener(listener);
    }        

    protected abstract IValEditor<T, V> createValEditor();

    public IValEditor<T, V> getValEditor() {
        if (valEditor == null) {
            valEditor = createValEditor();
            valEditor.addValueChangeListener(changeValueListener);
            valEditor.addStartChangeValueListener(startChangeListener);
            valEditor.addFinishChangeValueListener(finishChangeListener);
            valEditor.addUnacceptableInputListener(unacceptableInputListener);
        }
        return valEditor;
    }

    @Override
    public void setValue(final T value) {
        this.value = value;
        getValEditor().setValue(value);
        //doValidation();
    }

    protected void valueEdited(final T value) {
        T oldValue = this.value;
        this.value = value;
        changeValueListener.onValueChanged(oldValue, this.value);
        doValidation();
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean hasAcceptableInput() {
        return getValEditor().hasAcceptableInput();
    }

    @Override
    public UnacceptableInput getUnacceptableInput() {
        return getValEditor().getUnacceptableInput();
    }

    @Override
    public void setInputText(final String inputText) {
        getValEditor().setInputText(inputText);
    }

    @Override
    public boolean checkInput(String messageTitle, String firstMessageLine) {
        return getValEditor().checkInput(messageTitle, firstMessageLine);
    }

    @Override
    public boolean checkInput() {
        return getValEditor().checkInput();
    }            

    @Override
    public final ValidationResult getValidationResult() {
        final ValidationResult selfValidationResult = getSelfValidationResult();
        return selfValidationResult == ValidationResult.ACCEPTABLE ? valEditor.getValidationResult() : selfValidationResult;
    }

    private ValidationResult getSelfValidationResult() {
        return externalValueStatus == ValidationResult.ACCEPTABLE ? internalValueStatus : externalValueStatus;
    }

    @Override
    public final void setValidationResult(final ValidationResult newValueStatus) {
        if (externalValueStatus != newValueStatus) {
            final ValidationResult oldResult = getSelfValidationResult();
            externalValueStatus = newValueStatus;
            if (oldResult != getSelfValidationResult()) {
                afterChangeValueStatus();
            }
        }
    }

    @Override
    public void setTextOptions(final WpsTextOptions options) {
        getValEditor().setTextOptions(options);
    }

    @Override
    public boolean addTextOptionsMarkers(ETextOptionsMarker... markers) {
        return getValEditor().addTextOptionsMarkers(markers);
    }

    @Override
    public boolean removeTextOptionsMarkers(ETextOptionsMarker... markers) {
        return getValEditor().removeTextOptionsMarkers(markers);
    }

    @Override
    public boolean setTextOptionsMarkers(ETextOptionsMarker... markers) {
        return getValEditor().setTextOptionsMarkers(markers);
    }

    @Override
    public EnumSet<ETextOptionsMarker> getTextOptionsMarkers() {
        return getValEditor().getTextOptionsMarkers();
    }

    @Override
    public void setTextOptionsProvider(final ITextOptionsProvider textOptionsProvider) {
        getValEditor().setTextOptionsProvider(textOptionsProvider);
    }

    @Override
    public void setDefaultTextOptions(final WpsTextOptions options) {
        getValEditor().setDefaultTextOptions(options);
    }

    @Override
    public void refreshTextOptions() {
        getValEditor().refreshTextOptions();
    }

    @Override
    public void setTextOptionsForMarker(final ETextOptionsMarker marker, final WpsTextOptions options) {
        getValEditor().setTextOptionsForMarker(marker, options);
    }

    @Override
    public WpsTextOptions getTextOptions() {
        return getValEditor().getTextOptions();
    }

    protected ValidationResult calcValidationResult(T value) {
        if (getEditMask() == null) {
            return ValidationResult.ACCEPTABLE;
        } else {
            return getEditMask().validate(getEnvironment(), value);
        }
    }

    protected final void doValidation() {
        if (isAutoValidationEnabled()) {
            final ValidationResult currentStatus = getSelfValidationResult();
            final ValidationResult newStatus = calcValidationResult(getValue());
            if (newStatus != currentStatus || internalValueStatus != newStatus) {
                internalValueStatus = newStatus;
                afterChangeValueStatus();
                validationResultChangeListener.onValidationResultChange(newStatus);
            }
        }
    }

    private void afterChangeValueStatus() {
        ValidationResult validationResult = getSelfValidationResult();
        if (validationResult == ValidationResult.ACCEPTABLE || isReadOnly()) {
            getValEditor().setValidationResult(ValidationResult.ACCEPTABLE);
        } else {
            getValEditor().setValidationResult(validationResult);
        }
    }

    @Override
    public boolean isReadOnly() {
        return getValEditor().isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getValEditor().setReadOnly(readOnly);
        if (getSelfValidationResult() != ValidationResult.ACCEPTABLE) {
            afterChangeValueStatus();
        }
    }

    public boolean isMandatory() {
        return !canBeNull;
    }

    public void setMandatory(boolean mandatory) {
        this.canBeNull = !mandatory;
    }

    public final boolean isAutoValidationEnabled() {
        return isAutoValidationEnabled;
    }

    public final void setAutoValidationEnabled(final boolean isEnabled) {
        isAutoValidationEnabled = isEnabled;
    }

    @Override
    public void refresh() {
        setValue(value);
    }

    public void close() {
        if (valEditor != null) {
            valEditor.removeValueChangeListener(changeValueListener);
            valEditor.removeStartChangeValueListener(startChangeListener);
            valEditor.removeFinishChangeValueListener(finishChangeListener);
        }
    }

    @Override
    public void addButton(IButton button) {
        getValEditor().addButton(button);
    }

    public void setEditMask(V editMask) {
        this.editMask = editMask;
        doValidation();
        getValEditor().refresh();
    }

    public V getEditMask() {
        return editMask;
    }

    @Override
    public void setToolTip(String toolTip) {
        getValEditor().setToolTip(toolTip);
    }

    @Override
    public void setInitialValue(final T value) {
        getValEditor().setInitialValue(value);
    }

    @Override
    public T getInitialValue() {
        return getValEditor().getInitialValue();
    }
}
