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

import java.awt.Rectangle;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.IntProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.LocalizedStringRefProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.RectProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.Settings;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtPropertiesGridItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;

public class RwtLabeledEditGridLayout implements Layout {

    @Override
    public void layout(Widget widget) {
        justify(widget);
    }

    @Override
    public boolean requiresJustification(Widget widget) {
        return true;
    }

    public static class CellInfo {

        public Rectangle bounds;
        public int labelWidth;
        public String label;
        public int row;
        public int col;
        AdsRwtWidgetDef widget;
    }

    public interface LabelProvider {

        public String getLabel(AdsRwtWidgetDef widget);
    }

    public static class DefaultLabelProvider implements LabelProvider {

        @Override
        public String getLabel(AdsRwtWidgetDef widget) {
            LocalizedStringRefProperty prop = (LocalizedStringRefProperty) AdsUIUtil.getUiProperty(widget, "label");
            if (prop != null) {
                AdsMultilingualStringDef string = prop.findLocalizedString();
                if (string != null) {
                    return string.getValue(Settings.getLanguage());
                }
            }
            return "";
        }
    }

    private static int getColumn(AdsRwtWidgetDef child) {
        IntProperty propCol = (IntProperty) AdsUIUtil.getUiProperty(child, "gridColumn");
        int col = 0;
        if (propCol != null) {
            col = propCol.value;
            if (col < 0) {
                col = 0;
            }
        }
        return col;
    }

    private static int getColSpan(AdsRwtWidgetDef child) {
        IntProperty propCol = (IntProperty) AdsUIUtil.getUiProperty(child, "colSpan");
        int col = 1;
        if (propCol != null) {
            col = propCol.value;
            if (col < 1) {
                col = 1;
            }
        }
        return col;
    }

    private static int getRow(AdsRwtWidgetDef child) {
        IntProperty propRow = (IntProperty) AdsUIUtil.getUiProperty(child, "gridRow");
        int row = 0;
        if (propRow != null) {
            row = propRow.value;
            if (row < 0) {
                return -1;
            } else {
                return row;
            }
        }
        return -1;
    }

    static class ItemMap {

        private static final Object label = new Object();
        private List<List<Object>> rows = new LinkedList<List<Object>>();
        private int columnCount = 0;

        public void addColumn() {
            for (List<Object> row : rows) {
                row.add(null);
            }
            columnCount++;
        }

        public void addRow() {
            List<Object> row = new LinkedList<Object>();
            for (int i = 0; i < columnCount; i++) {
                row.add(null);
            }
            rows.add(row);
        }

        void addColumn(int index) {
            if (index < getColumnCount()) {
                for (List<Object> row : rows) {
                    row.add(index, null);
                }
                columnCount++;
            } else {
                addColumn();
            }
        }

        public int getColumnCount() {
            return columnCount;
        }

        public int getRowCount() {
            return rows.size();
        }

        public void set(int row, int column, AdsRwtWidgetDef widget) {
            rows.get(row).set(column, widget);
        }

        public boolean rowIsEmpty(int row) {
            for (int i = 0; i < columnCount; i++) {
                if (rows.get(row).get(i) != null) {
                    return false;
                }
            }
            return true;
        }

        private void pack(boolean applyLabels) {
            for (int i = 0; i < rows.size();) {
                if (rowIsEmpty(i)) {
                    rows.remove(i);
                } else {
                    i++;
                }
            }
            for (int c = 0; c < columnCount;) {
                boolean isEmpty = true;
                for (int r = 0; r < rows.size(); r++) {
                    if (rows.get(r).get(c) != null) {
                        isEmpty = false;
                        break;
                    }

                }
                if (isEmpty) {
                    for (int r = 0; r < rows.size(); r++) {
                        rows.get(r).remove(c);
                    }
                    columnCount--;
                } else {
                    c++;
                }
            }
            //apply label columns
            if (applyLabels) {
                for (int r = 0; r < rows.size(); r++) {
                    List<Object> row = rows.get(r);
                    if (row.get(0) == null) {
                        row.add(0, null);
                    } else {
                        row.add(0, label);
                    }
                }
                columnCount++;
                for (int c = 1; c < columnCount; c++) {
                    if (c % 2 == 0) {
                        for (int r2 = 0; r2 < rows.size(); r2++) {
                            List<Object> row2 = rows.get(r2);
                            Object prev2 = row2.get(c - 1);
                            Object cur = row2.get(c);
                            if (prev2 == cur) {//colspan
                                row2.add(c, cur);
                            } else if (cur == null) {
                                row2.add(c, label);
                            } else {
                                row2.add(c, label);
                            }
                        }
                        columnCount++;
                        c++;
                    }
                }
            }
        }

