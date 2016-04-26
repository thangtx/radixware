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

package org.radixware.kernel.designer.ads.method.throwslist;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionModel;


final class ThrowsTableModel extends AbstractTableModel {

    public static ThrowsTableModel getModelFor(Set<ThrowsItem> list) {
        if (list != null) {
            return new ThrowsTableModel(list);
        }
        return new ThrowsTableModel(Collections.EMPTY_SET);
    }

    public static ThrowsTableModel getModelFor(AdsMethodThrowsList list) {
        Set<ThrowsItem> res = new HashSet<>();
        if (list != null) {
            for (int i = 0; i <= list.list().size() - 1; i++) {
                ThrowsItem throwsItem = new ThrowsItem(list.list().get(i));
                res.add(throwsItem);
            }
        }
        return new ThrowsTableModel(res);
    }

    private Set<ThrowsItem> currentThrowsItems;
    private AdsDefinition context;

    private ThrowsTableModel(Set<ThrowsItem> items) {
        currentThrowsItems = new TreeSet<>(ThrowsItem.THROWS_NAME_COMPARATOR);
        currentThrowsItems.addAll(items);
    }

    public void clear() {
        currentThrowsItems.clear();
        fireTableDataChanged();
    }

    public void setContext(AdsDefinition context) {
        this.context = context;
    }

    @Override
    public int getRowCount() {
        return currentThrowsItems.size();
    }

    public boolean contains(ThrowsItem item) {
        if (!currentThrowsItems.contains(item)) {
            for (ThrowsItem i : currentThrowsItems) {
                if (i.equals(item)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    Set<ThrowsItem> getAllThrows() {
        return Collections.unmodifiableSet(currentThrowsItems);
    }

    public void addValue(ThrowsItem item) {
        currentThrowsItems.add(item);
        fireTableDataChanged();
    }

    public void removeValue(ThrowsItem item) {
//        item.remove();
        currentThrowsItems.remove(item);
        fireTableDataChanged();
    }

    public ThrowsItem getValueAt(int row) {
        return (ThrowsItem) currentThrowsItems.toArray()[row];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return new CommonParametersEditorCellLib.TypePresentation(getValueAt(rowIndex).getType(), context);
        } else {
            return getValueAt(rowIndex).getDescriptionModel();
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue != null) {
            if (columnIndex == 0) {
                assert (aValue instanceof CommonParametersEditorCellLib.TypePresentation);
                getValueAt(rowIndex).setType(((CommonParametersEditorCellLib.TypePresentation) aValue).getType());
            }
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return CommonParametersEditorCellLib.TypePresentation.class;
        } else {
            return DescriptionModel.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return NbBundle.getMessage(ThrowsItem.class, "ThrowsTable-Name");
        } else {
            return NbBundle.getMessage(ThrowsItem.class, "ThrowsTable-Description");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}
