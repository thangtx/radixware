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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import org.openide.nodes.Children;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.components.TreeViewPanel;
import org.radixware.kernel.designer.common.dialogs.dependency.OrientedGraph;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Module;

public class LicenseDependenciesTreePanel extends JPanel {

    private Layer.License license;
    private final TreeViewPanel treeViewPanel = new TreeViewPanel();
    private final Map<String, Layer.License> unsavedLicenses;

    private OrientedGraph dependencyGraph;

    public LicenseDependenciesTreePanel(Layer.License license, Map<String, Layer.License> unsavedLicenses) {
        this.license = license;
        this.unsavedLicenses = unsavedLicenses;
        
        this.setLayout(new BorderLayout());
        this.add(treeViewPanel, BorderLayout.CENTER);

        update(null, null);
    }

    final public void update(List<String> licenseDependencies, List<Layer.License.RequiredModule> moduleDependencies) {
        if (!isLicenseChaged(licenseDependencies, moduleDependencies)) {
            return;
        }
        
        if (licenseDependencies != null && moduleDependencies != null) {
            license = new Layer.License(
                    license.getOwnName(),
                    license.getParentFullName(),
                    license.getChildren(),
                    licenseDependencies,
                    moduleDependencies,
                    license.getLayer()
            );
        }

        dependencyGraph = new LicenseDependencyGraph(license, unsavedLicenses);
        dependencyGraph.build();                
        applyFilter(false, false);
    }

    public void expandWay() {
        Node[] selectedNodes = treeViewPanel.getExplorerManager().getSelectedNodes();

        if (selectedNodes != null && selectedNodes.length == 1) {
            Node current = selectedNodes[0];

            while (!current.isLeaf()) {
                treeViewPanel.getTreeView().expandNode(current);
                if (current.getChildren().getNodesCount() > 0) {
                    current = current.getChildren().getNodeAt(0);
                } else {
                    break;
                }
            }
            treeViewPanel.getTreeView().expandNode(current);
        }
    }

    public void applyFilter(boolean hideModules, boolean hideLicenses) {
        OrientedGraph.Node start = dependencyGraph.findNode(license);
        List<OrientedGraph.Node> rootElements = new ArrayList<>();
        for (Object obj : start.getAllOutNodes()) {
            OrientedGraph.Node node = (OrientedGraph.Node) obj;

            if (node.getObject() instanceof Module && !hideModules) {
                rootElements.add(node);
            }
            if (node.getObject() instanceof Layer.License && !hideLicenses) {
                rootElements.add(node);
            }
        }

        Node root = new LicenseDependencyTreeNode(Children.create(
                new AllLicenseDepeneciesItemChildFactory(start, rootElements), false), license, false);

        treeViewPanel.getTreeView().setRootVisible(false);
        treeViewPanel.getExplorerManager().setRootContext(root);
    }

    private boolean isLicenseChaged(List<String> licenseDependencies, List<Layer.License.RequiredModule> moduleDependencies) {                
        if (licenseDependencies == null || moduleDependencies == null) {
            return true;
        }
        
        if ((license.getDependencies().size() != licenseDependencies.size())
                || (license.getRequiredModules().size() != moduleDependencies.size())) {
            return true;
        }
        
        return !license.getDependencies().containsAll(licenseDependencies) || !license.getRequiredModules().containsAll(moduleDependencies);        
    }
}
