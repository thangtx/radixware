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
package org.radixware.kernel.designer.common.general.nodes;

import java.awt.Component;
import java.beans.PropertyVetoException;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import org.netbeans.api.actions.*;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.project.ui.PathFinder;
import org.openide.ErrorManager;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.nodes.NodeNotFoundException;
import org.openide.nodes.NodeOp;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

public class NodesManager {

    private static final NodesFactoriesManager factoriesManager = new NodesFactoriesManager();

    private NodesManager() {
    }
    private static final Map<RadixObject, WeakReference<Node>> nodes = new WeakHashMap<>();
    private static final RadixObject.IRemoveListener removeListener = new RadixObject.IRemoveListener() {
        @Override
        public void onEvent(final RemovedEvent event) {
            final RadixObject removedRadixObject = event.radixObject;

            if (SwingUtilities.isEventDispatchThread()) {
                // to update FileObject in node after move
                onRadixObjectRemoved(removedRadixObject);
            } else {
                // called from uploader
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // to update FileObject in node after move
                        onRadixObjectRemoved(removedRadixObject);
                    }
                });
            }

        }
    };

    private static void onRadixObjectRemoved(final RadixObject radixObject) {
        // to update FileObject in node after move
        for (int iteration = 1;; iteration++) {
            if (nodesLock.tryLock()) {
                try {
                    //final WeakReference<Node> nodeRef = nodes.get(radixObject);
                    //final Node node = (nodeRef != null ? nodeRef.get() : null);
//            if (node != null) {
//            try {
//                node.destroy();
//            } catch (IOException ex) {
//                ErrorManager.getDefault().notify(ex);
//            }
                    //System.out.println("DEBUG: remove node " + String.valueOf(node) + " for object " + String.valueOf(radixObject));
                    //           }

                    final boolean ui
                            = radixObject instanceof AdsRwtWidgetDef
                            || radixObject instanceof AdsWidgetDef
                            || radixObject instanceof AdsLayout
                            || radixObject instanceof AdsLayout.SpacerItem;
                    if (!ui) {
                        nodes.remove(radixObject);
                        radixObject.getRemoveSupport().removeEventListener(removeListener);
                    }
                    return;
                } finally {
                    nodesLock.unlock();
                }
            } else {
                try {
                    Thread.sleep(iteration * sleepTimeout);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    /**
     * Find early created or create new RadixObject node using factory
     * registered by declarative way.
     */
    private static final Lock nodesLock = new ReentrantLock();
    private static final int sleepTimeout = 5;

    public static Node findOrCreateNode(final RadixObject radixObject) {
        return findOrCreateNode(null, radixObject);
    }

    @SuppressWarnings("unchecked") // developer must specify corrected node factories in layer.xml.
    public static Node findOrCreateNode(final RadixObject context, final RadixObject radixObject) {
        if (radixObject instanceof AdsMultilingualStringDef) {
            throw new RadixObjectError("Attempt to create node for MLS", radixObject); // looking for deadlock reason: RADIX-3314
        }

        for (int iteration = 1;; iteration++) {
            if (nodesLock.tryLock()) {
                try {
                    final WeakReference<Node> nodeRef = nodes.get(radixObject);
                    Node node = (nodeRef != null ? nodeRef.get() : null);
                    if (node instanceof IContextDependentNode) {
                        RadixObject nodeContext = ((IContextDependentNode) node).getContext();
                        if (nodeContext != context) {
                            node = null;
                        }
                    }
                    if (node == null) {
                        final INodeFactory factory = factoriesManager.get(radixObject);
                        if (factory instanceof IContextNodeFactory) {
                            node = ((IContextNodeFactory) factory).newInstance(context, radixObject);
                        } else {
                            node = factory.newInstance(radixObject);
                        }

                        radixObject.getRemoveSupport().addEventListener(removeListener);
                        nodes.put(radixObject, new WeakReference<>(node));

                        //System.out.println("DEBUG: add node " + String.valueOf(node) + " for object " + String.valueOf(radixObject));
                    }
                    return node;
                } finally {
                    nodesLock.unlock();
                }
            } else {
                try {
                    Thread.sleep(sleepTimeout * iteration);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public static TopComponent findProjectTab() {
        TopComponent tc = WindowManager.getDefault().findTopComponent("projectTabLogical_tc"); // from org.netbeans.modules.project.ui.ProjectTab.ID_LOGICAL
        if (tc == null) {
            tc = WindowManager.getDefault().findTopComponent("UdsExplorerTopComponent");
        }
        return tc;
    }
    
    public static TopComponent findFilesTab() {
        return WindowManager.getDefault().findTopComponent("projectTab_tc"); // from org.netbeans.modules.project.ui.ProjectTab.ID_PHYSICAL;
    }

    /**
     * Find JTree recursively in AWT Container
     *
     * @return tree or null if not found.
     */
    private static JTree findTree(java.awt.Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JTree) {
                return (JTree) c;
            }
            if (c instanceof java.awt.Container) {
                final java.awt.Container container = (java.awt.Container) c;
                final JTree tree = findTree(container);
                if (tree != null) {
                    return tree;
                }
            }
        }
        return null;
    }

    private static void collapseProjectTree() {
        final TopComponent projectsTab = findProjectTab();
        final JTree tree = findTree(projectsTab);
        for (int i = tree.getRowCount(); i > 0; i--) {
            tree.collapseRow(i);
        }
    }

    public static void collapseUnselectedNodes() {
        final TopComponent projectsTab = findProjectTab();
        final ExplorerManager.Provider explorerManagerProvider = (ExplorerManager.Provider) projectsTab;
        final ExplorerManager explorerManager = explorerManagerProvider.getExplorerManager();
        final Node[] selectedNodes = explorerManager.getSelectedNodes();
        collapseProjectTree();
        try {
            explorerManager.setSelectedNodes(selectedNodes);
        } catch (PropertyVetoException error) {
            ErrorManager.getDefault().notify(error);
        }
    }

    public interface LogicalViewProvider {

        Node findPath(Node root, Object target);
    }

    /**
     * Select RadixObject in Projects tab.
     */
    public static void selectInProjects(final RadixObject radixObject) {
        assert radixObject != null;

        final TopComponent projectsTab = findProjectTab();
        if (!(projectsTab instanceof ExplorerManager.Provider)) {
            return;
        }

        projectsTab.open();
        projectsTab.requestActive();

        final ExplorerManager.Provider explorerManagerProvider = (ExplorerManager.Provider) projectsTab;
        final ExplorerManager explorerManager = explorerManagerProvider.getExplorerManager();

        for (Project project : OpenProjects.getDefault().getOpenProjects()) {
            final org.netbeans.spi.project.ui.LogicalViewProvider logicalViewProvider = project.getLookup().lookup(org.netbeans.spi.project.ui.LogicalViewProvider.class);
            if (logicalViewProvider != null) {
                final Node node = logicalViewProvider.findPath(explorerManager.getRootContext(), radixObject);
                if (node != null) {
                    try {
                        //collapseProjectTree();
                        explorerManager.setSelectedNodes(new Node[]{node});
                        //collapseUnselectedNodes();
                    } catch (PropertyVetoException error) {
                        ErrorManager.getDefault().notify(error);
                    }
                }
                return;
            }
        }

        if (projectsTab instanceof LogicalViewProvider) {
            final Node node = ((LogicalViewProvider) projectsTab).findPath(explorerManager.getRootContext(), radixObject);
            if (node != null) {
                try {
                    //collapseProjectTree();
                    explorerManager.setSelectedNodes(new Node[]{node});
                    //collapseUnselectedNodes();
                } catch (PropertyVetoException error) {
                    ErrorManager.getDefault().notify(error);
                }
            }
        }

    }
    
    public static void selectInFiles(File file) {
        final FileObject fileObject = RadixFileUtil.toFileObject(file);
        selectInFiles(fileObject);
    }
    
    public static void selectInFiles(FileObject fileObject) {
        try {
            if (fileObject == null) {
                return;
            }
            TopComponent tc = NodesManager.findFilesTab();
            tc.open();
            tc.requestActive();
            final ExplorerManager.Provider explorerManagerProvider = (ExplorerManager.Provider) tc;
            final ExplorerManager explorerManager = explorerManagerProvider.getExplorerManager();
            Node node = explorerManager.getRootContext();
            node = findInFiles(node, fileObject);
            if (node != null) {
                explorerManager.setSelectedNodes(new Node[]{node});
            }
            
        } catch (PropertyVetoException ex) {
            ErrorManager.getDefault().notify(ex);
        }
    }
    
    private static Node findInFiles(final Node root, FileObject fo) { //from  org.netbeans.modules.project.ui.PhysicalView.PathFinder.findPath
        if (root == null || fo == null) {
            return null;
        }
        Project project = FileOwnerQuery.getOwner(fo);
        if (project == null) {
            return null;
        }
        FileObject projectFO = project.getProjectDirectory();
        String relPath = FileUtil.getRelativePath(projectFO, fo);
        ArrayList<String> path = new ArrayList<>();
        StringTokenizer strtok = new StringTokenizer(relPath, "/");
        while (strtok.hasMoreTokens()) {
            path.add(strtok.nextToken());
        }

        if (path.size() > 0) {
            path.remove(path.size() - 1);
        } else {
            return null;
        }
        try {
            Node projectNode = NodeOp.findChild(root, projectFO.getName());
            if (projectNode == null) {
                return null;
            }
            Node parent = NodeOp.findPath(projectNode, Collections.enumeration(path));
            if (parent != null) {
                Node[] nds = parent.getChildren().getNodes(true);
                for (Node nd : nds) {
                    FileObject dobj = nd.getLookup().lookup(FileObject.class);
                    if (dobj != null && fo.equals(dobj)) {
                        return nd;
                    }
                }
                String name = fo.getName();
                try {
                    DataObject dobj = DataObject.find(fo);
                    name = dobj.getNodeDelegate().getName();
                } catch (DataObjectNotFoundException ex) {
                }
                return parent.getChildren().findChild(name);
            }
        } catch (NodeNotFoundException e) {
            return null;
        }
        return null;
    }
    
    public static void updateOpenedEditors() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (TopComponent topComponent : TopComponent.getRegistry().getOpened()) {
                    if (topComponent instanceof IRadixObjectEditor) {
                        final IRadixObjectEditor editor = (IRadixObjectEditor) topComponent;
                        editor.update();
                    }
                }
            }
        });
    }

    public static List<RadixObject> collectSelectedRadixObjects() {
        List<RadixObject> list = new ArrayList<>();
        final TopComponent projectsTab = findProjectTab();
        final ExplorerManager.Provider explorerManagerProvider = (ExplorerManager.Provider) projectsTab;
        final ExplorerManager explorerManager = explorerManagerProvider.getExplorerManager();

        Node arr[] = explorerManager.getSelectedNodes();
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                RadixObject obj = arr[i].getLookup().lookup(RadixObject.class);
                if (obj != null) {
                    list.add(obj);
                }
            }

        }
        return list;
    }

    public static Node findNode(final RadixObject radixObject) {
        for (int iteration = 1;; iteration++) {
            if (nodesLock.tryLock()) {
                try {
                    final WeakReference<Node> ref = nodes.get(radixObject);
                    return (ref != null ? ref.get() : null);
                } finally {
                    nodesLock.unlock();
                }
            } else {
                try {
                    Thread.sleep(iteration * sleepTimeout);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}
