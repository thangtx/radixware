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

package org.radixware.kernel.designer.common.dialogs.sqlscript;

import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;


public class DefaultSQLScriptProviderFactory implements ISQLScriptProvider.Factory {

    @Override
    public ISQLScriptProvider create(Lookup context) {
        JTextComponent textComponent = context.lookup(JTextComponent.class);

        if (textComponent == null) {
            DataObject dataObject = context.lookup(DataObject.class);
            final EditorCookie editorCookie = dataObject.getLookup().lookup(EditorCookie.class);
            if (editorCookie != null) {
                JEditorPane[] panes = editorCookie.getOpenedPanes();
                if (panes != null && panes.length > 0) {
                    textComponent = panes[0];
                }
            }
        }

        if (textComponent == null) {
            throw new NullPointerException("No textComponent in context");
        }
        return new JTextComponentScriptProvider(textComponent);
    }
}
