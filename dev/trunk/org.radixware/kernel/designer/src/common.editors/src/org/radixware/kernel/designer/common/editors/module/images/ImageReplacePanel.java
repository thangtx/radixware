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
 * ImageReplacePanel.java
 *
 * Created on Nov 6, 2009, 11:32:20 AM
 */
package org.radixware.kernel.designer.common.editors.module.images;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ImageReplacePanel extends javax.swing.JPanel {

    private final AdsImageDef adsImage;
    private volatile boolean imageLoaded = false;
//    private volatile boolean initialImage = true;
    private RadixIcon radixIcon = null;
    private final JButton fileButton;
//    private static String path = null;

    /** Creates new form ImageReplacePanel */
    public ImageReplacePanel(AdsImageDef adsImage) {
        this.adsImage = adsImage;
        initComponents();

        extTextField.setEditable(false);
        fileButton = extTextField.addButton();
        fileButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        fileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });
        fileButton.setToolTipText(NbBundle.getBundle(ImageImportPanel.class).getString("Choose_File"));

        imagePanel.setLayout(new BorderLayout());
        imageLabel = new JLabel();
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        radixIcon = adsImage.getIcon();
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
        imageNameTextField.setEditable(false);
        imageKeywordsTextField.setEditable(false);
        imageDescriptionTextArea.setEditable(false);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                stateManager.ok();
                chooseFile();
//                changeSupport.fireChange();
            }
        });
    }

