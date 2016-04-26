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

import java.util.*;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;


final class AllModuleDependenciesChildFactory extends ChildFactory<ModuleDependencyGraph.Node> {
    private final Set<ModuleDependencyGraph.Node> allDependencies;
    private final Collection<ModuleDependencyGraph.Node> targets;
    private final ModuleDependencyGraph.Node node;

    public AllModuleDependenciesChildFactory(ModuleDependencyGraph.Node node, List<ModuleDependencyGraph.Node> targets) {
        this.node = node;
        allDependencies = new HashSet<>(node.getAllOutNodes());
        this.targets = targets != null ? targets : allDependencies;
    }

    @Override
    protected boolean createKeys(List<ModuleDependencyGraph.Node> toPopulate) {
        for (final OrientedGraph.Node dependencyNode : targets) {
            if (dependencyNode != node) {
                toPopulate.add(dependencyNode);
            }
        }
        Collections.sort(toPopulate);
        return true;
    }

    @Override
    protected Node createNodeForKey(ModuleDependencyGraph.Node key) {
        return new ModuleTreeNode(Children.create(new DependencyModuleWayChildFactory(
                key, node, allDependencies, null), false), key.getObject());
    }
}