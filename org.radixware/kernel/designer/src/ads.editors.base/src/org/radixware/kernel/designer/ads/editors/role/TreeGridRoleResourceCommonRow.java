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

package org.radixware.kernel.designer.ads.editors.role;

import java.awt.Color;
import javax.swing.Icon;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridModel;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridRow;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTable;


public abstract class TreeGridRoleResourceCommonRow extends TreeGridRow {
    
    protected Icon icon;
    protected String title;
    protected AdsRoleDef role;
    protected String resHash;
    
    protected String hash;
    protected TreeTable tbl;
    protected Restrictions res;
    protected boolean isMayChild;
    protected Id incorrectId;
    protected AdsEditorPresentationDef presentation;
    protected TreeGridModel.TreeGridNode row = null;
    
   @Override
   public TreeGridModel.TreeGridNode getRowEx()
   {
       return row;
   }
    @Override
   public void setRowEx(TreeGridModel.TreeGridNode row)
   {
       this.row = row;
   }

     @Override
    public Icon getIcon(int row) {
        return icon;
    }


        @Override
    public boolean isHaveChilds() {
        return isMayChild;
    }
        
        @Override
    public String getTitle(int column) {
        if (column==0)
            return title;
       return null;
    }

        
        
        @Override
        public Color getBackground(int row) {

            return Color.GRAY;
            //super.getBackground(row)
        }
    
}
