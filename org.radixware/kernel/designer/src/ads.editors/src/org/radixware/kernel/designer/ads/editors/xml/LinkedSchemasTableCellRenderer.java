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
package org.radixware.kernel.designer.ads.editors.xml;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.EXmlSchemaLinkMode;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Pair;

/**
 *
 * @author dlastochkin
 */
public class LinkedSchemasTableCellRenderer extends DefaultTableCellRenderer {

    private final static int DARKER_COLOR_COMPONENT = 5;
    private final static int LIGHTER_COLOR_COMPONENT = 80;

    private Color fgColor;
    private Color bgColor;
    private Color bgColorDarker;
    private Color bgSelectionColor;
    private Color fgSelectionColor;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Pair<AdsXmlSchemeDef, EXmlSchemaLinkMode> valuePair = (Pair<AdsXmlSchemeDef, EXmlSchemaLinkMode>) value;

        RadixIcon defIcon = valuePair.getFirst().getIcon();
        RadixIcon locationIcon = valuePair.getFirst().getOwnerForQualifedName() == null ? null : valuePair.getFirst().getOwnerForQualifedName().getIcon();

        String defTitle = valuePair.getFirst().getName();
        String location = valuePair.getFirst().getOwnerForQualifedName() == null ? null : valuePair.getFirst().getOwnerForQualifedName().getQualifiedName();

        initColors(table);

        if (isSelected) {
            setForeground(fgSelectionColor);
            setBackground(bgSelectionColor);
        } else {
            setForeground(fgColor);
            setBackground(row % 2 == 0 ? bgColor : bgColorDarker);
        }

        if (column == 0) {
            setText(defTitle);
            setIcon(defIcon.getIcon());
            setHorizontalAlignment(LEFT);
            setHorizontalTextPosition(RIGHT);
        } else if (column == 1) {
            setText(location);
            if (locationIcon != null) {
                setIcon(locationIcon.getIcon());
            }
            setHorizontalAlignment(RIGHT);
            setHorizontalTextPosition(LEFT);
        } else {
            setText(valuePair.getSecond().getValue());
            setHorizontalAlignment(CENTER);
            setIcon(null);
        }

        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));              

        return this;
    }

    private void initColors(JTable table) {
        fgColor = table.getForeground();
        
        bgColor = table.getBackground();
        bgColorDarker = new Color(
                Math.abs(bgColor.getRed() - DARKER_COLOR_COMPONENT),
                Math.abs(bgColor.getGreen() - DARKER_COLOR_COMPONENT),
                Math.abs(bgColor.getBlue() - DARKER_COLOR_COMPONENT));
        bgSelectionColor = table.getSelectionBackground();
        fgSelectionColor = table.getSelectionForeground();
    }
}
