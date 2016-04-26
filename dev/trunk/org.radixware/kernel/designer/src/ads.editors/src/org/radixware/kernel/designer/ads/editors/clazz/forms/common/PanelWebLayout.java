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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Row;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;

import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class PanelWebLayout implements Layout {

    public PanelWebLayout() {
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

            GraphSceneImpl scene = (GraphSceneImpl) widget.getScene();
            RadixObject node = (RadixObject) scene.findObject(widget);

            AdsRwtWidgetDef.PanelGrid grid = null;

            if (node instanceof AdsRwtWidgetDef) {
                grid = ((AdsRwtWidgetDef) AdsUIUtil.currentWidget(((AdsRwtWidgetDef) node))).getPanelGrid();
            }

            if (grid == null) {
                System.err.println("error: horizontal layout can not be null");
                return;
            }
            assert grid != null : "layout cann't be null";

            final BaseWidget wg = (BaseWidget) widget;



            final Rectangle r = wg.getGeometry();
            final Rectangle lr = wg.getLayoutGeometry();

            Map<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> cr = computeLayout(grid, r);
            for (AdsRwtWidgetDef.PanelGrid.Row row : grid.getRows()) {
                for (AdsRwtWidgetDef.PanelGrid.Row.Cell cell : row.getCells()) {
                    AdsRwtWidgetDef item = cell.findWidget();
                    if (item != null) {
                        BaseWidget w = (BaseWidget) scene.findWidget(item);
                        if (w != null) {
                            Rectangle rect = cr.get(cell);
                            if (rect != null) {
                                Rectangle selfGeometry = w.getGeometry();

                                selfGeometry.x = r.x + rect.x + rect.width / 2 - selfGeometry.width / 2;
                                selfGeometry.y = r.y + rect.y + rect.height / 2 - selfGeometry.height / 2;

                                w.setGeometry(selfGeometry);
                            }
                        }
                    }
                }
            }

            scene.repaint();
        }
    }

    public static Map<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> computeLayout(AdsRwtWidgetDef.PanelGrid grid, Rectangle rect) {
        return computeLayout(grid, rect, null);
    }

    public static Map<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> computeLayout(AdsRwtWidgetDef.PanelGrid grid, Rectangle rect, Map<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> reuse) {
        Map<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> map = reuse == null ? new HashMap<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle>() : reuse;
        map.clear();


        List<AdsRwtWidgetDef.PanelGrid.Row> rows = grid.getRows();
        int rowCount = rows.size();
        if (rowCount > 0) {
            int rowHeight = rect.height / rowCount;
            int rowTop = 0;//rect.y;
            Map<AdsRwtWidgetDef.PanelGrid.Row, List<AdsRwtWidgetDef.PanelGrid.Row.Cell>> row2cell = new HashMap<AdsRwtWidgetDef.PanelGrid.Row, List<AdsRwtWidgetDef.PanelGrid.Row.Cell>>();
            int maxCell = 0;



            for (AdsRwtWidgetDef.PanelGrid.Row row : rows) {
                List<AdsRwtWidgetDef.PanelGrid.Row.Cell> cells = row.getCells();
                if (cells.size() > maxCell) {
                    maxCell = cells.size();
                }
                row2cell.put(row, cells);
            }
            boolean empty = true;
            boolean nofit = true;
            if (maxCell > 0) {
                int[] cellWidths = new int[maxCell];
                Arrays.fill(cellWidths, -1);

                for (AdsRwtWidgetDef.PanelGrid.Row row : rows) {
                    List<AdsRwtWidgetDef.PanelGrid.Row.Cell> cells = row2cell.get(row);
                    for (int i = 0; i < cells.size(); i++) {
                        AdsRwtWidgetDef.PanelGrid.Row.Cell cell = cells.get(i);
                        AdsRwtWidgetDef widget = cell.findWidget();
                        if (widget != null) {
                            empty = false;
                            AdsUIProperty.RectProperty geometry = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
                            if (geometry != null) {
                                if (cell.isHFit()) {
                                    nofit = false;
                                } else {
                                    if (cellWidths[i] < geometry.width) {
                                        cellWidths[i] = geometry.width;
                                    }
                                }
                            } else {
                                if (cellWidths[i] < 0) {
                                    cellWidths[i] = 10;
                                }
                            }
                        } else {
                            if (cellWidths[i] < 0) {
                                cellWidths[i] = 10;
                            }
                        }
                    }
                }

                int total = 0;
                int minSize = 0;
                int tc = 0;
                for (int i = 0; i < maxCell; i++) {
                    if (cellWidths[i] < 0) {
                        cellWidths[i] = 10;
                    }
                    if (cellWidths[i] <= 10 && !empty) {
                        minSize += 10;
                    } else {
                        total += cellWidths[i];
                        tc++;
                    }
                }
                if (nofit) {
                    if (total < rect.width) {
                        float[] parts = new float[maxCell];
                        for (int i = 0; i < maxCell; i++) {
                            parts[i] = (float) cellWidths[i] / (float) total;
                        }
                        total = 0;

                        for (int i = 0; i < maxCell; i++) {
                            if (cellWidths[i] > 10 || empty) {
                                cellWidths[i] = Math.round(rect.width * parts[i]);
                                total += cellWidths[i];
                            }
                        }
                    }
                } 

                if (total + minSize > rect.width) {
                    int dec = Math.round((float) (total + minSize - rect.width) / (float) tc);
                    for (int i = 0; i < maxCell; i++) {
                        if (cellWidths[i] > 10 && cellWidths[i] - dec > 10) {
                            cellWidths[i] -= dec;
                        }
                    }
                }

                for (AdsRwtWidgetDef.PanelGrid.Row row : rows) {
                    List<AdsRwtWidgetDef.PanelGrid.Row.Cell> cells = row2cell.get(row);

                    if (maxCell > 0) {

                        int cellLeft = 0;//rect.x;

                        for (int i = 0; i < cells.size(); i++) {
                            AdsRwtWidgetDef.PanelGrid.Row.Cell cell = cells.get(i);
                            int cellWidth = cellWidths[i];
                            map.put(cell, new Rectangle(cellLeft, rowTop, cellWidth, rowHeight));
                            cellLeft += cellWidth;
                        }
                    }
                    rowTop += rowHeight;
                }
            }
        }
        return map;
    }

    public static Map.Entry<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> cellFromPoint(BaseWidget wg, Point localPoint) {

        //GraphSceneImpl scene = wg.getSceneImpl();
        Point p = wg.convertLocalToGeometry(localPoint);

        RadixObject node = wg.getNode();
        AdsRwtWidgetDef.PanelGrid grid = null;
        if (node instanceof AdsRwtWidgetDef) {
            grid = ((AdsRwtWidgetDef) node).getPanelGrid();
        }
        if (grid == null) {
            return null;
        }


        Point matchPoint = new Point(localPoint);

        Map<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> map = PanelWebLayout.computeLayout(grid, wg.getGeometry());

        for (Map.Entry<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> e : map.entrySet()) {
            Rectangle r = e.getValue();
            if (matchPoint.x > r.x && matchPoint.x < r.x + r.width && matchPoint.y > r.y && matchPoint.y < r.y + r.height) {
                return e;
            }
        }

        return null;

    }
}
