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

/*
 * 10/21/11 3:43 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShadow;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShape;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;

public class StackedWidgetItem extends FrameItem {

    public static final class StackedWidgetPropertyCollector extends FrameItem.FramePropertyCollector {

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);
            props.set("autoFillBackground", true);
            return props;
        }
    }

    public static final class StackedWidgetPainter extends FrameItem.FramePainter {

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            paintSwitcher(graphics, rect);
        }

        /*   _____________
         *  |         < > |
         *  |             |
         *  |             |
         *  |_____________|
         */
        private void paintSwitcher(Graphics2D graphics, Rectangle rect) {

            int x0 = rect.x + rect.width - INDENT - 5 * UNIT,
                    y0 = rect.y + INDENT + UNIT;

            int x = x0, y = y0;
            Polygon polygon = new Polygon();
            polygon.addPoint(x, y);

            x = x0 + UNIT;
            y = y0 - UNIT;
            polygon.addPoint(x, y);

            x = x0 + UNIT;
            y = y0 + UNIT;
            polygon.addPoint(x, y);

            graphics.setColor(Color.BLACK);
            graphics.fillPolygon(polygon);

            polygon = new Polygon();
            x = x0 + 3 * UNIT;
            y = y0 + UNIT;
            polygon.addPoint(x, y);

            x = x0 + 3 * UNIT;
            y = y0 - UNIT;
            polygon.addPoint(x, y);

            x = x0 + 4 * UNIT;
            y = y0;
            polygon.addPoint(x, y);
            graphics.fillPolygon(polygon);
        }
    }

    public static final StackedWidgetItem DEFAULT = new StackedWidgetItem();

   
    private static final int UNIT = 3;
    private static final int INDENT = 2 * UNIT;

    public StackedWidgetItem() {
        super(Group.GROUP_CONTAINERS, NbBundle.getMessage(TabItem.class, "Stacked_Widget"), AdsMetaInfo.STACKED_WIDGET_CLASS,
                new StackedWidgetPainter(), new StackedWidgetPropertyCollector());
    }

    @Override
    public AdsWidgetDef createObjectUI(RadixObject context) {
        AdsWidgetDef widget = super.createObjectUI(context);
        AdsWidgetDef page = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
        page.setName("page");
        widget.getWidgets().add(page);

        widget.getProperties().add(new AdsUIProperty.IntProperty("currentIndex", 0));
        widget.getProperties().add(new AdsUIProperty.StringProperty("currentPageName", "Page"));
        ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(widget, "frameShape")).value = EFrameShape.NoFrame;
        ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(widget, "frameShadow")).value = EFrameShadow.Plain;

        return widget;
    }

    @Override
    public StackedWidgetPainter getPainter() {
        return (StackedWidgetPainter) super.getPainter();
    }

    /**
     *
     * @param graphics graphic object
     * @param rect rectangle of widget
     * @param p mouse cursor position
     * @return if cursor enters in prev_button region return -1, next_button
     * region return 1, or 0 otherwise
     */
    public int insideSwitcher(Graphics2D graphics, Rectangle rect, Point p) {

        int x0 = rect.x + rect.width - INDENT - 5 * UNIT,
                y0 = rect.y + INDENT + UNIT;

        int x = x0, y = y0;

        if (p.x > x0 - UNIT && p.x < x0 + 2 * UNIT && p.y > y0 - 2 * UNIT && p.y < y0 + 2 * UNIT) {
            return -1;
        }

        x0 = x0 + 2 * UNIT;
        if (p.x > x0 - UNIT && p.x < x0 + 2 * UNIT && p.y > y0 - 2 * UNIT && p.y < y0 + 2 * UNIT) {
            return 1;
        }

        return 0;
    }
}
