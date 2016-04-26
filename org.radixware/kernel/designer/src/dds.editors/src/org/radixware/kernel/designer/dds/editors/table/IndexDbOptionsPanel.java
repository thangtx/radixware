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
 * IndexDbOptionsPanel.java
 *
 * Created on Jan 23, 2009, 12:53:09 PM
 */
package org.radixware.kernel.designer.dds.editors.table;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;


public class IndexDbOptionsPanel extends javax.swing.JPanel implements IUpdateable {

    private DdsIndexDef index = null;
    private boolean readOnly = false;
    private boolean inherited = false;
    private volatile boolean updating = false;
    private final StateDisplayer stateDisplayer = new StateDisplayer();

    /**
     * Creates new form IndexDbOptionsPanel
     */
    public IndexDbOptionsPanel() {
        initComponents();
        IStateDisplayer.Locator.register(stateDisplayer, this);

        constraintDbNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && hasUniqueConstraint() && constraintDbNameEditPanel.isComplete()) {
                    index.getUniqueConstraint().setDbName(constraintDbNameEditPanel.getDbName());
                }
            }
        });

        indexTableSpaceDbNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && index != null && indexTableSpaceDbNameEditPanel.isComplete()) {
                    index.setTablespaceDbName(indexTableSpaceDbNameEditPanel.getDbName());
                }
            }
        });

        this.setVisible(false);
        stateDisplayer.setVisible(false);
    }

    public StateDisplayer getStateDisplayer() {
        return stateDisplayer;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private void updateEnableState() {
        generateUniqueConstraintCheckBox.setEnabled(!readOnly && !inherited);
        indexTableSpaceCheckBox.setEnabled(!readOnly && !inherited);
        indexTableSpaceDbNameEditPanel.setEditable(!readOnly && !inherited && hasTableSpace());
        indexBitmapCheckBox.setEnabled(!readOnly && !inherited);
        indexLocalCheckBox.setEnabled(!readOnly && !inherited);
        indexNologgingCheckBox.setEnabled(!readOnly && !inherited);
        indexInvisibleCheckBox.setEnabled(!readOnly && !inherited);
        indexUniqueCheckBox.setEnabled(!readOnly && !inherited);
        indexReverseCheckBox.setEnabled(!readOnly && !inherited);
        updateConstraintEnableState();
    }

    private boolean hasTableSpace() {
        return index != null ? (index.getTablespaceDbName() != null) : false;
    }

    private boolean hasUniqueConstraint() {
        return index != null ? (index.getUniqueConstraint() != null) : false;
    }

    public void setIndex(DdsIndexDef index, boolean inherited) {
        if (Utils.equals(this.index, index)) {
            return;
        }
        this.index = index;
        this.inherited = inherited;
        if (index == null) {
            this.setVisible(false);
            stateDisplayer.setVisible(false);
            return;
        }
        update();
        this.setVisible(true);
        stateDisplayer.setVisible(true);
    }

    private void updateConstraintEnableState() {
        if (hasUniqueConstraint()) {
            constraintAutoDbNameCheckBox.setEnabled(!readOnly && !inherited);
            constraintDbNameEditPanel.setEditable(!readOnly && !inherited && !index.getUniqueConstraint().isAutoDbName());
            constraintDeferrableCheckBox.setEnabled(!readOnly && !inherited);
            constraintDisableCheckBox.setEnabled(!readOnly && !inherited);
            constraintInitiallyDeferredCheckBox.setEnabled(!readOnly && !inherited);
            constraintNovalidateCheckBox.setEnabled(!readOnly && !inherited);
            constraintRelyCheckBox.setEnabled(!readOnly && !inherited);
        } else {
            constraintAutoDbNameCheckBox.setEnabled(false);
            constraintDbNameEditPanel.setEditable(false);
            constraintDeferrableCheckBox.setEnabled(false);
            constraintDisableCheckBox.setEnabled(false);
            constraintInitiallyDeferredCheckBox.setEnabled(false);
            constraintNovalidateCheckBox.setEnabled(false);
            constraintRelyCheckBox.setEnabled(false);
        }
    }

    private void updateConstraintOptions() {
//        updating = true;
        if (hasUniqueConstraint()) {
            DdsUniqueConstraintDef constraint = index.getUniqueConstraint();
            constraintAutoDbNameCheckBox.setSelected(constraint.isAutoDbName());
//            if (constraint.isAutoDbName())
//                constraint.setDbName(constraint.calcAutoDbName());
            constraintDbNameEditPanel.setDbName(constraint.getDbName());
            constraintRelyCheckBox.setSelected(constraint.getDbOptions().contains(EDdsConstraintDbOption.RELY));
            constraintDisableCheckBox.setSelected(constraint.getDbOptions().contains(EDdsConstraintDbOption.DISABLE));
            constraintNovalidateCheckBox.setSelected(constraint.getDbOptions().contains(EDdsConstraintDbOption.NOVALIDATE));
            constraintDeferrableCheckBox.setSelected(constraint.getDbOptions().contains(EDdsConstraintDbOption.DEFERRABLE));
            constraintInitiallyDeferredCheckBox.setSelected(constraint.getDbOptions().contains(EDdsConstraintDbOption.INITIALLY_DEFERRED));
        } else {
            constraintAutoDbNameCheckBox.setSelected(false);
            constraintDbNameEditPanel.setDbName("");
            constraintRelyCheckBox.setSelected(false);
            constraintDisableCheckBox.setSelected(false);
            constraintNovalidateCheckBox.setSelected(false);
            constraintDeferrableCheckBox.setSelected(false);
            constraintInitiallyDeferredCheckBox.setSelected(false);
        }
//        updating = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        generateUniqueConstraintCheckBox = new javax.swing.JCheckBox();
        constraintAutoDbNameCheckBox = new javax.swing.JCheckBox();
        constraintDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        constraintRelyCheckBox = new javax.swing.JCheckBox();
        constraintDisableCheckBox = new javax.swing.JCheckBox();
        constraintNovalidateCheckBox = new javax.swing.JCheckBox();
        constraintDeferrableCheckBox = new javax.swing.JCheckBox();
        constraintInitiallyDeferredCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        indexTableSpaceCheckBox = new javax.swing.JCheckBox();
        indexTableSpaceDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        indexBitmapCheckBox = new javax.swing.JCheckBox();
        indexLocalCheckBox = new javax.swing.JCheckBox();
        indexNologgingCheckBox = new javax.swing.JCheckBox();
        indexInvisibleCheckBox = new javax.swing.JCheckBox();
        indexUniqueCheckBox = new javax.swing.JCheckBox();
        indexReverseCheckBox = new javax.swing.JCheckBox();

        generateUniqueConstraintCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.generateUniqueConstraintCheckBox.text")); // NOI18N
        generateUniqueConstraintCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                generateUniqueConstraintCheckBoxItemStateChanged(evt);
            }
        });

        constraintAutoDbNameCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.constraintAutoDbNameCheckBox.text")); // NOI18N
        constraintAutoDbNameCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                constraintAutoDbNameCheckBoxItemStateChanged(evt);
            }
        });

        constraintRelyCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.constraintRelyCheckBox.text")); // NOI18N
        constraintRelyCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                constraintRelyCheckBoxItemStateChanged(evt);
            }
        });

        constraintDisableCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.constraintDisableCheckBox.text")); // NOI18N
        constraintDisableCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                constraintDisableCheckBoxItemStateChanged(evt);
            }
        });

        constraintNovalidateCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.constraintNovalidateCheckBox.text")); // NOI18N
        constraintNovalidateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                constraintNovalidateCheckBoxItemStateChanged(evt);
            }
        });

        constraintDeferrableCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.constraintDeferrableCheckBox.text")); // NOI18N
        constraintDeferrableCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                constraintDeferrableCheckBoxItemStateChanged(evt);
            }
        });

        constraintInitiallyDeferredCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.constraintInitiallyDeferredCheckBox.text")); // NOI18N
        constraintInitiallyDeferredCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                constraintInitiallyDeferredCheckBoxItemStateChanged(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.jLabel1.text")); // NOI18N

        indexTableSpaceCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.indexTableSpaceCheckBox.text")); // NOI18N
        indexTableSpaceCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexTableSpaceCheckBoxItemStateChanged(evt);
            }
        });

        indexBitmapCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.indexBitmapCheckBox.text")); // NOI18N
        indexBitmapCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexBitmapCheckBoxItemStateChanged(evt);
            }
        });

        indexLocalCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.indexLocalCheckBox.text")); // NOI18N
        indexLocalCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexLocalCheckBoxItemStateChanged(evt);
            }
        });

        indexNologgingCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.indexNologgingCheckBox.text")); // NOI18N
        indexNologgingCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexNologgingCheckBoxItemStateChanged(evt);
            }
        });

        indexInvisibleCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.indexInvisibleCheckBox.text")); // NOI18N
        indexInvisibleCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexInvisibleCheckBoxItemStateChanged(evt);
            }
        });

        indexUniqueCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.indexUniqueCheckBox.text")); // NOI18N
        indexUniqueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexUniqueCheckBoxItemStateChanged(evt);
            }
        });

        indexReverseCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexDbOptionsPanel.class, "IndexDbOptionsPanel.indexReverseCheckBox.text")); // NOI18N
        indexReverseCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexReverseCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(generateUniqueConstraintCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(constraintAutoDbNameCheckBox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(constraintDbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(constraintRelyCheckBox)
                                            .addComponent(constraintDisableCheckBox)
                                            .addComponent(constraintNovalidateCheckBox))
                                        .addGap(102, 102, 102)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(constraintInitiallyDeferredCheckBox)
                                            .addComponent(constraintDeferrableCheckBox))))))
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(indexTableSpaceCheckBox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(indexTableSpaceDbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(indexBitmapCheckBox)
                                            .addComponent(indexLocalCheckBox)
                                            .addComponent(indexNologgingCheckBox))
                                        .addGap(109, 109, 109)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(indexReverseCheckBox)
                                            .addComponent(indexInvisibleCheckBox)
                                            .addComponent(indexUniqueCheckBox))))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(generateUniqueConstraintCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(constraintDbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(constraintAutoDbNameCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(constraintRelyCheckBox)
                    .addComponent(constraintDeferrableCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(constraintDisableCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(constraintNovalidateCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addComponent(constraintInitiallyDeferredCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(indexTableSpaceCheckBox)
                    .addComponent(indexTableSpaceDbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(indexBitmapCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(indexLocalCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(indexNologgingCheckBox))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(indexInvisibleCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(indexUniqueCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(indexReverseCheckBox)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void generateUniqueConstraintCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_generateUniqueConstraintCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (generateUniqueConstraintCheckBox.isSelected()) {
            DdsUniqueConstraintDef constraint = DdsUniqueConstraintDef.Factory.newInstance();
            index.setUniqueConstraint(constraint);
            DbNameUtils.updateAutoDbNames(constraint);
        } else {
            index.setUniqueConstraint(null);
        }
        updating = true;
        updateConstraintOptions();
        updating = false;
        updateConstraintEnableState();
    }//GEN-LAST:event_generateUniqueConstraintCheckBoxItemStateChanged

    private void constraintAutoDbNameCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_constraintAutoDbNameCheckBoxItemStateChanged
        if (updating || !hasUniqueConstraint()) {
            return;
        }
        DdsUniqueConstraintDef constraint = index.getUniqueConstraint();
        boolean isAutoDbName = constraintAutoDbNameCheckBox.isSelected();
        constraintDbNameEditPanel.setEditable(!isAutoDbName);
        constraint.setAutoDbName(isAutoDbName);
        if (isAutoDbName) {
            String dbName = constraint.calcAutoDbName();
            constraint.setDbName(dbName);
            updating = true;
            constraintDbNameEditPanel.setDbName(dbName);
            updating = false;
        } else {
            if (constraintAutoDbNameCheckBox.hasFocus()) {
                constraintDbNameEditPanel.requestFocusInWindow();
                constraintDbNameEditPanel.selectAll();
            }
        }
    }//GEN-LAST:event_constraintAutoDbNameCheckBoxItemStateChanged

    private void constraintRelyCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_constraintRelyCheckBoxItemStateChanged
        if (updating || !hasUniqueConstraint()) {
            return;
        }
        if (constraintRelyCheckBox.isSelected()) {
            index.getUniqueConstraint().getDbOptions().add(EDdsConstraintDbOption.RELY);
        } else {
            index.getUniqueConstraint().getDbOptions().remove(EDdsConstraintDbOption.RELY);
        }
    }//GEN-LAST:event_constraintRelyCheckBoxItemStateChanged

    private void constraintDisableCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_constraintDisableCheckBoxItemStateChanged
        if (updating || !hasUniqueConstraint()) {
            return;
        }
        if (constraintDisableCheckBox.isSelected()) {
            index.getUniqueConstraint().getDbOptions().add(EDdsConstraintDbOption.DISABLE);
        } else {
            index.getUniqueConstraint().getDbOptions().remove(EDdsConstraintDbOption.DISABLE);
        }
    }//GEN-LAST:event_constraintDisableCheckBoxItemStateChanged

    private void constraintNovalidateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_constraintNovalidateCheckBoxItemStateChanged
        if (updating || !hasUniqueConstraint()) {
            return;
        }
        if (constraintNovalidateCheckBox.isSelected()) {
            index.getUniqueConstraint().getDbOptions().add(EDdsConstraintDbOption.NOVALIDATE);
        } else {
            index.getUniqueConstraint().getDbOptions().remove(EDdsConstraintDbOption.NOVALIDATE);
        }
    }//GEN-LAST:event_constraintNovalidateCheckBoxItemStateChanged

    private void constraintDeferrableCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_constraintDeferrableCheckBoxItemStateChanged
        if (updating || !hasUniqueConstraint()) {
            return;
        }
        if (constraintDeferrableCheckBox.isSelected()) {
            index.getUniqueConstraint().getDbOptions().add(EDdsConstraintDbOption.DEFERRABLE);
        } else {
            index.getUniqueConstraint().getDbOptions().remove(EDdsConstraintDbOption.DEFERRABLE);
        }
    }//GEN-LAST:event_constraintDeferrableCheckBoxItemStateChanged

    private void constraintInitiallyDeferredCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_constraintInitiallyDeferredCheckBoxItemStateChanged
        if (updating || !hasUniqueConstraint()) {
            return;
        }
        if (constraintInitiallyDeferredCheckBox.isSelected()) {
            index.getUniqueConstraint().getDbOptions().add(EDdsConstraintDbOption.INITIALLY_DEFERRED);
        } else {
            index.getUniqueConstraint().getDbOptions().remove(EDdsConstraintDbOption.INITIALLY_DEFERRED);
        }
    }//GEN-LAST:event_constraintInitiallyDeferredCheckBoxItemStateChanged

    private void indexTableSpaceCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexTableSpaceCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        indexTableSpaceDbNameEditPanel.setEditable(indexTableSpaceCheckBox.isSelected());
        if (indexTableSpaceCheckBox.isSelected()) {
            if (indexTableSpaceDbNameEditPanel.isComplete()) {
                index.setTablespaceDbName(indexTableSpaceDbNameEditPanel.getDbName());
            }
            if (indexTableSpaceCheckBox.hasFocus()) {
                indexTableSpaceDbNameEditPanel.requestFocusInWindow();
                indexTableSpaceDbNameEditPanel.selectAll();
            }
        } else {
            index.setTablespaceDbName(null);
            updating = true;
            indexTableSpaceDbNameEditPanel.setDbName(TablespaceCalculator.calcTablespaceForIndex(index));
            updating = false;
        }
    }//GEN-LAST:event_indexTableSpaceCheckBoxItemStateChanged

    private void indexBitmapCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexBitmapCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (indexBitmapCheckBox.isSelected()) {
            index.getDbOptions().add(DdsIndexDef.EDbOption.BITMAP);
        } else {
            index.getDbOptions().remove(DdsIndexDef.EDbOption.BITMAP);
        }
    }//GEN-LAST:event_indexBitmapCheckBoxItemStateChanged

    private void indexLocalCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexLocalCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (indexLocalCheckBox.isSelected()) {
            index.getDbOptions().add(DdsIndexDef.EDbOption.LOCAL);
        } else {
            index.getDbOptions().remove(DdsIndexDef.EDbOption.LOCAL);
        }
    }//GEN-LAST:event_indexLocalCheckBoxItemStateChanged

    private void indexNologgingCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexNologgingCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (indexNologgingCheckBox.isSelected()) {
            index.getDbOptions().add(DdsIndexDef.EDbOption.NOLOGGING);
        } else {
            index.getDbOptions().remove(DdsIndexDef.EDbOption.NOLOGGING);
        }
    }//GEN-LAST:event_indexNologgingCheckBoxItemStateChanged

    private void indexInvisibleCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexInvisibleCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (indexInvisibleCheckBox.isSelected()) {
            index.getDbOptions().add(DdsIndexDef.EDbOption.INVISIBLE);
        } else {
            index.getDbOptions().remove(DdsIndexDef.EDbOption.INVISIBLE);
        }
    }//GEN-LAST:event_indexInvisibleCheckBoxItemStateChanged

    private void indexUniqueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexUniqueCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (indexUniqueCheckBox.isSelected()) {
            index.getDbOptions().add(DdsIndexDef.EDbOption.UNIQUE);
        } else {
            index.getDbOptions().remove(DdsIndexDef.EDbOption.UNIQUE);
        }
    }//GEN-LAST:event_indexUniqueCheckBoxItemStateChanged

    private void indexReverseCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexReverseCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (indexReverseCheckBox.isSelected()) {
            index.getDbOptions().add(DdsIndexDef.EDbOption.REVERSE);
        } else {
            index.getDbOptions().remove(DdsIndexDef.EDbOption.REVERSE);
        }
    }//GEN-LAST:event_indexReverseCheckBoxItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox constraintAutoDbNameCheckBox;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel constraintDbNameEditPanel;
    private javax.swing.JCheckBox constraintDeferrableCheckBox;
    private javax.swing.JCheckBox constraintDisableCheckBox;
    private javax.swing.JCheckBox constraintInitiallyDeferredCheckBox;
    private javax.swing.JCheckBox constraintNovalidateCheckBox;
    private javax.swing.JCheckBox constraintRelyCheckBox;
    private javax.swing.JCheckBox generateUniqueConstraintCheckBox;
    private javax.swing.JCheckBox indexBitmapCheckBox;
    private javax.swing.JCheckBox indexInvisibleCheckBox;
    private javax.swing.JCheckBox indexLocalCheckBox;
    private javax.swing.JCheckBox indexNologgingCheckBox;
    private javax.swing.JCheckBox indexReverseCheckBox;
    private javax.swing.JCheckBox indexTableSpaceCheckBox;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel indexTableSpaceDbNameEditPanel;
    private javax.swing.JCheckBox indexUniqueCheckBox;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        if (index == null) {
            return;
        }
        updating = true;
        generateUniqueConstraintCheckBox.setSelected(hasUniqueConstraint());
        updateConstraintOptions();

        indexTableSpaceCheckBox.setSelected(hasTableSpace());
        if (hasTableSpace()) {
            indexTableSpaceDbNameEditPanel.setDbName(index.getTablespaceDbName());
        } else {
            indexTableSpaceDbNameEditPanel.setDbName(TablespaceCalculator.calcTablespaceForIndex(index));
        }

        constraintDbNameEditPanel.setNameAcceptor(NameAcceptorFactory.newDbNameAcceptor(index.getLayer()));

        indexBitmapCheckBox.setSelected(index.getDbOptions().contains(DdsIndexDef.EDbOption.BITMAP));
        indexLocalCheckBox.setSelected(index.getDbOptions().contains(DdsIndexDef.EDbOption.LOCAL));
        indexNologgingCheckBox.setSelected(index.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING));
        indexInvisibleCheckBox.setSelected(index.getDbOptions().contains(DdsIndexDef.EDbOption.INVISIBLE));
        indexUniqueCheckBox.setSelected(index.getDbOptions().contains(DdsIndexDef.EDbOption.UNIQUE));
        indexReverseCheckBox.setSelected(index.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE));
        updateEnableState();
        updating = false;
    }
}
