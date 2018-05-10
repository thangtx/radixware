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
 * IndexMainOptionsPanel.java
 *
 * Created on Jan 23, 2009, 11:28:01 AM
 */
package org.radixware.kernel.designer.dds.editors.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.dds.editors.table.widgets.IndexColumnsTable;
import org.radixware.kernel.designer.dds.editors.table.widgets.TableColumnsList;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.common.dialogs.sqlscript.actions.ShowReCreateScriptAction;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;
import org.radixware.kernel.designer.dds.script.defs.DdsIndexScriptGenerator;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;


public class IndexMainOptionsPanel extends javax.swing.JPanel implements IUpdateable {

    @Override
    public void update() {
        if (index == null) {
            return;
        }
        updating = true;
        indexNameEditPanel.setCurrentName(index.getName());
        indexAutoDbNameCheckBox.setSelected(index.isAutoDbName());
        indexDbNameEditPanel.setDbName(index.getDbName());
        indexGenerateInDbCheckBox.setSelected(index.isGeneratedInDb());
        indexColumnsTable.setIndex(index);
        tableColumnsList.setIndex(index);
        chIsDeprecated.setSelected(index.isDeprecated());
        updateEnableState();
        updating = false;
    }

    public interface IndexChangeListener {

        public void indexChanged(DdsIndexDef index);
    }
    private DdsIndexDef index = null;
    private boolean readOnly = false;
    private boolean inherited = false;
    private volatile boolean updating = false;
    private final IndexColumnsTable indexColumnsTable;
    private final TableColumnsList tableColumnsList;
    private final ArrayList<IndexChangeListener> listeners = new ArrayList<IndexChangeListener>();
    private final StateDisplayer stateDisplayer = new StateDisplayer();

