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
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.rights.SystemPresentationBuilder;


public class GenerateDrcInfoAction extends AdsDefinitionAction {

    public static class GenerateDrcInfoCookie implements Node.Cookie {

        private AdsEntityClassDef aec;

        public GenerateDrcInfoCookie(AdsEntityClassDef aec) {
            this.aec = aec;

        }

        private final void generate() {
            SystemPresentationBuilder.createSystemPresentation(aec);
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length > 0) {
            GenerateDrcInfoCookie c = activatedNodes[0].getCookie(GenerateDrcInfoCookie.class);
            if (c != null) {
                return !c.aec.isReadOnly();
            } else {
                return false;
            }
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
        return new Class<?>[]{GenerateDrcInfoCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        GenerateDrcInfoCookie c = activatedNodes[0].getCookie(GenerateDrcInfoCookie.class);
        if (c != null) {
            c.generate();
        }
    }

    @Override
    public String getName() {
        return "Update Rights System Presentations";
    }
}
