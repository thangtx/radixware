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
import org.radixware.kernel.designer.debugger.impl.ObjectReferenceWrapper;
import org.radixware.kernel.designer.debugger.impl.ThisReferenceWrapper;
import org.radixware.kernel.designer.debugger.impl.ValueWrapper;
import org.radixware.kernel.designer.debugger.impl.VariableWrapper;


public class LocalsViewTableModel extends AbstractTableModel {

    @Override
    public Object getValueAt(Object node, String columnID) throws UnknownTypeException {
        if (LOCALS_TYPE_COLUMN_ID.equals(columnID)) {
            if (node instanceof VariableWrapper) {
                ValueWrapper val = ((VariableWrapper) node).getValue();
                if (val == null) {
                    return "";
                }
                String valTypeName = val.getTypeName();
                if (valTypeName != null) {
                    return valTypeName;
                }
                return ((VariableWrapper) node).getTypeName();
            } else if (node instanceof ThisReferenceWrapper) {
                return ((ThisReferenceWrapper) node).getTypeName();
            } else {
                return "";
            }
        } else if (LOCALS_VALUE_COLUMN_ID.equals(columnID)) {
            if (node instanceof VariableWrapper) {
                Object val = ((VariableWrapper) node).getValue();
                return val == null ? null : val.toString();
            } else if (node instanceof ThisReferenceWrapper) {
                return ((ThisReferenceWrapper) node).toString();
            } else {
                return "";
            }
        } else if (LOCALS_TO_STRING_COLUMN_ID.equals(columnID)) {
            ValueWrapper wrapper = null;
            if (node instanceof VariableWrapper) {
                wrapper = ((VariableWrapper) node).getValue();
            } else if (node instanceof ThisReferenceWrapper) {
                wrapper = (ThisReferenceWrapper) node;
            } else {
                return "";
            }
            if (wrapper instanceof ObjectReferenceWrapper) {
                return ((ObjectReferenceWrapper) wrapper).getToStringValue();
            } else {
                return "";
            }
        } else {
            return "";
        }

    }
}
