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

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.designer.ads.editors.clazz.creation.ColumnBasedPropertiesPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class CreateColumnBasedPropsAction extends AdsDefinitionAction {

    public static class CreateColumnBasedPropsCookie implements Node.Cookie {

        AdsEntityObjectClassDef clazz;

        public CreateColumnBasedPropsCookie(AdsEntityObjectClassDef clazz) {
            this.clazz = clazz;
        }

        private void create() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ColumnBasedPropertiesPanel panel = new ColumnBasedPropertiesPanel();
                    panel.open(clazz);
                    ModalDisplayer displayer = new ModalDisplayer(panel);
                    displayer.setTitle(NbBundle.getMessage(CreateColumnBasedPropsAction.class, "CreateColumnBasedPropsDialogTitle"));
                    if (displayer.showModal()) {
                        panel.apply();
                    }
                }
            });
        }
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        if (nodes.length == 1) {
            CreateColumnBasedPropsCookie cookie = nodes[0].getCookie(CreateColumnBasedPropsCookie.class);
            if (cookie != null) {
                return !cookie.clazz.isReadOnly();
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CreateColumnBasedPropsAction.class, "CreateColumnBasedPropsTip");
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected Class<?>[] cookieClasses() {
        return new Class[]{
            CreateColumnBasedPropsCookie.class
        };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        CreateColumnBasedPropsCookie cookie = activatedNodes[0].getCookie(CreateColumnBasedPropsCookie.class);
        if (cookie != null) {
            cookie.create();
        }
    }
}
