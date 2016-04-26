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
 * AdsEnumClassStructPanel.java
 *
 * Created on Aug 16, 2011, 10:02:45 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.util.List;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableRowSorter;
import org.openide.actions.CopyAction;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class AdsEnumClassStructureEditorPanel extends javax.swing.JPanel {
    public static String getUniqueName(List<? extends RadixObject> list, String prototype) {
        assert list != null && prototype != null : "illegal arguments";

        final String digits = "0123456789";
        int pos = prototype.length() - 1;
        while (pos >= 0 && digits.indexOf(prototype.charAt(pos)) != -1) {
            --pos;
        }
        ++pos;

        int ver = 0;
        String name = prototype.toString();

        if (pos < prototype.length()) {
            ver = Integer.parseInt(name.substring(pos));
            name = name.substring(0, pos);
        }

        String candidate = name + (ver > 0 ? ver : "");
        while (!isUniqueName(list, candidate, (RadixObject) null)) {
            ++ver;
            candidate = name + ver;
        }
        return candidate;
    }
    public static boolean isUniqueName(List<? extends RadixObject> list, String name, RadixObject exclude) {
        assert list != null : "empty list";

        for (RadixObject o : list) {
            if (o.getName().equals(name) && o != exclude) {
                return false;
            }
        }
        return true;
    }
    private static final class TablePanel extends FilteredTablePanel {

        @Override
        protected AdvanceTable<?> createTable() {
            return new AdsEnumClassStructureTable();
        }
    }

    public AdsEnumClassStructureEditorPanel() {
        initComponents();

        getStructureTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtons();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnDuplicate = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        tablePanel = new TablePanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMinimumSize(new java.awt.Dimension(400, 240));

        btnAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        btnAdd.setText(org.openide.util.NbBundle.getMessage(AdsEnumClassStructureEditorPanel.class, "AdsEnumClassStructureEditorPanel.btnAdd.text")); // NOI18N
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdd.setPreferredSize(new java.awt.Dimension(80, 25));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnRemove.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        btnRemove.setText(org.openide.util.NbBundle.getMessage(AdsEnumClassStructureEditorPanel.class, "AdsEnumClassStructureEditorPanel.btnRemove.text")); // NOI18N
        btnRemove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemove.setPreferredSize(new java.awt.Dimension(80, 25));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnDuplicate.setIcon(SystemAction.get(CopyAction.class).getIcon());
        btnDuplicate.setText(org.openide.util.NbBundle.getMessage(AdsEnumClassStructureEditorPanel.class, "AdsEnumClassStructureEditorPanel.btnDuplicate.text")); // NOI18N
        btnDuplicate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDuplicate.setPreferredSize(new java.awt.Dimension(80, 25));
        btnDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDuplicateActionPerformed(evt);
            }
        });

        btnUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon(13, 13));
        btnUp.setText(org.openide.util.NbBundle.getMessage(AdsEnumClassStructureEditorPanel.class, "AdsEnumClassStructureEditorPanel.btnUp.text")); // NOI18N
        btnUp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUp.setPreferredSize(new java.awt.Dimension(80, 25));
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon(13, 13));
        btnDown.setText(org.openide.util.NbBundle.getMessage(AdsEnumClassStructureEditorPanel.class, "AdsEnumClassStructureEditorPanel.btnDown.text")); // NOI18N
        btnDown.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDown.setPreferredSize(new java.awt.Dimension(80, 25));
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });

        tablePanel.setLayout(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDown, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(btnUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDuplicate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDuplicate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
            .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
    addParamMethod();
}//GEN-LAST:event_btnAddActionPerformed

private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
    removeParamMethod();
}//GEN-LAST:event_btnRemoveActionPerformed

