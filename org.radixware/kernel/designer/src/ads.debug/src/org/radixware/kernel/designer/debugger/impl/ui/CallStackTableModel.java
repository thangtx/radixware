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

package org.radixware.kernel.designer.debugger.impl.ui;

import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.impl.CallStackFrame;


public class CallStackTableModel extends AbstractTableModel {

    @Override
    public Object getValueAt(Object node, String columnID) throws UnknownTypeException {
        if (CALL_STACK_FRAME_LOCATION_COLUMN_ID.equals(columnID)) {
            if (node instanceof CallStackFrame) {
                return ((CallStackFrame) node).getDisplayName();
            } else {
                return node.toString();
            }
        } else {
            return "";
        }
    }
}
