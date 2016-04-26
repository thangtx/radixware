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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IAdjuster;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IWidgetPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPainterAdapter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;

/**
 * It is recommended to use instead of {@link Item}
 */
public class WidgetItem extends Item {

    private static class DefaultWidgetPainter extends WidgetPainterAdapter {

        private Color color;

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            if (color == null) {
                Random rand = new Random();
                color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            }
            DrawUtil.drawShadeRect(graphics, rect.x, rect.y, rect.width, rect.height, false, 1, 0, color);
        }
    }
    private final IWidgetPainter painter;
    private final WidgetPropertyCollector propertyCollector;

    public WidgetItem(Group group, String title, RadixIcon icon, String clazz, IWidgetPainter painter, WidgetPropertyCollector propertyCollector) {
        super(group, title, icon, clazz);

        this.painter = painter != null ? painter : new DefaultWidgetPainter();
        this.propertyCollector = propertyCollector != null ? propertyCollector : new WidgetPropertyCollector();
    }

    public WidgetItem(Group group, String title, String clazz, IWidgetPainter painter, WidgetPropertyCollector propertyCollector) {
        this(group, title, AdsDefinitionIcon.WIDGETS.calcIcon(clazz), clazz, painter, propertyCollector);
    }

    public WidgetItem(Group group, String title, RadixIcon icon, String clazz) {
        this(group, title, icon, clazz, new DefaultWidgetPainter(), new WidgetPropertyCollector());

    }

    public WidgetItem(Group group, String title, String clazz) {
        this(group, title, clazz, new DefaultWidgetPainter(), new WidgetPropertyCollector());
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        PropertyStore props = getPropertyCollector().getBackgroundProperties(node, rect);
        getPainter().paintBackground(graphics, rect, props);
    }

    @Override
    public void paintBorder(Graphics2D graphics, Rectangle rect, RadixObject node) {
        PropertyStore props = getPropertyCollector().getBorderProperties(node, rect);
        getPainter().paintBorder(graphics, rect, props);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        PropertyStore props = getPropertyCollector().getWidgetProperties(node, rect);
        getPainter().paintWidget(graphics, rect, props);
    }

    @Override
    public Rectangle adjustContentGeometry(RadixObject node, Rectangle rect) {
        PropertyStore props = getPropertyCollector().getAdjustProperties(node, rect);
        return getContentAdjuster().adjust(rect, props);
    }

    @Override
    public Rectangle adjustLayoutGeometry(RadixObject node, Rectangle rect) {
        PropertyStore props = getPropertyCollector().getAdjustProperties(node, rect);
        return super.adjustLayoutGeometry(node, getLayoutAdjuster().adjust(rect, props));
    }

    public IWidgetPainter getPainter() {
        return painter;
    }

    public WidgetPropertyCollector getPropertyCollector() {
        return propertyCollector;
    }

    public IAdjuster getContentAdjuster() {
        return IAdjuster.EMPTY_ADJUSTER;
    }

    public IAdjuster getLayoutAdjuster() {
        return IAdjuster.EMPTY_ADJUSTER;
    }
}
