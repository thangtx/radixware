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

package org.radixware.kernel.designer.common.editors.branch;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.CheckableNode;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.Utils;

/**
 *
 * @deprecated replaced by BaseDistUriEditor
 */
@Deprecated
class DistributeableLayerList extends JPanel implements ExplorerManager.Provider {

    static class DistributionManager {

        private final Branch branch;
        private final List<String> distributable;
        private OutlineView outline;

        DistributionManager(Branch branch) {
            this.branch = branch;
            this.distributable = new LinkedList<>(branch.getBaseDistURIs());
        }

        public boolean isDistributable(Layer layer) {
            if (layer == null) {
                return false;
            }
            if (distributable.contains(layer.getURI())) {
                return true;
            } else {
                List<Layer> prevs = layer.listBaseLayers();
                for (Layer prev : prevs) {
                    if (isDistributable(prev)) {
                        return true;
                    }
                }
                return false;
            }
        }

        private void removeAllSublayersFromList(final Layer layer) {
            final List<String> toRemove = new LinkedList<String>();

            for (final String uri : distributable) {
                if (uri.equals(layer.getURI())) {
                    continue;
                }
                Layer l = branch.getLayers().findByURI(uri);
                Layer.HierarchyWalker.walk(l, new Layer.HierarchyWalker.Acceptor<Object>() {

                    @Override
                    public void accept(HierarchyWalker.Controller<Object> controller, Layer l) {
                        if (Utils.equals(l.getURI(), layer.getURI())) {
                            toRemove.add(uri);
                            controller.stop();
                        }
                    }
                });
            }
            for (String uri : toRemove) {
                distributable.remove(uri);
            }
        }

        private void addDirectSublayersToList(Layer layer) {
            List<String> toAdd = new LinkedList<String>();

            for (Layer l : branch.getLayers()) {
                if (l.listBaseLayers().contains(layer)) {
                    toAdd.add(l.getURI());
                }
            }
            for (String uri : toAdd) {
                if (!distributable.contains(uri)) {
                    distributable.add(uri);

                }
            }
        }

        public boolean setDistributable(Layer layer, boolean flag) {
            if (canChangeDistributionState(layer)) {
                if (flag) {
                    if (!distributable.contains(layer.getURI())) {
                        removeAllSublayersFromList(layer);
                        distributable.add(layer.getURI());
                        updateView();
                        return true;
                    }
                } else {
                    if (distributable.contains(layer.getURI())) {
                        distributable.remove(layer.getURI());
                        addDirectSublayersToList(layer);
                        updateView();
                        return true;
                    }
                }
            }


            return false;
        }

        public boolean canChangeDistributionState(Layer layer) {
            if (isDistributable(layer)) {
                return distributable.contains(layer.getURI());
            } else {
                return true;
            }
        }

        void updateView() {
            if (outline != null) {
                outline.revalidate();
                outline.repaint();
            }
        }

        void apply() {
            branch.setBaseDistURIs(distributable);
        }
    }

    private static class LayerNodeChildren extends Children.Array {

        static Children createChildren(DistributionManager manager, Layer currentLayer, List<Layer> allLayers) {
            List<Node> nodes = calcNodes(manager, currentLayer, allLayers);
            if (nodes.isEmpty()) {
                return Children.LEAF;
            } else {
                return new LayerNodeChildren(manager, currentLayer, allLayers);
            }
        }

        private static List<Node> calcNodes(DistributionManager manager, Layer currentLayer, List<Layer> allLayers) {
            List<Node> nodes = new LinkedList<Node>();
            for (Layer l : allLayers) {
                if (l.listBaseLayers().contains(currentLayer)) {
                    nodes.add(new LayerNode(manager, l, allLayers));
                }
            }
            return nodes;
        }

        public LayerNodeChildren(DistributionManager manager, Layer currentLayer, List<Layer> allLayers) {
            super();
            this.nodes = calcNodes(manager, currentLayer, allLayers);
        }
    }

    private static class LayerNode extends AbstractNode implements CheckableNode {

        private final Layer layer;
        private final DistributionManager manager;

        public LayerNode(DistributionManager manager, Layer currentLayer, List<Layer> allLayers) {
            super(LayerNodeChildren.createChildren(manager, currentLayer, allLayers));
            this.layer = currentLayer;
            this.manager = manager;
            setDisplayName(this.layer == null ? "Root" : this.layer.getName());
        }

        @Override
        public boolean isCheckable() {
            return true;
        }

        @Override
        public boolean isCheckEnabled() {
            return manager.canChangeDistributionState(layer);
        }

        @Override
        public Boolean isSelected() {
            return manager.isDistributable(layer);
        }

        @Override
        public void setSelected(Boolean selected) {
            manager.setDistributable(layer, selected);
        }

        @Override
        public Image getIcon(int type) {
            return layer.getIcon().getImage();
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }

    private static class MyOutline extends OutlineView {

        public MyOutline() {
            getOutline().setRootVisible(false);
            getOutline().setShowGrid(false);
            getOutline().setRowSelectionAllowed(true);
            getOutline().setColumnHidingAllowed(false);
            getOutline().getColumnModel().getColumn(0).setHeaderValue("Layers");
        }
    }
    private final MyOutline outline;
    private final ExplorerManager manager = new ExplorerManager();

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public DistributeableLayerList(DistributionManager manager, Branch branch, JPanel parent) {
        parent.add(this);
        this.setLayout(new BorderLayout());
        outline = new MyOutline();
        this.add(outline);
        manager.outline = outline;
        this.manager.setRootContext(new LayerNode(manager, null, branch.getLayers().list()));
    }
}
