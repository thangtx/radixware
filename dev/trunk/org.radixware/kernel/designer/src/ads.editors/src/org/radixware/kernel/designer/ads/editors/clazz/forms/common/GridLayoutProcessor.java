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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.ESizePolicy;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class GridLayoutProcessor extends LayoutProcessor {

    private final static double HPERCENT = 0.1;
    private final static double VPERCENT = 0.5;

    private enum APPEND_MODE {

        HORIZONTAL,
        VERTICAL,
        REPLACE,
        CORNER;
    }
    private AdsLayout layout;

    public GridLayoutProcessor(BaseWidget wg, AdsWidgetDef widget) {
        super(wg);
        layout = widget.getLayout();
        if (layout == null || !layout.getClassName().equals(AdsMetaInfo.GRID_LAYOUT_CLASS)) {
            wg.setLayout(LayoutFactory.createAbsoluteLayout()); // reset layout to default
            AdsUIUtil.layoutToWidgets(widget);
            AdsUIUtil.widgetsToLayout(widget, layout = new AdsLayout(AdsMetaInfo.GRID_LAYOUT_CLASS));
            saveGeometry(widget);

            int idx = 0;
            for (AdsLayout.Item item : layout.getItems()) {
                item.row = 0;
                item.column = idx++;
                item.rowSpan = 1;
                item.columnSpan = 1;
            }
        }

        wg.setLayout(new GridLayout());
        wg.revalidate();
    }

    public GridLayoutProcessor(BaseWidget wg) {
        this(wg, (AdsWidgetDef) AdsUIUtil.currentWidget((AdsWidgetDef) wg.getSceneImpl().findObject(wg)));
    }

    public GridLayoutProcessor(BaseWidget wg, AdsLayout layout) {
        super(wg);
        this.layout = layout;
        wg.setLayout(new GridLayout());
        wg.revalidate();
    }

    private APPEND_MODE indexFromPoint(Point p, double itemWidth[], double itemHeight[], int idx[]) {
        final Rectangle r = wg.getLayoutGeometry();
        int hspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutHorizontalSpacing")).value;
        hspacing = Math.max(hspacing, LayoutUtil.MIN_SPACING);
        int vspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutVerticalSpacing")).value;
        vspacing = Math.max(vspacing, LayoutUtil.MIN_SPACING);

        if (layout.getItems().size() == 0) {
            idx[0] = idx[1] = 0;
            return APPEND_MODE.VERTICAL;
        }

        AdsLayout.Item[][] items = layout.getItemsAsArray();
        int rows = items.length;
        int cols = items[0].length;

        int minHeight[] = new int[rows];
        int maxHeight[] = new int[rows];
        int hintHeight[] = new int[rows];
        ESizePolicy vsp[] = new ESizePolicy[rows];
        LayoutUtil.calcGridHeight(layout, r, minHeight, maxHeight, hintHeight, itemHeight, vsp);

        int minWidth[] = new int[cols];
        int maxWidth[] = new int[cols];
        int hintWidth[] = new int[cols];
        ESizePolicy hsp[] = new ESizePolicy[cols];
        LayoutUtil.calcGridWidth(layout, r, minWidth, maxWidth, hintWidth, itemWidth, hsp);

        int totalHeight = 0;
        for (int i = 0; i < rows; i++) {
            totalHeight += itemHeight[i];
        }

        double vmargin = (double) (r.height - totalHeight - vspacing * (rows - 1)) / (rows * 2);
        for (int i = 0; i < rows; i++) {
            itemHeight[i] += 2 * vmargin;
        }

        int totalWidth = 0;
        for (int i = 0; i < cols; i++) {
            totalWidth += itemWidth[i];
        }

        double hmargin = (double) (r.width - totalWidth - hspacing * (cols - 1)) / (cols * 2);
        for (int i = 0; i < cols; i++) {
            itemWidth[i] += 2 * hmargin;
        }

        if (p.x < r.x && p.y < r.y) {
            idx[0] = 0;
            idx[1] = 0;
            return APPEND_MODE.CORNER;
        }

        if (p.x < r.x && p.y >= r.y + r.height) {
            idx[0] = rows;
            idx[1] = 0;
            return APPEND_MODE.CORNER;
        }

        if (p.x >= r.x + r.width && p.y < r.y) {
            idx[0] = 0;
            idx[1] = cols;
            return APPEND_MODE.CORNER;
        }

        if (p.x >= r.x + r.width && p.y >= r.y + r.height) {
            idx[0] = rows;
            idx[1] = cols;
            return APPEND_MODE.CORNER;
        }

        int row = -1, col = -1;

        double voffs = r.y;
        for (int i = 0; i < rows; i++) {
            if (p.y >= voffs - (i > 0 ? vspacing / 2.0 : 0) && p.y < voffs + itemHeight[i] + (i < rows - 1 ? vspacing / 2.0 : 0)) {
                row = i;
            }
            voffs += itemHeight[i] + vspacing;
        }

        double hoffs = r.x;
        for (int i = 0; i < cols; i++) {
            if (p.x >= hoffs - (i > 0 ? hspacing / 2.0 : 0) && p.x < hoffs + itemWidth[i] + (i < cols - 1 ? hspacing / 2.0 : 0)) {
                col = i;
            }
            hoffs += itemWidth[i] + hspacing;
        }

        if (row >= 0 && col >= 0) {
            if (items[row][col] == null) {
                idx[0] = row;
                idx[1] = col;
                return APPEND_MODE.REPLACE;
            }
        }

        if (p.x < r.x) {
            idx[0] = row;
            idx[1] = 0;
            return APPEND_MODE.HORIZONTAL;
        }

        if (p.x >= r.x + r.width) {
            idx[0] = row;
            idx[1] = cols;
            return APPEND_MODE.HORIZONTAL;
        }

        if (p.y < r.y) {
            idx[0] = 0;
            idx[1] = col;
            return APPEND_MODE.VERTICAL;
        }

        if (p.y >= r.y + r.height) {
            idx[0] = rows;
            idx[1] = col;
            return APPEND_MODE.VERTICAL;
        }

        //APPEND_MODE mode = APPEND_MODE.VERTICAL;
        idx[0] = -1;
        idx[1] = -1;

        hoffs = r.x;
        for (int i = 0; i < cols; i++) {
            if (i == 0 && hoffs + itemWidth[i] * HPERCENT > p.x) {
                idx[1] = i;
            } else if (i == cols - 1 && hoffs + itemWidth[i] * (1 - HPERCENT) <= p.x) {
                idx[1] = i + 1;
            } else {
                if (i > 0) {
                    if (hoffs - itemWidth[i - 1] * 0.5 - hspacing <= p.x && hoffs + itemWidth[i] * HPERCENT > p.x) {
                        idx[1] = i;
                    }
                }
            }
            hoffs += itemWidth[i] + hspacing;
        }

        if (idx[1] >= 0) {
            idx[0] = row;
            return APPEND_MODE.HORIZONTAL;
        }

        voffs = r.y;
        for (int i = 0; i < rows; i++) {
            if (i == 0 && voffs + itemHeight[i] * VPERCENT > p.y) {
                idx[0] = i;
            } else if (i == rows - 1 && voffs + itemHeight[i] * (1 - VPERCENT) <= p.y) {
                idx[0] = i + 1;
            } else {
                if (i > 0) {
                    if (voffs - itemHeight[i - 1] * VPERCENT - vspacing <= p.y && voffs + itemHeight[i] * VPERCENT > p.y) {
                        idx[0] = i;
                    }
                }
            }
            voffs += itemHeight[i] + vspacing;
        }

        idx[1] = col;
        return APPEND_MODE.VERTICAL;
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        Point p = wg.convertLocalToGeometry(localPoint);
        assert layout != null : "layout cann't be null";

        final Rectangle r = wg.getLayoutGeometry();
        int hspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutHorizontalSpacing")).value;
        hspacing = Math.max(hspacing, LayoutUtil.MIN_SPACING);
        int vspacing = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutVerticalSpacing")).value;
        vspacing = Math.max(vspacing, LayoutUtil.MIN_SPACING);

        if (layout.getItems().size() == 0) {
            // draw rect
            Point p1 = new Point(r.x - 2, r.y + r.height);
            Point p2 = new Point(r.x - 2, r.y - 2);
            Point p3 = new Point(r.x + r.width, r.y - 2);
            Point p4 = new Point(r.x + r.width, r.y + r.height);
            Point p5 = new Point(r.x - 2, r.y + r.height);
            locator.locate(Arrays.asList(p1, p2, p3, p4, p5));
            return;
        }

        AdsLayout.Item[][] items = layout.getItemsAsArray();
        int rows = items.length;
        int cols = items[0].length;

        double itemHeight[] = new double[rows];
        double itemWidth[] = new double[cols];

        int idx[] = new int[]{0, 0};
        APPEND_MODE mode = indexFromPoint(p, itemWidth, itemHeight, idx);
        int row = idx[0], col = idx[1];

        double voffs[] = new double[rows];
        for (int i = 0; i < rows; i++) {
            voffs[i] = i > 0 ? voffs[i - 1] + itemHeight[i - 1] + vspacing : r.y;
        }

        double hoffs[] = new double[cols];
        for (int i = 0; i < cols; i++) {
            hoffs[i] = i > 0 ? hoffs[i - 1] + itemWidth[i - 1] + hspacing : r.x;
        }

        if (mode.equals(APPEND_MODE.REPLACE)) {
            // draw rect
            Point p1 = new Point((int) Math.round(hoffs[col] - 2), (int) Math.round(voffs[row] + itemHeight[row]));
            Point p2 = new Point((int) Math.round(hoffs[col] - 2), (int) Math.round(voffs[row] - 2));
            Point p3 = new Point((int) Math.round(hoffs[col] + itemWidth[col]), (int) Math.round(voffs[row] - 2));
            Point p4 = new Point((int) Math.round(hoffs[col] + itemWidth[col]), (int) Math.round(voffs[row] + itemHeight[row]));
            Point p5 = new Point((int) Math.round(hoffs[col] - 2), (int) Math.round(voffs[row] + itemHeight[row]));
            locator.locate(Arrays.asList(p1, p2, p3, p4, p5));
        } else if (mode.equals(APPEND_MODE.CORNER)) {
            if (row == 0 && col == 0) {
                Point p1 = new Point((int) Math.round(hoffs[0] - 2), (int) Math.round(voffs[0] - 2));
                Point p2 = new Point((int) Math.round(hoffs[0]), (int) Math.round(voffs[0] - 2));
                locator.locate(Arrays.asList(p1, p2));
            } else if (row == 0 && col == cols) {
                Point p1 = new Point((int) Math.round(hoffs[cols - 1] + itemWidth[cols - 1]), (int) Math.round(voffs[0] - 2));
                Point p2 = new Point((int) Math.round(hoffs[cols - 1] + itemWidth[cols - 1] + 2), (int) Math.round(voffs[0] - 2));
                locator.locate(Arrays.asList(p1, p2));
            } else if (row == rows && col == 0) {
                Point p1 = new Point((int) Math.round(hoffs[0] - 2), (int) Math.round(voffs[rows - 1] + itemHeight[rows - 1]));
                Point p2 = new Point((int) Math.round(hoffs[0]), (int) Math.round(voffs[rows - 1] + itemHeight[rows - 1]));
                locator.locate(Arrays.asList(p1, p2));
            } else if (row == rows && col == cols) {
                Point p1 = new Point((int) Math.round(hoffs[cols - 1] + itemWidth[cols - 1]), (int) Math.round(voffs[rows - 1] + itemHeight[rows - 1]));
                Point p2 = new Point((int) Math.round(hoffs[cols - 1] + itemWidth[cols - 1] + 2), (int) Math.round(voffs[rows - 1] + itemHeight[rows - 1]));
                locator.locate(Arrays.asList(p1, p2));
            }
        } else {
            if (mode.equals(APPEND_MODE.HORIZONTAL)) {
                double offs;
                if (col == 0) {
                    offs = hoffs[0] - 2;
                } else if (col == cols) {
                    offs = hoffs[col - 1] + itemWidth[col - 1];
                } else {
                    offs = hoffs[col] - hspacing / 2 - 1;
                }
                Point p1 = new Point((int) Math.round(offs), (int) Math.round(voffs[row]));
                Point p2 = new Point((int) Math.round(offs), (int) Math.round(voffs[row] + itemHeight[row]));
                locator.locate(Arrays.asList(p1, p2));
            } else {
                double offs;
                if (row == 0) {
                    offs = voffs[0] - 2;
                } else if (row == rows) {
                    offs = voffs[row - 1] + itemHeight[row - 1];
                } else {
                    offs = voffs[row] - vspacing / 2 - 1;
                }
                Point p1 = new Point((int) Math.round(hoffs[col]), (int) Math.round(offs));
                Point p2 = new Point((int) Math.round(hoffs[col] + itemWidth[col]), (int) Math.round(offs));
                locator.locate(Arrays.asList(p1, p2));
            }
        }
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {
        Point p = wg.convertLocalToGeometry(localPoint);
        AdsLayout.Item items[][] = layout.getItemsAsArray();
        int rows = items.length;
        int cols = items.length > 0 ? items[0].length : 0;

        AdsLayout.Item item = null;
        if (node instanceof AdsLayout) {
            item = new AdsLayout.LayoutItem((AdsLayout) node);
        } else if (node instanceof AdsWidgetDef) {
            item = new AdsLayout.WidgetItem((AdsWidgetDef) node);
        } else if (node instanceof AdsLayout.SpacerItem) {
            item = (AdsLayout.SpacerItem) node;
        }
        assert item != null : "item can't be null: " + layout + ", " + node + ", " + localPoint;

        item.rowSpan = 1;
        item.columnSpan = 1;

        double itemHeight[] = new double[rows];
        double itemWidth[] = new double[cols];

        final int idx[] = new int[]{0, 0};
        APPEND_MODE mode = indexFromPoint(p, itemWidth, itemHeight, idx);
        final int row = idx[0], col = idx[1];

        if (cols != 0 && rows != 0) {
            if (mode.equals(APPEND_MODE.HORIZONTAL) || mode.equals(APPEND_MODE.CORNER)) {
                for (AdsLayout.Item elem : layout.getItems()) {
                    if (elem.column >= col) {
                        elem.column++;
                    }
                }
            }
            if (mode.equals(APPEND_MODE.VERTICAL) || mode.equals(APPEND_MODE.CORNER)) {
                for (AdsLayout.Item elem : layout.getItems()) {
                    if (elem.row >= row) {
                        elem.row++;
                    }
                }
            }
        }

        item.row = row;
        item.column = col;
        layout.getItems().add(item);

        wg.getSceneImpl().revalidate();
        return item;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        AdsLayout.Item item = null;
        if (node instanceof AdsLayout.SpacerItem) {
            item = (AdsLayout.SpacerItem) node;
        } else if (node.getContainer() instanceof AdsLayout.Item) {
            item = (AdsLayout.Item) node.getContainer();
        }

        if (item != null && layout.equals(item.getOwnerLayout())) {
            node.delete();
            layout.adjustItems();
            wg.getSceneImpl().revalidate();
            return item;
        }

        return null;
    }
}
