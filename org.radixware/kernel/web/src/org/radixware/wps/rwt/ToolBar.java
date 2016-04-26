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
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.views.RwtAction;


public class ToolBar extends UIObject implements IToolBar {

    protected static class ButtonPane extends TableLayout {

        private int iconWidth = 16;
        private int iconHeight = 16;
        private Row singleRow;
        private int orientation;
        private final UIObject.UIObjectPropertyListener buttonsListener = new UIObjectPropertyListener() {
            @Override
            public void propertyChange(String name, Object oldValue, Object value) {
                if (UIObject.Properties.VISIBLE.equals(name)) {
                    updateButtons();
                }
            }
        };

        public ButtonPane() {
            //this.row = addRow();
            setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        }

        private void updateButtonSize(ToolButton button) {
            button.setIconHeight(iconHeight);
            button.setIconWidth(iconWidth);
            button.setHeight(iconHeight + 4);
            button.setWidth(iconWidth + 4);
        }

        private Row getSingleRow() {
            if (singleRow == null) {
                singleRow = addRow();
            }
            return singleRow;
        }

        public void add(UIObject child) {
            if (orientation == 0) {
                getSingleRow().addCell().add(child);
            } else {
                addRow().addCell().add(child);
            }
            child.addPropertyListener(buttonsListener);
            if (child instanceof ToolButton) {
                updateButtonSize((ToolButton) child);
            }
            updateButtons();
        }

        public void add(int index, UIObject child) {
            if (orientation == 0) {
                getSingleRow().addCell(index).add(child);
            } else {
                addRow(index).addCell().add(child);
            }
            child.addPropertyListener(buttonsListener);
            if (child instanceof ToolButton) {
                updateButtonSize((ToolButton) child);
            }
            updateButtons();
        }

        public void clear() {

            if (orientation == 0 && singleRow != null) {
                List<Row.Cell> cells = singleRow.getCells();
                for (Row.Cell cell : cells) {
                    for (UIObject obj : cell.getChildren()) {
                        obj.removePropertyListener(buttonsListener);
                    }
                    singleRow.remove(cell);
                }
                removeRow(singleRow);
                singleRow = null;
            } else {
                for (Row row : getRows()) {
                    for (Row.Cell cell : row.getCells()) {
                        for (UIObject obj : cell.getChildren()) {
                            obj.removePropertyListener(buttonsListener);
                        }
                    }
                    removeRow(row);
                }
            }
        }

        private void updateButtonObj(UIObject obj, UIObject[] fl) {
            if (obj.isVisible() && fl[0] == null) {
                fl[0] = obj;
            } else {
                if (orientation == 0) {
                    obj.getHtml().removeClass("ui-corner-left");
                } else {
                    obj.getHtml().removeClass("ui-corner-top");
                }
            }
            if (orientation == 0) {
                obj.getHtml().removeClass("ui-corner-right");
            } else {
                obj.getHtml().removeClass("ui-corner-bottom");
            }
            obj.getHtml().addClass("rwt-gradient-bar");
            if (obj.isVisible()) {
                fl[1] = obj;
            }
        }

        private void updateButtons() {
            UIObject[] fl = new UIObject[2];

            if (orientation == 0 && singleRow != null) {
                for (Row.Cell cell : singleRow.getCells()) {
                    UIObject obj = cell.getChildren().get(0);
                    updateButtonObj(obj, fl);
                }
            } else {
                for (Row row : getRows()) {
                    Row.Cell cell = row.getCells().get(0);
                    UIObject obj = cell.getChildren().get(0);
                    updateButtonObj(obj, fl);
                }
            }

            if (fl[0] != null) {
                if (orientation == 0) {
                    fl[0].getHtml().addClass("ui-corner-left");
                    fl[0].getHtml().removeClass("ui-corner-top");
                } else {
                    fl[0].getHtml().addClass("ui-corner-top");
                    fl[0].getHtml().removeClass("ui-corner-left");
                }
            }
            if (fl[1] != null) {
                if (orientation == 0) {
                    fl[1].getHtml().addClass("ui-corner-right");
                    fl[1].getHtml().removeClass("ui-corner-bottom");
                } else {
                    fl[1].getHtml().addClass("ui-corner-bottom");
                    fl[1].getHtml().removeClass("ui-corner-right");
                }
            }
        }

        protected List<UIObject> getChildren() {
            List<UIObject> objs = new LinkedList<>();
            if (orientation == 0 && singleRow != null) {
                for (Row.Cell cell : singleRow.getCells()) {
                    UIObject obj = cell.getChildren().get(0);
                    objs.add(obj);
                }
            } else {
                for (Row row : getRows()) {
                    Row.Cell cell = row.getCells().get(0);
                    UIObject obj = cell.getChildren().get(0);
                    objs.add(obj);
                }
            }
            return objs;
        }

        private void remove(UIObject obj) {
            if (orientation == 0 && singleRow != null) {
                Row.Cell cellToRemove = null;
                for (Row.Cell cell : singleRow.getCells()) {
                    UIObject item = cell.getChildren().get(0);
                    if (item == obj) {
                        cellToRemove = cell;
                    }
                }
                if (cellToRemove != null) {
                    singleRow.remove(cellToRemove);
                }
            } else {
                for (Row row : getRows()) {
                    Row.Cell cell = row.getCells().get(0);
                    UIObject item = cell.getChildren().get(0);
                    if (item == obj) {
                        removeRow(row);
                        break;
                    }
                }
            }
        }

