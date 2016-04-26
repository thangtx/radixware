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
 *  9/21/11 9:15 AM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;


public interface IValueChangeable<TValue> {
    void addValueChangeListener(ValueChangeListener<TValue> listener);
    void removeValueChangeListener(ValueChangeListener<TValue> listener);

    /**
     * Indicates that value was changed.
     * @return true if value was changed by this or other editor
     */
    boolean isValueChanged();
}
