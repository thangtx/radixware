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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.components.SimpleTable;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridModel;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTable;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTableModel;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class AdsUsedByRoleAccessAndCommandsPanel extends AdsUsedByRolePanel {

    private Boolean[] enableArray = new Boolean[8];
    private List<Boolean> mayRemoveCmd = new ArrayList<>();
//    private List<Boolean> mayRemoveChld = new ArrayList<Boolean>();

    private List<AdsScopeCommandDef> cmdList = null;
    private List<Id> childExplorerItemsList = null;
    private List<Id> editorPagesList = null;

    private AdsEntityObjectClassDef cd = null;
    private AdsEntityGroupClassDef gcd = null;
    private List<AdsScopeCommandDef> currEnabledCommandDefs = new ArrayList<>(0);

    private JTable jEnabledCmdTable = null;

    private TreeGridModel treeTablePresExplorerItemsModel;
    private TreeTable treeTablePresExplorerItems;

    private TreeGridModel treeTablePresPagesModel;
    private TreeTable treeTablePresPages;
    private TreeGridRoleResourceEdPresentationExplorerItemRow explorerItemRoot = null;
    private TreeGridRoleResourceEdPresentationPageRow pagesRoot = null;



    protected class CustomTableRenderer extends JCheckBox
            implements TableCellRenderer, Serializable {

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
            setSelected((value != null && ((Boolean) value).booleanValue()));

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(new EmptyBorder(1, 1, 1, 1));
            }
            this.setHorizontalAlignment(SwingConstants.CENTER);

            int index = jTable1.getSelectedRow();
            boolean isWhite = jTable1.getRowCount() > 0;

            if (isWhite) {
                if (!mayModify.get(index)) {
                    isWhite = false;
                } else {
                    if (row > 0 && finishRestrictionList.get(index).isDenied(ERestriction.ACCESS)
                            ||  finishRestrictionList.get(index).isDenied(ERestriction.VIEW) && row == 7
                            ) {
                        isWhite = false;
                    }
                }

                ERestriction rest = null;
                if (isWhite)
                {
                if (object instanceof AdsEditorPresentationDef)
                    {
                        switch (row) {
                        case 0:
                            rest = ERestriction.ACCESS;
                            break;
                        case 1:
                            rest = ERestriction.CREATE;
                            break;
                        case 2:
                            rest = ERestriction.DELETE;
                            break;
                        case 3:
                            rest = ERestriction.UPDATE;
                            break;
                        case 4:
                            rest = ERestriction.VIEW;
                            break;
                        case 5:
                            rest = ERestriction.ANY_COMMAND;
                            break;
                        case 6:
                            rest = ERestriction.ANY_CHILD;
                            break;
                        case 7:
                            rest = ERestriction.ANY_PAGES;
                            break;
                        }
                    }
                else
                    {
                        switch (row) {
                        case 0:
                            rest = ERestriction.ACCESS;
                            break;
                        case 1:
                            rest = ERestriction.CREATE;
                            break;
                        case 2:
                            rest = ERestriction.DELETE_ALL;
                            break;
                        case 3:
                            rest = ERestriction.ANY_COMMAND;
                            break;
                        }
                    }
                }

                //hash
                //rest
                Restrictions or = getOverwriteResourceRestrictions(finishRoleList.get(index) , hash);
                Restrictions curr = finishRestrictionList.get(index);
                //
                isWhite = curr.isDenied(rest) || or.isDenied(rest);
                //finishRoleList.get(index)

            }

            enableArray[row] = isWhite;
            if (!isWhite) {
                this.setEnabled(false);
                setBackground(backgroundColor);
            } else {
                this.setEnabled(true);
                setBackground(Color.WHITE);
            }
            return this;
        }
    };


    private DefaultTableModel createJEnabledCmdTableModel()
    {
       return  new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Enabled command", "Rights"

                }) {


            @Override
            public Class getColumnClass(int columnIndex) {

                return columnIndex==0 ? java.lang.String.class : java.lang.Boolean.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {

                return  columnIndex == 0 || mayRemoveCmd.get(rowIndex);
            }
        };
    }
 private class BooleanRendererForCmd extends JCheckBox
            implements TableCellRenderer, Serializable {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            this.setHorizontalAlignment(SwingConstants.CENTER);
            setSelected((value != null && ((Boolean) value).booleanValue()));
//            if (!isSelected)
            {
               boolean isGray = !mayRemoveCmd.get(row);
                if (isGray) {
                    this.setEnabled(false);
                    setBackground(backgroundColor);
                } else {
                    this.setEnabled(true);
                    setBackground(Color.WHITE);
                }
            }
//            else
//                setBackground(Color.WHITE);
            //this.setSelected(isSelected);

            return this;
        }
    }

