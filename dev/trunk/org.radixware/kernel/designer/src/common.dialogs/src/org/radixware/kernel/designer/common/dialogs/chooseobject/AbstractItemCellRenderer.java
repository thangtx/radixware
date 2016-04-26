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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;


public abstract class AbstractItemCellRenderer extends DefaultTableCellRenderer implements ChangeListener {

    private static final Border border = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    private final JTable table;
    private JPanel rendererComponent;
    private JLabel jlName = new JLabel();
    private JLabel jlPath = new JLabel();
    private int DARKER_COLOR_COMPONENT = 5;
    private int LIGHTER_COLOR_COMPONENT = 80;
    private Color fgColor;
    private Color fgColorLighter;
    private Color bgColor;
    private Color bgColorDarker;
    private Color bgSelectionColor;
    private Color fgSelectionColor;

    public AbstractItemCellRenderer(JTable table) {

        this.table = table;

        rendererComponent = new JPanel();
        rendererComponent.setLayout(new BorderLayout());
        rendererComponent.add(jlName, BorderLayout.WEST);
        rendererComponent.add(jlPath, BorderLayout.CENTER);

        jlName.setOpaque(false);
        jlPath.setOpaque(false);

        jlName.setFont(table.getFont());
        jlPath.setFont(table.getFont());

        jlName.setHorizontalAlignment(LEFT);
        jlName.setHorizontalTextPosition(RIGHT);

        jlPath.setHorizontalAlignment(RIGHT);
        jlPath.setHorizontalTextPosition(LEFT);

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

        Container container = table.getParent();
        if (container instanceof JViewport) {
            ((JViewport) container).addChangeListener(this);
            jlName.setText("setHeight");
            jlName.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon());
            jlName.setBorder(border);
            table.setRowHeight(jlName.getPreferredSize().height);
            stateChanged(new ChangeEvent(container));
        }
    }

    public abstract String getObjectName(Object object, int row, int column);

    public abstract String getObjectLocation(Object object, int row, int column);

    public abstract RadixIcon getObjectIcon(Object object, int row, int column);

    public abstract RadixIcon getObjectLocationIcon(Object object, int row, int column);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            jlName.setForeground(fgSelectionColor);
            jlPath.setForeground(fgSelectionColor);
            rendererComponent.setBackground(bgSelectionColor);
        } else {
            jlName.setForeground(fgColor);
            jlPath.setForeground(fgColorLighter);
            rendererComponent.setBackground(row % 2 == 0 ? bgColor : bgColorDarker);
        }

        final String name = getObjectName(value, row, column);
        jlName.setText(name);
        final RadixIcon iconInfo = getObjectIcon(value, row, column);
        jlName.setIcon(iconInfo != null ? iconInfo.getIcon() : null);
        jlName.setBorder(border);

        final String location = getObjectLocation(value, row, column);
        jlPath.setText(location);
        final RadixIcon locationIconInfo = getObjectLocationIcon(value, row, column);
        jlPath.setIcon(locationIconInfo != null ? locationIconInfo.getIcon() : null);

        return rendererComponent;
    }

    @Override
    public void stateChanged(ChangeEvent event) {
      /*  JViewport jv = (JViewport) event.getSource();
        final int width = jv.getExtentSize().width;
        if (width > 0){
            table.setRowHeight(width);
        }*/
    }
}

