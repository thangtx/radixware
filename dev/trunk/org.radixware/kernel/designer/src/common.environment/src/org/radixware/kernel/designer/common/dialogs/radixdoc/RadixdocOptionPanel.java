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

package org.radixware.kernel.designer.common.dialogs.radixdoc;

import java.awt.Desktop;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.openide.util.NbBundle;


@NbBundle.Messages({
    "RadixdocOptionPanel-DialogTitle=Choose destination dir"
})
final class RadixdocOptionPanel extends javax.swing.JPanel {

    private File outDir;
    private final RadixdocOptions options;

    public RadixdocOptionPanel() {
        initComponents();
        
        options = RadixdocOptions.Factory.getInstance();
        
        txtOutput.setText(options.getOutputLocation());
        chbBrowser.setSelected(options.isOpenInBrowser());
        chbDependencies.setSelected(options.isIncludeDependencies());
        chbZip.setSelected(options.isCompressToZip());
    }

    public boolean compressToZip() {
        return chbZip.isSelected();
    }

    public boolean includeDependencies() {
        return chbDependencies.isSelected();
    }

    public boolean openBrowser() {
        return chbBrowser.isSelected();
    }

    public File getOutputFile() {
        if (outDir == null) {
            outDir = new File(txtOutput.getText());
        }
        if (compressToZip()) {
            return new File(outDir, "radixdoc.zip");
        }
        return outDir;
    }

    private void chooseDir() {
        JFileChooser chooser = new JFileChooser(txtOutput.getText());
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle(Bundle.RadixdocOptionPanel_DialogTitle());

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            outDir = chooser.getSelectedFile();
            txtOutput.setText(outDir.getPath());
        }
    }

    public void browse(String index) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop() != null
                && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                if (compressToZip()) {
                    Desktop.getDesktop().open(getOutputFile());
                } else {
                    Desktop.getDesktop().open(new File(getOutputFile(), index));
                }
            } catch (Exception e) {
                Logger.getLogger(RadixdocGenerator.class.getName()).log(Level.WARNING, null, e);
            }
        }
    }

    void saveOptions() {
        options.setCompressToZip(chbZip.isSelected());
        options.setIncludeDependencies(chbDependencies.isSelected());
        options.setOpenInBrowser(chbBrowser.isSelected());
        options.setOutputLocation(txtOutput.getText());
        options.save();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        txtOutput = new javax.swing.JTextField();
        btnChooseFile = new javax.swing.JButton();
        chbDependencies = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        chbZip = new javax.swing.JCheckBox();
        chbBrowser = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setMinimumSize(new java.awt.Dimension(300, 150));
        setName(""); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        txtOutput.setText(org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.txtOutput.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(txtOutput, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(btnChooseFile, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.btnChooseFile.text")); // NOI18N
        btnChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        add(btnChooseFile, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbDependencies, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbDependencies.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        add(chbDependencies, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        add(jLabel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbZip, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbZip.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        add(chbZip, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbBrowser, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbBrowser.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 1.0;
        add(chbBrowser, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseFileActionPerformed
        chooseDir();
    }//GEN-LAST:event_btnChooseFileActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseFile;
    private javax.swing.JCheckBox chbBrowser;
    private javax.swing.JCheckBox chbDependencies;
    private javax.swing.JCheckBox chbZip;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtOutput;
    // End of variables declaration//GEN-END:variables
}
