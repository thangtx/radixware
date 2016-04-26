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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
//import org.radixware.kernel.designer.debugger.breakpoints.RadixBreakpoints;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public class ToggleMethodBreakpointAction extends AdsDefinitionAction {

    public static class ToggleMethodBreakpointCookie implements Node.Cookie {

        private AdsMethodDef method;

        public ToggleMethodBreakpointCookie(AdsMethodDef method) {
            this.method = method;
        }

        private boolean isEnabled() {
            return true;
        }

        private void toggle() {
       //     RadixBreakpoints.toggleMethodBreakpoint(method);
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            ToggleMethodBreakpointCookie cookie = activatedNodes[0].getCookie(ToggleMethodBreakpointCookie.class);
            if (cookie != null) {
                return cookie.isEnabled();
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ToggleMethodBreakpointCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            ToggleMethodBreakpointCookie cookie = activatedNodes[0].getCookie(ToggleMethodBreakpointCookie.class);
            if (cookie != null) {
                cookie.toggle();
            }
        }
    }

    @Override
    public String getName() {
        return "Toggle Breakpoint";
    }
}
