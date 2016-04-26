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

public class TableLayout extends UIObject {

    private static int DEFAULT_SPACE = 3;

    private static class VerticalDevider extends UIObject {

        public VerticalDevider(final int height) {
            super(new Html("div"));
            setMinimumHeight(height);
            getHtml().setCss("max-height", height + "px");
        }
    }

    private static class HorizontalDevider extends UIObject {

        public HorizontalDevider(final int width) {
            super(new Html("div"));
            setMinimumWidth(width);
            getHtml().setCss("max-width", width + "px");
            getHtml().setCss("width", width + "px");
        }
    }

    public class Row extends UIObject {

        private boolean expandCells;

        public class Cell extends AbstractContainer {

            private boolean expandContent;

            public Cell(int index) {
                super(new Html("td"));
                this.html.setCss("padding", "0px");
                this.html.setCss("margin", "0px");
                this.html.removeClass("rwt-ui-background");
                Row.this.html.add(index, html);
                setParent(Row.this);
            }

            public void setAutoExpandContent(final boolean autoExpand) {
                expandContent = autoExpand;
                if (expandContent) {
                    getHtml().setAttr("expandContent", "true");
                } else {
                    getHtml().setAttr("expandContent", null);
                }
            }

            public boolean getAutoExpandContent() {
                return expandContent;
            }
        }

        private final List<Cell> cells = new LinkedList<>();

        public Row(int index) {
            super(new Html("tr"));
            body.add(index, html);
            setParent(TableLayout.this);
        }

        public Cell addCell() {
            Cell cell = new Cell(-1);
            cells.add(cell);
            return cell;
        }

        public Cell addCell(int index) {
            if (index < 0 || index >= cells.size()) {
                return addCell();
            } else {
                Cell cell = new Cell(index);
                cells.add(index, cell);
                return cell;
            }
        }

        public Cell addSpace(int width) {
            final Cell cell = addCell();
            cell.add(new HorizontalDevider(width));
            return cell;
        }

        public Cell addSpace() {
            return addSpace(DEFAULT_SPACE);
        }

        public void remove(Cell cell) {
            if (cells.remove(cell)) {
                this.html.remove(cell.getHtml());
            }
        }

        public List<Cell> getCells() {
            return new ArrayList<Cell>(cells);
        }

        public Cell getCell(final int col) {
            return cells.get(col);
        }

        public int getCellsCount() {
            return cells.size();
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            for (Cell cell : cells) {
                if (cell != null) {
                    result = cell.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            for (Cell cell : new ArrayList<>(cells)) {
                if (cell != null) {
                    cell.visit(visitor);
                }
            }
        }
    }
    private List<Row> rows = new LinkedList<Row>();
    private Html body;

    public TableLayout() {
        super(new Html("table"));
        this.body = new Html("tbody");
        this.html.add(body);
        this.html.setAttr("cellspacing", "0");
        this.html.setAttr("cellpadding", "0");
        this.html.layout("$RWT.table_layout._layout");
        this.setHSizePolicy(SizePolicy.EXPAND);
    }

    public Row addRow() {
        Row row = new Row(-1);
        rows.add(row);
        return row;
    }

    public Row addVerticalSpace(int height) {
        final Row row = addRow();
        row.addCell().add(new VerticalDevider(height));
        return row;
    }

    public Row addVerticalSpace() {
        return addVerticalSpace(DEFAULT_SPACE);
    }

    public Row addRow(int index) {
        Row row = new Row(index);
        if (index >= 0 && index < rows.size()) {
            rows.add(index, row);
        } else {
            rows.add(row);
        }
        return row;
    }

    public List<Row> getRows() {
        return new ArrayList<Row>(rows);
    }

    public Row getRow(final int index) {
        return rows.get(index);
    }

    public Row.Cell getCell(final int row, final int col) {
        return getRow(row).getCell(col);
    }

    public void clearRows() {
        for (Row row : getRows()) {
            removeRow(row);
        }

    }

    public void removeRow(Row row) {
        if (rows.remove(row)) {
            body.remove(row.getHtml());
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        for (Row row : rows) {
            result = row.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        for (Row row : new ArrayList<>(rows)) {
            row.visit(visitor);
        }
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/table-layout.js"};
    }
}
