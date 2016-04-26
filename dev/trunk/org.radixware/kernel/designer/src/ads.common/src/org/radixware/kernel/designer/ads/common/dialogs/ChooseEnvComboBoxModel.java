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

package org.radixware.kernel.designer.ads.common.dialogs;

import java.util.ArrayList;
import java.util.WeakHashMap;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

/**
 * Combo box model for {@linkplain  ESystemComponent} based value choose
 */
public class ChooseEnvComboBoxModel implements ComboBoxModel {

    private class Item {

        ERuntimeEnvironmentType env;

        public Item(ERuntimeEnvironmentType env) {
            this.env = env;
        }

        @Override
        public String toString() {
            return env.getName();
        }
    }
    private ArrayList<Item> items = new ArrayList<Item>();
    private Item selected;

    public ChooseEnvComboBoxModel() {
        for (ERuntimeEnvironmentType sc : ERuntimeEnvironmentType.values()) {
            items.add(new Item(sc));
        }
        selected = items.get(0);
    }

    public void setSelectedItem(Object anItem) {
        if (anItem instanceof Item) {
            selected = (Item) anItem;
        }
    }

    public Object getSelectedItem() {
        return selected;
    }

    public int getSize() {
        return items.size();
    }

    public Object getElementAt(int index) {
        return items.get(index);
    }
    private WeakHashMap<ListDataListener, Object> listeners = null;

    public void addListDataListener(ListDataListener l) {
        synchronized (this) {
            if (listeners == null) {
                listeners = new WeakHashMap<ListDataListener, Object>();
            }
            listeners.put(l, l);
        }
    }

    public void removeListDataListener(ListDataListener l) {
        synchronized (this) {
            if (listeners != null) {
                listeners.remove(l);
            }
        }
    }

    public ERuntimeEnvironmentType getCurrentEnv() {
        return selected.env;
    }

    public void setCurrentEnv(ERuntimeEnvironmentType e) {
        for (Item item : items) {
            if (item.env == e) {
                setSelectedItem(item);
            }
        }
    }
}
