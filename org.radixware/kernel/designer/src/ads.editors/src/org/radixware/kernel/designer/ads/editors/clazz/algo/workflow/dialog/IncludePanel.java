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

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;


public class IncludePanel extends EditorDialog.EditorPanel<AdsIncludeObject> {

    public IncludePanel(AdsIncludeObject node) {
        super(node);
        initComponents();

        textName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                obj.setName(textName.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                obj.setName(textName.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                obj.setName(textName.getText());
            }

        });

        editor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(127, 157, 185), 1), NbBundle.getMessage(IncludePanel.class, "IncludePanel.panelJml.text")));
        panelJml.add(editor);

        textName.setEnabled(!node.isReadOnly());
        textAlgorithm.setEnabled(!node.isReadOnly());
        buttonAlgorithm.setEnabled(!node.isReadOnly());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelName = new javax.swing.JLabel();
        textName = new javax.swing.JTextField();
        labelAlgorithm = new javax.swing.JLabel();
        textAlgorithm = new javax.swing.JTextField();
        buttonAlgorithm = new javax.swing.JButton();
        panelJml = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setPreferredSize(new java.awt.Dimension(400, 300));
        setRequestFocusEnabled(false);

        labelName.setText(org.openide.util.NbBundle.getMessage(IncludePanel.class, "IncludePanel.labelName.text")); // NOI18N

        labelAlgorithm.setText(org.openide.util.NbBundle.getMessage(IncludePanel.class, "IncludePanel.labelAlgorithm.text")); // NOI18N

        textAlgorithm.setEditable(false);

        buttonAlgorithm.setText(org.openide.util.NbBundle.getMessage(IncludePanel.class, "IncludePanel.buttonAlgorithm.text")); // NOI18N
        buttonAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAlgorithmActionPerformed(evt);
            }
        });

        panelJml.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelAlgorithm)
                    .addComponent(labelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textName, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(textAlgorithm, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(panelJml, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelName)
                    .addComponent(textName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAlgorithm)
                    .addComponent(buttonAlgorithm)
                    .addComponent(textAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelJml, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAlgorithmActionPerformed

        VisitorProvider provider = new VisitorProvider() {

            @Override
            public boolean isContainer(RadixObject object) {
                return true;
            }

            @Override
            public boolean isTarget(RadixObject object) {
                return object instanceof AdsAlgoClassDef;
            }
        };

        AdsPage page = obj.getOwnerPage();
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(page, provider);
        AdsAlgoClassDef algoDef = (AdsAlgoClassDef) ChooseDefinition.chooseDefinition(cfg);

        if (obj.getAlgoDef() != algoDef) {
            obj.setAlgoDef(algoDef);
            textAlgorithm.setText(algoDef != null ? algoDef.getQualifiedName() : NbBundle.getMessage(getClass(), "Panel.empty.text"));
        }
    }//GEN-LAST:event_buttonAlgorithmActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAlgorithm;
    private javax.swing.JLabel labelAlgorithm;
    private javax.swing.JLabel labelName;
    private javax.swing.JPanel panelJml;
    private javax.swing.JTextField textAlgorithm;
    private javax.swing.JTextField textName;
    // End of variables declaration//GEN-END:variables

    private JmlEditor editor = new JmlEditor();

    @Override
    public void init() {
        textName.setText(obj.getName());
        textName.requestFocusInWindow();

        AdsAlgoClassDef algoDef = obj.getAlgoDef();
        textAlgorithm.setText(algoDef != null ? algoDef.getQualifiedName() : NbBundle.getMessage(getClass(), "Panel.empty.text"));

        editor.open(obj.getPreExecute());
    }

    @Override
    public void apply() {
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_IncludePanel");
    }
}