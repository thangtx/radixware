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

import java.io.IOException;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class AdsDefinitionReloadAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final AdsDefinition definition;

        public Cookie(AdsDefinition definition) {
            this.definition = definition;
        }

        public void reloadAdsDefifnition() {

            String message = "Reload " + definition.getTypeTitle().toLowerCase() + " '" + definition.getQualifiedName() + "' ?";
            if (definition.getEditState() != EEditState.NONE) {
                message += "\nALL UNSAVED CHANGES WILL BE LOST!";
            }
            if (!DialogUtils.messageConfirmation(message)) {
                return;
            }

            RadixMutex.writeAccess(new Runnable() {

                @Override
                public void run() {
                    try {
                        final AdsModule module = definition.getModule();
                        if (module != null) {
                            final RadixObject def = module.getTopContainer().reload(definition);

                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    NodesManager.selectInProjects(def);
                                }
                            });
                        }
                    } catch (IOException cause) {
                        DefinitionError error = new DefinitionError("Unable to reload ADS definition.", definition, cause);
                        DialogUtils.messageError(error);
                    }
                }
            });
        }
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
        for (Node node : nodes) {
            final Cookie cookie = node.getCookie(AdsDefinitionReloadAction.Cookie.class);
            if (cookie != null) {
                cookie.reloadAdsDefifnition();
            }
        }
    }

    @Override
    public String getName() {
        return "Reload"; // TODO: translate
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true; // to prevent deadlock, because tree can be recreated
    }
}
