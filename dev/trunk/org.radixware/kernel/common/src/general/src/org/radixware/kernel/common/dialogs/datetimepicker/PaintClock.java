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
package org.radixware.kernel.common.dialogs.datetimepicker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Calendar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PaintClock extends JPanel {

    private Image clockImg;
    private int hour;
    private int min;
    private int sec;

    public PaintClock(IImageDistributor img) {
        clockImg = img.getClockImg();
        setOpaque(true);
        setPreferredSize(new Dimension(150, 150));
    }

    public PaintClock(Calendar time, IImageDistributor img) {
        this(img);
        setTime(time);
    }

    public final void setTime(Calendar time) {
        this.hour = time.get(Calendar.HOUR) * 30 + time.get(Calendar.MINUTE) / 2;
        this.min = time.get(Calendar.MINUTE) * 6 + time.get(Calendar.SECOND) / 10;
        this.sec = time.get(Calendar.SECOND) * 6;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                revalidate();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(clockImg, 0, 0, null);
        double pi = Math.PI;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform at = new AffineTransform();
        at.rotate(hour * pi / 180.0, 75, 75);
        g2d.transform(at);
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(4.0f));
        g2d.drawLine(75, 75, 75, 40);

        at.rotate(-2 * hour * pi / 180.0, 75, 75);
        at.rotate(min * pi / 180.0, 75, 75);
        g2d.transform(at);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(75, 75, 75, 30);

        at.rotate(hour * pi / 180.0, 75, 75);
        at.rotate(- 2 * min * pi / 180.0, 75, 75);
        at.rotate(sec * pi / 180.0, 75, 75);
        g2d.transform(at);
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(75, 75, 75, 30);

        at.rotate(min * pi / 180.0, 75, 75);
        at.rotate(-2 * sec * pi / 180.0, 75, 75);
        g2d.transform(at);
    }
}
