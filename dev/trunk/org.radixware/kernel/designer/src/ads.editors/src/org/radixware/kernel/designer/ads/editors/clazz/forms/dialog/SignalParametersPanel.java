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
 * SignalParametersPanel.java
 *
 * Created on Jul 30, 2009, 12:21:29 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.choosetype.ETypeNature;


public class SignalParametersPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private final AdsAbstractUIDef customDef;

    /** Creates new form SignalParametersPanel */
    public SignalParametersPanel(final AdsAbstractUIDef customDef, String signalName, List<AdsTypeDeclaration> types,/*  AdsUISignalDef signal,*/ boolean isNewSignal) {
        this.customDef = customDef;
        //this.signal=signal;
        initComponents();
        table.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                onFocusEvent();
            }

            @Override
            public void focusGained(FocusEvent e) {
                onFocusEvent();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                SignalParametersPanel.this.onFocusEvent();
            }
        });
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseType.DefaultTypeFilter typeFilter = new ChooseType.DefaultTypeFilter(customDef, null);
                typeFilter.except(ETypeNature.JAVA_PRIMITIVE);
                AdsTypeDeclaration newtype = ChooseType.getInstance().chooseType(typeFilter);
                if (newtype != null) {
                    addType(newtype);
                }
            }
        });
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeType();
            }
        });
        btnUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveRow(true);
            }
        });
        btnDown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveRow(false);
            }
        });
        createUI(signalName, types, isNewSignal);
    }

    private void createUI(String signalName, List<AdsTypeDeclaration> types,/*  AdsUISignalDef signal,*/ boolean isNewSignal) {
        btnAdd.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        btnRemove.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        btnDown.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());
        btnUp.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon());

        setupTableUI();
        edSignalName.setText(signalName);
        if (!isNewSignal) {
            // edSignalName.setText("newSigсnal");
            //}
            //else{
            edSignalName.setText(signalName);
            edSignalName.setEnabled(false);
            lbSignalName.setEnabled(false);
            showType(types);
        }
        btnDown.setEnabled(false);
        btnUp.setEnabled(false);
        btnRemove.setEnabled(false);
        onFocusEvent();
    }

    private void onFocusEvent() {
        if (tableModel != null) {
            if (tableModel.getRowCount() > 0) {
                if (table.getSelectedRow() != -1) {
                    btnRemove.setEnabled(true);
                    btnUp.setEnabled(table.getSelectedRow() > 0);
                    btnDown.setEnabled(table.getSelectedRow() < (tableModel.getRowCount() - 1));
                }
            } else {
                btnRemove.setEnabled(false);
                btnUp.setEnabled(false);
                btnDown.setEnabled(false);
            }
        }
    }

    private void moveRow(boolean up) {
        stopCellEditing();
        int row = table.getSelectedRow();
        CommonParametersEditorCellLib.TypePresentation typeitem = (CommonParametersEditorCellLib.TypePresentation) tableModel.getValueAt(row, 0);
        tableModel.removeRow(row);
        int newrow = up ? row - 1 : row + 1;
        tableModel.insertRow(newrow, new Object[]{typeitem});
        table.requestFocusInWindow();
        table.getSelectionModel().setSelectionInterval(newrow, newrow);
    }
    CommonParametersEditorCellLib.TypeCellEditor typeEditor;

    private void setupTableUI() {
        String[] titles = new String[1];
        titles[0] = NbBundle.getMessage(SignalParametersPanel.class, "Type");
        tableModel = new DefaultTableModel(titles, 0);
        table.setModel(tableModel);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        typeEditor = new CommonParametersEditorCellLib.TypeCellEditor(new ChangeSupport(this), false, false, tableModel);
        //ExtendableTextField textField=(ExtendableTextField)typeEditor.getComponent();
        //Dimension d=new Dimension(textField.getPreferredSize().width,table.getRowHeight());
        //textField.setPreferredSize(d);

        table.getColumnModel().getColumn(0).setCellEditor(typeEditor);
        table.getColumnModel().getColumn(0).setCellRenderer(new CommonParametersEditorCellLib.TypeCellRenderer());
    }

    private void showType(List<AdsTypeDeclaration> types) {
        if (types == null) {
            return;
        }
        for (int i = 0, size = types.size(); i < size; i++) {
            CommonParametersEditorCellLib.TypePresentation typeitem = new CommonParametersEditorCellLib.TypePresentation(types.get(i), customDef);
            tableModel.addRow(new Object[]{typeitem});
        }
    }

    private void addType(AdsTypeDeclaration newtype) {
        stopCellEditing();
        CommonParametersEditorCellLib.TypePresentation typeitem = new CommonParametersEditorCellLib.TypePresentation(newtype, customDef);
        tableModel.addRow(new Object[]{typeitem});
        onFocusEvent();
        //table.getSelectionModel().setSelectionInterval(tableModel.getRowCount()-1, tableModel.getRowCount()-1);
    }

    private void stopCellEditing() {
        if (typeEditor.getComponent().isShowing()) {
            typeEditor.stopCellEditing();
        }
    }

    private void removeType() {
        stopCellEditing();
        int row = table.getSelectedRow();
        tableModel.removeRow(row);
        if (tableModel.getRowCount() > 0) {
            int index = row == 0 ? row : row - 1;
            table.getSelectionModel().setSelectionInterval(index, index);
        }
    }

    public List<AdsTypeDeclaration> getSignalTypes() {
        List<AdsTypeDeclaration> types = new LinkedList<AdsTypeDeclaration>();
        int cols = tableModel.getColumnCount();
        for (int i = 0; i <= tableModel.getRowCount() - 1; i++) {
            AdsTypeDeclaration t = ((CommonParametersEditorCellLib.TypePresentation) tableModel.getValueAt(i, cols - 1)).getType();
            types.add(t);
        }
        return types;
    }

    public String getSignalName() {
        return edSignalName.getText();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        edSignalName = new javax.swing.JTextField();
        lbSignalName = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        lbParameters = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();

        setMinimumSize(new java.awt.Dimension(395, 300));
        setPreferredSize(new java.awt.Dimension(395, 300));

        edSignalName.setText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.edSignalName.text")); // NOI18N
        edSignalName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edSignalNameActionPerformed(evt);
            }
        });

        lbSignalName.setText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.lbSignalName.text")); // NOI18N

        btnAdd.setText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnAdd.text")); // NOI18N
        btnAdd.setToolTipText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnAdd.toolTipText")); // NOI18N
        btnAdd.setPreferredSize(new java.awt.Dimension(32, 32));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnRemove.setText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnRemove.text")); // NOI18N
        btnRemove.setToolTipText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnRemove.toolTipText")); // NOI18N
        btnRemove.setPreferredSize(new java.awt.Dimension(32, 32));

        btnUp.setText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnUp.text")); // NOI18N
        btnUp.setToolTipText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnUp.toolTipText")); // NOI18N
        btnUp.setPreferredSize(new java.awt.Dimension(32, 32));

        btnDown.setText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnDown.text")); // NOI18N
        btnDown.setToolTipText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.btnDown.toolTipText")); // NOI18N
        btnDown.setPreferredSize(new java.awt.Dimension(32, 32));

        lbParameters.setText(org.openide.util.NbBundle.getMessage(SignalParametersPanel.class, "SignalParametersPanel.lbParameters.text")); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setRowHeight(24);
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbParameters)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, 0, 357, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbSignalName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edSignalName, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnDown, btnRemove, btnUp});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edSignalName)
                    .addComponent(lbSignalName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbParameters)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAdd, btnDown, btnRemove, btnUp});

    }// </editor-fold>//GEN-END:initComponents

    private void edSignalNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edSignalNameActionPerformed
        // TODO add your handling code here:
        //newSignal.setName(jTextField1.getText());
    }//GEN-LAST:event_edSignalNameActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUp;
    private javax.swing.JTextField edSignalName;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbParameters;
    private javax.swing.JLabel lbSignalName;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    // End of variables declaration//GEN-END:variables
}
