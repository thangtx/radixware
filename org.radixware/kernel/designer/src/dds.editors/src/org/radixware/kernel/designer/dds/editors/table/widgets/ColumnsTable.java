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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnsInfo;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsages;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfgPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;


public class ColumnsTable extends JTable {

    
    private class CheckIcon implements Icon{
        final RadixIcon checkRadixIcon = RadixWareIcons.CHECK.CHECK;
        int size = 14;
        boolean selected = false;
        
        
        public CheckIcon(boolean selected){
            this.selected = selected;
        }
        
        public CheckIcon(boolean selected, int size) {
            this.size = size;
            this.selected = selected;
        }


        public void setSize(int size) {
            this.size = size;
        }


        public void setSelected(boolean selected) {
            this.selected = selected;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (selected){
                checkRadixIcon.getIcon(size).paintIcon(c, g, x, y);
            }
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
        
        
    }

    public interface EditingColumnChangeListener {

        public void editingColumnChanged(DdsColumnDef column, boolean inherited);
    }

    private class ColumnsTableModel extends AbstractTableModel implements ContainerChangesListener {

        private class ColumnModel {

            DdsColumnDef column;
            boolean inherited;

            ColumnModel(DdsColumnDef column, boolean inherited) {
                this.column = column;
                this.inherited = inherited;
            }
        }
        private final DdsTableDef table;
        private final ArrayList<ColumnModel> columns = new ArrayList<ColumnModel>();
        private final String[] columnHeaders = {
            "T",
            NbBundle.getBundle(ColumnsTable.class).getString("COL_NAME"),
            NbBundle.getBundle(ColumnsTable.class).getString("COL_TYPE"),
            "P",
            "M",
            "F"
        };

        public ColumnsTableModel(DdsTableDef table) {
            this.table = table;
            updateData();
            table.getColumns().getLocal().getContainerChangesSupport().addEventListener(this);
        }

        private void updateData() {
            columns.clear();
            for (DdsTableDef curTable = table.findOverwritten(); curTable != null; curTable = curTable.findOverwritten()) {
                for (int i = 0; i < curTable.getColumns().getLocal().size(); ++i) // base columns must me displayed firstly.
                {
                    columns.add(i, new ColumnModel(curTable.getColumns().getLocal().get(i), true));
                }
            }
            for (DdsColumnDef col : table.getColumns().getLocal()) {
                columns.add(new ColumnModel(col, false));
            }
        }

        @Override
        public int getRowCount() {
            return columns.size();
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
                    return EValType.class;
                case 1:
                case 2:
                    return JLabel.class;
                case 3:
                case 4:
                    return Boolean.class;
                case 5:
                    return CheckIcon.class;
//                    return Boolean.class;
                default:
                    return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
//            return !readOnly && getColumnModelAt(rowIndex) != null && !getColumnModelAt(rowIndex).inherited &&
//                    (columnIndex == 1 || (columnIndex == 3 && !inherited) ||  columnIndex == 4);
            return isRowEditable(rowIndex) && columnIndex == 1;
        }

