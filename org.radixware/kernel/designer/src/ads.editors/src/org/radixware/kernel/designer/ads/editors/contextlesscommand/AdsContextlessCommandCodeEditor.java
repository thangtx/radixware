/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.editors.contextlesscommand;

import javax.swing.BorderFactory;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;



public class AdsContextlessCommandCodeEditor extends javax.swing.JPanel {

    public void open(OpenInfo info, AdsContextlessCommandDef command ) {
        updateProfile(command);
        codeEditor.open(command.getSource(), info);
        codeEditor.getPane().requestFocusInWindow();

    }
     
    public void updateProfile(AdsContextlessCommandDef command){
        profileEditor.setValue(command.computeProfile().getProfileHtml());
    }
     
    public void update(AdsContextlessCommandDef command) {
        updateProfile(command);
        codeEditor.update();
    }
    
    public AdsContextlessCommandCodeEditor() {
        initComponents();
        codeEditor.setBorder(BorderFactory.createEtchedBorder());
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        codeEditor = new org.radixware.kernel.designer.common.editors.jml.JmlEditor();
        jPanel1 = new javax.swing.JPanel();
        profileEditor = new org.radixware.kernel.common.components.ExtendableTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        profileEditor.setEditorType(org.radixware.kernel.common.components.ExtendableTextField.EDITOR_HTML_TEXTFIELD);
        profileEditor.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(profileEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(profileEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codeEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(codeEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.editors.jml.JmlEditor codeEditor;
    private javax.swing.JPanel jPanel1;
    private org.radixware.kernel.common.components.ExtendableTextField profileEditor;
    // End of variables declaration//GEN-END:variables
}
