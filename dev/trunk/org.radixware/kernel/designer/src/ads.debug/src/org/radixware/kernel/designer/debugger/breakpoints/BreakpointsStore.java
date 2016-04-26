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

package org.radixware.kernel.designer.debugger.breakpoints;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class BreakpointsStore {

    private static final String CONFIG_PARAM_NAME_BREAKPOINTS = "last-breakpoint-set";
    private List<JmlBreakpoint> knownBreakpoints = null;
    private final Object lock = new Object();
    private static final BreakpointsStore instance = new BreakpointsStore();

    public static BreakpointsStore getInstance() {
        return instance;

    }

    private BreakpointsStore() {
    }

    public void saveBreakpoints() {
        synchronized (lock) {
            StringBuilder sb = new StringBuilder();
            Set<String> configs = new HashSet<String>();
            if (knownBreakpoints != null) {
                for (RadixBreakpoint bp : knownBreakpoints) {
                    String bpConfig = bp.getConfigStr();
                    if (bpConfig != null && !configs.contains(bpConfig)) {
                        sb.append(bp.getTypeId()).
                                append(":").
                                append(bpConfig).
                                append("\n");
                        configs.add(bpConfig);
                    }
                }
            }

            Preferences.userNodeForPackage(BreakpointAnnotationListener.class).put(CONFIG_PARAM_NAME_BREAKPOINTS, sb.toString().replace("\uFFFF", "&separator&"));
            try {
                Preferences.userNodeForPackage(BreakpointAnnotationListener.class).flush();
            } catch (BackingStoreException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    public void addBreakpoint(JmlBreakpoint bp) {
        synchronized (lock) {
            initBreakpoints();
            knownBreakpoints.add(bp);
            saveBreakpoints();
        }
    }

    public void removeBreakpoint(JmlBreakpoint bp) {
        synchronized (lock) {
            initBreakpoints();
            knownBreakpoints.remove(bp);
            saveBreakpoints();
        }
    }

    public List<JmlBreakpoint> initBreakpoints() {
        synchronized (lock) {
            if (knownBreakpoints == null) {
                knownBreakpoints = new LinkedList<JmlBreakpoint>();
                String config = Preferences.userNodeForPackage(BreakpointAnnotationListener.class).get(CONFIG_PARAM_NAME_BREAKPOINTS, null);
                if (config != null) {

                    config = config.replace("&separator&", "\uFFFF");
                    String[] bpcs = config.split("\n");
                    knownBreakpoints = new LinkedList<JmlBreakpoint>();
                    for (String bpc : bpcs) {
                        if (!bpc.isEmpty()) {
                            List<JmlBreakpoint> bps = JmlBreakpoint.resolve(bpc);
                            if (!bps.isEmpty()) {
                                knownBreakpoints.addAll(bps);
                            }
                        }
                    }

                }
            }
            return knownBreakpoints;
        }

    }
}
