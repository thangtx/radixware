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
package org.radixware.kernel.common.dialogs.chooseobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dlastochkin
 */
public class SelectableObjectsTableModel extends AbstractTableModel {

    List<SelectableObjectDelegate> objects;

    public SelectableObjectsTableModel(Collection<SelectableObjectDelegate> objects) {
        if (objects != null) {
            this.objects = new ArrayList<>(objects);
        } else {
            this.objects = new ArrayList<>();
        }
    }

    @Override
    public int getRowCount() {
        return objects.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0 || columnIndex == 1) {
            return objects.get(rowIndex);
        } else {
            return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return;
        }
        if (rowIndex >= objects.size()) {
            objects.add((SelectableObjectDelegate) aValue);
        } else {
            objects.set(rowIndex, (SelectableObjectDelegate) aValue);
        }
    }
    
    public void addRow(SelectableObjectDelegate rowValue) {
        objects.add(rowValue);
    }
    
}
