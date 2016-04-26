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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.IntProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.RectProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;

public class GridBoxWebLayout implements Layout {

    public static int gap = 3;
    public static LinkedList<CellInfo> occupied;
    private static TableMesh mesh;

    public static class CellInfo {

        public Rectangle bounds;
        public int row;
        public int col;
        public int colspan;
        public int rowspan;
        public AdsRwtWidgetDef widget;
    }

    public static class CellRect extends Rectangle {

        AdsRwtWidgetDef widget;
        int cellRow;
        int cellCol;

        public CellRect(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        public CellRect(Rectangle r) {
            super(r);
        }
    }

    static class TableMesh {

        private List<List<Object>> rows = new LinkedList<List<Object>>();
        private int columnCount = -1;

        int getColumnCount() {
            int cc = -1;
            if (columnCount < 0) {
                columnCount = 0;
            } else if (columnCount >= 0) {
                for (List<Object> row : rows) {
                    if (row != null) {
                        cc = Math.max(cc, row.size());
                    }
                }
                columnCount = cc;
            }
            return columnCount;
        }

        int getRowCount() {
            return rows.size();
        }

        boolean isRowEmpty(int row) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (rows.get(row).get(i) != null) {
                    return false;
                }
            }
            return true;
        }

        int getColumnWidth(int col) {

            int colWidth = -1;
            for (List<Object> row : rows) {
                if (col >= getColumnCount() || row == null) {
                } else {

                    Object obj = row.get(col);

                    if (obj != null) {
                        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) obj;
                        RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
                        int width = geometry.width;
                        colWidth = Math.max(width, colWidth);

                    }
                }
            }
            return colWidth;
        }

        int getRowHeight(int row) {
            int rowHeight = -1;
            if (row > getRowCount()) {
                //do nothing
            } else {
                List<Object> r = rows.get(row);
                for (Object obj : r) {
                    if (obj != null) {
                        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) obj;
                        RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
                        int h = geometry.height;
                        rowHeight = Math.max(rowHeight, h);

                    }
                }
            }
            return rowHeight;
        }

        boolean isEmpty() {
            for (int i = 0; i < rows.size(); i++) {
                if (!isRowEmpty(i)) {
                    return false;
                }
            }
            return true;
        }

        void addColumn() {
            for (List<Object> row : rows) {
                row.add(null);
            }
            columnCount++;
        }

        void addRow() {
            List<Object> row = new LinkedList<Object>();
            int cc = getColumnCount();
            for (int i = 0; i < cc; i++) {
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

        void addRow(int index) {
            if (index < getRowCount()) {
                List<Object> row = new LinkedList<Object>();
                int cc = getColumnCount();
                for (int i = 0; i < cc; i++) {
                    row.add(null);
                }
                rows.add(index, row);
            } else {
                addRow();
            }
        }

        void set(int row, int column, AdsRwtWidgetDef widget, int rowspan, int colspan) {
            if (rows.get(row).get(column) != null) {
                while (rowspan > 1) {
                    addRow(row + 1);
                    rowspan--;
                }
                while (colspan > 1) {
                    addColumn(column + 1);
                    colspan--;
                }
            }
            rows.get(row).set(column, widget);
        }

        void removeColumn(int idx) {

            if (idx >= getColumnCount() - 1 || idx < 0) {
                return;
            }
            for (List<Object> row : rows) {
                if (row == null) {
                } else if (!row.isEmpty()) {
                    Object obj = row.get(idx);
                    row.remove(obj);
                }
            }
        }

        void removeRow(int idx) {

            if (idx >= rows.size() - 1 || idx < 0) {
                return;
            }
            rows.get(idx).clear();
            List<Object> list = rows.remove(idx);

        }
    }
    //end of table newmesh

    static TableMesh fillTableMesh(AdsRwtWidgetDef rw) {

        TableMesh newMesh = new TableMesh();

        List<AdsRwtWidgetDef> children = rw.getWidgets().list();
        for (AdsRwtWidgetDef child : children) {
            int row = getWidgetRow(child);
            int col = getWidgetColumn(child);
            int colSpan = getColSpan(child);
            int rowSpan = getRowSpan(child);
            //create newmesh
            if (row < 0) {
                newMesh.addRow();
                row = newMesh.getRowCount() - 1;

            } else if (row >= 0) {
                while (newMesh.getRowCount() <= row + rowSpan) {
                    newMesh.addRow();
                }
            }
            if (col < 0) {
                newMesh.addColumn();
                col = newMesh.getColumnCount() - 1;

            } else if (col >= 0) {
                while (newMesh.getColumnCount() <= col + colSpan) {
                    newMesh.addColumn();
                }
            }
            //fill the newmesh
            for (int i = row; i < row + rowSpan; i++) {
                for (int j = col; j < col + colSpan; j++) {
                    newMesh.set(i, j, child, rowSpan, colSpan);
                }
            }
        }

        mesh = newMesh;
        return newMesh;
    }

    public static LinkedList<LinkedList> getRowsList() {//should return empty lists!

        LinkedList<LinkedList> rowslist = new LinkedList();
        if (!occupied.isEmpty()) {

            for (int i = 0; i < mesh.getRowCount() - 1; i++) {
                if (getRowItemsAt(i).isEmpty()) {
                    rowslist.add(null);
                } else {
                    rowslist.add(getRowItemsAt(i));
                }
            }
        }
        return rowslist;
    }

    public static LinkedList<LinkedList> getColumnsList() {//should return empty lists!
        LinkedList colslist = new LinkedList();
        if (!occupied.isEmpty()) {

            for (int i = 0; i < mesh.getColumnCount() - 1; i++) {
                if (getColumnItemsAt(i).isEmpty()) {
                    colslist.add(null);
                } else {
                    colslist.add(getColumnItemsAt(i));
                }
            }
        }
        return colslist;
    }

    public static LinkedList getRowItemsAt(int index) {
        LinkedList<CellInfo> rowlist = new LinkedList();
        if (index >= 0 && index < mesh.getRowCount() - 1 && !occupied.isEmpty()) {

            for (CellInfo info : occupied) {
                if (info.row == index) {
                    rowlist.add(info);
                }
            }
        }
        return rowlist;
    }

    public static LinkedList getColumnItemsAt(int index) {
        LinkedList<CellInfo> collist = new LinkedList();
        if (index >= 0 && index < mesh.getColumnCount() - 1 && !occupied.isEmpty()) {

            for (CellInfo info : occupied) {
                if (info.col == index) {
                    collist.add(info);
                }
            }
        }
        return collist;
    }

    public static CellInfo getComponentAt(int row, int col) {
        CellInfo cellInfo = null;

        for (CellInfo info : occupied) {
            if (info.row == row && info.col == col) {
                cellInfo = info;
            }
        }
        return cellInfo;
    }

    public static void swapRows(int oldIndex, int newIndex) {
        if (oldIndex < mesh.getRowCount() - 1 && oldIndex >= 0 && newIndex >= 0 && newIndex < mesh.getRowCount() - 1) {
            LinkedList<CellInfo> items = getRowItemsAt(oldIndex);
            LinkedList<CellInfo> nitems = getRowItemsAt(newIndex);
            for (CellInfo item : items) {
                item.row = newIndex;
                AdsUIProperty.IntProperty prop = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(item.widget, "row");
                prop.value = newIndex;
                AdsUIUtil.setUiProperty(item.widget, prop);
            }
            for (CellInfo item : nitems) {
                item.row = oldIndex;
                AdsUIProperty.IntProperty prop = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(item.widget, "row");
                prop.value = oldIndex;
                AdsUIUtil.setUiProperty(item.widget, prop);
            }
        }
    }

    public static void swapColumns(int oldIndex, int newIndex) {

        if (oldIndex < mesh.getColumnCount() - 1 && oldIndex >= 0 && newIndex >= 0 && newIndex < mesh.getColumnCount() - 1) {

            List<CellInfo> nitems = getColumnItemsAt(newIndex);
            List<CellInfo> items = getColumnItemsAt(oldIndex);

            for (CellInfo item : items) {

                item.col = newIndex;
                IntProperty prop = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(item.widget, "col");
                prop.value = newIndex;
                AdsUIUtil.setUiProperty(item.widget, prop);
            }

            for (CellInfo item : nitems) {
                item.col = oldIndex;
                IntProperty prop = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(item.widget, "col");
                prop.value = oldIndex;
                AdsUIUtil.setUiProperty(item.widget, prop);
            }
        }
    }

    public static void insertEmptyRow(int index) {

        if (index >= 0 && index < mesh.getRowCount() - 1) {
            int cc = mesh.getColumnCount();
            if (cc > 0) {
                Object[] arr = new Object[cc];

                List<Object> columns = new ArrayList<>(Arrays.asList(arr));
                mesh.rows.add(index, columns);
            }
        }
    }

    public static void insertEmptyColumn(int index) {

        if (index >= 0 && index < mesh.getColumnCount() - 1) {

            ArrayList<List<List<Object>>> rows = new ArrayList<>(Arrays.asList(mesh.rows));

            for (List<List<Object>> row : rows) {

                row.add(index, null);
            }
        }
    }

    public static void delRow(int index) {
        LinkedList<CellInfo> list = getRowItemsAt(index);

        if (index >= 0 && index < mesh.getRowCount() - 1 && !list.isEmpty()) {
            Iterator<CellInfo> iterator = list.iterator();
            while (iterator.hasNext()) {

                occupied.remove(list.getFirst());
                list.getFirst().widget.delete();
                list.remove();
            }
            mesh.removeRow(index);
        }
    }

    public static void delColumn(int index) {
        LinkedList<CellInfo> list = getColumnItemsAt(index);

        if (index >= 0 && index < mesh.getColumnCount() - 1 && !list.isEmpty()) {
            Iterator<CellInfo> iterator = list.iterator();
            while (iterator.hasNext()) {

                occupied.remove(list.getFirst());
                list.getFirst().widget.delete();
                list.remove();
            }

            mesh.removeColumn(index);
        }
    }

    public static int getGap() {
        return gap;
    }

    //set gap between cells in the table (gap = 3px by default)
    public static void setGap(int value) {
        if (value >= 0) {
            gap = value;
        }
    }

    private static int getWidgetColumn(AdsRwtWidgetDef child) {
        AdsUIProperty.IntProperty propCol = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(child, "col");
        int col = -1;
        if (propCol != null) {
            col = propCol.value;
            if (col < 0) {

                return -1;
            } else {
                return col;
            }
        }
        return col;
    }

    private static int getWidgetRow(AdsRwtWidgetDef child) {
        AdsUIProperty.IntProperty propRow = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(child, "row");
        int row = -1;
        if (propRow != null) {
            row = propRow.value;
            if (row < 0) {

                return -1;

            } else {
                return row;
            }
        }
        return row;
    }

    public static int getColSpan(AdsRwtWidgetDef child) {

        int cs = 1;
        if (child != null) {
            AdsUIProperty.IntProperty propCol = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(child, "colSpan");

            if (propCol != null) {
                cs = propCol.value;
                if (cs < 1) {
                    cs = 1;
                }
            }
        }
        return cs;
    }

    public static int getRowSpan(AdsRwtWidgetDef child) {

        int rs = 1;
        if (child != null) {
            AdsUIProperty.IntProperty propCol = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(child, "rowSpan");

            if (propCol != null) {
                rs = propCol.value;
                if (rs < 1) {
                    rs = 1;
                }
            }
        }
        return rs;
    }

    public static CellRect[][] computeRectangles(AdsRwtWidgetDef rw, Rectangle r) {

        TableMesh mesh = fillTableMesh(rw);
        //System.out.println("\ncompute rects 1 mesh="+mesh.);
        if (mesh.isEmpty()) {

            CellRect rect = new CellRect(r);
            rect.height = 20;
            rect.width = 100;
            rect.cellCol = 0;
            rect.cellRow = 0;
            return new CellRect[][]{{rect}};
        }

        int widths[] = new int[mesh.getColumnCount()];
        CellRect[][] rectangles = new CellRect[mesh.rows.size() + 1][];

        for (int i = 0; i < widths.length; i++) {

            int w = mesh.getColumnWidth(i);
            widths[i] = w;

        }

        int starty = gap + r.y;
        int row;
        Object obj;
        for (row = 0; row < mesh.rows.size(); row++) {
            int startx = r.x + gap;
            int defaultHeight = Math.max(20, mesh.getRowHeight(row));
            int defaultWidth = 100;
            rectangles[row] = new CellRect[mesh.rows.get(row).size()];
            for (int col = 0; col < mesh.rows.get(row).size(); col++) {
                obj = mesh.rows.get(row).get(col);
                rectangles[row][col] = new CellRect(startx, starty, defaultWidth, defaultHeight);
                rectangles[row][col].cellCol = col;
                rectangles[row][col].cellRow = row;

                if (obj != null) {
                    AdsRwtWidgetDef widget = (AdsRwtWidgetDef) obj;
                    RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
                    if (defaultHeight < geometry.height) {
                        defaultHeight = geometry.height;
                    }
                    rectangles[row][col].widget = widget;
                }
                startx += mesh.getColumnWidth(col) + gap;
            }
            for (int i = 0; i < rectangles[row].length; i++) {
                rectangles[row][i].height = defaultHeight + gap;
                rectangles[row][i].width = defaultWidth + gap;
            }
            starty += defaultHeight + gap;
        }
        rectangles[row] = new CellRect[mesh.getColumnCount()];       
        for (int i = 0; i < mesh.getColumnCount(); i++) {
                CellRect newCell = new CellRect(0, 0, 100, 20);
                rectangles[row][i] = newCell;
                placeInMesh(r, rectangles, row, i);
            }
        return rectangles;
    }

    public static List<CellInfo> computeCellInfo(AdsRwtWidgetDef rw, Rectangle r) {
        CellRect[][] cellrect = computeRectangles(rw, r);

        if (mesh.isEmpty()) {
            return new LinkedList();
        }

        List<CellInfo> columnInfos = new LinkedList<CellInfo>();
        int[] cwidths = new int[mesh.getColumnCount()];
        for (int i = 0; i < cwidths.length; i++) {
            int cw = mesh.getColumnWidth(i);
            cwidths[i] = cw;
        }
        int row;
        Object obj;
        int gh = 0;
        RectProperty geocurr = null;
        CellInfo current = null;

        for (row = 0; row < mesh.rows.size(); row++) {
            Object prev = null;
            int startx = r.x + gap;

            for (int col = 0; col < mesh.rows.get(row).size(); col++) {

                CellRect cell = cellrect[row][col];

                obj = mesh.rows.get(row).get(col);
                int starty = cell.y;
                if (obj != null) {
                    current = new CellInfo();
                    current.widget = (AdsRwtWidgetDef) obj;
                    geocurr = (RectProperty) AdsUIUtil.getUiProperty(current.widget, "geometry");
                    current.row = row;
                    current.col = col;
                    gh = Math.max(mesh.getRowHeight(row), geocurr.height);
                    Rectangle newrect = new Rectangle(startx, starty, cwidths[col], gh);
                    current.bounds = newrect;
                    current.rowspan = getRowSpan(current.widget);
                    current.colspan = getColSpan(current.widget);
                    columnInfos.add(current);
                    prev = obj;
                } else if (obj == null) {
                    prev = obj;
                }
                startx += cwidths[col] + gap;
            }
        }
        return columnInfos;
    }

    private static void setSpans(CellInfo current, TableMesh mesh) {

        int colspan = current.colspan;
        int rowspan = current.rowspan;
        int width = 0;
        int x = 0;
        int height = 0;
        int y = 0;

        for (int r = 0; r < mesh.getRowCount() - 1; r++) {
            for (int c = 0; c < mesh.getColumnCount() - 1; c++) {

                AdsRwtWidgetDef item = (AdsRwtWidgetDef) mesh.rows.get(r).get(c);

                if (item != null && item.equals(current.widget)) {

                    int w = mesh.getColumnWidth(c);
                    int h = mesh.getRowHeight(r);

                    width += w;
                    x += mesh.getColumnWidth(c) + gap;

                    height += h;
                    y += mesh.getRowHeight(r) + gap;
                }
            }
        }

        if (colspan > 1) {
            current.bounds.x = current.bounds.x - x + current.bounds.width;
            current.bounds.width = width + gap * (colspan - 1);
        }

        if (rowspan > 1) {
            current.bounds.y = current.bounds.y - y + current.bounds.height + gap;
            current.bounds.height = height + gap * (rowspan - 1);

        }
    }

    private static void placeInMesh(Rectangle r, CellRect[][] rects, int row, int col) {
        rects[row][col].cellCol = col;
        rects[row][col].cellRow = row;

        if (row == 0 && col == 0) {
            rects[row][col].x = r.x + 3;
            rects[row][col].y = r.y + 3;
        } else if (row > 0 && col > 0) {

            rects[row][col].width = rects[row - 1][col].width;
            rects[row][col].height = rects[row][col - 1].height;
            rects[row][col].x = rects[row][col - 1].x + rects[row][col - 1].width + r.x + gap;
            rects[row][col].y = rects[row - 1][col].y + rects[row - 1][col].height + r.y + gap;

        } else if (row == 0 && col > 0) {
            rects[row][col].height = rects[row][col - 1].height;
            rects[row][col].x = rects[row][col - 1].x + rects[row][col - 1].width + gap + r.x;
            //rects[row][col].y = r.y;//отступ при добавлении 

        } else if (row > 0 && col == 0) {
            rects[row][col].width = rects[row - 1][col].width;
            rects[row][col].x = r.x;
            rects[row][col].y = rects[row - 1][col].y + rects[row - 1][col].height + gap + r.y;
        }
    }

    @Override
    public void justify(Widget widget) {

        synchronized (LayoutProcessor.LOCK) {
            BaseWidget w = (BaseWidget) widget;
            occupied = (LinkedList<CellInfo>) computeCellInfo((AdsRwtWidgetDef) w.getNode(), w.getInnerGeometry());
            GraphSceneImpl scene = (GraphSceneImpl) w.getScene();
            for (CellInfo info : occupied) {
                BaseWidget rcw = (BaseWidget) scene.findWidget(info.widget);

                if (info.colspan > 1 || info.rowspan > 1) {
                    setSpans(info, mesh);
                }
                rcw.setGeometry(info.bounds);
                rcw.revalidate();
            }
            scene.repaint();
        }
    }

    @Override
    public void layout(Widget widget) {
        justify(widget);
    }

    @Override
    public boolean requiresJustification(Widget widget) {
        return true;
    }
}