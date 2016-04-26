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
    
    private final Table table = new Table();    
    private final List<ObjectInfo> children = new LinkedList<>();
    private final List<UIObject> objects = new LinkedList<>();

    public GridBoxContainer() {
        this(0,0);
    }

    public GridBoxContainer(int row, int col) {
        super(new Div());
        if (row>0 && col>0){
            createTable(row, col);
        }
        this.html.add(table);
        this.html.setCss("width", "auto");
        this.html.setCss("height", "auto");
        table.addClass("rwt-gridbox-container");
        table.setAttr("cellpadding", "0");
    }

    public GridBoxContainer getGridBoxContainer() {
        return this;
    }

    private Table createTable(int row, int col) {
        for (int i = 0; i < row; i++) {
            Table.Row r = this.table.addRow();
            for (int j = 0; j < col; j++) {
                addEmptyCell(r);
            }
        }
        return table;
    }
    
    private void addEmptyCell(final Table.Row row){
        final Table.DataCell td = row.addCell();
        td.addClass("rwt-gbc-td");
        td.setAttr("rowspan", 1);
        td.setAttr("colspan", 1);
        td.setAttr("empty", 1);
        Div div = new Div();
        td.add(div);
        div.addClass("rwt-gbc-div");
        div.addClass("rwt-container");        
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
            this.insertRows(rc, rc - row + 1);
        }
        if (col >= mc) {
            this.insertCols(mc, mc - col + 1);
        }
        putObjIntoCell(obj, row, col);
        if (verticalAlign!=null){
            table.getRow(row).getCell(col).setCss("vertical-align", verticalAlign.getCssValue());
        }
    }
    
    private void putObjIntoCell(final UIObject obj, final int row, final int col){
        final Div div = getCellContent(row, col);
        div.setAttr("empty", "0");
        div.add(obj.getHtml());
        obj.getHtml().setCss("position", "static");
        obj.getHtml().addClass("rwt-gbc-element");
        obj.setParent(this);
        final ObjectInfo info = new ObjectInfo(obj, row, col);
        children.add(info);
        objects.add(obj);        
        this.html.renew();        
    }
    
    private Div getCellContent(final int row, final int col){
        return (Div)table.getRow(row).getCell(col).getChildAt(0);
    }

    private ObjectInfo findInfo(UIObject obj) {
        for (ObjectInfo info : children) {
            if (info.obj == obj) {
                return info;
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
        }
        int cc = table.getRow(row).cellCount();
        int rowspan = Integer.parseInt(table.getRow(row).getCell(col).getAttr("rowspan"));
        if (colspan > 1) {
            if (col + colspan < cc) {
                table.getRow(row).getCell(col).setAttr("colspan", colspan);
                this.removeCells(row, col);
            } else if (col + colspan > cc) {
                table.getRow(row).getCell(col).setAttr("colspan", colspan);

                if (rowspan <= 1) {
                    int index = colspan - cc + col;
                    this.insertCols(cc, index);//cc-1
                    int icc = table.getRow(row).cellCount();
                    table.renew();
                    for (int i = col + 1; i < icc; i++) {
                        table.getRow(row).getChildAt(i).setCss("display", "none");
                    }

                } else if (rowspan > 1) {
                    for (int j = row; j < row + rowspan; j++) {
                        Table.Row tr = table.getRow(j);
                        int trc = tr.cellCount();
                        for (int k = trc - colspan + 1; k < trc; k++) {
                            tr.getCell(k).setCss("display", "none");
                        }
                    }
                }
            } else if (col + colspan == cc) {
                table.getRow(row).getCell(col).setAttr("colspan", colspan);
                for (int i = col + 1; i < col + colspan; i++) {
                    table.getRow(row).getCell(i).setCss("visibility", "hidden");
                    table.getRow(row).getCell(i).setCss("display", "none");
                }
                if (rowspan > 1) {
                    for (int j = row; j < row + rowspan; j++) {
                        Table.Row jtr = table.getRow(j);
                        int jtrc = jtr.cellCount();
                        for (int l = 0; l < jtrc; l++) {
                            jtr.getCell(l).setCss("visibility", "hidden");
                            jtr.getCell(l).setCss("display", "none");
                        }
                        jtr.getCell(0).setCss("display", "table-cell");
                    }
                }
            }
            this.html.renew();
            this.fixCellCrossing(row, col);
        }
    }

    private int getColSpanNum(int irow) {
        int csnum = 0;
        for (int i = 0; i <= irow; i++) {
            Table.Row itr = table.getRow(i);
            for (int j = 0; j < itr.cellCount(); j++) {
                int rowspan = Integer.parseInt(itr.getCell(j).getAttr("rowspan"));
                int colspan = Integer.parseInt(itr.getCell(j).getAttr("colspan"));
                if (colspan > 1 && rowspan == 1 && i == irow) {
                    csnum++;
                } else if (colspan > 1 && rowspan > 1 && (irow < i + rowspan && irow > i)) {
                    csnum++;
                }
            }
        }
        return csnum;
    }

    public void setRowSpan(int row, int col, int rowspan) {
        if (rowspan < 1) {
            return;
        }
        int colspan = this.getColSpan(row, col);
        if (rowspan > 1) {
            if (row + rowspan <= table.rowCount()) {
                table.getRow(row).getCell(col).setAttr("rowspan", rowspan);
                this.removeCells(row, col);
                if (colspan == table.getRow(row).cellCount()) {
                    for (int k = row + 1; k < row + rowspan; k++) {
                        Table.Row tr = table.getRow(k);
                        for (int l = 0; l < tr.cellCount(); l++) {
                            tr.getCell(l).setCss("visibility", "hidden");
                            tr.getCell(l).setCss("display", "none");
                        }
                        tr.getCell(tr.cellCount() - 1).setCss("display", null);
                    }
                }
            } else if (row + rowspan > table.rowCount()) {//cell-merging beyond table borders
                this.insertRows(table.rowCount(), table.rowCount() - rowspan + row);
                table.getRow(row).getCell(col).setAttr("rowspan", rowspan);
                for (int i = row + rowspan - 1; i > row; i--) {
                    this.html.renew();
                    int trc = table.getRow(i).cellCount() - 1;
                    table.getRow(i).getCell(trc).setCss("display", "none");
                }
            }
        }
        this.fixCellCrossing(row, col);
    }

    private int getRowSpanSum(int irow) {
        int rscol = 0;
        for (int i = 0; i <= irow; i++) {
            Table.Row tr = table.getRow(i);
            int trc = tr.cellCount();
            for (int j = 0; j < trc; j++) {
                int rowspan = Integer.parseInt(tr.getCell(j).getAttr("rowspan"));
                if (rowspan > 1 && irow > i && irow < i + rowspan) {
                    rscol++;
                }
            }
        }
        return rscol;
    }

    private int getColspanSum(int irow) {
        int sum = 0;
        for (int i = 0; i <= irow; i++) {
            Table.Row itr = table.getRow(i);
            for (int j = 0; j < itr.cellCount(); j++) {
                int rowspan = Integer.parseInt(itr.getCell(j).getAttr("rowspan"));
                int colspan = Integer.parseInt(itr.getCell(j).getAttr("colspan"));
                if (colspan > 1 && rowspan == 1 && i == irow) {
                    sum += colspan;
                } else if (colspan > 1 && rowspan > 1 && (irow < i + rowspan && irow > i)) {
                    sum += colspan;
                }
            }
        }
        return sum;
    }

    public void setAutoWidth(UIObject obj, boolean set) {
        if (this.children.contains(findInfo(obj)) && set) {
            obj.html.setCss("width", "auto");
        } else {
            obj.html.setCss("overflow-x", "hidden");
        }
    }

    public void setAutoHeight(UIObject obj, boolean set) {
        if (this.children.contains(findInfo(obj)) && set) {
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

    public void removeObject(final UIObject obj) {
        if (obj!=null){
            final ObjectInfo info = findInfo(obj);
            if (info != null) {            
                info.obj.getHtml().getParent().setAttr("rowspan", "1");
                info.obj.getHtml().getParent().setAttr("colspan", "1");
                info.obj.setParent(null);
                info.obj.getHtml().remove();

                children.remove(info);
                obj.setParent(null);
                objects.remove(obj);
            }
        }
    }

    public void clearTable() {//clear the table, clear object list
        table.clear();
        table.children().clear();
        children.clear();
        for (UIObject o : objects) {
            o.setParent(null);
        }
        objects.clear();
        this.html.renew();
    }

    public void setOpacity(UIObject obj, double o) {
        String so = String.valueOf(o);
        obj.html.setCss("opacity", so == null ? null : String.valueOf(o));
    }

    //Move the object into another cell, drop old object
    public void moveObject(UIObject obj, int row, int col) {
        final ObjectInfo info = findInfo(obj);
        this.html.renew();
        int oldrow = info.row;
        int oldcell = info.col;
        Html div = new Html("div");
        div.addClass("rwt-gbc-div");
        div.addClass("rwt-container");
        table.getRow(oldrow).getCell(oldcell).clear();
        table.getRow(row).getCell(col).clear();
        table.getRow(row).getCell(col).add(div);        
        final ObjectInfo newInfo = new ObjectInfo(obj, row, col);
        children.remove(info);
        children.add(newInfo);
        this.html.renew();
    }

    private void removeCells(int row, int col) {
        int rowspan = this.getRowSpan(row, col);
        if (rowspan > 1) {
            for (int j = row; j < row + rowspan; j++) {
                Table.Row tr = table.getRow(j);
                int trc = tr.cellCount();
                int rscol = this.getRowSpanSum(j);
                int css = this.getColspanSum(j);
                int csn = this.getColSpanNum(j);
                for (int i = trc - css + csn - rscol; i < trc; i++) {
                    tr.getCell(i).setCss("visibility", "collapse");
                    tr.getCell(i).setCss("display", "none");
                }
                table.getRow(j).getCell(0).setCss("visibility", "visible");
                table.getRow(j).getCell(0).setCss("display", null);
            }
        } else if (rowspan <= 1) {
            Table.Row tr = table.getRow(row);
            int rcolsum = this.getColspanSum(row);
            int rscol = this.getRowSpanSum(row);
            int rcolnum = this.getColSpanNum(row);
            int trcc = tr.cellCount();
            for (int i = trcc - rcolsum + rcolnum - rscol; i < trcc; i++) {
                tr.getCell(i).setCss("visibility", "collapse");
                tr.getCell(i).setCss("display", "none");
            }
        }
        this.html.renew();
    }

    public void setObjectOverflow(UIObject obj, Overflow of) {

        if (children.contains(findInfo(obj))) {
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
            table.getRow(row).getCell(col).clear();
            table.getRow(row).getCell(col).setAttr("colspan", "1");
            table.getRow(row).getCell(col).setAttr("rowspan", "1");
            for (ObjectInfo info : children) {
                if (info.row == row && info.col == col) {
                    info.obj.setParent(null);
                    objects.remove(info.obj);
                }
                children.remove(info);
            }
        }
    }

    public void setRowHeight(int row, int rh) {
        if (row >= table.rowCount()) {
            return;
        } else {
            final Table.Row tableRow = table.getRow(row);
            for (int i=0,count=tableRow.cellCount(); i<count; i++){
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
        if (w > 0) {
            this.html.setCss("width", String.valueOf(w) + "px");
            this.html.setCss("max-width", String.valueOf(w) + "px");
        }
        table.setAttr("wresize", String.valueOf(w));
    }

    @Override
    public void setHeight(int h) {
        if (h > 0) {
            this.html.setCss("height", String.valueOf(h) + "px");
            this.html.setCss("max-height", String.valueOf(h) + "px");
        }
        table.setAttr("hresize", String.valueOf(h));
    }

    public void setMinWidth(int mw) {
        table.setCss("min-width", mw);
        table.setCss("display", "table-cell");
    }

    public void setMaxWidth(int mw) {
        table.setCss("max-width", mw + "px");
        table.setCss("width", null);
        table.setCss("display", "table-cell");
    }

    public void insertRows(int index, int num) {
        final int cc = this.maxCellCount();
        if (num > 0 && index <= table.rowCount()) {
            for (int i = 0; i < num; i++) {
                Table.Row tr = table.insertRow(index);                
                int rssum = this.getRowSpanSum(i);
                for (int j = 0; j < cc - rssum; j++) {
                    addEmptyCell(tr);
                }
            }
        }
    }

    private int maxCellCount() {
        int mc = 0;
        for (int l = 0; l < table.rowCount(); l++) {
            Table.Row r = table.getRow(l);
            mc = Math.max(mc, r.cellCount());
        }
        return mc;
    }

    public void insertCols(int index, int num) {
        int mc = this.maxCellCount();
        if (num > 0 && index <= mc) {
            int trc = table.rowCount();
            for (int i = 0; i < trc; i++) {
                Table.Row tr = table.getRow(i);
                for (int j = 0; j < num; j++) {
                    addEmptyCell(tr);
                }
            }
            this.html.renew();
        }
    }

    public void removeRow(int row) {
        if (row<table.rowCount()) {
            for (ObjectInfo child : children) {
                int colspan = this.getColSpan(child.row, child.col);
                if (child.row == row && colspan == 1) {
                        child.obj.setParent(null);
                    children.remove(child);
                } else if (child.row == row && colspan > 1) {
                        child.obj.setParent(null);
                    children.remove(child);
                    table.getRow(child.row).getCell(child.col).setAttr("colspan", null);
                }

            }
            table.getRow(row).clear();
            table.getRow(row).remove();
            this.html.renew();
        }
    }

    public void removeCol(int col) {
        for (int j = 0; j < table.rowCount(); j++) {
            table.getRow(j).getCell(col).clear();
            table.getRow(j).getCell(col).remove();
            for (ObjectInfo child : children) {
                int rowspan = this.getRowSpan(child.row, child.col);
                if (child.col == col && rowspan == 1) {
                        child.obj.setParent(null);
                    children.remove(child);
                } else if (child.col == col && rowspan > 1) {
                        child.obj.setParent(null);
                    children.remove(child);
                    for (int i = child.col; i < table.getRow(j).cellCount(); i++) {
                        table.getRow(child.row).addCell(i);
                    }
                }
            }
        }
        this.html.renew();
    }
    
    public int getRowCount(){
        return table.rowCount();
    }
    
    public int getCellCount(int row){
        return table.getRow(row).cellCount();
    }

    private void fixCellCrossing(int row, int col) {
        int colspan = this.getColSpan(row, col);
        int rowspan = this.getRowSpan(row, col);
        if (colspan > 1) {
            for (int i = 0; i < row; i++) {
                Table.Row itr = table.getRow(i);
                int icc = itr.cellCount();
                for (int j = 0; j < icc; j++) {
                    int irowspan = this.getRowSpan(i, j);
                    int empty = Integer.parseInt(itr.getCell(j).getAttr("empty"));
                    if (irowspan > 1 && row > i && row < i + irowspan && j > col && j < col + colspan) {
                        this.insertCols(j, 1);
                    }
                    if (empty == 0) {
                        this.insertCols(j + 1, 1);
                    }
                }
            }
        }
        if (rowspan > 1) {
            for (int i = row; i < row + rowspan; i++) {
                Table.Row itr = table.getRow(i);

                for (int j = 0; j < itr.cellCount(); j++) {
                    int irowspan = this.getRowSpan(i, j);
                    int empty = Integer.parseInt(itr.getCell(j).getAttr("empty"));
                    int icolspan = this.getColSpan(i, j);
                    if (icolspan > 1 && col > j && col < j + icolspan) {
                        int index = i;
                        this.insertRows(index, 1);

                        if (irowspan > 1) {
                            for (int l = row + 1; l < row + rowspan; l++) {
                                int icc = table.getRow(l).cellCount();
                                table.getRow(l).getCell(icc - 1).setCss("visibility", "collapse");
                                table.getRow(l).getCell(icc - 1).setCss("display", "none");
                            }
                        }
                        if (irowspan == 1) {
                            for (int b = table.getRow(row).cellCount() - icolspan + 1; b < table.getRow(row).cellCount() - 1; b++) {
                                table.getRow(row).getCell(b).setCss("visible", "collapse");
                                table.getRow(row).getCell(b).setCss("display", "none");
                                table.getRow(row).getCell(b).setAttr("irse1", true);
                            }
                            for (int k = i; k < i + rowspan; k++) {
                                table.getRow(k).getCell(j + icolspan).setCss("visibility", "visible");
                                table.getRow(k).getCell(j + icolspan).setCss("display", "table-cell");
                                table.getRow(k).getCell(j + icolspan).setAttr("fixed", true);
                            }
                            for (int l = row + 1; l < row + rowspan; l++) {
                                int icc = table.getRow(l).cellCount();
                                int irssum = this.getRowSpanSum(l);
                                table.getRow(l).getCell(icc - irssum).setCss("visibility", "collapse");// table.getRow(l).getCell(icc - 1).setCss("visibility", "collapse");
                                table.getRow(l).getCell(icc - irssum).setCss("display", "none");
                            }
                        }
                    }
                    if (empty == 0) {
                        this.insertRows(i + 1, 1);
                    }
                }
            }
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
        super.setVSizePolicy(vSizePolicy);
        switch (vSizePolicy) {
            case EXPAND:
                table.setCss(Html.CSSRule.HEIGHT, "100%");
                break;
            case MINIMUM_EXPAND:
                html.setCss(Html.CSSRule.HEIGHT, null);
                break;
        }
    }

    @Override
    public void setHSizePolicy(final SizePolicy hSizePolicy) {
        super.setHSizePolicy(hSizePolicy);
        switch (hSizePolicy) {
            case EXPAND:
                table.setCss(Html.CSSRule.WIDTH, "100%");
                break;
            case MINIMUM_EXPAND:
                html.setCss(Html.CSSRule.WIDTH, null);
                break;
        }
    }        
}
