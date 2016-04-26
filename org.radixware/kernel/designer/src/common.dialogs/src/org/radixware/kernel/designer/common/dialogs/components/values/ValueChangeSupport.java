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

package org.radixware.kernel.designer.common.dialogs.components.values;


public final class ValueChangeSupport<ValueType> extends EventsSupport<ValueChangeListener<ValueType>, ValueChangeEvent<ValueType>> {

    @Deprecated
    public final void addValueChangeListener(ValueChangeListener<ValueType> listener) {
        addListener(listener);
    }

    @Deprecated
    public final void removeValueChangeListener(ValueChangeListener<ValueType> listener) {
        removeListener(listener);
    }

    public void fireValueChange(Object source, ValueType newValue, ValueType oldValue) {

        fireChange(new ValueChangeEvent<>(source, newValue, oldValue));
    }

    @Override
    protected void performEvent(ValueChangeEvent<ValueType> event, ValueChangeListener<ValueType> listener) {
        listener.valueChanged(event);
    }
}
