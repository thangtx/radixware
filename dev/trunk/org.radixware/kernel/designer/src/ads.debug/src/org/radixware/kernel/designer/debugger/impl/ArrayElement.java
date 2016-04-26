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

package org.radixware.kernel.designer.debugger.impl;


public class ArrayElement extends VariableWrapper {

    private ValueWrapper value;
    private int index;

    public ArrayElement(int index, ValueWrapper w) {
        this.value = w;
        this.index = index;
    }

    @Override
    public String getDisplayName() {
        return "[" + index + "]";
    }

    @Override
    public String getTypeName() {
        return "";
    }

    @Override
    public ValueWrapper getValue() {
        return value;
    }
}
