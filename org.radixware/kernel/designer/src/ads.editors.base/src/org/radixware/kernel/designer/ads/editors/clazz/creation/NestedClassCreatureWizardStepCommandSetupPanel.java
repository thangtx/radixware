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

package org.radixware.kernel.designer.ads.editors.clazz.creation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class NestedClassCreatureWizardStepCommandSetupPanel extends javax.swing.JPanel implements ChangeListener {

    /**
     * Creates new form NestedClassCreatureWizardStepCommandSetupPanel
     */
    private String defaultName;
    private AdsCommandDef command;
    private AdsModelClassDef containerModel;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private StateManager sm = new StateManager(this);

    public NestedClassCreatureWizardStepCommandSetupPanel() {
        initComponents();
        nameEditor.addChangeListener(this);
        commandLink.addChangeListener(this);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void fireChange() {
        changeSupport.fireChange();
    }

    public boolean hasListeners() {
        return changeSupport.hasListeners();
    }

    public AdsCommandDef getCommand() {
        return command;
    }

    public String getClassName() {
        return nameEditor.getCurrentName();
    }

    public void open(String name, final AdsModelClassDef containerModel, AdsCommandDef command, String defaultName) {
        this.command = command;
        this.containerModel = containerModel;
        this.defaultName = defaultName;
        this.nameEditor.setCurrentName(name == null || name.isEmpty() ? defaultName : name);

        commandLink.open(ChooseDefinitionCfg.Factory.newInstance(containerModel, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsCommandDef) {
                    AdsCommandDef command = (AdsCommandDef) radixObject;

                    if (command instanceof AdsScopeCommandDef) {
                        AdsScopeCommandDef scopeCommand = (AdsScopeCommandDef) command;
                        AdsClassDef clazz = scopeCommand.getOwnerClass();
                        if (clazz == null) {
                            return false;
                        }
                        AdsClassDef contextClazz = containerModel.findServerSideClasDef();
                        if (contextClazz != clazz && !contextClazz.getInheritance().isSubclassOf(clazz) && !DefinitionsUtils.isOverridesOrOverwrites(contextClazz, clazz)) {
                            return false;
                        }
                    }

                    Id handlerId = Id.Factory.changePrefix(command.getId(), EDefinitionIdPrefix.ADS_COMMAND_MODEL_CLASS);
                    if (containerModel.getNestedClasses().findById(handlerId, ExtendableDefinitions.EScope.LOCAL).isEmpty()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }), command, command == null ? null : command.getId());
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        String checkName = null;
        command = (AdsCommandDef) commandLink.getDefinition();

        if (command != null) {
            checkName = command.getName();
        }

        changeSupport.fireChange();
        if (e.getSource() != nameEditor) {
            if (checkName != null) {
                checkName(checkName);
            }
        }
    }

    private void checkName(final String check) {
        String name = nameEditor.getCurrentName();
        if (name == null || name.isEmpty() || name.equals(defaultName)) {
            defaultName = check;
            nameEditor.removeChangeListener(this);
            nameEditor.setCurrentName(check);
            nameEditor.addChangeListener(this);
        }
    }

    public boolean isComplete() {
        boolean res = nameEditor.isComplete();

        if (res) {
            if (command == null) {
                sm.error("Command should be specified");
                res = false;
            } else {
                sm.ok();
            }
            return res;
        }
        return res;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        commandLink = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nameEditor = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(NestedClassCreatureWizardStepCommandSetupPanel.class, "NestedClassCreatureWizardStepCommandSetupPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(NestedClassCreatureWizardStepCommandSetupPanel.class, "NestedClassCreatureWizardStepCommandSetupPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commandLink, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                    .addComponent(nameEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(commandLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel commandLink;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel nameEditor;
    // End of variables declaration//GEN-END:variables
}
