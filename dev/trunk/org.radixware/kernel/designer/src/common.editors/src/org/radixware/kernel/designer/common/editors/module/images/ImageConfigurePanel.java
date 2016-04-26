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
 * ImageConfigurePanel.java
 *
 * Created on Dec 27, 2008, 8:36:23 PM
 */
package org.radixware.kernel.designer.common.editors.module.images;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


class ImageConfigurePanel extends javax.swing.JPanel {

    private final AdsImageDef adsImage;

    /** Creates new form ImageConfigurePanel */
    public ImageConfigurePanel(AdsImageDef adsImage) {
        this.adsImage = adsImage;
        initComponents();
        imagePanel.setLayout(new BorderLayout());
        imageLabel = new JLabel();
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        Icon icon = adsImage.getIcon().getIcon(32, 32);
        if (icon != null) {
            imageLabel.setIcon(icon);
            originalSizeCheckBox.setEnabled(true);
        } else {
            imageLabel.setText(adsImage.getName());
            originalSizeCheckBox.setEnabled(false);
        }
        imageNameTextField.setText(adsImage.getName());
        StringBuilder b = new StringBuilder();
        for (String s : adsImage.getKeywords()) {
            b.append(s);
            b.append(" ");
        }
        if (b.length() > 0) {
            b.deleteCharAt(b.length() - 1);
        }
        imageKeywordsTextField.setText(b.toString());
        imageDescriptionTextArea.setText(adsImage.getDescription());
        updateEnableState();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                stateManager.ok();
//                changeSupport.fireChange();
            }
        });
    }

    private void updateEnableState() {
        boolean enabled = !adsImage.isReadOnly();
        imageNameTextField.setEditable(enabled);
        imageKeywordsTextField.setEditable(enabled);
        imageDescriptionTextArea.setEditable(enabled);
    }

    public void apply() {
        if (adsImage.isReadOnly()) {
            return;
        }
        adsImage.setName(imageNameTextField.getText().trim());
        adsImage.setKeywords(Arrays.asList(imageKeywordsTextField.getText().split("[ ,.;\t\n]")));
        adsImage.setDescription(imageDescriptionTextArea.getText());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePanel = new javax.swing.JPanel();
        imageNameTextField = new javax.swing.JTextField();
        imageKeywordsTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        imageDescriptionTextArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        originalSizeCheckBox = new javax.swing.JCheckBox();

        imagePanel.setBackground(java.awt.SystemColor.text);
        imagePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        imagePanel.setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );

        imageNameTextField.setText(org.openide.util.NbBundle.getMessage(ImageConfigurePanel.class, "ImageConfigurePanel.imageNameTextField.text")); // NOI18N
        imageNameTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                imageNameTextFieldCaretUpdate(evt);
            }
        });

        imageKeywordsTextField.setText(org.openide.util.NbBundle.getMessage(ImageConfigurePanel.class, "ImageConfigurePanel.imageKeywordsTextField.text")); // NOI18N

        jLabel1.setLabelFor(imageNameTextField);
        jLabel1.setText(org.openide.util.NbBundle.getMessage(ImageConfigurePanel.class, "ImageConfigurePanel.jLabel1.text")); // NOI18N

        jLabel2.setLabelFor(imageKeywordsTextField);
        jLabel2.setText(org.openide.util.NbBundle.getMessage(ImageConfigurePanel.class, "ImageConfigurePanel.jLabel2.text")); // NOI18N

        imageDescriptionTextArea.setColumns(20);
        imageDescriptionTextArea.setRows(2);
        jScrollPane1.setViewportView(imageDescriptionTextArea);

        jLabel3.setLabelFor(imageDescriptionTextArea);
        jLabel3.setText(org.openide.util.NbBundle.getMessage(ImageConfigurePanel.class, "ImageConfigurePanel.jLabel3.text")); // NOI18N

        originalSizeCheckBox.setText(org.openide.util.NbBundle.getMessage(ImageConfigurePanel.class, "ImageConfigurePanel.originalSizeCheckBox.text")); // NOI18N
        originalSizeCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                originalSizeCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(originalSizeCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imageKeywordsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(imageNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(imagePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(imageNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(imageKeywordsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(originalSizeCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void imageNameTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_imageNameTextFieldCaretUpdate
        if (imageNameTextField.isEditable()) {
            changeSupport.fireChange();
        }
    }//GEN-LAST:event_imageNameTextFieldCaretUpdate

    private void originalSizeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_originalSizeCheckBoxItemStateChanged
        if (originalSizeCheckBox.isSelected()) {
            Icon icon = null;
            Image img = adsImage.getIcon().getOriginalImage();
            if (img != null) {
                icon = new ImageIcon(img);
            }
            if (icon != null) {
                imageLabel.setIcon(icon);
            }
        } else {
            Icon icon = adsImage.getIcon().getIcon(32, 32);
            if (icon != null) {
                imageLabel.setIcon(icon);
            }
        }
    }//GEN-LAST:event_originalSizeCheckBoxItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea imageDescriptionTextArea;
    private javax.swing.JTextField imageKeywordsTextField;
    private javax.swing.JTextField imageNameTextField;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox originalSizeCheckBox;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JLabel imageLabel;
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        if (imageNameTextField.getText().isEmpty()) {
            stateManager.error(NbBundle.getMessage(ImageConfigurePanel.class, "Incorrect_name"));
            return false;
        } else {
            stateManager.ok();
            return true;
        }
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
}
