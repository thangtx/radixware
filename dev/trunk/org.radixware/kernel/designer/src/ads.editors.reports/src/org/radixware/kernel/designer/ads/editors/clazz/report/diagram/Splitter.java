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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;


class Splitter extends JComponent {

    private class MouseListener extends MouseInputAdapter {

        private int y;

        @Override
        public void mousePressed(final MouseEvent e) {
            y = e.getY();
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            final int dY = e.getY() - y;
            if (dY != 0) {
                final DragEvent event = new DragEvent(dY);
                for (DragListener l : dragListeners) {
                    l.onDrag(event);
                }
            }
        }
    }

    public static class DragEvent {

        private final int dY;

        public DragEvent(final int dY) {
            this.dY = dY;
        }

        public int getdY() {
            return dY;
        }
    }

    public static interface DragListener {

        public void onDrag(final DragEvent e);
    }
    public static final int HEIGHT_PX = 1;
    public static final Color COLOR = Color.DARK_GRAY;
    private final MouseListener mouseListener = new MouseListener();
    private final Set<DragListener> dragListeners = new HashSet<>();

    public Splitter() {
        this.addMouseMotionListener(mouseListener);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
    }

    public void addDragListener(final DragListener l) {
        dragListeners.add(l);
    }

    public void removeDragListener(final DragListener l) {
        dragListeners.remove(l);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final int width = getSize().width;
        final int height = getSize().height;

        g.setColor(Splitter.COLOR);
        g.fillRect(0, 0, width, height);
    }
}
