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

import java.awt.Color;
import java.util.EnumSet;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.icons.WpsIcon;


public interface IGrid {
    
    public static enum EColumnSizePolicy{
                        
        INTERACTIVE("manual"),
        BY_CONTENT("content"),
        STRETCH("stretch"),
        FIXED("fixed");
        
        private final String htmlAttr;
        
        private EColumnSizePolicy(final String attr){
            htmlAttr = attr;
        }
        
        public String getHtmlAttrValue(){
            return htmlAttr;
        }
    }
    
    public static enum ESelectionStyle{
        BACKGROUND_COLOR,CELL_FRAME,ROW_FRAME
    }
    
    public abstract static class AbstractFocusFrame {
        
        private final Html frameLines[] = new Html[4];//left,right,top,bottom
        private final Html holder;
        private final String attrName;
        private int offsetLeft = 0;
    
        protected AbstractFocusFrame(final Html grid, final String attrName){
            for (int i=0; i<4; i++){
                frameLines[i] = new Html("hr");                    
                frameLines[i].addClass("rwt-grid-focus-frame-line");
                if (i<2){
                    frameLines[i].addClass("rwt-grid-focus-frame-vertical-line");
                }else{
                    frameLines[i].addClass("rwt-grid-focus-frame-horizontal-line");
                }
                grid.add(frameLines[i]);
            }
            this.attrName = attrName;
            holder = grid;
            writeToJs();
        }
        
        public final void setColor(final Color color){
            if (color!=null){
                for (int i=0; i<4; i++){
                    frameLines[i].setCss("border-color", TextOptions.color2Str(color));
                }
            }
        }

        public int getOffsetLeft() {
            return offsetLeft;
        }

        public void setOffsetLeft(int offset) {
            offsetLeft = offset;
            writeToJs();
        }
        
        protected abstract String getRowId();
        
        protected abstract int getCellIndex();
        
        public abstract void hide();
        
        protected final void writeToJs(){
            final StringBuilder jsWriter = new StringBuilder("{");
            jsWriter.append("\"left\": \"");
            jsWriter.append(frameLines[0].getId());
            jsWriter.append("\", \"right\": \"");
            jsWriter.append(frameLines[1].getId());
            jsWriter.append("\", \"top\": \"");
            jsWriter.append(frameLines[2].getId());
            jsWriter.append("\", \"bottom\": \"");
            jsWriter.append(frameLines[3].getId());
            jsWriter.append("\", \"row\": \"");
            jsWriter.append(getRowId());
            jsWriter.append("\", \"cell\": \"");
            jsWriter.append(String.valueOf(getCellIndex()));
            jsWriter.append("\", \"offsetLeft\": ");
            jsWriter.append(String.valueOf(offsetLeft));
            jsWriter.append("}");
            holder.setAttr(attrName, jsWriter.toString());
        }
    }
    
    public static final class RowFrame extends AbstractFocusFrame{
        
        private String rowId;
        
        public RowFrame(final Html grid, final String attrName){
            super(grid,attrName);
        }
        
        public void setRow(final UIObject row){
            this.rowId = row==null ? null : row.getHtmlId();
            writeToJs();
        }

        @Override
        protected String getRowId() {
            return rowId;
        }

        @Override
        protected int getCellIndex() {
            return -1;
        }

        @Override
        public void hide() {
            setRow(null);            
        }
        
    }
    
    public static final class CellFrame extends AbstractFocusFrame{
        
        private String rowId;
        private int cellIndex = -1;
        
        public CellFrame(final Html grid, final String attrName){
            super(grid,attrName);            
        }
        
        public void setCell(final UIObject row, final int cellIndex){
            this.rowId = row==null ? null : row.getHtmlId();
            this.cellIndex = cellIndex;
            writeToJs();
        }

        @Override
        protected String getRowId() {
            return rowId;
        }

        @Override
        protected int getCellIndex() {
            return cellIndex;
        }

        @Override
        public void hide() {
            setCell(null,-1);
        }                        
    }
    
