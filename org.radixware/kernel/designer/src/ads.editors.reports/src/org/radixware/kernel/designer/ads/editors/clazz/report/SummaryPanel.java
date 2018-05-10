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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.enums.EReportSummaryCellType;
import org.radixware.kernel.common.utils.Utils;


public class SummaryPanel extends javax.swing.JPanel {
    private volatile boolean updating = false;
    private final AdsReportSummaryCell cell;
    
    
    private class Group {

        public final AdsReportGroup adsGroup;

        public Group(AdsReportGroup group) {
            this.adsGroup = group;
        }

        @Override
        public String toString() {
            return adsGroup.getName();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SummaryPanel.Group) {
                SummaryPanel.Group grp = (SummaryPanel.Group)obj;
                return this.adsGroup.equals(grp.adsGroup);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (this.adsGroup != null ? this.adsGroup.hashCode() : 0);
            return hash;
        }

    }
    /**
     * Creates new form SummaryPanel
     */
    public SummaryPanel(AdsReportSummaryCell cell) {
        this.cell = cell;
        initComponents();
        DefaultComboBoxModel<EReportSummaryCellType> model=new DefaultComboBoxModel<>(EReportSummaryCellType.values());
        summaryTypeComboBox.setModel(model);
        allGroupsCheckBox.setText("All group");
        allGroupsCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!updating){
                    if (allGroupsCheckBox.isSelected()) {
                        SummaryPanel.this.cell.setGroupCount(0);
                    } else {
                        SummaryPanel.Group group = (SummaryPanel.Group)groupComboBox.getSelectedItem();
                        if (group == null) {
                            SummaryPanel.this.cell.setGroupCount(0);
                        } else {
                            SummaryPanel.this.cell.setGroupCount(group.adsGroup.getIndex() + 1);
                        }
                    }
                    updateEnableState();
                }
            }
        });
        summaryTypeComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!updating){
                    EReportSummaryCellType oldSummaryType = SummaryPanel.this.cell.getSummaryType();
                    EReportSummaryCellType newSummaryType = (EReportSummaryCellType)summaryTypeComboBox.getSelectedItem();
                    if (!Utils.equals(oldSummaryType, newSummaryType)) {
                        SummaryPanel.this.cell.setSummaryType(newSummaryType);
                        firePropertyChange(AdsReportWidgetNamePanel.CHANGE_NAME, false, true);
                    }
                }
                     
                
            }
        });
        
        groupComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               if (updating) {
                    return;
                }
                SummaryPanel.Group group = (SummaryPanel.Group)groupComboBox.getSelectedItem();
                if (group == null) {
                    SummaryPanel.this.cell.setGroupCount(0);
                } else {
                    SummaryPanel.this.cell.setGroupCount(group.adsGroup.getIndex() + 1);
                }
            }
        });

        setupInitialValues();
    }
    
    private void setupInitialValues() {
        updating = true;
        //fieldEditor.open(cell.findProperty(), cell.getPropertyId());
        summaryTypeComboBox.setSelectedItem(cell.getSummaryType());
        groupComboBox.removeAllItems();
        final AdsReportBand ownerBand = cell.getOwnerBand();
        if (ownerBand != null) {
            final AdsReportForm ownerForm = ownerBand.getOwnerForm();
            if (ownerForm != null) {
                for (AdsReportGroup group : ownerForm.getGroups()) {
                    groupComboBox.addItem(new SummaryPanel.Group(group));
                }
            }
        }
        AdsReportGroup group = cell.findSummaryGroup();
        if (group == null) {
            allGroupsCheckBox.setSelected(true);
            if (groupComboBox.getItemCount() > 0) {
                groupComboBox.setSelectedIndex(0);
            }
        } else {
            allGroupsCheckBox.setSelected(false);
            groupComboBox.setSelectedItem(new SummaryPanel.Group(group));
        }
        updateEnableState();
        updating = false;
    }
    
     private void updateEnableState() {
        boolean enabled = !cell.isReadOnly();
        summaryTypeComboBox.setEnabled(enabled);
        if (cell.findSummaryGroup() == null) {
            groupLabel.setEnabled(false);
            groupComboBox.setEnabled(false);
        } else {
            groupLabel.setEnabled(true);
            groupComboBox.setEnabled(true);
        }
//        groupCountSpinner.setEnabled(enabled);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sumTypeLabel = new javax.swing.JLabel();
        summaryTypeComboBox = new javax.swing.JComboBox<EReportSummaryCellType>();
        allGroupsCheckBox = new javax.swing.JCheckBox();
        groupLabel = new javax.swing.JLabel();
        groupComboBox = new javax.swing.JComboBox<SummaryPanel.Group>();

        sumTypeLabel.setText(org.openide.util.NbBundle.getMessage(SummaryPanel.class, "SummaryPanel.sumTypeLabel.text")); // NOI18N

        allGroupsCheckBox.setText(org.openide.util.NbBundle.getMessage(SummaryPanel.class, "SummaryPanel.allGroupsCheckBox.text")); // NOI18N

        groupLabel.setText(org.openide.util.NbBundle.getMessage(SummaryPanel.class, "SummaryPanel.groupLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sumTypeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(summaryTypeComboBox, 0, 260, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(allGroupsCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(groupLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sumTypeLabel)
                    .addComponent(summaryTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(allGroupsCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupLabel)
                    .addComponent(groupComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox allGroupsCheckBox;
    private javax.swing.JComboBox<SummaryPanel.Group> groupComboBox;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JLabel sumTypeLabel;
    private javax.swing.JComboBox<EReportSummaryCellType> summaryTypeComboBox;
    // End of variables declaration//GEN-END:variables
}
