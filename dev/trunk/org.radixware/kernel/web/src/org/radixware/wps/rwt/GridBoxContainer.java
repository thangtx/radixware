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

import java.util.Arrays;
import java.util.HashMap;
import org.radixware.kernel.common.html.Html;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Table;


public class GridBoxContainer extends UIObject {

    public enum Overflow {

        AUTO,
        INHERIT,
        SCROLL,
        HIDDEN,
        VISIBLE
    }
    
    public enum FixedColMode{
        PIXELS,
        PERCENT
    }
        
    private static class ObjectInfo {

        private final UIObject obj;        
        private final int row;
        private final int col;
        

        public ObjectInfo(final UIObject obj, final int row, final int col) {
            this.obj = obj;
            this.row = row;
            this.col = col;
        }
    }
    
    private class TableImpl {
        
        private final Map<Integer, String> colWidthMap = new HashMap<>();
        private Integer expandedCol = null;
        private Integer expandedRow = null;
        private final TableInner table = new TableInner();
        private boolean isAutoAdjustColWidth = true;
        
        private class TableInner extends Table {

            @Override
            public Row getRow(int index) {
                return (TableInner.Row)super.getRow(index);
            }
            
            @Override
            protected Body createBody() {
                return new Body();
            }
            
            private class Body extends Table.Body {

                @Override
                public Row getRow(int index) {
                    return (Row) children().get(index);
                }

                @Override
                public Row insertRow(int index) {
                    Row row = new Row();
                    add(index, row);
                    return row;
                }

                @Override
                public Row addRow() {
                    Row row = new Row();
                    add(row);
                    return row;
                }
                
            }
            
            private class Row extends Table.Row {

                private boolean isAccessoryRow = false;
                
                @Override
                public DataCell getCell(int index) {
                    return (DataCell) children().get(index);
                }

                @Override
                public int cellCount() {
                        return isAccessoryRow ? super.cellCount() - 1 : super.cellCount(); 
                }

                @Override
                public DataCell addCell(int index) {
                    DataCell cell = new DataCell();
                    if ((index < 0 || index >= children().size()) && isAccessoryRow) {
                        add(cellCount()-1, cell);
                    } else {
                        add(index, cell);
                    }
                    return cell;
                }

                @Override
                public DataCell addCell(String content) {
                    final DataCell cell = addCell();
                    cell.setInnerText(content);
                    return cell;
                }

                @Override
                public DataCell addCell() {
                    return addCell(-1);
                }
            }
            
            private class DataCell extends Table.DataCell {
                private boolean isSpanned = false;
                private DataCell mainCell = null;
                private String maxWidth;
                private UIObject obj;
                private Integer getColIndex() {
                    if (this.getParent() != null) {
                        return this.getParent().children().indexOf(this);
                    } else {
                        return null;
                    }
                }
                
                private Integer getRowIndex() {
                    if (this.getParent() != null && this.getParent().getParent() != null) {
                        return this.getParent().getParent().indexOfChild(this.getParent());
                    } else {
                        return null;
                    }
                }

                private void setVisible(boolean isVisible) {
                    if (isVisible) {
                        this.setCss("display", null);
                        this.isSpanned = false;
                        this.mainCell = null;
                    } else {
                        this.setCss("display", "none");
                        this.isSpanned = true;
                    }
                }
                
                public boolean isEmpty() {
                    return this.getChildAt(0).children().isEmpty();
                }
            }
            
        }
        
        public TableImpl() {
            table.addClass("rwt-gridbox-container");
            table.setAttr("cellpadding", "0");
        }
        
        private TableInner.Row addRow() {
            return (TableInner.Row)table.addRow();
        }

        private int rowCount() {
            return table.rowCount();
        }

        private TableInner.Row getRow(int index) {
            return table.getRow(index);
        }
        
        private TableInner.Row insertRow(int index) {
            return (TableInner.Row)table.insertRow(index);
        }
        
