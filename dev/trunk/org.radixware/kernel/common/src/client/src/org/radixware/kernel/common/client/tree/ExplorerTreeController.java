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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.clientstate.ExplorerTreePath;
import org.radixware.schemas.clientstate.ExplorerTreeState;


public final class ExplorerTreeController {
    
    private final static class SilentlyCleanEntityModelController extends CleanModelController{
        
        private final Pid silentPid;
        
        public SilentlyCleanEntityModelController(final Pid pid){
            this.silentPid = pid;
        }

        @Override
        public boolean needToCheckMandatoryProperties(final Model model) {
            if (model instanceof EntityModel && Objects.equals(silentPid, ((EntityModel)model).getPid())){
                return false;
            }
            return true;
        }
    }
    
    private final static class Icons extends ClientIcon {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public final static Icons REMOVE_FROM_TREE = new Icons("classpath:images/remove_from_tree.svg");
        public final static Icons REMOVE_ALL_FROM_TREE = new Icons("classpath:images/remove_all_from_tree.svg");
        public final static Icons OPEN_PARENT = new Icons("classpath:images/go_to_parent.svg");
        public final static Icons GO_TO_CURRENT = new Icons("classpath:images/go_to_current.svg");
        public final static Icons GO_BACK = new Icons("classpath:images/prev.svg");
        public final static Icons GO_FORWARD = new Icons("classpath:images/next.svg");
    }    
    
    private class Actions implements IExplorerTree.Actions{
        
        private final Action remove;
        private final Action removeAll;
        private final Action goToOwner;
        private final Action goToCurrent;
        private final Action goBack;
        private final Action goForward;
        
        private final IClientEnvironment environment;         
        
        private final Action.ActionListener goBackListener = new Action.ActionListener(){
            @Override
            public void triggered(final Action action) {
                final Object userObject = action.getUserObject();
                if (userObject instanceof IExplorerTreeNode){
                    ExplorerTreeController.this.goBack((IExplorerTreeNode)userObject);
                }
            }
        };
        private final Action.ActionListener goForwardListener = new Action.ActionListener(){
            @Override
            public void triggered(final Action action) {
                final Object userObject = action.getUserObject();
                if (userObject instanceof IExplorerTreeNode){
                    ExplorerTreeController.this.goForward((IExplorerTreeNode)userObject);
                }
            }
        };     
        
        private final List<Action> allActions = new LinkedList<>();
        
        public Actions(final IClientEnvironment environment){
            this.environment = environment;
            final MessageProvider mp = environment.getMessageProvider();
            remove = createAction(Icons.REMOVE_FROM_TREE,
                                                mp.translate("ExplorerTree", "Close in Tree"),
                                                mp.translate("ExplorerTree", "Close Object in Tree"), 
                                                "close_in_tree",
                                                false,
                                                 new Action.ActionListener() {
                                                @Override
                                                public void triggered(final Action action) {
                                                    ExplorerTreeController.this.removeAction();
                                                }
                                            });
            removeAll = createAction(Icons.REMOVE_ALL_FROM_TREE,
                                                   mp.translate("ExplorerTree",  "Close All in Tree"),
                                                   mp.translate("ExplorerTree", "Close All Objects in Tree"), 
                                                    "close_all_in_tree",
                                                    false,
                                                    new Action.ActionListener() {
                                                         @Override
                                                         public void triggered(final Action action) {
                                                             ExplorerTreeController.this.removeAllAction();
                                                         }
                                                  });
            goToOwner = createAction(Icons.OPEN_PARENT,
                                                      mp.translate("ExplorerTree",  "Go To Parent Branch"),
                                                      null,
                                                      "go_to_owner",
                                                      false,
                                                      new Action.ActionListener() {
                                                           @Override
                                                           public void triggered(final Action action) {
                                                               ExplorerTreeController.this.goToOwnerAction();
                                                           }
                                                      });
            goToCurrent = createAction(Icons.GO_TO_CURRENT,
                                                       mp.translate("ExplorerTree", "Go To Current Item"),
                                                       null,
                                                       "go_to_current",
                                                       false,
                                                       new Action.ActionListener() {
                                                            @Override
                                                            public void triggered(final Action action) {
                                                                ExplorerTreeController.this.goToCurrentAction();
                                                            }
                                                       });
            goBack = createAction(Icons.GO_BACK,
                                               mp.translate("ExplorerTree", "Go Backward"),
                                               null,
                                               "go_back",
                                               false,
                                               new Action.ActionListener() {
                                                      @Override
                                                      public void triggered(final Action action) {
                                                          ExplorerTreeController.this.goBackAction();
                                                      }
                                                });
            goBack.setActionMenu(environment.getApplication().getWidgetFactory().newMenu());
            goForward = createAction(Icons.GO_FORWARD,
                                                    mp.translate("ExplorerTree", "Go Forward"),
                                                    null,
                                                    "go_forward",
                                                    false,
                                                    new Action.ActionListener() {
                                                          @Override
                                                          public void triggered(final Action action) {
                                                              ExplorerTreeController.this.goForwardAction();
                                                          }
                                                    });
            goForward.setActionMenu(environment.getApplication().getWidgetFactory().newMenu());
        }
        
