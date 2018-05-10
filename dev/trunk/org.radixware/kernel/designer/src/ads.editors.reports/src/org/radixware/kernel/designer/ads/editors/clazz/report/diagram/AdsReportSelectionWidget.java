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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectionEvent;

public class AdsReportSelectionWidget extends JComponent {
    enum Mode {
        WINDOW, CROSS
    }

    private class MouseListener extends MouseInputAdapter {

        private int getX(final MouseEvent e) {
            int result = e.getX();
            Component c = e.getComponent();
            while (c != null && !(c instanceof AdsReportFormDiagram)) {
                result += c.getLocation().x;
                c = c.getParent();
            }
            return result;
        }

        private int getY(final MouseEvent e) {
            int result = e.getY();
            Component c = e.getComponent();
            while (c != null && !(c instanceof AdsReportFormDiagram)) {
                result += c.getLocation().y;
                c = c.getParent();
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
                selectCells(mode == Mode.WINDOW);
                e.consume();
            }
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            if (e.isConsumed()){
                return;
            }
            int width = getX(e) - x;
            int left;
            if (width > 0) {
                left = x;
                mode = Mode.WINDOW;
            } else {
                width = -width;
                left = x - width;
                mode = Mode.CROSS;
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
    private static final Color ALL_SELECTION_COLOR = new Color(100, 255, 100);
    private Mode mode = Mode.WINDOW;

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

        super.paintComponent(g);

        // g.setColor(Color.black);
        //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        if (mode == Mode.CROSS){
            g.setColor(ALL_SELECTION_COLOR);
        } else {
            g.setColor(SELECTION_COLOR);
        }
        
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public void attachTo(final AbstractAdsReportWidget widget) {
        widget.addMouseListener(mouseListener);
        widget.addMouseMotionListener(mouseListener);
    }
    
    public void detachFrom(final AbstractAdsReportWidget widget) {
        widget.removeMouseListener(mouseListener);
        widget.removeMouseMotionListener(mouseListener);
    }

    private void selectCells(boolean isAll) {
        final AdsReportFormDiagram form = (AdsReportFormDiagram) getParent();
        boolean selectionChange = false;
        for (AdsReportBandWidget bandWidget : form.getBandWidgets()) {
            final Dimension size = bandWidget.bandSubWidget.getSize();
            final int x = bandWidget.getX() + bandWidget.bandSubWidget.getX();
            final int y = bandWidget.getY() + bandWidget.bandSubWidget.getY();
            if (contains(false, x, y, (int) size.getWidth(), (int) size.getHeight())) {
                if (contains(isAll, x, y, (int) size.getWidth(), (int) size.getHeight())) {
                    selectionChange = selectWidget(selectionChange, form, bandWidget);
                }

                selectionChange = selectedCells(isAll, bandWidget.bandSubWidget, form, selectionChange, x, y);
            }

            for (AdsSubReportWidget subReportWidget : bandWidget.getSubReportWidgets()) {
                final int subReportX = bandWidget.getX() + subReportWidget.getX();
                final int subReportY = bandWidget.getY() + subReportWidget.getY();
                final Dimension subReportSize = subReportWidget.getSize();
                if (contains(isAll, subReportX, subReportY, (int) subReportSize.getWidth(), (int) subReportSize.getHeight())) {
                    selectionChange = selectWidget(selectionChange, form, subReportWidget);
                }
            }

        }
    }

    public boolean selectedCells(final boolean isAll, AdsReportBaseContainer subBandWidget, AdsReportFormDiagram form, boolean selectionChange, int subBandX, int subBandY) {
        for (AdsReportSelectableWidget cellWidget : subBandWidget.getCellWidgets()) {
            final int cellX = subBandX + cellWidget.getX();
            final int cellY = subBandY + cellWidget.getY();
            if (contains(false, cellX, cellY, (int) cellWidget.getWidth(), (int) cellWidget.getHeight())) {
                if (contains(isAll, cellX, cellY, (int) cellWidget.getWidth(), (int) cellWidget.getHeight())) {
                    selectionChange = selectWidget(selectionChange, form, cellWidget);
                }
                if (cellWidget.getCell().isReportContainer()) {
                    selectionChange = selectedCells(isAll, (AdsReportBaseContainer) cellWidget, form, selectionChange, cellX, cellY);
                }
            }
        }
        return selectionChange;
    }

    public boolean selectWidget(boolean selectionChange, AdsReportFormDiagram form, AdsReportAbstractSelectableWidget widget) {
        if (!selectionChange) {
            form.fireSelectionChanged(new SelectionEvent(form, true));
            selectionChange = true;
        }
        form.fireSelectionChanged(new SelectionEvent(widget, true, false));
        return selectionChange;
    }

    public boolean contains(boolean isAll, int x, int y, int width, int height) {
        Point location = getLocation();
        if (isAll) {
            return (x >= location.x) && (x + width <= location.x + getWidth()) && (y >= location.y) && (y + height <= location.y + getHeight());
        } else {
            return (x + width >= location.x) && (x <= location.x + getWidth()) && (y + height >= location.y) && (y <= location.y + getHeight());
        }
    }
}