    /** Creates new form IndexMainOptionsPanel */
    public IndexMainOptionsPanel(DdsTableDef table) {
        initComponents();
        IStateDisplayer.Locator.register(stateDisplayer, this);
        upButton.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon());
        downButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());
        getButton.setIcon(RadixWareIcons.ARROW.LEFT.getIcon());
        takeButton.setIcon(RadixWareIcons.ARROW.RIGHT.getIcon());

        indexColumnsTable = new IndexColumnsTable();
        indexColumnsScrollPane.setViewportView(indexColumnsTable);

        tableColumnsList = new TableColumnsList(table);
        tableColumnsScrollPane.setViewportView(tableColumnsList);

        indexColumnsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexColumnsTableSelectionChanged();
            }
        });

        tableColumnsList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableColumnsListSelectionChanged();
            }
        });

        indexNameEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && index != null && indexNameEditPanel.isComplete()) {
                    String name = indexNameEditPanel.getCurrentName();
                    index.setName(name);
                    if (index.isAutoDbName()) {
                        DbNameUtils.updateAutoDbNames(index);
                        updating = true;
                        indexDbNameEditPanel.setDbName(index.getDbName());
                        updating = false;
                    }
                    for (IndexChangeListener listener : listeners) {
                        listener.indexChanged(index);
                    }
                }
            }
        });

        indexDbNameEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && index != null && indexDbNameEditPanel.isComplete()) {
                    index.setDbName(indexDbNameEditPanel.getDbName());
                }
            }
        });

        indexDbNameEditPanel.setNameAcceptor(NameAcceptorFactory.newDbNameAcceptor(table.getLayer()));

        tableColumnsList.addChooseColumnActionListener(new TableColumnsList.ChooseColumnActionListener() {

            @Override
            public void chooseColumnActionPerformed() {
                if (index != null && !readOnly && !inherited) {
                    getButtonActionPerformed(null);
                }
            }
        });

        chIsDeprecated.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating && index != null) {
                    index.setDeprecated(chIsDeprecated.isSelected());
                    for (IndexChangeListener listener : listeners) {
                        listener.indexChanged(index);
                    }
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

    private boolean isPrimaryKey() {
        if (index == null) {
            return false;
        }
        return Utils.equals(index.getOwnerTable().getPrimaryKey(), index);
    }

    private void updateEnableState() {
        indexNameEditPanel.setEditable(!readOnly && !inherited && !isPrimaryKey());
        indexAutoDbNameCheckBox.setEnabled(!readOnly && !inherited);
        indexDbNameEditPanel.setEditable(!readOnly && (index != null ? !index.isAutoDbName() : false) && !inherited);
        indexGenerateInDbCheckBox.setEnabled(!readOnly && !inherited);
        indexColumnsTable.setEnabled(!readOnly && !inherited);
        tableColumnsList.setEnabled(!readOnly && !inherited);
        chIsDeprecated.setEnabled(!readOnly && !inherited);
        indexColumnsTableSelectionChanged();
        tableColumnsListSelectionChanged();
    }

    /**
     * Set editing index
     * @param index can be null
     */
    public void setIndex(DdsIndexDef index, boolean inherited) {
        if (Utils.equals(this.index, index)) {
            return;
        }
        this.index = index;
        btViewReCreateScript.setAction(new ShowReCreateScriptAction(index));
        this.inherited = inherited;
        if (index == null) {
            this.setVisible(false);
            stateDisplayer.setVisible(false);
            return;
        }
//        CheckForDuplicationProvider checkForDuplicationProvider = CheckForDuplicationProvider.Factory.newForRenaming(index);
//        indexNameEditPanel.setCheckForDuplicationProvider(checkForDuplicationProvider);
        update();
        this.setVisible(true);
        stateDisplayer.setVisible(true);
    }

    private void indexColumnsTableSelectionChanged() {
        int idx = indexColumnsTable.getSelectedRow();
        if (idx == -1) {
            upButton.setEnabled(false);
            downButton.setEnabled(false);
            takeButton.setEnabled(false);
        } else {
            upButton.setEnabled(!readOnly && !inherited && idx != 0);
            downButton.setEnabled(!readOnly && !inherited && idx != indexColumnsTable.getRowCount() - 1);
            takeButton.setEnabled(!readOnly && !inherited);
        }
    }

    private void tableColumnsListSelectionChanged() {
        int idx = tableColumnsList.getSelectedIndex();
        getButton.setEnabled(!readOnly && !inherited && idx != -1);
    }

    public void addIndexChangeListener(IndexChangeListener listener) {
        listeners.add(listener);
    }

    public void removeIndexChangeListener(IndexChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean requestFocusInWindow() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                indexNameEditPanel.requestFocusInWindow();
            }
        });
        return super.requestFocusInWindow();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        indexGenerateInDbCheckBox = new javax.swing.JCheckBox();
        indexAutoDbNameCheckBox = new javax.swing.JCheckBox();
        indexDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        indexNameLabel = new javax.swing.JLabel();
        indexNameEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        mainPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        indexColumnsScrollPane = new javax.swing.JScrollPane();
        upDownButtonPanel = new javax.swing.JPanel();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        centerPanel = new javax.swing.JPanel();
        getButton = new javax.swing.JButton();
        takeButton = new javax.swing.JButton();
        rightPanel = new javax.swing.JPanel();
        tableColumnsScrollPane = new javax.swing.JScrollPane();
        chIsDeprecated = new javax.swing.JCheckBox();
        btViewReCreateScript = new javax.swing.JButton();

        indexGenerateInDbCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.indexGenerateInDbCheckBox.text")); // NOI18N
        indexGenerateInDbCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexGenerateInDbCheckBoxItemStateChanged(evt);
            }
        });

        indexAutoDbNameCheckBox.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.indexAutoDbNameCheckBox.text")); // NOI18N
        indexAutoDbNameCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indexAutoDbNameCheckBoxItemStateChanged(evt);
            }
        });

        indexNameLabel.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.indexNameLabel.text")); // NOI18N

        upDownButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        upButton.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.upButton.text")); // NOI18N
        upButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.upButton.toolTipText")); // NOI18N
        upButton.setMinimumSize(new java.awt.Dimension(47, 20));
        upButton.setPreferredSize(new java.awt.Dimension(64, 29));
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        upDownButtonPanel.add(upButton);

        downButton.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.downButton.text")); // NOI18N
        downButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.downButton.toolTipText")); // NOI18N
        downButton.setMinimumSize(new java.awt.Dimension(47, 20));
        downButton.setPreferredSize(new java.awt.Dimension(64, 29));
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        upDownButtonPanel.add(downButton);

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(upDownButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
            .addComponent(indexColumnsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addComponent(indexColumnsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upDownButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        centerPanel.setPreferredSize(new java.awt.Dimension(51, 303));

        getButton.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.getButton.text")); // NOI18N
        getButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.getButton.toolTipText")); // NOI18N
        getButton.setPreferredSize(new java.awt.Dimension(51, 29));
        getButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonActionPerformed(evt);
            }
        });

        takeButton.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.takeButton.text")); // NOI18N
        takeButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.takeButton.toolTipText")); // NOI18N
        takeButton.setPreferredSize(new java.awt.Dimension(51, 29));
        takeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout centerPanelLayout = new javax.swing.GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(getButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(takeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        centerPanelLayout.setVerticalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centerPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(getButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(takeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(173, 173, 173))
        );

        rightPanel.setPreferredSize(new java.awt.Dimension(251, 303));

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 141, Short.MAX_VALUE)
            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tableColumnsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tableColumnsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(centerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(centerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
            .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        );

        chIsDeprecated.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.chIsDeprecated.text")); // NOI18N

        btViewReCreateScript.setText(org.openide.util.NbBundle.getMessage(IndexMainOptionsPanel.class, "IndexMainOptionsPanel.btViewReCreateScript.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(indexGenerateInDbCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btViewReCreateScript))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(indexAutoDbNameCheckBox)
                            .addComponent(indexNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(indexNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chIsDeprecated))
                            .addComponent(indexDbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(indexNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(indexNameLabel)
                    .addComponent(chIsDeprecated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(indexDbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(indexAutoDbNameCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(indexGenerateInDbCheckBox)
                    .addComponent(btViewReCreateScript))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void indexAutoDbNameCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexAutoDbNameCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        boolean isAutoDbName = indexAutoDbNameCheckBox.isSelected();
        indexDbNameEditPanel.setEditable(!isAutoDbName);
        index.setAutoDbName(isAutoDbName);
        if (isAutoDbName) {
            String dbName = index.calcAutoDbName();
            index.setDbName(dbName);
            updating = true;
            indexDbNameEditPanel.setDbName(dbName);
            updating = false;
        } else {
            if (indexAutoDbNameCheckBox.hasFocus()) {
                indexDbNameEditPanel.requestFocusInWindow();
                indexDbNameEditPanel.selectAll();
            }
        }
    }//GEN-LAST:event_indexAutoDbNameCheckBoxItemStateChanged

    private void indexGenerateInDbCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indexGenerateInDbCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        index.setGeneratedInDb(indexGenerateInDbCheckBox.isSelected());
    }//GEN-LAST:event_indexGenerateInDbCheckBoxItemStateChanged

    private void getButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonActionPerformed
        tableColumnsList.takeSelectedColumns();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int last = indexColumnsTable.getRowCount();
                indexColumnsTable.setRowSelectionInterval(last - 1, last - 1);
            }
        });
    }//GEN-LAST:event_getButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        indexColumnsTable.raiseSelectedColumn();
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        indexColumnsTable.lowerSelectedColumn();
    }//GEN-LAST:event_downButtonActionPerformed

    private void takeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeButtonActionPerformed
        indexColumnsTable.takeSelectedColumn();
        tableColumnsList.clearSelection();
}//GEN-LAST:event_takeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btViewReCreateScript;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JCheckBox chIsDeprecated;
    private javax.swing.JButton downButton;
    private javax.swing.JButton getButton;
    private javax.swing.JCheckBox indexAutoDbNameCheckBox;
    private javax.swing.JScrollPane indexColumnsScrollPane;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel indexDbNameEditPanel;
    private javax.swing.JCheckBox indexGenerateInDbCheckBox;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel indexNameEditPanel;
    private javax.swing.JLabel indexNameLabel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JScrollPane tableColumnsScrollPane;
    private javax.swing.JButton takeButton;
    private javax.swing.JButton upButton;
    private javax.swing.JPanel upDownButtonPanel;
    // End of variables declaration//GEN-END:variables
}
