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

package org.radixware.kernel.common.html;


public class Table extends Html {

    public static class Body extends Html {

        public Body() {
            super("tbody");
        }

        public Row addRow() {
            Row row = new Row();
            add(row);
            return row;
        }

        public Row insertRow(int index) {
            Row row = new Row();
            add(index, row);
            return row;
        }

        public int rowCount() {
            return children().size();
        }

        public boolean isEmpty() {
            return children().isEmpty();
        }

        public Row getRow(int index) {
            return (Row) children().get(index);
        }
    }

    public static class Row extends Html {

        public Row() {
            super("tr");
        }

        public DataCell addCell() {
            return addCell(-1);
        }

        public DataCell addCell(int index) {
            DataCell cell = new DataCell();
            add(index, cell);
            return cell;
        }

        public int cellCount() {
            return children().size();
        }

        public DataCell getCell(int index) {
            return (DataCell) children().get(index);
        }
    }

    public static class DataCell extends Html {

        public DataCell() {
            super("td");
        }
    }
    private Body body = null;

    public Table() {
        super("table");
    }

    public Row addRow() {
        if (body == null) {
            body = new Body();
            add(body);
        }
        return body.addRow();
    }

    public Row insertRow(int index) {//?
        if (body == null) {
            body = new Body();
            add(body);
        }
        return body.insertRow(index);
    }

    public Row addRow(String... cellTexts) {
        Row row = addRow();
        if (cellTexts != null) {
            for (String s : cellTexts) {
                row.addCell().setInnerText(s);
            }
        }
        return row;
    }

    public Row insertRow(int index, String... cellTexts) {
        Row row = insertRow(index);
        if (cellTexts != null) {
            for (String s : cellTexts) {
                row.addCell().setInnerText(s);
            }
        }
        return row;
    }

    public Row getRow(int index) {
        if (body == null) {
            throw new IndexOutOfBoundsException();
        }
        return body.getRow(index);
    }

    public int rowCount() {
        return body == null ? 0 : body.rowCount();
    }

    public boolean isEmpty() {
        return body == null ? true : body.isEmpty();
    }
}
