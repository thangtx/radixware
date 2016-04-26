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
 * 12.10.11 9:42
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import java.util.EventObject;


public class ValueChangeEvent<TValue> extends EventObject {

    public final TValue newValue;
    public final TValue oldValue;

    public ValueChangeEvent(Object source, TValue newValue, TValue oldValue) {
        super(source);

        this.newValue = newValue;
        this.oldValue = oldValue;
    }
}
