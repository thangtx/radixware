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

package org.radixware.kernel.common.utils.events;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Generic event source for radix events.
 * General purpose: notify registered {@linkplain IRadixEventListener listeners} about some {@linkplain  RadixEvent event}
 * Requirements: to subscribe on events from the events source you should
 * implement interface {@linkplain IRadixEventListener} with 
 * type of event same with event source event type
 * 
 */
public class RadixEventSource<T extends IRadixEventListener<E>, E extends RadixEvent> {

    private WeakHashMap<T, Object> listeners;

    /*
     * Adds new listener to the source
     **/
    public synchronized void addEventListener(T listener) {
        if (listeners == null) {
            listeners = new WeakHashMap<T, Object>(1); // memory optimization
        }
        listeners.put(listener, null);
    }

    /**
     * Removes specvified listener from source
     */
    public synchronized void removeEventListener(T listener) {
        if (listener == null) {
            return;
        }
        if (listeners == null) {
            return;
        }
        
        listeners.remove(listener);

        if (listeners.isEmpty()) {
            listeners = null;
        }
    }

    /**
     * Notifies all of listeners about radix event e
     */
    public void fireEvent(E event) {
        final List<T> list;
        synchronized (this) {
            if (listeners != null) {
                list = new ArrayList<T>(listeners.keySet());
            } else {
                list = null;
            }
        }
        if (list != null) {
            for (T l : list) {
                if (l != null) {
                    l.onEvent(event);
                }
            }
        }
    }
}
