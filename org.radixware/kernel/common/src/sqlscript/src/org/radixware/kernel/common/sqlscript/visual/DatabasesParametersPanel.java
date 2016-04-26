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

package org.radixware.kernel.common.sqlscript.visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.sqlscript.visual.IDatabasesParametersInfo.IDatabase;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class DatabasesParametersPanel extends JPanel {

    public DatabasesParametersPanel() {

        this.setLayout(new BorderLayout());

        JPanel topPanel1 = new JPanel();
        topPanel1.setLayout(new BoxLayout(topPanel1, BoxLayout.X_AXIS));

        JLabel label1 = new JLabel(" Database: ");
        topPanel1.add(label1);
        topPanel1.add(databasesComboBox);

        JPanel topPanel2 = new JPanel();
        topPanel2.setLayout(new BoxLayout(topPanel2, BoxLayout.X_AXIS));
        JLabel label2 = new JLabel(" Version: ");
        topPanel2.add(label2);
        topPanel2.add(versionsComboBox);

        JPanel bigTopPanel = new JPanel();
        bigTopPanel.setLayout(new BorderLayout());

        bigTopPanel.add(topPanel1, BorderLayout.NORTH);
        bigTopPanel.add(topPanel2, BorderLayout.SOUTH);


        setWidth(label2, label1.getPreferredSize().width);
        this.add(bigTopPanel, BorderLayout.NORTH);
        this.add(add(new JScrollPane(table)), BorderLayout.CENTER);

        table.setModel(tableModel);
        TableColumn tc2 = table.getColumnModel().getColumn(2);
        tc2.setMinWidth(50);
        tc2.setMaxWidth(70);
        tc2.setCellRenderer(new DatabasesParametersPanel.BooleanRendere());

        table.getTableHeader().setReorderingAllowed(false);

        JCheckBox b = new JCheckBox();
        table.setBackground(b.getBackground());

    }
    private final ActionListener databasesComboBoxActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            chandeDatabasesComboBox();
        }
    };
    private final String[] colNames = new String[]{"Parameter", "Description", "Enable"};
    private final DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(new Object[][]{}, colNames) {
        @Override
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return Boolean.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex != 2) {
                return false;
            }
            return mayEditCheckBox(rowIndex);
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            super.setValueAt(aValue, row, column);
        }
    };

    private boolean mayEditCheckBox(int rowIndex) {
        int index = databasesComboBox.getSelectedIndex();
        if (index < 0) {
            return false;
        }

        IDatabase database = info.getDatabases().get(index);
        List<? extends IDatabasesParametersInfo.IParameter> parameters = database.getParameters();
        return parameters.get(rowIndex).mayEdit();
    }

    private class BooleanRendere extends JCheckBox implements TableCellRenderer, Serializable {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            this.setHorizontalAlignment(SwingConstants.CENTER);
            setSelected((value != null && ((Boolean) value).booleanValue()));
            this.setEnabled(mayEditCheckBox(row));
//            if (isSelected)
//                this.setBackground(table.getBackground().darker());
//            else
//                this.setBackground(table.getBackground().brighter());
            return this;
        }
    }

    public void open(IDatabasesParametersInfo info, boolean usUpdate, IPreferencesSimpleSupporter preferencesSimpleSupporter) {
        this.info = info;
        this.preferencesSimpleSupporter = preferencesSimpleSupporter;

        if (info.getDatabases().isEmpty()) {
            databasesComboBox.setEnabled(false);
            versionsComboBox.setEnabled(false);
            table.setEnabled(false);
        }

        databasesComboBox.removeActionListener(databasesComboBoxActionListener);
        for (IDatabase database : info.getDatabases()) {
            databasesComboBox.addItem(database.getTitle());
        }
        if (databasesComboBox.getItemCount()>0)
            databasesComboBox.setSelectedIndex(0);

        databasesComboBox.addActionListener(databasesComboBoxActionListener);
        chandeDatabasesComboBox();

        if (preferencesSimpleSupporter != null) {
            isMustSaveColumnWidth = false;

            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                int columnWidth = preferencesSimpleSupporter.getInt(CFG_PREFFIX + String.valueOf(i) + DatabasesParametersPanel.class.getSimpleName(), getWidth() / 2);
                table.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
                table.getColumnModel().getColumn(i).setWidth(columnWidth);
            }
            isMustSaveColumnWidth = true;
        }
 
        table.getColumnModel().addColumnModelListener(tableColumnModelListener);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        

        databasesComboBox.setEnabled(!usUpdate);
        versionsComboBox.setEnabled(!usUpdate);        
        
    }

    public IDatabase getSelectedDatabase() {
        int index = databasesComboBox.getSelectedIndex();
        if (index < 0) {
            return null;
        }
        return info.getDatabases().get(index);
    }

    public String getSelectedVersion() {
        IDatabase selectedDatabase = getSelectedDatabase();
        if (selectedDatabase == null) {
            return null;
        }
        int index = versionsComboBox.getSelectedIndex();
        if (index < 0) {
            return null;
        }
        return selectedDatabase.getVersions().get(index);
    }

    public List<Boolean> getParameterValues() {
        List<Boolean> values = new ArrayList(table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            values.add((Boolean) table.getValueAt(i, 2));
        }
        return values;
    }
    private final TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
        @Override
        public void columnAdded(TableColumnModelEvent e) {
        }

        @Override
        public void columnRemoved(TableColumnModelEvent e) {
        }

        @Override
        public void columnMoved(TableColumnModelEvent e) {
        }

        @Override
        public void columnSelectionChanged(ListSelectionEvent e) {
        }

        @Override
        public void columnMarginChanged(ChangeEvent e) {
            if (preferencesSimpleSupporter != null && isMustSaveColumnWidth) {
                for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                    preferencesSimpleSupporter.putInt(CFG_PREFFIX + String.valueOf(i) + DatabasesParametersPanel.class.getSimpleName(), table.getColumnModel().getColumn(i).getWidth());
                }
            }
        }
    };
    private final static String CFG_PREFFIX = "COLUMNS_WIDTH_";
    private final JComboBox databasesComboBox = new JComboBox();
    private final JComboBox versionsComboBox = new JComboBox();
    private boolean isMustSaveColumnWidth = false;

    private void chandeDatabasesComboBox() {
        versionsComboBox.removeAllItems();
        int index = databasesComboBox.getSelectedIndex();
        if (index < 0) {
            return;
        }

        IDatabase database = info.getDatabases().get(index);
        versionsComboBox.removeAllItems();
        for (String version : database.getVersions()) {
            versionsComboBox.addItem(version);
        }

        List<? extends IDatabasesParametersInfo.IParameter> parameters = database.getParameters();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(parameters.size());

        int row = 0;
        for (IDatabasesParametersInfo.IParameter parameter : parameters) {
            table.setValueAt(parameter.getName(), row, 0);
            table.setValueAt(parameter.getDescription(), row, 1);
            table.setValueAt(parameter.getDefaultValue(), row, 2);

            row++;
        }
    }
    private final JTable table = new JTable();
    private IDatabasesParametersInfo info = null;
    private IPreferencesSimpleSupporter preferencesSimpleSupporter = null;

    private static void setWidth(JComponent component, int width) {
        Dimension dimension = component.getPreferredSize();
        dimension.width = width;
        component.setPreferredSize(dimension);
        component.setMinimumSize(dimension);
        component.setMaximumSize(dimension);
    }
}
