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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.rwt.ListBox.ListBoxItem;


public abstract class DropDownListDelegate<T> extends InputBox.DropDownDelegate<ListBox> {

    private boolean showCurrentItemInList;

    public static class DropDownListItem<T> extends ListBox.ListBoxItem {

        private final T value;

        public DropDownListItem(final String title, final T value, final Icon icon) {
            super();
            this.value = value;
            setText(title);
            setIcon(icon);
            setTabIndex(-1);
        }

        public DropDownListItem(final String title, final T value) {
            this(title, value, null);
        }

        public T getValue() {
            return value;
        }
    }

    private class CurrentItemListener implements ListBox.CurrentItemListener {

        private final InputBox inputBox;

        public CurrentItemListener(final ListBox listBox, final InputBox inputBox) {
            this.inputBox = inputBox;
        }

        @Override
        public void currentItemChanged(final ListBoxItem currentItem) {
            DropDownListDelegate.this.setCurrentItem(currentItem, inputBox);
        }
    }

    @SuppressWarnings("unchecked")
    private void setCurrentItem(final ListBoxItem currentItem, final InputBox inputBox) {
        if (currentItem != null) {
            try {
                updateInputBox(((DropDownListItem<T>) currentItem), inputBox);
            } finally {
                inputBox.closeActiveDropDown();
            }
        }
    }

    public final boolean isCurrentItemDisplayedInDropDownList() {
        return showCurrentItemInList;
    }

    public final void setDisplayCurrentItemInDropDownList(final boolean display) {
        showCurrentItemInList = display;
    }
   
    @SuppressWarnings("unchecked")
    protected boolean updateInputBox(final DropDownListItem<T> currentItem, final InputBox inputBox) {
        final T value = currentItem.getValue();
        inputBox.setValue(value);
        if (value != null) {
            inputBox.setValueIcon(currentItem.getIcon());
        }
        return true;
    }

    @Override
    protected final ToolButton createDropDownButton() {
        final ToolButton listBoxButton = new ToolButton();
        listBoxButton.setIcon(WsIcons.SPIN_DOWN);
        listBoxButton.setIconHeight(5);
        listBoxButton.setIconWidth(8);
        return listBoxButton;
    }

    @Override
    protected void updateButton(final ToolButton button, final InputBox inputBox) {
        final List<DropDownListItem<T>> items = getItems();
        final int itemsCount = items == null ? 0 : items.size();
        final boolean isValid = inputBox.getValidationMessage() == null || inputBox.getValidationMessage().isEmpty();
        final boolean isPopupEnabled = !inputBox.isReadOnly()
                && itemsCount > 0
                && (itemsCount > 1 || inputBox.getValue() == null || !isValid || inputBox.isClearable());
        button.setVisible(isPopupEnabled);
        if (items != null && inputBox.getValue() != null) {
            for (DropDownListItem<T> item : items) {
                if (Utils.equals(item.getValue(), inputBox.getValue())) {
                    inputBox.setValueIcon(item.getIcon());
                    break;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final ListBox createUIObject(final InputBox box, final InputBox.DisplayController displayController) {
        final ListBox list = new ListBox();
        list.setParent(box);
        list.addCurrentItemListener(new CurrentItemListener(list, box));
        list.setBackground(Color.WHITE);
        boolean hasNullItem = false;
        final List<DropDownListItem<T>> items = getItems();
        if (items != null) {
            for (DropDownListItem item : items) {
                if (item == null) {
                    continue;
                }
                if (showCurrentItemInList || !Utils.equals(box.getValue(), item.getValue())) {
                    list.add(item);
                    if (item.getValue() == null) {
                        hasNullItem = true;
                    }
                }
            }
        }

        if (box.isClearable() && !box.isReadOnly() && !hasNullItem && box.getValue() != null) {
            final String nullItemTitle = displayController.getDisplayValue(null, false, false);
            final Icon nullItemIcon = box.getApplication().getImageManager().getIcon(ClientIcon.Editor.CLEAR);
            list.add(new DropDownListItem<T>(nullItemTitle, null, nullItemIcon));
        }
        return list;
    }

    @Override
    protected List<String> getInputHandlers(final InputBox box) {
        final List<DropDownListItem<T>> items = getItems();
        if (box.isEnabled() && !box.isReadOnly() && items != null && !items.isEmpty()) {
            return Arrays.asList("up", "down", "pageup", "pagedown", "home", "end", "alt+down", "wheel");
        } else {
            return Collections.emptyList();
        }
    }

    private void changeCurrentItem(final List<DropDownListItem<T>> items, final int delta, final InputBox box) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (Utils.equals(box.getValue(), items.get(i).getValue())) {
                final int index;
                if (delta > 0) {
                    index = Math.min(i + delta, items.size() - 1);
                } else {
                    index = Math.max(i + delta, 0);
                }
                if (index != i) {
                    setCurrentItem(items.get(index), box);
                }
                return;
            }
        }
        if (box.getValue() == null && !items.isEmpty()) {
            setCurrentItem(items.get(0), box);
        }
    }

    @Override
    protected void handleHotKey(final String hotKey, final InputBox box) {
        final List<DropDownListItem<T>> items = getItems();
        if (items != null && !items.isEmpty()) {
            if ("alt+down".equals(hotKey)) {
                expose(box);
            } else if ("home".equals(hotKey)) {
                setCurrentItem(items.get(0), box);
            } else if ("end".equals(hotKey)) {
                setCurrentItem(items.get(items.size() - 1), box);
            } else if ("up".equals(hotKey) || "pageup".equals(hotKey)) {
                changeCurrentItem(items, 1, box);
            } else if ("down".equals(hotKey) || "pagedown".equals(hotKey)) {
                changeCurrentItem(items, -1, box);
            }
        }
    }

    @Override
    protected void handleMouseWheel(final int wheel, final InputBox box) {
        //changeCurrentItem(getItems(), wheel, box);//RADIX-7110
    }

    protected abstract List<DropDownListItem<T>> getItems();
}
