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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;


public class DashedBorder implements Border {

    private final int top;
    private final int bottom;
    private final int left;
    private final int right;
    private final Color color;
    private final int length;
    private final Insets ins;

    public DashedBorder(final int top, final int left, final int bottom, final int right, final Color color, final int length, final Insets ins) {
        this.color = color;
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.length = length;
        this.ins = ins;
    }

    @Override
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
        final Insets insets = getBorderInsets(c);
        g.setColor(color);
        final int numWide = Math.round(width / length);
        final int numHigh = Math.round(height / length);
        int startPoint;
        for (int i = 0; i <= numWide; i += 2) {
            startPoint = x + length * i;
            g.fillRect(startPoint, y, length, top);
            g.fillRect(startPoint, y + height - insets.bottom,
                    length, bottom);
        }
        for (int i = 0; i <= numHigh; i += 2) {
            startPoint = x + length * i;
            g.fillRect(x, startPoint, left, length);
            g.fillRect(x + width - insets.right, startPoint,
                    right, length);
        }
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public Insets getBorderInsets(final Component c) {
//            JComponent comp = (JComponent)textSamplePanel;
//            Dimension dim = comp.getPreferredSize();
//            Insets ins = new Insets(comp.getY() + 20, comp.getX() + 20,
//                    comp.getY() + 50, comp.getX() + 50);
//            Insets ins = new Insets(comp.getY(), comp.getX(),
//                    comp.getY() + dim.height, comp.getX() + dim.width);
        return ins;
    }
}
