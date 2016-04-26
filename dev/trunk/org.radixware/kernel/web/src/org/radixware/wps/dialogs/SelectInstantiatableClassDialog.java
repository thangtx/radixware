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

package org.radixware.wps.dialogs;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.dialogs.ISelectInstantiatableClassDialog;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.kernel.common.client.types.FilterRules;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.INodeCellRenderer;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.rwt.tree.Tree.ICellRendererProvider;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


class SelectInstantiatableClassDialog extends Dialog implements ISelectInstantiatableClassDialog {

    private final static String DLG_SETTINGS_GROUP_NAME=SettingNames.SYSTEM+"/SEL_INST_CLASS_DLG";
    private final static String EXPANDED_ITEMS_GROUP_NAME="expandedItems";    
    
    private InstantiatableClass currentClass = null;
    private Node currentNode;
    private final Tree tree = new Tree();
    private final ValStrEditorController filter;
    private final boolean sortByTitles;
    private final String settingsGroupName;
    private final List<InstantiatableClass> expandedClasses = new LinkedList<>();
    
    private static class NodeRenderer extends Node.DefaultTreeColumnCellRenderer{

        private final boolean isAbstract;
        
        public NodeRenderer(final boolean forAbstractClass){
            isAbstract = forAbstractClass;
        }
        
        @Override
        public UIObject getUI() {
            final UIObject ui = super.getUI();
            if (isAbstract){
                ui.getHtml().setCss("font-style", "italic");
            }
            return ui;
        }
    }

    public SelectInstantiatableClassDialog(final WpsEnvironment env, 
                                           final List<InstantiatableClass> classes, 
                                           final String configGroup,
                                           final boolean sortByTitles) {
        super(env.getDialogDisplayer(), "");
        settingsGroupName = DLG_SETTINGS_GROUP_NAME+"/"+configGroup;
        filter = new ValStrEditorController(env);
        this.sortByTitles = sortByTitles;
        final List<InstantiatableClass> compactedClasses;
        if (sortByTitles){
            compactedClasses = InstantiatableClass.sortByTitles(InstantiatableClass.compact(classes));
        }else{
            compactedClasses = InstantiatableClass.compact(classes);
        }        
        setupUi(env, compactedClasses);
    }
    
    private void setupUi(final WpsEnvironment env, final List<InstantiatableClass> compactedClasses){
        setWindowTitle(env.getMessageProvider().translate("SelectInstantiatableClass", "Select Class of New Object"));
        final VerticalBoxContainer container = new VerticalBoxContainer();
                
        filter.setMandatory(true);
        filter.setLabel("Filter:");
        filter.setLabelVisible(true);        
        container.add((UIObject)filter.getValEditor());
        
        container.addSpace();
        container.addSpace();
                
        tree.showHeader(false);
        tree.setRootVisible(false);
        tree.setFilterEditor(filter);
        container.add(tree);
        tree.getTreeColumn().setCellRendererProvider(new ICellRendererProvider() {
            @Override
            public INodeCellRenderer newCellRenderer(final Node node, final int c) {                
                return new NodeRenderer(SelectInstantiatableClassDialog.getClass(node).isAbstractClass());
            }
        });        
        tree.addBranchListener(new Tree.BranchListener() {

            @Override
            public void afterExpand(final Node node) {
                if (node!=null){
                    expandedClasses.add(SelectInstantiatableClassDialog.getClass(node));
                }
            }

            @Override
            public void afterCollapse(final Node node) {
                if (node!=null){
                    expandedClasses.remove(SelectInstantiatableClassDialog.getClass(node));
                }
            }
        });

        final Node.DefaultNode root = new Node.DefaultNode();
        final List<InstantiatableClass> expandedItems = readExpandedItems();
        for (InstantiatableClass cls : compactedClasses) {
            fillTree(cls, root, expandedItems);
        }
        
        tree.setRootNode(root);        
        tree.addSelectionListener(new Tree.NodeListener() {
            @Override
            public void selectionChanged(Node oldSelection, Node newSelection) {
                if (oldSelection != newSelection) {
                    currentClass = SelectInstantiatableClassDialog.getClass(newSelection);
                    setActionEnabled(EDialogButtonType.OK, currentClass!=null && !currentClass.isAbstractClass());
                    currentNode = newSelection;
                }
            }
        });        
        tree.addDoubleClickListener(new Tree.DoubleClickListener() {
            @Override
            public void nodeDoubleClick(final Node item) {
                tree.setSelectedNode(item);
                if (isActionEnabled(EDialogButtonType.OK)){
                    acceptDialog();
                }
            }
        });
        tree.addFilterListener(new Tree.FilterListener() {
            @Override
            public void afterApplyFilter(final String filter) {
                if (currentNode==null || !currentNode.isVisible()){
                    updateCurrentNode();
                }else if (currentNode!=null){                
                    for (Node node=currentNode.getParentNode(); node!=null; node=node.getParentNode()){
                        if (!node.isVisible() || !node.isExpanded()){
                            updateCurrentNode();
                            break;
                        }
                    }
                }
            }
        });

        container.setAutoSize(tree, true);
            
        add(container);
        container.setVSizePolicy(SizePolicy.EXPAND);        
        addCloseAction(EDialogButtonType.OK);
        addCloseAction(EDialogButtonType.CANCEL);        
        setActionEnabled(EDialogButtonType.OK, false);
        ((UIObject)filter.getValEditor()).setFocused(true);
    }        
    
