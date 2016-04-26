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

package org.radixware.kernel.designer.ads.editors.sortings;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef.OrderBy;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.types.Id;


class PropertiesTableModel extends AbstractTableModel {

    private AdsSortingDef sorting;
    private Vector<Boolean> askCheckboxes;
    private Vector<String> actualPropertiesNames;
    private static final String wrongPropertiesName = "<Not Defined>";

    public PropertiesTableModel(AdsSortingDef sorting) {
        super();
        this.sorting = sorting;
        RadixObjects<OrderBy> order = sorting.getOrder();
        final int count = order.size();
        askCheckboxes = new Vector<Boolean>(count);
        actualPropertiesNames = new Vector<String>(count);
        for (int i = 0; i < count; ++i) {
            askCheckboxes.add(new Boolean(order.get(i).getOrder() == EOrder.DESC));
            final AdsPropertyDef adsPropertyDef = order.get(i).findProperty();
            actualPropertiesNames.add(adsPropertyDef == null ? wrongPropertiesName : adsPropertyDef.getName());
        }
    }

    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    @Override
    public int getRowCount() {
        return sorting.getOrder().size();
    }

    @Override
    public String getColumnName(int column) {
        return column == FIRST_COLUMN_INDEX ? FIRST_COLUMN_NAME : SECOND_COLUMN_NAME;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == FIRST_COLUMN_INDEX) {
            return actualPropertiesNames.get(rowIndex);
        } else {
            return askCheckboxes.get(rowIndex);
        }
    }

    public int getRowIndexByProperty(Object obj) {
        if (obj instanceof Id) {
            final Id propId = (Id) obj;
            RadixObjects<OrderBy> order = sorting.getOrder();
            for (int i = 0, count = order.size(); i < count; ++i) {
                final OrderBy orderBy = order.get(i);
                if (orderBy.getPropertyId().equals(propId)) {
                    return i;
                }
            }

        }
        return -1;
    }

    public OrderBy getOrderByRow(int rowIndex) {
        return sorting.getOrder().get(rowIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {

        assert (columnIndex == SECOND_COLUMN_INDEX);
        assert (value instanceof Boolean);
        final Boolean booleanValue = (Boolean) value;
        askCheckboxes.set(rowIndex, booleanValue);
        sorting.getOrder().get(rowIndex).setOrder(booleanValue ? EOrder.DESC : EOrder.ASC);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !sorting.getOrder().isReadOnly() && columnIndex == SECOND_COLUMN_INDEX;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == FIRST_COLUMN_INDEX ? String.class : Boolean.class;
    }

    public void addItem(Object obj) {
        if (obj instanceof Id) {
            AdsPropertyDef adsPropertyDef = sorting.getOwnerClass().getProperties().findById((Id) obj, EScope.ALL).get();
            final OrderBy newOrderBy = OrderBy.Factory.newInstance(adsPropertyDef);
            sorting.getOrder().add(newOrderBy);
            askCheckboxes.add(new Boolean(newOrderBy.getOrder() == EOrder.DESC));
            actualPropertiesNames.add(adsPropertyDef == null ? wrongPropertiesName : adsPropertyDef.getName());

            fireTableDataChanged();
        }
    }

    public void addItem(int index, Object obj){
        if (obj instanceof Id){
            AdsPropertyDef adsPropertyDef = sorting.getOwnerClass().getProperties().findById((Id) obj, EScope.ALL).get();
            final OrderBy newOrderBy = OrderBy.Factory.newInstance(adsPropertyDef);
            sorting.getOrder().add(index, newOrderBy);
            askCheckboxes.add(new Boolean(newOrderBy.getOrder() == EOrder.DESC));
            actualPropertiesNames.add(adsPropertyDef == null ? wrongPropertiesName : adsPropertyDef.getName());

            fireTableDataChanged();
        }
    }

    public void removeItem(Object obj) {
        if (obj instanceof Id) {
            final Id propsId = (Id) obj;
            RadixObjects<OrderBy> order = sorting.getOrder();
            for (int i = 0, count = order.size(); i < count; ++i) {
                final OrderBy orderBy = order.get(i);
                if (orderBy.getPropertyId().equals(propsId)) {
                    final int position = order.indexOf(orderBy);
                    assert (position != -1);
                    askCheckboxes.remove(position);
                    actualPropertiesNames.remove(position);
                    order.remove(orderBy);
                    break;
                }
            }
            fireTableDataChanged();
        }
    }

    public void moveUpItem(int rowIndex) {
        assert (rowIndex > 0 && rowIndex < getRowCount());

        sorting.getOrder().moveUp(rowIndex);
        java.util.Collections.swap(askCheckboxes, rowIndex, rowIndex - 1);
        java.util.Collections.swap(actualPropertiesNames, rowIndex, rowIndex - 1);

        fireTableDataChanged();
    }

    public void moveDownItem(int rowIndex) {
        assert (rowIndex < getRowCount() - 1 && rowIndex >= 0);

        sorting.getOrder().moveDown(rowIndex);
        java.util.Collections.swap(askCheckboxes, rowIndex, rowIndex + 1);
        java.util.Collections.swap(actualPropertiesNames, rowIndex, rowIndex + 1);

        fireTableDataChanged();
    }
    public static final int FIRST_COLUMN_INDEX = 0;
    public static final int SECOND_COLUMN_INDEX = 1;
    public static final String FIRST_COLUMN_NAME = "Property";
    public static final String SECOND_COLUMN_NAME = "Desc";
    private static final int COLUMNS_COUNT = 2;
}
