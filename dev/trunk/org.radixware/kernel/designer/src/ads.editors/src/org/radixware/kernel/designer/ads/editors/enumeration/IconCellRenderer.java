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

package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JLabel;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.types.Id;


class IconCellRenderer extends ColorCellRenderer {

    public IconCellRenderer(){
        super(false);
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component comp  = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ((JLabel) comp).setText("");
        if (value != null){
            if (value instanceof Id){
                AdsEnumItemDef item = ((EnumerationTableModel) table.getModel()).getViewItemByRow(row);
                if (item != null){
                    AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(item).findById((Id) value).get();
                    if (imageDef != null){
                        ((JLabel) comp).setIcon(imageDef.getIcon().getIcon(16, 16));
                    } else {
                        ((JLabel) comp).setIcon(null);
                    }
                } else {
                    ((JLabel) comp).setIcon(null);
                }
            } else {
                ((JLabel) comp).setIcon(null);
            }
        } else {
            ((JLabel) comp).setIcon(null);
        }
        return comp;
    }
}
