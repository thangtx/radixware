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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;


public class AdsReportSelectionWidget extends JComponent {

    private class MouseListener extends MouseInputAdapter {

        private int getX(final MouseEvent e) {
            int result;
            if (e.getComponent() instanceof AdsReportBaseContainer) {
                result = e.getX() + e.getComponent().getLocation().x;
            } else {
                result = e.getX();                
            }
            return result;
        }

        private int getY(final MouseEvent e) {
            int result;
            if (e.getComponent() instanceof AdsReportBaseContainer) {
                result = e.getY() + e.getComponent().getLocation().y;
            } else {
                result = e.getY();                
            }
            return result;
        }
        int x = 0;
        int y = 0;

        @Override
        public void mousePressed(final MouseEvent e) {
            x = getX(e);
            y = getY(e);
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            if (isVisible()) {
                setVisible(false);
                selectCells();
            }
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            int width = getX(e) - x;
            int left;
            if (width > 0) {
                left = x;
            } else {
                width = -width;
                left = x - width;
            }

            int height = getY(e) - y;
            int top;
            if (height > 0) {
                top = y;
            } else {
                height = -height;
                top = y - height;
            }

            setLocation(left, top);
            setSize(width, height);
            if (width > 3 && height > 3) {
                setVisible(true);
            }
        }
    }
    private final MouseInputListener mouseListener = new MouseListener();

    public AdsReportSelectionWidget() {
        super();
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    private static final Color SELECTION_COLOR = new Color(100, 100, 255);

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

        super.paintComponent(g);

        // g.setColor(Color.black);
        //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.setColor(SELECTION_COLOR);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public void attachTo(final AbstractAdsReportWidget widget) {
        widget.addMouseListener(mouseListener);
        widget.addMouseMotionListener(mouseListener);
    }

    private void selectCells() {
        final AdsReportBandWidget bandWidget = (AdsReportBandWidget) getParent();

        final int x = this.getLocation().x - bandWidget.bandSubWidget.getLocation().x;
        final int y = this.getLocation().y - bandWidget.bandSubWidget.getLocation().y;
        final int width = this.getSize().width;
        final int height = this.getSize().height;

        AdsReportWidgetUtils.selectBand(bandWidget);
        for (AdsReportSelectableWidget cellWidget : bandWidget.getCellWidgets()) {
            final int centerX = cellWidget.getLocation().x + cellWidget.getSize().width / 2;
            final int centerY = cellWidget.getLocation().y + cellWidget.getSize().height / 2;
            final boolean selected = (centerX >= x && centerY >= y && centerX <= x + width && centerY <= y + height);
            cellWidget.setSelected(selected);
        }
    }
}
