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

package org.radixware.kernel.designer.dds.editors.reference;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.ColumnsInfoItem;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.ColumnsInfoItems;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ColumnsTable extends JTable {

    private class ColumnsTableModel extends AbstractTableModel implements ContainerChangesListener {

        private final ColumnsInfoItems items;
        private final String[] headers = {
            NbBundle.getBundle(ColumnsTable.class).getString("PARENT_COLUMN"),
            NbBundle.getBundle(ColumnsTable.class).getString("CHILD_COLUMN")
        };

        public ColumnsTableModel(ColumnsInfoItems items) {
            this.items = items;
            items.getContainerChangesSupport().addEventListener(this);
        }

        public void unregisterContainerChangeListener() {
            items.getContainerChangesSupport().removeEventListener(this);
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return headers.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return headers[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return DdsColumnDef.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return !readOnly && (columnIndex != 0 || !basedOnKeys);
        }

        public ColumnsInfoItem getItemAt(int row) {
            if (row < 0 || row >= getRowCount()) {
                return null;
            }
            return items.get(row);
        }

        public void removeItem(ColumnsInfoItem item) {
            if (item != null) {
                items.remove(item);
            }
//            fireTableDataChanged();
        }

        public void assignIndex(DdsIndexDef index) {
            HashMap<Id, Id> map = new HashMap<Id, Id>(items.size());
            for (ColumnsInfoItem item : items) {
                DdsColumnDef pCol = item.findParentColumn();
                DdsColumnDef cCol = item.findChildColumn();
                if (pCol != null && cCol != null) {
                    map.put(pCol.getId(), cCol.getId());
                }
            }
            items.clear();
            for (ColumnInfo col : index.getColumnsInfo().list()) {
                ColumnsInfoItem item = ColumnsInfoItem.Factory.newInstance();
                Id id = col.getColumnId();
                item.setParentColumnId(id);
                if (map.containsKey(id)) {
                    item.setChildColumnId(map.get(id));
                }
                items.add(item);
            }
//            fireTableDataChanged();
        }

        public void addItem() {
            ColumnsInfoItem item = ColumnsInfoItem.Factory.newInstance();
            items.add(item);
//            fireTableDataChanged();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ColumnsInfoItem item = getItemAt(rowIndex);
            if (item == null) {
                return null;
            }
            if (columnIndex == 0) {
                return item.findParentColumn();
            } else {
                return item.findChildColumn();
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ColumnsInfoItem item = getItemAt(rowIndex);
            if (item == null) {
                return;
            }
            DdsColumnDef parCol = (DdsColumnDef) aValue;
            if (columnIndex == 0) {
                if (parCol == null) {
                    item.setChildColumnId(null);
                } else {
                    item.setParentColumnId(parCol.getId());
                    DdsColumnDef chlCol = item.findChildColumn();
                    if (chlCol != null) {
                        if (!Utils.equals(parCol.getValType(), chlCol.getValType())) {
                            item.setChildColumnId(null);
                        }
                    }
                }
            } else {
                if (parCol != null) {
                    item.setChildColumnId(parCol.getId());
                } else {
                    item.setChildColumnId(null);
                }
            }
            fireTableDataChanged();
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            fireTableDataChanged();
        }
    }

    private class ColumnsTableCellRenderer extends DefaultTableCellRenderer {

//        private final Border focusBorder;
        public ColumnsTableCellRenderer() {
//            JTable table = new JTable(1, 1);
//            JComponent pattern = (JComponent)super.getTableCellRendererComponent(table, "", false, true, 0, 0);
//            focusBorder = pattern.getBorder();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent pattern = (JComponent) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
            if (value == null) {
//                if (hasFocus)
//                    pattern.setBorder(focusBorder);
                return pattern;
            }
            DdsColumnDef ddsColumn = (DdsColumnDef) value;
            JLabel label = new JLabel(ddsColumn.getName());
            label.setOpaque(true);
            label.setIcon(RadixObjectIcon.getForValType(ddsColumn.getValType()).getIcon());
            label.setBackground(pattern.getBackground());
            if (hasFocus) {
                label.setBorder(pattern.getBorder());
            }
//                label.setBorder(focusBorder);
            return label;
        }
    }

    private class ColumnsComboBoxRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final JComponent pattern = (JComponent) super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
            if (value == null) {
                return pattern;
            }
            final DdsColumnDef ddsColumn = (DdsColumnDef) value;
            final JLabel label = new JLabel(ddsColumn.getName());
            label.setOpaque(true);
            label.setIcon(RadixObjectIcon.getForValType(ddsColumn.getValType()).getIcon());
            label.setBackground(pattern.getBackground());
            if (cellHasFocus) {
                label.setBorder(pattern.getBorder());
            }
            return label;
        }
    }

    private class ColumnsTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        private JComboBox comboBox;
//        private int curRow = -1, curCol = -1;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

//            curRow = row;
//            curCol = column;
            comboBox = new JComboBox(new DefaultComboBoxModel(getColumnsAt(row, column)));
            comboBox.setSelectedItem(value);
            comboBox.setRenderer(new ColumnsComboBoxRenderer());
//            comboBox.addItemListener(new ItemListener() {
//
//                @Override
//                public void itemStateChanged(ItemEvent e) {
//                    model.setValueAt(comboBox.getSelectedItem(), curRow, curCol);
//                }
//            });
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (comboBox.isValid() && comboBox.isShowing()) {
                        comboBox.requestFocusInWindow();
                        comboBox.showPopup();
                    }
                }
            });
            return comboBox;
        }

        @Override
        public Object getCellEditorValue() {
            return comboBox.getSelectedItem();
        }
    }

