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
package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject.Prop;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDwfFormClerkAutoSelect;
import org.radixware.kernel.common.enums.EDwfFormPriority;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class FormCommonPanel extends EditorDialog.EditorPanel<AdsAppObject> {

    public FormCommonPanel(AdsAppObject node) {
        super(node);
        initComponents();

        final AdsAppObject.Prop t = node.getPropByName("title");
        if (t != null) {
            t.setValue((String) null);
        }
        final AdsAppObject.Prop ht = node.getPropByName("headerTitle");
        if (ht != null) {
            ht.setValue((String) null);
        }
        final AdsAppObject.Prop ft = node.getPropByName("footerTitle");
        if (ft != null) {
            ft.setValue((String) null);
        }

        clerkAutoSelect = node.getPropByName("clerkAutoSelect");
        clerk = node.getPropByName("clerk");
        captureProcess = node.getPropByName("captureProcess");
        contentSaving = node.getPropByName("contentSaving");

        title = node.getPropByName("titleStrId");
        headerTitle = node.getPropByName("headerTitleStrId");
        footerTitle = node.getPropByName("footerTitleStrId");

        priority = node.getPropByName("priority");
        clerkRoles = node.getPropByName("clerkRoleGuids");
        adminRoles = node.getPropByName("adminRoleGuids");

        forbidForProcessCreator = node.getPropByName("forbidForProcessCreator");
        forbidRedirection = node.getPropByName("forbidRedirection");

        comboClerkAutoSelect.setEditorType(ExtendableTextField.EDITOR_COMBO);
        comboClerkAutoSelect.setComboBoxModel(new javax.swing.DefaultComboBoxModel(EDwfFormClerkAutoSelect.values()));

        comboPriority.setEditorType(ExtendableTextField.EDITOR_COMBO);
        EDwfFormPriority[] priorities = EDwfFormPriority.values();
        Arrays.sort(priorities, new Comparator<EDwfFormPriority>() {
            @Override
            public int compare(EDwfFormPriority p0, EDwfFormPriority p1) {
                return p0.getValue().compareTo(p1.getValue());
            }
        });

        comboPriority.setComboBoxModel(new javax.swing.DefaultComboBoxModel(priorities));

        textClerkRoles.setEditable(false);
        textClerkRoles.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        return object instanceof AdsRoleDef;
                    }
                ;
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                List<Definition> roleDefs = ChooseDefinition.chooseDefinitions(cfg);
                if (roleDefs != null) {
                    String[] guids = new String[roleDefs.size()];
                    for (int i = 0; i < roleDefs.size(); i++) {
                        guids[i] = String.valueOf(roleDefs.get(i).getId());
                    }
                    clerkRoles.setValue(new ArrStr(guids).toString());
                } else {
                    clerkRoles.setValue((ValAsStr) null);
                }
                setRolesTitle(textClerkRoles, clerkRoles.getValue());
            }
        });

        textClerkRoles.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clerkRoles.setValue((ValAsStr) null);
                setRolesTitle(textClerkRoles, clerkRoles.getValue());
            }
        });

        textAdminRoles.setEditable(false);
        textAdminRoles.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        return object instanceof AdsRoleDef;
                    }
                ;
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                List<Definition> roleDefs = ChooseDefinition.chooseDefinitions(cfg);
                if (roleDefs != null) {
                    String[] guids = new String[roleDefs.size()];
                    for (int i = 0; i < roleDefs.size(); i++) {
                        guids[i] = String.valueOf(roleDefs.get(i).getId());
                    }
                    adminRoles.setValue(new ArrStr(guids).toString());
                } else {
                    adminRoles.setValue((ValAsStr) null);
                }
                setRolesTitle(textAdminRoles, adminRoles.getValue());
            }
        });

        textAdminRoles.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminRoles.setValue((ValAsStr) null);
                setRolesTitle(textAdminRoles, adminRoles.getValue());
            }
        });

        panelTitle.setReadonly(node.isReadOnly());
        panelHeaderTitle.setReadonly(node.isReadOnly());
        panelFooterTitle.setReadonly(node.isReadOnly());
        comboClerkAutoSelect.setEnabled(!node.isReadOnly());
        chkCaptureProcess.setEnabled(!node.isReadOnly());
        textClerk.setEnabled(!node.isReadOnly());
        comboPriority.setEnabled(!node.isReadOnly());
        textClerkRoles.setEnabled(!node.isReadOnly());
        textAdminRoles.setEnabled(!node.isReadOnly());
        cbForbidForProcessCreator.setEnabled(!node.isReadOnly());
        cbForbidRedirection.setEnabled(!node.isReadOnly());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTitle = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel(RadixResourceBundle.getMessage(getClass(), "FormCommonPanel.panelTitle.text"));
        panelHeaderTitle = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel(RadixResourceBundle.getMessage(getClass(), "FormCommonPanel.panelHeaderTitle.text"));
        panelFooterTitle = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel(RadixResourceBundle.getMessage(getClass(), "FormCommonPanel.panelFooterTitle.text"));
        panel1 = new javax.swing.JPanel();
        labelClerkAutoSelect = new javax.swing.JLabel();
        labelClerk = new javax.swing.JLabel();
        textClerk = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        comboClerkAutoSelect = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        textClerkRoles = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelWorkerRole = new javax.swing.JLabel();
        cbForbidForProcessCreator = new javax.swing.JCheckBox();
        cbForbidRedirection = new javax.swing.JCheckBox();
        panel2 = new javax.swing.JPanel();
        chkCaptureProcess = new javax.swing.JCheckBox();
        comboPriority = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        textAdminRoles = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelPriority = new javax.swing.JLabel();
        labelAdminRole = new javax.swing.JLabel();
        chkContentSaving = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 430));
        setPreferredSize(new java.awt.Dimension(500, 430));
        setRequestFocusEnabled(false);

        labelClerkAutoSelect.setText(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.labelClerkAutoSelect.text")); // NOI18N

        labelClerk.setText(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.labelClerk.text")); // NOI18N

        labelWorkerRole.setText(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.labelWorkerRole.text")); // NOI18N

        cbForbidForProcessCreator.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbForbidForProcessCreator.setLabel(org.openide.util.NbBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.cbForbidForProcessCreator.label")); // NOI18N
        cbForbidForProcessCreator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbForbidForProcessCreatorActionPerformed(evt);
            }
        });

        cbForbidRedirection.setText(org.openide.util.NbBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.cbForbidRedirection.text")); // NOI18N
        cbForbidRedirection.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        cbForbidRedirection.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelClerkAutoSelect)
                            .addComponent(labelClerk)
                            .addComponent(labelWorkerRole))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textClerk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboClerkAutoSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textClerkRoles, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGap(0, 29, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbForbidForProcessCreator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbForbidRedirection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboClerkAutoSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelClerkAutoSelect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textClerk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelClerk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textClerkRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelWorkerRole))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbForbidForProcessCreator)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbForbidRedirection))
        );

        chkCaptureProcess.setText(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.chkCaptureProcess.text")); // NOI18N
        chkCaptureProcess.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkCaptureProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCaptureProcessActionPerformed(evt);
            }
        });

        labelPriority.setText(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.labelPriority.text")); // NOI18N

        labelAdminRole.setText(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.labelAdminRole.text")); // NOI18N

        chkContentSaving.setText(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.chkContentSaving.text")); // NOI18N
        chkContentSaving.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkContentSaving.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkContentSavingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelPriority)
                            .addComponent(labelAdminRole))
                        .addGap(4, 4, 4)
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textAdminRoles, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(comboPriority, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                        .addComponent(chkContentSaving)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkCaptureProcess)))
                .addContainerGap())
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkCaptureProcess)
                    .addComponent(chkContentSaving))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPriority))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textAdminRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelAdminRole))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelHeaderTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFooterTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelHeaderTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFooterTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkCaptureProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCaptureProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCaptureProcessActionPerformed

    private void chkContentSavingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkContentSavingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkContentSavingActionPerformed

    private void cbForbidForProcessCreatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbForbidForProcessCreatorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbForbidForProcessCreatorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbForbidForProcessCreator;
    private javax.swing.JCheckBox cbForbidRedirection;
    private javax.swing.JCheckBox chkCaptureProcess;
    private javax.swing.JCheckBox chkContentSaving;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField comboClerkAutoSelect;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField comboPriority;
    private javax.swing.JLabel labelAdminRole;
    private javax.swing.JLabel labelClerk;
    private javax.swing.JLabel labelClerkAutoSelect;
    private javax.swing.JLabel labelPriority;
    private javax.swing.JLabel labelWorkerRole;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel2;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel panelFooterTitle;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel panelHeaderTitle;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel panelTitle;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textAdminRoles;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textClerk;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textClerkRoles;
    // End of variables declaration//GEN-END:variables

    private AdsAppObject.Prop clerkAutoSelect;
    private AdsAppObject.Prop clerk;
    private AdsAppObject.Prop captureProcess;
    private AdsAppObject.Prop contentSaving;

    private AdsAppObject.Prop title;
    private AdsAppObject.Prop headerTitle;
    private AdsAppObject.Prop footerTitle;

    private AdsAppObject.Prop priority;
    private AdsAppObject.Prop clerkRoles;
    private AdsAppObject.Prop adminRoles;
    private AdsAppObject.Prop forbidForProcessCreator;
    private AdsAppObject.Prop forbidRedirection;

    //private AdsRoleDef workerRoleDef = null;
    //private AdsRoleDef adminRoleDef = null;
    private Id titleId;
    private Id headerTitleId;
    private Id footerTitleId;

    @Override
    public void init() {
        titleId = Id.Factory.loadFrom(title.getValue() != null ? String.valueOf(title.getValue()) : null);
        panelTitle.open(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return obj.getOwnerClass();
            }

            @Override
            public Id getTitleId() {
                return titleId;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef stringDef) {
                titleId = stringDef != null ? stringDef.getId() : null;
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });
        headerTitleId = Id.Factory.loadFrom(headerTitle.getValue() != null ? String.valueOf(headerTitle.getValue()) : null);
        panelHeaderTitle.open(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return obj.getOwnerClass();
            }

            @Override
            public Id getTitleId() {
                return headerTitleId;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef stringDef) {
                headerTitleId = stringDef != null ? stringDef.getId() : null;
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });
        footerTitleId = Id.Factory.loadFrom(footerTitle.getValue() != null ? String.valueOf(footerTitle.getValue()) : null);
        panelFooterTitle.open(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return obj.getOwnerClass();
            }

            @Override
            public Id getTitleId() {
                return footerTitleId;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef stringDef) {
                footerTitleId = stringDef != null ? stringDef.getId() : null;
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });

        final EDwfFormClerkAutoSelect cas = EDwfFormClerkAutoSelect.getForValue((Long) clerkAutoSelect.getValue().toObject(EValType.INT));
        comboClerkAutoSelect.setComboBoxSelectedItem(cas);
        comboClerkAutoSelect.addChangeListener(new ExtendableTextChangeListener() {
            @Override
            public void onEvent(ExtendableTextChangeEvent e) {
                final EDwfFormClerkAutoSelect cas = (EDwfFormClerkAutoSelect) comboClerkAutoSelect.getComboBoxSelectedItem();
                textClerk.setEnabled(EDwfFormClerkAutoSelect.NONE.equals(cas));
            }
        });

        chkContentSaving.setSelected((Boolean) contentSaving.getValue().toObject(EValType.BOOL));
        chkCaptureProcess.setSelected((Boolean) captureProcess.getValue().toObject(EValType.BOOL));
        textClerk.setEnabled(EDwfFormClerkAutoSelect.NONE.equals(cas));

        textClerk.setTextFieldValue(clerk.getValue() != null ? String.valueOf(clerk.getValue()) : null);
        comboPriority.setComboBoxSelectedItem(EDwfFormPriority.getForValue((Long) priority.getValue().toObject(EValType.INT)));

        cbForbidForProcessCreator.setSelected(forbidForProcessCreator != null && ((Boolean) forbidForProcessCreator.getValue().toObject(EValType.BOOL)));
        cbForbidRedirection.setSelected(forbidRedirection != null && ((Boolean) forbidRedirection.getValue().toObject(EValType.BOOL)));

        setRolesTitle(textClerkRoles, clerkRoles.getValue());
        setRolesTitle(textAdminRoles, adminRoles.getValue());
    }

    private void setRolesTitle(ExtTextField field, ValAsStr value) {
        if (value == null) {
            field.setTextFieldValue(RadixResourceBundle.getMessage(FormCommonPanel.class, "FormCommonPanel.rolesEmpty.text"));
            return;
        }
        String fieldValue = "";
        final ArrStr arr = ArrStr.fromValAsStr(value.toString());
        for (String guid : arr) {
            AdsRoleDef roleDef = (AdsRoleDef) AdsSearcher.Factory.newAdsDefinitionSearcher(obj).findById(Id.Factory.loadFrom(guid)).get();
            if (roleDef != null) {
                fieldValue += roleDef.getName();
                if (arr.indexOf(guid) != arr.size() - 1) {
                    fieldValue += ", ";
                }
            }
        }
        field.setTextFieldValue(fieldValue);
    }

    @Override
    public void apply() {
        title.setValue(titleId != null ? titleId.toString() : null);
        headerTitle.setValue(headerTitleId != null ? headerTitleId.toString() : null);
        footerTitle.setValue(footerTitleId != null ? footerTitleId.toString() : null);

        final EDwfFormClerkAutoSelect cas = (EDwfFormClerkAutoSelect) comboClerkAutoSelect.getComboBoxSelectedItem();
        clerkAutoSelect.setValue(String.valueOf(cas.getValue()));

        contentSaving.setValue(chkContentSaving.isSelected() ? "1" : "0");
        captureProcess.setValue(chkCaptureProcess.isSelected() ? "1" : "0");
        final String forbidForProcessCreatorVal = cbForbidForProcessCreator.isSelected() ? "1" : "0";
        if (forbidForProcessCreator == null) {
            if (forbidForProcessCreatorVal.equals("1")) {
                obj.registerProp(new Prop("forbidForProcessCreator", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true, forbidForProcessCreatorVal));
            }
        } else {
            forbidForProcessCreator.setValue(forbidForProcessCreatorVal);
        }
        
        final String forbidRedirectionVal = cbForbidRedirection.isSelected() ? "1" : "0";
        if (forbidRedirection == null) {
            if (forbidRedirectionVal.equals("1")) {
                obj.registerProp(new Prop("forbidRedirection", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true, forbidRedirectionVal));
            }
        } else {
            forbidRedirection.setValue(forbidRedirectionVal);
        }

        String cl = String.valueOf(textClerk.getValue()).trim();
        clerk.setValue(cl.isEmpty() ? null : cl);

        EDwfFormPriority p = (EDwfFormPriority) comboPriority.getComboBoxSelectedItem();
        priority.setValue(String.valueOf(p.getValue()));

        //clerkRoles.setValue(workerRoleDef != null ? String.valueOf(workerRoleDef.getId()) : null);
        //adminRoles.setValue(adminRoleDef != null ? String.valueOf(adminRoleDef.getId()) : null);
    }

    @Override
    public String getTitle() {
        return RadixResourceBundle.getMessage(getClass(), "CTL_CommonProperties");
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.EDIT.PROPERTIES;
    }
}