private class BooleanRendererForChilds extends JCheckBox
            implements TableCellRenderer, Serializable {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            this.setHorizontalAlignment(SwingConstants.CENTER);
            setSelected((value != null && ((Boolean) value).booleanValue()));

            if (!isSelected)
            {
                boolean isGray = true;
                Object obj = table.getValueAt(row, 0);
                if (obj!=null && obj instanceof TreeGridModel.TreeGridNode)
                {
                    TreeGridModel.TreeGridNode tItem = (TreeGridModel.TreeGridNode)obj;
                    if (tItem.getGridItem() instanceof TreeGridRoleResourceEdPresentationExplorerItemRow){
                        TreeGridRoleResourceEdPresentationExplorerItemRow  item =
                        (TreeGridRoleResourceEdPresentationExplorerItemRow)tItem.getGridItem();
                        isGray = item.res == null ||
                                        item.res.isDenied(ERestriction.ACCESS) ||
                                        !item.res.isDenied(ERestriction.ANY_CHILD);
                    }
                    else{
                         TreeGridRoleResourceEdPresentationPageRow  item =
                        (TreeGridRoleResourceEdPresentationPageRow)tItem.getGridItem();
                        isGray = item.res == null ||
                                        item.res.isDenied(ERestriction.ACCESS)
                                        || item.res.isDenied(ERestriction.VIEW)
                                        || !item.res.isDenied(ERestriction.ANY_PAGES)
                                ;
                    }



                }
             if (isGray) {
                    this.setEnabled(false);
                    setBackground(backgroundColor);
                } else {
                    this.setEnabled(true);
                    setBackground(Color.WHITE);
                }
            }

            this.setHorizontalAlignment(SwingConstants.CENTER);

            return this;
        }
    }
 private class  BackgroundTableRenderer extends DefaultTableCellRenderer {
             @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected)
                    c.setBackground(backgroundColor);
                return c;
             }
 }

    private JButton jbtSetAllCmd = new JButton() ;
    private JButton jbtUnSetAllCmd = new JButton();

    private JButton jbtSetAllExplorerItemChilds = new JButton() ;
    private JButton jbtUnSetAllExplorerItemChilds = new JButton();

    private JButton jbtSetAllPages = new JButton() ;
    private JButton jbtUnSetAllPages = new JButton();

    private JPanel  addEnabledCommantPanel(JPanel parent)
    {
        JPanel jPanel2 = new JPanel();

        JToolBar jToolBar10 = new JToolBar();
        jToolBar10.setFloatable(false);

        jbtSetAllCmd.setIcon(RadixWareDesignerIcon.CHECK.SET.getIcon());
        jbtSetAllCmd.setToolTipText("Enable the right to all the commands");
        jbtSetAllCmd.setFocusable(false);
        jbtSetAllCmd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetAllCmd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetAllCmd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetAllCmd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetAllCmd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetAllCmd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int index = jTable1.getSelectedRow();
                //AdsRoleDef rol = finishRoleList.get(index);
                Restrictions rest = finishRestrictionList.get(index);
                for (AdsScopeCommandDef cmd : cmdList)
                    rest.setCommandEnabled(cmd.getId(), true);
                 refreshRights();
            }
        });


        jbtUnSetAllCmd.setIcon(RadixWareDesignerIcon.CHECK.UNSET.getIcon());
        jbtUnSetAllCmd.setToolTipText("Disable the right to all the commands");
        jbtUnSetAllCmd.setFocusable(false);
        jbtUnSetAllCmd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtUnSetAllCmd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllCmd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllCmd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllCmd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtUnSetAllCmd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int index = jTable1.getSelectedRow();
                //AdsRoleDef rol = finishRoleList.get(index);
                Restrictions rest = finishRestrictionList.get(index);
                for (AdsScopeCommandDef cmd : cmdList)
                    rest.setCommandEnabled(cmd.getId(), false);
                 refreshRights();

            }
        });

        jToolBar10.add(jbtSetAllCmd);
        jToolBar10.add(jbtUnSetAllCmd);

        parent.setLayout(new BorderLayout());
        parent.add(jToolBar10, BorderLayout.NORTH);
        parent.add(jPanel2, BorderLayout.CENTER);

        return jPanel2;
    }


     private JPanel  addExplorerItemsPanel(JPanel parent)
    {
        JPanel jPanel2 = new JPanel();

        JToolBar jToolBar10 = new JToolBar();
        jToolBar10.setFloatable(false);

        jbtSetAllExplorerItemChilds.setIcon(RadixWareDesignerIcon.CHECK.SET.getIcon());
        jbtSetAllExplorerItemChilds.setToolTipText("Enable the right to all the explorer childs");
        jbtSetAllExplorerItemChilds.setFocusable(false);
        jbtSetAllExplorerItemChilds.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetAllExplorerItemChilds.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetAllExplorerItemChilds.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetAllExplorerItemChilds.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetAllExplorerItemChilds.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetAllExplorerItemChilds.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (childExplorerItemsList==null) return;
                int index = jTable1.getSelectedRow();
                Restrictions rest = finishRestrictionList.get(index);
                for (Id id : childExplorerItemsList)
                    rest.setChildEnabled(id, true);
                refreshChildTree();
            }
        });


        jbtUnSetAllExplorerItemChilds.setIcon(RadixWareDesignerIcon.CHECK.UNSET.getIcon());
        jbtUnSetAllExplorerItemChilds.setToolTipText("Disable the right to all the explorer childs");
        jbtUnSetAllExplorerItemChilds.setFocusable(false);
        jbtUnSetAllExplorerItemChilds.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtUnSetAllExplorerItemChilds.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllExplorerItemChilds.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllExplorerItemChilds.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllExplorerItemChilds.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtUnSetAllExplorerItemChilds.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (childExplorerItemsList==null) return;
                int index = jTable1.getSelectedRow();
                Restrictions rest = finishRestrictionList.get(index);
                for (Id id : childExplorerItemsList)
                    rest.setChildEnabled(id, false);
                refreshChildTree();
            }
        });

        jToolBar10.add(jbtSetAllExplorerItemChilds);
        jToolBar10.add(jbtUnSetAllExplorerItemChilds);

        parent.setLayout(new BorderLayout());
        parent.add(jToolBar10, BorderLayout.NORTH);
        parent.add(jPanel2, BorderLayout.CENTER);

        return jPanel2;
    }

     private JPanel  addPagesPanel(JPanel parent)
    {
        JPanel jPanel2 = new JPanel();

        JToolBar jToolBar10 = new JToolBar();
        jToolBar10.setFloatable(false);

        jbtSetAllPages.setIcon(RadixWareDesignerIcon.CHECK.SET.getIcon());
        jbtSetAllPages.setToolTipText("Enable the right to all the pages");
        jbtSetAllPages.setFocusable(false);
        jbtSetAllPages.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetAllPages.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtSetAllPages.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtSetAllPages.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtSetAllPages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetAllPages.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (editorPagesList==null) return;
                int index = jTable1.getSelectedRow();
                Restrictions rest = finishRestrictionList.get(index);
                for (Id id : editorPagesList)
                    rest.setPageEnabled(id, true);
                refreshPagesTree();
            }
        });


        jbtUnSetAllPages.setIcon(RadixWareDesignerIcon.CHECK.UNSET.getIcon());
        jbtUnSetAllPages.setToolTipText("Disable the right to all the pages");
        jbtUnSetAllPages.setFocusable(false);
        jbtUnSetAllPages.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtUnSetAllPages.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllPages.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllPages.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtUnSetAllPages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtUnSetAllPages.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (editorPagesList==null) return;
                int index = jTable1.getSelectedRow();
                Restrictions rest = finishRestrictionList.get(index);
                for (Id id : editorPagesList)
                    rest.setPageEnabled(id, false);
                refreshPagesTree();
            }
        });

        jToolBar10.add(jbtSetAllPages);
        jToolBar10.add(jbtUnSetAllPages);

        parent.setLayout(new BorderLayout());
        parent.add(jToolBar10, BorderLayout.NORTH);
        parent.add(jPanel2, BorderLayout.CENTER);

        return jPanel2;
    }

    private static String[]  cNamesPres = {"", "", "", "", ""};
    private static Class[]   cTypesBool = {TreeTableModel.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class};

    public AdsUsedByRoleAccessAndCommandsPanel(RadixObject obj) {
        super(obj);
        initComponents();



        treeTablePresExplorerItemsModel = new TreeGridModel(null, cNamesPres, cTypesBool);
        treeTablePresExplorerItems = new TreeTable(treeTablePresExplorerItemsModel);

        treeTablePresPagesModel = new TreeGridModel(null, cNamesPres, cTypesBool);
        treeTablePresPages = new TreeTable(treeTablePresPagesModel);

        jEnabledCmdTable = new SimpleTable();

        DefaultTableModel model = createJEnabledCmdTableModel();
        jEnabledCmdTable.setModel(model);

        TableColumn col;

        col = jEnabledCmdTable.getColumnModel().getColumn(0);
        col.setCellRenderer(new BackgroundTableRenderer());
        col = jEnabledCmdTable.getColumnModel().getColumn(1);
        col.setCellRenderer(new BooleanRendererForCmd());

        col = treeTablePresExplorerItems.getColumnModel().getColumn(1);
        col.setCellRenderer(new BooleanRendererForChilds());


        jEnabledCmdTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                int index = jTable1.getSelectedRow();
                int index2 = jEnabledCmdTable.getSelectedRow();
                if (index<0 || index2<0) return;
                Restrictions rest = finishRestrictionList.get(index);
                rest.setCommandEnabled(
                            cmdList.get(index2).getId(),
                            (Boolean)jEnabledCmdTable.getValueAt(index2, 1)
                           );
            }
        });

        col = jEnabledCmdTable.getColumnModel().getColumn(1);
        col.setMaxWidth(70);

        Object arr[][];

        if (obj instanceof AdsEditorPresentationDef)
        {
            JTabbedPane jTab = new JTabbedPane();

            jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));
            jPanel3.add(jTab);

            JScrollPane scroll2 = new JScrollPane();
            jTab.add("Allowed commands", scroll2);
            JPanel jParentRefPanelParent = new javax.swing.JPanel();
            scroll2.setViewportView(jParentRefPanelParent);



            {
                JPanel jPanel2 = addEnabledCommantPanel(jParentRefPanelParent);
                jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
                jPanel2.add(jEnabledCmdTable);
            }


             arr = new Object[][]{
                    {"Access", null},
                    {"Create", null},
                    {"Delete", null},
                    {"Update", null},
                    {"View", null},
                    {"Any command", null},
                    {"Any childs", null},
                    {"Any pages", null}

                };




            JScrollPane scroll3 = new JScrollPane();
            jTab.add("Enabled Children", scroll3);
            JPanel jParentRefPanelParent2 = new javax.swing.JPanel();
            scroll3.setViewportView(jParentRefPanelParent2);
            {
                JPanel jPanel4 = addExplorerItemsPanel(jParentRefPanelParent2);
                jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.Y_AXIS));
                jPanel4.add(treeTablePresExplorerItems);
            }

            //if (false)
            {

            JScrollPane scroll4 = new JScrollPane();
            jTab.add("Enabled Pages", scroll4);
            JPanel jParentRefPanelParent3 = new javax.swing.JPanel();
            scroll4.setViewportView(jParentRefPanelParent3);
            {
                JPanel jPanel4 = addPagesPanel(jParentRefPanelParent3);
                jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.Y_AXIS));
                jPanel4.add(treeTablePresPages);
            }

            }



            treeTablePresExplorerItems.setRootVisible(false);
            treeTablePresExplorerItems.getTableHeader().setReorderingAllowed(false);

            treeTablePresPages.setRootVisible(false);
            treeTablePresPages.getTableHeader().setReorderingAllowed(false);


        }
        else
        {
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Allowed commands"));
            JPanel jPanel2 = addEnabledCommantPanel(jPanel3);
            jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
            jPanel2.add(jEnabledCmdTable);

            arr = new Object[][]{
                    {"Access", null},
                    {"Create", null},
                    {"Delete All", null},
                    {"Any command", null}
                };

        }
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
                arr,
                new String[]{
                    "", ""
                }) {

            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                int index = jTable1.getSelectedRow();
                boolean isMayModify = jTable1.getRowCount() > 0 &&
                        enableArray[rowIndex] && columnIndex == 1;
                if (isMayModify) {
                    if (!mayModify.get(index)) {
                        isMayModify = false;
                    } else {
                        if (rowIndex > 0 && (finishRestrictionList.get(index)).isDenied(ERestriction.ACCESS)) {
                            isMayModify = false;
                        }
                    }
                }
                return isMayModify;
            }
        });

        CustomTableRenderer customTableRenderer = new CustomTableRenderer();
        jTable2.getColumnModel().getColumn(1).setCellRenderer(customTableRenderer);
        JViewport viewPort = new JViewport();
        viewPort.setVisible(false);
        jScrollPane2.setColumnHeader(viewPort);
        jTable2.getColumnModel().getColumn(1).setMaxWidth(15);

        if (object instanceof AdsEditorPresentationDef)
        {
            AdsEditorPresentationDef ep = (AdsEditorPresentationDef) object;
            cd = ep.getOwnerClass();
            cmdList = cd.getPresentations().getCommands().get(EScope.ALL);
            AdsRoleEditorPanel.removeOverwriteItems(cmdList);
            RadixObjectsUtils.sortByName(cmdList);

            //chldList = ep.getExplorerItems().get
            List<AdsExplorerItemDef> list =
            ep.getExplorerItems().
                        getChildren().
                                get(EScope.LOCAL_AND_OVERWRITE);
            AdsRoleEditorPanel.removeOverwriteItems(list);
            childExplorerItemsList = new ArrayList<>();
            for (AdsExplorerItemDef item : list)
            {
                AdsRoleEditorPanel.collectExplorerItems(childExplorerItemsList, item);
            }

            List<AdsEditorPageDef>  pages = ep.getEditorPages().get(EScope.ALL);
            editorPagesList = new ArrayList<>();

            for (AdsEditorPageDef page :  pages){
                editorPagesList.add(page.getId());
            }

            defId = cd.getId();
            subDefId = ep.getId();
            type = EDrcResourceType.EDITOR_PRESENTATION;
        }
        else
        {
            type = EDrcResourceType.SELECTOR_PRESENTATION;
            AdsSelectorPresentationDef sp = (AdsSelectorPresentationDef) object;
            AdsEntityObjectClassDef tmp = cd = sp.getOwnerClass();
            while (tmp!=null)
            {
                if (tmp instanceof AdsEntityClassDef)
                {
                    AdsEntityClassDef clazz = (AdsEntityClassDef)tmp;
                    gcd = clazz.findEntityGroup();
                    break;
                }
                tmp = tmp.findBasis();
            }

            if (gcd!=null){
                cmdList = gcd.getPresentations().getCommands().get(EScope.ALL);
                AdsRoleEditorPanel.removeOverwriteItems(cmdList);
                RadixObjectsUtils.sortByName(cmdList);
            }
            else
                cmdList = new ArrayList<>();
            defId = cd.getId();
            subDefId = sp.getId();
        }


        hash = AdsRoleDef.generateResHashKey(type, defId, subDefId);


        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(javax.swing.UIManager.getDefaults().getColor("TableHeader.background"));
        jTable2.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        jTable2.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        update();
    }

    private void refreshRights() {

        int index = jTable1.getSelectedRow();
        DefaultTableModel model3 = (DefaultTableModel) jEnabledCmdTable.getModel();
       // boolean isMustClearTable = true;
        currEnabledCommandDefs.clear();
        mayRemoveCmd.clear();
        model3.setRowCount(0);

        //mayRemoveCmd.clear();

        if (index > -1) {
            Restrictions res = finishRestrictionList.get(index);
            AdsRoleDef rol = this.finishRoleList.get(index);
            Restrictions or = this.getOverwriteResourceRestrictions(rol, hash);
            List<Id> ecmdList = or.getEnabledCommandIds();
            if (ecmdList==null)
                ecmdList = new ArrayList<>(0);
            boolean isAccess = !res.isDenied(ERestriction.ACCESS);
            boolean isAnyCommand = !res.isDenied(ERestriction.ANY_COMMAND);
            boolean isAnyChilds = !res.isDenied(ERestriction.ANY_CHILD);
            boolean isAnyPages = !res.isDenied(ERestriction.ANY_PAGES);

            if (object instanceof AdsEditorPresentationDef)
            {
                jTable2.setValueAt(Boolean.valueOf(isAccess), 0, 1);
                jTable2.setValueAt(Boolean.valueOf(!res.isDenied(ERestriction.CREATE)), 1, 1);
                jTable2.setValueAt(Boolean.valueOf(!res.isDenied(ERestriction.DELETE)), 2, 1);
                jTable2.setValueAt(Boolean.valueOf(!res.isDenied(ERestriction.UPDATE)), 3, 1);
                jTable2.setValueAt(Boolean.valueOf(!res.isDenied(ERestriction.VIEW)), 4, 1);
                jTable2.setValueAt(Boolean.valueOf(isAnyCommand), 5, 1);
                jTable2.setValueAt(Boolean.valueOf(isAnyChilds), 6, 1);
                jTable2.setValueAt(Boolean.valueOf(isAnyPages), 7, 1);

            }
            else
            {
                jTable2.setValueAt(Boolean.valueOf(isAccess), 0, 1);
                jTable2.setValueAt(Boolean.valueOf(!res.isDenied(ERestriction.CREATE)), 1, 1);
                jTable2.setValueAt(Boolean.valueOf(!res.isDenied(ERestriction.DELETE_ALL)), 2, 1);
                jTable2.setValueAt(Boolean.valueOf(isAnyCommand), 3, 1);
            }

            if (isAccess) {
                if (isAnyCommand) {
                    model3.setRowCount(cmdList.size());
                    int i = 0;
                    for (AdsScopeCommandDef cmd : cmdList) {
                        model3.setValueAt(cmd.getName(), i, 0);
                        model3.setValueAt(Boolean.TRUE, i, 1);
                        mayRemoveCmd.add(false);
                        i++;
                    }
                } else {
                     model3.setRowCount(cmdList.size());
                    int i = 0;
                    List<Id> list = res.getEnabledCommandIds();
                    for (AdsScopeCommandDef cmd : cmdList) {
                        model3.setValueAt(cmd.getName(), i, 0);
                        model3.setValueAt(list.contains(cmd.getId()), i, 1);
                        mayRemoveCmd.add(!ecmdList.contains(cmd.getId()));
                        i++;
                    }
                }
            }
            else
            {
                model3.setRowCount(cmdList.size());
                int i = 0;
                for (AdsScopeCommandDef cmd : cmdList) {
                    model3.setValueAt(cmd.getName(), i, 0);
                    model3.setValueAt(Boolean.FALSE, i, 1);
                    mayRemoveCmd.add(false);
                    i++;
                }
            }
        } else {
            for (int i = 0; i < (object instanceof AdsEditorPresentationDef ? 6 : 4); i++) {
                jTable2.setValueAt(Boolean.valueOf(false), i, 1);
            }
        }

//        if (isMustClearTable) {
//            model3.setRowCount(0);
//        }

        int cnt_1 = model3.getRowCount() - 1;
        if (cnt_1 > -1) {
            jEnabledCmdTable.setRowSelectionInterval(cnt_1, cnt_1);
        }
        
        jTable2.repaint();
        
        refreshChildTree();
        refreshPagesTree();
        setButtonState();

    }

    private void refreshChildTree()
    {
        if (!(object instanceof AdsEditorPresentationDef))return;
        int index = jTable1.getSelectedRow();

        AdsEditorPresentationDef presentation = (AdsEditorPresentationDef)object;

        if (jTable1.getSelectedRow()<0){
                treeTablePresExplorerItemsModel = new TreeGridModel(null, cNamesPres, cTypesBool);
                treeTablePresExplorerItems.afterOpen(treeTablePresExplorerItemsModel , Color.lightGray);
                treeTablePresExplorerItems.expandAll();
                treeTablePresExplorerItemsModel.openRoot(null);
                //treeTablePresExplorerItems.setRootVisible(false);
                treeTablePresExplorerItems.repaint();
                return;
            }
        treeTablePresExplorerItems.setRootVisible(false);

        //this.e
        List<AdsExplorerItemDef> list =
                presentation.
                            getExplorerItems().
                                    getChildren().
                                            get(EScope.LOCAL_AND_OVERWRITE);
        AdsRoleEditorPanel.removeOverwriteItems(list);
        RadixObjectsUtils.sortByName(list);


        AdsRoleDef rol = this.finishRoleList.get(index);

        explorerItemRoot = new
                TreeGridRoleResourceEdPresentationExplorerItemRow(finishRestrictionList.get(index),
                    treeTablePresExplorerItems, hash, rol, null, presentation);
        explorerItemRoot.list = new ArrayList<>();
        explorerItemRoot.isMayChild = !list.isEmpty();
        for (AdsExplorerItemDef eItem : list)
        {
            TreeGridRoleResourceEdPresentationExplorerItemRow item = new
                TreeGridRoleResourceEdPresentationExplorerItemRow(finishRestrictionList.get(index),
                    treeTablePresExplorerItems, hash, rol, eItem, presentation);
            explorerItemRoot.list.add(item);
        }

        //treeTablePresExplorerItemsModel = new TreeGridModel(root, cNames, cTypesBool);
        treeTablePresExplorerItemsModel.openRoot(explorerItemRoot);
        treeTablePresExplorerItems.afterOpen(treeTablePresExplorerItemsModel, backgroundColor);



        for (int i =0;i<2;i++)// swing bug ?
        {
            for (int j =1;j<5;j++)
            {
                int size = j == 2 ? 70 : 0;

            treeTablePresExplorerItems.getColumnModel().getColumn(j).setMaxWidth(size);
            treeTablePresExplorerItems.getColumnModel().getColumn(j).setMinWidth(size);
            treeTablePresExplorerItems.getColumnModel().getColumn(j).setWidth(size);
            }
        }

        TableColumn col = treeTablePresExplorerItems.getColumnModel().getColumn(2);
        col.setCellRenderer(new BooleanRendererForChilds());

        treeTablePresExplorerItems.expandAll();
        treeTablePresExplorerItems.repaint();

    }



 private void refreshPagesTree()
    {
        if (!(object instanceof AdsEditorPresentationDef))return;
        int index = jTable1.getSelectedRow();

        AdsEditorPresentationDef presentation = (AdsEditorPresentationDef)object;

        if (jTable1.getSelectedRow()<0){
                treeTablePresPagesModel = new TreeGridModel(null, cNamesPres, cTypesBool);
                treeTablePresPages.afterOpen(treeTablePresPagesModel , Color.lightGray);
                treeTablePresPages.expandAll();
                treeTablePresPagesModel.openRoot(null);
                //treeTablePresPages.setRootVisible(false);
                treeTablePresPages.repaint();
                return;
            }
        treeTablePresPages.setRootVisible(false);

        //this.e
       // List<AdsEditorPageDef> list =
         EditorPages.PageOrder pageOrder = presentation.getEditorPages().getOrder();

                  //.get get(EScope.LOCAL_AND_OVERWRITE);
        //AdsRoleEditorPanel.removeOverwriteItems(list);
        //RadixObjectsUtils.sortByName(list);


        AdsRoleDef rol = this.finishRoleList.get(index);

        pagesRoot = new  TreeGridRoleResourceEdPresentationPageRow(finishRestrictionList.get(index),
                    treeTablePresPages, hash, rol, null, presentation);
        pagesRoot.list = new ArrayList<>();
        pagesRoot.isMayChild = !pageOrder.isEmpty();

        for (OrderedPage orderPage : pageOrder)
        {
            AdsEditorPageDef pItem = orderPage.findPage();
            if (pItem!=null
                    ){
                TreeGridRoleResourceEdPresentationPageRow item = new
                    TreeGridRoleResourceEdPresentationPageRow(finishRestrictionList.get(index),
                        treeTablePresPages, hash, rol, pItem, presentation);
                pagesRoot.list.add(item);
            }
        }

        //treeTablePresExplorerItemsModel = new TreeGridModel(root, cNames, cTypesBool);
        treeTablePresPagesModel.openRoot(pagesRoot);
        treeTablePresPages.afterOpen(treeTablePresPagesModel, backgroundColor);



        for (int i =0;i<2;i++)// swing bug ?
        {
            for (int j =1;j<5;j++)
            {
                int size = j == 2 ? 70 : 0;

            treeTablePresPages.getColumnModel().getColumn(j).setMaxWidth(size);
            treeTablePresPages.getColumnModel().getColumn(j).setMinWidth(size);
            treeTablePresPages.getColumnModel().getColumn(j).setWidth(size);
            }
        }

        TableColumn col = treeTablePresPages.getColumnModel().getColumn(2);
        col.setCellRenderer(new BooleanRendererForChilds());

        treeTablePresPages.expandAll();
        treeTablePresPages.repaint();

    }











    private void setButtonState() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        //DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
        //DefaultTableModel model3 = (DefaultTableModel) jEnabledCmdTable.getModel();

        boolean e = model.getRowCount() > 0;

        jbtGoToRole.setEnabled(e);
        jbtClear.setEnabled(e);
        if (e)
            e = e && mayModify.get(jTable1.getSelectedRow());
        jbtDel.setEnabled(e);


