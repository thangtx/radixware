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
import org.openide.actions.CopyAction;
import org.openide.actions.PasteAction;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.editors.table.widgets.TriggersList;
import org.radixware.kernel.common.resources.RadixWareIcons;


class TriggersPanel extends javax.swing.JPanel implements TriggersList.EditingTriggerChangeListener, IUpdateable {

    private final DdsTableDef table;
    private final TriggersList triggersList;
    private final TriggerOptionsPanel triggerOptionsPanel;
    private boolean readOnly = false;

    /** Creates new form TriggersPanel */
    public TriggersPanel(DdsTableDef table) {
        initComponents();
        this.table = table;

        copyButton.setIcon(SystemAction.get(CopyAction.class).getIcon());
        pasteButton.setIcon(SystemAction.get(PasteAction.class).getIcon());
        addButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        removeButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());

        triggerOptionsPanel = new TriggerOptionsPanel();
        optionsPanel.setLayout(new BorderLayout());
        optionsPanel.add(new JScrollPane(createTab(triggerOptionsPanel, triggerOptionsPanel.getStateDisplayer())), BorderLayout.CENTER);

        triggersList = new TriggersList(table);
        listScrollPane.setViewportView(triggersList);
        triggersList.addEditingIndexChangeListener(this);
        if (table.getTriggers().get(EScope.ALL).size() > 0) {
            triggersList.setSelectedIndex(0);
        }

        triggerOptionsPanel.addTriggerChangeListener(new TriggerOptionsPanel.TriggerChangeListener() {

            @Override
            public void triggerChanged(DdsTriggerDef trigger) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        triggersList.updateRow(triggersList.getCurrentTriggerIdx());
                    }
                });
            }
        });

        initButton(copyButton, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "CTRL_C");
        initButton(pasteButton, KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "CTRL_V");
        initButton(addButton, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "INSERT");
        initButton(removeButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
    }

    public void removeListeners() {
        triggersList.removeListeners();
    }

    @Override
    public void editingTriggerChanged(DdsTriggerDef editingTrigger, boolean inherited) {
        triggerOptionsPanel.setTrigger(editingTrigger, inherited);
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
        InputMap inputMap = triggersList.getInputMap(JList.WHEN_FOCUSED);
        inputMap.put(keyStroke, key);
        triggersList.getActionMap().put(key, action);
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

    public void setSelectedTrigger(DdsTriggerDef trigger) {
        triggersList.setSelectedTrigger(trigger);
    }

    private void updateButtonsEnableState() {
        addButton.setEnabled(!readOnly);
        pasteButton.setEnabled(!readOnly);
        removeButton.setEnabled(!readOnly && triggersList.canRemoveSelectedTriggers());
        copyButton.setEnabled(triggersList.getSelectedIndices().length > 0);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        triggerOptionsPanel.setReadOnly(readOnly);

        updateButtonsEnableState();
//        triggersList.fireEditingTriggerChanged();
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
        jPanel2 = new javax.swing.JPanel();
        listScrollPane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        pasteButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        optionsPanel = new javax.swing.JPanel();

        splitPane.setDividerLocation(200);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        addButton.setText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.addButton.text")); // NOI18N
        addButton.setToolTipText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.addButton.toolTipText")); // NOI18N
        addButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addButton);

        removeButton.setText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.removeButton.text")); // NOI18N
        removeButton.setToolTipText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.removeButton.toolTipText")); // NOI18N
        removeButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(removeButton);

        copyButton.setText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.copyButton.text")); // NOI18N
        copyButton.setToolTipText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.copyButton.toolTipText")); // NOI18N
        copyButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });
        jPanel1.add(copyButton);

        pasteButton.setText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.pasteButton.text")); // NOI18N
        pasteButton.setToolTipText(org.openide.util.NbBundle.getMessage(TriggersPanel.class, "TriggersPanel.pasteButton.toolTipText")); // NOI18N
        pasteButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        pasteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteButtonActionPerformed(evt);
            }
        });
        jPanel1.add(pasteButton);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addComponent(listScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(listScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splitPane.setLeftComponent(jPanel2);

        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 421, Short.MAX_VALUE)
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 433, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        splitPane.setRightComponent(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        final DdsTriggerDef newTrigger = DdsTriggerDef.Factory.newInstance("NewTrigger");
        table.getTriggers().getLocal().add(newTrigger);
        DbNameUtils.updateAutoDbNames(newTrigger);
//        if (newTrigger.isAutoDbName())
//            newTrigger.setDbName(newTrigger.calcAutoDbName());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ArrayList<DdsTriggerDef> arr = new ArrayList<DdsTriggerDef>(1);
                arr.add(newTrigger);
                triggersList.selectTriggers(arr);
                triggerOptionsPanel.requestFocusInWindow();
            }
        });
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(TriggersPanel.class).getString("REMOVE_TRIGGER_CONFIRM"))) {
            return;
        }
        List<DdsTriggerDef> remTriggers = triggersList.getSelectedTriggers();
        int idx = triggersList.getSelectedIndex();
        boolean last = idx == triggersList.getModel().getSize() - 1;
        int cnt = triggersList.getModel().getSize();
        for (DdsTriggerDef remTrigger : remTriggers) {
            table.getTriggers().getLocal().remove(remTrigger);
        }

        triggersList.clearSelection();
        if (remTriggers.size() == 1) {
            if (last) {
                if (cnt > 1) {
                    triggersList.setSelectedIndex(idx - 1);
                }
            } else {
                triggersList.setSelectedIndex(idx);
            }
        } else {
            if (cnt > remTriggers.size()) {
                triggersList.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        List<DdsTriggerDef> triggers = triggersList.getSelectedTriggers();
        List<RadixObject> objs = new ArrayList<RadixObject>(triggers.size());
        for (DdsTriggerDef trigger : triggers) {
            objs.add((RadixObject) trigger);
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
        if (ClipboardUtils.canPaste(table.getTriggers().getLocal(), resolver)) {
            final List<DdsTriggerDef> oldTriggers = table.getTriggers().getLocal().list();
            ClipboardUtils.pasteInModalEditor(table.getTriggers().getLocal(), resolver);
            final List<DdsTriggerDef> newTriggers = table.getTriggers().getLocal().list();
            newTriggers.removeAll(oldTriggers);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    triggersList.selectTriggers(newTriggers);
                }
            });
        }
    }//GEN-LAST:event_pasteButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton copyButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane listScrollPane;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JButton pasteButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables

    public void open(OpenInfo openInfo) {
        triggerOptionsPanel.open(openInfo);
    }

    @Override
    public void update() {
        triggerOptionsPanel.update();
    }
}