        public void setOrientation(int orientation) {
            if (orientation != this.orientation) {
                List<UIObject> buttons = getChildren();
                for (UIObject obj : buttons) {
                    remove(obj);
                    obj.getHtml().renew();
                }
                removeRow(singleRow);
                singleRow = null;
                this.orientation = orientation;
                for (UIObject obj : buttons) {
                    add(obj);
                }
            }
        }
    }
    private ButtonPane container;
    private Html centerComponent = null;

    public ToolBar() {
        this(new ButtonPane());
    }

    protected ToolBar(ButtonPane container) {
        super(new Div());
        html.setCss("float", "left");
        html.setCss("display", "block");
        html.setCss("overflow", "hidden");
        html.setCss("padding-top", "3px");
        html.setCss("padding-bottom", "3px");
        html.setCss("padding-left", "3px");
        html.setAttr("role", "toolBar");
        html.layout("$RWT.toolBar.layout");
        html.setAttr("rwt_f_minsize", "$RWT.toolBar.minsize");
        this.container = container;
        container.setParent(ToolBar.this);
        this.html.add(container.getHtml());
        setHeight(container.iconHeight + 10);
        // container.setVCoverage(100);
    }

    @Override
    public void setIconSize(int w, int h) {
        container.iconWidth = w;
        container.iconHeight = h;
        for (UIObject obj : container.getChildren()) {
            if (obj instanceof ToolButton) {
                container.updateButtonSize((ToolButton) obj);
            }
        }
        setHeight(container.iconHeight + 10);
    }

    @Override
    public void setHidden(boolean isHidden) {
        setVisible(!isHidden);
    }

    @Override
    public int getIconHeight() {
        return container.iconHeight;
    }

    public void add(UIObject element) {
        if (element != null) {
            container.add(element);
        }
    }

    public void remove(UIObject element) {
        if (element != null) {
            container.remove(element);
        }
    }

    @Override
    public Action[] getActions() {
        final List<Action> actions = new LinkedList<>();

        for (UIObject obj : container.getChildren()) {
            if (obj instanceof ToolButton) {
                ToolButton b = (ToolButton) obj;
                if (b.getAction() != null) {
                    actions.add(b.getAction());
                }
            }
        }
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public void insertAction(Action before, Action action) {
        int actionIndex = -1;
        int index = 0;
        for (UIObject obj : container.getChildren()) {
            if (obj instanceof ToolButton) {
                ToolButton b = (ToolButton) obj;
                if (b.getAction() == before) {
                    actionIndex = index;
                    break;
                }
            }
            index++;
        }
        addAction(actionIndex, action);
    }

    public void setActions(Action[] actions) {
        for (Action a : actions) {
            if (a != null) {
                addAction(a);
            }
        }
    }

    private ToolButton findAction(Action action) {
        for (UIObject obj : container.getChildren()) {
            if (obj instanceof ToolButton) {
                ToolButton b = (ToolButton) obj;
                if (b.getAction() == action) {
                    return b;

                }
            }
        }
        return null;
    }

    public void setAlignment(Alignment a) {
//        switch (a) {
//            case CENTER:
//                if (centerComponent == null) {
//                    centerComponent = new Html("center");
//                    this.html.remove(container.html);
//                    centerComponent.add(container.html);
//                    this.html.add(centerComponent);
//                }
//                break;
//            default:
//                if (centerComponent != null) {
//                    this.html.remove(centerComponent);
//                    centerComponent = null;
//                    this.html.add(container.html);
//                }
//        }
    }

    @Override
    public IToolButton getWidgetForAction(Action action) {
        return findAction(action);
    }

    @Override
    public void setFloatable(boolean isFloatable) {
    }

    @Override
    public void insertSeparator(Action before) {
    }

    private void addAction(int index, Action action) {
        ToolButton button = new ToolButton(action);
        container.updateButtonSize(button);
        container.add(index, button);
        ((RwtAction) action).addActionPresenter(button);
        button.setIconHeight(container.iconHeight);
        button.setIconWidth(container.iconWidth);
    }

    @Override
    public void addAction(Action action) {
        addAction(-1, action);
    }

    @Override
    public void removeAction(Action action) {
        ToolButton b = findAction(action);
        if (b != null) {
            ((RwtAction) action).removeActionPresenter(b);
            container.remove(b);
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        if (centerComponent != null && centerComponent.getId().equals(id)) {
            return this;
        }
        return container.findObjectByHtmlId(id);
    }

    protected ButtonPane getButtonsPane() {
        return container;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        container.visit(visitor);
    }

    public boolean isVertical() {
        return container.orientation != 0;
    }

    public boolean isHorizontal() {
        return container.orientation == 0;
    }

    public void setVertical() {
        container.setOrientation(1);
    }

    public void setHorizontal() {
        container.setOrientation(0);
    }

    @Override
    public void removeAllActions() {
        container.clear();
    }

    @Override
    public void disconnect(IWidget w) {
    }
}
