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
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.ResizeControlPointResolver;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeProvider.ControlPoint;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.SplitterWidget;

public final class ResizeAction extends WidgetAction.LockedAdapter {

    public static final WidgetControlPointResolver WIDGET_CONTROL_POINT_RESOLVER
            = new WidgetControlPointResolver();
    public static final RootWidgetControlPointResolver ROOT_WIDGET_CONTROL_POINT_RESOLVER
            = new RootWidgetControlPointResolver();
    private ResizeControlPointResolver resolver;
    private Widget resizingWidget = null;
    private ResizeProvider.ControlPoint controlPoint;
    private Rectangle originalSceneRectangle = null;
    private Point dragSceneLocation = null;
    private Insets insets;
    private int splitterIdx[] = new int[1];
    private Rectangle[] splitterRects;

    public ResizeAction() {
        this(WIDGET_CONTROL_POINT_RESOLVER);
    }

    public ResizeAction(ResizeControlPointResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected boolean isLocked() {
        return resizingWidget != null;
    }

    @Override
    public State mousePressed(Widget widget, WidgetMouseEvent event) {
        if (isLocked()) {
            return State.createLocked(widget, this);
        }
        if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 1) {
            insets = widget.getBorder().getInsets();
            splitterIdx[0] = -1;
            controlPoint = null;
            if (widget instanceof SplitterWidget) {
                SplitterWidget wg = (SplitterWidget) widget;
                splitterRects = LayoutUtil.justifySplitterLayout((AdsUIItemDef) wg.getNode(), wg.getLayoutGeometry());
                controlPoint = ((WidgetControlPointResolver) resolver).resolveSplitterControlPoint(widget, event.getPoint(), splitterIdx);
            }
            if (controlPoint == null) {
                controlPoint = resolver.resolveControlPoint(widget, event.getPoint());
            }
            if (controlPoint != null) {
                resizingWidget = widget;
                originalSceneRectangle = null;
                if (widget.isPreferredBoundsSet()) {
                    originalSceneRectangle = widget.getPreferredBounds();
                }
                if (originalSceneRectangle == null) {
                    originalSceneRectangle = widget.getBounds();
                }
                dragSceneLocation = widget.convertLocalToScene(event.getPoint());

                BaseWidget parent = ((BaseWidget) widget).getContainerWidget();
                if (parent != null) {
                    Point offsetPoint = parent.offsetPoint();
                    if (offsetPoint != null) {
                        //dragSceneLocation.x += offsetPoint.x;
                        // dragSceneLocation.y += offsetPoint.y;
                        originalSceneRectangle.x += offsetPoint.x;
                        originalSceneRectangle.y += offsetPoint.y;
                    }
                }
                // resizing started
                return State.createLocked(widget, this);
            }

        }
        return State.REJECTED;
    }

    @Override
    public State mouseReleased(Widget widget, WidgetMouseEvent event) {
        boolean state = resize(widget, event.getPoint(), true);
        // resizing finished
        resizingWidget = null;
        if (state) {
            BaseWidget w = (BaseWidget) widget;

            RadixObject node = w.getNode();
            AdsUIUtil.fire(node, AdsUIUtil.getUiProperty(node, "geometry"));

            w.restoreGeometry(); //!!! for some move bugs
            w.revalidate();
            //w.getScene().repaint();
        }
        return state ? State.CONSUMED : State.REJECTED;
    }

    @Override
    public State mouseDragged(Widget widget, WidgetMouseEvent event) {
        return resize(widget, event.getPoint(), false) ? State.createLocked(widget, this) : State.REJECTED;
    }

