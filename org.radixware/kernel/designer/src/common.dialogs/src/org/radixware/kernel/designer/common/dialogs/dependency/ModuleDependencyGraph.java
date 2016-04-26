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
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.uds.UdsSegment;


final class ModuleDependencyGraph extends OrientedGraph<Module> {

    private class ModuleNode extends OrientedGraph<Module>.Node {

        public ModuleNode(Module object) {
            super(object);
        }

        @Override
        public int compareTo(OrientedGraph<Module>.Node o) {
            int result = super.compareTo(o);

            if (result == 0) {
                switch (o.getObject().getSegment().getType()) {
                    case DDS:
                        return 1;
                    case ADS:
                        return -1;
                }
            }

            return result;
        }
    }
    private final Layer root;

    public ModuleDependencyGraph(Layer root) {
        this.root = root;
    }

    @Override
    protected Node createNode(Module object) {
        return new ModuleNode(object);
    }

    @Override
    protected void collectLinks() {

        for (final Layer layer : root.getBranch().getLayers().getInOrder()) {
            layer.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    final Module module = (Module) radixObject;
                    final Node node = findOrCreateNode(module);

                    for (Dependences.Dependence dependence : module.getDependences()) {

                        final List<Module> dependencyModules = dependence.findDependenceModule(module);
                        if (dependencyModules.isEmpty()) {
                            continue;
                        }
                        final Node child = findOrCreateNode(dependencyModules.get(0));

                        if (!node.getOutNodes().containsKey(child.getKey())) {
                            node.addOut(child);
                        }

                        if (!child.getInNodes().containsKey(node.getKey())) {
                            child.addIn(node);
                        }
                    }

                    final Module overwritten = module.findOverwritten();
                    if (overwritten != null) {
                        final Node overwrittenNode = findOrCreateNode(overwritten);

                        if (!node.getOutNodes().containsKey(overwrittenNode.getKey())) {
                            node.addOut(overwrittenNode);
                        }

                        if (!overwrittenNode.getInNodes().containsKey(node.getKey())) {
                            overwrittenNode.addIn(node);
                        }
                    }
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof Module;
                }

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    return !(radixObject instanceof Module) && !(radixObject instanceof UdsSegment);
                }
            });
        }
    }

    @Override
    protected String getNodeKey(Module module) {
        return module.getId().toString() + "_" + getNodeName(module);
    }

    @Override
    protected String getNodeName(Module object) {
        return object.getQualifiedName();
    }
}
