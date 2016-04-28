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
import org.radixware.kernel.common.html.Html;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.html.Div;


public class ListBox extends UIObject {

    public interface CurrentItemListener {

        public void currentItemChanged(ListBoxItem currentItem);
    }

    public interface DoubleClickListener {

        public void itemDoubleClick(ListBoxItem item);
    }

    public static class ListBoxItem extends ButtonBase {

        private Object userData;

        public ListBoxItem() {
            super(new Div());
            html.setCss("padding-left", "5px");
            html.setCss("padding-right", "5px");
            html.setCss("padding-top", "3px");
            html.setCss("padding-bottom", "3px");
            html.setCss("display", "list-item");
            //html.setCss("background-color", "transparent");
            setIconWidth(16);
            setIconHeight(16);
            setTabIndex(1);
            html.markAsChoosable();
            html.setAttr("ondblclick", "$RWT.listBox.item.dblclick");
            html.setAttr("onkeydown", "$RWT.listBox.item.keyDown");
        }

        private ListBoxItem(final ListBoxItem source) {
            this();
            userData = source.userData;
            setText(source.getText());
            setToolTip(source.getToolTip());
            setIcon(source.getIcon());
            setIconSize(source.getIconWidth(), source.getIconHeight());
        }

       
        public ListBoxItem copy() {
            return new ListBoxItem(this);
        }

        private void markCurrent() {
            html.addClass("rwt-ui-selected-item");
        }

        private void unmarkCurrent() {
            html.removeClass("rwt-ui-selected-item");
        }

        public Object getUserData() {
            return userData;
        }

        public void setUserData(Object userData) {
            this.userData = userData;
        }

        private ListBox findParentLisbox() {
            UIObject obj = getParent();
            while (obj != null) {
                if (obj instanceof ListBox) {
                    return (ListBox) obj;
                }
                obj = obj.getParent();
            }
            return null;
        }

        @Override
        public void processAction(String actionName, String actionParam) {
            if (Events.EVENT_NAME_ONDBLCLICK.equals(actionName) && isEnabled()) {
                ListBox box = findParentLisbox();
                if (box != null) {
                    box.itemDoubleClick(this);
                }
                return;
            }
            super.processAction(actionName, actionParam);
        }

        @Override
        public void setEnabled(boolean isEnabled) {
            if (isEnabled) {
                html.markAsChoosable();
            } else {
                html.removeChoosableMarker();
            }
            super.setEnabled(isEnabled);
        }
    }

    private class ItemHolder extends UIObject {

        private ListBoxItem item;

        public ItemHolder(ListBoxItem item) {
            super(new Html("li"));

            this.html.add(item.getHtml());
            item.setParent(this);
            setParent(ListBox.this.container);
            this.item = item;
        }

        public void changeItem(final ListBoxItem newItem) {
            html.add(newItem.getHtml());
            newItem.setParent(this);
            item = newItem;
        }

        public ListBox getListBox() {
            return ListBox.this;
        }

        public void dispose() {
            if (item != null) {
                item.removeClickHandler(itemClickHandler);
            }
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject obj = super.findObjectByHtmlId(id);
            if (obj != null) {
                return obj;
            }
            if (item != null) {
                return item.findObjectByHtmlId(id);
            } else {
                return null;
            }
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            if (item != null) {
                item.visit(visitor);
            }
        }
    }
    private AbstractContainer container = new AbstractContainer(new Html("ul"));

    {
        container.html.removeClass("rwt-ui-background");
    }
    private IButton.ClickHandler itemClickHandler = new IButton.ClickHandler() {
        @Override
        public void onClick(IButton source) {

            if (source instanceof ListBoxItem) {
                ListBoxItem item = (ListBoxItem) source;
                UIObject parent = item.getParent();

                if (parent instanceof ItemHolder && ((ItemHolder) parent).getListBox() == ListBox.this) {
                    setCurrent((ListBoxItem) source);
                }

            }
        }
    };

    private static class DefaultCurrentItemListener implements CurrentItemListener {

        private final List<CurrentItemListener> listeners = new LinkedList<CurrentItemListener>();

        @Override
        public void currentItemChanged(ListBoxItem currentItem) {
            synchronized (listeners) {
                for (CurrentItemListener l : listeners) {
                    l.currentItemChanged(currentItem);
                }
            }
        }

        void addCurrentItemListener(CurrentItemListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        void removeCurrentItemListener(CurrentItemListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }
    }

    private static class DefaultDoubleClickListener implements DoubleClickListener {

        private final List<DoubleClickListener> listeners = new LinkedList<DoubleClickListener>();
        private final ListBox listBox;

        private DefaultDoubleClickListener(ListBox listBox) {
            this.listBox = listBox;
        }

        @Override
        public void itemDoubleClick(ListBoxItem item) {
            synchronized (listeners) {
                for (DoubleClickListener l : listeners) {
                    l.itemDoubleClick(item);
                }
            }
        }

