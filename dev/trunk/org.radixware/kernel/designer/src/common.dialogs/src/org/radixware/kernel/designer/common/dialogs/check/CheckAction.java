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

package org.radixware.kernel.designer.common.dialogs.check;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.defs.RadixObject;

/**
 * Check Radix objects global action. Remapped in editor and explorer.
 *
 */
public final class CheckAction extends CookieAction {

    public static class CheckCookie implements org.openide.nodes.Node.Cookie {

        private final RadixObject radixObject;

        public CheckCookie(final RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        public RadixObject getRadixObject() {
            return radixObject;
        }
        //public abstract Collection<RadixObject> getSelectedObjects();
    }

    public CheckAction() {
        //setIcon(RadixWareDesignerIcon.CHECK.CHECK.getIcon());
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F9"));
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class<?>[]{CheckCookie.class};
    }

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CheckAction.class, "CTL_CheckAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    private CheckOptions requestCheckOptions() {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = getClass().getClassLoader();
            }
            Class c = cl.loadClass("org.radixware.kernel.designer.tree.ads.check.ConfigureCheck");
            return (CheckOptions) c.getMethod("configureCheck", new Class[]{}).invoke(null, new Object[0]);
        } catch (Throwable e) {
            return new CheckOptions(false, true, null);
        }
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        final Collection<RadixObject> radixObjects = new ArrayList<>();
        for (Node node : activatedNodes) {
            final CheckCookie checkCookie = node.getLookup().lookup(CheckCookie.class);
            if (checkCookie != null) {
                radixObjects.add(checkCookie.getRadixObject());
            }
        }
        
        CheckOptions options = requestCheckOptions();
        
        if (options != null){
            final RadixObjectsChecker radixObjectsChecker = new RadixObjectsChecker(options);
            radixObjectsChecker.check(radixObjects);
        }
    }

    @Override
    protected String iconResource() {
        return "org/radixware/kernel/designer/common/dialogs/check/check_xml.png";
    }
}