    private boolean resize(Widget widget, Point newLocation, boolean mouseReleased) {
        if (resizingWidget != widget) {
            return false;
        }

        if (!((BaseWidget) widget).isResizable()) {
            return false;
        }

        BaseWidget wg = (BaseWidget) widget;
        GraphSceneImpl scene = wg.getSceneImpl();
        RadixObject node = wg.getNode();

        newLocation = widget.convertLocalToScene(newLocation);

        int dx = newLocation.x - dragSceneLocation.x;
        int dy = newLocation.y - dragSceneLocation.y;

        int minx = insets.left + insets.right;
        int miny = insets.top + insets.bottom;
        boolean snaped = scene.isSnaped();

        if (AdsUIUtil.isReadOnlyNode(((GraphSceneImpl) widget.getScene()).getUI(), node)) {
            return false;
        }

        Rectangle rectangle = new Rectangle(originalSceneRectangle);

//        BaseWidget parent = wg.getContainerWidget();
//        Point offset = null;
//        if (parent instanceof BaseWidget) {
//            offset = ((BaseWidget) parent).offsetPoint();
//
//        }
//        if (offset != null) {
//            rectangle.x += offset.x;
//            rectangle.y += offset.y;
//        }
        if (splitterIdx[0] >= 0) { // splitter !!!
            int idx = splitterIdx[0];
            Rectangle[] ir = splitterRects;

            AdsUIItemDef w = (AdsUIItemDef) node;
            Definitions<? extends AdsUIItemDef> widgets;
            if (w instanceof AdsWidgetDef) {
                widgets = ((AdsWidgetDef) w).getWidgets();
            } else {
                widgets = ((AdsRwtWidgetDef) w).getWidgets();
            }

            double dw;
            switch (controlPoint) {
                case BOTTOM_CENTER:
                case TOP_CENTER:
                    dx = (dx >= 0)
                            ? Math.min(dx, ir[idx + 1].width)
                            : -Math.min(-dx, ir[idx].width);
                    if (dx == 0) {
                        break;
                    }
                    dw = (LayoutUtil.getSplitterItemWeight(widgets.get(idx)) + LayoutUtil.getSplitterItemWeight(widgets.get(idx + 1))) / (ir[idx].width + ir[idx + 1].width);
                    LayoutUtil.setSplitterItemWeight(widgets.get(idx), dw * (ir[idx].width + dx));
                    LayoutUtil.setSplitterItemWeight(widgets.get(idx + 1), dw * (ir[idx + 1].width - dx));
                    break;
                case CENTER_LEFT:
                case CENTER_RIGHT:
                    dy = (dy >= 0)
                            ? Math.min(dy, ir[idx + 1].height)
                            : -Math.min(-dy, ir[idx].height);
                    if (dy == 0) {
                        break;
                    }
                    dw = (LayoutUtil.getSplitterItemWeight(widgets.get(idx)) + LayoutUtil.getSplitterItemWeight(widgets.get(idx + 1))) / (ir[idx].height + ir[idx + 1].height);

                    LayoutUtil.setSplitterItemWeight(widgets.get(idx), dw * (ir[idx].height + dy));
                    LayoutUtil.setSplitterItemWeight(widgets.get(idx + 1), dw * (ir[idx + 1].height - dy));
                    break;
            }

            wg.setModified();
            wg.revalidate();

            return true;
        }

        AdsLayout.Item item = null;
        if (node instanceof AdsLayout.SpacerItem) {
            item = (AdsLayout.Item) node;
        }
        if (node.getContainer() instanceof AdsLayout.Item) {
            item = (AdsLayout.Item) node.getContainer();
        }

        AdsLayout layout = null;
        if (item != null) {
            layout = item.getOwnerLayout();
        }

        if (layout != null && AdsMetaInfo.GRID_LAYOUT_CLASS.equals(layout.getClassName()) && mouseReleased) {
            AdsLayout.Item[][] items = layout.getItemsAsArray();
            int rows = items.length;
            int cols = items[0].length;

            boolean sizeBottom = true;
            if (item.row + item.rowSpan < rows) {
                for (int i = 0; i < item.columnSpan; i++) {
                    if (items[item.row + item.rowSpan][item.column + i] != null) {
                        sizeBottom = false;
                        break;
                    }
                }
            } else {
                sizeBottom = false;
            }

            boolean sizeTop = true;
            if (item.row > 0) {
                for (int i = 0; i < item.columnSpan; i++) {
                    if (items[item.row - 1][item.column + i] != null) {
                        sizeTop = false;
                        break;
                    }
                }
            } else {
                sizeTop = false;
            }

            boolean sizeLeft = true;
            if (item.column > 0) {
                for (int i = 0; i < item.rowSpan; i++) {
                    if (items[item.row + i][item.column - 1] != null) {
                        sizeLeft = false;
                        break;
                    }
                }
            } else {
                sizeLeft = false;
            }

            boolean sizeRight = true;
            if (item.column + item.columnSpan < cols) {
                for (int i = 0; i < item.rowSpan; i++) {
                    if (items[item.row + i][item.column + item.columnSpan] != null) {
                        sizeRight = false;
                        break;
                    }
                }
            } else {
                sizeRight = false;
            }

            switch (controlPoint) {
                case BOTTOM_CENTER:
                    if (dy > 0 && sizeBottom) {
                        item.rowSpan++;
                        wg.setModified();
                    }
                    break;
                case BOTTOM_LEFT:
                    if (dy > 0 && sizeBottom) {
                        item.rowSpan++;
                        wg.setModified();
                    }
                    if (dx < 0 && sizeLeft) {
                        item.column--;
                        item.columnSpan++;
                        wg.setModified();
                    }
                    break;
                case BOTTOM_RIGHT:
                    if (dy > 0 && sizeBottom) {
                        item.rowSpan++;
                        wg.setModified();
                    }
                    if (dx > 0 && sizeRight) {
                        item.columnSpan++;
                        wg.setModified();
                    }
                    break;
                case CENTER_LEFT:
                    if (dx < 0 && sizeLeft) {
                        item.column--;
                        item.columnSpan++;
                        wg.setModified();
                    }
                    break;
                case CENTER_RIGHT:
                    if (dx > 0 && sizeRight) {
                        item.columnSpan++;
                        wg.setModified();
                    }
                    break;
                case TOP_CENTER:
                    if (dy < 0 && sizeTop) {
                        item.row--;
                        item.rowSpan++;
                        wg.setModified();
                    }
                    break;
                case TOP_LEFT:
                    if (dy < 0 && sizeTop) {
                        item.row--;
                        item.rowSpan++;
                        wg.setModified();
                    }
                    if (dx < 0 && sizeLeft) {
                        item.column--;
                        item.columnSpan++;
                        wg.setModified();
                    }
                    break;
                case TOP_RIGHT:
                    if (dy < 0 && sizeTop) {
                        item.row--;
                        item.rowSpan++;
                        wg.setModified();
                    }
                    if (dx > 0 && sizeRight) {
                        item.columnSpan++;
                        wg.setModified();
                    }
                    break;
            }

            wg.getContainerWidget().revalidate();
            return false;
        }

        if (layout != null && mouseReleased) {
            wg.getContainerWidget().revalidate();
            return false;
        }
        /*
         * if ((node instanceof AdsLayout.SpacerItem &&
         * ((AdsLayout.SpacerItem)node).getOwnerLayout() != null) || (isLayout
         * && !isGridLayout) ||
         * (AdsMetaInfo.SPLITTER_CLASS.equals(AdsUIUtil.getUiClassName(node.getOwnerDefinition())))
         * ) { return false; }
         */
        if (AdsMetaInfo.SPLITTER_CLASS.equals(AdsUIUtil.getUiClassName(node.getOwnerDefinition()))
                || AdsMetaInfo.ADVANCED_SPLITTER_CLASS.equals(AdsUIUtil.getUiClassName(node.getOwnerDefinition()))) {
            return false;
        }

        switch (controlPoint) {
            case BOTTOM_CENTER:
                resizeToBottom(miny, rectangle, dy, snaped);
                break;
            case BOTTOM_LEFT:
                resizeToLeft(minx, rectangle, dx, snaped);
                resizeToBottom(miny, rectangle, dy, snaped);
                break;
            case BOTTOM_RIGHT:
                resizeToRight(minx, rectangle, dx, snaped);
                resizeToBottom(miny, rectangle, dy, snaped);
                break;
            case CENTER_LEFT:
                resizeToLeft(minx, rectangle, dx, snaped);
                break;
            case CENTER_RIGHT:
                resizeToRight(minx, rectangle, dx, snaped);
                break;
            case TOP_CENTER:
                resizeToTop(miny, rectangle, dy, snaped);
                break;
            case TOP_LEFT:
                resizeToLeft(minx, rectangle, dx, snaped);
                resizeToTop(miny, rectangle, dy, snaped);
                break;
            case TOP_RIGHT:
                resizeToRight(minx, rectangle, dx, snaped);
                resizeToTop(miny, rectangle, dy, snaped);
                break;
        }

        wg.setModified();

        Rectangle set = new Rectangle(rectangle);

        final BaseWidget parent = ((BaseWidget) widget).getContainerWidget();
        if (!mouseReleased && parent != null) {
            Point offsetPoint = parent.offsetPoint();
            if (offsetPoint != null) {
                //dragSceneLocation.x += offsetPoint.x;
                // dragSceneLocation.y += offsetPoint.y;
                set.x -= offsetPoint.x;
                set.y -= offsetPoint.y;
            }
        }

        wg.setPreferredBounds(set);
        if (mouseReleased) {
            if (AdsMetaInfo.RWT_UI_DIALOG.equals(AdsUIUtil.getQtClassName(node))
                    || AdsMetaInfo.RWT_CUSTOM_DIALOG.equals(AdsUIUtil.getQtClassName(node))
                    || AdsMetaInfo.RWT_PROP_EDITOR_DIALOG.equals(AdsUIUtil.getQtClassName(node))
                    || AdsMetaInfo.RWT_FORM_DIALOG.equals(AdsUIUtil.getQtClassName(node))
                    || AdsMetaInfo.RWT_CUSTOM_REPORT_DIALOG.equals(AdsUIUtil.getQtClassName(node))) {
                set.height -= 30;
            }
            wg.setPreferredBounds(set);
            wg.saveGeometry();
        }

        wg.revalidate();

        wg.getScene().repaint();
        if (mouseReleased && parent != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    parent.revalidate();
                    parent.getSceneImpl().repaint();
                }
            });
        }
        return true;
    }

    private static void resizeToTop(int miny, Rectangle rectangle, int dy, boolean snaped) {
        if (rectangle.height - dy < miny) {
            dy = rectangle.height - miny;
        }
        rectangle.y += dy;
        if (snaped) {
            int y = rectangle.y;
            rectangle.y = Math.round(1f * rectangle.y / GraphSceneImpl.SNAP_SIZE) * GraphSceneImpl.SNAP_SIZE;
            dy += rectangle.y - y;
        }
        rectangle.height -= dy;
    }

    private static void resizeToBottom(int miny, Rectangle rectangle, int dy, boolean snaped) {
        if (rectangle.height + dy < miny) {
            dy = miny - rectangle.height;
        }
        rectangle.height += dy;
        if (snaped) {
            rectangle.height = Math.round(1f * rectangle.height / GraphSceneImpl.SNAP_SIZE) * GraphSceneImpl.SNAP_SIZE;
        }
    }

    private static void resizeToLeft(int minx, Rectangle rectangle, int dx, boolean snaped) {
        if (rectangle.width - dx < minx) {
            dx = rectangle.width - minx;
        }
        rectangle.x += dx;
        if (snaped) {
            int x = rectangle.x;
            rectangle.x = Math.round(1f * rectangle.x / GraphSceneImpl.SNAP_SIZE) * GraphSceneImpl.SNAP_SIZE;
            dx += rectangle.x - x;
        }
        rectangle.width -= dx;
    }

    private static void resizeToRight(int minx, Rectangle rectangle, int dx, boolean snaped) {
        if (rectangle.width + dx < minx) {
            dx = minx - rectangle.width;
        }
        rectangle.width += dx;
        if (snaped) {
            rectangle.width = Math.round(1f * rectangle.width / GraphSceneImpl.SNAP_SIZE) * GraphSceneImpl.SNAP_SIZE;
        }
    }

    public static class WidgetControlPointResolver implements ResizeControlPointResolver {

        @Override
        public ControlPoint resolveControlPoint(Widget widget, Point point) {
            ControlPoint controlPoint = resolveSplitterControlPoint(widget, point, new int[1]);
            if (controlPoint != null) {
                return controlPoint;
            }

            Rectangle bounds = widget.getBounds();
            Insets insets = widget.getBorder().getInsets();
            Point center = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
            Dimension centerDimension = new Dimension(Math.max(insets.left, insets.right), Math.max(insets.top, insets.bottom));

            if (point.y >= bounds.y + bounds.height - insets.bottom && point.y < bounds.y + bounds.height) {
                if (point.x >= bounds.x + bounds.width - insets.right && point.x < bounds.x + bounds.width) {
                    return ResizeProvider.ControlPoint.BOTTOM_RIGHT;
                } else if (point.x >= bounds.x && point.x < bounds.x + insets.left) {
                    return ResizeProvider.ControlPoint.BOTTOM_LEFT;
                } else if (point.x >= center.x - centerDimension.height / 2 && point.x < center.x + centerDimension.height - centerDimension.height / 2) {
                    return ResizeProvider.ControlPoint.BOTTOM_CENTER;
                }
            } else if (point.y >= bounds.y && point.y < bounds.y + insets.top) {
                if (point.x >= bounds.x + bounds.width - insets.right && point.x < bounds.x + bounds.width) {
                    return ResizeProvider.ControlPoint.TOP_RIGHT;
                } else if (point.x >= bounds.x && point.x < bounds.x + insets.left) {
                    return ResizeProvider.ControlPoint.TOP_LEFT;
                } else if (point.x >= center.x - centerDimension.height / 2 && point.x < center.x + centerDimension.height - centerDimension.height / 2) {
                    return ResizeProvider.ControlPoint.TOP_CENTER;
                }
            } else if (point.y >= center.y - centerDimension.width / 2 && point.y < center.y + centerDimension.width - centerDimension.width / 2) {
                if (point.x >= bounds.x + bounds.width - insets.right && point.x < bounds.x + bounds.width) {
                    return ResizeProvider.ControlPoint.CENTER_RIGHT;
                } else if (point.x >= bounds.x && point.x < bounds.x + insets.left) {
                    return ResizeProvider.ControlPoint.CENTER_LEFT;
                }
            }

            return null;
        }

        public ControlPoint resolveSplitterControlPoint(Widget widget, Point point, int[] idx) {
            if (!(widget instanceof SplitterWidget)) {
                return null;
            }

            SplitterWidget wg = (SplitterWidget) widget;
            AdsUIItemDef splitter = (AdsUIItemDef) wg.getNode();

            int[] ps = LayoutUtil.getSplitterControlPoints(splitter, wg.getLayoutGeometry());
            if (ps == null) {
                return null;
            }

            Rectangle r = wg.getGeometry();
            Point p = wg.convertLocalToScene(point);
            AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(splitter, "orientation");
            if (EOrientation.Horizontal.equals(orientation.value)) {
                if (r.width < ResizeBorder.THICKNESS * 5) {
                    return null;
                }
                Rectangle rc;
                for (int i = 0; i < ps.length; i++) {
                    rc = new Rectangle(ps[i] - ResizeBorder.THICKNESS / 2, r.y - ResizeBorder.THICKNESS, ResizeBorder.THICKNESS, ResizeBorder.THICKNESS);
                    if (rc.contains(p)) {
                        idx[0] = i;
                        return ResizeProvider.ControlPoint.TOP_CENTER;
                    }
                    rc = new Rectangle(ps[i] - ResizeBorder.THICKNESS / 2, r.y + r.height, ResizeBorder.THICKNESS, ResizeBorder.THICKNESS);
                    if (rc.contains(p)) {
                        idx[0] = i;
                        return ResizeProvider.ControlPoint.BOTTOM_CENTER;
                    }
                }
            } else {
                if (r.height < ResizeBorder.THICKNESS * 5) {
                    return null;
                }
                Rectangle rc;
                for (int i = 0; i < ps.length; i++) {
                    rc = new Rectangle(r.x - ResizeBorder.THICKNESS, ps[i] - ResizeBorder.THICKNESS / 2, ResizeBorder.THICKNESS, ResizeBorder.THICKNESS);
                    if (rc.contains(p)) {
                        idx[0] = i;
                        return ResizeProvider.ControlPoint.CENTER_LEFT;
                    }
                    rc = new Rectangle(r.x + r.width, ps[i] - ResizeBorder.THICKNESS / 2, ResizeBorder.THICKNESS, ResizeBorder.THICKNESS);
                    if (rc.contains(p)) {
                        idx[0] = i;
                        return ResizeProvider.ControlPoint.CENTER_RIGHT;
                    }
                }
            }
            return null;
        }
    }

    public static class RootWidgetControlPointResolver implements ResizeControlPointResolver {

        private static final double PERCENT = 0.1d;

        @Override
        public ControlPoint resolveControlPoint(Widget widget, Point point) {
            Rectangle bounds = widget.getBounds();
            Insets insets = widget.getBorder().getInsets();
            int width = bounds.width - insets.right - insets.left;
            int height = bounds.height - insets.top - insets.bottom;
            if (point.y >= bounds.y + bounds.height - insets.bottom && point.y < bounds.y + bounds.height) {
                if (point.x >= bounds.x + bounds.width - insets.right - width * PERCENT && point.x < bounds.x + bounds.width) {
                    return ResizeProvider.ControlPoint.BOTTOM_RIGHT;
                } //                else if (point.x >= bounds.x && point.x < bounds.x + insets.left)
                //                    return ResizeProvider.ControlPoint.BOTTOM_LEFT;
                else {
                    return ResizeProvider.ControlPoint.BOTTOM_CENTER;
                }
//            } else if (point.y >= bounds.y && point.y < bounds.y + insets.top) {
//                if (point.x >= bounds.x + bounds.width - insets.right && point.x < bounds.x + bounds.width)
//                    return ResizeProvider.ControlPoint.TOP_RIGHT;
//                else if (point.x >= bounds.x && point.x < bounds.x + insets.left)
//                    return ResizeProvider.ControlPoint.TOP_LEFT;
//                else
//                    return ResizeProvider.ControlPoint.TOP_CENTER;
            } else if (point.y >= bounds.y + insets.top && point.y < bounds.y + bounds.height - insets.bottom) {
                if (point.x >= bounds.x + bounds.width - insets.right && point.x < bounds.x + bounds.width) {
                    if (point.y < bounds.y + bounds.height - insets.bottom - height * PERCENT) {
                        return ResizeProvider.ControlPoint.CENTER_RIGHT;
                    } else {
                        return ResizeProvider.ControlPoint.BOTTOM_RIGHT;
                    }
                }
//                else if (point.x >= bounds.x && point.x < bounds.x + insets.left)
//                    return ResizeProvider.ControlPoint.CENTER_LEFT;
            }

            // TODO - resolve CENTER points
            return null;
        }
    }
}
