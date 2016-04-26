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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.openide.util.NbBundle;


public class ChooseDialogUtils {

    public final static String WAIT = NbBundle.getMessage(ChooseDialogUtils.class, "Wait-Model");
    public final static String NO_ITEMS = NbBundle.getMessage(ChooseDialogUtils.class, "Empty-Model");
    public final static String INVALID = NbBundle.getMessage(ChooseDialogUtils.class, "Invalid-Model");

    public static DefaultTreeModel getTreeWaitModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root", true);
        DefaultTreeModel lm = new DefaultTreeModel(root);
        lm.setRoot(root);
        root.add(new DefaultMutableTreeNode(WAIT));
        return lm;
    }

    public static DefaultTreeModel getTreeEmptyModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root", true);
        DefaultTreeModel lm = new DefaultTreeModel(root);
        lm.setRoot(root);
        root.add(new DefaultMutableTreeNode(NO_ITEMS));
        return lm;
    }

    public static DefaultTreeModel getTreeInvalidModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root", true);
        DefaultTreeModel lm = new DefaultTreeModel(root);
        lm.setRoot(root);
        root.add(new DefaultMutableTreeNode(INVALID));
        return lm;
    }

    public static boolean isInvalidTreeModel(TreeModel model) {
        return model.getChildCount(model.getRoot()) == 1
                && model.getChild(model.getRoot(), 0).toString().equals(INVALID);
    }

    public static boolean isEmptyTreeModel(TreeModel model) {
        return model.getChildCount(model.getRoot()) == 1
                && model.getChild(model.getRoot(), 0).toString().equals(NO_ITEMS);
    }

    public static AbstractListModel getListWaitModel() {
        DefaultListModel lm = new DefaultListModel();
        lm.addElement(WAIT);
        return lm;
    }

    public static AbstractListModel getListEmptyModel() {
        DefaultListModel lm = new DefaultListModel();
        lm.addElement(NO_ITEMS);
        return lm;
    }

    @SuppressWarnings("unchecked")
    public static AbstractListModel getListInvalidModel() {
        DefaultListModel lm = new DefaultListModel();
        lm.addElement(INVALID);
        return lm;
    }

    public static boolean isInvalidListModel(ListModel lm) {
        return lm != null && lm.getSize() == 1 && lm.getElementAt(0).equals(INVALID);
    }

    public static boolean isEmptyListModel(ListModel lm) {
        return lm != null && lm.getSize() == 1 && lm.getElementAt(0).equals(NO_ITEMS);
    }

    public static boolean isWaitListModel(ListModel lm) {
        return lm != null && lm.getSize() == 1 && lm.getElementAt(0).equals(WAIT);
    }
}
