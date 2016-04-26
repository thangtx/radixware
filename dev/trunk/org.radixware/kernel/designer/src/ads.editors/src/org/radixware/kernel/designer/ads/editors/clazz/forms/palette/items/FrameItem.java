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
 * 10/4/11 2:25 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.*;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShadow;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShape;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.register;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.GroupBoxItem.DEFAULT;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.*;

public class FrameItem extends WidgetItem {

    public static class FramePropertyCollector extends WidgetPropertyCollector {

        @Override
        public PropertyStore getBorderProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBorderProperties(node, rect);

            AdsUIProperty.EnumValueProperty shadowProp = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "frameShadow");
            if (shadowProp != null) {
                props.set("frameShadow", shadowProp.getValue());
            }

            AdsUIProperty.EnumValueProperty shapeProp = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "frameShape");
            if (shapeProp != null) {
                props.set("frameShape", shapeProp.getValue());
            }

            AdsUIProperty.IntProperty lineWidthProp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "lineWidth");
            if (lineWidthProp != null) {
                props.set("lineWidth", lineWidthProp.value);
            }

            AdsUIProperty.IntProperty midLineWidthProp = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "midLineWidth");
            if (midLineWidthProp != null) {
                props.set("midLineWidth", midLineWidthProp.value);
            }

            return props;
        }

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);

            AdsUIProperty.BooleanProperty autoFillBackgroundProp = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "autoFillBackground");

            if (autoFillBackgroundProp != null) {
                props.set("autoFillBackground", autoFillBackgroundProp.value);
            }

            return props;
        }

        @Override
        public PropertyStore getAdjustProperties(RadixObject node, Rectangle rect) {
            return getBorderProperties(node, rect);
        }
    }

    public static class FramePainter extends WidgetPainterAdapter {

        private static enum Direction {

            VERTICAL, HORIZONTAL
        }

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            boolean autoFillBackground = props.get("autoFillBackground", false);

            if (autoFillBackground) {
                graphics.setColor(DrawUtil.COLOR_WINDOW);
                graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }

        @Override
        public void paintBorder(Graphics2D graphics, Rectangle rect, PropertyStore props) {

            EFrameShadow shadow = props.get("frameShadow", EFrameShadow.Raised);
            EFrameShape shape = props.get("frameShape", EFrameShape.StyledPanel);

            int lineWidth = props.get("lineWidth", 1),
                    midLineWidth = props.get("midLineWidth", 0);

            Rectangle rectangle = rect.getBounds();

            switch (shape) {
                case Box:
                    paintBoxBorder(graphics, rectangle, shadow, lineWidth, midLineWidth);
                    break;
                case WinPanel:
                    paintWinPanel(graphics, rectangle, shadow, lineWidth, midLineWidth);
                    break;
                case Panel:
                    paintPanelBorder(graphics, rectangle, shadow, lineWidth, midLineWidth);
                    break;
                case NoFrame:
                case StyledPanel:
                    paintStyledPanel(graphics, rectangle, shadow, lineWidth, midLineWidth);
                    break;
                case HLine:
                    paintLineBorder(graphics, rectangle, Direction.HORIZONTAL, shadow, lineWidth, midLineWidth);
                    break;
                case VLine:
                    paintLineBorder(graphics, rectangle, Direction.VERTICAL, shadow, lineWidth, midLineWidth);
                    break;
                default:
            }
        }

        private void paintPanelBorder(Graphics2D graphics, Rectangle rect, EFrameShadow shadow, int lineWidth, int midLineWidth) {

            Point p1 = new Point(rect.x, rect.y), p2 = new Point(rect.x + rect.width, rect.y + rect.height);
            int w = lineWidth;

            Color top, down;

            if (shadow == EFrameShadow.Plain) {
                paintFlatBorder(graphics, rect, shadow, lineWidth, midLineWidth);
                return;
            } else if (shadow == EFrameShadow.Raised) {
                top = Color.WHITE;
                down = Color.LIGHT_GRAY;
            } else {
                top = Color.LIGHT_GRAY;
                down = Color.WHITE;
            }

            Polygon p = new Polygon();
            p.addPoint(p1.x, p1.y);
            p.addPoint(p2.x, p1.y);
            p.addPoint(p2.x - w, p1.y + w);
            p.addPoint(p1.x + w, p1.y + w);
            p.addPoint(p1.x + w, p2.y - w);
            p.addPoint(p1.x, p2.y);

            graphics.setColor(top);
            graphics.fillPolygon(p);

            p = new Polygon();
            p.addPoint(p1.x, p2.y);
            p.addPoint(p1.x, p2.y - 1);
            p.addPoint(p1.x + w - 1, p2.y - w);
            p.addPoint(p2.x - w, p2.y - w);
            p.addPoint(p2.x - w, p1.y + w - 1);
            p.addPoint(p2.x - 1, p1.y);
            p.addPoint(p2.x, p1.y);
            p.addPoint(p2.x, p2.y);

            graphics.setColor(down);
            graphics.fillPolygon(p);
        }

        private void paintBoxBorder(Graphics2D graphics, Rectangle rect, EFrameShadow shadow, int lineWidth, int midLineWidth) {
            if (shadow == EFrameShadow.Plain) {
                paintFlatBorder(graphics, rect, shadow, lineWidth, midLineWidth);
            } else {
                DrawUtil.drawShadeRect(graphics, rect, (shadow == EFrameShadow.Sunken), lineWidth, midLineWidth, null);
            }
        }

        private void paintFlatBorder(Graphics2D graphics, Rectangle rect, EFrameShadow shadow, int lineWidth, int midLineWidth) {
            DrawUtil.drawPlainRect(graphics, rect, Color.BLACK, lineWidth, null);
        }

        private void paintLineBorder(Graphics2D graphics, Rectangle rect, Direction direction, EFrameShadow shadow, int lineWidth, int midLineWidth) {

            Point p1, p2;
            double dx = 0, dy = 0;
            if (direction == Direction.HORIZONTAL) {
                p1 = new Point(rect.x, rect.y + rect.height / 2);
                p2 = new Point(rect.x + rect.width, rect.y + rect.height / 2);
                dy = lineWidth / 2.0;
            } else {
                p1 = new Point(rect.x + rect.width / 2, rect.y);
                p2 = new Point(rect.x + rect.width / 2, rect.y + rect.height);
                dx = lineWidth / 2.0;
            }

            if (shadow == EFrameShadow.Raised || shadow == EFrameShadow.Sunken) {
                DrawUtil.drawShadeLine(graphics, p1, p2, (shadow == EFrameShadow.Sunken), lineWidth, midLineWidth);
            } else {
                graphics.setColor(Color.BLACK);
                int x = (int) (p1.x - dx),
                        y = (int) (p1.y - dy),
                        w = (int) (Math.abs(p2.x - p1.x) + 2 * dx),
                        h = (int) (Math.abs(p2.y - p1.y) + 2 * dy);

                graphics.fillRect(x, y, w, h);
            }
        }

        private void paintWinPanel(Graphics2D graphics, Rectangle rect, EFrameShadow shadow, int lineWidth, int midLineWidth) {
            paintPanelBorder(graphics, rect, shadow, 2, 0);
        }

        private void paintStyledPanel(Graphics2D graphics, Rectangle rect, EFrameShadow shadow, int lineWidth, int midLineWidth) {
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
        }
    }

    public static final class BorderAdjuster extends WrapAdjuster {

        public BorderAdjuster(IAdjuster spurce) {
            super(spurce);
        }

        @Override
        protected Rectangle adjustImpl(Rectangle rect, Rectangle sourceRect, PropertyStore props) {

            final EFrameShape frameShape = props.get("frameShape", EFrameShape.StyledPanel);
            final int width;

            if (frameShape == EFrameShape.WinPanel || frameShape == EFrameShape.StyledPanel) {
                width = 2;
            } else if (frameShape == EFrameShape.NoFrame) {
                width = 0;
            } else {
                width = props.get("lineWidth", 1);
            }

            rect.x += width;
            rect.y += width;
            rect.width -= 2 * width;
            rect.height -= 2 * width;

            return rect;
        }

        @Override
        protected Point getOffsetImpl(Rectangle rect, Point point, Point sourcePoint, PropertyStore props) {

            final int width = props.get("lineWidth", 1);
            final EFrameShape frameShape = props.get("frameShape", EFrameShape.StyledPanel);

            if (frameShape != EFrameShape.WinPanel && frameShape != EFrameShape.NoFrame && frameShape != EFrameShape.StyledPanel) {

                point.x += width;
                point.y += width;
            }
            return point;
        }
    }
    public static final FrameItem DEFAULT = new FrameItem();

 

    protected FrameItem(Group group, String title, String clazz, IWidgetPainter painter, WidgetPropertyCollector propertyCollector) {
        super(group, title, clazz, painter, propertyCollector);
    }

    public FrameItem() {
        this(Group.GROUP_CONTAINERS, NbBundle.getMessage(FrameItem.class, "Frame"),
                AdsMetaInfo.FRAME_CLASS, new FramePainter(), new FramePropertyCollector());
    }

    @Override
    public AdsWidgetDef createObjectUI(RadixObject context) {
        AdsWidgetDef wdg = (AdsWidgetDef) super.createObjectUI(context);

        wdg.getProperties().add(new AdsUIProperty.EnumValueProperty("frameShape", "StyledPanel"));
        wdg.getProperties().add(new AdsUIProperty.EnumValueProperty("frameShadow", "Raised"));

        return wdg;
    }

    @Override
    public FramePainter getPainter() {
        return (FramePainter) super.getPainter();
    }

    @Override
    public IAdjuster getContentAdjuster() {
        return new BorderAdjuster(super.getContentAdjuster());
    }

    @Override
    public IAdjuster getLayoutAdjuster() {
        return new BorderAdjuster(super.getContentAdjuster());
    }
}
