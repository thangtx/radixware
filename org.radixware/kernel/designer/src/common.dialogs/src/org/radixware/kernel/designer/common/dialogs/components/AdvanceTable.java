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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent.NamedContext;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;
import org.radixware.kernel.designer.common.dialogs.utils.SearchFieldAdapter;


public abstract class AdvanceTable<TModel extends AdvanceTableModel> extends SimpleTable {

    public interface ICellPainter<TTable extends AdvanceTable> {

        void paint(Component component, TTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column);
    }

    protected static abstract class AdvanceCellEditor extends AbstractCellEditor implements TableCellEditor {
    }

    protected static class StringEditor extends AdvanceCellEditor {

        private JTextField editor;

        public StringEditor() {
            super();
            editor = new JTextField();
        }

        @Override
        public String getCellEditorValue() {
            return editor.getText();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editor.setText(value != null ? value.toString() : "");
            return isSelected ? editor : null;
        }
    }

    public static abstract class NameEditor extends AdvanceCellEditor {

        private NameEditorComponent editor;

        public NameEditor() {
            super();
            editor = new NameEditorComponent();
        }

        @Override
        public String getCellEditorValue() {
            editor.commit();
            return editor.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                final NamedContext namedContext = getNamedContext(table, value, row, column);
                editor.open(namedContext);
                if (namedContext != null && namedContext.getName() != null) {
                    editor.getEditorComponent().setSelectionStart(0);
                    editor.getEditorComponent().setSelectionEnd(namedContext.getName().length());
                }
                return editor.getEditorComponent();
            }
            return null;
        }

        public final NameEditorComponent getEditor() {
            return editor;
        }

        protected abstract NameEditorComponent.NamedContext getNamedContext(JTable table, Object value, int row, int column);
    }

    public static abstract class TypeEditor extends AdvanceCellEditor {

        private TypeEditorComponent editor;

        public TypeEditor() {
            editor = new TypeEditorComponent();
            editor.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChanged(ValueChangeEvent e) {
                    stopCellEditing();
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            editor.commit();
            return editor.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                int modelRow = table.convertRowIndexToModel(row);
                editor.open(getTypeContext(table, value, modelRow, column), getCurrentValue(table, value, row, column));
                return editor.getEditorComponent();
            }
            return null;
        }

        protected abstract TypeEditorComponent.TypeContext getTypeContext(JTable table, Object value, int row, int column);

        protected abstract AdsTypeDeclaration getCurrentValue(JTable table, Object value, int row, int column);
    }

    protected static class DefaultColorCellRender extends DefaultTableCellRenderer {

        public DefaultColorCellRender() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ICellPainter pointer = ((AdvanceTable) table).getCellPainter();
            if (pointer != null) {
                pointer.paint(component, (AdvanceTable) table, value, isSelected, hasFocus, row, column);
            }
            return component;
        }
    }

    public static class RowSorter extends TableRowSorter<AdvanceTableModel> {

        private SortOrder currSortOrder;

        public RowSorter(AdvanceTableModel model) {
            super(model);
            currSortOrder = SortOrder.UNSORTED;
        }

        @Override
        public void toggleSortOrder(int column) {

            if (isSortable(column)) {
                List<SortKey> keys = new ArrayList<>(getSortKeys());
                SortKey sortKey;
                int sortIndex;
                for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--) {
                    if (keys.get(sortIndex).getColumn() == column) {
                        break;
                    }
                }
                if (sortIndex == -1) {
                    currSortOrder = SortOrder.ASCENDING;
                    sortKey = new SortKey(column, currSortOrder);
                    keys.add(0, sortKey);
                } else if (sortIndex == 0) {
                    SortKey key = toggle(keys.get(0));
                    currSortOrder = key.getSortOrder();
                    keys.set(0, key);
                } else {
                    keys.remove(sortIndex);
                    currSortOrder = SortOrder.ASCENDING;
                    keys.add(0, new SortKey(column, currSortOrder));
                }
                if (keys.size() > getMaxSortKeys()) {
                    keys = keys.subList(0, getMaxSortKeys());
                }
                setSortKeys(keys);
            }
        }

        public SortKey toggle(SortKey key) {
            if (key.getSortOrder() == SortOrder.ASCENDING) {
                return new SortKey(key.getColumn(), SortOrder.DESCENDING);
            } else if (key.getSortOrder() == SortOrder.DESCENDING) {
                return new SortKey(key.getColumn(), SortOrder.UNSORTED);
            }
            return new SortKey(key.getColumn(), SortOrder.ASCENDING);
        }

        public SortOrder getCurrentSortOrder() {
            return currSortOrder;
        }
    }

    public static final class SorterFactory {

        private SorterFactory() {
        }

        public static TableRowSorter<AdvanceTableModel> createInstance(AdvanceTableModel model, final JTextField searchComponent) {
            final TableRowSorter<AdvanceTableModel> sorter = new AdvanceTable.RowSorter(model);

            sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
                @Override
                public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {

                    final String filter = searchComponent.getText().toLowerCase();
                    final String token = entry.getValue(0).toString().toLowerCase();
                    return SearchFieldAdapter.isFitingToken(filter, token);
                }
            });

            return sorter;
        }
    }

    public AdvanceTable() {
        super();

        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        TableCellRenderer defRender = new DefaultColorCellRender();

        setDefaultRenderer(Object.class, defRender);
        setDefaultRenderer(String.class, defRender);
        setDefaultRenderer(Integer.class, defRender);
        setDefaultRenderer(Float.class, defRender);
        setDefaultRenderer(Double.class, defRender);

    }

    protected abstract AdvanceTableModel getDefauldModel();

    public ICellPainter getCellPainter() {
        return new ICellPainter() {
            @Override
            public void paint(Component component, AdvanceTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            }
        };
    }

    public final int[] convertRowIndexToModel(int[] rows) {
        int[] convert = new int[rows.length];

        for (int i = 0; i < rows.length; i++) {
            convert[i] = convertRowIndexToModel(rows[i]);
        }

        return convert;
    }

    public final int[] getModelSelectedRows() {
        return convertRowIndexToModel(getSelectedRows());
    }

    public final int getModelSelectedRow() {
        int row = getSelectedRow();
        if (row > -1) {
            return convertRowIndexToModel(row);
        } else {
            return -1;
        }
    }

    public void open(TModel model) {
        super.setModel(model);
    }

    public void deactivate() {
        editingStopped(null);
    }

    @Override
    public TableRowSorter<? extends AdvanceTableModel> getRowSorter() {
        return (TableRowSorter<? extends AdvanceTableModel>) super.getRowSorter();
    }

    @Override
    public final TModel getModel() {
        return (TModel) super.getModel();
    }

    /**
     *
     * @param dataModel must be instance of {@link AdvanceTableModel} or
     * <tt>null</tt>
     * @deprecated use {@link #open(AdvanceTableModel)} instead
     */
    @Override
    @Deprecated
    public final void setModel(TableModel dataModel) {
        if (dataModel instanceof AdvanceTableModel) {
            super.setModel(dataModel);
        } else if (dataModel == null) {
            super.setModel(getDefauldModel());
        } else {
//            throw new IllegalArgumentException();
        }
        afterSetModel();
    }

    @Override
    protected final AdvanceTableModel createDefaultDataModel() {
        return getDefauldModel();
    }

    protected void afterSetModel() {
    }
}