private void btnDuplicateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDuplicateActionPerformed
    duplicateParamMethod();
}//GEN-LAST:event_btnDuplicateActionPerformed

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        upParamMethod();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        downParamMethod();
    }//GEN-LAST:event_btnDownActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnDuplicate;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUp;
    private javax.swing.JPanel tablePanel;
    // End of variables declaration//GEN-END:variables
    private AdsEnumClassDef enumClass = null;
    private boolean isReadonly = false;
    private boolean isUpdating = false;
    private AdsEnumClassStructTableModel fieldStructModel;

    public void open(AdsEnumClassDef enumClass) {
        this.enumClass = enumClass;
        this.isReadonly = enumClass.isReadOnly();
        fieldStructModel = new AdsEnumClassStructTableModel(enumClass);
        getStructureTable().open(fieldStructModel);

        final TableRowSorter<AdvanceTableModel> sorter = getTablePanel().setSorter();
        sorter.addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (e.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
                    updateButtons();
                }
            }
        });

        update();
    }

    public void update() {
        isUpdating = true;

//        updateTable();
        updateButtons();
        updateUI();

        isUpdating = false;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
    }

    private void updateUpDownButtons() {

        int row = getStructureTable().getModelSelectedRow();
        if (!getTablePanel().getSearchField().getText().isEmpty()
                || getStructureTable().getSelectedRowCount() > 1
                || row < 0
                || fieldStructModel.getRowSourceScope(row) != AdvanceTableModel.ERowScope.LOCAL) {

            btnUp.setEnabled(false);
            btnDown.setEnabled(false);
            return;
        }

        TableRowSorter<?> sorter = getStructureTable().getRowSorter();
        if (sorter != null && sorter instanceof AdvanceTable.RowSorter) {
            AdvanceTable.RowSorter sort = (AdvanceTable.RowSorter) sorter;
            if (sort.getCurrentSortOrder() != SortOrder.UNSORTED) {
                btnUp.setEnabled(false);
                btnDown.setEnabled(false);
                return;
            }
        }

        int fullSize = fieldStructModel.getModelSource().getFieldStruct().getFullSize(),
                localSize = fieldStructModel.getModelSource().getFieldStruct().getLocal().size();
        btnUp.setEnabled(row > fullSize - localSize);
        btnDown.setEnabled(row + 1 < fullSize);
    }

    private void updateDuplicateRemoveButtons() {

        int count = getStructureTable().getSelectedRowCount();

        if (count > 0) {
            int[] rows = getStructureTable().getModelSelectedRows();

            for (int row : rows) {
                if (fieldStructModel.getRowSourceScope(row) != AdvanceTableModel.ERowScope.LOCAL) {
                    btnRemove.setEnabled(false);
                    btnDuplicate.setEnabled(count == 1);
                    return;
                }
            }
            btnDuplicate.setEnabled(count == 1);
            btnRemove.setEnabled(true);
            return;
        }
        btnRemove.setEnabled(false);
        btnDuplicate.setEnabled(false);
    }

    private void updateButtons() {

        updateUpDownButtons();
        updateDuplicateRemoveButtons();
    }

    private AdsEnumClassStructureTable getStructureTable() {
        return (AdsEnumClassStructureTable) getTablePanel().getValuesTable();
    }

    private TablePanel getTablePanel() {
        return (TablePanel) tablePanel;
    }

    /////////////////////////////////////////////////////////////////
    ///////////////////// method's handlers /////////////////////////
    /////////////////////////////////////////////////////////////////
    private void upParamMethod() {
        int row = getStructureTable().getModelSelectedRow();
        fieldStructModel.moveRowUp(row);
        getStructureTable().setRowSelectionInterval(row - 1, row - 1);
    }

    private void downParamMethod() {
        int row = getStructureTable().getModelSelectedRow();
        fieldStructModel.moveRowDown(row);
        getStructureTable().setRowSelectionInterval(row + 1, row + 1);
    }

    private void duplicateParamMethod() {
        int row = getStructureTable().convertRowIndexToModel(getStructureTable().getModelSelectedRow());

        AdsFieldParameterDef src = fieldStructModel.getRowSource(row),
                duplParam = AdsFieldParameterDef.Factory.newTemporaryInstanceFrom(enumClass, src);
        duplParam.setName(getUniqueName(enumClass.getFieldStruct().get(EScope.LOCAL_AND_OVERWRITE), duplParam.getName()).toString());

        AdsEnumClassParameterDisplayer addParamDisplayer = new AdsEnumClassParameterDisplayer(enumClass, duplParam, false);

        if (addParamDisplayer.showModal()) {
            fieldStructModel.addRow(AdsFieldParameterDef.Factory.newInstanceFrom(addParamDisplayer.getParameter()));
        }
    }

    private void addParamMethod() {

        AdsFieldParameterDef newParam = AdsFieldParameterDef.Factory.newTemporaryInstance(enumClass);
        newParam.setName(getUniqueName(enumClass.getFieldStruct().get(EScope.LOCAL_AND_OVERWRITE), "newParameter").toString());

        AdsEnumClassParameterDisplayer addParamDisplayer = new AdsEnumClassParameterDisplayer(enumClass, newParam, true);

        if (addParamDisplayer.showModal()) {
            fieldStructModel.addRow(AdsFieldParameterDef.Factory.newInstanceFrom(addParamDisplayer.getParameter()));
        }
    }

    private void removeParamMethod() {
        int[] rows = getStructureTable().getModelSelectedRows();

        String message = NbBundle.getMessage(AdsEnumClassStructureEditorPanel.class, "AdsEnumClassFieldStructParam-ConfirmRemoveMessage"),
                title = NbBundle.getMessage(AdsEnumClassStructureEditorPanel.class, "AdsEnumClassFieldStructParam-ConfirmRemoveTitle");

        boolean confirm = DialogUtils.messageConfirmation(message);
        if (confirm) {
            fieldStructModel.removeParameters(rows);
            int rowCount = fieldStructModel.getRowCount();
            if (rowCount > 0) {
                if (rows[0] < rowCount) {
                    getStructureTable().setRowSelectionInterval(rows[0], rows[0]);
                } else {
                    getStructureTable().setRowSelectionInterval(rowCount - 1, rowCount - 1);
                }
            }
        }
    }
}
