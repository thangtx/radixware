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

package org.radixware.kernel.explorer.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.nodes.ExplorerItemNode;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;


class ExplorerTreeSettings {

    private final ClientSettings settings;
    private final IExplorerTree tree;

    ExplorerTreeSettings(final ClientSettings configStore, final IExplorerTree tree) {
        settings = configStore;
        this.tree = tree;
    }

    public void store() {
        if (!needToRestorePosition()) {
            return;
        }
        final List<IExplorerTreeNode> path = new ArrayList<IExplorerTreeNode>();
        final IExplorerTreeNode currentNode = tree.getCurrent();
        for (IExplorerTreeNode node = currentNode; node != null; node = node.getParentNode()) {
            path.add(0, node);
        }
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        settings.beginGroup(SettingNames.ExplorerTree.Common.STATE);
        settings.remove("");
        try {
            saveCurrentNodePath(tree, path);
            saveExpandedItems(tree);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }

    private void saveCurrentNodePath(final IExplorerTree tree, final List<IExplorerTreeNode> path) {
        settings.beginWriteArray("currentNode");
        try {
            int index = 0;
            final ArrStr expandedExplorerItems = new ArrStr();
            for (IExplorerTreeNode node : path) {
                if (!node.isValid()) {
                    break;
                }
                if (node.getView().getExplorerItemId() != null) {
                    settings.setArrayIndex(index);
                    settings.writeId("explorerItemId", node.getView().getExplorerItemId());
                } else {
                    final ExplorerItemView.EntityInfo info = ((ExplorerItemView) node.getView()).getChoosenEntityInfo();
                    if (info.context instanceof IContext.ChoosenEntityEditing) {
                        settings.setArrayIndex(index);
                        ((IContext.ChoosenEntityEditing) info.context).writeToSettings(settings);
                    } else if (info.context instanceof IContext.ReferencedChoosenEntityEditing) {
                        settings.setArrayIndex(index);
                        ((IContext.ReferencedChoosenEntityEditing) info.context).writeToSettings(settings);
                    } else {
                        break;//cant restore other context type
                    }
                    settings.writeId("presentationClassId", info.presentationClassId);
                    settings.writeId("presentationId", info.presentationId);
                    settings.writePid("entityPid", info.pid);
                }

                if (tree.isExpanded(node)) {
                    settings.writeBoolean("isExpanded", true);
                    expandedExplorerItems.clear();
                    for (IExplorerTreeNode childNode : node.getChildNodes()) {
                        if (childNode.isValid() && tree.isExpanded(childNode) && childNode.getView().getExplorerItemId() != null) {
                            expandedExplorerItems.add(childNode.getView().getExplorerItemId().toString());
                        }
                    }
                    if (!expandedExplorerItems.isEmpty()) {
                        settings.writeString("expandedNodes", expandedExplorerItems.toString());
                    }
                }

                index++;
            }
        } finally {
            settings.endArray();
        }
    }

    private void saveExpandedItems(final IExplorerTree tree) {
        settings.beginWriteArray("expandedNodes");
        try {
            final Collection<IExplorerTreeNode> rootNodes = tree.getRootNodes();
            final Stack<IExplorerTreeNode> stack = new Stack<IExplorerTreeNode>();
            final List<Id> savedExplorerItems = new ArrayList<Id>();
            final ArrStr explorerItemsToSave = new ArrStr();
            int arrayIndex = 0;
            boolean childItemIsExpanded, newItemInStack;
            for (IExplorerTreeNode rootNode : rootNodes) {
                if (rootNode.isValid() && tree.isExpanded(rootNode) && rootNode.getView().getExplorerItemId() != null) {
                    stack.push(rootNode);
                    newItemInStack = true;
                    savedExplorerItems.clear();

                    while (!stack.isEmpty()) {
                        childItemIsExpanded = false;
                        for (IExplorerTreeNode node : stack.peek().getChildNodes()) {
                            if (node.isValid() && tree.isExpanded(node)
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
            }//for (IExplorerTreeNode rootNode: rootNodes)
        } finally {
            settings.endArray();
        }
    }

    public void restore() {
        if (!needToRestorePosition()) {
            return;
        }
        final IProgressHandle progress = tree.getEnvironment().getProgressHandleManager().newStandardProgressHandle();

        progress.startProgress(Application.translate("ExplorerTree", "Restoring position"), true);
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
            currentNode = restoreCurrentNode(tree.getRootNodes(), progress, settingsPath.toString());
            restoreExpandedItems(tree.getRootNodes(), explorerTreeStateSettings);
        } finally {
            RunParams.removeRestoringContextParam();
            progress.finishProgress();
        }

        if (currentNode != null) {
            tree.setCurrent(currentNode);
        }
    }

    private boolean needToRestorePosition() {
        if (RunParams.needToRestoreContext()) {
            return true;
        } else {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            try {
                return settings.readBoolean(SettingNames.ExplorerTree.Common.RESTORE_POSITION, false);
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
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
                        
        final int nodesCount = settings.readInteger(settingsPath+"size", -1);        
        String nodeSettings;
        for (int i = 1; i <= nodesCount && !progress.wasCanceled(); i++) {
            nodeSettings=settingsPath+Integer.toString(i);
            if (settings.contains(nodeSettings+"/explorerItemId")) {
                nodes = currentNode == null ? rootNodes : currentNode.getChildNodes();
                explorerItemId = settings.readId(nodeSettings+"/explorerItemId");
                node = findNodeByExplorerItemId(nodes, explorerItemId);
                if (node == null) {
                    break;
                }
            } else {
                final IContext.Entity context;
                settings.beginGroup(nodeSettings);
                try {
                    context = IContext.Entity.Factory.readFromSettings(tree.getEnvironment(), settings);
                } catch (Exception ex) {
                    traceError(null, ex);
                    break;
                } finally{
                    settings.endGroup();
                }
                presentationClassId = settings.readId(nodeSettings+"/presentationClassId");
                if (presentationClassId == null) {
                    traceError("Cannot read presentationClassId", null);
                    break;
                }
                presentationId = settings.readId(nodeSettings+"/presentationId");
                if (presentationId == null) {
                    traceError("Cannot read presentationId", null);
                    break;
                }
                entityPid = settings.readPid(nodeSettings+"/entityPid");
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
                    final org.radixware.schemas.eas.ExceptionEnum.Enum faultType =
                            org.radixware.schemas.eas.ExceptionEnum.Enum.forString(fault.getFaultString());
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
            if (currentNode != null && settings.readBoolean(nodeSettings+"/isExpanded", false)) {
                currentNode.getView().expand();
                expandedNodeIds = parseIds(settings.readString(nodeSettings+"/expandedNodes"));
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

    private void restoreExpandedItems(final Collection<IExplorerTreeNode> rootNodes, final String settingsPath) {
        settings.beginGroup(settingsPath);
        final int nodesCount = settings.beginReadArray("expandedNodes");
        try {
            List<Id> expandedExplorerItems;
            Collection<IExplorerTreeNode> nodes;
            IExplorerTreeNode currentNode = null;
            for (int i = 0; i < nodesCount; i++) {
                settings.setArrayIndex(i);
                expandedExplorerItems = parseIds(settings.readString("explorerItemIds"));
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
            settings.endArray();
            settings.endGroup();
        }
    }

    private void traceError(final String message, final Throwable error) {
        final ClientTracer tracer = tree.getEnvironment().getTracer();
        if (error == null) {
            final String msg = Application.translate("ExplorerTree", "Cannot restore position in explorer tree: %s");
            tracer.warning(String.format(msg, message));
        } else if (message == null) {
            final String msg = Application.translate("ExplorerTree", "Cannot restore position in explorer tree: %s\n%s");
            tracer.warning(String.format(msg, ClientException.getExceptionReason(tree.getEnvironment().getMessageProvider(), error), ClientException.exceptionStackToString(error)));
        } else {
            final String msg = Application.translate("ExplorerTree", "Cannot restore position in explorer tree: %s\n%s");
            tracer.warning(String.format(msg, message, ClientException.exceptionStackToString(error)));
        }
    }

    private static IExplorerTreeNode findNodeByExplorerItemId(final Collection<IExplorerTreeNode> nodes, final Id explorerItemId) {
        for (IExplorerTreeNode node : nodes) {
            if (node.isValid() && explorerItemId.equals(node.getView().getExplorerItemId())) {
                return node;
            }
        }
        return null;
    }

    private static List<Id> parseIds(final String idsAsStr) {
        final List<Id> ids = new ArrayList<Id>();
        if (idsAsStr != null && !idsAsStr.isEmpty()) {
            final ArrStr arr = ArrStr.fromValAsStr(idsAsStr);
            for (String idAsStr : arr) {
                ids.add(Id.Factory.loadFrom(idAsStr));
            }
        }
        return ids;
    }
}