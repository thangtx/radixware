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
import org.radixware.kernel.common.html.CssConstants;
import org.radixware.kernel.common.html.CssConstants.VerticalAlign;
import org.radixware.kernel.common.html.Div;



public class Panel extends UIObject {

    public class Table {

        public class Row {

            public class Cell {

                private boolean hfit = false;
                private boolean vfit = false;
                private Html html = new Html("td");
                private int rowSpan;
                private int colSpan;
                private CssConstants.VerticalAlign vAlign = null;
                private UIObject obj;

                public Cell() {
                    //html.setCss("vertical-align", "middle");
                }

                public int getColSpan() {
                    return colSpan;
                }

                public void setColSpan(int colSpan) {
                    this.colSpan = colSpan;
                    if (this.colSpan > 1) {
                        html.setAttr("colspan", colSpan);
                    } else {
                        html.setAttr("colspan", null);
                    }
                }

                public int getRowSpan() {
                    return rowSpan;
                }

                public void setRowSpan(int rowSpan) {
                    this.rowSpan = rowSpan;
                    if (this.rowSpan > 1) {
                        html.setAttr("rowspan", rowSpan);
                    } else {
                        html.setAttr("rowspan", null);
                    }
                }

                public void setComponent(UIObject obj) {
                    if (this.obj != null && this.obj != obj) {
                        html.remove(this.obj.html);
                        this.obj.setParent(null);
                    }
                    if (this.obj != obj) {
                        this.obj = obj;
                        html.add(this.obj.html);
                        obj.setParent(Panel.this);
                    }
                }

                public UIObject getComponent() {
                    return obj;
                }

                public void setHfit(boolean hfit) {
                    this.hfit = hfit;
                    if (hfit) {
                        html.setAttr("hfit", true);
                    } else {
                        html.setAttr("hfit", null);
                    }
                }

                public void setVfit(boolean vfit) {
                    this.vfit = vfit;
                    if (vfit) {
                        html.setAttr("vfit", true);
                    } else {
                        html.setAttr("vfit", null);
                    }
                }

                public VerticalAlign getVAlign() {
                    return vAlign;
                }

                public void setVAlign(VerticalAlign vAlign) {
                    this.vAlign = vAlign;
                    if (vAlign != null && vAlign != VerticalAlign.BASELINE) {
                        html.setCss("vertical-align", vAlign.value);
                    } else {
                        html.setCss("vertical-align", null);
                    }
                }
            }
            private boolean expand = false;
            private Html html = new Html("tr");
            private List<Cell> cells = new LinkedList<Cell>();

            public Cell addCell() {
                Cell cell = new Cell();
                cells.add(cell);
                html.add(cell.html);
                return cell;
            }

            public void setExpand(boolean expand) {
                this.expand = expand;
                if (expand) {
                    html.setAttr("expand", true);
                } else {
                    html.setAttr("expand", null);
                }
            }
        }
        private Html table = new Html("table");
        private Html tbody = new Html("tbody");
        private List<Row> rows = new LinkedList<Row>();

        public Table() {
            table.add(tbody);
        }

        public Table.Row addRow() {
            Table.Row row = new Table.Row();
            rows.add(row);
            tbody.add(row.html);
            return row;
        }
    }
    private Table table = new Table();

    public Panel() {
        super(new Div());
        this.html.add(table.table);
        html.layout("$RWT.panel.layout");
    }

    public Table getTable() {
        return table;
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        } else {
            for (Table.Row r : table.rows) {
                for (Table.Row.Cell c : r.cells) {
                    if (c.obj != null) {
                        result = c.obj.findObjectByHtmlId(id);
                    }
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        for (Table.Row r : table.rows) {
            for (Table.Row.Cell c : r.cells) {
                if (c.obj != null) {
                    c.obj.visit(visitor);
                }
            }
        }
    }
}
