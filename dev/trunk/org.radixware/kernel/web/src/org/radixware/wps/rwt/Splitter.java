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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.widgets.ISplitter;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.WpsEnvironment;


public class Splitter extends UIObject implements ISplitter {

    public interface SplitterListener {

        public void partChanged(int part, float currentValue);
    }

    protected static class Cell extends UIObject {

        private UIObject component;
        private Html handle = new Div();
        private Html content = new Div();
        private boolean isCollapsed = false;
        private UIObject tableContainer = null;
        private float part;
        private float savePart = -1;
        private String partSettingsKey;

        public Cell(UIObject component, Orientation orientation, ClientSettings settings) {
            super(new Div());
            handle.addClass("divider");
            content.addClass("content");
            html.add(handle);
            html.add(content);
            html.setAttr("role", "splitterCell");
            this.content.add(component.getHtml());
            this.content.setCss("overflow", "hidden");
            this.content.setCss("position", "relative");
            this.component = component;
            component.setParent(this);
            updateOrientation(orientation);
            component.addPropertyListener(new UIObjectPropertyListener() {
                @Override
                public void propertyChange(String name, Object oldValue, Object value) {
                    if (UIObject.Properties.VISIBLE.equals(name)) {
                        boolean visible = Cell.this.component.isVisible();
                        Cell.this.setVisible(visible);
                        if (tableContainer != null) {
                            tableContainer.setVisible(visible);
                        }
                    }
                }
            });
        }

        boolean isCollapsed() {
            return isCollapsed;
        }

        void collapse(boolean collapse) {
            if (collapse) {
                this.content.setCss("display", "none");
                this.handle.addClass("collapsed");
            } else {
                this.content.setCss("display", null);
                this.handle.removeClass("collapsed");
            }
            isCollapsed = collapse;
            checkInCollapsedState();
        }

        private void checkInCollapsedState() {
            if (tableContainer != null) {
                if (isCollapsed) {
                    if (isLast()) {
                        tableContainer.setVisible(false);
                    } else {
                        if (component != null) {
                            tableContainer.setVisible(this.component.isVisible());
                        } else {
                            tableContainer.setVisible(true);
                        }
                    }
                } else {

                    if (component != null) {
                        tableContainer.setVisible(this.component.isVisible());
                    } else {
                        tableContainer.setVisible(true);
                    }
                }
            }
        }

        private boolean isLast() {
            Splitter s = getSplitter();
            if (s == null) {
                return false;
            } else {
                return s.cells.indexOf(this) == s.cells.size() - 1;
            }
        }

        public void setPartSettingsKey(final String key) {
            partSettingsKey = key;
        }

        private void updateOrientation(Orientation o) {
            switch (o) {
                case HORIZONTAL:
                    content.setCss("float", "left");
                    handle.setCss("float", "left");
                    handle.addClass("vertical");
                    handle.removeClass("horizontal");
                    break;
                case VERTICAL:
                    content.setCss("float", "bottom");
                    handle.setCss("float", "bottom");
                    handle.removeClass("vertical");
                    handle.addClass("horizontal");
                    break;
            }
        }

        private Splitter getSplitter() {
            UIObject obj = getParent();
            while (obj != null) {
                if (obj instanceof Splitter) {
                    return (Splitter) obj;
                }
                obj = obj.getParent();
            }
            return null;
        }

        public void setPart(float part) {
            if (part >= 0 && part <= 1) {
                this.part = part;
                html.setAttr("ratio", part);
                storePositionToConfig();
                Splitter splitter = getSplitter();
                if (splitter != null) {
                    splitter.defaultListener.partChanged(splitter.cells.indexOf(this) - 1, this.part);
                }
            }
        }

        public void savePosition() {
            savePart = part;
        }

        public void restorePosition() {
            collapse(false);
            if (savePart > 0) {
                setPart(savePart);
                savePart = -1;
            } else {
                restorePositionFromConfig();
            }
        }

