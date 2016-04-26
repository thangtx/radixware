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
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
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
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.OverridePanel.OverrideList;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class DialogDuplicatorCommonPanel extends EditorDialog.EditorPanel<AdsAppObject> {

    public DialogDuplicatorCommonPanel(AdsAppObject node) {
        super(node);
        initComponents();

        final AdsAppObject.Prop t = node.getPropByName("title");
        if (t != null)
            t.setValue((String)null);
        final AdsAppObject.Prop ht = node.getPropByName("headerTitle");
        if (ht != null)
            ht.setValue((String)null);
        final AdsAppObject.Prop ft = node.getPropByName("footerTitle");
        if (ft != null)
            ft.setValue((String)null);

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
                    };
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                List<Definition> roleDefs = ChooseDefinition.chooseDefinitions(cfg);
                if (roleDefs != null) {
                    String[] guids = new String[roleDefs.size()];
                    for (int i=0; i<roleDefs.size(); i++) {
                        guids[i] = String.valueOf(roleDefs.get(i).getId());
                    }
                    clerkRoles.setValue(new ArrStr(guids).toString());
                } else
                    clerkRoles.setValue((ValAsStr)null);
                setRolesTitle(textClerkRoles, clerkRoles.getValue());
            }
        });

        textClerkRoles.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clerkRoles.setValue((ValAsStr)null);
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
                    };
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                List<Definition> roleDefs = ChooseDefinition.chooseDefinitions(cfg);
                if (roleDefs != null) {
                    String[] guids = new String[roleDefs.size()];
                    for (int i=0; i<roleDefs.size(); i++) {
                        guids[i] = String.valueOf(roleDefs.get(i).getId());
                    }
                    adminRoles.setValue(new ArrStr(guids).toString());
                } else
                    adminRoles.setValue((ValAsStr)null);
                setRolesTitle(textAdminRoles, adminRoles.getValue());
            }
        });

        textAdminRoles.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminRoles.setValue((ValAsStr)null);
                setRolesTitle(textAdminRoles, adminRoles.getValue());
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
        panel2 = new javax.swing.JPanel();
        chkCaptureProcess = new javax.swing.JCheckBox();
        comboPriority = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        textAdminRoles = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelPriority = new javax.swing.JLabel();
        labelAdminRole = new javax.swing.JLabel();
        chkContentSaving = new javax.swing.JCheckBox();
        btnOverrideTitle = new javax.swing.JToggleButton();
        btnOverrideHeader = new javax.swing.JToggleButton();
        btnOverrideFooter = new javax.swing.JToggleButton();
        btnOverridePriority = new javax.swing.JToggleButton();
        btnOverrideAr = new javax.swing.JToggleButton();
        btnOverrideCr = new javax.swing.JToggleButton();
        btnOverrideClerk = new javax.swing.JToggleButton();
        btnOverrideCs = new javax.swing.JToggleButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 430));
        setPreferredSize(new java.awt.Dimension(500, 430));
        setRequestFocusEnabled(false);

        labelClerkAutoSelect.setText(RadixResourceBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.labelClerkAutoSelect.text")); // NOI18N

        labelClerk.setText(RadixResourceBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.labelClerk.text")); // NOI18N

        labelWorkerRole.setText(RadixResourceBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.labelWorkerRole.text")); // NOI18N

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelClerkAutoSelect)
                    .addComponent(labelClerk)
                    .addComponent(labelWorkerRole))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textClerk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(textClerkRoles, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(comboClerkAutoSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
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
                .addContainerGap(6, Short.MAX_VALUE))
        );

        chkCaptureProcess.setText(RadixResourceBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.chkCaptureProcess.text")); // NOI18N
        chkCaptureProcess.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkCaptureProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCaptureProcessActionPerformed(evt);
            }
        });

        labelPriority.setText(RadixResourceBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.labelPriority.text")); // NOI18N

        labelAdminRole.setText(RadixResourceBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.labelAdminRole.text")); // NOI18N

        chkContentSaving.setText(RadixResourceBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.chkContentSaving.text")); // NOI18N
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
                            .addComponent(textAdminRoles, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                            .addComponent(comboPriority, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                        .addComponent(chkCaptureProcess)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkContentSaving)))
                .addContainerGap())
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkContentSaving)
                    .addComponent(chkCaptureProcess))
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

        btnOverrideTitle.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideTitle.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideTitle.text")); // NOI18N
        btnOverrideTitle.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideTitle.toolTipText")); // NOI18N
        btnOverrideTitle.setFocusPainted(false);
        btnOverrideTitle.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideTitle.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideTitle.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideTitle.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideTitleActionPerformed(evt);
            }
        });

        btnOverrideHeader.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideHeader.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideHeader.text")); // NOI18N
        btnOverrideHeader.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideHeader.toolTipText")); // NOI18N
        btnOverrideHeader.setFocusPainted(false);
        btnOverrideHeader.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideHeader.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideHeader.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideHeader.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideHeaderActionPerformed(evt);
            }
        });

        btnOverrideFooter.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideFooter.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideFooter.text")); // NOI18N
        btnOverrideFooter.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideFooter.toolTipText")); // NOI18N
        btnOverrideFooter.setFocusPainted(false);
        btnOverrideFooter.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideFooter.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideFooter.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideFooter.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideFooter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideFooterActionPerformed(evt);
            }
        });

        btnOverridePriority.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverridePriority.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverridePriority.text")); // NOI18N
        btnOverridePriority.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverridePriority.toolTipText")); // NOI18N
        btnOverridePriority.setFocusPainted(false);
        btnOverridePriority.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverridePriority.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverridePriority.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverridePriority.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverridePriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverridePriorityActionPerformed(evt);
            }
        });

        btnOverrideAr.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideAr.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideAr.text")); // NOI18N
        btnOverrideAr.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideAr.toolTipText")); // NOI18N
        btnOverrideAr.setFocusPainted(false);
        btnOverrideAr.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideAr.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideAr.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideAr.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideAr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideArActionPerformed(evt);
            }
        });

        btnOverrideCr.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideCr.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideCr.text")); // NOI18N
        btnOverrideCr.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideCr.toolTipText")); // NOI18N
        btnOverrideCr.setFocusPainted(false);
        btnOverrideCr.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideCr.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideCr.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideCr.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideCrActionPerformed(evt);
            }
        });

        btnOverrideClerk.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideClerk.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideClerk.text")); // NOI18N
        btnOverrideClerk.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideClerk.toolTipText")); // NOI18N
        btnOverrideClerk.setFocusPainted(false);
        btnOverrideClerk.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideClerk.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideClerk.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideClerk.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideClerk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideClerkActionPerformed(evt);
            }
        });

        btnOverrideCs.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideCs.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideCs.text")); // NOI18N
        btnOverrideCs.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorCommonPanel.class, "DialogDuplicatorCommonPanel.btnOverrideCs.toolTipText")); // NOI18N
        btnOverrideCs.setFocusPainted(false);
        btnOverrideCs.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideCs.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideCs.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideCs.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideCs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideCsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(panelHeaderTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(panelFooterTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOverrideCr, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOverrideClerk, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnOverrideTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOverrideHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOverrideFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOverrideAr, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnOverridePriority, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOverrideCs, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOverrideTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelHeaderTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOverrideHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelFooterTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOverrideFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnOverrideClerk, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOverrideCr, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btnOverrideCs, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnOverridePriority, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOverrideAr, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkCaptureProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCaptureProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCaptureProcessActionPerformed

    private void chkContentSavingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkContentSavingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkContentSavingActionPerformed

    private void btnOverrideTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideTitleActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("title", btnOverrideTitle.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideTitleActionPerformed

    private void btnOverrideHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideHeaderActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("headerTitle", btnOverrideHeader.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideHeaderActionPerformed

    private void btnOverrideFooterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideFooterActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("footerTitle", btnOverrideFooter.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideFooterActionPerformed

    private void btnOverridePriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverridePriorityActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("priority", btnOverridePriority.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverridePriorityActionPerformed

    private void btnOverrideArActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideArActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("adminRoleGuids", btnOverrideAr.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideArActionPerformed

    private void btnOverrideCsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideCsActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("contentSaving", btnOverrideCs.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideCsActionPerformed

    private void btnOverrideClerkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideClerkActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("clerk", btnOverrideClerk.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideClerkActionPerformed

    private void btnOverrideCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideCrActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("clerkRoleGuids", btnOverrideCr.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideCrActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnOverrideAr;
    private javax.swing.JToggleButton btnOverrideClerk;
    private javax.swing.JToggleButton btnOverrideCr;
    private javax.swing.JToggleButton btnOverrideCs;
    private javax.swing.JToggleButton btnOverrideFooter;
    private javax.swing.JToggleButton btnOverrideHeader;
    private javax.swing.JToggleButton btnOverridePriority;
    private javax.swing.JToggleButton btnOverrideTitle;
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

    private Id titleId;
    private Id headerTitleId;
    private Id footerTitleId;

    private OverrideList ovr;

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
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                titleId = multilingualStringDef != null ? multilingualStringDef.getId() : null;
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
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                footerTitleId = multilingualStringDef != null ? multilingualStringDef.getId() : null;
            }
            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });

        final EDwfFormClerkAutoSelect cas = EDwfFormClerkAutoSelect.getForValue((Long)clerkAutoSelect.getValue().toObject(EValType.INT));
        comboClerkAutoSelect.setComboBoxSelectedItem(cas);
        comboClerkAutoSelect.addChangeListener(new ExtendableTextChangeListener() {
            @Override
            public void onEvent(ExtendableTextChangeEvent e) {
                update();
            }
        });

        chkContentSaving.setSelected((Boolean)contentSaving.getValue().toObject(EValType.BOOL));
        chkCaptureProcess.setSelected((Boolean)captureProcess.getValue().toObject(EValType.BOOL));
        textClerk.setEnabled(EDwfFormClerkAutoSelect.NONE.equals(cas));

        textClerk.setTextFieldValue(clerk.getValue() != null ? String.valueOf(clerk.getValue()) : null);
        comboPriority.setComboBoxSelectedItem(EDwfFormPriority.getForValue((Long)priority.getValue().toObject(EValType.INT)));

        setRolesTitle(textClerkRoles, clerkRoles.getValue());
        setRolesTitle(textAdminRoles, adminRoles.getValue());
    }

    private void setRolesTitle(ExtTextField field, ValAsStr value) {
        if (value == null) {
            field.setEmpty();
            return;
        }
        String fieldValue = "";
        final ArrStr arr = ArrStr.fromValAsStr(value.toString());
        for (String guid: arr) {
            AdsRoleDef roleDef = (AdsRoleDef)AdsSearcher.Factory.newAdsDefinitionSearcher(obj).findById(Id.Factory.loadFrom(guid)).get();
            if (roleDef != null) {
                fieldValue += roleDef.getName();
                if (arr.indexOf(guid) != arr.size()-1)
                    fieldValue += ", ";
            }
        }
        field.setTextFieldValue(fieldValue);
    }

    @Override
    public void activate() {
        ovr = new OverrideList(obj);
        update();
    }

    @Override
    public void apply() {
        title.setValue(titleId != null ? titleId.toString() : null);
        headerTitle.setValue(headerTitleId != null ? headerTitleId.toString() : null);
        footerTitle.setValue(footerTitleId != null ? footerTitleId.toString() : null);

        final EDwfFormClerkAutoSelect cas = (EDwfFormClerkAutoSelect)comboClerkAutoSelect.getComboBoxSelectedItem();
        clerkAutoSelect.setValue(String.valueOf(cas.getValue()));

        contentSaving.setValue(chkContentSaving.isSelected() ? "1" : "0");
        captureProcess.setValue(chkCaptureProcess.isSelected() ? "1" : "0");

        String cl = String.valueOf(textClerk.getValue()).trim();
        clerk.setValue(cl.isEmpty() ? null : cl);

        EDwfFormPriority p = (EDwfFormPriority)comboPriority.getComboBoxSelectedItem();
        priority.setValue(String.valueOf(p.getValue()));
    }

    private void update() {
        btnOverrideTitle.setEnabled(!obj.isReadOnly());
        btnOverrideTitle.setSelected(ovr.isOverride("title"));
        panelTitle.setReadonly(!ovr.isOverride("title") || obj.isReadOnly());

        btnOverrideHeader.setEnabled(!obj.isReadOnly());
        btnOverrideHeader.setSelected(ovr.isOverride("headerTitle"));
        panelHeaderTitle.setReadonly(!ovr.isOverride("headerTitle") || obj.isReadOnly());

        btnOverrideFooter.setEnabled(!obj.isReadOnly());
        btnOverrideFooter.setSelected(ovr.isOverride("footerTitle"));
        panelFooterTitle.setReadonly(!ovr.isOverride("footerTitle") || obj.isReadOnly());

        btnOverridePriority.setEnabled(!obj.isReadOnly());
        btnOverridePriority.setSelected(ovr.isOverride("priority"));
        comboPriority.setEnabled(ovr.isOverride("priority") && !obj.isReadOnly());

        btnOverrideClerk.setEnabled(!obj.isReadOnly());
        btnOverrideClerk.setSelected(ovr.isOverride("clerk"));
        comboClerkAutoSelect.setEnabled(ovr.isOverride("clerk") && !obj.isReadOnly());
        final EDwfFormClerkAutoSelect cas = (EDwfFormClerkAutoSelect)comboClerkAutoSelect.getComboBoxSelectedItem();
        textClerk.setEnabled(ovr.isOverride("clerk") && !obj.isReadOnly() && EDwfFormClerkAutoSelect.NONE.equals(cas));

        btnOverrideAr.setEnabled(!obj.isReadOnly());
        btnOverrideAr.setSelected(ovr.isOverride("adminRoleGuids"));
        textAdminRoles.setEnabled(ovr.isOverride("adminRoleGuids") && !obj.isReadOnly());

        btnOverrideCr.setEnabled(!obj.isReadOnly());
        btnOverrideCr.setSelected(ovr.isOverride("clerkRoleGuids"));
        textClerkRoles.setEnabled(ovr.isOverride("clerkRoleGuids") && !obj.isReadOnly());

        btnOverrideCs.setEnabled(!obj.isReadOnly());
        btnOverrideCs.setSelected(ovr.isOverride("contentSaving"));
        chkContentSaving.setEnabled(ovr.isOverride("contentSaving") && !obj.isReadOnly());

        chkCaptureProcess.setEnabled(!obj.isReadOnly());
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