        int getColumnWidth(int col, LabelProvider labelProvider) {
            int maxWidth = -1;
            for (int r = 0; r < rows.size(); r++) {
                List<Object> row = rows.get(r);
                Object holder = row.get(col);
                if (holder == label) {
                    String text = "";
                    if (row.get(col + 1) != null) {
                        AdsRwtWidgetDef prop = (AdsRwtWidgetDef) row.get(col + 1);
                        text = labelProvider.getLabel(prop);
                    }
                    if (text != null) {
                        int width = DrawUtil.getFontMetrics().stringWidth(text);
                        if (width > maxWidth) {
                            maxWidth = width;
                        }
                    }
                }
            }
            return maxWidth;
        }

        int getRowHeight(int row) {
            int rowHeight = -1;
            if (row < getRowCount()) {
                final List<Object> r = rows.get(row);
                for (final Object obj : r) {
                    if (obj != null && obj instanceof AdsRwtWidgetDef) {
                        final AdsRwtWidgetDef widget = (AdsRwtWidgetDef) obj;
                        final RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
                        final int h = geometry.height;
                        rowHeight = Math.max(rowHeight, h);

                    }
                }
            }
            return rowHeight;
        }

        boolean isEmpty() {
            for (int i = 0; i < rows.size(); i++) {
                if (!rowIsEmpty(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    static ItemMap fillItemMap(AdsRwtWidgetDef rw) {
        ItemMap map = new ItemMap();
        List<AdsRwtWidgetDef> children = rw.getWidgets().list();
        for (AdsRwtWidgetDef child : children) {
            int row = getRow(child);
            int col = getColumn(child);
            int colSpan = getColSpan(child);
            if (row < 0) {
                map.addRow();
                row = map.getRowCount() - 1;
            } else {
                while (map.getRowCount() <= row + 1) {
                    map.addRow();
                }
            }
            if (col < 0) {
                map.addColumn();
                col = map.getColumnCount() - 1;
            } else {
                while (map.getColumnCount() <= col + colSpan) {
                    map.addColumn();
                }
            }
            for (int i = 0; i < colSpan; i++) {
                map.set(row, col + i, child);
            }
        }
        return map;


    }

    static class ExtRect extends Rectangle {

        AdsRwtWidgetDef widget;
        int row;
        int col;

        public ExtRect(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        public ExtRect(Rectangle r) {
            super(r);
        }
    }

    public static ExtRect[][] computeRectangles(AdsRwtWidgetDef rw, Rectangle r) {
        LabelProvider labelProvider;
        if (AdsMetaInfo.RWT_PROPERTIES_GRID.equals(rw.getClassName())) {
            labelProvider = new RwtPropertiesGridItem.PropLabelProvider();
        } else {
            labelProvider = new DefaultLabelProvider();
        }
        ItemMap map = fillItemMap(rw);
        map.pack(true);

        if (map.isEmpty()) {
            ExtRect rect = new ExtRect(r);
            rect.height = 20;
            rect.width = 100;//by default
            rect.col = 0;
            rect.row = 0;
            return new ExtRect[][]{{rect}};
        }

        int[] widths = new int[map.getColumnCount()];//Ð¾Ð±Ñ‰Ð°Ñ� ÑˆÐ¸Ñ€Ð¸Ð½Ð° Ð²Ð¸Ð´Ð¶ÐµÑ‚Ð¾Ð²

        ExtRect[][] rectangles = new ExtRect[map.rows.size() + 1][];
        int occupied = 0;
        int mutableCount = 0;
        for (int i = 0; i < widths.length; i++) {
            int w = map.getColumnWidth(i, labelProvider);
            widths[i] = w;
            if (w >= 0) {
                occupied += widths[i];
            } else {
                mutableCount++;
            }
        }

        int gapCount = widths.length + 2;
        int gapSize = gapCount * 3;
        float free = r.width - occupied - gapSize;
        int mutableWidth = mutableCount > 0 ? Math.round(free / mutableCount) : 0;
        for (int i = 0; i < widths.length; i++) {
            if (widths[i] < 0) {
                widths[i] = mutableWidth + 3;
            } else {
                widths[i] += 3;
            }
        }

        int starty = r.y + 3;
        int row;
        for (row = 0; row < map.rows.size(); row++) {
            int startx = r.x + 3;
            int maxItemHeight = 0;
            rectangles[row] = new ExtRect[map.rows.get(row).size()];

            for (int col = 0; col < map.rows.get(row).size(); col++) {
                Object obj = map.rows.get(row).get(col);
                rectangles[row][col] = new ExtRect(startx, starty, widths[col], map.getRowHeight(row));
                rectangles[row][col].col = col;
                rectangles[row][col].row = row;
                if (col % 2 == 0) {
                    rectangles[row][col / 2] = new ExtRect(startx, starty, widths[col], map.getRowHeight(row));
                    rectangles[row][col / 2].col = col / 2;
                    rectangles[row][col / 2].row = row;
                } else {
                    rectangles[row][(col - 1) / 2].width += widths[col];
                    startx = rectangles[row][((col - 1) / 2)].x + rectangles[row][(col - 1) / 2].width;
                    rectangles[row][col].x = startx;
                    if (obj != null) {
                        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) obj;
                        RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
                        if (maxItemHeight < geometry.height) {
                            maxItemHeight = geometry.height;
                        }
                        rectangles[row][(col - 1) / 2].widget = widget;
                    }
                }
            }
            for (int i = 0; i < rectangles[row].length; i++) {
                rectangles[row][i].height = maxItemHeight + 3;
            }
            starty += maxItemHeight + 3;
        }
        //add generic row
        rectangles[row] = new ExtRect[map.getColumnCount()];
        for (int j = 0; j < map.columnCount; j++) {
            rectangles[row][j] = new ExtRect(0, 0, 100, 20);
            rectangles[row][j].y = starty;
            rectangles[row][j].height = 30;

            rectangles[row][j].col = j;
            rectangles[row][j].row = row;
            if (j > 0) {
                rectangles[row][j].x = rectangles[row][j - 1].x + rectangles[row][j - 1].width + 3;

            }
            if (row > 0) {
                rectangles[row][j].width = rectangles[row - 1][j].width;
            }
        }
        return rectangles;
    }

    public static List<CellInfo> computeCellInfo(AdsRwtWidgetDef rw, Rectangle r) {
        ExtRect[][] rects = computeRectangles(rw, r);
        LabelProvider labelProvider;
        if (AdsMetaInfo.RWT_PROPERTIES_GRID.equals(rw.getClassName())) {
            labelProvider = new RwtPropertiesGridItem.PropLabelProvider();
        } else {
            labelProvider = new DefaultLabelProvider();
        }


        ItemMap map = fillItemMap(rw);

        map.pack(true);

        if (map.isEmpty()) {
            return Collections.emptyList();
        }


        List<CellInfo> columnInfos = new LinkedList<CellInfo>();


        int[] widths = new int[map.getColumnCount()];

        int occupied = 0;
        int mutableCount = 0;
        for (int i = 0; i < widths.length; i++) {

            int w = map.getColumnWidth(i, labelProvider);
            widths[i] = w;
            if (w >= 0) {
                occupied += widths[i];
            } else {
                mutableCount++;
            }
        }

        int gapCount = widths.length + 2;
        int gapSize = gapCount * 3;
        float free = r.width - occupied - gapSize;
        int mutableWidth = mutableCount > 0 ? Math.round(free / mutableCount) : 0;

        for (int i = 0; i < widths.length; i++) {
            if (widths[i] < 0) {
                widths[i] = mutableWidth;
            }
        }

        int row = 0;

        for (List<Object> list : map.rows) {

            Object prev = null;
            CellInfo current = null;
            int startx = r.x + 3;
            int maxItemHeight = 0;
            int actialCol = 0;
            for (int col = 0; col < list.size(); col++) {
                ExtRect cell = rects[row][col];
                Object obj = list.get(col);
                int starty = cell.y;
                if (obj == map.label || obj == null) {
                    prev = obj;
                } else {
                    if (prev == obj) {
                        current.bounds.width += widths[col] + 3;
                        actialCol++;
                    } else {

                        current = new CellInfo();
                        current.bounds = new Rectangle();
                        current.row = row;
                        current.col = actialCol;

                        current.labelWidth = widths[col - 1];
                        current.bounds.width = widths[col] + current.labelWidth + 3;
                        current.bounds.x = startx - current.labelWidth;
                        current.widget = (AdsRwtWidgetDef) obj;
                        current.label = labelProvider.getLabel(current.widget);
                        RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(current.widget, "geometry");
                        current.bounds.height = geometry.height;
                        current.bounds.y = starty;
                        if (maxItemHeight < geometry.height) {
                            maxItemHeight = geometry.height;
                        }
                        columnInfos.add(current);
                        actialCol++;
                    }
                    prev = obj;
                }
                startx += widths[col] + 3;
            }
            //starty += maxItemHeight + 3;
            row++;
        }

        return columnInfos;
    }

    @Override
    public void justify(Widget widget) {
        synchronized (LayoutProcessor.LOCK) {
            BaseWidget w = (BaseWidget) widget;


            List<CellInfo> cellInfos = computeCellInfo((AdsRwtWidgetDef) w.getNode(), w.getInnerGeometry());

            GraphSceneImpl scene = (GraphSceneImpl) w.getScene();


            for (CellInfo info : cellInfos) {
                BaseWidget rcw = (BaseWidget) scene.findWidget(info.widget);
                Rectangle rect = new Rectangle(info.bounds);
                rect.width -= info.labelWidth;
                rect.x += info.labelWidth;

                rcw.setGeometry(rect);
                rcw.revalidate();
            }

            scene.repaint();
        }
    }
}