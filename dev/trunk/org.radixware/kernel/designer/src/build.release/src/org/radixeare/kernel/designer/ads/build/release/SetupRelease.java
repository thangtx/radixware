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

package org.radixeare.kernel.designer.ads.build.release;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.ChangeSupport;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.builder.release.ReleaseSettings;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class SetupRelease extends javax.swing.JPanel {

    private ReleaseSettings settings;

    /**
     * Creates new form SetupRelease
     */
    public SetupRelease(final ReleaseSettings settings) {
        initComponents();

        if (!settings.isPatchRelease()) {
            int hegiht = chVerify.getY() + 3 * chVerify.getHeight();
            int diff = getHeight() - hegiht;
            Dimension parentSize = this.getSize();
            parentSize.height -= diff;
            parentSize.width = 0;
            this.setSize(parentSize);
            this.setMaximumSize(parentSize);
//            this.setMinimumSize(this.getMaximumSize());
        }
        this.settings = settings;
        this.edReleaseVersion.setText(settings.getNumber());
        this.edReleaseVersion.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SetupRelease.this.settings.setNumber(edReleaseVersion.getText());
                SetupRelease.this.changeSupport.fireChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SetupRelease.this.settings.setNumber(edReleaseVersion.getText());
                SetupRelease.this.changeSupport.fireChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SetupRelease.this.settings.setNumber(edReleaseVersion.getText());
                SetupRelease.this.changeSupport.fireChange();
            }
        });


        this.chCompileADS.setSelected(settings.performCleanAndBuild());
        this.chCompileADS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                settings.setPerformCleanAndBuild(chCompileADS.isSelected());
            }
        });
        this.chVerify.setSelected(settings.verifyRelease());
        this.chVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                settings.setVerifyRelease(chVerify.isSelected());
            }
        });

        chbRadixdoc.setSelected(settings.isGenerateRadixdoc());
        chbRadixdoc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setGenerateRadixdoc(chbRadixdoc.isSelected());
            }
        });
        
        updateState();


    }
    
    private boolean prepared;

    public void initialize(final ModalDisplayer displayer) {
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!isShowing()) {
                        try {
                            Thread.sleep(100);
                            continue;
                        } catch (InterruptedException ex) {
                        }
                    }
                    synchronized (SetupRelease.this) {
                        if (!prepared) {
                            prepared = true;
                        } else {
                            return;
                        }
                    }

                    if (!prepare()) {
                        displayer.close(false);
                    }
                    return;//NOPMD
                }
            }
        });
    }

    private boolean prepare() {
        changeSupport.fireChange();
        final boolean[] result = new boolean[]{true};
        return result[0];
    }
    
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }
    private final StateManager stateManager = new StateManager(this);

    public boolean isReady() {
        if (ReleaseSettings.isValidReleaseName(edReleaseVersion.getText(), settings.isPatchRelease())) {
            stateManager.ok();
            return true;
        } else {
            stateManager.error("Incorrect release number");
            return false;
        }
    }

    private void updateState() {

        edReleaseVersion.setEnabled(true);
        changeSupport.fireChange();
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

        jLabel1 = new javax.swing.JLabel();
        edReleaseVersion = new javax.swing.JTextField();
        stateDisplayer = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        chCompileADS = new javax.swing.JCheckBox();
        chVerify = new javax.swing.JCheckBox();
        chbRadixdoc = new javax.swing.JCheckBox();

        setMinimumSize(new java.awt.Dimension(400, 150));
        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(SetupRelease.class, "SetupRelease.jLabel1.text_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 12, 0, 0);
        add(jLabel1, gridBagConstraints);

        edReleaseVersion.setText(org.openide.util.NbBundle.getMessage(SetupRelease.class, "SetupRelease.edReleaseVersion.text_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 12);
        add(edReleaseVersion, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 12);
        add(stateDisplayer, gridBagConstraints);

        chCompileADS.setText(org.openide.util.NbBundle.getMessage(SetupRelease.class, "SetupRelease.chCompileADS.text_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 0, 12);
        add(chCompileADS, gridBagConstraints);

        chVerify.setText(org.openide.util.NbBundle.getMessage(SetupRelease.class, "SetupRelease.chVerify.text_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        add(chVerify, gridBagConstraints);

        chbRadixdoc.setActionCommand(org.openide.util.NbBundle.getMessage(SetupRelease.class, "SetupRelease.chbRadixdoc.actionCommand_1")); // NOI18N
        chbRadixdoc.setLabel(org.openide.util.NbBundle.getMessage(SetupRelease.class, "SetupRelease.chbRadixdoc.label_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        add(chbRadixdoc, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chCompileADS;
    private javax.swing.JCheckBox chVerify;
    private javax.swing.JCheckBox chbRadixdoc;
    private javax.swing.JTextField edReleaseVersion;
    private javax.swing.JLabel jLabel1;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer;
    // End of variables declaration//GEN-END:variables
}
