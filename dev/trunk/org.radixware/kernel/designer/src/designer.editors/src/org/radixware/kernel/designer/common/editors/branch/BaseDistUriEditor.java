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
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.CheckableNode;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.repository.Branch.Layers;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class BaseDistUriEditor extends JPanel implements ExplorerManager.Provider {

    static void edit(final BaseDistUriManager manager) {
        final BaseDistUriEditor baseDistUriEditor = new BaseDistUriEditor(manager);
        final ModalDisplayer displayer = new ModalDisplayer(baseDistUriEditor,
                NbBundle.getMessage(BaseDistUriEditor.class, "BaseDistUriEditor.title"));

        if (displayer.showModal()) {
            Set<String> uris = new HashSet<>(baseDistUriEditor.getModel().getBaseDistrUris());
            if (uris != null) {
                Layers layers = baseDistUriEditor.getModel().getBranch().getLayers();
                for (String uri : uris) {
                    if (layers.findByURI(uri) == null) {
                        baseDistUriEditor.getModel().setBaseDistrUri(uri, false);
                    }
                }
            }
            manager.setModel(baseDistUriEditor.getModel());
        }
    }
    

    private final class ExcludeFromRealizedProperty extends Node.Property<Boolean> {

        LayerNode node;

        public ExcludeFromRealizedProperty(LayerNode node) {
            super(Boolean.class);
            setName(EXCLUDE_FROM_RELEASE_PROPERTY);
            this.node = node;
        }

        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return node.isExcludedFromRelease();
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return !node.isParentEcludeFromRealize();
        }

        @Override
        public void setValue(Boolean value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            node.setEcludeFromRelease(value);
            updateView();
        }
    }
        
    private class LayerNode extends AbstractNode implements CheckableNode {
        
        private final Layer currentLayer;

        public LayerNode(Layer currentLayer, List<Layer> allLayers) {
            super(createChildren(currentLayer, allLayers));

            setDisplayName(currentLayer.getName());
            this.currentLayer = currentLayer;

            final Sheet.Set set = new Sheet.Set();
            set.put(new ExcludeFromRealizedProperty(this));
            getSheet().put(set);
        }

        @Override
        public boolean isCheckable() {
            return true;
        }

        @Override
        public boolean isCheckEnabled() {
            return !isParentSelected() && !isExcludedFromRelease();
        }

        @Override
        public Boolean isSelected() {
            return isParentSelected() && !isExcludedFromRelease() || model.isBaseDistrUri(getURI());
        }

        private boolean isParentSelected() {
            Node parentNode = this.getParentNode();
            if (parentNode instanceof LayerNode && ((LayerNode) parentNode).isSelected()) {
                return true;
            }
            return false;
        }

        private boolean isParentEcludeFromRealize() {
            Node parentNode = this.getParentNode();
            if (parentNode instanceof LayerNode && ((LayerNode) parentNode).isExcludedFromRelease()) {
                return true;
            }
            return false;
        }

        private void clearChilderSelection() {
            for (final Node node : getChildren().snapshot()) {
                final LayerNode layerNode = (LayerNode) node;
                layerNode.clearChilderSelection();
                model.setBaseDistrUri(layerNode.getURI(), false);
            }
        }

        @Override
        public void setSelected(Boolean selected) {
            model.setBaseDistrUri(getURI(), selected);

            clearChilderSelection();

            updateView();
        }

        @Override
        public Image getIcon(int type) {
            return currentLayer.getIcon().getImage();
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        public boolean isExcludedFromRelease() {
            return isParentEcludeFromRealize() || model.isExcludedFromRelease(getURI());
        }

        private void clearChilderEcludeFromRelease() {
            for (final Node node : getChildren().snapshot()) {
                final LayerNode layerNode = (LayerNode) node;
                model.setExcludedFromRelease(layerNode.getURI(), false);
                layerNode.clearChilderEcludeFromRelease();
            }
        }

        public void setEcludeFromRelease(boolean ecludeFromRealize) {
            model.setExcludedFromRelease(getURI(), ecludeFromRealize);
            if (ecludeFromRealize) {
                setSelected(false);
                clearChilderEcludeFromRelease();
            }
        }

        public String getURI() {
            return currentLayer.getURI();
        }
    }

    private class LayerNodeChildren extends Children.Array {

        public LayerNodeChildren(Layer currentLayer, List<Layer> allLayers) {
            super();
            this.nodes = calcNodes(currentLayer, allLayers);
        }

        private List<Node> calcNodes(Layer currentLayer, List<Layer> allLayers) {
            final List<Node> child = new LinkedList<>();
            for (final Layer layer : allLayers) {
                if (layer.listBaseLayers().contains(currentLayer)) {
                    child.add(new LayerNode(layer, allLayers));
                }
            }
            return child;
        }
    }

    private final ExplorerManager manager = new ExplorerManager();
    private OutlineView outline = new OutlineView();
    private static final String EXCLUDE_FROM_RELEASE_PROPERTY = "Exclude from release";
    private final BaseDistUriManager.DistributableLayersModel model;
    
    public BaseDistUriEditor(BaseDistUriManager urisManager) {
        setLayout(new BorderLayout());

        add(outline, BorderLayout.CENTER);

        outline.getOutline().setShowGrid(false);
        outline.getOutline().setRowSelectionAllowed(true);
        outline.getOutline().setColumnHidingAllowed(false);
        outline.addPropertyColumn(EXCLUDE_FROM_RELEASE_PROPERTY, EXCLUDE_FROM_RELEASE_PROPERTY);
        outline.getOutline().getColumnModel().getColumn(0).setHeaderValue("Layer");

        float width = outline.getFontMetrics(outline.getFont()).stringWidth(EXCLUDE_FROM_RELEASE_PROPERTY) * 1.2f;
        outline.getOutline().getColumn(EXCLUDE_FROM_RELEASE_PROPERTY).setMaxWidth((int) width);
        outline.getOutline().getColumn(EXCLUDE_FROM_RELEASE_PROPERTY).setPreferredWidth((int) width);
        
        this.model = urisManager.getModel().getCopy();
        
        final Layer root = model.getBranch().getLayers().findByURI(Layer.ORG_RADIXWARE_LAYER_URI);
        if (model.getBranch().getBaseDevelopmentLayer() != null) {
            getExplorerManager().setRootContext(new LayerNode(root, model.getBranch().getLayers().list()));
            
            expandLeafs(getExplorerManager().getRootContext());
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
    
    BaseDistUriManager.DistributableLayersModel getModel() {
        return model;
    }

    private void updateView() {
        if (outline != null) {
            outline.revalidate();
            outline.repaint();
        }
    }

    private Children createChildren(Layer currentLayer, List<Layer> allLayers) {
        if (!hasChildren(currentLayer, allLayers)) {
            return Children.LEAF;
        } else {
            return new LayerNodeChildren(currentLayer, allLayers);
        }
    }

    private boolean hasChildren(Layer currentLayer, List<Layer> allLayers) {
        
        if (currentLayer.isLocalizing()) {
            return false;
        }
        
        for (final Layer layer : allLayers) {
            if (layer.listBaseLayers().contains(currentLayer)) {
                return true;
            }
        }
        return false;
    }
    
    private void expandLeafs(Node node) {
        
        for (final Node n : node.getChildren().getNodes()) {
            if (n.isLeaf() || n.getChildren().getNodesCount() == 0) {
                outline.expandNode(n);
            } else {
                expandLeafs(n);
            }
        }
    }
}
