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

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.models.EntityModel;

import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.clientstate.ExplorerTreeState;


public interface IExplorerTree {
    
    public static interface Actions{
        Action getRemoveCurrentNodeAction();
        Action getRemoveChildChoosenObjectsAction();
        Action getGoToParentNodeAction();
        Action getGoToCurrentNodeAction();
        Action getGoBackAction();
        Action getGoForwardAction();
    }

    void setNodeVisible(final IExplorerTreeNode node, final boolean isVisible);

    boolean isNodeVisible(final IExplorerTreeNode node);

    void update(IExplorerTreeNode node);

    void lock();

    void unlock();

    IViewManager getViewManager();

    void removeNode(final IExplorerTreeNode node);

    IExplorerTreeNode addChoosenEntity(final ExplorerTreeNode parent, final EntityModel entity, final int index);
    
    IExplorerTreeNode addUserExplorerItem(final ExplorerTreeNode parent, final RadExplorerItemDef userItem, final int index);

    boolean setCurrent(final IExplorerTreeNode node);

    IExplorerTreeNode getCurrent();        

    IExplorerTreeNode findNodeByExplorerItemId(final Id explorerItemId);

    IExplorerTreeNode findNodeByPosition(final int xPos, final int yPos);

    void expand(final IExplorerTreeNode node);

    void collapse(final IExplorerTreeNode node);

    boolean isExpanded(final IExplorerTreeNode node);

    List<IExplorerTreeNode> getRootNodes();

    boolean isExplorerItemAccessible(Id explorerItemId);

    IClientEnvironment getEnvironment();
    
    Actions getActions();
    
    ExplorerTreeState writeStateToXml();
    
    void restoreStateFromXml(ExplorerTreeState state);
}
