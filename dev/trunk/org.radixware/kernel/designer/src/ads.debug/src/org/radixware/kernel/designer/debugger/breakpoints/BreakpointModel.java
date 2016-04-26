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

import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;


public class BreakpointModel implements NodeModel {

    public static final String BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/NonLineBreakpoint";
    public static final String LINE_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/Breakpoint";
    public static final String CURRENT_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/NonLineBreakpointHit";
    public static final String CURRENT_LINE_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/BreakpointHit";
    public static final String DISABLED_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/DisabledNonLineBreakpoint";
    public static final String DISABLED_LINE_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/DisabledBreakpoint";
    public static final String DISABLED_CURRENT_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/DisabledNonLineBreakpointHit";
    public static final String DISABLED_CURRENT_LINE_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/DisabledBreakpointHit";
    public static final String LINE_CONDITIONAL_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/ConditionalBreakpoint";
    public static final String CURRENT_LINE_CONDITIONAL_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/ConditionalBreakpointHit";
    public static final String DISABLED_LINE_CONDITIONAL_BREAKPOINT =
            "org/netbeans/modules/debugger/resources/breakpointsView/DisabledConditionalBreakpoint";

    @Override
    public String getDisplayName(Object node) throws UnknownTypeException {
        if (node instanceof RadixBreakpoint) {
            RadixBreakpoint bp = (RadixBreakpoint) node;
            return bp.getDisplayName();
        } else {
            return "";
        }
    }

    @Override
    public String getIconBase(Object node) throws UnknownTypeException {
        if (node instanceof RadixBreakpoint) {
            RadixBreakpoint bp = (RadixBreakpoint) node;
            if (bp.isEnabled()) {
                return LINE_BREAKPOINT;
            } else {
                return DISABLED_LINE_BREAKPOINT;
            }
        } else {
            return "";
        }
    }

    @Override
    public String getShortDescription(Object node) throws UnknownTypeException {
        if (node instanceof RadixBreakpoint) {
            RadixBreakpoint bp = (RadixBreakpoint) node;
            return bp.getShortDescription();
        } else {
            return "";
        }
    }

    @Override
    public void addModelListener(ModelListener l) {
    }

    @Override
    public void removeModelListener(ModelListener l) {
    }
}