        private void setWidth(int w) {
            if (w > 0) {
                GridBoxContainer.this.html.setCss("width", String.valueOf(w) + "px");
                GridBoxContainer.this.html.setCss("max-width", String.valueOf(w) + "px");
            }
            table.setCss("width", w+"px");
            table.setAttr("wresize", String.valueOf(w));
        }
        
        private void setHeight(int h) {
            if (h > 0) {
                GridBoxContainer.this.html.setCss("height", String.valueOf(h) + "px");
                GridBoxContainer.this.html.setCss("max-height", String.valueOf(h) + "px");
            }
            table.setCss("height", h+"px");
            table.setAttr("hresize", String.valueOf(h));
        }

        private void setMinWidth(int mw) {
            table.setCss("min-width", mw);
            table.setCss("display", "table-cell");
        }

        private void setMaxWidth(int mw) {
            table.setCss("max-width", mw + "px");
            table.setCss("width", null);
            table.setCss("display", "table-cell");
        }
        
        private void setVSizePolicy(final SizePolicy vSizePolicy) {
            GridBoxContainer.super.setVSizePolicy(vSizePolicy);
            switch (vSizePolicy) {
                case EXPAND:
                    table.setCss(Html.CSSRule.HEIGHT, "100%");
                    break;
                case MINIMUM_EXPAND:
                    table.setCss(Html.CSSRule.HEIGHT, null);
                    break;
            }
        }
        
        private void setHSizePolicy(final SizePolicy hSizePolicy) {
           GridBoxContainer.super.setHSizePolicy(hSizePolicy);
           switch (hSizePolicy) {
                case EXPAND:
                    table.setCss(Html.CSSRule.WIDTH, "100%");
                    break;
                case MINIMUM_EXPAND:
                    table.setCss(Html.CSSRule.WIDTH, null);
                    break;
            }
        }    
        
        private Html getHtml() {
            return table;
        }
        
        private void setFixedColWidth(int col, int val, FixedColMode fixedColMode) {
            if (!table.isEmpty() && col < table.getRow(0).cellCount()) {  
                if (!colWidthMap.containsKey(col)) {
                    for (int i = 0; i < table.rowCount(); i++) {
                        TableInner.DataCell curCell = table.getRow(i).getCell(col);
                        if (!curCell.getChildAt(0).children().isEmpty()) {
                            curCell.maxWidth = curCell.getChildAt(0).getChildAt(0).getCss("max-width");
                        }
                    }
                }
                if (expandedCol != null && expandedCol == col) {
                    expandedCol = null;
                }
                String value = fixedColMode.equals(FixedColMode.PIXELS) ? val + "px" : val + "%";
                colWidthMap.put(col, value);
                updateColWidthAttr();
                adjustColWidth();
            } else {
                throw new IndexOutOfBoundsException();
            }
        }
        
        private void adjustColWidth() {
            if (isAutoAdjustColWidth && expandedCol == null) {
                for(int i = 0; i < table.rowCount(); i++) {
                    TableImpl.TableInner.Row currRow = table.getRow(i);
                    if(!currRow.isAccessoryRow && !currRow.children().isEmpty()) {
                        TableImpl.TableInner.DataCell cell = addEmptyCell(currRow, currRow.cellCount());
                        cell.setCss("visibility", "hidden");
                        cell.setCss("width", "100%");
                        currRow.isAccessoryRow = true;
                    }
                }
            } else {
                for(int i = 0; i < table.rowCount(); i++) {
                    TableImpl.TableInner.Row currRow = table.getRow(i);
                    if(currRow.isAccessoryRow) {
                        currRow.remove(currRow.getCell(currRow.cellCount()));
                        currRow.isAccessoryRow = false;
                    }
                }
            }
        }
        
