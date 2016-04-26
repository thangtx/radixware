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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;


public class PopupButton extends JButton {

    private JPopupMenu popupMenu;
    int margin = 2;
    int lenght = 5;

    public PopupButton(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (PopupButton.this.isEnabled()) {
                    PopupButton.this.popupMenu.show(PopupButton.this, 0, getHeight());
                }
            }
        });
        setFocusPainted(false);
        setMargin(new Insets(margin, margin, margin, margin));
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        int alpha = isEnabled() ? 192 : 64;
        g.setColor(new Color(getForeground().getRed(), getForeground().getGreen(), getForeground().getBlue(), alpha));
        Path2D.Float triangle = new Path2D.Float();
        int w = getWidth();
        int h = getHeight();
        triangle.moveTo(w - margin, h - lenght - margin);
        triangle.lineTo(w - lenght - margin, h - margin);
        triangle.lineTo(w - margin, h - margin);
        ((Graphics2D) g).fill(triangle);
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
