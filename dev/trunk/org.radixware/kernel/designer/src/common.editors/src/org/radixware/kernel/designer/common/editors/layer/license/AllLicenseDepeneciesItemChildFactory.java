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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.designer.common.dialogs.dependency.OrientedGraph;

public class AllLicenseDepeneciesItemChildFactory extends ChildFactory<OrientedGraph.Node> {

    private final Set<OrientedGraph.Node> allDependencies;
    private final Collection<OrientedGraph.Node> targets;
    private final OrientedGraph.Node node;

    public AllLicenseDepeneciesItemChildFactory(OrientedGraph.Node node, List<OrientedGraph.Node> targets) {
        allDependencies = new HashSet<>(node.getAllOutNodes());
        this.node = node;
        this.targets = targets != null ? targets : allDependencies;
    }

    @Override
    protected boolean createKeys(List<OrientedGraph.Node> toPopulate) {
        for (final OrientedGraph.Node dependencyNode : targets) {
            if (dependencyNode != node) {
                toPopulate.add(dependencyNode);
            }
        }
        Collections.sort(toPopulate, new LicenseDependencyGraphNodeComparator());
        return true;
    }

    @Override
    protected Node createNodeForKey(OrientedGraph.Node key) {        
        LicenseDependencyTreeNode result = new LicenseDependencyTreeNode(Children.create(new LicenseDependencyChildFactory(
                key, node, allDependencies, null), false), key.getObject(), false);
        
        if (key.getInNodes().containsValue(node)) {
            result.setIsTargetParent(true);
        }
        
        return result;
    }
}
