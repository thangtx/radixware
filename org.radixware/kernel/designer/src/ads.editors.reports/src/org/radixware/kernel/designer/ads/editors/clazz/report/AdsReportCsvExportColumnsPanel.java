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
import javax.swing.AbstractCellEditor;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import org.openide.DialogDescriptor;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.StringCellEditor;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;



public class AdsReportCsvExportColumnsPanel extends JPanel {
    private AdsReportClassDef report;
    private CsvExportColumnTableModel model;
    private AdsExportCsvColumn lastSelectedItem;

    /** Creates new form AdsReportCsvExportColumnsPanel */
    public AdsReportCsvExportColumnsPanel() {
        initComponents();        
        final ActionListener btnListener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (e.getSource().equals(addButton)) {
                    AdsReportCsvExportColumnsPanel.this.onAddButtonPressed();
                } else if (e.getSource().equals(removeButton)) {
                    AdsReportCsvExportColumnsPanel.this.onRemoveButtonPressed();
                } else if (e.getSource().equals(upButton)) {
                    AdsReportCsvExportColumnsPanel.this.onUpButtonPressed();
                } else if (e.getSource().equals(downButton)) {
                    AdsReportCsvExportColumnsPanel.this.onDownButtonPressed();
                }
               
            }
        };
        addButton.addActionListener(btnListener);
        removeButton.addActionListener(btnListener);
        upButton.addActionListener(btnListener);
        downButton.addActionListener(btnListener);
    }
    
    public void open(final AdsReportClassDef report,final OpenInfo openInfo) {
        this.report=report;
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
        final boolean isExportColumnName=report.getCsvInfo().isExportColumnName();
        cbExportColumnNames.setSelected(isExportColumnName);        

        if (report.isReadOnly()) {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            cbExportColumnNames.setEnabled(false);
        } else {
            updateRemoveButtonState();
        }
        updateUpAndDownButtonsState();
    }

    private void setupTable() {
        model = new CsvExportColumnTableModel(report);  
        table.setModel(model);
        
        table.getColumnModel().getColumn(CsvExportColumnTableModel.COLUMN_FORMAT).setCellEditor(getFormatCellEditor());
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        final TableColumnModel tableColumnModel = table.getColumnModel();
        final boolean readonly = report.isReadOnly();        
        tableColumnModel.getColumn(CsvExportColumnTableModel.EXT_NAME_COLUMN).setCellEditor(new StringCellEditor(null, model, readonly));
        table.setRowSelectionAllowed(true);
        table.setAutoCreateColumnsFromModel(false);  

        final MouseAdapter tableMouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                AdsReportCsvExportColumnsPanel.this.updateRemoveButtonState();
                AdsReportCsvExportColumnsPanel.this.updateUpAndDownButtonsState();

            }
        };
        table.addMouseListener(tableMouseAdapter);       
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                AdsReportCsvExportColumnsPanel.this.onSelectionChange();
            }
        });
        table.setEnabled(true); 
    }
   
    private void onAddButtonPressed() {
        addColumn();
    }

    private void onRemoveButtonPressed() {
        final String frameHeader = NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel-RemoveDialog-Header");
        final String frameQuest =NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel-RemoveDialog-Quest");
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
            updateRemoveButtonState();
            updateUpAndDownButtonsState();
            table.requestFocusInWindow();
        }
    }

    private void onUpButtonPressed() {
        final AdsExportCsvColumn curItem = getSelectedItem();
        if (curItem != null) {
            final int pos=model.getRowByViewItem(curItem)-1;
            model.moveItem(curItem, pos);
            model.reload();
            final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
            table.setRowSelectionInterval(newRowPosition, newRowPosition);
            updateUpAndDownButtonsState();
            table.scrollToVisible(newRowPosition, 0);
        }
    }

    private void onDownButtonPressed() {
        final AdsExportCsvColumn curItem = getSelectedItem();
        if (curItem != null) {
            final int pos=model.getRowByViewItem(curItem)+1;
            model.moveItem(curItem, pos);
            model.reload();
            final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
            table.setRowSelectionInterval(newRowPosition, newRowPosition);
            updateUpAndDownButtonsState();
            
            table.scrollToVisible(newRowPosition, 0);
        }

    }
    
    private void addColumn() {
        final CsvExportColumnDialog dialog=new CsvExportColumnDialog( report,model.getProps()) ;
        dialog.invokeModalDialog();
        if (dialog.isOK() && dialog.getSelectedItem()!=null) {
           final AdsExportCsvColumn item=dialog.getSelectedItem();
           model.addItem(item);               
           model.fireTableDataChanged();
        }  
    }
    
    private AdsExportCsvColumn getSelectedItem() {
        final int selectedRow = table.getSelectedRow();
        return selectedRow == -1
                ? null
                : model.getViewItemByRow(selectedRow);
    }
    
    private void updateRemoveButtonState() {
        final int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            removeButton.setEnabled( !report.isReadOnly());
        } else {
            removeButton.setEnabled(false);
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
    
    private void onSelectionChange() {
        lastSelectedItem = getSelectedItem();

        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }
        addButton.setEnabled(!report.isReadOnly() );
        updateRemoveButtonState();
        updateUpAndDownButtonsState();
    }
    
    private FormatCellEditor getFormatCellEditor() {
        return new FormatCellEditor();
    }

    private static class FormatCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final FormatEditor editor;
        private JTable table;

        public FormatCellEditor() {
            editor = new FormatEditor(new Runnable() {
                @Override
                public void run() {
                    if (table != null) {
                        table.getCellEditor().stopCellEditing();
                        //final CsvExportColumnTableModel csvExportColumnTableModel = (CsvExportColumnTableModel) table.getModel();
                        // AdsExportCsvColumn c=csvExportColumnTableModel.getViewItemByRow(table.getSelectedRow());
                        // c.setFormat(editor.getFormat());
                    }
                }
            },false);
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getFormat();
        }

        @Override
        public Component getTableCellEditorComponent(final JTable table,final Object value,final boolean isSelected,final int row,final int column) {
            this.table = table;
            final CsvExportColumnTableModel csvExportColumnTableModel = (CsvExportColumnTableModel) table.getModel();
            final AdsExportCsvColumn c=csvExportColumnTableModel.getViewItemByRow(row);
            final AdsPropertyDef prop= csvExportColumnTableModel.getProperty(row);
            //Object obj=csvExportColumnTableModel.getValueAt(row, CsvExportColumnTableModel.COLUMN_FORMAT);
            if(prop!=null/* && obj instanceof AdsPropertyDef*/){
                //AdsPropertyDef prop=(AdsPropertyDef)obj;
                editor.open(c.getFormat(), prop.getValue().getType().getTypeId());
            }

            return editor;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
        btnRowCondition = new javax.swing.JButton();
        cbExportColumnNames = new javax.swing.JCheckBox();

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
        addButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel.addButton.text")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeButton.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        removeButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel.removeButton.text")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        upButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon(13, 13));
        upButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel.upButton.text")); // NOI18N
        upButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        downButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon(13, 13));
        downButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel.downButton.text")); // NOI18N
        downButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel.jLabel1.text")); // NOI18N

        btnRowCondition.setText(org.openide.util.NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel.btnRowCondition.text")); // NOI18N
        btnRowCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRowConditionActionPerformed(evt);
            }
        });

        cbExportColumnNames.setText(org.openide.util.NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel.cbExportColumnNames.text")); // NOI18N
        cbExportColumnNames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbExportColumnNamesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnRowCondition)
                                .addContainerGap())
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(removeButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(upButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbExportColumnNames, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, btnRowCondition, downButton, removeButton, upButton});

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRowCondition))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbExportColumnNames))
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

private void btnRowConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRowConditionActionPerformed
    final AdsCsvReportInfoEditor editor =new AdsCsvReportInfoEditor(report.getCsvInfo());
    editor.open(new OpenInfo(report));
    final String title= NbBundle.getMessage(AdsReportCsvExportColumnsPanel.class, "AdsReportCsvExportColumnsPanel-RowCondition-Title");
    final ModalDisplayer md=new ModalDisplayer(editor, title, new Object[]{DialogDescriptor.CLOSED_OPTION}) {
         @Override
         protected void apply() {
             //
         }
     };
     md.showModal();
}//GEN-LAST:event_btnRowConditionActionPerformed

    private void cbExportColumnNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbExportColumnNamesActionPerformed
        report.getCsvInfo().setIsExportColumnName(cbExportColumnNames.isSelected());
    }//GEN-LAST:event_cbExportColumnNamesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton btnRowCondition;
    private javax.swing.JCheckBox cbExportColumnNames;
    private javax.swing.JButton downButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
