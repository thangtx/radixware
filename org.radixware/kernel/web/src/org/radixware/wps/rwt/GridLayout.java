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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.html.Div;



public class GridLayout extends UIObject {

    public static class Cell extends AbstractContainer {

        private int w, h;
        private Html td = new Html("td");

        public Cell() {
            super(new Div());
            this.td.add(html);
            html.layout("$RWT.grid_layout.cell.layout");
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            if (td.getId().equals(id)) {
                return this;
            } else {

                UIObject obj = super.findObjectByHtmlId(id);
                if (obj != null) {
                    return obj;
                } else {
                    return null;
                }
            }
        }

        public boolean isFitWidth() {
            return html.getAttr("hfit") != null;
        }

        public void setFitWidth(boolean fit) {
            html.setAttr("hfit", fit ? "true" : null);
        }

        public boolean isFitHeight() {
            return html.getAttr("vfit") != null;
        }

        public void setFitHeight(boolean fit) {
            html.setAttr("vfit", fit ? "true" : null);
        }
    }
    private Html table = new Html("table");
    private Html body = new Html("tbody");
    private List<Row> rows = new LinkedList<Row>();

    public static class Row extends UIObject {

        private List<Cell> cells = new LinkedList<Cell>();

        public Row() {
            super(new Html("tr"));
        }

        public void add(Cell cell) {
            cells.add(cell);
            html.add(cell.td);
            cell.setParent(this);
        }

        public void remove(Cell cell) {
            if (cells.contains(cell)) {
                html.remove(cell.td);
                cell.setParent(null);
                cells.remove(cell);
            }
        }

        public List<Cell> getRows() {
            return new ArrayList<Cell>(cells);
        }

        public void clear() {
            for (Cell c : cells) {
                html.remove(c.td);
                c.setParent(null);
            }
            cells.clear();
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject obj = super.findObjectByHtmlId(id);
            if (obj != null) {
                return obj;
            } else {
                for (Cell cell : cells) {
                    obj = cell.findObjectByHtmlId(id);
                    if (obj != null) {
                        return obj;
                    }
                }
                return null;
            }
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            for (Cell c : cells) {
                c.visit(visitor);
            }
        }
    }

    public GridLayout() {
        super(new Div());
        table.setCss("width", "100%");
        html.setCss("overflow", "auto");
        html.add(table);
        table.add(body);
    }

    public void add(Row row) {
        add(-1, row);
    }

    public void add(int index, Row row) {
        if (index < 0 || index > rows.size()) {
            rows.add(row);
            body.add(row.html);
        } else {
            rows.add(index, row);
            body.add(index, row.html);
        }

        row.setParent(this);
    }

    public void remove(Row row) {
        if (rows.contains(row)) {
            body.remove(row.html);
            rows.remove(row);
            row.setParent(null);
        }
    }

    public List<Row> getRows() {
        return new ArrayList<Row>(rows);
    }

    public void clear() {
        for (Row r : rows) {
            body.remove(r.html);
            r.setParent(null);
        }
        rows.clear();
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        if (table.getId().equals(id)) {
            return this;
        } else if (body.getId().equals(id)) {
            return this;
        } else {
            for (Row r : rows) {
                obj = r.findObjectByHtmlId(id);
                if (obj != null) {
                    return obj;
                }
            }
            return null;
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        for (Row r : rows) {
            r.visit(visitor);
        }
    }
}