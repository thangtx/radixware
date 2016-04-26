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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.common.sql.AdsSqlClassVisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;


class AdsReportSummaryCellEditor extends JPanel {
/*private class Group {

        public final AdsReportGroup group;

        public Group(AdsReportGroup group) {
            this.group = group;
        }

        @Override
        public String toString() {
            return group.getName();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Group) {
                Group grp = (Group)obj;
                return this.group.equals(grp.group);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (this.group != null ? this.group.hashCode() : 0);
            return hash;
        }

    }*/

    private volatile boolean updating = false;
    private final AdsReportSummaryCell cell;
    private  FormattedCellPanel formatPanel=null;
    private final JLabel fieldLabel = new JLabel("Field:");
    private final SummaryPanel summaryPanel;
    //private final JLabel sumTypeLabel= new JLabel("Summary type:");
    //private final JLabel groupLabel= new JLabel("Group:");
    //private final JComboBox summaryTypeComboBox=new JComboBox();
    //private final JCheckBox allGroupsCheckBox=new JCheckBox();
    //private final JComboBox groupComboBox=new JComboBox();
    private final DefinitionLinkEditPanel fieldEditor = new DefinitionLinkEditPanel();
    

    /** Creates new form AdsReportSummaryCellEditor */
    protected AdsReportSummaryCellEditor(final AdsReportSummaryCell cell) {
        super();
        this.cell = cell;
        initComponents();
        summaryPanel=new SummaryPanel(cell);
        //summaryTypeComboBox.setModel(new DefaultComboBoxModel(EReportSummaryCellType.values()));
        fieldEditor.setComboMode();
        final List<AdsPropertyDef> list = cell.getOwnerBand().getOwnerForm().getOwnerReport().getProperties().get(
                EScope.ALL, AdsSqlClassVisitorProviderFactory.newPropertyForSummaryCell());
        RadixObjectsUtils.sortByName(list);
        fieldEditor.setComboBoxValues(list, false);
        fieldEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                if (!updating) {
                    final AdsReportSummaryCell sumCell=AdsReportSummaryCellEditor.this.cell;
                    sumCell.setPropertyId(fieldEditor.getDefinitionId());
                    final AdsPropertyDef propertyDef = sumCell.findProperty();
                    AdsTypeDeclaration type=null;
                    if (propertyDef != null) {
                        type = propertyDef.getValue().getType();
                    }
                    if(type!=null){
                        sumCell.getFormat().setUseDefaultFormat(true);
                        AdsReportSummaryCellEditor.this.formatPanel.open(sumCell.getFormat(),type.getTypeId());
                    }
                }
            }
        });
        
        /*allGroupsCheckBox.setText("All group");
        allGroupsCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (allGroupsCheckBox.isSelected()) {
                    AdsReportSummaryCellEditor.this.cell.setGroupCount(0);
                } else {
                    Group group = (Group)groupComboBox.getSelectedItem();
                    if (group == null) {
                        AdsReportSummaryCellEditor.this.cell.setGroupCount(0);
                    } else {
                        AdsReportSummaryCellEditor.this.cell.setGroupCount(group.group.getIndex() + 1);
                    }
                }
                updateEnableState();
            }
        });
        summaryTypeComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!updating)
                    AdsReportSummaryCellEditor.this.cell.setSummaryType((EReportSummaryCellType)summaryTypeComboBox.getSelectedItem()); 
                
            }
        });
        
        groupComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               if (updating) {
                    return;
                }
                Group group = (Group)groupComboBox.getSelectedItem();
                if (group == null) {
                    AdsReportSummaryCellEditor.this.cell.setGroupCount(0);
                } else {
                    AdsReportSummaryCellEditor.this.cell.setGroupCount(group.group.getIndex() + 1);
                }
            }
        });*/
        createUi();
        setupInitialValues();
    }
    
    private void createUi(){
        final JPanel content = new JPanel();
        final GridBagLayout gbl = new GridBagLayout();
        content.setLayout(gbl);
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        gbl.setConstraints(fieldLabel, c);
        content.add(fieldLabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(fieldEditor, c);
        content.add(fieldEditor);
        
        c.insets = new Insets(10, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.gridwidth = 2;

        final AdsPropertyDef propertyDef = cell.findProperty();
        AdsTypeDeclaration type=null;
        if (propertyDef != null) {
             type = propertyDef.getValue().getType();
        }
        formatPanel =new FormattedCellPanel(cell.getFormat(),type==null ? EValType.ANY:type.getTypeId(),cell.isReadOnly());
        if(formatPanel!=null){            
            gbl.setConstraints(formatPanel, c);
            content.add(formatPanel); 
        } 
        
        c.gridy = 2;
        c.gridx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        gbl.setConstraints(summaryPanel, c);
        content.add(summaryPanel);
        
        
        /*c.gridwidth = 1;
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        gbl.setConstraints(sumTypeLabel, c);
        content.add(sumTypeLabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(summaryTypeComboBox, c);
        content.add(summaryTypeComboBox);
        
        c.gridwidth = 1;
        c.gridy = 3;
        c.gridx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        gbl.setConstraints(allGroupsCheckBox, c);
        content.add(allGroupsCheckBox);        
        
        c.gridwidth = 1;
        c.gridy = 4;
        c.gridx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        gbl.setConstraints(groupLabel, c);
        content.add(groupLabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(groupComboBox, c);
        content.add(groupComboBox);*/

        setLayout(new BorderLayout());
        add(content, BorderLayout.NORTH);
    }

    private void setupInitialValues() {
        updating = true;
        fieldEditor.open(cell.findProperty(), cell.getPropertyId());
        /*summaryTypeComboBox.setSelectedItem(cell.getSummaryType());
        groupComboBox.removeAllItems();
        final AdsReportBand ownerBand = cell.getOwnerBand();
        if (ownerBand != null) {
            final AdsReportForm ownerForm = ownerBand.getOwnerForm();
            if (ownerForm != null) {
                for (AdsReportGroup group : ownerForm.getGroups()) {
                    groupComboBox.addItem(new Group(group));
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
            groupComboBox.setSelectedItem(new Group(group));
        }*/
        updateEnableState();
        updating = false;
    }

    private void updateEnableState() {
        final boolean enabled = !cell.isReadOnly();
        if(formatPanel!=null){
            formatPanel.setEnabled(enabled);
        }
        fieldEditor.setEnabled(enabled);
        /*summaryTypeComboBox.setEnabled(enabled);
        if (cell.findSummaryGroup() == null) {
            groupLabel.setEnabled(false);
            groupComboBox.setEnabled(false);
        } else {
            groupLabel.setEnabled(true);
            groupComboBox.setEnabled(true);
        }*/
//        groupCountSpinner.setEnabled(enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /*if (!updating)
            cell.setGroupCount(((Integer)groupCountSpinner.getValue()).intValue());*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
