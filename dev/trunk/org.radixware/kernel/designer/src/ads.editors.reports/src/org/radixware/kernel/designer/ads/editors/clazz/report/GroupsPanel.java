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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;

class GroupsPanel extends javax.swing.JPanel {

    private final AdsReportForm form;
    private volatile boolean updating = false;
    private final JPanel bandsPanel = new JPanel();
    private boolean isMultiFile = false;
    private JPanel top;
    private final Stack<AdsReportGroupEditor> editors = new Stack<AdsReportGroupEditor>();

    /**
     * Creates new form GroupsPanel
     */
    public GroupsPanel(final AdsReportForm form) {
        this.form = form;
        initComponents();

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        top = bandsPanel;

        scrollPane.setViewportView(bandsPanel);
        levelSpinner.setEditor(new CheckedNumberSpinnerEditor(levelSpinner));
        edMultiFileLevel.setEditor(new CheckedNumberSpinnerEditor(edMultiFileLevel));
        SpinnerNumberModel model = (SpinnerNumberModel) edMultiFileLevel.getModel();
        model.setMinimum(1);

        chMultiFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updating) {
                    return;
                }
                if (chMultiFile.isSelected()) {
                    if (!form.getGroups().isEmpty()) {
                        form.setMultifileGroupLevel(0);
                    }
                } else {
                    form.setMultifileGroupLevel(-1);
                }
                edMultiFileLevel.setValue(form.getMultifileGroupLevel() + 1);
                updateEnableState();
            }
        });
        edMultiFileLevel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (updating) {
                    return;
                }
                Object value = edMultiFileLevel.getValue();
                if (value instanceof Integer) {
                    int level = ((Integer) value).intValue();
                    if (level >= 0 && level < form.getGroups().size()) {
                        form.setMultifileGroupLevel(level - 1);
                    }
                }
            }
        });
        chRepeatHeaders.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (chRepeatHeaders.isSelected()) {
                    form.setRepeatGroupHeadersOnNewPage(true);
                } else {
                    form.setRepeatGroupHeadersOnNewPage(false);
                }
            }
        });
        setupInitialValues();
    }

    private void setupInitialValues() {
        updating = true;
        levelSpinner.setValue(Long.valueOf(form.getGroups().size()));
        showCheckBox.setSelected(form.isColumnsHeaderForEachGroupDisplayed());
        chRepeatHeaders.setSelected(form.isRepeatGroupHeadersOnNewPage());
        for (AdsReportGroup group : form.getGroups()) {
            addGroup(group, -1);
        }
        SpinnerNumberModel model = (SpinnerNumberModel) edMultiFileLevel.getModel();
        model.setMaximum(form.getGroups().size());
        if (form.getMultifileGroupLevel() >= 0 && !form.getGroups().isEmpty()) {
            edMultiFileLevel.setValue(form.getMultifileGroupLevel() + 1);
        } else {
            edMultiFileLevel.setValue(0);
        }
        chMultiFile.setSelected(form.getMultifileGroupLevel() >= 0);
        updateEnableState();
        updating = false;
    }

    private void updateEnableState() {
        final boolean enabled = !form.isReadOnly();
        levelSpinner.setEnabled(enabled);
        showCheckBox.setEnabled(enabled);
        scrollPane.setVisible(form.getGroups().size() > 0);
        chMultiFile.setEnabled(enabled && !form.getGroups().isEmpty());
        chRepeatHeaders.setEnabled(enabled && !form.getGroups().isEmpty());
        edMultiFileLevel.setEnabled(chMultiFile.isEnabled() && chMultiFile.isSelected());
        this.revalidate();
        this.repaint();
    }

    private void addGroup(final AdsReportGroup group, final int sz) {
        if (sz != -1 && group.getName().isEmpty()) {
            group.setName("Group " + sz);
        }
        final AdsReportGroupEditor editor = new AdsReportGroupEditor(group);
        top.setLayout(new BorderLayout());
        top.add(editor, BorderLayout.NORTH);
        editor.setVisible(true);
        final JPanel tmp = new JPanel();
        top.add(tmp, BorderLayout.CENTER);
        top = tmp;
        scrollPane.addComponentListener(editor);
        editors.push(editor);
        bandsPanel.revalidate();
        bandsPanel.repaint();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                editor.setPreferredSize(new Dimension(scrollPane.getWidth() - 30, editor.getHeight()));
            }
        });
    }

    private void removeEditor(final AdsReportGroupEditor editor) {
        scrollPane.removeComponentListener(editor);
        final JPanel par = (JPanel) editor.getParent();
        editor.setVisible(false);
        par.getLayout().removeLayoutComponent(editor);
        top.setVisible(false);
        par.getLayout().removeLayoutComponent(top);
        top = par;
        bandsPanel.revalidate();
        bandsPanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        levelSpinner = new javax.swing.JSpinner();
        showCheckBox = new javax.swing.JCheckBox();
        chMultiFile = new javax.swing.JCheckBox();
        edMultiFileLevel = new javax.swing.JSpinner();
        chRepeatHeaders = new javax.swing.JCheckBox();
        scrollPane = new javax.swing.JScrollPane();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(GroupsPanel.class, "GroupsPanel.jLabel1.text")); // NOI18N

        levelSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(20L), Long.valueOf(1L)));
        levelSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                levelSpinnerStateChanged(evt);
            }
        });

        showCheckBox.setText(org.openide.util.NbBundle.getMessage(GroupsPanel.class, "GroupsPanel.showCheckBox.text")); // NOI18N
        showCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showCheckBoxActionPerformed(evt);
            }
        });

        chMultiFile.setText(org.openide.util.NbBundle.getMessage(GroupsPanel.class, "GroupsPanel.chMultiFile.text")); // NOI18N

        chRepeatHeaders.setText(org.openide.util.NbBundle.getMessage(GroupsPanel.class, "GroupsPanel.chRepeatHeaders.text")); // NOI18N

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
                        .addComponent(levelSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showCheckBox)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chMultiFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edMultiFileLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chRepeatHeaders))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(levelSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chMultiFile)
                    .addComponent(edMultiFileLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chRepeatHeaders)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        scrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GroupsPanel.class, "GroupsPanel.scrollPane.border.title"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void levelSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_levelSpinnerStateChanged
        if (updating) {
            return;
        }
        while (((Long) levelSpinner.getValue()).intValue() < editors.size()) {
            form.getGroups().remove(editors.peek().getGroup());
            removeEditor(editors.pop());
        }
        while (((Long) levelSpinner.getValue()).intValue() > editors.size()) {
            final AdsReportGroup group = form.getGroups().addNew();
            addGroup(group, group.getIndex() + 1);
        }
        SpinnerNumberModel model = (SpinnerNumberModel) edMultiFileLevel.getModel();
        model.setMaximum(form.getGroups().size());
        updateEnableState();
    }//GEN-LAST:event_levelSpinnerStateChanged

    private void showCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCheckBoxActionPerformed
        form.setColumnsHeaderForEachGroupDisplayed(showCheckBox.isSelected());
    }//GEN-LAST:event_showCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chMultiFile;
    private javax.swing.JCheckBox chRepeatHeaders;
    private javax.swing.JSpinner edMultiFileLevel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner levelSpinner;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JCheckBox showCheckBox;
    // End of variables declaration//GEN-END:variables

}
