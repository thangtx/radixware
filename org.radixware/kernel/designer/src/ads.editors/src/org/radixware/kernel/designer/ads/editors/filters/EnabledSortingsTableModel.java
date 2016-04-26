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

package org.radixware.kernel.designer.ads.editors.filters;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.EnabledSorting;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.types.Id;


public class EnabledSortingsTableModel extends AbstractTableModel {

    private int availableSortingsSize;
    private List<AdsSortingDef> availableSortings;
    private AdsFilterDef adsFilterDef;
    private RadixObjects<EnabledSorting> enabledSortings;
    private List<Boolean> checkFlags;

    public EnabledSortingsTableModel(AdsFilterDef adsFilterDef) {
        super();

        this.adsFilterDef = adsFilterDef;
        this.enabledSortings = adsFilterDef.getEnabledSortings();
        this.availableSortings = adsFilterDef.getOwnerClass().getPresentations().getSortings().get(EScope.ALL);
        this.availableSortingsSize = availableSortings.size();

        this.checkFlags = new ArrayList<Boolean>(availableSortingsSize);

        for (AdsSortingDef xAvailableSortingDef : availableSortings){
            final Id xAvailableSortingDefId = xAvailableSortingDef.getId();
            boolean found = false;
            for (EnabledSorting xEnabledSorting : enabledSortings){
                if (xEnabledSorting.getSortingId().equals(xAvailableSortingDefId)){
                    found = true;
                    break;
                }
            }
            checkFlags.add(Boolean.valueOf(found));
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columnIndex == EnabledSortingsTableModel.CHECKBOX_COLUMN ? checkFlags.get(rowIndex) : availableSortings.get(rowIndex).getName();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        assert(columnIndex == EnabledSortingsTableModel.CHECKBOX_COLUMN);

        //it is a Boolean value
        assert (value instanceof Boolean);
        final Boolean flagIsChecked = (Boolean) value;
        checkFlags.set(rowIndex, flagIsChecked);

        if (flagIsChecked) {
            adsFilterDef.enableSorting(availableSortings.get(rowIndex));
        } else {
            final AdsSortingDef adsSortingDef = availableSortings.get(rowIndex);
            final Id adsSortingDefId = adsSortingDef.getId();
            for (int i = 0, count = enabledSortings.size(); i < count; ++i){
                if (enabledSortings.get(i).getSortingId().equals(adsSortingDefId)){
                    enabledSortings.remove(i);
                    break;
                }
            }
        }
        fireTableCellUpdated(rowIndex, columnIndex);
        fireTableCellUpdated(rowIndex, columnIndex + 1);
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return c == EnabledSortingsTableModel.CHECKBOX_COLUMN ? Boolean.class : String.class;
    }

    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    @Override
    public int getRowCount() {
        return availableSortingsSize;
    }

    @Override
    public String getColumnName(int column) {
        return column == EnabledSortingsTableModel.CHECKBOX_COLUMN ? checkBoxColumnName : nameColumnName;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !adsFilterDef.isReadOnly() && columnIndex == EnabledSortingsTableModel.CHECKBOX_COLUMN && !adsFilterDef.isAnyBaseSortingEnabled();
    }
    public static final int CHECKBOX_COLUMN = 0;
    public static final int NAME_COLUMN = 1;
    private static final int COLUMNS_COUNT = 2;
    private static final String checkBoxColumnName = "";
    private static final String nameColumnName = "Sorting";
}
