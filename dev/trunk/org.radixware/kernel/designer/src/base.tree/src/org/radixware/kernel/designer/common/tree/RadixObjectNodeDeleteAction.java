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

package org.radixware.kernel.designer.common.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.RadixObjectsUtils;

/**
 * Allows to pass bug in Netbeans, node.canDestroy() calls after popup menu
 * popuped :-(
 *
 */
public class RadixObjectNodeDeleteAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final RadixObject radixObject;

        public Cookie(RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        public RadixObject getRadixObject() {
            return radixObject;
        }
    }

    public RadixObjectNodeDeleteAction() {
        setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        final List<RadixObject> selectedObjects = new ArrayList<RadixObject>();
        for (int i = 0; i < nodes.length; i++) {
            final Cookie cookie = nodes[i].getCookie(Cookie.class);
            if (cookie != null) {
                selectedObjects.add(cookie.radixObject);
            }
        }
        RadixObjectsUtils.removeDuplicates(selectedObjects);
        DialogUtils.deleteObjects(selectedObjects);
    }

    @Override
    public String getName() {
        Node[] nodes = getActivatedNodes();
        if (nodes != null && nodes.length == 1) {
            RadixObject obj = null;
            if (nodes[0] instanceof RadixObjectNode) {
                obj = ((RadixObjectNode) nodes[0]).getRadixObject();
            }
            if (obj == null) {
                Node node = nodes[0];
                if (node instanceof FilterNode) {
                    obj = node.getLookup().lookup(RadixObject.class);
                }
            }
            if (obj instanceof AdsExplorerItemDef) {
                AdsExplorerItemDef ei = (AdsExplorerItemDef) obj;
                if (ei.getHierarchy().findOverwritten().get() != null) {
                    return "Inherit";
                }
            }
        }
        return "Delete";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return super.enable(activatedNodes);
    }
}
