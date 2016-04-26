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

package org.radixware.kernel.designer.common.dialogs.defs.module.creation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ModuleCreatureWizardStep1Panel extends javax.swing.JPanel {

    private final ButtonGroup group = new ButtonGroup();
    private JButton ovrBtn;
    private Segment context;
    private Module overwritten;
    private ModuleCreatureWizardStep1 step;
    private StateManager ovrModuleManager = new StateManager(this);

    /**
     * Creates new form ModuleSetupStep1Visual
     */
    public ModuleCreatureWizardStep1Panel() {
        initComponents();
        newCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (group) {
                    if (newCheck.isSelected()) {
                        ovrBtn.setEnabled(false);
                        if (step != null) {
                            step.creature.setOverwrite(false);
                            step.fireChange();
                        }
                    } else {
                        ovrBtn.setEnabled(true);
                        if (step != null) {
                            step.creature.setOverwrite(true);
                            step.fireChange();
                        }
                    }

                }
            }
        });

        ovrCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (group) {
                    if (ovrCheck.isSelected()) {
                        if (step != null) {
                            ovrBtn.setEnabled(true);
                            step.creature.setOverwrite(true);
                            step.fireChange();
                        }
                    } else {
                        if (step != null) {
                            step.creature.setOverwrite(false);
                            step.fireChange();
                        }
                    }
                }
            }
        });

        group.add(newCheck);
        group.add(ovrCheck);
        newCheck.setSelected(true);

        ovrEditor.setValue(NbBundle.getMessage(ModuleCreatureWizardStep1Panel.class, "ErrorState"));
        ovrBtn = ovrEditor.addButton();
        ovrBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        ovrBtn.setEnabled(false);
        ovrBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (context != null) {
//                    DependencesSettings settings = new DependencesSettings((Module)null, context.getType(), context.getLayer(), false);
//                    AddDependencePanel panel = new AddDependencePanel(settings, ListSelectionModel.SINGLE_SELECTION);
//                    DependenceListItem item = panel.addDependence();
//                    if (item != null) {
//                        ovrEditor.setValue(item.getModuleName());
//                        overwritten = item.getModule();
//                        if(step != null){
//                            step.creature.setModuleForOverwrite(overwritten);
//                            step.fireChange();
//                        }
//                    }

                    final Layer layer = context.getLayer();
                    final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(layer, new VisitorProvider() {

                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            if (radixObject instanceof Module) {
                                final Module module = (Module) radixObject;
                                if (module.getSegmentType() != context.getType()) { // in another type of segment
                                    return false;
                                }
                                if (module.getSegment() == context) { // in current segment
                                    return false;
                                }
                                if (context.getModules().findById(module.getId()) != null) { // overwrite already exist
                                    return false;
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                    final Definition chosen = ChooseDefinition.chooseDefinition(cfg);
                    if (chosen != null) {
                        ovrEditor.setValue(chosen.getQualifiedName());
                        overwritten = (Module) chosen;
                        if (step != null) {
                            step.creature.setModuleForOverwrite(overwritten);
                            step.fireChange();
                        }
                    }
                }
            }
        });

        add(new StateDisplayer());
    }

    public void open(ModuleCreatureWizardStep1 step) {
        this.step = step;
        this.context = step.creature.getSegment();

        if (context.getLayer().getBaseLayerURIs().isEmpty()) {
            ovrCheck.setEnabled(false);
            newCheck.setSelected(true);
            ovrBtn.setEnabled(false);
            ovrLabel.setEnabled(false);
            ovrEditor.setValue("");
        } else {
            if (step.creature.isOverwrite()) {
                ovrCheck.setSelected(true);
                ovrBtn.setEnabled(true);
            } else {
                newCheck.setSelected(true);
                ovrBtn.setEnabled(false);
            }
            this.overwritten = step.creature.getModuleForOverwrite();
            if (overwritten != null) {
                ovrEditor.setValue(this.overwritten.getQualifiedName());
            } else {
                ovrEditor.setValue(NbBundle.getMessage(ModuleCreatureWizardStep1Panel.class, "ErrorState"));
            }
        }
    }

    public String getSelectedInstanceType() {
        return group.getSelection().getActionCommand();
    }

    public Module getOverwrittenInstance() {
        return overwritten;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        newCheck = new javax.swing.JRadioButton();
        ovrCheck = new javax.swing.JRadioButton();
        ovrLabel = new javax.swing.JLabel();
        ovrEditor = new org.radixware.kernel.common.components.ExtendableTextField();

        newCheck.setText(org.openide.util.NbBundle.getMessage(ModuleCreatureWizardStep1Panel.class, "CreateNewModule")); // NOI18N

        ovrCheck.setText(org.openide.util.NbBundle.getMessage(ModuleCreatureWizardStep1Panel.class, "CreatePublishing")); // NOI18N

        ovrLabel.setText(org.openide.util.NbBundle.getMessage(ModuleCreatureWizardStep1Panel.class, "PublishName")); // NOI18N

        ovrEditor.setReadOnly(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newCheck)
                    .addComponent(ovrCheck)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ovrLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ovrEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ovrCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ovrLabel)
                    .addComponent(ovrEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ovrEditor, ovrLabel});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton newCheck;
    private javax.swing.JRadioButton ovrCheck;
    private org.radixware.kernel.common.components.ExtendableTextField ovrEditor;
    private javax.swing.JLabel ovrLabel;
    // End of variables declaration//GEN-END:variables

    boolean isComplete() {
        if (newCheck.isSelected()) {
            ovrModuleManager.ok();
            return true;
        } else {
            boolean res = true;
            if (overwritten == null) {
                ovrModuleManager.error(NbBundle.getMessage(ModuleCreatureWizardStep1Panel.class, "ErrorStateLabel"));
                res = false;
            } else {
                ovrModuleManager.ok();
            }
            return res;
        }
    }
}
