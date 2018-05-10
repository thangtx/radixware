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
package org.radixware.kernel.utils.traceview.utils;

import java.util.Date;
import javax.swing.RowFilter;

/**
 *
 * @author danil
 */
public class ContextFilter extends RowFilter<Object, Object> {

    private ContextSet cs;
    private int column;

    public ContextFilter(ContextSet cs, int column) {
        this.column = column;
        this.cs = cs;
    }

    public boolean include(
            Entry<? extends Object, ? extends Object> value) {
        Object v = value.getValue(this.column);
        if (v instanceof ContextSet) {
            if (cs.filter((ContextSet) v)) {
                return true;
            }
        }
        return false;
    }
}
