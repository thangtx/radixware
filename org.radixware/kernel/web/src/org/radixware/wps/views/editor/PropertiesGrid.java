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

import java.util.LinkedList;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter.IPresenterItem;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Html.Visitor.VisitResult;
import java.util.List;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter;
import org.radixware.kernel.common.client.widgets.propertiesgrid.PropertiesGridController;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Table;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editor.property.AbstractPropEditor;
import org.radixware.wps.views.editor.property.PropEditor;


import org.radixware.wps.views.editor.property.PropLabel;


public class PropertiesGrid extends UIObject implements IModelWidget {

    private EditorPageModelItem page;
    private final PropertiesGridController<PropLabel, AbstractPropEditor> controller;
    private final IPropertiesGridPresenter<PropLabel, AbstractPropEditor> presenter = new IPropertiesGridPresenter<PropLabel, AbstractPropEditor>() {
        private int colCount;

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
        public void destroyWidgets(PropLabel label, AbstractPropEditor editor) {
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
        }

        public void clear() {
            controller.clear();
        }

        @Override
        public int getCellHeight(IPresenterItem<PropLabel, AbstractPropEditor> item) {
            return item.getPropertyEditor().getHeight();
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
        }

        class RowInfoItem {

            private IPresenterItem<PropLabel, AbstractPropEditor> item;

            public RowInfoItem(IPresenterItem<PropLabel, AbstractPropEditor> item) {
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

            private void setValue(final IPresenterItem<PropLabel, AbstractPropEditor> item) {
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
        }

        class CellInfo {

            IPresenterItem<PropLabel, AbstractPropEditor> item;
            boolean isLabelCell;
            int startColumn = -1;
            int endColumn = -1;
            int spanValue = 1;

            public CellInfo(IPresenterItem<PropLabel, AbstractPropEditor> item, boolean isLabelCell) {
                this.item = item;
                this.isLabelCell = isLabelCell;
            }
        }
        private List<RowInfo> rowInfos = new LinkedList<>();

        @Override
        public void presentCell(IPresenterItem<PropLabel, AbstractPropEditor> item, int columnsCount) {
            rowInfos.get(item.getRow()).setValue(item);
        }

        @Override
        public void presentSpanColumn(int col) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clearCellPresentation(IPresenterItem<PropLabel, AbstractPropEditor> item) {
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

            final float perColumnScaleFactor = 100f / colCount;

            for (int i = 0; i < table.rowCount(); i++) {
                final Table.Row row = table.getRow(i);
                final RowInfo info = rowInfos.get(i);
                if (!info.rendered) {
                    row.clear();
                    RowInfoItem prev = null;
                    for (int c = 0; c < info.items.length; c++) {

                        if (info.items[c] == null) {
                            Table.DataCell labelCell = row.addCell();
                            labelCell.setCss("width", "1px");
                            labelCell.setCss("padding-left", "3px");

                            Table.DataCell dataCell = row.addCell();
                            dataCell.setCss("width", String.valueOf(perColumnScaleFactor) + "%");
                            prev = null;
                        } else {
                            if (info.items[c] == prev) {
                                continue;
                            } else {
                                prev = info.items[c];
                                //----------- label ----------------
                                Table.DataCell labelCell = row.addCell();
                                labelCell.setCss("width", "1px");
                                labelCell.setCss("padding-left", "3px");
                                IPresenterItem<PropLabel, AbstractPropEditor> presenter = prev.item;
                                PropLabel labelComponent = presenter.getPropertyLabel();
                                labelCell.add(labelComponent.getHtml());
                                if (!children.contains(labelComponent)) {
                                    children.add(labelComponent);
                                }
                                //----------- editor ----------------
                                Table.DataCell dataCell = row.addCell();
                                dataCell.setCss("width", String.valueOf(perColumnScaleFactor) + "%");
                                if (presenter.getColumnSpan() > 1) {
                                    dataCell.setAttr("colspan", presenter.getColumnSpan() * 2 - 1);
                                }
                                AbstractPropEditor editorComponent = presenter.getPropertyEditor();
                                dataCell.add(editorComponent.getHtml());
                                if (!children.contains(editorComponent)) {
                                    children.add(editorComponent);
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
        }

        @Override
        public void scrollToCell(IPresenterItem<PropLabel, AbstractPropEditor> item) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateGeometry() {
        }
    };
    private List<UIObject> children = new LinkedList<>();

    public PropertiesGrid(EditorPageModelItem page) {
        this();
        this.page = page;
    }

    public PropertiesGrid() {
        super(new Div());
        table = new Table();
        this.html.add(table);
        this.html.setCss("overflow-x", "hidden");
        this.html.setCss("overflow-y", "auto");
        this.table.setCss("width", "100%");
        html.layout("$RWT.componentGrid.layout");
        html.addClass("rwt-ui-auto-height");
        this.setClientHandler("adjustHeight", "$RWT.componentGrid.adjustHeight");

        controller = new PropertiesGridController<>(presenter, getEnvironment());

    }
    private final Table table;

    public PropertiesGrid(List<Property> props) {
        this();
        if (props != null && !props.isEmpty()) {
            for (Property prop : props) {
                if (prop.isVisible()) {
                    controller.addProperty(prop);
                }
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
        final IPropertiesGridPresenter.IPresenterItem<PropLabel, AbstractPropEditor> item = controller.addProperty(prop, col, row);
        return item.getPropertyEditor();
    }

    public final AbstractPropEditor addProperty(Property prop, int col, int row, int colSpan) {
        final IPropertiesGridPresenter.IPresenterItem<PropLabel, AbstractPropEditor> item = controller.addProperty(prop, col, row, colSpan);
        return item.getPropertyEditor();
    }

    public final AbstractPropEditor addProperty(Property prop, int col, int row, int colSpan, boolean stickToLeft, boolean stikToRight) {
        final IPropertiesGridPresenter.IPresenterItem<PropLabel, AbstractPropEditor> item = controller.addProperty(prop, col, row, colSpan, stickToLeft, stikToRight);
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
    }

    @Override
    public void bind() {
        controller.bind(page);
    }

    @Override
    public void refresh(ModelItem pageItem) {
        controller.refresh(pageItem);
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
