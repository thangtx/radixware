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

import java.util.List;
import java.util.ArrayList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.IViewManager;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.views.ViewHolder;


class ViewManager extends VerticalBox implements IViewManager {

    private final static String VIEW_AREA_SETTING_NAME = "viewArea";
    private final IMenu selectorMenu, editorMenu;
    private final List<ViewHolder> viewHolders = new ArrayList<ViewHolder>();
    private ViewHolder activeHolder;
    private IExplorerTreeNode currentNode;
    private final WpsEnvironment environment;
    private final ViewHolder.CloseListener closeListener = new ViewHolder.CloseListener() {

        @Override
        public void closed(ViewHolder h) {
            holderWasClosed(h);
        }
    };

    public ViewManager(IClientEnvironment environment, final IMenu selectorMenu, final IMenu editorMenu, IMainView mainView) {

        this.environment = (WpsEnvironment) environment;
        this.selectorMenu = selectorMenu;
        this.editorMenu = editorMenu;
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        mainView.setView(this);
    }

    public ViewManager(IClientEnvironment environment, IMainView mainView) {
        this(environment, null, null, mainView);
    }

    @Override
    public IView openView(final IExplorerTreeNode node, final boolean closeCurrent) {
        ViewHolder newHolder;
        if (activeHolder == null) {
            newHolder = new ViewHolder(environment);
            newHolder.setObjectName("viewHolder");
            newHolder.setEditorMenu(editorMenu);
            newHolder.setSelectorMenu(selectorMenu);
            newHolder.addCloseListener(closeListener);
            add(newHolder);
        } else {
            newHolder = null;
        }

        final IView result;
        try {
            if (newHolder != null) {
                result = newHolder.open((ExplorerTreeNode) node);
            } else {
                result = activeHolder.open((ExplorerTreeNode) node);
            }
        } catch (InterruptedException ex) {
            if (newHolder != null) {
                newHolder.close();
            } else {
                activeHolder.close();
                activeHolder = null;
            }
            return null;
        } catch (RuntimeException ex) {
            node.getExplorerTree().getEnvironment().processException(ex);
            return null;
        } finally {
            currentNode = node;
        }

        if (newHolder != null) {
            //addDockWidget(Qt.DockWidgetArea.LeftDockWidgetArea, newHolder);            
            newHolder.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            viewHolders.add(newHolder);
            if (viewHolders.size() == 2) {
                activeHolder.setTitleBarHidden(false);
                newHolder.setTitleBarHidden(false);
            }
            activeHolder = newHolder;
            setVisible(true);
        }
        return result;
    }

    @SuppressWarnings("unused")
    private void holderWasClosed(ViewHolder closedHolder) {

        if (closedHolder != null) {
            closedHolder.removeCloseListener(closeListener);
            viewHolders.remove(closedHolder);
            final int holdersCount = viewHolders.size();
            if (activeHolder == closedHolder) {
                activeHolder = holdersCount > 0 ? viewHolders.get(holdersCount - 1) : null;
            }
            if (holdersCount == 1) {
                activeHolder.setTitleBarHidden(true);
            }
            remove(closedHolder);
            closedHolder.setParent(null);

            //   if (activeHolder == null) {
            //   setVisible(false);
            //  }
        }
    }

    @Override
    public IExplorerTreeNode getCurrentNode() {
        return activeHolder != null ? currentNode : null;
    }

    @Override
    public boolean isViewOpenedForModel(final Model model) {
        return model.getView() != null
                && currentNode != null
                && currentNode.isValid()
                && currentNode.getView() != null
                && currentNode.getView().getModel() == model;
    }

    @Override
    public void closeAll() {
        for (ViewHolder holder : viewHolders) {
            holder.removeCloseListener(closeListener);
            holder.forceClose();
            remove(holder);
        }
        viewHolders.clear();
        activeHolder = null;
    }

    @Override
    public void closeCurrentView() {
        if (activeHolder != null) {
            activeHolder.forceClose();
        }
    }

    @Override
    public boolean canSafetyClose() {
        return activeHolder != null ? activeHolder.canSafetyClose() : true;
    }
}
