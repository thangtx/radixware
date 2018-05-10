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
package org.radixware.kernel.designer.common.editors.layer.license;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

import org.radixware.kernel.designer.common.dialogs.dependency.OrientedGraph;

class LicenseDependencyChildFactory extends ChildFactory<OrientedGraph.Node> {

    protected final OrientedGraph.Node node;
    protected final LicenseDependencyChildFactory parentFactory;

    private final OrientedGraph.Node target;
    private final Set<OrientedGraph.Node> allDependentNodes;

    public LicenseDependencyChildFactory(OrientedGraph.Node node,
            OrientedGraph.Node target,
            Set<OrientedGraph.Node> allDependentNodes,
            LicenseDependencyChildFactory parentFactory) {

        this.node = node;
        this.parentFactory = parentFactory;

        this.allDependentNodes = allDependentNodes;
        this.target = target;
    }

    @Override
    protected final Node createNodeForKey(OrientedGraph.Node key) {
        if (isNodeLeaf(key)) {
            return new LicenseDependencyTreeNode(Children.LEAF, key.getObject(), true);
        }
        LicenseDependencyTreeNode result = new LicenseDependencyTreeNode(createChildrenForKey(key), key.getObject(), false);
        if (key.getInNodes().containsValue(target)) {
            result.setIsTargetParent(true);
        }
        
        return result;
    }

    @Override
    protected boolean createKeys(final List<OrientedGraph.Node> toPopulate) {
        collectChildrenForKey(node, toPopulate, false);
        if (!toPopulate.isEmpty()) {
            Collections.sort(toPopulate);
        }
        Collections.sort(toPopulate, new LicenseDependencyGraphNodeComparator());
        return true;
    }

    boolean isNodeLeaf(OrientedGraph.Node key) {
        return collectChildrenForKey(key, new ArrayList<OrientedGraph.Node>(), true).isEmpty();
    }

    protected final boolean inWay(OrientedGraph.Node conditate) {

        LicenseDependencyChildFactory current = parentFactory;

        while (current != null) {
            if (current.node == conditate) {
                return true;
            }
            current = current.parentFactory;
        }
        return false;
    }
    
    List<OrientedGraph.Node> collectChildrenForKey(final OrientedGraph.Node key, List<OrientedGraph.Node> toPopulate, boolean findFirst) {
        final OrientedGraph.IFilter<OrientedGraph.Node> filter = new OrientedGraph.IFilter<OrientedGraph.Node>() {

            @Override
            public boolean accept(OrientedGraph.Node dependencyNode) {
                return allDependentNodes.contains(dependencyNode) && !inWay(dependencyNode) && key != dependencyNode;
            }
        };

        if (!inWay(key) && key != target) {
            List<OrientedGraph.Node> dependencyNodes = new ArrayList<>(key.getInNodes().values());
            for (final OrientedGraph.Node dependencyNode : dependencyNodes) {
                final OrientedGraph graph = (OrientedGraph) key.getOwnerGraph();

                if (graph.hasWay(filter, dependencyNode, target, OrientedGraph.EEdgeDirection.IN)) {
                    toPopulate.add(dependencyNode);
                    if (findFirst) {
                        break;
                    }
                }
            }
        }

        return toPopulate;
    }
    
    Children createChildrenForKey(OrientedGraph.Node key) {
        return Children.create(new LicenseDependencyChildFactory(key, target, allDependentNodes, this), false);
    }
}
