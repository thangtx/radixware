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

package org.radixware.kernel.designer.uds.tree.actions;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.JFileChooser;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.schemas.udsdef.UserFunctionDefinition;


public class ImportBodyAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private UdsUserFuncDef uf;

        public Cookie(UdsUserFuncDef uf) {
            this.uf = uf;
        }

        public void exec() {
            File file = ActionUtil.chooseFile(JFileChooser.OPEN_DIALOG, "Import User Function");
            if (file != null) {
                try {
                    uf.importBody(file, new UdsUserFuncDef.UICallback() {

                        @Override
                        public void showError(String message) {
                            DialogUtils.messageError(message);
                        }

                        @Override
                        public Id chooseId(Map<Id, UserFunctionDefinition> id2title) {
                            FuncChooser chooser = new FuncChooser(id2title);
                            ModalDisplayer displayer = new ModalDisplayer(chooser);
                            if (displayer.showModal()) {
                                return chooser.getSelectedId();
                            } else {
                                return null;
                            }
                        }
                    });
                } catch (IOException ex) {
                    DialogUtils.messageError(ex);
                }
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        Cookie c = nodes[0].getCookie(Cookie.class);
        if (c != null) {
            c.exec();
        }
    }

    @Override
    public String getName() {
        return "Import...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
