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
package org.radixware.kernel.common.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.enums.EOptionMode;

public class LayerUtils {

    public static class DependencyInfo {

        private Layer.DatabaseOptionDependency dependency;
        private Layer defaultModeSourceLayer;
        private Layer editableSourceLayer;
        private Layer defaultUpgradeModeSourceLayer;
        private Layer editableOnUpgradeSourceLayer;
        private final List<EOptionMode> defaultModeAvailableValues;
        private final List<EOptionMode> defaultUpgradeModeAvailableValues;
        private final List<Boolean> editableAvailableValues;
        private final List<Boolean> editableOnUpgradeAvailableValues;

        public DependencyInfo(Layer.DatabaseOptionDependency dependency, Layer defaultModeSourceLayer, Layer editableSourceLayer, Layer defaultUpgradeModeSourceLayer, Layer editableOnUpgradesourceLayer) {
            this.dependency = dependency;
            this.defaultModeSourceLayer = defaultModeSourceLayer;
            this.editableSourceLayer = editableSourceLayer;
            this.defaultUpgradeModeSourceLayer = defaultUpgradeModeSourceLayer;
            this.editableOnUpgradeSourceLayer = editableOnUpgradesourceLayer;
            this.defaultModeAvailableValues = new ArrayList<>(Arrays.asList(EOptionMode.values()));
            this.defaultModeAvailableValues.add(null);
            if (dependency.getEditableOnUpgrade() != false) {
                this.defaultUpgradeModeAvailableValues = new ArrayList<>(Arrays.asList(EOptionMode.values()));
                this.editableOnUpgradeAvailableValues = new ArrayList<>(Arrays.asList(new Boolean[]{true, false, null}));
            } else {
                this.defaultUpgradeModeAvailableValues = new ArrayList<>();
                this.editableOnUpgradeAvailableValues = new ArrayList<>(Arrays.asList(new Boolean[]{null}));
            }
            this.defaultUpgradeModeAvailableValues.add(null);
            this.editableAvailableValues = new ArrayList<>(Arrays.asList(new Boolean[]{true, false, null}));            
        }

        public Layer.DatabaseOptionDependency getDependency() {
            return dependency;
        }

        public void setDependency(Layer.DatabaseOptionDependency dependency) {
            this.dependency = dependency;
        }

        public Layer getDefaultModeSourceLayer() {
            return defaultModeSourceLayer;
        }

        public void setDefaultModeSourceLayer(Layer defaultModeSourceLayer) {
            this.defaultModeSourceLayer = defaultModeSourceLayer;
        }

        public Layer getEditableSourceLayer() {
            return editableSourceLayer;
        }

        public void setEditableSourceLayer(Layer editableSourceLayer) {
            this.editableSourceLayer = editableSourceLayer;
        }

        public Layer getDefaultUpgradeModeSourceLayer() {
            return defaultUpgradeModeSourceLayer;
        }

        public void setDefaultUpgradeModeSourceLayer(Layer defaultUpgradeModeSourceLayer) {
            this.defaultUpgradeModeSourceLayer = defaultUpgradeModeSourceLayer;
        }

        public Layer getEditableOnUpgradeSourceLayer() {
            return editableOnUpgradeSourceLayer;
        }

        public void setEditableOnUpgradeSourceLayer(Layer editableOnUpgradeSourceLayer) {
            this.editableOnUpgradeSourceLayer = editableOnUpgradeSourceLayer;
        }

        public List<EOptionMode> getDefaultModeAvailableValues() {
            return defaultModeAvailableValues;
        }

        public List<EOptionMode> getDefaultUpgradeModeAvailableValues() {
            return defaultUpgradeModeAvailableValues;
        }

        public List<Boolean> getEditableAvailableValues() {
            return editableAvailableValues;
        }

        public List<Boolean> getEditableOnUpgradeAvailableValues() {
            return editableOnUpgradeAvailableValues;
        }

    }

