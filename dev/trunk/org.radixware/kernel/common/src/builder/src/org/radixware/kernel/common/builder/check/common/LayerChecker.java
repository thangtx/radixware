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

package org.radixware.kernel.common.builder.check.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.LayerUtils;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class LayerChecker<T extends Layer> extends RadixObjectChecker<T> {

    @Override
    public void check(T layer, IProblemHandler problemHandler) {
        checkLicense(layer.getLicenses(), problemHandler);
        checkTargetDbConfigurations(layer.getTargetDatabases(), layer, problemHandler);
        checkAccessibleRoots(layer, problemHandler);
    }

    void checkTargetDbConfigurations(final List<Layer.TargetDatabase> targetDatabases, final Layer layer, final IProblemHandler ph) {
        final Set<String> dbTypes = new HashSet<>();
        for (Layer.TargetDatabase targetDb : targetDatabases) {
            if (!dbTypes.add(targetDb.getType())) {
                ph.accept(RadixProblem.Factory.newError(layer, "Duplicated target database type '" + targetDb.getType() + "'"));
            }
            checkTargetDbConfiguration(targetDb, layer, ph);
        }
    }

    void checkTargetDbConfiguration(final Layer.TargetDatabase targetDb, final Layer layer, final IProblemHandler ph) {
        if (targetDb.getType() == null || targetDb.getType().isEmpty()) {
            ph.accept(RadixProblem.Factory.newError(layer, "Target database type name is empty"));
            return;
        }
        if (!Layer.ORG_RADIXWARE_LAYER_URI.equals(layer.getURI()) && !LayerUtils.getAvailableBaseTargetDbTypes(targetDb.getLayer()).contains(targetDb.getType())) {
            ph.accept(RadixProblem.Factory.newError(layer, "Target database type '" + targetDb.getType() + "' is not supported in base layers"));
            return;
        }
        for (Layer baseLayer : layer.getBranch().getLayers()) {
            if (layer.getBaseLayerURIs().contains(baseLayer.getURI())) {
                boolean found = false;
                for (Layer.TargetDatabase baseTargetDb : baseLayer.getTargetDatabases()) {
                    if (targetDb.getType().equals(baseTargetDb.getType())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ph.accept(RadixProblem.Factory.newError(layer, "Target database type '" + targetDb.getType() + "' is unsupported in base layer '" + baseLayer.getURI() + "'"));
                }
            }
        }

        final List<Layer.DatabaseOptionDependency> nonExistingDeps = LayerUtils.collectNonExistingDependencies(targetDb);

        for (Layer.DatabaseOptionDependency nonExistingDep : nonExistingDeps) {
            ph.accept(RadixProblem.Factory.newError(layer, "Non-existing base option '" + nonExistingDep.getOptionName() + "'"));
        }

        if (targetDb.getDependencies() != null) {
            final Set<String> overriddenDeps = new HashSet<>();
            for (Layer.DatabaseOptionDependency dep : targetDb.getDependencies()) {
                if (!overriddenDeps.add(dep.getOptionName())) {
                    ph.accept(RadixProblem.Factory.newError(layer, "Duplicated dependency on '" + dep.getOptionName() + "'"));
                }
            }
        }

        if (targetDb.getOptions() != null) {
            final Set<String> ownOptions = new HashSet<>();
            for (Layer.DatabaseOption opt : targetDb.getOptions()) {
                if (!ownOptions.add(opt.getName())) {
                    ph.accept(RadixProblem.Factory.newError(layer, "Duplicated option '" + opt.getName() + "'"));
                }
            }
        }

        final Map<Layer, Set<Layer>> allLayerDepsMap = LayerUtils.collectAllLayerDepsMap(layer.getBranch());
        final List<Layer> layersToCheck = new ArrayList<>(allLayerDepsMap.get(layer));
        layersToCheck.add(layer);
        LayerUtils.collectOptions(layersToCheck, targetDb.getType(), ph);
    }

    boolean checkLicense(final Layer.License rootLicense, final IProblemHandler problemHandler) {
        boolean result = true;
        if (!checkLicenseRecursive(rootLicense, problemHandler)) {
            result = false;
        }
        final List<List<Layer.License>> cycles = new ArrayList<>();
        searchCycles(rootLicense, rootLicense.collectFullNameToLicenseMap(), cycles);
        if (!cycles.isEmpty()) {
            for (List<Layer.License> cycle : cycles) {
                problemHandler.accept(RadixProblem.Factory.newError(rootLicense.getLayer(), "Cyclic license dependency: " + cycleToString(cycle)));
            }
        }
        return result;
    }

    private String cycleToString(final List<Layer.License> licenses) {
        if (licenses == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < licenses.size(); i++) {
            sb.append(licenses.get(i).getFullName());
            if (i < licenses.size() - 1) {
                sb.append("->");
            }
        }
        return sb.toString();
    }

    void searchCycles(final Layer.License license, final Map<String, Layer.License> fullNameToLicense, final List<List<Layer.License>> cycles) {
        final Set<Layer.License> black = new HashSet<>();
        final Set<Layer.License> grey = new HashSet<>();
        final List<Layer.License> path = new ArrayList<>();
        doSearch(license, black, grey, path, cycles, fullNameToLicense);
        if (license.getChildren() != null) {
            for (Layer.License childLicense : license.getChildren()) {
                searchCycles(childLicense, fullNameToLicense, cycles);
            }
        }
    }

    void doSearch(final Layer.License license, final Set<Layer.License> black, final Set<Layer.License> grey, final List<Layer.License> path, final List<List<Layer.License>> cyclePaths, final Map<String, Layer.License> fullNameToLicense) {
        if (black.contains(license)) {
            return;
        }
        path.add(license);
        try {
            if (grey.contains(license)) {
                if (path.get(0) == license) {
                    cyclePaths.add(new ArrayList<>(path));
                }
                return;
            }
            grey.add(license);
            if (license.getDependencies() != null) {
                for (String depName : license.getDependencies()) {
                    final Layer.License depLicense = fullNameToLicense.get(depName);
                    if (depLicense != null) {
                        doSearch(depLicense, black, grey, path, cyclePaths, fullNameToLicense);
                    }
                }
            }
            grey.remove(license);
            black.add(license);
        } finally {
            path.remove(path.size() - 1);
        }
    }

    boolean checkLicenseRecursive(final Layer.License license, final IProblemHandler problemHandler) {
        boolean result = true;
        if (license.getOwnName() == null || license.getOwnName().isEmpty()) {
            problemHandler.accept(RadixProblem.Factory.newError(license.getLayer(), "Empty license in " + license.getParentFullName()));
            result = false;
        }
        if (license.getDependencies() != null) {
            for (final String dependency : license.getDependencies()) {
                final AtomicBoolean found = new AtomicBoolean(false);
                new Layer.HierarchyWalker().go(license.getLayer(), new HierarchyWalker.AbstractDefaultAcceptor<Layer>() {
                    @Override
                    public void accept(HierarchyWalker.Controller controller, Layer layer) {
                        if (layer.getLicenses().contains(dependency)) {
                            found.set(true);
                            controller.stop();
                        }
                    }
                });
                if (!found.get()) {
                    problemHandler.accept(RadixProblem.Factory.newError(license.getLayer(), "Dependency '" + dependency + "' for '" + license.getFullName() + "' is not found"));
                    result = false;
                }
            }
        }
        if (license.getChildren() != null) {
            for (int i = 0; i < license.getChildren().size(); i++) {
                if (!checkLicenseRecursive(license.getChildren().get(i), problemHandler)) {
                    result = false;
                }
                for (int j = i + 1; j < license.getChildren().size(); j++) {
                    if (license.getChildren().get(i).getOwnName().equals(license.getChildren().get(j).getOwnName())) {
                        problemHandler.accept(RadixProblem.Factory.newError(license.getLayer(), "Duplicated nested license '" + license.getChildren().get(i).getOwnName() + "' in '" + license.getFullName() + "'"));
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return Layer.class;
    }
    
    private void checkAccessibleRoots(T layer, IProblemHandler problemHandler) {
        final Map<String, Set<Id>> rootsMap = layer.getAccessibleRoots().getRootMap();

        final Set<Id> usedIds = new HashSet<>();
        final List<AdsParagraphExplorerItemDef> roots = new ArrayList<>();
        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Void>() {

            @Override
            public void accept(HierarchyWalker.Controller<Void> controller, Layer currLayer) {
                final Set<Id> ids = rootsMap.get(currLayer.getURI());

                if (ids == null || ids.isEmpty()) {
                    return;
                }
                
                currLayer.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        final AdsParagraphExplorerItemDef paragraphItem = (AdsParagraphExplorerItemDef) radixObject;
                        final Id id = paragraphItem.getId();

                        if (ids.contains(id) && !usedIds.contains(id)) {
                            roots.add(paragraphItem);
                            usedIds.add(id);
                        }
                    }
                }, AdsVisitorProviders.newParagraphVisitorProvider(true));
            }
        });
        
        for (final AdsParagraphExplorerItemDef root : roots) {
            if (!root.isRoot()) {
                error(layer, problemHandler, "Root paragraph '" + root.getQualifiedName() + "'is not root");
            }
        }
        
        for (final Layer.RootDefinition rootParagraph : layer.getAccessibleRoots()) {
            if (!usedIds.contains(rootParagraph.defId)) {
                error(layer, problemHandler, "Root paragraph '" + rootParagraph.layerUri + ":#" + rootParagraph.defId + "'not found");
            }
        }
        for (final Layer.RootDefinition rootParagraph : layer.getAccessibleRoles()) {
            if (!usedIds.contains(rootParagraph.defId)) {
                error(layer, problemHandler, "Role '" + rootParagraph.layerUri + ":#" + rootParagraph.defId + "'not found");
            }
        }
    }
}
