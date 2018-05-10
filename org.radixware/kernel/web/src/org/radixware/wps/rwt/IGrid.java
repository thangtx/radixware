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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.enums.EMouseButton;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.events.ClickHtmlEvent;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.events.MouseClickEventFilter;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


public interface IGrid {
    
    public static enum EColumnSizePolicy{
                        
        INTERACTIVE("manual", true),
        BY_CONTENT("content", false),
        WEAK_BY_CONTENT("weak_content", true),
        STRETCH("stretch", false),
        WEAK_STRETCH("weak_stretch", true),
        FIXED("fixed", false);
        
        private final String htmlAttr;
        private final boolean canResize;
        
        private EColumnSizePolicy(final String attr, final boolean canResize){
            htmlAttr = attr;
            this.canResize = canResize;
        }
        
        public String getHtmlAttrValue(){
            return htmlAttr;
        }
        
        public boolean isUserCanResize(){
            return canResize;
        }
        
        static EColumnSizePolicy fromString(final String policyAsStr){            
            for (EColumnSizePolicy policy: EColumnSizePolicy.values()){
                if (policy.getHtmlAttrValue().equals(policyAsStr)){
                    return policy;
                }
            }
            return null;
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

    public static interface IColumn {
        
        public static interface ISizePolicyListener{
            void sizePolicyChanged(IColumn column, EColumnSizePolicy oldPolicy, EColumnSizePolicy newPolicy);
        }        

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
        
        public AbstractColumnHeaderCell getHeaderCell();
        
        public void updateCellsRenderer();
        
        public void setToolTip(final String toolTip);                
        
        public String getToolTip();
        
        public void setPersistenceKey(final String key);
        
        public String getPersistenceKey();
        
        public void setObjectName(final String name);
        
        public String getObjectName();
        
        public void addSizePolicyListener(final ISizePolicyListener listener);
        
        public void removeSizePolicyListener(final ISizePolicyListener listener);
        
        public void setUserCanResizeByContent(final boolean canResize);
        
        public boolean isUserCanResizeByContent();
        
        public void applySettings(final int width, final EColumnSizePolicy sizePolicy);
        
    }
    
    public abstract static class AbstractColumn extends UIObject implements IColumn{
                
        private final AbstractColumnHeaderCell headerCell;
        private boolean isEditable = true;
        private boolean isUserCanResizeByContent;
        
        private int width;
        private EColumnSizePolicy policy;
        private final IEditingOptions editOpts = new EditingOptions(this);  
        private List<ISizePolicyListener> sizePolicyListeners;
        
        public AbstractColumn(final AbstractColumnHeaderCell cell, final String jsClickHandler, final String jsContextMenuHandler){
            super(new Html("td"));
            headerCell = cell;
            getHtml().setCss("padding", "0px");
            getHtml().setCss("border-top", "none");
            getHtml().addClass("rwt-grid-row-cell");            
            getHtml().setAttr("onclick", jsClickHandler);
            getHtml().setAttr("oncontextmenu", jsContextMenuHandler);
            cell.setParent(this);            
            getHtml().add(headerCell.getHtml());            
            
            applySizePolicy(EColumnSizePolicy.INTERACTIVE);
        }
        
        @Override
        public final IEditingOptions getEditingOptions() {
            return editOpts;
        }        
        
        @Override
        public final void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                if (actionParam == null || actionParam.isEmpty()) {
                    processClickEvent(0);
                } else {
                    final int keyModifiers;
                    try {
                        keyModifiers = Integer.parseInt(actionParam);
                    } catch (NumberFormatException exception) {
                        processClickEvent(0);
                        return;
                    }
                    processClickEvent(keyModifiers);
                }
            } else if ("resizeByContent".equals(actionName) && isUserCanResizeByContent()){
                setSizePolicy(EColumnSizePolicy.WEAK_BY_CONTENT);
            } else {
                super.processAction(actionName, actionParam);
            }
        }
        
        @Override
        public final void setIcon(final WpsIcon icon) {
            headerCell.setIcon(icon);
        }

        @Override
        public final WpsIcon getIcon() {
            return headerCell.getIcon();
        }

        @Override
        public final void setWidth(final int w) {
            headerCell.getHtml().setCss("width", String.valueOf(w) + "px");
            headerCell.getHtml().setAttr("width", w);
            width = w;
        }

        @Override
        public int getWidth() {
            return width;
        }                

        @Override
        public final Object getUserData() {
            return headerCell.getUserData();
        }

        @Override
        public final void setUserData(final Object userData) {
            headerCell.setUserData(userData);
        }

        @Override
        public final String getTitle() {
            return headerCell.getTitle();
        }

        @Override
        public final void setTitle(final String title) {
            headerCell.setTitle(title);
        }
        
        public final boolean isEditable() {
            return isEditable;
        }

        public final void setEditable(boolean editable) {
            this.isEditable = editable;
        }

        public final boolean getEditable() {
            return isEditable();
        }
        
        @Override
        public final int getFixedWidth() {
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
        public final boolean isSetFixedWidth() {
            return html.getAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME) != null;
        }

        @Override
        public final void setFixedWidth(int fw) {
            html.setAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME, fw);
            setSizePolicy(IGrid.EColumnSizePolicy.FIXED);
        }

