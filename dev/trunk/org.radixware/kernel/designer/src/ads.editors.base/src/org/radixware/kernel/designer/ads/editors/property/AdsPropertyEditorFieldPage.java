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
 *
 * Created on Jun 15, 2010, 1:35:40 PM
 */
package org.radixware.kernel.designer.ads.editors.property;

import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;


public class AdsPropertyEditorFieldPage extends javax.swing.JPanel {

    private DbTypePanelWrapper dbTypePanel;

    public AdsPropertyEditorFieldPage() {
        initComponents();
        typeFildEditPanel.addChangeListener(fieldTypeListener);

    }
    private ChangeListener fieldTypeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {

            if (!isMayModify || isReadOnly) {
                return;
            }


            org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel panel =
                    panel = typeFildEditPanel;

            prop.getValue().setType(panel.getCurrentType());

            update();
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        descriptionPanel = new DescriptionPanel();
        accessComboBox1 = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        typeFildEditPanel = new org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel();

        javax.swing.GroupLayout descriptionPanelLayout = new javax.swing.GroupLayout(descriptionPanel);
        descriptionPanel.setLayout(descriptionPanelLayout);
        descriptionPanelLayout.setHorizontalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        descriptionPanelLayout.setVerticalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel5.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldPage.class, "AdsPropertyEditorFieldPage.jLabel5.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(accessComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(descriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(typeFildEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accessComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(typeFildEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void update() {

        isMayModify = false;
        if (prop instanceof AdsFieldPropertyDef && !(prop instanceof AdsFieldRefPropertyDef)) {

            typeFildEditPanel.open(prop.getValue().getType(), prop, prop.getValue());
            typeFildEditPanel.setReadonly(isReadOnly);
            typeFildEditPanel.isComplete();
            
            ((DescriptionPanel) descriptionPanel).open(prop);
            ((DescriptionPanel) descriptionPanel).setReadonly(isReadOnly);
            accessComboBox1.open(prop);
            dbTypePanel.update();
        }

        isMayModify = true;
    }

    public void open(AdsPropertyDef prop, boolean isReadOnly) {
        this.prop = prop;
        this.isReadOnly = isReadOnly;
        if (prop instanceof AdsFieldPropertyDef && !(prop instanceof AdsFieldRefPropertyDef)) {
            if (dbTypePanel == null) {
                dbTypePanel = new DbTypePanelWrapper();
                jPanel1.add(dbTypePanel, BorderLayout.SOUTH);
            }
            dbTypePanel.open(prop);
        }
        update();
    }
    AdsPropertyDef prop = null;
    boolean isReadOnly;
    boolean isMayModify = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessComboBox1;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel typeFildEditPanel;
    // End of variables declaration//GEN-END:variables
}
