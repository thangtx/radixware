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
import java.util.LinkedList;
import java.util.List;


class TableContainer extends UIObject {

    private static class Body extends AbstractContainer {

        public Body() {
            super(new Html("tbody"));
            html.removeClass("rwt-ui-background");
        }
    }

    static class Row extends AbstractContainer {

        public Row() {
            super(new Html("tr"));
            html.removeClass("rwt-ui-background");
        }

        public Cell addCell() {
            Cell cell = new Cell();
            add(cell);
            return cell;
        }

        public void removeCell(Cell cell) {
            remove(cell);
        }

        public List<Cell> getCells() {
            List<Cell> cells = new LinkedList<Cell>();
            for (UIObject obj : getChildren()) {
                if (obj instanceof Cell) {
                    cells.add((Cell) obj);
                }
            }
            return cells;
        }
    }

    static class Cell extends AbstractContainer {

        public Cell() {
            super(new Html("td"));
            html.removeClass("rwt-ui-background");
        }

        Row getRow() {
            return (Row) getParent();
        }
    }
    private final Body body = new Body();

    public TableContainer() {
        super(new Html("table"));
        this.html.add(body.html);
    }

    Row addRow() {
        Row row = new Row();
        body.add(row);
        return row;
    }

    void removeRow(Row row) {
        body.remove(row);
    }

    void clearRows() {
        body.clear();
    }

    public List<Row> getRows() {
        List<Row> rows = new LinkedList<Row>();
        for (UIObject child : body.getChildren()) {
            if (child instanceof Row) {
                rows.add((Row) child);
            }
        }
        return rows;
    }

    Cell findCellByObject(UIObject object) {
        for (UIObject child : body.getChildren()) {
            if (child instanceof Row) {
                for (UIObject cell : ((Row) child).getChildren()) {
                    if (cell instanceof Cell) {
                        if (((Cell) cell).getChildren().contains(object)) {
                            return (Cell) cell;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        return body.findObjectByHtmlId(id);
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        body.visit(visitor);
    }
}
