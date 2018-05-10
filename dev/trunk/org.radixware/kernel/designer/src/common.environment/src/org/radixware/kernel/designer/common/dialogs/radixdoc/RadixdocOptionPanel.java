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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.dialog.ChooseLanguagesPanel;

@NbBundle.Messages({
    "RadixdocOptionPanel-DialogTitle=Choose destination dir"
})
final class RadixdocOptionPanel extends javax.swing.JPanel {

    private File outDir;
    private final RadixdocOptions options;
    private final List<EIsoLanguage> availableLanguages;
    private final List<EIsoLanguage> selectedLanguages;

    public RadixdocOptionPanel(List<EIsoLanguage> availableLanguages) {
        options = RadixdocOptions.Factory.getInstance();

        this.availableLanguages = new ArrayList<>(availableLanguages);
        this.selectedLanguages = options.getSelectedLanguages();
        Collections.sort(availableLanguages);

        initComponents();
        txtOutput.setText(options.getOutputLocation());
        chbBrowser.setSelected(options.isOpenInBrowser());
        chbDependencies.setSelected(options.isIncludeDependencies());
        chbZip.setSelected(options.isCompressToZip());
        chbHTML.setSelected(options.isExportHTMLDoc());
        chbTechDocHtml.setSelected(options.isExportTechDocHtml());
        chbTechDocPdf.setSelected(options.isExportTechDocPdf());
        chbTechDocDoc.setSelected(options.isExportTechDocDoc());

        update();
    }

    public boolean compressToZip() {
        return chbZip.isSelected();
    }

    public boolean includeDependencies() {
        return chbDependencies.isSelected();
    }

    public boolean openDocumentation() {
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

    public List<EIsoLanguage> getExportLanguages() {
        return ((ChooseLanguagesPanel) languagesPanel).getSelection();
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

    public void open(String index) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop() != null
                && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
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

    boolean needsExportTechDocHtml() {
        return chbTechDocHtml.isSelected();
    }

    boolean needsExportTechDocPdf() {
        return chbTechDocPdf.isSelected();
    }

    boolean needsExportTechDocDoc() {
        return chbTechDocDoc.isSelected();
    }

    boolean needsExportHTMLDoc() {
        return chbHTML.isSelected();
    }

    boolean needsExportDoc() {
        return needsExportTechDocHtml() || needsExportTechDocPdf() || needsExportTechDocDoc() || needsExportHTMLDoc();
    }

    void saveOptions() {
        options.setCompressToZip(chbZip.isSelected());
        options.setIncludeDependencies(chbDependencies.isSelected());
        options.setOpenInBrowser(chbBrowser.isSelected());
        options.setOutputLocation(txtOutput.getText());
        options.setExportHTMLDoc(chbHTML.isSelected());
        options.setExportTechDocHtml(chbTechDocHtml.isSelected());
        options.setExportTechDocPdf(chbTechDocPdf.isSelected());
        options.setExportTechDocDoc(chbTechDocDoc.isSelected());
        options.setSelectedLanguages(((ChooseLanguagesPanel) languagesPanel).getSelection());
        options.save();
    }

    private void update() {
        boolean enabled = chbHTML.isSelected() || chbTechDocHtml.isSelected()
                || chbTechDocPdf.isSelected() || chbTechDocDoc.isSelected();
        btnChooseFile.setEnabled(enabled);
        chbBrowser.setEnabled(enabled);
        chbZip.setEnabled(enabled);
        jLabel1.setEnabled(enabled);
        txtOutput.setEnabled(enabled);
        languagesPanel.setEnabled(enabled);
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
        chbHTML = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        chbZip = new javax.swing.JCheckBox();
        chbBrowser = new javax.swing.JCheckBox();
        chbDependencies = new javax.swing.JCheckBox();
        languagesPanel = new ChooseLanguagesPanel(this.availableLanguages, this.selectedLanguages, "Export Languages");
        chbTechDocHtml = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        chbTechDocPdf = new javax.swing.JCheckBox();
        chbTechDocDoc = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setMinimumSize(new java.awt.Dimension(300, 150));
        setName(""); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        txtOutput.setText(org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.txtOutput.text")); // NOI18N
        txtOutput.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 51;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, -60, 0, 0);
        add(txtOutput, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(btnChooseFile, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.btnChooseFile.text")); // NOI18N
        btnChooseFile.setEnabled(false);
        btnChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 51;
        add(btnChooseFile, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbHTML, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbHTML.text")); // NOI18N
        chbHTML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbHTMLActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(chbHTML, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.jLabel1.text")); // NOI18N
        jLabel1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        add(jLabel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbZip, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbZip.text")); // NOI18N
        chbZip.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(chbZip, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbBrowser, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbBrowser.text")); // NOI18N
        chbBrowser.setEnabled(false);
        chbBrowser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbBrowserActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 90;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(chbBrowser, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbDependencies, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbDependencies.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(chbDependencies, gridBagConstraints);

        languagesPanel.setEnabled(false);
        languagesPanel.setMinimumSize(new java.awt.Dimension(150, 150));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        add(languagesPanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbTechDocHtml, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbTechDocHtml.text")); // NOI18N
        chbTechDocHtml.setActionCommand(org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbTechDocHtml.actionCommand")); // NOI18N
        chbTechDocHtml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbTechDocHtmlActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 41;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(chbTechDocHtml, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabel2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(jLabel3, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jLabel4, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jLabel5, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jLabel6, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbTechDocPdf, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbTechDocPdf.text")); // NOI18N
        chbTechDocPdf.setActionCommand(org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbTechDocPdf.actionCommand")); // NOI18N
        chbTechDocPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbTechDocPdfActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 42;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(chbTechDocPdf, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(chbTechDocDoc, org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbTechDocDoc.text")); // NOI18N
        chbTechDocDoc.setActionCommand(org.openide.util.NbBundle.getMessage(RadixdocOptionPanel.class, "RadixdocOptionPanel.chbTechDocDoc.actionCommand")); // NOI18N
        chbTechDocDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbTechDocDocActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 43;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(chbTechDocDoc, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseFileActionPerformed
        chooseDir();
    }//GEN-LAST:event_btnChooseFileActionPerformed

    private void chbHTMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbHTMLActionPerformed
        update();
    }//GEN-LAST:event_chbHTMLActionPerformed

    private void chbTechDocHtmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbTechDocHtmlActionPerformed
        update();
    }//GEN-LAST:event_chbTechDocHtmlActionPerformed

    private void chbBrowserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbBrowserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chbBrowserActionPerformed

    private void chbTechDocPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbTechDocPdfActionPerformed
        update();
    }//GEN-LAST:event_chbTechDocPdfActionPerformed

    private void chbTechDocDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbTechDocDocActionPerformed
        update();
    }//GEN-LAST:event_chbTechDocDocActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseFile;
    private javax.swing.JCheckBox chbBrowser;
    private javax.swing.JCheckBox chbDependencies;
    private javax.swing.JCheckBox chbHTML;
    private javax.swing.JCheckBox chbTechDocDoc;
    private javax.swing.JCheckBox chbTechDocHtml;
    private javax.swing.JCheckBox chbTechDocPdf;
    private javax.swing.JCheckBox chbZip;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel languagesPanel;
    private javax.swing.JTextField txtOutput;
    // End of variables declaration//GEN-END:variables
}
