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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;


class ColorCellRenderer extends DefaultTableCellRenderer {

    private static final Color deprecatedItemBackgroundColor = new Color(216, 210, 210);
    private static final Color deprecatedItemSelectedBackgroundColor = new Color(180, 168, 170);
    private static final Color deprecatedItemForegroundColor = Color.BLACK;
    private static final Color addedItemBackgroundColor = new Color(195, 218, 190);
    private static final Color addedItemSelectedBackgroundColor = new Color(138, 163, 134);
    private static final Color addedItemForegroundColor = Color.BLACK;
    private static final Color overwrittenItemBackgroundColor = new Color(216, 216, 216);
    private static final Color overwrittenItemSelectedBackgroundColor = new Color(177, 177, 177);
    private static final Color overwrittenItemForegroundColor = Color.BLACK;

    private boolean isHexMode = false;

    public ColorCellRenderer(boolean isHexMode) {
        super();
        this.isHexMode = isHexMode;        
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final EnumerationTableModel tableModel = (EnumerationTableModel) table.getModel();

        String tooltip = null;

        final Component comp;
        if (value instanceof String || value instanceof Number){

             String preparedValue = value.toString();
             if (value instanceof Long && isHexMode){
                 preparedValue = Long.toHexString((Long) value).toUpperCase();
             }

             final String strValue = " " + preparedValue + " ";

             if (tableModel.isDeprecatedItem(row)){
                 comp = super.getTableCellRendererComponent(table, "<HTML><STRIKE>" + strValue + "</STRIKE></HTML>", isSelected, hasFocus, row, column);
             } else if (tableModel.isOverwrittenItem(row)){
                 comp = super.getTableCellRendererComponent(table, "<HTML><B>" + strValue + "</B></HTML>", isSelected, hasFocus, row, column);
             }else{
                 comp = super.getTableCellRendererComponent(table, strValue, isSelected, hasFocus, row, column);
             }
        }else{
             comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        if (isSelected) {
            if (tableModel.isDeprecatedItem(row)) {
                comp.setBackground(ColorCellRenderer.deprecatedItemSelectedBackgroundColor);
                comp.setForeground(ColorCellRenderer.deprecatedItemForegroundColor);
                tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Deprecated");
            } else if (tableModel.isOverwrittenItem(row)){
                comp.setBackground(ColorCellRenderer.overwrittenItemSelectedBackgroundColor);
                comp.setForeground(ColorCellRenderer.overwrittenItemForegroundColor);
                tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Overwritten");
            } else if (tableModel.isAddedItem(row) && !tableModel.isNativeEnum()) {
                comp.setBackground(ColorCellRenderer.addedItemSelectedBackgroundColor);
                comp.setForeground(ColorCellRenderer.addedItemForegroundColor);
                tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Own");
            } else {
                if (!tableModel.isNativeEnum()){
                    tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Inherited");
                }
            }
        } else {
            if (tableModel.isDeprecatedItem(row)) {
                comp.setBackground(ColorCellRenderer.deprecatedItemBackgroundColor);
                comp.setForeground(ColorCellRenderer.deprecatedItemForegroundColor);
                tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Deprecated");
            } else if (tableModel.isOverwrittenItem(row)){
                comp.setBackground(ColorCellRenderer.overwrittenItemBackgroundColor);
                comp.setForeground(ColorCellRenderer.overwrittenItemForegroundColor);
                tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Overwritten");
            } else if (tableModel.isAddedItem(row) && !tableModel.isNativeEnum()) {
                comp.setBackground(ColorCellRenderer.addedItemBackgroundColor);
                comp.setForeground(ColorCellRenderer.addedItemForegroundColor);
                tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Own");
            } else {
                comp.setBackground(null);
                comp.setForeground(null);
                if (!tableModel.isNativeEnum()){
                    tooltip = NbBundle.getMessage(ColorCellRenderer.class, "ItemTableToolTips-Inherited");
                }
            }
        }

        if (comp instanceof JLabel){
            ((JLabel) comp).setToolTipText(tooltip); 
        }

        return comp;
    }
}
