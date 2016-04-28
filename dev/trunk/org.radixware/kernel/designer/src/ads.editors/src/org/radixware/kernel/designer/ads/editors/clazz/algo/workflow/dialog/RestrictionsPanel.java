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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import java.util.EnumSet;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class RestrictionsPanel extends EditorDialog.EditorPanel<AdsAppObject> {

    private final List<ERestriction> availableRestrictions;
    private EnumSet<ERestriction> usedRestrictions;

    public RestrictionsPanel(AdsAppObject node, List<ERestriction> availableRestrictions) {
        super(node);
        initComponents();
        this.availableRestrictions = availableRestrictions;
        restrictions = node.getPropByName("restrictions");
        tableRestrictions.setEnabled(!node.isReadOnly());
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
        tableRestrictions = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 430));
        setPreferredSize(new java.awt.Dimension(500, 430));
        setRequestFocusEnabled(false);

        tableRestrictions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Deny"
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
        jScrollPane1.setViewportView(tableRestrictions);
        tableRestrictions.getColumnModel().getColumn(0).setHeaderValue(RadixResourceBundle.getMessage(RestrictionsPanel.class, "RestrictionsPanel.tableRestrictions.columnModel.title0")); // NOI18N
        tableRestrictions.getColumnModel().getColumn(1).setMinWidth(70);
        tableRestrictions.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableRestrictions.getColumnModel().getColumn(1).setMaxWidth(70);
        tableRestrictions.getColumnModel().getColumn(1).setHeaderValue(RadixResourceBundle.getMessage(RestrictionsPanel.class, "RestrictionsPanel.tableRestrictions.columnModel.title1")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableRestrictions;
    // End of variables declaration//GEN-END:variables

    private AdsAppObject.Prop restrictions;

    @Override
    public void init() {
        usedRestrictions = ERestriction.fromBitField((Long)restrictions.getValue().toObject(EValType.INT));
        tableRestrictions.setModel(new RestrictionsModel());
        tableRestrictions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRestrictions.getColumnModel().getColumn(1).setMinWidth(70);
        tableRestrictions.getColumnModel().getColumn(1).setMaxWidth(70);
        tableRestrictions.getColumnModel().getColumn(1).setPreferredWidth(70);
    }

    @Override
    public void apply() {
        restrictions.setValue(String.valueOf(ERestriction.toBitField(usedRestrictions)));
    }

    @Override
    public String getTitle() {
        return RadixResourceBundle.getMessage(getClass(), "CTL_RestrictionProperties");
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.SECURITY.KEY;
    }

    class RestrictionsModel extends AbstractTableModel {

        private final String[] columns = new String[] {
            RadixResourceBundle.getMessage(RestrictionsPanel.class, "RestrictionsPanel.tableRestrictions.columnModel.title0"),
            RadixResourceBundle.getMessage(RestrictionsPanel.class, "RestrictionsPanel.tableRestrictions.columnModel.title1")
        };

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public int getRowCount() {
            return availableRestrictions.size();
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return availableRestrictions.get(row).getName();
                case 1:
                    return Boolean.valueOf(usedRestrictions.contains(availableRestrictions.get(row)));
            }
            return null;
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col > 0;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
                case 1:
                    Boolean v = (Boolean)value;
                    if (v)
                        usedRestrictions.add(availableRestrictions.get(row));
                    else
                        usedRestrictions.remove(availableRestrictions.get(row));
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }
}