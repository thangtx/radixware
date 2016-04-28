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

package org.radixeare.kernel.designer.ads.build.release;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.builder.release.ReleaseSettings;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;


class ChooseExplicitModulesPanel extends javax.swing.JPanel {

    private final ReleaseSettings settings;
    private AdsModuleListModel model = new AdsModuleListModel();
    private final List<AdsModule> listToStoreModules;

    /** Creates new form ChooseExplicitModulesPanel */
    public ChooseExplicitModulesPanel(ReleaseSettings settings, List<AdsModule> listToStoreModules) {
        initComponents();
        this.settings = settings;
        this.listToStoreModules = listToStoreModules;
        lstModules.setModel(model);
        lstModules.setCellRenderer(new ModuleListCellRenderer(lstModules));
        btAddModule.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addPatchModule(null);
            }
        });
        btRemoveModule.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removePatchModule();
            }
        });
    }
    ChangeSupport pcs = new ChangeSupport(this);

    public void addChangeListener(ChangeListener listener) {
        pcs.addChangeListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstModules = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        btRemoveModule = new javax.swing.JButton();
        btAddModule = new javax.swing.JButton();

        jScrollPane1.setOpaque(false);

        lstModules.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstModules.setOpaque(false);
        lstModules.setVisibleRowCount(1);
        jScrollPane1.setViewportView(lstModules);

        btRemoveModule.setText(org.openide.util.NbBundle.getMessage(ChooseExplicitModulesPanel.class, "ChooseExplicitModulesPanel.btRemoveModule.text_1")); // NOI18N

        btAddModule.setText(org.openide.util.NbBundle.getMessage(ChooseExplicitModulesPanel.class, "ChooseExplicitModulesPanel.btAddModule.text_1")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btAddModule, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
            .addComponent(btRemoveModule, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btAddModule)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemoveModule)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddModule;
    private javax.swing.JButton btRemoveModule;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstModules;
    // End of variables declaration//GEN-END:variables

    private class ChooseAdsModuleCfg extends ChooseDefinitionCfg {

        public ChooseAdsModuleCfg() {
            super(settings.getBranch(), new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof AdsModule && !model.contains((AdsModule) radixObject);
                }
            });
        }
    }

    private void addPatchModule(AdsModule m) {

        List<Definition> choosen = m == null ? ChooseDefinition.chooseDefinitions(new ChooseAdsModuleCfg()) : Collections.singletonList((Definition) m);
        if (choosen == null) {
            return;
        }
        for (Definition d : choosen) {
            model.add((AdsModule) d);
        }
        sync();
        updateState();
    }

    private void removePatchModule() {
        AdsModule selected = getSelectedModule();
        if (selected != null) {
            model.remove(selected);
            sync();
            updateState();
        }
    }

    private void sync() {
        listToStoreModules.clear();
        listToStoreModules.addAll(model.list());
    }

    private AdsModule getSelectedModule() {
        int index = lstModules.getSelectedIndex();
        if (index >= 0 && index < model.getSize()) {
            return model.get(index);
        } else {
            return null;
        }
    }

    private void updateState() {
        if (settings.isPatchRelease()) {
            btAddModule.setEnabled(true);

            lstModules.setEnabled(true);
            int selectedIndex = lstModules.getSelectedIndex();

            if (selectedIndex < 0 || selectedIndex >= model.getSize()) {
                selectedIndex = model.getSize() - 1;
            }
            lstModules.setSelectedIndex(selectedIndex);
            btRemoveModule.setEnabled(getSelectedModule() != null);
        } else {
            btAddModule.setEnabled(false);
            btRemoveModule.setEnabled(false);
            lstModules.setEnabled(false);
        }
        pcs.fireChange();
    }
}