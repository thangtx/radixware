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
package org.radixware.kernel.designer.ads.editors.role;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDrcPredefinedRoleId;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class AdsRoleEditorGeneralPanel extends JPanel {

    private JButton jbtRemoveIncorrectResource = null;
    private JButton jbtRemoveDuplicatedResource = null;
    private JCheckBox jAbstract = null;
    private JCheckBox jDeprecated = null;
    private JCheckBox jOverwrite = null;
    private DescriptionEditor descriptionEditor;
    private JPanel abstructAndRemoveIncorrectResourcesPanel = null;
    private LocalizingEditorPanel localizingPaneList = null;
    private AdsRoleDef role = null;
    private AdsRoleEditorPanel ownerPanel;

    AdsRoleEditorGeneralPanel(AdsRoleEditorPanel ownerPanel) {
        this.ownerPanel = ownerPanel;
    }
    private HandleInfoEx handleInfo = new HandleInfoEx();

    private class HandleInfoEx extends HandleInfo {

        @Override
        public AdsDefinition getAdsDefinition() {
            return role;
        }

        @Override
        public Id getTitleId() {
            return role.getTitleId();
        }

        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (role != null) {
                if (multilingualStringDef != null) {
                    role.setTitleId(multilingualStringDef.getId());
                } else {
                    role.setTitleId(null);
                }
            }
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            this.getAdsMultilingualStringDef().setValue(language, newStringValue);
        }
    }

    private boolean isMustAddComponents = true;

    private void addComponents() {
        abstructAndRemoveIncorrectResourcesPanel = new JPanel();
        jbtRemoveIncorrectResource = new JButton();

        jbtRemoveIncorrectResource.setIcon(RadixWareDesignerIcon.CHECK.CHECK.getIcon());
        jbtRemoveIncorrectResource.setToolTipText("Remove Incorrect Resources");
        jbtRemoveIncorrectResource.setFocusable(false);
        jbtRemoveIncorrectResource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtRemoveIncorrectResource.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtRemoveIncorrectResource.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtRemoveIncorrectResource.setName("jbtRemoveIncorrectResource"); // NOI18N
        jbtRemoveIncorrectResource.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtRemoveIncorrectResource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtRemoveIncorrectResource.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boolean isTestRole = role.getModule().isTest();
                // jbtRemoveIncorrectResourceActionPerformed(evt);
                List<String> incorrectIds = new ArrayList<String>();
                Map<String, AdsRoleDef.Resource> resources = role.getAllResourceRestrictions();
                int removeCmd = 0;
                int removeChild = 0;
                int removePage = 0;
                for (AdsRoleDef.Resource res : resources.values()) {
                    if (res.defId == null) {
                    } else if (res.type.equals(EDrcResourceType.SERVER_RESOURCE)) {
                        //do nothing
                    } else if (res.type.equals(EDrcResourceType.EXPLORER_ROOT_ITEM)) {

                        AdsParagraphExplorerItemDef root = (AdsParagraphExplorerItemDef) ownerPanel.findAdsDefinition(res.defId);
                        if (root == null) {
                            incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                        } else {
                            if (!root.isRoot()) {
                                incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                            } else if (!isTestRole && root.getModule().isTest()) {
                                incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                            } else {
                                if (res.subDefId != null) {
                                    AdsExplorerItemDef ei = root.getExplorerItems().findChildExplorerItem(res.subDefId);

                                    if (ei == null && AdsRoleDef.RoleResourcesCash.findChildExplorerItem(ownerPanel.getOverwriteOptions(), res.subDefId) == null) {
                                        incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                                    }
                                }
                            }
                        }
                    } else if (res.type.equals(EDrcResourceType.EDITOR_PRESENTATION)) {
                        AdsDefinition def = ownerPanel.findAdsDefinition(res.defId);
                        boolean isMustRemove = false;
                        AdsEditorPresentationDef editorPresentationDef = null;

                        if (def == null) {
                            incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                            isMustRemove = true;
                        } else {
                            if (!isTestRole && def.getModule().isTest()) {
                                incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                                isMustRemove = true;
                            } else {
                                AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) def;
                                if (res.subDefId == null) {
                                    incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                                    isMustRemove = true;
                                } else {
                                    editorPresentationDef
                                            = classDef.getPresentations().getEditorPresentations().findById(res.subDefId, EScope.ALL).get();
                                    if (editorPresentationDef == null) {
                                        incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                                        isMustRemove = true;
                                    }
                                }
                            }
                        }
                        if (!isMustRemove) {
                            AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) def;

                            //RADIX-9796
                            final boolean canChangeAccess = res.restrictions.isDenied(ERestriction.ACCESS);
                            if (canChangeAccess) {
                                res.restrictions.allow(ERestriction.ACCESS);
                            }
                            final boolean canChangeAnyCommand = !res.restrictions.isDenied(ERestriction.ANY_COMMAND);
                            if (canChangeAnyCommand) {
                                res.restrictions.deny(ERestriction.ANY_COMMAND);
                            }
                            final boolean canChangeAnyChild = !res.restrictions.isDenied(ERestriction.ANY_CHILD);
                            if (canChangeAnyChild) {
                                res.restrictions.deny(ERestriction.ANY_CHILD);
                            }
                            final boolean canChangeAnyPage = !res.restrictions.isDenied(ERestriction.ANY_PAGES);
                            if (canChangeAnyPage) {
                                res.restrictions.deny(ERestriction.ANY_PAGES);
                            }
                            //end RADIX-9796                            

                            {
                                ExtendableDefinitions<AdsScopeCommandDef> cmdList = classDef.getPresentations().getCommands();
                                List<Id> listId = res.restrictions.getEnabledCommandIds();

                                if (listId != null
                                        && !listId.isEmpty()
                                        && !res.restrictions.isDenied(ERestriction.ACCESS)
                                        && res.restrictions.isDenied(ERestriction.ANY_COMMAND)) {
                                    for (int i = listId.size() - 1; i >= 0; i--) {
                                        Id id = listId.get(i);
                                        final SearchResult<AdsScopeCommandDef> searchResult = cmdList.findById(id, EScope.ALL);
                                        if (searchResult == null || searchResult.isEmpty()) {
                                            removeCmd++;
                                            //listId.remove(i);
                                            res.restrictions.setCommandEnabled(id, false);
                                        }
                                    }
                                }
                            }
                            
                            {
                                List<AdsExplorerItemDef> childList
                                        = AdsRoleDef.collectPresentationExplorerItems(editorPresentationDef);

                                List<Id> listId = res.restrictions.getEnabledChildIds();

                                if (listId != null
                                        && !listId.isEmpty()
                                        && !res.restrictions.isDenied(ERestriction.ACCESS)
                                        && res.restrictions.isDenied(ERestriction.ANY_CHILD)) {
                                    for (int i = listId.size() - 1; i >= 0; i--) {
                                        Id id = listId.get(i);
                                        boolean isFind = false;
                                        for (AdsExplorerItemDef ei : childList) {
                                            if (ei.getId().equals(id)) {
                                                isFind = true;
                                                break;
                                            }
                                        }

                                        if (!isFind) {
                                            removeCmd++;
                                            res.restrictions.setChildEnabled(id, false);
                                        }
                                    }
                                }
                            }
                            

                            { 
                            List<AdsEditorPageDef> pagesList = new ArrayList();
                            if (editorPresentationDef.getEditorPages()!=null){
                                pagesList = editorPresentationDef.getEditorPages().get(EScope.ALL);
                            }                                

                                List<Id> listId = res.restrictions.getEnabledPageIds();

                                if (listId != null
                                        && !listId.isEmpty()
                                        && !res.restrictions.isDenied(ERestriction.ACCESS)
                                        && res.restrictions.isDenied(ERestriction.ANY_PAGES)) {
                                    for (int i = listId.size() - 1; i >= 0; i--) {
                                        Id id = listId.get(i);
                                        boolean isFind = false;
                                        for (AdsEditorPageDef page : pagesList) {
                                            if (page.getId().equals(id)) {
                                                isFind = true;
                                                break;
                                            }
                                        }

                                        if (!isFind) {
                                            removePage++;
                                            res.restrictions.setPageEnabled(id, false);
                                        }
                                    }
                                }
                            }                            
                            
                            

                            //RADIX-9796
                            if (canChangeAccess) {
                                res.restrictions.deny(ERestriction.ACCESS);
                            }
                            if (canChangeAnyCommand) {
                                res.restrictions.allow(ERestriction.ANY_COMMAND);
                            }
                            if (canChangeAnyChild) {
                                res.restrictions.allow(ERestriction.ANY_CHILD);
                            }
                            if (canChangeAnyPage) {
                                res.restrictions.allow(ERestriction.ANY_PAGES);
                            }
                            //end RADIX-9796                            

                        }
                    } else if (res.type.equals(EDrcResourceType.SELECTOR_PRESENTATION)) {
                        AdsDefinition def = ownerPanel.findAdsDefinition(res.defId);
                        boolean isMustRemove = false;
                        if (def == null) {
                            incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                            isMustRemove = true;
                        } else {
                            if (!isTestRole && def.getModule().isTest()) {
                                incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                                isMustRemove = true;
                            } else {
                                AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) def;
                                if (res.subDefId == null) {
                                    incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                                    isMustRemove = true;
                                } else {
                                    AdsSelectorPresentationDef selectorPresentationDef
                                            = classDef.getPresentations().getSelectorPresentations().findById(res.subDefId, EScope.ALL).get();
                                    if (selectorPresentationDef == null) {
                                        incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                                        isMustRemove = true;
                                    }
                                }
                            }
                        }
                        if (!isMustRemove) {
                            AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) def;

                            //classDef.
                            List<Id> listId = res.restrictions.getEnabledCommandIds();

                            List<Id> currListId = new ArrayList(0);
                            //ExtendableDefinitions<AdsScopeCommandDef> cmdList = classDef.getPresentations().getCommands();
                            AdsEntityObjectClassDef basic = classDef;
                            while (true) {
                                AdsEntityObjectClassDef tmp = basic.findBasis();
                                if (tmp == null) {
                                    break;
                                }
                                basic = tmp;
                            }
                            if (basic != null && (basic instanceof AdsEntityClassDef)) {
                                AdsEntityClassDef ent = (AdsEntityClassDef) basic;
                                AdsEntityGroupClassDef group = ent.findEntityGroup();
                                if (group != null) {
                                    List<AdsScopeCommandDef> lst = group.getPresentations().getCommands().get(EScope.ALL);
                                    for (AdsScopeCommandDef cmd : lst) {
                                        currListId.add(cmd.getId());
                                    }
                                }
                            }
                            //RADIX-9796
                            final boolean canChangeAccess = res.restrictions.isDenied(ERestriction.ACCESS);
                            if (canChangeAccess) {
                                res.restrictions.allow(ERestriction.ACCESS);
                            }
                            final boolean canChangeAnyCommand = !res.restrictions.isDenied(ERestriction.ANY_COMMAND);
                            if (canChangeAnyCommand) {
                                res.restrictions.deny(ERestriction.ANY_COMMAND);
                            }
                            //end RADIX-9796

                            if (listId != null
                                    && !listId.isEmpty()
                                    && !res.restrictions.isDenied(ERestriction.ACCESS)
                                    && res.restrictions.isDenied(ERestriction.ANY_COMMAND)) {
                                for (int i = listId.size() - 1; i >= 0; i--) {
                                    Id id = listId.get(i);
                                    if (!currListId.contains(id)) {
                                        removeCmd++;
                                        //listId.remove(i);
                                        res.restrictions.setCommandEnabled(id, false);
                                    }
                                }
                            }

                            //RADIX-9796
                            if (canChangeAccess) {
                                res.restrictions.deny(ERestriction.ACCESS);
                            }
                            if (canChangeAnyCommand) {
                                res.restrictions.allow(ERestriction.ANY_COMMAND);
                            }
                            //end RADIX-9796

                        }
                    } else if (res.type.equals(EDrcResourceType.CONTEXTLESS_COMMAND)) {
                        AdsDefinition def = ownerPanel.findAdsDefinition(res.defId);
                        if (def == null) {
                            incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                        } else {
                            if (!isTestRole && def.getModule().isTest()) {
                                incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                            }
                        }
                    } else {
                        incorrectIds.add(AdsRoleDef.generateResHashKey(res));
                    }
                }
                if (incorrectIds.isEmpty() && removeCmd == 0 && removeChild == 0 && removePage == 0) {
                    DialogUtils.messageInformation("No problems found.");
                } else {
                    role.setEditState(RadixObject.EEditState.MODIFIED);
                    for (String hash : incorrectIds) {
                        role.RemoveResourceRestrictions(hash);
                    }
                    DialogUtils.messageInformation(String.valueOf(incorrectIds.size()) + " record(s), "
                            + String.valueOf(removeCmd) + " allowed commands, "
                            + String.valueOf(removePage) + " allowed pages "
                            + String.valueOf(removeChild) + " and allowed children were removed.");

                    ownerPanel.update();
                }

            }
        });

        jbtRemoveDuplicatedResource = new JButton();

        jbtRemoveDuplicatedResource.setIcon(RadixWareDesignerIcon.WIDGETS.UNCHECKED_SOURCE.getIcon());
        jbtRemoveDuplicatedResource.setToolTipText("Remove Own Duplicate Resources");
        jbtRemoveDuplicatedResource.setFocusable(false);
        jbtRemoveDuplicatedResource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtRemoveDuplicatedResource.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtRemoveDuplicatedResource.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtRemoveDuplicatedResource.setName("jbtRemoveDuplicatedResource");
        jbtRemoveDuplicatedResource.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtRemoveDuplicatedResource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jbtRemoveDuplicatedResource.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                int count = 0;
                Collection<AdsRoleDef.Resource> resources = new HashSet(role.getResources());
                for (AdsRoleDef.Resource res : resources) {

                    //this.ownerPanel.findAdsDefinition(null)
                    String hash = AdsRoleDef.generateResHashKey(res);
                    Restrictions ownRestriction;
                    Restrictions ancestorRestriction;
                    if (EDrcResourceType.EDITOR_PRESENTATION == res.type) {

                        AdsDefinition def = ownerPanel.findAdsDefinition(res.defId);

                        AdsEditorPresentationDef editorPresentationDef = null;

                        if (def != null) {
                            AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) def;
                            editorPresentationDef = classDef.getPresentations().getEditorPresentations().findById(res.subDefId, EScope.ALL).get();

                        }
                        ownRestriction = role.getAncestorResourceRestrictions(hash, editorPresentationDef);
                        ancestorRestriction = role.getResourceRestrictions(hash, editorPresentationDef);
                    } else {
                        ownRestriction = role.getAncestorResourceRestrictions(hash, null);
                        ancestorRestriction = role.getResourceRestrictions(hash, null);
                    }
                    if (Restrictions.equalIgnoringOrder(ownRestriction, ancestorRestriction)) {
                        role.RemoveResourceRestrictions(hash);
                        count++;
                    }

                }

                if (count == 0) {
                    DialogUtils.messageInformation("There was no change.");
                } else {
                    DialogUtils.messageInformation(String.valueOf(count) + " record(s) were deleted.");
                }
                ownerPanel.update();
            }
        });

        boolean isKernelRole = !role.isAppRole();

        if (isKernelRole) {
            jAbstract = new JCheckBox();
            jAbstract.setText("Abstract  ");
            jAbstract.setName("jAbstract");
            jAbstract.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    role.setAbstract(jAbstract.isSelected());
                }
            });

            jDeprecated = new JCheckBox();
            jDeprecated.setText("Deprecated  ");
            jDeprecated.setName("jDeprecated");
            jDeprecated.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    role.setDeprecated(jDeprecated.isSelected());
                }
            });

            jOverwrite = new JCheckBox();
            jOverwrite.setText("Overwrite");
            jOverwrite.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    role.setOverwrite(jOverwrite.isSelected());
                }
            });

            javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(abstructAndRemoveIncorrectResourcesPanel);
            abstructAndRemoveIncorrectResourcesPanel.setLayout(jPanel19Layout);
            jPanel19Layout.setHorizontalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel19Layout.createSequentialGroup().addContainerGap().addComponent(jAbstract).addComponent(jDeprecated).addComponent(jOverwrite).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 286, Short.MAX_VALUE)
                            .addComponent(jbtRemoveIncorrectResource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtRemoveDuplicatedResource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    //.addComponent(jbtRemoveIncorrectResource2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    ));

            jPanel19Layout.setVerticalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel19Layout.createSequentialGroup().addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jAbstract).addComponent(jDeprecated).addComponent(jOverwrite)
                                    .addComponent(jbtRemoveIncorrectResource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbtRemoveDuplicatedResource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            ).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    //.addComponent(jbtRemoveIncorrectResource2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    ));
        } else {
            /*
             * jAbstract.setVisible(false); jDeprecated.setVisible(false);
             * abstructAndRemoveIncorrectResourcesPanel.setVisible(false);
             */
        }

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gbl);

        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1.0;
        if (isKernelRole) {

            localizingPaneList = new LocalizingEditorPanel();

            c.gridy++;
            c.insets = new Insets(4, 6, 4, 6);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.NORTH;
            gbl.setConstraints(localizingPaneList, c);
            this.add(localizingPaneList);
        }

        c.gridy++;
        c.weightx = 1.0;
        c.insets = new Insets(4, 6, 4, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;

        gbl.setConstraints(abstructAndRemoveIncorrectResourcesPanel, c);
        this.add(abstructAndRemoveIncorrectResourcesPanel);

        //GridBagConstraints
        //c = new GridBagConstraints();
        c.gridy++;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(4, 6, 4, 6);

        //GridBagConstraints.
        c.fill = GridBagConstraints.BOTH;

        c.anchor = GridBagConstraints.NORTHEAST;
        c.weighty = 1.0;

        descriptionEditor = new DescriptionEditor();
        add(descriptionEditor, c);
    }

    public void open(RadixObject definition, OpenInfo info) {
        if (isMustAddComponents) {
            isMustAddComponents = false;
            role = (AdsRoleDef) definition;
            addComponents();
        }
        if (localizingPaneList != null && role.isInBranch()) {
            localizingPaneList.open(handleInfo);
        }
        update();
    }

    public void update() {
        boolean isSuperAdmin = role.getId().toString().equals(EDrcPredefinedRoleId.SUPER_ADMIN.getValue());
        boolean roleIsReadOnly = role.isReadOnly();
        if (jAbstract != null) {
            jAbstract.setSelected(role.isAbstract());
            jAbstract.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        }
        if (jDeprecated != null) {
            jDeprecated.setSelected(role.isDeprecated());
            jDeprecated.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        }

        if (jOverwrite != null) {
            jOverwrite.setSelected(role.isOverwrite());
            jOverwrite.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        }

        jbtRemoveIncorrectResource.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtRemoveDuplicatedResource.setEnabled(!isSuperAdmin && !roleIsReadOnly/* && !role.isEmptyAncestorList()*/);

        if (localizingPaneList != null) {
            localizingPaneList.setReadonly(roleIsReadOnly);
            localizingPaneList.update(handleInfo);
        }

        descriptionEditor.open(role);
    }
}