        @SuppressWarnings("unchecked")
        private void updateColWidthAttr() {
            JSONObject json = new JSONObject();
            json.putAll(colWidthMap);
            table.setAttr("fixedcolls", json.toString());
            if (expandedCol != null) { 
                table.setAttr("expandedcol", expandedCol);
            } else {
                table.setAttr("expandedcol", null);
            }
        }
        
        private void updateAfterInsertCols(int index, int num) {
            Integer[] keyArr = colWidthMap.keySet().toArray(new Integer[colWidthMap.size()]);
            Arrays.sort(keyArr);
            for (int i = keyArr.length - 1; i >= 0; i--) {
                Integer col = keyArr[i];
                if (col > index) {
                    String val = colWidthMap.get(col);
                    colWidthMap.remove(col);
                    colWidthMap.put(col + num, val);
                }
            }
            if (expandedCol != null && expandedCol > index) {
                expandedCol += num;
            }
        }
        
        private void updateAfterRemoveCol(int index) {
             
            Integer[] keyArr = colWidthMap.keySet().toArray(new Integer[colWidthMap.size()]);
            Arrays.sort(keyArr);
            for (int i = 0; i < keyArr.length; i++) {
                Integer col = keyArr[i];
                if (col > index) {
                    String val = colWidthMap.get(col);
                    colWidthMap.remove(col);
                    colWidthMap.put(col - 1, val);
                } else if (col == index) {
                    colWidthMap.remove(col);
                }
            }
            if (expandedCol != null) {
                if (expandedCol == index) {
                    expandedCol = null;
                } else if (expandedCol > index) {
                    expandedCol += 1;
                }
            }
        }
        
        private void setAutoAdjustColWidth(boolean isAutoAdjustColWidth) {
            this.isAutoAdjustColWidth = isAutoAdjustColWidth;
        }
        
        private boolean isEmpty() {
            return table.isEmpty();
        }   
    }
    
    private final TableImpl table = new TableImpl();    
    private final List<UIObject> objects = new LinkedList<>();

    public GridBoxContainer() {
        this(0,0);
    }

    public GridBoxContainer(int row, int col) {
        super(new Div());
        if (row>0 && col>0){
            createTable(row, col);
        }
        this.html.add(table.getHtml());
        this.html.setCss("width", "auto");
        this.html.setCss("height", "auto");
        this.html.layout("$RWT.gridBoxContainer.layout");
    }

    public GridBoxContainer getGridBoxContainer() {
        return this;
    }

    private TableImpl createTable(int row, int col) {
        for (int i = 0; i < row; i++) {
            TableImpl.TableInner.Row r = this.table.addRow();
            for (int j = 0; j < col; j++) {
                addEmptyCell(r);
            }
        }
        return table;
    }
    
    private TableImpl.TableInner.DataCell addEmptyCell(final TableImpl.TableInner.Row row) {
        return addEmptyCell(row, -1);
    }
    
    private TableImpl.TableInner.DataCell addEmptyCell(final TableImpl.TableInner.Row row, int index){
        final TableImpl.TableInner.DataCell td = row.addCell(index);
        td.addClass("rwt-gbc-td");
        td.setAttr("rowspan", 1);
        td.setAttr("colspan", 1);
        td.setAttr("empty", 1);
        Div div = new Div();
        td.add(div);
        div.addClass("rwt-gbc-div");
        div.addClass("rwt-container");
        return td;
    }
    
    public void add(final UIObject obj, final int row, final int col) {
        add(obj,row,col,null);
    }

    public void add(final UIObject obj, final int row, final int col, final Alignment verticalAlign) {
        if (obj==null || findInfo(obj) != null) {
            return;
        } 
        final int mc = this.maxCellCount();
        final int rc = table.rowCount();
        if (row >= rc) {
            this.insertRows(rc, rc - row + 1, false);
        }
        if (col >= mc) {
            this.insertCols(mc, mc - col + 1, false);
        }
        putObjIntoCell(obj, row, col, verticalAlign);
        if (verticalAlign!=null){
            table.getRow(row).getCell(col).setCss("vertical-align", verticalAlign.getCssValue());
        }
    }
    