    private void fillTree(final InstantiatableClass cls, 
                          final Node.DefaultNode parentNode,
                          final List<InstantiatableClass> expandedItems){
        final Node.DefaultNode node = createTreeNodeForClass(cls);
        parentNode.add(node);
        if (cls.getDerivedClassesCount()>0){
            final List<InstantiatableClass> derivedClasses;
            if (sortByTitles){
                derivedClasses = InstantiatableClass.sortByTitles(cls.getDerivedClasses());
            }else{
                derivedClasses = cls.getDerivedClasses();
            }
            for (InstantiatableClass derivedClass: derivedClasses){
                fillTree(derivedClass, node, expandedItems);
            }
            if (cls.containsIn(expandedItems)){
                node.expand();
                expandedClasses.add(cls);
            }
        }
    }
    
    private Node.DefaultNode createTreeNodeForClass(final InstantiatableClass cls){
        final Node.DefaultNode node = new Node.DefaultNode(cls.getTitle());
        node.setUserData(cls);
        if (!cls.isAbstractClass()){
            final FilterRules rules = new FilterRules();
            rules.addRule(cls.getTitle());
            rules.addRule(cls.getItemId().toString(),false,true);
            if (cls.getId()!=null){
                rules.addRule(cls.getId().toString(),false,true);
            }
            node.setFilterRules(rules);
        }        
        return node;
    }
    
    private static InstantiatableClass getClass(final Node node){
        return node==null ? null : (InstantiatableClass) node.getUserData();
    }
    
    private void updateCurrentNode(){
        final Stack<Node> stack = new Stack<>();
        for (int i=tree.getRootNode().getChildCount()-1; i>=0; i--){
            stack.push(tree.getRootNode().getChildAt(i));
        }
        Node curNode;
        while (!stack.isEmpty()){
            curNode = stack.pop();
            if (curNode.isVisible()){
                if (!getClass(curNode).isAbstractClass()){
                    tree.setSelectedNode(curNode);
                    return;
                }
                if (curNode.isExpanded()){
                    for (int i=curNode.getChildCount()-1; i>=0; i--){
                        stack.push(curNode.getChildAt(i));
                    }
                }
            }
        }
        tree.setSelectedNode(null);
        ((UIObject)filter.getValEditor()).setFocused(true);
    }

    @Override
    public InstantiatableClass getCurrentClass() {
        return currentClass;
    }

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
        writeExpandedItems();
        return super.onClose(action, actionResult);
    }
    
    
    
    private void writeExpandedItems(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.remove(settingsGroupName);
        settings.beginGroup(settingsGroupName);
        try{            
            InstantiatableClass.writeToSettings(settings, EXPANDED_ITEMS_GROUP_NAME, expandedClasses);
        }finally{
            settings.endGroup();
        }
    }
    
    private List<InstantiatableClass> readExpandedItems(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.beginGroup(settingsGroupName);
        try{
            return InstantiatableClass.readFromSettings(settings, EXPANDED_ITEMS_GROUP_NAME, null);
        }finally{
            settings.endGroup();
        }        
    }    
}
