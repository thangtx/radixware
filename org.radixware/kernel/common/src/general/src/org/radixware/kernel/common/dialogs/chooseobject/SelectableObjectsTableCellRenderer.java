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
package org.radixware.kernel.common.dialogs.chooseobject;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dlastochkin
 */
public class SelectableObjectsTableCellRenderer extends DefaultTableCellRenderer {

    private final static int DARKER_COLOR_COMPONENT = 5;
    private final static int LIGHTER_COLOR_COMPONENT = 80;

    private Color fgColor;
    private Color fgColorLighter;
    private Color bgColor;
    private Color bgColorDarker;
    private Color bgSelectionColor;
    private Color fgSelectionColor;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        initColors(table);

        if (isSelected) {
            setForeground(fgSelectionColor);
            setBackground(bgSelectionColor);
        } else {
            if (column == 0) {
                setForeground(fgColor);                
            } else {
                setForeground(fgColorLighter);
            }
            setBackground(row % 2 == 0 ? bgColor : bgColorDarker);
        }                

        if (column == 0) {
            setIcon(((SelectableObjectDelegate) value).getIcon());
            setHorizontalAlignment(LEFT);
            setHorizontalTextPosition(RIGHT);
        } else {
            setText(((SelectableObjectDelegate) value).getLocation());
            setIcon(((SelectableObjectDelegate) value).getLocationIcon());            
            setHorizontalAlignment(RIGHT);
            setHorizontalTextPosition(LEFT);
        }
        
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        return this;
    }

    private void initColors(JTable table) {
        fgColor = table.getForeground();
        fgColorLighter = new Color(
                Math.min(255, fgColor.getRed() + LIGHTER_COLOR_COMPONENT),
                Math.min(255, fgColor.getGreen() + LIGHTER_COLOR_COMPONENT),
                Math.min(255, fgColor.getBlue() + LIGHTER_COLOR_COMPONENT));

        bgColor = table.getBackground();
        bgColorDarker = new Color(
                Math.abs(bgColor.getRed() - DARKER_COLOR_COMPONENT),
                Math.abs(bgColor.getGreen() - DARKER_COLOR_COMPONENT),
                Math.abs(bgColor.getBlue() - DARKER_COLOR_COMPONENT));
        bgSelectionColor = table.getSelectionBackground();
        fgSelectionColor = table.getSelectionForeground();
    }
}
