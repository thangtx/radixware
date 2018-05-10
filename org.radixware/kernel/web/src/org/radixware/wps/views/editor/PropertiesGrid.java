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

package org.radixware.wps.views.editor;

import java.util.ArrayList;
import java.util.LinkedList;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter.IPresenterItem;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Html.Visitor.VisitResult;
import java.util.List;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter;
import org.radixware.kernel.common.client.widgets.propertiesgrid.PropertiesGridController;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Table;
import org.radixware.kernel.common.html.Table.DataCell;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.views.editor.property.AbstractPropEditor;
import org.radixware.wps.views.editor.property.PropEditor;


import org.radixware.wps.views.editor.property.PropLabel;


public class PropertiesGrid extends UIObject implements IModelWidget {
    
    private final static class PropsTable extends Table{
                
        private Table.Body body;        
        private List<Html> columns = new ArrayList<>();
        
        public PropsTable(){            
        }

        @Override
        protected Body createBody() {
            body = new Table.Body();
            return body;
        }
        
        public void createColumns(final int count){
            Html column;
            for (int i=0; i<count; i++){
                column = new Html("col");
                add(column);
                columns.add(column);
            }  
        }
        
        public void removeColumns(){
            for (int i=columns.size()-1; i>=0; i--){
                remove(columns.get(i));
            }
            columns.clear();
        }
    }

