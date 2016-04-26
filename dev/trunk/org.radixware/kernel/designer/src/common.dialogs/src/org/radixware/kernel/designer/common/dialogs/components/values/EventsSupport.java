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

package org.radixware.kernel.designer.common.dialogs.components.values;

import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;


public abstract class EventsSupport<ListenerType extends EventListener, EventType> {
    private final List<ListenerType> listeners = new LinkedList<>();

    public final void addListener(ListenerType listener) {
        listeners.add(listener);
    }

    public final void removeListener(ListenerType listener) {
        listeners.remove(listener);
    }

    public final boolean hasListeners() {
        return !listeners.isEmpty();
    }

    protected final Collection<ListenerType> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }

    public void fireChange(EventType event) {
        for (final ListenerType listener : listeners) {
            performEvent(event, listener);
        }
    }

    protected abstract void performEvent(EventType event, ListenerType listener);
}