    private void putObjIntoCell(final UIObject obj, final int row, final int col, Alignment verticalAlign){
        final Div div = getCellContent(row, col);
        if (!div.children().isEmpty()) {
            throw new IllegalArgumentException("Cell (" + row + ", " + col + ") is not empty");
        }
        div.setAttr("empty", "0");
        div.add(obj.getHtml());
        if (verticalAlign == Alignment.MIDDLE) {
            div.setCss("display", "inline");
        }
        obj.getHtml().setCss("position", "static");
        obj.getHtml().addClass("rwt-gbc-element");
        obj.setParent(this);
        table.getRow(row).getCell(col).obj = obj;
        table.getRow(row).getCell(col).maxWidth = obj.getHtml().getCss("max-width");
        objects.add(obj);        
        this.html.renew();        
    }
    
    private Div getCellContent(final int row, final int col){
        return (Div)table.getRow(row).getCell(col).getChildAt(0);
    }

    private ObjectInfo findInfo(UIObject obj) {
        for (int i = 0; i < table.rowCount(); i++) {
            for (int j = 0; j < table.getRow(i).cellCount(); j++) {
                if (table.getRow(i).getCell(j).obj != null && table.getRow(i).getCell(j).obj.equals(obj)) {
                    ObjectInfo info = new ObjectInfo(obj, i, j);
                    return info;
                }
            }
        }
        return null;
    }    

    public int getColSpan(int row, int col) {
        int colspan = Integer.parseInt(table.getRow(row).getCell(col).getAttr("colspan"));
        return colspan;
    }

    public int getRowSpan(int row, int col) {
        int rowspan = Integer.parseInt(table.getRow(row).getCell(col).getAttr("rowspan"));
        return rowspan;
    }

    public void setColSpan(int row, int col, int colspan) {
        if (colspan < 1) {
            return;
        } else {
            if (colspan >= 1) {
                int cc = table.getRow(row).cellCount();
                TableImpl.TableInner.DataCell cell = table.getRow(row).getCell(col);
                int prevRowSpan = getRowSpan(row, col);
                int prevColSpan = getColSpan(row, col);
                if (col + colspan <= cc) {
                    checkCrossing(row, col, 0, colspan, prevRowSpan, prevColSpan);
                    cell.setAttr("colspan", colspan);
                    this.hideCells(row, col, prevRowSpan, prevColSpan);
                } else {
                    checkCrossing(row, col, 0, cc - col, prevRowSpan, prevColSpan);
                    cell.setAttr("colspan", colspan);
                    int count = colspan - cc + col;
                    this.insertCols(cc, count, false);//cc-1
                    this.hideCells(row, col, prevRowSpan, prevColSpan);
                }
            } 
            this.html.renew();
        }
    }

    public void setRowSpan(int row, int col, int rowspan) {
        if (rowspan < 1) {
            return;
        } else {
            int prevRowSpan = getRowSpan(row, col);
            int prevColSpan = getColSpan(row, col);
            if (row + rowspan <= table.rowCount()) {
                checkCrossing(row, col, rowspan, 0, prevRowSpan, prevColSpan);
                table.getRow(row).getCell(col).setAttr("rowspan", rowspan);
                this.hideCells(row, col, prevRowSpan, prevColSpan);
            } else {
                checkCrossing(row, col, table.rowCount() - row, 0, prevRowSpan, prevColSpan);
                this.insertRows(table.rowCount(), table.rowCount() - rowspan + row, false);
                table.getRow(row).getCell(col).setAttr("rowspan", rowspan);
                this.hideCells(row, col, prevRowSpan, prevColSpan);
            }
        }
    }

    public void setAutoWidth(UIObject obj, boolean set) {
        if (findInfo(obj) != null) {
            obj.html.setCss("width", "auto");
        } else {
            obj.html.setCss("overflow-x", "hidden");
        }
    }

