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
package org.radixware.wps.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.StringTokenizer;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;

import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.tree.ExplorerTreeController;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.IExplorerTreePresenter;
import org.radixware.kernel.common.client.tree.IViewManager;
import org.radixware.kernel.common.client.tree.nodes.ChoosenEntityNode;
import org.radixware.kernel.common.client.tree.nodes.ExplorerItemNode;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.RootParagraphNode;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.views.NavigationView;
import org.radixware.wps.views.RwtAction;

class WpsTree extends org.radixware.wps.rwt.tree.Tree implements IExplorerTree {

    private static class Presenter implements IExplorerTreePresenter {

        final WpsTree tree;

        public Presenter(final WpsTree tree) {
            this.tree = tree;
        }

        @Override
        public IExplorerTree getView() {
            return tree;
        }

        @Override
        public void setFocus() {
            tree.setFocused(true);
        }

        @Override
        public void removeNode(final IExplorerTreeNode node) {
            final Node treeNode = tree.findNodeByExplorerTreeNode(node);
            if (treeNode!=null){
                final IExplorerTreeNode parentNode = node.getParentNode();
                final int index = parentNode==null ? -1 : parentNode.getChildNodes().indexOf(node);
                treeNode.remove();
                if (index>=0 && parentNode instanceof ExplorerTreeNode){
                    ((ExplorerTreeNode)parentNode).removeNode(index);
                }
            }
        }

        @Override
        public boolean isNodeExists(final IExplorerTreeNode node) {
            return tree.findNodeByExplorerTreeNode(node)!=null;
        }

        @Override
        public void setCurrent(IExplorerTreeNode node) {
            final Node treeNode = tree.findNodeByExplorerTreeNode(node);
            if (treeNode!=null){
                tree.internalChangeCurrent = true;
                try{
                    tree.setSelectedNode(treeNode);
                }finally{
                    tree.internalChangeCurrent = false;
                }
            }
        }

        @Override
        public void resizeToContents() {
            
        }

        @Override
        public void scrollTo(final IExplorerTreeNode node) {
            
        }

        @Override
        public Action createAction(final Icon icon, final String title) {
            return new RwtAction(icon, title);
        }                
    }
    
