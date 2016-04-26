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

package org.radixware.kernel.designer.common.dialogs.components;

import java.util.*;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel.Item;


public class KernelEnumComboBoxModel<T extends Enum<T> & IKernelEnum>
    extends AbstractListModel<Item<T>> implements ComboBoxModel<Item<T>> {

    public static final Item NULL_ITEM = new SpecialItem("<null>");
    public static final Item INHERITED = new SpecialItem("inherit");

    public static class SpecialItem<T extends Enum<T> & IKernelEnum> extends Item<T> {

        private final String value;

        public SpecialItem(String value) {
            super(null);

            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SpecialItem<T> other = (SpecialItem<T>) obj;
            if (!Objects.equals(this.value, other.value)) {
                return false;
            }
            return true;
        }
    }

    public static class Item<T extends Enum<T> & IKernelEnum> {

        private final T source;

        public Item(T source) {
            this.source = source;
        }

        public final T getSource() {
            return source;
        }

        @Override
        public String toString() {
            if (source != null) {
                return source.getName();
            }
            return "<null>";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Item<T> other = (Item<T>) obj;
            if (!Objects.equals(this.source, other.source)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.source);
            return hash;
        }
    }

    private Item<T> selected;
    private final List<Item<T>> items = new ArrayList<>();
    private final List<Item<T>> allItems = new ArrayList<>();

    public KernelEnumComboBoxModel(Class<T> enumClass, T selected) {
        this(enumClass, Collections.<Item<T>>emptySet(), Collections.<T>emptySet(), selected);
    }

    public KernelEnumComboBoxModel(Class<T> enumClass, Collection<T> include, T selected) {
        assert enumClass != null;
        if (enumClass != null) {

            for (final T elem : EnumSet.allOf(enumClass)) {
                final Item<T> item = new Item<>(elem);
                allItems.add(item);

                if (include == null || include.contains(elem)) {
                    items.add(item);
                }
            }

            this.selected = getBy(selected);
        }
    }

    public KernelEnumComboBoxModel(Class<T> enumClass, Collection<Item<T>> additional, Collection<T> exclude, T selected) {

        assert enumClass != null;
        if (enumClass != null) {

            if (additional != null && !additional.isEmpty()) {
                allItems.addAll(additional);
                items.addAll(additional);
            }

            for (final T elem : EnumSet.allOf(enumClass)) {
                final Item<T> item = new Item<>(elem);
                allItems.add(item);

                if (exclude != null && !exclude.contains(elem)) {
                    items.add(item);
                }
            }

            this.selected = getBy(selected);
        }
    }

    public KernelEnumComboBoxModel(Class<T> enumClass, Collection<Item<T>> additional, Collection<T> exclude, Item<T> selected) {
        this(enumClass, additional, exclude, (T) null);

        this.selected = items.indexOf(selected) >= 0 ? selected : NULL_ITEM;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public Item<T> getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public void setSelectedItem(Object selected) {
        if (items.indexOf(selected) == -1) {
            this.selected = NULL_ITEM;
        } else {
            this.selected = (Item<T>) selected;
        }

        fireContentsChanged(selected, 0, getSize());
    }

    @Override
    public Item<T> getSelectedItem() {
        return selected;
    }

    public T getSelectedItemSource() {
        return getSelectedItem().getSource();
    }

    public void setSelectedItemSource(T source) {
        setSelectedItem(getBy(source));
    }

    public final Item<T> getBy(T source) {
        for (final Item<T> item : allItems) {
            if (item.source == source) {
                return item;
            }
        }
        return NULL_ITEM;
    }
}
