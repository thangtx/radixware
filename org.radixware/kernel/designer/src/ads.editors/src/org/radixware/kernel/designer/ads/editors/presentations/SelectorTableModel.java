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

package org.radixware.kernel.designer.ads.editors.presentations;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;


public class SelectorTableModel extends AbstractTableModel {

    private AdsSelectorPresentationDef presentation;
    private EIsoLanguage[] languagesArray;
    private ArrayList<String> columnsNames;
    final int VISIBILITY_COLUMN = 0;
    final int PROP_COLUMN = 1;
    final int ALIGN_COLUMN = 2;
    final int SIZE_POLICY_COLUMN = 3;

    SelectorTableModel(final AdsSelectorPresentationDef presentation) {
        this.presentation = presentation;

        columnsNames = new ArrayList<String>();
        columnsNames.add(NbBundle.getMessage(SelectorTableModel.class, "SelectorVisibility"));
        columnsNames.add(NbBundle.getMessage(SelectorTableModel.class, "SelectorProperty"));
        columnsNames.add(NbBundle.getMessage(SelectorTableModel.class, "SelectorAlign"));
        columnsNames.add(NbBundle.getMessage(SelectorTableModel.class, "SelectorSizePolicy"));


        if (presentation != null) {
            final Layer layer = presentation.getModule().getSegment().getLayer();
            languagesArray = new EIsoLanguage[layer.getLanguages().size()];
            layer.getLanguages().toArray(languagesArray);

            for (EIsoLanguage language : languagesArray) {
                columnsNames.add(language.getName() + " " + NbBundle.getMessage(SelectorTableModel.class, "SelectorTitle"));
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == PROP_COLUMN) {
            return AdsSelectorPresentationDef.SelectorColumn.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !presentation.isReadOnly() && !presentation.isColumnsInherited();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames.get(column);
    }

    @Override
    public int getRowCount() {
        return presentation.getColumns().size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AdsSelectorPresentationDef.SelectorColumn column = presentation.getColumns().get(rowIndex);
        if (column != null) {
            if (columnIndex == VISIBILITY_COLUMN) {
                return column.getVisibility().getName();
            } else if (columnIndex == PROP_COLUMN) {
                return column;
            } else if (columnIndex == ALIGN_COLUMN) {
                return column.getAlign().getName();
            } else if (columnIndex == SIZE_POLICY_COLUMN) {
                return column.getSizePolicy().getName();
            } else if (columnIndex > SIZE_POLICY_COLUMN && columnIndex <= getColumnCount()) {
                String title = column.getTitle(languagesArray[columnIndex - SIZE_POLICY_COLUMN - 1]);
                return title;
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        AdsSelectorPresentationDef.SelectorColumn currentColumn = presentation.getColumns().get(rowIndex);
        if (currentColumn != null) {
            if (columnIndex == VISIBILITY_COLUMN) {
                ESelectorColumnVisibility visibility = ESelectorColumnVisibility.getForTitle(aValue.toString());
                if (!visibility.equals(currentColumn.getVisibility())) {
                    currentColumn.setVisibility(visibility);
                }
            } else if (columnIndex == ALIGN_COLUMN) {
                ESelectorColumnAlign align = ESelectorColumnAlign.getForTitle(aValue.toString());
                if (!align.equals(currentColumn.getAlign())) {
                    currentColumn.setAlign(align);
                }
            } else if (columnIndex == SIZE_POLICY_COLUMN) {
                ESelectorColumnSizePolicy sp = ESelectorColumnSizePolicy.getForTitle(aValue.toString());
                if (!sp.equals(currentColumn.getAlign())) {
                    currentColumn.setSizePolicy(sp);
                }
            } else if (columnIndex == PROP_COLUMN && aValue instanceof AdsSelectorPresentationDef.SelectorColumn) {
                RadixObjects<AdsSelectorPresentationDef.SelectorColumn> allColumns = presentation.getColumns();
                allColumns.remove(currentColumn);
                allColumns.add(rowIndex, (AdsSelectorPresentationDef.SelectorColumn) aValue);
            } else if (columnIndex > SIZE_POLICY_COLUMN && columnIndex < getColumnCount()) {
                Id titleId = currentColumn.getTitleId();
                //boolean isEmpty = aValue == null || aValue.toString().isEmpty();
                if (aValue != null) {
                    currentColumn.setTitle(languagesArray[columnIndex - SIZE_POLICY_COLUMN - 1], aValue.toString());
                } else if (titleId != null && aValue == null) {
                    currentColumn.setTitleId(null);
                }
            }
//            fireTableDataChanged();
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    public void removeRow(int row) {
        presentation.getColumns().remove(row);
        fireTableDataChanged();
    }

    public void addRow(int index, AdsPropertyDef property) {
        AdsSelectorPresentationDef.SelectorColumn newcolumn = AdsSelectorPresentationDef.SelectorColumn.Factory.newInstance((IAdsPresentableProperty) property);
        presentation.getColumns().add(index, newcolumn);
        fireTableDataChanged();
    }

    public void addRow(AdsPropertyDef property) {
        AdsSelectorPresentationDef.SelectorColumn newcolumn = AdsSelectorPresentationDef.SelectorColumn.Factory.newInstance((IAdsPresentableProperty) property);
        presentation.getColumns().add(newcolumn);
        fireTableDataChanged();
    }

    public int moveRow(int row, boolean up) {
        RadixObjects<AdsSelectorPresentationDef.SelectorColumn> columns = presentation.getColumns();
        AdsSelectorPresentationDef.SelectorColumn current = columns.get(row);
        columns.remove(row);
        final int n_row = up ? row - 1 : row + 1;
        columns.add(n_row, current);
        fireTableDataChanged();
        return n_row;
    }

    public void changeAlign(int row, String alignTitle) {
        if (row > -1 && row < getRowCount()) {
            presentation.getColumns().get(row).setAlign(ESelectorColumnAlign.getForTitle(alignTitle));
//            fireTableDataChanged();
            fireTableRowsUpdated(row, row);
        }
    }

    public void changeVisibility(int row, String visibilityTitle) {
        if (row > -1 && row < getRowCount()) {
            presentation.getColumns().get(row).setVisibility(ESelectorColumnVisibility.getForTitle(visibilityTitle));
//            fireTableDataChanged();
            fireTableRowsUpdated(row, row);
        }
    }
}
