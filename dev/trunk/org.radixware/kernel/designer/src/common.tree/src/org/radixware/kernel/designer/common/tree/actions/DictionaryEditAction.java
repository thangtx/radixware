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

package org.radixware.kernel.designer.common.tree.actions;

import org.openide.actions.EditAction;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.utils.spellchecker.DictionariesSuite;


public class DictionaryEditAction extends EditAction {

    private final static String ACTION_NAME;

    static {
        ACTION_NAME = NbBundle.getMessage(DictionaryEditAction.class, "LayerDictionary-Name");
    }

    public static final class EditCookie extends RadixObjectEditCookie {

        private static RadixObject getDictionariesContainer(Layer layer) {
            
            DictionariesSuite container = new DictionariesSuite(layer);
            
            return container;
        }

        public EditCookie(Layer layer) {
            super(getDictionariesContainer(layer));
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class<?>[]{ DictionaryEditAction.EditCookie.class };
    }

    @Override
    public String getName() {
        return ACTION_NAME;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node node : activatedNodes) {
            EditCookie es = node.getCookie(EditCookie.class);

            if (es != null) {
                es.edit();
            }
        }
    }
}
