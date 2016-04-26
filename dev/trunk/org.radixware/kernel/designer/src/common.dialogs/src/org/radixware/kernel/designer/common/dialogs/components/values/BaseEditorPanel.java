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
 * 12/21/11 5:09 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.AdvancePanel;


abstract class BaseEditorPanel<TValue> extends AdvancePanel implements IValueEditor<TValue> {
    private final List<ValueChangeListener<TValue>> valueChangeSupport;

    private boolean editable = true;

    public BaseEditorPanel() {
        super();

        valueChangeSupport = new ArrayList<>();
    }

    protected void fireValueChange(TValue newValue, TValue oldValue) {
        for (ValueChangeListener<TValue> listener : valueChangeSupport) {
            listener.valueChanged(new ValueChangeEvent<>(this, newValue, oldValue));
        }
    }

    @Override
    public final void addValueChangeListener(ValueChangeListener<TValue> listener) {
        valueChangeSupport.add(listener);
    }

    @Override
    public final void removeValueChangeListener(ValueChangeListener<TValue> listener) {
        valueChangeSupport.remove(listener);
    }

    @Override
    public TValue getNullValue() {
        return null;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public boolean isSetValue() {
        return !Utils.equals(getValue(), getNullValue());
    }

    @Override
    public boolean isValidValue(TValue value) {
        return true;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (Component item : getComponents()) {
            item.setEnabled(enabled);
        }
    }
}
