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

package org.radixware.kernel.reporteditor.editors;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.userreport.repository.UserReport;


public class DescriptionPanel extends javax.swing.JPanel {

    /**
     * Creates new form DescriptionPanel
     */
    private final UserReport report;
    private boolean updating = false;

    public DescriptionPanel(final UserReport report) {
        initComponents();
        this.report = report;
        edDescription.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(final DocumentEvent e) {
                saveDescription();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                saveDescription();

            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                saveDescription();
            }
        });
    }

    private void saveDescription() {
        if (!updating) {
            report.setDescription(edDescription.getText());
        }
    }

    public void update() {
        try {
            updating = true;
            final String description = report.getDescription();

            if (description == null) {
                edDescription.setText("");               
            } else {
                edDescription.setText(description);
            }
            edDescription.setEditable(!report.isReadOnly());
        } finally {
            updating = false;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        edDescription = new javax.swing.JTextArea();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DescriptionPanel.class, "DescriptionPanel.border.title"))); // NOI18N

        edDescription.setColumns(20);
        edDescription.setRows(5);
        edDescription.setBorder(null);
        jScrollPane1.setViewportView(edDescription);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea edDescription;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
