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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;

import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.editors.ColumnEditors;



public class ObjectTitleFormatPanel extends javax.swing.JPanel {

    private HandleInfo handleInfo = null;
    private boolean readonly = false;
    private AdsObjectTitleFormatDef objectTitleFormat = null;
    private IAdsPropertiesListProvider propertiesListProvider = null;
    private ColumnEditors<TitleItem> rowEditorModel = new ColumnEditors<TitleItem>();

    /** Creates new form ObjectTitleFormatPanel */
    public ObjectTitleFormatPanel() {
        super();
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        objectTitleFormatTable =  new ObjectTitleFormatTableX();
        displayInsteadOfNULLPanel = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel("Display instead of null");
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        testButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ObjectTitleFormatPanel.class, "ObjectTitleFormatPanel.border.outsideBorder.title")), javax.swing.BorderFactory.createEmptyBorder(6, 0, 10, 0))); // NOI18N

        /*
        objectTitleFormatTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        */
        jScrollPane1.setViewportView(objectTitleFormatTable);

        /*
        */

        addButton.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        addButton.setText(org.openide.util.NbBundle.getMessage(ObjectTitleFormatPanel.class, "ObjectTitleFormatPanel.addButton.text")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        removeButton.setText(org.openide.util.NbBundle.getMessage(ObjectTitleFormatPanel.class, "ObjectTitleFormatPanel.removeButton.text")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        upButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon(13, 13));
        upButton.setText(org.openide.util.NbBundle.getMessage(ObjectTitleFormatPanel.class, "ObjectTitleFormatPanel.upButton.text")); // NOI18N
        upButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon(13, 13));
        downButton.setText(org.openide.util.NbBundle.getMessage(ObjectTitleFormatPanel.class, "ObjectTitleFormatPanel.downButton.text")); // NOI18N
        downButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        testButton.setIcon(RadixWareDesignerIcon.EDIT.FIX.getIcon(13, 13));
        testButton.setText(org.openide.util.NbBundle.getMessage(ObjectTitleFormatPanel.class, "ObjectTitleFormatPanel.testButton.text")); // NOI18N
        testButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(displayInsteadOfNULLPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(upButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(testButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(testButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayInsteadOfNULLPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        //displayInstedOfNULLPanel.setTitle("Display Instead of Null");
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed

        new ModalDisplayer(new NewTitleItemsPanel(createPropertiesListProvider()), "Select Property") {

            @Override
            protected void apply() {

                final List<AdsPropertyDef> selectedPropertiesList = ((NewTitleItemsPanel) getComponent()).getSelectedProperties();

                //create new titleitems for current selected properties
                if (!selectedPropertiesList.isEmpty()) {

                    final ObjectTitleFormatTableModel objectTitleFormatTableModel = (ObjectTitleFormatTableModel) objectTitleFormatTable.getModel();
                    int selected = objectTitleFormatTable.getSelectedRow();
                    if (selected > -1 && selected < (objectTitleFormatTableModel.getRowCount() - 1)) {
                        int start = selected + 1;
                        for (AdsPropertyDef property : selectedPropertiesList) {
                            final AdsObjectTitleFormatDef.TitleItem titleItem = AdsObjectTitleFormatDef.TitleItem.Factory.newInstance(property);
                            objectTitleFormatTableModel.addTitleItem(start, titleItem);
                            rowEditorModel.addEditorForItem(titleItem, new ObjectTitleItemCellEditor(objectTitleFormat));
                            start++;
                        }
                        objectTitleFormatTable.requestFocusInWindow();
                        objectTitleFormatTable.getSelectionModel().setSelectionInterval(selected + 1, start - 1);
                    } else {
                        for (AdsPropertyDef property : selectedPropertiesList) {
                            final AdsObjectTitleFormatDef.TitleItem titleItem = AdsObjectTitleFormatDef.TitleItem.Factory.newInstance(property);
                            objectTitleFormatTableModel.addTitleItem(titleItem);
                            rowEditorModel.addEditorForItem(titleItem, new ObjectTitleItemCellEditor(objectTitleFormat));
                        }
                        final int rowsCount = objectTitleFormatTable.getRowCount();
                        objectTitleFormatTable.requestFocusInWindow();
                        objectTitleFormatTable.getSelectionModel().setSelectionInterval(rowsCount - 1, rowsCount - 1);
                    }
                }
            }
        }.showModal();
    }//GEN-LAST:event_addButtonActionPerformed

    private void updateState() {
        final boolean localReadOnly = readonly || objectTitleFormat ==null || objectTitleFormat.isReadOnly();
        addButton.setEnabled(!localReadOnly);

        final int rowsCount = objectTitleFormatTable.getRowCount();
        final boolean tableHasRows = rowsCount > 0;
        final int curSelectedRow = objectTitleFormatTable.getSelectedRow();

        removeButton.setEnabled(!localReadOnly && tableHasRows);
        upButton.setEnabled(!localReadOnly && tableHasRows && curSelectedRow > 0 && objectTitleFormatTable.getSelectedRowCount() == 1);
        downButton.setEnabled(!localReadOnly && tableHasRows && curSelectedRow < rowsCount - 1 && objectTitleFormatTable.getSelectedRowCount() == 1);
        testButton.setEnabled(!localReadOnly && tableHasRows);

        objectTitleFormatTable.setEnabled(!localReadOnly);
        displayInsteadOfNULLPanel.setReadonly(localReadOnly);
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed

        //remove specified row
        if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to remove item(s)?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            int[] selection = objectTitleFormatTable.getSelectedRows();
            if (selection != null && selection.length > 0) {
                int start = selection[0];
                int end = selection[selection.length - 1];
                int oldRowCount = objectTitleFormatTable.getRowCount();

                for (int i = 0, size = selection.length - 1; i <= size; i++) {
                    final int selectedRow = selection[i];
                    final int selectedRowInModel = objectTitleFormatTable.convertRowIndexToModel(selectedRow);
                    final AdsObjectTitleFormatDef.TitleItem titleItem = ((ObjectTitleFormatTableModel) objectTitleFormatTable.getModel()).getTitleItem(selectedRowInModel);

                    rowEditorModel.removeEditorForItem(titleItem);

                    ((ObjectTitleFormatTableModel) objectTitleFormatTable.getModel()).removeTitleItem(titleItem);
                }

                final int rowsCount = objectTitleFormatTable.getRowCount();
                if (rowsCount > 0) {
                    if (end == oldRowCount) {
                        objectTitleFormatTable.setRowSelectionInterval(rowsCount - 1, rowsCount - 1);
                    } else {
                        if (start == 0){
                           objectTitleFormatTable.setRowSelectionInterval(0, 0);
                        } else {
                           objectTitleFormatTable.setRowSelectionInterval(start - 1, start - 1);
                        }
                    }
                }
            }
            updateState();
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        final int selectedRow = objectTitleFormatTable.getSelectedRow();

        if (objectTitleFormatTable.getRowCount() > 1) {
            ((ObjectTitleFormatTableModel) objectTitleFormatTable.getModel()).moveUp(objectTitleFormatTable.convertRowIndexToModel(selectedRow));
            objectTitleFormatTable.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        final int selectedRow = objectTitleFormatTable.getSelectedRow();

        if (selectedRow < objectTitleFormatTable.getRowCount() - 1) {
            ((ObjectTitleFormatTableModel) objectTitleFormatTable.getModel()).moveDown(objectTitleFormatTable.convertRowIndexToModel(objectTitleFormatTable.getSelectedRow()));
            objectTitleFormatTable.getSelectionModel().setSelectionInterval(selectedRow + 1, selectedRow + 1);
        }
    }//GEN-LAST:event_downButtonActionPerformed

    //returns properties list provider which contains not used properties only
    private IAdsPropertiesListProvider createPropertiesListProvider() {

        final List<AdsPropertyDef> allPropertiesList = this.propertiesListProvider.getAdsPropertiesList();

        final IAdsPropertiesListProvider usedPropertiesListProvider = ((ObjectTitleFormatTableModel) objectTitleFormatTable.getModel()).getPropertiesListProvider();
        final List<AdsPropertyDef> usedPropertiesList = usedPropertiesListProvider.getAdsPropertiesList();

        return new IAdsPropertiesListProvider() {

            @Override
            public List<AdsPropertyDef> getAdsPropertiesList() {

                List<AdsPropertyDef> list = new ArrayList<AdsPropertyDef>();
                for (AdsPropertyDef xAdsPropertyDef : allPropertiesList) {
                    if (!usedPropertiesList.contains(xAdsPropertyDef)) {
                        list.add(xAdsPropertyDef);
                    }
                }

                return list;
            }
        };
    }

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed

        new ModalDisplayer(new PreviewObjectTitleFormatPanel(objectTitleFormat), "Object Title Format Test") {

            @Override
            protected void apply() {
                //do nothing
            }
        }.showModal();
    }//GEN-LAST:event_testButtonActionPerformed

    public void open(final AdsObjectTitleFormatDef objectTitleFormat, IAdsPropertiesListProvider propertiesListProvider) {

        this.objectTitleFormat = objectTitleFormat;
        this.propertiesListProvider = propertiesListProvider;

        handleInfo = new HandleInfo() {

            @Override
            public Definition getAdsDefinition() {
                return objectTitleFormat;
            }

            @Override
            public Id getTitleId() {
                return objectTitleFormat.getNullValTitleId();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (multilingualStringDef != null) {
                    objectTitleFormat.setNullValTitleId(multilingualStringDef.getId());
                } else {
                    objectTitleFormat.setNullValTitleId(null);
                }
            }

            @Override
            public void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                objectTitleFormat.setNullValTitle(language, newStringValue);
            }
        };

        update();
    }

    public void update() {
        setupTable();
        if (objectTitleFormatTable.getRowCount() > 0) {
            objectTitleFormatTable.setRowSelectionInterval(0, 0);
        }
        displayInsteadOfNULLPanel.update(handleInfo);
        updateState();
    }

    private void setupTable() {

        objectTitleFormatTable.setModel(new ObjectTitleFormatTableModel(objectTitleFormat));

        // tell the JTableX which RowEditorModel we are using
        objectTitleFormatTable.setRowEditorModel(rowEditorModel);

        objectTitleFormatTable.setRowHeight(24);
        //objectTitleFormatTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        objectTitleFormatTable.setAutoCreateColumnsFromModel(false);
        objectTitleFormatTable.setColumnSelectionAllowed(false);
        objectTitleFormatTable.setRowSelectionAllowed(true);

        final TableColumn propertiesColumn = objectTitleFormatTable.getColumnModel().getColumn(ObjectTitleFormatTableModel.TITLES_FORMAT_COLUMN);
        final ObjectTitleFormatCellEditor titleFormatCellComponent = new ObjectTitleFormatCellEditor(propertiesListProvider);
        propertiesColumn.setCellEditor(titleFormatCellComponent);

        final List<AdsObjectTitleFormatDef.TitleItem> items = objectTitleFormat.getItems().list();

        for (TitleItem xItem : items) {
            final ObjectTitleItemCellEditor valueCellComponent = new ObjectTitleItemCellEditor(objectTitleFormat);
            rowEditorModel.addEditorForItem(xItem, valueCellComponent);
        }

        objectTitleFormatTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (objectTitleFormatTable.isEditing()) {
                    objectTitleFormatTable.getCellEditor().cancelCellEditing();
                }
                /*
                TableColumnModel tableColumnModel = objectTitleFormatTable.getColumnModel();

                TableCellEditor tableCellEditor = tableColumnModel.getColumn(ObjectTitleFormatTableModel.FORMAT_COLUMN).getCellEditor();
                if (tableCellEditor != null) {
                tableCellEditor.cancelCellEditing();
                }

                tableCellEditor = tableColumnModel.getColumn(ObjectTitleFormatTableModel.TITLES_FORMAT_COLUMN).getCellEditor();
                if (tableCellEditor != null) {
                tableCellEditor.cancelCellEditing();
                }*/

                updateState();
            }
        });
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        updateState();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel displayInsteadOfNULLPanel;
    private javax.swing.JButton downButton;
    private javax.swing.JScrollPane jScrollPane1;
    /*
    private javax.swing.JTable objectTitleFormatTable;
    */
    private ObjectTitleFormatTableX objectTitleFormatTable;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton testButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
