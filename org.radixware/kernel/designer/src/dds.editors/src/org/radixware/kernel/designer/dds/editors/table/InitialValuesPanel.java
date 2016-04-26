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

package org.radixware.kernel.designer.dds.editors.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.jdesktop.swingx.JXDatePicker;
import org.openide.actions.CopyAction;
import org.openide.actions.PasteAction;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.IEnumDef.IItem;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.PropertyValueEditPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;


class InitialValuesPanel extends javax.swing.JPanel {

    private class InitialValuesTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final PropertyValueEditPanel editor = new PropertyValueEditPanel();
//        private final JComboBox comboBox = new JComboBox();
//        private final TunedComboCellEditor tunedEditor = new TunedComboCellEditor(initialValuesTable);
//        private int curRow = -1, curCol = -1;

        public InitialValuesTableCellEditor() {
//            editor.addChangeListener(new ChangeListener() {
//
//                @Override
//                public void stateChanged(ChangeEvent e) {
//                    model.setValueAt(editor.getValue(), curRow, curCol);
//                }
//            });
        }

        @Override
        public boolean isCellEditable(EventObject evt) {
            if (evt instanceof MouseEvent) {
                return ((MouseEvent) evt).getClickCount() >= 2;
            }
            return true;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

//            curRow = row;
//            curCol = column;
            DdsColumnDef col = model.getColumnAt(column);
            IEnumDef enumDef = getEnumForColumn(col);
            if (enumDef != null) {
//                comboBox.removeAllItems();
//                editor.setEnum(enumDef, (ValAsStr) value, initialValuesTable);
                editor.setNullAble(!col.isNotNull());
                editor.setEnum(enumDef, (ValAsStr) value);
//                String cur = null;
//                ValAsStr val = (ValAsStr)value;
//                String[] options = new String[enumDef.getItems().size()];
//                int i = 0;
//                for (IEnumDef.IItem item : enumDef.getItems().list(EScope.ALL)) {
//                    options[i++] = item.getName();
//                    if (Utils.equals(val, item.getValue())) {
//                        cur = item.getName();
//                    }
//                }
//                tunedEditor.setComboValues(options);
//                if (cur != null)
//                    return tunedEditor.getTableCellEditorComponent(table, cur, isSelected, row, column);
////                    comboBox.setSelectedItem(cur);
//                return tunedEditor.getComponent();
            } else {
//                if (value == null) {
//                    editor.setNullAble(true);
//                    editor.setValue(col.getValType(), null);
//                } else {
                editor.setNullAble(!col.isNotNull());
                editor.setValue(col.getValType(), (ValAsStr) value);
//                }
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    editor.requestFocusInWindow();
                }
            });
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
//            if (enumDef != null) {
//                String cur = (String)tunedEditor.getCellEditorValue();
            // FIXME:
//                IEnumDef.IItem item = null;//enumDef.getItems().findByName(cur);
//                return item != null ? item.getValue() : null;
//            }
            return editor.getValue();
        }
    }

    private class InitialValuesTableModel extends AbstractTableModel implements ContainerChangesListener {

        private final ArrayList<DdsColumnDef> columns;
        private int rowCount;

        public InitialValuesTableModel(ArrayList<DdsColumnDef> columns) {
            this.columns = columns;
            rowCount = 0;
            for (DdsColumnDef column : columns) {
                rowCount = Math.max(rowCount, column.getInitialValues().size());
            }
            for (DdsColumnDef column : columns) {
                while (column.getInitialValues().size() < rowCount) {
                    column.getInitialValues().add(getDefaultInitialValueForColumn(column));
                }
            }
        }

        private ValAsStr getDefaultInitialValueForColumn(DdsColumnDef column) {
            if (column.getDefaultValue() != null) {
                return column.getDefaultValue().getValAsStr();
            }
            final IEnumDef enumDef = getEnumForColumn(column);
            if (enumDef != null) {
                final List<? extends IItem> list = enumDef.getItems().list(EScope.ALL);
                if (!list.isEmpty()) {
                    return list.get(0).getValue();
                } else {
                    return null;
                }
            }
            if (column.isNotNull()) {
                if (column.getValType() == EValType.CHAR) {
                    return ValAsStr.Factory.newInstance(Character.valueOf('A'), EValType.CHAR);
                }
                if (column.getValType() == EValType.DATE_TIME) {
                    JXDatePicker picker = new JXDatePicker(new Date());
                    return ValAsStr.Factory.newInstance(picker.getDate(), EValType.DATE_TIME);
                }
                if (column.getValType() == EValType.INT) {
                    return ValAsStr.Factory.newInstance(Integer.valueOf(0), EValType.INT);
                }
                if (column.getValType() == EValType.NUM) {
                    return ValAsStr.Factory.newInstance(Integer.valueOf(0), EValType.NUM);
                }
                if (column.getValType() == EValType.STR) {
                    return ValAsStr.Factory.newInstance("", EValType.STR);
                }
                if (column.getValType() == EValType.BOOL) {
                    return ValAsStr.Factory.newInstance(Boolean.FALSE, EValType.BOOL);
                }
            }
            return null;
        }

        public DdsColumnDef getColumnAt(int column) {
            if (column < 0 || column >= getColumnCount()) {
                return null;
            }
            return columns.get(column);
        }

        @Override
        public int getRowCount() {
            return rowCount;
        }

        @Override
        public int getColumnCount() {
            return columns.size();
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columns.get(columnIndex).getName();
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return ValAsStr.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return !readOnly && !isValueInherited(rowIndex, columnIndex);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return columns.get(columnIndex).getInitialValues().get(rowIndex);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex >= getRowCount() || columnIndex < 0 || columnIndex >= getColumnCount()) {
                return;
            }
            ValAsStr valAsStr = (ValAsStr) aValue;
            DdsColumnDef col = getColumnAt(columnIndex);
            IEnumDef enumDef = getEnumForColumn(col);
            if (enumDef == null && valAsStr != null) {
                EValType type = col.getValType();
                Object obj = valAsStr.toObject(type);
                ValAsStr value;
                if (type == EValType.STR && ((String) obj).equals("")) {
                    value = null;
                } else {
                    value = ValAsStr.Factory.newInstance(obj, type);
                }
                col.getInitialValues().set(rowIndex, value);
            } else {
                col.getInitialValues().set(rowIndex, valAsStr);
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        public void raiseRow(int row) {
            if (row < 1 || row >= getRowCount()) {
                return;
            }
            for (DdsColumnDef column : columns) {
                ValAsStr up = column.getInitialValues().get(row - 1);
                ValAsStr cur = column.getInitialValues().get(row);
                column.getInitialValues().set(row - 1, cur);
                column.getInitialValues().set(row, up);
            }
            fireTableRowsUpdated(row - 1, row);
        }

        public void lowerRow(int row) {
            if (row < 0 || row >= getRowCount() - 1) {
                return;
            }
            for (DdsColumnDef column : columns) {
                ValAsStr down = column.getInitialValues().get(row + 1);
                ValAsStr cur = column.getInitialValues().get(row);
                column.getInitialValues().set(row + 1, cur);
                column.getInitialValues().set(row, down);
            }
            fireTableRowsUpdated(row, row + 1);
        }

        public int addRow(int after) {
            int index = after + 1;
            for (DdsColumnDef column : columns) {
                if (after + 1 > column.getInitialValues().size()) {
                    column.getInitialValues().add(getDefaultInitialValueForColumn(column));
                    index = column.getInitialValues().size() - 1;
                } else {
                    column.getInitialValues().add(after + 1, getDefaultInitialValueForColumn(column));
                }
            }
            ++rowCount;
            fireTableRowsInserted(index, index);
            return index;
        }

        public void deleteRow(int row) {
            if (row < 0 || row >= getRowCount()) {
                return;
            }
            for (DdsColumnDef column : columns) {
                column.getInitialValues().remove(row);
            }
            --rowCount;
            fireTableRowsDeleted(row, row);
        }

        public void addColumns(List<DdsColumnDef> cols) {
            for (DdsColumnDef col : cols) {
                while (col.getInitialValues().size() < rowCount) {
                    col.getInitialValues().add(getDefaultInitialValueForColumn(col));
                }
                columns.add(col);
            }
            if (!cols.isEmpty()) {
                fireTableStructureChanged();
            }
        }

        public void deleteColumn(int col) {
            if (col < 0 || col >= getColumnCount()) {
                return;
            }
            DdsColumnDef column = getColumnAt(col);
            column.getInitialValues().clear();
            columns.remove(col);
            fireTableStructureChanged();
        }

        public void clear() {
            ArrayList<DdsColumnDef> rem = new ArrayList<DdsColumnDef>();
            for (DdsColumnDef column : columns) {
                if (!inheritedColumns.containsKey(column) && !necessaryColumns.contains(column)) {
                    column.getInitialValues().clear();
                    rem.add(column);
                }
            }
            for (DdsColumnDef column : rem) {
                columns.remove(column);
            }
            for (DdsColumnDef column : columns) {
                while (column.getInitialValues().size() > maxInheritedRowCount) {
                    column.getInitialValues().remove(column.getInitialValues().size() - 1);
                }
            }
            rowCount = maxInheritedRowCount;
            fireTableStructureChanged();
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            List<DdsColumnDef> rem = new ArrayList<DdsColumnDef>();
            List<DdsColumnDef> all = table.getColumns().get(EScope.ALL);
            for (DdsColumnDef col : columns) {
                if (!all.contains(col)) {
                    rem.add(col);
                }
            }
            if (!rem.isEmpty()) {
                for (DdsColumnDef col : rem) {
                    columns.remove(col);
                }
                fireTableStructureChanged();
            }
        }
    }

    private class InitialValuesTableCellRenderer extends DefaultTableCellRenderer {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            DdsColumnDef col = model.getColumnAt(column);
            ValAsStr val = (ValAsStr) value;
            String str = "NULL";

            AdsEnumDef enumDef = getEnumForColumn(col);
            if (enumDef != null) {
                for (IEnumDef.IItem item : enumDef.getItems().list(EScope.ALL)) {
                    if (Utils.equals(item.getValue(), val)) {
                        str = item.getName();
                        break;
                    }
                }
            } else if (val != null && col.getValType() == EValType.DATE_TIME) {
                try {
                    Timestamp timestamp = (Timestamp) val.toObject(EValType.DATE_TIME);
                    Date date = new Date(timestamp.getTime());
                    str = dateFormat.format(date);
                } catch (WrongFormatError e) {
                    str = val.toString();
                }
            } else {
                str = val != null ? val.toString() : str;
            }

            JComponent comp = (JComponent) super.getTableCellRendererComponent(table, str, isSelected, hasFocus, row, column);
            if (isValueInherited(row, column)) {
                comp.setForeground(Color.DARK_GRAY);
            }
            return comp;
        }
    }
    private final DdsTableDef table;
    private final InitialValuesTableModel model;
    private final InitialValuesTableCellEditor editor;
    private final HashMap<DdsColumnDef, Integer> inheritedColumns;
    private final HashSet<DdsColumnDef> necessaryColumns;
    private final Map<DdsColumnDef, AdsEnumDef> enumForColumn = new HashMap<DdsColumnDef, AdsEnumDef>();
    private boolean readOnly = false;
    private int maxInheritedRowCount;
    private Object copiedValue = null;
    private EValType copiedType = null;

    /**
     * Creates new form InitialValuesPanel
     */
    public InitialValuesPanel(DdsTableDef table) {
        initComponents();
        this.table = table;

        addColumnButton.setIcon(RadixWareIcons.CREATE.ADD_COLUMN.getIcon(20, 20));
        removeColumnButton.setIcon(RadixWareIcons.DELETE.DELETE_COLUMN.getIcon(20, 20));
        addRowButton.setIcon(RadixWareIcons.CREATE.ADD_ROW.getIcon(20, 20));
        removeRowButton.setIcon(RadixWareIcons.DELETE.DELETE_ROW.getIcon(20, 20));
        clearButton.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon(20, 20));
        upButton.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon(20, 20));
        downButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(20, 20));

        List<DdsColumnDef> inhCols = getInheritedColumns(table);
        inheritedColumns = new HashMap<DdsColumnDef, Integer>(inhCols.size());
        maxInheritedRowCount = 0;
        for (DdsColumnDef col : inhCols) {
            inheritedColumns.put(col, Integer.valueOf(col.getInitialValues().size()));
            maxInheritedRowCount = Math.max(maxInheritedRowCount, col.getInitialValues().size());
        }

        List<DdsColumnDef> allCols = getAllColumns(table);
        ArrayList<DdsColumnDef> cols = new ArrayList<DdsColumnDef>();
        necessaryColumns = new HashSet<DdsColumnDef>();
        for (DdsColumnDef column : allCols) {
            if (column.isNotNull() && column.getDefaultValue() == null) {
                necessaryColumns.add(column);
                cols.add(column);
            } else if (column.getInitialValues().size() > 0) {
                cols.add(column);
            }
        }
        model = new InitialValuesTableModel(cols);
        table.getColumns().getLocal().getContainerChangesSupport().addEventListener(model);
        editor = new InitialValuesTableCellEditor();

        initTable();
        initialValuesTable.clearSelection();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateButtonsEnableState();
            }
        });
    }

    public void completeEditing() {
        editor.stopCellEditing();
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        addColumnButton.setEnabled(!readOnly);
        addRowButton.setEnabled(!readOnly);
        removeColumnButton.setEnabled(!readOnly);
        removeRowButton.setEnabled(!readOnly);
        clearButton.setEnabled(!readOnly);
        upButton.setEnabled(!readOnly);
        downButton.setEnabled(!readOnly);
    }

    private boolean isValueInherited(int row, int column) {
        DdsColumnDef col = model.getColumnAt(column);
        if (inheritedColumns.containsKey(col)) {
            return row < inheritedColumns.get(col).intValue();
        }
        return false;
    }

    private AdsEnumDef getEnumForColumn(DdsColumnDef column) {
        if (!enumForColumn.containsKey(column)) {
            enumForColumn.put(column, AdsEnumUtils.findColumnEnum(column));
        }
        return enumForColumn.get(column);
    }

    private List<DdsColumnDef> getInheritedColumns(DdsTableDef table) {
        ArrayList<DdsColumnDef> ret = new ArrayList<DdsColumnDef>();
        for (DdsTableDef curTable = table.findOverwritten(); curTable != null; curTable = curTable.findOverwritten()) {
            for (int i = 0; i < curTable.getColumns().getLocal().size(); ++i) { // base columns must me displayed firstly.
                ret.add(i, curTable.getColumns().getLocal().get(i));
            }
        }
        return ret;
    }

    private List<DdsColumnDef> getAllColumns(DdsTableDef table) {
        ArrayList<DdsColumnDef> ret = new ArrayList<DdsColumnDef>(getInheritedColumns(table));
        for (DdsColumnDef col : table.getColumns().getLocal()) {
            ret.add(col);
        }
        return ret;
    }

    private class TablePopupMenu extends JPopupMenu {

        public TablePopupMenu() {
            super();
            AbstractAction action = new AbstractAction("Copy", SystemAction.get(CopyAction.class).getIcon()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    copyValue();
                }
            };
            JMenuItem menuItem = new JMenuItem(action);
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
            menuItem.setAccelerator(keyStroke);
            add(menuItem);
            action = new AbstractAction("Paste", SystemAction.get(PasteAction.class).getIcon()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pasteValue();
                }
            };
            menuItem = new JMenuItem(action);
            keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
            menuItem.setAccelerator(keyStroke);
            add(menuItem);
        }
    }

    private void copyValue() {
        if (initialValuesTable.getSelectedRow() != -1) {
            int row = initialValuesTable.getSelectedRow();
            int col = initialValuesTable.getSelectedColumn();
            copiedValue = initialValuesTable.getValueAt(row, col);
            copiedType = model.getColumnAt(col).getValType();
        }
    }

    private void pasteValue() {
        if (initialValuesTable.getSelectedRow() != -1) {
            int row = initialValuesTable.getSelectedRow();
            int col = initialValuesTable.getSelectedColumn();
            if (copiedType == model.getColumnAt(col).getValType() && model.isCellEditable(row, col)) {
                initialValuesTable.setValueAt(copiedValue, row, col);
            }
        }
    }

    private void initTable() {
        initialValuesTable.setModel(model);
        initialValuesTable.setDefaultRenderer(ValAsStr.class, new InitialValuesTableCellRenderer());
        initialValuesTable.setDefaultEditor(ValAsStr.class, editor);
        initialValuesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        initialValuesTable.setCellSelectionEnabled(true);
        initialValuesTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        initialValuesTable.setRowHeight(24);
        initialValuesTable.getTableHeader().setReorderingAllowed(false);
        initialValuesTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateButtonsEnableState();
                    }
                });
            }
        });
        initialValuesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateButtonsEnableState();
                    }
                });
            }
        });
        initialValuesTable.setComponentPopupMenu(new TablePopupMenu());
        InputMap inputMap = initialValuesTable.getInputMap();
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(keyStroke, "copy");
        ActionMap actionMap = initialValuesTable.getActionMap();
        actionMap.put("copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyValue();
            }
        });
        KeyStroke keyStroke2 = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(keyStroke2, "paste");
        actionMap.put("paste", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasteValue();
            }
        });
    }

    private void updateButtonsEnableState() {
        int row = initialValuesTable.getSelectedRow();
        int col = initialValuesTable.getSelectedColumn();
        removeColumnButton.setEnabled(!readOnly && row != -1
                && !inheritedColumns.containsKey(model.getColumnAt(col)) && !necessaryColumns.contains(model.getColumnAt(col)));
        removeRowButton.setEnabled(!readOnly && row != -1 && row >= maxInheritedRowCount);
        clearButton.setEnabled(!readOnly && initialValuesTable.getRowCount() > 0);
        upButton.setEnabled(!readOnly && row > maxInheritedRowCount);
        downButton.setEnabled(!readOnly && row >= maxInheritedRowCount && row < initialValuesTable.getRowCount() - 1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        initialValuesTable = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        addColumnButton = new javax.swing.JButton();
        removeColumnButton = new javax.swing.JButton();
        addRowButton = new javax.swing.JButton();
        removeRowButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        initialValuesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollPane.setViewportView(initialValuesTable);

        addColumnButton.setText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.addColumnButton.text")); // NOI18N
        addColumnButton.setToolTipText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.addColumnButton.toolTipText")); // NOI18N
        addColumnButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addColumnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addColumnButtonActionPerformed(evt);
            }
        });

        removeColumnButton.setText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.removeColumnButton.text")); // NOI18N
        removeColumnButton.setToolTipText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.removeColumnButton.toolTipText")); // NOI18N
        removeColumnButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeColumnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeColumnButtonActionPerformed(evt);
            }
        });

        addRowButton.setText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.addRowButton.text")); // NOI18N
        addRowButton.setToolTipText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.addRowButton.toolTipText")); // NOI18N
        addRowButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowButtonActionPerformed(evt);
            }
        });

        removeRowButton.setText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.removeRowButton.text")); // NOI18N
        removeRowButton.setToolTipText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.removeRowButton.toolTipText")); // NOI18N
        removeRowButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRowButtonActionPerformed(evt);
            }
        });

        clearButton.setText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.clearButton.text")); // NOI18N
        clearButton.setToolTipText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.clearButton.toolTipText")); // NOI18N
        clearButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        upButton.setText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.upButton.text")); // NOI18N
        upButton.setToolTipText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.upButton.toolTipText")); // NOI18N
        upButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.downButton.text")); // NOI18N
        downButton.setToolTipText(org.openide.util.NbBundle.getMessage(InitialValuesPanel.class, "InitialValuesPanel.downButton.toolTipText")); // NOI18N
        downButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addColumnButton)
                    .addComponent(removeColumnButton)
                    .addComponent(addRowButton)
                    .addComponent(removeRowButton)
                    .addComponent(clearButton)
                    .addComponent(upButton)
                    .addComponent(downButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addColumnButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeColumnButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addRowButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeRowButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton)
                        .addGap(18, 18, 18)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addColumnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addColumnButtonActionPerformed
        editor.stopCellEditing();
        final IFilter<DdsColumnDef> filter = DdsVisitorProviderFactory.newColumnForInitialValuesFilter(table);
        final Collection<DdsColumnDef> avalaibleColumns = table.getColumns().get(EScope.ALL, filter);
        for (int i = 0; i < model.getColumnCount(); ++i) {
            avalaibleColumns.remove(model.getColumnAt(i));
        }
        final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(avalaibleColumns);
        final List<Definition> selected = ChooseDefinition.chooseDefinitions(cfg);
        if (selected != null && !selected.isEmpty()) {
            final List<DdsColumnDef> selectedColumns = new ArrayList<DdsColumnDef>();
            for (Definition def : selected) {
                selectedColumns.add((DdsColumnDef) def);
            }
            model.addColumns(selectedColumns);
        }
    }//GEN-LAST:event_addColumnButtonActionPerformed

    private void removeColumnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeColumnButtonActionPerformed
        editor.stopCellEditing();
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(InitialValuesPanel.class).getString("REMOVE_COLUMN_CONFIRM"))) {
            return;
        }
        int col = initialValuesTable.getSelectedColumn();
        model.deleteColumn(col);
        if (col < model.getColumnCount()) {
            initialValuesTable.setColumnSelectionInterval(col, col);
        } else if (col > 0) {
            initialValuesTable.setColumnSelectionInterval(col - 1, col - 1);
        } else {
            initialValuesTable.clearSelection();
        }
        updateButtonsEnableState();
    }//GEN-LAST:event_removeColumnButtonActionPerformed

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        editor.stopCellEditing();
        int index = initialValuesTable.getSelectedRow();
        if (index < 0) {
            index = initialValuesTable.getRowCount();
        }
        index = model.addRow(index);
        if (model.getColumnCount() > 0) {
            initialValuesTable.setRowSelectionInterval(index, index);
            initialValuesTable.setColumnSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_addRowButtonActionPerformed

    private void removeRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRowButtonActionPerformed
        editor.stopCellEditing();
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(InitialValuesPanel.class).getString("REMOVE_ROW_CONFIRM"))) {
            return;
        }
        int row = initialValuesTable.getSelectedRow();
        model.deleteRow(row);
        if (row < model.getRowCount()) {
            initialValuesTable.setRowSelectionInterval(row, row);
        } else if (row > 0) {
            initialValuesTable.setRowSelectionInterval(row - 1, row - 1);
        } else {
            initialValuesTable.clearSelection();
        }
        updateButtonsEnableState();
    }//GEN-LAST:event_removeRowButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        editor.stopCellEditing();
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(InitialValuesPanel.class).getString("CLEAR_CONFIRM"))) {
            return;
        }
        model.clear();
        initialValuesTable.clearSelection();
        updateButtonsEnableState();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        editor.stopCellEditing();
        int row = initialValuesTable.getSelectedRow();
        model.raiseRow(row);
        if (row > 0) {
            initialValuesTable.setRowSelectionInterval(row - 1, row - 1);
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        editor.stopCellEditing();
        int row = initialValuesTable.getSelectedRow();
        model.lowerRow(row);
        if (row < initialValuesTable.getRowCount() - 1) {
            initialValuesTable.setRowSelectionInterval(row + 1, row + 1);
        }
    }//GEN-LAST:event_downButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addColumnButton;
    private javax.swing.JButton addRowButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton downButton;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable initialValuesTable;
    private javax.swing.JButton removeColumnButton;
    private javax.swing.JButton removeRowButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