        public boolean isRowEditable(int row) {
            return !readOnly && getColumnModelAt(row) != null && !getColumnModelAt(row).inherited;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ColumnModel column = getColumnModelAt(rowIndex);
            if (column == null) {
                return null;
            }
            switch (columnIndex) {
                case 0:
                    return column.column.getValType();
                case 1: {
                    JLabel label = new JLabel(column.column.getName());
                    if (column.inherited) {
                        label.setForeground(Color.GRAY);
                    }

                    if (column.column.isDeprecated()) {
                        Font font = label.getFont();
                        Map attributes = font.getAttributes();
                        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                        label.setFont(new Font(attributes));
                    }

                    return label;
                }
                case 2: {
                    JLabel label = new JLabel(column.column.getDbType());
                    if (column.inherited) {
                        label.setForeground(Color.GRAY);
                    }
                    return label;
                }
                case 3:
                    return new Boolean(column.column.isPrimaryKey());
                case 4:
                    return new Boolean(column.column.isNotNull());
                case 5:
                        return new CheckIcon(column.column.isForeignKey());

//                    return new Boolean(column.column.isForeignKey());
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (!isCellEditable(rowIndex, columnIndex) && !(model.isRowEditable(rowIndex)
                    && ((columnIndex == 3 && !inherited) || columnIndex == 4))) {
                return;
            }
            final DdsColumnDef column = getColumnModelAt(rowIndex).column;
            final boolean inherited = getColumnModelAt(rowIndex).inherited;
            switch (columnIndex) {
                case 1:
                    String name = (String) aValue;
                    if (name != null && !name.isEmpty() && !name.equals(column.getName())) {
                        column.setName(name);
                        for (EditingColumnChangeListener listener : listeners) {
                            listener.editingColumnChanged(column, inherited);
                        }
                    }
                    break;
                case 3:
                    ColumnsInfo prKey = table.getPrimaryKey().getColumnsInfo();
                    if (((Boolean) aValue).booleanValue()) {
                        prKey.add(column, EOrder.ASC);
                        column.setNotNull(true);
                    } else {
                        DdsIndexDef.ColumnInfo rem = null;
                        for (DdsIndexDef.ColumnInfo columnInfo : prKey) {
                            if (Utils.equals(columnInfo.getColumnId(), column.getId())) {
                                rem = columnInfo;
                                break;
                            }
                        }
                        if (rem != null) {
                            prKey.remove(rem);
                        }
                    }
                    for (EditingColumnChangeListener listener : listeners) {
                        listener.editingColumnChanged(column, inherited);
                    }
                    break;
                case 4:
                    if (!((Boolean) aValue).booleanValue() && column.isPrimaryKey()) {
                        return;
                    }
                    column.setNotNull(((Boolean) aValue).booleanValue());
                    break;
            }
            fireTableRowsUpdated(rowIndex, rowIndex);
        }

        public ColumnModel getColumnModelAt(int row) {
            if (row >= getRowCount() || row < 0) {
                return null;
            }
            return columns.get(row);
        }

        public boolean canRaiseColumn(int idx) {
            return idx > 0 && !getColumnModelAt(idx).inherited && !getColumnModelAt(idx - 1).inherited;
        }

        public boolean canLowerColumn(int idx) {
            return idx < getRowCount() - 1 && !getColumnModelAt(idx).inherited && !getColumnModelAt(idx + 1).inherited;
        }

        public boolean canReplaceColumnsTo(int idx) {
            return idx >= 0 && idx < getRowCount() && !getColumnModelAt(idx).inherited;
        }

        public void replaceColumns(List<DdsColumnDef> cols, int idx) {
            if (!canReplaceColumnsTo(idx)) {
                return;
            }
            ColumnModel mod = getColumnModelAt(idx);
            idx = table.getColumns().getLocal().indexOf(mod.column);
            if (idx == -1) {
                return;
            }
            for (DdsColumnDef col : cols) {
                int pos = table.getColumns().getLocal().indexOf(col);
                if (pos != -1) {
                    table.getColumns().getLocal().remove(pos);
                    table.getColumns().getLocal().add(idx, col);
                    ++idx;
                }
            }
        }

        public void raiseColumn(DdsColumnDef column) {
            int idx = table.getColumns().getLocal().indexOf(column);
            if (idx != -1/* && canRaiseColumn(idx)*/) {
                table.getColumns().getLocal().remove(idx);
                table.getColumns().getLocal().add(idx - 1, column);
            }
        }

        public void lowerColumn(DdsColumnDef column) {
            int idx = table.getColumns().getLocal().indexOf(column);
            if (idx != -1/* && canLowerColumn(idx)*/) {
                table.getColumns().getLocal().remove(idx);
                table.getColumns().getLocal().add(idx + 1, column);
            }
        }

        public int getIndexOfColumn(DdsColumnDef column) {
            for (int i = 0; i < columns.size(); ++i) {
                if (Utils.equals(columns.get(i).column.getId(), column.getId())) {
                    return i;
                }
            }
            return -1;
        }

        public void removeListeners() {
            table.getColumns().getLocal().getContainerChangesSupport().removeEventListener(this);
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            updateData();
            fireTableDataChanged();
        }
    }

    private class ColumnsTableStringCellEditor extends DefaultCellEditor {

        private int curRow, curCol;

        public ColumnsTableStringCellEditor() {
            super(new JTextField());
            ((JTextField) getComponent()).addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    ColumnsTable.this.setValueAt(((JTextField) getComponent()).getText(), curRow, curCol);
                }
            });
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ColumnsTableStringCellEditor.this.stopCellEditing();
                }
            };
            KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
            ((JTextField) getComponent()).registerKeyboardAction(actionListener, enter, JComponent.WHEN_FOCUSED);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            curRow = row;
            curCol = column;
            final String name = ((JLabel) value).getText();
            final JTextField textField = (JTextField) super.getTableCellEditorComponent(table, name, isSelected, row, column);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            textField.setText(name);
                            textField.selectAll();
                            textField.requestFocusInWindow();
                        }
                    });
                }
            });
            return textField;
        }
    }

    private class ColumnsTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (hasFocus) {
                focusedRow = row;
                focusedCol = column;
            }
