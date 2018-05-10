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
package org.radixware.kernel.designer.ads.editors.xml;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlTree extends JTree {

    public static XmlTree newInstance(Document document) {
        XmlTreeNode mainRoot = new XmlTreeNode(XmlUtils.getChildElements(document).get(0));

        XmlTreeNode allNodesRoot = new XmlTreeNode(XmlUtils.getChildElements(document).get(0));
        constructAllNodesTree(allNodesRoot);

        XmlTreeNode documentedNodesRoot = new XmlTreeNode(XmlUtils.getChildElements(document).get(0));
        constructDocumentedNodesTree(documentedNodesRoot, documentedNodesRoot);

        mainRoot.add(allNodesRoot);
        mainRoot.add(documentedNodesRoot);

        XmlTree tree = new XmlTree(new XmlTreeModel(mainRoot, allNodesRoot, documentedNodesRoot));
        tree.setVisibleRecursive(allNodesRoot, false);

        return tree;
    }

    private static void constructAllNodesTree(XmlTreeNode parent) {
        for (Element child : XmlUtils.getChildElements(parent.getElement())) {
            XmlTreeNode childNode = new XmlTreeNode(child);
            parent.add(childNode);
            constructAllNodesTree(childNode);
        }
    }

    private static void constructDocumentedNodesTree(XmlTreeNode parent, XmlTreeNode node) {
        for (Element child : XmlUtils.getChildElements(node.getElement())) {
            XmlTreeNode childNode = new XmlTreeNode(child);
            if (XmlTreeUtils.needsDocumentation(childNode)) {
                parent.add(childNode);
                constructDocumentedNodesTree(childNode, childNode);
            } else {
                constructDocumentedNodesTree(parent, childNode);
            }
        }
    }

    private XmlTree(TreeModel model) {
        super(model);
    }

    public void applyFilter(final String filter, final boolean hideDocumented, final boolean hideUndocumented) {
        XmlTreeModel model = (XmlTreeModel) this.getModel();
        XmlTreeNode root = (XmlTreeNode) model.getRoot();
        final XmlTreeCellRenderer renderer = (XmlTreeCellRenderer) this.getCellRenderer();

        final IAcceptor<XmlTreeNode> stringFilterAcceptor = new IAcceptor<XmlTreeNode>() {
            final Pattern namePattern = XmlTreeUtils.getRegExpPattern(filter);

            @Override
            public boolean accept(XmlTreeNode candidate) {
                if (filter == null || filter.isEmpty()) {
                    return true;
                }

                if (candidate == null) {
                    return false;
                }

                return namePattern.matcher(candidate.toString()).matches() || namePattern.matcher(candidate.getElement().getLocalName()).matches();
            }
        };

        final IAcceptor<XmlTreeNode> hideDocumentedAcceptor = new IAcceptor<XmlTreeNode>() {

            @Override
            public boolean accept(XmlTreeNode candidate) {
                if (renderer.getDocumentedNodes().keySet().contains(candidate.getXPath()) || !XmlTreeUtils.needsDocumentation(candidate)) {
                    return !hideDocumented;
                } else {
                    return true;
                }
            }
        };

        final IAcceptor<XmlTreeNode> hideUndocumentedAcceptor = new IAcceptor<XmlTreeNode>() {

            @Override
            public boolean accept(XmlTreeNode candidate) {
                if (!renderer.getDocumentedNodes().keySet().contains(candidate.getXPath())) {
                    return !hideUndocumented;
                } else {
                    return true;
                }
            }
        };

        final Set<IAcceptor<XmlTreeNode>> acceptors = new HashSet<>();
        acceptors.add(stringFilterAcceptor);
        acceptors.add(hideDocumentedAcceptor);
        acceptors.add(hideUndocumentedAcceptor);

        final XmlTreeFilterAcceptor acceptor = new XmlTreeFilterAcceptor(acceptors);
        acceptor.addToGroup(stringFilterAcceptor, "MAIN_FILTER");
        acceptor.addToGroup(hideDocumentedAcceptor, "ADDITIONAL_FILTER");
        acceptor.addToGroup(hideUndocumentedAcceptor, "ADDITIONAL_FILTER");

        setVisibleRecursive(root, false);
        root.setVisible(true);
        filterNodes(root, acceptor, filter);
        model.reload();
        XmlTreeUtils.expandAll(this);
    }

    private void filterNodes(final XmlTreeNode node, XmlTreeFilterAcceptor acceptor, final String filter) {
        Enumeration e = node.children();
        while (e.hasMoreElements()) {
            XmlTreeNode child = (XmlTreeNode) e.nextElement();
            filterNodes(child, acceptor, filter);
        }
        boolean visibleRecursive = filter != null && filter.endsWith("/");
        if ((!node.isVisible() && acceptor.accept(node)) || (acceptor.acceptGroup(node, "MAIN_FILTER") && visibleRecursive)) {
            if (visibleRecursive) {
                applyAdditionalFilter(node, acceptor);
            } else {
                node.setVisible(true);
            }
            setParentsVisible(node, true);
        }
    }

    private void setParentsVisible(XmlTreeNode node, boolean visible) {
        while (node != null && node.getParent() instanceof XmlTreeNode) {
            node = (XmlTreeNode) node.getParent();
            node.setVisible(visible);
        }
    }

    private void setVisibleRecursive(XmlTreeNode node, boolean visible) {
        Enumeration e = node.children();
        while (e.hasMoreElements()) {
            XmlTreeNode child = (XmlTreeNode) e.nextElement();
            setVisibleRecursive(child, visible);
        }
        node.setVisible(visible);
    }

    private void applyAdditionalFilter(XmlTreeNode node, XmlTreeFilterAcceptor acceptor) {
        Enumeration e = node.children();
        while (e.hasMoreElements()) {
            XmlTreeNode child = (XmlTreeNode) e.nextElement();
            applyAdditionalFilter(child, acceptor);
        }

        if (!node.isVisible() && acceptor.acceptGroup(node, "ADDITIONAL_FILTER")) {
            node.setVisible(true);
            setParentsVisible(node, true);
        }
    }

    public void setServiceNodesVisible(boolean visible) {
        XmlTreeModel model = (XmlTreeModel) this.getModel();
        if (model.getAllNodesRoot() != null && model.getDocumentedNodesRoot() != null) {
            setVisibleRecursive(model.getAllNodesRoot(), !visible);
            setVisibleRecursive(model.getDocumentedNodesRoot(), visible);
        }
    }

    private void selectUndocumentedNode(int direction) {
        Set<String> documentedNodes = ((XmlTreeCellRenderer) this.getCellRenderer()).getDocumentedNodes().keySet();

        XmlTreeUtils.TreeState initalTreeState = XmlTreeUtils.getTreeState(this);
        XmlTreeUtils.expandAll(this);

        int selectedRow = this.getSelectionCount() > 0 ? this.getSelectionRows()[0] : 0;
        int currentIndex = selectedRow + direction;
        if (currentIndex == this.getRowCount()) {
            currentIndex = 0;
        }

        if (currentIndex < 0) {
            currentIndex = this.getRowCount() - 1;
        }

        while (currentIndex != selectedRow) {
            TreePath currentPath = this.getPathForRow(currentIndex);
            XmlTreeNode node = (XmlTreeNode) currentPath.getLastPathComponent();

            if (node.isVisible() && XmlTreeUtils.needsDocumentation(node) && !documentedNodes.contains(node.getXPath())) {
                XmlTreeUtils.setTreeState(this, initalTreeState);

                this.setSelectionPath(currentPath);
                this.scrollPathToVisible(currentPath);
                break;
            }

            currentIndex += direction;
            if (currentIndex == this.getRowCount()) {
                currentIndex = 0;
            }

            if (currentIndex < 0) {
                currentIndex = this.getRowCount() - 1;
            }
        }
    }

    public void nextUndocumentedNode() {
        selectUndocumentedNode(1);
    }

    public void prevUndocumentedNode() {
        selectUndocumentedNode(-1);
    }
}
