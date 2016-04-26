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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;
import org.radixware.kernel.designer.common.editors.JTableX;


class PreviewObjectTitleFormatTableX extends JTableX<TitleItem> {

    @Override
    protected TableCellEditor getColumnCellEditorByRow(int row, int col) {
        if (col == PreviewObjectTitleFormatTableModel.COLUMN_VALUE) {
            if (columnEditors != null) {
                final int modelIndex = this.convertRowIndexToModel(row);
                final AdsObjectTitleFormatDef.TitleItem titleItem = ((PreviewObjectTitleFormatTableModel) this.getModel()).getTitleItem(modelIndex);

                TableCellEditor result =  columnEditors.getEditorForItem(titleItem);

                return result;
            }
        }
        return null;
    }
}
