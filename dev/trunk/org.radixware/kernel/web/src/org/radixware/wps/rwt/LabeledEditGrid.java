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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import org.radixware.kernel.common.html.Html.Visitor.VisitResult;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Table;


public class LabeledEditGrid extends UIObject {

    public static abstract class Editor2LabelMatcher {

        protected abstract UIObject createLabelComonent(UIObject editorComponent);

        public int getColumnSpan(UIObject editorComponent) {
            return 1;
        }

        protected abstract void acceptEditor2Label(UIObject editorComponent, UIObject labelComponent);

        protected abstract void closeEditor2Label(UIObject editorComponent, UIObject labelComponent);

        protected abstract boolean isVisible(UIObject editorComponent);
    }

    private static class Editor2Label {

        private UIObject editor;
        private UIObject label;

        public Editor2Label(LabeledEditGrid container, Editor2LabelMatcher matcher, UIObject editorObject) {
            this.editor = editorObject;
            this.label = matcher.createLabelComonent(editorObject);
            this.label.setParent(container);
            this.editor.setParent(container);
            this.editor.setHeight(this.editor.getPreferredHeight());
            matcher.acceptEditor2Label(editorObject, label);
        }

        public void udpate(LabeledEditGrid container) {
            this.label.setParent(container);
            this.editor.setParent(container);
        }
    }
    private List<Editor2Label> propEditors = new LinkedList<Editor2Label>();
    protected Editor2LabelMatcher matcher;
    private final Table table = new Table();

    public abstract static class AbstractEditor2LabelMatcher extends Editor2LabelMatcher {

        @Override
        protected void acceptEditor2Label(UIObject editorComponent, UIObject labelComponent) {
        }

        @Override
        protected void closeEditor2Label(UIObject editorComponent, UIObject labelComponent) {
        }

        @Override
        protected boolean isVisible(UIObject editorComponent) {
            return editorComponent.isVisible();
        }
    }

    public Editor2LabelMatcher getEditor2LabelMatcher() {
        return matcher;
    }

    public void setEditor2LabelMatcher(Editor2LabelMatcher matcher) {
        this.matcher = matcher;
    }

    public static class DefaultEditor2LabelMatcher extends Editor2LabelMatcher {

        private Map<UIObject, String> obj2Label;

        public static DefaultEditor2LabelMatcher newInstance(UIObject[] editors, String[] labels) {
            Map<UIObject, String> obj2Label = new HashMap<UIObject, String>();
            for (int i = 0; i < Math.max(editors.length, labels.length); i++) {
                obj2Label.put(editors[i], labels[i]);
            }
            return new DefaultEditor2LabelMatcher(obj2Label);
        }

        public DefaultEditor2LabelMatcher(Map<UIObject, String> obj2Label) {
            this.obj2Label = obj2Label;
        }

        @Override
        protected UIObject createLabelComonent(UIObject editorComponent) {
            Label label = new Label(obj2Label.get(editorComponent));
            label.setTextWrapDisabled(true);
            return label;
        }

        @Override
        protected void acceptEditor2Label(UIObject editorComponent, UIObject labelComponent) {
        }

        @Override
        protected void closeEditor2Label(UIObject editorComponent, UIObject labelComponent) {
        }

        @Override
        protected boolean isVisible(UIObject editorComponent) {
            return editorComponent.isVisible();
        }
    }

    public LabeledEditGrid(Editor2LabelMatcher matcher) {
        super(new Div());
        this.matcher = matcher;
        html.layout("$RWT.componentGrid.layout");
        this.html.add(table);
        this.html.setCss("overflow-x", "hidden");
        this.html.setCss("overflow-y", "auto");
        this.table.setCss("width", "100%");
        this.table.setCss("border-collapse", "collapse");//to make size of <tbody> the same as size of <table>
    }

    public int computePrefferedHeight() {
        int rowCount = table.rowCount();
        return rowCount * 25;
    }

    private static class SimpleGridMapper {

        private static class Column {

            private List<Editor2Label> items;

            public Column() {
                this(null);
            }

            public Column(List<Editor2Label> items) {
                this.items = items;
            }

            Editor2Label getCell(int index) {
                if (items == null) {
                    items = new LinkedList<Editor2Label>();
                }
                while (index >= items.size()) {
                    items.add(null);
                }
                return items.get(index);
            }

            void setCell(int index, Editor2Label data) {
                if (items == null) {
                    items = new LinkedList<Editor2Label>();
                }
                if (index < 0) {
                    items.add(data);
                } else {
                    while (index >= items.size()) {
                        items.add(null);
                    }
                    if (items.get(index) == null) {
                        items.set(index, data);
                    } else {
                        items.add(index, data);
                    }
                }
            }