//            isSelected = false;
//            for (int i : table.getSelectedRows()) {
//                if (row == i) {
//                    isSelected = true;
//                    break;
//                }
//            }
            JComponent pattern = (JComponent) super.getTableCellRendererComponent(table, "NULL", isSelected, hasFocus, row, column);
            if (value instanceof EValType) {
                JLabel label = new JLabel(RadixObjectIcon.getForValType(((EValType) value)).getIcon());
                label.setOpaque(true);
                label.setBackground(pattern.getBackground());
                if (hasFocus) {
                    label.setBorder(pattern.getBorder());
                }
                return label;
            }
            if (value instanceof JLabel) {
                JLabel label = (JLabel) value;
                label.setOpaque(true);
                label.setBackground(pattern.getBackground());
                if (isSelected) {
                    label.setForeground(pattern.getForeground());
                }
                if (hasFocus) {
                    label.setBorder(pattern.getBorder());
                }
                return label;
            }
            if (value instanceof Icon){
                JComponent comp = (JComponent) table.getDefaultRenderer(Icon.class).
                        getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return comp;
            }
            if (value instanceof Boolean) {
                JComponent comp = (JComponent) table.getDefaultRenderer(Boolean.class).
                        getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return comp;
//                comp.setOpaque(true);
//                comp.setBackground(pattern.getBackground());
//                JPanel panel = new JPanel(new BorderLayout());
//                panel.add(comp, BorderLayout.CENTER);
//                if (hasFocus)
//                    panel.setBorder(pattern.getBorder());
//                return panel;
            }
            return pattern;
        }
    }
    private final ColumnsTableModel model;
    private final boolean inherited;
    private boolean readOnly = false;
    private int focusedRow = -1;
    private int focusedCol = -1;
    private final ArrayList<EditingColumnChangeListener> listeners = new ArrayList<EditingColumnChangeListener>();

    public ColumnsTable(DdsTableDef table) {
        super();
        inherited = table.findOverwritten() != null;

        this.setModel(model = new ColumnsTableModel(table));
        ColumnsTableCellRenderer renderer = new ColumnsTableCellRenderer();
        this.setAutoCreateColumnsFromModel(false);
        for (int i = 0; i < this.getColumnCount(); ++i) {
            this.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        ColumnsTableStringCellEditor stringEditor = new ColumnsTableStringCellEditor();
        this.getColumnModel().getColumn(1).setCellEditor(stringEditor);

        ((DefaultTableCellRenderer) this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setRowSelectionAllowed(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowHeight(24);

        int[] cols = {0, 3, 4, 5};
        for (int col : cols) {
            this.getColumnModel().getColumn(col).setMaxWidth(24);
            this.getColumnModel().getColumn(col).setMinWidth(24);
            this.getColumnModel().getColumn(col).setPreferredWidth(24);
            this.getColumnModel().getColumn(col).setResizable(false);
        }

        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        KeyStroke space = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
        final ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!model.isCellEditable(focusedRow, focusedCol) && !(model.isRowEditable(focusedRow)
                        && ((focusedCol == 3 && !inherited) || focusedCol == 4))) {
                    return;
                }
                if (focusedCol == 3 || focusedCol == 4) {
                    Boolean bool = (Boolean) model.getValueAt(focusedRow, focusedCol);
                    model.setValueAt(new Boolean(!bool.booleanValue()), focusedRow, focusedCol);
                } else if (focusedCol == 1) {
                    ColumnsTable.this.editCellAt(focusedRow, focusedCol);
                }
            }
        };
        this.registerKeyboardAction(actionListener, enter, JComponent.WHEN_FOCUSED);
        this.registerKeyboardAction(actionListener, space, JComponent.WHEN_FOCUSED);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row >= 0 && row < getRowCount() && col >= 0 && col < getColumnCount()
                        && model.isRowEditable(row) && ((col == 3 && !inherited) || col == 4)) {
                    Boolean bool = (Boolean) model.getValueAt(row, col);
                    model.setValueAt(new Boolean(!bool.booleanValue()), row, col);
                }
            }
        });

        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                DdsColumnDef column;
                boolean inherited;
                int idx = getCurrentColumnIdx();
                if (idx != -1) {
                    column = model.getColumnModelAt(idx).column;
                    inherited = model.getColumnModelAt(idx).inherited;
                    scrollToVisible(idx, 1);
                } else {
                    column = null;
                    inherited = false;
                }
                for (EditingColumnChangeListener listener : listeners) {
                    listener.editingColumnChanged(column, inherited);
                }
            }
        });
    }

    public void removeListeners() {
        model.removeListeners();
    }

    public int getInheritedColumnsCount() {
        int count = model.getRowCount();
        int ret = 0;
        for (int i = 0; i < count; ++i) {
            if (model.getColumnModelAt(i).inherited) {
                ++ret;
            }
        }
        return ret;
    }

    public void setSelectedColumn(final DdsColumnDef column) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
        int idx = model.getIndexOfColumn(column);
        if (idx != -1) {
            ColumnsTable.this.setRowSelectionInterval(idx, idx);
        }
