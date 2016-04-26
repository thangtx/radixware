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
 * GeneralPanel.java
 *
 * Created on Feb 10, 2009, 11:20:59 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.algo;

import javax.swing.JButton;
import org.openide.util.NbBundle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.components.ExtendableTextField;


public class GeneralPanel extends javax.swing.JPanel {

    private AdsAlgoClassDef algoDef;

    private void deleteReplacement(AdsAlgoClassDef algoDef) {
        if (algoDef.getReplacementId() == null) {
            return;
        }
        AdsAlgoClassDef repDef = (AdsAlgoClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(algoDef).findById(algoDef.getReplacementId()).get();
        if (repDef != null) {
            deleteReplacement(repDef);
            repDef.delete();
        }
        algoDef.setReplacementId(null);
    }

    private void createReplacement(AdsAlgoClassDef algoDef) {
        final AdsAlgoClassDef repDef = algoDef.getClipboardSupport().duplicate();
        algoDef.getModule().getDefinitions().add(repDef);
        repDef.setName(algoDef.getName() + "_replacement");
        algoDef.setReplacementId(repDef.getId());
        repDef.setReplacedId(algoDef.getId());
    }

    /** Creates new form GeneralPanel */
    public GeneralPanel() {
        initComponents();

        JButton button = textReplacement.addButton();
        button.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteReplacement(algoDef);
                createReplacement(algoDef);
                update(algoDef);
            }
        });

        button = textReplacement.addButton();
        button.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteReplacement(algoDef);
                update(algoDef);
            }
        });

        button = textProcess.addButton();
        button.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsClassDef processDef = (AdsClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(algoDef).findById(AdsAlgoClassDef.PROCESS_CLASS_ID).get();
                if (processDef != null) {
                    ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(algoDef, AdsVisitorProviders.newInheritanceProvider(processDef));
                    AdsClassDef def = (AdsClassDef) ChooseDefinition.chooseDefinition(cfg);
                    algoDef.setProcessId(def != null ? def.getId() : null);
                }
                update(algoDef);
            }
        });

        button = textProcess.addButton();
        button.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                algoDef.setProcessId(null);
                update(algoDef);
            }
        });
    }

    public void update(AdsAlgoClassDef algoDef) {
        this.algoDef = algoDef;
        AdsClassDef def;

        Id replacementId = algoDef.getReplacementId();
        def = (AdsAlgoClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(algoDef).findById(replacementId).get();
        textReplacement.setValue(def != null ? def.getQualifiedName() : NbBundle.getMessage(getClass(), "GeneralPanel.empty.text"));

        Id replacedId = algoDef.getReplacedId();
        def = (AdsAlgoClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(algoDef).findById(replacedId).get();
        textReplaced.setValue(def != null ? def.getQualifiedName() : NbBundle.getMessage(getClass(), "GeneralPanel.empty.text"));

        Id processId = algoDef.getProcessId();
        def = (AdsClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(algoDef).findById(processId).get();
        textProcess.setValue(def != null ? def.getQualifiedName() : NbBundle.getMessage(getClass(), "GeneralPanel.empty.text"));
    }

    public void setReadonly(boolean readonly) {
        textProcess.setReadOnly(readonly);
        textReplaced.setReadOnly(readonly);
        textReplacement.setReadOnly(readonly);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textReplacement = new ExtendableTextField(true);
        labelReplacement = new javax.swing.JLabel();
        labelReplaced = new javax.swing.JLabel();
        textReplaced = new ExtendableTextField(true);
        labelProcess = new javax.swing.JLabel();
        textProcess = new ExtendableTextField(true);

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.border.title"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(32767, 130));

        labelReplacement.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.labelReplacement.text")); // NOI18N

        labelReplaced.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.labelReplaced.text")); // NOI18N

        labelProcess.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.labelProcess.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelReplaced)
                    .addComponent(labelReplacement)
                    .addComponent(labelProcess))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textReplacement, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(textReplaced, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(textProcess, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textReplacement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelReplacement))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textReplaced, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelReplaced))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelProcess))
                .addGap(8, 8, 8))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelProcess;
    private javax.swing.JLabel labelReplaced;
    private javax.swing.JLabel labelReplacement;
    private org.radixware.kernel.common.components.ExtendableTextField textProcess;
    private org.radixware.kernel.common.components.ExtendableTextField textReplaced;
    private org.radixware.kernel.common.components.ExtendableTextField textReplacement;
    // End of variables declaration//GEN-END:variables
}
