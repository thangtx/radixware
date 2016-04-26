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

package org.radixware.kernel.designer.common.dialogs.components.selector;

import java.util.LinkedList;
import java.util.List;


public class SelectionChangeSupport<TItem> {

    private final Object source;
    private final List<SelectionListener<TItem>> listeners = new LinkedList<>();

    public SelectionChangeSupport(Object source) {
        this.source = source;
    }

    public void addSelectionListener(SelectionListener<TItem> listener) {
        listeners.add(listener);
    }

    public void removeSelectionListener(SelectionListener<TItem> listener) {
        listeners.remove(listener);
    }

    public void fireSelectionChange(TItem newValue) {
        for (SelectionListener<TItem> listener : listeners) {
            listener.selectionChanged(new SelectionEvent<TItem>(source, newValue));
        }
    }

    public boolean hasListeners() {
        return !listeners.isEmpty();
    }
}