//    @Override
//    public boolean editCellAt(int row, int column, EventObject e) {
//        editor.setComboValues(getColumnsAt(row, column));
//        return super.editCellAt(row, column, e);
//    }
    private Object[] getColumnsAt(int row, int column) {
        DdsTableDef table;
        if (column == 0) {
            table = reference.findParentTable(reference);
        } else {
            table = reference.findChildTable(reference);
        }
        if (table == null) {
            return new Object[0];
        }
        ArrayList<DdsColumnDef> all = new ArrayList<DdsColumnDef>(table.getColumns().get(EScope.ALL));
//        for (int i = 0; i < model.getRowCount(); ++i) {
//            if (i != row)
//                all.remove(model.getValueAt(i, column));
//        }
        if (column == 1) {
            DdsColumnDef parentColumn = (DdsColumnDef) model.getValueAt(row, 0);
            if (parentColumn != null) {
                ArrayList<DdsColumnDef> rem = new ArrayList<DdsColumnDef>();
                for (DdsColumnDef col : all) {
                    if (!Utils.equals(col.getValType(), parentColumn.getValType())) {
                        rem.add(col);
                    }
                }
                for (DdsColumnDef col : rem) {
                    all.remove(col);
                }
            } else {
                all.clear();
            }
        }
        Object[] ret = new Object[all.size()];
        int p = 0;
        for (DdsColumnDef col : all) {
            ret[p++] = col;
        }
        return ret;
    }
    private final DdsReferenceDef reference;
    private boolean readOnly = false;
    private boolean basedOnKeys = false;
    private final ColumnsTableModel model;
//    private final JComboBox comboBox = new JComboBox();
//    private final TunedComboCellEditor editor = new TunedComboCellEditor(this, comboBox);
    private final ColumnsTableCellEditor editor;

    public ColumnsTable(DdsReferenceDef reference) {
        super();
        this.reference = reference;
        this.setModel(model = new ColumnsTableModel(reference.getColumnsInfo()));
        ColumnsTableCellRenderer renderer = new ColumnsTableCellRenderer();
        this.getColumnModel().getColumn(0).setCellRenderer(renderer);
        this.getColumnModel().getColumn(1).setCellRenderer(renderer);

        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellSelectionEnabled(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        this.setRowHeight(24);
        this.getTableHeader().setReorderingAllowed(false);

//        comboBox.setRenderer(new ColumnsComboBoxRenderer());
//        comboBox.addFocusListener(new FocusListener() {
//
//            @Override
//            public void focusGained(FocusEvent e) {
////                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                int row = ColumnsTable.this.getSelectedRow();
//                int col = ColumnsTable.this.getSelectedColumn();
//                model.setValueAt(editor.getCellEditorValue(), row, col);
//            }
//        });

        editor = new ColumnsTableCellEditor();
        this.getColumnModel().getColumn(0).setCellEditor(editor);
        this.getColumnModel().getColumn(1).setCellEditor(editor);
    }

    public void stopEditing() {
        editor.stopCellEditing();
    }

    public void unregisterContainerChangeListener() {
        model.unregisterContainerChangeListener();
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setBasedOnKeys(boolean basedOnKeys) {
        this.basedOnKeys = basedOnKeys;
    }

    public boolean canAddItem() {
        DdsTableDef child = reference.findChildTable(reference);
        DdsTableDef parent = reference.findParentTable(reference);
        if (child != null && parent != null) {
            int all = model.getRowCount();
            int cnt1 = child.getColumns().get(EScope.ALL).size();
            int cnt2 = parent.getColumns().get(EScope.ALL).size();
            return Math.min(cnt1, cnt2) > all;
        }
        return false;
    }

    public void assignIndex(DdsIndexDef index) {
        if (index != null) {
            model.assignIndex(index);
        }
    }

    public void autoAssignChildColumns() {
        DdsTableDef childTable = reference.findChildTable(reference);
        boolean canAdd = false, allowed = false, invoked = false;
        if (childTable != null) {
            canAdd = !childTable.isReadOnly();
        }
        for (int i = 0; i < model.getRowCount(); ++i) {
            ColumnsInfoItem item = model.getItemAt(i);
            if (item.findChildColumn() == null && item.findParentColumn() != null) {
                Object[] cols = getColumnsAt(i, 1);
                if (cols.length > 0) {
                    model.setValueAt(cols[0], i, 1);
                } else {
                    if (canAdd) {
                        if (!invoked) {
                            invoked = true;
                            allowed = DialogUtils.messageConfirmation(NbBundle.getBundle(ColumnsTable.class).getString("CONFIRM_ADD_COLUMN"));
                        }
                        if (allowed) {
                            DdsColumnDef column = DdsColumnDef.Factory.newInstance("NewColumn");
                            childTable.getColumns().getLocal().add(column);
                            if (column.isAutoDbName()) {
                                column.setDbName(column.calcAutoDbName());
                            }
                            column.setValType(item.findParentColumn().getValType());
                            model.setValueAt(column, i, 1);
                        }
                    }
                }
            }
        }
    }

    public void addItem() {
        model.addItem();
        int last = this.getRowCount() - 1;
        this.setRowSelectionInterval(last, last);
        this.setColumnSelectionInterval(0, 0);
//        scrollToVisible(last, 0);
    }

    public void removeSelectedItem() {
        int idx = this.getSelectedRow();
        if (idx != -1) {
            model.removeItem(model.getItemAt(idx));
        }
    }
}
