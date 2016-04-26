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

package org.radixware.kernel.designer.dds.editors.table.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnsInfo;
import org.radixware.kernel.common.enums.EOrder;


public class IndexColumnsTable extends JTable {

    private class IndexColumnsTableModel extends AbstractTableModel implements ContainerChangesListener {

        private ColumnsInfo columnsInfo = null;
        private final String[] columnHeaders = {
            NbBundle.getBundle(IndexColumnsTable.class).getString("COL_INDEX_COLUMN"),
            NbBundle.getBundle(IndexColumnsTable.class).getString("COL_DESC")
        };

        public IndexColumnsTableModel() {
        }

        public void setColumnsInfo(ColumnsInfo info) {
            if (columnsInfo != null) {
                columnsInfo.getContainerChangesSupport().removeEventListener(this);
            }
            columnsInfo = info;
            columnsInfo.getContainerChangesSupport().addEventListener(this);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return columnsInfo != null ? columnsInfo.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return columnHeaders.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnHeaders[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return DdsColumnDef.class;
                case 1:
                    return Boolean.class;
                default:
                    return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ColumnInfo info = getColumnInfoAt(rowIndex);
            if (info == null) {
                return null;
            }
            switch (columnIndex) {
                case 0:
                    return info.findColumn();
                case 1:
                    return new Boolean(info.getOrder() == EOrder.DESC);
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                Boolean val = (Boolean) aValue;
                getColumnInfoAt(rowIndex).setOrder(val.booleanValue() ? EOrder.DESC : EOrder.ASC);
                fireTableRowsUpdated(rowIndex, rowIndex);
            }
        }

        public ColumnInfo getColumnInfoAt(int row) {
            if (row >= getRowCount() || row < 0) {
                return null;
            }
            return columnsInfo.get(row);
        }

        public void removeColumnInfo(ColumnInfo info) {
            columnsInfo.remove(info);
        }

        public void raiseColumnInfo(ColumnInfo info) {
            int idx = columnsInfo.indexOf(info);
            if (idx != -1) {
                columnsInfo.remove(idx);
                columnsInfo.add(idx - 1, info);
            }
        }

        public void lowerColumnInfo(ColumnInfo info) {
            int idx = columnsInfo.indexOf(info);
            if (idx != -1) {
                columnsInfo.remove(idx);
                columnsInfo.add(idx + 1, info);
            }
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            fireTableDataChanged();
        }
    }

    private class IndexColumnsTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            isSelected = row == table.getSelectedRow();
            JComponent pattern = (JComponent) super.getTableCellRendererComponent(table, "NULL", isSelected, hasFocus, row, column);
            if (value instanceof DdsColumnDef) {
                DdsColumnDef ddsColumn = (DdsColumnDef) value;
                JLabel label = new JLabel(ddsColumn.getName());
                label.setOpaque(true);
                label.setIcon(RadixObjectIcon.getForValType(ddsColumn.getValType()).getIcon());
                label.setBackground(pattern.getBackground());
                label.setForeground(pattern.getForeground());
                if (hasFocus) {
                    label.setBorder(pattern.getBorder());
                }
                return label;
            }
            if (value instanceof Boolean) {
                JComponent comp = (JComponent) table.getDefaultRenderer(Boolean.class).
                        getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                comp.setOpaque(true);
                comp.setBackground(pattern.getBackground());
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(comp, BorderLayout.CENTER);
                if (hasFocus) {
                    panel.setBorder(pattern.getBorder());
                }
                return panel;
            }
            pattern.setForeground(Color.RED);
            return pattern;
        }
    }
    private final IndexColumnsTableModel model;

    public IndexColumnsTable() {
        super();
        this.setModel(model = new IndexColumnsTableModel());
        IndexColumnsTableCellRenderer renderer = new IndexColumnsTableCellRenderer();
        this.getColumnModel().getColumn(0).setCellRenderer(renderer);
        this.getColumnModel().getColumn(1).setCellRenderer(renderer);

        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setRowSelectionAllowed(true);
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowHeight(24);

        this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        this.getColumnModel().getColumn(1).setMaxWidth(50);
        this.getColumnModel().getColumn(1).setMinWidth(24);
        this.getColumnModel().getColumn(1).setPreferredWidth(50);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int idx = IndexColumnsTable.this.getSelectedRow();
                if (idx == -1) {
                    return;
                }
                Boolean bool = (Boolean) model.getValueAt(idx, 1);
                model.setValueAt(new Boolean(!bool.booleanValue()), idx, 1);
            }
        };
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        KeyStroke space = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
        this.registerKeyboardAction(actionListener, enter, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.registerKeyboardAction(actionListener, space, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int idx = IndexColumnsTable.this.getSelectedRow();
                if (idx != -1) {
                    scrollToVisible(idx, 0);
                }
            }
        });
    }

    public void setIndex(DdsIndexDef index) {
        model.setColumnsInfo(index.getColumnsInfo());
        this.clearSelection();
    }

    public void scrollToVisible(int rowIndex, int vColIndex) {
        if (!(getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) getParent();
        Rectangle rect = getCellRect(rowIndex, vColIndex, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        viewport.scrollRectToVisible(rect);
    }

    public void raiseSelectedColumn() {
        final int idx = this.getSelectedRow();
        if (idx == -1) {
            return;
        }
        ColumnInfo info = model.getColumnInfoAt(idx);
        model.raiseColumnInfo(info);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IndexColumnsTable.this.setRowSelectionInterval(idx - 1, idx - 1);
            }
        });
    }

    public void lowerSelectedColumn() {
        final int idx = this.getSelectedRow();
        if (idx == -1) {
            return;
        }
        ColumnInfo info = model.getColumnInfoAt(idx);
        model.lowerColumnInfo(info);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IndexColumnsTable.this.setRowSelectionInterval(idx + 1, idx + 1);
            }
        });
    }

    public void takeSelectedColumn() {
        final int idx = this.getSelectedRow();
        if (idx == -1) {
            return;
        }
        ColumnInfo info = model.getColumnInfoAt(idx);
        model.removeColumnInfo(info);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (idx < model.getRowCount()) {
                    IndexColumnsTable.this.setRowSelectionInterval(idx, idx);
                } else if (idx > 0) {
                    IndexColumnsTable.this.setRowSelectionInterval(idx - 1, idx - 1);
                } else {
                    IndexColumnsTable.this.clearSelection();
                }
            }
        });
    }
}
