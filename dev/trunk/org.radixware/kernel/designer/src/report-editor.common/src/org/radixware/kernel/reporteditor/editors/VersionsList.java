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

package org.radixware.kernel.reporteditor.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.netbeans.api.progress.ProgressUtils;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.kernel.reporteditor.tree.RemoveVersionUtil;


public class VersionsList extends javax.swing.JPanel {

    private class CellRenderer extends DefaultTableCellRenderer {

        private final VersionsTableModel model;
        private final TableCellRenderer srcRenderer;

        public CellRenderer(VersionsTableModel model, TableCellRenderer srcRenderer) {
            this.model = model;
            this.srcRenderer = srcRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component src = srcRenderer == null ? super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) : srcRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            UserReport.ReportVersion version = model.versions.get(row);
            if (version.isCurrent()) {
                if (isSelected) {
                    src.setForeground(Color.white);
                } else {
                    src.setForeground(Color.blue);
                }
            } else {
                if (isSelected) {
                    src.setForeground(Color.white);
                } else {
                    src.setForeground(Color.black);
                }
            }

            return src;
        }
    }

    private class VersionsTableModel implements TableModel {

        private List<UserReport.ReportVersion> versions = new LinkedList<>();
        private final List<TableModelListener> listeners = new LinkedList<>();

        @Override
        public int getRowCount() {
            return versions.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "#";
                case 1:
                    return "Name";
                /*
                 * case 2: return "Description";
                 */
                default:
                    return "Show In Tree";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Integer.class;
                case 1:
                    return String.class;
                /*
                 * case 2: return String.class;
                 */
                default:
                    return Boolean.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex > 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return versions.get(rowIndex).getVersion();
                case 1:
                    return versions.get(rowIndex).getName();
                /*
                 * case 2: return versions.get(rowIndex).getDescription();
                 */
                default:
                    return versions.get(rowIndex).isVisible();
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 1:
                    // versions.get(rowIndex).setName((String) aValue);
                    break;
                case 2:
                    versions.get(rowIndex).setVisible((Boolean) aValue);
                    break;
            }
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        private void fireChange() {
            final List<TableModelListener> currentListeners;
            synchronized (listeners) {
                currentListeners = new ArrayList<>(listeners);
            }
            final TableModelEvent event = new TableModelEvent(this);
            for (TableModelListener l : currentListeners) {
                l.tableChanged(event);
            }
        }

        public void update() {
            if (report != null) {
                this.versions = report.getVersions().list();
            } else {
                this.versions = Collections.emptyList();
            }
            fireChange();
        }
    }
    private UserReport report;
    private boolean updating = false;
    private final VersionsTableModel model = new VersionsTableModel();

    /**
     * Creates new form VersionsList
     */
    public VersionsList() {
        initComponents();

        this.vTable.setModel(model);
        for (int i = 0; i < this.vTable.getColumnCount(); i++) {
            this.vTable.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer(model, this.vTable.getDefaultRenderer(model.getColumnClass(i))));
        }


        this.vTable.getColumnModel().getColumn(0).setMaxWidth(40);
        this.vTable.getColumnModel().getColumn(2).setMaxWidth(100);

        btAddVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!updating && report != null) {
                            addNewVersion();
                        }
                    }
                });
            }
        });
        btMakeCurrent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!updating && report != null) {
                            makeCurrent();
                        }
                    }
                });
            }
        });
        btRemoveVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!updating && report != null) {
                            removeVersion();
                        }
                    }
                });
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        vTable = new javax.swing.JTable();
        btAddVersion = new javax.swing.JButton();
        btRemoveVersion = new javax.swing.JButton();
        btMakeCurrent = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(VersionsList.class, "VersionsList.border.title"))); // NOI18N

        vTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(vTable);
        vTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        btAddVersion.setText(org.openide.util.NbBundle.getMessage(VersionsList.class, "VersionsList.btAddVersion.text")); // NOI18N

        btRemoveVersion.setText(org.openide.util.NbBundle.getMessage(VersionsList.class, "VersionsList.btRemoveVersion.text")); // NOI18N

        btMakeCurrent.setText(org.openide.util.NbBundle.getMessage(VersionsList.class, "VersionsList.btMakeCurrent.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btMakeCurrent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addComponent(btRemoveVersion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btAddVersion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btAddVersion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRemoveVersion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btMakeCurrent)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddVersion;
    private javax.swing.JButton btMakeCurrent;
    private javax.swing.JButton btRemoveVersion;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable vTable;
    // End of variables declaration//GEN-END:variables

    public void open(UserReport report) {
        this.report = report;
        update();
    }

    public void update() {
        try {
            updating = true;
            model.update();
        } finally {
            updating = false;
        }
    }

    private void addNewVersion() {

        if (report != null) {
            ProgressUtils.showProgressDialogAndRun(new Runnable() {
                @Override
                public void run() {
                    report.getVersions().addNewVersion(report.getVersions().getCurrent());
                }
            }, "Create New Version");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    private void makeCurrent() {
        final UserReport.ReportVersion selection = getSelectedVersion();
        if (selection != null) {
            if (selection.isCurrent()) {
                DialogUtils.messageError(selection.getName() + " is current version for report " + selection.getOwnerReport().getName());
                return;
            }
            //  ProgressHandle handle = ProgressHandleFactory.createHandle("Change current version of " + selection.getOwnerReport().getName());
            try {
                //     handle.start();
                ProgressUtils.showProgressDialogAndRun(new Runnable() {
                    @Override
                    public void run() {                       
                        UserExtensionManager.getInstance().makeCurrent(null, true, selection, selection.getOwnerReport());
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                update();
                            }
                        });
                    }
                }, "Set as Current Version");
                //selection.makeCurrent(null, true);

            } catch (RadixError e) {
                DialogUtils.messageError(e);
            } finally {
                //   handle.finish();
            }
        }
    }

    private UserReport.ReportVersion getSelectedVersion() {
        int index = vTable.getSelectedRow();
        if (index >= 0 && index < model.getRowCount()) {
            return model.versions.get(index);
        }
        return null;
    }

    private void removeVersion() {
        final UserReport.ReportVersion selection = getSelectedVersion();
        if (selection != null) {

            new RemoveVersionUtil(selection) {
                @Override
                protected void done() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            update();
                        }
                    });
                }
            }.remove();

        }

    }
}