        private void restorePositionFromConfig() {
            if (partSettingsKey != null) {
                final String partAsStr = ((WpsEnvironment) getEnvironment()).getConfigStore().readString(partSettingsKey, null);
                if (partAsStr != null) {
                    try {
                        final float part = Float.parseFloat(partAsStr);
                        setPart(part);
                    } catch (NumberFormatException exception) {
                        return;
                    }
                }
            }
        }

        private void storePositionToConfig() {
            if (partSettingsKey != null) {
                ((WpsEnvironment) getEnvironment()).getConfigStore().writeString(partSettingsKey, Float.toString(getPart()));
            }
        }

        public float getPart() {
            return part;
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject obj = super.findObjectByHtmlId(id);
            if (obj != null) {
                return obj;
            }
            if (content.getId().equals(id)) {
                return this;
            }
            if (handle.getId().equals(id)) {
                return this;
            }
            return component.findObjectByHtmlId(id);
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            component.visit(visitor);
        }

        @Override
        public void processAction(String actionName, String actionParam) {
            if ("collapsed".equals(actionName)) {
                collapse("true".equals(actionParam));
            } else if ("up-r".equals(actionName)) {
                try {
                    setPart(Float.parseFloat(actionParam));
                } catch (NumberFormatException e) {
                }
            } else {
                super.processEvent(actionName, actionParam);
            }
        }
    }

    public enum Orientation {

        HORIZONTAL,
        VERTICAL
    }
    private Orientation orientation = Orientation.HORIZONTAL;
    private List<Cell> cells = new LinkedList<Cell>();
    private TableLayout table = new TableLayout();
    private TableLayout.Row row_;
    private final ClientSettings settings;
    private String ratioSettingKey;
    private final List<SplitterListener> listeners = new LinkedList<SplitterListener>();
    private final SplitterListener defaultListener = new SplitterListener() {
        @Override
        public void partChanged(int part, float currentValue) {
            final List<SplitterListener> list;
            synchronized (listeners) {
                list = new ArrayList<SplitterListener>(listeners);
            }
            for (SplitterListener l : list) {
                l.partChanged(part, currentValue);
            }
        }
    };

    public Splitter() {
        this(getSettingsStatic(), null);
    }

    private static ClientSettings getSettingsStatic() {
        IClientEnvironment env = getEnvironmentStatic();
        if (env != null) {
            return env.getConfigStore();
        } else {
            return null;
        }
    }

    public Splitter(final ClientSettings settings, final String ratioSettingKey) {
        super(new Div());
        table.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        table.setParent(this);
        this.html.add(table.getHtml());
        html.addClass("rwt-split-panel");
        table.getHtml().layout("$RWT.table_layout.layoutSplitter");
        html.setAttr("orientation", orientation.name().toLowerCase());
        this.settings = settings;
        this.ratioSettingKey = ratioSettingKey;
    }

    public String getRatioSettingKey() {
        return ratioSettingKey;
    }

    public void setRatioSettingKey(final String key) {
        ratioSettingKey = key;
        updateCellSettingsKey();
    }

    private void updateCellSettingsKey() {
        if (ratioSettingKey != null) {
            if (cells.size() > 0) {
                cells.get(0).setPartSettingsKey(null);
            }
            for (int i = 1; i < cells.size(); i++) {
                cells.get(i).setPartSettingsKey(ratioSettingKey + "/part" + String.valueOf(i));
            }
        }
    }