//    private void updateEnableState() {
//        boolean enabled = !adsImage.isReadOnly();
////        extTextField.setEnabled(enabled);
//        fileButton.setEnabled(enabled);
//        imageNameTextField.setEditable(enabled);
//        imageKeywordsTextField.setEditable(enabled);
//        imageDescriptionTextArea.setEditable(enabled);
////        imageNameTextField.setEnabled(enabled);
////        imageKeywordsTextField.setEnabled(enabled);
////        imageDescriptionTextArea.setEnabled(enabled);
//    }

    public void apply() {
        if (adsImage.isReadOnly()) {
            return;
        }
        if (imageLoaded) {
            try {
                adsImage.importImage(getFile());
            } catch (IOException e) {
                DialogUtils.messageError(e);
            }
        }
        adsImage.setName(imageNameTextField.getText().trim());
        adsImage.setKeywords(Arrays.asList(imageKeywordsTextField.getText().split("[ ,.;\t\n]")));
        adsImage.setDescription(imageDescriptionTextArea.getText());
    }

    private File getFile() {
        return new File(extTextField.getValue().toString());
    }

    private File getImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(NbBundle.getBundle(ImageImportPanel.class).getString("Choose_Image"));
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String fname = f.getName();
                return f.isDirectory() || fname.endsWith(".svg") || fname.endsWith(".png") || fname.endsWith(".img") ||
                        fname.endsWith(".gif") || fname.endsWith(".jpg");
            }

            @Override
            public String getDescription() {
                return NbBundle.getBundle(ImageImportPanel.class).getString("Image") + " (*.svg, *.png, *.img, *.gif, *.jpg)";
            }
        });
        String path = ModuleImagesEditorOptions.getDefault().getFilePath();
        if (!path.isEmpty()) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                fileChooser.setCurrentDirectory(file);
            }
        }
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            path = file.getParent();
            ModuleImagesEditorOptions.getDefault().setFilePath(path);
            return file;
        }
        return null;
    }

    private void chooseFile() {
        File file = getImageFile();
        if (file == null) {
            return;
        }
        extTextField.setValue(file.getPath());
//        initialImage = false;
//        String name = FileUtils.getFileBaseName(file);
        try {
            radixIcon = new RadixIcon(file);
            Icon icon = radixIcon.getIcon(32, 32);
            if (icon != null) {
                init(icon, true);
            } else {
                init(null, false);
            }
        } catch (RadixError e) {
            init(null, false);
            DialogUtils.messageError(e);
        }
        changeSupport.fireChange();
    }

    private void init(Icon icon, boolean editable) {
        imageLabel.setIcon(icon);
//        imageNameTextField.setText(name);
//        imageKeywordsTextField.setText(name);
//        imageDescriptionTextArea.setText(name);
        imageNameTextField.setEditable(editable);
        imageKeywordsTextField.setEditable(editable);
        imageDescriptionTextArea.setEditable(editable);
        originalSizeCheckBox.setEnabled(editable);
        originalSizeCheckBox.setSelected(false);
        imageLoaded = editable;
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
        jLabel1 = new javax.swing.JLabel();
        extTextField = new org.radixware.kernel.common.components.ExtendableTextField();
        jLabel2 = new javax.swing.JLabel();
        imageNameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        imageKeywordsTextField = new javax.swing.JTextField();
        originalSizeCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        imageDescriptionTextArea = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();

        imagePanel.setBackground(java.awt.SystemColor.text);
        imagePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        imagePanel.setMinimumSize(new java.awt.Dimension(50, 50));
        imagePanel.setPreferredSize(new java.awt.Dimension(95, 95));

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ImageReplacePanel.class, "ImageReplacePanel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(ImageReplacePanel.class, "ImageReplacePanel.jLabel2.text")); // NOI18N

        imageNameTextField.setEditable(false);
        imageNameTextField.setText(org.openide.util.NbBundle.getMessage(ImageReplacePanel.class, "ImageReplacePanel.imageNameTextField.text")); // NOI18N
        imageNameTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                imageNameTextFieldCaretUpdate(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(ImageReplacePanel.class, "ImageReplacePanel.jLabel3.text")); // NOI18N

        imageKeywordsTextField.setEditable(false);
        imageKeywordsTextField.setText(org.openide.util.NbBundle.getMessage(ImageReplacePanel.class, "ImageReplacePanel.imageKeywordsTextField.text")); // NOI18N

        originalSizeCheckBox.setText(org.openide.util.NbBundle.getMessage(ImageReplacePanel.class, "ImageReplacePanel.originalSizeCheckBox.text")); // NOI18N
        originalSizeCheckBox.setEnabled(false);
        originalSizeCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                originalSizeCheckBoxItemStateChanged(evt);
            }
        });

        imageDescriptionTextArea.setColumns(20);
        imageDescriptionTextArea.setEditable(false);
        imageDescriptionTextArea.setRows(2);
        jScrollPane1.setViewportView(imageDescriptionTextArea);

        jLabel5.setText(org.openide.util.NbBundle.getMessage(ImageReplacePanel.class, "ImageReplacePanel.jLabel5.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(extTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                            .addComponent(imageNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                            .addComponent(imageKeywordsTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)))
                    .addComponent(originalSizeCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(imagePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(extTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(imageNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(imageKeywordsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(originalSizeCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void imageNameTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_imageNameTextFieldCaretUpdate
        if (imageNameTextField.isEditable()) {
            changeSupport.fireChange();
        }
}//GEN-LAST:event_imageNameTextFieldCaretUpdate

    private void originalSizeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_originalSizeCheckBoxItemStateChanged
        if (radixIcon == null) {
            return;
        }
        if (originalSizeCheckBox.isSelected()) {
            Icon icon = null;
            Image img = radixIcon.getOriginalImage();
            if (img != null) {
                icon = new ImageIcon(img);
            }
            if (icon != null) {
                imageLabel.setIcon(icon);
            }
        } else {
            Icon icon = radixIcon.getIcon(32, 32);
            if (icon != null) {
                imageLabel.setIcon(icon);
            }
        }
}//GEN-LAST:event_originalSizeCheckBoxItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.components.ExtendableTextField extTextField;
    private javax.swing.JTextArea imageDescriptionTextArea;
    private javax.swing.JTextField imageKeywordsTextField;
    private javax.swing.JTextField imageNameTextField;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox originalSizeCheckBox;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JLabel imageLabel;
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        if (!imageLoaded) {
            this.stateManager.error(NbBundle.getMessage(ImageImportPanel.class, "Unable_to_load_image"));
            return false;
        } else if (imageNameTextField.getText().isEmpty()) {
            this.stateManager.error(NbBundle.getMessage(ImageImportPanel.class, "Incorrect_name"));
            return false;
        } else {
            this.stateManager.ok();
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
