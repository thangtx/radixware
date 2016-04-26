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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsRPCMethodDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixPublishedException;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public class UpdateCommandHandlerProfileAction extends AdsDefinitionAction {

    public static class UpdateCommandHandlerProfileCookie implements Node.Cookie {

        AdsMethodDef method;

        public UpdateCommandHandlerProfileCookie(AdsMethodDef method) {
            this.method = method;
        }

        private void update() {
            try {
                if (method instanceof AdsCommandHandlerMethodDef) {
                    ((AdsCommandHandlerMethodDef) method).updateProfile();
                } else if (method instanceof AdsRPCMethodDef) {
                    ((AdsRPCMethodDef) method).updateComponents();
                }
            } catch (RadixPublishedException ex) {
                DialogUtils.messageError(ex);
            } catch (RadixError ex) {
                DialogUtils.messageError(ex);
            }
        }

        private String getActionName() {
            if (method instanceof AdsCommandHandlerMethodDef) {
                return "Sync profile with command";
            } else if (method instanceof AdsRPCMethodDef) {
                return "Update remote call components";
            } else {
                return "Update command handler method";
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{UpdateCommandHandlerProfileCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        UpdateCommandHandlerProfileCookie c = activatedNodes[0].getCookie(UpdateCommandHandlerProfileCookie.class);
        if (c != null) {
            c.update();
        }
    }

    @Override
    public String getName() {
        Node[] nodes = getActivatedNodes();
        if (nodes.length == 1) {
            UpdateCommandHandlerProfileCookie c = nodes[0].getCookie(UpdateCommandHandlerProfileCookie.class);
            if (c != null) {
                return c.getActionName();
            }
        }

        return "Sync Profile With Command";
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return super.enable(activatedNodes);
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        if (nodes.length == 1) {
            UpdateCommandHandlerProfileCookie c = nodes[0].getCookie(UpdateCommandHandlerProfileCookie.class);
            if (c != null) {
                return !c.method.isReadOnly();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
