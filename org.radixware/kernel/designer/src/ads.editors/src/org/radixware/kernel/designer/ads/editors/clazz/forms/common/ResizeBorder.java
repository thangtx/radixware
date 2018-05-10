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

package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.border.Border;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.AdvancedSplitterItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.SplitterItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.AdvancedSplitterWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.SplitterWidget;


public final class ResizeBorder implements Border {

    private final BaseWidget wg;
    public static final int THICKNESS = 5;
    public final Color SPLITTER_COLOR = Color.green;
    public final Color COLOR = new Color(0x447BCD);

    public ResizeBorder(BaseWidget wg) {
        this.wg = wg;
    }

    @Override
    public Insets getInsets() {
        return new Insets(THICKNESS, THICKNESS, THICKNESS, THICKNESS);
    }

    private static Point center(Rectangle rectangle) {
        return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
    }

    @Override
    public void paint(Graphics2D gr, Rectangle bounds) {
        gr.setColor(COLOR);

        gr.fillRect(bounds.x, bounds.y, THICKNESS, THICKNESS);
        gr.fillRect(bounds.x + bounds.width - THICKNESS, bounds.y, THICKNESS, THICKNESS);
        gr.fillRect(bounds.x, bounds.y + bounds.height - THICKNESS, THICKNESS, THICKNESS);
        gr.fillRect(bounds.x + bounds.width - THICKNESS, bounds.y + bounds.height - THICKNESS, THICKNESS, THICKNESS);

        Point center = center(bounds);
        if (bounds.width >= THICKNESS * 5) {
            gr.fillRect(center.x - THICKNESS / 2, bounds.y, THICKNESS, THICKNESS);
            gr.fillRect(center.x - THICKNESS / 2, bounds.y + bounds.height - THICKNESS, THICKNESS, THICKNESS);
        }
        if (bounds.height >= THICKNESS * 5) {
            gr.fillRect(bounds.x, center.y - THICKNESS / 2, THICKNESS, THICKNESS);
            gr.fillRect(bounds.x + bounds.width - THICKNESS, center.y - THICKNESS / 2, THICKNESS, THICKNESS);
        }

        if (wg instanceof SplitterWidget) {
            paintSplitter(gr, bounds);
        }
    }

    private void paintSplitter(Graphics2D gr, Rectangle bounds) {
        final AdsUIItemDef splitter = (AdsUIItemDef) wg.getNode();

        Rectangle lr = bounds.getBounds();
        lr.grow(-THICKNESS, -THICKNESS);
        if (wg instanceof AdvancedSplitterWidget) {
            lr = AdvancedSplitterItem.DEFAULT.adjustLayoutGeometry(splitter, lr);
        } else {
            lr = SplitterItem.DEFAULT.adjustLayoutGeometry(splitter, lr);
        }

        int[] ps = LayoutUtil.getSplitterControlPoints(splitter, lr);
        if (ps == null) {
            return;
        }

        AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(splitter, "orientation");
        if (EOrientation.Horizontal.equals(orientation.value)) {
            if (bounds.width >= THICKNESS * 5) {
                for (int i = 0; i < ps.length; i++) {
                    paintSplitterPoint(gr, ps[i] - THICKNESS / 2, bounds.y);
                    paintSplitterPoint(gr, ps[i] - THICKNESS / 2, bounds.y + bounds.height - THICKNESS);
                }
            }
        } else {
            if (bounds.height >= THICKNESS * 5) {
                for (int i = 0; i < ps.length; i++) {
                    paintSplitterPoint(gr, bounds.x, ps[i] - THICKNESS / 2);
                    paintSplitterPoint(gr, bounds.x + bounds.width - THICKNESS, ps[i] - THICKNESS / 2);
                }
            }
        }
    }

    private void paintSplitterPoint(Graphics2D gr, int x, int y) {
        gr.setColor(SPLITTER_COLOR);
        gr.fillRect(x, y, THICKNESS, THICKNESS);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
