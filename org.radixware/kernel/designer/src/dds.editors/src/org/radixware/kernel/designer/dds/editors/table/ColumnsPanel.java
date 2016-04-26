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

package org.radixware.kernel.designer.dds.editors.table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.actions.CopyAction;
import org.openide.actions.PasteAction;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver.Resolution;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.editors.table.widgets.ColumnsTable;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


class ColumnsPanel extends javax.swing.JPanel implements ColumnsTable.EditingColumnChangeListener, IUpdateable {

    private final DdsTableDef table;
    private final ColumnsTable columnsTable;
    private final ColumnMainOptionsPanel columnMainOptionsPanel;
    private final ColumnValuesPanel columnValuesPanel;
    private final JPanel panel1;
    private javax.swing.JScrollPane panel1Scroll;
    private javax.swing.JScrollPane panel2Scroll;
    private final JPanel panel2;
    private boolean readOnly = false;

    /**
     * Creates new form ColumnsPanel
     */
    public ColumnsPanel(DdsTableDef table) {
        initComponents();
        this.table = table;

        copyButton.setIcon(SystemAction.get(CopyAction.class).getIcon());
        pasteButton.setIcon(SystemAction.get(PasteAction.class).getIcon());
        importButton.setIcon(RadixWareIcons.DATABASE.IMPORT.getIcon());
        addColumnButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        removeColumnsButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        upButton.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon());
        downButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());
        findUsagesButton.setIcon(RadixWareIcons.TREE.DEPENDENT.getIcon());
        copyIdButton.setIcon(RadixWareIcons.JML_EDITOR.ID.getIcon());

        columnsTable = new ColumnsTable(table);
        columnsTableScrollPane.setViewportView(columnsTable);
        columnsTable.addEditingColumnChangeListener(this);

        columnMainOptionsPanel = new ColumnMainOptionsPanel();
        panel1 = createTab(columnMainOptionsPanel, columnMainOptionsPanel.getStateDisplayer());
        panel1Scroll = new javax.swing.JScrollPane(panel1);
        tabbedPane.addTab(NbBundle.getBundle(ColumnsPanel.class).getString("ColumnMainOptionsPanelName"),
                RadixWareIcons.EDIT.PROPERTIES.getIcon(),
                panel1Scroll);

        columnValuesPanel = new ColumnValuesPanel();
        panel2 = createTab(columnValuesPanel, columnValuesPanel.getStateDisplayer());
        panel2Scroll = new javax.swing.JScrollPane(panel2);
        tabbedPane.addTab(NbBundle.getBundle(ColumnsPanel.class).getString("ColumnValuesPanelName"),
                DdsDefinitionIcon.DATABASE_ATTRIBUTES.getIcon(),
                panel2Scroll);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                update();
            }
        });

        columnsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonsEnableState();
            }
        });

        columnMainOptionsPanel.addColumnChangeListener(new ColumnMainOptionsPanel.ColumnChangeListener() {
            @Override
            public void columnChanged(DdsColumnDef column) {
                columnsTable.updateRow(columnsTable.getCurrentColumnIdx());
                columnValuesPanel.updateDefaultValue();
            }
        });

        if (table.getColumns().get(EScope.ALL).size() > 0) {
            columnsTable.setRowSelectionInterval(0, 0);
        }

        initButton(copyButton, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "CTRL_C");
        initButton(pasteButton, KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "CTRL_V");
        initButton(importButton, KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK), "CTRL_I");
        initButton(addColumnButton, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "INSERT");
        initButton(removeColumnsButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
        initButton(upButton, KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK), "UP");
        initButton(downButton, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK), "DOWN");
    }

    public void removeListeners() {
        columnsTable.removeListeners();
    }

    @Override
    public void editingColumnChanged(DdsColumnDef column, boolean inherited) {
        columnMainOptionsPanel.setColumn(column, inherited);
        columnValuesPanel.setColumn(column, inherited);
    }

    private void initButton(final JButton button, KeyStroke keyStroke, String key) {
        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button.isEnabled()) {
                    button.doClick();
                }
            }
        };
        InputMap inputMap = columnsTable.getInputMap(JTable.WHEN_FOCUSED);
        inputMap.put(keyStroke, key);
        columnsTable.getActionMap().put(key, action);
    }

    public void setSelectedColumn(DdsColumnDef column) {
        columnsTable.setSelectedColumn(column);
    }

    private void updateButtonsEnableState() {
        int[] idxs = columnsTable.getSelectedRows();
        copyButton.setEnabled(idxs.length > 0);
        removeColumnsButton.setEnabled(!readOnly && columnsTable.canRemoveSelectedColumns());
        upButton.setEnabled(!readOnly && columnsTable.canRaiseSelectedColumn());
        downButton.setEnabled(!readOnly && columnsTable.canLowerSelectedColumn());
    }

    private JPanel createTab(JPanel mainPanel, StateDisplayer stateDisplayer) {
        JPanel statePanel = new JPanel(new BorderLayout());
        statePanel.setBorder(new EmptyBorder(0, 14, 5, 0));
        statePanel.add(stateDisplayer, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(statePanel, BorderLayout.SOUTH);
        return panel;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        columnsTable.setReadOnly(readOnly);
        columnMainOptionsPanel.setReadOnly(readOnly);
        columnValuesPanel.setReadOnly(readOnly);

        pasteButton.setEnabled(!readOnly);
        addColumnButton.setEnabled(!readOnly);
        importButton.setEnabled(!readOnly);

        updateButtonsEnableState();
    }

    public void open(OpenInfo openInfo) {
        for (RadixObject obj = openInfo.getTarget(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof DdsColumnDef) {
                tabbedPane.setSelectedComponent(panel1Scroll);
                columnMainOptionsPanel.open(openInfo);
                break;
            } else if (obj instanceof DdsCheckConstraintDef) {
                tabbedPane.setSelectedComponent(panel2Scroll);
                columnValuesPanel.open(openInfo);
                break;
            }
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

        splitPane = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        columnsTableScrollPane = new javax.swing.JScrollPane();
        buttonPanel = new javax.swing.JPanel();
        copyButton = new javax.swing.JButton();
        pasteButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        addColumnButton = new javax.swing.JButton();
        removeColumnsButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        findUsagesButton = new javax.swing.JButton();
        copyIdButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();

        splitPane.setDividerLocation(300);

        buttonPanel.setMinimumSize(new java.awt.Dimension(250, 16));
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        copyButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.copyButton.text")); // NOI18N
        copyButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.copyButton.toolTipText")); // NOI18N
        copyButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(copyButton);

        pasteButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.pasteButton.text")); // NOI18N
        pasteButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.pasteButton.toolTipText")); // NOI18N
        pasteButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        pasteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(pasteButton);

        importButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.importButton.text")); // NOI18N
        importButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.importButton.toolTipText")); // NOI18N
        importButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(importButton);

        addColumnButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.addColumnButton.text")); // NOI18N
        addColumnButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.addColumnButton.toolTipText")); // NOI18N
        addColumnButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addColumnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addColumnButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(addColumnButton);

        removeColumnsButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.removeColumnsButton.text")); // NOI18N
        removeColumnsButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.removeColumnsButton.toolTipText")); // NOI18N
        removeColumnsButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeColumnsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeColumnsButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(removeColumnsButton);

        upButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.upButton.text")); // NOI18N
        upButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.upButton.toolTipText")); // NOI18N
        upButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(upButton);

        downButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.downButton.text")); // NOI18N
        downButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.downButton.toolTipText")); // NOI18N
        downButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(downButton);
        buttonPanel.add(jSeparator1);

        findUsagesButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.findUsagesButton.text")); // NOI18N
        findUsagesButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.findUsagesButton.toolTipText")); // NOI18N
        findUsagesButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        findUsagesButton.setMaximumSize(new java.awt.Dimension(16, 16));
        findUsagesButton.setMinimumSize(new java.awt.Dimension(16, 16));
        findUsagesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findUsagesButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(findUsagesButton);

        copyIdButton.setText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.copyIdButton.text")); // NOI18N
        copyIdButton.setToolTipText(org.openide.util.NbBundle.getMessage(ColumnsPanel.class, "ColumnsPanel.copyIdButton.toolTipText")); // NOI18N
        copyIdButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyIdButton.setMaximumSize(new java.awt.Dimension(16, 16));
        copyIdButton.setMinimumSize(new java.awt.Dimension(16, 16));
        copyIdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyIdButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(copyIdButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addComponent(columnsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(columnsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splitPane.setLeftComponent(jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
        );

        splitPane.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        List<DdsColumnDef> cols = columnsTable.getSelectedDdsColumns();
        List<RadixObject> objs = new ArrayList<RadixObject>(cols.size());
        for (DdsColumnDef col : cols) {
            objs.add((RadixObject) col);
        }
        if (ClipboardUtils.canCopy(objs)) {
            ClipboardUtils.copyToClipboard(objs);
        }
    }//GEN-LAST:event_copyButtonActionPerformed

    private void pasteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteButtonActionPerformed
        final ClipboardSupport.DuplicationResolver resolver = new ClipboardSupport.DuplicationResolver() {
            @Override
            public Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
                return Resolution.CANCEL;
            }
        };
        if (ClipboardUtils.canPaste(table.getColumns().getLocal(), resolver)) {
            final List<DdsColumnDef> oldColumns = table.getColumns().getLocal().list();
            int currentColumnIdx = columnsTable.getCurrentColumnIdx();
            ClipboardUtils.pasteInModalEditor(table.getColumns().getLocal(), resolver);
            final List<DdsColumnDef> newColumns = table.getColumns().getLocal().list();
            newColumns.removeAll(oldColumns);
            columnsTable.replaceColumns(newColumns, currentColumnIdx);
        }
    }//GEN-LAST:event_pasteButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(table.getOwnerModel(), DdsVisitorProviderFactory.newColumnProvider());
        cfg.setStepCount(2);
        cfg.setTypesTitle("Columns");
        List<Definition> defs = ChooseDefinition.chooseDefinitions(cfg);
        if (defs != null) {
            List<RadixObject> objs = new ArrayList<RadixObject>(defs);
            objs = ClipboardSupport.duplicate(objs);
            ArrayList<DdsColumnDef> cols = new ArrayList<DdsColumnDef>(objs.size());
            int currentColumnIdx = columnsTable.getCurrentColumnIdx();
            for (RadixObject obj : objs) {
                table.getColumns().getLocal().add((DdsColumnDef) obj);
                cols.add((DdsColumnDef) obj);
            }
            columnsTable.replaceColumns(cols, currentColumnIdx);
        }
    }//GEN-LAST:event_importButtonActionPerformed

    private void addColumnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addColumnButtonActionPerformed
        DdsColumnDef column = DdsColumnDef.Factory.newInstance("NewColumn");
        int count = columnsTable.getInheritedColumnsCount();
        int curIdx = columnsTable.getCurrentColumnIdx();
        int idx = curIdx - count;
        if (idx < 0) {
            idx = 0;
        } else {
            idx++;
        }
        if (idx > table.getColumns().getLocal().size()) {
            idx = table.getColumns().getLocal().size();
        }
//        int idx = dcolumnsTable.getSelectedRow() != -1 ? columnsTable.getSelectedRow() - count : table.getColumns().getLocal().size();
//        idx = Math.max(0, idx) + 1;
//        if (idx > columnsTable.getRowCount())
//            idx = columnsTable.getRowCount();
        table.getColumns().getLocal().add(idx, column);
        DbNameUtils.updateAutoDbNames(column);
        columnsTable.setRowSelectionInterval(count + idx, count + idx);
//        if (column.isAutoDbName()) {
//            column.setDbName(column.calcAutoDbName());
//        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                columnMainOptionsPanel.requestFocusInWindow();
//                int last = columnsTable.getRowCount() - 1;
//                columnsTable.setRowSelectionInterval(last, last);
//                columnsTable.scrollToVisible(last, 1);
            }
        });
    }//GEN-LAST:event_addColumnButtonActionPerformed

    private void removeColumnsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeColumnsButtonActionPerformed
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(ColumnsPanel.class).getString("REMOVE_COLUMNS_CONFIRM"))) {
            return;
        }
        List<DdsColumnDef> remCols = columnsTable.getSelectedDdsColumns();
        int idx = columnsTable.getSelectedRow();
        boolean last = idx == columnsTable.getRowCount() - 1;
        int cnt = columnsTable.getRowCount();

        HashSet<Id> remColsIds = new HashSet<Id>(remCols.size());
        for (DdsColumnDef remCol : remCols) {
            table.getColumns().getLocal().remove(remCol);
            remColsIds.add(remCol.getId());
        }

        ArrayList<DdsIndexDef.ColumnInfo> remInd = new ArrayList<DdsIndexDef.ColumnInfo>();
        if (table.findOverwritten() == null) {
            for (DdsIndexDef.ColumnInfo colInfo : table.getPrimaryKey().getColumnsInfo()) {
                if (remColsIds.contains(colInfo.getColumnId())) {
                    remInd.add(colInfo);
                }
            }
            for (DdsIndexDef.ColumnInfo colInfo : remInd) {
                table.getPrimaryKey().getColumnsInfo().remove(colInfo);
            }
        }
        for (DdsIndexDef index : table.getIndices().getLocal()) {
            remInd.clear();
            for (DdsIndexDef.ColumnInfo colInfo : index.getColumnsInfo()) {
                if (remColsIds.contains(colInfo.getColumnId())) {
                    remInd.add(colInfo);
                }
            }
            for (DdsIndexDef.ColumnInfo colInfo : remInd) {
                index.getColumnsInfo().remove(colInfo);
            }
        }

        ArrayList<DdsTriggerDef.ColumnInfo> remTrg = new ArrayList<DdsTriggerDef.ColumnInfo>();
        for (DdsTriggerDef trigger : table.getTriggers().getLocal()) {
            remTrg.clear();
            for (DdsTriggerDef.ColumnInfo colInfo : trigger.getColumnsInfo()) {
                if (remColsIds.contains(colInfo.getColumnId())) {
                    remTrg.add(colInfo);
                }
            }
            for (DdsTriggerDef.ColumnInfo colInfo : remTrg) {
                trigger.getColumnsInfo().remove(colInfo);
            }
        }

        columnsTable.clearSelection();
        if (remCols.size() == 1) {
            if (last) {
                if (cnt > 1) {
                    columnsTable.setRowSelectionInterval(idx - 1, idx - 1);
                }
            } else {
                columnsTable.setRowSelectionInterval(idx, idx);
            }
        } else {
            if (cnt > remCols.size()) {
                columnsTable.setRowSelectionInterval(0, 0);
            }
        }

    }//GEN-LAST:event_removeColumnsButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        columnsTable.raiseSelectedColumn();
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        columnsTable.lowerSelectedColumn();
    }//GEN-LAST:event_downButtonActionPerformed

    private void findUsagesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findUsagesButtonActionPerformed
        columnsTable.findUsages();
    }//GEN-LAST:event_findUsagesButtonActionPerformed

    private void copyIdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyIdButtonActionPerformed
        columnsTable.copyId();
    }//GEN-LAST:event_copyIdButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addColumnButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JScrollPane columnsTableScrollPane;
    private javax.swing.JButton copyButton;
    private javax.swing.JButton copyIdButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton findUsagesButton;
    private javax.swing.JButton importButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton pasteButton;
    private javax.swing.JButton removeColumnsButton;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        columnMainOptionsPanel.update();
        columnValuesPanel.update();
    }
}