    public static List<Layer.DatabaseOptionDependency> collectBaseOptions(final List<Layer> layers, final String dbType) {
        List<Layer.DatabaseOptionDependency> depList = new ArrayList<>();
        for (DependencyInfo depInfo : collectOptions(layers, dbType, null)) {
            depList.add(depInfo.getDependency());
        }
        return depList;
    }

    public static List<Layer> sortLayers(List<Layer> layers) {
        List<Layer> sortedByTitleLayers = new ArrayList<>(layers);
        Collections.sort(layers, new Comparator<Layer>() {
            @Override
            public int compare(Layer o1, Layer o2) {
                String match1 = o1.getTitle() != null ? o1.getTitle() : o1.getURI();
                String match2 = o2.getTitle() != null ? o2.getTitle() : o2.getURI();
                return match1.compareTo(match2);
            }
        });
        List<Layer> result = new LinkedList<>();
        for (Layer layer : sortedByTitleLayers) {
            if (result.contains(layer)) {
                continue;
            }

            int index = -1;
            for (int i = 0; i < result.size(); i++) {
                Layer added = result.get(i);
                if (isBaseLayer(layer, added)) {
                    index = i;
                    break;
                }
            }

            if (index < 0 || index >= result.size()) {
                result.add(layer);
            } else {
                result.add(index, layer);
            }
        }
        return result;
    }

