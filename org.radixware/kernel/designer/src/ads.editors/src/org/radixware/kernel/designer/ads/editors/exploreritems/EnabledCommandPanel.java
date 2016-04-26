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

package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.lookup.AdsCommandsLookupSupport;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class EnabledCommandPanel extends javax.swing.JPanel {

    private Restrictions restrictions;
    private AdsDefinition adscontext;
    private AdsCommandsLookupSupport context;
    private Collection<AdsCommandDef> chooseableCommands;
    private boolean readonly = false;

    /** Creates new form EnabledCommandPanel */
    public EnabledCommandPanel() {
        initComponents();
        addBtn.setToolTipText(NbBundle.getMessage(EnabledCommandPanel.class, "SelectorItem-CommandsAddHint"));
        removeBtn.setToolTipText(NbBundle.getMessage(EnabledCommandPanel.class, "SelectorItem-CommandsRemoveHint"));
        clearBtn.setToolTipText(NbBundle.getMessage(EnabledCommandPanel.class, "SelectorItem-CommandsClearHint"));
        setupListeners();
        commandsList.setCellRenderer(new CommandsRenderer());
    }

    private void setupListeners() {
        ActionListener addListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(EnabledCommandPanel.this.chooseableCommands);
                List<Definition> chosen = ChooseDefinition.chooseDefinitions(cfg);
                if (chosen != null && !chosen.isEmpty()) {
                    CommandsListModel model = (CommandsListModel) EnabledCommandPanel.this.commandsList.getModel();
                    for (Definition d : chosen) {
                        if (EnabledCommandPanel.this.restrictions.setCommandEnabled(d.getId(), true)) {
                            model.addCommand(new CommandItem(d.getId(), (AdsCommandDef) d));
                        }
                    }
                }
                EnabledCommandPanel.this.updateChooseableCommands();
                EnabledCommandPanel.this.onFocusEvent();
            }
        };
        addBtn.addActionListener(addListener);

        ActionListener removeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] selected = commandsList.getSelectedValues();
                CommandsListModel model = (CommandsListModel) commandsList.getModel();
                Set<CommandItem> toRemove = new HashSet<CommandItem>();
                for (Object s : selected) {
                    CommandItem item = (CommandItem) s;
                    if (restrictions.setCommandEnabled(item.id, false)) {
                        toRemove.add(item);
                    }
                }
                model.removeCommands(toRemove);
                EnabledCommandPanel.this.updateChooseableCommands();
                EnabledCommandPanel.this.onFocusEvent();
            }
        };
        removeBtn.addActionListener(removeListener);

        ActionListener clearListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CommandsListModel model = (CommandsListModel) commandsList.getModel();
                Set<CommandItem> toRemove = new HashSet<CommandItem>();
                for (int i = 0; i <= model.getSize() - 1; i++) {
                    CommandItem item = (CommandItem) model.getElementAt(i);
                    if (restrictions.setCommandEnabled(item.id, false)) {
                        toRemove.add(item);
                    }
                }
                model.removeCommands(toRemove);
                EnabledCommandPanel.this.updateChooseableCommands();
                EnabledCommandPanel.this.onFocusEvent();
            }
        };
        clearBtn.addActionListener(clearListener);

        FocusListener listFocusListener = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                EnabledCommandPanel.this.onFocusEvent();
            }

            @Override
            public void focusLost(FocusEvent e) {
                EnabledCommandPanel.this.onFocusEvent();
            }
        };
        commandsList.addFocusListener(listFocusListener);

        ListSelectionListener selectionListener = new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                EnabledCommandPanel.this.onFocusEvent();
            }
        };
        commandsList.addListSelectionListener(selectionListener);

        ActionListener upListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] selected = commandsList.getSelectedValues();
                int[] indexes = commandsList.getSelectedIndices();
                CommandsListModel model = (CommandsListModel) commandsList.getModel();

                model.moveUp(selected, indexes);

                if (selected.length > 1) {
                    for (int i = 0, s = selected.length - 1; i <= s; i++) {
                        int oldindex = indexes[i];
                        oldindex = oldindex - 1;
                        restrictions.getCommandsOrder().moveUp(((CommandItem) selected[i]).id);
                    }
                    commandsList.setSelectedIndices(indexes);
                } else {
                    restrictions.getCommandsOrder().moveUp(((CommandItem) selected[0]).id);
                    commandsList.setSelectedValue(selected[0], true);
                }

                EnabledCommandPanel.this.onFocusEvent();
            }
        };
        upBtn.addActionListener(upListener);

        ActionListener downListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] selected = commandsList.getSelectedValues();
                int[] indexes = commandsList.getSelectedIndices();
                CommandsListModel model = (CommandsListModel) commandsList.getModel();

                model.moveDown(selected, indexes);

                if (selected.length > 1) {
                    for (int i = 0, s = selected.length - 1; i <= s; i++) {
                        int oldindex = indexes[i];
                        oldindex = oldindex + 1;
                        restrictions.getCommandsOrder().moveDn(((CommandItem) selected[i]).id);
                    }
                    commandsList.setSelectedIndices(indexes);
                } else {
                    restrictions.getCommandsOrder().moveDn(((CommandItem) selected[0]).id);
                    commandsList.setSelectedValue(selected[0], true);
                }

                EnabledCommandPanel.this.onFocusEvent();
            }
        };
        downBtn.addActionListener(downListener);
    }

    private void onFocusEvent() {
        if (chooseableCommands.isEmpty()) {
            addBtn.setEnabled(false);
        } else {
            addBtn.setEnabled(!readonly);
        }
        if (commandsList.getModel().getSize() != 0) {
            clearBtn.setEnabled(!readonly);
            int selection = commandsList.getSelectedIndex();
            int size = commandsList.getModel().getSize();
            if (selection > -1 &&
                    selection <= size) {
                removeBtn.setEnabled(!readonly);
                int[] allSelected = commandsList.getSelectedIndices();
                if (allSelected.length == 1) {
                    upBtn.setEnabled(!readonly && selection > 0);
                    downBtn.setEnabled(!readonly && selection < (size - 1));
                } else {
                    upBtn.setEnabled(!readonly && selection > 0);
                    downBtn.setEnabled(!readonly && allSelected[allSelected.length - 1] < (size - 1));
                }
            } else {
                removeBtn.setEnabled(false);
                upBtn.setEnabled(false);
                downBtn.setEnabled(false);
            }
        } else {
            removeBtn.setEnabled(false);
            clearBtn.setEnabled(false);
            upBtn.setEnabled(false);
            downBtn.setEnabled(false);
        }
    }

    public void open(final AdsDefinition adscontext, Restrictions restrictions, AdsCommandsLookupSupport context, boolean readonly) {
        this.adscontext = adscontext;
        this.restrictions = restrictions;
        this.context = context;
        this.readonly = readonly;
        update();
    }

    public void setResctrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    private void updateChooseableCommands() {
        chooseableCommands = new HashSet<AdsCommandDef>();
        List<Id> enabledIds = restrictions.getEnabledCommandIds();
        Collection<AdsCommandDef> all = context.getAvailableCommandList();
        for (AdsCommandDef c : all) {
            if (restrictions.canEnableCommand(c.getId()) &&
                    !enabledIds.contains(c.getId())) {
                chooseableCommands.add(c);
            }
        }
    }

    public void update() {
        commandsList.setEnabled(!readonly);
        if (restrictions != null && restrictions.getEnabledCommandIds() != null) {
            chooseableCommands = new HashSet<AdsCommandDef>();
            List<Id> enabledIds = restrictions.getEnabledCommandIds();
            Collection<AdsCommandDef> all = context.getAvailableCommandList();
            for (AdsCommandDef c : all) {
                if (restrictions.canEnableCommand(c.getId()) &&
                        !enabledIds.contains(c.getId())) {
                    chooseableCommands.add(c);
                }
            }
            CommandsListModel model = new CommandsListModel(enabledIds, context);
            commandsList.setModel(model);
            onFocusEvent();
        } else {
            commandsList.setModel(new CommandsListModel(new ArrayList<Id>(), context));
            commandsList.setEnabled(false);
            addBtn.setEnabled(false);
            removeBtn.setEnabled(false);
            clearBtn.setEnabled(false);
            upBtn.setEnabled(false);
            downBtn.setEnabled(false);
        }
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public void setEnabled(boolean enabled) {
        commandsList.setEnabled(enabled);
        this.readonly = !enabled;
        onFocusEvent();
        super.setEnabled(enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        commandsList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        addBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        upBtn = new javax.swing.JButton();
        downBtn = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(commandsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        addBtn.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addBtn.setText(org.openide.util.NbBundle.getMessage(EnabledCommandPanel.class, "UsedContextlessCommandsBtns-Add")); // NOI18N
        addBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeBtn.setIcon(RadixWareIcons.DELETE.DELETE.getIcon(13, 13));
        removeBtn.setText(org.openide.util.NbBundle.getMessage(EnabledCommandPanel.class, "UsedContextlessCommandsBtns-Remove")); // NOI18N
        removeBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        clearBtn.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon(13, 13));
        clearBtn.setText(org.openide.util.NbBundle.getMessage(EnabledCommandPanel.class, "UsedContextlessCommandsBtns-Clear")); // NOI18N
        clearBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        upBtn.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon(13, 13));
        upBtn.setText(org.openide.util.NbBundle.getMessage(EnabledCommandPanel.class, "EnabledCommandPanel-UpBtn")); // NOI18N
        upBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        downBtn.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(13, 13));
        downBtn.setText(org.openide.util.NbBundle.getMessage(EnabledCommandPanel.class, "EnabledCommandPanel-DwnBtn")); // NOI18N
        downBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addBtn)
            .addComponent(removeBtn)
            .addComponent(clearBtn)
            .addComponent(upBtn)
            .addComponent(downBtn)
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addBtn, clearBtn, downBtn, removeBtn, upBtn});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(addBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JList commandsList;
    private javax.swing.JButton downBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton upBtn;
    // End of variables declaration//GEN-END:variables

    private class CommandsRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value.getClass().equals(CommandItem.class)) {
                CommandItem item = (CommandItem) value;
                setText(item.toString());
                if (item.toString().equals(item.getIdStr())) {
                    setForeground(Color.RED);
                }
                if (item.command != null) {
                    Id iconid = item.command.getPresentation().getIconId();
                    if (iconid != null) {
                        AdsImageDef info = AdsSearcher.Factory.newImageSearcher(adscontext).findById(iconid).get();
                        if (info != null) {
                            setIcon(info.getIcon().getIcon(16, 16));
                        } else {
                            setIcon(RadixObjectIcon.UNKNOWN.getIcon(16, 16));
                        }
                    } 
                } else {
                    setIcon(RadixObjectIcon.UNKNOWN.getIcon(16, 16));
                }
            } else {
                setText("<indefinded list item type>");
                setForeground(Color.RED);
            }
            return this;
        }
    }
}
