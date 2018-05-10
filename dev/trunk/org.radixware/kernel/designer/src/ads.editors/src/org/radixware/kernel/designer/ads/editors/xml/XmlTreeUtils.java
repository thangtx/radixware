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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.utils.XPathUtils;

/**
 *
 * @author dlastochkin
 */
public class XmlTreeUtils {

    public static class TreeState {

        final String expansionState;

        public TreeState(String expansionState) {
            this.expansionState = expansionState;
        }

        public String getExpansionState() {
            return expansionState;
        }
    }

    public static TreeState getTreeState(JTree tree) {
        StringBuilder expansionState = new StringBuilder();
        for (int i = 0; i < tree.getRowCount(); i++) {
            if (tree.isExpanded(i)) {
                expansionState.append(tree.getPathForRow(i).toString()).append(",");
            }
        }

        return new TreeState(expansionState.toString());
    }

    public static void setTreeState(JTree tree, TreeState state) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            if (state.getExpansionState().contains(tree.getPathForRow(i).toString())) {
                tree.expandRow(i);
            } else {
                tree.collapseRow(i);
            }
        }
    }

    public static List<String> getNodesXPathList(JTree tree) {
        List<String> result = new ArrayList<>();
        List<XmlTreeNode> treeNodes = getTreeNodes(tree.getModel(), tree.getModel().getRoot());

        for (XmlTreeNode node : treeNodes) {
            result.add(node.getXPath());
        }

        return result;
    }

    public static List<XmlTreeNode> getTreeNodes(TreeModel model, Object node) {
        List<XmlTreeNode> result = new ArrayList<>();
        if (!model.isLeaf(node)) {
            for (int i = 0; i < model.getChildCount(node); i++) {
                result.addAll(getTreeNodes(model, model.getChild(node, i)));
            }
        }
        if (node instanceof XmlTreeNode) {
            result.add(((XmlTreeNode) node));
        }

        return result;
    }

    static Pattern getRegExpPattern(String regExp) {
        final StringBuilder regexBuilder = new StringBuilder("");
        int lastWildCardPosition = 0;
        boolean isNeedChildPattern = regExp.length() > 1 && regExp.endsWith("/");

        for (int i = 0; i < regExp.length(); i++) {
            if (i == regExp.length() - 2 && isNeedChildPattern) {
                break;
            }
            if (regExp.charAt(i) == '?') {
                regexBuilder.append(regExp.substring(lastWildCardPosition, i));
                regexBuilder.append('.');
                lastWildCardPosition = i + 1;
            } else if (regExp.charAt(i) == '*') {
                regexBuilder.append(regExp.substring(lastWildCardPosition, i));
                regexBuilder.append(".*");
                lastWildCardPosition = i + 1;
            }
        }
        if (!isNeedChildPattern) {
            regexBuilder.append(regExp.substring(lastWildCardPosition, regExp.length()));
        } else {
            regexBuilder.append(regExp.substring(lastWildCardPosition, regExp.length() - 2));
        }
        regexBuilder.append(".*");

        Pattern pattern = null;
        try {
            pattern = Pattern.compile(regexBuilder.toString(), Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException ex) {
            Logger.getLogger(XmlTreeUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }

        return pattern;
    }

    public static void expandAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }        

    public static void collapseAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.collapseRow(i);
        }
    }
    
    public static void collapseRecursive(JTree tree) {
        for (int i = tree.getRowCount(); i > 0; i--) {
            tree.collapseRow(i);
        }
    }

    public static void addTreeRMBListener(final JTree tree, final JMenuItem item) {
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showMenu(e);
            }

            private void showMenu(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    TreePath pathForLocation = tree.getPathForLocation(e.getPoint().x, e.getPoint().y);
                    if (pathForLocation != null) {
                        XmlTreeNode selectedNode = (XmlTreeNode) pathForLocation.getLastPathComponent();
                        XmlTreeCellRenderer render = (XmlTreeCellRenderer) tree.getCellRenderer();
                        tree.setSelectionPath(pathForLocation);
                        if (render.getDocumentedNodes().keySet().contains(selectedNode.getXPath())) {
                            JPopupMenu menu = new JPopupMenu();
                            menu.add(item);
                            menu.show(tree, e.getX(), e.getY());
                        }
                    }
                }
            }
        });
    }

    public static boolean needsDocumentation(XmlTreeNode node) {
        if (node != null && node.getElement() != null && node.getElement().getNodeName() != null) {
            return XPathUtils.isElementNeedsDoc(node.getElement());
        }
        return false;
    }

    public static int getDocumentedElementsCount(JTree tree) {
        int result = 0;

        List<XmlTreeNode> treeNodes = getTreeNodes(tree.getModel(), tree.getModel().getRoot());
        Set<String> documentedNodes = ((XmlTreeCellRenderer) tree.getCellRenderer()).getDocumentedNodes().keySet();

        for (XmlTreeNode node : treeNodes) {
            if (needsDocumentation(node) && documentedNodes.contains(node.getXPath())) {
                result++;
            }
        }

        return result;
    }

    public static int getUndocumentedElementsCount(JTree tree) {
        int result = 0;
        List<XmlTreeNode> treeNodes = getTreeNodes(tree.getModel(), tree.getModel().getRoot());

        for (XmlTreeNode node : treeNodes) {
            if (needsDocumentation(node)) {
                result++;
            }
        }

        return result;
    }

    public static boolean setSelectedNode(final JTree tree, final String xPath) {
        if (xPath == null) {
            return false;
        }

        if (tree.getSelectionPath() != null) {
            XmlTreeNode node = (XmlTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (xPath.equals(node.getXPath())) {
                return true;
            }
        }

        boolean isNeedFindRoot = xPath.equals(((XmlTreeNode) tree.getModel().getRoot()).getXPath());
        if (isNeedFindRoot) {
            tree.setSelectionPath(tree.getPathForRow(0));
            tree.scrollPathToVisible(tree.getPathForRow(0));
            return true;
        }

        List<TreeSelectionListener> listeners = Arrays.asList(tree.getTreeSelectionListeners());
        for (TreeSelectionListener listener : listeners) {
            tree.removeTreeSelectionListener(listener);
        }

        boolean isFound = false;

        TreeState initalTreeState = getTreeState(tree);
        expandAll(tree);
        for (int i = 0; i < tree.getRowCount(); i++) {
            TreePath currentPath = tree.getPathForRow(i);
            XmlTreeNode node = (XmlTreeNode) currentPath.getLastPathComponent();
            if (xPath.equals(node.getXPath())) {
                setTreeState(tree, initalTreeState);

                tree.setSelectionPath(currentPath);
                tree.scrollPathToVisible(currentPath);
                isFound = true;
                break;
            }
        }
        for (TreeSelectionListener listener : listeners) {
            tree.addTreeSelectionListener(listener);
        }
        return isFound;
    }
}
