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
 * FormatGroupSeparatorPanel.java
 *
 * Created on Nov 24, 2011, 12:59:04 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;


public class FormatGroupSeparatorPanel extends javax.swing.JPanel {
    private JCheckBox cbGroupSeparator;
    //private String groupSeparator_;

    /** Creates new form FormatGroupSeparatorPanel */
    public FormatGroupSeparatorPanel(final FormatStringEditorPanel formatStringEditorPanel,
            String groupSeparator,int groupSize) {
        //String groupSeparator=fgroupSeparator;
        initComponents();
        boolean isGroupSeparatorSet = groupSeparator!=null ; 
        String title="Set Group Separator";
        cbGroupSeparator=new JCheckBox(title);
        cbGroupSeparator.setBorder(null);
        cbGroupSeparator.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                setPanelEnable();
                //boolean isSetGroupSeparator=cbGroupSeparator.isSelected();
                //if(isSetGroupSeparator){
                //    groupSeparator_=null;
                //}else{
                //    groupSeparator_=fieldSeparator.getText();
                //}
                formatStringEditorPanel.check();
            }
        });
        cbGroupSeparator.setSelected(isGroupSeparatorSet);
        setBorder(new ComponentTitledBorder(cbGroupSeparator, this, BorderFactory.createTitledBorder("")));
        
        //lbSeparator=new javax.swing.JLabel("Separator:");        
        //fieldSeparator = new javax.swing.JFormattedTextField(groupSeparator != null ? groupSeparator : "");
        FixedSizeDocument dox=new FixedSizeDocument(1);
        fieldSeparator.setDocument(dox);
        //fieldSeparator.setColumns(1);
        fieldSeparator.setFormatterFactory(null);
        fieldSeparator.setText(groupSeparator!=null ? groupSeparator: "");
        fieldSeparator.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        formatStringEditorPanel.check();
                    }
                    
                    
         });        
         setPanelEnable();
        //lbSize=new javax.swing.JLabel("Gropu size:");
        
        //spinnerSize=new javax.swing.JSpinner(); 
        spinnerSize.setValue(groupSize);  
        spinnerSize.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent ce) {
                formatStringEditorPanel.check();
            }
        });
    }
    
    public class FixedSizeDocument extends PlainDocument{
       private int max_;

       public FixedSizeDocument(int max){  
           max_=max;
       }

       @Override
       public void insertString(int offs, String str, AttributeSet a)throws BadLocationException {
          if (getLength()+str.length()>max_) {
             str = str.substring(0, max_ - getLength());
          }
          super.insertString(offs, str, a);
       }
    }
    
    private void setPanelEnable() {
        lbSeparator.setEnabled(cbGroupSeparator.isSelected());
        fieldSeparator.setEnabled(cbGroupSeparator.isSelected());
        lbSize.setEnabled(cbGroupSeparator.isSelected());
        spinnerSize.setEnabled(cbGroupSeparator.isSelected());
    }
    
    public boolean isSetGroupSeparator(){
        return cbGroupSeparator.isSelected();
    }    
    
    public String getGroupSeparator(){
        return cbGroupSeparator.isSelected()?fieldSeparator.getText():null;
    }
    
    public int getGroupSize(){
        return (Integer)spinnerSize.getValue();
    }
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbSeparator = new javax.swing.JLabel();
        fieldSeparator = new javax.swing.JFormattedTextField();
        lbSize = new javax.swing.JLabel();
        spinnerSize = new javax.swing.JSpinner();

        lbSeparator.setText(org.openide.util.NbBundle.getMessage(FormatGroupSeparatorPanel.class, "FormatGroupSeparatorPanel.lbSeparator.text")); // NOI18N

        fieldSeparator.setText(org.openide.util.NbBundle.getMessage(FormatGroupSeparatorPanel.class, "FormatGroupSeparatorPanel.fieldSeparator.text")); // NOI18N
        fieldSeparator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSeparatorActionPerformed(evt);
            }
        });

        lbSize.setText(org.openide.util.NbBundle.getMessage(FormatGroupSeparatorPanel.class, "FormatGroupSeparatorPanel.lbSize.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbSeparator)
                .addGap(2, 2, 2)
                .addComponent(fieldSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbSize)
                .addGap(3, 3, 3)
                .addComponent(spinnerSize, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbSeparator)
                    .addComponent(lbSize)
                    .addComponent(spinnerSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void fieldSeparatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSeparatorActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_fieldSeparatorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField fieldSeparator;
    private javax.swing.JLabel lbSeparator;
    private javax.swing.JLabel lbSize;
    private javax.swing.JSpinner spinnerSize;
    // End of variables declaration//GEN-END:variables
}
