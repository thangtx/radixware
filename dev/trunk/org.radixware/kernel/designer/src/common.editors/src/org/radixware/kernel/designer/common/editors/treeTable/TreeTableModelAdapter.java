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

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;

public class TreeTableModelAdapter extends AbstractTableModel
{
    JTree tree;
    TreeGridModel treeGridModel;

    public TreeTableModelAdapter(TreeGridModel treeGridModel, JTree tree/*, JTreeTable2 tbl*/) {
        this.tree = tree;
        this.treeGridModel = treeGridModel;

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
              fireTableDataChanged();
            }
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
              fireTableDataChanged();
            }
        });


       
    }

    @Override
    public int getColumnCount() {
        return treeGridModel.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return treeGridModel.getColumnName(column);
    }

    @Override
    public Class getColumnClass(int column) {
        return treeGridModel.getColumnClass(column);
    }

    @Override
    public int getRowCount() {
        return tree.getRowCount();
    }

    protected Object nodeForRow(int row) {
        TreePath treePath = tree.getPathForRow(row);
        return treePath.getLastPathComponent();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return treeGridModel.getValueAt(nodeForRow(row), column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
         return treeGridModel.isCellEditable(nodeForRow(row), column);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        treeGridModel.setValueAt(value, nodeForRow(row), column);
    }

    protected void delayedFireTableDataChanged() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fireTableDataChanged();
            }
        });
    }
}

