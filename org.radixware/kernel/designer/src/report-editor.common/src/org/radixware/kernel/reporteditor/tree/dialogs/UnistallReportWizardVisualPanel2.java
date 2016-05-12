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

package org.radixware.kernel.reporteditor.tree.dialogs;

import java.awt.CardLayout;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import org.radixware.kernel.common.types.Id;

public final class UnistallReportWizardVisualPanel2 extends JPanel {

    /**
     * Creates new form UnistallReportWizardVisualPanel2
     */
    private final UnistallReportWizardWizardPanel2 panel;

    public UnistallReportWizardVisualPanel2(final UnistallReportWizardWizardPanel2 panel) {
        this.panel = panel;
        initComponents();
        ((CardLayout) jPanel1.getLayout()).show(jPanel1, "label");
    }

    @Override
    public String getName() {
        return "Report publications to execute the selected action on";
    }

    void startAction(final Id reportId, final String action) {
        switch (action) {
            case UnistallReportWizardWizardPanel1.ACTION_DISABLE:
                jLabel1.setText("Please wait while disabling report publications");
                break;
            case UnistallReportWizardWizardPanel1.ACTION_CLEAR:
                jLabel1.setText("Please wait while removing report publications");
                break;
        }
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                execDisableAction(reportId, action);

            }
        });
        t.start();

    }

    private void execDisableAction(final Id reportId, final String command) {
        final List<String> result = UnistallReportWizardWizardAction.execDisableAction(reportId, "preview:" + command);
        final DefaultListModel<String> model = new DefaultListModel<>();
        for (String r : result) {
            model.addElement(r);
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                panel.requestCompleted(!result.isEmpty());
                final TitledBorder border = (TitledBorder) jPanel2.getBorder();
                String message =
                        "Following report publications will be ";
                switch (command) {
                    case "remove":
                        message += "removed";
                        break;
                    case "disable":
                        message += "disabled";
                        break;
                    case "enable":
                        message += "enabled";
                        break;
                }
                border.setTitle(message);
                jList1.setModel(model);
                ((CardLayout) jPanel1.getLayout()).show(jPanel1, "report");
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<String>();

        jPanel1.setLayout(new java.awt.CardLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(UnistallReportWizardVisualPanel2.class, "UnistallReportWizardVisualPanel2.jLabel1.text")); // NOI18N
        jPanel1.add(jLabel1, "label");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(UnistallReportWizardVisualPanel2.class, "UnistallReportWizardVisualPanel2.jPanel2.border.title"))); // NOI18N

        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel1.add(jPanel2, "report");

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
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}