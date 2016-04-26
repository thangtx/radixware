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

package org.radixware.kernel.designer.ads.editors.clazz.sql.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef.UsedTable;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable;
import org.radixware.kernel.designer.common.dialogs.utils.ModalObjectEditor;


public class AliasesEditorPanel extends ModalObjectEditor<List<AliasesEditorPanel.TableInfo>> {

    DefaultTableModel model;
    TunedTable table;
    JScrollPane scTable;

    public AliasesEditorPanel() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        setLayout(new BorderLayout(5, 5));

        table = new TunedTable();
        model = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Table", "Alias"
                }) {

            boolean[] canEdit = new boolean[]{
                false, true
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        table.setModel(model);
        table.setCellSelectionEnabled(true);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            protected void setValue(Object value) {
                if (value instanceof TableInfo) {
                    TableInfo info = (TableInfo) value;
                    super.setIcon(info.getTable().getIcon().getIcon());
                    super.setValue(info.getTable().getName());
                }
            }
        });

        scTable = new JScrollPane(table);

        add(scTable, BorderLayout.CENTER);

        setMinimumSize(new Dimension(300, 70));
    }

    @Override
    protected String getTitle() {
        return NbBundle.getMessage(AliasesEditorPanel.class, "msg-change-table-alias");
    }

    @Override
    protected void applyChanges() {
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            TableInfo info = (TableInfo) model.getValueAt(i, 0);
            info.setAlias((String) model.getValueAt(i, 1));
        }
    }

    public void open(List<UsedTable> tables) {
        List<TableInfo> infos = new LinkedList<TableInfo>();
        for (UsedTable usedTable : tables) {
            infos.add(new UsedTableInfo(usedTable));
        }
        open(infos, null);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        table.setEnabled(!readOnly);
    }

    @Override
    protected void afterOpen() {
        model.setRowCount(0);
        for (TableInfo info : getObject()) {
            Object[] data = new Object[]{
                info,
                info.getAlias()
            };
            model.addRow(data);
        }
    }

    public static class TableInfo {

        private DdsTableDef table;
        private String alias;

        public TableInfo(DdsTableDef table, String alias) {
            this.table = table;
            this.alias = alias == null ? "" : alias;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public DdsTableDef getTable() {
            return table;
        }
    }

    public static class UsedTableInfo extends TableInfo {

        UsedTable usedTable;

        public UsedTableInfo(UsedTable usedTable) {
            super(usedTable.findTable(), usedTable.getAlias());
            this.usedTable = usedTable;
        }

        @Override
        public String getAlias() {
            return usedTable.getAlias();
        }

        @Override
        public DdsTableDef getTable() {
            return usedTable.findTable();
        }

        @Override
        public void setAlias(String alias) {
            usedTable.setAlias(alias);
        }
    }
}
