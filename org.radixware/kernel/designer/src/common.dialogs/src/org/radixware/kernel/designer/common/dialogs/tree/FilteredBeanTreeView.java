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

package org.radixware.kernel.designer.common.dialogs.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;


public class FilteredBeanTreeView extends JPanel implements ExplorerManager.Provider, Lookup.Provider {

    private final ExplorerManager manager;
    private final Lookup lookup;
    private Component filterComponent;
    private Node rootNode;
    private final BeanTreeView treeView = new BeanTreeView();
    private final RequestProcessor requestProcessor = new RequestProcessor("FilteredBeanTreeView-RequestProcessor", 1);
    private static final Node NODE_WAIT = new AbstractNode(Children.LEAF);
    private final IFilterFactory filterFactory;
    private final Action defaultAction;

    static {
        NODE_WAIT.setDisplayName("Please wait...");
    }

    /**
     * If default action is not null, then it will be used instead of default
     * actions provided by other ways.
     *
     * @param filterFactory
     * @param defaultAction
     */
    public FilteredBeanTreeView(IFilterFactory filterFactory, Action defaultAction) {
        manager = new ExplorerManager();
        lookup = ExplorerUtils.createLookup(manager, new ActionMap());
        this.filterFactory = filterFactory;
        setLayout(new BorderLayout());
        this.defaultAction = defaultAction;
        add(treeView, BorderLayout.CENTER);
    }

    public FilteredBeanTreeView() {
        this(new NameFilterFactory(), null);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public void setRootNode(Node node) {
        rootNode = null;
        if (filterComponent != null) {
            remove(filterComponent);
            filterComponent = null;
        }

        if (node == null) {
            manager.setRootContext(Node.EMPTY);
            return;
        }

        rootNode = new FilteredNode(node, new IAcceptor<Node>() {
            @Override
            public boolean accept(Node candidate) {
                return true;
            }
        }, defaultAction);

        final INavigatorFilter filter = filterFactory.createFilter(node.getLookup().lookup(RadixObject.class));

        if (filter == null) {
            manager.setRootContext(rootNode);
        } else {
            filterComponent = filter.getComponent();
            if (filterComponent != null) {
                add(filterComponent, filter.getComponentPosition());
            }
            filter.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(final ChangeEvent e) {
                    filterNodes(filter.getNodeAcceptor());
                }
            });
            filterNodes(filter.getNodeAcceptor());
        }
    }

    private void filterNodes(final IAcceptor<Node> acceptor) {

        manager.setRootContext(NODE_WAIT);

        final Node processedRootNode = rootNode;

        requestProcessor.post(new Runnable() {
            @Override
            public void run() {
                final Set<Node> acceptedNodes = getAcceptedChildren(processedRootNode, acceptor);

                final IAcceptor<Node> filterAcceptor = new IAcceptor<Node>() {
                    @Override
                    public boolean accept(final Node candidate) {
                        return acceptedNodes.contains(candidate);
                    }
                };

                if (processedRootNode != null) {
                    final Node filteredRootNode = new FilteredNode(processedRootNode, filterAcceptor, defaultAction);

                    final List<Node> trueAcceptedNodes = new LinkedList<Node>();

                    collectTrueAcceptedNodes(filteredRootNode, trueAcceptedNodes, acceptor);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (processedRootNode == rootNode) {
                                manager.setRootContext(filteredRootNode);

                                for (Node node : trueAcceptedNodes) {
                                    treeView.expandNode(node);
                                }
                            }
                        }
                    });
                } else {
                    throw new IllegalStateException("processedRootNode is null");
                }
            }
        });
    }

    private void collectTrueAcceptedNodes(final Node node, final List<Node> result, final IAcceptor<Node> acceptor) {
        if (acceptor.accept(node)) {
            result.add(node);
        }
        for (Node childNode : node.getChildren().getNodes(true)) {
            collectTrueAcceptedNodes(childNode, result, acceptor);
        }
    }

    private Set<Node> getAcceptedChildren(final Node rootNode, final IAcceptor<Node> acceptor) {
        final Set<Node> result = new HashSet<Node>();
        for (Node childNode : rootNode.getChildren().getNodes(true)) {
            result.addAll(collectAcceptedNodes(childNode, acceptor));
        }
        return result;
    }

    private List<Node> collectAcceptedNodes(final Node node, final IAcceptor<Node> acceptor) {
        final List<Node> result = new LinkedList<Node>();
        for (Node childNode : node.getChildren().getNodes(true)) {
            result.addAll(collectAcceptedNodes(childNode, acceptor));
        }
        if (!result.isEmpty() || acceptor.accept(node)) {
            result.add(node);
        }
        return result;
    }
}
