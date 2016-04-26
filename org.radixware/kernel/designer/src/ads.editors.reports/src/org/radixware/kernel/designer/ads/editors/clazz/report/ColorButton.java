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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ColorButton extends JButton {

    protected class ColorIcon extends ImageIcon {
        
        private final Color color;
        
        public ColorIcon(Color color) {
            super(RadixWareIcons.EDIT.EDIT.getImage());
            this.color = color;
        }
        
        @Override
        public synchronized void paintIcon(final Component c, final Graphics g, final int x, final int y) {
            g.setColor(Color.BLACK);
            g.drawPolygon(new int[] {x, x + this.getIconWidth(), x + this.getIconWidth(), x},
                    new int[] {y, y, y + this.getIconHeight(), y + this.getIconHeight()}, 4);
            g.setColor(color);
            g.fillRect(x + 1, y + 1, this.getIconWidth() - 1, this.getIconHeight() - 1);
        }
        
    }

    private Color color;

    public ColorButton() {
        super();
        setColor(Color.WHITE);
    }

    public final void setColor(final Color color) {
        this.color = color;
        this.setIcon(new ColorIcon(color));
    }

    public Color getColor() {
        return color;
    }

}