    private List<Id> accessibleExplorerItems;
    private ExplorerRoot explorerRoot;
    private final ViewManager viewManager;
    private final IClientEnvironment userSession;
    private final WpsEnvironment e;
    private final List<IExplorerTreeNode> invisibleNodes = new LinkedList<>();
    private final ExplorerTreeController controller;
    private final IMainView mainView;
    private boolean internalChangeCurrent;
    private String initNodes = "";
    private EnumMap<TreeNodeSettings.NodeType,TreeNodeSettings> currentNodeSettings;
            
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            applySettings();
        }
    };    

    WpsTree(final WpsEnvironment userSession, 
                  final IMainView mainView, 
                  final IMenu selectorMenu, 
                  final IMenu editorMenu) {
        this.userSession = userSession;
        this.e = userSession;
        html.setAttr("role", "wpsTree");
        viewManager = new ViewManager(userSession, selectorMenu, editorMenu, mainView);
        controller = new ExplorerTreeController(new Presenter(this));
        controller.setViewManager(viewManager);
        this.mainView = mainView;
        ((NavigationView) mainView.getNavigator()).add(WpsTree.this);
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        showHeader(false);
        addSelectionListener(new NodeListener() {
            @Override
            public void selectionChanged(Node oldSelection, Node newSelection) {
                if (!internalChangeCurrent){
                    if (oldSelection != newSelection) {
                        if (WebServerRunParams.restoreTreePosition()) {
                            saveNode(newSelection);
                        }
                        if (newSelection instanceof TreeNode) {                        
                            final ExplorerTreeNode treeNode = ((TreeNode) newSelection).getExplorerTreeNode();                                            
                            controller.enterNode(treeNode, true);
                        }
                    }                
                    controller.refreshActions();
                }
            }
        });
        applyBackgroundColorSettings();
        currentNodeSettings = TreeNodeSettings.readSettings(getSettings());
        userSession.addSettingsChangeListener(l);
    }

    private void saveNode(final Node node) {
        WpsSettings treeSettings = getSettings();

        final List<Node> nodes = node.getPath();
        final StringBuilder pathBuilder = new StringBuilder();
        if (!initNodes.isEmpty()) {
            pathBuilder.append(initNodes);
            pathBuilder.append(" ");
        }
        for (Node curNode : nodes) {
            if (curNode instanceof TreeNode) {
                final IExplorerTreeNode explorerTreeNode = ((TreeNode) curNode).getExplorerTreeNode();
                if (explorerTreeNode != null && explorerTreeNode.isValid() && !explorerTreeNode.getView().isEntityView()) {
                    if (curNode != nodes.get(0)) {
                        pathBuilder.append("/");
                    }
                    pathBuilder.append(explorerTreeNode.getView().getExplorerItemId().toString());
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        final String result = pathBuilder.toString();
        e.writeToCookies("initNodes", result);
        treeSettings.beginGroup("ExplorerTree");
        treeSettings.writeString("initialNodes", result);
        treeSettings.endGroup();
    }

    boolean close(final boolean forced) {
        if (controller.canLeaveNode(forced,null) || forced) {
            ((NavigationView) mainView.getNavigator()).updateToolBarState(new LinkedList<Action>());
            viewManager.closeAll();            
            store();
            controller.close();
            if (l != null) {
                e.removeSettingsChangeListener(l);
            }
            return true;
        }
        return false;
    }

    public void removeCurrentNode() {
        internalChangeCurrent = true;
        try{
            controller.removeCurrentNode();
        }finally{
            internalChangeCurrent  = false;
        }
    }
    
    public final void goToNode(final IExplorerTreeNode node){
        if (node!=null){
            controller.ensurePathExpanded(node);
        }
    }

    void open(final ExplorerRoot root, final List<Id> visibleExplorerItems, final ExplorerRoot explorerRoot) {
        this.accessibleExplorerItems = new ArrayList<>(visibleExplorerItems);
        this.explorerRoot = explorerRoot;
        final RadParagraphDef rootParagraph = userSession.getDefManager().getParagraphDef(root.getId());
        final IExplorerTreeNode rootNode =  new RootParagraphNode(this, rootParagraph);        
        final TreeNode rootTreeNode = new TreeNode(this, rootNode);
        setRootVisible(true);
        setRootNode(rootTreeNode);
        final List<IExplorerTreeNode> iterable = new ArrayList<>(invisibleNodes);
        for (IExplorerTreeNode node : iterable) {
            setNodeVisible(node, false);
        }
        invisibleNodes.clear();
        TreeNode initialNode = rootTreeNode;
        if (WebServerRunParams.restoreTreePosition()) {
            initialNode = restoreNode(rootParagraph.getId(), rootTreeNode);
        }
        setSelectedNode(initialNode);
        rootTreeNode.expand();

        final RootPanel rootWindow = findRoot();
        if (rootWindow != null) {
            rootWindow.setIcon(rootTreeNode.getIcon());
        }
        restore();
    }

    private TreeNode restoreNode(final Id rootParagraphId, final TreeNode rootNode) {
        WpsSettings treeSettings = getSettings();
        treeSettings.beginGroup("ExplorerTree");
        final String savedInitialNodes = treeSettings.readString("initialNodes", "");
        treeSettings.endGroup();
        if (savedInitialNodes.isEmpty()) {
            return rootNode;
        }
        final StringTokenizer pathTokenizer = new StringTokenizer(savedInitialNodes, " ");
        TreeNode node = rootNode;
        final StringBuilder initNodesBuilder = new StringBuilder();
        do {
            final String path = pathTokenizer.nextToken();
            final StringTokenizer IdsTokenizer = new StringTokenizer(path, "/");
            if (rootParagraphId.toString().equals(IdsTokenizer.nextToken())) {
                boolean nextNodeFound = true;
                while (IdsTokenizer.hasMoreTokens() && nextNodeFound) {
                    final Id nextNodeId = Id.Factory.loadFrom(IdsTokenizer.nextToken());
                    nextNodeFound = false;
                    node.expand();
                    for (Node c : node.getChildNodes().getNodes()) {
                        if (c instanceof TreeNode) {
                            final IExplorerTreeNode explorerTreeNode = ((TreeNode) c).getExplorerTreeNode();
                            if (explorerTreeNode != null && explorerTreeNode.isValid()
                                    && nextNodeId.equals(explorerTreeNode.getView().getExplorerItemId())) {
                                node = (TreeNode) c;
                                nextNodeFound = true;
                                break;
                            }
                        }
                    }
                }
            } else {
                if (initNodesBuilder.length() > 0) {
                    initNodesBuilder.append(" ");
                }
                initNodesBuilder.append(path);
            }
        } while (pathTokenizer.hasMoreTokens());
        initNodes = initNodesBuilder.toString();
        return node;
    }

    @Override
    public IExplorerTree.Actions getActions() {
        return controller.getActions();
    }

    @Override
    public void setNodeVisible(final IExplorerTreeNode node, boolean isVisible) {
        final Node treeNode = findNodeByExplorerTreeNode(node);
        if (treeNode != null) {
            treeNode.setVisible(isVisible);
        } else if (!isVisible) {
            invisibleNodes.add(node);
        }
    }

    @Override
    public boolean isNodeVisible(final IExplorerTreeNode node) {
        final Node treeNode = findNodeByExplorerTreeNode(node);
        if (treeNode != null) {
            return treeNode.isVisible();
        } else {
            return invisibleNodes.contains(node);
        }
    }

    @Override
    public void update(final IExplorerTreeNode node) {
        Node treeNode = findNodeByExplorerTreeNode(node);
        if (treeNode != null) {
            treeNode.update();
        }
    }
    
    TreeNodeSettings getNodeSettings(final TreeNodeSettings.NodeType nodeType){
        return currentNodeSettings.get(nodeType);
    }

    private WpsSettings getSettings() {
        return ((WpsEnvironment) getEnvironment()).getConfigStore();
    }

    private void applySettings() {
        applyBackgroundColorSettings();
        final EnumMap<TreeNodeSettings.NodeType,TreeNodeSettings> newSettings = 
            TreeNodeSettings.readSettings(getSettings());
        if (!Objects.equals(currentNodeSettings, newSettings)){
            final Stack<Node> nodes = new Stack<>();
            nodes.push(getRootNode());
            Node currentNode;
            TreeNodeSettings.NodeType nodeType;
            while(!nodes.isEmpty()){
                currentNode = nodes.pop();
                if (currentNode instanceof TreeNode){
                    nodeType = ((TreeNode)currentNode).getType();
                    ((TreeNode)currentNode).applySettings(newSettings.get(nodeType));
                }
                nodes.addAll(currentNode.getChildNodes().getCreatedNodes());
            }
            currentNodeSettings = newSettings;
        }        
    }

    private void restoreExpandedItems(final Collection<IExplorerTreeNode> rootNodes, final String settingsPath) {
        final WpsSettings treeSettings = (WpsSettings) getEnvironment().getConfigStore();
        treeSettings.beginGroup(settingsPath);
        final int nodesCount = treeSettings.beginReadArray("expandedNodes");
        try {
            List<Id> expandedExplorerItems;
            Collection<IExplorerTreeNode> nodes;
            IExplorerTreeNode currentNode = null;
            for (int i = 0; i < nodesCount; i++) {
                treeSettings.setArrayIndex(i);
                expandedExplorerItems = parseIds(treeSettings.readString("explorerItemIds"));
                for (Id explorerItemId : expandedExplorerItems) {
                    nodes = currentNode == null ? rootNodes : currentNode.getChildNodes();
                    currentNode = findNodeByExplorerItemId(nodes, explorerItemId);
                    if (currentNode != null) {
                        currentNode.getView().expand();
                    } else {
                        break;
                    }
                }
            }
        } finally {
            treeSettings.endArray();
            treeSettings.endGroup();
        }
    }

    private void traceError(final String message, final Throwable error) {
        final ClientTracer tracer = e.getTracer();
        if (error == null) {
            final String msg = e.getMessageProvider().translate("ExplorerTree", "Cannot restore position in explorer tree: %s");
            tracer.warning(String.format(msg, message));
        } else if (message == null) {
            final String msg = e.getMessageProvider().translate("ExplorerTree", "Cannot restore position in explorer tree: %s\n%s");
            tracer.warning(String.format(msg, ClientException.getExceptionReason(e.getMessageProvider(), error), ClientException.exceptionStackToString(error)));
        } else {
            final String msg = e.getMessageProvider().translate("ExplorerTree", "Cannot restore position in explorer tree: %s\n%s");
            tracer.warning(String.format(msg, message, ClientException.exceptionStackToString(error)));
        }
    }        

    private void applyBackgroundColorSettings() {
        WpsSettings treeSettings = getSettings();
        if (treeSettings != null) {
            treeSettings.beginGroup(SettingNames.SYSTEM);
            treeSettings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            treeSettings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);            
            try {
                final String color = 
                    treeSettings.readString(SettingNames.ExplorerTree.Common.TREE_BACKGROUND);
                if (color != null) {
                    this.setBackgroundColor(color);
                }
            } finally {
                treeSettings.endGroup();
                treeSettings.endGroup();
                treeSettings.endGroup();
            }
        }
    }

    private static List<Id> parseIds(final String idsAsStr) {
        final List<Id> ids = new ArrayList<>();
        if (idsAsStr != null && !idsAsStr.isEmpty()) {
            final ArrStr arr = ArrStr.fromValAsStr(idsAsStr);
            for (String idAsStr : arr) {
                ids.add(Id.Factory.loadFrom(idAsStr));
            }
        }
        return ids;
    }

    @Override
    public void lock() {
    }

    @Override
    public void unlock() {
    }

    @Override
    public IViewManager getViewManager() {
        return viewManager;
    }

    private boolean isCurrentOrParentForCurrent(final Node nodeToCheck) {
        for (Node node = getSelectedNode(); node != null; node = node.getParentNode()) {
            if (node == nodeToCheck) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeNode(final IExplorerTreeNode node) {
        internalChangeCurrent = true;
        try{
            controller.removeNode(node);
        }finally{
            internalChangeCurrent = false;
        }
    }

    @Override
    public IExplorerTreeNode addChoosenEntity(ExplorerTreeNode parent, EntityModel entity, int index) {
        Node parentNode = findNodeByExplorerTreeNode(parent);
        if (parentNode != null) {
            ChoosenEntityNode choosenNode = new ChoosenEntityNode(this, entity, parent);
            //init child nodes
            choosenNode.getChildNodes();
            ((TreeNode) parentNode).add(index, choosenNode);
            controller.refreshActions();
            return choosenNode;
        } else {
            return null;
        }
    }

    @Override
    public IExplorerTreeNode addUserExplorerItem(final ExplorerTreeNode parentNode, final RadExplorerItemDef userItem, final int index) {
        final Node treeNode = findNodeByExplorerTreeNode(parentNode);
        if (treeNode != null) {
            final ExplorerItemNode newNode = new ExplorerItemNode(this, parentNode, userItem.getId());            
            ((TreeNode) treeNode).add(index, newNode);
            return newNode;
        } else {
            return null;
        }
    }

    @Override
    public boolean setCurrent(final IExplorerTreeNode node) {
        internalChangeCurrent = true;
        try{
            return controller.setCurrent(node);
        }finally{
            internalChangeCurrent = false;
        }
    }

    @Override
    public IExplorerTreeNode getCurrent() {
        Node node = getSelectedNode();
        if (node instanceof TreeNode) {
            return ((TreeNode) node).getExplorerTreeNode();
        }
        return null;
    }
    
    @Override
    protected boolean onChangeSelection(Node oldSelection, Node newSelection) {  
        if (oldSelection==null){
            return true;
        }
        final IExplorerTreeNode nextNode;
        if (newSelection instanceof TreeNode) {
            nextNode = ((TreeNode) newSelection).getExplorerTreeNode();        
        }else{
            nextNode = null;
        }
        return controller.canLeaveNode(false,nextNode);
    }

    @Override
    public IExplorerTreeNode findNodeByExplorerItemId(Id explorerItemId) {
        if (getRootNode() == null) {
            return null;
        }
        return findNodeByExplorerItemId(getRootNode(), explorerItemId);
    }

    @Override
    public IExplorerTreeNode findNodeByPosition(int xPos, int yPos) {
        return null;
    }

    @Override
    public void expand(final IExplorerTreeNode node) {
        for (Node treeNode = findNodeByExplorerTreeNode(node); treeNode!=null; treeNode = treeNode.getParentNode()){
            treeNode.expand();
        }
    }

    @Override
    public void collapse(IExplorerTreeNode node) {
        Node treeNode = findNodeByExplorerTreeNode(node);
        if (treeNode != null) {
            treeNode.collapse();
        }
    }

    @Override
    public boolean isExpanded(IExplorerTreeNode node) {
        Node treeNode = findNodeByExplorerTreeNode(node);
        if (treeNode != null) {
            return treeNode.isExpanded();
        } else {
            return false;
        }
    }

    @Override
    public List<IExplorerTreeNode> getRootNodes() {
        if (getRootNode() != null) {
            return Collections.<IExplorerTreeNode>singletonList(((TreeNode) getRootNode()).getExplorerTreeNode());
        } else {
            return Collections.emptyList();
        }

    }

    private ExplorerTreeNode getExplorerRoot() {
        if (getRootNode() instanceof TreeNode) {
            return ((TreeNode) getRootNode()).getExplorerTreeNode();
        } else {
            return null;
        }
    }

    private Node findNodeByExplorerTreeNode(IExplorerTreeNode node) {
        if (getRootNode() == null) {
            return null;
        }
        return findNodeByExplorerTreeNode(getRootNode(), node);
    }

    private Node findNodeByExplorerTreeNode(Node current, IExplorerTreeNode node) {
        if (current instanceof TreeNode) {
            IExplorerTreeNode cn = ((TreeNode) current).getExplorerTreeNode();
            if (cn == node) {
                return current;
            }
        }
        for (Node c : current.getChildNodes().getNodes()) {
            Node result = findNodeByExplorerTreeNode(c, node);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private IExplorerTreeNode findNodeByExplorerItemId(Node current, Id id) {
        if (current instanceof TreeNode) {
            ExplorerTreeNode cn = ((TreeNode) current).getExplorerTreeNode();
            if (cn.getExplorerItemId() == id) {
                return cn;
            }
        }
        for (Node c : current.getChildNodes().getNodes()) {
            IExplorerTreeNode result = findNodeByExplorerItemId(c, id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static IExplorerTreeNode findNodeByExplorerItemId(final Collection<IExplorerTreeNode> nodes, final Id explorerItemId) {
        for (IExplorerTreeNode node : nodes) {
            if (node.isValid() && explorerItemId.equals(node.getView().getExplorerItemId())) {
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean isExplorerItemAccessible(Id explorerItemId) {
        return accessibleExplorerItems.contains(explorerItemId);
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return userSession;
    }

    private boolean needToRestorePosition() {
        WpsSettings treeSettings = getSettings();
        if (RunParams.needToRestoreContext()) {
            return true;
        } else {
            treeSettings.beginGroup(SettingNames.SYSTEM);
            treeSettings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            treeSettings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            try {
                return treeSettings.readBoolean(SettingNames.ExplorerTree.Common.RESTORE_POSITION, false);
            } finally {
                treeSettings.endGroup();
                treeSettings.endGroup();
                treeSettings.endGroup();
            }
        }
    }

    public final void refill(final IProgressHandle progress, final boolean relogin) {
        if (relogin) {
            if (progress != null) {
                progress.setText(getEnvironment().getMessageProvider().translate("ExplorerTree", "Updating list of accessible explorer items"));
            }
            accessibleExplorerItems.clear();
            try {
                accessibleExplorerItems.addAll(explorerRoot.getVisibleExplorerItems());
            } catch (ServiceClientException | InterruptedException exception) {
                final String errMessage = getEnvironment().getMessageProvider().translate("ExplorerError", "Can't get a list of accessible explorer items: %s\n%s");
                final String errReason = ClientException.getExceptionReason(userSession.getMessageProvider(), exception);
                final String errStack = ClientException.exceptionStackToString(exception);
                userSession.getTracer().error(String.format(errMessage, errReason, errStack));
                accessibleExplorerItems.add(getExplorerRoot().getOwnerDefinitionId());
            }
        }
        if (progress != null) {
            progress.setText(getEnvironment().getMessageProvider().translate("ExplorerTree", "Preparing for Update"));
        }
        final ArrayList<Id[]> nodesToExpand = new ArrayList<>();
        Node selection = getSelectedNode();
        int[] currentNodePath = selection == null ? null : selection.getIndexPath();
        controller.clearHistory();
        List<int[]> expandedPathes = new LinkedList<>();
        fillExpandedPathes(getRootNode(), expandedPathes);
        setVisible(false);
        try {
            reinit(progress);
        } finally {
            setVisible(true);
        }

        if (progress != null) {
            progress.setText(getEnvironment().getMessageProvider().translate("ExplorerTree", "Finishing Update"));
            progress.setMaximumValue(nodesToExpand.size());
        }
        nodesToExpand.clear();

        getRootNode().getChildNodes().reset();

        for (int[] path : expandedPathes) {
            Node node = findNodeByPath(path);
            if (node != null) {
                expandNode(node);
            }
        }
        expandedPathes.clear();
        setSelectedNode(null);
        if (currentNodePath != null) {
            selection = findNodeByPath(currentNodePath);
            setSelectedNode(selection);
        }
    }

    private void fillExpandedPathes(Node root, List<int[]> expandedPathes) {
        if (root.isExpanded()) {
            expandedPathes.add(root.getIndexPath());
        }
        for (Node node : root.getChildNodes().getCreatedNodes()) {
            fillExpandedPathes(node, expandedPathes);
        }
    }

    private void reinit(IProgressHandle progress) {

        final IExplorerTreeNode rootNode = getExplorerRoot();
        final List<IExplorerTreeNode> nodesToView = new ArrayList<>();

        nodesToView.add(rootNode);
        nodesToView.addAll(rootNode.getChildNodesRecursively());
        if (progress != null) {
            progress.setText(userSession.getMessageProvider().translate("ExplorerTree", "Updating Explorer Items"));
            progress.setMaximumValue(nodesToView.size());
        }
        int counter = 0;
        for (IExplorerTreeNode currentNode : nodesToView) {
            if (progress != null) {
                progress.setValue(++counter);
            }
            currentNode.getView();//reinitNode
        }
        if (progress != null) {
            progress.setValue(0);
            progress.setMaximumValue(0);
        }
    }

    public void store() {
        WpsSettings treeSettings = getSettings();
        if (!needToRestorePosition()) {
            return;
        }
        final List<IExplorerTreeNode> path = new ArrayList<>();
        final IExplorerTreeNode currentNode = this.getCurrent();
        for (IExplorerTreeNode node = currentNode; node != null; node = node.getParentNode()) {
            path.add(0, node);
        }
        treeSettings.beginGroup(SettingNames.SYSTEM);
        treeSettings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        treeSettings.beginGroup(SettingNames.ExplorerTree.Common.STATE);
        treeSettings.remove("");
        try {
            saveCurrentNodePath(path);
            saveExpandedItems();
        } finally {
            treeSettings.endGroup();
            treeSettings.endGroup();
            treeSettings.endGroup();
        }
    }

    public void restore() {
        if (!needToRestorePosition()) {
            return;
        }
        final IProgressHandle progress = getEnvironment().getProgressHandleManager().newStandardProgressHandle();
        progress.startProgress(getEnvironment().getMessageProvider().translate("ExplorerTree", "Restoring position"), true);
        final StringBuilder settingsPath = new StringBuilder();
        settingsPath.append(SettingNames.SYSTEM);
        settingsPath.append("/");
        settingsPath.append(SettingNames.EXPLORER_TREE_GROUP);
        settingsPath.append("/");
        settingsPath.append(SettingNames.ExplorerTree.Common.STATE);
        final String explorerTreeStateSettings = settingsPath.toString();
        settingsPath.append("/currentNode/");
        final IExplorerTreeNode currentNode;
        try {
            currentNode = restoreCurrentNode(getRootNodes(), progress, settingsPath.toString());
            restoreExpandedItems(getRootNodes(), explorerTreeStateSettings);
        } finally {
            RunParams.removeRestoringContextParam();
            progress.finishProgress();
        }

        if (currentNode != null) {
            setCurrent(currentNode);
        }
    }

    private void saveCurrentNodePath(final List<IExplorerTreeNode> path) {
        WpsSettings treeSettings = getSettings();
        treeSettings.beginWriteArray("currentNode");
        try {
            int index = 0;
            final ArrStr expandedExplorerItems = new ArrStr();
            for (IExplorerTreeNode node : path) {
                if (!node.isValid()) {
                    break;
                }
                if (node.getView().getExplorerItemId() != null) {
                    treeSettings.setArrayIndex(index);
                    treeSettings.writeId("explorerItemId", node.getView().getExplorerItemId());
                } else {
                    final ExplorerItemView.EntityInfo info = ((ExplorerItemView) node.getView()).getChoosenEntityInfo();
                    if (info.context instanceof IContext.ChoosenEntityEditing) {
                        treeSettings.setArrayIndex(index);
                        ((IContext.ChoosenEntityEditing) info.context).writeToSettings(treeSettings);
                    } else if (info.context instanceof IContext.ReferencedChoosenEntityEditing) {
                        treeSettings.setArrayIndex(index);
                        ((IContext.ReferencedChoosenEntityEditing) info.context).writeToSettings(treeSettings);
                    } else {
                        break;//cant restore other context type
                    }
                    treeSettings.writeId("presentationClassId", info.presentationClassId);
                    treeSettings.writeId("presentationId", info.presentationId);
                    treeSettings.writePid("entityPid", info.pid);
                }
                if (isExpanded(node)) {
                    treeSettings.writeBoolean("isExpanded", true);
                    expandedExplorerItems.clear();
                    for (IExplorerTreeNode childNode : node.getChildNodes()) {
                        if (childNode.isValid() && isExpanded(childNode) && childNode.getView().getExplorerItemId() != null) {
                            expandedExplorerItems.add(childNode.getView().getExplorerItemId().toString());
                        }
                    }
                    if (!expandedExplorerItems.isEmpty()) {
                        treeSettings.writeString("expandedNodes", expandedExplorerItems.toString());
                    }
                }
                index++;
            }
        } finally {
            treeSettings.endArray();
        }
    }

    private IExplorerTreeNode restoreCurrentNode(final Collection<IExplorerTreeNode> rootNodes,
            final IProgressHandle progress,
            final String settingsPath) {
        IExplorerTreeNode currentNode = null, node = null;
        Collection<IExplorerTreeNode> nodes;
        List<Id> expandedNodeIds;
        Id explorerItemId, presentationClassId, presentationId;
        Pid entityPid;
        WpsSettings treeSettings = getSettings();
        final int nodesCount = treeSettings.readInteger(settingsPath + "size", -1);
        String nodeSettings;
        for (int i = 0; i < nodesCount && !progress.wasCanceled(); i++) {
            nodeSettings = settingsPath + Integer.toString(i);
            if (treeSettings.contains(nodeSettings + "/explorerItemId")) {
                nodes = currentNode == null ? rootNodes : currentNode.getChildNodes();
                explorerItemId = treeSettings.readId(nodeSettings + "/explorerItemId");
                node = findNodeByExplorerItemId(nodes, explorerItemId);
                if (node == null) {
                    break;
                }
            } else {
                final IContext.Entity context;
                treeSettings.beginGroup(nodeSettings);
                try {
                    context = IContext.Entity.Factory.readFromSettings(e, treeSettings);
                } catch (Exception ex) {
                    traceError(null, ex);
                    break;
                } finally {
                    treeSettings.endGroup();
                }
                presentationClassId = treeSettings.readId(nodeSettings + "/presentationClassId");
                if (presentationClassId == null) {
                    traceError("Cannot read presentationClassId", null);
                    break;
                }
                presentationId = treeSettings.readId(nodeSettings + "/presentationId");
                if (presentationId == null) {
                    traceError("Cannot read presentationId", null);
                    break;
                }
                entityPid = treeSettings.readPid(nodeSettings + "/entityPid");
                if (entityPid == null) {
                    traceError("Cannot read entityPid", null);
                    break;
                }
                final EntityModel entity;
                try {
                    entity = EntityModel.openModel(entityPid, presentationClassId, Collections.singletonList(presentationId), context);
                    if (currentNode != null) {
                        node = currentNode.getExplorerTree().addChoosenEntity((ExplorerTreeNode) currentNode, entity, 0);
                    }
                } catch (ServiceCallFault fault) {
                    final org.radixware.schemas.eas.ExceptionEnum.Enum faultType
                            = org.radixware.schemas.eas.ExceptionEnum.Enum.forString(fault.getFaultString());
                    if (faultType != org.radixware.schemas.eas.ExceptionEnum.OBJECT_NOT_FOUND) {
                        traceError(null, fault);
                    }
                    break;
                } catch (ServiceClientException exception) {
                    traceError(null, exception);
                    break;
                } catch (InterruptedException exception) {
                    break;
                }
            }
            currentNode = node;
            if (currentNode != null && treeSettings.readBoolean(nodeSettings + "/isExpanded", false)) {
                currentNode.getView().expand();
                expandedNodeIds = parseIds(treeSettings.readString(nodeSettings + "/expandedNodes"));
                IExplorerTreeNode expandedNode;
                for (Id expandedNodeId : expandedNodeIds) {
                    expandedNode = findNodeByExplorerItemId(currentNode.getChildNodes(), expandedNodeId);
                    if (expandedNode != null) {
                        expandedNode.getView().expand();
                    }
                }
            }
        }
        return currentNode;
    }

    private void saveExpandedItems() {
        final WpsSettings settings = (WpsSettings) getEnvironment().getConfigStore();
        settings.beginWriteArray("expandedNodes");
        try {
            final Collection<IExplorerTreeNode> rootNodes = getRootNodes();
            final Stack<IExplorerTreeNode> stack = new Stack<>();
            final List<Id> savedExplorerItems = new ArrayList<>();
            final ArrStr explorerItemsToSave = new ArrStr();
            int arrayIndex = 0;
            boolean childItemIsExpanded, newItemInStack;
            for (IExplorerTreeNode rootNode : rootNodes) {
                if (rootNode.isValid() && isExpanded(rootNode) && rootNode.getView().getExplorerItemId() != null) {
                    stack.push(rootNode);
                    newItemInStack = true;
                    savedExplorerItems.clear();

                    while (!stack.isEmpty()) {
                        childItemIsExpanded = false;
                        for (IExplorerTreeNode node : stack.peek().getChildNodes()) {
                            if (node.isValid() && isExpanded(node)
                                    && node.getView().getExplorerItemId() != null
                                    && !savedExplorerItems.contains(node.getView().getExplorerItemId())) {
                                stack.push(node);
                                childItemIsExpanded = true;
                                newItemInStack = true;
                                break;
                            }
                        }
                        if (!childItemIsExpanded) {//Если ни один из дочерних не раскрыт
                            //сохранение содержимого стека
                            if (newItemInStack) {
                                Id explorerItemId;
                                explorerItemsToSave.clear();
                                for (IExplorerTreeNode node : stack) {
                                    explorerItemId = node.getView().getExplorerItemId();
                                    savedExplorerItems.add(explorerItemId);
                                    explorerItemsToSave.add(explorerItemId.toString());
                                }
                                settings.setArrayIndex(arrayIndex);
                                settings.writeString("explorerItemIds", explorerItemsToSave.toString());
                                arrayIndex++;
                                newItemInStack = false;
                            }
                            stack.pop();
                        }
                    }//while(!stack.isEmpty())

                }
            }
        } finally {
            settings.endArray();
        }
    }

    @Override
    public org.radixware.schemas.clientstate.ExplorerTreeState writeStateToXml() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void restoreStateFromXml(org.radixware.schemas.clientstate.ExplorerTreeState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
}
