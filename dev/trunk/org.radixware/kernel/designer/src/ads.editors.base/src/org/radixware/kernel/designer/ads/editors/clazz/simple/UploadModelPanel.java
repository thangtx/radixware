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
 * UploadModelPanel.java
 *
 * Created on 04.03.2010, 10:35:10
 */

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.enums.EDefinitionUploadMode;


public class UploadModelPanel extends javax.swing.JPanel {

    private AdsClassDef classDef;

    /** Creates new form UploadModelPanel */
    public UploadModelPanel() {
        initComponents();
        uploadModel.setPrototypeDisplayValue(EDefinitionUploadMode.ON_DEMAND.getName());
        uploadModel.setModel(new DefinitionUploadComboModel());
        ActionListener uploadListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!UploadModelPanel.this.isUpdate){
                    String selected = (String) UploadModelPanel.this.uploadModel.getSelectedItem();
                    EDefinitionUploadMode mode = EDefinitionUploadMode.getForName(selected);
                    UploadModelPanel.this.classDef.setRuntimeUploadMode(mode); 
                }
            }

        };
        uploadModel.addActionListener(uploadListener);
    }

    public void open(AdsClassDef classDef){
        this.classDef = classDef;
        update();
    }

    private boolean isUpdate = false;
    public void update(){
        isUpdate = true;
        uploadModel.setSelectedItem(classDef.getRuntimeUploadMode().getName());
        uploadModel.setEnabled(!classDef.isReadOnly());
        isUpdate = false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        uploadModel = new javax.swing.JComboBox();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(UploadModelPanel.class, "UploadModelTitle")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(uploadModel, 0, 153, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(uploadModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox uploadModel;
    // End of variables declaration//GEN-END:variables

}