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

package org.radixware.kernel.designer.ads.localization.translation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;


public class RoundedBorder implements Border{
        private final int radius;
        private final int shadowSize;

        public RoundedBorder(final int radius,final int shadowSize) {
            this.radius = radius;
            this.shadowSize=shadowSize;
        }

        @Override
        public Insets getBorderInsets(final Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            final Color saveColor=g.getColor();
            g.setColor(Color.lightGray);
           /* g.fillRoundRect(x+10,y+10,width-11,height-11,radius,radius);
            g.setColor(Color.white);
            g.fillRoundRect(x,y,width-10,height-11,radius,radius);
            g.setColor(Color.darkGray);
            g.drawRoundRect(x,y,width-10,height-11,radius,radius);
            g.setColor(saveColor);*/

            //drawShade( (Graphics2D) g,  x,  y,  width,  height);
            g.fillRoundRect(x+shadowSize,y+shadowSize,width-shadowSize,height-shadowSize,radius,radius);
            //Color color=new Color(239, 235, 231);
            g.setColor(Color.white);
            g.fillRoundRect(x,y,width-shadowSize,height-shadowSize,radius,radius);
            g.setColor(Color.darkGray);
            g.drawRoundRect(x,y,width-shadowSize,height-shadowSize,radius,radius);
            g.setColor(saveColor);
        }

        /*private void drawShade( Graphics2D g, int x, int y, int width, int height){
            Paint oldPaint = g.getPaint();

            Rectangle rectBottom=new Rectangle(x+6,height-12,width,height);

            GradientPaint greytowhite = new GradientPaint(rectBottom.x, rectBottom.y, Color.darkGray, rectBottom.x,rectBottom.height, Color.white);
            g.setPaint(greytowhite);
            g.fillRoundRect(x+6,y+6,width-6,height-6,radius,radius);
            //gradientFillRect( g,  rectBottom);

            Rectangle rectRight=new Rectangle(width-12,y+6,width,height);
            greytowhite = new GradientPaint(rectRight.x, rectRight.y, Color.darkGray, rectRight.width,rectRight.y, Color.white);
            g.setPaint(greytowhite);
            g.fillRoundRect(x+6,y+6,width-6,height-12,radius,radius);

            g.setPaint(oldPaint);
        }*/
}
