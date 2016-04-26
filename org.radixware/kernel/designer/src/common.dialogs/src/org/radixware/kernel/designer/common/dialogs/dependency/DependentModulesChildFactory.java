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

import java.util.Collection;
import java.util.List;
import org.openide.nodes.Children;


final class DependentModulesChildFactory extends ModuleDependencyChildFactory {

    private Collection<ModuleDependencyGraph.Node> targets;

    public DependentModulesChildFactory(ModuleDependencyGraph.Node node,
            ModuleDependencyChildFactory parentFactory, Collection<ModuleDependencyGraph.Node> targets) {
        super(node, parentFactory);

        this.targets = targets;
    }

    @Override
    List<ModuleDependencyGraph.Node> collectChildrenForKey(final ModuleDependencyGraph.Node key, List<ModuleDependencyGraph.Node> toPopulate, boolean findFirst) {
        if (targets != null && targets.contains(key)) {
            return toPopulate;
        }

        if (key != null && !inWay(key)) {
            for (final ModuleDependencyGraph.Node child : key.getInNodes().values()) {
                if (key != child && !inWay(child) && hasWay(key, child)) {
                    toPopulate.add(child);
                    if (findFirst) {
                        break;
                    }
                }
            }
        }
        return toPopulate;
    }

    @Override
    Children createChildrenForKey(ModuleDependencyGraph.Node key) {
        return Children.create(new DependentModulesChildFactory(key, this, targets), false);
    }

    private boolean hasWay(final ModuleDependencyGraph.Node key, final ModuleDependencyGraph.Node nodeFrom) {

        final ModuleDependencyGraph.IFilter filter = new ModuleDependencyGraph.IFilter<ModuleDependencyGraph.Node>() {

            @Override
            public boolean accept(ModuleDependencyGraph.Node node) {
                return node != key && !inWay(node);
            }
        };

        if (targets == null) {
            return true;
        }
        for (final OrientedGraph.Node target : targets) {
            if (nodeFrom == target || nodeFrom.getOwnerGraph().hasWay(filter, nodeFrom, target, OrientedGraph.EEdgeDirection.IN)) {
                return true;
            }
        }

        return false;
    }
}