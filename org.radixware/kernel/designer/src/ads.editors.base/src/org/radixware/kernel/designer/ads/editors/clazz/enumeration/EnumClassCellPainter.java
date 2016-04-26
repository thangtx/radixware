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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.awt.Color;
import java.awt.Component;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;


class EnumClassCellPainter implements AdvanceTable.ICellPainter {

    private final Color addedItemColor = new Color(195, 218, 190);
    private final Color addedItemSelectedColor = new Color(138, 163, 134);
    private final Color addedItemFontColor = Color.BLACK;

    private final Color overwrittenItemColor = new Color(216, 216, 216);
    private final Color overwrittenItemSelectedColor = new Color(177, 177, 177);
    private final Color overwrittenItemFontColor = Color.BLACK;

    @Override
    public void paint(Component component, AdvanceTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        row = table.convertRowIndexToModel(row);
        Color bgColor, foreColor;

        if (table.getModel().getRowSourceScope(row) == AdvanceTableModel.ERowScope.OVERWRITE) {
            bgColor = isSelected ? overwrittenItemSelectedColor : overwrittenItemColor;
            foreColor = overwrittenItemFontColor;
        } else if (table.getModel().getRowSourceScope(row) == AdvanceTableModel.ERowScope.LOCAL) {
            bgColor = isSelected ? addedItemSelectedColor : addedItemColor;
            foreColor = addedItemFontColor;
        } else {
            if (!isSelected) {
                bgColor = Color.white;
                foreColor = Color.black;
            } else {
                return;
            }
        }

        component.setBackground(bgColor);
        component.setForeground(foreColor);
    }
}