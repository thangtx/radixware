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
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.designer.ads.common.dialogs.ProfilePanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public class TuneProfileAction extends AdsDefinitionAction {

    public static class TuneProfileCookie implements Node.Cookie {

        final IProfileable profileable;

        public TuneProfileCookie(IProfileable profileable) {
            this.profileable = profileable;
        }

        private void perform() {
            ProfilePanel panel = new ProfilePanel();
            panel.open(profileable);
            ModalDisplayer displayer = new ModalDisplayer(panel, "Profiling Settings for " + profileable.getAdsDefinition().getQualifiedName());
            displayer.getDialogDescriptor().setValid(panel.isOk());
            if (displayer.showModal()) {
                panel.apply();
            }
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            TuneProfileCookie c = activatedNodes[0].getCookie(TuneProfileCookie.class);
            if (c != null) {
                AdsDefinition def = c.profileable.getAdsDefinition();
                return def != null && def.isInBranch() && !def.isReadOnly();
            }
        }
        return false;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{TuneProfileCookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        if (nodes.length == 1) {
            TuneProfileCookie c = nodes[0].getCookie(TuneProfileCookie.class);
            if (c != null) {
                c.perform();
            }
        }
    }

    @Override
    public String getName() {
        return "Profiling Settings...";
    }
}