//            }
//        });
    }

    public void scrollToVisible(int rowIndex, int colIndex) {
        if (!(getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) getParent();
        Rectangle rect = getCellRect(rowIndex, colIndex, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        viewport.scrollRectToVisible(rect);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean canRemoveSelectedColumns() {
        int[] idxs = this.getSelectedRows();
        if (idxs.length == 0) {
            return false;
        }
        for (int i : idxs) {
            if (model.getColumnModelAt(i).inherited) {
                return false;
            }
        }
        return true;
    }

    public boolean canRaiseSelectedColumn() {
        int[] idxs = this.getSelectedRows();
        if (idxs.length == 1) {
            return model.canRaiseColumn(idxs[0]);
        }
        return false;
    }

    public boolean canLowerSelectedColumn() {
        int[] idxs = this.getSelectedRows();
        if (idxs.length == 1) {
            return model.canLowerColumn(idxs[0]);
        }
        return false;
    }

    public void raiseSelectedColumn() {
        int idx = getCurrentColumnIdx();
        if (idx != -1) {
            DdsColumnDef column = model.getColumnModelAt(idx).column;
            model.raiseColumn(column);
            this.setRowSelectionInterval(idx - 1, idx - 1);
        }
    }

    public void lowerSelectedColumn() {
        int idx = getCurrentColumnIdx();
        if (idx != -1) {
            DdsColumnDef column = model.getColumnModelAt(idx).column;
            model.lowerColumn(column);
            this.setRowSelectionInterval(idx + 1, idx + 1);
        }
    }

    public void findUsages() {
        final int idx = getCurrentColumnIdx();
        if (idx != -1) {
            RequestProcessor.getDefault().post(new Runnable() {
                @Override
                public void run() {
                    DdsColumnDef column = model.getColumnModelAt(idx).column;
                    DdsTableDef ownerTable = column.getOwnerModel().getTables().findById(column.getOwnerTable().getId());
                    DdsColumnDef originalColumn = null;
                    if (ownerTable != null){
                        originalColumn = ownerTable.getColumns().getLocal().findById(column.getId());
                    } else {
                        DdsViewDef ownerView = column.getOwnerModel().getViews().findById(column.getOwnerTable().getId());
                        if (ownerView != null){
                            originalColumn = ownerView.getColumns().getLocal().findById(column.getId());
                        }
                    }
                    
                    if (originalColumn != null) {
                        final DdsColumnDef original = originalColumn;
                        SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    final FindUsagesCfg cfg = FindUsagesCfgPanel.askCfg(original);
                                    if (cfg != null) {
                                        RequestProcessor.getDefault().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                FindUsages.search(cfg);
                                            }
                                        });
                                    }
                                }
                            });
                    }
                        
                }
            });

        }
    }

    public void copyId() {
        int idx = getCurrentColumnIdx();
        if (idx != -1) {
            DdsColumnDef column = model.getColumnModelAt(idx).column;
            ClipboardUtils.copyToClipboard(column.getId().toString());
        }
    }

    public int getCurrentColumnIdx() {
        int ret = this.getSelectionModel().getAnchorSelectionIndex();
        if (ret >= this.getRowCount()) {
            ret = -1;
        }
        //FIXED: RADIX-3711 Пропадают поля редактирования при снятии выделения в таблице колонок
//        if (!this.isRowSelected(ret))
//            ret = this.getSelectedRow();
        return ret;
    }

    public void updateRow(int row) {
        model.fireTableRowsUpdated(row, row);
    }

    public void replaceColumns(List<DdsColumnDef> cols, int idx) {
        if (model.canReplaceColumnsTo(idx)) {
            model.replaceColumns(cols, idx);
            int end = idx + cols.size() - 1;
            if (end >= idx && end < this.getRowCount()) {
                this.setRowSelectionInterval(idx, end);
            }
        } else {
            int end = this.getRowCount() - 1;
            int beg = end - cols.size() + 1;
            if (beg >= 0 && beg <= end) {
                this.setRowSelectionInterval(beg, end);
            }
        }
    }

    public void addEditingColumnChangeListener(EditingColumnChangeListener listener) {
        listeners.add(listener);
    }

    public void removeEditingColumnChangeListener(EditingColumnChangeListener listener) {
        listeners.remove(listener);
    }

    public List<DdsColumnDef> getSelectedDdsColumns() {
        int[] idxs = this.getSelectedRows();
        ArrayList<DdsColumnDef> ret = new ArrayList<DdsColumnDef>(idxs.length);
        for (int idx : idxs) {
            ret.add(model.getColumnModelAt(idx).column);
        }
        return ret;
    }
}
