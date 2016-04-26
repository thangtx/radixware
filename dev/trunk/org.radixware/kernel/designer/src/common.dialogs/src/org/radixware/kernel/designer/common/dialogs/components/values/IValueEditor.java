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
 * 9/20/11 6:05 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;


public interface IValueEditor<TValue> extends IValueChangeable<TValue> {

    TValue getValue();

    void setValue(TValue value);

    boolean isSetValue();

    boolean isValidValue(TValue value);

    /**
     * @return null-value at which the {@link isSetValue()} method returns false.
     * It is not necessarily the same as null
     */
    TValue getNullValue();

    boolean isEditable();

    void setEditable(boolean editable);
}
