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

package org.radixware.kernel.common.defs;

import java.util.concurrent.ConcurrentHashMap;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;

import org.radixware.kernel.common.defs.EntireChangesSupport.EntireChangeEvent;
import org.radixware.kernel.common.defs.EntireChangesSupport.EntireChangeListener;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public class EntireChangesSupport extends RadixEventSource<EntireChangeListener, EntireChangeEvent> implements RadixObjects.ContainerChangesListener {

    @Override
    public void onEvent(ContainerChangedEvent e) {
        fireChange();
    }
    private static final ConcurrentHashMap<Class<? extends Definition>, EntireChangesSupport> sps = new ConcurrentHashMap<>();

    public static final class EntireChangeEvent extends RadixEvent {

        public final RadixObject eventSource;

        public EntireChangeEvent(RadixObject eventSource) {
            this.eventSource = eventSource;
        }

        public EntireChangeEvent() {
            this.eventSource = null;
        }
    }

    public interface EntireChangeListener extends IRadixEventListener<EntireChangeEvent> {
    }

    /**
     * Map expected here to separate events for different defintiion classes but
     * until only class catalogs are really use this feature single sattic field
     * is in use
     */
    //private static final EntireChangesSupport entireChangesSupport = new EntireChangesSupport();
    public static EntireChangesSupport getInstance(Class<? extends Definition> clazz) {
        final EntireChangesSupport newEntireChangesSupport = new EntireChangesSupport();
        final EntireChangesSupport oldEntireChangesSupport = sps.putIfAbsent(clazz, newEntireChangesSupport);
        return (oldEntireChangesSupport != null ? oldEntireChangesSupport : newEntireChangesSupport);
    }

    public void fireChange() {
        fireEvent(new EntireChangeEvent());
    }

    public void fireChange(RadixObject eventSource) {
        fireEvent(new EntireChangeEvent(eventSource));
    }
}
