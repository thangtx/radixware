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
package org.radixeare.kernel.designer.ads.build.release.actions;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.repository.Branch;

public class AbstractReleaseAction extends CookieAction {

    private final boolean isPatch;

    AbstractReleaseAction(boolean isPatch) {
        this.isPatch = isPatch;
    }

    @Override
    public String getName() {
        return isPatch ? "Create Patch..." : "Make Release...";
    }

    public static class MakeReleaseCookie implements Node.Cookie {

        Branch radixObject;

        public MakeReleaseCookie(Branch radixObject) {
            this.radixObject = radixObject;
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        for (Node node : activatedNodes) {
            final MakeReleaseCookie buildCookie = node.getLookup().lookup(MakeReleaseCookie.class);
            if (buildCookie != null) {
                try {
                    ReleaseActionPerformer.perform(buildCookie.radixObject);
                } catch (Throwable ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{MakeReleaseCookie.class};
    }
}
