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

package org.radixware.kernel.designer.debugger.actions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import org.netbeans.spi.debugger.ActionsProvider;
import org.netbeans.spi.debugger.ActionsProviderListener;
import org.netbeans.spi.debugger.ContextProvider;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public abstract class AsyncActionProvider extends ActionsProvider {

    protected final RadixDebugger debugger;

    protected AsyncActionProvider(ContextProvider contextProvider) {
        debugger = contextProvider.lookupFirst(null, RadixDebugger.class);
    }
    private boolean isEnabled = false;

    @Override
    public boolean isEnabled(Object action) {
        checkIsEnabledImpl(action);
        return isEnabled;
    }

    protected abstract boolean checkIsEnabled(Object action);

    private void checkIsEnabledImpl(Object action) {
        synchronized (this) {
            boolean e = checkIsEnabled(action);
            if (e != isEnabled) {
                isEnabled = e;
                fireChange(action);
            }
        }
    }
    private final List<ActionsProviderListener> listeners = new LinkedList<ActionsProviderListener>();

    @Override
    public void addActionsProviderListener(ActionsProviderListener l) {
        listeners.add(l);
    }

    @Override
    public void removeActionsProviderListener(ActionsProviderListener l) {
        listeners.remove(l);
    }

    protected void fireChange(final Object action) {
        final boolean enabled;
        synchronized (this) {
            enabled = checkIsEnabled(action);
        }
        debugger.getRequestProcessor().post(new Runnable() {

            @Override
            public void run() {
                synchronized (listeners) {
                    for (ActionsProviderListener l : listeners) {
                        l.actionStateChange(action, enabled);
                    }
                }
            }
        });
    }

    @Override
    public void postAction(final Object action, final Runnable actionPerformedNotifier) {
        debugger.getRequestProcessor().post(new Runnable() {

            @Override
            public void run() {
                final Lock lock = debugger.getAccessLock().writeLock();
                if (lock.tryLock()) {
                    try {
                        if (isEnabled(action)) {
                            try {
                                doAction(action);
                                checkIsEnabledImpl(action);
                            } finally {
                                actionPerformedNotifier.run();
                            }
                        }
                    } finally {
                        lock.unlock();
                    }
                } else {
                    checkIsEnabledImpl(action);
                }
            }
        });
    }
}
