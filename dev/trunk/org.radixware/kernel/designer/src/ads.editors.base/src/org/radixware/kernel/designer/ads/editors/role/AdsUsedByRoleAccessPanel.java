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

package org.radixware.kernel.designer.ads.editors.role;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.TableCellRenderer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.components.SimpleTable;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class AdsUsedByRoleAccessPanel extends AdsUsedByRolePanel {

    protected class CustomTableRenderer extends JCheckBox
            implements TableCellRenderer, Serializable {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelected((value != null && ((Boolean) value).booleanValue()));

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(new EmptyBorder(1, 1, 1, 1));
            }
            this.setHorizontalAlignment(SwingConstants.CENTER);

            if (!mayModify.get(row)
                    || !getOverwriteResourceRestrictions(finishRoleList.get(row), hash).isDenied(ERestriction.ACCESS)
                    && !finishRestrictionList.get(row).isDenied(ERestriction.ACCESS)) {

                this.setEnabled(false);

                setBackground(backgroundColor);
            } else {
                this.setEnabled(true);
                setBackground(Color.WHITE);
            }
            return this;
        }
    };

    public AdsUsedByRoleAccessPanel(RadixObject obj) {
        super(obj);
        init(obj);
    }

    private void init(final RadixObject obj) {
        initComponents();





        object = (AdsDefinition) obj;

        if (object instanceof AdsExplorerItemDef) {
            type = EDrcResourceType.EXPLORER_ROOT_ITEM;
        } else if (object instanceof AdsContextlessCommandDef) {
            type = EDrcResourceType.CONTEXTLESS_COMMAND;
        }


        if ((type == EDrcResourceType.EXPLORER_ROOT_ITEM) && !(object instanceof AdsParagraphExplorerItemDef && ((AdsParagraphExplorerItemDef) object).isRoot())) {
            defId = ((AdsExplorerItemDef) object).findOwnerExplorerRoot().getId();
            subDefId = ((Definition) object).getId();
        } else {
            defId = ((Definition) object).getId();
            subDefId = null;
        }
        hash = AdsRoleDef.generateResHashKey(type, defId, subDefId);

        jTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Role",
            type == EDrcResourceType.EXPLORER_ROOT_ITEM
            ? "Allow" : (type == EDrcResourceType.CONTEXTLESS_COMMAND
            ? "Allow Execution" : "Allow Creation")
                
        }) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex > 0
                        && rowIndex < mayModify.size()
                        && mayModify.get(rowIndex);
            }
        });

        CustomTableRenderer customTableRenderer = new CustomTableRenderer();
        jTable.getColumnModel().getColumn(1).setCellRenderer(customTableRenderer);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(javax.swing.UIManager.getDefaults().getColor("TableHeader.background"));
        jTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTable.getColumnModel().getColumn(1).setMaxWidth(130);

        jTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        update();
    }

    public void update() {
        refreshList(object);
        DefaultTableModel tableModel = (DefaultTableModel) jTable.getModel();
        tableModel.setRowCount(finishRoleList.size());
        int i = 0;
        for (Definition r : finishRoleList) {
            tableModel.setValueAt(r.getQualifiedName(), i, 0);
            tableModel.setValueAt(Boolean.valueOf(!finishRestrictionList.get(i).isDenied(ERestriction.ACCESS)), i, 1);
            i++;
        }
        if (i > 0) {
            jTable.setRowSelectionInterval(0, 0);
        }
        setButtonState();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar9 = new javax.swing.JToolBar();
        jbtAdd = new javax.swing.JButton();
        jbtDel = new javax.swing.JButton();
        jbtClear = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jbtGoToRole = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new SimpleTable();

        setName("mainPanel"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jToolBar9.setFloatable(false);
        jToolBar9.setName("jToolBar9"); // NOI18N

        jbtAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAdd.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessPanel.class, "AdsUsedByRoleAccessPanel.jbtAdd.toolTipText")); // NOI18N
        jbtAdd.setFocusable(false);
        jbtAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAdd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAdd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAdd.setName("jbtAdd"); // NOI18N
        jbtAdd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtAdd);

        jbtDel.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDel.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessPanel.class, "AdsUsedByRoleAccessPanel.jbtDel.toolTipText")); // NOI18N
        jbtDel.setFocusable(false);
        jbtDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDel.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDel.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDel.setName("jbtDel"); // NOI18N
        jbtDel.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtDel);

        jbtClear.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        jbtClear.setText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessPanel.class, "AdsUsedByRoleAccessPanel.jbtClear.text")); // NOI18N
        jbtClear.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessPanel.class, "AdsUsedByRoleAccessPanel.jbtClear.toolTipText")); // NOI18N
        jbtClear.setFocusable(false);
        jbtClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtClear.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtClear.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtClear.setName("jbtClear"); // NOI18N
        jbtClear.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtClear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtClearActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtClear);

        jSeparator13.setName("jSeparator13"); // NOI18N
        jToolBar9.add(jSeparator13);

        jbtGoToRole.setIcon(RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon());
        jbtGoToRole.setText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessPanel.class, "AdsUsedByRoleAccessPanel.jbtGoToRole.text")); // NOI18N
        jbtGoToRole.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessPanel.class, "AdsUsedByRoleAccessPanel.jbtGoToRole.toolTipText")); // NOI18N
        jbtGoToRole.setFocusable(false);
        jbtGoToRole.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtGoToRole.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtGoToRole.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtGoToRole.setName("jbtGoToRole"); // NOI18N
        jbtGoToRole.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtGoToRole.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtGoToRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtGoToRoleActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtGoToRole);

        add(jToolBar9, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Role", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setName("jTable"); // NOI18N
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableMouseReleased(evt);
            }
        });
        jTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTablePropertyChange(evt);
            }
        });
        jTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddActionPerformed

        List<Definition> list = org.radixware.kernel.designer.common.general.utils.DefinitionsUtils.collectTopInside(
                this.getObject().getModule().getSegment().getLayer().getBranch(),
                new UsedByRoleVisitorProvider(this.getObject(), finishRoleList, false));
        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                object.getModule().getSegment().getLayer().getBranch(),
                null, list);

        AdsRoleDef role = (AdsRoleDef) ChooseDefinition.chooseDefinition(cfg);


        if (role != null) {
            int index = findPlace(finishRoleList, role, false);
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();

            if (index == finishRoleList.size()) {
                model.addRow(new Object[]{role.getQualifiedName(), Boolean.valueOf(true)});
                finishRoleList.add(role);
                mayModify.add(!role.isReadOnly());
                finishRestrictionList.add(Restrictions.Factory.newInstance(0));
                //finishOverwriteRestrictionList.add(or);
                // overwriteRoleList.add(lst);
            } else {
                model.insertRow(index, new Object[]{role.getQualifiedName(), Boolean.valueOf(true)});
                finishRoleList.add(index, role);
                mayModify.add(index, !role.isReadOnly());
                finishRestrictionList.add(index, Restrictions.Factory.newInstance(0));
                // finishOverwriteRestrictionList.add(index, or);
                // overwriteRoleList.add(index, lst);
            }
            jTable.setRowSelectionInterval(index, index);
        }
        setButtonState();
}//GEN-LAST:event_jbtAddActionPerformed

    private void jbtDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        int index = jTable.getSelectedRow();
        if (model.getRowCount() == 0 || index < 0) {
            return;
        }
        if (!mayModify.get(index)) {
            return;
        }
        finishRoleList.remove(index);
        finishRestrictionList.remove(index);
        model.removeRow(index);
        mayModify.remove(index);

        if (index == jTable.getRowCount()) {
            index = jTable.getRowCount() - 1;
        }
        if (index >= 0) {
            jTable.setRowSelectionInterval(index, index);
        }
        setButtonState();
}//GEN-LAST:event_jbtDelActionPerformed

    private void jbtClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtClearActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        for (int i = model.getRowCount() - 1; i > -1; i--) {
            if (mayModify.get(i)) {
                mayModify.remove(i);
                finishRoleList.remove(i);
                finishRestrictionList.remove(i);
                model.removeRow(i);
            }
        }
        int cnt_1 = model.getRowCount() - 1;
        if (cnt_1 > 1) {
            jTable.setRowSelectionInterval(cnt_1, cnt_1);
        }
        setButtonState();
}//GEN-LAST:event_jbtClearActionPerformed

    private void jbtGoToRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtGoToRoleActionPerformed
        if (jTable.getRowCount() == 0 || jTable.getSelectedRow() < 0) {
            return;
        }
        AdsRoleDef role = finishRoleList.get(jTable.getSelectedRow());
        NodesManager.selectInProjects(role);
}//GEN-LAST:event_jbtGoToRoleActionPerformed

    private void jTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseReleased
        setButtonState();
    }//GEN-LAST:event_jTableMouseReleased

    private void jTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableKeyReleased
        setButtonState();
    }//GEN-LAST:event_jTableKeyReleased

    private void jTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTablePropertyChange
        int row = jTable.getSelectedRow();
        if (row < 0 || !mayModify.get(row)) {
            return;
        }

        if (!getOverwriteResourceRestrictions(finishRoleList.get(row), hash).isDenied(ERestriction.ACCESS)
                && !finishRestrictionList.get(row).isDenied(ERestriction.ACCESS)) {
            jTable.setValueAt(!(Boolean) false, row, 1);
            return;
        }
        finishRestrictionList.set(row,
                Restrictions.Factory.newInstance(
                ((Boolean) jTable.getValueAt(row, 1)) ? 0 : ERestriction.ACCESS.getValue()));
        jTable.repaint();
        /*
         for (AdsRoleDef role : finishRoleList)
         {

         }
         */

    }//GEN-LAST:event_jTablePropertyChange
    private void setButtonState() {
        if (type == EDrcResourceType.EXPLORER_ROOT_ITEM) {
            boolean isRoot = false;
            if (object instanceof AdsParagraphExplorerItemDef) {
                AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) object;
                isRoot = par.isRoot();
            }
            jbtAdd.setEnabled(isRoot);
        }

        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        boolean e = model.getRowCount() > 0;
        jbtGoToRole.setEnabled(e);
        jbtClear.setEnabled(e);
        e = e && jTable.getSelectedRow() > -1 && mayModify.get(jTable.getSelectedRow());
        jbtDel.setEnabled(e);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JTable jTable;
    private javax.swing.JToolBar jToolBar9;
    private javax.swing.JButton jbtAdd;
    private javax.swing.JButton jbtClear;
    private javax.swing.JButton jbtDel;
    private javax.swing.JButton jbtGoToRole;
    // End of variables declaration//GEN-END:variables
}
