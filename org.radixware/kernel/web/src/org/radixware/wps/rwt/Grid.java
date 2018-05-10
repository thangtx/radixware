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
import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.types.FilterRules;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.wps.rwt.IGrid.CellRenderer;
import org.radixware.wps.rwt.events.ClickHtmlEvent;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.events.KeyDownEventFilter;
import org.radixware.wps.rwt.events.KeyDownHtmlEvent;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class Grid extends UIObject implements IGrid {        

    public static enum SelectionMode {

        ROW, CELL, NONE;
    }

    public interface CurrentRowListener {

        public void currentRowChanged(Row oldRow, Row newRow);

        public boolean beforeChangeCurrentRow(Row oldRow, Row newRow);
    }

    public interface CurrentCellListener {

        public void currentCellChanged(Cell oldCell, Cell newCell);

        public boolean beforeChangeCurrentCell(Cell oldCell, Cell newCell);
    }

    public static abstract class UnconditionalRowSelectionListener implements CurrentRowListener {

        @Override
        public boolean beforeChangeCurrentRow(Row oldRow, Row newRow) {
            return true;
        }
    }

    public interface DoubleClickListener {

        void onDoubleClick(Row row, Cell cell);
    }

    public interface ColumnHeaderClickListener {

        void onClick(final Column column, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }

    public interface CellValueChangeListener {

        void onValueChanged(final Cell cell, final Object oldValue, final Object newValue);
    }
    
    public class Column extends IGrid.AbstractColumn {

        @Deprecated
        public void setPersistentId(String id) {
            setPersistenceKey(id);
        }

        Column(final AbstractColumnHeaderCell headerCell) {
            super(headerCell,"$RWT.grid.onHeaderClick", "$RWT.grid.onContextMenu");
        }

        public int getIndex() {
            return Grid.this.horizontalHeader.getColumnIndex(this);
        }

        public LinkedList<Cell> getCellsAtColumn(int columnIndex) {
            if (columnIndex < 0 && columnIndex >= getColumnCount()) {
                return new LinkedList<>();
            }
            LinkedList<Cell> column = new LinkedList<>();
            for (Row r : verticalHeader) {
                column.add(r.cells.get(columnIndex));
            }
            return column;
        }

        @Override
        public void setVisible(final boolean visible) {
            if (visible!=isVisible()){
                super.setVisible(visible);
                Grid.this.updateColumnsVisibility(null);
                Grid.this.updateColumnsResizingMode();
            }
        }
        
        private void setResizingColumnIdx(final int idx){
            getHeaderCell().getHtml().setAttr(IGrid.IColumn.RESIZE_COLUMN_INDEX_ATTR_NAME, idx);            
            getHeaderCell().setResizable(idx>=0);
            final int nextColumnIndex = getIndex()+1;
            if (nextColumnIndex<Grid.this.horizontalHeader.getColumnsCount()){
                Grid.this.horizontalHeader.getColumn(nextColumnIndex).getHeaderCell().setPrevCellResizable(idx>=0);
            }
        }

        @Override
        public void updateCellsRenderer() {
            final LinkedList<Cell> cells = getCellsAtColumn(getIndex());
            for (Cell c : cells) {
                c.updateRenderer();
            }
        }

        @Override
        protected void afterChangeSizePolicy() {
            Grid.this.updateColumnsResizingMode();
        }

        @Override
        protected void setupColumnsVisiblity() {
            Grid.this.setupColumnsVisiblity();
        }

        @Override
        protected void notifyColumnHeaderClick(EnumSet<EKeyboardModifier> modifiers) {
            Grid.this.notifyColumnHeaderClick(this, modifiers);
        }                
    }
    
    private void updateColumnsResizingMode(){
        if (isStickToRight()){
            final IGrid.EColumnSizePolicy[] actualSizePolicy = columnsResizeController.calcFinalSizePolicy();                    
            for (int i=0,count=getColumnCount(); i<count; i++){
                getColumn(i).applySizePolicy(actualSizePolicy[i]);
            }
        }
        for (int i=0,count=getColumnCount(); i<count; i++){            
            final Column column = getColumn(i);            
            column.setResizingColumnIdx(columnsResizeController.findSectionToResize(i));
        }
    }            

    public void setColumnsHeaderAlignment(final Alignment a) {
        horizontalHeader.setTextAlignment(a);        
    }

    public Alignment getColumnsHeaderAlignment() {
        return horizontalHeader.getTextAlignment();
    }
    
    public interface RowHeaderClickListener{
        void onClick(final Row row, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }
    
    public interface RowHeaderDoubleClickListener{
        void onDoubleClick(final Row row, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }    
            
    private class VerticalHeader extends IGrid.AbstractRowHeader implements Iterable<Row>{
        
        private List<RowHeaderClickListener> clickListeners;
        private List<RowHeaderDoubleClickListener> dblClickListeners;
                
        private List<Row> rows = new ArrayList<>();
        
        public VerticalHeader(){
            super(Grid.this);
        }
                
        public Row addRow(final int index, final String title, final AbstractRowHeaderCell rowHeaderCell){
            final boolean addLastRow;
            final int actualIndex = index<0 || index>=rows.size() ? rows.size() : index;
            final AbstractRowHeaderCell actualRowHeaderCell = rowHeaderCell==null ? new DefaultRowHeaderCell() : rowHeaderCell;
            final VerticalHeaderCell headerCell = new VerticalHeaderCell(this, actualRowHeaderCell);
            final Row row = new Row(headerCell);
            if (index<0 || index>=rows.size()){
                if (!rows.isEmpty()) {
                    final Row lastRow = rows.get(rows.size() - 1);
                    for (Cell c : lastRow.cells) {
                        c.html.removeClass("rwt-grid-data-last-row");
                        c.html.addClass("rwt-grid-data-row");
                    }
                }
                rows.add(row);
                addHeaderCell(headerCell);
                addLastRow = true;
            }else{
                rows.add(index, row);
                addHeaderCell(index, headerCell);
                addLastRow = false;
            }
            for (int i=0, count=horizontalHeader.getColumnsCount(); i<count; i++) {
                final Column c = horizontalHeader.getColumn(i);
                final Cell cell = row.addCell(c, i);
                cell.html.addClass("rwt-grid-data-col");
                if (addLastRow){
                    cell.html.addClass("rwt-grid-data-last-row");
                }
                cell.setVisible(c.isVisible());
            }
            row.setParent(Grid.this.data);
            Grid.this.data.addRow(actualIndex, row);
            if (title!=null && !title.isEmpty()){
                actualRowHeaderCell.setTitle(title);
            }
            Grid.this.updateVerticalHeaderVisibility();
            row.applyFilter();
            return row;
        }
        
        public Row addRowWithSpannedCells(final String title, final AbstractRowHeaderCell rowHeaderCell) {

            if (!rows.isEmpty()) {
                Row lastRow = rows.get(rows.size() - 1);
                for (Cell c : lastRow.cells) {
                    c.html.removeClass("rwt-grid-data-last-row");
                    c.html.addClass("rwt-grid-data-row");
                }
            }
            final AbstractRowHeaderCell actualRowHeaderCell = rowHeaderCell==null ? new DefaultRowHeaderCell() : rowHeaderCell;
            final VerticalHeaderCell headerCell = new VerticalHeaderCell(this, actualRowHeaderCell);            
            
            final Row row = new Row(headerCell);
            row.setParent(Grid.this.data);   
            rows.add(row);            
            
            final Cell cell = row.addCell(horizontalHeader.getColumn(0), 0);
            cell.html.setAttr("colSpan", horizontalHeader.getColumnsCount());
            cell.html.addClass("rwt-grid-data-col");
            cell.html.addClass("rwt-grid-data-last-row");
            Grid.this.data.addRow(-1, row);
            
            addHeaderCell(headerCell);
            if (title!=null && !title.isEmpty()){
                headerCell.setTitle(title);
            }
            Grid.this.updateVerticalHeaderVisibility();
            row.applyFilter();
            return row;
        }     
        
        public void swapRows(final int index1, final int index2) {
            if (index1 < 0 || index1 >= rows.size()) {
                return;
            }
            if (index2 < 0 || index2 >= rows.size()) {
                return;
            }
            if (index1 == index2) {
                return;
            }

            final Row row1 = getRow(index1);
            final Row row2 = getRow(index2);
            final VerticalHeaderCell cell1 = row1.getHeaderCell();
            final VerticalHeaderCell cell2 = row2.getHeaderCell();
            
            rows.set(index1, row2);
            rows.set(index2, row1);

            Grid.this.data.removeRow(row1);
            row1.getHtml().renew();
            
            Grid.this.data.removeRow(row2);
            row2.getHtml().renew();
            
            removeHeaderCell(cell1);
            cell1.getHtml().renew();
            
            removeHeaderCell(cell2);
            cell2.getHtml().renew();

            if (index1<index2){
                Grid.this.data.addRow(index1, row2);
                Grid.this.data.addRow(index2, row1);
                addHeaderCell(index1, cell2);
                addHeaderCell(index2, cell1);
            }else{
                Grid.this.data.addRow(index2, row1);
                Grid.this.data.addRow(index1, row2);
                addHeaderCell(index2, cell1);
                addHeaderCell(index1, cell2);
            }            
            
            row1.applyFilter();
            row2.applyFilter();
        }
        
        public int getRowsCount(){
            return rows.size();
        }
        
        public int getRowIndex(final Row row){
            return rows.indexOf(row);
        }
        
        public Row getRow(final int index){            
            return rows.get(index);
        }

        @Override
        public Iterator<Row> iterator() {
            return rows.iterator();
        }                
        
        public void removeAllRows(){
            Grid.this.data.removeAllRows();
            rows.clear();
            clearHeaderCells();
            Grid.this.updateVerticalHeaderVisibility();
        }
        
        public void removeRow(final Row row) {
            if (rows != null) {
                if (rows.remove(row)) {
                    Grid.this.data.removeRow(row);
                    removeHeaderCell(row.getHeaderCell());
                    Grid.this.updateVerticalHeaderVisibility();
                }
            }
        }

        @Override
        public UIObject findObjectByHtmlId(final String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            if (!rows.isEmpty()) {
                for (Row r : rows) {
                    result = r.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
            }            
            return null;
        }

        @Override
        public void visit(final Visitor visitor) {            
            if (!rows.isEmpty()) {
                for (Row r : rows) {
                    r.visit(visitor);
                }
            }
            super.visit(visitor);
        }        
        
        public void addClickListener(final RowHeaderClickListener listener){
            if (listener!=null){
                if (clickListeners==null){
                    clickListeners = new LinkedList<>();
                    getHtml().setAttr("onclick", "$RWT.gridLayout.onVerticalHeaderCellClick");
                }
                if (!clickListeners.contains(listener)){
                    clickListeners.add(listener);
                }
            }
        }
        
        public void removeClickListener(final RowHeaderClickListener listener){
            if (clickListeners!=null && !clickListeners.isEmpty() && listener!=null){
                clickListeners.remove(listener);
                if (clickListeners.isEmpty()){
                    clickListeners = null;
                    getHtml().setAttr("onclick", null);
                }
            }
        }       
        
        @Override
        protected void notifyClick(final VerticalHeaderCell vHeaderCell, final EnumSet<EKeyboardModifier> modifiers){
            if (clickListeners!=null){
                final Row row = findRowForHeaderCell(vHeaderCell);
                if (row!=null){
                    final List<RowHeaderClickListener> listeners = new LinkedList<>(clickListeners);
                    for (RowHeaderClickListener listener: listeners){
                        listener.onClick(row, modifiers);
                    }
                }
            }
        }
        
        public void addDoubleClickListener(final RowHeaderDoubleClickListener listener){
            if (listener!=null){
                if (dblClickListeners==null){
                    dblClickListeners = new LinkedList<>();
                    getHtml().setAttr("ondblclick", "$RWT.gridLayout.onVerticalHeaderCellDblClick");
                }
                if (!dblClickListeners.contains(listener)){
                    dblClickListeners.add(listener);
                }
            }
        }
        
        public void removeDoubleClickListener(final RowHeaderDoubleClickListener listener){
            if (dblClickListeners!=null && !dblClickListeners.isEmpty() && listener!=null){
                dblClickListeners.remove(listener);
                if (dblClickListeners.isEmpty()){
                    dblClickListeners = null;
                    getHtml().setAttr("ondblclick", null);
                }
            }
        }
        
        @Override
        protected void notifyDoubleClick(final VerticalHeaderCell vHeaderCell, final EnumSet<EKeyboardModifier> modifiers){
            if (dblClickListeners!=null){
                final Row row = findRowForHeaderCell(vHeaderCell);
                if (row!=null){
                    final List<RowHeaderDoubleClickListener> listeners = new LinkedList<>(dblClickListeners);
                    for (RowHeaderDoubleClickListener listener: listeners){
                        listener.onDoubleClick(row, modifiers);
                    }
                }
            }
        }        
        
        private Row findRowForHeaderCell(final VerticalHeaderCell headerCell){
            for (Row row: rows){
                if (row.getHeaderCell()==headerCell){
                    return row;
                }
            }
            return null;
        }        
    }
    
    private static class Data extends UIObject {

        private final Html table = new Html("table");
        private final Html body = new Html("tbody");        
        private boolean browserFocusFrameEnabled = true;
        private boolean borderShown;        
        private Color shadeColor;
        private int indexOffset;
        
        public Data() {
            super(new Div());            
            
            table.setCss("cellspacing", "0px");
            table.setCss("cellpadding", "0px");
            table.setCss("border-collapse", "collapse");
            table.setCss("border", "none");
            
            table.setCss("table-layout", "fixed");
            table.add(body);
            
            getHtml().add(table);
            getHtml().addClass("rwt-grid-data-panel");
            getHtml().addClass("rwt-grid");
            getHtml().setCss("overflow", "auto");            
            getHtml().setAttr("onscroll", "_rwt_grid_flow._syncScroll");                                    
        }

        public void addRow(final int index, final Row row) {
            if (row.spannedCells()){
                addHtmlForRowWithSpannedCells(index>-1 ? index+indexOffset : index, row.getHtml());
            }else{
                body.add(index>-1 ? index+indexOffset : index, row.getHtml());
            }
        }
        
        public void removeRow(final Row row){
            if (row.spannedCells()){
                removeHtmlForRowWithSpannedCells(row.getHtml());
            }else{
                body.remove(row.getHtml());
            }
        }

        private void addHtmlForRowWithSpannedCells(final int index, final Html row) {
            this.body.add(index, row);
            //add empty row to workaround of https://code.google.com/p/chromium/issues/detail?id=356132
            Html dummyRow = new Html("tr");
            if (index > -1) {
                this.body.add(index + 1, dummyRow);
            } else {
                this.body.add(dummyRow);
            }
            //add second empty row to restore even row
            dummyRow = new Html("tr");
            if (index > -1) {
                this.body.add(index + 2, dummyRow);
            } else {
                this.body.add(dummyRow);
            }
            indexOffset+=2;
        }

        private void removeHtmlForRowWithSpannedCells(final Html row) {
            final int rowHtmlIndex = this.body.indexOfChild(row);
            if (rowHtmlIndex > -1) {
                //remove actual row
                Html rowHtml = body.getChildAt(rowHtmlIndex);
                this.body.remove(rowHtml);
                //remove first dummy empty row
                rowHtml = body.getChildAt(rowHtmlIndex);
                this.body.remove(rowHtml);
                //remove second dummy empty row
                rowHtml = body.getChildAt(rowHtmlIndex);
                this.body.remove(rowHtml);
                indexOffset-=2;
            }
        }
        
        public void removeAllRows(){
            this.body.clear();
        }
                
        public void setBrowserFocusFrameEnabled(final boolean enabled){
            if (browserFocusFrameEnabled!=enabled){
                browserFocusFrameEnabled = enabled;
                if (enabled){
                    table.removeClass("rwt-grid-disable-standard-focus-frame");
                }else{
                    table.addClass("rwt-grid-disable-standard-focus-frame");
                }
            }
        }
        
        public boolean isBrowserFocusFrameEnabled(){
            return browserFocusFrameEnabled;
        }
        
        public void setBorderVisible(final boolean showBorder) {
            if (borderShown!=showBorder){
                table.setAttr(IGrid.SHOW_BORDER_ATTR_NAME, showBorder ? "true" : null);
                borderShown = showBorder;
            }
        }
        
        public boolean isBorderVisible() {
            return borderShown;
        }
        
        public void setShadeColor(final Color color) {
            if (color != null && !color.equals(shadeColor)) {
                shadeColor = color;
                final StringBuilder colorBuilder = new StringBuilder("rgba(");
                colorBuilder.append(String.valueOf(color.getRed())).append(',');
                colorBuilder.append(String.valueOf(color.getGreen())).append(',');
                colorBuilder.append(String.valueOf(color.getBlue())).append(')');                
                table.setAttr("shadeColor", colorBuilder.toString());
            }
        }

        public Color getShadeColor() {
            return shadeColor;
        }
        
        public void shadeEvenRow(int opacityPercent) {
            table.setAttr("alpha", opacityPercent);
        }        
    }

    public class Row extends UIObject {

        private List<Cell> cells = new LinkedList<>();
        private VerticalHeaderCell headerCell;
        private boolean isEditable = true;
        private boolean isMatchToFilter = true;
        private boolean isVisible = true;        
        private Color backgroundColor;
        private FilterRules filterRules;

        Row(final VerticalHeaderCell headerCell) {
            super(new Html("tr"));
            this.headerCell = headerCell;
            if (getSelectionMode() == SelectionMode.ROW) {
                html.markAsChoosable();
            }
        }

        @Override
        public boolean isEnabled() {
            if (super.isEnabled()) {
                return Grid.this.isEnabled();
            } else {
                return false;
            }
        }

        private Cell addCell(Column c, int columnIndex) {
            Cell cell = new Cell(c, this);
            cell.setParent(this);
            this.html.add(columnIndex, cell.getHtml());
            this.cells.add(columnIndex, cell);
            cell.renderer = Grid.this.rendererProvider.newCellRenderer(getRowIndex(cell.row), c.getIndex());
            if (cell.renderer != null) {
                cell.renderer.getUI().setParent(cell);
                cell.container.add(cell.renderer.getUI().html);
            }
            cell.container.addClass("rwt-ui-element");
            cell.container.setCss("width", "100%");
            cell.container.setCss("height", "100%");
            cell.container.setCss("overflow", "hidden");
            return cell;
        }

        public Cell getCell(int idx) {
            return cells.get(idx);
        }

        public Cell getCell(Column c) {
            int idx = horizontalHeader.getColumnIndex(c);
            return cells.get(idx);
        }

        public int getCellCount() {
            return cells.size();
        }

        @Override
        public void visit(final Visitor visitor) {
            super.visit(visitor);
            if (cells != null) {
                for (Cell c : cells) {
                    c.visit(visitor);
                }
            }
            headerCell.visit(visitor);
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            if (cells != null) {
                for (Cell c : cells) {
                    result = c.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
            }            
            return headerCell.findObjectByHtmlId(id);
        }

        boolean spannedCells() {
            if (cells.size() > 0) {
                final String columnSpan = cells.get(0).getHtml().getAttr("colSpan");
                return columnSpan != null && !columnSpan.isEmpty();
            }
            return false;
        }

        public Object getUserData() {
            return headerCell.getUserData();
        }

        public void setUserData(final Object userData) {
            headerCell.setUserData(userData);
        }

        public void setEditable(final boolean isEditable) {
            this.isEditable = isEditable;
        }

        public boolean isEditable() {
            return isEditable;
        }

        public boolean getEditable() {
            return isEditable();
        }
        
        VerticalHeaderCell getHeaderCell(){
            return headerCell;
        }
        
        void setHeaderCell(final VerticalHeaderCell cell){
            headerCell = cell;
        }
        
        public String getTitle(){
            return headerCell.getTitle();
        }
        
        public void setTitle(final String title){
            headerCell.setTitle(title);
        }
        
        public Color getPrimaryBackgroundColor(){
            return backgroundColor;
        }
        
        public void setPrimaryBackgroundColor(final Color color){
            if (!Objects.equals(backgroundColor, color)){
                backgroundColor = color;
                if (cells != null) {
                    for (Cell c : cells) {
                        c.updateSelectionBackground();
                    }
                }
            }
        }
        
        public final void setFilterRules(final FilterRules newRules){
            filterRules = newRules==null ? null : newRules.copy();        
        }

        public final FilterRules getFilterRules(){
            return filterRules==null ? null : filterRules.copy();
        }

        @Override
        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
            super.setVisible(isMatchToFilter && isVisible);
        }
        
        public final void applyFilter(){
            isMatchToFilter = filterRules==null ? true : filterRules.isMatchToSomeFilter(Grid.this.currentFilter);
            super.setVisible(isMatchToFilter && isVisible);
        }        
    }

    public static class DefaultRenderer extends UIObject implements CellRenderer {

        protected final Html label = new Html("label");
        private Html icon;

        public DefaultRenderer() {
            super(new Div());
            this.html.add(label);
            label.setCss("white-space", "nowrap");
            label.setCss("cursor", "inherit");
            label.addClass("rwt-ui-element-text");
            resetComponentState();
            html.addClass("renderer");
            setDefaultClassName("rwt-ui-element-text");
        }

        protected final void resetComponentState() {
            html.setCss("vertical-align", "middle");
            html.setCss("padding-top", "4px");
            html.setCss("padding-bottom", "4px");
            html.setCss("padding-left", "4px");
            label.setCss("padding-right", "6px");
            html.setCss("overflow", "hidden");
            html.setCss("width", "100%");
            html.setCss("height", "100%");
        }

        protected String getDisplayText(final Object value) {
            return String.valueOf(value);
        }

        private Cell findCell() {

            if (getParent() instanceof Cell) {
                return (Cell) getParent();
            }
            return null;
        }

        @Override
        public void update(final int r, final int c, final Object value) {
            label.setInnerText(getDisplayText(value));
            final Cell cell = findCell();
            if (cell != null) {
                if (cell.getIcon() == null) {
                    if (this.icon != null) {
                        this.icon.remove();
                        this.icon = null;
                    }
                } else {
                    if (this.icon == null) {
                        this.icon = new Html("img");
                        this.icon.setCss("width", "12px");
                        this.icon.setCss("height", "12px");
                        this.icon.setCss("top", "1px");
                        this.icon.setCss("position", "relative");
                        html.add(0, this.icon);
                    }
                    if (!Objects.equals(this.icon.getAttr("src"), cell.getIcon().getURI(this))) {
                        this.icon.setAttr("src", cell.getIcon().getURI(this));
                    }
                }
            }
        }

        @Override
        public void selectionChanged(final int r, final int c, final Object value, final ICell cell, final boolean isSelected) {
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
        }

        @Override
        public UIObject getUI() {
            return this;
        }

        @Override
        protected Html getForegroundHolder() {
            return label;
        }
        
        
        @Override
        protected Html getBackgroundHolder() {
            return label;
        }
    }

    public class Cell extends UIObject implements ICell {

        private Html container = new Div();
        private CellRenderer renderer;
        private final Row row;
        private final Column col;
        private boolean isEditable = true;
        private Object value = null;
        private WpsIcon icon;
        private CellEditor editor = null;
        private Object userData;
        private boolean isSelected = false;
        private final IEditingOptions editOpts = new EditingOptions(this);
        private Color selectedBackground = colorFromStr("#6495ED");//cornflowerblue

        @Override
        public IEditingOptions getEditingOptions() {
            return editOpts;
        }

        public Cell(final Column c, final Row r) {
            super(new Html("td"));
            this.col = c;
            setTabIndex(col.getIndex() + 1);
            this.row = r;
            html.setFocusSencitive(false);
            this.html.add(container);
            this.html.setCss("padding", "0px");

            container.addClass("rwt-grid-row-cell-content");
            //this.html.setCss("margin-left", "5px");
            //this.html.setCss("margin-right", "5px");
            registerEventListeners();
            this.html.setAttr("onkeydown", "$RWT.grid.cell.keyDown");
            this.html.setAttr("onfocus", "$RWT.grid.cell.focused");
            if (getSelectionMode() == SelectionMode.CELL) {
                html.markAsChoosable();
            }
            if (!isBorderVisible()){
                html.setCss("border-width","0px");
            }            
        }
        
        private void registerEventListeners(){
            this.html.setAttr("onclick", "$RWT.grid.cell.click");
            this.html.setAttr("ondblclick", "$RWT.grid.cell.dblclick");            
        }
        
        private void removeEventListeners(){
            this.html.setAttr("onclick", null);
            this.html.setAttr("ondblclick", null);
        }

        public Row getRow() {
            return row;
        }

        @Override
        public Object getUserData() {
            return userData;
        }

        @Override
        public void setUserData(Object userData) {
            this.userData = userData;
        }

        public Column getColumn() {
            return col;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(final Object value) {
            final Object oldValue = this.value;
            this.value = value;
            if (editor != null) {
                editor.setValue(getRowIndex(row), col.getIndex(), value);
            } else {
                if (renderer != null) {
                    renderer.update(getRowIndex(row), col.getIndex(), value);
                }
            }
            if (!Objects.equals(oldValue, this.value)) {
                notifyCellValueChangeListeners(this, oldValue, this.value);
            }
        }
        private Color background = null;

        void setCurrent(boolean isCurrent) {
            if (getSelectionMode() == SelectionMode.ROW) {
                for (Cell cell : row.cells) {
                    cell.setSelected(isCurrent);
                }
                if (isCurrent){
                    row.html.addClass("grid-current-row");
                }else{
                    row.html.removeClass("grid-current-row");
                }
                
            } else if (getSelectionMode() == SelectionMode.CELL) {
                setSelected(isCurrent);
                row.html.removeClass("grid-current-row");
            }
            setupFrames(isCurrent ? this : null);
            if (isCurrent){
                html.addClass("grid-current-cell");
            }else{
                html.removeClass("grid-current-cell");
            }
        }

        public int getCellIndex() {
            return col.getIndex();
        }        

        public void setSelectedCellBackground(Color c) {
            if (c != null) {
                selectedBackground = c;
                html.setCss("background-color", color2Str(c));
            }
        }

        public Color getSelectedCellBackground() {
            return selectedBackground;
        }

        @Override
        public Color getBackground() {
            if (renderer!=null){
                if (renderer.getUI().getBackground()!=null)
                return renderer.getUI().getBackground();
            }
            return super.getBackground();
        }
        
        void setSelected(boolean isSelected) {
            if (this.isSelected != isSelected) {
                this.isSelected = isSelected;
                updateSelectionBackground();
                if (renderer!=null){
                    renderer.selectionChanged(getRowIndex(row), getColumn().getIndex(), getValue(), this, isSelected);
                }
            }
        }
        
        void updateSelectionBackground(){
            if (!getHtml().containsClass("editor-cell")){
                if (isSelected && getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)){
                    background = getBackground();
                    if (!getHtml().containsClass("rwt-ui-selected-item")){
                        getHtml().addClass("rwt-ui-selected-item");
                    }
                    final Color color = getSelectedCellBackground();
                    if (renderer != null) {
                        renderer.getUI().setBackground(color);
                    }
                    super.setBackground(color);
                }else{
                    getHtml().removeClass("rwt-ui-selected-item");
                    final Color rowColor = row==null ? null : row.getPrimaryBackgroundColor();
                    applyBackgroundColor(rowColor==null ? background : rowColor);
                }
            }
        }

        public boolean finishEdit(boolean applyChanges) {
            if (editor != null) {
                if (applyChanges) {
                    editor.applyChanges();
                } else {
                    editor.cancelChanges();
                }
                final Object oldValue = value;
                value = editor.getValue();

                renderer.update(getRowIndex(row), col.getIndex(), value);
                container.remove(editor.getUI().html);
                editor.getUI().setParent(null);
                container.add(renderer.getUI().html);
                renderer.getUI().setParent(this);
                registerEventListeners();
                editor = null;
                html.removeClass("editor-cell");
                if (getSelectionMode()==SelectionMode.CELL){
                    html.markAsChoosable();
                }else if (getSelectionMode()==SelectionMode.ROW){
                    getRow().getHtml().markAsChoosable();
                }
                if (!Objects.equals(oldValue, value)) {
                    notifyCellValueChangeListeners(this, oldValue, value);
                }
                return true;
            } else {
                return true;
            }
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            if (editor != null) {
                UIObject obj = editor.getUI().findObjectByHtmlId(id);
                if (obj != null) {
                    return obj;
                }
            }
            if (container.getId().equals(id)) {
                return this;
            }
            UIObject obj = super.findObjectByHtmlId(id);
            if (obj != null) {
                return obj;
            } else if (renderer != null) {
                return renderer.getUI().findObjectByHtmlId(id);
            }
            return null;
        }

        @Override
        public void visit(final Visitor visitor) {
            super.visit(visitor);
            if (editor != null) {
                editor.getUI().visit(visitor);
            } else {
                renderer.getUI().visit(visitor);
            }
        }

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_KEY_DOWN.equals(actionName)) {
                switch (actionParam) {
                    case "27":
                        //esc
                        if (editor != null && editor.getUI() != null) {
                            finishEdit(false);
                        }
                        break;
                    case "13"://Enter
                    case "113"://F2
                        if (editor != null && editor.getUI() != null) {
                            finishEdit(true);
                        } else {
                            tryToOpenEditor();
                        }
                        break;
                }
            } else if (Events.EVENT_NAME_ONDBLCLICK.equals(actionName) && isEnabled()) {
                Grid.this.notifyDoubleClick(row, this);
            } else {
                super.processAction(actionName, actionParam);
            }
        }

        @Override
        protected void processHtmlEvent(final HtmlEvent event) {
            if (event instanceof ClickHtmlEvent){
                final UIObject editorUI = editor==null ? null : editor.getUI();
                Grid.this.processCellClickEvent(this, (ClickHtmlEvent)event, editorUI, renderer.getUI());
            }else{
                super.processHtmlEvent(event);
            }
        }
               
        public boolean isEditable() {
            if (Grid.this.isEditable && Grid.this.editorProvider != null) {
                if (!row.isEditable || !col.isEditable()) {
                    return false;
                } else {
                    return isEditable;
                }
            } else {
                return false;
            }
        }
        
        public boolean isInEditingMode(){
            return getHtml().containsClass("editor-cell");
        }

        public void setEditable(boolean editable) {
            this.isEditable = editable;
        }

        public boolean getEditable() {
            return isEditable();
        }

        public boolean canEdit() {
            if (editor != null) {
                return true;
            } else {
                if (renderer == null) {
                    return false;
                } else {
                    if (renderer instanceof EditMaskRenderer) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean isEnabled() {
            if (super.isEnabled()) {
                return row.isEnabled();
            } else {
                return false;
            }
        }

        public final WpsIcon getIcon() {
            return icon;
        }

        public final void setIcon(WpsIcon icon) {
            if (!Objects.equals(icon, this.icon)) {
                this.icon = icon;
                renderer.update(getRowIndex(row), col.getIndex(), value);
            }
        }

        private boolean tryToOpenEditor() {
            if (isEnabled() && isEditable()) {

                CellEditor e = Grid.this.editorProvider.newCellEditor(getRowIndex(row), col.getIndex());

                if (e != null) {
                    e.setValue(getRowIndex(row), col.getIndex(), value);
                    UIObject object = e.getUI();
                    if (object != null) {
                        object.setParent(this);
                        this.renderer.getUI().setParent(null);
                        this.container.remove(renderer.getUI().html);
                        this.container.add(object.html);
                        this.editor = e;
                        removeEventListeners();
                        this.html.addClass("editor-cell");
                        if (getSelectionMode()==SelectionMode.CELL){
                            html.removeChoosableMarker();
                        }else if (getSelectionMode()==SelectionMode.ROW){
                            getRow().getHtml().removeChoosableMarker();
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void setBackground(final Color c) {
            if (html.containsClass("rwt-ui-selected-item")) {
                this.background = c;
            }else{
                final Color rowColor = row==null ? null : row.getPrimaryBackgroundColor();
                final Color actualColor;
                if (rowColor==null){
                    actualColor = c;
                }else{
                    this.background = c;
                    actualColor = rowColor;
                }
                applyBackgroundColor(actualColor);
            }
        }
        
        private void applyBackgroundColor(final Color color){
            if (renderer != null) {
                renderer.getUI().setBackground(color);
            }
            super.setBackground(color);
        }

        @Override
        public void setForeground(final Color c) {
            renderer.getUI().setForeground(c);
        }

        @Override
        protected ICssStyledItem getForegroundHolder() {
            if (renderer != null) {
                return renderer.getUI().getHtml();
            } else {
                return html;
            }
        }

        public final Object getEditorValue() {
            if (this.renderer instanceof EditMaskRenderer) {
                return ((EditMaskRenderer) renderer).getEditorValue();
            }
            return null;
        }

        @Override
        public void updateRenderer() {
            if (editor == null) {
                Column column = this.getColumn();
                int r = getRowIndex(this.row);
                int c = this.col.getIndex();

                IEditingOptions colOpts = column.getEditingOptions();
                IEditingOptions cellopts = this.getEditingOptions();
                EditMask cellmask = cellopts.getEditMask();
                EditMask colmask = colOpts.getEditMask();
                EditMask currMask = cellmask != null ? cellmask : colmask;
                boolean isMaskRenderer = this.renderer instanceof EditMaskRenderer;

                if (currMask == null && isMaskRenderer) {
                    this.setEditable(true);
                    changeRenderer(r, c);
                } else if (currMask != null && !isMaskRenderer) {
                    this.setEditable(false);
                    changeRenderer(r, c);
                } else if (currMask != null && isMaskRenderer) {
                    EditMaskRenderer editMaskRenderer = (EditMaskRenderer) renderer;
                    if (!currMask.getType().equals(editMaskRenderer.getEditMaskType())) {
                        changeRenderer(r, c);
                    }
                }
                Object val = getValue();
                this.renderer.update(r, c, val);
            }
        }

        private void changeRenderer(int r, int c) {
            this.renderer.getUI().setParent(null);
            this.container.remove(this.renderer.getUI().html);
            this.renderer = rendererProvider.newCellRenderer(r, c);
            this.renderer.getUI().setParent(this);
            this.container.add(this.renderer.getUI().html);

        }
    }
    
    private final AbstractHorizontalHeader<Column> horizontalHeader = new IGrid.AbstractHorizontalHeader<Column>() {
        @Override
        public Column createColumn(final AbstractColumnHeaderCell headerCell) {
            return new Column(headerCell);
        }
    };    
    private final VerticalHeader verticalHeader = new VerticalHeader();    
    private final Html outerContainer = new Div();//contains data and hHeader
    private final Data data = new Data();
    private final IGrid.RowFrame currentRowFrame;
    private final IGrid.CellFrame currentCellFrame;
    private final IGrid.ColumnsResizeController columnsResizeController = new IGrid.ColumnsResizeController(this);
    private final EnumSet<IGrid.ESelectionStyle> selectionStyle = EnumSet.of(IGrid.ESelectionStyle.BACKGROUND_COLOR,IGrid.ESelectionStyle.CELL_FRAME);
    private Cell selection = null;
    private boolean isEditable = true;
    private boolean isVerticalHeaderVisible;    
    private SelectionMode selectionMode = SelectionMode.CELL;
    private Color currentCellFrameColor = Color.decode("#404040");
    private Color currentRowFrameColor = Color.decode("#3399ff");    
    
    private final DefaultCurrentRowListener rowSelectionListener = new DefaultCurrentRowListener();
    private final DefaultCurrentCellListener cellSelectionListener = new DefaultCurrentCellListener();
    private List<DoubleClickListener> doubleClickListeners;
    private List<CellValueChangeListener> cellValueChangeListeners;
    private List<ColumnHeaderClickListener> headerClickListeners;
    
    private CellEditorProvider editorProvider = null;
    private CellRendererProvider rendererProvider = new CellRendererProvider() {
        @Override
        public CellRenderer newCellRenderer(int r, int c) {
            Column column = getColumn(c);
            Cell cell = getCell(r, c);
            EditMask cellmask = cell.getEditingOptions().getEditMask();
            EditMask colmask = column.getEditingOptions().getEditMask();
            EditMask mask = cellmask != null ? cellmask : (colmask != null ? colmask : null);
            if (mask != null) {
                return new EditMaskRenderer(mask, Grid.this, getEnvironment(), cell);
            } else {
                return new DefaultRenderer();
            }
        }
    };
    
    private String currentFilter;
    private final IGrid.FilterEditorController filterController;
    
    private final ValueEditor.ValueChangeListener<String> filterChangeListener = 
            new ValueEditor.ValueChangeListener<String>(){
                    @Override
                    public void onValueChanged(final String oldValue, final String newValue) {
                        applyFilter(newValue);
                    }        
            };
    

    private class DefaultCurrentRowListener implements CurrentRowListener {

        private final List<CurrentRowListener> listeners = new LinkedList<>();

        @Override
        public void currentRowChanged(Row oldRow, Row newRow) {
            List<CurrentRowListener> list;
            synchronized (listeners) {
                list = new ArrayList<>(listeners);
            }
            for (CurrentRowListener l : list) {
                l.currentRowChanged(oldRow, newRow);
            }
        }

        public void addSelectionListener(CurrentRowListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeSelectionListener(CurrentRowListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        @Override
        public boolean beforeChangeCurrentRow(Row oldRow, Row newRow) {
            List<CurrentRowListener> list;
            synchronized (listeners) {
                list = new ArrayList<>(listeners);
            }
            for (CurrentRowListener l : list) {
                if (!l.beforeChangeCurrentRow(oldRow, newRow)) {
                    return false;
                }
            }
            return true;
        }
    }

    private class DefaultCurrentCellListener implements CurrentCellListener {

        private final List<CurrentCellListener> listeners = new LinkedList<>();

        @Override
        public void currentCellChanged(final Cell oldCell, final Cell newCell) {
            final List<CurrentCellListener> list = new LinkedList<>(listeners);
            for (CurrentCellListener l : list) {
                l.currentCellChanged(oldCell, newCell);
            }
        }

        @Override
        public boolean beforeChangeCurrentCell(final Cell oldCell, final Cell newCell) {
            final List<CurrentCellListener> list = new LinkedList<>(listeners);
            for (CurrentCellListener l : list) {
                if (!l.beforeChangeCurrentCell(oldCell, newCell)) {
                    return false;
                }
            }
            return true;
        }

        public void addSelectionListener(final CurrentCellListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeSelectionListener(final CurrentCellListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }
    }
    
    public CellRendererProvider getRendererProvider() {
        return rendererProvider;
    }
        

    public CellEditorProvider getEditorProvider() {
        return editorProvider;
    }

    public Grid() {
        super(new Div());
        
        this.outerContainer.addClass("rwt-grid-outer-container");
        this.outerContainer.setCss("overflow", "none");
                        
        horizontalHeader.setParent(this);
        data.setParent(this);
        outerContainer.add(horizontalHeader.getHtml());
        outerContainer.add(data.getHtml());
        html.add(outerContainer);
        html.layout("$RWT.grid._layout");
        html.setCss("overflow", "hidden");
        html.setAttr("more-rows", "true");
        currentCellFrame = new IGrid.CellFrame(data.getHtml(),"cellFrame");
        currentCellFrame.setColor(currentCellFrameColor);
        currentRowFrame = new IGrid.RowFrame(data.getHtml(),"rowFrame");
        currentRowFrame.setColor(currentRowFrameColor);
        setBorderVisible(true);
        subscribeToKeyEvents();
        filterController = new FilterEditorController(getHtml(), filterChangeListener);
    }
    
    private void subscribeToKeyEvents(){
        final EKeyEvent keyCodes[] = 
            new EKeyEvent[]{EKeyEvent.VK_UP, EKeyEvent.VK_DOWN, EKeyEvent.VK_LEFT, EKeyEvent.VK_RIGHT, 
                      EKeyEvent.VK_TAB, EKeyEvent.VK_HOME, EKeyEvent.VK_END, EKeyEvent.VK_RETURN};
        for (EKeyEvent keyCode: keyCodes){
            subscribeToEvent(new KeyDownEventFilter(keyCode, EKeyboardModifier.ANY));
        }
    }

    @Deprecated
    public String getPersistentId() {
        return getPersistenceKey();
    }

    public Row getCurrentRow() {
        return selection == null ? null : selection.row;
    }

    protected void updateColumnsVisibility(List<ColumnDescriptor> visibleColumns) {
        if (visibleColumns == null) {//default behaviour
            boolean visible[] = new boolean[horizontalHeader.getColumnsCount()];
            int i = 0;
            for (Column c : horizontalHeader) {
                visible[i] = c.isVisible();
                i++;
            }
            for (Row r : verticalHeader) {
                if (!r.spannedCells()){
                    for (i = 0; i < visible.length; i++) {
                        r.getCell(i).setVisible(visible[i]);
                    }
                }
            }
        }
        if (selection!=null && getSelectionStyle().contains(IGrid.ESelectionStyle.CELL_FRAME)){
            setupFrames(selection);
        }
    }

    public boolean isHeaderVisible() {
        return horizontalHeader.isVisible();        
    }

    public void setHeaderVisible(boolean visible) {
        horizontalHeader.setVisible(visible);
    }

    public boolean getHeaderVisible() {
        return isHeaderVisible();
    }

    protected List<ColumnDescriptor> getAllColumnDescriptors() {
        return null;
    }

    protected List<ColumnDescriptor> getVisibleColumnDescriptors(List<ColumnDescriptor> all) {
        return null;
    }
    
    protected boolean showRestoreDefaultColumnSettingsButton(){
        return false;
    }
    
    protected void restoreDefaultColumnSettings(){
        
    }

    private void setupColumnsVisiblity() {
        final List<ColumnDescriptor> allColumns = getAllColumnDescriptors();
        if (allColumns==null){
            return;
        }
        final List<ColumnDescriptor> visibleColumns = getVisibleColumnDescriptors(allColumns);
        if (visibleColumns==null){
            return;
        }
        final IGrid.SetupColumnVisibilityDialog dialog = 
            new IGrid.SetupColumnVisibilityDialog(getEnvironment(), allColumns,  visibleColumns, false, showRestoreDefaultColumnSettingsButton());
        if (dialog.execDialog(this)==DialogResult.ACCEPTED){
            updateColumnsVisibility(dialog.getSelectedColumns());
            updateColumnsResizingMode();
            updateConfigStr();
        }else if (dialog.needToRestoreDefaultSettings()){
            restoreDefaultColumnSettings();
            updateColumnsResizingMode();
            updateConfigStr();            
        }
    }

    public Cell getCurrentCell() {
        return selection;
    }

    public void shadeEvenRow(final int opacityPercent) {
        data.shadeEvenRow(opacityPercent);
    }        

    public void setShadeColor(final Color color) {
        data.setShadeColor(color);
    }

    public Color getShadeColor() {
        return data.getShadeColor();
    }

    @Override
    public void removeColumn(int index) {
        final Row selectedRow;
        final int selectedCell;
        if (selection != null
                && !selection.getRow().spannedCells()) {
            selectedRow = selection.getRow();
            selectedCell = selection.getColumn().getIndex();
            if (selectedCell == index) {
                if (index + 1 < selectedRow.getCellCount()) {
                    setCurrentCell(selectedRow.getCell(index + 1));
                } else {
                    setCurrentCell(selectedRow.getCell(index - 1));
                }
            }
        } else {
            selectedRow = null;
            selectedCell = -1;
        }
        this.horizontalHeader.removeColumn(index);
        final int columnsCount = getColumnCount();
        for (Row row : verticalHeader) {
            if (row.spannedCells()) {
                Cell cell = row.getCell(0);
                cell.getHtml().setAttr("colSpan", columnsCount);
            } else {
                Cell cell = row.cells.remove(index);
                row.html.remove(cell.html);
                cell.setParent(null);
            }
        }
        if (selectedRow != null && Math.abs(index - selectedCell) < 2 && selection != null) {
            //update borders
            setupFrames(selection);
        }
        updateColumnsResizingMode();
    }

    @Override
    public Column addColumn(final String title) {
        return addColumn(-1, title, null);
    }
    
    @Override
    public Column addColumn(final String title, final AbstractColumnHeaderCell columnHeaderCell) {
        return addColumn(-1, title, columnHeaderCell);
    }    
    
    @Override
    public Column addColumn(final int index, final String title){
        return addColumn(index, title, null);
    }
    
    @Override
    public Column addColumn(final int index, final String title, final AbstractColumnHeaderCell columnHeaderCell) {
        final int newColumnIndex = index < 0 ? getColumnCount() : index;
        final boolean updateCurrentCellBorder = selection != null
                && (selection.getRow().spannedCells() || Math.abs(newColumnIndex - selection.getColumn().getIndex()) < 2);
        if (updateCurrentCellBorder) {
            setupFrames(null);
        }
        Column c = horizontalHeader.addColumn(index, title, columnHeaderCell);
        if (verticalHeader.getRowsCount()>0) {
            int columnIndex = horizontalHeader.getColumnIndex(c);
            int columnCount = getColumnCount();
            final Row selectedRow;
            if (selectionMode == SelectionMode.ROW && selection != null) {
                selectedRow = selection.row;
            } else {
                selectedRow = null;
            }
            for (Row r : verticalHeader) {
                if (r.spannedCells()) {
                    Cell cell = r.getCell(0);
                    cell.getHtml().setAttr("colSpan", columnCount);
                } else {
                    Cell cell = r.addCell(c, columnIndex);
                    if (columnIndex == columnCount - 1 && columnCount > 1) {
                        Cell oldLastCell = r.getCell(columnCount - 2);
                        oldLastCell.container.removeClass("rwt-grid-data-last-col");
                        oldLastCell.container.addClass("rwt-grid-data-col");
                        cell.container.addClass("rwt-grid-data-last-col");
                    }
                    if (r == selectedRow && getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)) {
                        cell.setSelected(true);
                        if (cell.renderer != null) {
                            cell.renderer.rowSelectionChanged(true);
                        }
                    }
                    if (selectionMode == SelectionMode.CELL) {
                        cell.getHtml().markAsChoosable();
                    }
                }
            }
            if (updateCurrentCellBorder && selection!=null) {
                setupFrames(selection);
            }
        }
        html.layout("$RWT.grid._layout");
        updateColumnsResizingMode();
        return c;
    }

    public void clearRows() {
        verticalHeader.removeAllRows();
    }

    @Override
    public Column getColumn(int index) {
        return horizontalHeader.getColumn(index);
    }

    @Override
    public int getColumnCount() {
        return horizontalHeader.getColumnsCount();
    }

    public Row addRow() {
        return verticalHeader.addRow(-1, "",null);
    }
    
    public Row addRow(final String title){
        return verticalHeader.addRow(-1, title, null);
    }
    
    public Row addRow(final String title, final AbstractRowHeaderCell rowHeaderCell){
        return verticalHeader.addRow(-1, title, rowHeaderCell);
    }    

    public Row insertRow(final int index) {
        return verticalHeader.addRow(index, "", null);
    }
    
    public Row insertRow(final int index, final String title) {
        return verticalHeader.addRow(index, title, null);
    }
    
    public Row insertRow(final int index, final String title, final AbstractRowHeaderCell rowHeaderCell) {
        return verticalHeader.addRow(index, title, rowHeaderCell);
    }

    public void swapRows(final int index1, final int index2) {
        this.verticalHeader.swapRows(index1, index2);
    }

    public Row addRowWithSpannedCells() {
        return verticalHeader.addRowWithSpannedCells("",null);
    }
    
    public Row addRowWithSpannedCells(final String title) {
        return verticalHeader.addRowWithSpannedCells(title,null);
    }
    
    public Row addRowWithSpannedCells(final String title, final AbstractRowHeaderCell factory) {
        return verticalHeader.addRowWithSpannedCells(title,factory);
    }    
    

    public Row getRow(int index) {
        return verticalHeader.getRow(index);
    }

    public int getRowIndex(Row row) {
        return verticalHeader.getRowIndex(row);
    }

    public void removeRow(Row row) {
        verticalHeader.removeRow(row);
        if (selection!=null && selection.row==row){
            setCurrentCell(null);
        }
    }

    public int getRowCount() {
        return verticalHeader.getRowsCount();
    }

    @Override
    public void setWidth(int w) {
        super.setWidth(w);
        html.layout("$RWT.grid._layout");
    }

    public void setAdjustWidth(boolean isAdjustWidth) {
        data.getHtml().setAttr("isAdjustWidth", isAdjustWidth);
    }
    
    public void setAdjustHeight(boolean isAdjustHeight) {
        data.getHtml().setAttr("isAdjustHeight", isAdjustHeight);
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/client.js", "org/radixware/wps/rwt/grid.js"};
    }

    @Override
    protected String[] clientCssRequired() {
        return new String[]{"org/radixware/wps/rwt/grid.css"};
    }

    public void setCurrentCell(final Cell cell) {
        final Row oldRow = selection == null ? null : selection.row;
        final Row newRow = cell == null ? null : cell.row;
        if (!cellSelectionListener.beforeChangeCurrentCell(selection, cell)) {
            return;
        }
        if (oldRow != newRow && !rowSelectionListener.beforeChangeCurrentRow(oldRow, newRow)) {
            return;
        }

        if (selection != null) {
            selection.setCurrent(false);
        }

        final Cell old = selection;
        selection = cell;

        if (cell != null) {
            cell.setCurrent(true);
        }

        if (oldRow != newRow && getSelectionMode() == SelectionMode.ROW) {
            if (oldRow != null) {
                for (Cell orc : oldRow.cells) {
                    if (orc.renderer != null) {
                        orc.renderer.rowSelectionChanged(false);
                    }
                }
            }
            if (newRow != null) {
                for (Cell orc : newRow.cells) {
                    if (orc.renderer != null) {
                        orc.renderer.rowSelectionChanged(true);
                    }
                }
            }
        }
        cellSelectionListener.currentCellChanged(old, selection);
        if (oldRow != newRow) {
            rowSelectionListener.currentRowChanged(old == null ? null : old.row, selection == null ? null : selection.row);
        }
        if (selection != null) {
            selection.setFocused(true);
        }
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    @SuppressWarnings("fallthrough")
    public void setSelectionMode(final SelectionMode newMode) {
        if (newMode != null && selectionMode != newMode) {
            switch (selectionMode) {
                case ROW: {
                    updateRowSelectionMode(newMode);
                    for (Row row : verticalHeader) {
                        row.getHtml().removeChoosableMarker();
                        if (newMode == SelectionMode.CELL) {
                            for (Cell cell : row.cells) {
                                cell.getHtml().markAsChoosable();
                            }
                        }
                    }
                }
                break;
                case CELL: {
                    updateRowSelectionMode(newMode);
                    for (Row row : verticalHeader) {
                        if (newMode == SelectionMode.ROW) {
                            row.getHtml().markAsChoosable();
                        }
                        for (Cell cell : row.cells) {
                            cell.getHtml().removeChoosableMarker();
                        }
                    }
                }
                default: {
                    setSelectionModeImpl(newMode);
                }
            }
            selectionMode = newMode;
        }
    }
    
    private void setSelectionModeImpl(final SelectionMode mode){
        if (mode == SelectionMode.ROW) {
            if (selection != null && getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)) {
                for (Cell cell : selection.row.cells) {
                    cell.setSelected(true);
                    if (cell.renderer != null) {
                        cell.renderer.rowSelectionChanged(true);
                    }
                }
            }
            for (Row row : verticalHeader) {
                row.getHtml().markAsChoosable();
            }
        } else if (mode == SelectionMode.CELL){//CELL
            if (selection != null && getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)) {
                selection.setSelected(true);
            }
            for (Row row : verticalHeader) {
                for (Cell cell : row.cells) {
                    cell.getHtml().markAsChoosable();
                }
            }
        }
    }

    private void updateRowSelectionMode(final SelectionMode mode) {
        if (selection != null && getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)) {
            final Row row = selection.row;
            for (Cell cell : row.cells) {
                if (cell != selection) {
                    cell.setSelected(mode == SelectionMode.ROW);
                    if (cell.renderer != null) {
                        cell.renderer.rowSelectionChanged(mode == SelectionMode.ROW);
                    }
                } else if (mode == SelectionMode.NONE) {
                    cell.setSelected(false);
                    if (cell.renderer != null) {
                        cell.renderer.rowSelectionChanged(false);
                    }
                }
            }
        }
    }

    @Override
    public final Color getCurrentCellFrameColor() {
        return currentCellFrameColor;
    }

    @Override
    public final void setCurrentCellFrameColor(final Color color) {
        if (color!=null && !Objects.equals(currentCellFrameColor, color)) {
            currentCellFrameColor = color;
            currentCellFrame.setColor(color);
        }
    }
    
    public final Color getCurrentRowFrameColor(){
        return currentRowFrameColor;
    }
    
    public final void setCurrentRowFrameColor(final Color color){
        if (color!=null && !Objects.equals(currentRowFrameColor, color)){
            currentRowFrameColor = color;
            currentRowFrame.setColor(color);
        }
    }
    
    @Override
    public EnumSet<IGrid.ESelectionStyle> getSelectionStyle(){
        return selectionStyle.clone();
    }
    
    @Override
    public void setSelectionStyle(final EnumSet<IGrid.ESelectionStyle> newStyle){
        if (newStyle!=null && !selectionStyle.equals(newStyle)){
            final boolean enablingBackgroundColor = 
                !selectionStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR) && newStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR);
            final boolean disablingBackgroundColor = 
                selectionStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR) && !newStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR);
            selectionStyle.clear();
            selectionStyle.addAll(newStyle);
            if (selection!=null){
                if (enablingBackgroundColor){
                    setSelectionModeImpl(getSelectionMode());                    
                }
                if (disablingBackgroundColor){
                    final Row row = selection.getRow();
                    for (Cell cell : row.cells) {
                        cell.setSelected(false);
                    }
                }
                if (enablingBackgroundColor || disablingBackgroundColor){
                    if (getSelectionMode()==SelectionMode.ROW){
                        for (int i=selection.getRow().getCellCount()-1; i>=0; i--){
                            selection.getRow().getCell(i).updateSelectionBackground();
                        }                        
                    }else if (getSelectionMode()==SelectionMode.CELL){
                        selection.updateSelectionBackground();
                    }
                }
                setupFrames(selection);
            }
        }
    }

    private void setupFrames(final Cell currentCell){
        currentCellFrame.hide();
        currentRowFrame.hide();        
        if (currentCell!=null){
            if (getSelectionStyle().contains(IGrid.ESelectionStyle.ROW_FRAME)){
                currentRowFrame.setRow(currentCell.getRow());
            }
            if (getSelectionStyle().contains(IGrid.ESelectionStyle.CELL_FRAME)){
                int visibleCellIndex = currentCell.getCellIndex();
                for (int i=0,count=getColumnCount(),index=currentCell.getCellIndex();i<index && i<count;i++){
                    if (!getColumn(i).isVisible()){
                        visibleCellIndex--;
                    }
                }
                currentCellFrame.setCell(currentCell.getRow(), visibleCellIndex);
            }
        }
    }
    
    @Override
    public void visit(final Visitor visitor) {
        super.visit(visitor);
        horizontalHeader.visit(visitor);
        verticalHeader.visit(visitor);
        data.visit(visitor);
    }

    @Override
    public UIObject findObjectByHtmlId(final String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        result = horizontalHeader.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        result = verticalHeader.findObjectByHtmlId(id);
        if (result != null ){
            return result;
        }
        result = data.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }        
        return null;
    }

    public void removeCurrentRowListener(final CurrentRowListener l) {
        rowSelectionListener.removeSelectionListener(l);
    }

    public void addCurrentRowListener(final CurrentRowListener l) {
        rowSelectionListener.addSelectionListener(l);
    }

    public void removeCurrentCellListener(final CurrentCellListener l) {
        cellSelectionListener.removeSelectionListener(l);
    }

    public void addCurrentCellListener(final CurrentCellListener l) {
        cellSelectionListener.addSelectionListener(l);
    }

    public void removeDoubleClickListener(DoubleClickListener l) {
        if (doubleClickListeners != null) {
            doubleClickListeners.remove(l);
        }
    }

    public void addDoubleClickListener(DoubleClickListener l) {
        if (doubleClickListeners == null && l != null) {
            doubleClickListeners = new LinkedList<>();
        }
        if (!doubleClickListeners.contains(l) && l != null) {
            doubleClickListeners.add(l);
        }
    }

    private void notifyDoubleClick(final Row row, final Cell cell) {
        if (doubleClickListeners != null) {
            List<DoubleClickListener> list;
            list = new LinkedList<>(doubleClickListeners);
            for (DoubleClickListener listener : list) {
                listener.onDoubleClick(row, cell);
            }
        }
    }

    public void addColumnHeaderClickListener(final ColumnHeaderClickListener l) {
        if (l != null) {
            if (headerClickListeners == null) {
                headerClickListeners = new LinkedList<>();
            }
            if (!headerClickListeners.contains(l)) {
                headerClickListeners.add(l);
            }
        }
    }
    
    public void addRowHeaderClickListener(final RowHeaderClickListener l){
        verticalHeader.addClickListener(l);
    }
    
    public void removeRowHeaderClickListener(final RowHeaderClickListener l){
        verticalHeader.removeClickListener(l);
    }
    
    public void addRowHeaderDoubleClickListener(final RowHeaderDoubleClickListener l){
        verticalHeader.addDoubleClickListener(l);
    }
    
    public void removeRowHeaderDoubleClickListener(final RowHeaderDoubleClickListener l){
        verticalHeader.removeDoubleClickListener(l);
    }

    public void removeColumnHeaderClickListener(final ColumnHeaderClickListener l) {
        if (headerClickListeners != null && l != null) {
            headerClickListeners.remove(l);
        }
    }

    private void notifyColumnHeaderClick(final Column column, final EnumSet<EKeyboardModifier> keyboardModifiers) {
        if (headerClickListeners != null) {
            List<ColumnHeaderClickListener> list;
            list = new LinkedList<>(headerClickListeners);
            for (ColumnHeaderClickListener listener : list) {
                listener.onClick(column, keyboardModifiers.clone());
            }
        }
    }

    public void removeCellValueChangeListener(final CellValueChangeListener l) {
        if (cellValueChangeListeners != null) {
            cellValueChangeListeners.remove(l);
        }
    }

    public void addCellValueChangeListener(final CellValueChangeListener l) {
        if (l != null) {
            if (cellValueChangeListeners == null) {
                cellValueChangeListeners = new LinkedList<>();
            }
            if (!cellValueChangeListeners.contains(l)) {
                cellValueChangeListeners.add(l);
            }
        }
    }

    private void notifyCellValueChangeListeners(final Cell cell, final Object oldValue, final Object newValue) {
        if (cellValueChangeListeners != null) {
            final List<CellValueChangeListener> list = new LinkedList<>(cellValueChangeListeners);
            for (CellValueChangeListener listener : list) {
                listener.onValueChanged(cell, oldValue, newValue);
            }
        }
    }

    protected boolean hasMoreData() {
        return false;
    }

    @Override
    protected Runnable componentRendered(HttpQuery query) {
        final Runnable innerRun = super.componentRendered(query);

        return new Runnable() {
            @Override
            public void run() {
                if (innerRun != null) {
                    innerRun.run();
                }
                checkHasMoreData();
            }
        };
    }

    protected void checkHasMoreData() {
        if (hasMoreData()) {
            html.setAttr("more-rows", "true");
        } else {
            html.setAttr("more-rows", null);
        }
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("config".equals(actionName)) {
            applyConfigStr(actionParam);
            updateConfigStr();
        } else if ("filter".equals(actionName)){
            applyFilter(actionParam);
        }else{
            super.processAction(actionName, actionParam);
        }
    }

    public void setEditorProvider(CellEditorProvider editorProvider) {
        this.editorProvider = editorProvider;
    }

    public Cell getCell(final int row, final int column) {
        if (row >= getRowCount() || row < 0 || column >= getColumnCount() || column < 0) {
            return null;
        }
        final Row r = verticalHeader.getRow(row);
        Cell cell = r.getCell(column);
        return cell;
    }

    public void setRendererProvider(final CellRendererProvider rendererProvider) {
        this.rendererProvider = rendererProvider;
        for (Row r : verticalHeader) {
            for (Cell cell : r.cells) {
                cell.updateRenderer();
            }
        }
    }

    public boolean isStickToRight() {
        return "1".equals(html.getAttr("stick"));
    }

    public boolean getStickToRight() {
        return isStickToRight();
    }

    public void setStickToRight(boolean stick) {
        if (stick!=isStickToRight()){
            if (stick) {
                html.setAttr("stick", "1");
            } else {
                html.setAttr("stick", null);
            }
            updateColumnsResizingMode();
        }        
    }

    private void updateConfigStr() {
        String configStr = getConfigStr();
        if (getPersistentId() != null) {
            html.setAttr("config", configStr);
        }
    }

    protected String getConfigStr() {
        return null;
    }

    protected void applyConfigStr(final String config) {
    }

    @Override
    public String getHeaderSettings() {
        return horizontalHeader.getSettings();
    }

    @Override
    public void setHeaderSettings(final String settings) {
        horizontalHeader.setSettings(settings);
    }

    public static class EditMaskRenderer implements CellRenderer {

        private EditMask editMask;
        private IValEditor editor;
        private final IClientEnvironment e;
        private final Grid grid;
        private ECellEditingMode editMode;
        private final Cell editingCell;

        @Override
        @SuppressWarnings("unchecked")
        public void update(final int r, final int c, final Object value) {
            if (editor == null) {
                return;
            }
            Cell cell = grid.getCell(r, c);
            Column col = grid.getColumn(c);
            IEditingOptions cellOpts = cell.getEditingOptions();
            IEditingOptions colOpts = col.getEditingOptions();

            EditMask cellMask = cellOpts.getEditMask();
            EditMask colMask = colOpts.getEditMask();
            ECellEditingMode cellMode = cellOpts.getEditingMode();
            ECellEditingMode colMode = colOpts.getEditingMode();
            if (cellMask != null) {
                editMask = cellMask;

            } else  if (colMask !=null) {
                editMask = colMask;
            }

            final ValEditorController controller = editor.getController();
            controller.setEditMask(editMask);
            editMode = cellMode != null ? cellMode : colMode;
            setModeOpts(editMode, controller);
            editor.setValue(value);
        }

        private void setModeOpts(ECellEditingMode mode, ValEditorController controller) {
            switch (mode) {
                case NULL_VALUE_ACCEPTED:
                    controller.setReadOnly(false);
                    controller.setMandatory(false);
                    break;
                case NULL_VALUE_RESTRICTED:
                    controller.setReadOnly(false);
                    controller.setMandatory(true);
                    break;
                case READ_ONLY:
                    controller.setReadOnly(true);
                    break;
            }
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
        }

        @Override
        public void selectionChanged(int r, int c, Object value, IGrid.ICell cell, boolean isSelected) {
        }

        @Override
        @SuppressWarnings("unchecked")
        public UIObject getUI() {
            if (editor == null) {
                this.editor = createValEditor();

                UIObject uio = (UIObject) editor;
                uio.html.addClass("renderer");
                uio.setHeight(21);

                editor.addValueChangeListener(new ValueEditor.ValueChangeListener() {
                    @Override
                    public void onValueChanged(Object oldValue, Object newValue) {
                        if (editingCell != null) {
                            editingCell.setValue(newValue);
                        }
                    }
                });
            } else {
                final UIObject parent = ((UIObject) editor).getParent();
                if (parent != null) {
                    parent.html.addClass("editor-cell");
                }
            }

            return (UIObject) editor;
        }
        
        protected IValEditor createValEditor(){
            return ValEditorFactory.getDefault().createValEditor(null, editMask, e);
        }

        public EEditMaskType getEditMaskType() {
            return editMask.getType();
        }

        public EditMaskRenderer(EditMask editMask, Grid grid, IClientEnvironment env, Cell cell) {
            e = env;
            this.grid = grid;
            this.editMask = editMask;
            this.editingCell = cell;
        }

        public EditMaskRenderer(EditMask editMask, Grid grid, IClientEnvironment env) {
            e = env;
            this.grid = grid;
            this.editMask = editMask;
            editingCell = null;
        }

        public Object getEditorValue() {
            if (editor != null) {
                return editor.getValue();
            }
            return null;
        }
    }
    
    @Override
    public final void setBorderVisible(final boolean showBorder) {
        data.setBorderVisible(showBorder);
    }

    @Override
    public boolean isBorderVisible() {
        return data.isBorderVisible();
    }

    @Override
    public void setBrowserFocusFrameEnabled(final boolean enabled){
        data.setBrowserFocusFrameEnabled(enabled);
    }
    
    @Override
    public boolean isBrowserFocusFrameEnabled(){
        return data.isBrowserFocusFrameEnabled();
    }
    
    public CornerHeaderCell getCornerHeaderCell(){
        return verticalHeader.getCornerCell();
    }
    
    public void setCornerHeaderCell(final CornerHeaderCell cell){
        verticalHeader.setCornerCell(cell);
    }
    
    public final void setRowHeaderVisible(final boolean isVisible){
        if (isVisible!=isVerticalHeaderVisible){
            isVerticalHeaderVisible = isVisible;
            updateVerticalHeaderVisibility();
        }
    }
    
    public final boolean isRowHeaderVisible(){
        return isVerticalHeaderVisible;
    }   
    
    private void updateVerticalHeaderVisibility(){
        final boolean currentVisibility = getHtml().getChildAt(0)==verticalHeader.getHtml();
        final boolean vHeaderActualVisibility = isVerticalHeaderVisible && getRowCount()>0;
        if (currentVisibility!=vHeaderActualVisibility){
            if (vHeaderActualVisibility){
                verticalHeader.getHtml().renew();
                getHtml().add(0, verticalHeader.getHtml());
            }else{
                getHtml().remove(verticalHeader.getHtml());
            }            
        }
    }
    
    protected void processCellClickEvent(final Cell cell,
                                         final ClickHtmlEvent event,
                                         final UIObject cellEditor,
                                         final UIObject rendererUi
                                         ){
        final Cell currentCell = getCurrentCell();
        if (currentCell == cell) {
            if (cellEditor != null) {
                cellEditor.processAction(Events.EVENT_NAME_ONCLICK, "");
            } else {
                if (!cell.tryToOpenEditor()
                        && rendererUi != null
                        && cell.getHtml().containsClass("editor-cell")) {
                    rendererUi.processAction(Events.EVENT_NAME_ONCLICK, "");
                }
            }
        } else {
            if (currentCell == null || currentCell.finishEdit(true)) {
                setCurrentCell(cell);
            }
        }        
    }

    @Override
    protected void processHtmlEvent(final HtmlEvent event) {
        if (event instanceof KeyDownHtmlEvent){
            processKeyDownHtmlEvent((KeyDownHtmlEvent)event);
        }else{
            super.processHtmlEvent(event);
        }
    }    
    
    protected boolean processKeyDownHtmlEvent(final KeyDownHtmlEvent event){
        final Cell currentCell = getCurrentCell();
        if (currentCell==null){
            return false;
        }
        Cell newCurrentCell = null;
        switch (EKeyEvent.getForValue(Long.valueOf(event.getButton()))){
            case VK_TAB:{                
                currentCell.processAction(RESIZE_ACTION_NAME, RESIZE_ACTION_NAME);
                if (event.getKeyboardModifiers().contains(EKeyboardModifier.SHIFT)){//reverse tabulation
                    newCurrentCell = getPrevVisibleCell(currentCell);
                }else{
                    newCurrentCell = getNextVisibleCell(currentCell);
                }
                break;
            }case VK_LEFT:{                
                newCurrentCell = currentCell.isInEditingMode() ? null : getPrevVisibleCell(currentCell);
                break;
            }case VK_RIGHT:{
                newCurrentCell = currentCell.isInEditingMode() ? null : getNextVisibleCell(currentCell);
                break;
            }case VK_UP:{
                return moveCursorUp();
            }case VK_DOWN:{
                return moveCursorDown();
            }case VK_HOME:{
                newCurrentCell = currentCell.isInEditingMode() ? null : getFirstVisibleCell(currentCell.getRow());
                break;
            }case VK_END:{
                newCurrentCell = currentCell.isInEditingMode() ? null : getLastVisibleCell(currentCell.getRow());
                break;
            }
            case VK_RETURN:{
                return !currentCell.isInEditingMode() && currentCell.tryToOpenEditor();
            }
        }
        return changeCurrentCell(currentCell, newCurrentCell);
    }
    
    protected final boolean moveCursorUp(){
        final Cell currentCell = getCurrentCell();
        if (currentCell!=null){
            final Row prevRow = currentCell.isInEditingMode() ? null : getPrevVisibleRow(currentCell.getRow());
            if (prevRow!=null){
                return changeCurrentCell(currentCell, prevRow.getCell(prevRow.spannedCells() ? 0 : currentCell.getCellIndex()) );                
            }
        }
        return false;
    }
    
    protected final boolean moveCursorDown(){
        final Cell currentCell = getCurrentCell();
        if (currentCell!=null){
            final Row nextRow = currentCell.isInEditingMode() ? null : getNextVisibleRow(currentCell.getRow());
            if (nextRow!=null){
                return changeCurrentCell(currentCell, nextRow.getCell(nextRow.spannedCells() ? 0 : currentCell.getCellIndex()) );                
            }
        }
        return false;
    }    
    
    private boolean changeCurrentCell(final Cell currentCell, final Cell newCurrentCell){
        if (newCurrentCell==null || currentCell==null){
            return false;
        }else{
            currentCell.finishEdit(true);
            setCurrentCell(newCurrentCell);
            if (getCurrentCell()!=null){
                getCurrentCell().setFocused(true);
            }
            return true;
        }
    }
    
    protected final Cell getNextVisibleCell(final Cell cell){
        if (cell!=null){
            final int colIndex = cell.getCellIndex();
            final Row currentRow = cell.getRow();
            if (!currentRow.spannedCells()){
                for (int i=colIndex+1; i<getColumnCount(); i++){
                    if (getColumn(i).isVisible()){
                        return currentRow.getCell(i);
                    }
                }
                return getFirstVisibleCell(getNextVisibleRow(currentRow));
            }
        }
        return null;
    }
    
    protected final Row getNextVisibleRow(final Row row){
        if (row!=null){
            final int rowIndex = getRowIndex(row);
            for (int r=rowIndex+1; r<getRowCount(); r++){
                final Row nextRow = getRow(r);
                if (nextRow.isVisible()){
                    return nextRow;
                }
            }
        }
        return null;   
    }        
    
    protected final Cell getFirstVisibleCell(final Row row){
        if (row!=null){
            if (row.spannedCells()){
                return row.getCell(0);
            }
            for (int c=0; c<getColumnCount(); c++){
                if (getColumn(c).isVisible()){
                    return row.getCell(c);
                }
            }
        }
        return null;
    }
    
    protected final Cell getLastVisibleCell(final Row row){
        if (row!=null){
            if (row.spannedCells()){
                return row.getCell(0);
            }            
            for (int c=getColumnCount()-1; c>=0; c--){
                if (getColumn(c).isVisible()){
                    return row.getCell(c);
                }
            }
        }
        return null;
    }
    
    protected final Cell getPrevVisibleCell(final Cell cell){
        if (cell!=null){
            final int colIndex = cell.getCellIndex();
            final Row currentRow = cell.getRow();
            if (!currentRow.spannedCells()){
                for (int i=colIndex-1; i>=0; i--){
                    if (getColumn(i).isVisible()){
                        return currentRow.getCell(i);
                    }
                }
                return getLastVisibleCell(getPrevVisibleRow(currentRow));
            }
        }
        return null;        
    }
    
    protected final Row getPrevVisibleRow(final Row row){
        if (row!=null){
            final int rowIndex = getRowIndex(row);
            for (int r=rowIndex-1; r>=0; r--){
                final Row prevRow = getRow(r);
                if (prevRow.isVisible()){
                    return prevRow;
                }
            }
        }
        return null;
    }
        
    @Override
    public final void setFilterEditor(final ValStrEditorController editor){
        filterController.setFilterEditor(editor);
    }
    
    @Override
    public final ValStrEditorController getFilterEditor(){
        return filterController.getFilterEditor();
    }    
    
    public final void addFilterListener(final IGrid.FilterListener listener){        
        filterController.addFilterListener(listener);
    }
    
    public final void removeFilterListener(final IGrid.FilterListener listener){
        filterController.removeFilterListener(listener);
    }
    
    private void notifyFilterListeners(final String filter){
        filterController.notifyFilterListeners(filter);
    }    
    
    private void applyFilter(final String filter){
        if (!Objects.equals(currentFilter, filter)){
            currentFilter = filter;
            applyCurrentFilter();
        }
    }
    
    @Override
    public void applyCurrentFilter(){
        for (int r=0,count=getRowCount(); r<count; r++){
            getRow(r).applyFilter();
        }            
        notifyFilterListeners(currentFilter);
    }
}
