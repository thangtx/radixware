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

package org.radixware.kernel.designer.common.dialogs.dependency;

import java.util.List;
import java.util.Set;
import org.openide.nodes.Children;


class DependencyModuleWayChildFactory extends ModuleDependencyChildFactory {
    private final ModuleDependencyGraph.Node target;
    private final Set<ModuleDependencyGraph.Node> nodes;

    public DependencyModuleWayChildFactory(ModuleDependencyGraph.Node node,
            ModuleDependencyGraph.Node target,
            Set<ModuleDependencyGraph.Node> nodes,
            ModuleDependencyChildFactory parentFactory) {

        super(node, parentFactory);

        this.nodes = nodes;
        this.target = target;
    }

    @Override
    List<ModuleDependencyGraph.Node> collectChildrenForKey(final ModuleDependencyGraph.Node key, List<ModuleDependencyGraph.Node> toPopulate, boolean findFirst) {
        final ModuleDependencyGraph.IFilter<ModuleDependencyGraph.Node> filter = new ModuleDependencyGraph.IFilter<ModuleDependencyGraph.Node>() {

            @Override
            public boolean accept(ModuleDependencyGraph.Node dependencyNode) {
                return nodes.contains(dependencyNode) && !inWay(dependencyNode) && key != dependencyNode;
            }
        };

        if (!inWay(key) && key != target) {
            for (final ModuleDependencyGraph.Node dependencyNode : key.getInNodes().values()) {
                final ModuleDependencyGraph graph = (ModuleDependencyGraph) key.getOwnerGraph();

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

    @Override
    Children createChildrenForKey(OrientedGraph.Node key) {
        return Children.create(new DependencyModuleWayChildFactory(key, target, nodes, this), false);
    }
}
