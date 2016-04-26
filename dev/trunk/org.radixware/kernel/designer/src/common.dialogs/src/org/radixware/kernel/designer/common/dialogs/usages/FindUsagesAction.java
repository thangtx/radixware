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

package org.radixware.kernel.designer.common.dialogs.usages;

import javax.swing.Action;
import javax.swing.KeyStroke;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.resources.RadixWareIcons;

/**
 * Find usages global action. Remapped in editor and explorer.
 */
public final class FindUsagesAction extends CookieAction {

    public static class FindUsagesCookie implements org.openide.nodes.Node.Cookie {

        private final Definition definition;

        public FindUsagesCookie(final Definition definition) {
            this.definition = definition;
        }

        public Definition getDefinition() {
            return definition;
        }
    }

    public FindUsagesAction() {
       // putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F7"));
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{FindUsagesCookie.class};
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.TREE.DEPENDENCIES.getResourceUri();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(FindUsagesAction.class, "CTL_FindUsagesAction");
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
            final FindUsagesCookie findUsagesCookie = node.getLookup().lookup(FindUsagesCookie.class);
            if (findUsagesCookie != null) {
                final Definition definition = findUsagesCookie.getDefinition();
                final FindUsagesCfg cfg = FindUsagesCfgPanel.askCfg(definition);
                if (cfg != null) {
                    FindUsages.search(cfg);
                }
                return;
            }
        }
    }
}
