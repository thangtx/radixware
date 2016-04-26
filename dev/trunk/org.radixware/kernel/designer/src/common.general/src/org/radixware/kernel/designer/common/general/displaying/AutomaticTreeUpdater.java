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

package org.radixware.kernel.designer.common.general.displaying;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class AutomaticTreeUpdater {

    public static interface State {

        boolean wasChanged();

        void update();

        RadixObject getRadixObject();
    }
    private static final AutomaticTreeUpdater INSTANCE = new AutomaticTreeUpdater();
    private final List<State> states = new LinkedList<State>();
    private final RadixObject.IRemoveListener removeListener = new RadixObject.IRemoveListener() {

        @Override
        public void onEvent(RemovedEvent e) {
            unregister(e.radixObject);
        }
    };
    private static final long DELAY_MS = 1000;
    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            RadixMutex.readAccess(new Runnable() {

                @Override
                public void run() {
                    perform();
                }
            });
        }
    };

    public static AutomaticTreeUpdater getDefault() {
        return INSTANCE;
    }

    public AutomaticTreeUpdater() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(runnable, DELAY_MS, DELAY_MS, TimeUnit.MILLISECONDS);
    }

    public void register(State state) {
        assert state != null;

        synchronized (states) {
            states.add(state);
            state.getRadixObject().getRemoveSupport().addEventListener(removeListener);
        }
    }

    public void unregister(RadixObject radixObject) {
        assert radixObject != null;

        synchronized (states) {
            Iterator<State> iterator = states.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getRadixObject() == radixObject) {
                    iterator.remove();
                }
            }
            radixObject.getRemoveSupport().removeEventListener(removeListener);
        }
    }

    private void perform() {
        final List<State> copy;
        synchronized (states) {
            copy = new ArrayList<State>(states);
        }
        for (State state : copy) {
            if (state.wasChanged()) {
                state.update();
            }
        }
    }
}
