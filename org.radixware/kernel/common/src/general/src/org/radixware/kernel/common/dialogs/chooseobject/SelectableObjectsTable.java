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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import org.radixware.kernel.common.utils.namefilter.ESearchType;
import org.radixware.kernel.common.utils.namefilter.NameFilterUtils;
import org.radixware.kernel.common.utils.namefilter.NameMatcher;
import org.radixware.kernel.common.utils.namefilter.NameMatcherFactory;

/**
 *
 * @author dlastochkin
 */
public class SelectableObjectsTable extends JTable {

    private final List<Integer> filteredIndexes;

    public SelectableObjectsTable(Collection<SelectableObjectDelegate> objects) {
        super(new SelectableObjectsTableModel(objects));

        this.setTableHeader(null);
        this.setShowGrid(false);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setFillsViewportHeight(true);
        this.setRowHeight(this.getRowHeight() + 4);
        this.setDefaultRenderer(Object.class, new SelectableObjectsTableCellRenderer());
        this.setRowSorter(new TableRowSorter<>((SelectableObjectsTableModel) this.getModel()));

        filteredIndexes = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            filteredIndexes.add(i);
        }
    }

    public void applyFilter(final String filterText, final boolean caseSensitive) {
        RowFilter<SelectableObjectsTableModel, Object> rowFilter;

        int size = filteredIndexes.size();
        filteredIndexes.clear();

        rowFilter = new RowFilter<SelectableObjectsTableModel, Object>() {

            @Override
            public boolean include(RowFilter.Entry<? extends SelectableObjectsTableModel, ? extends Object> entry) {
                String candidate = entry.getModel().getValueAt((Integer) entry.getIdentifier(), 0).toString();

                if (filterText == null || filterText.isEmpty()) {
                    filteredIndexes.add((Integer) entry.getIdentifier());
                    return true;
                }

                if (filterText.trim().isEmpty()) {
                    filteredIndexes.add((Integer) entry.getIdentifier());
                    return true;
                }

                final ESearchType searchKind;
                final boolean exact = filterText.endsWith(" ");
                String transormedFilter = filterText;

                if (exact) {
                    searchKind = (caseSensitive ? ESearchType.EXACT_NAME : ESearchType.CASE_INSENSITIVE_EXACT_NAME);
                } else if (NameFilterUtils.isAllUpper(filterText) && filterText.length() > 1) {
                    searchKind = ESearchType.CAMEL_CASE;
                } else if (NameFilterUtils.containsWildCard(filterText) != -1) {
                    transormedFilter = NameFilterUtils.transformWildCardsToJavaStyle(filterText);
                    searchKind = caseSensitive ? ESearchType.REGEXP : ESearchType.CASE_INSENSITIVE_REGEXP;
                } else {
                    searchKind = caseSensitive ? ESearchType.PREFIX : ESearchType.CASE_INSENSITIVE_PREFIX;
                }

                final NameMatcher matcher = NameMatcherFactory.createNameMatcher(transormedFilter, searchKind);
                if (matcher == null) {
                    return false;
                } else {
                    if (matcher.accept(candidate)) {
                        filteredIndexes.add((Integer) entry.getIdentifier());
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        };

        ((TableRowSorter<SelectableObjectsTableModel>) this.getRowSorter()).setRowFilter(rowFilter);
    }

    public List<SelectableObjectDelegate> getSelected() {
        List<SelectableObjectDelegate> result = new ArrayList<>();
        for (int index : this.getSelectedRows()) {
            int resultIndex = filteredIndexes.get(index);
            result.add((SelectableObjectDelegate) this.getModel().getValueAt(resultIndex, 0));
        }

        return result;
    }
    
    public List<SelectableObjectDelegate> getFiltered() {
        List<SelectableObjectDelegate> result = new ArrayList<>();
        for (int index : filteredIndexes) {
            result.add((SelectableObjectDelegate) this.getModel().getValueAt(index, 0));
        }

        return result;
    }
    
    public void addRow(SelectableObjectDelegate rowValue) {
        ((SelectableObjectsTableModel) this.getModel()).addRow(rowValue);
    }

}
