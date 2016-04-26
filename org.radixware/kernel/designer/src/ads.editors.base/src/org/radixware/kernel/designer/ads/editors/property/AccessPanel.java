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

package org.radixware.kernel.designer.ads.editors.property;

import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;
import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel.Item;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeSupport;


final class AccessPanel extends javax.swing.JPanel {

    private final ValueChangeSupport<EAccess> valueChangeSupport = new ValueChangeSupport<>();
    private KernelEnumComboBoxModel<EAccess> model;

    public AccessPanel(String lable, String toolTip) {
        initComponents();

        lblAccess.setVisible(lable != null && !lable.isEmpty());
        lblAccess.setText(lable);

        cmbAccess.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                valueChangeSupport.fireValueChange(AccessPanel.this, getAccess(), null);
            }
        });
        cmbAccess.setToolTipText(toolTip);
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        if (mgr != null) {
            super.setLayout(mgr);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        lblAccess.setEnabled(enabled);
        cmbAccess.setEnabled(enabled);
    }

    public void open(EAccess access, EAccess... deny) {
        final List<Item<EAccess>> additional = Arrays.asList((KernelEnumComboBoxModel.Item<EAccess>) KernelEnumComboBoxModel.INHERITED);

        model = access != null
            ? new KernelEnumComboBoxModel<>(EAccess.class, additional, Arrays.asList(deny), access)
            : new KernelEnumComboBoxModel<>(EAccess.class, additional, Arrays.asList(deny), KernelEnumComboBoxModel.INHERITED);

        cmbAccess.setModel(model);
    }

    public EAccess getAccess() {
        return model.getSelectedItemSource();
    }

    public final void removeValueChangeListener(ValueChangeListener<EAccess> listener) {
        valueChangeSupport.removeValueChangeListener(listener);
    }

    public final void addValueChangeListener(ValueChangeListener<EAccess> listener) {
        valueChangeSupport.addValueChangeListener(listener);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblAccess = new javax.swing.JLabel();
        cmbAccess = new javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EAccess>>();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));
        setLayout(new java.awt.BorderLayout());

        lblAccess.setText(org.openide.util.NbBundle.getMessage(AccessPanel.class, "AccessPanel.lblAccess.text")); // NOI18N
        lblAccess.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 8));
        add(lblAccess, java.awt.BorderLayout.LINE_START);

        cmbAccess.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(cmbAccess, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<KernelEnumComboBoxModel.Item<EAccess>> cmbAccess;
    private javax.swing.JLabel lblAccess;
    // End of variables declaration//GEN-END:variables
}
