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
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.actions.CopyAction;
import org.openide.actions.PasteAction;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.editors.table.widgets.IndicesList;
import org.radixware.kernel.common.resources.RadixWareIcons;


class IndicesPanel extends javax.swing.JPanel implements IndicesList.EditingIndexChangeListener, IUpdateable {

    private final DdsTableDef table;
    private final IndicesList indicesList;
    private final IndexMainOptionsPanel indexMainOptionsPanel;
    private final IndexDbOptionsPanel indexDbOptionsPanel;
    private boolean readOnly = false;

    /** Creates new form IndicesPanel */
    public IndicesPanel(final DdsTableDef table) {
        initComponents();
        this.table = table;

        copyButton.setIcon(SystemAction.get(CopyAction.class).getIcon());
        pasteButton.setIcon(SystemAction.get(PasteAction.class).getIcon());
        addIndexButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        removeIndexButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());

        indexMainOptionsPanel = new IndexMainOptionsPanel(table);
        tabbedPane.addTab(NbBundle.getBundle(IndicesPanel.class).getString("IndicesMainPanelName"),
                RadixWareIcons.EDIT.PROPERTIES.getIcon(),
                new JScrollPane(createTab(indexMainOptionsPanel, indexMainOptionsPanel.getStateDisplayer())));

        indexDbOptionsPanel = new IndexDbOptionsPanel();
        tabbedPane.addTab(NbBundle.getBundle(IndicesPanel.class).getString("IndicesDbOptionsPanelName"),
                DdsDefinitionIcon.DATABASE_ATTRIBUTES.getIcon(),
                createTab(indexDbOptionsPanel, indexDbOptionsPanel.getStateDisplayer()));

        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                update();
            }
        });

        indicesList = new IndicesList(table);
        indicesListScrollPane.setViewportView(indicesList);
        indicesList.addEditingIndexChangeListener(this);
        if (table.getPrimaryKey() != null) {
            indicesList.setSelectedIndex(0);
        }

        indexMainOptionsPanel.addIndexChangeListener(new IndexMainOptionsPanel.IndexChangeListener() {

            @Override
            public void indexChanged(DdsIndexDef index) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        indicesList.updateRow(indicesList.getCurrentIndexIdx());
                    }
                });
            }
        });

        initButton(copyButton, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "CTRL_C");
        initButton(pasteButton, KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "CTRL_V");
        initButton(addIndexButton, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "INSERT");
        initButton(removeIndexButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
    }

    public void removeListeners() {
        indicesList.removeListeners();
    }

    @Override
    public void editingIndexChanged(DdsIndexDef editingIndex, boolean inherited) {
        indexMainOptionsPanel.setIndex(editingIndex, inherited);
        indexDbOptionsPanel.setIndex(editingIndex, inherited);
        updateButtonsEnableState();
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
        InputMap inputMap = indicesList.getInputMap(JList.WHEN_FOCUSED);
        inputMap.put(keyStroke, key);
        indicesList.getActionMap().put(key, action);
    }

    public void setSelectedIndex(DdsIndexDef index) {
        indicesList.setSelectedIndex(index);
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

    private void updateButtonsEnableState() {
        addIndexButton.setEnabled(!readOnly);
        pasteButton.setEnabled(!readOnly);
        removeIndexButton.setEnabled(!readOnly && indicesList.canRemoveSelectedIndices());
        copyButton.setEnabled(indicesList.getSelectedIndices().length > 0);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        indexMainOptionsPanel.setReadOnly(readOnly);
        indexDbOptionsPanel.setReadOnly(readOnly);

        updateButtonsEnableState();
//        indicesList.fireEditingIndexChanged();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        indicesListScrollPane = new javax.swing.JScrollPane();
        buttonPanel = new javax.swing.JPanel();
        addIndexButton = new javax.swing.JButton();
        removeIndexButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        pasteButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();

        splitPane.setDividerLocation(200);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        addIndexButton.setText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.addIndexButton.text")); // NOI18N
        addIndexButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.addIndexButton.toolTipText")); // NOI18N
        addIndexButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addIndexButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addIndexButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(addIndexButton);

        removeIndexButton.setText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.removeIndexButton.text")); // NOI18N
        removeIndexButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.removeIndexButton.toolTipText")); // NOI18N
        removeIndexButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeIndexButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeIndexButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(removeIndexButton);

        copyButton.setText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.copyButton.text")); // NOI18N
        copyButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.copyButton.toolTipText")); // NOI18N
        copyButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(copyButton);

        pasteButton.setText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.pasteButton.text")); // NOI18N
        pasteButton.setToolTipText(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.pasteButton.toolTipText")); // NOI18N
        pasteButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        pasteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(pasteButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addComponent(indicesListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(indicesListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splitPane.setLeftComponent(jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
        );

        tabbedPane.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(IndicesPanel.class, "IndicesPanel.tabbedPane.AccessibleContext.accessibleName")); // NOI18N

        splitPane.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addIndexButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIndexButtonActionPerformed
        final DdsIndexDef newIndex = DdsIndexDef.Factory.newInstance("NewIndex");
        table.getIndices().getLocal().add(newIndex);
        DbNameUtils.updateAutoDbNames(newIndex);
//        if (newIndex.isAutoDbName())
//            newIndex.setDbName(newIndex.calcAutoDbName());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ArrayList<DdsIndexDef> arr = new ArrayList<DdsIndexDef>(1);
                arr.add(newIndex);
                indicesList.selectIndices(arr);
                indexMainOptionsPanel.requestFocusInWindow();
            }
        });
    }//GEN-LAST:event_addIndexButtonActionPerformed

    private void removeIndexButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeIndexButtonActionPerformed
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(IndicesPanel.class).getString("REMOVE_INDEX_CONFIRM"))) {
            return;
        }
        List<DdsIndexDef> remIndices = indicesList.getSelectedDdsIndices();
        int idx = indicesList.getSelectedIndex();
        boolean last = idx == indicesList.getModel().getSize() - 1;
        int cnt = indicesList.getModel().getSize();
        for (DdsIndexDef remIndex : remIndices) {
            table.getIndices().getLocal().remove(remIndex);
        }

        indicesList.clearSelection();
        if (remIndices.size() == 1) {
            if (last) {
                if (cnt > 1) {
                    indicesList.setSelectedIndex(idx - 1);
                }
            } else {
                indicesList.setSelectedIndex(idx);
            }
        } else {
            if (cnt > remIndices.size()) {
                indicesList.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_removeIndexButtonActionPerformed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        List<DdsIndexDef> indices = indicesList.getSelectedDdsIndices();
        List<RadixObject> objs = new ArrayList<RadixObject>(indices.size());
        for (DdsIndexDef index : indices) {
            objs.add((RadixObject) index);
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
        if (ClipboardUtils.canPaste(table.getIndices().getLocal(), resolver)) {
            final List<DdsIndexDef> oldIndices = table.getIndices().getLocal().list();
            ClipboardUtils.pasteInModalEditor(table.getIndices().getLocal(), resolver);
            final List<DdsIndexDef> newIndices = table.getIndices().getLocal().list();
            newIndices.removeAll(oldIndices);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    indicesList.selectIndices(newIndices);
                }
            });
        }
    }//GEN-LAST:event_pasteButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addIndexButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton copyButton;
    private javax.swing.JScrollPane indicesListScrollPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton pasteButton;
    private javax.swing.JButton removeIndexButton;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        indexMainOptionsPanel.update();
        indexDbOptionsPanel.update();
    }
}
