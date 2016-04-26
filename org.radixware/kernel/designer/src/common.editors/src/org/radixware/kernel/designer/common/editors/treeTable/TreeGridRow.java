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

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.Icon;
import org.radixware.kernel.common.defs.RadixObject;



public abstract class TreeGridRow extends RadixObject{
    public abstract boolean isHaveChilds();
            //{return false;}
    public abstract boolean isMayModify(int column);
    public abstract void CellWasModified(int column, Object val);

    public abstract List<? extends TreeGridRow> getChilds();
    public abstract String getTitle(int column);
    public abstract Icon getIcon(int row);
    public Font getFont(int row){return null;};
    public Color getForeground(int row){return Color.BLACK;}
    public Color getBackground(int row){return Color.WHITE;}
    public abstract TreeGridModel.TreeGridNode getRowEx();
    public abstract void loadValues();
    public abstract void setRowEx(TreeGridModel.TreeGridNode row);
}