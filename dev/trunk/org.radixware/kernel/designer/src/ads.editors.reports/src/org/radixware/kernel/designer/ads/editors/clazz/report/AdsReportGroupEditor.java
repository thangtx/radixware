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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.common.resources.RadixWareIcons;


class AdsReportGroupEditor extends JPanel implements ComponentListener {

    private volatile boolean updating = false;
    private final AdsReportGroup group;

    /** Creates new form AdsReportGroupEditor */
    public AdsReportGroupEditor(final AdsReportGroup adsReportGroup) {
        super();
        this.group = adsReportGroup;
        initComponents();

        final int number = adsReportGroup.getIndex() + 1;
        ((TitledBorder)jPanel1.getBorder()).setTitle("Group " + number);
//        headerCheckBox.setText(headerCheckBox.getText() + " " + number);
//        footerCheckBox.setText(footerCheckBox.getText() + " " + number);
//        if (number > 1) {
//            this.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
//        }

        setupInitialValues();
    }

    public AdsReportGroup getGroup() {
        return group;
    }

    private void setupInitialValues() {
        updating = true;
        nameTextField.setText(group.getName());
        headerCheckBox.setSelected(group.isHeaderBandUsed());
        footerCheckBox.setSelected(group.isFooterBandUsed());
        updateEnableState();
        updating = false;
    }

    private void updateEnableState() {
        final boolean enabled = !group.isReadOnly();
        nameTextField.setEnabled(enabled);
        headerCheckBox.setEnabled(enabled);
        footerCheckBox.setEnabled(enabled);
        headerButton.setEnabled(group.isHeaderBandUsed());
        footerButton.setEnabled(group.isFooterBandUsed());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        headerCheckBox = new javax.swing.JCheckBox();
        footerCheckBox = new javax.swing.JCheckBox();
        headerButton = new javax.swing.JButton();
        footerButton = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportGroupEditor.class, "AdsReportGroupEditor.jPanel1.border.title"))); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsReportGroupEditor.class, "AdsReportGroupEditor.jLabel1.text")); // NOI18N

        nameTextField.setText(org.openide.util.NbBundle.getMessage(AdsReportGroupEditor.class, "AdsReportGroupEditor.nameTextField.text")); // NOI18N
        nameTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                nameTextFieldCaretUpdate(evt);
            }
        });

        headerCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportGroupEditor.class, "AdsReportGroupEditor.headerCheckBox.text")); // NOI18N
        headerCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerCheckBoxActionPerformed(evt);
            }
        });

        footerCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportGroupEditor.class, "AdsReportGroupEditor.footerCheckBox.text")); // NOI18N
        footerCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                footerCheckBoxActionPerformed(evt);
            }
        });

        headerButton.setIcon(RadixWareIcons.EDIT.EDIT.getIcon());
        headerButton.setText(org.openide.util.NbBundle.getMessage(AdsReportGroupEditor.class, "AdsReportGroupEditor.headerButton.text")); // NOI18N
        headerButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        headerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerButtonActionPerformed(evt);
            }
        });

        footerButton.setIcon(RadixWareIcons.EDIT.EDIT.getIcon());
        footerButton.setText(org.openide.util.NbBundle.getMessage(AdsReportGroupEditor.class, "AdsReportGroupEditor.footerButton.text")); // NOI18N
        footerButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        footerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                footerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(headerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(headerCheckBox))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(footerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(footerCheckBox)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerButton)
                    .addComponent(headerCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(footerButton)
                    .addComponent(footerCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void headerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerButtonActionPerformed
        if (group.getHeaderBand() != null) {
            EditorsManager.getDefault().open(group.getHeaderBand());
        }
    }//GEN-LAST:event_headerButtonActionPerformed

    private void footerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_footerButtonActionPerformed
        if (group.getFooterBand() != null) {
            EditorsManager.getDefault().open(group.getFooterBand());
        }
    }//GEN-LAST:event_footerButtonActionPerformed

    private void headerCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerCheckBoxActionPerformed
        group.setHeaderBandUsed(headerCheckBox.isSelected());
        updateEnableState();
    }//GEN-LAST:event_headerCheckBoxActionPerformed

    private void footerCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_footerCheckBoxActionPerformed
        group.setFooterBandUsed(footerCheckBox.isSelected());
        updateEnableState();
    }//GEN-LAST:event_footerCheckBoxActionPerformed

    private void nameTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_nameTextFieldCaretUpdate
        if (!updating) {
            group.setName(nameTextField.getText());
        }
    }//GEN-LAST:event_nameTextFieldCaretUpdate

    @Override
    public void componentResized(final ComponentEvent e) {
        final JComponent comp = (JComponent) e.getSource();
        this.setPreferredSize(new Dimension(comp.getWidth() - 30, this.getHeight()));
    }

    @Override
    public void componentMoved(final ComponentEvent e) {
    }

    @Override
    public void componentShown(final ComponentEvent e) {
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton footerButton;
    private javax.swing.JCheckBox footerCheckBox;
    private javax.swing.JButton headerButton;
    private javax.swing.JCheckBox headerCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nameTextField;
    // End of variables declaration//GEN-END:variables
}
