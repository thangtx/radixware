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

import org.netbeans.spi.debugger.ui.Constants;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;


public class ThreadsTableModel extends AbstractTableModel {

    @Override
    public Object getValueAt(Object node, String columnID) throws UnknownTypeException {
        if (node instanceof ThreadWrapper) {
            ThreadWrapper w = (ThreadWrapper) node;
            if (Constants.THREAD_STATE_COLUMN_ID.equals(columnID)) {
                return w.getStateName();
            } else if (Constants.THREAD_SUSPENDED_COLUMN_ID.equals(columnID)) {
                return w.isSuspended();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
}
