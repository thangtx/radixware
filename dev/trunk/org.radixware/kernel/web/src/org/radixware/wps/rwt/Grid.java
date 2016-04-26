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
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.enums.EMouseButton;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.wps.rwt.IGrid.CellRenderer;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;

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

    public class Column extends UIObject implements IColumn {

        private Html cell = new Div();
        private Html title = new Html("label");
        private Html separator = new Html("span");
        private Html srtDirectionIndicator;
        private Html srtSequenceIndicator;
        private boolean isEditable = true;
        private EColumnSizePolicy policy;
        private Object userData;
        private final IEditingOptions editOpts = new EditingOptions(this);
        private WpsIcon icon;
        private Div imageContainer;

        @Deprecated
        public void setPersistentId(String id) {
            setPersistenceKey(id);
        }

        @Override
        public IEditingOptions getEditingOptions() {
            return editOpts;
        }

        public Column() {
            super(new Html("td"));
            this.html.add(cell);
            this.html.setCss("padding", "0px");
            this.html.setCss("border-top", "none");

            this.html.addClass("rwt-grid-row-cell");
            this.cell.addClass("header-cell");

            this.cell.setCss("overflow", "hidden");
            this.cell.setCss("padding-top", "3px");
            this.cell.setCss("vertical-align", "middle");
            this.cell.setCss("width", "100%");

            this.cell.add(title);
            this.title.addClass("rwt-ui-element");
            this.title.setCss("white-space", "nowrap");
            this.title.setCss("cursor", "pointer");

            this.cell.add(separator);
            separator.setCss("width", "1px");
            separator.setCss("height", "100%");
            separator.setCss("float", "right");
            separator.addClass("header-handle");
            html.setAttr("onclick", "$RWT.grid.onHeaderClick");
            html.setAttr("oncontextmenu", "$RWT.grid.onContextMenu");
            setSizePolicyImpl(EColumnSizePolicy.INTERACTIVE);            
        }

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                if (actionParam == null || actionParam.isEmpty()) {
                    processClickEvent(0);
                } else {
                    final int buttonsMask;
                    try {
                        buttonsMask = Integer.parseInt(actionParam);
                    } catch (NumberFormatException exception) {
                        processClickEvent(0);
                        return;
                    }
                    processClickEvent(buttonsMask);
                }
            } else {
                super.processAction(actionName, actionParam);
            }
        }

        @Override
        public void setIcon(final WpsIcon icon) {            
            if (!Objects.equals(icon, this.icon)) {
                this.icon = icon;
            }
            if (icon == null) {
                if (imageContainer!=null){
                    imageContainer.remove();
                    imageContainer = null;
                }
            } else {
                final Html image;
                if (imageContainer == null) {
                    imageContainer = new Div();
                    image = new Html("img");
                    imageContainer.add(image);
                    image.setCss("width", "12px");
                    image.setCss("height", "12px");

                    imageContainer.setCss("vertical-align", "middle");
                    imageContainer.setCss("padding-top", "3px");
                    imageContainer.setCss("display", "inline");
                    imageContainer.setCss("margin-right", "3px");

                    this.cell.add(0, imageContainer);                    
                }else{
                    image = imageContainer.getChildAt(0);
                }
                final String imageUri = getIcon().getURI(this);
                if (!Objects.equals(image.getAttr("src"), imageUri)) {
                    image.setAttr("src", imageUri);
                }                
            }
        }

        @Override
        public WpsIcon getIcon() {
            return icon;
        }

        @Override
        public int getWidth() {
            return super.getWidth();
        }

        @Override
        public final void setWidth(int w) {
            this.cell.setCss("width", String.valueOf(w) + "px");
            this.cell.setAttr("width", w);
        }

        private void processClickEvent(final int buttonsMask) {
            final EnumSet<EMouseButton> mouseButtons = EMouseButton.fromAwtBitMask(buttonsMask);
            if (mouseButtons.contains(EMouseButton.LEFT)) {
                Grid.this.notifyColumnHeaderClick(this, EKeyboardModifier.fromAwtBitMask(buttonsMask));
            } else if (mouseButtons.contains(EMouseButton.RIGHT)) {
                Grid.this.setupColumnsVisiblity();
            }
        }

        @Override
        public Object getUserData() {
            return userData;
        }

        @Override
        public void setUserData(Object userData) {
            this.userData = userData;
        }

        @Override
        public String getTitle() {
            return this.title.getInnerText();
        }

        @Override
        public void setTitle(String title) {
            this.title.setInnerText(title);
        }

        public int getIndex() {
            return Grid.this.columns.indexOf(this);
        }

        public boolean isEditable() {
            return isEditable;
        }

        public void setEditable(boolean editable) {
            this.isEditable = editable;
        }

        public boolean getEditable() {
            return isEditable();
        }

        public LinkedList<Cell> getCellsAtColumn(int columnIndex) {
            if (columnIndex < 0 && columnIndex >= getColumnCount()) {
                return new LinkedList<>();
            }
            LinkedList<Cell> column = new LinkedList<>();
            for (Row r : rows) {
                column.add(r.cells.get(columnIndex));
            }
            return column;
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);            
            updateColumnsVisibility(null);
            Grid.this.updateColumnsResizingMode();
        }

        @Override
        public int getFixedWidth() {
            String attr = html.getAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME);
            if (attr != null) {
                try {
                    return Integer.parseInt(attr);
                } catch (NumberFormatException e) {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        @Override
        public boolean isSetFixedWidth() {
            return html.getAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME) != null;
        }

        @Override
        public void setFixedWidth(int fw) {
            html.setAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME, fw);
            setSizePolicy(IGrid.EColumnSizePolicy.FIXED);
        }

        @Override
        public void unsetFixedWidth() {
            html.setAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME, null);
            setSizePolicy(IGrid.EColumnSizePolicy.INTERACTIVE);
        }

        @Override
        public int getInitialWidth() {
            String attr = html.getAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME);
            if (attr != null) {
                try {
                    return Integer.parseInt(attr);
                } catch (NumberFormatException e) {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        @Override
        public boolean isSetInitialWidth() {
            return html.getAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME) != null;
        }

        @Override
        public void setInitialWidth(int iw) {
            html.setAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME, iw);
        }

        @Override
        public void unsetInitialWidth() {
            html.setAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME, null);
        }

        @Override
        public void showSortingIndicator(final RadSortingDef.SortingItem.SortOrder direction,
                final int sequenceNumber) {
            if (sequenceNumber > 0) {
                if (srtSequenceIndicator == null) {
                    initSrtSequenceIndicator();
                }
                srtSequenceIndicator.setCss("visibility", null);
                srtSequenceIndicator.setInnerText(String.valueOf(sequenceNumber));
            } else if (srtSequenceIndicator != null) {
                srtSequenceIndicator.setCss("visibility", "hidden");
            }
            if (srtDirectionIndicator == null) {
                initSrtDirectionIndicator();
            }
            if (direction == RadSortingDef.SortingItem.SortOrder.ASC) {
                srtDirectionIndicator.setInnerText(SelectorSortUtils.ASC_ARROW);
            } else {
                srtDirectionIndicator.setInnerText(SelectorSortUtils.DESC_ARROW);
            }
            srtDirectionIndicator.setCss("visibility", null);
        }

        @Override
        public void hideSortingIndicator() {
            if (srtDirectionIndicator != null) {
                srtDirectionIndicator.setCss("visibility", "hidden");
            }
            if (srtSequenceIndicator != null) {
                srtSequenceIndicator.setCss("visibility", "hidden");
            }
        }

        @Override
        public boolean isSortingIndicatorVisible() {
            return srtDirectionIndicator != null && !"hidden".equals(srtDirectionIndicator.getCss("visibility"));
        }

        private void initSrtSequenceIndicator() {
            srtSequenceIndicator = createSrtIndicatorLabel();
            srtSequenceIndicator.setCss("font-size", "10px");
        }

        private void initSrtDirectionIndicator() {
            srtDirectionIndicator = createSrtIndicatorLabel();
            srtDirectionIndicator.setCss("font-size", "14px");
        }

        private Html createSrtIndicatorLabel() {
            final Html srtIndicator = new Html("label");
            cell.add(srtIndicator);
            srtIndicator.addClass("rwt-ui-element");
            srtIndicator.setCss("white-space", "nowrap");
            srtIndicator.setCss("float", "right");
            srtIndicator.setCss("color", "dimgray");
            srtIndicator.setCss("font-weight", "bold");
            srtIndicator.setCss("height", "100%");
            return srtIndicator;
        }

        @Override
        public void setSizePolicy(final EColumnSizePolicy newPolicy) {
            if (policy!=newPolicy){
                if (newPolicy==EColumnSizePolicy.FIXED && !isSetFixedWidth()){
                    return;
                }
                setSizePolicyImpl(newPolicy);
                Grid.this.updateColumnsResizingMode();
            }
        }
        
        final void setSizePolicyImpl(final EColumnSizePolicy newPolicy){
            getHtml().setAttr(IGrid.IColumn.SIZE_POLISY_ATTR_NAME,newPolicy.getHtmlAttrValue());
            policy = newPolicy;            
        }

        @Override
        public EColumnSizePolicy getSizePolicy() {
            return policy;
        }
        
        private void setResizingColumnIdx(final int idx){
            cell.setAttr(IGrid.IColumn.RESIZE_COLUMN_INDEX_ATTR_NAME, idx);
            separator.setAttr(IGrid.IColumn.RESIZABLE_ATTR_NAME, idx>=0);
        }        
    }
    
    private void updateColumnsResizingMode(){
        if (isStickToRight()){
            final IGrid.EColumnSizePolicy[] actualSizePolicy = columnsResizeController.calcFinalSizePolicy();                    
            for (int i=0,count=getColumnCount(); i<count; i++){
                getColumn(i).setSizePolicyImpl(actualSizePolicy[i]);
            }
        }
        for (int i=0,count=getColumnCount(); i<count; i++){            
            final Column column = getColumn(i);            
            column.setResizingColumnIdx(columnsResizeController.findSectionToResize(i));
        }
    }    
    
    private Alignment headerAlignment;

    public void setHeaderAlignment(Alignment a) {
        this.headerAlignment = a;
        header.updateHeaderAlign(a);
    }

    public Alignment getHeaderAlignment() {
        return headerAlignment;
    }

    private class Header extends UIObject {

        private Html head;
        private Html tr;

        public Header() {
            super(new Html("table"));
            html.setCss("position", "relative");
            this.head = new Html("thead");
            this.tr = new Html("tr");
            this.html.add(this.head);
            this.head.add(tr);
            this.html.setCss("cellspacing", "0px");
            this.html.setCss("cellpadding", "0px");
            this.html.setCss("border-collapse", "collapse");
            this.html.setCss("border", "none");
            
            this.html.setCss("table-layout", "fixed");
            Grid.this.headerContainer.add(html);
            Grid.this.headerContainer.setCss("position", "relative");
            updateHeaderAlign(getHeaderAlignment());
            setParent(Grid.this);
        }

        public int getColumnIndex(Column c) {
            return columns.indexOf(c);
        }

        public Column addColumn(int index, String title) {

            Column c = new Column();

            if (index < 0 || index >= columns.size()) {
                columns.add(c);
            } else {
                columns.add(index, c);
            }
            c.setTitle(title);
            c.setParent(this);

            this.tr.add(columns.indexOf(c), c.getHtml());
            return c;
        }

        public void removeColumn(int index) {
            Column c = columns.remove(index);
            this.tr.remove(c.html);
            c.setParent(null);
        }

        public final void updateHeaderAlign(Alignment a) {
            if (columns != null && !columns.isEmpty()) {
                for (Column c : columns) {
                    c.cell.setCss("text-align", a.name());
                }
            }
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            if (columns != null) {
                for (Column c : columns) {
                    c.visit(visitor);
                }
            }
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            if (columns != null) {
                for (Column c : columns) {
                    result = c.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }

        @Override
        public void processAction(String actionName, String actionParam) {
            if (IGrid.RESIZE_ACTION_NAME.equals(actionName) & actionParam != null) {
                headerSettings = actionParam;
            }
            super.processAction(actionName, actionParam);
        }
    }

    private class Data extends UIObject {

        private Html body = new Html("tbody");

        public Data() {
            super(new Html("table"));
            Grid.this.bodyContainer.add(html);
            Grid.this.bodyContainer.addClass("rwt-grid-data-panel");
            this.html.setCss("cellspacing", "0px");
            this.html.setCss("cellpadding", "0px");
            this.html.setCss("border-collapse", "collapse");
            this.html.setCss("border", "none");
            
            this.html.setCss("table-layout", "fixed");
            this.html.add(body);
            setParent(Grid.this);
        }

        public Row addRow() {
            Row row = new Row();
            if (!rows.isEmpty()) {
                Row lastRow = rows.get(rows.size() - 1);
                for (Cell c : lastRow.cells) {
                    c.html.removeClass("rwt-grid-data-last-row");
                    c.html.addClass("rwt-grid-data-row");
                }
            }
            rows.add(row);
            for (Column c : columns) {
                Cell cell = row.addCell(c, columns.indexOf(c));
                cell.html.addClass("rwt-grid-data-col");
                cell.html.addClass("rwt-grid-data-last-row");
                cell.setVisible(c.isVisible());
            }
            row.setParent(this);
            this.body.add(row.getHtml());
            return row;
        }

        public Row insertRow(int index) {
            if (index < 0 || index >= rows.size()) {
                return addRow();
            }
            Row row = new Row();
            rows.add(index, row);
            for (Column c : columns) {
                Cell cell = row.addCell(c, columns.indexOf(c));
                cell.html.addClass("rwt-grid-data-col");
                cell.html.addClass("rwt-grid-data-last-row");
                cell.setVisible(c.isVisible());
            }
            row.setParent(this);
            this.body.add(index, row.getHtml());
            return row;
        }

        public void swapRows(int index1, int index2) {
            if (index1 < 0 || index1 >= rows.size()) {
                return;
            }
            if (index2 < 0 || index2 >= rows.size()) {
                return;
            }
            if (index1 == index2) {
                return;
            }

            Row row1 = getRow(index1);
            Row row2 = getRow(index2);

            final boolean row1WithSpannedCells = row1.spannedCells();
            final boolean row2WithSpannedCells = row2.spannedCells();

            rows.set(index1, row2);
            rows.set(index2, row1);

            if (row1WithSpannedCells) {
                removeHtmlForRowWithSpannedCells(row1.getHtml());
            } else {
                this.body.remove(row1.getHtml());
            }
            row1.getHtml().renew();
            if (row2WithSpannedCells) {
                removeHtmlForRowWithSpannedCells(row2.getHtml());
            } else {
                this.body.remove(row2.getHtml());
            }
            row2.getHtml().renew();

            if (row2WithSpannedCells) {
                addHtmlForRowWithSpannedCells(index1, row2.getHtml());
            } else {
                this.body.add(index1, row2.getHtml());
            }
            if (row1WithSpannedCells) {
                addHtmlForRowWithSpannedCells(index2, row1.getHtml());
            } else {
                this.body.add(index2, row1.getHtml());
            }
        }

        public Row addRowWithSpannedCells() {
            Row row = new Row();

            if (!rows.isEmpty()) {
                Row lastRow = rows.get(rows.size() - 1);
                for (Cell c : lastRow.cells) {
                    c.html.removeClass("rwt-grid-data-last-row");
                    c.html.addClass("rwt-grid-data-row");
                }
            }

            Cell cell = row.addCell(columns.get(0), 0);
            cell.html.setAttr("colSpan", columns.size());
            cell.html.addClass("rwt-grid-data-col");
            cell.html.addClass("rwt-grid-data-last-row");
            row.setParent(this);
            addHtmlForRowWithSpannedCells(-1, row.getHtml());
            rows.add(row);
            return row;
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
            }
        }

        public void removeRow(final Row row) {
            if (rows != null) {
                final boolean spannedCells = row.spannedCells();
                if (rows.remove(row)) {
                    if (spannedCells) {
                        removeHtmlForRowWithSpannedCells(row.getHtml());
                    } else {
                        this.body.remove(row.getHtml());
                    }
                }
            }
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            if (rows != null) {
                for (Row r : rows) {
                    r.visit(visitor);
                }
            }
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            if (rows != null) {
                for (Row r : rows) {
                    result = r.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }
    }

    public class Row extends UIObject {

        private List<Cell> cells = new LinkedList<>();
        private Object userData;
        private boolean isEditable = true;

        public Row() {
            super(new Html("tr"));
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
            int idx = header.getColumnIndex(c);
            return cells.get(idx);
        }

        public int getCellCount() {
            return cells.size();
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            if (cells != null) {
                for (Cell c : cells) {
                    c.visit(visitor);
                }
            }
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
            return null;
        }

        boolean spannedCells() {
            if (cells.size() > 0) {
                final String columnSpan = cells.get(0).getHtml().getAttr("colSpan");
                return columnSpan != null && !columnSpan.isEmpty();
            }
            return false;
        }

        public Object getUserData() {
            return userData;
        }

        public void setUserData(Object userData) {
            this.userData = userData;
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
    }

    public static class DefaultRenderer extends UIObject implements CellRenderer {

        private Html label = new Html("label");
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

        public Cell(Column c, Row r) {
            super(new Html("td"));
            this.col = c;
            setTabIndex(col.getIndex() + 1);
            this.row = r;
            html.setFocusSencitive(false);
            this.html.add(container);
            this.html.addClass("rwt-grid-row-cell");
            this.html.setCss("padding", "0px");

            container.addClass("rwt-grid-row-cell-content");
            //this.html.setCss("margin-left", "5px");
            //this.html.setCss("margin-right", "5px");
            registerEventListeners();
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
            this.html.setAttr("onkeydown", "$RWT.grid.cell.keyDown");            
        }
        
        private void removeEventListeners(){
            this.html.setAttr("onclick", null);
            this.html.setAttr("ondblclick", null);
            this.html.setAttr("onkeydown", null);
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

        void setupBorder(final boolean currentRow, final boolean currentCell) {
            final Color frameColor;
            final boolean rowBorder = 
                currentRow && getSelectionStyle().contains(IGrid.ESelectionStyle.ROW_FRAME);
            final boolean cellBorder = 
                currentCell && getSelectionStyle().contains(IGrid.ESelectionStyle.CELL_FRAME);            
            if (cellBorder){
                frameColor = getCurrentCellFrameColor();
            }else if (rowBorder){
                frameColor = getCurrentRowFrameColor();
            }else{
                frameColor = null;
            }
            getHtml().removeClass("current-cell-frame");
            getHtml().removeClass("current-row-frame");            
            getHtml().setCss("border-color", null);
            getHtml().setCss("border-top-color", null);
            getHtml().setCss("border-bottom-color", null);
            if (frameColor == null) {                
                if (col.getIndex() > 0) {
                    final Cell prevCell = row.getCell(col.getIndex() - 1);
                    prevCell.getHtml().removeClass("prev-current-cell-frame");
                    prevCell.getHtml().setCss("border-right-color", null);
                }
                final int rowIndex = getRowIndex(row);
                if (rowIndex > 0) {
                    final Row upperRow = Grid.this.getRow(rowIndex - 1);
                    if (row.spannedCells()) {
                        for (int i = upperRow.getCellCount() - 1; i >= 0; i--) {
                            final Cell upperCell = upperRow.getCell(i);
                            upperCell.getHtml().removeClass("upper-current-cell-frame");
                            upperCell.getHtml().setCss("border-bottom-color", null);
                        }
                        if (rowIndex < (Grid.this.getRowCount() - 1)) {
                            final Row lowerRow = Grid.this.getRow(rowIndex + 1);
                            for (int i = lowerRow.getCellCount() - 1; i >= 0; i--) {
                                final Cell lowerCell = lowerRow.getCell(i);
                                lowerCell.getHtml().removeClass("lower-current-cell-frame");
                                lowerCell.getHtml().setCss("border-top-color", null);
                            }
                        }
                    } else if (upperRow.spannedCells()) {
                        upperRow.getCell(0).getHtml().setCss("border-bottom-width", null);
                    } else if (col.getIndex() > -1) {
                        final int colIndex = Math.min(upperRow.getCellCount() - 1, col.getIndex());
                        final Cell upperCell = upperRow.getCell(colIndex);
                        upperCell.getHtml().removeClass("upper-current-cell-frame");
                        upperCell.getHtml().setCss("border-bottom-color", null);
                    }
                }
            } else {
                final String frameColorStr = UIObject.color2Str(frameColor);
                getHtml().addClass(currentCell ? "current-cell-frame" : "current-row-frame");
                if (currentCell){
                    getHtml().setCss("border-color", frameColorStr);
                }else{
                    getHtml().setCss("border-top-color", frameColorStr);
                    getHtml().setCss("border-bottom-color", frameColorStr);
                }
                if (col.getIndex() > 0 && currentCell) {
                    final Cell prevCell = row.getCell(col.getIndex() - 1);
                    prevCell.getHtml().addClass("prev-current-cell-frame");
                    prevCell.getHtml().setCss("border-right-color", frameColorStr);
                }
                final int rowIndex = getRowIndex(row);
                if (rowIndex > 0) {
                    final Row upperRow = Grid.this.getRow(rowIndex - 1);
                    if (row.spannedCells() && currentCell) {
                        for (int i = upperRow.getCellCount() - 1; i >= 0; i--) {
                            final Cell upperCell = upperRow.getCell(i);
                            upperCell.getHtml().addClass("upper-current-cell-frame");
                            upperCell.getHtml().setCss("border-bottom-color", frameColorStr);
                        }
                        if (rowIndex < (Grid.this.getRowCount() - 1)) {
                            final Row lowerRow = Grid.this.getRow(rowIndex + 1);
                            for (int i = lowerRow.getCellCount() - 1; i >= 0; i--) {
                                final Cell lowerCell = lowerRow.getCell(i);
                                lowerCell.getHtml().addClass("lower-current-cell-frame");
                                lowerCell.getHtml().setCss("border-top-color", frameColorStr);
                            }
                        }
                    } else if (upperRow.spannedCells()) {
                        upperRow.getCell(0).getHtml().setCss("border-bottom-width", "0px");
                    } else {
                        final Cell upperCell = upperRow.getCell(col);
                        upperCell.getHtml().addClass("upper-current-cell-frame");
                        upperCell.getHtml().setCss("border-bottom-color", frameColorStr);
                    }
                }
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
                    setBackground(background);                    
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
        public void visit(Visitor visitor) {
            super.visit(visitor);
            if (editor != null) {
                editor.getUI().visit(visitor);
            } else {
                renderer.getUI().visit(visitor);
            }
        }

        @Override
        public void processAction(String actionName, String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                if (Grid.this.getCurrentCell() == this) {
                    if (editor != null && editor.getUI() != null) {
                        editor.getUI().processAction(actionName, actionParam);
                    } else {
                        if (!tryToOpenEditor()
                                && renderer.getUI() != null
                                && html.containsClass("editor-cell")) {
                            renderer.getUI().processAction(actionName, actionParam);
                        }
                    }
                } else {
                    if (Grid.this.getCurrentCell() != null) {
                        if (Grid.this.getCurrentCell().finishEdit(true)) {
                            Grid.this.setCurrentCell(this);
                        }
                    } else {
                        Grid.this.setCurrentCell(this);
                    }
                }
            } else if (Events.EVENT_NAME_KEY_DOWN.equals(actionName)) {
                switch (actionParam) {
                    case "27":
                        //esc
                        if (editor != null && editor.getUI() != null) {
                            finishEdit(false);
                        }
                        break;
                    case "13":
                        if (editor != null && editor.getUI() != null) {
                            finishEdit(true);
                        } else {
                            tryToOpenEditor();
                        }
                        break;
                }
            } else if (Events.EVENT_NAME_ONDBLCLICK.equals(actionName) && isEnabled()) {
                Grid.this.notifyDoubleClick(row, this);
            }
        }

        public boolean isEditable() {
            if (Grid.this.isEditable && Grid.this.editorProvider != null) {
                if (!row.isEditable || !col.isEditable) {
                    return false;
                } else {
                    return isEditable;
                }
            } else {
                return false;
            }
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
        public void setBackground(Color c) {
            if (html.containsClass("rwt-ui-selected-item")) {
                this.background = c;
            }else{
                if (renderer != null) {
                    renderer.getUI().setBackground(c);
                }
                super.setBackground(c);
            }
        }

       /* @Override
        public Color getBackground() {
            if (html.containsClass("rwt-ui-selected-item")) {
                return this.background;
            } else {
                return renderer.getUI().getBackground();
            }
        }*/

        @Override
        public void setForeground(Color c) {
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

       /* @Override
        protected ICssStyledItem getBackgroundHolder() {
            return renderer.getUI().getHtml();
        }*/

        public final Object getEditorValue() {
            if (this.renderer instanceof EditMaskRenderer) {
                return ((EditMaskRenderer) renderer).getEditorValue();
            }
            return null;
        }

        protected void updateRenderer() {
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
    
    private Header header;
    private Html headerContainer = new Div();
    private Html bodyContainer = new Div();
    private Data data;
    private List<Column> columns = new LinkedList<>();
    private List<Row> rows = new LinkedList<>();
    private Cell selection = null;
    private boolean isEditable = true;
    private boolean borderShown;
    private boolean browserFocusFrameEnabled = true;
    private SelectionMode selectionMode = SelectionMode.CELL;
    private Color currentCellFrameColor = Color.decode("#404040");
    private Color currentRowFrameColor = Color.decode("#3399ff");
    private final IGrid.RowFrame currentRowFrame;
    private final IGrid.CellFrame currentCellFrame;
    private String headerSettings = "";    
    private final IGrid.ColumnsResizeController columnsResizeController = new IGrid.ColumnsResizeController(this);
    private final EnumSet<IGrid.ESelectionStyle> selectionStyle = EnumSet.of(IGrid.ESelectionStyle.BACKGROUND_COLOR,IGrid.ESelectionStyle.CELL_FRAME);

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
    private DefaultCurrentRowListener rowSelectionListener = new DefaultCurrentRowListener();
    private DefaultCurrentCellListener cellSelectionListener = new DefaultCurrentCellListener();
    private List<DoubleClickListener> doubleClickListeners;
    private List<CellValueChangeListener> cellValueChangeListeners;
    private List<ColumnHeaderClickListener> headerClickListeners;
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

    public CellRendererProvider getRendererProvider() {
        return rendererProvider;
    }
    private CellEditorProvider editorProvider = null;

    public CellEditorProvider getEditorProvider() {
        return editorProvider;
    }

    public Grid() {
        super(new Div());
        this.headerContainer.addClass("rwt-grid-header-panel");
        this.headerContainer.setCss("overflow", "hidden");
        this.headerContainer.setCss("width", "100%");
        //this.headerContainer.setCss("padding-bottom", "5px");
        this.bodyContainer.setCss("overflow", "auto");
        this.bodyContainer.setCss("width", "100%");
        this.bodyContainer.setAttr("onscroll", "_rwt_grid_flow._syncScroll");        
        headerContainer.addClass("header");
        this.header = new Header();
        this.data = new Data();
        this.html.add(this.headerContainer);
        this.html.add(this.bodyContainer);
        this.bodyContainer.addClass("rwt-grid");
        html.layout("$RWT.grid._layout");
        html.setCss("overflow", "hidden");
        html.setAttr("more-rows", "true");
        currentCellFrame = new IGrid.CellFrame(bodyContainer,"cellFrame");
        currentCellFrame.setColor(currentCellFrameColor);
        currentRowFrame = new IGrid.RowFrame(bodyContainer,"rowFrame");
        currentRowFrame.setColor(currentRowFrameColor);
        setBorderVisible(true);
    }

    @Deprecated
    public String getPersistentId() {
        return getPersistenceKey();
    }

    public Row getCurrentRow() {
        return selection == null ? null : selection.row;
    }

    private class SetupColumnVisibilityDialog extends Dialog {

        private class VCheckBox extends CheckBox {

            ColumnDescriptor desc;

            VCheckBox(ColumnDescriptor desc) {
                setText(desc.getTitle());
                this.desc = desc;
            }
        }

        public SetupColumnVisibilityDialog(IClientEnvironment env) {
            super(env, null);
            this.setWindowTitle(getEnvironment().getMessageProvider().translate("Selector", "Columns Visibility"));
            List<ColumnDescriptor> all = getAllColumnDescriptors();
            if (all == null) {
                return;
            }
            List<ColumnDescriptor> visible = getVisibleColumnDescriptors(all);
            if (visible == null) {
                return;
            }
            this.getHtml().setAttr("dlgId", "visibleColumns");
            this.setWidth(300);
            this.setHeight(300);
            LabeledEditGrid grid = createLabeledEditGrid();
            this.add(grid);
            List<VCheckBox> cbss = fillColumnDescription(all, visible, grid);

            grid.setTop(3);
            grid.setLeft(3);
            grid.getAnchors().setRight(new Anchors.Anchor(1, -3));
            grid.getAnchors().setBottom(new Anchors.Anchor(1, -3));
            this.addCloseAction(EDialogButtonType.OK);
            this.addCloseAction(EDialogButtonType.CANCEL);

            if (this.execDialog() == DialogResult.ACCEPTED) {
                List<ColumnDescriptor> descriptor = new LinkedList<>();
                for (VCheckBox cb : cbss) {
                    if (cb.isSelected()) {
                        descriptor.add(cb.desc);
                    }
                }                
                updateColumnsVisibility(descriptor);
                Grid.this.updateColumnsResizingMode();
                updateConfigStr();
            }
        }

        private LabeledEditGrid createLabeledEditGrid() {
            return new LabeledEditGrid(new LabeledEditGrid.AbstractEditor2LabelMatcher() {
                @Override
                protected UIObject createLabelComonent(UIObject editorComponent) {
                    return new Label();
                }
            });
        }

        private List<VCheckBox> fillColumnDescription(List<ColumnDescriptor> all, final List<ColumnDescriptor> visible, LabeledEditGrid grid) {
            final List<VCheckBox> cbss = new LinkedList<>();
            for (ColumnDescriptor desc : all) {
                VCheckBox cb = new VCheckBox(desc);
                grid.addEditor(cb, 0, -1);
                cb.setSelected(visible.contains(desc));
                cbss.add(cb);
                if (visible.size() == 1) {
                    cb.setEnabled(!visible.contains(desc));
                }
                cb.addSelectionStateListener(new CheckBox.SelectionStateListener() {
                    @Override
                    public void onSelectionChange(CheckBox cb) {
                        int j = 0;
                        for (int i = 0; i < cbss.size(); i++) {
                            if (!cbss.get(i).isSelected()) {
                                j++;
                            }
                            int index = findUncheckedItemIndex(cbss);
                            cbss.get(i).setEnabled(i != index);
                            cbss.get(index).setEnabled(!(j == cbss.size() - 1));
                        }

                    }
                });
            }
            return cbss;
        }

        private int findUncheckedItemIndex(List<VCheckBox> cbss) {
            for (VCheckBox cb : cbss) {
                if (cb.isSelected()) {
                    return cbss.indexOf(cb);
                }
            }
            return cbss.size() - 1;
        }
    }

    protected void updateColumnsVisibility(List<ColumnDescriptor> visibleColumns) {
        if (visibleColumns == null) {//default behaviour
            boolean visible[] = new boolean[columns.size()];
            int i = 0;
            for (Column c : columns) {
                visible[i] = c.isVisible();
                i++;
            }
            for (Row r : rows) {
                for (i = 0; i < visible.length; i++) {
                    r.getCell(i).setVisible(visible[i]);
                }
            }
        }
    }

    public boolean isHeaderVisible() {
        return !"none".equals(headerContainer.getCss("display"));
    }

    public void setHeaderVisible(boolean visible) {
        headerContainer.setCss("display", visible ? null : "none");
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

    private void setupColumnsVisiblity() {
        Dialog setupDialog = new SetupColumnVisibilityDialog(getEnvironment());
        setupDialog.execDialog(null);
    }

    public Cell getCurrentCell() {
        return selection;
    }

    public void shadeEvenRow(int opacityPercent) {
        if (data != null) {
            data.getHtml().setAttr("alpha", opacityPercent);
        }
    }
    private Color shadeColor;

    public void setShadeColor(Color color) {
        if (data != null && color != null) {
            shadeColor = color;
            String c = "rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
            data.html.setAttr("shadeColor", c);
        }
    }

    public Color getShadeColor() {
        return shadeColor;
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
        this.header.removeColumn(index);
        final int columnsCount = getColumnCount();
        if (!rows.isEmpty()) {
            for (Row row : rows) {
                if (row.spannedCells()) {
                    Cell cell = row.getCell(0);
                    cell.getHtml().setAttr("colSpan", columnsCount);
                } else {
                    Cell cell = row.cells.remove(index);
                    row.html.remove(cell.html);
                    cell.setParent(null);
                }
            }
        }
        if (selectedRow != null && Math.abs(index - selectedCell) < 2 && selection != null) {
            //update borders
            setupFrames(selection);
        }
        updateColumnsResizingMode();
    }

    @Override
    public Column addColumn(String title) {
        return addColumn(-1, title);
    }

    @Override
    public Column addColumn(int index, String title) {
        final int newColumnIndex = index < 0 ? getColumnCount() : index;
        final boolean updateCurrentCellBorder = selection != null
                && (selection.getRow().spannedCells() || Math.abs(newColumnIndex - selection.getColumn().getIndex()) < 2);
        if (updateCurrentCellBorder) {
            setupFrames(null);
        }
        Column c = header.addColumn(index, title);
        if (!rows.isEmpty()) {
            int columnIndex = header.getColumnIndex(c);
            int columnCount = getColumnCount();
            final Row selectedRow;
            if (selectionMode == SelectionMode.ROW && selection != null) {
                selectedRow = selection.row;
            } else {
                selectedRow = null;
            }
            for (Row r : rows) {
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
        for (Row r : new ArrayList<>(rows)) {
            removeRow(r);
        }
    }

    @Override
    public Column getColumn(int index) {
        return columns.get(index);
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    public Row addRow() {
        return data.addRow();
    }

    public Row insertRow(int index) {
        return data.insertRow(index);
    }

    public void swapRows(int index1, int index2) {
        data.swapRows(index1, index2);
    }

    public Row addRowWithSpannedCells() {
        return data.addRowWithSpannedCells();
    }

    public Row getRow(int index) {
        return rows.get(index);
    }

    public int getRowIndex(Row row) {
        return rows.indexOf(row);
    }

    public void removeRow(Row row) {
        data.removeRow(row);
        if (selection!=null && selection.row==row){
            setCurrentCell(null);
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    @Override
    public void setWidth(int w) {
        super.setWidth(w);
        html.layout("$RWT.grid._layout");
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
        if (selection == cell) {
            return;
        }

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
                    for (Row row : rows) {
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
                    for (Row row : rows) {
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
            for (Row row : rows) {
                row.getHtml().markAsChoosable();
            }
        } else if (mode == SelectionMode.CELL){//CELL
            if (selection != null && getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)) {
                selection.setSelected(true);
            }
            for (Row row : rows) {
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
                currentCellFrame.setCell(currentCell.getRow(), currentCell.getCellIndex());
            }
        }
    }
    
    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        this.header.visit(visitor);
        this.data.visit(visitor);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        result = header.findObjectByHtmlId(id);
        if (result != null) {
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
        } else {
            super.processAction(actionName, actionParam);
        }
    }

    public void setEditorProvider(CellEditorProvider editorProvider) {
        this.editorProvider = editorProvider;
    }

    public Cell getCell(int row, int column) {
        if (row >= getRowCount() || row < 0 || column >= getColumnCount() || column < 0) {
            return null;
        }
        Row r = rows.get(row);
        Cell cell = r.getCell(column);
        return cell;
    }

    public void setRendererProvider(CellRendererProvider rendererProvider) {
        this.rendererProvider = rendererProvider;
        for (Row r : rows) {
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
        return headerSettings;
    }

    @Override
    public void setHeaderSettings(final String settings) {
        if (settings != null && !settings.isEmpty() && !settings.equals(headerSettings)) {
            this.headerSettings = settings;
            String[] arrHeaderSettings = settings.split(";");
            if (arrHeaderSettings != null && arrHeaderSettings.length > 0) {
                for (int i = 0; i < arrHeaderSettings.length; i++) {
                    String id = arrHeaderSettings[i].split(":")[0];
                    int width = Integer.parseInt(arrHeaderSettings[i].split(":")[1].split(",")[0]);                    
                    for (Column s : columns) {
                        if (s.isVisible() && s.getPersistenceKey().equals(id) && s.getSizePolicy()==IGrid.EColumnSizePolicy.INTERACTIVE) {
                            s.setInitialWidth(width);
                        }
                    }
                }
            }
        }
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

            } else {
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
                this.editor = ValEditorFactory.getDefault().createValEditor(null, editMask, e);

                UIObject uio = (UIObject) editor;
                uio.html.addClass("renderer");
                uio.setHeight(21);
            } else {
                final UIObject parent = ((UIObject) editor).getParent();
                if (parent != null) {
                    parent.html.addClass("editor-cell");
                }
            }

            editor.addValueChangeListener(new ValueEditor.ValueChangeListener() {
                @Override
                public void onValueChanged(Object oldValue, Object newValue) {
                    if (editingCell != null) {
                        editingCell.setValue(newValue);
                    }
                }
            });

            return (UIObject) editor;
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

    public static class EditingOptions implements IEditingOptions {

        private ECellEditingMode mode;
        private final Cell cell;
        private final Column column;
        private EditMask mask = null;

        EditingOptions(Cell cell) {
            mode = null;
            this.cell = cell;
            this.column = null;
        }

        EditingOptions(Column col) {
            mode = ECellEditingMode.NULL_VALUE_ACCEPTED;
            this.cell = null;
            this.column = col;
        }

        @Override
        public EditMask getEditMask() {
            if (mask == null) {
                return null;
            }
            return EditMask.newCopy(mask);
        }

        @Override
        public ECellEditingMode getEditingMode() {
            return mode;
        }

        @Override
        public void setEditMask(EditMask editMask) {
            if (mask == null && editMask == null) {
                return;
            }
            mask = editMask==null ? null : EditMask.newCopy(editMask);
            if (cell != null) {
                cell.updateRenderer();
            }
            if (column != null) {
                LinkedList<Cell> cells = column.getCellsAtColumn(column.getIndex());
                for (Cell c : cells) {
                    c.updateRenderer();
                }
            }
        }

        @Override
        public void setEditingMode(ECellEditingMode editMode) {
            if (editMode == null) {
                //throw new IllegalArgumentException("Cell editing mode can`t be null!");
                mode = ECellEditingMode.NULL_VALUE_ACCEPTED;//by default
            }
            if (editMode == mode) {
                return;
            }
            this.mode = editMode;

            if (cell != null) {
                cell.updateRenderer();
            }
            if (column != null) {
                LinkedList<Cell> cells = column.getCellsAtColumn(column.getIndex());
                for (Cell c : cells) {
                    c.updateRenderer();
                }
            }
        }
    }
    
    @Override
    public final void setBorderVisible(boolean showBorder) {
        if (borderShown!=showBorder){
            data.getHtml().setAttr(IGrid.SHOW_BORDER_ATTR_NAME, showBorder ? "true" : null);
            borderShown = showBorder;
        }
    }

    @Override
    public boolean isBorderVisible() {
        return borderShown;
    }

    @Override
    public void setBrowserFocusFrameEnabled(final boolean enabled){
        if (browserFocusFrameEnabled!=enabled){
            browserFocusFrameEnabled = enabled;
            if (enabled){
                bodyContainer.removeClass("rwt-grid-disable-standard-focus-frame");
            }else{
                bodyContainer.addClass("rwt-grid-disable-standard-focus-frame");
            }
        }
    }
    
    @Override
    public boolean isBrowserFocusFrameEnabled(){
        return browserFocusFrameEnabled;
    }
}