            public List<Editor2Label> getVisibleItems(Editor2LabelMatcher matcher) {
                if (items == null) {
                    return null;
                } else {
                    List<Editor2Label> result = null;
                    for (Editor2Label p : items) {
                        if (p != null && matcher.isVisible(p.editor)) {
                            if (result == null) {
                                result = new LinkedList<Editor2Label>();
                            }
                            result.add(p);
                        }
                    }
                    return result;
                }
            }
        }
        private List<Column> columns = new LinkedList<SimpleGridMapper.Column>();

        public Column getColumn(int index) {
            if (index < 0) {
                Column c = new Column();
                columns.add(c);
                return c;
            }
            while (index >= columns.size()) {
                columns.add(new Column());
            }
            return columns.get(index);
        }

        public final void unset(Editor2Label registry) {
            for (Column c : columns) {
                if (c.items != null) {
                    for (int i = 0; i < c.items.size();) {
                        Editor2Label el = c.items.get(i);
                        if (el == registry) {
                            c.items.remove(i);
                        } else {
                            i++;
                        }
                    }
                }
            }
        }

        public void close() {
            columns.clear();
        }

        public List<Column> getEffectiveColumnList(Editor2LabelMatcher matcher, int[] maxRows) {
            List<Column> result = new LinkedList<Column>();
            for (Column c : columns) {
                List<Editor2Label> list = c.getVisibleItems(matcher);

                if (list != null) {
                    if (maxRows[0] < list.size()) {
                        maxRows[0] = list.size();
                    }
                    result.add(new Column(list));
                }
            }
            return result;
        }
    }
    private final SimpleGridMapper mapper = new SimpleGridMapper();

    public final void addEditor(UIObject editor, int col, int row) {
        if (editor == null) {
            return;
        }
        if (findRegistry(editor) != null) {
            removeEditor(editor);
        }
        Editor2Label registry = new Editor2Label(this, matcher, editor);
        editor.unsetLocation();
        editor.unsetWidth();
        int h = editor.getHeight();
        if (h == 0) {
            editor.setHeight(20);
        }
        mapper.getColumn(col).setCell(row, registry);
        propEditors.add(registry);
        editor.getHtml().setCss("position", "relative");
        updateHtml();
        editor.addPropertyListener(editorListener);
    }
    private final UIObjectPropertyListener editorListener = new UIObjectPropertyListener() {
        @Override
        public void propertyChange(String name, Object oldValue, Object value) {
            if (Properties.VISIBLE.equals(name)) {
                updateHtml();
            }
        }
    };

    private Editor2Label findRegistry(UIObject editor) {
        for (Editor2Label r : propEditors) {
            if (r.editor == editor) {
                return r;
            }
        }
        return null;
    }

    public final void removeEditor(UIObject editor) {
        Editor2Label registry = findRegistry(editor);
        if (registry != null) {
            editor.removePropertyListener(editorListener);
            mapper.unset(registry);


            for (Editor2Label el : propEditors) {
                el.editor.html.remove();
                el.label.html.remove();
                el.editor.setParent(null);
                el.label.setParent(null);
                if (el != registry) {
                    el.udpate(this);
                }
            }
            propEditors.remove(registry);
            while (table.rowCount() > 0) {
                table.getRow(0).remove();
            }

            updateHtml();
        }
    }

    private class WidthInfo {

        float width;
        int index;

        public WidthInfo(float width, int index) {
            this.width = width;
            this.index = index;
        }
    }
    private List<WidthInfo> widths = null;
    private List<WidthInfo> covers = null;

    public void setColumnWidth(int column, int width) {
        unsetColumnCoverage(column);
        WidthInfo info = findColumnWidth(column);
        if (info == null) {
            info = new WidthInfo(width, column);
            if (widths == null) {
                widths = new LinkedList<WidthInfo>();
            }
            widths.add(info);
        } else {
            info.width = width;
        }
    }

    public void unsetColumnWidth(int column) {
        WidthInfo info = findColumnWidth(column);
        if (info != null) {
            if (widths != null) {
                widths.remove(info);
                if (widths.isEmpty()) {
                    widths = null;
                }
            }
        }
    }

    private WidthInfo findColumnWidth(int column) {
        if (widths == null) {
            return null;
        } else {
            for (WidthInfo info : widths) {
                if (info.index == column) {
                    return info;
                }
            }
            return null;
        }
    }

