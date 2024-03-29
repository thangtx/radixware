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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsMergeObject;


public class MergePanel extends EditorDialog.EditorPanel<AdsMergeObject> {

    public MergePanel(AdsMergeObject node) {
        super(node);
        initComponents();

        spinnerSyncCount.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer m = (Integer)spinnerSyncCount.getValue();
                obj.setM(m.intValue());
            }
        });

        spinnerEntryCount.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer ec = (Integer)spinnerEntryCount.getValue();
                obj.setEC(ec.intValue());
            }
        });

        spinnerSyncCount.setEnabled(!node.isReadOnly());
        spinnerEntryCount.setEnabled(!node.isReadOnly());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spinnerSyncCount = new javax.swing.JSpinner();
        LabelSyncCount = new javax.swing.JLabel();
        spinnerEntryCount = new javax.swing.JSpinner();
        labelFrom = new javax.swing.JLabel();
        labelEntryCount = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMaximumSize(new java.awt.Dimension(200, 70));
        setMinimumSize(new java.awt.Dimension(200, 70));
        setPreferredSize(new java.awt.Dimension(200, 70));
        setRequestFocusEnabled(false);

        spinnerSyncCount.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        LabelSyncCount.setText(org.openide.util.NbBundle.getMessage(MergePanel.class, "MergePanel.LabelSyncCount.text")); // NOI18N

        spinnerEntryCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 5, 1));

        labelFrom.setText(org.openide.util.NbBundle.getMessage(MergePanel.class, "MergePanel.labelFrom.text")); // NOI18N

        labelEntryCount.setText(org.openide.util.NbBundle.getMessage(MergePanel.class, "MergePanel.labelEntryCount.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelSyncCount)
                    .addComponent(labelEntryCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spinnerEntryCount)
                    .addComponent(spinnerSyncCount, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFrom)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelSyncCount)
                    .addComponent(spinnerSyncCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFrom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEntryCount)
                    .addComponent(spinnerEntryCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelSyncCount;
    private javax.swing.JLabel labelEntryCount;
    private javax.swing.JLabel labelFrom;
    private javax.swing.JSpinner spinnerEntryCount;
    private javax.swing.JSpinner spinnerSyncCount;
    // End of variables declaration//GEN-END:variables

    @Override
    public void init() {        
        spinnerSyncCount.setValue(new Integer(obj.getM()));
        spinnerEntryCount.setValue(new Integer(obj.getEC()));
    }

    @Override
    public void apply() {
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_MergePanel");
    }
}
