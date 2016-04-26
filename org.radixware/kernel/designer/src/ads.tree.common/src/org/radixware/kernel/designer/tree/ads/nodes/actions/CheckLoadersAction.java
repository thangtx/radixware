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
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public class CheckLoadersAction extends AdsDefinitionAction {

    @Override
    protected boolean calcEnabled(Node[] nodes) {

        if (nodes.length == 1) {
            CheckLoadersCookie c = nodes[0].getCookie(CheckLoadersCookie.class);
            if (c != null) {
                return !c.clazz.isReadOnly() && c.clazz.isCodeEditable();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static class CheckLoadersCookie implements Node.Cookie {

        AdsEntityObjectClassDef clazz;
        AdsMethodGroup group = null;

        public CheckLoadersCookie(AdsEntityObjectClassDef clazz, AdsMethodGroup group) {
            this.clazz = clazz;
            this.group = group;
        }

        private void check() {
            AdsMethodDef loader = clazz.getMethods().getLocal().findById(AdsSystemMethodDef.ID_LOAD_BY_PID_STR);
            if (loader == null) {
                loader = AdsSystemMethodDef.Factory.newLoadByPidStr();
                clazz.getMethods().getLocal().add(loader);
                if (group != null && group != clazz.getMethodGroup()) {
                    group.addMember(loader);
                }

            }
            if (clazz.isPublished()) {
                loader.setPublished(true);
            }
            ((AdsSystemMethodDef) loader).syncWithSystem();
            loader = clazz.getMethods().getLocal().findById(AdsSystemMethodDef.ID_LOAD_BY_PK);
            if (loader == null) {
                loader = AdsSystemMethodDef.Factory.newLoadByPK();
                clazz.getMethods().getLocal().add(loader);
                if (group != null && group != clazz.getMethodGroup()) {
                    group.addMember(loader);
                }

            }
            if (clazz.isPublished()) {
                loader.setPublished(true);
            }
            ((AdsSystemMethodDef) loader).syncWithSystem();

        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{
                    CheckLoadersCookie.class
                };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        CheckLoadersCookie c = activatedNodes[0].getCookie(CheckLoadersCookie.class);
        if (c != null) {
            c.check();
        }
    }

    @Override
    public String getName() {
        return "Create/Update Instance Loading Methods";
    }
}
