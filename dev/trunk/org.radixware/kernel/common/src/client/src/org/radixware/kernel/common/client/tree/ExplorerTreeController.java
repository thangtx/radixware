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

package org.radixware.kernel.common.client.tree;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.types.Id;


public final class ExplorerTreeController {
    
    private final IExplorerTree tree;
        
    public ExplorerTreeController(final IExplorerTreePresenter presenter){
        tree = presenter.getView();
    }
    
    public void removeCurrentNode() {
        final IExplorerTreeNode node = tree.getCurrent();
        if (node != null) {
            if (node.isRemovable()) {
                final boolean isValid = node.isValid();                
                if (isValid && node.getView().isUserItemView()){
                    final String title = 
                        tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Confirm to delete explorer item");
                    final String message = 
                        tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Do you really want to delete explorer item \'%1s\'");
                    if (!tree.getEnvironment().messageConfirmation(title, String.format(message, node.getView().getTitle()))){
                        return;
                    }
                    final IExplorerItemView view = node.getView();                    
                    final IExplorerItemsHolder itemsHolder = view.getTopLevelExplorerItemsHolder();
                    if (itemsHolder instanceof RadEditorPresentationDef){//Удаление пользовательского элемента в контексте объекта
                        final Id tableId = ((RadEditorPresentationDef)itemsHolder).getTableId();
                        //Нужно удалить элемент из всех вставленных в дерево объектов этой сущности                        
                        final Collection<IExplorerTreeNode> allNodes = new LinkedList<>();
                        final Id thisExplorerItemId = view.getExplorerItemId();
                        for (IExplorerTreeNode rootNode: tree.getRootNodes()){
                            allNodes.addAll(rootNode.getChildNodesRecursively());
                        }
                        tree.lock();
                        try{
                            for (IExplorerTreeNode treeNode: allNodes){
                                if (treeNode!=node//remove current node later
                                    && treeNode.isValid()
                                    && thisExplorerItemId.equals(treeNode.getView().getExplorerItemId())
                                   ){                               
                                   final IExplorerItemsHolder treeNodeHolder = 
                                        treeNode.getView().getTopLevelExplorerItemsHolder();
                                   if (treeNodeHolder instanceof RadEditorPresentationDef
                                       && tableId.equals(((RadEditorPresentationDef)treeNodeHolder).getTableId())
                                       && treeNode!=node
                                      ){
                                        tree.removeNode(treeNode);
                                   }
                                }
                            }
                            tree.removeNode(node);
                        }finally{
                            tree.unlock();
                        }
                        return;
                    }else{
                        tree.removeNode(node);
                        return;
                    }
                }else if (!isValid || node.getView().isChoosenObject()){
                    tree.removeNode(node);
                    return;
                }
            }
            node.getView().removeChoosenEntities();
            refreshActions();
            tree.update(node);
        }                
    }
    
    public void ensurePathExpanded(final IExplorerTreeNode node){
        for (IExplorerTreeNode n=node.getParentNode(); n!=null; n=n.getParentNode()){
            tree.expand(n);
        }
    }
    
    public void refreshActions(){
        final IExplorerTree.Actions actions = tree.getActions();
        final Action remove = actions.getRemoveCurrentNodeAction();
        final Action removeAll = actions.getRemoveChildChoosenObjectsAction();
        final Action goToOwner = actions.getGoToParentNodeAction();
        final Action goToCurrent = actions.getGoToCurrentNodeAction();

        final IExplorerTreeNode node = tree.getCurrent();
        if (node != null) {
            goToOwner.setEnabled(node.getParentNode() != null);
            goToCurrent.setEnabled(true);
            final boolean isChoosenEntityExists = (node.isValid() && node.getView().isChoosenObject())
                    || isChoosenEntityExists(node.getChildNodes());                
            remove.setEnabled(node.isRemovable());
            if (remove.isEnabled()){
                if (node.isValid() && node.getView().isChoosenObject()){
                    remove.setToolTip(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Object in Tree"));
                    remove.setText(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Object in Tree"));
                }else{
                    remove.setToolTip(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Delete Item from Tree"));
                    remove.setText(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Delete Item from Tree"));
                }
            }else{
                remove.setToolTip("");
            }
            removeAll.setEnabled(isChoosenEntityExists
                    || (node.isValid() && node.getView().hasChoosenObjects()));
        } else {
            remove.setEnabled(false);
            removeAll.setEnabled(false);
            goToOwner.setEnabled(false);
            goToCurrent.setEnabled(false);
        }     
    }
    
    private boolean isChoosenEntityExists(final List<IExplorerTreeNode> nodes) {
        for (IExplorerTreeNode node : nodes) {
            if (node.isValid() && node.getView().isChoosenObject()) {
                return true;
            }
        }
        return false;
    }    
    
}
