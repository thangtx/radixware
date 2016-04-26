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
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.designer.ads.editors.clazz.transparent.PublishedClassSynchronizer;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public class SynchronizePublishedClassAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        private final AdsClassDef publishedClass;

        public Cookie(AdsClassDef publishedClass){
            this.publishedClass = publishedClass;
        }

        void synchronize(){
            PublishedClassSynchronizer.synchronize(publishedClass);
        }

        public AdsClassDef getPublishedClass() {
            return publishedClass;
        }
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        if (nodes.length == 1) {
            Cookie c = nodes[0].getCookie(Cookie.class);
            if (c != null) {
                return !c.getPublishedClass().isReadOnly();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        Cookie c = activatedNodes[0].getCookie(Cookie.class);

        if (c != null) {
            c.synchronize();
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(SynchronizePublishedClassAction.class, "SynchronizeActionTip");
    }


}
