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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;


class TitleItemsTableModel extends AbstractTableModel {

    private List<AdsPropertyDef> availableProperties;
    private Boolean isCheckArray[];

    public TitleItemsTableModel(IAdsPropertiesListProvider propertiesListProvider) {
        super();

        this.availableProperties = propertiesListProvider.getAdsPropertiesList();
        final int count = getRowCount();
        isCheckArray = new Boolean[count];
        for (int i = 0; i < count; ++i) {
            isCheckArray[i] = Boolean.FALSE;
        }
    }

    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public final int getRowCount() {
        return availableProperties.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_CHECK_INDEX;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == COLUMN_CHECK_INDEX) {
            return isCheckArray[rowIndex];
        } else {
            return availableProperties.get(rowIndex).getName();
        }
    }

    public List<AdsPropertyDef> getSelectedProperties() {

        final List<AdsPropertyDef> result = new ArrayList<AdsPropertyDef>();
        for (int i = 0, rowsCount = getRowCount(); i < rowsCount; ++i) {
            if ((Boolean)getValueAt(i, COLUMN_CHECK_INDEX)) {
                assert(availableProperties.get(i) instanceof AdsPropertyDef);
                result.add( availableProperties.get(i));
            }
        }

        return result;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        assert (columnIndex == COLUMN_CHECK_INDEX && value instanceof Boolean);
        isCheckArray[rowIndex] = (Boolean) value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public static final String columnNames[] = new String[]{
        "", "Property"
    };
    
    public static final int COLUMN_CHECK_INDEX = 0,  COLUMN_PROPERTY_TITLE_INDEX = 1;
    private static final int COLUMNS_COUNT = 2;
}
