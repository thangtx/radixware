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

package org.radixware.kernel.common.defs.ads.ui;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.schemas.ui.Item;
import org.radixware.schemas.ui.Widget;


public class AdsItemWidgetDef extends AdsWidgetDef {

    public static class Row extends RadixObject {

        UiProperties properties = new UiProperties(this);

        public Row() {
        }

        private Row(org.radixware.schemas.ui.Row xRow) {
            properties.loadFrom(xRow.getPropertyList());
        }

        public UiProperties getProperties() {
            return properties;
        }

        public Row duplicate() {
            org.radixware.schemas.ui.Row xRow = org.radixware.schemas.ui.Row.Factory.newInstance();
            appendTo(xRow); return new Row(xRow);
        }

        private void appendTo(org.radixware.schemas.ui.Row xRow) {
            xRow.setPropertyArray(properties.toXml());
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            properties.visit(visitor, provider);
        }

    }

    public static class Column extends RadixObject {

        UiProperties properties = new UiProperties(this);

        public Column() {
        }

        private Column(org.radixware.schemas.ui.Column xColumn) {
            properties.loadFrom(xColumn.getPropertyList());
        }

        public UiProperties getProperties() {
            return properties;
        }

        public Column duplicate() {
            org.radixware.schemas.ui.Column xColumn = org.radixware.schemas.ui.Column.Factory.newInstance();
            appendTo(xColumn); return new Column(xColumn);
        }

        private void appendTo(org.radixware.schemas.ui.Column xColumn) {
            xColumn.setPropertyArray(properties.toXml());
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            properties.visit(visitor, provider);
        }
    }

    public static class Columns extends RadixObjects<Column> {

        public Columns(RadixObject owner) {
            super(owner);
        }

        void loadFrom(Widget w) {
            for (org.radixware.schemas.ui.Column column : w.getColumnList()) {
                add(new Column(column));
            }
        }

        public void appendTo(Widget w) {
            for (Column r : this) {
                r.appendTo(w.addNewColumn());
            }
        }
    }

    public static class Rows extends RadixObjects<Row> {

        public Rows(RadixObject owner) {
            super(owner);
        }

        void loadFrom(Widget w) {
            for (org.radixware.schemas.ui.Row row : w.getRowList()) {
                add(new Row(row));
            }
        }

        public void appendTo(Widget w) {
            for (Row r : this) {
                r.appendTo(w.addNewRow());
            }
        }
    }

    public static class WidgetItem extends RadixObject {

        public int column, row;
        final UiProperties properties = new UiProperties(this);
        final Items items = new Items(this);

        public WidgetItem(int column, int row) {
            this.column = column;
            this.row = row;
        }
        
        public WidgetItem() {
        }
        
        public WidgetItem(Item xItem) {
            column = xItem.getColumn();
            row = xItem.getRow();
            properties.loadFrom(xItem.getPropertyList());
            items.loadFrom(xItem.getItemList());
        }

        public UiProperties getProperties() {
            return properties;
        }

        public Items getItems() {
            return items;
        }

        public RadixObject getOwner() {
            return getContainer().getContainer();
        }

        public WidgetItem getOwnerItem() {
            RadixObject owner = getOwner();
            return owner instanceof WidgetItem ? (WidgetItem)owner : null;
        }

        public AdsItemWidgetDef getOwnerWidget() {
            RadixObject owner = getOwner();
            return owner instanceof AdsItemWidgetDef ? (AdsItemWidgetDef)owner : null;
        }

        public WidgetItem duplicate() {
            org.radixware.schemas.ui.Item xItem = org.radixware.schemas.ui.Item.Factory.newInstance();
            appendTo(xItem); return new WidgetItem(xItem);
        }

        private void appendTo(Item xItem) {
            xItem.setColumn(column);
            xItem.setRow(row);
            xItem.setPropertyArray(properties.toXml());
            xItem.setItemArray(items.toXml());
        }
        
        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            properties.visit(visitor, provider);
            items.visit(visitor, provider);
        }
    }

    public static class Items extends RadixObjects<WidgetItem> {

        public Items(RadixObject owner) {
            super(owner);
        }

        void loadFrom(List<Item> list) {
            for (Item item : list) {
                add(new WidgetItem(item));
            }
        }

        Item[] toXml() {
            ArrayList<Item> itemsList = new ArrayList<Item>();
            for (WidgetItem p : this) {
                Item pp = Item.Factory.newInstance();
                p.appendTo(pp);
                itemsList.add(pp);
            }
            Item[] arr = new Item[itemsList.size()];
            itemsList.toArray(arr);

            return arr;
        }
    }

    private final Items items = new Items(this);
    private final Columns columns = new Columns(this);
    private final Rows rows = new Rows(this);

    public AdsItemWidgetDef(String className) {
        super(className);
    }

    public AdsItemWidgetDef(Widget xWidget) {
        this(null, xWidget);
    }

    public AdsItemWidgetDef(RadixObject container, Widget xWidget) {
        super(container, xWidget);
        items.loadFrom(xWidget.getItemList());
        rows.loadFrom(xWidget);
        columns.loadFrom(xWidget);
    }

    public Items getItems() {
        return items;
    }

    public Columns getColumns() {
        return columns;
    }

    public Rows getRows() {
        return rows;
    }

    public WidgetItem[][] getItemsAsArray() {
        int colCount = 0, rowCount = 0;
        for (WidgetItem item : items) {
            colCount = Math.max(item.column + 1, colCount);
            rowCount = Math.max(item.row + 1, rowCount);
        }
        WidgetItem its[][] = new WidgetItem[rowCount][colCount];
        for (WidgetItem item : items)
            its[item.row][item.column] = item;
        return its;
    }

    @Override
    public void appendTo(Widget xWidget) {
        super.appendTo(xWidget);
        xWidget.setItemArray(items.toXml());
        columns.appendTo(xWidget);
        rows.appendTo(xWidget);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        items.visit(visitor, provider);
        columns.visit(visitor, provider);
        rows.visit(visitor, provider);        
    }
}
