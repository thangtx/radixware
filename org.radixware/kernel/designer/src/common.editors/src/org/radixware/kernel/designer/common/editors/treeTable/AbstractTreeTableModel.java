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

package org.radixware.kernel.designer.common.editors.treeTable;

import javax.swing.tree.*;
import javax.swing.event.*;


public abstract class AbstractTreeTableModel extends DefaultTreeCellRenderer implements TreeTableModel {
    protected Object root;
    protected EventListenerList listenerList_ = new EventListenerList();

  
    public AbstractTreeTableModel(Object root) {
        this.root = root;
    }


    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {}

    // This is not called in the JTree's default mode: use a naive implementation.
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
	    if (getChild(parent, i).equals(child)) {
	        return i;
	    }
        }
	return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList_.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList_.remove(TreeModelListener.class, l);
    }

    protected void fireTreeNodesInserted(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList_.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
            }
        }
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {

        Object[] listeners = listenerList_.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
            }
        }
    }

    protected void fireTreeStructureChanged(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
      //*
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList_.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                 //Object[] path, int[] childIndices, Object[]
                Object[] path2 = new Object[0];
                int[] childIndices2 = new int[0];
                //new Object[] (0);
                if (e == null)
                    e = new TreeModelEvent(source, path2,
                                           childIndices2, children);
                ((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
            }
        }
      //   */
    }
 
    @Override
    public Class getColumnClass(int column) { return Object.class; }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return true;
         //return getColumnClass(column) == TreeTableModel.class;
    }




    /**/




}
