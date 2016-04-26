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

/*
 * 10/20/11 3:26 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import java.awt.Component;
import javax.swing.event.EventListenerList;
import org.radixware.kernel.common.utils.Utils;

/**
 * Base component for all variations of value editors
 */
public abstract class ValueEditorComponent<TValue> implements IValueEditorComponent<TValue> {

    protected final EventListenerList eventListeners;
    protected ValueEditorModel<?, TValue> model;
    private boolean editable;

    public static abstract class SampleValueEditorModel<TValue> extends ValueEditorModel<TValue, TValue> {

        @Override
        protected TValue toExternal(TValue local) {
            return local;
        }

        @Override
        protected TValue toLocal(TValue value) {
            return value;
        }
    }

    public static abstract class ValueEditorModel<TLocalValue, TValue> implements IValueEditorModel<TValue> {

        protected static enum ValueSource {
            LOCAL, CACHE
        }

        protected final EventListenerList eventListeners;
        private boolean isValueChange;
        private TLocalValue localValue;
        private TValue value;
        protected ValueSource valueSource;

        public ValueEditorModel() {
            eventListeners = new EventListenerList();

            valueSource = ValueSource.LOCAL;
        }

        @Override
        public boolean isSetValue() {
            return !Utils.equals(getValue(), getNullValue());
        }

        @Override
        public boolean isValueChanged() {
            return getValueChange();
        }

        @Override
        public TValue getValue() {
            if (valueSource == ValueSource.LOCAL) {
                value = toExternal(getLocalValue());
                valueSource = ValueSource.CACHE;
            }
            return value;
        }

        @Override
        public void setValue(TValue value) {
            setValueInternal(value, true, true);
        }

        @Override
        public final void addValueChangeListener(ValueChangeListener<TValue> listener) {
            eventListeners.add(ValueChangeListener.class, listener);
        }

        @Override
        public final void removeValueChangeListener(ValueChangeListener<TValue> listener) {
            eventListeners.remove(ValueChangeListener.class, listener);
        }

        @Override
        public TValue getNullValue() {
            return null;
        }

        @Override
        public boolean isValidValue(TValue value) {
            return true;
        }

        @Override
        public void updateValue(Object... params) {
        }

        protected final void setValueInternal(TValue value, boolean compare, boolean fireChange) {
            TValue oldValue = getValue();
            if (!compare || !Utils.equals(getValue(), value)) {
                this.value = getAvalValue(value);
                if (!compare || !Utils.equals(getLocalValue(), toLocal(this.value))) {
                    setLocalValue(toLocal(this.value));
                    valueSource = ValueSource.CACHE;
                    if (fireChange) {
                        fireValueChange(this.value, oldValue);
                    }
                }
            }
        }

        protected final void setLocalValue(TLocalValue value, boolean fireChange) {
            TLocalValue oldLocalValue = getLocalValue();
            TValue oldValue = getValue();

            setLocalValue(value);
            if (fireChange && !Utils.equals(oldLocalValue, value)) {
                fireValueChange(toExternal(value), oldValue);
            }
        }

        protected final void setLocalValue(TLocalValue value) {
            localValue = value;
            valueSource = ValueSource.LOCAL;
        }

        public final TLocalValue getLocalValue() {
            return localValue;
        }

        protected TValue getAvalValue(TValue value) {
            return isValidValue(value) ? value : getNullValue();
        }

        /**
         * Convert value from external format to local
         * @param value in external format
         * @return value in local format
         */
        protected abstract TLocalValue toLocal(TValue value);

        /**
         * Convert value from local format to external
         * @param value in local format
         * @return value in external format
         */
        protected abstract TValue toExternal(TLocalValue local);

        public final void fireValueChange(TValue newValue, TValue oldValue) {

            isValueChange = true;

            for (ValueChangeListener<TValue> listener : eventListeners.getListeners(ValueChangeListener.class)) {
                listener.valueChanged(new ValueChangeEvent<TValue>(this, newValue, oldValue));
            }
        }
        protected final void setValueChange(boolean change) {
            isValueChange = change;
        }