    public static final String SHOW_BORDER_ATTR_NAME = "showborder";
    public static final String RESIZE_ACTION_NAME = "resizeEvent";

    public interface IColumn {

        public static final String INITIAL_WIDTH_ATTR_NAME = "rwt_initialWidth";
        public static final String FIXED_WIDTH_ATTR_NAME = "rwt_fixedWidth";
        public static final String SIZE_POLISY_ATTR_NAME = "policySize";
        public static final String RESIZE_COLUMN_INDEX_ATTR_NAME = "resizingColumnIdx";
        public static final String RESIZABLE_ATTR_NAME = "resizable";

        public int getFixedWidth();

        public boolean isSetFixedWidth();

        public void setFixedWidth(int fw);

        public void unsetFixedWidth();

        public int getInitialWidth();

        public void setInitialWidth(int iw);

        public boolean isSetInitialWidth();

        public void unsetInitialWidth();

        public Object getUserData();

        public void setUserData(Object object);

        public void showSortingIndicator(RadSortingDef.SortingItem.SortOrder direction, int sequenceNumber);

        public void hideSortingIndicator();

        public boolean isSortingIndicatorVisible();

        public IEditingOptions getEditingOptions();

        public void setSizePolicy(EColumnSizePolicy p);

        public EColumnSizePolicy getSizePolicy();

        public boolean isVisible();

        public void setVisible(boolean visible);

        public void setWidth(int width);

        public int getWidth();

        public String getTitle();

        public void setTitle(String title);
        
        public void setIcon(final WpsIcon icon);
        
        public WpsIcon getIcon();
    }
    
    public static class ColumnsResizeController{
        
        private final IGrid grid;
        
        public ColumnsResizeController(final IGrid grid){
            this.grid = grid;
        }
        
        public IGrid.EColumnSizePolicy[] calcFinalSizePolicy(){
            int stretchColumnIndex = -1;
            final int columnsCount = grid.getColumnCount();
            final IGrid.EColumnSizePolicy[] arrColumnsSizePolicy = new IGrid.EColumnSizePolicy[columnsCount];
            for (int i = 0; i < columnsCount; i++) {
                IGrid.EColumnSizePolicy columnResizeMode = grid.getColumn(i).getSizePolicy();
                arrColumnsSizePolicy[i]=columnResizeMode;                
                if (columnResizeMode == IGrid.EColumnSizePolicy.STRETCH && grid.getColumn(i).isVisible()) {
                    if (stretchColumnIndex >= 0) {
                        arrColumnsSizePolicy[i]=IGrid.EColumnSizePolicy.INTERACTIVE;
                    }
                    stretchColumnIndex = i;
                }
            }
            if (stretchColumnIndex < 0) {
                int lastVisibleColumnIdx = -1;
                for (int i = columnsCount - 1; i >= 0; i--) {                    
                    if (lastVisibleColumnIdx < 0 && grid.getColumn(i).isVisible()) {
                        lastVisibleColumnIdx = i;
                    }
                    if (grid.getColumn(i).getSizePolicy() == IGrid.EColumnSizePolicy.INTERACTIVE
                            && grid.getColumn(i).isVisible()) {
                        arrColumnsSizePolicy[i]=IGrid.EColumnSizePolicy.STRETCH;
                        stretchColumnIndex = i;
                        break;
                    }
                }
                if (stretchColumnIndex < 0 && lastVisibleColumnIdx >= 0) {
                    arrColumnsSizePolicy[lastVisibleColumnIdx]=IGrid.EColumnSizePolicy.STRETCH;
                }
            }
            return arrColumnsSizePolicy;
        }
        
        private boolean isAfterStretchSection(final int logicalIndex){
            final int visualIndex = visualIndex(logicalIndex);
            final int startIndex = isRightToLeft() ? visualIndex : 0;
            final int endIndex = isRightToLeft() ? count()-1 : visualIndex;
            for (int i=startIndex; i<=endIndex; i++){
                final int log = logicalIndex(i);
                if (resizeMode(log)==EColumnSizePolicy.STRETCH && !isSectionHidden(log)){
                    return true;
                }
            }
            return false;
        };

