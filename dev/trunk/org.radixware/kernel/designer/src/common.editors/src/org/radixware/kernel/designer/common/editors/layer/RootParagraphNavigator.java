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
package org.radixware.kernel.designer.common.editors.layer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.swing.Icon;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemClusterizator;

final class RootParagraphNavigator {

    private final Layer layer;
    private final boolean forParagraphs;

    public RootParagraphNavigator(Layer layer, boolean forParagraph) {
        this.layer = layer;
        this.forParagraphs = forParagraph;
    }

    private List<RootDefinitionItem> collectRoots(Layer layer, final Set<Id> used) {
        final List<RootDefinitionItem> all = new ArrayList<>();

        layer.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                Definition def = null;
                if (forParagraphs) {
                    final AdsParagraphExplorerItemDef paragraphItem = (AdsParagraphExplorerItemDef) radixObject;
                    final Id id = paragraphItem.getId();

                    if (paragraphItem.isRoot() && !used.contains(id)) {
                        def = paragraphItem;
                        used.add(id);
                    }
                } else {
                    AdsRoleDef role = (AdsRoleDef) radixObject;
                    if (!used.contains(role.getId())) {
                        def = role;
                        used.add(role.getId());
                    }
                }

                if (def != null) {
                    final RootDefinitionItem rootParagraphItem = new RootDefinitionItem(def, def.getId());
                    all.add(rootParagraphItem);
                }
            }
        }, forParagraphs ? AdsVisitorProviders.newParagraphVisitorProvider(true) : new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsRoleDef;
            }
        });

        return all;
    }

    private enum CollectMode {

        ALL, INHERIT, DEFAULT
    }

    private List<RootDefinitionItem> collectSelectedRoots(Layer layer, Set<Id> used, CollectMode mode) {
        final List<RootDefinitionItem> all = new ArrayList<>();
        final Layer.RootDefinitionList roots = forParagraphs ? layer.getAccessibleRoots() : layer.getAccessibleRoles();

        if (roots.isInherited() || mode == CollectMode.INHERIT || mode == CollectMode.ALL) {

            for (String uri : layer.getBaseLayerURIs()) {
                final Layer prevLayer = layer.getBranch().getLayers().findByURI(uri);
                if (prevLayer == null) {
                    continue;
                }

                final List<RootDefinitionItem> prevRoots = collectRoots(prevLayer, used);
                all.addAll(prevRoots);
                final List<RootDefinitionItem> prevSelectedRoots = collectSelectedRoots(prevLayer, used, mode == CollectMode.ALL ? CollectMode.ALL : CollectMode.DEFAULT);
                all.addAll(prevSelectedRoots);
            }
        } else {
            final Map<String, Set<Id>> rootMap = roots.getRootMap();
            for (final String uri : rootMap.keySet()) {
                final Layer currLayer = layer.getBranch().getLayers().findByURI(uri);
                if (currLayer == null) {
                    continue;
                }
                for (final Id id : rootMap.get(uri)) {
                    if (used.contains(id)) {
                        continue;
                    }

                    Definition paragraph = null;

                    final AdsSegment ads = (AdsSegment) currLayer.getAds();
                    for (final AdsModule module : ads.getModules()) {
                        final AdsDefinition def = module.getTopContainer().findById(id);

                        if (def != null) {
                            paragraph = def;
                            used.add(id);
                            break;
                        }
                    }

                    all.add(new RootDefinitionItem(paragraph, id));
                }
            }
        }

        return all;
    }

    ItemClusterizator.IClusterizatorModel<RootDefinitionItem> createModel() {
        final List<RootDefinitionItem> all = new ArrayList<>();
        final List<RootDefinitionItem> selected = new ArrayList<>();
        final List<RootDefinitionItem> constSelected = new ArrayList<>();

        final Set<Id> usedIds = new HashSet<>();

        final Layer.RootDefinitionList roots = forParagraphs ? layer.getAccessibleRoots() : layer.getAccessibleRoles();
        final List<RootDefinitionItem> currRoots = collectRoots(layer, usedIds);
        constSelected.addAll(currRoots);

        if (!roots.isInherited()) {
            final List<RootDefinitionItem> selectedRoots = collectSelectedRoots(layer, new HashSet<>(usedIds), CollectMode.DEFAULT);
            selected.addAll(selectedRoots);
        }

        final List<RootDefinitionItem> inherited = collectSelectedRoots(layer, new HashSet<>(usedIds), CollectMode.INHERIT);
        final List<RootDefinitionItem> allRoots = collectSelectedRoots(layer, new HashSet<>(usedIds), CollectMode.ALL);

        for (final RootDefinitionItem item : allRoots) {
            if (forParagraphs) {
                AdsParagraphExplorerItemDef paragraph = (AdsParagraphExplorerItemDef) item.paragraph;
                if (paragraph.isRoot()) {
                    all.add(item);
                }
            } else {
                all.add(item);
            }
        }

        final ItemClusterizator.IClusterizatorItemProvider<RootDefinitionItem> provider = new ItemClusterizator.IClusterizatorItemProvider<RootDefinitionItem>() {
            @Override
            public List<RootDefinitionItem> getAllAvaliable() {
                return all;
            }

            @Override
            public List<RootDefinitionItem> getSelected() {
                return selected;
            }

            @Override
            public boolean isInherit() {
                return roots.isInherited();
            }

            @Override
            public List<RootDefinitionItem> getConstSelected() {
                return constSelected;
            }

            @Override
            public List<RootDefinitionItem> getInherited() {
                return inherited;
            }
        };

        return new RootParagraphModel(provider, !layer.getBaseLayerURIs().isEmpty(), false);
    }
}

class RootDefinitionItem implements ItemClusterizator.IItem {

    final Definition paragraph;
    final Id id;

    public RootDefinitionItem(Definition paragraph, Id id) {
        this.paragraph = paragraph;
        this.id = id;
    }

    @Override
    public String getName() {
        if (paragraph != null) {
            return paragraph.getQualifiedName();
        }

        return "#" + String.valueOf(id);
    }

    @Override
    public Icon getIcon() {
        if (paragraph != null) {
            return paragraph.getIcon().getIcon();
        }

        return RadixObjectIcon.UNKNOWN.getIcon();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.paragraph);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RootDefinitionItem other = (RootDefinitionItem) obj;
        if (!Objects.equals(this.paragraph, other.paragraph)) {
            return false;
        }
        return true;
    }

    public String getShortName() {
        if (paragraph != null) {
            return paragraph.getName();
        }

        return "#" + String.valueOf(id);
    }
}

final class RootParagraphModel extends ItemClusterizator.AbstractClusterizatorModel<RootDefinitionItem> {

    public RootParagraphModel(ItemClusterizator.IClusterizatorItemProvider<RootDefinitionItem> provider, boolean inheritable, boolean sortable) {
        super(provider, inheritable, sortable);
    }

    @Override
    public String toString() {
        if (isInherit()) {
            return "Inherited...";
        }

        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final RootDefinitionItem item : getAllSelected()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(item.getShortName());
        }

        return sb.toString();
    }
}