        protected final boolean getValueChange() {
            return isValueChange;
        }

        protected void reset() {
            setValueChange(false);
            value = getNullValue();
            valueSource = ValueSource.CACHE;
        }
    }

    public ValueEditorComponent(final ValueEditorModel<?, TValue> model) {
        assert model != null : "null model";

        eventListeners = new EventListenerList();
        ValueChangeListener<TValue> listener = new ValueChangeListener<TValue>() {

            @Override
            public void valueChanged(ValueChangeEvent<TValue> event) {
                notifyModelChanged(event.newValue, event.oldValue);
            }
        };

        this.model = model;
        this.model.addValueChangeListener(listener);

        editable = true;
    }

    @Override
    public TValue getValue() {
        return model.getValue();
    }

    @Override
    public void setValue(TValue value) {
        model.setValue(value);
    }

    @Override
    public final void addValueChangeListener(ValueChangeListener<TValue> listener) {
        eventListeners.add(ValueChangeListener.class, listener);
    }

    @Override
    public final void removeValueChangeListener(ValueChangeListener<TValue> listener) {
        eventListeners.remove(ValueChangeListener.class, listener);
    }

    @Override
    public boolean isSetValue() {
        return model.isSetValue();
    }

    @Override
    public boolean isValueChanged() {
        return model.isValueChanged();
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public int getBaseline(int width, int height) {
        Component editor = getEditorComponent();

        if (editor != null) {
            return editor.getBaseline(width, height);
        }
        return 0;
    }

    @Override
    public boolean isFocusOwner() {
        Component editor = getEditorComponent();

        if (editor != null) {
            return editor.isFocusOwner();
        }
        return false;
    }

    @Override
    public void requestFocus() {
        Component editor = getEditorComponent();

        if (editor != null) {
            editor.requestFocusInWindow();
        }
    }

    @Override
    public TValue getNullValue() {
        return model.getNullValue();
    }

    @Override
    public boolean isValidValue(TValue value) {
        return model.isValidValue(value);
    }

    @Override
    public boolean isEnabled() {
        Component editor = getEditorComponent();
        if (editor != null) {
            return getEditorComponent().isEnabled();
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        Component editor = getEditorComponent();
        if (editor != null) {
            getEditorComponent().setEnabled(enabled);
        }
    }

    public final void fireValueChange(TValue newValue, TValue oldValue) {
        model.setValueChange(true);
        for (ValueChangeListener<TValue> listener : eventListeners.getListeners(ValueChangeListener.class)) {
            listener.valueChanged(new ValueChangeEvent<TValue>(this, newValue, oldValue));
        }
    }

    protected ValueEditorModel<?, TValue> getModel() {
        return model;
    }


    protected abstract void connectEditorComponent();

    protected abstract void disconnectEditorComponent();

    /**
     * Synchronize local value with editor's current value
     */
    protected abstract void updateModelValue();

    /**
     * Opposite {@link #updateLocalValue()}
     */
    protected abstract void updateEditorComponent();

    protected final void notifyModelOpened() {
        lockModelChange();
        updateEditorComponent();
        unLockModelChange();
    }

    protected final void notifyComponentChanged() {

        if (!isLockModelChange()) {
            lockComponentChange();
            updateModelValue();
            unLockComponentChange();
        }
    }

    protected final void notifyModelChanged(TValue newValue, TValue oldValue) {

        disconnectEditorComponent();

        if (!isLockComponentChange()) {
            lockModelChange();
            updateEditorComponent();
            unLockModelChange();
        }

        fireValueChange(newValue, oldValue);

        connectEditorComponent();
    }

    private boolean lockModelChange;
    protected final void lockModelChange() {
        lockModelChange = true;
    }

    protected final void unLockModelChange() {
        lockModelChange = false;
    }

    protected final boolean isLockModelChange() {
        return lockModelChange;
    }


    private boolean lockComponentChange;
    protected final void lockComponentChange() {
        lockComponentChange = true;
    }

    protected final void unLockComponentChange() {
        lockComponentChange = false;
    }

    protected final boolean isLockComponentChange() {
        return lockComponentChange;
    }
}
