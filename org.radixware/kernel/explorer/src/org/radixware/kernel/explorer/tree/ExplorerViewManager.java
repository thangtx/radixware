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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.IViewManager;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.views.IView;

import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.explorer.views.ViewHolder;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;


public class ExplorerViewManager extends QMainWindow implements IViewManager {

    private final static String VIEW_AREA_SETTING_NAME = "viewArea";
    private final ExplorerMenu selectorMenu, editorMenu;
    private final List<ViewHolder> viewHolders = new ArrayList<ViewHolder>();
    private ViewHolder activeHolder;
    private IExplorerTreeNode currentNode;
    private final IClientEnvironment environment;

    public ExplorerViewManager(IClientEnvironment environment, final ExplorerMenu selectorMenu, final ExplorerMenu editorMenu, final QWidget parent) {
        super(parent);
        this.environment = environment;
        this.selectorMenu = selectorMenu;
        this.editorMenu = editorMenu;
        final QSizePolicy sizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        sizePolicy.setHorizontalStretch((byte) 1);
        sizePolicy.setVerticalStretch((byte) 1);
        setSizePolicy(sizePolicy);
        setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
    }

    public ExplorerViewManager(IClientEnvironment environment, final QWidget parent) {
        this(environment, null, null, parent);
    }

    @Override
    public IView openView(final IExplorerTreeNode node, final boolean closeCurrent) {
        ViewHolder newHolder;
        if (activeHolder == null) {
            newHolder = new ViewHolder(environment);
            newHolder.setObjectName("viewHolder");
            newHolder.setEditorMenu(editorMenu);
            newHolder.setSelectorMenu(selectorMenu);
            newHolder.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
            newHolder.wasClosed.connect(this, "holderWasClosed()");
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
            addDockWidget(Qt.DockWidgetArea.LeftDockWidgetArea, newHolder);
            final QSizePolicy sizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            sizePolicy.setHorizontalStretch((byte) 1);
            newHolder.setSizePolicy(sizePolicy);
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
    private void holderWasClosed() {
        final ViewHolder closedHolder = (ViewHolder) QObject.signalSender();
        if (closedHolder != null) {
            closedHolder.wasClosed.disconnect();
            viewHolders.remove(closedHolder);
            final int holdersCount = viewHolders.size();
            if (activeHolder == closedHolder) {
                activeHolder = holdersCount > 0 ? viewHolders.get(holdersCount - 1) : null;
            }
            if (holdersCount == 1) {
                activeHolder.setTitleBarHidden(true);
            }
            removeDockWidget(closedHolder);
            closedHolder.setParent(null);

            if (activeHolder == null) {
                setVisible(false);
            }
        }
    }

    public IExplorerTreeNode getCurrentNode() {
        return activeHolder != null ? currentNode : null;
    }

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
            holder.wasClosed.disconnect();
            holder.forceClose();
            removeDockWidget(holder);
        }
        viewHolders.clear();
        activeHolder = null;
        currentNode = null;
    }

    @Override
    public void closeCurrentView() {
        if (activeHolder != null) {
            activeHolder.forceClose();
        }
    }

    public boolean canSafetyClose() {
        return activeHolder != null ? activeHolder.canSafetyClose() : true;
    }
}
