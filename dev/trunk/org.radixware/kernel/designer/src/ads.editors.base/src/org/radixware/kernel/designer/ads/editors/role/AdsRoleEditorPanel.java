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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import javax.swing.tree.*;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef.RoleResourcesCash;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.EChooseDefinitionDisplayMode;
import org.radixware.kernel.designer.common.dialogs.components.SimpleTable;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridModel;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridModel.TreeGridNode;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridRow;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTable;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTableModel;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class AdsRoleEditorPanel extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        mainTabbedPane4 = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        ancestorsPane4 = new javax.swing.JPanel();
        jToolBar9 = new javax.swing.JToolBar();
        jbtAddAncestor = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jbtDelAncestor = new javax.swing.JButton();
        jbtClearAncestors = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jbtGoToAncestorRole = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jAncestorList = new javax.swing.JList();
        resourcePane2 = new javax.swing.JPanel();
        jResourcePane = new javax.swing.JTabbedPane();
        esourcePane2 = new javax.swing.JPanel();
        jScrollPaneServerResource = new javax.swing.JScrollPane();
        jToolBar1 = new javax.swing.JToolBar();
        jbtServerAllowedAll = new javax.swing.JButton();
        jbtServerInheritAll = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPaneExplorerRoots = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        jbtExplItemAllowedTree = new javax.swing.JButton();
        jbtExplItemInheritTree = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPaneExplorerItems = new javax.swing.JScrollPane();
        jToolBar2 = new javax.swing.JToolBar();
        jbtExplRootAllowedAll = new javax.swing.JButton();
        jbtExplRootInheritAll = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jbtAddExplorerRoot = new javax.swing.JButton();
        jbtDelExplorerRoot = new javax.swing.JButton();
        jSeparatorGoToExplorerRoot = new javax.swing.JToolBar.Separator();
        jbtGoToExplorerRoot = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jbtAllowedEditorPres = new javax.swing.JButton();
        jbtInheritEditorPres = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JToolBar.Separator();
        jbtAddEntity = new javax.swing.JButton();
        jbtDelEntity = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        jbtGoToEntity = new javax.swing.JButton();
        jSeparatorGoToEntity = new javax.swing.JToolBar.Separator();
        jbtSearchEntity = new javax.swing.JButton();
        jbtChangeMode = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel14 = new javax.swing.JPanel();
        jToolBar8 = new javax.swing.JToolBar();
        jbtAllowedEditorPres1 = new javax.swing.JButton();
        jbtInheritEdPr = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jbtAddEdPr = new javax.swing.JButton();
        jbtDelEdPr = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel23 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jScrollPaneCmdCurr = new javax.swing.JScrollPane();
        jtblCurrentEditorEnabledCmd = new SimpleTable();
        jToolBar11 = new javax.swing.JToolBar();
        jbtSelectAllCmdForEdPr = new javax.swing.JButton();
        jbtDeselectAllCmdForEdPr = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar17 = new javax.swing.JToolBar();
        jbtSetEdPresExplChild = new javax.swing.JButton();
        jbtUnSetEdPresExplChild = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jToolBar18 = new javax.swing.JToolBar();
        jbtSetEdPresPages = new javax.swing.JButton();
        jbtUnSetEdPresPages = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel22 = new javax.swing.JPanel();
        jToolBar16 = new javax.swing.JToolBar();
        jbtSetAllRightsSelectorPres1 = new javax.swing.JButton();
        jbtSetAccessRightsSelectorPres1 = new javax.swing.JButton();
        jScrollPaneCurr = new javax.swing.JScrollPane();
        jlEditorPresTitle = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblSelectorPresentation = new SimpleTable();
        jToolBar13 = new javax.swing.JToolBar();
        jbtAllowedSelectorPres = new javax.swing.JButton();
        jbtInheritSlPr = new javax.swing.JButton();
        jSeparator17 = new javax.swing.JToolBar.Separator();
        jbtAddSlPr = new javax.swing.JButton();
        jbtDelSpPr = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jScrollPaneCurr1 = new javax.swing.JScrollPane();
        jToolBar14 = new javax.swing.JToolBar();
        jbtSetAllRightsSelectorPres = new javax.swing.JButton();
        jbtSetAccessRightsSelectorPres = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jScrollPaneCmdCurr1 = new javax.swing.JScrollPane();
        jtblCurrentSelectorEnabledCmd = new SimpleTable();
        jToolBar15 = new javax.swing.JToolBar();
        jbtSetSelectorPresCmd = new javax.swing.JButton();
        jbtUnSetSelectorPresCmd = new javax.swing.JButton();
        jlSelectorPresTitle = new javax.swing.JLabel();
        jScrollPaneEntitys = new javax.swing.JScrollPane();
        jPanel18 = new javax.swing.JPanel();
        jScrollPaneContextlessCommands = new javax.swing.JScrollPane();
        jToolBar5 = new javax.swing.JToolBar();
        jbtCCmdAllowedAll = new javax.swing.JButton();
        jbtCCmdInheritAll = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        jbtAddCCmd = new javax.swing.JButton();
        jbtDelCCmd = new javax.swing.JButton();
        jSeparatorGoToCCmd = new javax.swing.JToolBar.Separator();
        jbtGoToCCmd = new javax.swing.JButton();
        jToolBar7 = new javax.swing.JToolBar();
        apfamilyPane2 = new javax.swing.JPanel();
        jToolBar10 = new javax.swing.JToolBar();
        jbtDelAPFamily = new javax.swing.JButton();
        jbtClearAPFamily = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JToolBar.Separator();
        jbtAddAutoAPFamily = new javax.swing.JButton();
        jSeparatorGoToAPFamily = new javax.swing.JToolBar.Separator();
        jbtGoToAPFamily = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jAPFamilyList = new javax.swing.JList();

        jPanel10.setName("jPanel10"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setName("mainPanel"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        jPanel4.setName("jPanel4"); // NOI18N

        jPanel19.setName("jPanel11"); // NOI18N

        mainTabbedPane4.setAutoscrolls(true);
        mainTabbedPane4.setName("mainTabbedPane2"); // NOI18N
        mainTabbedPane4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabbedPane4StateChanged(evt);
            }
        });

        generalPanel.setName("generalPanel"); // NOI18N

        jPanel26.setName("jPanel26"); // NOI18N

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 516, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 554, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainTabbedPane4.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.generalPanel.TabConstraints.tabTitle"), generalPanel); // NOI18N

        ancestorsPane4.setName("ancestorsPane2"); // NOI18N
        ancestorsPane4.setLayout(new java.awt.BorderLayout());

        jToolBar9.setFloatable(false);
        jToolBar9.setName("jToolBar7"); // NOI18N

        jbtAddAncestor.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAddAncestor.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddAncestor.toolTipText")); // NOI18N
        jbtAddAncestor.setEnabled(false);
        jbtAddAncestor.setFocusable(false);
        jbtAddAncestor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddAncestor.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddAncestor.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddAncestor.setName("jButton13"); // NOI18N
        jbtAddAncestor.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddAncestor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddAncestor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddAncestorActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtAddAncestor);

        jSeparator7.setName("jSeparator5"); // NOI18N
        jToolBar9.add(jSeparator7);

        jbtDelAncestor.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDelAncestor.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelAncestor.toolTipText")); // NOI18N
        jbtDelAncestor.setEnabled(false);
        jbtDelAncestor.setFocusable(false);
        jbtDelAncestor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelAncestor.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelAncestor.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelAncestor.setName("jButton14"); // NOI18N
        jbtDelAncestor.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelAncestor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelAncestor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelAncestorActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtDelAncestor);

        jbtClearAncestors.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        jbtClearAncestors.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditor.jButton15.text")); // NOI18N
        jbtClearAncestors.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtClearAncestors.toolTipText")); // NOI18N
        jbtClearAncestors.setEnabled(false);
        jbtClearAncestors.setFocusable(false);
        jbtClearAncestors.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtClearAncestors.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtClearAncestors.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtClearAncestors.setName("jButton15"); // NOI18N
        jbtClearAncestors.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtClearAncestors.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtClearAncestors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtClearAncestorsActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtClearAncestors);

        jSeparator9.setName("jSeparator9"); // NOI18N
        jToolBar9.add(jSeparator9);

        jbtGoToAncestorRole.setIcon(RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon());
        jbtGoToAncestorRole.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToAncestorRole.text")); // NOI18N
        jbtGoToAncestorRole.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToAncestorRole.toolTipText")); // NOI18N
        jbtGoToAncestorRole.setFocusable(false);
        jbtGoToAncestorRole.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtGoToAncestorRole.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtGoToAncestorRole.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtGoToAncestorRole.setName("jbtGoToAncestorRole"); // NOI18N
        jbtGoToAncestorRole.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtGoToAncestorRole.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtGoToAncestorRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtGoToAncestorRoleActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtGoToAncestorRole);

        ancestorsPane4.add(jToolBar9, java.awt.BorderLayout.PAGE_START);

        jScrollPane7.setName("jScrollPane5"); // NOI18N

        jAncestorList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jAncestorList.setDragEnabled(true);
        jAncestorList.setName("jList5"); // NOI18N
        jAncestorList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jAncestorListMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jAncestorListMouseReleased(evt);
            }
        });
        jAncestorList.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jAncestorListCaretPositionChanged(evt);
            }
        });
        jAncestorList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jAncestorListKeyReleased(evt);
            }
        });
        jScrollPane7.setViewportView(jAncestorList);

        ancestorsPane4.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        mainTabbedPane4.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditor.ancestorsPane2.TabConstraints.tabTitle"), ancestorsPane4); // NOI18N

        resourcePane2.setName("resourcePane2"); // NOI18N
        resourcePane2.setLayout(new java.awt.BorderLayout());

        jResourcePane.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jResourcePane.toolTipText")); // NOI18N
        jResourcePane.setName("jResourcePane"); // NOI18N

        esourcePane2.setName("esourcePane2"); // NOI18N
        esourcePane2.setLayout(new java.awt.BorderLayout());

        jScrollPaneServerResource.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPaneServerResource.setName("jScrollPaneServerResource"); // NOI18N
        esourcePane2.add(jScrollPaneServerResource, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setFocusable(false);
        jToolBar1.setMaximumSize(new java.awt.Dimension(93, 32));
        jToolBar1.setMinimumSize(new java.awt.Dimension(93, 32));
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setPreferredSize(new java.awt.Dimension(69, 32));

        jbtServerAllowedAll.setIcon(RadixWareDesignerIcon.DIALOG.OK.getIcon());
        jbtServerAllowedAll.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtServerAllowedAll.text_1")); // NOI18N
        jbtServerAllowedAll.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtServerAllowedAll.toolTipText")); // NOI18N
        jbtServerAllowedAll.setFocusable(false);
        jbtServerAllowedAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtServerAllowedAll.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtServerAllowedAll.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtServerAllowedAll.setName("jbtServerAllowedAll"); // NOI18N
        jbtServerAllowedAll.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtServerAllowedAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtServerAllowedAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtServerAllowedAllActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtServerAllowedAll);

        jbtServerInheritAll.setIcon(RadixWareDesignerIcon.FILE.NEW_DOCUMENT.getIcon());
        jbtServerInheritAll.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtServerInheritAll.text_1")); // NOI18N
        jbtServerInheritAll.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtServerInheritAll.toolTipText")); // NOI18N
        jbtServerInheritAll.setFocusable(false);
        jbtServerInheritAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtServerInheritAll.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtServerInheritAll.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtServerInheritAll.setName("jbtServerInheritAll"); // NOI18N
        jbtServerInheritAll.setPreferredSize(new java.awt.Dimension(27, 27));
        jbtServerInheritAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtServerInheritAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtServerInheritAllActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtServerInheritAll);

        esourcePane2.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jResourcePane.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.esourcePane2.TabConstraints.tabTitle"), esourcePane2); // NOI18N

        jPanel15.setName("jPanel12"); // NOI18N
        jPanel15.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setDividerLocation(114);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jScrollPaneExplorerRoots.setName("jScrollPaneExplorerRoots"); // NOI18N
        jSplitPane2.setTopComponent(jScrollPaneExplorerRoots);

        jPanel13.setName("jPanel13"); // NOI18N
        jPanel13.setLayout(new java.awt.BorderLayout());

        jToolBar6.setFloatable(false);
        jToolBar6.setBorderPainted(false);
        jToolBar6.setFocusable(false);
        jToolBar6.setMaximumSize(new java.awt.Dimension(32, 32));
        jToolBar6.setMinimumSize(new java.awt.Dimension(32, 32));
        jToolBar6.setName("jToolBar6"); // NOI18N
        jToolBar6.setPreferredSize(new java.awt.Dimension(32, 32));

        jbtExplItemAllowedTree.setIcon(RadixWareDesignerIcon.TREE.ALLOWED.getIcon(  ));
        jbtExplItemAllowedTree.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplItemAllowedTree.text")); // NOI18N
        jbtExplItemAllowedTree.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplItemAllowedTree.toolTipText")); // NOI18N
        jbtExplItemAllowedTree.setFocusable(false);
        jbtExplItemAllowedTree.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtExplItemAllowedTree.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtExplItemAllowedTree.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtExplItemAllowedTree.setName("jbtExplItemAllowedTree"); // NOI18N
        jbtExplItemAllowedTree.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtExplItemAllowedTree.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtExplItemAllowedTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExplItemAllowedTreeActionPerformed(evt);
            }
        });
        jToolBar6.add(jbtExplItemAllowedTree);

        jbtExplItemInheritTree.setIcon(RadixWareDesignerIcon.TREE.INHERIT.getIcon());
        jbtExplItemInheritTree.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplItemInheritTree.text")); // NOI18N
        jbtExplItemInheritTree.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplItemInheritTree.toolTipText")); // NOI18N
        jbtExplItemInheritTree.setFocusable(false);
        jbtExplItemInheritTree.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtExplItemInheritTree.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtExplItemInheritTree.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtExplItemInheritTree.setName("jbtExplItemInheritTree"); // NOI18N
        jbtExplItemInheritTree.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtExplItemInheritTree.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtExplItemInheritTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExplItemInheritTreeActionPerformed(evt);
            }
        });
        jToolBar6.add(jbtExplItemInheritTree);

        jPanel13.add(jToolBar6, java.awt.BorderLayout.PAGE_START);

        jPanel5.setName("jPanel5"); // NOI18N

        jScrollPaneExplorerItems.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPaneExplorerItems.setName("jScrollPaneExplorerItems"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneExplorerItems, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneExplorerItems, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel5, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(jPanel13);

        jPanel15.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jToolBar2.setFloatable(false);
        jToolBar2.setBorderPainted(false);
        jToolBar2.setFocusable(false);
        jToolBar2.setMaximumSize(new java.awt.Dimension(134, 32));
        jToolBar2.setName("jToolBar2"); // NOI18N

        jbtExplRootAllowedAll.setIcon(RadixWareDesignerIcon.DIALOG.OK.getIcon());
        jbtExplRootAllowedAll.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplRootAllowedAll.text")); // NOI18N
        jbtExplRootAllowedAll.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplRootAllowedAll.toolTipText")); // NOI18N
        jbtExplRootAllowedAll.setFocusable(false);
        jbtExplRootAllowedAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtExplRootAllowedAll.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtExplRootAllowedAll.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtExplRootAllowedAll.setName("jbtExplRootAllowedAll"); // NOI18N
        jbtExplRootAllowedAll.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtExplRootAllowedAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtExplRootAllowedAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExplRootAllowedAllActionPerformed(evt);
            }
        });
        jToolBar2.add(jbtExplRootAllowedAll);

        jbtExplRootInheritAll.setIcon(RadixWareDesignerIcon.FILE.NEW_DOCUMENT.getIcon());
        jbtExplRootInheritAll.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplRootInheritAll.text")); // NOI18N
        jbtExplRootInheritAll.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtExplRootInheritAll.toolTipText")); // NOI18N
        jbtExplRootInheritAll.setFocusable(false);
        jbtExplRootInheritAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtExplRootInheritAll.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtExplRootInheritAll.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtExplRootInheritAll.setName("jbtExplRootInheritAll"); // NOI18N
        jbtExplRootInheritAll.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtExplRootInheritAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtExplRootInheritAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExplRootInheritAllActionPerformed(evt);
            }
        });
        jToolBar2.add(jbtExplRootInheritAll);

        jSeparator13.setName("jSeparator13"); // NOI18N
        jToolBar2.add(jSeparator13);

        jbtAddExplorerRoot.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAddExplorerRoot.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddExplorerRoot.text")); // NOI18N
        jbtAddExplorerRoot.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddExplorerRoot.toolTipText")); // NOI18N
        jbtAddExplorerRoot.setFocusable(false);
        jbtAddExplorerRoot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddExplorerRoot.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddExplorerRoot.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddExplorerRoot.setName("jbtAddExplorerRoot"); // NOI18N
        jbtAddExplorerRoot.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddExplorerRoot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddExplorerRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddExplorerRootActionPerformed(evt);
            }
        });
        jToolBar2.add(jbtAddExplorerRoot);

        jbtDelExplorerRoot.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDelExplorerRoot.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelExplorerRoot.text")); // NOI18N
        jbtDelExplorerRoot.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelExplorerRoot.toolTipText")); // NOI18N
        jbtDelExplorerRoot.setFocusable(false);
        jbtDelExplorerRoot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelExplorerRoot.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelExplorerRoot.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelExplorerRoot.setName("jbtDelExplorerRoot"); // NOI18N
        jbtDelExplorerRoot.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelExplorerRoot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelExplorerRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelExplorerRootActionPerformed(evt);
            }
        });
        jToolBar2.add(jbtDelExplorerRoot);

        jSeparatorGoToExplorerRoot.setMaximumSize(new java.awt.Dimension(6, 32));
        jSeparatorGoToExplorerRoot.setName("jSeparatorGoToExplorerRoot"); // NOI18N
        jToolBar2.add(jSeparatorGoToExplorerRoot);

        jbtGoToExplorerRoot.setIcon(RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon());
        jbtGoToExplorerRoot.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToExplorerRoot.text")); // NOI18N
        jbtGoToExplorerRoot.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToExplorerRoot.toolTipText")); // NOI18N
        jbtGoToExplorerRoot.setFocusable(false);
        jbtGoToExplorerRoot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtGoToExplorerRoot.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtGoToExplorerRoot.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtGoToExplorerRoot.setName("jbtGoToExplorerRoot"); // NOI18N
        jbtGoToExplorerRoot.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtGoToExplorerRoot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtGoToExplorerRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtGoToExplorerRootActionPerformed(evt);
            }
        });
        jToolBar2.add(jbtGoToExplorerRoot);

        jPanel15.add(jToolBar2, java.awt.BorderLayout.PAGE_START);

        jResourcePane.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditor.jPanel12.TabConstraints.tabTitle"), jPanel15); // NOI18N

        jPanel16.setName("jPanel15"); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setLayout(new java.awt.BorderLayout());

        jToolBar3.setFloatable(false);
        jToolBar3.setMaximumSize(new java.awt.Dimension(32, 32));
        jToolBar3.setMinimumSize(new java.awt.Dimension(32, 32));
        jToolBar3.setName("jToolBar3"); // NOI18N
        jToolBar3.setPreferredSize(new java.awt.Dimension(32, 32));

        jbtAllowedEditorPres.setIcon(RadixWareDesignerIcon.DIALOG.OK.getIcon());
        jbtAllowedEditorPres.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAllowedEditorPres.text")); // NOI18N
        jbtAllowedEditorPres.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAllowedEditorPres.toolTipText")); // NOI18N
        jbtAllowedEditorPres.setFocusable(false);
        jbtAllowedEditorPres.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAllowedEditorPres.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAllowedEditorPres.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAllowedEditorPres.setName("jbtAllowedEditorPres"); // NOI18N
        jbtAllowedEditorPres.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAllowedEditorPres.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAllowedEditorPres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAllowedEditorPresActionPerformed(evt);
            }
        });
        jToolBar3.add(jbtAllowedEditorPres);

        jbtInheritEditorPres.setIcon(RadixWareDesignerIcon.FILE.NEW_DOCUMENT.getIcon());
        jbtInheritEditorPres.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtInheritEditorPres.text")); // NOI18N
        jbtInheritEditorPres.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtInheritEditorPres.toolTipText")); // NOI18N
        jbtInheritEditorPres.setFocusable(false);
        jbtInheritEditorPres.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtInheritEditorPres.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtInheritEditorPres.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtInheritEditorPres.setName("jbtInheritEditorPres"); // NOI18N
        jbtInheritEditorPres.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtInheritEditorPres.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtInheritEditorPres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtInheritEditorPresActionPerformed(evt);
            }
        });
        jToolBar3.add(jbtInheritEditorPres);

        jSeparator16.setName("jSeparator16"); // NOI18N
        jToolBar3.add(jSeparator16);

        jbtAddEntity.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAddEntity.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddEntity.text")); // NOI18N
        jbtAddEntity.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddEntity.toolTipText")); // NOI18N
        jbtAddEntity.setFocusable(false);
        jbtAddEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddEntity.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddEntity.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddEntity.setName("jbtAddEntity"); // NOI18N
        jbtAddEntity.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddEntityActionPerformed(evt);
            }
        });
        jToolBar3.add(jbtAddEntity);

        jbtDelEntity.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDelEntity.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelEntity.text")); // NOI18N
        jbtDelEntity.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelEntity.toolTipText")); // NOI18N
        jbtDelEntity.setFocusable(false);
        jbtDelEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelEntity.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelEntity.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelEntity.setName("jbtDelEntity"); // NOI18N
        jbtDelEntity.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelEntityActionPerformed(evt);
            }
        });
        jToolBar3.add(jbtDelEntity);

        jSeparator12.setMaximumSize(new java.awt.Dimension(6, 32));
        jSeparator12.setName("jSeparator12"); // NOI18N
        jToolBar3.add(jSeparator12);

        jbtGoToEntity.setIcon(RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon());
        jbtGoToEntity.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToEntity.text")); // NOI18N
        jbtGoToEntity.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToEntity.toolTipText")); // NOI18N
        jbtGoToEntity.setFocusable(false);
        jbtGoToEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtGoToEntity.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtGoToEntity.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtGoToEntity.setName("jbtGoToEntity"); // NOI18N
        jbtGoToEntity.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtGoToEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtGoToEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtGoToEntityActionPerformed(evt);
            }
        });
        jToolBar3.add(jbtGoToEntity);

        jSeparatorGoToEntity.setMaximumSize(new java.awt.Dimension(6, 32));
        jSeparatorGoToEntity.setName("jSeparatorGoToEntity"); // NOI18N
        jToolBar3.add(jSeparatorGoToEntity);

        jbtSearchEntity.setIcon(RadixWareDesignerIcon.EDIT.SEARCH.getIcon());
        jbtSearchEntity.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSearchEntity.toolTipText")); // NOI18N
        jbtSearchEntity.setFocusable(false);
        jbtSearchEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSearchEntity.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSearchEntity.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSearchEntity.setName("jbtSearchEntity"); // NOI18N
        jbtSearchEntity.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSearchEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSearchEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSearchEntityActionPerformed(evt);
            }
        });
        jToolBar3.add(jbtSearchEntity);

        jbtChangeMode.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtChangeMode.toolTipText")); // NOI18N
        jbtChangeMode.setFocusable(false);
        jbtChangeMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtChangeMode.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtChangeMode.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtChangeMode.setName("jbtChangeMode"); // NOI18N
        jbtChangeMode.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtChangeMode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtChangeMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtChangeModeActionPerformed(evt);
            }
        });
        jToolBar3.add(jbtChangeMode);

        jPanel7.add(jToolBar3, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(236, 1));
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(0, 0));
        jTabbedPane2.setName("jTabbedPane2"); // NOI18N
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(0, 0));

        jPanel8.setName("jPanel8"); // NOI18N

        jSplitPane3.setDividerLocation(230);
        jSplitPane3.setMinimumSize(new java.awt.Dimension(266, 30));
        jSplitPane3.setName("jSplitPane3"); // NOI18N

        jPanel14.setMinimumSize(new java.awt.Dimension(130, 1));
        jPanel14.setName("jPanel14"); // NOI18N
        jPanel14.setLayout(new java.awt.BorderLayout());

        jToolBar8.setFloatable(false);
        jToolBar8.setName("jToolBar8"); // NOI18N

        jbtAllowedEditorPres1.setIcon(RadixWareDesignerIcon.DIALOG.OK.getIcon());
        jbtAllowedEditorPres1.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAllowedEditorPres1.text")); // NOI18N
        jbtAllowedEditorPres1.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAllowedEditorPres1.toolTipText")); // NOI18N
        jbtAllowedEditorPres1.setFocusable(false);
        jbtAllowedEditorPres1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAllowedEditorPres1.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAllowedEditorPres1.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAllowedEditorPres1.setName("jbtAllowedEditorPres1"); // NOI18N
        jbtAllowedEditorPres1.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAllowedEditorPres1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAllowedEditorPres1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAllowedEditorPres1ActionPerformed(evt);
            }
        });
        jToolBar8.add(jbtAllowedEditorPres1);

        jbtInheritEdPr.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        jbtInheritEdPr.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtInheritEdPr.text")); // NOI18N
        jbtInheritEdPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtInheritEdPr.toolTipText")); // NOI18N
        jbtInheritEdPr.setFocusable(false);
        jbtInheritEdPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtInheritEdPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtInheritEdPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtInheritEdPr.setName("jbtInheritEdPr"); // NOI18N
        jbtInheritEdPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtInheritEdPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtInheritEdPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtInheritEdPrActionPerformed(evt);
            }
        });
        jToolBar8.add(jbtInheritEdPr);

        jSeparator8.setName("jSeparator8"); // NOI18N
        jToolBar8.add(jSeparator8);

        jbtAddEdPr.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAddEdPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddEdPr.toolTipText")); // NOI18N
        jbtAddEdPr.setFocusable(false);
        jbtAddEdPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddEdPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddEdPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddEdPr.setName("jbtAddEdPr"); // NOI18N
        jbtAddEdPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddEdPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddEdPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddEdPrActionPerformed(evt);
            }
        });
        jToolBar8.add(jbtAddEdPr);

        jbtDelEdPr.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDelEdPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelEdPr.toolTipText")); // NOI18N
        jbtDelEdPr.setFocusable(false);
        jbtDelEdPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelEdPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelEdPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelEdPr.setName("jbtDelEdPr"); // NOI18N
        jbtDelEdPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelEdPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelEdPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelEdPrActionPerformed(evt);
            }
        });
        jToolBar8.add(jbtDelEdPr);

        jPanel14.add(jToolBar8, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jPanel14.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane3.setLeftComponent(jPanel14);

        jPanel23.setName("jPanel23"); // NOI18N
        jPanel23.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(129, 1));
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel12.setName("jPanel12"); // NOI18N
        jPanel12.setLayout(new java.awt.BorderLayout());

        jScrollPaneCmdCurr.setName("jScrollPaneCmdCurr"); // NOI18N

        jtblCurrentEditorEnabledCmd.setBackground(javax.swing.UIManager.getDefaults().getColor("TableHeader.background"));
        jtblCurrentEditorEnabledCmd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtblCurrentEditorEnabledCmd.setName("jtblCurrentEditorEnabledCmd"); // NOI18N
        jtblCurrentEditorEnabledCmd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblCurrentEditorEnabledCmdMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtblCurrentEditorEnabledCmdMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jtblCurrentEditorEnabledCmdMouseReleased(evt);
            }
        });
        jtblCurrentEditorEnabledCmd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblCurrentEditorEnabledCmdKeyReleased(evt);
            }
        });
        jScrollPaneCmdCurr.setViewportView(jtblCurrentEditorEnabledCmd);

        jPanel12.add(jScrollPaneCmdCurr, java.awt.BorderLayout.CENTER);

        jToolBar11.setFloatable(false);
        jToolBar11.setName("jToolBar11"); // NOI18N

        jbtSelectAllCmdForEdPr.setIcon(RadixWareDesignerIcon.DIALOG.CHECK.SET.getIcon());
        jbtSelectAllCmdForEdPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSelectAllCmdForEdPr.toolTipText")); // NOI18N
        jbtSelectAllCmdForEdPr.setFocusable(false);
        jbtSelectAllCmdForEdPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSelectAllCmdForEdPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSelectAllCmdForEdPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSelectAllCmdForEdPr.setName("jbtSelectAllCmdForEdPr"); // NOI18N
        jbtSelectAllCmdForEdPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSelectAllCmdForEdPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSelectAllCmdForEdPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSelectAllCmdForEdPrActionPerformed(evt);
            }
        });
        jToolBar11.add(jbtSelectAllCmdForEdPr);

        jbtDeselectAllCmdForEdPr.setIcon(RadixWareDesignerIcon.CHECK.UNSET.getIcon());
        jbtDeselectAllCmdForEdPr.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDeselectAllCmdForEdPr.text")); // NOI18N
        jbtDeselectAllCmdForEdPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDeselectAllCmdForEdPr.toolTipText")); // NOI18N
        jbtDeselectAllCmdForEdPr.setFocusable(false);
        jbtDeselectAllCmdForEdPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDeselectAllCmdForEdPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDeselectAllCmdForEdPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDeselectAllCmdForEdPr.setName("jbtDeselectAllCmdForEdPr"); // NOI18N
        jbtDeselectAllCmdForEdPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDeselectAllCmdForEdPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDeselectAllCmdForEdPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDeselectAllCmdForEdPrActionPerformed(evt);
            }
        });
        jToolBar11.add(jbtDeselectAllCmdForEdPr);

        jPanel12.add(jToolBar11, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jPanel12.TabConstraints.tabTitle"), jPanel12); // NOI18N

        jPanel27.setName("jPanel27"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar17.setFloatable(false);
        jToolBar17.setName("jToolBar17"); // NOI18N
        jToolBar17.setPreferredSize(new java.awt.Dimension(64, 32));

        jbtSetEdPresExplChild.setIcon(RadixWareDesignerIcon.DIALOG.CHECK.SET.getIcon());
        jbtSetEdPresExplChild.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetEdPresExplChild.text")); // NOI18N
        jbtSetEdPresExplChild.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetEdPresExplChild.toolTipText")); // NOI18N
        jbtSetEdPresExplChild.setFocusable(false);
        jbtSetEdPresExplChild.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetEdPresExplChild.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetEdPresExplChild.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetEdPresExplChild.setName("jbtSetEdPresExplChild"); // NOI18N
        jbtSetEdPresExplChild.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetEdPresExplChild.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetEdPresExplChild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetEdPresExplChildActionPerformed(evt);
            }
        });
        jToolBar17.add(jbtSetEdPresExplChild);

        jbtUnSetEdPresExplChild.setIcon(RadixWareDesignerIcon.CHECK.UNSET.getIcon());
        jbtUnSetEdPresExplChild.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtUnSetEdPresExplChild.text")); // NOI18N
        jbtUnSetEdPresExplChild.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtUnSetEdPresExplChild.toolTipText")); // NOI18N
        jbtUnSetEdPresExplChild.setFocusable(false);
        jbtUnSetEdPresExplChild.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtUnSetEdPresExplChild.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtUnSetEdPresExplChild.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtUnSetEdPresExplChild.setName("jbtUnSetEdPresExplChild"); // NOI18N
        jbtUnSetEdPresExplChild.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtUnSetEdPresExplChild.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtUnSetEdPresExplChild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtUnSetEdPresExplChildActionPerformed(evt);
            }
        });
        jToolBar17.add(jbtUnSetEdPresExplChild);

        jPanel1.add(jToolBar17, java.awt.BorderLayout.PAGE_START);

        jPanel24.setName("jPanel24"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel24, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jPanel27.TabConstraints.tabTitle"), jPanel27); // NOI18N

        jPanel2.setName(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jPanel2.name")); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        jToolBar18.setFloatable(false);
        jToolBar18.setName(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jToolBar18.name")); // NOI18N
        jToolBar18.setPreferredSize(new java.awt.Dimension(64, 32));

        jbtSetEdPresPages.setIcon(RadixWareDesignerIcon.DIALOG.CHECK.SET.getIcon());
        jbtSetEdPresPages.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetEdPresPages.text")); // NOI18N
        jbtSetEdPresPages.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetEdPresPages.toolTipText")); // NOI18N
        jbtSetEdPresPages.setFocusable(false);
        jbtSetEdPresPages.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetEdPresPages.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetEdPresPages.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetEdPresPages.setName(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetEdPresPages.name")); // NOI18N
        jbtSetEdPresPages.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetEdPresPages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetEdPresPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetEdPresPagesActionPerformed(evt);
            }
        });
        jToolBar18.add(jbtSetEdPresPages);

        jbtUnSetEdPresPages.setIcon(RadixWareDesignerIcon.CHECK.UNSET.getIcon());
        jbtUnSetEdPresPages.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtUnSetEdPresPages.text")); // NOI18N
        jbtUnSetEdPresPages.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtUnSetEdPresPages.toolTipText")); // NOI18N
        jbtUnSetEdPresPages.setFocusable(false);
        jbtUnSetEdPresPages.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtUnSetEdPresPages.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtUnSetEdPresPages.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtUnSetEdPresPages.setName(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtUnSetEdPresPages.name")); // NOI18N
        jbtUnSetEdPresPages.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtUnSetEdPresPages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtUnSetEdPresPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtUnSetEdPresPagesActionPerformed(evt);
            }
        });
        jToolBar18.add(jbtUnSetEdPresPages);

        jPanel2.add(jToolBar18, java.awt.BorderLayout.PAGE_START);

        jScrollPane4.setName(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jScrollPane4.name")); // NOI18N
        jPanel2.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel23.add(jTabbedPane1, gridBagConstraints);

        jPanel22.setMaximumSize(new java.awt.Dimension(10000, 100));
        jPanel22.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel22.setName("jPanel22"); // NOI18N
        jPanel22.setPreferredSize(new java.awt.Dimension(0, 0));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jToolBar16.setFloatable(false);
        jToolBar16.setName("jToolBar16"); // NOI18N

        jbtSetAllRightsSelectorPres1.setIcon(RadixWareDesignerIcon.RIGHTS.ALL.getIcon());
        jbtSetAllRightsSelectorPres1.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAllRightsSelectorPres1.text")); // NOI18N
        jbtSetAllRightsSelectorPres1.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAllRightsSelectorPres1.toolTipText")); // NOI18N
        jbtSetAllRightsSelectorPres1.setFocusable(false);
        jbtSetAllRightsSelectorPres1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetAllRightsSelectorPres1.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetAllRightsSelectorPres1.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetAllRightsSelectorPres1.setName("jbtSetAllRightsSelectorPres1"); // NOI18N
        jbtSetAllRightsSelectorPres1.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetAllRightsSelectorPres1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetAllRightsSelectorPres1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetAllRightsSelectorPres1ActionPerformed(evt);
            }
        });
        jToolBar16.add(jbtSetAllRightsSelectorPres1);

        jbtSetAccessRightsSelectorPres1.setIcon(RadixWareDesignerIcon.RIGHTS.ACCESS_ONLY.getIcon());
        jbtSetAccessRightsSelectorPres1.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAccessRightsSelectorPres1.text")); // NOI18N
        jbtSetAccessRightsSelectorPres1.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAccessRightsSelectorPres1.toolTipText")); // NOI18N
        jbtSetAccessRightsSelectorPres1.setFocusable(false);
        jbtSetAccessRightsSelectorPres1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetAccessRightsSelectorPres1.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetAccessRightsSelectorPres1.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetAccessRightsSelectorPres1.setName("jbtSetAccessRightsSelectorPres1"); // NOI18N
        jbtSetAccessRightsSelectorPres1.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetAccessRightsSelectorPres1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetAccessRightsSelectorPres1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetAccessRightsSelectorPres1ActionPerformed(evt);
            }
        });
        jToolBar16.add(jbtSetAccessRightsSelectorPres1);

        jPanel22.add(jToolBar16, java.awt.BorderLayout.PAGE_START);

        jScrollPaneCurr.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPaneCurr.setName("jScrollPaneCurr"); // NOI18N
        jPanel22.add(jScrollPaneCurr, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 126;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel23.add(jPanel22, gridBagConstraints);

        jlEditorPresTitle.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jlEditorPresTitle.text")); // NOI18N
        jlEditorPresTitle.setName("jlEditorPresTitle"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel23.add(jlEditorPresTitle, gridBagConstraints);

        jSplitPane3.setRightComponent(jPanel23);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        jPanel9.setName("jPanel9"); // NOI18N

        jSplitPane5.setDividerLocation(230);
        jSplitPane5.setMinimumSize(new java.awt.Dimension(266, 30));
        jSplitPane5.setName("jSplitPane5"); // NOI18N

        jPanel25.setMinimumSize(new java.awt.Dimension(130, 1));
        jPanel25.setName("jPanel25"); // NOI18N
        jPanel25.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jtblSelectorPresentation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtblSelectorPresentation.setName("jtblSelectorPresentation"); // NOI18N
        jtblSelectorPresentation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtblSelectorPresentationMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jtblSelectorPresentationMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblSelectorPresentationMouseClicked(evt);
            }
        });
        jtblSelectorPresentation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblSelectorPresentationKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jtblSelectorPresentation);

        jPanel25.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jToolBar13.setFloatable(false);
        jToolBar13.setName("jToolBar13"); // NOI18N

        jbtAllowedSelectorPres.setIcon(RadixWareDesignerIcon.DIALOG.OK.getIcon());
        jbtAllowedSelectorPres.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAllowedSelectorPres.text")); // NOI18N
        jbtAllowedSelectorPres.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAllowedSelectorPres.toolTipText")); // NOI18N
        jbtAllowedSelectorPres.setFocusable(false);
        jbtAllowedSelectorPres.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAllowedSelectorPres.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAllowedSelectorPres.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAllowedSelectorPres.setName("jbtAllowedSelectorPres"); // NOI18N
        jbtAllowedSelectorPres.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAllowedSelectorPres.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAllowedSelectorPres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAllowedSelectorPresActionPerformed(evt);
            }
        });
        jToolBar13.add(jbtAllowedSelectorPres);

        jbtInheritSlPr.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        jbtInheritSlPr.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtInheritSlPr.text")); // NOI18N
        jbtInheritSlPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtInheritSlPr.toolTipText")); // NOI18N
        jbtInheritSlPr.setFocusable(false);
        jbtInheritSlPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtInheritSlPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtInheritSlPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtInheritSlPr.setName("jbtInheritSlPr"); // NOI18N
        jbtInheritSlPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtInheritSlPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtInheritSlPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtInheritSlPrActionPerformed(evt);
            }
        });
        jToolBar13.add(jbtInheritSlPr);

        jSeparator17.setName("jSeparator17"); // NOI18N
        jToolBar13.add(jSeparator17);

        jbtAddSlPr.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAddSlPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddSlPr.toolTipText")); // NOI18N
        jbtAddSlPr.setFocusable(false);
        jbtAddSlPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddSlPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddSlPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddSlPr.setName("jbtAddSlPr"); // NOI18N
        jbtAddSlPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddSlPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddSlPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddSlPrActionPerformed(evt);
            }
        });
        jToolBar13.add(jbtAddSlPr);

        jbtDelSpPr.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDelSpPr.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelSpPr.toolTipText")); // NOI18N
        jbtDelSpPr.setFocusable(false);
        jbtDelSpPr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelSpPr.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelSpPr.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelSpPr.setName("jbtDelSpPr"); // NOI18N
        jbtDelSpPr.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelSpPr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelSpPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelSpPrActionPerformed(evt);
            }
        });
        jToolBar13.add(jbtDelSpPr);

        jPanel25.add(jToolBar13, java.awt.BorderLayout.PAGE_START);

        jSplitPane5.setLeftComponent(jPanel25);

        jPanel17.setName("jPanel17"); // NOI18N
        jPanel17.setLayout(new java.awt.GridBagLayout());

        jPanel20.setMaximumSize(new java.awt.Dimension(2147483647, 58));
        jPanel20.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel20.setName("jPanel20"); // NOI18N
        jPanel20.setPreferredSize(new java.awt.Dimension(0, 0));
        jPanel20.setRequestFocusEnabled(false);
        jPanel20.setLayout(new java.awt.BorderLayout());

        jScrollPaneCurr1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPaneCurr1.setName("jScrollPaneCurr1"); // NOI18N
        jPanel20.add(jScrollPaneCurr1, java.awt.BorderLayout.CENTER);

        jToolBar14.setFloatable(false);
        jToolBar14.setName("jToolBar14"); // NOI18N

        jbtSetAllRightsSelectorPres.setIcon(RadixWareDesignerIcon.RIGHTS.ALL.getIcon());
        jbtSetAllRightsSelectorPres.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAllRightsSelectorPres.text")); // NOI18N
        jbtSetAllRightsSelectorPres.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAllRightsSelectorPres.toolTipText")); // NOI18N
        jbtSetAllRightsSelectorPres.setFocusable(false);
        jbtSetAllRightsSelectorPres.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetAllRightsSelectorPres.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetAllRightsSelectorPres.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetAllRightsSelectorPres.setName("jbtSetAllRightsSelectorPres"); // NOI18N
        jbtSetAllRightsSelectorPres.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetAllRightsSelectorPres.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetAllRightsSelectorPres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetAllRightsSelectorPresActionPerformed(evt);
            }
        });
        jToolBar14.add(jbtSetAllRightsSelectorPres);

        jbtSetAccessRightsSelectorPres.setIcon(RadixWareDesignerIcon.RIGHTS.ACCESS_ONLY.getIcon());
        jbtSetAccessRightsSelectorPres.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAccessRightsSelectorPres.text")); // NOI18N
        jbtSetAccessRightsSelectorPres.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetAccessRightsSelectorPres.toolTipText")); // NOI18N
        jbtSetAccessRightsSelectorPres.setFocusable(false);
        jbtSetAccessRightsSelectorPres.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetAccessRightsSelectorPres.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetAccessRightsSelectorPres.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetAccessRightsSelectorPres.setName("jbtSetAccessRightsSelectorPres"); // NOI18N
        jbtSetAccessRightsSelectorPres.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetAccessRightsSelectorPres.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetAccessRightsSelectorPres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetAccessRightsSelectorPresActionPerformed(evt);
            }
        });
        jToolBar14.add(jbtSetAccessRightsSelectorPres);

        jPanel20.add(jToolBar14, java.awt.BorderLayout.PAGE_START);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 1.0;
        jPanel17.add(jPanel20, gridBagConstraints);

        jPanel21.setName("jPanel21"); // NOI18N
        jPanel21.setLayout(new java.awt.BorderLayout());

        jScrollPaneCmdCurr1.setName("jScrollPaneCmdCurr1"); // NOI18N

        jtblCurrentSelectorEnabledCmd.setBackground(javax.swing.UIManager.getDefaults().getColor("TableHeader.background"));
        jtblCurrentSelectorEnabledCmd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Enabled Command", "Inherit", "Own", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtblCurrentSelectorEnabledCmd.setName("jtblCurrentSelectorEnabledCmd"); // NOI18N
        jtblCurrentSelectorEnabledCmd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblCurrentSelectorEnabledCmdMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtblCurrentSelectorEnabledCmdMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jtblCurrentSelectorEnabledCmdMouseReleased(evt);
            }
        });
        jtblCurrentSelectorEnabledCmd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jtblCurrentSelectorEnabledCmdPropertyChange(evt);
            }
        });
        jtblCurrentSelectorEnabledCmd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblCurrentSelectorEnabledCmdKeyReleased(evt);
            }
        });
        jScrollPaneCmdCurr1.setViewportView(jtblCurrentSelectorEnabledCmd);

        jPanel21.add(jScrollPaneCmdCurr1, java.awt.BorderLayout.CENTER);

        jToolBar15.setFloatable(false);
        jToolBar15.setName("jToolBar15"); // NOI18N

        jbtSetSelectorPresCmd.setIcon(RadixWareDesignerIcon.DIALOG.CHECK.SET.getIcon());
        jbtSetSelectorPresCmd.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetSelectorPresCmd.text")); // NOI18N
        jbtSetSelectorPresCmd.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtSetSelectorPresCmd.toolTipText")); // NOI18N
        jbtSetSelectorPresCmd.setFocusable(false);
        jbtSetSelectorPresCmd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetSelectorPresCmd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetSelectorPresCmd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetSelectorPresCmd.setName("jbtSetSelectorPresCmd"); // NOI18N
        jbtSetSelectorPresCmd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetSelectorPresCmd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetSelectorPresCmd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetSelectorPresCmdActionPerformed(evt);
            }
        });
        jToolBar15.add(jbtSetSelectorPresCmd);

        jbtUnSetSelectorPresCmd.setIcon(RadixWareDesignerIcon.CHECK.UNSET.getIcon());
        jbtUnSetSelectorPresCmd.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtUnSetSelectorPresCmd.text")); // NOI18N
        jbtUnSetSelectorPresCmd.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtUnSetSelectorPresCmd.toolTipText")); // NOI18N
        jbtUnSetSelectorPresCmd.setFocusable(false);
        jbtUnSetSelectorPresCmd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtUnSetSelectorPresCmd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtUnSetSelectorPresCmd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtUnSetSelectorPresCmd.setName("jbtUnSetSelectorPresCmd"); // NOI18N
        jbtUnSetSelectorPresCmd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtUnSetSelectorPresCmd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtUnSetSelectorPresCmd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtUnSetSelectorPresCmdActionPerformed(evt);
            }
        });
        jToolBar15.add(jbtUnSetSelectorPresCmd);

        jPanel21.add(jToolBar15, java.awt.BorderLayout.PAGE_START);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel17.add(jPanel21, gridBagConstraints);

        jlSelectorPresTitle.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jlSelectorPresTitle.text")); // NOI18N
        jlSelectorPresTitle.setName("jlSelectorPresTitle"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel17.add(jlSelectorPresTitle, gridBagConstraints);

        jSplitPane5.setRightComponent(jPanel17);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(jSplitPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jPanel9.TabConstraints.tabTitle"), jPanel9); // NOI18N

        jSplitPane1.setRightComponent(jTabbedPane2);

        jScrollPaneEntitys.setMinimumSize(new java.awt.Dimension(27, 1));
        jScrollPaneEntitys.setName("jScrollPaneEntitys"); // NOI18N
        jSplitPane1.setTopComponent(jScrollPaneEntitys);

        jPanel7.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
        );

        jResourcePane.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditor.jPanel15.TabConstraints.tabTitle"), jPanel16); // NOI18N

        jPanel18.setName("jPanel17"); // NOI18N
        jPanel18.setLayout(new java.awt.BorderLayout());

        jScrollPaneContextlessCommands.setName("jScrollPaneContextlessCommands"); // NOI18N
        jPanel18.add(jScrollPaneContextlessCommands, java.awt.BorderLayout.CENTER);

        jToolBar5.setFloatable(false);
        jToolBar5.setName("jToolBar5"); // NOI18N

        jbtCCmdAllowedAll.setIcon(RadixWareDesignerIcon.DIALOG.OK.getIcon());
        jbtCCmdAllowedAll.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtCCmdAllowedAll.text_1")); // NOI18N
        jbtCCmdAllowedAll.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtCCmdAllowedAll.toolTipText")); // NOI18N
        jbtCCmdAllowedAll.setFocusable(false);
        jbtCCmdAllowedAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtCCmdAllowedAll.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtCCmdAllowedAll.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtCCmdAllowedAll.setName("jbtCCmdAllowedAll"); // NOI18N
        jbtCCmdAllowedAll.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtCCmdAllowedAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtCCmdAllowedAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCCmdAllowedAllActionPerformed(evt);
            }
        });
        jToolBar5.add(jbtCCmdAllowedAll);

        jbtCCmdInheritAll.setIcon(RadixWareDesignerIcon.FILE.NEW_DOCUMENT.getIcon());
        jbtCCmdInheritAll.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtCCmdInheritAll.text")); // NOI18N
        jbtCCmdInheritAll.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtCCmdInheritAll.toolTipText")); // NOI18N
        jbtCCmdInheritAll.setFocusable(false);
        jbtCCmdInheritAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtCCmdInheritAll.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtCCmdInheritAll.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtCCmdInheritAll.setName("jbtCCmdInheritAll"); // NOI18N
        jbtCCmdInheritAll.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtCCmdInheritAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtCCmdInheritAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCCmdInheritAllActionPerformed(evt);
            }
        });
        jToolBar5.add(jbtCCmdInheritAll);

        jSeparator11.setName("jSeparator11"); // NOI18N
        jToolBar5.add(jSeparator11);

        jbtAddCCmd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAddCCmd.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddCCmd.text")); // NOI18N
        jbtAddCCmd.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddCCmd.toolTipText")); // NOI18N
        jbtAddCCmd.setFocusable(false);
        jbtAddCCmd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddCCmd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddCCmd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddCCmd.setName("jbtAddCCmd"); // NOI18N
        jbtAddCCmd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddCCmd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddCCmd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddCCmdActionPerformed(evt);
            }
        });
        jToolBar5.add(jbtAddCCmd);

        jbtDelCCmd.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDelCCmd.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelCCmd.text")); // NOI18N
        jbtDelCCmd.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelCCmd.toolTipText")); // NOI18N
        jbtDelCCmd.setFocusable(false);
        jbtDelCCmd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelCCmd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelCCmd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelCCmd.setName("jbtDelCCmd"); // NOI18N
        jbtDelCCmd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelCCmd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelCCmd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelCCmdActionPerformed(evt);
            }
        });
        jToolBar5.add(jbtDelCCmd);

        jSeparatorGoToCCmd.setMaximumSize(new java.awt.Dimension(6, 32));
        jSeparatorGoToCCmd.setName("jSeparatorGoToCCmd"); // NOI18N
        jToolBar5.add(jSeparatorGoToCCmd);

        jbtGoToCCmd.setIcon(RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon());
        jbtGoToCCmd.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToCCmd.text")); // NOI18N
        jbtGoToCCmd.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToCCmd.toolTipText")); // NOI18N
        jbtGoToCCmd.setFocusable(false);
        jbtGoToCCmd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtGoToCCmd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtGoToCCmd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtGoToCCmd.setName("jbtGoToCCmd"); // NOI18N
        jbtGoToCCmd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtGoToCCmd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtGoToCCmd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtGoToCCmdActionPerformed(evt);
            }
        });
        jToolBar5.add(jbtGoToCCmd);

        jPanel18.add(jToolBar5, java.awt.BorderLayout.PAGE_START);

        jResourcePane.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditor.jPanel17.TabConstraints.tabTitle"), jPanel18); // NOI18N

        resourcePane2.add(jResourcePane, java.awt.BorderLayout.CENTER);

        jToolBar7.setFloatable(false);
        jToolBar7.setRollover(true);
        jToolBar7.setName("jToolBar7"); // NOI18N
        resourcePane2.add(jToolBar7, java.awt.BorderLayout.PAGE_START);

        mainTabbedPane4.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.resourcePane2.TabConstraints.tabTitle"), resourcePane2); // NOI18N

        apfamilyPane2.setName("apfamilyPane2"); // NOI18N
        apfamilyPane2.setLayout(new java.awt.BorderLayout());

        jToolBar10.setFloatable(false);
        jToolBar10.setName("jToolBar8"); // NOI18N

        jbtDelAPFamily.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDelAPFamily.setText(null);
        jbtDelAPFamily.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtDelAPFamily.toolTipText")); // NOI18N
        jbtDelAPFamily.setFocusable(false);
        jbtDelAPFamily.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelAPFamily.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelAPFamily.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelAPFamily.setName("jButton17"); // NOI18N
        jbtDelAPFamily.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelAPFamily.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelAPFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelAPFamilyActionPerformed(evt);
            }
        });
        jToolBar10.add(jbtDelAPFamily);

        jbtClearAPFamily.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        jbtClearAPFamily.setFocusable(false);
        jbtClearAPFamily.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtClearAPFamily.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtClearAPFamily.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtClearAPFamily.setName("jButton18"); // NOI18N
        jbtClearAPFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtClearAPFamilyActionPerformed(evt);
            }
        });
        jToolBar10.add(jbtClearAPFamily);

        jSeparator19.setName("jSeparator19"); // NOI18N
        jToolBar10.add(jSeparator19);

        jbtAddAutoAPFamily.setIcon(RadixWareDesignerIcon.TREE.DEPENDENCIES.getIcon());
        jbtAddAutoAPFamily.setText(null);
        jbtAddAutoAPFamily.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtAddAutoAPFamily.toolTipText")); // NOI18N
        jbtAddAutoAPFamily.setFocusable(false);
        jbtAddAutoAPFamily.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddAutoAPFamily.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddAutoAPFamily.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddAutoAPFamily.setName("jbtAddAutoAPFamily"); // NOI18N
        jbtAddAutoAPFamily.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddAutoAPFamily.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddAutoAPFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddAutoAPFamilyActionPerformed(evt);
            }
        });
        jToolBar10.add(jbtAddAutoAPFamily);

        jSeparatorGoToAPFamily.setName("jSeparatorGoToAPFamily"); // NOI18N
        jToolBar10.add(jSeparatorGoToAPFamily);

        jbtGoToAPFamily.setIcon(RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon());
        jbtGoToAPFamily.setText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToAPFamily.text")); // NOI18N
        jbtGoToAPFamily.setToolTipText(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.jbtGoToAPFamily.toolTipText")); // NOI18N
        jbtGoToAPFamily.setFocusable(false);
        jbtGoToAPFamily.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtGoToAPFamily.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtGoToAPFamily.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtGoToAPFamily.setName("jbtGoToAPFamily"); // NOI18N
        jbtGoToAPFamily.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtGoToAPFamily.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtGoToAPFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtGoToAPFamilyActionPerformed(evt);
            }
        });
        jToolBar10.add(jbtGoToAPFamily);

        apfamilyPane2.add(jToolBar10, java.awt.BorderLayout.PAGE_START);

        jScrollPane8.setAutoscrolls(true);
        jScrollPane8.setName("jScrollPane6"); // NOI18N

        jAPFamilyList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jAPFamilyList.setDragEnabled(true);
        jAPFamilyList.setName("jList6"); // NOI18N
        jAPFamilyList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jAPFamilyListMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jAPFamilyListMouseReleased(evt);
            }
        });
        jAPFamilyList.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jAPFamilyListCaretPositionChanged(evt);
            }
        });
        jAPFamilyList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jAPFamilyListKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(jAPFamilyList);

        apfamilyPane2.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        mainTabbedPane4.addTab(org.openide.util.NbBundle.getMessage(AdsRoleEditorPanel.class, "AdsRoleEditorPanel.apfamilyPane2.TabConstraints.tabTitle"), apfamilyPane2); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabbedPane4)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabbedPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 10000.0;
        gridBagConstraints.weighty = 10000.0;
        add(jPanel4, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jAncestorListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jAncestorListMouseClicked
        refreshAncestorsButtons();
    }//GEN-LAST:event_jAncestorListMouseClicked

    private void jAncestorListCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jAncestorListCaretPositionChanged
        refreshAncestorsButtons();
    }//GEN-LAST:event_jAncestorListCaretPositionChanged

    private void jAPFamilyListCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jAPFamilyListCaretPositionChanged
        refreshAPFButtons();
    }//GEN-LAST:event_jAPFamilyListCaretPositionChanged

    private void jAPFamilyListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jAPFamilyListMouseClicked
        refreshAPFButtons();
    }//GEN-LAST:event_jAPFamilyListMouseClicked

    void setBranch(DefaultMutableTreeNode val, int x) {

        if (val.getChildCount() > 0) {
            for (DefaultMutableTreeNode chld = (DefaultMutableTreeNode) val.getFirstChild(); chld != null; chld = chld.getNextSibling()) {
                setBranch(chld, x);
            }
        }

        FooItemWithIcon itemPtr = (FooItemWithIcon) val.getUserObject();
        AdsExplorerItemDef item = (AdsExplorerItemDef) itemPtr.GetMyObject();
        if (x == 0) {
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    lastExplorerRootId,
                    val.isRoot() ? null : item.getId());
            role.RemoveResourceRestrictions(hash);
            Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            itemPtr.setIsBold(!ar.isDenied(ERestriction.ACCESS));
        } else if (x == 1) {
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    lastExplorerRootId,
                    val.isRoot() ? null : item.getId(),
                    Restrictions.Factory.newInstance(role, 0)));
            itemPtr.setIsBold(true);
        } else if (x == 2) {
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    lastExplorerRootId,
                    val.isRoot() ? null : item.getId(),
                    Restrictions.Factory.newInstance(role, ERestriction.ACCESS.getValue().longValue())));
            itemPtr.setIsBold(false);
        }
    }

    void setBranch(AdsExplorerItemDef val, int x, boolean isRoot, Id rootId) {
        if (val instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) val;
            for (AdsExplorerItemDef item : par.getExplorerItems().getChildren().getLocal()) {
                setBranch(item, x, false, rootId);
            }
        } else if (val instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) val;
            AdsParagraphExplorerItemDef par2 = def.findReferencedParagraph();
            if (par2 != null) {
                setBranch(par2, x, false, rootId);
            }


        }

        if (x == 0) {
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    rootId,
                    isRoot ? null : val.getId());
            role.RemoveResourceRestrictions(hash);
        } else if (x == 1) {
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    rootId,
                    isRoot ? null : val.getId(),
                    Restrictions.Factory.newInstance(role, 0)));
        } else if (x == 2) {
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    rootId,
                    isRoot ? null : val.getId(),
                    Restrictions.Factory.newInstance(role, ERestriction.ACCESS.getValue().longValue())));
        }
    }
    private void jbtCCmdInheritAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCCmdInheritAllActionPerformed

        String sPrefix;
        if (role.getAncestorIds().size() > 0 || role.isOverwrite()) {
            sPrefix = "Inherit ";
        } else {
            sPrefix = "Delete ";
        }
        if (!DialogUtils.messageConfirmation(sPrefix + "execution rights for all commands?")) {
            return;
        }

        List<CoverFoo> contextlessCommands2 = new ArrayList<>(0);
        HashMap<Id, CoverFoo> contextlessCommandsMap2 = new HashMap<>();

        for (CoverFoo cmd : contextlessCommands) {
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.CONTEXTLESS_COMMAND,
                    cmd.getId(), null);
            if (cmd.item != null) {
                Restrictions rest = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
                if (rest.isDenied(ERestriction.ACCESS)) {
                    contextlessCommandsMap2.put(cmd.getId(), cmd);
                } else {
                    contextlessCommands2.add(cmd);
                }
            }
            role.RemoveResourceRestrictions(hash);
        }
        final Iterator<CoverFoo> iter = contextlessCommandsMap.values().iterator();
        while (iter.hasNext()) {
            final CoverFoo cmd = iter.next();
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.CONTEXTLESS_COMMAND,
                    cmd.getId(), null);
            Restrictions rest = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            if (rest.isDenied(ERestriction.ACCESS)) {
                contextlessCommandsMap2.put(cmd.getId(), cmd);
            } else {
                contextlessCommands2.add(cmd);
            }
            role.RemoveResourceRestrictions(hash);
        }

        contextlessCommands = contextlessCommands2;
        contextlessCommandsMap = contextlessCommandsMap2;
        setContextlessCommands();

}//GEN-LAST:event_jbtCCmdInheritAllActionPerformed

    public void clearAllPresentations(AdsEntityClassDef ecd) {
    }

    private void jbtInheritEditorPresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtInheritEditorPresActionPerformed
        String sPrefix;
        if (role.getAncestorIds().size() > 0 || role.isOverwrite()) {
            sPrefix = "Inherit ";
        } else {
            sPrefix = "Delete ";
        }
        if (!DialogUtils.messageConfirmation(sPrefix + "rights for all editor and selector presentations and theirs explorer items?")) {
            return;
        }


        Iterator<AdsRoleDef.Resource> iter = role.getResources().iterator();
        int size = 0;
        while (iter.hasNext()) {
            AdsRoleDef.Resource res = iter.next();
            if (res.type.equals(EDrcResourceType.EDITOR_PRESENTATION)
                    || res.type.equals(EDrcResourceType.SELECTOR_PRESENTATION)) {
                size++;
            }
        }
        String hashes[] = new String[size];
        size = 0;
        iter = role.getResources().iterator();
        while (iter.hasNext()) {
            AdsRoleDef.Resource res = iter.next();
            if (res.type.equals(EDrcResourceType.EDITOR_PRESENTATION)
                    || res.type.equals(EDrcResourceType.SELECTOR_PRESENTATION)) {
                hashes[size++] = AdsRoleDef.generateResHashKey(res);
            }
        }

        List<Id> mustRemoveIncorrectIdsList = new ArrayList<>(0);
        Iterator<Entry<Id, CoverEntityObjectClasses>> iterator = entityObjectClassesMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Id, CoverEntityObjectClasses> e = iterator.next();
            if (e.getValue().clazz == null) {
                mustRemoveIncorrectIdsList.add(e.getKey());
            }
        }

        for (int i = 0; i < size; i++) {
            role.RemoveResourceRestrictions(hashes[i]);
        }

        for (Id id : mustRemoveIncorrectIdsList) {
            entityObjectClassesMap.remove(id);
        }
        setClasses();
        stateEntityClassDisableStatus();



}//GEN-LAST:event_jbtInheritEditorPresActionPerformed

    private void jbtExplRootInheritAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExplRootInheritAllActionPerformed
        String sPrefix;
        if (role.getAncestorIds().size() > 0 || role.isOverwrite()) {
            sPrefix = "Inherit ";
        } else {
            sPrefix = "Delete ";
        }
        if (!DialogUtils.messageConfirmation(sPrefix + "rights for all explorer roots and theirs subitems?")) {
            return;
        }

        List<CoverFoo> paragraphs2 = new ArrayList<>(0);
        HashMap<Id, CoverFoo> paragraphsMap2 = new HashMap<>();

        for (CoverFoo par : paragraphs) {
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    par.getId(), null);
            Restrictions rest = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            if (rest.isDenied(ERestriction.ACCESS)) {
                paragraphsMap2.put(par.getId(), par);
            } else {
                paragraphs2.add(par);
            }
            if (par.getParagraphItem() != null) {
                setBranch(par.getParagraphItem(), 0, true, par.getParagraphItem().getId());
            } else {
                role.RemoveResourceRestrictions(hash);
            }


        }

        final Iterator<CoverFoo> iter = paragraphsMap.values().iterator();
        while (iter.hasNext()) {
            final CoverFoo par = iter.next();
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    par.getParagraphItem().getId(), null);
            Restrictions rest = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            if (rest.isDenied(ERestriction.ACCESS)) {
                paragraphsMap2.put(par.getParagraphItem().getId(), par);
            } else {
                paragraphs2.add(par);
            }
            setBranch(par.getParagraphItem(), 0, true, par.getParagraphItem().getId());
        }

        paragraphs = paragraphs2;
        paragraphsMap = paragraphsMap2;

        setParagraphs();

        fillExplorerItemsTree();

}//GEN-LAST:event_jbtExplRootInheritAllActionPerformed

    private void jbtServerInheritAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtServerInheritAllActionPerformed
        String sPrefix;
        if (role.getAncestorIds().size() > 0 || role.isOverwrite()) {
            sPrefix = "Inherit ";
        } else {
            sPrefix = "Delete ";
        }
        if (!DialogUtils.messageConfirmation(sPrefix + "rights for all resources?")) {
            return;
        }
        for (int i = 0; i < SERVER_RESOURCES_SIZE; i++) {
            role.RemoveResourceRestrictions(
                    AdsRoleDef.generateResHashKey(EDrcResourceType.SERVER_RESOURCE, indexToServerResource(i)));
            jtblServerResource.setValueAt(jtblServerResource.getValueAt(i, 0), i, 0);
            jtblServerResource.setValueAt(Inherit, i, 2);
            jtblServerResource.setValueAt(jtblServerResource.getValueAt(i, 1), i, 3);
            jtblServerResource.setValueAt(Boolean.FALSE, i, 4);
        }

}//GEN-LAST:event_jbtServerInheritAllActionPerformed

    private void jbtSelectAllCmdForEdPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSelectAllCmdForEdPrActionPerformed


        if (currCoverEditorPresentation == null
                || editorCommandsList == null
                || currCoverEditorPresentation.epr == null) {
            return;
        }

        Restrictions res = role.getResourceRestrictions(EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.epr.getOwnerClass().getId(),
                currCoverEditorPresentation.epr.getId(),
                currCoverEditorPresentation.epr);
        for (AdsScopeCommandDefCover cmd : editorCommandsList) {
            res.setCommandEnabled(cmd.getId(), true);
        }

        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.epr.getOwnerClass().getId(),
                currCoverEditorPresentation.epr.getId(),
                res);
        role.CreateOrReplaceResourceRestrictions(resource);
        fillEditorPresentationRightsAndCommand();
    }//GEN-LAST:event_jbtSelectAllCmdForEdPrActionPerformed

    private void jbtDeselectAllCmdForEdPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDeselectAllCmdForEdPrActionPerformed
        if (currCoverEditorPresentation == null
                || currCoverEditorPresentation.epr == null
                || editorCommandsList == null) {
            return;
        }

        Restrictions res = role.getResourceRestrictions(EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.epr.getOwnerClass().getId(),
                currCoverEditorPresentation.epr.getId(),
                currCoverEditorPresentation.epr);
        String hash = AdsRoleDef.generateResHashKey(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId);

        Restrictions or = role.getOverwriteResourceRestrictions(hash, currCoverEditorPresentation.getEditorPresentation());
        List<Id> lst = or.getEnabledCommandIds();

        for (AdsScopeCommandDefCover cmd : editorCommandsList) {
            if (!lst.contains(cmd.getId())) {
                res.setCommandEnabled(cmd.getId(), false);
            }
        }

        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.epr.getOwnerClass().getId(),
                currCoverEditorPresentation.epr.getId(),
                res);
        role.CreateOrReplaceResourceRestrictions(resource);
        fillEditorPresentationRightsAndCommand();

    }//GEN-LAST:event_jbtDeselectAllCmdForEdPrActionPerformed

    private void jbtServerAllowedAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtServerAllowedAllActionPerformed

        if (!DialogUtils.messageConfirmation("Allow all resources?")) {
            return;
        }


        for (int i = 0; i < SERVER_RESOURCES_SIZE; i++) {
            EDrcServerResource resource = indexToServerResource(i);
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.SERVER_RESOURCE,
                    Id.Factory.loadFrom(resource.getValue()),
                    null,
                    Restrictions.Factory.newInstance(role, 0)));

            jtblServerResource.setValueAt(jtblServerResource.getValueAt(i, 0), i, 0);
            jtblServerResource.setValueAt(Allowed, i, 2);
            jtblServerResource.setValueAt(Allowed, i, 3);
            jtblServerResource.setValueAt(Boolean.TRUE, i, 4);
        }

    }//GEN-LAST:event_jbtServerAllowedAllActionPerformed

    private void jbtExplRootAllowedAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExplRootAllowedAllActionPerformed

        if (!DialogUtils.messageConfirmation("Allow access to all explorer roots and theirs subitems?")) {
            return;
        }
        final Iterator<CoverFoo> iter = paragraphsMap.values().iterator();
        while (iter.hasNext()) {
            final CoverFoo par = iter.next();
            paragraphs.add(par);
        }
        paragraphsMap.clear();
        for (CoverFoo par : paragraphs) {
            setBranch(par.getParagraphItem(), 1, true, par.getParagraphItem().getId());
        }
        RadixObjectsUtils.sortByQualifiedName(paragraphs);
        setParagraphs();

        fillExplorerItemsTree();
        if (jtblExplorerRoots.getRowCount() > 0) {
            jtblExplorerRoots.setRowSelectionInterval(0, 0);
        }


    }//GEN-LAST:event_jbtExplRootAllowedAllActionPerformed

    private void jbtExplItemAllowedTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExplItemAllowedTreeActionPerformed



        int row = treeTableExplorerItems.getSelectedRow();
        if (treeTableExplorerItems.getRowCount() == 0 || row < 0) {
            return;
        }
        if (!DialogUtils.messageConfirmation("Allow access to branch and all its subitems?")) {
            return;
        }


        TreeGridModel.TreeGridNode tgn = (TreeGridModel.TreeGridNode) treeTableExplorerItems.getValueAt(row, 0);
        TreeGridRoleResourceRow item = (TreeGridRoleResourceRow) tgn.getGridItem();
        item.setInheritOrAllow(false);
        Id parentId = item.parentId;
        TreeGridRoleResourceRow item2 = item;
        while (item2 != null) {
            role.CreateOrReplaceResourceRestrictions(
                    new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    item2.parentId,
                    parentId == item2.explorerItem.getId() ? null : item2.explorerItem.getId(),
                    Restrictions.Factory.newInstance(0)));
            item2.loadValues();
            item2 = item2.parent;
        }

        treeTableExplorerItems.repaint();
        new CurrParagraphRightChecker().check();

    }//GEN-LAST:event_jbtExplItemAllowedTreeActionPerformed

    private void jbtExplItemInheritTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExplItemInheritTreeActionPerformed

        int row = treeTableExplorerItems.getSelectedRow();
        if (treeTableExplorerItems.getRowCount() == 0 || row < 0) {
            return;
        }
        if (!DialogUtils.messageConfirmation("Inherit access rights for branch and all its subitems?")) {
            return;
        }

        TreeGridModel.TreeGridNode tgn = (TreeGridModel.TreeGridNode) treeTableExplorerItems.getValueAt(row, 0);
        TreeGridRoleResourceRow item = (TreeGridRoleResourceRow) tgn.getGridItem();
        item.setInheritOrAllow(true);
        treeTableExplorerItems.repaint();
        new CurrParagraphRightChecker().check();
    }//GEN-LAST:event_jbtExplItemInheritTreeActionPerformed

    private void jbtCCmdAllowedAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCCmdAllowedAllActionPerformed

        if (!DialogUtils.messageConfirmation("Allow execution of all commands?")) {
            return;
        }

        final Iterator<CoverFoo> iter = contextlessCommandsMap.values().iterator();
        while (iter.hasNext()) {
            final CoverFoo cmd = iter.next();
            contextlessCommands.add(cmd);
        }
        contextlessCommandsMap.clear();
        for (CoverFoo cmd : contextlessCommands) {
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.CONTEXTLESS_COMMAND,
                    cmd.getId(),
                    null,
                    Restrictions.Factory.newInstance(role, 0)));
        }

        RadixObjectsUtils.sortByQualifiedName(contextlessCommands);
        setContextlessCommands();


        if (jtblContextlessCommands.getRowCount() > 0) {
            jtblContextlessCommands.setRowSelectionInterval(0, 0);
        }

    }//GEN-LAST:event_jbtCCmdAllowedAllActionPerformed

    private void jbtDelAPFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelAPFamilyActionPerformed
        DefaultListModel model = (DefaultListModel) jAPFamilyList.getModel();
        int index = jAPFamilyList.getSelectedIndex();
        if (model.getSize() == 0 || index < overwriteAPF.size()) {
            refreshAPFButtons();
            return;
        }

        FooItem item = (FooItem) model.get(index);

        Id id;//= null;
        if (item.GetMyObject() instanceof Id) {
            id = (Id) item.GetMyObject();
        } else {
            DdsAccessPartitionFamilyDef apf = (DdsAccessPartitionFamilyDef) item.GetMyObject();
            id = apf.getId();
        }

        boolean ok = false;
        if (id != null) {
            ok = role.removeAPFamilieId(id);
        }

        if (!ok) {
            DialogUtils.messageError("Removing failed");
        } else {
            model.remove(index);
            if (index > 0) {
                jAPFamilyList.setSelectedIndex(index - 1);
            } else if (model.getSize() > 0) {
                jAPFamilyList.setSelectedIndex(model.getSize() - 1);
            }

        }
        fillAPFList();

    }//GEN-LAST:event_jbtDelAPFamilyActionPerformed

    private void jbtClearAPFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtClearAPFamilyActionPerformed
        if (!DialogUtils.messageConfirmation("Delete all access partition families?")) {
            return;
        }
        DefaultListModel model = (DefaultListModel) jAPFamilyList.getModel();
        for (int i = model.size() - 1; i >= overwriteAPF.size(); i--) {
            model.remove(i);
        }
        if (model.size() > 0) {
            jAPFamilyList.setSelectionInterval(0, 0);
        }

        role.clearAPFamilieIds();

        fillAPFList();
    }//GEN-LAST:event_jbtClearAPFamilyActionPerformed

    private void mainTabbedPane4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainTabbedPane4StateChanged
        if (isMustRefreshInheritRihghts && mainTabbedPane4.getSelectedIndex() == 2) {
            fillServerResourceTable();
            fillResources();
            isMustRefreshInheritRihghts = false;
        }
    }//GEN-LAST:event_mainTabbedPane4StateChanged

    private void jbtGoToCCmdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtGoToCCmdActionPerformed
        if (jtblContextlessCommands.getRowCount() == 0 || jtblContextlessCommands.getSelectedRow() < 0) {
            return;
        }
        CoverFoo cmd = contextlessCommands.get(jtblContextlessCommands.getSelectedRow());
        if (cmd.getCmdItem() != null) {
            NodesManager.selectInProjects(cmd.getCmdItem());
        }
}//GEN-LAST:event_jbtGoToCCmdActionPerformed

    private void jbtGoToEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtGoToEntityActionPerformed
        if (jEntityAndApplTree.getSelectionPath() == null) {
            return;
        }
        Object currObject = jEntityAndApplTree.getSelectionPath().getLastPathComponent();
        DefaultMutableTreeNodeEx currNode = (DefaultMutableTreeNodeEx) currObject;
        currCoverEntityObjectClasses = (CoverEntityObjectClasses) currNode.obj;
        if (currCoverEntityObjectClasses.clazz != null) {
            NodesManager.selectInProjects(currCoverEntityObjectClasses.clazz);
        }
}//GEN-LAST:event_jbtGoToEntityActionPerformed

    private void jbtGoToExplorerRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtGoToExplorerRootActionPerformed
        if (jtblExplorerRoots.getRowCount() == 0 || jtblExplorerRoots.getSelectedRow() < 0) {
            return;
        }
        AdsParagraphExplorerItemDef par = paragraphs.get(jtblExplorerRoots.getSelectedRow()).getParagraphItem();
        if (par != null) {
            NodesManager.selectInProjects(par);
        }
}//GEN-LAST:event_jbtGoToExplorerRootActionPerformed

    private void jbtAddAncestorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddAncestorActionPerformed

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(role.getModule(), new RoleAncestorVisitorProvider());
        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def != null) {
            DefaultListModel model = (DefaultListModel) jAncestorList.getModel();
            model.add(model.size(), new FooItem(def.getQualifiedName(), def));

            role.addAncestorId(def.getId());
            jAncestorList.setSelectedIndex(model.getSize() - 1);
        }
        refreshAncestorsButtons();
        isMustRefreshInheritRihghts = true;

    }//GEN-LAST:event_jbtAddAncestorActionPerformed

    private void jbtDelAncestorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelAncestorActionPerformed
        DefaultListModel model = (DefaultListModel) jAncestorList.getModel();
        int index = jAncestorList.getSelectedIndex();
        if (model.getSize() == 0 || index < overwriteAncestors.size()) {
            refreshAncestorsButtons();
            return;
        }
        FooItem item = (FooItem) model.get(index);
        Id id;// = null;
        if (item.GetMyObject() instanceof Id) {
            id = (Id) item.GetMyObject();
        } else {
            AdsRoleDef r = (AdsRoleDef) item.GetMyObject();
            id = r.getId();
        }
        boolean ok = false;
        if (id != null) {
            ok = role.removeAncestorId(id);
        }
        if (!ok) {
            DialogUtils.messageError("Removing failed");
        } else {
            model.remove(index);
            if (index > 0) {
                jAncestorList.setSelectedIndex(index - 1);
            } else if (model.getSize() > 0) {
                jAncestorList.setSelectedIndex(model.getSize() - 1);
            }
        }
        refreshAncestorsButtons();
        isMustRefreshInheritRihghts = true;

    }//GEN-LAST:event_jbtDelAncestorActionPerformed

    private void jbtClearAncestorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtClearAncestorsActionPerformed

        if (!DialogUtils.messageConfirmation("Delete all ancesors?")) {
            return;
        }
        DefaultListModel model = (DefaultListModel) jAncestorList.getModel();
        for (int i = model.size() - 1; i >= overwriteAncestors.size(); i--) {
            model.remove(i);
        }
        if (model.size() > 0) {
            jAncestorList.setSelectionInterval(0, 0);
        }


        role.clearAncestorIds();
        refreshAncestorsButtons();
        isMustRefreshInheritRihghts = true;

    }//GEN-LAST:event_jbtClearAncestorsActionPerformed

    private void jbtAllowedEditorPresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAllowedEditorPresActionPerformed



        if (!DialogUtils.messageConfirmation("Allow access to all editor and selector presentations and theirs explorer items?")) {
            return;
        }

        Iterator<CoverEntityObjectClasses> iter = entityObjectClassesMap.values().iterator();
        while (iter.hasNext()) {
            CoverEntityObjectClasses cover = iter.next();
            List<AdsEditorPresentationDef> ePresList;
            List<AdsSelectorPresentationDef> sPresList;

            if (cover.clazz == null) {
                ePresList = new ArrayList<>(0);
                sPresList = new ArrayList<>(0);
            } else {
                ePresList = cover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
                removeOverwriteItems(ePresList);
                sPresList = cover.clazz.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
                removeOverwriteItems(sPresList);
            }


            for (AdsEditorPresentationDef ePres : ePresList) {
                role.CreateOrReplaceResourceRestrictions(
                        new AdsRoleDef.Resource(
                        EDrcResourceType.EDITOR_PRESENTATION,
                        ePres.getOwnerClass().getId(), ePres.getId(),
                        Restrictions.Factory.newInstance(role, 0)));
            }
            for (AdsSelectorPresentationDef sPres : sPresList) {
                role.CreateOrReplaceResourceRestrictions(
                        new AdsRoleDef.Resource(
                        EDrcResourceType.SELECTOR_PRESENTATION,
                        sPres.getOwnerClass().getId(), sPres.getId(),
                        Restrictions.Factory.newInstance(role, 0)));
            }
        }
        setClasses();
        stateEntityClassDisableStatus();
    }//GEN-LAST:event_jbtAllowedEditorPresActionPerformed

    private void jbtAddExplorerRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddExplorerRootActionPerformed


        List<Definition> prList = new ArrayList<>(0);
        for (CoverFoo item : paragraphsMap.values()) {
            prList.add(item.getParagraphItem());
        }
        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                role.getModule(),
                null, prList);

        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def != null) {
            String s;
            if (role.getOverwriteAndAncestordResourceRestrictions(
                    AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EXPLORER_ROOT_ITEM, def.getId(), null), null).isDenied(ERestriction.ACCESS)) {
                s = Forbidden;
            } else {
                s = Allowed;
            }

            paragraphsMap.remove(def.getId());
            int index = AdsUsedByRolePanel.findPlace(coverListToAdsDefinitionList(paragraphs), def, false);
            if (index == paragraphs.size()) {
                paragraphs.add(createConverFoo((AdsDefinition) def));
                jtblExplorerRootsModel.addRow(new Object[]{def.getQualifiedName(), s, Allowed, Allowed, Boolean.TRUE});
            } else {
                paragraphs.add(index, createConverFoo((AdsDefinition) def));
                jtblExplorerRootsModel.insertRow(index, new Object[]{def.getQualifiedName(), s, Allowed, Allowed, Boolean.TRUE});
            }

            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    def.getId(),
                    null, Restrictions.Factory.newInstance(role, 0)));
            jtblExplorerRoots.setRowSelectionInterval(index, index);
            jbtGoToExplorerRoot.setEnabled(true);
            jbtDelExplorerRoot.setEnabled(!isSuperAdmin && !roleIsReadOnly);
            fillExplorerItemsTree();

        }
}//GEN-LAST:event_jbtAddExplorerRootActionPerformed

    private void jbtAddEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddEntityActionPerformed



        HashMap<Id, Definition> freeEntityClassesMap = new HashMap<>();
        Iterator<CoverEntityObjectClasses> iter = entityObjectClassesMap.values().iterator();
        while (iter.hasNext()) {
            CoverEntityObjectClasses cover = iter.next();
            if (!cover.isRightsOrRightsInChilds()
                    && (!cover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE).isEmpty()
                    || !cover.clazz.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE).isEmpty())) {
                freeEntityClassesMap.put(cover.clazz.getId(), cover.clazz);
            }
        }
        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                role.getModule(),
                null, freeEntityClassesMap.values());


        //cfg.setDisplayMode(EChooseDefinitionDisplayMode.QUALIFIED_NAME);

        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def != null) {
            CoverEntityObjectClasses cover = entityObjectClassesMap.get(def.getId());
            List<CoverEntityObjectClasses> lst = new ArrayList<>();
            lst.add(cover);
            CoverEntityObjectClasses prt = cover.parent;
            while (prt.clazz != null //invisible root
                    //&& prt.treeNodeEx == null //is in the tree
                    ) {
                lst.add(0, prt);
                prt = prt.parent;
            }
            List<CoverEntityObjectClasses> currChilds = new ArrayList<>();
            //insert
            l:
            for (CoverEntityObjectClasses item : lst) {

                boolean mustContinue = false;
                for (int row = 0; row < jEntityAndApplTree.getRowCount(); row++) {

                    final TreePath treePath = jEntityAndApplTree.getPathForRow(row);
                    final DefaultMutableTreeNodeEx obj = (DefaultMutableTreeNodeEx) treePath.getLastPathComponent();
                    final CoverEntityObjectClasses cover2 = (CoverEntityObjectClasses) obj.obj;
                    if (cover2 == item) {
                        mustContinue = true;
                        break;
                    }
                }
                if (mustContinue) {
                    prt = item;
                    continue;
                }

                currChilds.clear();
                for (CoverEntityObjectClasses chld : prt.childs) {
                    if (chld.isRightsOrRightsInChilds()) {
                        currChilds.add(chld);
                    }
                }


                int index = AdsUsedByRolePanel.findPlace(currChilds, item, false);

                Bool2 bool2 = new Bool2();
                bool2.color = Color.BLACK;// greyColor;
                DefaultMutableTreeNodeEx treeNodeEx = new DefaultMutableTreeNodeEx(item.clazz.getIcon().getIcon(), item, bool2);
                item.treeNodeEx = treeNodeEx;


                {
                    if (index >= prt.treeNodeEx.getChildCount()) {
                        prt.treeNodeEx.add(treeNodeEx);
                    } else {
                        prt.treeNodeEx.insert(treeNodeEx, index);
                    }
                }
                item.setRightsOrRightsInChilds(true);


                ((DefaultTreeModel) jEntityAndApplTree.getModel()).setRoot(
                        (TreeNode) jEntityAndApplTree.getModel().getRoot());
                for (int i = 0; i < jEntityAndApplTree.getRowCount(); i++) {
                    jEntityAndApplTree.expandRow(i);
                }




                prt = item;
            }

            {
                List<AdsEditorPresentationDef> eprList = cover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
                for (AdsEditorPresentationDef pres : eprList) {
                    String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION, cover.clazz.getId(), pres.getId());
                    Restrictions rest = role.getOverwriteAndAncestordResourceRestrictions(hash, pres);
                    role.CreateOrReplaceResourceRestrictions(
                            new AdsRoleDef.Resource(EDrcResourceType.EDITOR_PRESENTATION,
                            pres.getOwnerClass().getId(),
                            pres.getId(),
                            rest));
                }
            }

            {
                List<AdsSelectorPresentationDef> sprList = cover.clazz.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
                for (AdsSelectorPresentationDef pres : sprList) {
                    String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION, cover.clazz.getId(), pres.getId());
                    Restrictions rest = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
                    role.CreateOrReplaceResourceRestrictions(
                            new AdsRoleDef.Resource(EDrcResourceType.SELECTOR_PRESENTATION,
                            pres.getOwnerClass().getId(),
                            pres.getId(),
                            rest));
                }
            }





            TreeNode[] pathes = ((DefaultTreeModel) jEntityAndApplTree.getModel()).getPathToRoot(lst.get(lst.size() - 1).treeNodeEx);
            TreePath path = new TreePath(pathes);
            jEntityAndApplTree.expandPath(path);
            jEntityAndApplTree.setSelectionPath(path);
            jEntityAndApplTree.repaint();



        }


        refreshEditorAndSelectorPresentationList();
        stateEntityClassDisableStatus();

}//GEN-LAST:event_jbtAddEntityActionPerformed

    private void jbtAddCCmdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddCCmdActionPerformed

        List<Definition> prList = new ArrayList<>(0);
        for (CoverFoo item : contextlessCommandsMap.values()) {
            prList.add(item.getCmdItem());
        }
        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                role.getModule(),
                null, prList);
        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def != null) {
            String s;
            if (role.getOverwriteAndAncestordResourceRestrictions(
                    AdsRoleDef.generateResHashKey(
                    EDrcResourceType.CONTEXTLESS_COMMAND, def.getId(), null), null).isDenied(ERestriction.ACCESS)) {
                s = Forbidden;
            } else {
                s = Allowed;
            }

            contextlessCommandsMap.remove(def.getId());
            int index = AdsUsedByRolePanel.findPlace(coverListToAdsDefinitionList(contextlessCommands), def, false);
            if (index == contextlessCommands.size()) {
                contextlessCommands.add(createConverFoo((AdsDefinition) def));
                jtblContextlessCommandsModel.addRow(new Object[]{def.getQualifiedName(), s, Allowed, Allowed, Boolean.TRUE});
            } else {
                contextlessCommands.add(index, createConverFoo((AdsDefinition) def));
                jtblContextlessCommandsModel.insertRow(index, new Object[]{def.getQualifiedName(), s, Allowed, Allowed, Boolean.TRUE});
            }

            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.CONTEXTLESS_COMMAND,
                    def.getId(),
                    null, Restrictions.Factory.newInstance(role, 0)));
            jtblContextlessCommands.setRowSelectionInterval(index, index);
            jbtGoToCCmd.setEnabled(true);
            stateCmdDisableStatus();
        }
}//GEN-LAST:event_jbtAddCCmdActionPerformed

    private void jbtDelExplorerRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelExplorerRootActionPerformed

        int y = jtblExplorerRoots.getSelectedRow();
        if (jtblExplorerRoots.getRowCount() == 0 || y < 0) {
            return;
        }
        CoverFoo par = paragraphs.get(y);

        inheritedAllItemsAtaParagraph(par.getId());


        if (jtblExplorerRoots.getValueAt(y, 1).equals(Allowed)) {
            jtblExplorerRoots.setValueAt(Inherit, y, 2);
            jtblExplorerRoots.setValueAt(Allowed, y, 3);
        } else {
            paragraphs.remove(y);
            if (par.item != null) {
                paragraphsMap.put(par.getId(), par);
            }
            jtblExplorerRootsModel.removeRow(y);
            if (paragraphs.size() > 0) {
                jtblExplorerRoots.setRowSelectionInterval(0, 0);
            }
        }
        fillExplorerItemsTree();

    }//GEN-LAST:event_jbtDelExplorerRootActionPerformed

    private void jbtDelEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelEntityActionPerformed

        if (currCoverEntityObjectClasses == null) {
            return;
        }

        Id classId;
        if (currCoverEntityObjectClasses.clazz == null) {
            classId = currCoverEntityObjectClasses.incorrectId;
        } else {
            classId = currCoverEntityObjectClasses.clazz.getId();
        }
        role.removeAllSubResouses(EDrcResourceType.EDITOR_PRESENTATION, classId);
        role.removeAllSubResouses(EDrcResourceType.SELECTOR_PRESENTATION, classId);
        Bool2 b2 = isEntityDefContaintAllowedPresentation(currCoverEntityObjectClasses.clazz);
        if (b2.isBold() && currCoverEntityObjectClasses.clazz != null) {
            currCoverEntityObjectClasses.treeNodeEx.bool2 = b2;
            jEntityAndApplTree.repaint();
        } else {
            if (currCoverEntityObjectClasses.clazz == null) {
                entityObjectClassesMap.remove(currCoverEntityObjectClasses.incorrectId);
            } else {
                CoverEntityObjectClasses cl =
                        entityObjectClassesMap.get(currCoverEntityObjectClasses.clazz.getId());
                cl.setRightsOrRightsInChilds(false);
            }
            // entityObjectClassesMap.put(currCoverEntityObjectClasses.clazz.getId(), currCoverEntityObjectClasses);
            setClasses();
        }
        refreshEditorAndSelectorPresentationList();
        stateEntityClassDisableStatus();
    }//GEN-LAST:event_jbtDelEntityActionPerformed

    private void jbtDelCCmdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelCCmdActionPerformed
        int y = jtblContextlessCommands.getSelectedRow();
        if (jtblContextlessCommands.getRowCount() == 0 || y < 0) {
            return;
        }
        CoverFoo cmd = contextlessCommands.get(jtblContextlessCommands.getSelectedRow());

        String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.CONTEXTLESS_COMMAND, cmd.getId(), null);
        role.RemoveResourceRestrictions(hash);
        Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);

        if (ar.isDenied(ERestriction.ACCESS)) {
            if (cmd.getCmdItem() != null) {
                contextlessCommandsMap.put(cmd.getId(), cmd);
            }
            contextlessCommands.remove(y);
            jtblContextlessCommandsModel.removeRow(y);
            if (contextlessCommands.size() > 0) {
                jtblContextlessCommands.setRowSelectionInterval(0, 0);
            }
        } else {
            jtblContextlessCommandsModel.setValueAt(Allowed, y, 3);
            jtblContextlessCommandsModel.setValueAt(Inherit, y, 2);
        }
        stateCmdDisableStatus();

    }//GEN-LAST:event_jbtDelCCmdActionPerformed

    public AdsDefinition findAdsDefinition(Id id) {
        for (AdsModule m : adsModules) {
            for (AdsDefinition def : m.getDefinitions().list()) {
                if (def.getId().equals(id)) {
                    return def;
                }
            }
        }
        return null;
    }

    private void jAPFamilyListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jAPFamilyListKeyReleased
        refreshAPFButtons();
    }//GEN-LAST:event_jAPFamilyListKeyReleased

    private void jAPFamilyListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jAPFamilyListMouseReleased
        refreshAPFButtons();
    }//GEN-LAST:event_jAPFamilyListMouseReleased

    private void jAncestorListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jAncestorListKeyReleased
        refreshAncestorsButtons();
    }//GEN-LAST:event_jAncestorListKeyReleased

    private void jAncestorListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jAncestorListMouseReleased
        refreshAncestorsButtons();
    }//GEN-LAST:event_jAncestorListMouseReleased

    private void jtblCurrentEditorEnabledCmdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblCurrentEditorEnabledCmdKeyReleased
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentEditorEnabledCmdKeyReleased

    private void jtblCurrentEditorEnabledCmdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblCurrentEditorEnabledCmdMouseClicked
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentEditorEnabledCmdMouseClicked

    private void jtblCurrentEditorEnabledCmdMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblCurrentEditorEnabledCmdMouseReleased
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentEditorEnabledCmdMouseReleased

    private void jtblCurrentEditorEnabledCmdMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblCurrentEditorEnabledCmdMousePressed
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentEditorEnabledCmdMousePressed

    private void jbtAddAutoAPFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddAutoAPFamilyActionPerformed
        List<DdsAccessPartitionFamilyDef> list = new ArrayList<>(0);
        for (DdsModule m : ddsModules) {
            DdsModelDef nextModel = m.getModelManager().findModel();
            if (nextModel != null) {
                for (DdsAccessPartitionFamilyDef item : nextModel.getAccessPartitionFamilies()) {
                    if (!list.contains(item)) {
                        list.add(item);
                    }
                }
            }
        }



        List<DdsAccessPartitionFamilyDef> newList = role.getDependentAPF();


        AdsRoleDef r = role;
        List<Id> overwriteAPF_ = new ArrayList<>();
        while (r.isOverwrite()) {
            r = (AdsRoleDef) r.getHierarchy().findOverwritten().get();
            if (r == null) {
                break;
            }
            overwriteAPF_.addAll(r.getAPFamilieIds());
        }




        List<Id> newApfList = new ArrayList<>();
        RadixObjectsUtils.sortByQualifiedName(newList);
        for (DdsAccessPartitionFamilyDef apf : newList) {
            if (!overwriteAPF_.contains(apf.getId())) {
                newApfList.add(apf.getId());
            }
        }
        if (!role.getAPFamilieIds().equals(newApfList)) {
            role.clearAPFamilieIds();
            for (Id id : newApfList) {
                role.addAPFamilieId(id);
            }
        }

        fillAPFList();

    }//GEN-LAST:event_jbtAddAutoAPFamilyActionPerformed

    private void jbtInheritEdPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtInheritEdPrActionPerformed
        if (currCoverEntityObjectClasses == null) {
            return;
        }
        if (treeTableEditorPresentationsModel.getRoot() == null) {
            return;
        }

        String sPrefix;
        if (role.getAncestorIds().size() > 0 || role.isOverwrite()) {
            sPrefix = "Inherit ";
        } else {
            sPrefix = "Delete ";
        }
        if (!DialogUtils.messageConfirmation(sPrefix + "rights for all editor presentations and theirs explorer items?")) {
            return;
        }

        List<AdsEditorPresentationDef> currPresentations = new ArrayList<>();

        TreeGridNode node = (TreeGridNode) treeTableEditorPresentationsModel.getRoot();
        TreeGridRoleResourceRowForEditorPresentation rootCover =
                (TreeGridRoleResourceRowForEditorPresentation) node.getGridItem();

        collectThisAndChildEditorPresentation(rootCover.cover, currPresentations);

        //List<AdsEditorPresentationDef> allPresentations = getEditorPresentationList(currCoverEditorPresentation.getEditorPresentation().getOwnerClass());
        //Id clazzId = epd.getOwnerClass().getId();
        //AdsEditorPresentationDef ep = rootCover.cover.epr;
        for (AdsEditorPresentationDef epd : currPresentations) {
            role.RemoveResourceRestrictions(
                    AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EDITOR_PRESENTATION,
                    epd.getOwnerClass().getId(),
                    epd.getId()));
        }
        //CoverEntityObjectClasses
        if (currCoverEntityObjectClasses != null && currCoverEntityObjectClasses.clazz != null) {
            Id clazzId = currCoverEntityObjectClasses.clazz.getId();
            List<Id> lst = allEditorPresantationMap.get(clazzId);
            if (lst != null) {
                for (Id id : lst) {
                    role.RemoveResourceRestrictions(
                            AdsRoleDef.generateResHashKey(
                            EDrcResourceType.EDITOR_PRESENTATION,
                            clazzId,
                            id));
                }
                lst.clear();
            }
        }
        //

        checkEntityDefRowBold();
        refreshEditorAndSelectorPresentationList();
        refreshEditorPresentationTree(null);
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();

    }//GEN-LAST:event_jbtInheritEdPrActionPerformed

    private void jbtDelEdPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelEdPrActionPerformed

        if (currCoverEditorPresentation == null) {
            return;
        }
//        if (entityEditorPresentations.size()==0)return;
//        int index = jtblEditorPresentation.getSelectedRow();
        String hash;
        if (currCoverEditorPresentation.epr != null) {
            hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EDITOR_PRESENTATION,
                    currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                    currCoverEditorPresentation.epr.getId());
        } else {
            hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EDITOR_PRESENTATION,
                    currCoverEditorPresentation.parentId,
                    currCoverEditorPresentation.incorrectId);
            List<Id> lst = allEditorPresantationMap.get(currCoverEditorPresentation.parentId);
            if (lst != null) {
                for (Id currId : lst) {
                    if (currId.equals(currCoverEditorPresentation.incorrectId)) {
                        lst.remove(currId);
                        break;
                    }
                }
            }
        }

        role.RemoveResourceRestrictions(hash);
        treeTableEditorPresentations.repaint();
        refreshEditorPresentationTree(null);
        checkEntityDefRowBold();

        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();
        fillEditorPresentationRightsAndCommand();



    }//GEN-LAST:event_jbtDelEdPrActionPerformed

    private void jbtAddEdPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddEdPrActionPerformed

        if (currCoverEntityObjectClasses == null) {
            return;
        }
        editorPresantationMap.clear();
        collectPresentations(currCoverEntityObjectClasses);
        List<AdsEditorPresentationDef> usingList = new ArrayList<>();

        if (treeTableEditorPresentationsModel.getRoot() != null) {

            TreeGridNode gridNode = (TreeGridNode) treeTableEditorPresentationsModel.getRoot();
            collectVisiblePresentation(usingList, (TreeGridRoleResourceRowForEditorPresentation) gridNode.getGridItem());
        }

        Iterator<Map.Entry<Id, CoverEditorPresentation>> iter =
                editorPresantationMap.entrySet().iterator();


        //AdsEditorPresentationDef pres = entry.getValue().getEditorPresentation();
        HashMap<Definition, Definition> map = new HashMap<>();
        while (iter.hasNext()) {
            Map.Entry<Id, CoverEditorPresentation> entry = iter.next();
            AdsEditorPresentationDef pres = entry.getValue().epr;
            if (pres != null) {
                if (!usingList.contains(pres)) {
                    map.put(pres, pres);
                }
            }
        }

        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                role.getModule(),
                null, map.values());
        AdsEditorPresentationDef epd = (AdsEditorPresentationDef) ChooseDefinition.chooseDefinition(cfg);
        if (epd != null) {

            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EDITOR_PRESENTATION,
                    epd.getOwnerClass().getId(),
                    epd.getId(),
                    Restrictions.Factory.newInstance(role, 0)));
        }
        refreshEditorPresentationTree(epd);
        checkEntityDefRowBold();
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();


    }//GEN-LAST:event_jbtAddEdPrActionPerformed

    private void collectThisAndChildEditorPresentation(
            CoverEditorPresentation cover,
            List<AdsEditorPresentationDef> currPresentations) {
        if (cover.epr != null) {
            currPresentations.add(cover.epr);
        }
        for (CoverEditorPresentation childCover : cover.childs) {
            collectThisAndChildEditorPresentation(childCover, currPresentations);
        }
    }

    private void jbtAllowedEditorPres1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAllowedEditorPres1ActionPerformed
        if (currCoverEntityObjectClasses == null) {
            return;
        }
        if (treeTableEditorPresentationsModel.getRoot() == null) {
            return;
        }
        if (!DialogUtils.messageConfirmation("Allow access to all editor presentations and theirs explorer items?")) {
            return;
        }
        List<AdsEditorPresentationDef> currPresentations = new ArrayList<>();


        TreeGridNode node = (TreeGridNode) treeTableEditorPresentationsModel.getRoot();
        TreeGridRoleResourceRowForEditorPresentation rootCover =
                (TreeGridRoleResourceRowForEditorPresentation) node.getGridItem();


        collectThisAndChildEditorPresentation(rootCover.cover, currPresentations);
        for (AdsEditorPresentationDef epd : currPresentations) {

            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EDITOR_PRESENTATION,
                    epd.getOwnerClass().getId(),
                    epd.getId(),
                    Restrictions.Factory.newInstance(role, 0)));
        }
        checkEntityDefRowBold();
        refreshEditorAndSelectorPresentationList();
        refreshEditorPresentationTree(null);
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();

    }//GEN-LAST:event_jbtAllowedEditorPres1ActionPerformed

    private void jtblCurrentSelectorEnabledCmdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblCurrentSelectorEnabledCmdMouseClicked
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentSelectorEnabledCmdMouseClicked

    private void jtblCurrentSelectorEnabledCmdMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblCurrentSelectorEnabledCmdMousePressed
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentSelectorEnabledCmdMousePressed

    private void jtblCurrentSelectorEnabledCmdMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblCurrentSelectorEnabledCmdMouseReleased
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentSelectorEnabledCmdMouseReleased

    private void jtblCurrentSelectorEnabledCmdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblCurrentSelectorEnabledCmdKeyReleased
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jtblCurrentSelectorEnabledCmdKeyReleased

    private void jtblSelectorPresentationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblSelectorPresentationMouseClicked
        fillSelectorPresentationRightsAndCommand();
    }//GEN-LAST:event_jtblSelectorPresentationMouseClicked

    private void jtblSelectorPresentationMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblSelectorPresentationMousePressed
        fillSelectorPresentationRightsAndCommand();
    }//GEN-LAST:event_jtblSelectorPresentationMousePressed

    private void jtblSelectorPresentationMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblSelectorPresentationMouseReleased
        fillSelectorPresentationRightsAndCommand();
    }//GEN-LAST:event_jtblSelectorPresentationMouseReleased

    private void jtblSelectorPresentationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblSelectorPresentationKeyReleased
        fillSelectorPresentationRightsAndCommand();
    }//GEN-LAST:event_jtblSelectorPresentationKeyReleased

    private void jbtAllowedSelectorPresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAllowedSelectorPresActionPerformed
        if (currCoverEntityObjectClasses == null) {
            return;
        }
        if (!DialogUtils.messageConfirmation("Allow access to all selector presentations and theirs explorer items?")) {
            return;
        }
        List<AdsSelectorPresentationDef> allPresentations = getSelectorPresentationList(currCoverEntityObjectClasses.clazz);
        for (AdsSelectorPresentationDef spd : allPresentations) {

            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.SELECTOR_PRESENTATION,
                    currCoverEntityObjectClasses.clazz.getId(),
                    spd.getId(),
                    Restrictions.Factory.newInstance(role, 0)));
        }
        checkEntityDefRowBold();
        refreshEditorAndSelectorPresentationList();
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();

    }//GEN-LAST:event_jbtAllowedSelectorPresActionPerformed

    private void jbtInheritSlPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtInheritSlPrActionPerformed
        if (currCoverEntityObjectClasses == null) {
            return;
        }
        String sPrefix;
        if (role.getAncestorIds().size() > 0 || role.isOverwrite()) {
            sPrefix = "Inherit ";
        } else {
            sPrefix = "Delete ";
        }
        if (!DialogUtils.messageConfirmation(sPrefix + "rights for all selector presentations and theirs explorer items?")) {
            return;
        }

        List<AdsSelectorPresentationDef> allPresentations = getSelectorPresentationList(currCoverEntityObjectClasses.clazz);
        for (AdsSelectorPresentationDef epd : allPresentations) {
            role.RemoveResourceRestrictions(
                    AdsRoleDef.generateResHashKey(
                    EDrcResourceType.SELECTOR_PRESENTATION,
                    currCoverEntityObjectClasses.clazz.getId(),
                    epd.getId()));
        }

        List<Id> lst = allSelectorPresantationMap.get(currCoverEntityObjectClasses.clazz.getId());
        if (lst != null) {
            lst.clear();
            /*
             * for (Id currId : lst) if
             * (currId.equals(entitySelectorPresentations.get(index).getId())) {
             * lst.remove(currId); break; }
             */
        }


        // refreshSelectorPresentationList(null);


        checkEntityDefRowBold();
        refreshEditorAndSelectorPresentationList();
        refreshEditorPresentationTree(null);
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();
    }//GEN-LAST:event_jbtInheritSlPrActionPerformed

    private void jbtAddSlPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddSlPrActionPerformed

        if (currCoverEntityObjectClasses == null) {
            return;
        }
        List<AdsSelectorPresentationDef> allPresentations = getSelectorPresentationList(currCoverEntityObjectClasses.clazz);
        HashMap<Definition, Definition> map = new HashMap<>();
        for (AdsSelectorPresentationDef pres : allPresentations) {
            boolean isFind = false;
            for (CoverSelectorPresentation cover : entitySelectorPresentations) {
                if (cover.sp.equals(pres)) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                map.put(pres, pres);
            }
        }

        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                role.getModule(),
                null, map.values());
        AdsSelectorPresentationDef spd = (AdsSelectorPresentationDef) ChooseDefinition.chooseDefinition(cfg);
        if (spd != null) {
            int index = AdsUsedByRolePanel.findPlace(entitySelectorPresentations, spd, true);
            DefaultTableModel model = (DefaultTableModel) jtblSelectorPresentation.getModel();
            if (index == entitySelectorPresentations.size()) {
                entitySelectorPresentations.add(new CoverSelectorPresentation(spd));
                model.addRow(new Object[]{new FooItemWithBool(spd.getName(), true, Color.BLACK), Boolean.FALSE});

            } else {
                entitySelectorPresentations.add(index, new CoverSelectorPresentation(spd));
                model.insertRow(index, new Object[]{new FooItemWithBool(spd.getName(), true, Color.BLACK), Boolean.FALSE});
            }
            jtblSelectorPresentation.setRowSelectionInterval(index, index);
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.SELECTOR_PRESENTATION,
                    currCoverEntityObjectClasses.clazz.getId(),
                    spd.getId(),
                    Restrictions.Factory.newInstance(role, 0)));
        }

        checkEntityDefRowBold();
        fillSelectorPresentationRightsAndCommand();
        refreshEditorPresentationTree(null);
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();



    }//GEN-LAST:event_jbtAddSlPrActionPerformed

    private void jbtDelSpPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelSpPrActionPerformed

        if (currCoverEntityObjectClasses == null) {
            return;
        }
        if (entitySelectorPresentations.isEmpty()) {
            return;
        }
        int index = jtblSelectorPresentation.getSelectedRow();
        String hash = AdsRoleDef.generateResHashKey(
                EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                entitySelectorPresentations.get(index).getId());
        role.RemoveResourceRestrictions(hash);
        Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
//        if (ar.isDenied(ERestriction.ACCESS))
//        {
//            DefaultTableModel model = (DefaultTableModel)jtblSelectorPresentation.getModel();
//            model.removeRow(index);
//            entitySelectorPresentations.remove(index);
//            if (entitySelectorPresentations.size()>0)
//                jtblSelectorPresentation.setRowSelectionInterval(0, 0);
//        }
//        else
        {
            DefaultTableModel model = (DefaultTableModel) jtblSelectorPresentation.getModel();
            model.setValueAt(new FooItemWithBool(model.getValueAt(index, 0).toString(), !ar.isDenied(ERestriction.ACCESS), greyColor), index, 0);
            model.setValueAt(Boolean.FALSE, index, 1);
        }
        //List<Id>  allSelectorPresantationMap.get(entitySelectorPresentations.get(index).getId());

        List<Id> lst = allSelectorPresantationMap.get(currCoverEntityObjectClasses.clazz.getId());
        if (lst != null) {
            for (Id currId : lst) {
                if (currId.equals(entitySelectorPresentations.get(index).getId())) {
                    lst.remove(currId);
                    break;
                }
            }
        }


        refreshSelectorPresentationList(null);
        fillSelectorPresentationRightsAndCommand();
        checkEntityDefRowBold();
        stateEntityClassDisableStatus();
    }//GEN-LAST:event_jbtDelSpPrActionPerformed

    private void jbtSetAllRightsSelectorPresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetAllRightsSelectorPresActionPerformed

        if (entitySelectorPresentations == null
                || currCoverEntityObjectClasses == null) {
            return;
        }
        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                lastSelectorPresentationId,
                Restrictions.Factory.newInstance(0));
        role.CreateOrReplaceResourceRestrictions(resource);
        checkEntityDefRowBold();
        stateEntityClassDisableStatus();
        fillSelectorPresentationRightsAndCommand();

        TableModel model = jtblSelectorPresentation.getModel();
        int row = jtblSelectorPresentation.getSelectedRow();
        model.setValueAt(new FooItemWithBool(model.getValueAt(row, 0).toString(), true, Color.BLACK), row, 0);

    }//GEN-LAST:event_jbtSetAllRightsSelectorPresActionPerformed

    private void jbtSetAccessRightsSelectorPresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetAccessRightsSelectorPresActionPerformed
        if (entitySelectorPresentations == null
                || currCoverEntityObjectClasses == null) {
            return;
        }
        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                lastSelectorPresentationId,
                Restrictions.Factory.newInstance(~ERestriction.ACCESS.getValue()));
        role.CreateOrReplaceResourceRestrictions(resource);

        checkEntityDefRowBold();
        stateEntityClassDisableStatus();
        fillSelectorPresentationRightsAndCommand();

        TableModel model = jtblSelectorPresentation.getModel();
        int row = jtblSelectorPresentation.getSelectedRow();
        model.setValueAt(new FooItemWithBool(model.getValueAt(row, 0).toString(), true, Color.BLACK), row, 0);

    }//GEN-LAST:event_jbtSetAccessRightsSelectorPresActionPerformed

    private void jbtSetSelectorPresCmdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetSelectorPresCmdActionPerformed

        if (lastSelectorPresentationId == null
                || selectorCommandsList == null
                || currCoverEntityObjectClasses == null) {
            return;
        }

        Restrictions res = role.getResourceRestrictions(EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(), lastSelectorPresentationId, null);
        for (AdsScopeCommandDefCover cmd : selectorCommandsList) {
            res.setCommandEnabled(cmd.getId(), true);
        }

        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                lastSelectorPresentationId,
                res);
        role.CreateOrReplaceResourceRestrictions(resource);
        fillSelectorPresentationRightsAndCommand();
    }//GEN-LAST:event_jbtSetSelectorPresCmdActionPerformed

    private void jbtUnSetSelectorPresCmdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtUnSetSelectorPresCmdActionPerformed
        if (lastSelectorPresentationId == null
                || selectorCommandsList == null
                || currCoverEntityObjectClasses == null) {
            return;
        }
        String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                lastSelectorPresentationId);

        Restrictions or = role.getOverwriteResourceRestrictions(hash, null);
        Restrictions res = role.getResourceRestrictions(EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(), lastSelectorPresentationId, null);
        List<Id> lst = or.getEnabledCommandIds();
        for (AdsScopeCommandDefCover cmd : selectorCommandsList) {
            if (!lst.contains(cmd.getId())) {
                res.setCommandEnabled(cmd.getId(), false);
            }
        }

        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                lastSelectorPresentationId,
                res);
        role.CreateOrReplaceResourceRestrictions(resource);
        fillSelectorPresentationRightsAndCommand();
    }//GEN-LAST:event_jbtUnSetSelectorPresCmdActionPerformed

    private void jtblCurrentSelectorEnabledCmdPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jtblCurrentSelectorEnabledCmdPropertyChange
        if (canIgnoreChangeEvent(evt)) {
            return;
        }
        if (role != null && !role.isInBranch()) {
            return;
        }
        int col = jtblCurrentSelectorEnabledCmd.getSelectedColumn();
        if (col != 2 && col != 4) {
            return;
        }
        int col2 = col != 2 ? 2 : 4;
        int row = jtblCurrentSelectorEnabledCmd.getSelectedRow();
        if (lastSelectorPresentationId == null
                || row == -1
                || selectorCommandsList == null
                || currCoverEntityObjectClasses == null) {
            return;
        }
        Boolean val = (Boolean) jtblCurrentSelectorEnabledCmd.getValueAt(row, col);
        jtblCurrentSelectorEnabledCmd.setValueAt(val, row, col2);

        Restrictions res = role.getResourceRestrictions(EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(), lastSelectorPresentationId, null);
        res.setCommandEnabled(selectorCommandsList.get(row).getId(), val);

        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                lastSelectorPresentationId,
                res);
        role.CreateOrReplaceResourceRestrictions(resource);

        fillSelectorPresentationRightsAndCommand();

//        String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION,
//                                    currCoverEntityObjectClasses.clazz.getId(),
//                                    lastSelectorPresentationId);


    }//GEN-LAST:event_jtblCurrentSelectorEnabledCmdPropertyChange

    private void jbtSetAllRightsSelectorPres1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetAllRightsSelectorPres1ActionPerformed

        if (currCoverEditorPresentation == null
                || currCoverEditorPresentation.epr == null) {
            return;
        }

        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                currCoverEditorPresentation.getEditorPresentation().getId(),
                Restrictions.Factory.newInstance(0));
        role.CreateOrReplaceResourceRestrictions(resource);
//        checkEntityDefRowBold();
//        stateEntityClassDisableStatus();
//        fillEditorPresentationRightsAndCommand();


        refreshEditorPresentationTree(currCoverEditorPresentation.getEditorPresentation());
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();
        checkEntityDefRowBold();






    }//GEN-LAST:event_jbtSetAllRightsSelectorPres1ActionPerformed

    
    private Restrictions getEditorPresentationViewOnlyRestriction(){
        return Restrictions.Factory.newInstance(~ (ERestriction.ACCESS.getValue()|ERestriction.VIEW.getValue()|ERestriction.ANY_CHILD.getValue()|ERestriction.ANY_PAGES.getValue()));
    }
    
    private void jbtSetAccessRightsSelectorPres1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetAccessRightsSelectorPres1ActionPerformed
        if (currCoverEditorPresentation == null
                || currCoverEditorPresentation.epr == null) {
            return;
        }
        AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                currCoverEditorPresentation.getEditorPresentation().getId(),
                getEditorPresentationViewOnlyRestriction()
                );
        role.CreateOrReplaceResourceRestrictions(resource);
//        checkEntityDefRowBold();
//        stateEntityClassDisableStatus();
//        fillEditorPresentationRightsAndCommand();

        refreshEditorPresentationTree(currCoverEditorPresentation.getEditorPresentation());
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();
        checkEntityDefRowBold();

    }//GEN-LAST:event_jbtSetAccessRightsSelectorPres1ActionPerformed

    private void jbtSetEdPresExplChildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetEdPresExplChildActionPerformed

        if (currCoverEditorPresentation.getEditorPresentation() == null) {
            return;
        }
        String hash = AdsRoleDef.generateResHashKey(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId);
        Restrictions oldRestrictions = role.getOnlyCurrentResourceRestrictions(hash);
        List<Id> oldEnabledCommands = oldRestrictions.getEnabledCommandIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledCommandIds());
        List<Id> oldEnabledPages = oldRestrictions.getEnabledPageIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledPageIds());
        long bitMask = ERestriction.toBitField(oldRestrictions.getRestriction());
        List<AdsExplorerItemDef> list =
                currCoverEditorPresentation.getEditorPresentation().
                getExplorerItems().
                getChildren().
                get(EScope.LOCAL_AND_OVERWRITE);
        removeOverwriteItems(list);
        List<Id> collectLst = new ArrayList<>();
        for (AdsExplorerItemDef item : list) {
            collectExplorerItems(collectLst, item);
        }
        role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId,
                Restrictions.Factory.newInstance(role, bitMask, oldEnabledCommands, collectLst, oldEnabledPages)));
        checkEntityDefRowBold();
        stateEntityClassDisableStatus();
        fillEditorPresentationRightsAndCommand();
    }//GEN-LAST:event_jbtSetEdPresExplChildActionPerformed

    private void jbtUnSetEdPresExplChildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtUnSetEdPresExplChildActionPerformed
        if (currCoverEditorPresentation.getEditorPresentation() == null) {
            return;
        }
        String hash = AdsRoleDef.generateResHashKey(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId);
        Restrictions oldRestrictions = role.getOnlyCurrentResourceRestrictions(hash);
        List<Id> oldEnabledCommands = oldRestrictions.getEnabledCommandIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledCommandIds());
        List<Id> oldEnabledPages = oldRestrictions.getEnabledPageIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledPageIds());

        long bitMask = ERestriction.toBitField(oldRestrictions.getRestriction());

        Restrictions or = role.getOverwriteResourceRestrictions(hash, currCoverEditorPresentation.getEditorPresentation());
        List<Id> lst = or.getEnabledChildIds();
        role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId,
                Restrictions.Factory.newInstance(role, bitMask, oldEnabledCommands, lst, oldEnabledPages)));
        checkEntityDefRowBold();
        stateEntityClassDisableStatus();
        fillEditorPresentationRightsAndCommand();
    }//GEN-LAST:event_jbtUnSetEdPresExplChildActionPerformed

    private void jbtGoToAncestorRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtGoToAncestorRoleActionPerformed
        DefaultListModel model = (DefaultListModel) jAncestorList.getModel();
        if (model.getSize() == 0) {
            refreshAncestorsButtons();
            return;
        }
        int index = jAncestorList.getSelectedIndex();
        FooItem item = (FooItem) model.get(index);
        if (!(item.GetMyObject() instanceof AdsRoleDef)) {
            return;
        }
        AdsRoleDef r = (AdsRoleDef) item.GetMyObject();
        NodesManager.selectInProjects(r);
    }//GEN-LAST:event_jbtGoToAncestorRoleActionPerformed

    private void jbtGoToAPFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtGoToAPFamilyActionPerformed
        DefaultListModel model = (DefaultListModel) jAPFamilyList.getModel();
        if (model.getSize() == 0) {
            //refreshAncestorsButtons();
            return;
        }
        int index = jAPFamilyList.getSelectedIndex();
        FooItem item = (FooItem) model.get(index);
        if (!(item.GetMyObject() instanceof DdsAccessPartitionFamilyDef)) {
            return;
        }
        DdsAccessPartitionFamilyDef apf = (DdsAccessPartitionFamilyDef) item.GetMyObject();

        NodesManager.selectInProjects(apf);
    }//GEN-LAST:event_jbtGoToAPFamilyActionPerformed

    private void jbtSearchEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSearchEntityActionPerformed

        HashMap<Id, Definition> freeEntityClassesMap = new HashMap<>();

        Iterator<CoverEntityObjectClasses> iter = entityObjectClassesMap.values().iterator();

        while (iter.hasNext()) {
            CoverEntityObjectClasses classCover = iter.next();
            if (classCover.isRightsOrRightsInChilds()) {
//                if (!isShowInheritClassesResources && classCover.clazz instanceof AdsApplicationClassDef)
//                {
//                }
//                else
                if (classCover.clazz != null) {
                    freeEntityClassesMap.put(classCover.clazz.getId(), classCover.clazz);
                }
            }
        }


        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                role.getModule(),
                null, freeEntityClassesMap.values());


        cfg.setDisplayMode(EChooseDefinitionDisplayMode.QUALIFIED_NAME);

        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def != null) {

            for (int i = 0; i < jEntityAndApplTree.getRowCount(); i++) {
                TreePath treePath = jEntityAndApplTree.getPathForRow(i);
//            DefaultTreeModel model = (DefaultTreeModel)jEntityAndApplTree.getModel();
                DefaultMutableTreeNodeEx obj = (DefaultMutableTreeNodeEx) treePath.getLastPathComponent();
                CoverEntityObjectClasses cover =
                        (CoverEntityObjectClasses) obj.obj;
                if (cover.clazz == def) {
                    jEntityAndApplTree.expandPath(treePath);
                    jEntityAndApplTree.setSelectionPath(treePath);
                    jEntityAndApplTree.scrollPathToVisible(treePath);
                    refreshEditorAndSelectorPresentationList();
                    stateEntityClassDisableStatus();
                    break;
                }


            }
        }



    }//GEN-LAST:event_jbtSearchEntityActionPerformed

    private void jbtChangeModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtChangeModeActionPerformed
        isShowInheritClassesResources = !isShowInheritClassesResources;
        Preferences pref = Utils.findOrCreatePreferences(settingKey);
        pref.putBoolean(showSubClassesResourcesKey, isShowInheritClassesResources);
        refreshjbtChangeMode();
        setClasses();
    }//GEN-LAST:event_jbtChangeModeActionPerformed

    private void setAllEdPresPagees(boolean val) {

        if (currCoverEditorPresentation.getEditorPresentation() == null) {
            return;
        }
        String hash = AdsRoleDef.generateResHashKey(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId);
        Restrictions oldRestrictions = role.getOnlyCurrentResourceRestrictions(hash);
        List<Id> oldEnabledCommands = oldRestrictions.getEnabledCommandIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledCommandIds());
        List<Id> oldEnabledChilds = oldRestrictions.getEnabledChildIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledChildIds());
        //List<Id> oldEnabledPages = oldRestrictions.getEnabledPageIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledPageIds());
        long bitMask = ERestriction.toBitField(oldRestrictions.getRestriction());

        List<Id> collectLst = null;
        if (val) {
            List<AdsEditorPageDef> pages = currCoverEditorPresentation.getEditorPresentation().getEditorPages().get(EScope.ALL);
            collectLst = new ArrayList<>(pages.size());
            for (AdsEditorPageDef page : pages) {
                collectLst.add(page.getId());
            }
        }


        role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId,
                Restrictions.Factory.newInstance(role, bitMask, oldEnabledCommands, oldEnabledChilds, collectLst)));
        checkEntityDefRowBold();
        stateEntityClassDisableStatus();
        fillEditorPresentationRightsAndCommand();

    }

    private void jbtSetEdPresPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetEdPresPagesActionPerformed
        setAllEdPresPagees(true);
    }//GEN-LAST:event_jbtSetEdPresPagesActionPerformed

    private void jbtUnSetEdPresPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtUnSetEdPresPagesActionPerformed
        setAllEdPresPagees(false);
    }//GEN-LAST:event_jbtUnSetEdPresPagesActionPerformed
    private static String settingKey = "AdsRoleEditorSettings";
    private static String showSubClassesResourcesKey = "showSubClassesResourcesKey";
    private boolean isShowInheritClassesResources = true;

    private void refreshjbtChangeMode() {
        if (isShowInheritClassesResources) {
            jbtChangeMode.setIcon(RadixWareDesignerIcon.TREE.UNDEPENDENCIES.getIcon());
            jbtChangeMode.setToolTipText("Hide Inherited Сlasses");
        } else {
            jbtChangeMode.setIcon(RadixWareDesignerIcon.TREE.DEPENDENCIES2.getIcon());
            jbtChangeMode.setToolTipText("Show Inherited Сlasses");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ancestorsPane4;
    private javax.swing.JPanel apfamilyPane2;
    private javax.swing.JPanel esourcePane2;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JList jAPFamilyList;
    private javax.swing.JList jAncestorList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jResourcePane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPaneCmdCurr;
    private javax.swing.JScrollPane jScrollPaneCmdCurr1;
    private javax.swing.JScrollPane jScrollPaneContextlessCommands;
    private javax.swing.JScrollPane jScrollPaneCurr;
    private javax.swing.JScrollPane jScrollPaneCurr1;
    private javax.swing.JScrollPane jScrollPaneEntitys;
    private javax.swing.JScrollPane jScrollPaneExplorerItems;
    private javax.swing.JScrollPane jScrollPaneExplorerRoots;
    private javax.swing.JScrollPane jScrollPaneServerResource;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JToolBar.Separator jSeparator16;
    private javax.swing.JToolBar.Separator jSeparator17;
    private javax.swing.JToolBar.Separator jSeparator19;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JToolBar.Separator jSeparatorGoToAPFamily;
    private javax.swing.JToolBar.Separator jSeparatorGoToCCmd;
    private javax.swing.JToolBar.Separator jSeparatorGoToEntity;
    private javax.swing.JToolBar.Separator jSeparatorGoToExplorerRoot;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar10;
    private javax.swing.JToolBar jToolBar11;
    private javax.swing.JToolBar jToolBar13;
    private javax.swing.JToolBar jToolBar14;
    private javax.swing.JToolBar jToolBar15;
    private javax.swing.JToolBar jToolBar16;
    private javax.swing.JToolBar jToolBar17;
    private javax.swing.JToolBar jToolBar18;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JToolBar jToolBar9;
    private javax.swing.JButton jbtAddAncestor;
    private javax.swing.JButton jbtAddAutoAPFamily;
    private javax.swing.JButton jbtAddCCmd;
    private javax.swing.JButton jbtAddEdPr;
    private javax.swing.JButton jbtAddEntity;
    private javax.swing.JButton jbtAddExplorerRoot;
    private javax.swing.JButton jbtAddSlPr;
    private javax.swing.JButton jbtAllowedEditorPres;
    private javax.swing.JButton jbtAllowedEditorPres1;
    private javax.swing.JButton jbtAllowedSelectorPres;
    private javax.swing.JButton jbtCCmdAllowedAll;
    private javax.swing.JButton jbtCCmdInheritAll;
    private javax.swing.JButton jbtChangeMode;
    private javax.swing.JButton jbtClearAPFamily;
    private javax.swing.JButton jbtClearAncestors;
    private javax.swing.JButton jbtDelAPFamily;
    private javax.swing.JButton jbtDelAncestor;
    private javax.swing.JButton jbtDelCCmd;
    private javax.swing.JButton jbtDelEdPr;
    private javax.swing.JButton jbtDelEntity;
    private javax.swing.JButton jbtDelExplorerRoot;
    private javax.swing.JButton jbtDelSpPr;
    private javax.swing.JButton jbtDeselectAllCmdForEdPr;
    private javax.swing.JButton jbtExplItemAllowedTree;
    private javax.swing.JButton jbtExplItemInheritTree;
    private javax.swing.JButton jbtExplRootAllowedAll;
    private javax.swing.JButton jbtExplRootInheritAll;
    private javax.swing.JButton jbtGoToAPFamily;
    private javax.swing.JButton jbtGoToAncestorRole;
    private javax.swing.JButton jbtGoToCCmd;
    private javax.swing.JButton jbtGoToEntity;
    private javax.swing.JButton jbtGoToExplorerRoot;
    private javax.swing.JButton jbtInheritEdPr;
    private javax.swing.JButton jbtInheritEditorPres;
    private javax.swing.JButton jbtInheritSlPr;
    private javax.swing.JButton jbtSearchEntity;
    private javax.swing.JButton jbtSelectAllCmdForEdPr;
    private javax.swing.JButton jbtServerAllowedAll;
    private javax.swing.JButton jbtServerInheritAll;
    private javax.swing.JButton jbtSetAccessRightsSelectorPres;
    private javax.swing.JButton jbtSetAccessRightsSelectorPres1;
    private javax.swing.JButton jbtSetAllRightsSelectorPres;
    private javax.swing.JButton jbtSetAllRightsSelectorPres1;
    private javax.swing.JButton jbtSetEdPresExplChild;
    private javax.swing.JButton jbtSetEdPresPages;
    private javax.swing.JButton jbtSetSelectorPresCmd;
    private javax.swing.JButton jbtUnSetEdPresExplChild;
    private javax.swing.JButton jbtUnSetEdPresPages;
    private javax.swing.JButton jbtUnSetSelectorPresCmd;
    private javax.swing.JLabel jlEditorPresTitle;
    private javax.swing.JLabel jlSelectorPresTitle;
    private javax.swing.JTable jtblCurrentEditorEnabledCmd;
    private javax.swing.JTable jtblCurrentSelectorEnabledCmd;
    private javax.swing.JTable jtblSelectorPresentation;
    private javax.swing.JTabbedPane mainTabbedPane4;
    private javax.swing.JPanel resourcePane2;
    // End of variables declaration//GEN-END:variables
    public final static String Allowed = "Allowed";
    public final static String Forbidden = "Prohibited";
    public final static String Inherit = "Inherit";

    /**
     * @return the boldFont
     */
    private Font getBoldFont() {
        return boldFont;
    }

    /**
     * @param boldFont the boldFont to set
     */
    private void setBoldFont(Font boldFont) {
        this.boldFont = boldFont;
    }

    /**
     * @return the normalFont
     */
    private Font getNormalFont() {
        return normalFont;
    }

    /**
     * @param normalFont the normalFont to set
     */
    private void setNormalFont(Font normalFont) {
        this.normalFont = normalFont;
    }

    private class AbsolutelyFooItem {

        protected final String name;

        AbsolutelyFooItem(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final class FooItemWithBool extends AbsolutelyFooItem {

        public FooItemWithBool(final String name, final boolean val, final Color color) {
            super(name);
            setBool(val);
            this.color = color;
        }

        public boolean getBool() {
            return val;
        }

        public void setBool(final boolean val) {
            this.val = val;
        }
        protected boolean val;
        protected Color color;
    }

    private class FooItem extends AbsolutelyFooItem {

        public FooItem(final String name, final Object obj) {
            super(name);
            this.obj = obj;
        }

        public Object GetMyObject() {
            return obj;
        }
        protected final Object obj;
    }

    private final class FooItemWithIcon extends FooItem {

        FooItemWithIcon(final Id isRoot, final String name, final AdsExplorerItemDef obj, RadixIcon ico, boolean isBold, Color color) {
            super(name, obj);
            this.ico = ico;
            setIsBold(isBold);
            setColor(color);
            String hash;
            if (isRoot == obj.getId()) {
                hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, isRoot, null);
            } else {
                hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, isRoot, obj.getId());
            }
            setIsCanForbit(role.getOverwriteResourceRestrictions(hash, null).isDenied(ERestriction.ACCESS));

        }
        protected final RadixIcon ico;
        protected boolean isBold2;
        protected Color color;

        public boolean isCanForbit() {
            return isCanForbit;
        }

        public void setIsCanForbit(boolean isCanForbit) {
            this.isCanForbit = isCanForbit;
        }
        protected boolean isCanForbit;

        public void setColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public void setIsBold(boolean isBold) {
            this.isBold2 = isBold;
        }

        public boolean getIsBold() {
            return isBold2;
        }

        public RadixIcon getIcon() {

            return ico;
        }
    }

    private class RoleAncestorVisitorProvider extends AdsVisitorProvider {

        @Override
        public boolean isTarget(RadixObject obj) {

            if (obj instanceof AdsRoleDef) {
                AdsRoleDef a = (AdsRoleDef) obj;

//                if (role.isAppRole() != a.isAppRole()) {
//                    return false;
//                }
                if (role != a && !overwriteRoles.contains(a) && role.getId() != a.getId()
                        && !a.isAncestor(role, true) && !role.isAncestor(a, true)) {
                    for (AdsRoleDef r : overwriteAncestors) {
                        if (r.getId().equals(a.getId())) {
                            return false;
                        }
                    }
                    return true;
                }
            }

            return false;
        }
    }

    private class JTreeWithMouseMotionListener extends JTree implements MouseMotionListener {

        @Override
        public void mouseMoved(MouseEvent e) {
            JComponent c = (JComponent) e.getComponent();
            //Action action = c.getActionMap().get("postTip");
            // if (action != null)
            {
                if (jEntityAndApplTree == c) {
                    int row = jEntityAndApplTree.getRowForLocation(e.getX(), e.getY());
                    if (row == -1) {
                        c.setToolTipText("");
                        return;
                    }
                    TreePath treePath = jEntityAndApplTree.getPathForRow(row);
                    DefaultMutableTreeNodeEx obj = (DefaultMutableTreeNodeEx) treePath.getLastPathComponent();
                    CoverEntityObjectClasses cover =
                            (CoverEntityObjectClasses) obj.obj;

                    String sMess = "";
                    {
                        //Ищем от кого получили права

                        CoverEntityObjectClasses parentCover = cover;
                        while (true) {
                            CoverEntityObjectClasses t = parentCover.parent;
                            if (t == null || t.clazz == null) {
                                break;
                            }
                            parentCover = t;
                        }
                        HashMap<Id, AdsEditorPresentationDef> allPres = new HashMap<>();
                        collectEPR(allPres, parentCover);

                        List<AdsEditorPresentationDef> currEditorPresList;
                        if (cover.clazz == null) {
                            currEditorPresList = new ArrayList<>(0);
                        } else {
                            currEditorPresList = cover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
                        }
                        removeOverwriteItems(currEditorPresList);
                        RadixObjectsUtils.sortByQualifiedName(currEditorPresList);
                        boolean isFirst = true;


                        List<AdsRoleDef> roleList = role.collectAllAncestors();



                        for (AdsEditorPresentationDef pres : currEditorPresList) {
                            String currHash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                                    pres.getOwnerClass().getId(),
                                    pres.getId());

                            boolean isFindRights = false;
                            AdsEditorPresentationDef ownPres = allPres.get(pres.getBasePresentationId());
                            while (ownPres != null) {
                                String branchHash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                                        ownPres.getOwnerClass().getId(),
                                        ownPres.getId());
                                Restrictions res = role.getOnlyCurrentResourceRestrictions(branchHash);
                                if (res != null && !res.isDenied(ERestriction.ACCESS)) {
                                    if (!isFirst) {
                                        sMess += "<br>";
                                    } else {
                                        isFirst = false;
                                    }
                                    sMess += "Editor presentation rights on " + pres.getQualifiedName() + " inherited from presentation " + ownPres.getQualifiedName();
                                    isFindRights = true;
                                    break;
                                }
                                ownPres = allPres.get(ownPres.getBasePresentationId());
                            }
                            if (!isFindRights) {
                                String roleString = "";
                                int rolesCount = 0;
                                for (AdsRoleDef ar : roleList) {
                                    //Restrictions rest = ar.getResourceRestrictions(currHash, pres);
                                    Restrictions rest = ar.getOnlyCurrentResourceRestrictions(currHash);
                                    if (rest != null && !rest.isDenied(ERestriction.ACCESS)) {
                                        rolesCount++;
                                        if (roleString.isEmpty()) {
                                            roleString = ar.getQualifiedName();
                                        } else {
                                            roleString += ", " + ar.getQualifiedName();
                                        }
                                    }
                                }
                                if (rolesCount > 0) {
                                    if (!isFirst) {
                                        sMess += "<br>";
                                    } else {
                                        isFirst = false;
                                    }
                                    sMess += "Editor presentation rights on " + pres.getQualifiedName()
                                            + (rolesCount == 1 ? " inherited from role " : " inherited from roles ")
                                            + roleString;
                                }
                            }

                        }

                        List<AdsSelectorPresentationDef> currSelPresList;
                        if (cover.clazz == null) {
                            currSelPresList = new ArrayList<>(0);
                        } else {
                            currSelPresList = cover.clazz.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
                        }
                        removeOverwriteItems(currSelPresList);
                        RadixObjectsUtils.sortByQualifiedName(currSelPresList);

                        for (AdsSelectorPresentationDef pres : currSelPresList) {
                            String currHash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION,
                                    pres.getOwnerClass().getId(),
                                    pres.getId());

                            String roleString = "";
                            int rolesCount = 0;
                            for (AdsRoleDef ar : roleList) {
                                //Restrictions rest = ar.getResourceRestrictions(currHash, null);
                                Restrictions rest = ar.getOnlyCurrentResourceRestrictions(currHash);
                                if (rest != null && !rest.isDenied(ERestriction.ACCESS)) {
                                    rolesCount++;
                                    if (roleString.isEmpty()) {
                                        roleString = ar.getQualifiedName();
                                    } else {
                                        roleString += ", " + ar.getQualifiedName();
                                    }
                                }
                            }
                            if (rolesCount > 0) {
                                if (!isFirst) {
                                    sMess += "<br>";
                                } else {
                                    isFirst = false;
                                }
                                sMess += "Selector presentation rights on " + pres.getQualifiedName()
                                        + (rolesCount == 1 ? " inherited from role " : " inherited from roles ")
                                        + roleString;
                            }
                        }
                    }
                    if (sMess.isEmpty()) {
                        c.setToolTipText("Lack of inherit rights");
                    } else {
                        sMess = "<html>" + sMess + "</html>";
                        c.setToolTipText(sMess);
                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }
    }

    void collectEPR(HashMap<Id, AdsEditorPresentationDef> output, CoverEntityObjectClasses cover) {

        if (cover.clazz != null) {
            List<AdsEditorPresentationDef> presList =
                    cover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
            removeOverwriteItems(presList);

            for (AdsEditorPresentationDef pres : presList) {
                if (output.get(pres.getId()) == null) {
                    output.put(pres.getId(), pres);
                }
            }
        }


        for (CoverEntityObjectClasses child : cover.childs) {
            collectEPR(output, child);
        }


    }

    void collectClassRights(AdsRoleDef role, Id classId, List<RightCollection> output) {
    }

    private class RightCollection {

        public RightCollection(AdsRoleDef role, AdsEditorPresentationDef pres) {
            this.role = role;
            this.pres = pres;
        }
        AdsRoleDef role;
        AdsEditorPresentationDef pres;
    }
    private AdsRoleDef role = null;
    private JComboBoxEx comboBoxEx = new JComboBoxEx();
    private JCheckBox checkBoxEx = new JCheckBox();
    private JTable jtblServerResource;
    private DefaultTableModel jtblServerTableModel;
    private JTreeWithMouseMotionListener jEntityAndApplTree;
    //private JTable jtblEntityResource;
    //private DefaultTableModel jtblEntityResourceModel;
    private JTable jtblExplorerRoots;
    private DefaultTableModel jtblExplorerRootsModel;
    //private JTable jtblClassDeff;
    //private DefaultTableModel jtblClassDeffModel;
    private JTable jtblContextlessCommands;
    private DefaultTableModel jtblContextlessCommandsModel;
    private JTable jtblCurrentEditorRights;
    private JTable jtblCurrentSelectorRights;
    private Color backgroundColor;
    private Color greyColor;
    private TableCellRendererWithBold whiteTableCellRendererWithBold = null;
    private TableCellRendererWithBold grayTableCellRendererWithBold = null;
    private TableCellRendererWithBold2 rendererWithBold2 = null;
    private Id lastExplorerRootId = null;
    //private Id lastEntityId = null;
    private CoverEntityObjectClasses currCoverEntityObjectClasses = null;
    private CoverEditorPresentation currCoverEditorPresentation = null;
    // private AdsEntityObjectClassDef lastEntityClassDef = null;
    private Id lastEditorPresentationId = null;
    private Id lastSelectorPresentationId = null;
    //private List<Id> currEnabledCommandForEditorDefIds = null;
    private DefaultTableModel currEditorModel = null;
//    private DefaultTableModel currEditorModel1 = null;
    //private DefaultTableModel inhEditorModel = null;
    //private DefaultTableModel inhEditorModel1 = null;
    private DefaultTableModel cmdSelectorModel = null;
    //private DefaultTableModel inhSelectorModel = null;
    private int isUpdated = 1;
    private boolean isSuperAdmin = false;
    private boolean roleIsReadOnly = false;
    private boolean isMustRefreshInheritRihghts = false;
    private DefinitionSearcher<AdsDefinition> adsSearcher = null;
    private final List<AdsModule> adsModules = new ArrayList<>();
    private final List<DdsModule> ddsModules = new ArrayList<>();
    private final Boolean[] blueInheritEditorRights = new Boolean[8];
    private final Boolean[] blueInheritSelectorRights = new Boolean[4];
    private final List<Boolean> blueInheritEditorEnabledCommands = new ArrayList<>();
    private final List<Boolean> blueInheritEditorEnabledCommands1 = new ArrayList<>();
    private final List<Boolean> blueInheritSelectorEnabledCommands = new ArrayList<>();
    private final List<Boolean> mayRemoveEditorEnabledCommands = new ArrayList<>();
    private final List<Boolean> mayRemoveSelectorEnabledCommands = new ArrayList<>();
    private List<DdsAccessPartitionFamilyDef> overwriteAPF = new ArrayList<>(0);
    private List<AdsRoleDef> overwriteAncestors = new ArrayList<>(0);
    private List<AdsRoleDef> overwriteRoles = new ArrayList<>(0);
    private String[] cNames = {"Explorer Item", "Inherited Rights", "Own Rights", "Total Rights", "Rights"};
    private String[] cNamesPres = {"Explorer Item", "Inherit", "Own", "Total", "Rights"};
    private Class[] cTypes = {TreeTableModel.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, Boolean.class};
    private Class[] cTypesBool = {TreeTableModel.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class};
    private HashMap<Id, CoverFoo> paragraphsMap = new HashMap<>();
    private RoleResourcesCash allOverwriteOptions = new RoleResourcesCash();

    protected RoleResourcesCash getOverwriteOptions() {
        return allOverwriteOptions;
    }
    //private HashMap<Id, CoverFoo> classesMap = new HashMap<Id, CoverFoo>();
    private HashMap<Id, CoverFoo> contextlessCommandsMap = new HashMap<>();
    //private HashMap<Definition, Definition> entityClassesMap = new HashMap<Definition, Definition>();
    private static final int SERVER_RESOURCES_SIZE = 0x9;
    private List<Boolean> serverResourcesCanForbid = new ArrayList<>(SERVER_RESOURCES_SIZE);
    private Font boldFont = null;
    private Font normalFont = null;
    private boolean isDisableTableCurrentEditorRights = true;
    private boolean isDisableTableCurrentSelectorRights = true;
    private boolean isDisableTableCurrentEditorRights2 = true;
    private boolean isDisableTableCurrentSelectorRights2 = true;
    //private List<AdsEditorPresentationDef> entityEditorPresentations;
    private List<CoverSelectorPresentation> entitySelectorPresentations;
    private List<CoverFoo> paragraphs = new ArrayList<>(0);
    private List<CoverFoo> contextlessCommands = new ArrayList<>(0);

    private CoverFoo createConverFoo(AdsDefinition item) {


        if (item instanceof AdsParagraphExplorerItemDef) {
            return new CoverFoo(item, EDrcResourceType.EXPLORER_ROOT_ITEM);
        }
        if (item instanceof AdsContextlessCommandDef) {
            return new CoverFoo(item, EDrcResourceType.CONTEXTLESS_COMMAND);
        }
//         if (item instanceof AdsClassDef)
//             return new CoverFoo(item, EDrcResourceType.CLASS_INSTANTIATOR);
        throw new DefinitionError("Incorrect resource.");
    }
    private HashMap<Id, CoverEntityObjectClasses> entityObjectClassesMap = new HashMap<>();
    private HashMap<Id, CoverEditorPresentation> editorPresantationMap = new HashMap<>();
    private HashMap<Id, List<Id>> allEditorPresantationMap = new HashMap<>();
    private HashMap<Id, List<Id>> allSelectorPresantationMap = new HashMap<>();
    private HashMap<Id, List<Id>> allExplorerItemMap = new HashMap<>();
    /*
     * private class CoverApplicationClassDef extends RadixObject {
     * AdsApplicationClassDef clazz; CoverApplicationClassDef parent; }
     */
    private static Id unicalId = Id.Factory.loadFrom("01234567890123456789012345678");

    private class CoverSelectorPresentation extends Definition {

        Id incorrectId = null;
        AdsSelectorPresentationDef sp = null;

        CoverSelectorPresentation(Id id) {
            super(id);
            incorrectId = id;
        }

        CoverSelectorPresentation(AdsSelectorPresentationDef sp) {
            super(sp == null ? null : sp.getId());
            this.sp = sp;
        }

        @Override
        public String getName() {
            if (sp != null) {
                return sp.getName();
            }
            if (incorrectId != null) {
                return "#" + incorrectId.toString();
            }
            return null;
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public Id getId() {
            if (sp != null) {
                return sp.getId();
            }
            return incorrectId;
        }
    }

    private class CoverEditorPresentation extends Definition {

        private boolean isVisible = false;
        Id incorrectId = null;
        Id parentId = null;

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean isVisible) {
            //this.isVisible = isVisible;
            this.isVisible = true;
        }

        @Override
        public String getName() {
            if (epr != null) {
                if (this.parent != null && this.parent.epr == null) {
                    return epr.getName();
                }
                return epr != null ? epr.getName() + "(" + epr.getOwnerClass().getName() + ")" : null;
            }
            if (incorrectId != null) {
                return "#" + incorrectId.toString();
            }
            return null;
        }

        @Override
        public String toString() {
            return getName();
        }

        CoverEditorPresentation(Id incorrectId, Id parentId) {
            super(unicalId);
            epr = null;
            this.incorrectId = incorrectId;
            this.parentId = parentId;
        }
        final private AdsEditorPresentationDef epr;

        CoverEditorPresentation(AdsEditorPresentationDef def) {
            //AdsEntityObjectClassDef clazz;AdsEntityObjectClassDef clazz
            super(def == null ? unicalId : def.getId());
            epr = def;
            parent = null;
        }
        private List<CoverEditorPresentation> childs = new ArrayList<>(0);
        CoverEditorPresentation parent;

        public AdsEditorPresentationDef getEditorPresentation() {
            return epr;
        }
    }
    /*
     * static Restrictions getEditorPresentationRestriction(boolean useThis,
     * AdsRoleDef role_, CoverEditorPresentation curr) { CoverEditorPresentation
     * currtmp = useThis ? curr : curr.parent;
     *
     *
     * while (currtmp!=null) { String hash = currtmp.epr==null ? "" :
     * AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
     * currtmp.epr.getOwnerClass().getId(), currtmp.epr.getId()); Restrictions
     * rest = role_.getOnlyCurrentResourceRestrictions(hash);
     * role_.getAncestorResourceRestrictions(hash, null); if (rest != null)
     * return rest; currtmp = currtmp.parent; }
     * DefinitionSearcher<AdsDefinition> currSearcher =
     * AdsSearcher.Factory.newAdsDefinitionSearcher(role_);
     *
     * long bitField = 0xffffffff; List<Id> cmdList = new ArrayList<Id>(); for
     * (Id ancestorId : role_.getAncestorIds()) { AdsRoleDef ancestor =
     * (AdsRoleDef) currSearcher.findById(ancestorId); if (ancestor != null) {
     * final Restrictions ancRestr = getEditorPresentationRestriction(true,
     * ancestor, curr); if (ancRestr != null) { bitField &=
     * ERestriction.toBitField(ancRestr.getRestriction()); if
     * (ancRestr.getEnabledCommandIds()!=null) for (Id id :
     * ancRestr.getEnabledCommandIds()) { if (!cmdList.contains(id))
     * cmdList.add(id); } if (bitField == 0) return
     * Restrictions.Factory.newInstance(role, 0, cmdList); } } } return
     * Restrictions.Factory.newInstance(role, bitField, cmdList); }
     */

    private final class CoverEntityObjectClasses extends Definition {

        private AdsEntityObjectClassDef clazz;
        private List<CoverEntityObjectClasses> childs = new ArrayList<>(0);
        boolean isRightsOrRightsInChilds2;
        private DefaultMutableTreeNodeEx treeNodeEx;
        CoverEntityObjectClasses parent;
        private Id classDefId;
        private Id incorrectId = null;

        public boolean isRightsOrRightsInChilds() {
            return isRightsOrRightsInChilds2;
        }

        public void setRightsOrRightsInChilds(boolean isRightsOrRightsInChilds2) {
//            if (clazz!=null &&
//                    clazz instanceof AdsApplicationClassDef &&
//                        isRightsOrRightsInChilds2)
//                if (clazz!=null && clazz instanceof AdsApplicationClassDef);
            this.isRightsOrRightsInChilds2 = isRightsOrRightsInChilds2;
        }
        String sMess;

        CoverEntityObjectClasses(Id incorrectId) {
            super(incorrectId);
            classDefId = this.incorrectId = incorrectId;
            isRightsOrRightsInChilds2 = true;
        }

        CoverEntityObjectClasses(AdsEntityObjectClassDef clazz) {
            super(clazz == null ? unicalId : clazz.getId());
            classDefId = clazz == null ? unicalId : clazz.getId();
            this.clazz = clazz;
            setRightsOrRightsInChilds(false);
        }

        public Id getClassDefId() {
            return classDefId;
        }

        @Override
        public String toString() {
            if (incorrectId != null) {
                return "#" + incorrectId.toString();
            }
            return clazz == null ? null : clazz.getQualifiedName();
        }

        @Override
        public String getQualifiedName(RadixObject context) {
            if (incorrectId != null) {
                return "#" + incorrectId.toString();
            }
            return clazz.getQualifiedName(context);
        }

        @Override
        public String getQualifiedName() {
            if (incorrectId != null) {
                return "#" + incorrectId.toString();
            }
            return clazz == null ? null : clazz.getQualifiedName();
        }

        @Override
        public String getName() {
            if (incorrectId != null) {
                return "#" + incorrectId.toString();
            }
            return clazz == null ? null : clazz.getName();
        }
    }

    private class CoverFoo extends RadixObject {

        protected boolean canForbid;
        protected AdsDefinition item;
        protected Id id = null;

        public CoverFoo(Id id) {
            this.id = id;
        }

        public CoverFoo(AdsDefinition item, EDrcResourceType type) {
            if (item != null) {
                this.item = item;
                String hash = AdsRoleDef.generateResHashKey(
                        type,
                        item.getId(), null);
                Restrictions rest = role.getOverwriteResourceRestrictions(hash, null);
                this.canForbid = rest.isDenied(ERestriction.ACCESS);
            }
        }

        protected CoverFoo(boolean canForbid, AdsDefinition item) {
            this.canForbid = canForbid;
            this.item = item;
        }

        @Override
        public String getQualifiedName(RadixObject context) {
            if (id != null) {
                return "#" + id.toString();
            }
            return item.getQualifiedName(context);
        }

        @Override
        public String getQualifiedName() {
            if (id != null) {
                return "#" + id.toString();
            }
            return item.getQualifiedName();
        }

        @Override
        public String getName() {
            if (id != null) {
                return "#" + id.toString();
            }
            return item.getName();
        }

        public boolean isCanForbid() {
            return canForbid;
        }

        public void setCanForbid(boolean canForbid) {
            this.canForbid = canForbid;
        }

        public void setItem(AdsDefinition item) {
            this.item = item;
        }

        public AdsDefinition getItem() {
            return item;
        }

        public Id getId() {
            return item == null ? id : item.getId();
        }

        public AdsParagraphExplorerItemDef getParagraphItem() {
            if (item instanceof AdsParagraphExplorerItemDef) {
                return (AdsParagraphExplorerItemDef) item;
            }
            return null;
        }

        public AdsContextlessCommandDef getCmdItem() {
            if (item instanceof AdsContextlessCommandDef) {
                return (AdsContextlessCommandDef) item;
            }
            return null;
        }

        public AdsClassDef getClassItem() {
            if (item instanceof AdsClassDef) {
                return (AdsClassDef) item;
            }
            return null;
        }
    }

    class TableCellRendererWithBold2 extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            {
                if (value instanceof FooItemWithBool) {
                    FooItemWithBool item = (FooItemWithBool) value;
                    if (item.getBool()) {
                        c.setFont(getBoldFont());
                    }

                    Color oldColor = c.getForeground();
                    if (oldColor.equals(Color.BLACK) || oldColor.equals(Color.BLUE) || oldColor.equals(Color.RED) || oldColor.equals(greyColor)) {
                        c.setForeground(item.color);

                    }

                }
            }
            return c;
        }
    }

    private List<AdsDefinition> coverListToAdsDefinitionList(List<CoverFoo> list) {
        List<AdsDefinition> retList = new ArrayList<>(list.size());
        for (CoverFoo item : list) {
            retList.add(item.getItem());
        }
        return retList;
    }

    public class CurrParagraphRightChecker {

        void check() {
            jtblExplorerRoots.repaint();

            if (jtblExplorerRoots.getRowCount() <= 0) {
                return;
            }

            int j = jtblExplorerRoots.getSelectedRow();
            String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM,
                    paragraphs.get(j).getParagraphItem().getId(), null);


            Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
            if (!ar.isDenied(ERestriction.ACCESS)) {
                jtblExplorerRootsModel.setValueAt(Allowed, j, 1);
                jtblExplorerRootsModel.setValueAt(Boolean.TRUE, j, 4);
            } else {
                jtblExplorerRootsModel.setValueAt(Forbidden, j, 1);
                jtblExplorerRootsModel.setValueAt(Boolean.FALSE, j, 4);
            }

            if (r == null) {
                jtblExplorerRootsModel.setValueAt(Inherit, j, 2);
                jtblExplorerRootsModel.setValueAt(jtblExplorerRootsModel.getValueAt(j, 1), j, 3);
            } else {
                if (!r.isDenied(ERestriction.ACCESS)) {
                    jtblExplorerRootsModel.setValueAt(Allowed, j, 2);
                    jtblExplorerRootsModel.setValueAt(Allowed, j, 3);
                    jtblExplorerRootsModel.setValueAt(Boolean.TRUE, j, 4);
                } else {
                    jtblExplorerRootsModel.setValueAt(Forbidden, j, 2);
                    jtblExplorerRootsModel.setValueAt(Forbidden, j, 3);
                    jtblExplorerRootsModel.setValueAt(Boolean.FALSE, j, 4);
                }
            }

        }
    }

    private class TableCellRendererWithBold extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            {
                if (row > -1) {
                    if ((column == 0 || column == 3)
                            && table.getColumnCount() > 3
                            && table.getValueAt(row, 3) != null
                            && table.getValueAt(row, 3).toString().equals(Allowed)) {
                        c.setFont(getBoldFont());
                    }
                    if (/*
                             * table == treeTable &&
                             */!isSelected) {
                        if (column != 2 || isSuperAdmin) {
                            c.setBackground(backgroundColor);
                        } else {
                            c.setBackground(Color.white);
                        }
                    }
                    Color oldColor = c.getForeground();
                    if (oldColor.equals(Color.BLACK)
                            || oldColor.equals(Color.BLUE)
                            || oldColor.equals(Color.RED)
                            || oldColor.equals(greyColor)) {
                        if (table.getColumnCount() > 2
                                && table.getValueAt(row, 2) != null
                                && table.getValueAt(row, 2).toString().equals(Inherit)) {
                            Color currColor = greyColor;
                            Restrictions restr = null;

                            if (table == treeTableExplorerItems && jtblExplorerRoots.getSelectedRow() >= 0) {
                                TreeGridModel.TreeGridNode gridNode = (TreeGridModel.TreeGridNode) treeTableExplorerItems.getValueAt(row, 0);
                                TreeGridRoleResourceRow item = (TreeGridRoleResourceRow) gridNode.getGridItem();


                                Id parentId = paragraphs.get(jtblExplorerRoots.getSelectedRow()).getId();
                                Id childId = item.explorerItem == null ? item.incorrectId : item.explorerItem.getId();
                                restr = role.getOverwriteResourceRestrictions(AdsRoleDef.generateResHashKey(
                                        EDrcResourceType.EXPLORER_ROOT_ITEM,
                                        parentId, parentId != childId ? childId : null), null);
                            }
                            if (table == jtblServerResource) {
                                EDrcServerResource res = indexToServerResource(row);
                                restr = role.getOverwriteResourceRestrictions(AdsRoleDef.generateResHashKey(
                                        EDrcResourceType.SERVER_RESOURCE, res), null);
                            } else if (table == jtblExplorerRoots) {
                                restr = role.getOverwriteResourceRestrictions(AdsRoleDef.generateResHashKey(
                                        EDrcResourceType.EXPLORER_ROOT_ITEM,
                                        paragraphs.get(row).getId(), null), null);
                            } else if (table == jtblContextlessCommands) {

                                CoverFoo cf = contextlessCommands.get(row);
                                restr = role.getOverwriteResourceRestrictions(AdsRoleDef.generateResHashKey(
                                        EDrcResourceType.CONTEXTLESS_COMMAND,
                                        cf.getId(), null), null);

                            } else if (restr != null && !restr.isDenied(ERestriction.ACCESS)) {
                                currColor = Color.BLUE;
                            }
                            c.setForeground(currColor);
                        } else {

                            boolean isRed = false;
                            if (table == jtblContextlessCommands) {
                                CoverFoo cf = contextlessCommands.get(row);
                                isRed = cf.item == null;
                            }
                            if (table == jtblExplorerRoots) {
                                CoverFoo par = paragraphs.get(row);
                                isRed = par.item == null;
                            }

                            if (isRed) {
                                c.setForeground(Color.RED);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                        }

                    }


                }
            }
            return c;
        }
    }

    class TableCellBlueColorRenderer extends DefaultTableCellRenderer {

        //Color.RED);
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            boolean isIncorrect = false;
            if (row > -1) {
//                System.out.println("----------------------");
//                System.out.println(value.getClass());
                if (value instanceof FooItem) {
                    FooItem foo = (FooItem) value;
                    AdsScopeCommandDefCover cover = (AdsScopeCommandDefCover) foo.GetMyObject();
                    isIncorrect = cover.cmd == null;
                }
                if (isIncorrect) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }

                //c.setForeground(Color.RED);
                c.setBackground(backgroundColor);
            }
            return c;
        }
    }

//    class TableCellBlueColorRendererForEnabledCmd extends DefaultTableCellRenderer {
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                boolean hasFocus, int row, int column) {
//            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//            if (row > -1)
//            {
//             if (blueInheritEditorEnabledCommands.get(row))
//                 c.setForeground(Color.BLUE);
//             else
//                 c.setForeground(Color.BLACK);
//            }
//            return c;
//
//        }
//    }
    private void initContextlessCommandsTable() {
        jtblContextlessCommands = new SimpleTable();


        jtblContextlessCommandsModel = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Command", "Inherited Rights", "Own Rights", "Total Rights", "Rights"
        }) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Object.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 2) {
                    if (!isSuperAdmin && !roleIsReadOnly) {
                        int y = rowIndex;
                        if (contextlessCommands.get(y).isCanForbid()) {
                            if (comboBoxEx.getItemCount() == 2) {
                                comboBoxEx.addItem(Forbidden);
                            }
                        } else {
                            if (comboBoxEx.getItemCount() == 3) {
                                comboBoxEx.removeItemAt(2);
                            }
                        }
                        return true;
                    }
                }
                if (columnIndex == 4) {
                    return (!isSuperAdmin && !roleIsReadOnly);
                }
                return false;
            }
        };


        jtblContextlessCommands.setModel(jtblContextlessCommandsModel);
        jScrollPaneContextlessCommands.setViewportView(jtblContextlessCommands);




        jtblContextlessCommands.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                stateCmdDisableStatus();
            }
        });

        jtblContextlessCommands.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                stateCmdDisableStatus();
            }
        });

        jtblContextlessCommands.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (canIgnoreChangeEvent(evt)) {
                    return;
                }
                if (role == null || !role.isInBranch()) {
                    return;
                }
                int index = jtblContextlessCommands.getSelectedRow();
                if (index > -1) {
                    if (jtblContextlessCommands.getSelectedColumn() == 2) {
                        String value = (String) jtblContextlessCommandsModel.getValueAt(index, 2);
                        if (value.equals(Inherit)) {
                            role.RemoveResourceRestrictions(AdsRoleDef.generateResHashKey(EDrcResourceType.CONTEXTLESS_COMMAND, contextlessCommands.get(index).getId(), null));
                            jtblContextlessCommands.setValueAt(jtblContextlessCommands.getValueAt(index, 1), index, 3);
                        } else {
                            jtblContextlessCommands.setValueAt(value, index, 3);
                            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                    EDrcResourceType.CONTEXTLESS_COMMAND,
                                    contextlessCommands.get(index).getId(),
                                    null,
                                    Restrictions.Factory.newInstance(role, value.equals(Allowed) ? 0 : ERestriction.ACCESS.getValue().longValue())));
                        }

                    } else if (jtblContextlessCommands.getSelectedColumn() == 4) {
                        Boolean value = (Boolean) jtblContextlessCommandsModel.getValueAt(index, 4);
                        if (value) {
                            jtblContextlessCommands.setValueAt(Allowed, index, 2);
                            jtblContextlessCommands.setValueAt(Allowed, index, 3);
                            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                    EDrcResourceType.CONTEXTLESS_COMMAND,
                                    contextlessCommands.get(index).getId(), null,
                                    Restrictions.Factory.newInstance(role, 0)));
                        } else {
                            jtblContextlessCommands.setValueAt(Forbidden, index, 2);
                            jtblContextlessCommands.setValueAt(Forbidden, index, 3);
                            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                    EDrcResourceType.CONTEXTLESS_COMMAND,
                                    contextlessCommands.get(index).getId(), null,
                                    Restrictions.Factory.newInstance(role, ERestriction.ACCESS.getValue().longValue())));
                        }
                    }
                    jtblContextlessCommands.setValueAt(jtblContextlessCommands.getValueAt(index, 0), index, 0);
                }
                stateCmdDisableStatus();
            }
        });

        TableColumnModel columnModel = jtblContextlessCommands.getColumnModel();

        TableColumn column = columnModel.getColumn(2);
        column.setCellEditor(new DefaultCellEditor(comboBoxEx));

        column = columnModel.getColumn(4);
        column.setCellEditor(new DefaultCellEditor(checkBoxEx));
        column.setCellRenderer(new BooleanRendererForMainColumns());



        if (jtblContextlessCommands.getRowCount() > 0) {
            jtblContextlessCommands.setRowSelectionInterval(0, 0);
        }
        jtblContextlessCommands.setDefaultRenderer(Object.class, whiteTableCellRendererWithBold);
        for (int i = 0; i < 4; i++) {
            if (i == 2) {
                continue;
            }
            column = columnModel.getColumn(i);
            column.setCellRenderer(grayTableCellRendererWithBold);
        }
        jtblContextlessCommands.getTableHeader().setReorderingAllowed(false);

    }

    private class BooleanRendererForMainColumns extends JCheckBox
            implements TableCellRenderer, Serializable {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            this.setHorizontalAlignment(SwingConstants.CENTER);

            setSelected((value != null && ((Boolean) value).booleanValue()));

            boolean isGray = roleIsReadOnly || isSuperAdmin;
            if (isGray) {
                this.setEnabled(false);
                setBackground(backgroundColor);
            } else {
                this.setEnabled(true);
                setBackground(Color.WHITE);
            }

            return this;
        }
    }

    private class BooleanRendererForMainColumns2 extends JCheckBox
            implements TableCellRenderer, Serializable {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            this.setHorizontalAlignment(SwingConstants.CENTER);

            setSelected((value != null && ((Boolean) value).booleanValue()));

            boolean isGray = roleIsReadOnly || isSuperAdmin || column != 2 && column != 4;
            if (lastSelectorPresentationId == null
                    || selectorCommandsList == null
                    || currCoverEntityObjectClasses == null) {
                isGray = true;
            } else {
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION,
                        currCoverEntityObjectClasses.clazz.getId(),
                        lastSelectorPresentationId);
                Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
                if (r == null) {
                    isGray = true;
                } else if (r.isDenied(ERestriction.ACCESS) || !r.isDenied(ERestriction.ANY_COMMAND)) {
                    isGray = true;
                }
                if (!isGray) {
                    Restrictions or = role.getOverwriteResourceRestrictions(hash, null);

                    FooItem val = (FooItem) table.getValueAt(row, 0);
                    Boolean currVal = (Boolean) table.getValueAt(row, 2);

                    if (currVal != null && currVal) {
                        AdsScopeCommandDefCover cmd = (AdsScopeCommandDefCover) val.GetMyObject();
                        List<Id> lst = or.getEnabledCommandIds();
                        isGray = lst != null && lst.contains(cmd.getId());
                    }
                }
            }

            if (isGray) {
                this.setEnabled(false);
                setBackground(backgroundColor);
            } else {
                this.setEnabled(true);
                setBackground(Color.WHITE);
            }

            return this;
        }
    }

    private class BooleanRenderer extends JCheckBox
            implements TableCellRenderer, Serializable {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            boolean isVal = value != null && ((Boolean) value).booleanValue() ? true : false;

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            if (treeTableExplorerItems == table) {
                setSelected(isVal);

            } else {
                setSelected(
                        ((jtblCurrentEditorRights == table || jtblCurrentSelectorRights == table)
                        || (column == 1/*
                         * || column == 2
                         */)
                        && !(jtblCurrentEditorRights == table || jtblCurrentSelectorRights == table))
                        && isVal);
            }

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(new EmptyBorder(1, 1, 1, 1));
            }
            this.setHorizontalAlignment(SwingConstants.CENTER);

            boolean isGray =
                    roleIsReadOnly
                    || isSuperAdmin
                    || //table == jtblCurrentRights
                    //&&
                    column == 0
                    || (column == 1
                    || column == 2
                    && ((jtblCurrentSelectorRights == table
                    && (isDisableTableCurrentSelectorRights2
                    || row != 0 && isDisableTableCurrentSelectorRights))
                    || (jtblCurrentEditorRights == table
                    && (isDisableTableCurrentEditorRights2
                    || row != 0 && isDisableTableCurrentEditorRights)))
                    || column == 3)
                    && (jtblCurrentEditorRights == table || jtblCurrentSelectorRights == table);


            if (!isGray && jtblCurrentEditorRights == table && column == 2 && row == 7) {
                Boolean fl = (Boolean) jtblCurrentEditorRights.getValueAt(4, 2);
                if (fl == null || !fl) {
                    isGray = true;
                }
            }


            if (!isGray && jtblCurrentEditorRights == table) {
                isGray = blueInheritEditorRights[row];
            }

            if (!isGray && jtblCurrentSelectorRights == table && isVal)// && isSelected)
            {
                isGray = blueInheritSelectorRights[row];
            }


            if (isGray) {
                this.setEnabled(false);
                if (!isSelected) {
                    setBackground(backgroundColor);
                }
            } else {
                this.setEnabled(true);
                if (!isSelected) {
                    setBackground(Color.WHITE);
                }
            }

            return this;
        }
    };

    private class BooleanRenderer2 extends JCheckBox
            implements TableCellRenderer, Serializable {

        ERestriction restr;

        BooleanRenderer2(ERestriction restr) {
            this.restr = restr;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            setSelected(value != null && ((Boolean) value).booleanValue());
            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(new EmptyBorder(1, 1, 1, 1));
            }
            this.setHorizontalAlignment(SwingConstants.CENTER);

            //boolean isRed = false;
            boolean isGray = roleIsReadOnly
                    || isSuperAdmin
                    || (column == 1 || column == 3);
            if (!isGray) {
                if (column == 2 || column == 4) {
                    if (currCoverEditorPresentation == null
                            || currCoverEditorPresentation.getEditorPresentation() == null
                            || currCoverEditorPresentation.getEditorPresentation().getOwnerClass() == null) {
                        isGray = true;
                    } else {
                        String hash =
                                AdsRoleDef.generateResHashKey(
                                EDrcResourceType.EDITOR_PRESENTATION,
                                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                                currCoverEditorPresentation.getEditorPresentation().getId());
                        Restrictions rest = role.getOnlyCurrentResourceRestrictions(hash);
                        if (rest == null) {
                            isGray = true;
                        } else {
                            if (rest.isDenied(ERestriction.ACCESS)) {
                                isGray = true;
                            } else {
                                if (ERestriction.ANY_PAGES == restr) {
                                    isGray = !(rest.isDenied(ERestriction.ANY_PAGES) && !rest.isDenied(ERestriction.VIEW));
                                } else {
                                    isGray = !rest.isDenied(restr);
                                }

                            }
                        }

                        if (!isGray) {
                            Restrictions or = role.getOverwriteResourceRestrictions(hash, currCoverEditorPresentation.getEditorPresentation());

                            Object obj = table.getValueAt(row, 0);

                            if (obj instanceof FooItem) {
                                FooItem val = (FooItem) obj;
                                Boolean currVal = (Boolean) table.getValueAt(row, 2);
                                if (currVal != null && currVal) {
                                    AdsScopeCommandDefCover cmd = (AdsScopeCommandDefCover) val.GetMyObject();
                                    //isRed = cmd.cmd == null;
                                    List<Id> lst = or.getEnabledCommandIds();
                                    isGray = lst.contains(cmd.getId());
                                }
                            } else if (obj instanceof TreeGridNode) {
                                TreeGridNode t = (TreeGridNode) obj;

                                if (t.getGridItem() != null) {
                                    if (t.getGridItem() instanceof TreeGridRoleResourceEdPresentationExplorerItemRow) {
                                        TreeGridRoleResourceEdPresentationExplorerItemRow item =
                                                (TreeGridRoleResourceEdPresentationExplorerItemRow) t.getGridItem();
                                        List<Id> lst = or.getEnabledChildIds();
                                        isGray = item.explorerItem == null ? false
                                                : lst.contains(item.explorerItem.getId());
                                    } else {
                                        TreeGridRoleResourceEdPresentationPageRow item =
                                                (TreeGridRoleResourceEdPresentationPageRow) t.getGridItem();
                                        List<Id> lst = or.getEnabledPageIds();
                                        isGray = item.page == null ? false
                                                : lst.contains(item.page.getId());

                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isGray) {
                this.setEnabled(false);
                if (!isSelected) {
                    setBackground(backgroundColor);
                }
            } else {
                this.setEnabled(true);
                if (!isSelected) {
                    setBackground(Color.WHITE);
                }
            }
//            if (isRed){
//                setForeground(Color.RED);
//                setBackground(Color.RED);
//            }

            return this;
        }
    };

    private class DefaultMutableTreeNodeEx extends DefaultMutableTreeNode {

        private final Icon ico;
        Object obj;
        Bool2 bool2;

        DefaultMutableTreeNodeEx(Icon ico, Object obj, Bool2 bool2) {

            this.bool2 = bool2;
            this.ico = ico;
            this.obj = obj;
        }

        public Icon getIco() {
            return ico;
        }

        @Override
        public String toString() {
            return obj.toString();
        }
    }

    private class TreeCellRendererEx extends DefaultTreeCellRenderer {
        /*
         * @Override public Icon getDefaultClosedIcon() { return
         * super.getDefaultClosedIcon(); }
         */

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNodeEx) {
                DefaultMutableTreeNodeEx val = (DefaultMutableTreeNodeEx) value;
                setIcon(val.getIco());
                if (val.bool2.isBold) {
                    this.setFont(getBoldFont());
                } else {
                    this.setFont(getNormalFont());
                }
                if (val.bool2.color != Color.BLACK) {
                    this.setForeground(val.bool2.color);
                }
                // this.setForeground(Color.red);
            }
            return c;
        }
    }

    private void collectVisiblePresentation(List<AdsEditorPresentationDef> outList,
            TreeGridRoleResourceRowForEditorPresentation item) {
        if (item.cover.epr != null) {
            outList.add(item.cover.epr);
        }
        for (TreeGridRoleResourceRowForEditorPresentation child : item.list) {
            collectVisiblePresentation(outList, child);
        }
    }

    private void innateCollectPres(List<AdsEditorPresentationDef> lst, CoverEntityObjectClasses classCover) {
        if (classCover.clazz == null) {
            return;
        }
        List<AdsEditorPresentationDef> list =
                classCover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
        removeOverwriteItems(list);
        for (AdsEditorPresentationDef pres : list) {
            lst.add(pres);
        }


        for (CoverEntityObjectClasses chld : classCover.childs) {
            innateCollectPres(lst, chld);
        }
    }

    private void collectPresentations(CoverEntityObjectClasses classCover) {
        List<AdsEditorPresentationDef> pres_ = new ArrayList<AdsEditorPresentationDef>();
        innateCollectPres(pres_, classCover);
        innatecollectPresentations(classCover, pres_);

    }

    private void innatecollectPresentations(CoverEntityObjectClasses classCover, List<AdsEditorPresentationDef> lst) {
        if (classCover.clazz == null) {
            return;
        }
        List<AdsEditorPresentationDef> list =
                classCover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
        removeOverwriteItems(list);

        //role.getResources()


        for (AdsEditorPresentationDef pres : list) {
//            AdsEditorPresentationDef ownerPres = null;
//
//            if (pres.isRightsInheritanceModeInherited()) {
//                ownerPres = pres.findRightsInheritanceDefinePresentation().get();
//            } else if (pres.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_REPLACED)) {
//                ownerPres = pres.findReplacedEditorPresentation().get();
//            } else if (pres.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_DEFINED)) {
//                ownerPres = pres.findRightSourceEditorPresentation();
//            }
////            if (pres.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_REPLACED))
////            {
////             AdsEditorPresentationDef ownerPres = pres.findReplacedEditorPresentation();
//            if (ownerPres != null && !lst.contains(ownerPres) && ownerPres.getId() != pres.getId()) {
//                continue;
//            }
//            }
            CoverEditorPresentation cover = new CoverEditorPresentation(pres);
            editorPresantationMap.put(pres.getId(), cover);
        }
        List<Id> lstRes = allEditorPresantationMap.get(classCover.clazz.getId());
        if (lstRes != null) {
            for (Id id : lstRes) {
                boolean isFind = false;
                for (AdsEditorPresentationDef pres : list) {
                    if (pres.getId().equals(id)) {
                        isFind = true;
                        break;
                    }
                }
                if (!isFind) {
                    CoverEditorPresentation cover = new CoverEditorPresentation(id, classCover.clazz.getId());
                    cover.setVisible(true);
                    editorPresantationMap.put(id, cover);
                }
            }

        }

        for (CoverEntityObjectClasses chld : classCover.childs) {
            innatecollectPresentations(chld, lst);
        }

    }
    //entityObjectClassesMap.put(def.getId(), new CoverEntityObjectClasses((AdsEntityObjectClassDef)def));

    private void clearCoverEntityObjectClassesRights(CoverEntityObjectClasses cover) {
        cover.setRightsOrRightsInChilds(false);
        cover.treeNodeEx = null;
        for (CoverEntityObjectClasses chld : cover.childs) {
            clearCoverEntityObjectClassesRights(chld);
        }
    }

    private void refreshCoverEntityObjectClassesRights(CoverEntityObjectClasses cover) {
        AdsEntityObjectClassDef curr = null;
        if (currCoverEntityObjectClasses != null) {
            curr = currCoverEntityObjectClasses.clazz;
        }

        boolean isAllow = false;
//            if (!isShowInheritClassesResources && curr instanceof AdsApplicationClassDef)
//            {
//            }
//            else
        isAllow =
                curr == cover.clazz
                || isEntityDefContaintAllowedPresentationInnate(cover.clazz);
        if (isAllow) {
            CoverEntityObjectClasses tmp = cover;
            while (tmp != null) {
                tmp.setRightsOrRightsInChilds(true);
                tmp = tmp.parent;
            }
        }
        //cover.isRightsOrRightsInChilds = isAllow;
//        if (isShowInheritClassesResources)
        for (CoverEntityObjectClasses chld : cover.childs) {
            refreshCoverEntityObjectClassesRights(chld);
        }
    }

    void insertBranchInTheTree() {
    }

    void refreshEntityObjectTreeBrunch() {
        if (currCoverEntityObjectClasses == null) {
            return;
        }
        //AdsEntityObjectClassDef clazz = currCoverEntityObjectClasses.clazz;

        CoverEntityObjectClasses tmp = currCoverEntityObjectClasses;
        while (true) {
            if (tmp.parent.clazz == null) {
                break;
            }
            tmp = tmp.parent;
        }
        //int index;
        //

        DefaultTreeModel model = (DefaultTreeModel) jEntityAndApplTree.getModel();

        Object root = model.getRoot();
        int cnt = model.getChildCount(root);
        DefaultMutableTreeNodeEx node = null;
        int index = 0;
        for (int i = 0; i < cnt; i++) {
            DefaultMutableTreeNodeEx node2 = (DefaultMutableTreeNodeEx) model.getChild(root, i);
            CoverEntityObjectClasses cover = (CoverEntityObjectClasses) node2.obj;
            if (cover == tmp) {
                node = node2;
                index = i;
                break;
            }
        }
        if (node != null) {
            model.removeNodeFromParent(node);
        }

        clearCoverEntityObjectClassesRights(tmp);
        refreshCoverEntityObjectClassesRights(tmp);

        Bool2 bool2 = isEntityDefContaintAllowedPresentation(tmp.clazz);
        DefaultMutableTreeNodeEx newNode =
                new DefaultMutableTreeNodeEx(tmp.clazz == null ? null : tmp.clazz.getIcon().getIcon(),
                tmp, bool2);
        tmp.treeNodeEx = newNode;
        model.insertNodeInto(newNode, (MutableTreeNode) root, index);

        for (CoverEntityObjectClasses chld : tmp.childs) {
            addChildClass(newNode, chld);
        }

        currCoverEntityObjectClasses.setRightsOrRightsInChilds(true);

        if (currCoverEntityObjectClasses.treeNodeEx != null) {
            TreeNode[] pathes = currCoverEntityObjectClasses.treeNodeEx.getPath();
            TreePath path = new TreePath(pathes);
            jEntityAndApplTree.expandPath(path);
            jEntityAndApplTree.setSelectionPath(path);
        }

        refreshEntityTree();

    }

    private void refreshSelectorPresentationList(AdsSelectorPresentationDef visiblePres) {
        Object currObject =
                jEntityAndApplTree.getSelectionPath() == null
                ? null
                : jEntityAndApplTree.getSelectionPath().getLastPathComponent();
        DefaultTableModel selectorModel = (DefaultTableModel) jtblSelectorPresentation.getModel();
        if (currObject == null || jEntityAndApplTree.getRowCount() == 0) {
            selectorModel.setRowCount(0);

            currCoverEntityObjectClasses = null;
            lastEditorPresentationId = null;
            lastSelectorPresentationId = null;
            stateEnabledCommandsButtons();
            return;
        }


        DefaultMutableTreeNodeEx currNode = (DefaultMutableTreeNodeEx) currObject;
        currCoverEntityObjectClasses = (CoverEntityObjectClasses) currNode.obj;

        entitySelectorPresentations = new ArrayList<>();

        List<AdsSelectorPresentationDef> list;
        if (currCoverEntityObjectClasses.clazz != null) {
            list = currCoverEntityObjectClasses.clazz.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
            for (AdsSelectorPresentationDef sp : list) {
                entitySelectorPresentations.add(new CoverSelectorPresentation(sp));
            }
        } else {
            list = new ArrayList<>(0);
        }

        removeOverwriteItems(entitySelectorPresentations);


        if (currCoverEntityObjectClasses.clazz != null) {
            final List<Id> lst = allSelectorPresantationMap.get(currCoverEntityObjectClasses.clazz.getId());
            if (lst != null) {
                for (Id id : lst) {
                    boolean isFind = false;
                    for (AdsSelectorPresentationDef sp : list) {
                        if (sp.getId().equals(id)) {
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        entitySelectorPresentations.add(new CoverSelectorPresentation(id));
                    }
                }
            }
        }

        RadixObjectsUtils.sortByName(entitySelectorPresentations);





        int nSp = entitySelectorPresentations.size();
        selectorModel.setRowCount(nSp);
        RadixObjectsUtils.sortByName(entitySelectorPresentations);



        for (int j = 0; j < nSp; j++) {
            CoverSelectorPresentation spd = entitySelectorPresentations.get(j);
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.SELECTOR_PRESENTATION,
                    currCoverEntityObjectClasses.clazz.getId(),
                    spd.getId());
            Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
            boolean isBold = false;
            Color color = greyColor;
            if (r != null) {
                color = Color.BLACK;
                if (!r.isDenied(ERestriction.ACCESS)) {
                    isBold = true;
                }
            } else {
                Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
                Restrictions or = role.getOverwriteResourceRestrictions(hash, null);
                if (!or.isDenied(ERestriction.ACCESS)) {
                    color = Color.BLUE;
                } else {
                    color = greyColor;
                }

                if (!ar.isDenied(ERestriction.ACCESS)) {
                    isBold = true;
                }
            }
            if (spd.incorrectId != null) {
                color = Color.RED;
            }
//            else
//                color = greyColor;
            //    color = color;
            selectorModel.setValueAt(new FooItemWithBool(spd.getName(), isBold, color), j, 0);
            selectorModel.setValueAt(r != null, j, 1);
        }

        if (nSp > 0) {
            jtblSelectorPresentation.setRowSelectionInterval(0, 0);
        }

    }

    static AdsEditorPresentationDef getPresentationParent(AdsEditorPresentationDef pres) {
        if (pres.isRightsInheritanceModeInherited()) {
            return pres.findRightsInheritanceDefinePresentation().get();
        }
        if (pres.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_REPLACED)) {
            return pres.findReplacedEditorPresentation().get();
        }
        if (pres.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_DEFINED)) {
            return pres.findRightSourceEditorPresentation();
        }
        return null;
    }

    void refreshEditorPresentationTree(AdsEditorPresentationDef visiblePres) {
        if (jEntityAndApplTree.getSelectionPath() == null) {
            CoverEditorPresentation rootCover = new CoverEditorPresentation(null, null);
            treeTableEditorPresentationsModel = new TreeGridModel(null, cNames2, cTypes2);
            TreeGridRoleResourceRowForEditorPresentation root2 =
                    new TreeGridRoleResourceRowForEditorPresentation(rootCover);
            treeTableEditorPresentationsModel.openRoot(root2);
            treeTableEditorPresentations.setRootVisible(false);
            treeTableEditorPresentations.afterOpen(treeTableEditorPresentationsModel, backgroundColor);
            return;
        }
        Object currObject = jEntityAndApplTree.getSelectionPath().getLastPathComponent();
        DefaultMutableTreeNodeEx currNode = (DefaultMutableTreeNodeEx) currObject;
        currCoverEntityObjectClasses = (CoverEntityObjectClasses) currNode.obj;

        CoverEntityObjectClasses parentCover_ = currCoverEntityObjectClasses;
        while (true) {
            if (parentCover_.parent.clazz == null) {
                break;
            }
            parentCover_ = parentCover_.parent;
        }


        AdsEditorPresentationDef currEdPres = null;
        if (currCoverEditorPresentation != null) {
            currEdPres = currCoverEditorPresentation.epr;
        }


        if (currCoverEntityObjectClasses == null) {
            return;
        }
        editorPresantationMap.clear();

        //AdsEntityObjectClassDef clazz = currCoverEntityObjectClasses.clazz;
        //get all presentation to this class and all subclasses

        collectPresentations(parentCover_);

        CoverEditorPresentation rootCover = new CoverEditorPresentation(null, null);
        List<AdsEditorPresentationDef> lst = null;
        if (currCoverEntityObjectClasses.clazz != null) {
            lst = currCoverEntityObjectClasses.clazz.getPresentations().getEditorPresentations().
                    get(EScope.ALL);
            removeOverwriteItems(lst);


            for (AdsEditorPresentationDef pres : lst) {
//                AdsEditorPresentationDef ownPres = getPresentationParent(pres);
//                if (ownPres != null && lst.contains(ownPres)) {
//                    continue;
//                }
                AdsEditorPresentationDef tmp = pres;
                boolean mustContinue = false;
                while(true){                    
                    AdsEditorPresentationDef ownPres = getPresentationParent(tmp);
                    if (ownPres == null || tmp == ownPres){
                        break;
                    }                    
                    if (lst.contains(ownPres)){
                        mustContinue = true;
                        break;
                    }
                    tmp = ownPres;
                }
                if (mustContinue){
                    continue;
                }
                
                


                CoverEditorPresentation presFromFirstList = editorPresantationMap.get(pres.getId());
                if (presFromFirstList!=null){
                    String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                            presFromFirstList.epr.getOwnerClass().getId(),
                            presFromFirstList.epr.getId());
                    Restrictions rest = role.getResourceRestrictions(hash, presFromFirstList.epr);
                    //Restrictions rest = getEditorPresentationRestriction(true, role, presFromFirstList);
                    presFromFirstList.setVisible(!rest.isDenied(ERestriction.ACCESS));
                    presFromFirstList.parent = rootCover;
                    rootCover.childs.add(presFromFirstList);
                }
            }

            //Ищем корявые ресурсы ...
            List<Id> lstRes = allEditorPresantationMap.get(currCoverEntityObjectClasses.clazz.getId());
            if (lstRes != null) {
                for (Id id : lstRes) {
                    boolean isFind = false;
                    for (AdsEditorPresentationDef pres : lst) {
                        if (pres.getId().equals(id)) {
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        CoverEditorPresentation presFromFirstList = editorPresantationMap.get(id);
                        presFromFirstList.setVisible(true);
                        presFromFirstList.parent = rootCover;
                        rootCover.childs.add(presFromFirstList);
                    }
                }
            }
        }

        Iterator<Map.Entry<Id, CoverEditorPresentation>> iter =
                editorPresantationMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Id, CoverEditorPresentation> entry = iter.next();
            AdsEditorPresentationDef pres = entry.getValue().getEditorPresentation();


            Id replacedOrParentEditorPresentationId = null;
            if (pres != null) {
                if (pres.isRightsInheritanceModeInherited()) {
                    replacedOrParentEditorPresentationId = pres.getBasePresentationId();
                } else if (pres.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_REPLACED)) {
                    replacedOrParentEditorPresentationId = pres.getReplacedEditorPresentationId();
                } else if (pres.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_DEFINED)) {
                    replacedOrParentEditorPresentationId = pres.getRightsSourceEditorPresentationId();
                }
            }



            {
                CoverEditorPresentation parentCover =
                        replacedOrParentEditorPresentationId == null
                        ? null
                        : editorPresantationMap.get(replacedOrParentEditorPresentationId);
                //if (parentCover!=null)
                {
                    CoverEditorPresentation childCover = entry.getValue();
                    String hash;
                    if (childCover.epr != null) {
                        hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                                childCover.epr.getOwnerClass().getId(),
                                childCover.epr.getId());
                    } else {
                        hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                                childCover.parentId,
                                childCover.incorrectId);
                    }
                    boolean isMustAdd = role.getOnlyCurrentResourceRestrictions(hash) != null;
                    //isMustAdd = true;
                    if (parentCover != null) {
                        parentCover.childs.add(entry.getValue());
                        childCover.parent = parentCover;
                    }

                    if (!isMustAdd) {
                        //Restrictions rest = getEditorPresentationRestriction(true, role, childCover);
                        String hash2;
                        if (childCover.epr != null) {
                            hash2 = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                                    childCover.epr.getOwnerClass().getId(),
                                    childCover.epr.getId());
                            Restrictions rest = role.getAncestorResourceRestrictions(hash2, childCover.epr);
                            isMustAdd = !rest.isDenied(ERestriction.ACCESS);
                        } else {
                            isMustAdd = false;
                        }
                    }


                    if (isMustAdd || visiblePres == childCover.epr) {
                        List<CoverEditorPresentation> recLst = new ArrayList<>();
                        CoverEditorPresentation tmp = childCover;
                        while (tmp != null) {

                            tmp.setVisible(true);
                            tmp = tmp.parent;
                            if (recLst.contains(tmp)) {
                                break;
                            }
                            recLst.add(tmp);
                        }
                    }
                }
            }
        }



        iter = editorPresantationMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Id, CoverEditorPresentation> entry = iter.next();
            if (!entry.getValue().childs.isEmpty()) {
                RadixObjectsUtils.sortByName(entry.getValue().childs);
            }
        }

        editorPresantationMap.put(unicalId, rootCover);

        treeTableEditorPresentationsModel = new TreeGridModel(null, cNames2, cTypes2);
        TreeGridRoleResourceRowForEditorPresentation root2 =
                new TreeGridRoleResourceRowForEditorPresentation(rootCover);
        treeTableEditorPresentationsModel.openRoot(root2);
        treeTableEditorPresentations.afterOpen(treeTableEditorPresentationsModel, backgroundColor);
        //boolean isAncestors = role.getAncestorIds().size()>0 || role.isOverwrite();
        for (int u = 0; u < 2; u++) {
            treeTableEditorPresentations.getColumnModel().getColumn(1).setMaxWidth(70);
            treeTableEditorPresentations.getColumnModel().getColumn(1).setMinWidth(40);
            treeTableEditorPresentations.getColumnModel().getColumn(1).setWidth(70);
        }



        treeTableEditorPresentations.setRootVisible(false);
        treeTableEditorPresentations.expandAll();
        boolean isMustMoveToFirst = true;

        currCoverEditorPresentation = null;

        if (currEdPres != null) {
            for (int i = 0; i < treeTableEditorPresentations.getRowCount(); i++) {
                TreeGridNode node = (TreeGridNode) treeTableEditorPresentations.getValueAt(i, 0);
                TreeGridRoleResourceRowForEditorPresentation rowEx =
                        (TreeGridRoleResourceRowForEditorPresentation) node.getGridItem();
                if (rowEx.cover.epr == currEdPres) {
                    treeTableEditorPresentations.getSelectionModel().setSelectionInterval(i, i);
                    currCoverEditorPresentation = rowEx.cover;
                    isMustMoveToFirst = false;
                    break;
                }
            }
        }

        if (isMustMoveToFirst && treeTableEditorPresentations.getRowCount() > 0) {
            treeTableEditorPresentations.getSelectionModel().setSelectionInterval(0, 0);
            currCoverEditorPresentation = rootCover.childs.get(0);
        }
        treeTableEditorPresentations.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        fillEditorPresentationRightsAndCommand();

    }

    public class TreeGridRoleResourceRowForEditorPresentation extends TreeGridRow {

        CoverEditorPresentation cover;
        String hash;
        List<TreeGridRoleResourceRowForEditorPresentation> list;

        TreeGridRoleResourceRowForEditorPresentation(CoverEditorPresentation cover) {
            this.cover = cover;
            list = new ArrayList<>(cover.childs.size());
            hash = cover.epr == null
                    ? null
                    : AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                    cover.epr.getOwnerClass().getId(),
                    cover.epr.getId());


            for (CoverEditorPresentation childCover : cover.childs) {
                if (childCover.isVisible()) {
                    List<CoverEditorPresentation> recLst = new ArrayList<>();
                    //защита от рекурсии
                    CoverEditorPresentation tmp = cover.parent;

                    //boolean isMustBreak = false;
                    while (tmp != null //                            &&
                            //                            (
                            //                            tmp.epr!=null
                            //                            ||
                            //                            tmp.incorrectId!=null
                            //                            )
                            ) {
                        if (recLst.contains(tmp)) {
                            //      isMustBreak = true;
                            break;
                        }
                        recLst.add(tmp);
                        tmp = tmp.parent;
                    }
                    //if (isMustBreak)
                    //    break;

                    TreeGridRoleResourceRowForEditorPresentation chld =
                            new TreeGridRoleResourceRowForEditorPresentation(childCover);
                    list.add(chld);
                }
            }
        }

        @Override
        public Font getFont(int row) {
            //Restrictions rest = getEditorPresentationRestriction(true, role, cover);
            boolean isBold = false;

            if (cover.epr != null && cover.epr.getOwnerClass() != null) {
                String hash2 = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                        cover.epr.getOwnerClass().getId(),
                        cover.epr.getId());
                Restrictions rest = role.getResourceRestrictions(hash2, cover.epr);
                isBold = !rest.isDenied(ERestriction.ACCESS);
            }

            Font f;
            if (isBold) {
                f = new Font("Courier New", Font.BOLD, 12);
            } else {
                f = new Font("Courier New", Font.PLAIN, 12);
            }
            return f;
        }

        @Override
        public Color getForeground(int row) {
            if (cover.epr == null) {
                return Color.RED;
            }
            Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
            if (r == null || isSuperAdmin || role.isReadOnly()) {
                Restrictions or = role.getOverwriteResourceRestrictions(hash, cover.epr);
                if (!or.isDenied(ERestriction.ACCESS)) {
                    return Color.BLUE;
                }
                return Color.GRAY;
            }
            return Color.BLACK;
        }

        @Override
        public boolean isHaveChilds() {
            return !cover.childs.isEmpty();
        }

        @Override
        public boolean isMayModify(int column) {

            currCoverEditorPresentation = this.cover;
            fillEditorPresentationRightsAndCommand();
            return !isSuperAdmin && !role.isReadOnly();
        }

        Restrictions getInheritRestriction() {
            CoverEditorPresentation curr = this.cover;
            while (curr != null) {
                if (curr.epr == null) //is root element
                {
                    return null;
                }
                String currHash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                        curr.epr.getOwnerClass().getId(),
                        curr.epr.getId());
                Restrictions res = role.getOnlyCurrentResourceRestrictions(currHash);
                if (res != null) {
                    return res;
                }
                curr = curr.parent;
            }
            return role.getOverwriteAndAncestordResourceRestrictions(hash, curr.epr);
        }

        @Override
        public void CellWasModified(int column, Object val) {
            if (column != 1) {
                return;
            }

            AdsEditorPresentationDef pres = this.cover.epr;
            if (pres == null) {
                return;//root element
            }

            Boolean v = (Boolean) val;
            if (!v) {
                role.RemoveResourceRestrictions(hash);
            } else {
                Restrictions rest = //this.getInheritRestriction();
                        role.getOverwriteAndAncestordResourceRestrictions(hash, pres);
                //settingKey, pres)AncestorResourceRestrictions
                role.CreateOrReplaceResourceRestrictions(
                        new AdsRoleDef.Resource(EDrcResourceType.EDITOR_PRESENTATION,
                        pres.getOwnerClass().getId(),
                        pres.getId(),
                        rest));

            }
            currCoverEditorPresentation = this.cover;
            refreshEditorPresentationTree(this.cover.epr);
            refreshEntityObjectTreeBrunch();
            stateEntityClassDisableStatus();
            stateEnabledCommandsButtons();

        }

        @Override
        public List<? extends TreeGridRow> getChilds() {
            return list;
        }

        @Override
        public String getTitle(int column) {
            if (cover == null) {
                return null;
            }
            return cover.getName();
        }

        @Override
        public Icon getIcon(int row) {
            return AdsDefinitionIcon.EDITOR_PRESENTATION.getIcon();
        }
        private TreeGridModel.TreeGridNode row = null;

        @Override
        public TreeGridModel.TreeGridNode getRowEx() {
            return row;
        }

        @Override
        public void setRowEx(TreeGridModel.TreeGridNode row) {
            this.row = row;
        }

        @Override
        public void loadValues() {
            Object[] vals = new Object[2];
            AdsEditorPresentationDef pres = this.cover.epr;
            if (pres == null) {
                return;//root element
            }
            hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                    pres.getOwnerClass().getId(), pres.getId());
            Restrictions restr = role.getOnlyCurrentResourceRestrictions(hash);
            if (restr != null) {
                vals[1] = Boolean.TRUE;
            } else {
                vals[1] = Boolean.FALSE;
            }
            this.getRowEx().setValues(vals);
        }
    }

    void refreshEditorAndSelectorPresentationList() {
        refreshEditorPresentationTree(null);
        refreshSelectorPresentationList(null);
        fillEditorPresentationRightsAndCommand();
        fillSelectorPresentationRightsAndCommand();
    }

    private void refreshEntityTree() {

        if (jEntityAndApplTree.getSelectionPath() == null) {
            return;
        }
        Object currObject = jEntityAndApplTree.getSelectionPath().getLastPathComponent();

        DefaultMutableTreeNodeEx currNode = (DefaultMutableTreeNodeEx) currObject;
        currCoverEntityObjectClasses = (CoverEntityObjectClasses) currNode.obj;


        ((DefaultTreeModel) jEntityAndApplTree.getModel()).setRoot(
                (TreeNode) jEntityAndApplTree.getModel().getRoot());
        for (int i = 0; i < jEntityAndApplTree.getRowCount(); i++) {
            jEntityAndApplTree.expandRow(i);
        }
        TreeNode[] pathes = ((DefaultTreeModel) jEntityAndApplTree.getModel()).getPathToRoot(currNode);
        TreePath path = new TreePath(pathes);
        jEntityAndApplTree.expandPath(path);
        jEntityAndApplTree.setSelectionPath(path);
    }
    private TreeGridModel treeTableEditorPresentationsModel;
    private TreeTableExWithMouseMotionListener treeTableEditorPresentations;
    private String[] cNames2 = {"Editor presentation", "Own"};
    private Class[] cTypes2 = {TreeTableModel.class, Boolean.class};

    //private JTable jtblEditorPresentation = null;
    private static void registerComponent(JComponent c) {
        //ensure InputMap and ActionMap are created
        InputMap imap = c.getInputMap();
        //ActionMap amap = c.getActionMap();
        //put dummy KeyStroke into InputMap if is empty:
        boolean removeKeyStroke = false;
        KeyStroke[] ks = imap.keys();
        if (ks == null || ks.length == 0) {
            imap.put(KeyStroke.getKeyStroke(
                    KeyEvent.VK_BACK_SLASH, 0), "backSlash");
            removeKeyStroke = true;
        }
        //now we can register by ToolTipManager
        ToolTipManager.sharedInstance().registerComponent(c);
        //and remove dummy KeyStroke
        if (removeKeyStroke) {
            imap.remove(KeyStroke.getKeyStroke(
                    KeyEvent.VK_BACK_SLASH, 0));
        }
    }
    int jtblCurrentEditorRightsIndex = -1;

    private static boolean canIgnoreChangeEvent(java.beans.PropertyChangeEvent evt) {
        return evt != null && ("ToolTipText".equals(evt.getPropertyName()) || "ancestor".equals(evt.getPropertyName()));
    }

    private void initEntityResourceTree() {

        treeTableEditorPresentationsModel = new TreeGridModel(null, cNames2, cTypes2);
        treeTableEditorPresentations = new TreeTableExWithMouseMotionListener(treeTableEditorPresentationsModel);

        jScrollPane1.setViewportView(treeTableEditorPresentations);

        treeTableEditorPresentations.getTableHeader().setReorderingAllowed(false);
        treeTableEditorPresentations.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);


        jEntityAndApplTree = new JTreeWithMouseMotionListener();

        jScrollPaneEntitys.setViewportView(jEntityAndApplTree);
        jEntityAndApplTree.setCellRenderer(new TreeCellRendererEx());
        jEntityAndApplTree.setForeground(backgroundColor);
        jEntityAndApplTree.setBackground(backgroundColor);
        DefaultTreeCellRenderer rdr = (DefaultTreeCellRenderer) jEntityAndApplTree.getCellRenderer();
        rdr.setBackgroundNonSelectionColor(backgroundColor);

        jEntityAndApplTree.setRootVisible(false);

        jEntityAndApplTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                refreshEditorAndSelectorPresentationList();
                stateEntityClassDisableStatus();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                refreshEditorAndSelectorPresentationList();
                stateEntityClassDisableStatus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                refreshEditorAndSelectorPresentationList();
                stateEntityClassDisableStatus();
            }
        });



        jEntityAndApplTree.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                refreshEditorAndSelectorPresentationList();
                stateEntityClassDisableStatus();

            }
        });

        //refreshEditorAndSelectorPresentationList()


        //currEnabledCommandForEditorDefIds = new ArrayList<Id>();

        DefaultTableModel selectorTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Selector Presentation", "Own"
        }) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 0 ? false : !isSuperAdmin && !roleIsReadOnly;

            }
        };
        jtblSelectorPresentation.setModel(selectorTableModel);





        jtblCurrentEditorRights = new SimpleTable();
        jtblCurrentSelectorRights = new SimpleTable();

        DefaultTableModel currentEditorTableModel = new DefaultTableModel(
                new Object[][]{
            {"Access", null, null, null},
            {"Create", null, null, null},
            {"Delete", null, null, null},
            {"Update", null, null, null},
            {"View", null, null, null},
            {"Any commands", null, null, null},
            {"Any children", null, null, null},
            {"Any pages", null, null, null}
        },
                new String[]{
            "Rights", "Inherit", "Own", "Total"
        }) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                //if ()
                jtblCurrentEditorRightsIndex = rowIndex;
                if (columnIndex != 2 || isSuperAdmin
                        || roleIsReadOnly
                        || currCoverEditorPresentation == null
                        || currCoverEditorPresentation.epr == null) {
                    return false;
                }


                //if (rowIndex==0)
                //    return true;
                String hash = AdsRoleDef.generateResHashKey(
                        EDrcResourceType.EDITOR_PRESENTATION,
                        currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                        lastEditorPresentationId);
                Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
                if (r == null) {
                    return false;
                }
                ERestriction eRest = null;
                if (rowIndex != 0 && r.isDenied(ERestriction.ACCESS)) {
                    return false;
                }
                if (rowIndex == 7 && r.isDenied(ERestriction.VIEW)) {
                    return false;
                }
                switch (rowIndex) {
                    case 0:
                        eRest = ERestriction.ACCESS;
                        break;

                    case 1:
                        eRest = ERestriction.CREATE;
                        break;

                    case 2:
                        eRest = ERestriction.DELETE;
                        break;

                    case 3:
                        eRest = ERestriction.UPDATE;
                        break;

                    case 4:
                        eRest = ERestriction.VIEW;
                        break;

                    case 5:
                        eRest = ERestriction.ANY_COMMAND;
                        break;

                    case 6:
                        eRest = ERestriction.ANY_CHILD;
                        break;

                    case 7:
                        eRest = ERestriction.ANY_PAGES;
                        break;

                }

                if (r.isDenied(eRest)) {
                    return true;
                }

                Restrictions or = role.getOverwriteResourceRestrictions(hash, currCoverEditorPresentation.epr);
                return or.isDenied(eRest);




                //return false;
            }
        };

        DefaultTableModel currentSelectorTableModel = new DefaultTableModel(
                new Object[][]{
            {"Access", null, null, null},
            {"Create", null, null, null},
            {"Delete All", null, null, null},
            {"Any command", null, null, null}
        },
                new String[]{
            "Rights", "Inherit", "Own", "Total"
        }) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 2) {
                    if (!isSuperAdmin && !roleIsReadOnly && currCoverEntityObjectClasses != null) {
                        String hash = AdsRoleDef.generateResHashKey(
                                EDrcResourceType.SELECTOR_PRESENTATION,
                                currCoverEntityObjectClasses.getId(),
                                lastSelectorPresentationId);
                        Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
                        if (r == null) {
                            return false;
                        }
                        if (rowIndex != 0 && r.isDenied(ERestriction.ACCESS)) {
                            return false;
                        }


                        ERestriction eRest = null;

                        switch (rowIndex) {
                            case 0:
                                eRest = ERestriction.ACCESS;
                                break;

                            case 1:
                                eRest = ERestriction.CREATE;
                                break;

                            case 2:
                                eRest = ERestriction.DELETE_ALL;
                                break;

                            case 3:
                                eRest = ERestriction.ANY_COMMAND;
                                break;
                        }


                        if (r.isDenied(eRest)) {
                            return true;
                        }

                        Restrictions or = role.getOverwriteResourceRestrictions(hash, null);
                        return or.isDenied(eRest);


                    }
                }
                return false;
            }
        };


        jtblCurrentEditorRights.setModel(currentEditorTableModel);
        jtblCurrentSelectorRights.setModel(currentSelectorTableModel);

        PropertyChangeListener selectorPresentationChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (canIgnoreChangeEvent(evt)) {
                    return;
                }

                if (currCoverEntityObjectClasses == null) {
                    return;
                }
                if (!role.isInBranch()) {
                    return;
                }
                int row = jtblSelectorPresentation.getSelectedRow();
                if (jtblSelectorPresentation.getSelectedColumn() == 1 && row > -1) {
                    CoverSelectorPresentation spd = entitySelectorPresentations.get(row);
                    lastSelectorPresentationId = spd.getId();


                    String hash = AdsRoleDef.generateResHashKey(
                            EDrcResourceType.SELECTOR_PRESENTATION,
                            currCoverEntityObjectClasses.clazz.getId(),
                            lastSelectorPresentationId);
                    Boolean isOwn = !(Boolean) jtblSelectorPresentation.getValueAt(row, 1);
                    TableModel model = jtblSelectorPresentation.getModel();

                    boolean isBold = false;
                    Color color = greyColor;

                    Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
                    isBold = !ar.isDenied(ERestriction.ACCESS);

                    if (isOwn) {
                        role.RemoveResourceRestrictions(hash);
                        Restrictions or = role.getOverwriteResourceRestrictions(hash, null);
                        if (!or.isDenied(ERestriction.ACCESS)) {
                            color = Color.BLUE;
                        }

                    } else {
                        role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                EDrcResourceType.SELECTOR_PRESENTATION,
                                currCoverEntityObjectClasses.clazz.getId(),
                                lastSelectorPresentationId,
                                ar));
                        color = Color.BLACK;
                        Bool2 bool2 = new Bool2();
                        bool2.isBold = true;
                        bool2.color = Color.BLACK;
                        currCoverEntityObjectClasses.treeNodeEx.bool2 = bool2;
                        refreshEntityTree();

                    }

                    model.setValueAt(new FooItemWithBool(model.getValueAt(row, 0).toString(), isBold, color), row, 0);
                    checkEntityDefRowBold();
                    stateEntityClassDisableStatus();
                    fillSelectorPresentationRightsAndCommand();

                }
            }
        };
        jtblSelectorPresentation.addPropertyChangeListener(selectorPresentationChangeListener);

        PropertyChangeListener currEditorRightsChangeListener =
                new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (canIgnoreChangeEvent(evt)) {
                    return;
                }
                int row = jtblCurrentEditorRightsIndex;
                //jtblCurrentEditorRights.getSelectedRow();
                if (jtblCurrentEditorRights.getSelectedRowCount() == 1) //                        DialogUtils.messageInformation( String.valueOf(jtblCurrentEditorRights.getSelectedRows()[0]));
                {
                    if (currCoverEditorPresentation == null
                            || currCoverEditorPresentation.epr == null) {
                        return;
                    }
                }
                if (role == null || !role.isInBranch()) {
                    return;
                }

                //isUpdated = 0;
                if (row > -1 && isUpdated == 0) {
                    String hash = AdsRoleDef.generateResHashKey(
                            EDrcResourceType.EDITOR_PRESENTATION,
                            currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                            lastEditorPresentationId);
                    Restrictions oldRestrictions = role.getOnlyCurrentResourceRestrictions(hash);
                    if (oldRestrictions == null) {
                        return;
                    }


                    ERestriction restr = null;
                    switch (row) {
                        case 0:
                            restr = ERestriction.ACCESS;
                            break;
                        case 1:
                            restr = ERestriction.CREATE;
                            break;
                        case 2:
                            restr = ERestriction.DELETE;
                            break;
                        case 3:
                            restr = ERestriction.UPDATE;
                            break;
                        case 4:
                            restr = ERestriction.VIEW;
                            break;
                        case 5:
                            restr = ERestriction.ANY_COMMAND;
                            break;
                        case 6:
                            restr = ERestriction.ANY_CHILD;
                            break;
                        case 7:
                            restr = ERestriction.ANY_PAGES;
                            break;
                    }
                    long newBitMask = restr.getValue();

                    List<Id> oldEnabledCommands = oldRestrictions.getEnabledCommandIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledCommandIds());
                    List<Id> oldEnabledChilds = oldRestrictions.getEnabledChildIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledChildIds());
                    List<Id> oldEnabledPages = oldRestrictions.getEnabledPageIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledPageIds());


                    long bitMask = ERestriction.toBitField(oldRestrictions.getRestriction());

                    Boolean isSet = (Boolean) jtblCurrentEditorRights.getValueAt(row, 2);
                    jtblCurrentEditorRights.setValueAt(isSet, row, 3);
                    if (isSet == null) {
                        isSet = Boolean.FALSE;
                    }
                    boolean isMaySet = isSet;
                    Restrictions overwriteRestrictions = role.getOverwriteResourceRestrictions(hash, currCoverEditorPresentation.epr);
                    if (!isSet) {
                        isMaySet = overwriteRestrictions.getRestriction().contains(restr);
                    }

                    if (isDisableTableCurrentEditorRights && row != 0 || !isMaySet) {
                        jtblCurrentEditorRights.repaint();
                        return;
                    }


                    if ((row == 5) && !isSet) {
                        oldEnabledCommands = new ArrayList<>(overwriteRestrictions.getEnabledCommandIds());
                    } else if ((row == 6) && !isSet) {

                        oldEnabledChilds = new ArrayList<>(overwriteRestrictions.getEnabledChildIds());
                    } else if ((row == 7) && !isSet) {

                        oldEnabledPages = new ArrayList<>(overwriteRestrictions.getEnabledPageIds());
                    }



                    if (row == 0) {
                        isDisableTableCurrentEditorRights = !isSet;
                        int r = treeTableEditorPresentations.getSelectedRow();
                        if (r < 0) {
                            return;
                        }


                        treeTableEditorPresentations.setValueAt(new FooItemWithBool(treeTableEditorPresentations.getValueAt(r, 0).toString(), isSet, Color.BLACK), r, 0);
                        jtblCurrentEditorRights.repaint();
                    }


                    if (isSet) {
                        bitMask &= ~newBitMask;
                    } else {
                        bitMask |= newBitMask;
                    }


                    if ((row == 7 || row == 6 || row == 5) && !isSet) {
                        List<AdsExplorerItemDef> list =
                                currCoverEditorPresentation.getEditorPresentation().
                                getExplorerItems().
                                getChildren().
                                get(EScope.LOCAL_AND_OVERWRITE);
                        removeOverwriteItems(list);
                        List<Id> collectLst = new ArrayList<>();
                        for (AdsExplorerItemDef item : list) {
                            collectExplorerItems(collectLst, item);
                        }
                        role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                EDrcResourceType.EDITOR_PRESENTATION,
                                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                                lastEditorPresentationId,
                                //Restrictions.Factory.newInstance(role, bitMask, oldEnabledCommands, collectLst)));
                                Restrictions.Factory.newInstance(role, bitMask, oldEnabledCommands, oldEnabledChilds, oldEnabledPages)));
                    } else {
                        role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                EDrcResourceType.EDITOR_PRESENTATION,
                                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                                lastEditorPresentationId,
                                Restrictions.Factory.newInstance(role, bitMask, oldEnabledCommands, oldEnabledChilds, oldEnabledPages)));
                    }

                    if (row == 0 || row == 4 || row == 5 || row == 6 || row == 7) {
                        refreshEditorPresentationTree(currCoverEditorPresentation.getEditorPresentation());
                        refreshEntityObjectTreeBrunch();
                        stateEntityClassDisableStatus();
                        stateEnabledCommandsButtons();
                    }
                    checkEntityDefRowBold();
                }
            }
        };

        PropertyChangeListener currSelectorRightsChangeListener =
                new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (canIgnoreChangeEvent(evt)) {
                    return;
                }

                int row = jtblCurrentSelectorRights.getSelectedRow();

                if (role == null || !role.isInBranch() || currCoverEntityObjectClasses == null) {
                    return;
                }
                if (row > -1 && isUpdated == 0) {
                    String hash = AdsRoleDef.generateResHashKey(
                            EDrcResourceType.SELECTOR_PRESENTATION,
                            currCoverEntityObjectClasses.clazz.getId(),
                            lastSelectorPresentationId);
                    Restrictions oldRestrictions = role.getOnlyCurrentResourceRestrictions(hash);
                    if (oldRestrictions == null) {
                        return;
                    }
                    ERestriction restr = null;
                    switch (row) {
                        case 0:
                            restr = ERestriction.ACCESS;
                            break;
                        case 1:
                            restr = ERestriction.CREATE;
                            break;
                        case 2:
                            restr = ERestriction.DELETE_ALL;
                            break;
                        case 3:
                            restr = ERestriction.ANY_COMMAND;
                            break;
                    }
                    long newBitMask = restr.getValue();
                    List<Id> oldEnabledCommands = oldRestrictions.getEnabledCommandIds() == null ? new ArrayList<Id>() : new ArrayList<>(oldRestrictions.getEnabledCommandIds());
                    long bitMask = ERestriction.toBitField(oldRestrictions.getRestriction());
                    Boolean isSet = (Boolean) jtblCurrentSelectorRights.getValueAt(row, 2);
                    jtblCurrentSelectorRights.setValueAt(isSet, row, 3);
                    if (isSet == null) {
                        isSet = Boolean.FALSE;
                    }
                    boolean isMaySet = isSet;
                    Restrictions overwriteRestrictions = role.getOverwriteResourceRestrictions(hash, null);
                    if (!isSet) {
                        isMaySet = overwriteRestrictions.getRestriction().contains(restr);
                    }

                    if (isDisableTableCurrentSelectorRights && row != 0 || !isMaySet) {
                        fillSelectorPresentationRightsAndCommand();
                        jtblCurrentSelectorRights.repaint();
                        return;
                    }
                    if (row == 3 && !isSet) {
                        oldEnabledCommands = new ArrayList<>(overwriteRestrictions.getEnabledCommandIds());
                    }
                    if (oldEnabledCommands != null && oldEnabledCommands.size() > 0
                            && (!isSet && row == 0
                            || isSet && row == 3)) {
                        fillSelectorPresentationRightsAndCommand();
                        jtblCurrentSelectorRights.repaint();
                    }
                    if (row == 0) {
                        isDisableTableCurrentSelectorRights = !isSet;
                        int r = jtblSelectorPresentation.getSelectedRow();
                        if (r < 0) {
                            return;
                        }

                        jtblSelectorPresentation.setValueAt(new FooItemWithBool(jtblSelectorPresentation.getValueAt(r, 0).toString(), isSet, Color.BLACK), r, 0);

                        jtblCurrentSelectorRights.repaint();
                    }

                    if (isSet) {
                        bitMask &= ~newBitMask;
                    } else {
                        bitMask |= newBitMask;
                    }

                    role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                            EDrcResourceType.SELECTOR_PRESENTATION,
                            currCoverEntityObjectClasses.clazz.getId(),
                            lastSelectorPresentationId,
                            Restrictions.Factory.newInstance(role, bitMask, oldEnabledCommands, new ArrayList<Id>(), new ArrayList<Id>())));

                    if (row == 0 || row == 3) {
                        fillSelectorPresentationRightsAndCommand();

                    }
                    checkEntityDefRowBold();
                }
            }
        };

        jtblCurrentEditorRights.addPropertyChangeListener(currEditorRightsChangeListener);
        jtblCurrentSelectorRights.addPropertyChangeListener(currSelectorRightsChangeListener);

        jScrollPaneCurr.setViewportView(jtblCurrentEditorRights);
        jScrollPaneCurr1.setViewportView(jtblCurrentSelectorRights);

        currEditorModel = (DefaultTableModel) jtblCurrentEditorEnabledCmd.getModel();

        //
        TableColumn column;
        column = jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(0);
        column.setCellRenderer(new TableCellBlueColorRenderer());




        column = jtblSelectorPresentation.getColumnModel().getColumn(0);
        column.setCellRenderer(grayTableCellRendererWithBold);
        column = jtblCurrentEditorRights.getColumnModel().getColumn(0);
        column.setCellRenderer(grayTableCellRendererWithBold);
        column = jtblCurrentSelectorRights.getColumnModel().getColumn(0);
        column.setCellRenderer(grayTableCellRendererWithBold);


        BooleanRenderer renderer2 = new BooleanRenderer();
        column = jtblCurrentEditorRights.getColumnModel().getColumn(1);
        column.setCellRenderer(renderer2);
        column.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        column = jtblCurrentSelectorRights.getColumnModel().getColumn(1);
        column.setCellRenderer(renderer2);
        column.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        column = jtblCurrentEditorRights.getColumnModel().getColumn(2);
        column.setCellRenderer(renderer2);
        column = jtblCurrentSelectorRights.getColumnModel().getColumn(2);
        column.setCellRenderer(renderer2);
        column = jtblCurrentEditorRights.getColumnModel().getColumn(3);
        column.setCellRenderer(renderer2);
        column = jtblCurrentSelectorRights.getColumnModel().getColumn(3);
        column.setCellRenderer(renderer2);

        jtblCurrentSelectorRights.getTableHeader().setReorderingAllowed(false);

        jtblCurrentSelectorRights.getTableHeader().setReorderingAllowed(false);



        int rowHeight = jtblSelectorPresentation.getRowHeight();
        int delta = 46;
        {
            java.awt.GridBagConstraints gridBagConstraints = ((java.awt.GridBagLayout) jPanel17.getLayout()).getConstraints(jPanel20);


            gridBagConstraints.ipady = rowHeight * 5 + delta;

            jPanel17.remove(jPanel20);
            jPanel17.add(jPanel20, gridBagConstraints);
        }
        {
            java.awt.GridBagConstraints gridBagConstraints = ((java.awt.GridBagLayout) jPanel23.getLayout()).getConstraints(jPanel22);

            gridBagConstraints.ipady = rowHeight * 9 + delta;

            jPanel23.remove(jPanel22);
            jPanel23.add(jPanel22, gridBagConstraints);
        }


    }

    private void initEditorEnabledCmdTable() {
        //

        currEditorModel =
                new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Enabled Command", "Inherit", "Own", "Total", "Rights"
        }) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex != 2 && columnIndex != 4) {
                    return false;
                }
                if (currCoverEditorPresentation == null
                        || currCoverEditorPresentation.epr == null
                        || currCoverEntityObjectClasses == null) {
                    return false;
                }
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                        currCoverEditorPresentation.epr.getOwnerClass().getId(),
                        currCoverEditorPresentation.epr.getId());
                Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
                if (r == null) {
                    return false;
                }
                if (r.isDenied(ERestriction.ACCESS) || !r.isDenied(ERestriction.ANY_COMMAND)) {
                    return false;
                }

                Restrictions or = role.getOverwriteResourceRestrictions(hash, currCoverEditorPresentation.epr);
                List<Id> lst = or.getEnabledCommandIds();
                if (lst != null) {
                    FooItem obj = (FooItem) jtblCurrentEditorEnabledCmd.getValueAt(rowIndex, 0);
                    AdsScopeCommandDefCover cmd = (AdsScopeCommandDefCover) obj.obj;
                    Boolean fl = (Boolean) jtblCurrentEditorEnabledCmd.getValueAt(rowIndex, columnIndex);
                    return !lst.contains(cmd.getId())
                            || fl != null && !fl;
                }





                return true;
            }
        };
        jtblCurrentEditorEnabledCmd.setModel(currEditorModel);
        BooleanRenderer2 boolRenderer = new BooleanRenderer2(ERestriction.ANY_COMMAND);
        for (int i = 1; i < 5; i++) {
            jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(i).setCellRenderer(boolRenderer);
        }
        TableColumn column;
        column = jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(0);
        column.setCellRenderer(new TableCellBlueColorRenderer());


        jtblCurrentEditorEnabledCmd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (canIgnoreChangeEvent(evt)) {
                    return;
                }

                int row = jtblCurrentEditorEnabledCmd.getSelectedRow();
                if (editorCommandsList == null
                        || currCoverEditorPresentation == null
                        || row >= editorCommandsList.size()
                        || row < 0) {
                    return;
                }
                if (!role.isInBranch()) {
                    return;
                }
                int col = jtblCurrentEditorEnabledCmd.getSelectedColumn();
                if (col != 2 && col != 4) {
                    return;
                }
                int col2 = col != 2 ? 2 : 4;
                //AdsScopeCommandDef cmd = editorCommandsList.get(row);

                Boolean val = (Boolean) jtblCurrentEditorEnabledCmd.getValueAt(row, col);
                jtblCurrentEditorEnabledCmd.setValueAt(val, row, col2);
                jtblCurrentEditorEnabledCmd.setValueAt(val, row, 3);

                Restrictions res = role.getResourceRestrictions(EDrcResourceType.EDITOR_PRESENTATION,
                        currCoverEditorPresentation.epr.getOwnerClass().getId(), currCoverEditorPresentation.epr.getId(), currCoverEditorPresentation.epr);
                res.setCommandEnabled(editorCommandsList.get(row).getId(), val);

                AdsRoleDef.Resource resource = new AdsRoleDef.Resource(
                        EDrcResourceType.EDITOR_PRESENTATION,
                        currCoverEditorPresentation.epr.getOwnerClass().getId(),
                        currCoverEditorPresentation.epr.getId(),
                        res);
                role.CreateOrReplaceResourceRestrictions(resource);

            }
        });


    }

    private void initSelectorEnabledCmdTable() {
        cmdSelectorModel =
                new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Enabled Command", "Inherit", "Own", "Total", "Rights"
        }) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
//                boolean[] canEdit = new boolean [] {
//                    false, true, true, true
//                };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex != 2 && columnIndex != 4) {
                    return false;
                }

                if (lastSelectorPresentationId == null
                        || selectorCommandsList == null
                        || currCoverEntityObjectClasses == null) {
                    return false;
                }
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION,
                        currCoverEntityObjectClasses.clazz.getId(),
                        lastSelectorPresentationId);
                Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
                if (r == null) {
                    return false;
                }
                if (r.isDenied(ERestriction.ACCESS) || !r.isDenied(ERestriction.ANY_COMMAND)) {
                    return false;
                }

                Restrictions or = role.getOverwriteResourceRestrictions(hash, null);

                FooItem val = (FooItem) jtblCurrentSelectorEnabledCmd.getValueAt(rowIndex, 0);
                Boolean currVal = (Boolean) jtblCurrentSelectorEnabledCmd.getValueAt(rowIndex, 2);
                if (currVal != null && currVal) {
                    AdsScopeCommandDefCover cmd = (AdsScopeCommandDefCover) val.GetMyObject();
                    List<Id> lst = or.getEnabledCommandIds();
                    if (lst != null && lst.contains(cmd.getId())) {
                        return false;
                    }

                }
                return true;
            }
        };
        jtblCurrentSelectorEnabledCmd.setModel(cmdSelectorModel);
        BooleanRendererForMainColumns2 renderer = new BooleanRendererForMainColumns2();
        for (int i = 1; i < 5; i++) {
            TableColumn col = jtblCurrentSelectorEnabledCmd.getColumnModel().getColumn(i);
            col.setCellRenderer(renderer);
        }
        TableColumn column;
        column = jtblCurrentSelectorEnabledCmd.getColumnModel().getColumn(0);
        column.setCellRenderer(new TableCellBlueColorRenderer());
    }

    public static void collectExplorerItems(List<Id> collectLst, AdsExplorerItemDef explorerItem) {
        if (collectLst.contains(explorerItem.getId())) {
            return;
        }
        collectLst.add(explorerItem.getId());

        if (explorerItem instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) explorerItem;
            List<AdsExplorerItemDef> items = par.getExplorerItems().getChildren().get(EScope.ALL);
            for (AdsExplorerItemDef item : items) {
                collectExplorerItems(collectLst, item);
            }
        } else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) explorerItem;
            AdsParagraphExplorerItemDef par2 = def.findReferencedParagraph();
            if (par2 != null) {
                List<AdsExplorerItemDef> items = par2.getExplorerItems().getChildren().get(EScope.ALL);
                for (AdsExplorerItemDef item : items) {
                    collectExplorerItems(collectLst, item);
                }
            }
        }
    }

    private class Bool2 {

        boolean isBold;
        Color color;

        public Bool2() {
            isBold = false;
            color = Color.BLACK;
        }

        void SetBold(boolean isBold) {
            this.isBold = isBold;
        }

        void SetColor(Color color) {
            this.color = color;
        }

        boolean isBold() {
            return isBold;
        }

        boolean isBlack() {
            return GetColor().equals(Color.BLACK);
        }

        Color GetColor() {
            return color;
        }
    };

    private void inheritedAllItemsAtaParagraph(Id parentId) {
        List<String> hashesForRemovingItems = new ArrayList<>();
        Iterator<AdsRoleDef.Resource> iter = role.getResources().iterator();
        while (iter.hasNext()) {
            AdsRoleDef.Resource res = iter.next();
            if (res.type.equals(EDrcResourceType.EXPLORER_ROOT_ITEM)
                    && res.defId.equals(parentId)) {
                hashesForRemovingItems.add(AdsRoleDef.generateResHashKey(res));
            }

        }
        for (String hash : hashesForRemovingItems) {
            role.RemoveResourceRestrictions(hash);
        }

    }

    private boolean isParagraphContaintNotInheritedItem(AdsParagraphExplorerItemDef par) {
        if (par == null) {
            return true;
        }
        Iterator<AdsRoleDef.Resource> iter = role.getResources().iterator();
        while (iter.hasNext()) {
            AdsRoleDef.Resource res = iter.next();
            if (res.type.equals(EDrcResourceType.EXPLORER_ROOT_ITEM)
                    && res.defId.equals(par.getId())) {
                return true;
            }
        }
        return false;
    }

    private Bool2 isEntityDefContaintAllowedPresentation(AdsEntityObjectClassDef ecd) {

        Bool2 ret = new Bool2();
        if (ecd == null) {
            ret.SetColor(Color.RED);
            ret.SetBold(true);
            return ret;
        }
        List<AdsEditorPresentationDef> list = ecd.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
        removeOverwriteItems(list);
        boolean isFindBlack = false;
        boolean isFindBold = false;
        for (AdsEditorPresentationDef ep : list) {
            String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION, ecd.getId(), ep.getId());
            Restrictions rest = role.getOnlyCurrentResourceRestrictions(hash);

            if (rest != null) {
                isFindBlack = true;
                if (!rest.isDenied(ERestriction.ACCESS)) {
                    isFindBold = true;
                    break;
                }
            } else {
                rest = role.getOverwriteAndAncestordResourceRestrictions(hash, ep);
                if (!rest.isDenied(ERestriction.ACCESS)) {
                    isFindBold = true;
                    if (isFindBlack) {
                        break;
                    }
                }
            }
        }
        if (!isFindBold || !isFindBlack) {
            List<AdsSelectorPresentationDef> sList = ecd.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
            removeOverwriteItems(sList);
            for (AdsSelectorPresentationDef sp : sList) {
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION, ecd.getId(), sp.getId());
                Restrictions rest = role.getOnlyCurrentResourceRestrictions(hash);

                if (rest != null) {
                    isFindBlack = true;
                    if (!rest.isDenied(ERestriction.ACCESS)) {
                        isFindBold = true;
                        break;
                    }
                } else {
                    rest = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
                    if (!rest.isDenied(ERestriction.ACCESS)) {
                        isFindBold = true;
                        if (isFindBlack) {
                            break;
                        }
                    }
                }
            }

        }
        if (isFindBlack) {
            ret.SetColor(Color.BLACK);
        } else {
            ret.SetColor(greyColor);
        }
        ret.SetBold(isFindBold);
        return ret;
    }

    private boolean isEntityDefContaintAllowedPresentationInnate(AdsEntityObjectClassDef ecd) {
        if (ecd == null) {
            return true;//всяко удаленный класс
        }
        List<AdsSelectorPresentationDef> spList = ecd.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
        removeOverwriteItems(spList);
        for (AdsSelectorPresentationDef sp : spList) {
            String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION, ecd.getId(), sp.getId());
            Restrictions rest = role.getOnlyCurrentResourceRestrictions(hash);
            if (rest != null) {
                return true;
            } else if (isShowInheritClassesResources) {
                rest = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
                if (!rest.isDenied(ERestriction.ACCESS)) {
                    return true;
                }
            }
        }
        List<AdsEditorPresentationDef> epList = ecd.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
        removeOverwriteItems(epList);
        for (AdsEditorPresentationDef ep : epList) {
            String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION, ecd.getId(), ep.getId());
            Restrictions rest = role.getOnlyCurrentResourceRestrictions(hash);
            if (rest != null) {
                return true;
            } else if (isShowInheritClassesResources) {
                rest = role.getOverwriteAndAncestordResourceRestrictions(hash, ep);
                if (!rest.isDenied(ERestriction.ACCESS)) {
                    return true;
                }
            }
//            if (ep.getReplacedEditorPresentationId()!=null)
//            {
//                AdsRoleDef rr = null;
//                //AdsEditorPresentationDef ep.findReplacedEditorPresentation()
//
//            }
        }


        return false;
    }

    private void checkEntityDefRowBold() {

        if (jEntityAndApplTree.getSelectionPath() == null) {
            return;
        }
        Object currObject = jEntityAndApplTree.getSelectionPath().getLastPathComponent();


        DefaultMutableTreeNodeEx currNode = (DefaultMutableTreeNodeEx) currObject;
        currCoverEntityObjectClasses = (CoverEntityObjectClasses) currNode.obj;


        currNode.bool2 = isEntityDefContaintAllowedPresentation(currCoverEntityObjectClasses.clazz);
        jEntityAndApplTree.repaint();

    }

    private void initServerResourceTable() {
        jtblServerResource = new SimpleTable();

        jtblServerTableModel = new javax.swing.table.DefaultTableModel(
                new Object[][]{
            {"Connect to Explorer Access Service", null, null, null, null},
            {"Selector color schemes creation in Explorer", null, null, null, null},
            {"Filter creation and refining in Explorer", null, null, null, null},
            {"Access to server file resources", null, null, null, null},
            {"Sorting creation and refining in Explorer", null, null, null, null},
            {"Debug", null, null, null, null},
            {"Trace", null, null, null, null},
            {"View audit", null, null, null, null},
            {"User functions development", null, null, null, null}
        },
                new String[]{
            "Server Resource", "Inherited Rights", "Own Rights", "Total Rights", "Rights"
        }) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Object.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                try{
                    if (columnIndex == 2) {
                        if (!isSuperAdmin && !roleIsReadOnly) {
                            int y = rowIndex;

                            if (serverResourcesCanForbid.get(y)) {
                                if (comboBoxEx.getItemCount() == 3) {
                                    comboBoxEx.removeItemAt(2);
                                }
                            } else {
                                if (comboBoxEx.getItemCount() == 2) {
                                    comboBoxEx.addItem(Forbidden);
                                }
                            }
                            return true;
                        }
                    }
                    if (columnIndex == 4) {
                        return (!isSuperAdmin && !roleIsReadOnly);
                    }
                    return false;
                }
                catch (Throwable th){
                    return false;
                }
            }
        };
        jtblServerResource.setModel(jtblServerTableModel);
        jScrollPaneServerResource.setViewportView(jtblServerResource);


        TableColumnModel columnModel = jtblServerResource.getColumnModel();
        TableColumn column = columnModel.getColumn(2);
        column.setCellEditor(new DefaultCellEditor(comboBoxEx));

        column = columnModel.getColumn(4);
        column.setCellEditor(new DefaultCellEditor(checkBoxEx));
        column.setCellRenderer(new BooleanRendererForMainColumns());


        jtblServerResource.setDefaultRenderer(Object.class, whiteTableCellRendererWithBold);
        jtblServerResource.setRowSelectionInterval(0, 0);

        for (int i = 0; i < 4; i++) {
            if (i == 2) {
            } else {
                column = columnModel.getColumn(i);
                column.setCellRenderer(grayTableCellRendererWithBold);
            }
        }




        jtblServerResource.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (canIgnoreChangeEvent(evt)) {
                    return;
                }

                if (role == null || !role.isInBranch()) {
                    return;
                }
                int index = jtblServerResource.getSelectedRow();
                if (index > -1) {

                    EDrcServerResource resource = indexToServerResource(index);
                    if (jtblServerResource.getSelectedColumn() == 2) {



                        String value = (String) jtblServerTableModel.getValueAt(index, 2);
                        if (value.equals(Inherit)) {
                            role.RemoveResourceRestrictions(AdsRoleDef.generateResHashKey(EDrcResourceType.SERVER_RESOURCE, resource));
                            jtblServerResource.setValueAt(jtblServerResource.getValueAt(index, 1), index, 3);
                        } else {
                            jtblServerResource.setValueAt(value, index, 3);
                            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                    EDrcResourceType.SERVER_RESOURCE,
                                    Id.Factory.loadFrom(resource.getValue()),
                                    null,
                                    Restrictions.Factory.newInstance(role, value.equals(Allowed) ? 0 : ERestriction.ACCESS.getValue().longValue())));
                        }
                        jtblServerResource.setValueAt(jtblServerResource.getValueAt(index, 0), index, 0);

                    } else if (jtblServerResource.getSelectedColumn() == 4) {
                        Boolean value = (Boolean) jtblServerTableModel.getValueAt(index, 4);
                        if (value) {
                            jtblServerResource.setValueAt(Allowed, index, 2);
                            jtblServerResource.setValueAt(Allowed, index, 3);
                            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                    EDrcResourceType.SERVER_RESOURCE,
                                    Id.Factory.loadFrom(resource.getValue()),
                                    null,
                                    Restrictions.Factory.newInstance(role, 0)));
                        } else {
                            jtblServerResource.setValueAt(Inherit, index, 2);
                            jtblServerResource.setValueAt(jtblServerResource.getValueAt(index, 1), index, 3);
                            role.RemoveResourceRestrictions(AdsRoleDef.generateResHashKey(EDrcResourceType.SERVER_RESOURCE, resource));
                        }
                        jtblServerResource.repaint();
                    }
                }
            }
        });

        jtblServerResource.getTableHeader().setReorderingAllowed(false);

    }

    private void initExplorerRootsTable() {


        jtblExplorerRootsModel = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Explorer Root", "Inherited Rights", "Own Rights", "Total Rights", "Rights"
        }) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Object.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 2) {
                    if (!isSuperAdmin && !roleIsReadOnly) {
                        isUpdated++;
                        int y = rowIndex;
                        if (paragraphs.get(y).isCanForbid()) {
                            if (comboBoxEx.getItemCount() == 2) {
                                comboBoxEx.addItem(Forbidden);
                            }
                        } else {
                            if (comboBoxEx.getItemCount() == 3) {
                                comboBoxEx.removeItemAt(2);
                            }
                        }
                        isUpdated--;
                        return true;
                    }
                }
                if (columnIndex == 4) {
                    return (!isSuperAdmin && !roleIsReadOnly);
                }
                return false;
            }
        };

        jtblExplorerRoots = new SimpleTable();

        jtblExplorerRoots.setModel(jtblExplorerRootsModel);
        jScrollPaneExplorerRoots.setViewportView(jtblExplorerRoots);

        jtblExplorerRoots.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fillExplorerItemsTree();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fillExplorerItemsTree();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fillExplorerItemsTree();
            }
        });
        jtblExplorerRoots.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fillExplorerItemsTree();
            }
        });

        jtblExplorerRoots.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (canIgnoreChangeEvent(evt)) {
                    return;
                }
                int index = jtblExplorerRoots.getSelectedRow();
                if (index > -1) {
                    if (jtblExplorerRoots.getSelectedColumn() == 2) {
                        String value = (String) jtblExplorerRootsModel.getValueAt(index, 2);

                        if (value.equals(Inherit)) {
                            role.RemoveResourceRestrictions(AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, paragraphs.get(index).getId(), null));
                            String inheritValue = (String) jtblExplorerRootsModel.getValueAt(index, 1);

                            jtblExplorerRoots.setValueAt(inheritValue, index, 3);
                        } else {
                            jtblExplorerRoots.setValueAt(value, index, 3);
                            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                                    paragraphs.get(index).getId(),
                                    null,
                                    Restrictions.Factory.newInstance(role, value.equals(Allowed) ? 0 : ERestriction.ACCESS.getValue().longValue())));

                        }
                        if (index == jtblExplorerRootsModel.getRowCount()) {
                            index--;
                        }
                        if (index >= 0) {
                            jtblExplorerRoots.setRowSelectionInterval(index, index);
                        }
                    }
                    if (jtblExplorerRoots.getSelectedColumn() == 4) {
                        Boolean value = (Boolean) jtblExplorerRoots.getValueAt(index, 4);
                        if (value) {
                            jtblExplorerRoots.setValueAt(Allowed, index, 2);
                            jtblExplorerRoots.setValueAt(Allowed, index, 3);
                            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                                    paragraphs.get(index).getId(),
                                    null,
                                    Restrictions.Factory.newInstance(role, 0)));
                        } else {
                            jtblExplorerRoots.setValueAt(Inherit, index, 2);
                            jtblExplorerRoots.setValueAt(Inherit, index, 3);
                            jtblExplorerRoots.setValueAt(jtblExplorerRoots.getValueAt(index, 1), index, 3);

                            role.RemoveResourceRestrictions(
                                    AdsRoleDef.generateResHashKey(
                                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                                    paragraphs.get(index).getId(),
                                    null));
                            //        role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                            //                EDrcResourceType.EXPLORER_ROOT_ITEM,
                            //                paragraphs.get(index).getParagraphItem().getId(),
                            //                null,
                            //                Restrictions.Factory.newInstance(role, ERestriction.ACCESS.getValue().longValue())));
                        }
                        jtblExplorerRoots.repaint();
                    }
                    fillExplorerItemsTree();
                }

            }
        });

        TableColumnModel columnModel = jtblExplorerRoots.getColumnModel();
        TableColumn column = columnModel.getColumn(2);
        column.setCellEditor(new DefaultCellEditor(comboBoxEx));

        column = columnModel.getColumn(4);
        column.setCellEditor(new DefaultCellEditor(checkBoxEx));
        column.setCellRenderer(new BooleanRendererForMainColumns());

        jtblExplorerRoots.setDefaultRenderer(Object.class, whiteTableCellRendererWithBold);


        for (int i = 0; i < 4; i++) {

            if (i == 2) {
                continue;
            }
            column = columnModel.getColumn(i);
            column.setCellRenderer(grayTableCellRendererWithBold);
        }

        treeTableExplorerItemsModel = new TreeGridModel(null, cNames, cTypes);
        treeTableExplorerItems = new TreeTableExWithMouseMotionListener(treeTableExplorerItemsModel);
        jScrollPaneExplorerItems.setViewportView(treeTableExplorerItems);


        treeTableExplorerItems.getTableHeader().setReorderingAllowed(false);
        jtblExplorerRoots.getTableHeader().setReorderingAllowed(false);


    }
    private DefaultCellEditor treeTableCellEditorMain = null;

    private void initPresentationExplorerItemTable() {

        treeTablePresExplorerItemsModel = new TreeGridModel(null, cNamesPres, cTypesBool);
        treeTablePresExplorerItems = new TreeTable(treeTablePresExplorerItemsModel);
        jScrollPane3.setViewportView(treeTablePresExplorerItems);
        treeTablePresExplorerItems.setRootVisible(false);

        treeTablePresExplorerItems.getTableHeader().setReorderingAllowed(false);


        treeTablePresEditorPageModel = new TreeGridModel(null, cNamesPres, cTypesBool);
        treeTablePresEditorPage = new TreeTable(treeTablePresEditorPageModel);
        jScrollPane4.setViewportView(treeTablePresEditorPage);
        treeTablePresEditorPage.setRootVisible(false);

        treeTablePresEditorPage.getTableHeader().setReorderingAllowed(false);


        treeTableEditorPresentations.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int row = treeTableEditorPresentations.getSelectedRow();
                if (row == -1) {
                    return;
                }
                TreeGridModel.TreeGridNode tGreed = (TreeGridModel.TreeGridNode) treeTableEditorPresentations.getValueAt(row, 0);
                TreeGridRoleResourceRowForEditorPresentation rowItem = (TreeGridRoleResourceRowForEditorPresentation) tGreed.getGridItem();
                currCoverEditorPresentation = rowItem.cover;
                fillEditorPresentationRightsAndCommand();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int row = treeTableEditorPresentations.getSelectedRow();
                if (row == -1) {
                    return;
                }
                TreeGridModel.TreeGridNode tGreed = (TreeGridModel.TreeGridNode) treeTableEditorPresentations.getValueAt(row, 0);
                TreeGridRoleResourceRowForEditorPresentation rowItem = (TreeGridRoleResourceRowForEditorPresentation) tGreed.getGridItem();
                currCoverEditorPresentation = rowItem.cover;
                fillEditorPresentationRightsAndCommand();
            }
        });

    }

    private void fillPresentationEnabledCommandTable(Restrictions r, Restrictions ar, Restrictions or) {

        int i = 0;
        if (r == null
                || isSuperAdmin
                || roleIsReadOnly
                || r.isDenied(ERestriction.ACCESS)
                || !r.isDenied(ERestriction.ANY_COMMAND)) {
            jbtSelectAllCmdForEdPr.setEnabled(false);
            jbtDeselectAllCmdForEdPr.setEnabled(false);
        } else {
            jbtSelectAllCmdForEdPr.setEnabled(true);
            jbtDeselectAllCmdForEdPr.setEnabled(true);
        }


        if (currCoverEditorPresentation == null
                || currCoverEditorPresentation.epr == null) {
            currEditorModel.setRowCount(0);
            return;
        }

        List<AdsScopeCommandDef> cmList = currCoverEditorPresentation.getEditorPresentation().
                getOwnerClass().
                getPresentations().
                getCommands().
                get(EScope.ALL);

        editorCommandsList = new ArrayList<AdsScopeCommandDefCover>(cmList.size());
        for (AdsScopeCommandDef cmd : cmList) {
            AdsScopeCommandDefCover cover = new AdsScopeCommandDefCover();
            cover.set(cmd);
            editorCommandsList.add(cover);
        }

        //LOCAL_AND_OVERWRITE
        removeOverwriteItems(editorCommandsList);
        List<Id> idList = r != null ? r.getEnabledCommandIds() : null;
        if (idList == null) {
            idList = new ArrayList<Id>();
        }

        //Добавляем удаленные ресурсы
        for (Id id : idList) {
            boolean isFind = false;
            for (AdsScopeCommandDefCover cmd : editorCommandsList) {
                if (cmd.getId().equals(id)) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                AdsScopeCommandDefCover cover = new AdsScopeCommandDefCover();
                cover.set(id);
                editorCommandsList.add(cover);
            }
        }



        RadixObjectsUtils.sortByName(editorCommandsList);

        currEditorModel.setRowCount(editorCommandsList.size());


        //editorCommandsList.contains();

        boolean isNotDefinedAncestors = ar.isDenied(ERestriction.ACCESS);
        boolean isSetAncesstors = !isNotDefinedAncestors && !ar.isDenied(ERestriction.ANY_COMMAND);
        List<Id> ancestorIdList = ar.getEnabledCommandIds();
        if (ancestorIdList == null) {
            ancestorIdList = new ArrayList<>();
        }

        // && !r.isDenied(ERestriction.ANY_COMMAND)

        boolean isNotDefined = r != null && r.isDenied(ERestriction.ACCESS);
        boolean isSet = r != null && !isNotDefined && !r.isDenied(ERestriction.ANY_COMMAND);




//            if (!r.isDenied(ERestriction.ACCESS) && !r.isDenied(ERestriction.ANY_COMMAND))
        {

            for (AdsScopeCommandDefCover cmd : editorCommandsList) {
                jtblCurrentEditorEnabledCmd.setValueAt(new FooItem(cmd.getName(), cmd), i, 0);
                if (isNotDefinedAncestors) {
                    jtblCurrentEditorEnabledCmd.setValueAt(false, i, 1);
                } else if (isSetAncesstors) {
                    jtblCurrentEditorEnabledCmd.setValueAt(true, i, 1);
                } else {
                    jtblCurrentEditorEnabledCmd.setValueAt(ancestorIdList.contains(cmd.getId()), i, 1);
                }

                if (isNotDefined) {
                    jtblCurrentEditorEnabledCmd.setValueAt(false, i, 2);
                    jtblCurrentEditorEnabledCmd.setValueAt(false, i, 4);
                } else if (isSet) {
                    jtblCurrentEditorEnabledCmd.setValueAt(true, i, 2);
                    jtblCurrentEditorEnabledCmd.setValueAt(true, i, 4);
                } else {
                    Boolean flag = idList.contains(cmd.getId());
                    jtblCurrentEditorEnabledCmd.setValueAt(flag, i, 2);
                    jtblCurrentEditorEnabledCmd.setValueAt(flag, i, 4);
                }

                jtblCurrentEditorEnabledCmd.setValueAt(
                        r == null
                        ? jtblCurrentEditorEnabledCmd.getValueAt(i, 1)
                        : jtblCurrentEditorEnabledCmd.getValueAt(i, 2),
                        i,
                        3);
                mayRemoveEditorEnabledCommands.add(false);
                i++;

            }
        }

    }

    private void fillPresentationExplorerItemsTree() {


        if (currCoverEditorPresentation == null
                || currCoverEditorPresentation.epr == null) {
            treeTablePresExplorerItemsModel.openRoot(null);
            treeTablePresExplorerItems.afterOpen(treeTablePresExplorerItemsModel, Color.lightGray);

            TreeGridRoleResourceEdPresentationExplorerItemRow root = new TreeGridRoleResourceEdPresentationExplorerItemRow(
                    null, treeTablePresExplorerItems, "", role, null, (AdsEditorPresentationDef) null);
            treeTablePresExplorerItemsModel.openRoot(root);
            treeTablePresExplorerItems.afterOpen(treeTablePresExplorerItemsModel, backgroundColor);



            treeTablePresEditorPageModel.openRoot(null);
            treeTablePresEditorPage.afterOpen(treeTablePresEditorPageModel, Color.lightGray);

            TreeGridRoleResourceEdPresentationPageRow root2 = new TreeGridRoleResourceEdPresentationPageRow(
                    null, treeTablePresEditorPage, "", role, null, (AdsEditorPresentationDef) null);
            treeTablePresEditorPageModel.openRoot(root2);
            treeTablePresEditorPage.afterOpen(treeTablePresEditorPageModel, backgroundColor);




            return;
        }
        treeTablePresExplorerItems.setRootVisible(false);
        treeTablePresEditorPage.setRootVisible(false);


        List<AdsExplorerItemDef> list =
                currCoverEditorPresentation.getEditorPresentation().
                getExplorerItems().
                getChildren().
                get(EScope.ALL);
        removeOverwriteItems(list);
        //RadixObjectsUtils.sortByName(list);

        List<AdsEditorPageDef> list2 = new ArrayList<>();
        EditorPages.PageOrder pageOrder = currCoverEditorPresentation.getEditorPresentation().getEditorPages().getOrder();
        for (OrderedPage orderPage : pageOrder) {
            AdsEditorPageDef pItem = orderPage.findPage();
            if (pItem != null) {
                list2.add(pItem);
            }
        }



        String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.epr.getOwnerClass().getId(),
                currCoverEditorPresentation.epr.getId());


        TreeGridRoleResourceEdPresentationExplorerItemRow root = new TreeGridRoleResourceEdPresentationExplorerItemRow(
                null, treeTablePresExplorerItems, hash, role, null, currCoverEditorPresentation.epr);


        TreeGridRoleResourceEdPresentationPageRow root2 = new TreeGridRoleResourceEdPresentationPageRow(
                null, treeTablePresEditorPage, hash, role, null, currCoverEditorPresentation.epr);


        root.list = new ArrayList<>();
        root2.list = new ArrayList<>();

        root.isMayChild = !list.isEmpty();
        root2.isMayChild = !list2.isEmpty();


        for (AdsExplorerItemDef eItem : list) {
            TreeGridRoleResourceEdPresentationExplorerItemRow item = new TreeGridRoleResourceEdPresentationExplorerItemRow(
                    null, treeTablePresExplorerItems, hash, role, eItem, currCoverEditorPresentation.epr);
            root.list.add(item);
        }

        for (AdsEditorPageDef pItem : list2) {
            TreeGridRoleResourceEdPresentationPageRow item = new TreeGridRoleResourceEdPresentationPageRow(
                    null, treeTablePresEditorPage, hash, role, pItem, currCoverEditorPresentation.epr);
            root2.list.add(item);
        }


        Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
        {
            List<Id> ids = r != null ? r.getEnabledChildIds() : null;
            if (ids != null && !ids.isEmpty()) {
                List<AdsExplorerItemDef> lst = AdsRoleDef.collectPresentationExplorerItems(currCoverEditorPresentation.epr);
                for (Id id : ids) {
                    boolean isFind = false;
                    for (AdsExplorerItemDef ei : lst) {
                        if (ei.getId().equals(id)) {
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        TreeGridRoleResourceEdPresentationExplorerItemRow item = new TreeGridRoleResourceEdPresentationExplorerItemRow(
                                null, treeTablePresExplorerItems, hash, role, currCoverEditorPresentation.epr, id);
                        root.list.add(item);
                    }
                }
            }
        }
        {
            List<Id> ids = r != null ? r.getEnabledPageIds() : null;
            if (ids != null && !ids.isEmpty()) {
                List<AdsEditorPageDef> lst = currCoverEditorPresentation.epr.getEditorPages().get(EScope.ALL);
                for (Id id : ids) {
                    boolean isFind = false;
                    for (AdsEditorPageDef pg : lst) {
                        if (pg.getId().equals(id)) {
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        TreeGridRoleResourceEdPresentationPageRow item = new TreeGridRoleResourceEdPresentationPageRow(
                                null, treeTablePresExplorerItems, hash, role, currCoverEditorPresentation.epr, id);
                        root2.list.add(item);
                    }
                }
            }

        }

        //
        //List<AdsExplorerItemDef> lst

        treeTablePresExplorerItemsModel.openRoot(root);
        treeTablePresExplorerItems.afterOpen(treeTablePresExplorerItemsModel, backgroundColor);


        treeTablePresEditorPageModel.openRoot(root2);
        treeTablePresEditorPage.afterOpen(treeTablePresEditorPageModel, backgroundColor);


        BooleanRenderer2 boolRenderer = new BooleanRenderer2(ERestriction.ANY_CHILD);
        for (int i = 1; i < 5; i++) {
            treeTablePresExplorerItems.getColumnModel().getColumn(i).setCellRenderer(boolRenderer);
        }
        treeTablePresExplorerItems.expandAll();

        BooleanRenderer2 boolRenderer2 = new BooleanRenderer2(ERestriction.ANY_PAGES);
        for (int i = 1; i < 5; i++) {
            treeTablePresEditorPage.getColumnModel().getColumn(i).setCellRenderer(boolRenderer2);
        }
        treeTablePresEditorPage.expandAll();
    }

    private class CommonMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent me) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            JComponent c = (JComponent) e.getComponent();
            {
                if (c == jtblSelectorPresentation) {
                    String s = null;
                    int row = e.getY() / jtblSelectorPresentation.getRowHeight();
                    if (row >= 0 && row < entitySelectorPresentations.size()) {
                        AdsSelectorPresentationDef pres = entitySelectorPresentations.get(row).sp;
                        if (pres != null) {


                            AdsMultilingualStringDef msd = pres.findTitleStorage();
                            String title = msd == null ? null : msd.getValue(EIsoLanguage.ENGLISH);
                            if (title == null || title.isEmpty()) {
                                s = pres.getQualifiedName();
                            } else {
                                s = title + " (" + pres.getQualifiedName() + ")";
                            }
                        }
                    }
                    c.setToolTipText(s);
                } else if (c == jtblCurrentSelectorEnabledCmd) {
                    String s = null;
                    int row = e.getY() / jtblCurrentSelectorEnabledCmd.getRowHeight();
                    if (row >= 0 && row < selectorCommandsList.size()) {
                        AdsScopeCommandDef cmd = selectorCommandsList.get(row).cmd;
                        if (cmd != null) {
                            String title = cmd.getTitle(EIsoLanguage.ENGLISH);
                            if (title == null || title.isEmpty()) {
                                s = cmd.getQualifiedName();
                            } else {
                                s = title + " (" + cmd.getQualifiedName() + ")";
                            }
                        }
                    }
                    c.setToolTipText(s);
                } else if (jtblCurrentEditorEnabledCmd == c) {
                    String s = null;
                    int row = e.getY() / jtblCurrentEditorEnabledCmd.getRowHeight();
                    if (row >= 0 && row < editorCommandsList.size()) {
                        AdsScopeCommandDef cmd = editorCommandsList.get(row).cmd;
                        if (cmd != null) {
                            String title = cmd.getTitle(EIsoLanguage.ENGLISH);
                            if (title == null || title.isEmpty()) {
                                s = cmd.getQualifiedName();
                            } else {
                                s = title + " (" + cmd.getQualifiedName() + ")";
                            }
                        }
                    }
                    c.setToolTipText(s);
                } else if (treeTablePresExplorerItems == c) {
                    String s = null;
                    int row = treeTablePresExplorerItems.getRowForLocation(e.getY());
                    if (row >= 0 && row < treeTablePresExplorerItems.getRowCount()) {
                        TreeGridModel.TreeGridNode item = (TreeGridModel.TreeGridNode) treeTablePresExplorerItems.getValueAt(row, 0);
                        TreeGridRoleResourceEdPresentationExplorerItemRow curr = (TreeGridRoleResourceEdPresentationExplorerItemRow) item.getGridItem();

                        String title = curr.getExplorerItemTitle();
                        if (title == null || title.isEmpty()) {
                            s = curr.explorerItem == null ? null : curr.explorerItem.getQualifiedName();
                        } else {
                            s = title + " (" + (curr.explorerItem == null ? null : curr.explorerItem.getQualifiedName()) + ")";
                        }

                    }
                    c.setToolTipText(s);
                } else if (treeTablePresEditorPage == c) {
                    String s = null;
                    int row = treeTablePresEditorPage.getRowForLocation(e.getY());
                    if (row >= 0 && row < treeTablePresEditorPage.getRowCount()) {
                        TreeGridModel.TreeGridNode item = (TreeGridModel.TreeGridNode) treeTablePresEditorPage.getValueAt(row, 0);
                        TreeGridRoleResourceEdPresentationPageRow curr =
                                (TreeGridRoleResourceEdPresentationPageRow) item.getGridItem();

                        String title = curr.getTitle();
                        if (title == null || title.isEmpty()) {
                            s = curr.page == null ? null : curr.page.getQualifiedName();
                        } else {
                            s = title + " (" + (curr.page == null ? null : curr.page.getQualifiedName()) + ")";
                        }

                    }
                    c.setToolTipText(s);
                }
            }
        }
    }

    private class TreeTableExWithMouseMotionListener extends TreeTable implements MouseMotionListener {

        @Override
        public void mouseMoved(MouseEvent e) {
            JComponent c = (JComponent) e.getComponent();
            {
                if (treeTableEditorPresentations == c) {
                    int row = treeTableEditorPresentations.getRowForLocation(e.getY());
                    if (row == -1) {
                        c.setToolTipText("");
                        return;
                    }
                    TreeGridModel.TreeGridNode item = (TreeGridModel.TreeGridNode) treeTableEditorPresentations.getValueAt(row, 0);
                    TreeGridRoleResourceRowForEditorPresentation curr = (TreeGridRoleResourceRowForEditorPresentation) item.getGridItem();
                    if (curr.cover.epr == null) {
                        c.setToolTipText("Incorrect resource");
                    } else {
                        AdsMultilingualStringDef msd = curr.cover.epr.findTitleStorage();
                        String title = msd == null ? null : msd.getValue(EIsoLanguage.ENGLISH);
                        if (title == null || title.isEmpty()) {
                            c.setToolTipText(curr.cover.epr.getQualifiedName());
                        } else {
                            c.setToolTipText(title + " (" + curr.cover.epr.getQualifiedName() + ")");
                        }

                    }
                } else if (treeTableExplorerItems == c) {
                    int row = treeTableExplorerItems.getRowForLocation(e.getY());
                    if (row == -1) {
                        c.setToolTipText("");
                        return;
                    }
                    TreeGridModel.TreeGridNode item = (TreeGridModel.TreeGridNode) treeTableExplorerItems.getValueAt(row, 0);
                    TreeGridRoleResourceRow gridItem = (TreeGridRoleResourceRow) item.getGridItem();


                    c.setToolTipText(gridItem.getExplorerItemTitle()
                            + " (" + (gridItem.explorerItem == null ? "" : gridItem.explorerItem.getQualifiedName() + ")"));



                }
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        TreeTableExWithMouseMotionListener(TreeGridModel treeTableModel) {
            super(treeTableModel);
        }
//        Object getCurrentObject()
//        {
//            if (tree.getSelectionPath()!=null)
//                return null;
//            return tree.getSelectionPath().getLastPathComponent();
//        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == 2) {
                return treeTableCellEditorMain;
            }
            return super.getCellEditor(row, column);
        }
    }
    private TreeGridModel treeTableExplorerItemsModel;
    private TreeTableExWithMouseMotionListener treeTableExplorerItems;
    private TreeGridModel treeTablePresExplorerItemsModel;
    private TreeTable treeTablePresExplorerItems;
    private TreeGridModel treeTablePresEditorPageModel;
    private TreeTable treeTablePresEditorPage;

    EDrcServerResource indexToServerResource(int i) {
        EDrcServerResource serverResource;
        switch (i) {

            case 0:
                serverResource = EDrcServerResource.EAS;
                break;
            case 1:
                serverResource = EDrcServerResource.EAS_COLORING_CREATION;
                break;
            case 2:
                serverResource = EDrcServerResource.EAS_FILTER_CREATION;
                break;
            case 3:
                serverResource = EDrcServerResource.EAS_SERVER_FILES;
                break;
            case 4:
                serverResource = EDrcServerResource.EAS_SORTING_CREATION;
                break;
            case 5:
                serverResource = EDrcServerResource.DEBUG;
                break;
            case 6:
                serverResource = EDrcServerResource.TRACING;
                break;
            case 7:
                serverResource = EDrcServerResource.VIEW_AUDIT;
                break;
            /*
             * case 8: serverResource = EDrcServerResource.USER_FUNC_DEV; break;
             */
            default:
                serverResource = EDrcServerResource.USER_FUNC_DEV;
                break;
        }
        return serverResource;
    }

    class MyListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

//            if (value instanceof FooItem) {
//                FooItem foo = (FooItem) value;
//                if (foo.GetMyObject() instanceof Id) {
//                    c.setForeground(Color.RED);
//                }
//            }
            return c;
        }
    }

    private void initAncestorAndAPFamilyLists() {

        jAncestorList.setModel(new DefaultListModel());
        jAPFamilyList.setModel(new DefaultListModel());

        MyListCellRenderer cellRenderer = new MyListCellRenderer();

        jAncestorList.setCellRenderer(cellRenderer);
        jAPFamilyList.setCellRenderer(cellRenderer);


    }

    private void refreshAncestorsButtons() {
        DefaultListModel model = (DefaultListModel) jAncestorList.getModel();
        jbtAddAncestor.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtDelAncestor.setEnabled(!isSuperAdmin && !roleIsReadOnly && !(model.getSize() == 0 || jAncestorList.getSelectedIndex() < overwriteAncestors.size()));
        jbtGoToAncestorRole.setEnabled(model.getSize() != 0);
        jbtClearAncestors.setEnabled(!isSuperAdmin && !roleIsReadOnly && model.getSize() > overwriteAncestors.size());
    }

    private void refreshAPFButtons() {
        DefaultListModel model = (DefaultListModel) jAPFamilyList.getModel();
        int y = jAPFamilyList.getSelectedIndex();


        // jbtAddAPFamily.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtDelAPFamily.setEnabled(
                !isSuperAdmin
                && !roleIsReadOnly
                && !(model.getSize() == 0 || y < 0
                || y < overwriteAPF.size()));
        jbtClearAPFamily.setEnabled(!isSuperAdmin && !roleIsReadOnly && model.getSize() > overwriteAPF.size());
        jbtAddAutoAPFamily.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtGoToAPFamily.setEnabled(jAPFamilyList.getSelectedIndex() >= 0);
    }

    private void fillAncestorsList() {
        DefaultListModel model = (DefaultListModel) jAncestorList.getModel();
        adsSearcher = AdsSearcher.Factory.newAdsDefinitionSearcher(role.getModule());
        model.clear();
        if (this.isSuperAdmin) {
            return;
        }
        overwriteAncestors.clear();
        AdsRoleDef r = role;
        while (true) {
            AdsRoleDef roleOverwrite = null;
            if (r.isOverwrite()) {
                roleOverwrite = (AdsRoleDef) r.getHierarchy().findOverwritten().get();
            }
            if (roleOverwrite == null) {
                break;
            }
            r = roleOverwrite;
            overwriteRoles.add(r);
            for (Id id : r.getAncestorIds()) {
                AdsRoleDef ar = (AdsRoleDef) adsSearcher.findById(id).get();
                if (ar != null) {
                    overwriteAncestors.add(ar);
                }
            }
        }

        for (AdsRoleDef r2 : overwriteAncestors) {
            model.add(model.size(), new FooItem(r2.getQualifiedName(), r2.getId()));
        }


        for (Id id : role.getAncestorIds()) {
            adsSearcher = AdsSearcher.Factory.newAdsDefinitionSearcher(role.getModule());
            AdsRoleDef ancestor = (AdsRoleDef) adsSearcher.findById(id).get();

            if (ancestor == null) {
                model.add(model.size(), new FooItem(id.toString(), id));
            } else {
                model.add(model.size(), new FooItem(ancestor.getQualifiedName(), ancestor));
            }
        }
        if (model.getSize() > 0) {
            jAncestorList.setSelectedIndex(0);
        }
        refreshAncestorsButtons();
    }

    private void fillAPFList() {
        DefaultListModel model = (DefaultListModel) jAPFamilyList.getModel();
        model.clear();
        overwriteAPF.clear();

        if (this.isSuperAdmin) {
            for (DdsModule m : ddsModules) {
                DdsModelDef nextModel = m.getModelManager().findModel();
                if (nextModel != null) {
                    for (DdsAccessPartitionFamilyDef item : nextModel.getAccessPartitionFamilies()) {
                        model.add(model.size(), new FooItem(item.getQualifiedName(), item));
                    }
                }
            }
            if (model.getSize() > 0) {
                jAPFamilyList.setSelectedIndex(0);
            }
            refreshAPFButtons();
            return;
        }

        AdsRoleDef r = role;
        while (true) {
            AdsRoleDef roleOverwrite = null;
            if (r.isOverwrite()) {
                roleOverwrite = (AdsRoleDef) r.getHierarchy().findOverwritten().get();
            }
            if (roleOverwrite == null) {
                break;
            }
            r = roleOverwrite;

            for (Id id : r.getAPFamilieIds()) {
                DdsAccessPartitionFamilyDef apf = null;
                lbl:
                for (DdsModule m : ddsModules) {
                    DdsModelDef nextModel = m.getModelManager().findModel();
                    if (nextModel != null) {
                        for (DdsAccessPartitionFamilyDef item : nextModel.getAccessPartitionFamilies()) {
                            if (item.getId().equals(id)) {
                                apf = item;
                                break lbl;
                            }
                        }
                    }
                }
                if (apf != null) {
                    if (!role.getAPFamilieIds().contains(apf.getId())) {
                        overwriteAPF.add(apf);
                    }
                }
            }
        }

        for (DdsAccessPartitionFamilyDef apf : overwriteAPF) {
            model.add(model.size(), new FooItem(apf.getQualifiedName(), apf));
        }

        for (Id id : role.getAPFamilieIds()) {
            DdsAccessPartitionFamilyDef apf = null;
            lbl:
            for (DdsModule m : ddsModules) {
                DdsModelDef nextModel = m.getModelManager().findModel();
                if (nextModel != null) {
                    for (DdsAccessPartitionFamilyDef item : nextModel.getAccessPartitionFamilies()) {
                        if (item.getId().equals(id)) {
                            apf = item;
                            break lbl;
                        }
                    }
                }
            }
            if (apf == null) {
                model.add(model.size(), new FooItem(id.toString(), id));
            } else {
                //getHead().
                model.add(model.size(), new FooItem(apf.getQualifiedName(), apf));
            }

        }
        if (model.getSize() > 0) {
            jAPFamilyList.setSelectedIndex(0);
        }
        refreshAPFButtons();
    }

    private void fillExplorerItem() {
        jbtDelExplorerRoot.setEnabled(jtblExplorerRoots.getRowCount() > 0 && !isSuperAdmin && !roleIsReadOnly);
        jbtGoToExplorerRoot.setEnabled(jtblExplorerRoots.getRowCount() > 0);
        jbtAddExplorerRoot.setEnabled(!isSuperAdmin && !roleIsReadOnly && !paragraphsMap.isEmpty());

        jbtExplRootAllowedAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtExplRootInheritAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);

        jbtExplItemAllowedTree.setEnabled(!isSuperAdmin && !roleIsReadOnly && treeTableExplorerItems.getRowCount() > 0);
        jbtExplItemInheritTree.setEnabled(!isSuperAdmin && !roleIsReadOnly && treeTableExplorerItems.getRowCount() > 0);



    }

    public static <T extends Definition> void removeOverwriteItems(List<T> items) {
        //do nothing
//        int n = items.size();
//        for (int i = n - 1; i >= 0; i--) {
//            Definition item = items.get(i);
//            boolean isFind = false;
//            int j = 0;
//            Definition item2 = null;
//            for (int k = 0; k < i; k++) {
//                Definition item3 = items.get(k);
//                if (item3.getId().equals(item.getId())) {
//                    j = k;
//                    item2 = item3;
//                    isFind = true;
//                    break;
//                }
//            }
//            if (isFind) {
//                if (item.getModule().getSegment().getLayer().isHigherThan(
//                        item2.getModule().getSegment().getLayer())) {
//                    //Definition t = item2;
//                    items.set(j, (T) item);
//                }
//                items.remove(i);
//            }
//        }
    }

    private void fillEditorPresentationColumnTablesWidth() {
        jlEditorPresTitle.setText("Current editor presentation - \'" + (currCoverEditorPresentation == null || currCoverEditorPresentation.epr == null ? "none" : currCoverEditorPresentation.epr.getQualifiedName()) + "\'");
        boolean isMayInherit = false;
        if (currCoverEditorPresentation != null
                && currCoverEditorPresentation.epr != null) {
            isMayInherit = !role.getAncestorIds().isEmpty()
                    || role.isOverwrite()
                    || currCoverEditorPresentation.getEditorPresentation().getOwnerClass() instanceof AdsApplicationClassDef
                    || getPresentationParent(currCoverEditorPresentation.epr) != null;
        }
        if (isMayInherit) {
            for (int i = 0; i < 2; i++) {
                for (int r = 1; r < 4; r++) {
                    jtblCurrentEditorRights.getColumnModel().getColumn(r).setMaxWidth(70);
                    jtblCurrentEditorRights.getColumnModel().getColumn(r).setMinWidth(70);
                    jtblCurrentEditorRights.getColumnModel().getColumn(r).setWidth(70);
                }

                for (int u = 1; u < 4; u++) {
                    treeTablePresExplorerItems.getColumnModel().getColumn(u).setMaxWidth(70);
                    treeTablePresExplorerItems.getColumnModel().getColumn(u).setMinWidth(70);
                    treeTablePresExplorerItems.getColumnModel().getColumn(u).setWidth(70);

                    treeTablePresEditorPage.getColumnModel().getColumn(u).setMaxWidth(70);
                    treeTablePresEditorPage.getColumnModel().getColumn(u).setMinWidth(70);
                    treeTablePresEditorPage.getColumnModel().getColumn(u).setWidth(70);


                }

                treeTablePresExplorerItems.getColumnModel().getColumn(4).setMaxWidth(0);
                treeTablePresExplorerItems.getColumnModel().getColumn(4).setMinWidth(0);
                treeTablePresExplorerItems.getColumnModel().getColumn(4).setWidth(0);

                treeTablePresEditorPage.getColumnModel().getColumn(4).setMaxWidth(0);
                treeTablePresEditorPage.getColumnModel().getColumn(4).setMinWidth(0);
                treeTablePresEditorPage.getColumnModel().getColumn(4).setWidth(0);


                for (int u = 1; u < 4; u++) {
                    jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(u).setMaxWidth(70);
                    jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(u).setMinWidth(70);
                    jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(u).setWidth(70);
                }

                jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(4).setMaxWidth(0);
                jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(4).setMinWidth(0);
                jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(4).setWidth(0);
            }
        } else {
            for (int i = 0; i < 2; i++) {
                jtblCurrentEditorRights.getColumnModel().getColumn(2).setMaxWidth(70);
                jtblCurrentEditorRights.getColumnModel().getColumn(2).setMinWidth(70);

                for (int r = 1; r < 4; r += 2) {
                    jtblCurrentEditorRights.getColumnModel().getColumn(r).setMaxWidth(0);
                    jtblCurrentEditorRights.getColumnModel().getColumn(r).setMinWidth(0);
                    jtblCurrentEditorRights.getColumnModel().getColumn(r).setWidth(0);
                }



                for (int u = 1; u < 4; u++) {
                    treeTablePresExplorerItems.getColumnModel().getColumn(u).setMaxWidth(0);
                    treeTablePresExplorerItems.getColumnModel().getColumn(u).setMinWidth(0);
                    treeTablePresExplorerItems.getColumnModel().getColumn(u).setWidth(0);

                    treeTablePresEditorPage.getColumnModel().getColumn(u).setMaxWidth(0);
                    treeTablePresEditorPage.getColumnModel().getColumn(u).setMinWidth(0);
                    treeTablePresEditorPage.getColumnModel().getColumn(u).setWidth(0);

                }

                treeTablePresExplorerItems.getColumnModel().getColumn(4).setMaxWidth(70);
                treeTablePresExplorerItems.getColumnModel().getColumn(4).setMinWidth(70);
                treeTablePresExplorerItems.getColumnModel().getColumn(4).setWidth(70);

                treeTablePresEditorPage.getColumnModel().getColumn(4).setMaxWidth(70);
                treeTablePresEditorPage.getColumnModel().getColumn(4).setMinWidth(70);
                treeTablePresEditorPage.getColumnModel().getColumn(4).setWidth(70);


                for (int u = 1; u < 4; u++) {
                    jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(u).setMaxWidth(0);
                    jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(u).setMinWidth(0);
                    jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(u).setWidth(0);
                }

                jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(4).setMaxWidth(70);
                jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(4).setMinWidth(70);
                jtblCurrentEditorEnabledCmd.getColumnModel().getColumn(4).setWidth(70);
            }


        }

    }

    private void fillEditorPresentationRightsAndCommand() {


        if (treeTableEditorPresentations.getSelectedRow() < 0
                || currCoverEditorPresentation == null
                || currCoverEditorPresentation.epr == null) {
            isUpdated++;
            for (int i = 0; i < 7; i++) {
                jtblCurrentEditorRights.setValueAt(false, i, 1);
                jtblCurrentEditorRights.setValueAt(false, i, 2);
                jtblCurrentEditorRights.setValueAt(false, i, 3);
                blueInheritEditorRights[i] = false;
            }
            isUpdated--;

            jtblCurrentEditorRights.setBackground(backgroundColor);
            jbtDelEdPr.setEnabled(treeTableEditorPresentations.getSelectedRow() >= 0);
            boolean isEntityClasesListNotEmpty = false;//jtblEntityResourceModel.getRowCount() != 0;


            jbtAddEdPr.setEnabled(isEntityClasesListNotEmpty);
            jbtInheritEdPr.setEnabled(isEntityClasesListNotEmpty);
            jbtAllowedEditorPres1.setEnabled(isEntityClasesListNotEmpty);


            jtblCurrentEditorRights.setEnabled(false);
            jbtSetAllRightsSelectorPres1.setEnabled(false);
            jbtSetAccessRightsSelectorPres1.setEnabled(false);

            jbtSetEdPresExplChild.setEnabled(false);
            jbtUnSetEdPresExplChild.setEnabled(false);




            //inhEditorModel.setRowCount(0);
            currEditorModel.setRowCount(0);
            isDisableTableCurrentEditorRights = true;
            isDisableTableCurrentEditorRights2 = true;
            lastEditorPresentationId = null;
            if (treeTableEditorPresentations.getSelectedRow() < 0) {
                currCoverEditorPresentation = null;
            }
            fillPresentationExplorerItemsTree();
            fillPresentationEnabledCommandTable(null, null, null);
            stateEnabledCommandsButtons();
            fillEditorPresentationColumnTablesWidth();
            return;
        }


        //if (currCoverEditorPresentation==null || currCoverEditorPresentation.epr==null)
        //    return;










        AdsEditorPresentationDef epd = currCoverEditorPresentation.epr;//entityEditorPresentations.get(jtblEditorPresentation.getSelectedRow());
        lastEditorPresentationId = epd.getId();

        String hash = AdsRoleDef.generateResHashKey(
                EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
                lastEditorPresentationId);
        String hash2 = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION,
                currCoverEditorPresentation.epr.getOwnerClass().getId(),
                currCoverEditorPresentation.epr.getId());
        Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash2, currCoverEditorPresentation.epr);

        Restrictions or = role.getOverwriteResourceRestrictions(hash, epd);
        Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);

        fillPresentationExplorerItemsTree();
        fillPresentationEnabledCommandTable(r, ar, or);
        fillEditorPresentationColumnTablesWidth();

//        List<Id> overwriteCmdList = or.getEnabledCommandIds();
//        if (overwriteCmdList == null) {
//            overwriteCmdList = new ArrayList<>();
//        }
        blueInheritEditorRights[0] = !or.isDenied(ERestriction.ACCESS);
        blueInheritEditorRights[1] = !or.isDenied(ERestriction.CREATE);
        blueInheritEditorRights[2] = !or.isDenied(ERestriction.DELETE);
        blueInheritEditorRights[3] = !or.isDenied(ERestriction.UPDATE);
        blueInheritEditorRights[4] = !or.isDenied(ERestriction.VIEW);
        blueInheritEditorRights[5] = !or.isDenied(ERestriction.ANY_COMMAND);
        blueInheritEditorRights[6] = !or.isDenied(ERestriction.ANY_CHILD);
        blueInheritEditorRights[7] = !or.isDenied(ERestriction.ANY_PAGES);

        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ACCESS), 0, 1);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.CREATE), 1, 1);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.DELETE), 2, 1);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.UPDATE), 3, 1);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.VIEW), 4, 1);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ANY_COMMAND), 5, 1);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ANY_CHILD), 6, 1);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ANY_PAGES), 7, 1);

        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ACCESS), 0, 3);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.CREATE), 1, 3);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.DELETE), 2, 3);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.UPDATE), 3, 3);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.VIEW), 4, 3);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ANY_COMMAND), 5, 3);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ANY_CHILD), 6, 3);
        jtblCurrentEditorRights.setValueAt(!ar.isDenied(ERestriction.ANY_PAGES), 7, 3);



        blueInheritEditorEnabledCommands.clear();
        blueInheritEditorEnabledCommands1.clear();

        isUpdated++;
        if (r == null) {
            jbtSetAllRightsSelectorPres1.setEnabled(false);
            jbtSetAccessRightsSelectorPres1.setEnabled(false);

            jbtSetEdPresExplChild.setEnabled(false);
            jbtUnSetEdPresExplChild.setEnabled(false);




            jbtDelEdPr.setEnabled(false);



            jtblCurrentEditorRights.setBackground(backgroundColor);
            isDisableTableCurrentEditorRights = true;
            isDisableTableCurrentEditorRights2 = true;

            for (int j = 0; j < 8; j++) {
                jtblCurrentEditorRights.setValueAt(false, j, 2);
            }
            jtblCurrentEditorRights.setEnabled(false);
            //currEditorModel.setRowCount(0);
        } else {

            jbtSetAllRightsSelectorPres1.setEnabled(!roleIsReadOnly);
            jbtSetAccessRightsSelectorPres1.setEnabled(!roleIsReadOnly);


            jbtDelEdPr.setEnabled(true);




            jtblCurrentEditorRights.setBackground(Color.WHITE);
            jtblCurrentEditorRights.setEnabled(true);
            isDisableTableCurrentEditorRights2 = false;
            isDisableTableCurrentEditorRights = r.isDenied(ERestriction.ACCESS);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ACCESS), 0, 2);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.CREATE), 1, 2);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.DELETE), 2, 2);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.UPDATE), 3, 2);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.VIEW), 4, 2);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ANY_COMMAND), 5, 2);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ANY_CHILD), 6, 2);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ANY_PAGES), 7, 2);

            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ACCESS), 0, 3);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.CREATE), 1, 3);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.DELETE), 2, 3);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.UPDATE), 3, 3);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.VIEW), 4, 3);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ANY_COMMAND), 5, 3);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ANY_CHILD), 6, 3);
            jtblCurrentEditorRights.setValueAt(!r.isDenied(ERestriction.ANY_PAGES), 7, 3);

            boolean isMaySetChild = r.isDenied(ERestriction.ANY_CHILD) && !r.isDenied(ERestriction.ACCESS);
//            boolean isMaySetCmd = r.isDenied(ERestriction.ANY_COMMAND);

            jbtSetEdPresExplChild.setEnabled(isMaySetChild);
            jbtUnSetEdPresExplChild.setEnabled(isMaySetChild);


            mayRemoveEditorEnabledCommands.clear();
            if (jtblCurrentEditorEnabledCmd.getRowCount() > 0) {
                jtblCurrentEditorEnabledCmd.setRowSelectionInterval(jtblCurrentEditorEnabledCmd.getRowCount() - 1, jtblCurrentEditorEnabledCmd.getRowCount() - 1);
            }
        }
        stateEnabledCommandsButtons();
        isUpdated--;
    }

    private class AdsScopeCommandDefCover extends Definition {

        protected AdsScopeCommandDefCover() {
            super((Id) null);
        }
        AdsScopeCommandDef cmd = null;
        Id incorrectId = null;

        void set(AdsScopeCommandDef cmd) {
            this.cmd = cmd;
        }

        void set(Id incorrectId) {
            this.incorrectId = incorrectId;
        }

        @Override
        public String toString() {
            if (cmd != null) {
                return cmd.getName();
            }
            return "#" + incorrectId.toString();
        }

        @Override
        public String getName() {
            return toString();
        }

        @Override
        public Id getId() {
            if (cmd != null) {
                return cmd.getId();
            }
            return incorrectId;
        }
    }
    List<AdsScopeCommandDefCover> selectorCommandsList = null;
    List<AdsScopeCommandDefCover> editorCommandsList = null;

    private void fillSelectorPresentationRightsAndCommand() {


        jlSelectorPresTitle.setText("Current selector presentation - \'none\'");
        if (jtblSelectorPresentation.getRowCount() == 0) {
            isUpdated++;
            for (int i = 0; i < 4; i++) {
                jtblCurrentSelectorRights.setValueAt(false, i, 1);
                jtblCurrentSelectorRights.setValueAt(false, i, 2);
                jtblCurrentSelectorRights.setValueAt(false, i, 3);
                blueInheritSelectorRights[i] = false;
            }
            isUpdated--;

            jtblCurrentSelectorRights.setBackground(backgroundColor);

            cmdSelectorModel.setRowCount(0);
            isDisableTableCurrentSelectorRights = true;
            isDisableTableCurrentSelectorRights2 = true;
            stateEnabledCommandsButtons();

            jbtSetSelectorPresCmd.setEnabled(false);
            jbtUnSetSelectorPresCmd.setEnabled(false);

            jbtSetAllRightsSelectorPres.setEnabled(false);
            jbtSetAccessRightsSelectorPres.setEnabled(false);

            return;
        }


        CoverSelectorPresentation spd = entitySelectorPresentations.get(jtblSelectorPresentation.getSelectedRow());
        lastSelectorPresentationId = spd.getId();
        jlSelectorPresTitle.setText("Current selector presentation - \'" + spd.getQualifiedName() + "\'");

        String hash = AdsRoleDef.generateResHashKey(
                EDrcResourceType.SELECTOR_PRESENTATION,
                currCoverEntityObjectClasses.clazz.getId(),
                lastSelectorPresentationId);
        Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
        Restrictions or = role.getOverwriteResourceRestrictions(hash, null);
        Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
        List<Id> overwriteCmdList = or.getEnabledCommandIds();
        if (overwriteCmdList == null) {
            overwriteCmdList = new ArrayList<Id>();
        }

        jbtSetAllRightsSelectorPres.setEnabled(r != null && !this.roleIsReadOnly);
        jbtSetAccessRightsSelectorPres.setEnabled(r != null && !this.roleIsReadOnly);


        blueInheritSelectorRights[0] = !or.isDenied(ERestriction.ACCESS);
        blueInheritSelectorRights[1] = !or.isDenied(ERestriction.CREATE);
        blueInheritSelectorRights[2] = !or.isDenied(ERestriction.DELETE_ALL);
        blueInheritSelectorRights[3] = !or.isDenied(ERestriction.ANY_COMMAND);

        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.ACCESS), 0, 1);
        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.CREATE), 1, 1);
        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.DELETE_ALL), 2, 1);
        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.ANY_COMMAND), 3, 1);

        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.ACCESS), 0, 3);
        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.CREATE), 1, 3);
        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.DELETE_ALL), 2, 3);
        jtblCurrentSelectorRights.setValueAt(!ar.isDenied(ERestriction.ANY_COMMAND), 3, 3);


        //int i = 0;
        blueInheritSelectorEnabledCommands.clear();
        AdsEntityGroupClassDef group = null;
        CoverEntityObjectClasses tmp = currCoverEntityObjectClasses;
        while (tmp != null) {
            if (tmp.clazz instanceof AdsEntityClassDef) {
                AdsEntityClassDef clazz = (AdsEntityClassDef) tmp.clazz;
                group = clazz.findEntityGroup();
                break;
            }
            tmp = tmp.parent;
        }

        List<AdsScopeCommandDef> cmdList = group == null
                ? new ArrayList<AdsScopeCommandDef>(0)
                : group.getPresentations().getCommands().get(EScope.LOCAL_AND_OVERWRITE);

        selectorCommandsList = new ArrayList<AdsScopeCommandDefCover>(cmdList.size());
        for (AdsScopeCommandDef cmd : cmdList) {
            AdsScopeCommandDefCover cover = new AdsScopeCommandDefCover();
            cover.set(cmd);
            selectorCommandsList.add(cover);
        }

        removeOverwriteItems(selectorCommandsList);

        // добавляем удаленные деф-ии
        if (r != null) {
            List<Id> idList = r.getEnabledCommandIds();
            if (idList != null && !idList.isEmpty()) {

                for (Id id : idList) {
                    boolean isFind = false;
                    for (AdsScopeCommandDef cmd : cmdList) {
                        if (cmd.getId().equals(id)) {
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        AdsScopeCommandDefCover cover = new AdsScopeCommandDefCover();
                        cover.set(id);
                        selectorCommandsList.add(cover);
                    }

                }

            }
        }


        RadixObjectsUtils.sortByName(selectorCommandsList);

//        if (!ar.isDenied(ERestriction.ACCESS) && !ar.isDenied(ERestriction.ANY_COMMAND)) {
//
//            inhSelectorModel.setRowCount(cmdList.size());
//            for (AdsScopeCommandDef cmd : cmdList) {
//                blueInheritSelectorEnabledCommands.add(false);
//                jtblInheritSelectorEnabledCmd.setValueAt(new FooItem(cmd.getName(), cmd), i++, 0);
//            }
//        } else {
//            int count = ar.getEnabledCommandIds().size();
//
//            inhSelectorModel.setRowCount(count);
//
//            if (count > 0) {
//                for (Id id : ar.getEnabledCommandIds()) {
//                    blueInheritSelectorEnabledCommands.add(overwriteCmdList.contains(id));
//                    Definition def = null;
//                    for (AdsScopeCommandDef cmd : cmdList)
//                        if (cmd.getId().equals(id))
//                        {
//                            def = cmd;
//                            break;
//                        }
//
//                    if (def != null) {
//                        jtblInheritSelectorEnabledCmd.setValueAt(new FooItem(def.getName(), def), i++, 0);
//                    } else {
//                        jtblInheritSelectorEnabledCmd.setValueAt(new FooItem(id.toString(), id), i++, 0);
//                    }
//
//                }
//            }
//        }
        //  i = 0;

        isUpdated++;
        if (r == null) {
            jbtDelSpPr.setEnabled(false);
//            jbtAddEnabledCmdForSlPr.setEnabled(false);
//            jbtDelEnabledCmdForSlPr.setEnabled(false);
//            jbtClearEnabledCmdForSlPr.setEnabled(false);

            jtblCurrentSelectorRights.setBackground(backgroundColor);
            isDisableTableCurrentSelectorRights = true;
            isDisableTableCurrentSelectorRights2 = true;

            for (int j = 0; j < 4; j++) {
                jtblCurrentSelectorRights.setValueAt(false, j, 2);
            }
            jtblCurrentSelectorRights.setEnabled(false);
            cmdSelectorModel.setRowCount(0);
        } else {

            jbtDelSpPr.setEnabled(true);

            jtblCurrentSelectorRights.setBackground(Color.WHITE);
            jtblCurrentSelectorRights.setEnabled(true);

            isDisableTableCurrentSelectorRights2 = false;
            isDisableTableCurrentSelectorRights = r.isDenied(ERestriction.ACCESS);

            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.ACCESS), 0, 2);
            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.CREATE), 1, 2);
            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.DELETE_ALL), 2, 2);
            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.ANY_COMMAND), 3, 2);


            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.ACCESS), 0, 3);
            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.CREATE), 1, 3);
            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.DELETE_ALL), 2, 3);
            jtblCurrentSelectorRights.setValueAt(!r.isDenied(ERestriction.ANY_COMMAND), 3, 3);

            mayRemoveSelectorEnabledCommands.clear();

        }


        boolean isEnableSetAndUnset = false;

        //int cmdTblSize = 0;
        boolean isAncestorDefined =
                ar.isDenied(ERestriction.ACCESS)
                || !ar.isDenied(ERestriction.ANY_COMMAND);
        boolean isAncestorSet = ar.isDenied(ERestriction.ACCESS)
                && !ar.isDenied(ERestriction.ANY_COMMAND);
        cmdSelectorModel.setRowCount(selectorCommandsList.size());
        int i = 0;
        for (AdsScopeCommandDefCover cmd : selectorCommandsList) {
            jtblCurrentSelectorEnabledCmd.setValueAt(new FooItem(cmd.getName(), cmd), i, 0);
            if (isAncestorDefined) {
                jtblCurrentSelectorEnabledCmd.setValueAt(isAncestorSet, i, 1);
            } else {
                boolean isSet = false;
                List<Id> lst = ar.getEnabledCommandIds();
                if (lst != null) {
                    for (Id id : lst) {
                        if (id.equals(cmd.getId())) {
                            isSet = true;
                            break;
                        }
                    }
                }
                jtblCurrentSelectorEnabledCmd.setValueAt(isSet, i, 1);
                if (r == null) {
                    jtblCurrentSelectorEnabledCmd.setValueAt(isSet, i, 3);
                }
            }
            i++;
        }

        int n = selectorCommandsList.size();
        for (int j = 0; j < n; j++) {
            jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 2);
            jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 3);
            jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 4);
        }

        if (r == null) {
        } else if (r.isDenied(ERestriction.ACCESS)) {
            for (int j = 0; j < n; j++) {
                jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 2);
                jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 4);
                jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 3);
            }
        } else if (!r.isDenied(ERestriction.ANY_COMMAND)) {
            for (int j = 0; j < n; j++) {
                jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.TRUE, j, 2);
                jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.TRUE, j, 4);
                jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.TRUE, j, 3);
            }

        } else {
            isEnableSetAndUnset = !selectorCommandsList.isEmpty();
            List<Id> idList = r.getEnabledCommandIds();
            if (idList == null || idList.isEmpty()) {
                for (int j = 0; j < n; j++) {
                    jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 2);
                    jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 4);
                    jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.FALSE, j, 3);
                }
            } else {
                i = 0;
                for (AdsScopeCommandDefCover cmd : selectorCommandsList) {
                    for (int j = 0; j < idList.size(); j++) {
                        if (idList.get(j).equals(cmd.getId())) {
                            jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.TRUE, i, 2);
                            jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.TRUE, i, 3);
                            jtblCurrentSelectorEnabledCmd.setValueAt(Boolean.TRUE, i, 4);
                            break;
                        }
                    }
                    i++;
                }
            }
        }



        jbtSetSelectorPresCmd.setEnabled(isEnableSetAndUnset);
        jbtUnSetSelectorPresCmd.setEnabled(isEnableSetAndUnset);


        stateEnabledCommandsButtons();
        isUpdated--;
    }

    private void stateEnabledCommandsButtons() {
        //if (lastEditorPresentationId == null)return;
//            String hashEd = currCoverEditorPresentation==null? "":AdsRoleDef.generateResHashKey(
//                    EDrcResourceType.EDITOR_PRESENTATION,
//                    currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getId(),
//                    lastEditorPresentationId);
//            String hashSel = currCoverEntityObjectClasses==null ? "" :AdsRoleDef.generateResHashKey(
//                    EDrcResourceType.SELECTOR_PRESENTATION,
//                    currCoverEntityObjectClasses.getId(),
//                    lastSelectorPresentationId);
//            Restrictions edRestr = role.getOnlyCurrentResourceRestrictions(hashEd);
//            Restrictions slRestr = role.getOnlyCurrentResourceRestrictions(hashSel);
//
//
//
//            boolean editorEnabledButtonAdd =
//                    currCoverEditorPresentation != null &&
//                    edRestr!=null &&
//                    !isSuperAdmin && !roleIsReadOnly &&
//                    currCoverEditorPresentation != null &&
//                    currCoverEditorPresentation.getEditorPresentation().getOwnerClass().getPresentations().getCommands().get(EScope.ALL).size() > 0
//                    && !edRestr.isDenied(ERestriction.ACCESS) && edRestr.isDenied(ERestriction.ANY_COMMAND);
//        AdsEntityGroupClassDef group = null;
//        boolean selectorEnabledButtonAdd = false;
//        CoverEntityObjectClasses tmp = currCoverEntityObjectClasses;
//        while (tmp!=null)
//        {
//            if (tmp.clazz instanceof AdsEntityClassDef)
//            {
//                AdsEntityClassDef clazz = (AdsEntityClassDef)tmp.clazz;
//                group = clazz.findEntityGroup();
//                break;
//            }
//            tmp = tmp.parent;
//        }

        boolean isMayRemoveSelector = false;
        if (!isSuperAdmin && !roleIsReadOnly && jtblSelectorPresentation.getRowCount() > 0
                && jtblSelectorPresentation.getSelectedRow() != -1) {
            isMayRemoveSelector = (Boolean) jtblSelectorPresentation.getValueAt(jtblSelectorPresentation.getSelectedRow(), 1);
        }
        jbtDelSpPr.setEnabled(isMayRemoveSelector);

        boolean isMayRemoveEditor = false;
        if (!isSuperAdmin && !roleIsReadOnly && currCoverEditorPresentation != null) {
            if (currCoverEditorPresentation.epr != null) {
                String hash = AdsRoleDef.generateResHashKey(
                        EDrcResourceType.EDITOR_PRESENTATION,
                        currCoverEditorPresentation.epr.getOwnerClass().getId(),
                        currCoverEditorPresentation.epr.getId());
                isMayRemoveEditor = role.getOnlyCurrentResourceRestrictions(hash) != null;
            } else {
                isMayRemoveEditor = true;
            }
        }
        //if (currCoverEditorPresentation.epr==null)
        //    isMayRemoveEditor = (treeTableEditorPresentations.getSelectedRow() >= 0);
        jbtDelEdPr.setEnabled(isMayRemoveEditor);


        boolean isMustAdd = !isSuperAdmin && !roleIsReadOnly;
        if (isMustAdd && currCoverEntityObjectClasses != null) {
            if (currCoverEntityObjectClasses.clazz == null) {
                isMustAdd = false;
            } else {
                isMustAdd = !currCoverEntityObjectClasses.clazz.getPresentations().
                        getEditorPresentations().
                        get(EScope.LOCAL_AND_OVERWRITE).
                        isEmpty();
            }
        }
        jbtAddEdPr.setEnabled(isMustAdd);
        jbtAddSlPr.setEnabled(!isSuperAdmin && !roleIsReadOnly);

        jbtInheritEdPr.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtInheritSlPr.setEnabled(!isSuperAdmin && !roleIsReadOnly);

        jbtAllowedEditorPres1.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtAllowedSelectorPres.setEnabled(!isSuperAdmin && !roleIsReadOnly);

    }

    private List<AdsSelectorPresentationDef> getSelectorPresentationList(AdsEntityObjectClassDef ecd) {
        List<AdsSelectorPresentationDef> allPresentations;
        if (ecd != null) {
            allPresentations = ecd.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
        } else {
            allPresentations = new ArrayList<>(0);
        }
        removeOverwriteItems(allPresentations);
        return allPresentations;
    }

    private void fillExplorerItemsTree() {


        treeTableExplorerItemsModel = new TreeGridModel(null, cNames, cTypes);
        if (jtblExplorerRoots.getRowCount() == 0 || jtblExplorerRoots.getSelectedRow() < 0) {
            treeTableExplorerItemsModel.openRoot(null);
            treeTableExplorerItems.afterOpen(treeTableExplorerItemsModel, Color.lightGray);
            fillExplorerItem();
            setColumns(treeTableExplorerItems);
            return;
        }

        AdsParagraphExplorerItemDef par = paragraphs.get(jtblExplorerRoots.getSelectedRow()).getParagraphItem();
        if (par == null) {
            treeTableExplorerItemsModel.openRoot(null);
            treeTableExplorerItems.afterOpen(treeTableExplorerItemsModel, Color.lightGray);
            fillExplorerItem();
            setColumns(treeTableExplorerItems);
            return;
        }

        TreeGridRoleResourceRow root2 = new TreeGridRoleResourceRow(allOverwriteOptions, comboBoxEx, new CurrParagraphRightChecker(), treeTableExplorerItems, par.getId(), role, par, null);
        // getChilds()

        List<TreeGridRoleResourceRow> list = (List<TreeGridRoleResourceRow>) root2.getChilds();

        //AdsRoleDef.


        final ExplorerItems items = par.getExplorerItems();

        List<Id> allLst = allExplorerItemMap.get(par.getId());
        if (allLst != null) {
            for (Id id : allLst) {
                if (items.findChildExplorerItem(id) == null && RoleResourcesCash.findChildExplorerItem(allOverwriteOptions, id) == null) {

                    TreeGridRoleResourceRow zz = new TreeGridRoleResourceRow(allOverwriteOptions, comboBoxEx, new CurrParagraphRightChecker(),
                            treeTableExplorerItems, par.getId(), role, id, root2);
                    list.add(zz);

                }
            }
        }
        treeTableExplorerItemsModel.openRoot(root2);
        treeTableExplorerItems.afterOpen(treeTableExplorerItemsModel, backgroundColor);



        lastExplorerRootId = par.getId();


        fillExplorerItem();
        setColumns(treeTableExplorerItems);

        TableColumn column = treeTableExplorerItems.getColumnModel().getColumn(2);
        treeTableCellEditorMain = new DefaultCellEditor(comboBoxEx);
        column.setCellEditor(treeTableCellEditorMain);
        treeTableExplorerItems.setDefaultRenderer(Object.class, whiteTableCellRendererWithBold);

        for (int i = 1; i < 4; i++) {
            if (i == 2) {
                continue;
            }

            column = treeTableExplorerItems.getColumnModel().getColumn(i);
            column.setCellRenderer(grayTableCellRendererWithBold);
            column.setCellRenderer(whiteTableCellRendererWithBold);


        }
        column = treeTableExplorerItems.getColumnModel().getColumn(4);
        column.setCellRenderer(new BooleanRenderer());


    }

    private boolean isAllowed(String hash, AdsEditorPresentationDef epr) {
        if (isSuperAdmin) {
            return true;
        }

        Restrictions res = role.getOnlyCurrentResourceRestrictions(hash);
        if (res != null) {
            return true;//!res.isDenied(ERestriction.ACCESS);
        }
        res = role.getOverwriteAndAncestordResourceRestrictions(hash, epr);
        return !res.isDenied(ERestriction.ACCESS);
    }

    private boolean isFindOrRepleseInCoverHashMap(Definition def, HashMap<Id, CoverFoo> map) {
        Iterator<Id> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            Id id = iter.next();
            if (id.equals(def.getId())) {
                AdsDefinition def2 = map.get(id).getItem();
                if (def2 != def) {
                    if (def.getModule().getSegment().getLayer().isHigherThan(def2.getModule().getSegment().getLayer())) {

                        map.remove(id);
                        map.put(def.getId(), createConverFoo(def2));
                    }

                }
                return true;

            }
        }
        return false;
    }

    private boolean isFindOrRepleseInCoverList(Definition def, List<CoverFoo> list) {
        int i = 0;
        for (CoverFoo item : list) {
            AdsDefinition def2 = item.getItem();
            if (def2 == def) {
                return true;
            }
            if (def2.getId().equals(def.getId())) {
                if (def.getModule().getSegment().getLayer().isHigherThan(def2.getModule().getSegment().getLayer())) {
                    list.remove(i);
                    list.add(createConverFoo(def2));
                }
                return true;
            }
            i++;
        }
        return false;
    }

    private static void collectOwerwriteExplorerItems(RoleResourcesCash allOverwriteOptions, AdsExplorerItemDef par) {
        AdsParagraphExplorerItemDef oldParagraph = allOverwriteOptions.findParagraph(par.getId());
        if (oldParagraph == null) {
            allOverwriteOptions.allParagraphsMap.put(par.getId(), par);
        } else {
            if (AdsDefinition.Hierarchy.isOverwrittenDef(oldParagraph, par)) {
                allOverwriteOptions.allParagraphsMap.put(par.getId(), par);
            }
        }
    }

    private boolean checkParagraph(AdsDefinition def) {
        if (anitRecursion.contains(def)){
            return false;            
        }
        anitRecursion.add(def);
        if (def instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) def;
                            
            final List<AdsExplorerItemDef> childsAndThis = new ArrayList<>(par.getExplorerItems().getChildren().get(EScope.ALL));
            childsAndThis.add(par);
            for (AdsExplorerItemDef item : childsAndThis) {
                
                checkParagraph(item);
                
                collectOwerwriteExplorerItems(allOverwriteOptions, item);
            }
            
            //allExplorerItemsMap
            if (par.isRoot()
                    && !isFindOrRepleseInCoverList(par, paragraphs)
                    && !isFindOrRepleseInCoverHashMap(par, paragraphsMap)) {
                if (isAllowed(AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, par.getId(), null), null)) {
                    paragraphs.add(createConverFoo(par));
                } else {
                    paragraphsMap.put(par.getId(), createConverFoo(par));
                }
                return true;
            }
        }

        return false;
    }

    private boolean checkClasses(AdsDefinition def) {
        boolean res = false;

        if (def instanceof AdsApplicationClassDef) {
            if (entityObjectClassesMap.get(def.getId()) == null) {
                entityObjectClassesMap.put(def.getId(), new CoverEntityObjectClasses((AdsEntityObjectClassDef) def));
            }
            return true;
        }

        if (def instanceof AdsEntityClassDef) {
            if (entityObjectClassesMap.get(def.getId()) == null) {
                entityObjectClassesMap.put(def.getId(), new CoverEntityObjectClasses((AdsEntityObjectClassDef) def));
            }
        }
        return res;
    }

    private boolean checkCmd(AdsDefinition def) {
        if (def instanceof AdsContextlessCommandDef) {
            AdsContextlessCommandDef cmd = (AdsContextlessCommandDef) def;
            if (cmd != null
                    && !isFindOrRepleseInCoverList(cmd, contextlessCommands)
                    && !isFindOrRepleseInCoverHashMap(cmd, contextlessCommandsMap)) {
                if (isAllowed(AdsRoleDef.generateResHashKey(EDrcResourceType.CONTEXTLESS_COMMAND, cmd.getId(), null), null)) {
                    contextlessCommands.add(createConverFoo(cmd));
                } else {
                    contextlessCommandsMap.put(cmd.getId(), createConverFoo(cmd));
                }

                return true;
            }
        }
        return false;
    }

    private void setParagraphs() {
        int i1 = paragraphs.size();
        jtblExplorerRootsModel.setRowCount(i1);

        for (int j = 0; j < i1; j++) {
            CoverFoo par = paragraphs.get(j);
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    par.getId(), null);



            Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
            if (!ar.isDenied(ERestriction.ACCESS)) {
                jtblExplorerRootsModel.setValueAt(Allowed, j, 1);
            } else {
                jtblExplorerRootsModel.setValueAt(Forbidden, j, 1);
            }

            if (r == null) {
                jtblExplorerRootsModel.setValueAt(Inherit, j, 2);
                jtblExplorerRootsModel.setValueAt(jtblExplorerRootsModel.getValueAt(j, 1), j, 3);
                jtblExplorerRootsModel.setValueAt(Boolean.FALSE, j, 4);
            } else {
                if (!r.isDenied(ERestriction.ACCESS)) {
                    jtblExplorerRootsModel.setValueAt(Allowed, j, 2);
                    jtblExplorerRootsModel.setValueAt(Allowed, j, 3);
                    jtblExplorerRootsModel.setValueAt(Boolean.TRUE, j, 4);
                } else {
                    jtblExplorerRootsModel.setValueAt(Forbidden, j, 2);
                    jtblExplorerRootsModel.setValueAt(Forbidden, j, 3);
                    jtblExplorerRootsModel.setValueAt(Boolean.FALSE, j, 4);
                }
            }
            jtblExplorerRootsModel.setValueAt(par.getQualifiedName(), j, 0);

        }
        if (i1 > 0) {
            jtblExplorerRoots.setRowSelectionInterval(0, 0);
        }
        jbtGoToExplorerRoot.setEnabled(i1 > 0);
        stateExplorerRootDisableStatus();



        fillExplorerItemsTree();
    }

    private void setClasses() {
        CoverEntityObjectClasses rootCover = new CoverEntityObjectClasses((AdsEntityObjectClassDef) null);
        int parentCount = 0;
        Iterator<CoverEntityObjectClasses> iter = entityObjectClassesMap.values().iterator();
        while (iter.hasNext()) {
            CoverEntityObjectClasses classCover = iter.next();
            classCover.setRightsOrRightsInChilds(false);
            classCover.childs.clear();
            classCover.parent = null;
        }
        iter = entityObjectClassesMap.values().iterator();
        while (iter.hasNext()) {
            CoverEntityObjectClasses classCover = iter.next();
            if (classCover.clazz instanceof AdsApplicationClassDef) {
                AdsApplicationClassDef appClass = (AdsApplicationClassDef) classCover.clazz;
                AdsEntityObjectClassDef parent = appClass.findBasis();
                if (parent == null) {
                    continue;//incorrect hierarchy
                }
                CoverEntityObjectClasses parentCover = entityObjectClassesMap.get(parent.getId());
                parentCover.childs.add(classCover);
                classCover.parent = parentCover;
            } else {
                rootCover.childs.add(classCover);
                classCover.parent = rootCover;
                parentCount++;
            }
        }

        RadixObjectsUtils.sortByQualifiedName(rootCover.childs);

        iter = entityObjectClassesMap.values().iterator();
        while (iter.hasNext()) {
            CoverEntityObjectClasses classCover = iter.next();
            if (!classCover.childs.isEmpty()) {
                RadixObjectsUtils.sortByQualifiedName(classCover.childs);
            }
            boolean isAllow = false;

//            if (!isShowInheritClassesResources && classCover.clazz instanceof AdsApplicationClassDef)
//            {
//            }
//            else
            isAllow = isEntityDefContaintAllowedPresentationInnate(classCover.clazz);




            if (isAllow) {
                classCover.setRightsOrRightsInChilds(true);
            }
//            else
//                classCover.isRightsOrRightsInChilds = false;

            if (classCover.isRightsOrRightsInChilds()) {
                CoverEntityObjectClasses tmpCover = classCover.parent;
                while (tmpCover != null) {
                    tmpCover.setRightsOrRightsInChilds(true);
                    tmpCover = tmpCover.parent;
                    if (classCover.parent == tmpCover) {
                        break;
                    }
                }
            }
        }


        List<CoverEntityObjectClasses> entityClasses = new ArrayList<CoverEntityObjectClasses>(parentCount);
        iter = entityObjectClassesMap.values().iterator();
        while (iter.hasNext()) {
            CoverEntityObjectClasses classCover = iter.next();
            if (classCover.incorrectId != null || classCover.clazz instanceof AdsEntityClassDef) {
                entityClasses.add(classCover);
            }
        }
        RadixObjectsUtils.sortByQualifiedName(entityClasses);



        DefaultTreeModel model = (DefaultTreeModel) jEntityAndApplTree.getModel();
        iter = entityObjectClassesMap.values().iterator();
        Bool2 bool2 = new Bool2();
        DefaultMutableTreeNodeEx root =
                new DefaultMutableTreeNodeEx(
                null,
                rootCover,
                bool2);
        rootCover.treeNodeEx = root;
        rootCover.setRightsOrRightsInChilds(true);

        for (CoverEntityObjectClasses cover : entityClasses) {
            addChildClass(root, cover);
        }

        model.setRoot(root);

        for (int i = 0; i < jEntityAndApplTree.getRowCount(); i++) {
            jEntityAndApplTree.expandRow(i);
        }

        if (jEntityAndApplTree.getRowCount() > 0) {
            TreePath path = jEntityAndApplTree.getPathForRow(0);
            jEntityAndApplTree.expandPath(path);
            jEntityAndApplTree.setSelectionPath(path);
        }
        this.refreshEditorAndSelectorPresentationList();
        refreshEditorPresentationTree(null);
        checkEntityDefRowBold();
        refreshEntityObjectTreeBrunch();
        stateEntityClassDisableStatus();
        stateEnabledCommandsButtons();
    }

    private void setContextlessCommands() {
        int i3 = contextlessCommands.size();
        jtblContextlessCommandsModel.setRowCount(i3);

        for (int j = 0; j < i3; j++) {
            CoverFoo cmd = contextlessCommands.get(j);
            String hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.CONTEXTLESS_COMMAND,
                    cmd.getId(),
                    null);
            Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            Restrictions r = role.getOnlyCurrentResourceRestrictions(hash);
            if (!ar.isDenied(ERestriction.ACCESS)) {
                jtblContextlessCommandsModel.setValueAt(Allowed, j, 1);
            } else {
                jtblContextlessCommandsModel.setValueAt(Forbidden, j, 1);
            }

            if (r == null) {
                jtblContextlessCommandsModel.setValueAt(Inherit, j, 2);
                jtblContextlessCommandsModel.setValueAt(jtblContextlessCommandsModel.getValueAt(j, 1), j, 3);
                jtblContextlessCommandsModel.setValueAt(Boolean.FALSE, j, 4);
            } else {
                if (!r.isDenied(ERestriction.ACCESS)) {
                    jtblContextlessCommandsModel.setValueAt(Allowed, j, 2);
                    jtblContextlessCommandsModel.setValueAt(Allowed, j, 3);
                    jtblContextlessCommandsModel.setValueAt(Boolean.TRUE, j, 4);
                } else {
                    jtblContextlessCommandsModel.setValueAt(Forbidden, j, 2);
                    jtblContextlessCommandsModel.setValueAt(Forbidden, j, 3);
                    jtblContextlessCommandsModel.setValueAt(Boolean.FALSE, j, 4);
                }
            }
            jtblContextlessCommandsModel.setValueAt(cmd.getQualifiedName(), j, 0);
        }
        if (i3 > 0) {
            jtblContextlessCommands.setRowSelectionInterval(0, 0);
        }
        stateCmdDisableStatus();
    }

    private void stateCmdDisableStatus() {
        int y = jtblContextlessCommands.getSelectedRow();
        jbtCCmdAllowedAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtCCmdInheritAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtAddCCmd.setEnabled(!isSuperAdmin && !roleIsReadOnly && !contextlessCommandsMap.isEmpty());
        jbtDelCCmd.setEnabled(!isSuperAdmin && !roleIsReadOnly && y >= 0 && !jtblContextlessCommandsModel.getValueAt(y, 2).equals(Inherit));
        jbtGoToCCmd.setEnabled(y >= 0);


    }

    private void stateEntityClassDisableStatus() {
        boolean isBlack = false;
        boolean isRed = false;
        if (jEntityAndApplTree.getSelectionPath() != null) {
            Object currObject = jEntityAndApplTree.getSelectionPath().getLastPathComponent();
            DefaultMutableTreeNodeEx currNode = (DefaultMutableTreeNodeEx) currObject;
            isBlack = currNode.bool2.color == Color.BLACK;
            isRed = currNode.bool2.color == Color.RED;
        }

        jbtGoToEntity.setEnabled(jEntityAndApplTree.getSelectionPath() != null);

        jbtAllowedEditorPres.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtInheritEditorPres.setEnabled(!isSuperAdmin && !roleIsReadOnly);

        boolean isMayAdd = false;
        Iterator<CoverEntityObjectClasses> iter = entityObjectClassesMap.values().iterator();
        while (iter.hasNext()) {
            CoverEntityObjectClasses cover = iter.next();
            if (!cover.isRightsOrRightsInChilds()
                    && (!cover.clazz.getPresentations().getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE).isEmpty()
                    || !cover.clazz.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE).isEmpty())) {
                isMayAdd = true;
                break;
            }
        }


        jbtAddEntity.setEnabled(!isSuperAdmin && !roleIsReadOnly && isMayAdd);


        jbtDelEntity.setEnabled(!isSuperAdmin && !roleIsReadOnly
                && (isBlack || isRed));
        stateEnabledCommandsButtons();
    }

    private void stateExplorerRootDisableStatus() {
        int y = jtblExplorerRoots.getSelectedRow();
        jbtGoToExplorerRoot.setEnabled(paragraphs.size() > 0);
        jbtExplRootAllowedAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtExplRootInheritAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtAddExplorerRoot.setEnabled(!isSuperAdmin && !roleIsReadOnly);

        jbtDelExplorerRoot.setEnabled(
                !isSuperAdmin
                && !roleIsReadOnly
                && y >= 0
                && isParagraphContaintNotInheritedItem(paragraphs.get(y).getParagraphItem()));

        if (y < 0) {
            jbtDelEntity.setEnabled(false);
//            return;
        }

    }

    private void fillSetColumnVisible() {
        boolean isAncestors = role.getAncestorIds().size() > 0 || role.isOverwrite();
        Icon icon;// = null;
        String sPrefix;
        if (isAncestors) {
            sPrefix = "Inherit ";
            icon = RadixWareDesignerIcon.FILE.NEW_DOCUMENT.getIcon();
        } else {
            sPrefix = "Delete ";
            icon = RadixWareDesignerIcon.DELETE.CLEAR.getIcon();
        }
        jbtServerInheritAll.setToolTipText(
                sPrefix + "Rights for All");
        jbtExplRootInheritAll.setToolTipText(
                sPrefix + "Rights for All Explorer Roots and Theirs Subitems");
        jbtInheritEditorPres.setToolTipText(
                sPrefix + "Rights for All Editor Presentations and Theirs Explorer Items");

        jbtCCmdInheritAll.setToolTipText(
                sPrefix + "Execution Rights for All Commands");

        jbtInheritEdPr.setToolTipText(
                sPrefix + "Rights for All Editor Presentations");

        jbtInheritSlPr.setToolTipText(
                sPrefix + "Rights for All Selector Presentations");

        jbtServerInheritAll.setIcon(icon);
        jbtExplRootInheritAll.setIcon(icon);
        jbtInheritEditorPres.setIcon(icon);
        jbtCCmdInheritAll.setIcon(icon);
        jbtInheritEdPr.setIcon(icon);
        jbtInheritSlPr.setIcon(icon);



        setColumns(jtblServerResource);
        setColumns(jtblExplorerRoots);
        setColumns(jtblContextlessCommands);
        setSmallColumns(jtblCurrentSelectorEnabledCmd);
//
//        jPanel3.setVisible(isAncestors);
////        jPanel21.setVisible(isAncestors);
//
//        jSplitPane4.setEnabled(isAncestors);
//        jSplitPane6.setEnabled(isAncestors);


        if (!isAncestors) {
            //new EmptyBorder()
            jPanel12.setBorder(null);
//                jPanel24.setBorder(null);
//                jSplitPane4.setDividerSize(0);
//                jSplitPane6.setDividerSize(0);
            for (int i = 0; i < 2; i++) {

                jtblSelectorPresentation.getColumnModel().getColumn(1).setMaxWidth(70);
                jtblSelectorPresentation.getColumnModel().getColumn(1).setMinWidth(70);
                jtblSelectorPresentation.getColumnModel().getColumn(1).setWidth(70);

                jtblCurrentSelectorRights.getColumnModel().getColumn(2).setMaxWidth(70);
                jtblCurrentSelectorRights.getColumnModel().getColumn(2).setMinWidth(70);


                for (int r = 1; r < 4; r += 2) {

                    jtblCurrentSelectorRights.getColumnModel().getColumn(r).setMaxWidth(0);
                    jtblCurrentSelectorRights.getColumnModel().getColumn(r).setMinWidth(0);
                    jtblCurrentSelectorRights.getColumnModel().getColumn(r).setWidth(0);
                }
            }

        } else {
            TitledBorder border = javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Own",
                    javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.TOP);
            border.setTitlePosition(TitledBorder.CENTER);
            jPanel12.setBorder(border);

            for (int i = 0; i < 2; i++) {

                jtblSelectorPresentation.getColumnModel().getColumn(1).setMaxWidth(70);
                jtblSelectorPresentation.getColumnModel().getColumn(1).setMinWidth(70);
                jtblSelectorPresentation.getColumnModel().getColumn(1).setWidth(70);

                for (int r = 1; r < 4; r++) {

                    jtblCurrentSelectorRights.getColumnModel().getColumn(r).setMaxWidth(70);
                    jtblCurrentSelectorRights.getColumnModel().getColumn(r).setMinWidth(70);
                    jtblCurrentSelectorRights.getColumnModel().getColumn(r).setWidth(70);
 
                }
            }
        }
    }

    Set<AdsDefinition> anitRecursion = new HashSet<>(1024);
    
    private void fillResources() {

        anitRecursion.clear();

        allOverwriteOptions.clear();
        paragraphs.clear();

        contextlessCommands.clear();

        paragraphsMap.clear();
        contextlessCommandsMap.clear();
        entityObjectClassesMap.clear();


        for (AdsModule m : adsModules) {
            for (AdsDefinition def : m.getDefinitions().list()) {
                if (checkParagraph(def)
                        || checkClasses(def)
                        || checkCmd(def)) {
                }
            }
        }

        //Ищем права на удаленные дефиниции
        Iterator<AdsRoleDef.Resource> iter = role.getResources().iterator();
        allEditorPresantationMap.clear();
        allSelectorPresantationMap.clear();

        allExplorerItemMap.clear();


        Object[] contextlessCommandsArr = contextlessCommands.toArray();
        Object[] paragraphsArr = paragraphs.toArray();


        while (iter.hasNext()) {
            AdsRoleDef.Resource res = iter.next();
            if (res.type.equals(EDrcResourceType.CONTEXTLESS_COMMAND)) {

                if (contextlessCommandsMap.get(res.defId) == null) {
                    boolean isFind = false;
                    for (int i = 0; i < contextlessCommandsArr.length; i++) {
                        CoverFoo cmd = (CoverFoo) contextlessCommandsArr[i];
                        if (cmd.getId().equals(res.defId)) {
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        contextlessCommands.add(new CoverFoo(res.defId));
                    }
                }

            } else if (res.type.equals(EDrcResourceType.EDITOR_PRESENTATION)) {
                if (entityObjectClassesMap.get(res.defId) == null) {
                    entityObjectClassesMap.put(res.defId, new CoverEntityObjectClasses(res.defId));
                }
                List<Id> lst = allEditorPresantationMap.get(res.defId);
                if (lst == null) {
                    lst = new ArrayList<>();
                    allEditorPresantationMap.put(res.defId, lst);
                }
                lst.add(res.subDefId);
            } else if (res.type.equals(EDrcResourceType.SELECTOR_PRESENTATION)) {
                if (entityObjectClassesMap.get(res.defId) == null) {
                    entityObjectClassesMap.put(res.defId, new CoverEntityObjectClasses(res.defId));
                }
                List<Id> lst = allSelectorPresantationMap.get(res.defId);
                if (lst == null) {
                    lst = new ArrayList<>();
                    allSelectorPresantationMap.put(res.defId, lst);
                }
                lst.add(res.subDefId);
            } else if (res.type.equals(EDrcResourceType.EXPLORER_ROOT_ITEM)) {
                if (res.subDefId == null) {
                    if (paragraphsMap.get(res.defId) == null) {
                        boolean isFind = false;
                        for (int i = 0; i < paragraphsArr.length; i++) {
                            CoverFoo par = (CoverFoo) paragraphsArr[i];
                            if (par.getId().equals(res.defId)) {
                                isFind = true;
                                break;
                            }
                        }
                        if (!isFind) {
                            paragraphs.add(new CoverFoo(res.defId));
                        }
                    }
                } else {
                    List<Id> lst = allExplorerItemMap.get(res.defId);
                    if (lst == null) {
                        lst = new ArrayList<Id>();
                        allExplorerItemMap.put(res.defId, lst);
                    }
                    lst.add(res.subDefId);
                }
            }
        }

        RadixObjectsUtils.sortByQualifiedName(paragraphs);

        RadixObjectsUtils.sortByQualifiedName(contextlessCommands);

        setClasses();


        setParagraphs();
        setContextlessCommands();
        fillSetColumnVisible();
    }

    private void addChildClass(DefaultMutableTreeNodeEx parent, CoverEntityObjectClasses classCover) {

        if (classCover.isRightsOrRightsInChilds()) {
            Bool2 bool2 = isEntityDefContaintAllowedPresentation(classCover.clazz);
            //if (parent.bool2.isBold)
            //    bool2.isBold = true;
            DefaultMutableTreeNodeEx curr = new DefaultMutableTreeNodeEx(
                    classCover.clazz == null ? null : classCover.clazz.getIcon().getIcon(),
                    classCover,
                    bool2);
            classCover.treeNodeEx = curr;
            parent.add(curr);
            for (CoverEntityObjectClasses child : classCover.childs) {
                addChildClass(curr, child);
            }
        }

    }

    private void fillServerResourceTable() {

        Restrictions or;
        Restrictions ar;
        Restrictions r;
        String hash;
        TableModel tblModel = jtblServerResource.getModel();
        serverResourcesCanForbid.clear();

        for (int i = 0; i < SERVER_RESOURCES_SIZE; i++) {
            hash = AdsRoleDef.generateResHashKey(
                    EDrcResourceType.SERVER_RESOURCE,
                    indexToServerResource(i));

            ar = role.getOverwriteAndAncestordResourceRestrictions(hash, null);
            or = role.getOverwriteResourceRestrictions(hash, null);
            r = role.getOnlyCurrentResourceRestrictions(hash);

            serverResourcesCanForbid.add(!or.isDenied(ERestriction.ACCESS));
            if (!ar.isDenied(ERestriction.ACCESS)) {
                tblModel.setValueAt(Allowed, i, 1);
            } else {
                tblModel.setValueAt(Forbidden, i, 1);
            }
            if (r == null) {
                tblModel.setValueAt(Inherit, i, 2);
                tblModel.setValueAt(tblModel.getValueAt(i, 1), i, 3);
                tblModel.setValueAt(Boolean.FALSE, i, 4);
            } else {
                if (!r.isDenied(ERestriction.ACCESS)) {
                    tblModel.setValueAt(Allowed, i, 2);
                    tblModel.setValueAt(Allowed, i, 3);
                    tblModel.setValueAt(Boolean.TRUE, i, 4);
                } else {
                    tblModel.setValueAt(Forbidden, i, 2);
                    tblModel.setValueAt(Forbidden, i, 3);
                    tblModel.setValueAt(Boolean.FALSE, i, 4);
                }
            }
        }

        jbtServerAllowedAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtServerInheritAll.setEnabled(!isSuperAdmin && !roleIsReadOnly);

    }

    private class JComboBoxEx extends javax.swing.JComboBox {

        JComboBoxEx() {
            super();
            addItem(Inherit);
            addItem(Allowed);
            addItem(Forbidden);
        }
    }

    private void setSmallColumns(JTable t) {
        boolean isAncestors = role.getAncestorIds().size() > 0 || role.isOverwrite();

        if (isAncestors) {
            if (t.getColumnCount() > 4) {
                for (int i = 0; i < 2; i++) {
                    t.getColumnModel().getColumn(4).setMaxWidth(0);
                    t.getColumnModel().getColumn(4).setMinWidth(0);
                    t.getColumnModel().getColumn(4).setWidth(0);
                }
            }
            for (int i = 1; i < 4; i++) {
                t.getColumnModel().getColumn(i).setMaxWidth(70);
                t.getColumnModel().getColumn(i).setMinWidth(70);
            }


        } else {
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j < 4; j++) {
                    t.getColumnModel().getColumn(j).setMaxWidth(0);
                    t.getColumnModel().getColumn(j).setMinWidth(0);
                    t.getColumnModel().getColumn(j).setWidth(0);
                }
                if (t.getColumnCount() > 4) {
                    t.getColumnModel().getColumn(4).setMinWidth(70);
                    t.getColumnModel().getColumn(4).setMaxWidth(70);
                    t.getColumnModel().getColumn(4).setWidth(70);
                }

            }

        }

    }

    private void setColumns(JTable t) {
        boolean isAncestors = role.getAncestorIds().size() > 0 || role.isOverwrite();

        if (isAncestors) {
            if (t.getColumnCount() > 4) {
                for (int i = 0; i < 2; i++) {
                    t.getColumnModel().getColumn(4).setMaxWidth(0);
                    t.getColumnModel().getColumn(4).setMinWidth(0);
                    t.getColumnModel().getColumn(4).setWidth(0);
                }
            }
            for (int i = 1; i < 4; i++) {
                t.getColumnModel().getColumn(i).setMaxWidth(180);
                t.getColumnModel().getColumn(i).setMinWidth(110);
            }


        } else {
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j < 4; j++) {
                    t.getColumnModel().getColumn(j).setMaxWidth(0);
                    t.getColumnModel().getColumn(j).setMinWidth(0);
                    t.getColumnModel().getColumn(j).setWidth(0);
                }
                if (t.getColumnCount() > 4) {
                    t.getColumnModel().getColumn(4).setMinWidth(70);
                    t.getColumnModel().getColumn(4).setMaxWidth(120);
                    t.getColumnModel().getColumn(4).setWidth(100);
                }
            }
        }
    }

    private void initComponents2() {
        jbtAddEdPr.setVisible(false);
        jbtAddSlPr.setVisible(false);


        //jLabel1.setVisible(false);

        // jbtAddAPFamily.setVisible(false);
        //  jSeparator14.setVisible(false);
        //jbtDelAPFamily.setVisible(false);
        //jbtClearAPFamily.setVisible(false);
        jSeparator19.setVisible(false);

        Preferences pref = Utils.findOrCreatePreferences(settingKey);
        isShowInheritClassesResources = pref.getBoolean(showSubClassesResourcesKey, true);
        refreshjbtChangeMode();

        backgroundColor = javax.swing.UIManager.getDefaults().getColor("TableHeader.background");
        greyColor = Color.GRAY;

        final Font defaultFont = this.getFont();

        setBoldFont(new Font("Courier New", Font.BOLD, defaultFont.getSize()));
        setNormalFont(new Font("Courier New", Font.PLAIN, defaultFont.getSize()));

        whiteTableCellRendererWithBold = new TableCellRendererWithBold();
        grayTableCellRendererWithBold = new TableCellRendererWithBold();
        rendererWithBold2 = new TableCellRendererWithBold2();
        rendererWithBold2.setBackground(backgroundColor);
        grayTableCellRendererWithBold.setBackground(backgroundColor);
        whiteTableCellRendererWithBold.setBackground(Color.WHITE);

        checkBoxEx.setHorizontalAlignment(SwingConstants.CENTER);

        initAncestorAndAPFamilyLists();
        initServerResourceTable();
        initExplorerRootsTable();
        initContextlessCommandsTable();
        initEntityResourceTree();

        initPresentationExplorerItemTable();


        jtblServerResource.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtblExplorerRoots.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtblContextlessCommands.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jtblCurrentEditorEnabledCmd.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtblCurrentEditorRights.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jtblCurrentSelectorEnabledCmd.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtblCurrentSelectorRights.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtblSelectorPresentation.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        registerComponent(jEntityAndApplTree);
        jEntityAndApplTree.addMouseMotionListener(jEntityAndApplTree);

        initSelectorEnabledCmdTable();
        initEditorEnabledCmdTable();


        registerComponent(treeTableEditorPresentations);
        registerComponent(treeTableExplorerItems);

        CommonMouseMotionListener commonMouseMotionListener = new CommonMouseMotionListener();

        treeTableEditorPresentations.addMouseMotionListener(treeTableEditorPresentations);
        treeTableExplorerItems.addMouseMotionListener(treeTableExplorerItems);


        registerComponent(jtblSelectorPresentation);
        jtblSelectorPresentation.addMouseMotionListener(commonMouseMotionListener);

        registerComponent(jtblCurrentSelectorEnabledCmd);
        jtblCurrentSelectorEnabledCmd.addMouseMotionListener(commonMouseMotionListener);

        registerComponent(jtblCurrentEditorEnabledCmd);
        jtblCurrentEditorEnabledCmd.addMouseMotionListener(commonMouseMotionListener);

        registerComponent(treeTablePresExplorerItems);
        treeTablePresExplorerItems.addMouseMotionListener(commonMouseMotionListener);

        registerComponent(treeTablePresEditorPage);
        treeTablePresEditorPage.addMouseMotionListener(commonMouseMotionListener);




    }

    public AdsRoleEditorPanel() {
        role = null;
        initComponents();
        initComponents2();
    }
    AdsRoleEditorGeneralPanel generalPanel2 = null;
    boolean isAddGeneralPanel = false;

    public void open(RadixObject definition, OpenInfo info) {

        if (!isAddGeneralPanel) {
            isAddGeneralPanel = true;
            generalPanel2 = new AdsRoleEditorGeneralPanel(this);
            jPanel26.setLayout(new BorderLayout());
            jPanel26.add(generalPanel2, BorderLayout.NORTH);

        }
        generalPanel2.open(definition, info);
        this.role = (AdsRoleDef) definition;



        isSuperAdmin = role.getId().toString().equals(EDrcPredefinedRoleId.SUPER_ADMIN.getValue());
        roleIsReadOnly = role.isReadOnly();


        TableCellRendererWithBold renderer = isSuperAdmin || roleIsReadOnly ? grayTableCellRendererWithBold : whiteTableCellRendererWithBold;

        jtblSelectorPresentation.getColumnModel().getColumn(1).setCellRenderer(new BooleanRenderer());
        jtblSelectorPresentation.getColumnModel().getColumn(0).setCellRenderer(rendererWithBold2);
        jtblServerResource.getColumnModel().getColumn(2).setCellRenderer(renderer);
        jtblExplorerRoots.getColumnModel().getColumn(2).setCellRenderer(renderer);
        jtblContextlessCommands.getColumnModel().getColumn(2).setCellRenderer(renderer);

        jtblExplorerRoots.setSelectionForeground(Color.WHITE);

        update();
    }

    public void update() {
        isUpdated++;
//        jAbstract.setSelected(role.isAbstract());
//        jAbstract.setEnabled(!isSuperAdmin && !roleIsReadOnly);
//        jbtRemoveIncorrectResource.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        jbtAddAutoAPFamily.setEnabled(!isSuperAdmin && !roleIsReadOnly);
        generalPanel2.update();
        AdsModule module = role.getModule();
        adsModules.clear();

        Definition contextAsDefinition = module;

        Map<Id, Dependence> modileId2Dependence = new HashMap<>();
        contextAsDefinition.getDependenceProvider().collect(modileId2Dependence);
        Layer layer = role.getModule().getSegment().getLayer();
        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                for (Module m : layer.getAds().getModules()) {

                    if (!adsModules.contains((AdsModule) m)) {
                        adsModules.add((AdsModule) m);
                    }
                }
            }
        });

        ddsModules.clear();
        layer = role.getModule().getSegment().getLayer();
        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                for (Module m : layer.getDds().getModules()) {

                    if (!ddsModules.contains((DdsModule) m)) {
                        ddsModules.add((DdsModule) m);
                    }
                }
            }
        });


        fillServerResourceTable();

        fillResources();

        fillAncestorsList();

        fillAPFList();
        isUpdated = 0;

        boolean isAppRole = role.isAppRole();

        jbtGoToEntity.setVisible(!isAppRole);
        jSeparatorGoToEntity.setVisible(!isAppRole);


        jbtGoToCCmd.setVisible(!isAppRole);
        jSeparatorGoToCCmd.setVisible(!isAppRole);


        jbtGoToExplorerRoot.setVisible(!isAppRole);
        jSeparatorGoToExplorerRoot.setVisible(!isAppRole);


        jbtGoToAPFamily.setVisible(!isAppRole);
        jSeparatorGoToAPFamily.setVisible(!isAppRole);

    }
}
