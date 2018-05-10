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

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author dlastochkin
 */
public class XmlTreeModel extends DefaultTreeModel {
    
    private final XmlTreeNode allNodesRoot;
    private final XmlTreeNode documentedNodesRoot;

    public XmlTreeModel(TreeNode root) {
        super(root);
        this.allNodesRoot = null;
        this.documentedNodesRoot = null;
    }

    public XmlTreeModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        this.allNodesRoot = null;
        this.documentedNodesRoot = null;
    }
    
    public XmlTreeModel(TreeNode root, XmlTreeNode allNodesRoot, XmlTreeNode documentedNodesRoot) {
        super(root);
        this.allNodesRoot = allNodesRoot;
        this.documentedNodesRoot = documentedNodesRoot;
    }
    
    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof XmlTreeNode) {
            return ((XmlTreeNode) parent).getChildAt(index);
        }
        return ((TreeNode) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof XmlTreeNode) {
            return ((XmlTreeNode) parent).getChildCount();
        }
        return ((TreeNode) parent).getChildCount();
    }

    @Override
    public Object getRoot() {        
        if (allNodesRoot != null && allNodesRoot.isVisible()) {
            if (documentedNodesRoot != null && !documentedNodesRoot.isVisible()) {
                return allNodesRoot;
            }
        }
        
        if (documentedNodesRoot != null && documentedNodesRoot.isVisible()) {
            if (allNodesRoot != null && !allNodesRoot.isVisible()) {
                return documentedNodesRoot;
            }
        }
        
        return super.getRoot();
//        return null;
    } 

    @Override
    protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
        TreeNode[]              retNodes;
        if(aNode == null) {
            if(depth == 0)
                return null;
            else
                retNodes = new TreeNode[depth];
        }
        else {
            depth++;
            if(isRoot(aNode))
                retNodes = new TreeNode[depth];
            else
                retNodes = getPathToRoot(aNode.getParent(), depth);
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }        
    
    public boolean isRoot(Object node) {
        XmlTreeNode rootNode = (XmlTreeNode) this.getRoot();
        return rootNode.getXPath().equals(((XmlTreeNode) node).getXPath());
    }
    
    public XmlTreeNode getAllNodesRoot() {
        return allNodesRoot;
    }

    public XmlTreeNode getDocumentedNodesRoot() {
        return documentedNodesRoot;
    }
}
