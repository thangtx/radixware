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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Dependences;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.dependency.OrientedGraph;

public class LicenseDependencyGraph extends OrientedGraph<Object> {

    private final Layer.License root;
    private final Map<String, Layer.License> unsavedLicenses;

    public LicenseDependencyGraph(Layer.License root, Map<String, Layer.License> unsavedLicenses) {
        this.root = root;
        this.unsavedLicenses = unsavedLicenses;

        findOrCreateNode(root);
    }

    @Override
    protected void collectLinks() {
        for (final Layer layer : root.getLayer().getBranch().getLayers().getInOrder()) {
            layer.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    final Module module = (Module) radixObject;
                    if (module instanceof UdsModule) {
                        collectUdsModuleLinks(module);
                    } else {
                        collectModuleLinks(module);
                    }
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof Module;
                }

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    return !(radixObject instanceof Module);
                }
            });
        }

        for (final Layer layer : root.getLayer().getBranch().getLayers().getInOrder()) {
            Layer.License rootLayerLicense = layer.getLicenses();
            Map<String, Layer.License> allLayerLicenses = rootLayerLicense.collectFullNameToLicenseMap();
            allLayerLicenses.remove(rootLayerLicense.getFullName());

            for (Layer.License license : allLayerLicenses.values()) {
                if (!getNodeKey(root).equals(getNodeKey(license))) {
                    collectLicenseLinks(license, allLayerLicenses);
                }
            }
            for (Layer.License license : unsavedLicenses.values()) {
                if (!getNodeKey(root).equals(getNodeKey(license))) {
                    collectLicenseLinks(license, unsavedLicenses);
                }
            }

            Map<String, Layer.License> allLicenses = new HashMap<>(allLayerLicenses);
            allLicenses.putAll(unsavedLicenses);
            collectLicenseLinks(root, allLicenses);
        }
    }

    private void collectModuleLinks(Module module) {
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

    private void collectUdsModuleLinks(Module module) {
        final Node node = findOrCreateNode(module);
        
        List<Definition> dependences = new ArrayList<>();
        module.collectDependences(dependences);
        
        for (Definition def : dependences) {
            if (!(def instanceof Module)) {
                continue;
            }
            
            final Node child = findOrCreateNode((Module) def);

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

    private void collectLicenseLinks(Layer.License license, Map<String, Layer.License> allLicenses) {
        Node node = findOrCreateNode(license);
        for (String depLicenseName : license.getDependencies()) {
            Layer.License depLicense = allLicenses.get(depLicenseName);
            if (depLicense != null) {
                Node depLicenseNode = findOrCreateNode(depLicense);

                if (depLicenseNode == null) {
                    continue;
                }

                if (!node.getOutNodes().containsKey(depLicenseNode.getKey())) {
                    node.addOut(depLicenseNode);
                }

                if (!depLicenseNode.getInNodes().containsKey(node.getKey())) {
                    depLicenseNode.addIn(node);
                }
            }
        }

        for (Layer.License.RequiredModule depModule : license.getRequiredModules()) {
            String key = depModule.layerUri + "_" + depModule.id.toString();
            Node depModuleNode = getNodes().get(key);

            if (!node.getOutNodes().containsKey(depModuleNode.getKey())) {
                node.addOut(depModuleNode);
            }

            if (!depModuleNode.getInNodes().containsKey(node.getKey())) {
                depModuleNode.addIn(node);
            }
        }
    }

    @Override
    protected String getNodeKey(Object object) {
        if (object instanceof Module) {
            return ((Module) object).getLayer().getURI() + "_" + ((Module) object).getId().toString();
        } else if (object instanceof Layer.License) {
            return ((Layer.License) object).getFullName();
        } else {
            return object.toString();
        }
    }

    @Override
    protected String getNodeName(Object object) {
        if (object instanceof Module) {
            return ((Module) object).getQualifiedName();
        } else if (object instanceof Layer.License) {
            return ((Layer.License) object).getFullName();
        } else {
            return "<unknown>";
        }
    }

    public void addNode(Node node) {
        getNodes().put(node.getKey(), node);
    }
}
