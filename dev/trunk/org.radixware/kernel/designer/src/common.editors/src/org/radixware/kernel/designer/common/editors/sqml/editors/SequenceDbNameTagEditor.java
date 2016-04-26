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

package org.radixware.kernel.designer.common.editors.sqml.editors;

import java.awt.Component;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.SequenceDbNameTag;
import org.radixware.kernel.common.sqml.tags.SequenceDbNameTag.Postfix;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;


public class SequenceDbNameTagEditor extends SqmlTagEditor<SequenceDbNameTag> {

    public SequenceDbNameTagEditor() {
        initComponents();
    }

    private Definition getSequenceDefinition() {
        return sequencePanel.getDefinition();

    }

    private Postfix getPostfix() {
        if (rbCurrent.isSelected()) {
            return Postfix.CUR_VAL;
        } else if (rbNext.isSelected()) {
            return Postfix.NEXT_VAL;
        }
        return Postfix.NONE;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgPostrfix = new javax.swing.ButtonGroup();
        sequencePanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        lbSequence = new javax.swing.JLabel();
        pnValues = new javax.swing.JPanel();
        rbCurrent = new javax.swing.JRadioButton();
        rbNext = new javax.swing.JRadioButton();
        rbNone = new javax.swing.JRadioButton();

        setMinimumSize(new java.awt.Dimension(500, 145));
        setPreferredSize(new java.awt.Dimension(500, 145));

        lbSequence.setText(org.openide.util.NbBundle.getMessage(SequenceDbNameTagEditor.class, "SequenceDbNameTagEditor.lbSequence.text")); // NOI18N

        pnValues.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SequenceDbNameTagEditor.class, "SequenceDbNameTagEditor.pnValues.border.title"))); // NOI18N

        bgPostrfix.add(rbCurrent);
        rbCurrent.setSelected(true);
        rbCurrent.setText(org.openide.util.NbBundle.getMessage(SequenceDbNameTagEditor.class, "SequenceDbNameTagEditor.rbCurrent.text")); // NOI18N

        bgPostrfix.add(rbNext);
        rbNext.setText(org.openide.util.NbBundle.getMessage(SequenceDbNameTagEditor.class, "SequenceDbNameTagEditor.rbNext.text")); // NOI18N

        bgPostrfix.add(rbNone);
        rbNone.setText(org.openide.util.NbBundle.getMessage(SequenceDbNameTagEditor.class, "SequenceDbNameTagEditor.rbNone.text")); // NOI18N

        javax.swing.GroupLayout pnValuesLayout = new javax.swing.GroupLayout(pnValues);
        pnValues.setLayout(pnValuesLayout);
        pnValuesLayout.setHorizontalGroup(
            pnValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnValuesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbCurrent)
                .addGap(18, 18, 18)
                .addComponent(rbNext)
                .addGap(18, 18, 18)
                .addComponent(rbNone)
                .addContainerGap(152, Short.MAX_VALUE))
        );
        pnValuesLayout.setVerticalGroup(
            pnValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnValuesLayout.createSequentialGroup()
                .addGroup(pnValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbCurrent)
                    .addComponent(rbNext)
                    .addComponent(rbNone))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnValues, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sequencePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .addComponent(lbSequence, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbSequence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sequencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnValues, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgPostrfix;
    private javax.swing.JLabel lbSequence;
    private javax.swing.JPanel pnValues;
    private javax.swing.JRadioButton rbCurrent;
    private javax.swing.JRadioButton rbNext;
    private javax.swing.JRadioButton rbNone;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel sequencePanel;
    // End of variables declaration//GEN-END:variables

    @Override
    protected boolean tagDefined() {
        return getSequenceDefinition() != null;
    }

    @Override
    protected void applyChanges() {
        getTag().setSequenceId(sequencePanel.getDefinitionId());
        getTag().setPostfix(getPostfix());
    }

    @Override
    protected String getTitleKey() {
        return "sequence-db-name-tag-edit-panel-title";
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        sequencePanel.setEnabled(!readOnly);
        for (Component c : pnValues.getComponents()) {
            c.setEnabled(!readOnly);
        }
    }

    @Override
    protected void afterOpen() {
        SequenceDbNameTag tag = getObject();
        Sqml sqml = tag.getOwnerSqml();
        if (sqml == null) {
            sqml = (Sqml) getOpenInfo().getLookup().lookup(ScmlEditor.class).getSource();
        }
        Definition sequence = tag.findSequence();
        if (sequence == null) {
            sequence = getOpenInfo().getLookup().lookup(DdsSequenceDef.class);
        }
        if (sequence == null) {
            sequence = sqml.getEnvironment().findSequenceById(tag.getSequenceId());
        }
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                sqml,
                SqmlVisitorProviderFactory.newSequenceDbNameTagProvider());
        sequencePanel.setClearable(false);
        sequencePanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateOkState();
            }
        });

        sequencePanel.open(cfg, sequence, tag.getSequenceId());
        if (tag.getPostfix() != null) {
            if (tag.getPostfix().equals(Postfix.CUR_VAL)) {
                rbCurrent.setSelected(true);
            } else if (tag.getPostfix().equals(Postfix.NEXT_VAL)) {
                rbNext.setSelected(true);
            } else {
                rbNone.setSelected(true);
            }
        }

        updateOkState();
    }

    @Override
    protected boolean beforeShowModal() {
        return true;
    }
}
