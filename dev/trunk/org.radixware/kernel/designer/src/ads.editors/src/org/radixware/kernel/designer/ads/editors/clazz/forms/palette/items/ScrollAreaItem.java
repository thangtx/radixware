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
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.*;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShadow;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShape;
import org.radixware.kernel.common.defs.ads.ui.enums.EScrollBarPolicy;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IAdjuster;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WrapAdjuster;

public class ScrollAreaItem extends FrameItem {

    public static final ScrollAreaItem DEFAULT = new ScrollAreaItem();

  

    protected static class ScrollAreaItemPropertyCollector extends FramePropertyCollector {

        @Override
        public PropertyStore getGeneralProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getGeneralProperties(node, rect);

            AdsUIProperty.EnumValueProperty verticalScrollBarPolicy = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "verticalScrollBarPolicy");
            if (verticalScrollBarPolicy != null) {
                props.set("verticalScrollBarPolicy", verticalScrollBarPolicy.getValue());
            }

            AdsUIProperty.EnumValueProperty horizontalScrollBarPolicy = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "horizontalScrollBarPolicy");
            if (horizontalScrollBarPolicy != null) {
                props.set("horizontalScrollBarPolicy", horizontalScrollBarPolicy.getValue());
            }

            AdsUIProperty.BooleanProperty widgetResizable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "widgetResizable");
            if (widgetResizable != null) {
                props.set("widgetResizable", widgetResizable.value);
            }

            AdsUIProperty.RectProperty contentGeometry = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(AdsUIUtil.currentWidget((AdsUIItemDef) node), "geometry");
            if (contentGeometry != null) {
                props.set("contentGeometry", contentGeometry.getRectangle());
            }
            return props;
        }

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);
            props.set("autoFillBackground", true);
            return props;
        }

        @Override
        public PropertyStore getAdjustProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getAdjustProperties(node, rect);

            return props;
        }
    }

    protected static class ScrollAreaItemPainter extends FramePainter {

        private static final int BTN_SIZE = 16;

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            EScrollBarPolicy horizontalScrollBarPolicy = props.get("horizontalScrollBarPolicy", EScrollBarPolicy.ScrollBarAsNeeded),
                    verticalScrollBarPolicy = props.get("verticalScrollBarPolicy", EScrollBarPolicy.ScrollBarAsNeeded);

            boolean resizable = props.get("widgetResizable", true);
            Rectangle contentRect = props.get("contentGeometry", Rectangle.class);

            int diffWidth = 0, diffHeight = 0;

            if (contentRect != null) {
                diffWidth = rect.width - contentRect.width;
                diffHeight = rect.height - contentRect.height;
            }

            boolean hScroll = scroll(horizontalScrollBarPolicy, diffWidth, resizable),
                    vScroll = scroll(verticalScrollBarPolicy, diffHeight, resizable);

            if (vScroll) {
                diffWidth -= SCROLL_BAR_WIDTH;
                hScroll = scroll(horizontalScrollBarPolicy, diffWidth, resizable);
            }

            if (hScroll) {
                if (!vScroll) {
                    diffHeight -= SCROLL_BAR_WIDTH;
                    vScroll = scroll(verticalScrollBarPolicy, diffHeight, resizable);
                }
            }

            if (hScroll) {
                paintScrollBar(graphics, rect, ScrollLocation.HORIZONTAL, hScroll, vScroll);
            }

            if (vScroll) {
                paintScrollBar(graphics, rect, ScrollLocation.VERTICAL, hScroll, vScroll);
            }
        }

        private void paintScrollBar(Graphics2D graphics, Rectangle rect, ScrollLocation location, boolean hScroll, boolean vScroll) {
            Rectangle scrollBarRect = getScrollBarRect(rect, location, hScroll, vScroll);

            Color c1 = DrawUtil.COLOR_BUTTON, c2 = DrawUtil.COLOR_MID_LIGHT;
            if (location == ScrollLocation.HORIZONTAL) {
                graphics.setPaint(new GradientPaint(scrollBarRect.x, scrollBarRect.y, c1, scrollBarRect.x, scrollBarRect.y + scrollBarRect.height, c2, false));
            } else {
                graphics.setPaint(new GradientPaint(scrollBarRect.x, scrollBarRect.y, c1, scrollBarRect.x + scrollBarRect.width, scrollBarRect.y, c2, false));
            }
            graphics.fillRoundRect(scrollBarRect.x, scrollBarRect.y, scrollBarRect.width, scrollBarRect.height, 6, 6);

            graphics.setColor(DrawUtil.COLOR_DARK);
            graphics.drawRoundRect(scrollBarRect.x, scrollBarRect.y, scrollBarRect.width, scrollBarRect.height, 6, 6);

            if (location == ScrollLocation.HORIZONTAL) {
                int x0 = scrollBarRect.x + BTN_SIZE,
                        y0 = scrollBarRect.y,
                        x1 = x0,
                        y1 = scrollBarRect.y + scrollBarRect.height;

                graphics.drawLine(x0, y0, x1, y1);

                x0 = scrollBarRect.x + scrollBarRect.width - BTN_SIZE;
                x1 = x0;

                graphics.drawLine(x0, y0, x1, y1);

                drowDirectTriangle(graphics, location, -1, scrollBarRect, 0);
                drowDirectTriangle(graphics, location, 1, scrollBarRect, 0);

            } else {
                int x0 = scrollBarRect.x,
                        y0 = scrollBarRect.y + BTN_SIZE,
                        x1 = scrollBarRect.x + scrollBarRect.width,
                        y1 = y0;

                graphics.drawLine(x0, y0, x1, y1);

                y0 = scrollBarRect.y + scrollBarRect.height - BTN_SIZE;
                y1 = y0;

                graphics.drawLine(x0, y0, x1, y1);

                drowDirectTriangle(graphics, location, -1, scrollBarRect, 0);
                drowDirectTriangle(graphics, location, 1, scrollBarRect, 0);
            }
        }

        private void drowDirectTriangle(Graphics2D graphics, ScrollLocation location, int direct, Rectangle rect, int size) {
            Point center;
            Polygon polygon = new Polygon();

            if (location == ScrollLocation.HORIZONTAL) {
                if (direct < 0) {
                    center = new Point(rect.x + BTN_SIZE / 2, rect.y + rect.height / 2);

                    polygon.addPoint(center.x - 3, center.y);
                    polygon.addPoint(center.x + 2, center.y - 3);
                    polygon.addPoint(center.x + 2, center.y + 3);
                } else {
                    center = new Point(rect.x + rect.width - BTN_SIZE / 2, rect.y + rect.height / 2);

                    polygon.addPoint(center.x + 3, center.y);
                    polygon.addPoint(center.x - 2, center.y - 3);
                    polygon.addPoint(center.x - 2, center.y + 3);
                }
            } else {
                if (direct < 0) {
                    center = new Point(rect.x + rect.width / 2, rect.y + BTN_SIZE / 2);

                    polygon.addPoint(center.x, center.y - 3);
                    polygon.addPoint(center.x - 3, center.y + 2);
                    polygon.addPoint(center.x + 3, center.y + 2);
                } else {
                    center = new Point(rect.x + rect.width / 2, rect.y + rect.height - BTN_SIZE / 2);

                    polygon.addPoint(center.x, center.y + 3);
                    polygon.addPoint(center.x - 3, center.y - 2);
                    polygon.addPoint(center.x + 3, center.y - 2);
                }
            }

            graphics.setColor(Color.BLACK);
            graphics.fillPolygon(polygon);
        }

        private enum ScrollLocation {

            VERTICAL, HORIZONTAL
        }

        private Rectangle getScrollBarRect(Rectangle rect, ScrollLocation location, boolean hScroll, boolean vScroll) {
            int x, y;
            switch (location) {
                case VERTICAL:
                    x = rect.x + rect.width - SCROLL_BAR_WIDTH;
                    y = rect.y;
                    return new Rectangle(x, y, SCROLL_BAR_WIDTH - 1, rect.height - (hScroll ? SCROLL_BAR_WIDTH : 1));

                case HORIZONTAL:
                    x = rect.x;
                    y = rect.y + rect.height - SCROLL_BAR_WIDTH;
                    return new Rectangle(x, y, rect.width - (vScroll ? SCROLL_BAR_WIDTH : 1), SCROLL_BAR_WIDTH - 1);
                default:
                    return null;
            }
        }
    }

    private static class GeometryContentAdjuster extends WrapAdjuster {

        public GeometryContentAdjuster(IAdjuster spurce) {
            super(spurce);
        }

        @Override
        protected Rectangle adjustImpl(Rectangle rect, Rectangle sourceRect, PropertyStore props) {
            Rectangle contentGeometry = props.get("contentGeometry", Rectangle.class);
            boolean widgetResizable = props.get("widgetResizable", true);

            if (!widgetResizable && contentGeometry != null) {
                if (rect.width > contentGeometry.width) {
                    rect.width = contentGeometry.width;
                }
                if (rect.height > contentGeometry.height) {
                    rect.height = contentGeometry.height;
                }
            }
            return rect;
        }
    }

    private static class ScrollAdjuster extends WrapAdjuster {

        public ScrollAdjuster(IAdjuster spurce) {
            super(spurce);
        }

        @Override
        protected Rectangle adjustImpl(Rectangle rect, Rectangle sourceRect, PropertyStore props) {
            EScrollBarPolicy verticalScrollBarPolicy = props.get("verticalScrollBarPolicy", EScrollBarPolicy.ScrollBarAsNeeded);
            EScrollBarPolicy horizontalScrollBarPolicy = props.get("horizontalScrollBarPolicy", EScrollBarPolicy.ScrollBarAsNeeded);

            boolean resizable = props.get("widgetResizable", true);
            Rectangle contentRect = props.get("contentGeometry", Rectangle.class);

            // hscroll
            int diffWidth = 0, diffHeight = 0;

            if (contentRect != null) {
                diffWidth = rect.width - contentRect.width;
                diffHeight = rect.height - contentRect.height;
            }

            boolean hScroll = scroll(horizontalScrollBarPolicy, diffWidth, resizable),
                    vScroll = scroll(verticalScrollBarPolicy, diffHeight, resizable);

            if (vScroll) {
                rect.width -= SCROLL_BAR_WIDTH;

                diffWidth = rect.width - contentRect.width;
                hScroll = scroll(horizontalScrollBarPolicy, diffWidth, resizable);
            }

            if (hScroll) {
                rect.height -= SCROLL_BAR_WIDTH;

                if (!vScroll) {
                    diffHeight = rect.height - contentRect.height;
                    vScroll = scroll(verticalScrollBarPolicy, diffHeight, resizable);
                    if (vScroll) {
                        rect.width -= SCROLL_BAR_WIDTH;
                    }
                }
            }

            return rect;
        }
    }

    private static class GeometryLayoutAdjuster extends WrapAdjuster {

        public GeometryLayoutAdjuster(IAdjuster spurce) {
            super(spurce);
        }

        @Override
        protected Rectangle adjustImpl(Rectangle rect, Rectangle sourceRect, PropertyStore props) {

            Rectangle contentGeometry = props.get("contentGeometry", Rectangle.class);
            boolean widgetResizable = props.get("widgetResizable", true);

            if (!widgetResizable && contentGeometry != null) {
                rect.width = contentGeometry.width;
                rect.height = contentGeometry.height;
            }
            return rect;
        }
    }

    private static boolean scroll(EScrollBarPolicy scrollBarPolicy, int diff, boolean resizable) {
        if (scrollBarPolicy == EScrollBarPolicy.ScrollBarAlwaysOn) {
            return true;
        } else if (scrollBarPolicy == EScrollBarPolicy.ScrollBarAlwaysOff) {
            return false;
        } else {
            return diff < 0 && !resizable;
        }
    }
    public static final int SCROLL_BAR_WIDTH = 15;

    public ScrollAreaItem() {
        super(Group.GROUP_CONTAINERS, NbBundle.getMessage(ScrollAreaItem.class, "Scroll_Area"),
                AdsMetaInfo.SCROLL_AREA_CLASS, new ScrollAreaItemPainter(), new ScrollAreaItemPropertyCollector());
    }

    @Override
    public AdsWidgetDef createObjectUI(RadixObject context) {
        AdsWidgetDef scrollArea = super.createObjectUI(context);

        AdsWidgetDef widget = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
        widget.getProperties().add(new AdsUIProperty.RectProperty("geometry", 0, 0, 0, 0));
        scrollArea.getWidgets().add(widget);

        ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(scrollArea, "frameShape")).value = EFrameShape.StyledPanel;
        ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(scrollArea, "frameShadow")).value = EFrameShadow.Sunken;

        scrollArea.getProperties().add(new AdsUIProperty.BooleanProperty("widgetResizable", true));

        return scrollArea;
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        IAdjuster clipper = super.getContentAdjuster();
        PropertyStore widgetProps = getPropertyCollector().getWidgetProperties(node, rect);
        PropertyStore clipProps = getPropertyCollector().getAdjustProperties(node, rect);

        getPainter().paintWidget(graphics, clipper.adjust(rect, clipProps), widgetProps);
    }

    @Override
    public IAdjuster getContentAdjuster() {
        return new GeometryContentAdjuster(new ScrollAdjuster(super.getContentAdjuster()));
    }

    @Override
    public IAdjuster getLayoutAdjuster() {
        return new GeometryLayoutAdjuster(new ScrollAdjuster(super.getLayoutAdjuster()));
    }
}