        @Override
        public final void unsetFixedWidth() {
            html.setAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME, null);
            setSizePolicy(IGrid.EColumnSizePolicy.INTERACTIVE);
        }

        @Override
        public final int getInitialWidth() {
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
        public final boolean isSetInitialWidth() {
            return html.getAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME) != null;
        }

        @Override
        public final void setInitialWidth(int iw) {
            html.setAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME, iw);
        }

        @Override
        public final void unsetInitialWidth() {
            html.setAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME, null);
        }

        @Override
        public final void showSortingIndicator(final RadSortingDef.SortingItem.SortOrder direction,
                final int sequenceNumber) {
            headerCell.setSorting(direction, sequenceNumber);
        }

        @Override
        public final void hideSortingIndicator() {
            headerCell.setSorting(null, -1);
        }

        @Override
        public final boolean isSortingIndicatorVisible() {
            return headerCell.getSortingDirection()!=null;            
        }        
        
        @Override
        public final void setSizePolicy(final EColumnSizePolicy newPolicy) {
            if (policy!=newPolicy){
                if (newPolicy==EColumnSizePolicy.FIXED && !isSetFixedWidth()){
                    return;
                }
                applySizePolicy(newPolicy);
                afterChangeSizePolicy();
            }
        }
        
        public final void applySizePolicy(final EColumnSizePolicy newPolicy){
            if (policy!=newPolicy){
                getHtml().setAttr(IGrid.IColumn.SIZE_POLISY_ATTR_NAME,newPolicy.getHtmlAttrValue());
                final EColumnSizePolicy previousPolicy = policy;
                policy = newPolicy;
                updateCanResizeByContentAttribute();
                notifySizePolicyListeners(previousPolicy, newPolicy);
            }
        }

        @Override
        public final EColumnSizePolicy getSizePolicy() {
            return policy;
        }
        
        @Override
        public final AbstractColumnHeaderCell getHeaderCell(){
            return headerCell;
        }  

        @Override
        public UIObject findObjectByHtmlId(final String id) {
            final UIObject result = super.findObjectByHtmlId(id);
            return result==null ? headerCell.findObjectByHtmlId(id) : result;
        }

        @Override
        public void visit(final Visitor visitor) {
            super.visit(visitor);
            headerCell.visit(visitor);
        }                
        
        private void processClickEvent(final int buttonsMask) {
            final EnumSet<EMouseButton> mouseButtons = EMouseButton.fromAwtBitMask(buttonsMask);
            if (mouseButtons.contains(EMouseButton.LEFT)) {
                final EnumSet<EKeyboardModifier> keyModifiers = EKeyboardModifier.fromAwtBitMask(buttonsMask);
                if (keyModifiers.contains(EKeyboardModifier.CTRL) && isUserCanResizeByContent()){
                    setSizePolicy(EColumnSizePolicy.WEAK_BY_CONTENT);
                }else{
                    notifyColumnHeaderClick(EKeyboardModifier.fromAwtBitMask(buttonsMask));
                }
            } else if (mouseButtons.contains(EMouseButton.RIGHT)) {
                setupColumnsVisiblity();
            }
        }
        
        @Override
        public void addSizePolicyListener(final ISizePolicyListener listener){
            if (sizePolicyListeners==null){
                sizePolicyListeners = new LinkedList<>();
            }
            if (listener!=null && !sizePolicyListeners.contains(listener)){
                sizePolicyListeners.add(listener);
            }
        }
        
        @Override
        public void removeSizePolicyListener(final ISizePolicyListener listener){
            if (sizePolicyListeners!=null){
                sizePolicyListeners.remove(listener);
                if (sizePolicyListeners.isEmpty()){
                    sizePolicyListeners = null;
                }
            }
        }
        
        private void notifySizePolicyListeners(final EColumnSizePolicy oldSizePolicy, final EColumnSizePolicy newSizePolicy){
            if (sizePolicyListeners!=null){
                final List<ISizePolicyListener> listeners = new LinkedList<>(sizePolicyListeners);
                for (ISizePolicyListener listener: listeners){
                    listener.sizePolicyChanged(this, oldSizePolicy, newSizePolicy);
                }
            }
        }

        @Override
        public void setUserCanResizeByContent(final boolean canResize) {
            if (isUserCanResizeByContent!=canResize){
                isUserCanResizeByContent = canResize;
                updateCanResizeByContentAttribute();
            }
        }

        @Override
        public boolean isUserCanResizeByContent() {
            return isUserCanResizeByContent && getSizePolicy()==EColumnSizePolicy.INTERACTIVE;
        }           

        @Override
        public void applySettings(final int width, final EColumnSizePolicy sizePolicy) {
            if (sizePolicy==EColumnSizePolicy.WEAK_BY_CONTENT){
                setSizePolicy(EColumnSizePolicy.WEAK_BY_CONTENT);
            }else if (width>0){
                setSizePolicy(EColumnSizePolicy.INTERACTIVE);
                setInitialWidth(width);
            }
        }                
        
        private void updateCanResizeByContentAttribute(){
            if (isUserCanResizeByContent()){
                getHtml().setAttr("canresizebycontent", true);
            }else{
                getHtml().setAttr("canresizebycontent", null);
            }
        }
        
        protected abstract void afterChangeSizePolicy();
        
        protected abstract void setupColumnsVisiblity();
        
        protected abstract void notifyColumnHeaderClick(EnumSet<EKeyboardModifier> modifier);
    }
    
    public abstract static class AbstractHorizontalHeader<T extends UIObject & IGrid.IColumn> extends UIObject implements Iterable<T>{
        
        private static class SectionSettings{
            
            private final String columnKey;
            private final int width;            
            private final EColumnSizePolicy sizePolicy;
            
            private SectionSettings(final String columnId, final int width, final EColumnSizePolicy sizePolicy){
                this.columnKey = columnId;
                this.width = width;
                this.sizePolicy = sizePolicy;
            }

            public String getKey() {
                return columnKey;
            }

            public int getWidth() {
                return width;
            }

            public EColumnSizePolicy getSizePolicy() {
                return sizePolicy;
            }
                                    
            public static List<SectionSettings> parse(final String sectionsSetting, final IClientEnvironment environment){
                if (sectionsSetting==null || sectionsSetting.isEmpty()){
                    return Collections.emptyList();
                }else{
                    String[] arrHeaderSettings = sectionsSetting.split(";");
                    final List<SectionSettings> result = new LinkedList<>();
                    if (arrHeaderSettings != null) {
                        int colonIndex, commaIndex;
                        String columnId;
                        String columnWidthAndPolicy;
                        String columnSizePolicyAsStr;
                        int columnWidth;
                        EColumnSizePolicy columnSizePolicy;
                        for (int i = 0; i < arrHeaderSettings.length; i++) {
                            colonIndex = arrHeaderSettings[i].indexOf(':');
                            if (colonIndex<=0){
                                final String messageTemplate = 
                                    environment.getMessageProvider().translate("TraceMessage", "Unable to restore column settings from string '%1$s'");
                                environment.getTracer().debug(String.format(messageTemplate, arrHeaderSettings[i]));
                                continue;
                            }                            
                            columnId = arrHeaderSettings[i].substring(0, colonIndex);
                            columnWidthAndPolicy = arrHeaderSettings[i].substring(colonIndex+1);
                            commaIndex = columnWidthAndPolicy.indexOf(',');
                            if (commaIndex<=0){
                                final String messageTemplate = 
                                    environment.getMessageProvider().translate("TraceMessage", "Unable to restore column settings from string '%1$s'");
                                environment.getTracer().debug(String.format(messageTemplate, arrHeaderSettings[i]));
                                continue;                                
                            }
                            try{
                                columnWidth = Integer.parseInt(columnWidthAndPolicy.substring(0, commaIndex));
                            }catch(NumberFormatException error){
                                final String messageTemplate = 
                                    environment.getMessageProvider().translate("TraceMessage", "Failed to restore column width from string '%1$s'");
                                environment.getTracer().debug(String.format(messageTemplate, columnWidthAndPolicy), error);
                                continue;
                            }
                            columnSizePolicyAsStr = columnWidthAndPolicy.substring(commaIndex+1);
                            columnSizePolicy = EColumnSizePolicy.fromString(columnSizePolicyAsStr);
                            if (columnSizePolicy==null){
                                final String messageTemplate = 
                                    environment.getMessageProvider().translate("TraceMessage", "Unable to restore column resizing policy from string '%1$s'");
                                environment.getTracer().debug(String.format(messageTemplate, columnSizePolicyAsStr));
                                continue;
                            }
                            result.add(new SectionSettings(columnId, columnWidth, columnSizePolicy));
                        }
                    }
                    return result;
                }
            }
        }
                
        private final Html tr;
        private final List<T> columns = new ArrayList<>();
        private final String tableId;
        private Alignment headerAlignment;
        
        public AbstractHorizontalHeader(){
            super(new Div());
            final Html table = new Html("table");
            tableId = table.getId();
            table.setCss("position", "relative");
            final Html head = new Html("thead");
            
            tr = new Html("tr");
            table.add(head);
            head.add(tr);
            
            table.setCss("cellspacing", "0px");
            table.setCss("cellpadding", "0px");
            table.setCss("border-collapse", "collapse");
            table.setCss("border", "none");            
            table.setCss("table-layout", "fixed");
            
            getHtml().add(table);
            getHtml().setCss("overflow", "hidden");
            getHtml().setCss("position", "relative");
            getHtml().addClass("rwt-grid-horizontal-header-panel");
            getHtml().addClass("rwt-grid-header-panel");
            getHtml().addClass("header");
            getHtml().setAttr("role", "header");                   
        }


        public final int getColumnIndex(final T c) {
            return columns.indexOf(c);
        }
        
        public final T getColumn(final int index){
            return columns.get(index);
        }
        
        public final int getColumnsCount(){
            return columns.size();
        }        
        
        @Override
        public final Iterator<T> iterator() {
            return columns.iterator();
        }
        
        public final T addColumn(final int index, final String title, final AbstractColumnHeaderCell columnHeaderCell) {

            final int actualIndex;
            if (index < 0 || index >= columns.size()) {
                actualIndex = columns.size();
            } else {
                actualIndex = index;
            }
            
            final IGrid.AbstractColumnHeaderCell headerCell = columnHeaderCell==null ? new DefaultColumnHeaderCell() : columnHeaderCell;
            
            final T c = createColumn(headerCell);
            columns.add(actualIndex, c);
            
            c.setTitle(title);
            c.setParent(this);

            this.tr.add(actualIndex, c.getHtml());
            return c;
        }

        public final void removeColumn(final int index) {
            T c = columns.remove(index);
            this.tr.remove(c.html);
            c.setParent(null);
        }

        private void updateHeaderAlign(final Alignment a) {
            if (columns != null && !columns.isEmpty()) {
                for (T c : columns) {
                    c.getHeaderCell().setTextAlignment(a);
                }
            }
        }    
        
        public final void setTextAlignment(final Alignment a) {
            if (!Objects.equals(a, headerAlignment)){
                this.headerAlignment = a;
                updateHeaderAlign(a);
            }            
        }

        public final Alignment getTextAlignment() {
            return headerAlignment;
        }
        
        @Override
        public final void visit(final Visitor visitor) {
            super.visit(visitor);
            if (columns != null) {
                for (T c : columns) {
                    c.visit(visitor);
                }
            }
        }

        @Override
        public final UIObject findObjectByHtmlId(final String id) {
            UIObject result = super.findObjectByHtmlId(id);            
            if (result != null) {
                return result;
            }
            if (Objects.equals(id, tableId)){
                return this;
            }
            if (columns != null) {
                for (T c : columns) {
                    result = c.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }

        @Override
        public final void processAction(final String actionName, final String actionParam) {
            if (IGrid.RESIZE_ACTION_NAME.equals(actionName) & actionParam != null) {
                final List<SectionSettings> sectionSettings = SectionSettings.parse(actionParam, getEnvironment());
                if (sectionSettings!=null){
                    T column;
                    for (SectionSettings setting: sectionSettings){
                        column = findColumnByPersistenceKey(setting.getKey());
                        if (column!=null){
                            column.setSizePolicy(setting.getSizePolicy());
                        }
                    }
                    updateStretchPolicy();
                    for (SectionSettings setting: sectionSettings){
                        column = findColumnByPersistenceKey(setting.getKey());
                        if (column!=null && column.getSizePolicy()==EColumnSizePolicy.INTERACTIVE){
                            column.setWidth(setting.getWidth());
                        }
                    }
                }
            }
            super.processAction(actionName, actionParam);
        }        
        
        public final String getSettings(){
            final StringBuilder settingsBuilder = new StringBuilder();
            for (T column: columns){
                if (settingsBuilder.length()>0){
                    settingsBuilder.append(';');
                }
                if (column.getPersistenceKey()!=null){
                    settingsBuilder.append(column.getPersistenceKey());
                    settingsBuilder.append(':');
                    if (column.getSizePolicy()==EColumnSizePolicy.INTERACTIVE || column.getSizePolicy()==EColumnSizePolicy.WEAK_BY_CONTENT){
                        settingsBuilder.append(String.valueOf(column.getWidth()));
                    }else{
                        settingsBuilder.append('0');
                    }
                    settingsBuilder.append(',');
                    settingsBuilder.append(column.getSizePolicy().getHtmlAttrValue());
                }
            }
            return settingsBuilder.toString();
        }
        
        public final void setSettings(final String headerSettings){
            if (headerSettings != null && !headerSettings.isEmpty()) {
                final List<SectionSettings> sectionSettings = SectionSettings.parse(headerSettings, getEnvironment());
                if (sectionSettings!=null){
                    T column;
                    for (SectionSettings setting: sectionSettings){
                        column = findColumnByPersistenceKey(setting.getKey());
                        if (column!=null){
                            column.applySettings(setting.getWidth(), setting.getSizePolicy());
                        }
                    }
                }
            }
        }
        
        private T findColumnByPersistenceKey(final String key){
            if (key==null || key.isEmpty()){
                return null;
            }
            for (T c : columns) {
                if (key.equals(c.getPersistenceKey())){
                    return c;
                }
            }
            return null;
        }
        
        private boolean updateStretchPolicy(){
            boolean sizePolicyWasChanged = false;
            T lastVisibleColumn = null;
            for (int i=columns.size()-1; i>=0; i--){
                if (columns.get(i).isVisible()){
                    lastVisibleColumn = columns.get(i);
                    break;
                }
            }
            if (lastVisibleColumn!=null){
                for (T column: columns){
                    if (column!=lastVisibleColumn && column.getSizePolicy()==EColumnSizePolicy.WEAK_STRETCH){                    
                        column.setSizePolicy(EColumnSizePolicy.INTERACTIVE);
                        sizePolicyWasChanged = true;                    
                    }
                }
                if (sizePolicyWasChanged && lastVisibleColumn.getSizePolicy()!=EColumnSizePolicy.STRETCH && lastVisibleColumn.getSizePolicy()!=EColumnSizePolicy.WEAK_STRETCH){
                    lastVisibleColumn.setSizePolicy(EColumnSizePolicy.WEAK_STRETCH);
                }
            }
            return sizePolicyWasChanged;
        }        
        
        public abstract T createColumn(final AbstractColumnHeaderCell headerCell);
    }
    
    public static class ColumnsResizeController{
        
        private final IGrid grid;
        
        public ColumnsResizeController(final IGrid grid){
            this.grid = grid;
        }
        
        public EColumnSizePolicy[] calcFinalSizePolicy(){
            int stretchColumnIndex = -1;
            final int columnsCount = grid.getColumnCount();
            final EColumnSizePolicy[] arrColumnsSizePolicy = new EColumnSizePolicy[columnsCount];
            for (int i = 0; i < columnsCount; i++) {
                EColumnSizePolicy columnResizeMode = grid.getColumn(i).getSizePolicy();
                arrColumnsSizePolicy[i]=columnResizeMode;                
                if ((columnResizeMode ==EColumnSizePolicy.STRETCH || columnResizeMode==EColumnSizePolicy.WEAK_STRETCH) && grid.getColumn(i).isVisible()) {
                    if (stretchColumnIndex >= 0) {
                        arrColumnsSizePolicy[stretchColumnIndex]=EColumnSizePolicy.INTERACTIVE;
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
                    if (grid.getColumn(i).getSizePolicy() == EColumnSizePolicy.INTERACTIVE
                            && grid.getColumn(i).isVisible()) {
                        arrColumnsSizePolicy[i]=EColumnSizePolicy.WEAK_STRETCH;
                        stretchColumnIndex = i;
                        break;
                    }
                }
                if (stretchColumnIndex < 0 && lastVisibleColumnIdx >= 0) {
                    arrColumnsSizePolicy[lastVisibleColumnIdx]=EColumnSizePolicy.WEAK_STRETCH;
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
            while (section>-1 && !resizeMode(section).isUserCanResize()){
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
        
        public void updateRenderer();
    }

    public interface CellRenderer {

        public void update(int r, int c, Object value);

        public void selectionChanged(int r, int c, Object value, ICell cell, boolean isSelected);

        public void rowSelectionChanged(boolean isRowSelected);

        public UIObject getUI();
    }
    
    public static abstract class AbstractHeaderCell extends UIObject{
        
        private Object userData; 
        private Alignment textAlignment;
        
        public AbstractHeaderCell(){
            super(new Div());
            getHtml().addClass("header-cell");
        }
        
        public abstract String getTitle();
        public abstract void setTitle(final String title);
        
        public Alignment getTextAlignment() {
            return textAlignment;
        }

        
        public void setTextAlignment(final Alignment alignment) {
            if (!Objects.equals(alignment, textAlignment)){
                textAlignment = alignment;
                getHtml().setCss("text-align", alignment==null ? null : alignment.name());
            }
        }        
        
        public Object getUserData(){
            return userData;
        }
        
        public void setUserData(final Object userData){
            this.userData = userData;
        }
    }
    
    public static abstract class AbstractColumnHeaderCell extends AbstractHeaderCell{
        
        private WpsIcon icon;
        private RadSortingDef.SortingItem.SortOrder sortingDirection;
        private int sortingSequenceNumber;
        private boolean isResizable;        
        private final Html rightResizeHandle = new Html("span");//resize this cell
        private final Html leftResizeHandle = new Html("span");//resize previous cell
        
        public AbstractColumnHeaderCell(){
            super();            
            
            getHtml().add(rightResizeHandle);
            getHtml().add(leftResizeHandle);
            setupResizeHandle(rightResizeHandle);
            rightResizeHandle.addClass("header-right-handle");
            rightResizeHandle.setCss("padding-left", "0");
            setupResizeHandle(leftResizeHandle);
            leftResizeHandle.addClass("header-left-handle");
            leftResizeHandle.setCss("padding-right", "0");
        }
        
        private static void setupResizeHandle(final Html resizeHandle){
            resizeHandle.setCss("width", "1px");
            resizeHandle.setCss("height", "100%");            
            resizeHandle.setCss("padding-top", "0");
            resizeHandle.addClass("header-handle");
        }
        
        public void setIcon(final WpsIcon icon){
            this.icon = icon;            
        }
        
        public WpsIcon getIcon(){
            return icon;
        }
        
        public void setSorting(final RadSortingDef.SortingItem.SortOrder sortingDirection, final int sortingSequenceNumber){
            this.sortingDirection = sortingDirection;
            this.sortingSequenceNumber = sortingSequenceNumber;
        }
        
        public RadSortingDef.SortingItem.SortOrder getSortingDirection(){
            return sortingDirection;
        }
        
        public int getSortingSequenceNumber(){
            return sortingSequenceNumber;
        }
        
        public final void setResizable(final boolean resizable){
            isResizable = resizable;
            rightResizeHandle.setAttr(IGrid.IColumn.RESIZABLE_ATTR_NAME, resizable);
        }
        
        public final void setPrevCellResizable(final boolean resizable){
            leftResizeHandle.setAttr(IGrid.IColumn.RESIZABLE_ATTR_NAME, resizable);
        }
        
        public final boolean isResizable(){
            return isResizable;
        }
        
        protected final Html getResizeHandle(){
            return rightResizeHandle;
        }                
    }        
        
    
    public static class DefaultColumnHeaderCell extends AbstractColumnHeaderCell{
                
        private final Html title = new Html("label");        
        private Html srtDirectionIndicator;
        private Html srtSequenceIndicator;        
        private Div imageContainer;        
        
        public DefaultColumnHeaderCell(){
            super();
            
            getHtml().setCss("overflow", "hidden");
            getHtml().setCss("padding-top", "3px");
            getHtml().setCss("vertical-align", "middle");
            getHtml().setCss("width", "100%");
            
            getHtml().add(title);
            title.addClass("rwt-ui-element");
            title.setCss("white-space", "nowrap");
            title.setCss("cursor", "pointer");
        }

        @Override
        public void setTitle(final String title) {
            this.title.setInnerText(title);
        }

        @Override
        public String getTitle() {
            return title.getInnerText();
        }
               
        @Override
        public void setIcon(final WpsIcon icon) {
            if (!Objects.equals(icon, getIcon())){
                super.setIcon(icon);
            
                if (icon == null) {
                    if (imageContainer!=null){
                        imageContainer.remove();
                        imageContainer = null;
                    }
                } else {                    
                    if (imageContainer == null) {
                        imageContainer = createImageContainer();
                        getHtml().add(imageContainer);
                    }
                    final Html image = imageContainer.getChildAt(0);                    
                    final String imageUri = getIcon().getURI(this);
                    if (!Objects.equals(image.getAttr("src"), imageUri)) {
                        image.setAttr("src", imageUri);
                    }                
                }                
            }
        }    
        
        private static Div createImageContainer(){
            final Div result = new Div();
            final Html image = new Html("img");
            result.add(image);
            image.setCss("width", "12px");
            image.setCss("height", "12px");

            result.setCss("vertical-align", "middle");
            result.setCss("padding-top", "3px");
            result.setCss("display", "inline");
            result.setCss("margin-right", "3px");            
            return result;
        }

        @Override
        public void setSorting(final RadSortingDef.SortingItem.SortOrder sortingDirection, final int sortingSequenceNumber) {
            super.setSorting(sortingDirection, sortingSequenceNumber);
            
            if (sortingSequenceNumber > 0) {
                if (srtSequenceIndicator == null) {
                    srtSequenceIndicator = createSrtSequenceIndicator();
                    getHtml().add(srtSequenceIndicator);
                }
                srtSequenceIndicator.setCss("visibility", null);
                srtSequenceIndicator.setInnerText(String.valueOf(sortingSequenceNumber));
            } else if (srtSequenceIndicator != null) {
                srtSequenceIndicator.setCss("visibility", "hidden");
            }
            
            if (sortingDirection != null) {
                if (srtDirectionIndicator==null){
                    srtDirectionIndicator = createSrtDirectionIndicator();
                    getHtml().add(srtDirectionIndicator);
                }
                srtDirectionIndicator.setCss("visibility", null);
                srtDirectionIndicator.setInnerText(getSortingDirectionIndicatorSymbol(sortingDirection));
            }else if (srtDirectionIndicator!=null){
                srtDirectionIndicator.setCss("visibility", "hidden");
            }
        }
        
        private static String getSortingDirectionIndicatorSymbol(final RadSortingDef.SortingItem.SortOrder direction){
            return direction==RadSortingDef.SortingItem.SortOrder.ASC ? SelectorSortUtils.ASC_ARROW : SelectorSortUtils.DESC_ARROW;
        }        
        
        protected final Html getTitleHtml(){
            return title;
        }
        
        protected final Html getSortingDirectionIndicatorHtml(){
            return srtDirectionIndicator;
        }
        
        protected final Html getSortingSequenceIndicatorHtml(){
            return srtSequenceIndicator;
        }
        
        protected final Div getImageContainer(){
            return imageContainer;
        }
        
        protected Html createSrtSequenceIndicator() {
            final Html result = createSrtIndicator();
            result.setCss("font-size", "10px");
            return result;
        }

        protected Html createSrtDirectionIndicator() {
            final Html result = createSrtIndicator();
            result.setCss("font-size", "14px");
            return result;
        }

        protected Html createSrtIndicator() {
            final Html srtIndicator = new Html("label");            
            srtIndicator.addClass("rwt-ui-element");
            srtIndicator.setCss("white-space", "nowrap");
            srtIndicator.setCss("float", "right");
            srtIndicator.setCss("color", "dimgray");
            srtIndicator.setCss("font-weight", "bold");
            srtIndicator.setCss("height", "100%");
            return srtIndicator;
        }                        
    }        
    
    public static class SelectableColumnHeaderCell extends DefaultColumnHeaderCell{
        
        public static interface SelectionListener{
            void selectionChanged(SelectableColumnHeaderCell cell, Boolean isSelected, boolean changedByUser);
        }
        
        private final Html checkBoxIndicator = new Html("label");
        private final Html checkBox = new Div();
        private Boolean checked = Boolean.FALSE;
        private List<SelectionListener> listeners;
        
        public SelectableColumnHeaderCell(){                        
            super();
            getHtml().add(0, checkBox);
            
            getTitleHtml().setCss("position", "relative");
            getTitleHtml().setCss("display", "inline-block");
            
            checkBox.setCss("display", "inline-block");                
            checkBox.setCss("width", "12px");
            checkBox.setCss("height", "12px");
            checkBox.setCss("margin-bottom", "2px");
            checkBox.setCss("background-color", "white");
            checkBox.setCss("vertical-align", "middle");
            checkBox.setCss("border", "solid 1px #BBB");
            checkBox.setAttr("onclick", "default");
            
            checkBoxIndicator.setCss("vertical-align", "text-top");
            checkBoxIndicator.addClass("rwt-ui-element-text");
            checkBoxIndicator.setAttr("onclick", "default");
            checkBox.add(checkBoxIndicator);
            setCheckState(Boolean.FALSE);
            updateCheckBoxAlign();            
        }

        @Override
        public void setTitle(final String title) {
            super.setTitle(title);
            updateCheckBoxAlign();
        }
        
        private void updateCheckBoxAlign(){
            final String title = getTitle();
            if (title==null || title.isEmpty()){
                checkBox.setCss("padding-left", null);
                checkBox.setCss("padding-right", null);
                getHtml().setCss("marging", "auto");
            }else{
                getHtml().setCss("marging", null);
            }
        }
        
        public void setUserCanSelect(final boolean canSelect){
            checkBoxIndicator.setAttr("onclick", canSelect ? "default" : null);
        }
        
        public boolean isUserCanSelect(){
            return checkBoxIndicator.getAttr("onclick")!=null;
        }
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)){
                setCheckState(isUnselected() ? Boolean.TRUE : Boolean.FALSE);
                notifySelectionListeners(checked, true);
            }
            super.processAction(actionName, actionParam);
        }
        
        @Override
        public UIObject findObjectByHtmlId(final String id) {
            if (getHtmlId().equals(id) || checkBox.getId().equals(id) || checkBoxIndicator.getId().equals(id)){
                return this;
            }else{
                return null;
            }
        }
        
        public void setSelected(){
            if (!isSelected()){
                setCheckState(Boolean.TRUE);
                notifySelectionListeners(checked, false);
            }
        }
        
        public boolean isSelected(){
            return checked==Boolean.TRUE;
        }
        
        public void setPartiallySelected(){
            if (!isPartiallySelected()){
                setCheckState(null);
                notifySelectionListeners(checked, false);
            }
        }
        
        public boolean isPartiallySelected(){
            return checked==null;
        }
        
        public void setUnselected(){
            if (!isUnselected()){
                setCheckState(Boolean.FALSE);
                notifySelectionListeners(checked, false);
            }
        }
        
        private static String getCheckStateSymbol(final Boolean checkState){
            return checkState==Boolean.FALSE ? "\u200B" : "\u2714";
        }
        
        private void setCheckState(final Boolean checkState){
            checked = checkState;
            checkBoxIndicator.setInnerText(getCheckStateSymbol(checkState));
            setCheckBoxForeground(checkState==null ? Color.gray : Color.black);
        }
        
        public boolean isUnselected(){
            return checked==Boolean.FALSE;
        }
        
        private void setCheckBoxForeground(final Color c){
            checkBox.setCss("color", c == null ? null : color2Str(c));
            checkBoxIndicator.setCss("color", c == null ? null : color2Str(c));
        }
        
        public void addSelectionListener(final SelectionListener listener){
            if (listener!=null){
                if (listeners==null){
                    listeners = new LinkedList<>();
                }
                if (!listeners.contains(listener)){
                    listeners.add(listener);
                }
            }
        }
        
        public void removeSelectionListener(final SelectionListener listener){
            if (listener!=null && listeners!=null){
                listeners.remove(listener);
                if (listeners.isEmpty()){
                    listeners = null;
                }
            }
        }
        
        private void notifySelectionListeners(final Boolean isSelected, boolean changedByUser){
            if (listeners!=null){
                final List<SelectionListener> selectionListeners = new LinkedList<>(listeners);
                for (SelectionListener listener: selectionListeners){
                    listener.selectionChanged(this, isSelected, changedByUser);
                }
            }
        }
    }
    
    public abstract static class AbstractRowHeaderCell extends IGrid.AbstractHeaderCell{
        
        public AbstractRowHeaderCell(){
            super();
        }
    }
    
    public static class DefaultRowHeaderCell extends AbstractRowHeaderCell{
        
        private Html title = new Html("label");
        
        public DefaultRowHeaderCell(){
            super();
            getHtml().setCss("overflow", "hidden");
            getHtml().setCss("padding-top", "3px");
            getHtml().setCss("vertical-align", "middle");
            getHtml().setCss("width", "100%");     
            
            getHtml().add(title);
            title.addClass("rwt-ui-element");
            title.setCss("white-space", "nowrap");             
        }

        @Override
        public void setTitle(final String title){
            this.title.setInnerText(title);
        }

        @Override
        public String getTitle(){
            return title.getInnerText();
        }                
    }    
    
    public static abstract class CornerHeaderCell extends AbstractContainer{
        
        public static interface ClickListener{
            void onClick(final EnumSet<EKeyboardModifier> modifiers);
        }
        
        public static interface DoubleClickListener{
            void onDoubleClick(final EnumSet<EKeyboardModifier> modifiers);
        }
        
        private List<ClickListener> clickListeners;
        private List<DoubleClickListener> dblClickListeners;
        
        public CornerHeaderCell(){
            super();            
            getHtml().addClass("rwt-grid-header-corner-cell");
        }
        
        public void addClickListener(final ClickListener listener){
            if (listener!=null){
                if (clickListeners==null){
                    clickListeners = new LinkedList<>();
                    html.setAttr("onclick", "$_GF.onCornerHeaderCellClick");
                }
                if (!clickListeners.contains(listener)){
                    clickListeners.add(listener);
                }                
            }
            
        }
        
        public void removeClickListener(final ClickListener listener){
            if (listener!=null && clickListeners!=null){
                clickListeners.remove(listener);
                if (clickListeners.isEmpty()){
                    clickListeners = null;
                    html.setAttr("onclick", null);
                }
            }
        }
        
        private void notifyClickListenres(final EnumSet<EKeyboardModifier> modifiers){
            if (clickListeners!=null){
                final List<ClickListener> listeners = new LinkedList<>(clickListeners);
                for (ClickListener listener: listeners){
                    listener.onClick(modifiers);
                }
            }
        }                

        public void addDoubleClickListener(final DoubleClickListener listener){
            if (listener!=null){
                if (dblClickListeners==null){
                    dblClickListeners = new LinkedList<>();
                    html.setAttr("ondblclick", "$_GF.onCornerHeaderCellDblClick");
                }
                if (!dblClickListeners.contains(listener)){
                    dblClickListeners.add(listener);
                }
            }
            
        }
        
        public void removeDoubleListener(final DoubleClickListener listener){
            if (listener!=null && dblClickListeners!=null){
                dblClickListeners.remove(listener);
                if (dblClickListeners.isEmpty()){
                    dblClickListeners = null;
                    html.setAttr("ondblclick", null);
                }
            }
        }
        
        private void notifyDblClickListenres(final EnumSet<EKeyboardModifier> modifiers){
            if (dblClickListeners!=null){
                final List<DoubleClickListener> listeners = new LinkedList<>(dblClickListeners);
                for (DoubleClickListener listener: listeners){
                    listener.onDoubleClick(modifiers);
                }
            }
        } 

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)){
                notifyClickListenres(EKeyboardModifier.parseFromString(actionParam));
            } else if (Events.EVENT_NAME_ONDBLCLICK.equals(actionName)){
                notifyDblClickListenres(EKeyboardModifier.parseFromString(actionParam));
            } else{
                super.processAction(actionName, actionParam);
            }
        }
                        
        public abstract void setTitle(String title);
        public abstract String getTitle();
    }        
    
    public static class DefaultCornerHeaderCell extends CornerHeaderCell{
        
        private Html title = new Html("label");        
        
        public DefaultCornerHeaderCell(){
            super();
            getHtml().setCss("padding-top", "3px");
            getHtml().setCss("vertical-align", "middle");
            
            getHtml().add(title);
            title.addClass("rwt-ui-element");
            title.setCss("white-space", "nowrap");
            title.setCss("display", "block");
            title.setCss("text-align", "center");
            
            getHtml().addClass("rwt-grid-header-panel");
        }

        @Override
        public void setTitle(final String title) {
            this.title.setInnerText(title);
        }

        @Override
        public String getTitle() {
            return title.getInnerText();
        }                
    }        
    
    static class VerticalHeaderCell extends UIObject {

        private final AbstractRowHeaderCell cell;
        private final AbstractRowHeader header;
        
        public VerticalHeaderCell(final AbstractRowHeader verticalHeader, final AbstractRowHeaderCell headerCell){
            super(new Html("tr"));
            getHtml().addClass("rwt-grid-row-header");
            header = verticalHeader;
            cell = headerCell;
            
            final Html td = new Html("td");
            html.add(td);         
            td.setCss("padding", "0px");
            td.addClass("rwt-grid-row-cell");
            td.addClass("rwt-grid-header-panel");
            cell.setParent(this);
            td.add(cell.getHtml());
            setParent(verticalHeader);
        }
        
        public String getTitle(){
            return cell.getTitle();
        }
        
        public void setTitle(final String title){
            cell.setTitle(title);     
        }
        
        public Object getUserData(){
            return cell.getUserData();
        }
        
        public void setUserData(final Object object){
            cell.setUserData(object);
        }

        @Override
        public UIObject findObjectByHtmlId(final String id) {
            final UIObject result = super.findObjectByHtmlId(id);
            if (result!=null){
                return result;
            }
            return cell.findObjectByHtmlId(id);
        }

        @Override
        public void visit(final Visitor visitor) {
            super.visit(visitor);
            cell.visit(visitor);
        }        

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)){
                header.notifyClick(this, EKeyboardModifier.parseFromString(actionParam));
            }else if (Events.EVENT_NAME_ONDBLCLICK.equals(actionName)){
                header.notifyDoubleClick(this, EKeyboardModifier.parseFromString(actionParam));
            }else{
                super.processAction(actionName, actionParam);
            }
        }
    }    
    
    public static abstract class AbstractRowHeader extends UIObject{
        
        private IGrid.CornerHeaderCell cornerCell = new IGrid.DefaultCornerHeaderCell();
        private Html thead = new Html("thead");
        private Div tableContainer = new Div();
        
        public AbstractRowHeader(final UIObject parent){
            super(new Div());           
            
            getHtml().addClass("rwt-grid-vertical-header-panel");
            getHtml().setCss("position", "relative");
            getHtml().setCss("overflow", "hidden");
            getHtml().setCss("float", "left");
            getHtml().add(cornerCell.getHtml());
            
            tableContainer.setCss("position", "relative");
            tableContainer.setCss("overflow", "hidden");
            getHtml().add(tableContainer);
            
            final Html table = new Html("table");
            setHSizePolicy(SizePolicy.EXPAND);
            table.setCss("position", "relative");            
            table.add(thead);
            
            table.setCss("border-collapse", "collapse");
            table.setCss("border", "none");            
            table.setCss("table-layout", "fixed");                        
            table.setCss("width","100%");
            
            tableContainer.add(table);                    
            setParent(parent);
        }
        
        public CornerHeaderCell getCornerCell(){
            return cornerCell;
        }
        
        public final void setCornerCell(final CornerHeaderCell corner){
            if (cornerCell!=null){
                cornerCell.setParent(null);
                getHtml().remove(cornerCell.getHtml());
            }                        
            cornerCell = corner==null ? new DefaultCornerHeaderCell() : corner;
            getHtml().add(0, cornerCell.getHtml());
            cornerCell.setParent(this);
        }
        
        @Override
        public UIObject findObjectByHtmlId(final String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            return cornerCell.findObjectByHtmlId(id);
        }

        @Override
        public void visit(final Visitor visitor) {            
            cornerCell.visit(visitor);
            super.visit(visitor);
        }
        
        protected final void addHeaderCell(final VerticalHeaderCell cell){
            thead.add(cell.getHtml());
        }
        
        protected final void addHeaderCell(final int index, final VerticalHeaderCell cell){
            thead.add(index, cell.getHtml());
        }
        
        protected final void removeHeaderCell(final VerticalHeaderCell cell){
            thead.remove(cell.getHtml());
        }
        
        protected final void clearHeaderCells(){
            thead.clear();
        }
        
        protected abstract void notifyClick(final VerticalHeaderCell vHeaderCell, final EnumSet<EKeyboardModifier> modifiers);
        
        protected abstract void notifyDoubleClick(final VerticalHeaderCell vHeaderCell, final EnumSet<EKeyboardModifier> modifiers);
    }    
    
    public interface FilterListener{        
        void afterApplyFilter(final String filter);                
    }    
    
    public final static class FilterEditorController{
        
        private final ValueEditor.ValueChangeListener<String> filterChangeListener;
        private final Html gridHtml;
        private List<FilterListener> filterListeners;
        private ValStrEditorController filterEditor;
        
        public FilterEditorController(final Html gridHtml, final ValueEditor.ValueChangeListener<String> filterChangeListener){
            this.filterChangeListener = filterChangeListener;            
            this.gridHtml = gridHtml;
        }
        
        public void setFilterEditor(final ValStrEditorController editor){
            if (filterEditor!=editor){
                if (filterEditor!=null){
                    filterEditor.removeValueChangeListener(filterChangeListener);
                }
                filterEditor = editor;
                final UIObject editorUI = editor==null ? null : (UIObject)editor.getValEditor();
                gridHtml.setAttr("filterEditor", editorUI==null ? null : editorUI.getHtmlId());
                if (filterEditor!=null){
                    filterEditor.addValueChangeListener(filterChangeListener);
                }                    
            }
        }
        
        public ValStrEditorController getFilterEditor(){
            return filterEditor; 
        }
        
        public void addFilterListener(final FilterListener listener){
            if (listener!=null){
                if (filterListeners==null){
                    filterListeners = new LinkedList<>();
                }
                if (!filterListeners.contains(listener)){
                    filterListeners.add(listener);
                }
            }
        }

        public void removeFilterListener(final FilterListener listener){
            if (listener!=null && filterListeners!=null){
                filterListeners.remove(listener);
                if (filterListeners.isEmpty()){
                    filterListeners = null;
                }
            }
        }

        public void notifyFilterListeners(final String filter){
            if (filterListeners!=null){
                final List<FilterListener> copy = new LinkedList<>(filterListeners);
                for (FilterListener listener: copy){
                    listener.afterApplyFilter(filter);
                }
            }
        }        
    }    
    
    public static class SelectableCellRenderer extends UIObject implements CellRenderer{
        
        public interface ISelectionCellClickListener{            
            void onClick(final Object cellId, final boolean isSelected, final EnumSet<EKeyboardModifier> keyboardModifiers);
        }      
        
        private static class RowSelectionCheckBox extends CheckBox{
            
            public RowSelectionCheckBox(){
                super();            
                getContentElement().setCss("display", "none");
                getHtml().setCss("border-collapse","collapse");
                getHtml().setCss("table-layout","fixed");
                getHtml().setCss("border","none");
                clearClickHandlers();
            }
        }                  
        
        private final CheckBox check;
        private final Object cellId;
        private final List<ISelectionCellClickListener> listeners = new LinkedList<>();

        public SelectableCellRenderer(final Object cellId) {
            super(new Div());
            this.cellId = cellId;
            check = new RowSelectionCheckBox(){
                @Override
                protected void processHtmlEvent(final HtmlEvent event) {
                    final ClickHtmlEvent clickEvent = (ClickHtmlEvent)event;      
                    if (clickEvent.getButton()==MouseEvent.BUTTON1){
                        notifySelectionCellClick(check.isSelected(), clickEvent.getKeyboardModifiers());
                    }
                }
            };            
            check.subscribeToEvent(new MouseClickEventFilter(EKeyboardModifier.ANY));
            html.add(check.getHtml());
            html.addClass("renderer");
            html.setCss("vertical-align", "middle");
            html.setCss("padding-left", "4px");
            html.setCss("overflow", "hidden");
            html.setCss("width", "100%");
            html.setCss("height", "100%"); 
            html.setAttr("nolayout", "true");            
        }
        
        @Override
        public void update(final int r, final int c, final Object value) {
            if (value instanceof Boolean){
                check.setSelected((Boolean)value);
            }
        }
        
        public void addListener(final ISelectionCellClickListener listener){
            listeners.add(listener);
        }
        
        public void removeListener(final ISelectionCellClickListener listener){
            listeners.remove(listener);            
        }
        
        private void notifySelectionCellClick(final boolean isSelected, final EnumSet<EKeyboardModifier> keyboardModifiers){
            final List<ISelectionCellClickListener> copy = new LinkedList<>(listeners);
            for (ISelectionCellClickListener listener: copy){
                listener.onClick(cellId, isSelected, keyboardModifiers);
            }
        }
        
        @Override
        public void selectionChanged(final int r, final int c, final Object value, final ICell cell, final boolean isSelected) {
        }

        @Override
        public void rowSelectionChanged(final boolean isRowSelected) {
        }

        @Override
        public UIObject getUI() {
            return this;
        }
        
        @Override
        public UIObject findObjectByHtmlId(final String id) {
            final UIObject result = super.findObjectByHtmlId(id);
            return result==null ? check.findObjectByHtmlId(id) : result;
        }

        @Override
        public void visit(final Visitor visitor) {
            super.visit(visitor);
            check.visit(visitor);
        }
    }
    

    public IColumn addColumn(String title);
    
    public IColumn addColumn(String title, AbstractColumnHeaderCell columnHeaderCell);

    public IColumn addColumn(int index, String title);
    
    public IColumn addColumn(int index, String title, AbstractColumnHeaderCell columnHeaderCell);

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
    
    public void setFilterEditor(ValStrEditorController editor);
    
    public ValStrEditorController getFilterEditor();
    
    public void applyCurrentFilter();

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
    
    public static class EditingOptions implements IEditingOptions {

        private ECellEditingMode mode;
        private final ICell cell;
        private final IColumn column;
        private EditMask mask = null;

        public EditingOptions(final ICell cell) {
            mode = null;
            this.cell = cell;
            this.column = null;
        }

        public EditingOptions(final IColumn col) {
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
        public void setEditMask(final EditMask editMask) {
            if (mask == null && editMask == null) {
                return;
            }
            mask = EditMask.newCopy(editMask);
            if (cell != null) {
                cell.updateRenderer();
            }
            if (column != null) {
                column.updateCellsRenderer();
            }
        }

        @Override
        public void setEditingMode(final ECellEditingMode editMode) {
            if (editMode == null) {
                mode = ECellEditingMode.NULL_VALUE_ACCEPTED;
            }
            if (editMode == mode) {
                return;
            }
            this.mode = editMode;

            if (cell != null) {
                cell.updateRenderer();
            }
            if (column != null) {
                column.updateCellsRenderer();
            }
        }
    }
    
    public static class SetupColumnVisibilityDialog extends Dialog {

        private static class VCheckBox extends CheckBox {

            ColumnDescriptor desc;

            VCheckBox(ColumnDescriptor desc) {
                setText(desc.getTitle());
                this.desc = desc;
            }
        }
        
        private final List<VCheckBox> cbss;        
        private boolean restoreDefaultSettings;

        public SetupColumnVisibilityDialog(final IClientEnvironment env,
                                                            final List<ColumnDescriptor> allColumns,
                                                            final List<ColumnDescriptor> visibleColumns,
                                                            final boolean firstColumnVisible,
                                                            final boolean showRestoreDefaultsButton) {
            super(env, null);
            this.setWindowTitle(getEnvironment().getMessageProvider().translate("Selector", "Columns Visibility"));
            if (allColumns==null || allColumns.isEmpty() || visibleColumns==null){
                cbss = Collections.emptyList();
                return;                
            }
            getHtml().setAttr("dlgId", "visibleColumns");
            setWidth(300);
            setHeight(300);
            final LabeledEditGrid grid = createLabeledEditGrid();
            add(grid);
            cbss = fillColumnDescription(allColumns, visibleColumns, grid, firstColumnVisible);

            grid.setTop(3);
            grid.setLeft(3);
            grid.getAnchors().setRight(new Anchors.Anchor(1, -3));
            grid.getAnchors().setBottom(new Anchors.Anchor(1, -3));
            this.addCloseAction(EDialogButtonType.OK);
            if (showRestoreDefaultsButton){
                final Icon restoreDefaultsIcon = 
                    getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CANCEL);
                final IPushButton pbRestoreDefaults = addCloseAction(EDialogButtonType.RESTORE_DEFAULTS);
                pbRestoreDefaults.setIcon(restoreDefaultsIcon);
                pbRestoreDefaults.setTitle(getEnvironment().getMessageProvider().translate("Selector", "Restore Default Settings"));
                addCloseButtonListener(new Dialog.CloseButtonListener() {
                    @Override
                    public void onClose(final EDialogButtonType button, final DialogResult result) {
                        restoreDefaultSettings = button==EDialogButtonType.RESTORE_DEFAULTS;
                    }
                });
            }
            this.addCloseAction(EDialogButtonType.CANCEL);
            setEscapeAction(EDialogButtonType.CANCEL);
        }

        private static LabeledEditGrid createLabeledEditGrid() {
            return new LabeledEditGrid(new LabeledEditGrid.AbstractEditor2LabelMatcher() {
                @Override
                protected UIObject createLabelComonent(UIObject editorComponent) {
                    return new Label();
                }
            });
        }

        private static List<VCheckBox> fillColumnDescription(final List<ColumnDescriptor> all, 
                                                                                        final List<ColumnDescriptor> visible, 
                                                                                        final LabeledEditGrid grid, 
                                                                                        final boolean firstColumnMustBeVisible) {
            final List<VCheckBox> cbss = new LinkedList<>();
            int firstVisibleColumnIndex = -1;
            for (int count=all.size(),i=0; i<count; i++) {
                final ColumnDescriptor desc = all.get(i);
                if (visible.contains(desc) && firstVisibleColumnIndex<0){
                    firstVisibleColumnIndex = i;
                }                
                VCheckBox cb = new VCheckBox(desc);
                grid.addEditor(cb, 0, -1);
                cb.setSelected(visible.contains(desc));
                cbss.add(cb);
                if (visible.size() == 1) {
                    cb.setEnabled(!visible.contains(desc));
                }
                if (!firstColumnMustBeVisible){
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
            }
            if (firstColumnMustBeVisible){
                for (int i=0; i<=firstVisibleColumnIndex; i++){
                    cbss.get(i).setEnabled(false);
                }                
            }
            return cbss;
        }

        private static int findUncheckedItemIndex(List<VCheckBox> cbss) {
            for (VCheckBox cb : cbss) {
                if (cb.isSelected()) {
                    return cbss.indexOf(cb);
                }
            }
            return cbss.size() - 1;
        }
        
        public List<ColumnDescriptor> getSelectedColumns(){
            final List<ColumnDescriptor> descriptor = new LinkedList<>();
            for (VCheckBox cb : cbss) {
                if (cb.isSelected()) {
                    descriptor.add(cb.desc);
                }
            }
            return descriptor;
        }
        
        public boolean needToRestoreDefaultSettings(){
            return restoreDefaultSettings;
        }
    }
        
    public static interface ColumnDescriptor {
        public String getTitle();
    }
    
}