    public void addSplitterListener(SplitterListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeSplitterListener(SplitterListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    private TableLayout.Row getSingleRow() {
        if (row_ == null) {
            row_ = table.addRow();
        }
        return row_;
    }

    public void add(UIObject obj) {
        add(-1, obj);
    }

    public int indexOf(UIObject obj) {
        int index = 0;
        for (Cell cell : cells) {
            if (cell.component == obj) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void add(int position, UIObject obj) {

        final Cell cell = new Cell(obj, orientation, settings);
        if (position < 0 || position >= cells.size()) {
            if (cells.isEmpty()) {
                cell.setPart(0);
            } else {
                Cell last = cells.get(cells.size() - 1);
                float lastPart = last.getPart();
                cell.setPart((1 + lastPart) / 2);
            }
            cells.add(cell);
        } else {

            Cell instead = cells.get(position);
            Cell next = position + 1 < cells.size() ? cells.get(position + 1) : null;

            if (position == 0) {
                cell.setPart(0);
                if (next != null) {
                    instead.setPart(next.getPart() / 2);
                } else {
                    instead.setPart(0.5f);
                }
            } else {
                cell.setPart(instead.getPart());
                if (next != null) {
                    instead.setPart(next.getPart() / 2);
                } else {
                    instead.setPart(instead.getPart() + (1 - instead.getPart()) / 2);
                }
            }

            cells.add(position, cell);
            for (Cell c : cells) {
                c.checkInCollapsedState();
            }
        }
        updateCellSettingsKey();

        position = cells.indexOf(cell);
        obj.setParent(cell);
        if (orientation == Orientation.HORIZONTAL) {
            TableLayout.Row row = getSingleRow();
            TableLayout.Row.Cell tableCell = addCell(row);
            tableCell.add(position, cell);
            cell.tableContainer = tableCell;
        } else {
            TableLayout.Row row = table.addRow(position);
            TableLayout.Row.Cell tableCell = addCell(row);
            tableCell.add(cell);
            cell.tableContainer = row;
        }
    }

    private TableLayout.Row.Cell addCell(TableLayout.Row row) {
        TableLayout.Row.Cell cell = row.addCell();
        return cell;
    }

    public void remove(UIObject obj) {
        Cell toRemove = findCell(obj);
        if (toRemove != null) {
            toRemove.component.setParent(null);
            toRemove.setParent(null);
            toRemove.tableContainer = null;
            cells.remove(toRemove);
            updateCellSettingsKey();
        }
        switch (orientation) {
            case HORIZONTAL:
                for (TableLayout.Row.Cell cell : getSingleRow().getCells()) {
                    if (cell.getChildren().get(0) == toRemove) {
                        getSingleRow().remove(cell);
                    }
                }
                break;
            case VERTICAL:
                for (TableLayout.Row row : table.getRows()) {
                    if (row.getCells().get(0).getChildren().get(0) == toRemove) {
                        table.removeRow(row);
                    }
                }
                break;
        }
        for (Cell c : cells) {
            c.checkInCollapsedState();
        }
    }

    protected Cell findCell(UIObject c) {
        for (Cell cell : cells) {
            if (cell.component == c) {
                return cell;
            }
        }
        return null;
    }

    public void clear() {
        for (Cell cell : cells) {
            cell.component.setParent(null);
            cell.setParent(null);
            cell.tableContainer = null;
            this.html.remove(cell.getHtml());
        }
        cells.clear();
        table.clearRows();
        this.row_ = null;
    }

    public List<UIObject> getChildren() {
        List<UIObject> children = new ArrayList<UIObject>(cells.size());
        for (Cell cell : cells) {
            children.add(cell.component);
        }
        return children;
    }

    public void collapse(UIObject component) {
        Cell cell = findCell(component);
        if (cell != null) {
            cell.collapse(true);
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        switch (orientation) {
            case HORIZONTAL:
                this.row_ = null;
                table.clearRows();
                for (int i = 0; i < cells.size(); i++) {
                    Cell cell = cells.get(i);
                    cell.setParent(null);
                    TableLayout.Row row = getSingleRow();

                    TableLayout.Row.Cell tableCell = addCell(row);
                    tableCell.add(cell);
                    cell.updateOrientation(orientation);
                    cell.tableContainer = tableCell;
                }
                break;
            case VERTICAL:
                this.row_ = null;
                table.clearRows();
                for (Cell cell : cells) {
                    cell.setParent(null);
                    TableLayout.Row row = table.addRow();
                    addCell(row).add(cell);
                    cell.updateOrientation(orientation);
                    cell.tableContainer = row;
                }
                break;
        }
        this.orientation = orientation;
        html.setAttr("orientation", orientation.name().toLowerCase());

    }
    private Alignment splitterAlignment;

    public Alignment getSplitterAlignemnt() {
        return splitterAlignment;
    }

    public void setSplitterAlignment(Alignment align) {
        if (cells != null && !cells.isEmpty()) {
            splitterAlignment = align;
            switch (align) {
                case TOP:
                    setOrientation(Orientation.VERTICAL);
                    break;
                case RIGHT:
                    this.row_ = null;
                    table.clearRows();
                    orientation = Orientation.HORIZONTAL;
                    for (int i = cells.size() - 1; i >= 0; i--) {
                        Cell cell = cells.get(i);
                        cell.setParent(null);
                        TableLayout.Row row = getSingleRow();

                        TableLayout.Row.Cell tableCell = addCell(row);
                        tableCell.add(cell);
                        cell.updateOrientation(orientation);
                        cell.tableContainer = tableCell;
                    }
                    break;
                case BOTTOM:
                    this.row_ = null;
                    table.clearRows();
                    orientation = Orientation.VERTICAL;
                    for (int i = cells.size() - 1; i >= 0; i--) {
                        Cell cell = cells.get(i);
                        cell.setParent(null);
                        TableLayout.Row row = table.addRow();
                        addCell(row).add(cell);
                        cell.updateOrientation(orientation);
                        cell.tableContainer = row;
                    }
                    break;
                case LEFT:
                    setOrientation(Orientation.HORIZONTAL);
                    break;
                default:
                    setOrientation(Orientation.HORIZONTAL);
                    break;
            }
            if (orientation != null) {
                html.setAttr("orientation", orientation.name().toLowerCase());
            }
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        table.visit(visitor);
    }

    @Override
    protected String[] clientCssRequired() {
        return new String[]{"org/radixware/wps/rwt/split-panel.css"};
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        return table.findObjectByHtmlId(id);
    }

    @Override
    public boolean isCollapsed(int index) {
        if (index >= 0 && index < cells.size()) {
            return cells.get(0).isCollapsed();
        } else {
            return false;
        }
    }
    //private float savePart = -1;

    @Override
    public void saveCurrentPosition() {
        for (Cell c : cells) {
            c.savePosition();
        }
    }

    @Override
    public void collapse(int index) {
        if (index >= 0 && index < cells.size()) {
            cells.get(index).collapse(true);
        }
    }

    public void expand(int index) {
        if (index >= 0 && index < cells.size()) {
            cells.get(index).collapse(false);
        }
    }

    @Override
    public void restorePosition() {
        for (Cell c : cells) {
            c.restorePosition();
        }
        float prevPart = 0;
        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);
            float nextPart = 1;
            if (i + 1 < cells.size()) {
                Cell next = cells.get(i + 1);
                nextPart = next.part;
            }
            if (c.part < prevPart || c.part > nextPart) {
                c.setPart((nextPart + prevPart) / 2);
            }
            prevPart = c.getPart();
        }
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    public void setPart(int index, float part) {
        if (index + 1 >= 0 && index + 1 < cells.size()) {
            Cell cell = cells.get(index + 1);
            cell.setPart(part);
        }
    }

    public int getCellsCount() {
        return cells.size();
    }

    protected List<Cell> getSplitterCells() {
        return cells;
    }

    public float getPart(int index) {
        if (index + 1 >= 0 && index + 1 < cells.size()) {
            if (splitterAlignment == Alignment.BOTTOM || splitterAlignment == Alignment.RIGHT) {
                return cells.get(index).getPart();
            }
            return cells.get(index + 1).getPart();
        }
        throw new IndexOutOfBoundsException();
    }
}