        private int previousVisibleSection(final int logicalIndex){
            int visual = visualIndex(logicalIndex);
            while(visual>-1){
                final int result = logicalIndex(--visual);
                if (result<0){
                    break;
                }
                if (!isSectionHidden(result)){
                    return result;
                }
            }
            return -1;
        }
    
        private int nextVisibleSection(final int logicalIndex){
            int visual = visualIndex(logicalIndex);
            while(visual < this.count()) {
                final int result = logicalIndex(++visual);
                if (result<0){
                    break;
                }
                if (!isSectionHidden(result)){
                    return result;
                }
            }
            return -1;
        }
    
        public int findSectionToResize(final int startFrom){
            if (isSectionHidden(startFrom)){
                return -1;
            }
            final boolean forward = isAfterStretchSection(startFrom);
            int section = forward ? nextVisibleSection(startFrom) : startFrom;
            while (section>-1 && resizeMode(section)!= EColumnSizePolicy.INTERACTIVE){
                section = forward ? nextVisibleSection(section) : previousVisibleSection(section);
            }
            if (section>-1){
                int hiddenSections = 0;
                for (int i=0;i<section;i++){
                    if (isSectionHidden(i)){
                        hiddenSections++;
                    }
                }
                section-=hiddenSections;
            }
            return section;
        }
        
        private static int visualIndex(final int logicalIndex){
            return logicalIndex;
        }
        
        private int logicalIndex(final int visualIndex){
            return visualIndex<count() ? visualIndex : -1;
        }
        
        private EColumnSizePolicy resizeMode(final int index){
            return grid.getColumn(index).getSizePolicy();
        }
        
        private boolean isSectionHidden(final int index){
            return !grid.getColumn(index).isVisible();
        }
        
        private int count(){
            return grid.getColumnCount();
        }
        
        private static boolean isRightToLeft(){
            return false;
        }
    }


    public interface CellEditor {

        public void setValue(int r, int c, Object value);

        public Object getValue();

        public void applyChanges();

        public void cancelChanges();

        public UIObject getUI();
    }

    public interface ICell {

        public Html getHtml();

        public Object getUserData();

        public void setUserData(Object object);

        public IEditingOptions getEditingOptions();
    }

    public interface CellRenderer {

        public void update(int r, int c, Object value);

        public void selectionChanged(int r, int c, Object value, ICell cell, boolean isSelected);

        public void rowSelectionChanged(boolean isRowSelected);

        public UIObject getUI();
    }

    public IColumn addColumn(String title);

    public IColumn addColumn(int index, String title);

    public IColumn getColumn(int index);
    
    public void removeColumn(int index);

    public int getColumnCount();

    public String getHeaderSettings();

    public void setHeaderSettings(String settings);
    
    public boolean isBrowserFocusFrameEnabled();
            
    public void setBrowserFocusFrameEnabled(boolean enabled);
    
    public boolean isBorderVisible();
    
    public void setBorderVisible(boolean showBorder);
    
    public void setSelectionStyle(EnumSet<ESelectionStyle> newStyle);
    
    public EnumSet<ESelectionStyle> getSelectionStyle();           
    
    public void setCurrentCellFrameColor(Color color);
    
    public Color getCurrentCellFrameColor();

    public interface CellRendererProvider {

        public CellRenderer newCellRenderer(int r, int c);
    }

    public interface CellEditorProvider {

        public CellEditor newCellEditor(int r, int c);
    }

    public static enum ECellEditingMode {

        NULL_VALUE_ACCEPTED, NULL_VALUE_RESTRICTED, READ_ONLY
    }

    public interface IEditingOptions {

        public EditMask getEditMask();

        public ECellEditingMode getEditingMode();

        public void setEditMask(EditMask editMask);

        public void setEditingMode(ECellEditingMode editMode);
    }

    public interface ColumnDescriptor {

        public String getTitle();
    }
    
}
