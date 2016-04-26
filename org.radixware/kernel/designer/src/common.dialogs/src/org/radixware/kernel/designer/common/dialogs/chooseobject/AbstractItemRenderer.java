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
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;

/**
 * Choose Radix object item visual renderer.
 a-kiliyevich
 */
public abstract class AbstractItemRenderer extends DefaultListCellRenderer implements ChangeListener {

    private static final Border border = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    private final JList list;
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

    public AbstractItemRenderer(JList list) {

        this.list = list;

        rendererComponent = new JPanel();
        rendererComponent.setLayout(new BorderLayout());
        rendererComponent.add(jlName, BorderLayout.WEST);
        rendererComponent.add(jlPath, BorderLayout.CENTER);

        jlName.setOpaque(false);
        jlPath.setOpaque(false);

        jlName.setFont(list.getFont());
        jlPath.setFont(list.getFont());

        jlName.setHorizontalAlignment(LEFT);
        jlName.setHorizontalTextPosition(RIGHT);

        jlPath.setHorizontalAlignment(RIGHT);
        jlPath.setHorizontalTextPosition(LEFT);

        fgColor = list.getForeground();
        fgColorLighter = new Color(
                Math.min(255, fgColor.getRed() + LIGHTER_COLOR_COMPONENT),
                Math.min(255, fgColor.getGreen() + LIGHTER_COLOR_COMPONENT),
                Math.min(255, fgColor.getBlue() + LIGHTER_COLOR_COMPONENT));

        bgColor = list.getBackground();
        bgColorDarker = new Color(
                Math.abs(bgColor.getRed() - DARKER_COLOR_COMPONENT),
                Math.abs(bgColor.getGreen() - DARKER_COLOR_COMPONENT),
                Math.abs(bgColor.getBlue() - DARKER_COLOR_COMPONENT));
        bgSelectionColor = list.getSelectionBackground();
        fgSelectionColor = list.getSelectionForeground();

        Container container = list.getParent();
        if (container instanceof JViewport) {
            ((JViewport) container).addChangeListener(this);
            jlName.setText("setHeight");
            jlName.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon());
            jlName.setBorder(border);
            list.setFixedCellHeight(jlName.getPreferredSize().height);
            stateChanged(new ChangeEvent(container));
        }
    }

    public abstract String getObjectName(Object object);

    public abstract String getObjectLocation(Object object);

    public abstract RadixIcon getObjectIcon(Object object);

    public abstract RadixIcon getObjectLocationIcon(Object object);

    private static String removeFgColorInHtml(final String s) {
        return s.replaceAll("\\<font color=\\\"[^\\\"]+\\\"\\>", "");
    }

    @Override
    public Component getListCellRendererComponent(JList list,
            Object object,
            int index,
            boolean isSelected,
            boolean hasFocus) {

        int height = list.getFixedCellHeight();
        int width = list.getFixedCellWidth() - 1;

        width = width < 200 ? 200 : width;

        Dimension size = new Dimension(width, height);
        rendererComponent.setMaximumSize(size);
        rendererComponent.setPreferredSize(size);

        String name = getObjectName(object);

        if (isSelected) {
            jlName.setForeground(fgSelectionColor);
            jlPath.setForeground(fgSelectionColor);
            rendererComponent.setBackground(bgSelectionColor);
            name = removeFgColorInHtml(name);
        } else {
            jlName.setForeground(fgColor);
            jlPath.setForeground(fgColorLighter);
            rendererComponent.setBackground(index % 2 == 0 ? bgColor : bgColorDarker);
        }

        jlName.setText(name);
        final RadixIcon iconInfo = getObjectIcon(object);
        jlName.setIcon(iconInfo != null ? iconInfo.getIcon() : null);
        jlName.setBorder(border);

        final String location = getObjectLocation(object);
        jlPath.setText(location);
        final RadixIcon locationIconInfo = getObjectLocationIcon(object);
        jlPath.setIcon(locationIconInfo != null ? locationIconInfo.getIcon() : null);

        return rendererComponent;
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        JViewport jv = (JViewport) event.getSource();
        list.setFixedCellWidth(jv.getExtentSize().width);
    }

    public JLabel getNameLabel() {
        return jlName;
    }

    public JLabel getPathLabel() {
        return jlPath;
    }
}
