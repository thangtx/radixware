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

package org.radixware.kernel.designer.dds.editors;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeListener;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef.ReliesOnTableInfo;
import org.radixware.kernel.common.defs.dds.DdsPurityLevel;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionList;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListCfg;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionEditPanel;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.editors.htmlname.DdsFunctionProfile;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.radixware.kernel.designer.dds.editors.function.DdsFunctionSqmlEditor;

/**
 * DDS Function editor.
 *
 */
public class DdsFunctionEditor extends RadixObjectEditor<DdsFunctionDef> {

    private volatile boolean updating = false;
    private final SqmlEditorPanel sqmlEditorPanel;
    private final DdsFunctionProfile profile;
    private JButton profileBtn;
    private JButton chooseTablesBtn;    

    /**
     * Creates new form DdsFunctionEditor
     */
    public DdsFunctionEditor(DdsFunctionDef definition) {
        super(definition);
        initComponents();

        sqmlEditorPanel = new DdsFunctionSqmlEditor();
        panel.add(sqmlEditorPanel, BorderLayout.CENTER);

        profile = new DdsFunctionProfile(definition);

        profileEditor.setReadOnly(true);
        profileBtn = profileEditor.addButton();
        profileBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        profileBtn.setToolTipText(NbBundle.getBundle(DdsFunctionEditor.class).getString("EDIT_PROTOTYPE"));
        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DdsFunctionProfilePanel panel = new DdsFunctionProfilePanel();
                panel.editProfile(getRadixObject());
                profileEditor.setValue(profile.getName());
            }
        });

        reliesOnExtTextField.setReadOnly(true);
        chooseTablesBtn = reliesOnExtTextField.addButton();
        chooseTablesBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        chooseTablesBtn.setToolTipText(NbBundle.getBundle(DdsFunctionEditor.class).getString("CHOOSE_TABLES"));
        chooseTablesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseTablesActionPerformed();
            }
        });

        descriptionEditor.setReadonly(definition.isReadOnly());

        chIsDeprecated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    getFunction().setDeprecated(chIsDeprecated.isSelected());
                }
            }
        });
    }

    private void chooseTablesActionPerformed() {
        VisitorProvider provider = DdsVisitorProviderFactory.newTableForFunctionProvider();
        final ArrayList<Id> tablesIds = new ArrayList<Id>();
        DdsFunctionDef func = getFunction();
        for (ReliesOnTableInfo info : func.getReliesOnTablesInfo()) {
            tablesIds.add(info.getTableId());
        }
        ConfigureDefinitionListCfg cfg = ConfigureDefinitionListCfg.Factory.newInstance(func, provider);
        if (ConfigureDefinitionList.configure(tablesIds, cfg)) {
            func.getReliesOnTablesInfo().clear();
            for (Id id : tablesIds) {
                func.getReliesOnTablesInfo().add(id);
            }
            updateTablesList();
        }
    }

    private void updateTablesList() {
        StringBuilder str = new StringBuilder();
        DdsFunctionDef func = getFunction();
        for (ReliesOnTableInfo info : func.getReliesOnTablesInfo()) {
            DdsTableDef table = info.findTable();
            if (table != null) {
                str.append(table.getName() + ", ");
            }
        }
        if (str.length() >= 2) {
            str.delete(str.length() - 2, str.length());
        }
        updating = true;
        reliesOnExtTextField.setValue(str.toString());
        boolean isCacheResult = func.isCachedResult();
        cacheResultCheckBox.setSelected(isCacheResult);
        reliesOnLabel.setVisible(isCacheResult);
        reliesOnExtTextField.setVisible(isCacheResult);
        updating = false;
    }

    private void setupInitialValues() {
        updating = true;

        DdsFunctionDef func = getFunction();
        profileEditor.setValue(profile.getName());
        descriptionEditor.open(func);
        publicCheckBox.setSelected(func.isPublic());
        deterministicCheckBox.setSelected(func.isDeterministic());
        generateInDbCheckBox.setSelected(func.isGeneratedInDb());

        updateTablesList();

        DdsPurityLevel level = func.getPurityLevel();
        purityLevelEditor.setPurityLevel(level);
//        wndsCheckBox.setSelected(level.isWNDS());
//        rndsCheckBox.setSelected(level.isRNDS());
//        wnpsCheckBox.setSelected(level.isWNPS());
//        rnpsCheckBox.setSelected(level.isRNPS());
//        trustCheckBox.setSelected(level.isTrust());
//        updateAuto();

        sqmlEditorPanel.open(func.getBody());
        chIsDeprecated.setSelected(func.isDeprecated());
        updateEnableState();
        updating = false;
    }

    private void updateEnableState() {
        publicCheckBox.setEnabled(!getFunction().isReadOnly());
        deterministicCheckBox.setEnabled(!getFunction().isReadOnly());
        generateInDbCheckBox.setEnabled(!getFunction().isReadOnly());
        cacheResultCheckBox.setEnabled(!getFunction().isReadOnly());
        chooseTablesBtn.setEnabled(!getFunction().isReadOnly());
        purityLevelEditor.setReadOnly(getFunction().isReadOnly());
//        autoCheckBox.setEnabled(editable);
//        wndsCheckBox.setEnabled(editable);
//        rndsCheckBox.setEnabled(editable);
//        wnpsCheckBox.setEnabled(editable);
//        rnpsCheckBox.setEnabled(editable);
//        trustCheckBox.setEnabled(editable);
        sqmlEditorPanel.setEditable(!getFunction().isReadOnly());
        chIsDeprecated.setEnabled(!getFunction().isReadOnly());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        profileEditor = new org.radixware.kernel.common.components.ExtendableTextField();
        publicCheckBox = new javax.swing.JCheckBox();
        deterministicCheckBox = new javax.swing.JCheckBox();
        cacheResultCheckBox = new javax.swing.JCheckBox();
        reliesOnLabel = new javax.swing.JLabel();
        reliesOnExtTextField = new org.radixware.kernel.common.components.ExtendableTextField();
        purityLevelEditor = new org.radixware.kernel.designer.dds.editors.PurityLevelEditor();
        panel = new javax.swing.JPanel();
        generateInDbCheckBox = new javax.swing.JCheckBox();
        chIsDeprecated = new javax.swing.JCheckBox();
        descriptionEditor = new org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DdsFunctionEditor.class, "DdsFunctionEditor.jLabel1.text")); // NOI18N

        publicCheckBox.setText(org.openide.util.NbBundle.getMessage(DdsFunctionEditor.class, "DdsFunctionEditor.publicCheckBox.text")); // NOI18N
        publicCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                publicCheckBoxItemStateChanged(evt);
            }
        });

        deterministicCheckBox.setText(org.openide.util.NbBundle.getMessage(DdsFunctionEditor.class, "DdsFunctionEditor.deterministicCheckBox.text")); // NOI18N
        deterministicCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                deterministicCheckBoxItemStateChanged(evt);
            }
        });

        cacheResultCheckBox.setText(org.openide.util.NbBundle.getMessage(DdsFunctionEditor.class, "DdsFunctionEditor.cacheResultCheckBox.text")); // NOI18N
        cacheResultCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cacheResultCheckBoxItemStateChanged(evt);
            }
        });

        reliesOnLabel.setText(org.openide.util.NbBundle.getMessage(DdsFunctionEditor.class, "DdsFunctionEditor.reliesOnLabel.text")); // NOI18N

        panel.setLayout(new java.awt.BorderLayout());

        generateInDbCheckBox.setText(org.openide.util.NbBundle.getMessage(DdsFunctionEditor.class, "DdsFunctionEditor.generateInDbCheckBox.text")); // NOI18N
        generateInDbCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                generateInDbCheckBoxItemStateChanged(evt);
            }
        });

        chIsDeprecated.setText(org.openide.util.NbBundle.getMessage(DdsFunctionEditor.class, "DdsFunctionEditor.chIsDeprecated.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purityLevelEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(140, 140, 140)
                        .addComponent(profileEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chIsDeprecated))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(reliesOnLabel)
                        .addGap(13, 13, 13)
                        .addComponent(reliesOnExtTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(publicCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(deterministicCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(cacheResultCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(generateInDbCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(profileEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chIsDeprecated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(publicCheckBox)
                    .addComponent(deterministicCheckBox)
                    .addComponent(cacheResultCheckBox)
                    .addComponent(generateInDbCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reliesOnLabel)
                    .addComponent(reliesOnExtTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(purityLevelEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void publicCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_publicCheckBoxItemStateChanged
        if (!updating) {
            getFunction().setPublic(publicCheckBox.isSelected());
        }
    }//GEN-LAST:event_publicCheckBoxItemStateChanged

    private void deterministicCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_deterministicCheckBoxItemStateChanged
        if (!updating) {
            getFunction().setDeterministic(deterministicCheckBox.isSelected());
        }
    }//GEN-LAST:event_deterministicCheckBoxItemStateChanged

    private void cacheResultCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cacheResultCheckBoxItemStateChanged
        if (!updating) {
            getFunction().setCachedResult(cacheResultCheckBox.isSelected());
            updateTablesList();
        }
    }//GEN-LAST:event_cacheResultCheckBoxItemStateChanged

    private void generateInDbCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_generateInDbCheckBoxItemStateChanged
        if (!updating) {
            getFunction().setGeneratedInDb(generateInDbCheckBox.isSelected());
        }
    }//GEN-LAST:event_generateInDbCheckBoxItemStateChanged

    protected DdsFunctionDef getFunction() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        update();
        sqmlEditorPanel.open(sqmlEditorPanel.getSource(), openInfo);
        return super.open(openInfo);
    }

    @Override
    public void update() {
        // TODO: update editor when focused
        sqmlEditorPanel.update();
        setupInitialValues();
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DdsFunctionDef> {

        @Override
        public RadixObjectEditor newInstance(DdsFunctionDef ddsFunction) {
            return new DdsFunctionEditor(ddsFunction);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cacheResultCheckBox;
    private javax.swing.JCheckBox chIsDeprecated;
    private org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor descriptionEditor;
    private javax.swing.JCheckBox deterministicCheckBox;
    private javax.swing.JCheckBox generateInDbCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panel;
    private org.radixware.kernel.common.components.ExtendableTextField profileEditor;
    private javax.swing.JCheckBox publicCheckBox;
    private org.radixware.kernel.designer.dds.editors.PurityLevelEditor purityLevelEditor;
    private org.radixware.kernel.common.components.ExtendableTextField reliesOnExtTextField;
    private javax.swing.JLabel reliesOnLabel;
    // End of variables declaration//GEN-END:variables
}
