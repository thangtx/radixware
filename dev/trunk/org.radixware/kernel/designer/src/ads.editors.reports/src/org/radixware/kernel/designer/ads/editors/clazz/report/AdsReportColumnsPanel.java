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

/*
 * AdsReportCsvExportColumnsPanel.java
 *
 * Created on Nov 18, 2011, 2:57:53 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.StringCellEditor;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.StringCellValue;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class AdsReportColumnsPanel extends JPanel {

    private static final String SHOW_ID_KEY = "rdx.editor.showid";
    private final Preferences prefs = NbPreferences.root();
    
    private AdsReportClassDef report;
    private ReportColumnsTableModel model;
    private AdsReportColumnDef lastSelectedItem;

    /**
     * Creates new form AdsReportCsvExportColumnsPanel
     */
    public AdsReportColumnsPanel() {
        prefs.addPreferenceChangeListener(new PreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                if (evt.getKey().equals(SHOW_ID_KEY)) {
                    if (AdsReportColumnsPanel.this == null || model == null) {
                        return;
                    }
                    model.setIsShowColumnId(prefs.getBoolean(SHOW_ID_KEY, false));
                    table.repaint();
                }
            }
        });
        
        initComponents();        
        final ActionListener btnListener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (e.getSource().equals(addButton)) {
                    AdsReportColumnsPanel.this.onAddButtonPressed();
                } else if (e.getSource().equals(removeButton)) {
                    AdsReportColumnsPanel.this.onRemoveButtonPressed();
                } else if (e.getSource().equals(upButton)) {
                    AdsReportColumnsPanel.this.onUpButtonPressed();
                } else if (e.getSource().equals(downButton)) {
                    AdsReportColumnsPanel.this.onDownButtonPressed();
                } else if (e.getSource().equals(editButton)) {
                    AdsReportColumnsPanel.this.onEditButtonPressed();
                }

            }
        };                
        addButton.addActionListener(btnListener);
        removeButton.addActionListener(btnListener);
        upButton.addActionListener(btnListener);
        downButton.addActionListener(btnListener);
        editButton.addActionListener(btnListener);
    }

    public void open(final AdsReportClassDef report, final OpenInfo openInfo) {
        this.report = report;
        onOpening();
    }

    private void onOpening() {
        setupTable();
        update();
        table.requestFocusInWindow();
    }

    public void update() {
        final boolean hasElements = model.getRowCount() > 0;
        if (hasElements) {
            updateTableSelection();
        }

        if (report.isReadOnly()) {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);            
            editButton.setEnabled(false);
        } else {
            updateRemoveAndEditButtonsState();
        }
        updateUpAndDownButtonsState();
    }

    private void setupTable() {
        model = new ReportColumnsTableModel(report);
        model.setIsShowColumnId(prefs.getBoolean(SHOW_ID_KEY, false));
        
        table.setModel(model);

        final TableColumnModel tableColumnModel = table.getColumnModel();
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        final boolean readonly = report.isReadOnly();
        tableColumnModel.getColumn(ReportColumnsTableModel.NAME_COLUMN).setCellEditor(new ColumnNameCellEditor(null, model, readonly));
        table.setRowSelectionAllowed(true);
        table.setAutoCreateColumnsFromModel(false);

        final MouseAdapter tableMouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                AdsReportColumnsPanel.this.updateRemoveAndEditButtonsState();
                AdsReportColumnsPanel.this.updateUpAndDownButtonsState();

            }
        };                
        
        table.addMouseListener(tableMouseAdapter);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                AdsReportColumnsPanel.this.onSelectionChange();
            }
        });
        table.setEnabled(true);
    }

    private void onAddButtonPressed() {
        addColumn();
    }

    private void onRemoveButtonPressed() {
        final String frameHeader = NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportCsvExportColumnsPanel-RemoveDialog-Header");
        final String frameQuest = NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportCsvExportColumnsPanel-RemoveDialog-Quest");
        if (JOptionPane.showConfirmDialog(new JFrame(), frameQuest, frameHeader, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            final int arr[] = table.getSelectedRows();
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                boolean isSelect = false;
                for (int j : arr) {
                    if (j == i) {
                        isSelect = true;
                        break;
                    }
                }
                if (isSelect) {
                    model.removeRow(i);
                }
            }
            updateRemoveAndEditButtonsState();
            updateUpAndDownButtonsState();
            table.requestFocusInWindow();
        }
    }

    private void onUpButtonPressed() {
        final AdsReportColumnDef curItem = getSelectedItem();
        if (curItem != null) {
            final int pos = model.getRowByViewItem(curItem) - 1;
            model.moveItem(curItem, pos);
            model.reload();
            final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
            table.setRowSelectionInterval(newRowPosition, newRowPosition);
            updateUpAndDownButtonsState();
            table.scrollToVisible(newRowPosition, 0);
        }
    }

    private void onDownButtonPressed() {
        final AdsReportColumnDef curItem = getSelectedItem();
        if (curItem != null) {
            final int pos = model.getRowByViewItem(curItem) + 1;
            model.moveItem(curItem, pos);
            model.reload();
            final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
            table.setRowSelectionInterval(newRowPosition, newRowPosition);
            updateUpAndDownButtonsState();

            table.scrollToVisible(newRowPosition, 0);
        }

    }

    private void onEditButtonPressed() {
        editColumn();
    }

    private void addColumn() {
        ReportColumnEditorPanel panel = new ReportColumnEditorPanel(report, null, model.getProps());
        StateAbstractDialog displayer = new StateAbstractDialog(panel, "Add Report Column") {};
        if (displayer.showModal()) {
            final AdsReportColumnDef column = panel.getColumn();

            model.addItem(column);
            model.fireTableDataChanged();

            lastSelectedItem = column;
            updateTableSelection();
        }
    }

    private void editColumn() {
        AdsReportColumnDef currentColumn = getSelectedItem();
        if (currentColumn != null) {
            ReportColumnEditorPanel panel = new ReportColumnEditorPanel(report, currentColumn, model.getProps());
            StateAbstractDialog displayer = new StateAbstractDialog(panel, "Edit Report Column") {};
            if (displayer.showModal()) {
                final AdsReportColumnDef editedColumn = panel.getColumn();
                ReportColumnEditorUtils.copyColumnContent(report, editedColumn, currentColumn);
                model.fireTableDataChanged();

                lastSelectedItem = currentColumn;
                updateTableSelection();
            }
        }
    }

    private AdsReportColumnDef getSelectedItem() {
        final int selectedRow = table.getSelectedRow();
        return selectedRow == -1
                ? null
                : model.getViewItemByRow(selectedRow);
    }

    private void updateRemoveAndEditButtonsState() {
        final int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            removeButton.setEnabled(!report.isReadOnly());
            editButton.setEnabled(!report.isReadOnly());
        } else {
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
        }
    }

    private void updateUpAndDownButtonsState() {
        final boolean readonly = report.isReadOnly();

        final int curRow = table.getSelectedRow();
        final int count = model.getRowCount();
        final boolean possibility = !readonly && curRow != -1 && count > 1;

        upButton.setEnabled(possibility
                && curRow > 0);
        downButton.setEnabled(possibility
                && curRow < (count - 1));

    }

    private void updateTableSelection() {
        if (lastSelectedItem == null) {
            table.setRowSelectionInterval(0, 0);
        } else {
            final int indexInModel = model.getRowByViewItem(lastSelectedItem);
            final int indexInTable = table.convertRowIndexToView(indexInModel);

            if (table.getRowCount() >= indexInTable) {
                table.setRowSelectionInterval(indexInTable, indexInTable);
            } else {
                table.setRowSelectionInterval(0, 0);
            }
        }
    }

    private void onSelectionChange() {
        lastSelectedItem = getSelectedItem();

        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }
        addButton.setEnabled(!report.isReadOnly());
        updateRemoveAndEditButtonsState();
        updateUpAndDownButtonsState();
    }
    
    private static class ColumnNameCellEditor extends StringCellEditor {
        
        private final ReportColumnsTableModel ownerModel;
        
        public ColumnNameCellEditor(ChangeSupport changeSupport, ReportColumnsTableModel ownerModel, boolean readonly) {
            super(changeSupport, ownerModel, readonly);
            this.ownerModel = ownerModel;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {                        
            AdsReportColumnDef column = ownerModel.getViewItemByRow(r);            
            
            return super.getTableCellEditorComponent(table, new StringCellValue(column.getName()), isSelected, r, c);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        editButton = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 200));
        jScrollPane1.setViewportView(table);

        addButton.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        addButton.setText(org.openide.util.NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportColumnsPanel.addButton.text")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeButton.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        removeButton.setText(org.openide.util.NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportColumnsPanel.removeButton.text")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        upButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon(13, 13));
        upButton.setText(org.openide.util.NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportColumnsPanel.upButton.text")); // NOI18N
        upButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        downButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon(13, 13));
        downButton.setText(org.openide.util.NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportColumnsPanel.downButton.text")); // NOI18N
        downButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportColumnsPanel.jLabel1.text")); // NOI18N

        editButton.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        editButton.setText(org.openide.util.NbBundle.getMessage(AdsReportColumnsPanel.class, "AdsReportColumnsPanel.editButton.text")); // NOI18N
        editButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(upButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, downButton, removeButton, upButton});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
