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

package org.radixware.kernel.designer.common.editors;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.JLabel;


public class AdsLogoViewer extends JLabel {

    private Image image;

    public AdsLogoViewer(){
        setBackground(Color.WHITE);
    }

    public void setImageIcon(Image image){
        this.image = image;
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null){
            Graphics2D g2d = (Graphics2D)g;
            if (!isEnabled()) {
                g2d.setComposite(AlphaComposite.getInstance(
                                 AlphaComposite.SRC_OVER, 0.3f));
            }

            g.drawImage(image, 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null)
           return new Dimension(image.getWidth(null), image.getHeight(null));
        return super.getPreferredSize();
    }

}
