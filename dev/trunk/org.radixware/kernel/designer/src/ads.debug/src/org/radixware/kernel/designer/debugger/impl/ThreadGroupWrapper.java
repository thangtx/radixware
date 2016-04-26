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

package org.radixware.kernel.designer.debugger.impl;

import com.sun.jdi.ThreadGroupReference;
import java.lang.ref.WeakReference;
import java.util.List;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class ThreadGroupWrapper {

    final WeakReference<ThreadGroupReference> ref;
    public static final String PROP_NAME_ADDED = "prop-group-added";
    private RadixDebugger debugger;

    public ThreadGroupWrapper(RadixDebugger debugger, ThreadGroupReference tg) {
        this.ref = new WeakReference<ThreadGroupReference>(tg);
        this.debugger = debugger;
    }

    public ThreadGroupReference getThreadGroupReference() {
        return this.ref.get();
    }

    public String getDisplayName() {
        ThreadGroupReference groupRef = getThreadGroupReference();
        if (groupRef != null) {
            return groupRef.name();
        } else {
            return "<Collected Thread Group>";
        }
    }

    public List<ThreadGroupWrapper> getGroups() {
        return debugger.getThreadsCache().getSubGroups(this);
    }

    public List<ThreadWrapper> getThreads() {
        return debugger.getThreadsCache().getThreads(this);
    }

    public boolean isCurrentThreadGroup() {
        List<ThreadWrapper> threads = getThreads();
        for (ThreadWrapper w : threads) {
            if (w.isCurrent()) {
                return true;
            }
        }
        List<ThreadGroupWrapper> groups = getGroups();
        for (ThreadGroupWrapper g : groups) {
            if (g.isCurrentThreadGroup()) {
                return true;
            }
        }
        return false;
    }
}
