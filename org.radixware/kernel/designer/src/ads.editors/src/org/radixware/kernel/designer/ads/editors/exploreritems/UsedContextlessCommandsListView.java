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
 * UsedContextlessCommandsListView.java
 *
 * Created on 28.07.2009, 11:13:44
 */
package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.CommonRadixObjectPopupMenu;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class UsedContextlessCommandsListView extends javax.swing.JPanel
        implements CommonRadixObjectPopupMenu.IRadixObjectPopupMenuOwner {

    /** Creates new form UsedContextlessCommandsListView */
    public UsedContextlessCommandsListView() {
        initComponents();
        CommonRadixObjectPopupMenu menu = new CommonRadixObjectPopupMenu(this);
        list.setComponentPopupMenu(menu);
        list.setCellRenderer(new AbstractItemRenderer(list) {

            @Override
            public String getObjectName(Object object) {
                if (object != null) {
                    if (object instanceof Id) {
                        AdsDefinition prop = AdsSearcher.Factory.newAdsContextlessCommandSearcher(((AdsDefinition) context).getModule()).findById((Id) object).get();
                        if (prop != null) {
                            return prop.getName();
                        } else {
                            return "<Unknowkn: " + ((Id) object).toString() + ">";
                        }
                    }
                }
                return "<Not Defined>";
            }

            @Override
            public String getObjectLocation(Object object) {
                return "";
            }

            @Override
            public RadixIcon getObjectIcon(Object object) {
                if (object != null) {
                    if (object instanceof Id) {
                        AdsDefinition prop = AdsSearcher.Factory.newAdsContextlessCommandSearcher(((AdsDefinition) context).getModule()).findById((Id) object).get();
                        if (prop != null) {
                            return prop.getIcon();
                        }
                    }
                }
                return RadixObjectIcon.UNKNOWN;
            }

            @Override
            public RadixIcon getObjectLocationIcon(Object object) {
                return null;
            }
        });
        DefaultListModel model = new DefaultListModel();
        list.setModel(model);

        ActionListener addListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Definition currentDef = usage.getOwnerDefinition();

                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(currentDef, new UnusedCommandsProvider());
                cfg.setTypeTitle(NbBundle.getMessage(UsedContextlessCommandsListView.class, "UsedContextlessCommands-SearchTitle"));
                List<Definition> def = ChooseDefinition.chooseDefinitions(cfg);
                if (def != null && def.size() > 0) {
                    for (Definition d : def) {
                        usage.addUsedCommand((AdsContextlessCommandDef) d);

                        AdsModule newCommandOwner = (AdsModule) d.getModule();
                        boolean contains = currentDef.getModule().getDependences().contains(newCommandOwner);
                        if (!contains && !newCommandOwner.equals(currentDef.getModule())) {
                            currentDef.getModule().getDependences().add(newCommandOwner);
                        }
                        
                        //((DefaultListModel) list.getModel()).addElement(d);
                        UsedContextlessCommandsListView.this.update();
                    }
                    list.setSelectedValue((AdsContextlessCommandDef) def.get(def.size() - 1), true);
                }
            }
        };
        addButton.addActionListener(addListener);

        ActionListener removeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Id selected = (Id) list.getSelectedValue();
                int index = list.getSelectedIndex();
                int size = list.getModel().getSize();
                if (selected != null) {
                    usage.removeUsedCommandId(selected);
                } 
                ((DefaultListModel) list.getModel()).removeElement(selected);
                if (size > 0) {
                    list.setSelectedIndex(index == 0 ? index : index - 1);
                }
            }
        };
        removeButton.addActionListener(removeListener);

        ActionListener clearListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                List<ContextlessCommandUsage.CommandInfo> infos = usage.getCommandInfos();
                for (ContextlessCommandUsage.CommandInfo i : infos) {
                    usage.removeUsedCommandId(i.commmandId);
                }
                ((DefaultListModel) list.getModel()).clear();
                onFocusEvent();
            }
        };
        clearButton.addActionListener(clearListener);

        ListSelectionListener selectionListener = new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                onFocusEvent();
            }
        };
        list.addListSelectionListener(selectionListener);
    }

    private void onFocusEvent() {
        if (usage != null) {
            boolean readonly = usage.isReadOnly();
            int idsCount = usage.getCommandIds().size();
            clearButton.setEnabled(idsCount > 0 && !readonly);
            addButton.setEnabled(!readonly);
            removeButton.setEnabled(!list.isSelectionEmpty() && !readonly);
        } else {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    }
    private ContextlessCommandUsage.IContextlessCommandsUser context;
    private ContextlessCommandUsage usage;

    public void open(ContextlessCommandUsage.IContextlessCommandsUser definition) {
        this.context = definition;
        update();
    }

    public void update() {
        this.usage = context.getUsedContextlessCommands();

        List<ContextlessCommandUsage.CommandInfo> infos = usage.getCommandInfos();
        DefaultListModel model = new DefaultListModel();
        for (ContextlessCommandUsage.CommandInfo i : infos) {
            model.addElement(i.commmandId);
        }
        list.setModel(model);
        list.setEnabled(!usage.isReadOnly());
        onFocusEvent();
    }

    @Override
    public boolean isPopupMenuAvailable() {
        return !list.isSelectionEmpty();
    }

    @Override
    public RadixObject getSelectedRadixObject() {
        Object selection = list.getSelectedValue();
        if (selection != null) {
            assert (selection instanceof RadixObject);
            return (RadixObject) selection;
        }
        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        btnsPanel = new javax.swing.JPanel();
        clearButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();

        jScrollPane1.setViewportView(list);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(UsedContextlessCommandsListView.class, "UsedContextlessCommands")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );

        clearButton.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon(13, 13));
        clearButton.setText(org.openide.util.NbBundle.getMessage(UsedContextlessCommandsListView.class, "UsedContextlessCommandsBtns-Clear")); // NOI18N
        clearButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon(13, 13));
        removeButton.setText(org.openide.util.NbBundle.getMessage(UsedContextlessCommandsListView.class, "UsedContextlessCommandsBtns-Remove")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        addButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addButton.setText(org.openide.util.NbBundle.getMessage(UsedContextlessCommandsListView.class, "UsedContextlessCommandsBtns-Add")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout btnsPanelLayout = new javax.swing.GroupLayout(btnsPanel);
        btnsPanel.setLayout(btnsPanelLayout);
        btnsPanelLayout.setHorizontalGroup(
            btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnsPanelLayout.createSequentialGroup()
                .addGroup(btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addContainerGap())
        );
        btnsPanelLayout.setVerticalGroup(
            btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnsPanelLayout.createSequentialGroup()
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel btnsPanel;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables

    private class UnusedCommandsProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (radixObject instanceof AdsContextlessCommandDef) {
                Id id = ((AdsContextlessCommandDef) radixObject).getId();
                return !usage.getCommandIds().contains(id);
            }
            return false;
        }

        @Override
        public boolean isContainer(RadixObject radixObject) {
            return true;
        }
    }
}
