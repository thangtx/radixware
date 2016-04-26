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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;


abstract class ModuleDependencyChildFactory extends ChildFactory<ModuleDependencyGraph.Node> {

    protected final ModuleDependencyGraph.Node node;
    protected final ModuleDependencyChildFactory parentFactory;

    public ModuleDependencyChildFactory(ModuleDependencyGraph.Node node, ModuleDependencyChildFactory parentFactory) {
        this.node = node;
        this.parentFactory = parentFactory;
    }

    @Override
    protected final Node createNodeForKey(ModuleDependencyGraph.Node key) {
        if (isNodeLeaf(key)) {
            return new ModuleTreeNode(Children.LEAF, key.getObject());
        }
        return new ModuleTreeNode(createChildrenForKey(key), key.getObject());
    }

    @Override
    protected boolean createKeys(final List<ModuleDependencyGraph.Node> toPopulate) {
        collectChildrenForKey(node, toPopulate, false);
        if (!toPopulate.isEmpty()) {
            Collections.sort(toPopulate);
        }
        return true;
    }

    boolean isNodeLeaf(ModuleDependencyGraph.Node key) {
        return collectChildrenForKey(key, new ArrayList<ModuleDependencyGraph.Node>(), true).isEmpty();
    }

    protected final boolean inWay(ModuleDependencyGraph.Node conditate) {

        ModuleDependencyChildFactory current = parentFactory;

        while (current != null) {
            if (current.node == conditate) {
                return true;
            }
            current = current.parentFactory;
        }
        return false;
    }

    abstract Children createChildrenForKey(ModuleDependencyGraph.Node key);

    abstract List<ModuleDependencyGraph.Node> collectChildrenForKey(final ModuleDependencyGraph.Node key, List<ModuleDependencyGraph.Node> toPopulate, boolean findFirst);
}
