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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ActionItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ToolBarItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public final class ToolBarLayout implements Layout {

    public ToolBarLayout() {
    }

    @Override
    public void layout(Widget widget) {
        justify(widget);
    }

    @Override
    public boolean requiresJustification(Widget widget) {
        return true;
    }

    @Override
    public void justify(Widget widget) {
        synchronized (LayoutProcessor.LOCK) {
            final AdsWidgetDef node = (AdsWidgetDef) ((BaseWidget) widget).getNode();
            if (ToolBarItem.getOrientation(node) == EOrientation.Vertical) {
                justifyVertical(node, widget);
            } else {
                justifyHorizontal(node, widget);
            }
        }
    }

    private void justifyHorizontal(AdsWidgetDef node, Widget widget) {
        final Rectangle geometry = ((BaseWidget) widget).getLayoutGeometry();
        Point start = getStartPoint(geometry, EOrientation.Horizontal);

        for (final BaseWidget wdg : ToolBarLayoutProcessor.getWidgets(node, (GraphScene) widget.getScene())) {
            final Rectangle calcRect = calcHorizontalRect(start, geometry,
                    ActionItem.calcActionSize(getFontMetrics(widget.getScene()), (AdsWidgetDef) wdg.getNode()));

            start = new Point(start.x + calcRect.width, start.y);
            if (!wdg.isSelfContained()) {
                wdg.setGeometry(calcRect);
                wdg.saveGeometry();
            }
        }

        widget.getScene().repaint();
    }

    private FontMetrics getFontMetrics(Scene scene) {
        return scene.getGraphics().getFontMetrics(DrawUtil.DEFAULT_FONT);
    }

    private void justifyVertical(AdsWidgetDef node, Widget widget) {
        final Rectangle geometry = ((BaseWidget) widget).getLayoutGeometry();
        Point start = getStartPoint(geometry, EOrientation.Vertical);

        for (final BaseWidget wdg : ToolBarLayoutProcessor.getWidgets(node, (GraphScene) widget.getScene())) {
            final Rectangle calcRect = calcVerticalRect(start, geometry,
                    ActionItem.calcActionSize(getFontMetrics(widget.getScene()), (AdsWidgetDef) wdg.getNode()));

            start = new Point(start.x, start.y + calcRect.height + ToolBarLayoutProcessor.INTERVAL);
            if (!wdg.isSelfContained()) {
                wdg.setGeometry(calcRect);
                wdg.saveGeometry();
            }
        }

        widget.getScene().repaint();
    }

    public static Rectangle calcHorizontalRect(Point start, Rectangle toolGeometry, Dimension wdgSize) {
        final int height = Math.min(toolGeometry.height, wdgSize.height);
        return new Rectangle(start.x, start.y - height / 2, wdgSize.width, height);
    }

    public static Rectangle calcVerticalRect(Point start, Rectangle toolGeometry, Dimension wdgSize) {
        final int width = toolGeometry.width;
        return new Rectangle(start.x - width / 2, start.y, width, wdgSize.height);
    }

    public static Point getStartPoint(Rectangle rect, EOrientation orientation) {
        if (orientation == EOrientation.Horizontal) {
            return new Point(rect.x, rect.y + rect.height / 2);
        } else {
            return new Point(rect.x + rect.width / 2, rect.y + ToolBarLayoutProcessor.INTERVAL);
        }
    }
}
