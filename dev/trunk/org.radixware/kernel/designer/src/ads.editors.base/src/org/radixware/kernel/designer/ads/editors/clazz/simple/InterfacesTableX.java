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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.designer.common.editors.JTableX;


class InterfacesTableX extends JTableX<AdsClassDef>{

    @Override
    protected TableCellEditor getColumnCellEditorByRow(int row, int col) {
        if (col == InterfacesTableModel.INTERFACE_СOLUMN_INDEX && columnEditors != null) {
            
                final int modelIndex = this.convertRowIndexToModel(row);
                final AdsClassDef item = ((InterfacesTableModel) this.getModel()).getInterface(modelIndex);
                if (item != null){
                    return columnEditors.getEditorForItem(item);
                }
            
        }
        return null;
    }
}