        public void addDoubleClickListener(DoubleClickListener l) {
            synchronized (listeners) {

                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeDoubleClickListener(DoubleClickListener l) {
            synchronized (listeners) {

                listeners.remove(l);
            }
        }
    }
    private final DefaultCurrentItemListener defaultCurrentItemListener = new DefaultCurrentItemListener();
    private final DefaultDoubleClickListener defaultDoubleClickListener = new DefaultDoubleClickListener(this);
    private ListBoxItem currentItem = null;

    public ListBox() {
        super(new Div());
        this.html.add(container.getHtml());
        container.setParent(this);
        html.setCss("overflow", "auto");
        html.setCss("display", "block");
        html.addClass("rwt-list-box");
        container.getHtml().setCss("list-style-type", "none");
        container.getHtml().setCss("margin", "0px");
        container.getHtml().setCss("padding", "0px");
        html.layout("$RWT.listBox.layout");
    }

    public void add(ListBoxItem item) {
        item.unmarkCurrent();
        container.add(new ItemHolder(item));
        item.addClickHandler(itemClickHandler);
    }

    public void insert(final ListBoxItem item, final int row) {
        item.unmarkCurrent();
        container.add(row, new ItemHolder(item));
        item.addClickHandler(itemClickHandler);
    }

    private void itemDoubleClick(ListBoxItem item) {
        if (item != null) {
            setCurrent(item);
            defaultDoubleClickListener.itemDoubleClick(item);
        }
    }

    public void clear() {
        for (UIObject obj : container.getChildren()) {

            ((ItemHolder) obj).dispose();
        }
        container.clear();
    }

    private ItemHolder findHolder(ListBoxItem item) {
        for (UIObject obj : container.getChildren()) {
            if (((ItemHolder) obj).item == item) {
                return (ItemHolder) obj;
            }
        }
        return null;
    }

    public void remove(ListBoxItem item) {
        ItemHolder holder = findHolder(item);
        if (holder != null) {
            container.remove(holder);
            if (item == currentItem) {
                setCurrent(null);
            }
            item.removeClickHandler(itemClickHandler);
        }
    }

    public void removeRow(final int row) {
        remove(getItem(row));
    }

    private void fireCurrentItemChange() {
        defaultCurrentItemListener.currentItemChanged(currentItem);
    }

    public void setCurrent(ListBoxItem item) {
        ItemHolder holder = findHolder(item);
        if (holder != null) {
            if (currentItem != null) {
                currentItem.unmarkCurrent();
            }
            currentItem = item;
            if (currentItem != null) {
                currentItem.markCurrent();
            }
            fireCurrentItemChange();
        }
    }

    public void setCurrentRow(final int row) {
        setCurrent(getItem(row));
    }

    public ListBoxItem getCurrent() {
        return currentItem;
    }

    public int getCurrentRow() {
        final List<UIObject> children = container.getChildren();
        for (int i = 0, count = children.size(); i < count; i++) {
            if (((ItemHolder) children.get(i)).item == currentItem) {
                return i;
            }
        }
        return -1;
    }

    public ListBoxItem getItem(int row) {
        final List<UIObject> children = container.getChildren();
        if (row > -1 && row < children.size()) {
            return ((ItemHolder) children.get(row)).item;
        }
        throw new IndexOutOfBoundsException("index=" + row + " size=" + children.size());
    }

    public List<ListBoxItem> getItems() {
        final List<ListBoxItem> items = new LinkedList<ListBoxItem>();
        for (UIObject child : container.getChildren()) {
            items.add(((ItemHolder) child).item);
        }
        return Collections.unmodifiableList(items);
    }

    public void swapItems(final ListBoxItem item1, final ListBoxItem item2) {
        final ItemHolder holder1 = findHolder(item1);
        final ItemHolder holder2 = findHolder(item2);
        if (holder1 == null || holder2 == null || holder1 == holder2) {
            return;
        }
        holder1.getHtml().clear();
        holder2.getHtml().clear();
        final ListBoxItem newItem1 = item1.copy();
        final ListBoxItem newItem2 = item2.copy();
        newItem1.addClickHandler(itemClickHandler);
        newItem2.addClickHandler(itemClickHandler);

        holder1.changeItem(newItem2);
        holder2.changeItem(newItem1);

        if (ListBox.this.currentItem == item1) {
            newItem1.markCurrent();
            ListBox.this.currentItem = newItem1;
        } else if (ListBox.this.currentItem == item2) {
            newItem2.markCurrent();
            ListBox.this.currentItem = newItem2;
        }
    }

    public void swapRows(final int row1, final int row2) {
        swapItems(getItem(row1), getItem(row2));
    }

    public int count() {
        return container.getChildren().size();
    }

    public void removeCurrentItemListener(CurrentItemListener l) {
        defaultCurrentItemListener.removeCurrentItemListener(l);
    }

    public void addCurrentItemListener(CurrentItemListener l) {
        defaultCurrentItemListener.addCurrentItemListener(l);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        return container.findObjectByHtmlId(id);

    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        container.visit(visitor);
    }

    public void removeDoubleClickListener(DoubleClickListener l) {
        defaultDoubleClickListener.removeDoubleClickListener(l);
    }

    public void addDoubleClickListener(DoubleClickListener l) {
        defaultDoubleClickListener.addDoubleClickListener(l);
    }
}