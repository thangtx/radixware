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

package org.radixware.kernel.common.design.msdleditor.tree;

import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.design.msdleditor.Editor;
import org.radixware.kernel.common.design.msdleditor.Messages;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.RootMsdlScheme;


public class Tree extends JPanel {
    public JTree tree;
    public boolean lockTreeSelectionListener = false;
    private Editor editor;
    public Tree(final Editor editor, RootMsdlScheme fieldModel)  {
        this.editor = editor;
        setLayout(new BorderLayout());
        tree = new JTreeWithPopupMenu(this);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        RootStructureNode node = new RootStructureNode(this,fieldModel);
        ((DefaultTreeModel)tree.getModel()).setRoot(node);
        tree.addTreeSelectionListener(new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (lockTreeSelectionListener)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                DefaultMutableTreeNode parent = null;
                if (node != null && tree.getSelectionPath().getParentPath() != null)
                    parent = (DefaultMutableTreeNode)tree.getSelectionPath().getParentPath().getLastPathComponent();                
                 editor.newNodeSelected(node,parent);
            }
        } );

        JScrollPane areaScrollPane = new JScrollPane(tree);
        add(areaScrollPane,BorderLayout.CENTER);
    }

    public void selectRoot() {
        tree.setSelectionRow(0);
    }

    public void showErrors(ItemNode node) {
        if (node instanceof RootStructureNode) {
            editor.showErrors(((RootStructureNode)node).getRootMsdlScheme());
            return;
        }
        if (node instanceof FieldNode) {
            editor.showErrors(((FieldNode)node).getFieldModel());
            return;
        }
        if (node instanceof HeaderFieldsNode) {
            editor.showErrors(((HeaderFieldsNode)node).getFields());
            return;
        }
        if (node instanceof FieldsNode) {
            editor.showErrors(((FieldsNode)node).getFields());
            return;
        }
        if (node instanceof VariantsNode) {
            editor.showErrors(((VariantsNode)node).getFields());
            return;
        }
    }

    public void nameChanged() {
        TreePath treePath = tree.getSelectionPath();
        if (treePath != null) {
            DefaultMutableTreeNode cur = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            if (cur != null)
                ((DefaultTreeModel)tree.getModel()).nodeChanged(cur);
        }
    }
    
    public void addField() {
        if (tree.getSelectionPath()!=null) {
            DefaultMutableTreeNode owner = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
            if (owner instanceof IFieldsOwner) {
               MsdlField field = ((IFieldsOwner)owner).addField();
               ((ItemNode)owner).getChildren().refresh();
               ((ItemNode)owner).getChildren().structureChanged();
               ((ItemNode)owner).getChildren().setFieldInTree(field);
            }
        }
    }

    public void delField() {
        if (tree.getSelectionPath()!=null) {
            DefaultMutableTreeNode removed = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();       
            if (removed instanceof FieldNode) {
                if (JOptionPane.showConfirmDialog(null, Messages.DEL_FIELD,Messages.CONFIRMATION,JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                    return;
                TreePath parentPath = tree.getSelectionPath().getParentPath();
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                DefaultMutableTreeNode next = removed.getNextSibling();
                model.removeNodeFromParent(removed);
                ((IFieldsOwner)parentNode).delField(((FieldNode)removed).getFieldModel());
                if (next != null)
                    tree.setSelectionPath(parentPath.pathByAddingChild(next));
            }
        }
    }

    public void findNode(RadixObject radixObject) {
        /*
        Vector<MsdlField> path = new Vector<MsdlField>();
        MsdlField cur = (MsdlField)radixObject;
        path.add(cur);
        while (cur.getParentMsdlField() != null) {
            cur = cur.getParentMsdlField();
            path.add(cur);
        }
        TreeNode treeNode = (TreeNode)tree.getModel().getRoot();
        TreePath treePath = new TreePath(treeNode);

        lockTreeSelectionListener = true;
        tree.setScrollsOnExpand(true);
        tree.setExpandsSelectedPaths(true);
        tree.expandPath(treePath);
        tree.setSelectionPath(treePath);
        lockTreeSelectionListener = false;
         */
    }


}
