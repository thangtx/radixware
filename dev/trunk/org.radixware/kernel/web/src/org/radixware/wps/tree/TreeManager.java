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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.BrokenConnectionError;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.tree.nodes.ChoosenEntityNode;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.views.NavigationView;
import org.radixware.wps.views.RwtAction;


public class TreeManager implements IExplorerTreeManager {

    public static interface IUpdateVersionController {

        boolean prepareNewVersion(Collection<Id> changedDefinitions);

        void switchToNewVersion();
    }
    private WpsTree tree;
    private final WpsEnvironment environment;
    private IMainView parentWindow;
    private final Action changeRootAction;
    private final List<ExplorerRoot> currentExplorerRoots = new LinkedList<>();
    private final IUpdateVersionController updateVersionController;

    public List<Action> getActions() {
        if (tree == null) {
            return Collections.emptyList();
        } else {
            final List<Action> actions = new LinkedList<>();
            actions.add(changeRootAction);
            actions.addAll(tree.getActions().getActions());
            return actions;
        }
    }

    public TreeManager(final WpsEnvironment userSession, final IUpdateVersionController uvController) {
        this.environment = userSession;
        if (uvController == null) {
            throw new NullPointerException("UpdateVersionController must be not null");
        }
        updateVersionController = uvController;
        changeRootAction = new RwtAction(environment, ClientIcon.Definitions.TREES);
        changeRootAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(final Action action) {
                changeExplorerRoot();
            }
        });
        changeRootAction.setVisible(false);
    }

    @Override
    public IExplorerTree getCurrentTree() {
        synchronized (this) {
            return tree;
        }
    }

    @Override
    public void updateVersion(Collection<Id> changedDefinitions) throws CantUpdateVersionException {
        final AdsVersion version = environment.getDefManager().getAdsVersion();
        if (tree != null && tree.getViewManager() != null && version.isSupported() && !tree.getViewManager().canSafetyClose()) {
            throw new CantUpdateVersionException(null);
        }

        if (tree != null && !tree.getRootNodes().isEmpty()) {
            boolean needForRelogin = false;

            final IProgressHandle progress = environment.getProgressHandleManager().newProgressHandle();
            progress.startProgress(environment.getMessageProvider().translate("ExplorerMessage", "Updating Version"), false);
            try {
                {
                    for (IExplorerTreeNode rootNode : tree.getRootNodes()) {
                        if (rootNode != null && rootNode.getView() != null) {
                            if (changedDefinitions.contains(rootNode.getView().getDefinitionId())) {
                                needForRelogin = true;
                                break;
                            }
                        }
                    }

                    final IExplorerTreeNode currentNode = tree.getCurrent();
                    if (currentNode != null) {
                        //Check if definitions linked with parent nodes (relative to current node) was changed
                        for (IExplorerTreeNode parentNode = currentNode.getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
                            if (changedDefinitions.contains(parentNode.getView().getDefinitionId())
                                    || changedDefinitions.contains(parentNode.getView().getDefinitionOwnerClassId())) {
                                //check for choosen entity
                                if (parentNode == currentNode.getParentNode() && (currentNode instanceof ChoosenEntityNode)) {
                                    final ChoosenEntityNode choosenEntityNode = (ChoosenEntityNode) currentNode;
                                    final ExplorerTreeNode ownerNode = choosenEntityNode.getParentNode();
                                    if (choosenEntityNode.isValid()
                                            && ownerNode.isValid()
                                            && choosenEntityNode.getView().getDefinitionOwnerClassId().equals(ownerNode.getView().getDefinitionOwnerClassId())) {
                                        continue;
                                    }
                                }
                                //find last changed and  first unchanged parent node
                                IExplorerTreeNode unchangedParentNode, lastChangedParentNode = parentNode;
                                for (unchangedParentNode = parentNode.getParentNode(); unchangedParentNode != null; unchangedParentNode = unchangedParentNode.getParentNode()) {
                                    if (!changedDefinitions.contains(unchangedParentNode.getView().getDefinitionId())
                                            && !changedDefinitions.contains(unchangedParentNode.getView().getDefinitionOwnerClassId())) {
                                        break;
                                    } else {
                                        lastChangedParentNode = unchangedParentNode;
                                    }
                                }
                                if (unchangedParentNode == null) {
                                    needForRelogin = true;
                                }
                                if (lastChangedParentNode != null) {
                                    final String title = environment.getMessageProvider().translate("ExplorerMessage", "Confirm to Update Version");
                                    final String question = environment.getMessageProvider().translate("ExplorerMessage", "Changes in \'%s\' detected.\nCurrent child item '%s' maybe absent after update.\nDo you want to continue update?");
                                    final String formattedQuestion = String.format(question, lastChangedParentNode.getView().getTitle(), currentNode.getView().getTitle());
                                    if (!environment.messageConfirmation(title, formattedQuestion)) {
                                        throw new CantUpdateVersionException(null);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

                /*if (updateVersionController.prepareNewVersion(changedDefinitions) && tree.getViewManager() != null) {
                 tree.getViewManager().closeAll();
                 }

                 updateVersionController.switchToNewVersion();*/
                //refill tree
                tree.refill(progress, needForRelogin);
            } catch (BrokenConnectionError error) {
                environment.messageError(environment.getMessageProvider().translate("ExplorerMessage", "Can't Update Version"), error.getMessage());
                environment.disconnectAction.trigger();
            } finally {
                progress.finishProgress();
            }
        } else {
            /* if (updateVersionController.prepareNewVersion(changedDefinitions)) {
             updateVersionController.switchToNewVersion();
             }*/
        }
    }

    @Override
    public boolean closeAll(boolean forced) {
        if (closeAllImpl(forced)) {
            if (parentWindow != null) {
                parentWindow.close();
            }
            return true;
        }
        return false;
    }

    private boolean closeAllImpl(boolean forced) {
        if (tree != null) {
            if (!tree.close(forced) && !forced) {
                return false;
            }
            if (parentWindow != null) {
                ((NavigationView) parentWindow.getNavigator()).remove(tree);
            }
            this.tree = null;
            changeRootAction.setVisible(false);
        }
        return true;
    }

    @Override
    public void translate() {
    }

    @Override
    public IExplorerTree openTree(final List<ExplorerRoot> explorerRoots, final IWidget parentWindow) throws ServiceClientException, InterruptedException {
        synchronized (this) {
            if (explorerRoots == null || explorerRoots.isEmpty()) {
                return null;
            }
            {
                final String title =
                        environment.getMessageProvider().translate("ExplorerTree", "Change Explorer Root");
                final String toolTip =
                        environment.getMessageProvider().translate("ExplorerTree", "change explorer root");
                changeRootAction.setText(title);
                changeRootAction.setToolTip(toolTip);
            }
            currentExplorerRoots.clear();
            currentExplorerRoots.addAll(explorerRoots);

            final ExplorerRoot rootToOpen = selectExplorerRoot(explorerRoots);
            if (rootToOpen == null) {
                return null;
            }
            List<Id> visibleExplorerItems = rootToOpen.getVisibleExplorerItems();
            //but ok can initialize
            if (visibleExplorerItems.isEmpty()) {
                return null;
            }
            this.parentWindow = (IMainView) parentWindow;
            tree = new WpsTree(environment, this.parentWindow);
            tree.open(rootToOpen, visibleExplorerItems, rootToOpen);
            updateToolBar();
            changeRootAction.setVisible(currentExplorerRoots.size() > 1);
            return tree;
        }
    }

    private ExplorerRoot selectExplorerRoot(final List<ExplorerRoot> explorerRoots) {
        final Id rootId = environment.selectExplorerRootId(explorerRoots);
        if (rootId != null) {
            for (ExplorerRoot root : currentExplorerRoots) {
                if (root.getId().equals(rootId)) {
                    return root;
                }
            }
        }
        return null;
    }

    private void updateToolBar() {
        ((NavigationView) parentWindow.getNavigator()).updateToolBarState(getActions());
    }

    public boolean isOpened() {
        return tree != null;
    }

    @Override
    public IExplorerTree openSubTree(IExplorerTreeNode node, IWidget parentWindow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void afterEntitiesRemoved(Collection<Pid> removedEntitiesPids, IWidget currentView) {
        if (tree != null && removedEntitiesPids != null && !removedEntitiesPids.isEmpty()) {
            final boolean saveCurrentNode = currentView instanceof IExplorerItemView;
            for (IExplorerTreeNode rootNode : tree.getRootNodes()) {
                final List<IExplorerTreeNode> allNodes = rootNode.getChildNodesRecursively();
                for (IExplorerTreeNode node : allNodes) {
                    if (needToRemoveNode(node, removedEntitiesPids) && (!saveCurrentNode || !nodeInCurrentContext(tree, node))) {
                        tree.removeNode(node);
                    }
                }
            }
        }
    }

    private boolean needToRemoveNode(final IExplorerTreeNode node, final Collection<Pid> removedEntitiesPids) {
        if (node.isValid() && node.getView().isEntityView()) {
            final EntityModel entity = (EntityModel) node.getView().getModel();
            if (removedEntitiesPids.contains(entity.getPid())) {
                return true;
            }
            if (entity.getContext() instanceof IContext.ReferencedChoosenEntityEditing) {
                //if entity was inserted into tree from property reference
                //we must check if property owner was not removed
                final IContext.ReferencedChoosenEntityEditing choosenEntityContext =
                        (IContext.ReferencedChoosenEntityEditing) entity.getContext();
                if (removedEntitiesPids.contains(choosenEntityContext.ownerPid)) {
                    return true;
                }
                //then we must check if some parent for owner was removed
                for (Pid removedEntityPid : removedEntitiesPids) {
                    if (choosenEntityContext.ownerEntityIsChildFor(removedEntityPid)) {
                        return true;
                    }
                }
            } else if (entity.getContext() instanceof IContext.ChoosenEntityEditing) {
                final IContext.ChoosenEntityEditing choosenEntityContext = (IContext.ChoosenEntityEditing) entity.getContext();
                for (Pid removedEntityPid : removedEntitiesPids) {
                    if (choosenEntityContext.isChildEntity(removedEntityPid)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean nodeInCurrentContext(final IExplorerTree tree, final IExplorerTreeNode node) {
        for (IExplorerTreeNode contextNode = tree.getCurrent(); contextNode != null; contextNode = contextNode.getParentNode()) {
            if (contextNode.getIndexInExplorerTree() == node.getIndexInExplorerTree()) {
                return true;
            }
        }
        return false;
    }

    private void changeExplorerRoot() {
        final ExplorerRoot rootToOpen = selectExplorerRoot(null);
        if (rootToOpen != null && closeAllImpl(false)) {
            final List<Id> visibleExplorerItems;
            try {
                visibleExplorerItems = rootToOpen.getVisibleExplorerItems();
            } catch (InterruptedException exception) {
                return;
            } catch (ServiceClientException exception) {
                environment.processException(exception);
                return;
            }
            if (!visibleExplorerItems.isEmpty()) {
                tree = new WpsTree(environment, this.parentWindow);
                tree.open(rootToOpen, visibleExplorerItems, rootToOpen);
                changeRootAction.setVisible(currentExplorerRoots.size() > 1);
                updateToolBar();
            }
        }
    }
}