    private final IPropertiesGridPresenter<PropLabel, AbstractPropEditor, PropertiesGroupWidget> presenter = new IPropertiesGridPresenter<PropLabel, AbstractPropEditor, PropertiesGroupWidget>() {
        private int colCount;
        private Alignment headerAlignment;
        
        @Override
        public PropLabel createPropertyLabel(Property property) {
            final PropLabel label = (PropLabel) property.createPropertyLabel();
            label.setParent(PropertiesGrid.this);
            return label;
        }

        @Override
        public AbstractPropEditor createPropertyEditor(Property property) {
            final AbstractPropEditor editor = 
                (AbstractPropEditor) property.getOwner().createPropertyEditor(property.getId());
            editor.setParent(PropertiesGrid.this);
            editor.getHtml().renew();
            return editor;
        }

        @Override
        public PropertiesGroupWidget createPropertiesGroup(PropertiesGroupModelItem propertiesGroup) {
            final PropertiesGroupWidget group = new PropertiesGroupWidget(propertiesGroup, PropertiesGrid.this.controller);
            group.setParent(PropertiesGrid.this);
            return group;
        }

        @Override
        public void destroyWidgets(PropLabel label, AbstractPropEditor editor, PropertiesGroupWidget group) {
            if (editor != null) {
                if (!(editor instanceof PropEditor)) {
                    editor.setVisible(false);
                    editor.close();
                    editor.setParent(null);
                } else {
                    editor.setVisible(false);
                    ((PropEditor) editor).close();
                    editor.setParent(null);
                }
            }
            if (label != null) {
                label.setVisible(false);
                label.close();
                label.setParent(null);
            }
            if (group!=null){
                group.setVisible(false);
                group.close();
                group.setParent(null);
            }
        }

        public void clear() {
            controller.clear();
        }

        @Override
        public int getCellHeight(IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item) {
            //return item.getPropertyEditor().getHeight();
            return 0;//not used for web
        }

        @Override
        public void beforeUpdateCellsPresentation(int columnsCount, int rowsCount) {
            while (rowInfos.size() < rowsCount) {
                rowInfos.add(new RowInfo());
            }
            while (rowInfos.size() > rowsCount) {
                rowInfos.remove(rowInfos.size() - 1);
            }
            for (RowInfo info : rowInfos) {
                info.setColumnsCount(columnsCount);
            }
            colCount = columnsCount;
            final WpsSettings settings = ((WpsEnvironment)getEnvironment()).getConfigStore();
            try {
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.EDITOR_GROUP);
                settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
                Alignment headerAlignment = Alignment.getForValue(settings.readInteger(SettingNames.Editor.Common.TITLES_ALIGNMENT, 0));
                this.headerAlignment = headerAlignment;
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        }

        class RowInfoItem {

            private IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item;

            public RowInfoItem(IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item) {
                this.item = item;
            }
        }

        class RowInfo {

            RowInfoItem[] items;
            boolean rendered = false;

            private void setColumnsCount(int count) {
                if (items == null || items.length != count) {
                    RowInfoItem[] newItems = new RowInfoItem[count];
                    if (items == null) {
                        items = newItems;
                    } else {
                        if (count > items.length) {
                            System.arraycopy(items, 0, newItems, 0, items.length);
                        } else {
                            System.arraycopy(items, 0, newItems, 0, count);
                        }
                        items = newItems;
                    }
                    rendered = false;
                }
            }

            private void setValue(final IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item) {
                int col = item.getColumn();
                int span = item.getColumnSpan();
                RowInfoItem rowInfo = new RowInfoItem(item);
                for (int i = 0; i < span; i++) {
                    if (col + i < items.length) {
                        items[col + i] = rowInfo;
                    } else {
                        break;
                    }
                }
                rendered = false;
            }

            private void clearValue(IPresenterItem item) {
                int col = item.getColumn();
                int span = item.getColumnSpan();
                for (int i = 0; i < span; i++) {
                    if (col + i < items.length) {
                        items[col + i] = null;
                    } else {
                        break;
                    }
                }
                rendered = false;
            }
            
            private int getMaximumInnerRows(){
                int result = 1;
                for (RowInfoItem item: items){
                    if (item!=null && item.item!=null && item.item.getPropertiesGroupWidget()!=null){
                        result = Math.max(result, item.item.getPropertiesGroupWidget().getVisibleRowsCount());
                    }
                }
                return result;
            }
        }
        
        private List<RowInfo> rowInfos = new LinkedList<>();

        @Override
        public void presentCell(IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item, int columnsCount) {
            final int row = item.getRow();            
            for (int i=0; i<item.getRowSpan(); i++){
                rowInfos.get(row+i).setValue(item);
            }
        }
        
        private boolean hasPropertiesGroupCell(){
            for (RowInfo rowInfo: rowInfos){
                if (rowInfo.items!=null){
                    for (RowInfoItem item: rowInfo.items){
                        if (item!=null && item.item!=null && item.item.getPropertiesGroupWidget()!=null){
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void presentSpanColumn(int col) {
        }

        @Override
        public void presentSpanRow(int row) {            
        }                

        @Override
        public void clearCellPresentation(IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item) {
            int row = item.getRow();
            if (row >= 0 && row < rowInfos.size()) {
                rowInfos.get(item.getRow()).clearValue(item);
            }
        }

        @Override
        public void afterUpdateCellsPresentation() {
            while (table.rowCount() < rowInfos.size()) {
                table.addRow();
            }
            while (table.rowCount() > rowInfos.size()) {
                table.getRow(table.rowCount() - 1).remove();
            }
            
            final boolean manualLayout = PropertiesGrid.this.propertiesGroup!=null || hasPropertiesGroupCell();
            
            final RowInfoItem[] prevRow = new RowInfoItem[colCount];
            
            for (int r = 0; r < table.rowCount(); r++) {
                final Table.Row row = table.getRow(r);
                final RowInfo info = rowInfos.get(r);
                final int maxInnerRows = info.getMaximumInnerRows();
                if (!info.rendered) {
                    row.clear();
                    RowInfoItem prevCell = null;
                    for (int c = 0; c < info.items.length; c++) {                        
                        if (info.items[c] == null) {//empty cell                            
                            Table.DataCell labelCell = row.addCell();
                            labelCell.setCss("width", "1px");
                            labelCell.setCss("padding-left", "3px");
                            labelCell.setUserObject("label");
                            Table.DataCell dataCell = row.addCell();                            
                            final Html emptyContent = new Html("input");                            
                            emptyContent.setCss("width", "100%");
                            emptyContent.setCss("visibility", "hidden");
                            dataCell.add(emptyContent);
                            prevCell = null;
                            prevRow[c] = null;
                        } else {
                            if (info.items[c] == prevCell){
                                prevRow[c] = info.items[c];
                                continue;//span column
                            } if (info.items[c] == prevRow[c]){
                                continue;//span row
                            }else {
                                prevCell = info.items[c];
                                prevRow[c] = prevCell;
                                final IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> presenter = prevCell.item;
                                if (presenter.getPropertyEditor()!=null){
                                    //----------- label ----------------
                                    Table.DataCell labelCell = row.addCell();
                                    if (!manualLayout){
                                        labelCell.setCss("width", "1px");
                                    }
                                    labelCell.setCss("padding-left", "3px");
                                    labelCell.setCss("vertical-align", "middle");
                                    labelCell.setCss("text-align", headerAlignment.getCssValue());
                                    labelCell.setAttr("role", "label");
                                    final PropLabel labelComponent = presenter.getPropertyLabel();
                                    labelCell.add(labelComponent.getHtml());
                                    labelCell.setUserObject("label");
                                    if (!children.contains(labelComponent)) {
                                        children.add(labelComponent);
                                    }
                                    //----------- editor ----------------
                                    Table.DataCell dataCell = row.addCell();
                                    dataCell.setCss("vertical-align", "middle");
                                    dataCell.setAttr("role", "editor");
                                    if (presenter.getColumnSpan() > 1) {
                                        dataCell.setAttr("colspan", presenter.getColumnSpan() * 2 - 1);
                                    }
                                    final AbstractPropEditor editorComponent = presenter.getPropertyEditor();
                                    dataCell.add(editorComponent.getHtml());
                                    if (!children.contains(editorComponent)) {
                                        children.add(editorComponent);
                                    }
                                }
                                if (presenter.getPropertiesGroupWidget()!=null){
                                    //----------- group ----------------
                                    Table.DataCell groupCell = row.addCell();
                                    groupCell.setAttr("colspan", presenter.getColumnSpan() * 2 );
                                    if (presenter.getRowSpan()>1){
                                        groupCell.setAttr("rowspan", presenter.getRowSpan() );
                                    }
                                    final PropertiesGroupWidget groupComponent = presenter.getPropertiesGroupWidget();                                    
                                    groupCell.add(groupComponent.getHtml());
                                    if (!children.contains(groupComponent)){
                                        children.add(groupComponent);
                                    }                                    
                                    if (groupComponent.getVisibleRowsCount()<maxInnerRows){                                        
                                        groupComponent.setAdjustMode(GroupBox.AdjustMode.EXPAND_CONTENT_HEIGTH);
                                        //groupComponent.setBorderBoxSizingEnabled(false);
                                    }else{
                                        groupComponent.setAdjustMode(GroupBox.AdjustMode.EXPAND_HEIGHT_BY_CONTENT);
                                        //groupComponent.setBorderBoxSizingEnabled(true);
                                    }
                                    final PropertiesGrid innerGrid = groupComponent.getPropertiesGrid();
                                    groupCell.setAttr("innertable", innerGrid.table.getId());
                                    groupCell.setAttr("role", "group");
                                }
                            }
                        }
                    }
                    info.rendered = true;
                }
            }
            for (int i = 0; i < children.size();) {
                UIObject obj = children.get(i);
                boolean remove = true;
                Html html = obj.getHtml();
                while (html != null) {
                    if (html == table) {
                        remove = false;
                        break;
                    }
                    html = html.getParent();
                }
                if (remove) {
                    children.remove(i);
                } else {
                    i++;
                }
            }
            table.removeColumns();
            if (manualLayout){
                table.setCss("table-layout", "fixed");
                table.createColumns(colCount*2);
            }else{
                table.setCss("table-layout", null);
            }
        }

        @Override
        public void scrollToCell(IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }                

        @Override
        public void updateGeometry() {
        }
    };
    private EditorPageModelItem page;
    private final PropertiesGridController<PropLabel, AbstractPropEditor, PropertiesGroupWidget> controller;    
    private final List<UIObject> children = new LinkedList<>();
    private final PropertiesGroupModelItem propertiesGroup;
    private final PropsTable table;
    
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            applySettings();
        }
    };

    public PropertiesGrid(EditorPageModelItem page) {
        this();
        this.page = page;
    }

    public PropertiesGrid() {
        this(null,null);
    }
    
    PropertiesGrid(final PropertiesGroupModelItem propertiesGroup, final PropertiesGridController<PropLabel, AbstractPropEditor, PropertiesGroupWidget> parentController) {
        super(new Div());
        table = new PropsTable();
        this.html.add(table);
        this.html.setCss("overflow-x", "hidden");
        this.html.setCss("overflow-y", "auto");
        this.table.setCss("width", "100%");
        this.html.setAttr("isAdjustWidth", true);
        this.html.setAttr("isAdjustHeight", true);
        html.layout("$RWT.propertiesGrid.layout");
        html.addClass("rwt-ui-auto-height");
        this.setClientHandler("adjustHeight", "$RWT.componentGrid.adjustHeight");
        this.propertiesGroup = propertiesGroup;
        if (propertiesGroup==null){
            controller = new PropertiesGridController<>(presenter, getEnvironment());
        }else{
            controller = new PropertiesGridController<>(presenter, propertiesGroup.getId(), parentController, getEnvironment());
        }        
    }    

    public PropertiesGrid(List<Property> props) {
        this();
        if (props != null && !props.isEmpty()) {
            for (Property prop : props) {
                controller.addProperty(prop);
            }
        }
    }

    public int computePrefferedHeight() {
        int rowCount = table.rowCount();
        return rowCount * 25;
    }

    @Override
    public boolean setFocus(final Property property) {
        return controller.setFocus(property);
    }

    public final AbstractPropEditor addProperty(Property prop, int col, int row) {
        final IPropertiesGridPresenter.IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item = controller.addProperty(prop, col, row);
        return item.getPropertyEditor();
    }

    public final AbstractPropEditor addProperty(Property prop, int col, int row, int colSpan) {
        final IPropertiesGridPresenter.IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item = controller.addProperty(prop, col, row, colSpan);
        return item.getPropertyEditor();
    }

    public final AbstractPropEditor addProperty(Property prop, int col, int row, int colSpan, boolean stickToLeft, boolean stikToRight) {
        final IPropertiesGridPresenter.IPresenterItem<PropLabel, AbstractPropEditor, PropertiesGroupWidget> item = controller.addProperty(prop, col, row, colSpan, stickToLeft, stikToRight);
        return item.getPropertyEditor();
    }

    @Override
    public UIObject findObjectByHtmlId(final String id) {
        if (this.html.getId().equals(id) || table.getId().equals(id)) {
            return this;
        }

        for (UIObject obj : children) {
            UIObject result = obj.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
        }

        final boolean[] isThis = new boolean[]{false};
        table.visit(new Html.Visitor() {
            @Override
            public VisitResult accept(Html html) {
                if (isThis[0]) {
                    return VisitResult.SKIP_CHILDREN;
                }
                if (html.getId().equals(id)) {
                    isThis[0] = true;
                }
                if (html instanceof Table.DataCell) {
                    return VisitResult.SKIP_CHILDREN;
                } else {
                    return VisitResult.DEFAULT;
                }
            }
        });
        if (isThis[0]) {
            return this;
        } else {
            return null;
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        for (UIObject obj : children) {
            obj.visit(visitor);
        }
    }

    public void finishEdit() {
        controller.finishEdit();
    }

    public void close() {        
        controller.close(page);
        ((WpsEnvironment) this.getEnvironment()).removeSettingsChangeListener(l);
    }

    @Override
    public void bind() {
        if (propertiesGroup!=null){
            controller.bind(propertiesGroup);
        }else{
            controller.bind(page);
        }
        ((WpsEnvironment) this.getEnvironment()).addSettingsChangeListener(l);
    }

    private void applySettings() {
        final WpsSettings settings = ((WpsEnvironment)getEnvironment()).getConfigStore();
        final Alignment headerAlignment;
        try {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EDITOR_GROUP);
            settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
            headerAlignment = Alignment.getForValue(settings.readInteger(SettingNames.Editor.Common.TITLES_ALIGNMENT, 0));
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        for (int r = 0; r < table.rowCount(); r++) {
            final Table.Row row = table.getRow(r);
            for (int c = 0; c < row.cellCount(); c++) {
                DataCell cell = row.getCell(c);
                if ("label".equals(cell.getUserObject())) {
                    cell.setCss("text-align", headerAlignment.getCssValue());
                }
            }    
        }
    }   
    
    @Override
    public void refresh(ModelItem pageItem) {
        controller.refresh(pageItem);
    }
    
    public int getVisibleRowsCount(){
        return controller.getVisibleRowsCount();
    }    

    public void adjustToContent(boolean adjust) {
        if (adjust) {
            this.html.setAttr("adjustHeight", true);
        } else {
            this.html.setAttr("adjustHeight", false);
        }
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/table-layout.js"};
    }        
}