    public void setAutoHeight(UIObject obj, boolean set) {
        if (findInfo(obj) != null && set) {
            obj.html.setCss("height", "auto");
        } else {
            obj.html.setCss("overflow-y", "hidden");
        }
    }

    //set column width in percent
    public void setColummnExpand(int col, int w) {
        if (table.rowCount() > 0 && table.getRow(0).cellCount() > col) {
            if (table.getRow(0).getCell(col) != null) {
                if (w >= 0) {
                    String sw = String.valueOf(w);
                    table.getRow(0).getCell(col).setCss("width", sw + "%");
                }
            }
            this.html.renew();
        }
    }

    //set column width in persent
    public void setCellWidthInPercent(int row, int col, int w) {
        if (table.rowCount() > 0 && table.getRow(0).cellCount() > col) {
            if (table.getRow(0).getCell(col) != null) {
                if (w >= 0) {
                    table.getRow(row).getCell(col).setCss("width", w + "%");
                }
            }
        }
    }
    
    public void setColWidthInPixels(int col, int px) {
        table.setFixedColWidth(col, px, FixedColMode.PIXELS);
    }
    
    public void setColWidthInPersent(int col, int pt) {
        table.setFixedColWidth(col, pt, FixedColMode.PERCENT);
    }
    
    public void setStretchColumn(int col) {
        if (col < maxCellCount()) {
            table.expandedCol = col;
            table.updateColWidthAttr();
            table.adjustColWidth();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    
    public void setStretchRow(int row) {
        if (row < table.rowCount()) {
            if (table.expandedRow != null && table.expandedRow != row) {
                setRowHeightByContent(table.expandedRow);
            }
                table.expandedRow = row;
                table.getHtml().setAttr("expandedrow", row);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    
    public void setRowHeightByContent(int row) {
        if (row < table.rowCount()) {
            if (table.expandedRow != null && table.expandedRow == row) {
                table.expandedRow = null;
                TableImpl.TableInner.Row currRow = table.getRow(row);
                for (int i = 0; i < currRow.cellCount(); i++) {
                    TableImpl.TableInner.DataCell cell = currRow.getCell(i);
                    cell.getChildAt(0).setCss("height", null);
                    if (!cell.children().isEmpty() && !cell.getChildAt(0).children().isEmpty()) {
                        cell.getChildAt(0).getChildAt(0).setCss("height", null);
                    }
                }
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    
    public int getStretchColumn() {
        return table.expandedCol != null ? table.expandedCol : -1;
    }
    
    public void setColWidthByContent(int col) {
        if (col < maxCellCount()) {
            if (table.colWidthMap.containsKey(col)) {
                table.colWidthMap.remove(col);
            } else if (table.expandedCol != null && table.expandedCol == col) {
                table.expandedCol = null; 
            }
            for (int i = 0; i < table.rowCount(); i++) {
                for (int j = 0; j < table.getRow(i).cellCount(); j++) {
                    table.getRow(i).getCell(j).setCss("width", null);
                    TableImpl.TableInner.DataCell cell = table.getRow(i).getCell(j);
                    if (!cell.children().isEmpty() && !cell.getChildAt(0).children().isEmpty()) {
                        cell.getChildAt(0).getChildAt(0).setCss("max-width", cell.maxWidth);
                    }
                }
            }
            table.updateColWidthAttr();
            table.adjustColWidth();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    
    public void setAutoAdjustColWidth(boolean isAutoAdjustColWidth) {
        table.setAutoAdjustColWidth(isAutoAdjustColWidth);
        table.adjustColWidth();
    }
    
    public void removeObject(final UIObject obj) {
        if (obj!=null){
            final ObjectInfo info = findInfo(obj);
            if (info != null) {     
                table.getRow(info.row).getCell(info.col).obj = null;
                 table.getRow(info.row).getCell(info.col).maxWidth = null;
                info.obj.setParent(null);
                info.obj.getHtml().remove();
                obj.setParent(null);
                objects.remove(obj);
            }
        }
    }

    public void clearTable() {//clear the table, clear object list
        for (int i = 0; i < table.rowCount(); i++) {
            TableImpl.TableInner.Row currRow = table.getRow(i);
            for (int j = 0; j < currRow.cellCount(); j++) {
                clearCell(i, j); 
            }
        }
        this.html.renew();
    }

    public void setOpacity(UIObject obj, double o) {
        obj.html.setCss("opacity",String.valueOf(o));
    }

    //Move the object into another cell, drop old object // 
    public void moveObject(UIObject obj, int row, int col) {
        final ObjectInfo info = findInfo(obj);
        this.html.renew();
        if (info != null) {
            clearCell(info.row, info.col);
        } else {
            throw new IllegalArgumentException("No object was found");
        }
        if (!table.isEmpty() && row <= table.rowCount() && col < table.getRow(0).cellCount()) {
            clearCell(row, col);
        }
        putObjIntoCell(obj, row, col, null);
        this.html.renew();
    }

    private void hideCells(int row, int col, int prevRowSpan, int prevColSpan) {
        int rowSpan = getRowSpan(row, col);
        int colSpan = getColSpan(row, col);        
        for (int i = row; i < row + Math.max(rowSpan, prevRowSpan); i++) {
            for(int j = col; j < col + Math.max(colSpan, prevColSpan); j++) {
                if (i < row + rowSpan && j < col + colSpan) {
                    table.getRow(i).getCell(j).setVisible(false);
                    table.getRow(i).getCell(j).mainCell = table.getRow(row).getCell(col);
                } else {
                    table.getRow(i).getCell(j).setVisible(true);
                }
            }
        }
        table.getRow(row).getCell(col).setVisible(true);
    }
    
    public void setObjectOverflow(UIObject obj, Overflow of) {

        if (findInfo(obj) != null) {
            switch (of) {
                case HIDDEN:
                    obj.getHtml().setCss("overflow", "hidden");
                    break;
                case VISIBLE:
                    obj.getHtml().setCss("overflow", "visible");
                    break;
                case AUTO:
                    obj.getHtml().setCss("overflow", "auto");
                    break;
                case SCROLL:
                    obj.getHtml().setCss("overflow", "scroll");
                    break;
                case INHERIT:
                    obj.getHtml().setCss("overflow", "inherit");
                    break;
            }
        }
    }

    public Overflow getObjectOverflow(UIObject obj) {
        String of = obj.getHtml().getCss("overflow");
        switch (of) {
            case "auto":
                return Overflow.AUTO;
            case "hidden":
                return Overflow.HIDDEN;
            case "visible":
                return Overflow.VISIBLE;
            case "scroll":
                return Overflow.SCROLL;
            case "inherit":
                return Overflow.INHERIT;
            default:
                return Overflow.HIDDEN;
        }
    }

    public void clearCell(int row, int col) {
        if (table.getRow(row).getCell(col) != null && table.getRow(row).getCell(col).childCount() != 0) {
            UIObject obj = table.getRow(row).getCell(col).obj;
            if (obj != null) {
                obj.setParent(null);
                objects.remove(obj);
                table.getRow(row).getCell(col).obj = null;
                table.getRow(row).getCell(col).getChildAt(0).clear();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setRowHeight(int row, int rh) {
        if (row >= table.rowCount()) {
            return;
        } else {
            final Table.Row tableRow = table.getRow(row);
            for (int i=0,count=tableRow.children().size(); i<count; i++){
                final Html container = tableRow.getCell(i).getChildAt(0);
                if (rh>=0){
                    container.setCss("height", rh + "px");
                    container.setCss("min-height", rh + "px");
                    container.setCss("max-height", rh + "px");
                }else{
                    container.setCss("height", null);
                    container.setCss("min-height", null);
                    container.setCss("max-height", null);
                }                
            }
        }
    }

    @Override
    public void setWidth(int w) {
        table.setWidth(w);
    }

    @Override
    public void setHeight(int h) {
        table.setHeight(h);
    }

    public void setMinWidth(int mw) {
        table.setMinWidth(mw);
    }

    public void setMaxWidth(int mw) {
        table.setMaxWidth(mw);
    }

    public void insertRows(int index, int num) {
        insertRows(index, num, index < table.rowCount() ? true : false);
    }
    
    private void insertRows(int index, int num, boolean update) {
        if (num > 0 && index <= table.rowCount()) {
            for (int i = 0; i < num; i++) {
                TableImpl.TableInner.Row tr = table.insertRow(index);                
                for (int j = 0; j < this.maxCellCount(); j++) {
                    TableImpl.TableInner.DataCell cell = addEmptyCell(tr);
                    if (update && table.rowCount() > 1 && table.getRow(index + 1).getCell(j).isSpanned) {
                        cell.setVisible(false);
                        cell.mainCell = table.getRow(index + 1).getCell(j).mainCell;
                        TableImpl.TableInner.DataCell mainCell = cell.mainCell;
                        Integer mainCellColIndex = mainCell.getColIndex();
                        Integer mainCellRowIndex = mainCell.getRowIndex();
                        int mainCellColSpan = getColSpan(mainCellRowIndex, mainCellColIndex);
                        if (cell.mainCell.getColIndex() + mainCellColSpan - 1 == j) {
                            mainCell.setAttr("rowspan", getRowSpan(mainCellRowIndex, mainCellColIndex) + 1);
                        }   
                    }
                }
            }
            table.adjustColWidth();
        }
    }

    public void addRow() {
        insertRows(table.rowCount(), 1);
    }
    
    private int maxCellCount() {
        int mc = 0;
        for (int l = 0; l < table.rowCount(); l++) {
            TableImpl.TableInner.Row r = table.getRow(l);
            mc = Math.max(mc, r.cellCount());
        }
        return mc;
    }

    public void insertCols(int index, int num) {
        insertCols(index, num, true);
    }
    
    private void insertCols(int index, int num, boolean update) {
        int mc = this.maxCellCount();
        if (num > 0 && index <= mc) {
            int trc = table.rowCount();
            for (int i = 0; i < trc; i++) {
                TableImpl.TableInner.Row tr = table.getRow(i);
                TableImpl.TableInner.DataCell mainCell = null;
                if (update && tr.getCell(index).isSpanned) {
                    mainCell = tr.getCell(index).mainCell;
                }
                for (int j = 0; j < num; j++) {
                    TableImpl.TableInner.DataCell cell = addEmptyCell(tr, index + j);
                    if (mainCell != null) {
                        cell.mainCell = mainCell;
                        cell.setVisible(false);
                    }
                }
                if (mainCell != null && i == mainCell.getRowIndex() + getRowSpan(mainCell.getRowIndex(), mainCell.getColIndex()) -1) {
                    setColSpan(mainCell.getRowIndex(), mainCell.getColIndex(), getColSpan(mainCell.getRowIndex(), mainCell.getColIndex()) + num);
                }
            }
            table.updateAfterInsertCols(index, num);
            table.updateColWidthAttr();
            table.adjustColWidth();
            this.html.renew();
        }
    }

    public void removeRow(int row) {
            TableImpl.TableInner.Row currRow = table.getRow(row);
            for (int i = currRow.cellCount() - 1; i >= 0; i--) {
                TableImpl.TableInner.DataCell cell = currRow.getCell(i);
                if (getRowSpan(row, i) > 1) {
                    setRowSpan(row, i, 1);
                }
                if (cell.isSpanned) {
                    TableImpl.TableInner.DataCell mainCell =  cell.mainCell;
                    int mainCellRowIndex = mainCell.getRowIndex();
                    int mainCellColIndex = mainCell.getColIndex();
                    if (mainCellRowIndex == cell.getRowIndex()) {
                        setColSpan(mainCellRowIndex, mainCellColIndex, 1);
                    } else if (cell.getColIndex() == mainCellColIndex) {
                        mainCell.setAttr("rowspan", getRowSpan(mainCell.getRowIndex(), mainCell.getColIndex()) - 1);
                    }
                }
                clearCell(row, i);
            }
            if (table.expandedRow != null) {
                if (table.expandedRow.equals(row)) {
                    table.expandedRow = null;
                    table.getHtml().setAttr("expandedrow", table.expandedRow);
                } else {
                    if (table.expandedRow > row) {
                        table.expandedRow--;
                        table.getHtml().setAttr("expandedrow", table.expandedRow);
                    }
                }
            }
            table.getRow(row).clear();
            table.getRow(row).remove();
            this.html.renew();
    }

    public void removeCol(int col) {
        for (int j = 0; j < table.rowCount(); j++) {
            TableImpl.TableInner.DataCell cell = table.getRow(j).getCell(col);
            if (getColSpan(j, col) > 1) {
                setColSpan(j, col, 1);
            }
            if (getRowSpan(j, col) > 1) {
                setRowSpan(j, col, 1);
            }
            if (cell.isSpanned) {
                TableImpl.TableInner.DataCell mainCell = cell.mainCell;
                int mainCellRowIndex = mainCell.getRowIndex();
                int mainCellColIndex = mainCell.getColIndex();
                Integer mainCellRowSpan = getRowSpan(mainCellRowIndex, mainCellColIndex);
                if (cell.getRowIndex() == mainCellRowIndex + mainCellRowSpan - 1) {
                    Integer mainCellColSpan = getColSpan(mainCellRowIndex, mainCellColIndex);
                    setRowSpan(mainCellRowIndex, mainCellColIndex, mainCellColSpan - 1);
                }
            }
            clearCell(j, col);
            table.getRow(j).getCell(col).remove();
        }
        table.updateAfterRemoveCol(col);
        table.adjustColWidth();
        table.updateColWidthAttr();
        this.html.renew();
    }
    
    public int getRowCount(){
        return table.rowCount();
    }
    
    public int getCellCount(int row){
        return table.getRow(row).cellCount();
    }

    private void checkCrossing(int row, int col, int rowSpan, int colSpan, int prevRowspan, int prevColspan) {
        if (!table.getRow(row).getCell(col).isSpanned) {
            for (int i = row; i < row + Math.max(rowSpan, prevRowspan); i++) {
                for(int j = col; j < col + Math.max(colSpan, prevColspan); j++) {
                    if (table.getRow(i).getCell(j).isSpanned && table.getRow(i).getCell(j).mainCell != table.getRow(row).getCell(col)) {
                        throw new IllegalArgumentException("Span cross at cell(" + i + "," + j +")");
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Cell at (" + row + "," + col +") is spanned");
        }
    }
     
    public void setCellVerticalAlign(final int row, final int col, final Alignment alignment){
        table.getRow(row).getCell(col).setCss("vertical-align", alignment.getCssValue());
    }
    
    public Alignment getCellVerticalAlign(final int row, final int col){
        return Alignment.getForCssValue(table.getRow(row).getCell(col).getCss("vertical-align"));
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        } else {
            if (objects == null) {
                return null;
            } else {
                for (UIObject obj : objects) {
                    result = obj.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
                return null;
            }
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (objects != null) {
            for (UIObject obj : objects) {
                obj.visit(visitor);
            }
        }
    }
    
    @Override
    public void setVSizePolicy(final SizePolicy vSizePolicy) {
       table.setVSizePolicy(vSizePolicy);
    }

    @Override
    public void setHSizePolicy(final SizePolicy hSizePolicy) {
        table.setHSizePolicy(hSizePolicy);
    }        
}