//        e = e && model2.getRowCount() > 0 &&
//                    (Boolean) model2.getValueAt(0, 1) &&
//                    !(Boolean) model2.getValueAt((object instanceof AdsEditorPresentationDef) ? 4 : 3, 1);


//        jbtSetAllCmd.setEnabled(e);
//        if (object instanceof AdsSelectorPresentationDef && gcd==null)
//            jbtSetAllCmd.setEnabled(false);
//        e = e && model3.getRowCount() > 0;
//        jbtUnSetAllCmd.setEnabled(e && mayRemoveCmd.get(jEnabledCmdTable.getSelectedRow()));
        int index = jTable1.getSelectedRow();
        Restrictions rest = (index<0) ? null : finishRestrictionList.get(index);

        if (rest==null || rest.isDenied(ERestriction.ACCESS))
        {
            jbtSetAllCmd.setEnabled(false);
            jbtUnSetAllCmd.setEnabled(false);

            jbtSetAllExplorerItemChilds.setEnabled(false);
            jbtUnSetAllExplorerItemChilds.setEnabled(false);

            jbtSetAllPages.setEnabled(false);
            jbtUnSetAllPages.setEnabled(false);
        }
        else
        {
             if (!rest.isDenied(ERestriction.ANY_CHILD) || treeTablePresExplorerItems.getRowCount()==0)
             {
                jbtSetAllExplorerItemChilds.setEnabled(false);
                jbtUnSetAllExplorerItemChilds.setEnabled(false);
             }
             else
             {
                jbtSetAllExplorerItemChilds.setEnabled(true);
                jbtUnSetAllExplorerItemChilds.setEnabled(true);
             }


             if (!rest.isDenied(ERestriction.ANY_COMMAND) || jEnabledCmdTable.getRowCount()==0)
             {
                jbtSetAllCmd.setEnabled(false);
                jbtUnSetAllCmd.setEnabled(false);
             }
             else
             {
                jbtSetAllCmd.setEnabled(true);
                jbtUnSetAllCmd.setEnabled(true);
             }


             if (rest.isDenied(ERestriction.VIEW) ||
                 !rest.isDenied(ERestriction.ANY_PAGES)  ||
                  treeTablePresPages.getRowCount()==0)
             {
                jbtSetAllPages.setEnabled(false);
                jbtUnSetAllPages.setEnabled(false);
             }
             else
             {
                jbtSetAllPages.setEnabled(true);
                jbtUnSetAllPages.setEnabled(true);
             }
        }
    }

    final public void update() {
        //AdsEditorPresentationDef ep = (AdsEditorPresentationDef) object;
        //AdsEntityObjectClassDef cd = ep.getOwnerClass();
        refreshList(cd);
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        tableModel.setRowCount(finishRoleList.size());
        int i = 0;
        for (Definition r : finishRoleList) {
            tableModel.setValueAt(r.getQualifiedName(), i, 0);
            i++;
        }
        if (i > 0) {
            jTable1.setRowSelectionInterval(0, 0);
        }
        refreshRights();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jToolBar9 = new javax.swing.JToolBar();
        jbtAdd = new javax.swing.JButton();
        jbtDel = new javax.swing.JButton();
        jbtClear = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jbtGoToRole = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new SimpleTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new SimpleTable();

        jToggleButton1.setText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jToggleButton1.text")); // NOI18N
        jToggleButton1.setName(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jToggleButton1.name")); // NOI18N

        setName("mainPanel"); // NOI18N

        jToolBar9.setFloatable(false);
        jToolBar9.setName("jToolBar9"); // NOI18N

        jbtAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAdd.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jbtAdd.toolTipText")); // NOI18N
        jbtAdd.setFocusable(false);
        jbtAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAdd.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAdd.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAdd.setName("jbtAdd"); // NOI18N
        jbtAdd.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtAdd);

        jbtDel.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        jbtDel.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jbtDel.toolTipText")); // NOI18N
        jbtDel.setFocusable(false);
        jbtDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDel.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDel.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDel.setName("jbtDel"); // NOI18N
        jbtDel.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtDel);

        jbtClear.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        jbtClear.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jbtClear.toolTipText")); // NOI18N
        jbtClear.setFocusable(false);
        jbtClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtClear.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtClear.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtClear.setName("jbtClear"); // NOI18N
        jbtClear.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtClear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtClearActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtClear);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar9.add(jSeparator7);

        jbtGoToRole.setIcon(RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon());
        jbtGoToRole.setText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jbtGoToRole.text")); // NOI18N
        jbtGoToRole.setToolTipText(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jbtGoToRole.toolTipText")); // NOI18N
        jbtGoToRole.setFocusable(false);
        jbtGoToRole.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtGoToRole.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtGoToRole.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtGoToRole.setName("jbtGoToRole"); // NOI18N
        jbtGoToRole.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtGoToRole.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtGoToRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtGoToRoleActionPerformed(evt);
            }
        });
        jToolBar9.add(jbtGoToRole);

        jSplitPane2.setDividerLocation(100);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsUsedByRoleAccessAndCommandsPanel.class, "AdsUsedByRoleAccessAndCommandsPanel.jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jSplitPane1.setDividerLocation(211);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 302, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 307, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel3);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Access", null},
                {"Create", null},
                {"Delete", null},
                {"Update", null},
                {"View", null},
                {"Any command", null}
            },
            new String [] {
                "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setName("jTable2"); // NOI18N
        jTable2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable2PropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jSplitPane1.setLeftComponent(jScrollPane2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
        );

        jSplitPane2.setBottomComponent(jPanel1);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setBackground(javax.swing.UIManager.getDefaults().getColor("TableHeader.background"));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jSplitPane2.setTopComponent(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddActionPerformed
        //searchAll = false;
       // ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(object.getModule().getSegment().getLayer().getBranch(), new UsedByRoleVisitorProvider(cd /*must find class def!!!*/, finishRoleList, false));
       // AdsRoleDef role = (AdsRoleDef) ChooseDefinition.chooseDefinition(cfg);
        List<Definition> list = org.radixware.kernel.designer.common.general.utils.DefinitionsUtils.collectTopInside(
                this.getObject().getModule().getSegment().getLayer().getBranch(),
                new UsedByRoleVisitorProvider(cd, finishRoleList, false));
        ChooseKnownDefinitionCfg cfg =
                new ChooseKnownDefinitionCfg(
                object.getModule().getSegment().getLayer().getBranch(),
                null, list);

        AdsRoleDef role = (AdsRoleDef) ChooseDefinition.chooseDefinition(cfg);


        if (role != null) {
            int index = findPlace(finishRoleList, role, false);
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            if (index == finishRoleList.size()) {
                model.addRow(new Object[]{role.getQualifiedName(), Boolean.valueOf(true)});
                finishRoleList.add(role);
                mayModify.add(!role.isReadOnly());
                finishRestrictionList.add(Restrictions.Factory.newInstance(0));
            } else {
                model.insertRow(index, new Object[]{role.getQualifiedName(), Boolean.valueOf(true)});
                finishRoleList.add(index, role);
                mayModify.add(index, !role.isReadOnly());
                finishRestrictionList.add(index, Restrictions.Factory.newInstance(0));
            }
            jTable1.setRowSelectionInterval(index, index);
            refreshRights();
        }

}//GEN-LAST:event_jbtAddActionPerformed

    private void jbtDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelActionPerformed

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int index = jTable1.getSelectedRow();
        if (model.getRowCount() == 0 || index < 0) {
            return;
        }
        if (!mayModify.get(index)) {
            return;
        }
        finishRoleList.remove(index);
        model.removeRow(index);
        mayModify.remove(index);
        finishRestrictionList.remove(index);

        if (index == jTable1.getRowCount()) {
            index = jTable1.getRowCount() - 1;
        }
        if (index >= 0) {
            jTable1.setRowSelectionInterval(index, index);
        }
        refreshRights();
}//GEN-LAST:event_jbtDelActionPerformed

    private void jbtClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtClearActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = model.getRowCount() - 1; i > -1; i--) {
            if (mayModify.get(i)) {
                mayModify.remove(i);
                finishRoleList.remove(i);
                finishRestrictionList.remove(i);
                model.removeRow(i);
            }
        }
        int cnt_1 = model.getRowCount() - 1;
        if (cnt_1 > -1) {
            jTable1.setRowSelectionInterval(cnt_1, cnt_1);
        }
        refreshRights();
}//GEN-LAST:event_jbtClearActionPerformed

    private void jbtGoToRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtGoToRoleActionPerformed

        if (jTable1.getRowCount() == 0 || jTable1.getSelectedRow() < 0) {
            return;
        }
        AdsRoleDef role = finishRoleList.get(jTable1.getSelectedRow());
        NodesManager.selectInProjects(role);
}//GEN-LAST:event_jbtGoToRoleActionPerformed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        refreshRights();
    }//GEN-LAST:event_jTable1MouseReleased

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        refreshRights();
    }//GEN-LAST:event_jTable1KeyReleased

    private void jTable2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable2PropertyChange
        int index = jTable1.getSelectedRow();
        int row = jTable2.getSelectedRow();

        if (row > -1 && index > -1) {

            Restrictions oldRestrictions = finishRestrictionList.get(index);


            List<Id> oldEnabledCommands =
                    oldRestrictions.getEnabledCommandIds() == null ?  new ArrayList<Id>(0) : new ArrayList<>(oldRestrictions.getEnabledCommandIds());
            List<Id> oldEnabledChilds = oldRestrictions.getEnabledChildIds();
            List<Id> oldEnabledPages = oldRestrictions.getEnabledPageIds();



            long bitMask = ERestriction.toBitField(oldRestrictions.getRestriction());
            long newBitMask = 0;
            Boolean isSet = (Boolean) jTable2.getValueAt(row, 1);



            if (oldEnabledCommands.size() > 0 &&
                    (!isSet && row == 0 || isSet && row == 4)) {
                NotifyDescriptor d = new NotifyDescriptor.Confirmation("Enabled command list be clearing. Continue?", NotifyDescriptor.Confirmation.YES_NO_OPTION);
                if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.Confirmation.YES_OPTION) {
                    oldEnabledCommands.clear();
                } else {
                    refreshRights();
                    jTable2.repaint();
                    return;
                }
            }

            if (object instanceof AdsEditorPresentationDef)
            {
                switch (row) {
                    case 0:
                        newBitMask = ERestriction.ACCESS.getValue();
                        break;
                    case 1:
                        newBitMask = ERestriction.CREATE.getValue();
                        break;
                    case 2:
                        newBitMask = ERestriction.DELETE.getValue();
                        break;
                    case 3:
                        newBitMask = ERestriction.UPDATE.getValue();
                        break;
                    case 4:
                        newBitMask = ERestriction.VIEW.getValue();
                        break;
                    case 5:
                        newBitMask = ERestriction.ANY_COMMAND.getValue();
                        if (!isSet){
                             Restrictions or = getOverwriteResourceRestrictions(finishRoleList.get(index) , hash);
                             List<Id> lst = or.getEnabledCommandIds();
                             if (lst != null)
                                 for (Id id : lst)
                                 {
                                     if (!oldEnabledCommands.contains(id))
                                         oldEnabledCommands.add(id);
                                 }
                            }
                        break;
                    case 6:
                        newBitMask = ERestriction.ANY_CHILD.getValue();
                        oldEnabledChilds = new ArrayList<>(childExplorerItemsList);
                        break;
                    case 7:
                        newBitMask = ERestriction.ANY_PAGES.getValue();
                        oldEnabledPages = new ArrayList<>(editorPagesList);
                        break;
                }
            }
            else
            {
                switch (row) {
                    case 0:
                        newBitMask = ERestriction.ACCESS.getValue();
                        break;
                    case 1:
                        newBitMask = ERestriction.CREATE.getValue();
                        break;
                    case 2:
                        newBitMask = ERestriction.DELETE_ALL.getValue();
                        break;
                    case 3:
                    case 4:
                        newBitMask = ERestriction.ANY_COMMAND.getValue();
                        if (!isSet){
                             Restrictions or = getOverwriteResourceRestrictions(finishRoleList.get(index) , hash);
                             List<Id> lst = or.getEnabledCommandIds();
                             if (lst!=null)
                                 for (Id id : lst)
                                 {
                                     if (!oldEnabledCommands.contains(id))
                                         oldEnabledCommands.add(id);
                                 }
                            }
                        break;
                }
            }

            if (isSet) {
                bitMask &= ~newBitMask;
            } else {
                bitMask |= newBitMask;
            }
            Restrictions currRest = Restrictions.Factory.newInstance(bitMask, oldEnabledCommands, oldEnabledChilds, oldEnabledPages);
            finishRestrictionList.set(
                    index,
                    currRest);
            if (object instanceof AdsEditorPresentationDef)
            {
                if (explorerItemRoot!=null)
                    explorerItemRoot.setRestriction(currRest);
            }



            if (row == 0 || row == 3 || row == 4 || row == 5 || row == 6 || row == 7) {
                refreshRights();
            }

        }


    }//GEN-LAST:event_jTable2PropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar9;
    private javax.swing.JButton jbtAdd;
    private javax.swing.JButton jbtClear;
    private javax.swing.JButton jbtDel;
    private javax.swing.JButton jbtGoToRole;
    // End of variables declaration//GEN-END:variables
}