    public static boolean isBaseLayer(Layer candidate, Layer target) {
        if (target != null && candidate != null && target.getBaseLayerURIs() != null) {
            for (Layer targetBaseLayer : target.listBaseLayers()) {
                if (targetBaseLayer.getURI().equals(candidate.getURI()) || isBaseLayer(candidate, targetBaseLayer)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<DependencyInfo> collectOptions(List<Layer> layers, final String dbType, IProblemHandler ph) {
        final List<DependencyInfo> result = new ArrayList<>();
        if (layers == null || dbType == null) {
            return result;
        }
        final List<Layer> sortedLayers = sortLayers(layers);

        for (Layer layer : sortedLayers) {
            for (Layer.TargetDatabase targetDb : layer.getTargetDatabases()) {
                if (dbType.equals(targetDb.getType())) {
                    for (Layer.DatabaseOption option : targetDb.getOptions()) {
                        boolean found = false;
                        for (DependencyInfo di : result) {
                            if (option.getQualifiedName().equals(di.getDependency().getOptionName())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            result.add(new DependencyInfo(new Layer.DatabaseOptionDependency(
                                    option.getQualifiedName(), option.getDefaultMode(), true, option.getDefaultUpgradeMode(), option.getEditableOnUpgrade()
                            ), layer, layer, layer, layer));
                        }
                    }
                    for (Layer.DatabaseOptionDependency layerDep : targetDb.getDependencies()) {
                        boolean found = false;
                        for (DependencyInfo resultDepInfo : result) {
                            if (resultDepInfo.getDependency().getOptionName().equals(layerDep.getOptionName())) {
                                found = true;
                                if (layerDep.getDefaultMode() == null && layerDep.getEditable() != null) {
                                    if (ph != null) {
                                        ph.accept(RadixProblem.Factory.newError(layer, "Default value of the option '" + layerDep.getOptionName() + "' is INHERIT[" + resultDepInfo.getDependency().getDefaultMode() + "] but Editable is overridden by value '" + layerDep.getEditable() + "'"));
                                    }
                                }
                                if (resultDepInfo.getDependency().getEditable() == false) {
                                    if (layerDep.getEditable() != null) {
                                        if (!Objects.equals(resultDepInfo.getDependency().getEditable(), layerDep.getEditable())) {
                                            if (ph != null) {
                                                ph.accept(RadixProblem.Factory.newError(layer, "Editable of the base option '" + layerDep.getOptionName() + "' in layer '" + resultDepInfo.getEditableSourceLayer().getURI() + "' can't be " + layerDep.getEditable() + " in layer '" + layer.getURI() + "'"));
                                            }
                                        }
                                    }
                                    if (layerDep.getDefaultMode() != null) {
                                        if (ph != null) {
                                            ph.accept(RadixProblem.Factory.newError(layer, "Value of the base option '" + layerDep.getOptionName() + "' in layer '" + resultDepInfo.getEditableSourceLayer().getURI() + "' can't be overridden in layer '" + layer.getURI() + "'"));
                                        }
                                    }
                                }

                                if (layerDep.getDefaultUpgradeMode() == null && layerDep.getEditableOnUpgrade() != null) {
                                    if (ph != null) {
                                        ph.accept(RadixProblem.Factory.newError(layer, "Default value on upgrade of the option '" + layerDep.getOptionName() + "' is INHERIT[" + resultDepInfo.getDependency().getDefaultUpgradeMode() + "] but Editable is overridden by value '" + layerDep.getEditableOnUpgrade() + "'"));
                                    }
                                }
                                if (resultDepInfo.getDependency().getEditableOnUpgrade() == false) {
                                    if (layerDep.getEditableOnUpgrade() != null) {
                                        if (!Objects.equals(resultDepInfo.getDependency().getEditableOnUpgrade(), layerDep.getEditableOnUpgrade())) {
                                            if (ph != null) {
                                                ph.accept(RadixProblem.Factory.newError(layer, "Editable on upgrade of the base option '" + layerDep.getOptionName() + "' in layer '" + resultDepInfo.getEditableSourceLayer().getURI() + "' can't be " + layerDep.getEditableOnUpgrade() + " in layer '" + layer.getURI() + "'"));
                                            }
                                        }
                                    }
                                    if (layerDep.getDefaultUpgradeMode() != null) {
                                        if (ph != null) {
                                            ph.accept(RadixProblem.Factory.newError(layer, "Value of the base option '" + layerDep.getOptionName() + "' in layer '" + resultDepInfo.getEditableSourceLayer().getURI() + "' can't be overridden in layer '" + layer.getURI() + "'"));
                                        }
                                    }
                                }
                            }
                            if (found) {
                                if (layerDep.getDefaultMode() != null) {
                                    resultDepInfo.setDefaultModeSourceLayer(layer);
                                }
                                if (layerDep.getEditable() != null) {
                                    resultDepInfo.setEditableSourceLayer(layer);
                                    if (layerDep.getEditable() == false) {
                                        resultDepInfo.getDefaultModeAvailableValues().clear();
                                        resultDepInfo.getDefaultModeAvailableValues().add(null);
                                        resultDepInfo.getEditableAvailableValues().clear();
                                        resultDepInfo.getEditableAvailableValues().add(null);
                                    }
                                }
                                if (layerDep.getDefaultUpgradeMode() != null) {
                                    resultDepInfo.setDefaultUpgradeModeSourceLayer(layer);
                                }
                                if (layerDep.getEditableOnUpgrade() != null) {
                                    resultDepInfo.setEditableOnUpgradeSourceLayer(layer);
                                    if (layerDep.getEditableOnUpgrade() == false) {
                                        resultDepInfo.getDefaultUpgradeModeAvailableValues().clear();
                                        resultDepInfo.getDefaultUpgradeModeAvailableValues().add(null);
                                        resultDepInfo.getEditableOnUpgradeAvailableValues().clear();
                                        resultDepInfo.getEditableOnUpgradeAvailableValues().add(null);
                                    }
                                }

                                Layer.DatabaseOptionDependency composedDependency = new Layer.DatabaseOptionDependency(
                                        layerDep.getOptionName(),
                                        layerDep.getDefaultMode() != null ? layerDep.getDefaultMode() : resultDepInfo.getDependency().getDefaultMode(),
                                        layerDep.getEditable() != null ? layerDep.getEditable() : resultDepInfo.getDependency().getEditable(),
                                        layerDep.getDefaultUpgradeMode() != null ? layerDep.getDefaultUpgradeMode() : resultDepInfo.getDependency().getDefaultUpgradeMode(),
                                        layerDep.getEditableOnUpgrade() != null ? layerDep.getEditableOnUpgrade() : resultDepInfo.getDependency().getEditableOnUpgrade()
                                );
                                resultDepInfo.setDependency(composedDependency);
                            }
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    public static List<String> getAvailableBaseTargetDbTypes(final Layer layer) {
        boolean first = true;
        final List<String> availableTypes = new ArrayList<>();
        for (Layer baseLayer : layer.getBranch().getLayers()) {
            if (layer.getBaseLayerURIs().contains(baseLayer.getURI())) {
                if (first) {
                    first = false;
                    for (Layer.TargetDatabase targetDb : baseLayer.getTargetDatabases()) {
                        availableTypes.add(targetDb.getType());
                    }
                } else {
                    for (int i = availableTypes.size() - 1; i >= 0; i--) {
                        boolean found = false;
                        for (Layer.TargetDatabase targetDb : baseLayer.getTargetDatabases()) {
                            if (targetDb.getType().equals(availableTypes.get(i))) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            availableTypes.remove(i);
                        }
                    }
                }
            }
        }
        return availableTypes;
    }

    public static List<Layer.DatabaseOptionDependency> collectNonExistingDependencies(Layer.TargetDatabase targetDb) {
        return collectNonExistingDependencies(targetDb, collectBaseOptions(collectBaseLayers(targetDb.getLayer(), new ArrayList<Layer>()), targetDb.getType()));
    }

    public static List<Layer.DatabaseOptionDependency> collectNonExistingDependencies(Layer.TargetDatabase targetDb, final List<Layer.DatabaseOptionDependency> baseOptions) {
        final List<Layer.DatabaseOptionDependency> result = new ArrayList<>();
        if (targetDb.getDependencies() != null) {
            for (Layer.DatabaseOptionDependency dep : targetDb.getDependencies()) {
                boolean exists = false;
                if (baseOptions != null) {
                    for (Layer.DatabaseOptionDependency baseOpt : baseOptions) {
                        if (baseOpt.getOptionName().equals(dep.getOptionName())) {
                            exists = true;
                            break;
                        }
                    }
                }
                if (!exists) {
                    result.add(dep);
                }
            }
        }
        return result;
    }

    /**
     * Fills baseLayers parameter with base layers and returns it
     */
    public static List<Layer> collectBaseLayers(final Layer layer, final List<Layer> baseLayers) {
        if (layer != null && baseLayers != null) {
            final List<String> baseUris = layer.getBaseLayerURIs();
            if (baseUris != null) {
                for (String baseUri : baseUris) {
                    for (Layer branchLayer : layer.getBranch().getLayers()) {
                        if (baseUri.equals(branchLayer.getURI()) && !baseLayers.contains(branchLayer)) {
                            baseLayers.add(branchLayer);
                            collectBaseLayers(branchLayer, baseLayers);
                        }
                    }
                }
            }
        }
        return baseLayers;
    }

    /**
     * Associate each layer with set of all base layers for it (both direct and
     * indirect)
     */
    public static Map<Layer, Set<Layer>> collectAllLayerDepsMap(final Branch b) {
        final Map<Layer, Set<Layer>> directDepsMap = new HashMap<>();
        final Map<Layer, Set<Layer>> allDepsMap = new HashMap<>();
        for (Layer layer : b.getLayers()) {
            allDepsMap.put(layer, new HashSet<Layer>());
            final Set<Layer> baseLayers = new HashSet<>();
            for (Layer potentialBaseLayer : b.getLayers()) {
                if (layer.getBaseLayerURIs().contains(potentialBaseLayer.getURI())) {
                    baseLayers.add(potentialBaseLayer);
                }
            }
            directDepsMap.put(layer, baseLayers);
        }
        for (Map.Entry<Layer, Set<Layer>> entry : directDepsMap.entrySet()) {
            collectDependencies(entry.getKey(), directDepsMap, allDepsMap.get(entry.getKey()));
        }
        return allDepsMap;
    }

    private static void collectDependencies(Layer l, Map<Layer, Set<Layer>> directDepsMap, Set<Layer> result) {
        for (Layer dep : directDepsMap.get(l)) {
            result.add(dep);
            collectDependencies(dep, directDepsMap, result);
        }
    }
}
