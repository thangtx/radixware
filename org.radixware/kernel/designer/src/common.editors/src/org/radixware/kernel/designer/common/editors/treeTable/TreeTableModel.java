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


import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;


public interface TreeTableModel extends TreeModel
{
    public Component getTreeCellRendererComponent(JTree tree,
                    Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean hasFocus);
    public int getColumnCount();

    public String getColumnName(int column);

    public Class getColumnClass(int column);

    public Object getValueAt(Object node, int column);

    public boolean isCellEditable(Object node, int column);

    public void setValueAt(Object aValue, Object node, int column);
     
}