    private WidthInfo findColumnCoverage(int column) {
        if (covers == null) {
            return null;
        } else {
            for (WidthInfo info : covers) {
                if (info.index == column) {
                    return info;
                }
            }
            return null;
        }
    }

    public void setColumnCoverage(int column, float coverage) {
        unsetColumnWidth(column);
        WidthInfo info = findColumnWidth(column);
        if (info == null) {
            info = new WidthInfo(coverage, column);
            if (widths == null) {
                widths = new LinkedList<WidthInfo>();
            }
            widths.add(info);
        } else {
            info.width = coverage;
        }
    }

    public void unsetColumnCoverage(int column) {
        WidthInfo info = findColumnCoverage(column);
        if (info != null) {
            if (covers != null) {
                covers.remove(info);
                if (covers.isEmpty()) {
                    covers = null;
                }
            }
        }
    }

    private void updateHtml() {
        int[] maxRows = new int[]{0};
        List<SimpleGridMapper.Column> columns = mapper.getEffectiveColumnList(matcher, maxRows);

        List<Table.Row> rows = new LinkedList<Table.Row>();

        for (int i = 0; i < table.rowCount(); i++) {
            rows.add(table.getRow(i));
        }

        if (rows.size() > maxRows[0]) {
            while (rows.size() > maxRows[0]) {
                Table.Row row = rows.get(rows.size() - 1);
                row.remove();
                rows.remove(row);
            }
        } else {
            while (table.rowCount() < maxRows[0]) {
                rows.add(table.addRow());
            }
        }
        int rowIdx = 0;
        int cc = columns.size();

        for (Table.Row row : rows) {
            while (row.cellCount() > cc * 2) {
                row.getCell(row.cellCount() - 1).remove();
            }
            int ci = 1;
            int eci = 0;
            for (SimpleGridMapper.Column c : columns) {
                if (c.items.size() <= rowIdx) {
                    continue;
                }
                Table.DataCell labelCell;
                Table.DataCell editorCell;
                if (ci >= row.cellCount()) {
                    labelCell = row.addCell();
                    labelCell.setCss("width", "1px");
                    editorCell = row.addCell();
//                    editorCell.setAttr("vfit", "true");
//                    editorCell.layout("$RWT.grid_layout.cell.layout");
                } else {
                    labelCell = row.getCell(ci - 1);
                    editorCell = row.getCell(ci);
                }
                ci += 2;
                if (!labelCell.children().isEmpty()) {
                    if (!labelCell.children().contains(c.items.get(rowIdx).label.getHtml())) {
                        for (Html ch : labelCell.children()) {
                            ch.remove();
                        }
                        for (Html ch : editorCell.children()) {
                            ch.remove();
                        }

                        Html labelHtml = c.items.get(rowIdx).label.getHtml();
                        labelHtml.remove();
                        labelCell.add(labelHtml);

                        Html editorHtml = c.items.get(rowIdx).editor.getHtml();
                        editorHtml.remove();
                        editorCell.add(editorHtml);
                    }
                } else {
                    Html labelHtml = c.items.get(rowIdx).label.getHtml();
                    labelHtml.remove();
                    labelCell.add(labelHtml);

                    Html editorHtml = c.items.get(rowIdx).editor.getHtml();
                    editorHtml.remove();
                    editorCell.add(editorHtml);
                }
                WidthInfo info = findColumnWidth(eci);
                if (info != null) {
                    editorCell.setCss("width", String.valueOf(Math.round(info.width)) + "px");
                } else {
                    info = findColumnCoverage(eci);
                    if (info != null) {
                        editorCell.setCss("width", String.valueOf(info.width) + "%");
                    } else {
                        editorCell.setCss("width", null);
                    }
                };
                eci++;
            }
            rowIdx++;
        }
    }

    @Override
    public UIObject findObjectByHtmlId(final String id) {
        if (this.html.getId().equals(id) || table.getId().equals(id)) {
            return this;
        }
        for (Editor2Label r : propEditors) {
            UIObject result = r.editor.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            result = r.label.findObjectByHtmlId(id);
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
        for (Editor2Label r : propEditors) {
            r.editor.visit(visitor);
            r.label.visit(visitor);
        }
    }

    public void close() {
        final Collection<Editor2Label> propertyEditors = propEditors;
        for (Editor2Label propEditor2PropLabel : propertyEditors) {
            matcher.closeEditor2Label(propEditor2PropLabel.editor, propEditor2PropLabel.label);
        }
        mapper.close();
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
