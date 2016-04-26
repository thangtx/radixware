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
package org.radixware.kernel.designer.environment.merge;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXTable;
import org.radixware.kernel.common.utils.Utils;

public abstract class CopyMergeTable extends JTable {

    protected final static String P_KEY = "CopyMergeDlgKey";

    public CopyMergeTable() {
        super();
        setModel(jTableModel);
        getTableHeader().setReorderingAllowed(false);

        final TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
            @Override
            public void columnAdded(final TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(final TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(final TableColumnModelEvent e) {
            }

            @Override
            public void columnSelectionChanged(final ListSelectionEvent e) {
            }

            @Override
            public void columnMarginChanged(final ChangeEvent e) {
                final Preferences preferences = org.radixware.kernel.common.utils.Utils.findOrCreatePreferences(P_KEY);
                if (preferences != null && isMustSaveColumnWidth) {
                    //TableColumn col;

                    for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
                        final TableColumn col = getColumnModel().getColumn(i);
                        preferences.putInt("ColumnWidth" + String.valueOf(i), col.getWidth());
                    }
                }
            }
        };
        getColumnModel().addColumnModelListener(tableColumnModelListener);

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    protected boolean isMustSaveColumnWidth = false;

    protected abstract boolean cellEditable(final int rowIndex, final int columnIndex);

    protected abstract boolean mayCopy(final int rowIndex);

    protected abstract boolean mayMerge(final int rowIndex);

    protected abstract boolean copy(final int index) throws Exception;

    protected abstract boolean merge(final int index);

    protected abstract Icon getIconByRow(final int index);

    protected abstract boolean isDefaultBackgroundColor(final int index);

    protected abstract Color getCurrForegroundColor(final int index);

    protected abstract void copyAll() throws Exception;

    private class IconRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

        public IconRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            if (column == 0) {
                setIcon(getIconByRow(row));
            }
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());

                if (isDefaultBackgroundColor(row)) {
                    setBackground(UIManager.getColor("Button.background"));
                } else {
                    setBackground(Color.LIGHT_GRAY);
                }
                setForeground(getCurrForegroundColor(row));
            }
            setText((String) table.getValueAt(row, column));
            return this;
        }
    }

    void setColumnsWidth() {

        Preferences pref = Utils.findPreferencesWithoutException(P_KEY);
        if (pref != null) {
            isMustSaveColumnWidth = false;

            final TableColumnModel tableColumnModel2 = getColumnModel();
            TableColumn col;
            for (int i = 0; i < tableColumnModel2.getColumnCount(); i++) {
                col = tableColumnModel2.getColumn(i);
                col.setPreferredWidth(pref.getInt("ColumnWidth" + String.valueOf(i), col.getPreferredWidth()));
                col.setWidth(pref.getInt("ColumnWidth" + String.valueOf(i), col.getWidth()));
            }

            isMustSaveColumnWidth = true;

        }

        JCheckBox checkBox = new JCheckBox();
        final int size = 70;

        TableColumn c = this.getColumnModel().getColumn(0);
        c.setCellRenderer(new IconRenderer());

        DefaultCellEditor defaultCellEditor = new DefaultCellEditor(new JTextFieldEx());
        c.setCellEditor(defaultCellEditor);

        c = this.getColumnModel().getColumn(1);
        c.setCellEditor(defaultCellEditor);
        c.setCellRenderer(new IconRenderer());

        c = this.getColumnModel().getColumn(2);
        c.setCellEditor(new ButtonEditor(checkBox, "Copy", null));
        c.setCellRenderer(new ButtonRenderer("Copy", 2));
        c.setMaxWidth(size);
        c.setMinWidth(size);

        c = this.getColumnModel().getColumn(3);
        c.setCellEditor(new ButtonEditor(checkBox, "Merge", null));
        c.setCellRenderer(new ButtonRenderer("Merge", 3));
        c.setMaxWidth(size);
        c.setMinWidth(size);

        c = this.getColumnModel().getColumn(4);
        c.setCellEditor(new JXTable.BooleanEditor());
        c.setCellRenderer(new CheckRenderer());
        c.setMaxWidth(size);
        c.setMinWidth(size);

        this.setRowHeight(24);

        this.repaint();

//        isMayModify = false;
        isMustSaveColumnWidth = true;
    }
    DefaultTableModel jTableModel = new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Definition", "File", "Copy", "Merge", "Done"
            }) {
                @Override
                public Class getColumnClass(int columnIndex) {
                    return java.lang.Object.class;
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return cellEditable(rowIndex, columnIndex);

                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    super.setValueAt(aValue, row, column);
                }
            };

    class ButtonRenderer extends JButton implements TableCellRenderer {

        String title;
        int col;

        public ButtonRenderer(String title, int col) {
            setOpaque(true);
            this.title = title;
            this.col = col;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText(title);
            if (col == 2) {
                setEnabled(mayCopy(row));
            } else //if(col==3)
            {
                setEnabled(mayMerge(row));
            }
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private int currRow;
        final private String currText;

        public ButtonEditor(JCheckBox checkBox, String text, String hint) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currRow != -1) {
                        try {
                            if ("Copy".equals(currText)) {
                                copy(currRow);
                            } else //if ("Merge".equals(currText))
                            {
                                merge(currRow);
                            }
                        } catch (Exception ex) {
                            MergeUtils.messageError(ex);
                        }
                    }
                }
            });
            currRow = -1;
            button.setToolTipText(hint);
            button.setText(text);
            this.currText = text;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            currRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return currText;
        }
    }

    class CheckRenderer extends JCheckBox implements TableCellRenderer {

        public CheckRenderer() {
            super();
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            final boolean isCheck = value != null
                    && (value instanceof Boolean)
                    && ((Boolean) value);
            setSelected(isCheck);
            setEnabled(false);
            return this;
        }
    }

    private class JTextFieldEx extends JTextField {

        @Override
        public boolean isEditable() {
            return false;
        }
    }
}
