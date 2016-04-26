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

import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;


public class RadixBreakpoints {

    public static void toggleMethodBreakpoint(AdsMethodDef context) {
        if (context == null) {
            return;
        }
        Breakpoint[] breakpoints = DebuggerManager.getDebuggerManager().getBreakpoints();
        int i, k = breakpoints.length;

        for (i = 0; i < k; i++) {
            if (breakpoints[i] instanceof MethodBreakpoint && ((MethodBreakpoint) breakpoints[i]).isSameMethod(context)) {
                DebuggerManager.getDebuggerManager().removeBreakpoint(breakpoints[i]);
                return;
            }
        }

        MethodBreakpoint b = new MethodBreakpoint(context);
        if (b != null) {
            DebuggerManager.getDebuggerManager().addBreakpoint(b);
        }
    }
}
