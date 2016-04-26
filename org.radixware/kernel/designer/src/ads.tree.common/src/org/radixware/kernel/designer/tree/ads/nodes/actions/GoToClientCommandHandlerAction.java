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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class GoToClientCommandHandlerAction extends AdsDefinitionAction {

    public static class Cookie extends GoToCommandHandlerCommonCookie {

        public Cookie(AdsCommandDef command) {
            super(command);
        }

        @Override
        protected void initProvider() {
            provider = new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsCommandHandlerMethodDef) {
                        if (((AdsDefinition) radixObject).getUsageEnvironment().isClientEnv()) {
                            return ((AdsCommandHandlerMethodDef) radixObject).getCommandId().equals(command.getId());
                        }
                    }
                    return false;
                }
            };
        }
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        Cookie c = activatedNodes[0].getCookie(Cookie.class);

        if (c != null) {
            c.goToHandler();
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            Cookie c = activatedNodes[0].getCookie(Cookie.class);
            if (c != null) {
                return !c.command.isReadOnly();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    public String getName() {
        return "Go to Client Side Command Handler...";
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