        private Action createAction(final ClientIcon icon,
                                                 final String title,
                                                 final String toolTip,
                                                 final String objectName,
                                                 final boolean enabled,
                                                 final Action.ActionListener listener
                                                 ){
            final Icon actionIcon = environment.getApplication().getImageManager().getIcon(icon);
            final Action action = presenter.createAction(actionIcon, title);
            action.setToolTip(toolTip==null ? title : toolTip);
            action.setObjectName(objectName);
            action.addActionListener(listener);
            action.setEnabled(enabled);
            allActions.add(action);
            return action;
        }
        
        public void refresh(){
            final IExplorerTreeNode node = tree.getCurrent();        
            if (node != null) {
                goToOwner.setEnabled(node.getParentNode() != null);
                goToCurrent.setEnabled(true);
                final boolean isChoosenEntityExists = node.isValid() 
                                                      && (node.getView().isChoosenObject()
                                                          || node.getView().hasChoosenObjects());
                remove.setEnabled(node.isRemovable());
                if (remove.isEnabled() && node.isValid()){
                    if (node.getView().isChoosenObject()){
                        remove.setToolTip(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Object in Tree"));
                        remove.setText(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Object in Tree"));
                    }else if (isChoosenEntityExists){
                        remove.setToolTip(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Child Objects in Tree"));
                        remove.setText(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Child Objects in Tree"));
                    }else{
                        remove.setToolTip(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Delete Item from Tree"));
                        remove.setText(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Delete Item from Tree"));
                    }
                }else{
                    if (node.getView().isChoosenObject()){
                        remove.setToolTip(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Object in Tree"));
                        remove.setText(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Object in Tree"));
                    }else{
                        remove.setToolTip(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Child Objects in Tree"));
                        remove.setText(tree.getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Child Objects in Tree"));
                    }
                }
                IExplorerTreeNode rootNode;
                for(rootNode=node; rootNode.getParentNode()!=null; rootNode=rootNode.getParentNode()){};            
                removeAll.setEnabled(rootNode.isValid() && rootNode.getView().hasChoosenObjects());
                updateHistoryActions();
            } else {
                remove.setEnabled(false);
                removeAll.setEnabled(false);
                goToOwner.setEnabled(false);
                goToCurrent.setEnabled(false);
                actions.getGoBackAction().setEnabled(false);
                actions.getGoForwardAction().setEnabled(false);
            }
        }
        
        public void updateHistoryActions(){
            goBack.setEnabled(!history.getRecentlyEnteredNodes().isEmpty());            
            updateRecentlyUsedItemsMenu(goBack.getActionMenu());            
            goForward.setEnabled(!history.getReenterNodes().isEmpty());
            updateReenterItemsMenu(goForward.getActionMenu());
        }
        
        @Override
        public Action getRemoveCurrentNodeAction() {
            return remove;
        }

        @Override
        public Action getRemoveChildChoosenObjectsAction() {
            return removeAll;
        }

        @Override
        public Action getGoToParentNodeAction() {
            return goToOwner;
        }

        @Override
        public Action getGoToCurrentNodeAction() {
            return goToCurrent;
        }

        @Override
        public Action getGoBackAction() {
            return goBack;
        }

        @Override
        public Action getGoForwardAction() {
            return goForward;
        }
        
        public void disableAll(){
            for (Action action: allActions){
                action.setEnabled(false);
            }
        }        
        
        private Action createActionForNode(final IExplorerTreeNode node, final Action.ActionListener listener){
            final IExplorerItemView view = node.getView();            
            final Action action = presenter.createAction(view.getIcon(), view.getTitle());
            action.setToolTip(node.getPath());
            action.setUserObject(node);
            action.addActionListener(listener);            
            return action;
        }
        
        private void updateRecentlyUsedItemsMenu(final IMenu menu){
            if (menu!=null){
                menu.removeAllActions();
                menu.clear();
                final List<IExplorerTreeNode> nodes = new LinkedList<>(history.getRecentlyEnteredNodes());
                for (IExplorerTreeNode node: nodes){
                    if (node.isValid()){
                        menu.addAction(createActionForNode(node, goBackListener));
                    }else{
                        history.removeNode(node);
                    }
                }
            }
        }
        
        private void updateReenterItemsMenu(final IMenu menu){
            if (menu!=null){
                menu.removeAllActions();
                menu.clear();
                final List<IExplorerTreeNode> nodes = new LinkedList<>(history.getReenterNodes());
                for (IExplorerTreeNode node: nodes){
                    if (node.isValid()){
                        menu.addAction(createActionForNode(node, goForwardListener));
                    }else{
                        history.removeNode(node);
                    }
                }
            }
        }
    }
    
    private final IExplorerTree tree;
    private final IClientEnvironment environment;
    private final IExplorerTreePresenter presenter;    
    private final ExplorerTreeHistory history = new ExplorerTreeHistory();    
    private final List<IExplorerTreeNode> nodesToRemove = new ArrayList<>();
    private final List<IExplorerTreeNode> expandedNodes = new ArrayList<>();
    private final Actions actions;
    private boolean closeCurrent = true;
    private boolean wasClosed;
    private boolean enteringHistoryNode;
    private boolean enteringNode;
    private IViewManager viewManager;
    private IExplorerTreeNode currentNode;
        
    public ExplorerTreeController(final IExplorerTreePresenter presenter){        
        tree = presenter.getView();        
        environment = tree.getEnvironment();
        this.presenter = presenter;
        actions = new Actions(environment);        
    }
    
    public void setViewManager(final IViewManager viewManager){
        this.viewManager = viewManager;
    }
    
    public void afterNodeExpanded(final IExplorerTreeNode node){
        expandedNodes.add(node);
    }
    
    public Collection<IExplorerTreeNode> getExpandedNodes(){
        return Collections.unmodifiableCollection(expandedNodes);
    }
    
    public void afterNodeCollapsed(final IExplorerTreeNode node){
        expandedNodes.remove(node);
    }    
    
    public void clearExpandedNodes(){
        expandedNodes.clear();
    }
    
    public void open(){
        wasClosed = false;
    }
    
    public IExplorerTree.Actions getActions(){
        return actions;
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
            presenter.resizeToContents();
        }                
    }
    
    public void removeNode(final IExplorerTreeNode node) {        
        if (presenter.isNodeExists(node)) {
            if (canRemoveNow(node)) {
                beforeRemoveNode(node);
                presenter.removeNode(node);                
            } else {
                if (enteringNode) {//Removing current node inside of "enteringNode" method
                    //just mark for remove here
                    nodesToRemove.add(node);                    
                    return;
                }
                tree.lock();
                try {
                    if (canLeaveNode(false,null)) {
                        nodesToRemove.add(node);
                        enterNode(node, closeCurrent);
                    } else {
                        return;
                    }
                } finally {
                    tree.unlock();
                }
                presenter.setFocus();                
            }
            refreshActions();
        }
    }    
    
    private boolean canRemoveNow(final IExplorerTreeNode nodeToRemove) {
        for (IExplorerTreeNode node = tree.getCurrent(); node != null; node = node.getParentNode()) {
            if (node==nodeToRemove) {
                return false;
            }
        }
        return true;
    }    
    
    public void ensurePathExpanded(final IExplorerTreeNode node){
        for (IExplorerTreeNode n=node.getParentNode(); n!=null; n=n.getParentNode()){
            tree.expand(n);
        }
    }
    
    @SuppressWarnings("empty-statement")
    public void refreshActions(){
        actions.refresh();
    }
    
    public boolean canLeaveNode(final boolean forced, final IExplorerTreeNode nextNode) {
        final IExplorerTreeNode currentNode;
        if (viewManager != null && Objects.equals(tree.getCurrent(), viewManager.getCurrentNode())) {
            currentNode = viewManager.getCurrentNode();
        } else {
            currentNode = null;
        }

        if (currentNode != null && currentNode.isValid()) {
            final Model model = currentNode.getView().getModel();
            if (model != null && !forced) {
                if (sameNodes(currentNode,nextNode)){
                    final Pid pid = ((EntityModel)nextNode.getView().getModel()).getPid();
                    return model.canSafelyClean(new SilentlyCleanEntityModelController(pid));
                }else{
                    return model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE);
                } 
            }
        }
        return true;
    }            
    
    public void enterNode(IExplorerTreeNode targetNode, final boolean closeCurrent) {
        if (targetNode != null) {
            if (enteringNode) {
                throw new IllegalStateException("Recursive call is not supported");
            }
            enteringNode = true;
            try {
                targetNode = getActualTargetNode(targetNode);
                {//clean removed nodes
                    for (IExplorerTreeNode node : nodesToRemove) {                        
                        beforeRemoveNode(node);
                        if (presenter.isNodeExists(node)) {                                                        
                            presenter.removeNode(node);                            
                        }
                    }
                    nodesToRemove.clear();
                }
                
                presenter.setCurrent(targetNode);

                if (environment.getStatusBar()!=null){
                    environment.getStatusBar().setCurrentExplorerTreeNode(targetNode);
                }
                        
                if (viewManager != null) {
                    viewManager.openView(targetNode, closeCurrent);
                    if (wasClosed){//RADIX-9674 
                        return;
                    }
                }
                if (!nodesToRemove.isEmpty() && nodeWasRemoved(targetNode)) {
                    enteringNode = false;
                    enterNode(targetNode.getParentNode(), closeCurrent);
                } else {
                    afterEnterNode(targetNode);
                    refreshActions();
                }
            } finally {
                enteringNode = false;
            }
        }
    }    
    
    public final void goToNode(final IExplorerTreeNode node){
        if (node!=null){
            ensurePathExpanded(node);
            presenter.scrollTo(node);
        }
    }    
    
    public boolean setCurrent(final IExplorerTreeNode node){
        if (node == null) {
            return false;
        }
        if (!presenter.isNodeExists(node)) {
            return false;
        }
        tree.lock();
        try {
            if (canLeaveNode(false,node)) {
                enterNode(node, closeCurrent);
            } else {
                return false;
            }
        } finally {
            tree.unlock();
        }
        presenter.setFocus();
        return true;        
    }
    
    private IExplorerTreeNode getActualTargetNode(final IExplorerTreeNode candidate) {
        if (nodesToRemove.isEmpty() || !nodeWasRemoved(candidate)) {
            return candidate;
        }

        final IExplorerTreeNode parent = candidate.getParentNode();
        IExplorerTreeNode result = null,
                child;
        final List<IExplorerTreeNode> childs = parent.getChildNodes();
        final int itemCount = childs.size();

        //search for not removed sibiling node below candidate
        for (int i = childs.indexOf(candidate) + 1; i < itemCount && result == null; i++) {
            child = childs.get(i);
            if (!nodeWasRemoved(child)) {
                result = child;
            }
        }

        if (result == null) //search for not removed sibiling node above candidate
        {
            for (int i = childs.indexOf(candidate) - 1; i >= 0 && result == null; i--) {
                child = childs.get(i);
                if (!nodeWasRemoved(child)) {
                    result = child;
                }
            }
        }

        if (result == null) {
            //if appropriate sibiling node was not found than return parent node
            for (IExplorerTreeNode node = parent; result == null && node != null; node = node.getParentNode()) {
                if (!nodeWasRemoved(node)) {
                    result = node;
                }
            }
        }

        return result;
    }    
    
    private boolean nodeWasRemoved(final IExplorerTreeNode nodeForCheck) {
        for (IExplorerTreeNode node = nodeForCheck; node != null; node = node.getParentNode()) {
            if (nodesToRemove.contains(node)) {
                return true;
            }
        }
        return false;
    }    
    
    private static boolean sameNodes(final IExplorerTreeNode currentNode, final IExplorerTreeNode nextNode){
        if (currentNode==null || nextNode==null || !currentNode.isValid() || !nextNode.isValid()){
            return false;
        }
        final Model currentModel = currentNode.getView()==null ? null : currentNode.getView().getModel();
        final Model nextModel = nextNode.getView()==null ? null : nextNode.getView().getModel();
        if (currentModel instanceof GroupModel && currentModel.getView()!=null && nextModel instanceof EntityModel){
            final EntityModel currentEntityModel = ((ISelector)currentModel.getView()).getCurrentEntity();
            return currentEntityModel!=null && Objects.equals(currentEntityModel.getPid(), ((EntityModel)nextModel).getPid());
        }
        return false;
    }     
    
    private void afterEnterNode(final IExplorerTreeNode node){
        if (currentNode!=null && !enteringHistoryNode){
            history.addRecentlyEnteredNode(currentNode);
        }
        currentNode = node;
    }
    
    private void goBack(final IExplorerTreeNode node){        
        final IExplorerTreeNode actualNode = node==null ? history.getRecentlyEnteredNodes().get(0) : node;
        final IExplorerTreeNode curNode = currentNode;
        enteringHistoryNode = true;
        try{
            tree.setCurrent(actualNode);
        }finally{
            enteringHistoryNode = false;
        }
        history.afterGoBack(actualNode, curNode);
        actions.updateHistoryActions();
    }
    
    private void goForward(final IExplorerTreeNode node){
        final IExplorerTreeNode actualNode = node==null ? history.getReenterNodes().get(0) : node;
        final IExplorerTreeNode curNode = currentNode;
        enteringHistoryNode = true;
        try{
            tree.setCurrent(actualNode);
        }finally{
            enteringHistoryNode = false;
        }
        history.afterGoForward(actualNode, curNode);
        actions.updateHistoryActions();
    }
    
    public void clearHistory(){
        history.clear();
    }
    
    public ExplorerTreeState writeStateToXml(){        
        ExplorerTreeState.ExpandedNodes expandedNodes = null;
        ExplorerTreeState.InsertedObjects choosenObjects = null;        
        final Stack<IExplorerTreeNode> stack = new Stack<>();
        stack.addAll(tree.getRootNodes());
        IExplorerTreeNode node;
        while (!stack.isEmpty()){
            node = stack.pop();
            if (node.isValid() && node.getView().isVisible()){
                
                if (node.getView().isChoosenObject()){
                    if (choosenObjects==null){
                        choosenObjects = ExplorerTreeState.InsertedObjects.Factory.newInstance();                        
                    }
                    writeNodePath(node, choosenObjects.addNewPath());
                }
                
                if (tree.isExpanded(node)){
                    if (expandedNodes==null){
                        expandedNodes = ExplorerTreeState.ExpandedNodes.Factory.newInstance();
                    }
                    writeNodePath(node, expandedNodes.addNewPath());
                }
                
                if (node.isChildNodesInited()){
                    for (IExplorerTreeNode child: node.getChildNodes()){
                        stack.push(child);
                    }
                }
            }
        }
        
        final ExplorerTreeState xmlState = ExplorerTreeState.Factory.newInstance();
        if (expandedNodes!=null){
            xmlState.setExpandedNodes(expandedNodes);
        }
        if (choosenObjects!=null){
            xmlState.setInsertedObjects(choosenObjects);
        }
        if (currentNode!=null && currentNode.isValid()){
            writeNodePath(currentNode, xmlState.addNewCurrentPath());
        }        
        return xmlState;
    }
                
    public void restoreState(ExplorerTreeState xmlState){
        final ExplorerTreeState.InsertedObjects insertedObjects = xmlState.getInsertedObjects();
        final ChoosenObjectsProvider provider = new ChoosenObjectsProvider(environment);
        final Stack<IExplorerTreeNode> collapsedParentNodes = new Stack<>();
        provider.readInsertedObjects(insertedObjects);
        if (insertedObjects!=null){
            final List<ExplorerTreePath> pathList = insertedObjects.getPathList();
            if (pathList!=null && !pathList.isEmpty()){
                for (ExplorerTreePath path: pathList){
                    collapsedParentNodes.clear();
                    findNode(path, provider, collapsedParentNodes);
                    collapse(collapsedParentNodes);
                }
            }
        }
        
        final ExplorerTreeState.ExpandedNodes expandedNodes = xmlState.getExpandedNodes();
        if (expandedNodes!=null){
            final List<ExplorerTreePath> pathList = expandedNodes.getPathList();
            if (pathList!=null && !pathList.isEmpty()){
                IExplorerTreeNode node;
                for (ExplorerTreePath path: pathList){
                    collapsedParentNodes.clear();
                    node = findNode(path, provider, collapsedParentNodes);
                    if (node!=null){
                        tree.expand(node);
                    }
                    collapse(collapsedParentNodes);
                }
            }
        }
        
        final ExplorerTreePath currentNodePath = xmlState.getCurrentPath();
        if (currentNodePath!=null){
            final IExplorerTreeNode node = findNode(currentNodePath, provider, null);
            if (node!=null){
                tree.setCurrent(node);
            }
        }
    }
    
    private void writeNodePath(final IExplorerTreeNode node, final ExplorerTreePath xml){
        final List<IExplorerTreeNode> path = new LinkedList<>();
        for (IExplorerTreeNode n=node; n!=null; n=n.getParentNode()){
            path.add(0, n);
        }        
        for (IExplorerTreeNode n: path){
            n.writeToXml(xml.addNewNode());
        }
    }
    
    private IExplorerTreeNode findNode(final ExplorerTreePath xml, final ChoosenObjectsProvider objProvider, final Stack<IExplorerTreeNode> collapsedParentNodes){
        final List<org.radixware.schemas.clientstate.ExplorerTreeNode> path = xml.getNodeList();        
        IExplorerTreeNode currentNode = null;
        for (org.radixware.schemas.clientstate.ExplorerTreeNode node: path){
            if (node.isSetExplorerItem()){
                final Id explorerItemId = node.getExplorerItem().getId();
                if (explorerItemId==null){
                    return null;
                }
                if (currentNode==null){//rootNode                    
                    currentNode = findNodeById(tree.getRootNodes(), explorerItemId);
                    if (collapsedParentNodes!=null && !tree.isExpanded(currentNode)){
                        collapsedParentNodes.push(currentNode);
                    }
                }else if (currentNode.isValid()){
                    currentNode = findNodeById(currentNode.getChildNodes(), explorerItemId);
                    if (collapsedParentNodes!=null && !tree.isExpanded(currentNode)){
                        collapsedParentNodes.push(currentNode);
                    }                    
                }else{
                    return null;
                }
            }else if (node.isSetObject()){
                 final org.radixware.schemas.clientstate.ExplorerTreeNode.Object xmlObj = node.getObject();
                 if (currentNode==null){
                    throw new IllegalArgumentException("Choosen object cannot be a root node");
                 }else if (currentNode.isValid()){
                     final Id tableId = xmlObj.getTableId();
                     final Pid choosenObjectPid = new Pid(tableId, xmlObj.getPID());                     
                     final List<IExplorerTreeNode> childNodes = currentNode.getChildNodes();
                     boolean choosenObjectFound = false;
                     for (IExplorerTreeNode childNode: childNodes){
                         if (childNode.getView().isChoosenObject()
                             && choosenObjectPid.equals(childNode.getView().getChoosenEntityInfo().pid)){
                             currentNode = childNode;
                             if (collapsedParentNodes!=null && !tree.isExpanded(currentNode)){
                                 collapsedParentNodes.push(currentNode);
                             }
                             choosenObjectFound = true;
                             break;
                         }
                     }
                     if (!choosenObjectFound){
                        final EntityModel entityModel = objProvider.createChoosedEntityModel(xmlObj);
                        if (entityModel==null){
                            currentNode=null;
                        }else{
                            currentNode = tree.addChoosenEntity((ExplorerTreeNode)currentNode, entityModel, 0);
                            collapseAll(currentNode);
                            if (collapsedParentNodes!=null){
                                collapsedParentNodes.push(currentNode);
                            }
                        }
                     }
                 }else{
                    return null;
                 }
            }
            if (currentNode==null){
                return null;
            }
        }
        if (collapsedParentNodes!=null 
            && !collapsedParentNodes.isEmpty() 
            && currentNode!=null
            && collapsedParentNodes.peek()==currentNode){
            collapsedParentNodes.pop();//currentNode
        }
        return currentNode;
    }
    
    private void collapse(final Stack<IExplorerTreeNode> nodes){
        while(!nodes.isEmpty()){            
            tree.collapse(nodes.pop());
        }
    }
    
    private void collapseAll(final IExplorerTreeNode node){
        final Stack<IExplorerTreeNode> stack = new Stack<>();
        stack.add(node);
        IExplorerTreeNode currentNode;
        while(!stack.isEmpty()){
            currentNode  = stack.pop();
            tree.collapse(currentNode);
            if (currentNode.isChildNodesInited()){
                for (IExplorerTreeNode childNode: currentNode.getChildNodes()){
                    stack.push(childNode);
                }
            }
        }
    }
    
    private static final IExplorerTreeNode findNodeById(final Collection<IExplorerTreeNode> nodes, final Id explorerItemId){
        for (IExplorerTreeNode node: nodes){
            if (node.isValid() && explorerItemId.equals(node.getView().getExplorerItemId())){
                return node;
            }
        }
        return null;
    } 
    
    public void close(){
        expandedNodes.clear();
        nodesToRemove.clear();
        actions.disableAll();
        history.clear();
        wasClosed = true;
    }    
    
    private void removeAction(){
        removeCurrentNode();
        presenter.resizeToContents();
    }
        
    private void removeAllAction(){
        tree.lock();
        try {
            final List<IExplorerTreeNode> roots = tree.getRootNodes();
            if (canLeaveNode(false,null)) {                                        
                for (IExplorerTreeNode root: roots){
                    root.getView().removeChoosenEntities();
                }
            }
            final IExplorerTreeNode currentNode = tree.getCurrent();
            boolean currentNodeIsRoot = false;
            for (IExplorerTreeNode root: roots){
                if (currentNode==root){
                    currentNodeIsRoot = true;
                    break;
                }
            }
            if (!currentNodeIsRoot && !roots.isEmpty()) {
                enterNode(roots.get(0), closeCurrent);
            }
        } finally {
            tree.unlock();
        }
        actions.getRemoveChildChoosenObjectsAction().setEnabled(false);            
    }

    private void goToOwnerAction(){
        final IExplorerTreeNode currentNode = tree.getCurrent();
        if (currentNode!=null && currentNode.getParentNode()!=null){
            setCurrent(currentNode.getParentNode());
        }
    }

    private void goToCurrentAction(){            
        goToNode(tree.getCurrent());
    }

    private void goBackAction(){
        goBack(null);
    }

    private void goForwardAction(){            
        goForward(null);
    }    
    
    private void beforeRemoveNode(final IExplorerTreeNode node){
        for (IExplorerTreeNode childNode: node.getChildNodesRecursively()){
            expandedNodes.remove(childNode);
            history.removeNode(childNode);
            if (currentNode==childNode){
                currentNode = null;
            }
        }
        expandedNodes.remove(node);        
        history.removeNode(node);
        if (currentNode==node){
            currentNode = null;
        }
    }        
}
