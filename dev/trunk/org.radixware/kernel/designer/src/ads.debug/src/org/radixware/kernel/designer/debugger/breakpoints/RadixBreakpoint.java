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

import com.sun.jdi.event.Event;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.scml.LineMatcher;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

import org.radixware.kernel.designer.debugger.impl.JVMEventHandler;


public abstract class RadixBreakpoint extends Breakpoint implements JVMEventHandler.PreconditionEventHandler {

    private boolean enabled = true;

    static class BreakpointInfo extends LineMatcher.LocationInfo {

        public BreakpointInfo(String layerUri, Id moduleId, Id[] definitionPath, String suffix, String suffux2) {
            super(layerUri, moduleId, definitionPath, suffix, suffux2);
        }

        public static BreakpointInfo load(String config) {
            LineMatcher.LocationInfo loc = LineMatcher.decode(config);
            if (loc == null) {
                return null;
            }
            return new BreakpointInfo(loc.layerUri, loc.moduleId, loc.definitionPath, loc.suffix1, loc.suffix2);
        }

        public String save() {
            return encode(layerUri, moduleId, definitionPath, suffix1, suffix2);
        }

        public int getLineNumber() {
            return Integer.parseInt(suffix2);
        }

        public String getSourceName() {
            return encode(layerUri, moduleId, definitionPath, suffix1, null);
        }
    }

    abstract String getTypeId();

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void disable() {
        if (enabled) {
            enabled = false;
            firePropertyChange(PROP_ENABLED, true, false);
        }
    }

    @Override
    public void enable() {
        if (!enabled) {
            enabled = true;
            firePropertyChange(PROP_ENABLED, false, true);
        }
    }

    public abstract boolean isInvalid();

    @Override
    public boolean precondition(Event event) {
        return true;
    }

    @Override
    public boolean processEvent(Event event) {
        return false;
    }

    public abstract RadixObject getRadixObject();

    public abstract String getDisplayName();

    public abstract String getShortDescription();

    abstract String getConfigStr();

    public abstract List<String> getClassName();

    static List<JmlBreakpoint> resolve(String configStrWithBpType) {

        Collection<Branch> loadedBranches = RadixFileUtil.getOpenedBranches();
        while (loadedBranches.isEmpty()) {
            loadedBranches = RadixFileUtil.getOpenedBranches();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        List<JmlBreakpoint> result = new LinkedList<JmlBreakpoint>();

        int colon = configStrWithBpType.indexOf(':');
        if (colon < 0) {
            return result;
        }
        String typeId = configStrWithBpType.substring(0, colon);
        String configStr = configStrWithBpType.substring(colon + 1);

        if (typeId.equals(JmlBreakpoint.TYPE_ID)) {
            BreakpointInfo info = BreakpointInfo.load(configStr);
            if (info != null) {
                try {
                    info.getLineNumber();
                } catch (NumberFormatException e) {
                    return Collections.emptyList();
                }
                for (Branch branch : loadedBranches) {
                    JmlBreakpoint bp = JmlBreakpoint.resolve(branch, info);
                    if (bp != null) {
                        result.add(bp);
                    }
                }
            }
        } 
        return result;
    }
}
