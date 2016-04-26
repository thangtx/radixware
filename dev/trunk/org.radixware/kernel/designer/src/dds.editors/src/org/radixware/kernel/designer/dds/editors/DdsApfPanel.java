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

package org.radixware.kernel.designer.dds.editors;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;

public class DdsApfPanel extends javax.swing.JPanel implements ChangeListener {

    private DdsAccessPartitionFamilyDef apf = null;

    public DdsApfPanel() {
        super();
        initComponents();

        this.headEditor.addChangeListener(this);
        this.referenceEditor.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (apf != null) {
            apf.setHeadId(headEditor.getDefinitionId());
            apf.setParentFamilyReferenceId(referenceEditor.getDefinitionId());
        }

        updateState();
        changeSupport.fireChange();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbHead = new javax.swing.JLabel();
        headEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        lbReference = new javax.swing.JLabel();
        referenceEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();

        setMinimumSize(new java.awt.Dimension(300, 250));
        setPreferredSize(new java.awt.Dimension(350, 250));

        lbHead.setText(org.openide.util.NbBundle.getMessage(DdsApfPanel.class, "DdsApfPanel.lbHead.text")); // NOI18N

        lbReference.setText(org.openide.util.NbBundle.getMessage(DdsApfPanel.class, "DdsApfPanel.lbReference.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbHead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbReference, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(headEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(referenceEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbHead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(headEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lbReference, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(referenceEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(194, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel headEditor;
    private javax.swing.JLabel lbHead;
    private javax.swing.JLabel lbReference;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel referenceEditor;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    //STATE CHECK UTILS
    private final StateManager stateManager = new StateManager(this);

    public void open(DdsAccessPartitionFamilyDef apf) {
        this.apf = apf;
        update();
    }

    public void update() {
        VisitorProvider headProvider = DdsVisitorProviderFactory.newApfHeadProvider();
        Definition head = apf.findHead();
        ChooseDefinitionCfg headCfg = ChooseDefinitionCfg.Factory.newInstance(apf, headProvider);
        headEditor.open(headCfg, head, apf.getHeadId());

        VisitorProvider referenceProvider = DdsVisitorProviderFactory.newApfParentReferenceProvider(apf);
        DdsReferenceDef reference = apf.findParentFamilyReference();
        ChooseDefinitionCfg referenceCfg = ChooseDefinitionCfg.Factory.newInstance(apf, referenceProvider);
        referenceEditor.open(referenceCfg, reference, apf.getParentFamilyReferenceId());

        updateState();
    }

    private void updateState() {
        headEditor.setClearable(false);
        boolean enabled = apf != null && !apf.isReadOnly();
        this.headEditor.setEnabled(enabled);
        Id headId = headEditor.getDefinitionId();
        Id refId = referenceEditor.getDefinitionId();
        boolean isTable = (headId != null && headId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE);
        boolean refEditorEnabled = enabled && (isTable || refId != null);
        this.referenceEditor.setEnabled(refEditorEnabled);
    }

    public boolean isComplete() {
        if (headEditor.getDefinitionId() != null) {
            stateManager.ok();
            return true;
        } else {
            stateManager.error("Header is not defined");
            return false;
        }
    }
}
