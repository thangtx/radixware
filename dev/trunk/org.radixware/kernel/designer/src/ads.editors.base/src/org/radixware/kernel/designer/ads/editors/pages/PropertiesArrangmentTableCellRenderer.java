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

package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport.PropertyRef;


class PropertiesArrangmentTableCellRenderer extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        assert(value == null || value instanceof PropertyRef);
        final Component comp = super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        if (value != null){
            final PropertyRef propertyRef  = (PropertyRef) value;
            final AdsDefinition property = propertyRef.findProperty();
            final String title = property == null ? propertyRef.getPropertyId().toString() : property.getName();            
            ((JLabel) comp).setText(title); 
            if (property == null){
                comp.setForeground(Color.RED);
            }else{
                comp.setForeground(null);
            }
        }
        return comp;
    }
}
