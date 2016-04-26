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

import java.awt.Point;
import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.ext.ExtKit;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.netbeans.modules.editor.NbEditorDocument;
import org.netbeans.modules.editor.NbEditorKit;
import org.netbeans.api.editor.EditorActionRegistrations;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;

public class SQLScriptEditorKit  extends NbEditorKit {

    @Override
    public String getContentType() {
        return "text/x-sql";
    }

    @Override
    public Document createDefaultDocument() {
        return new NbEditorDocument("text/x-sql");
    }

    @EditorActionRegistrations(value = {
    @EditorActionRegistration(name = buildToolTipAction, shortDescription = buildToolTipAction, mimeType = "text/x-sql")
    })
    public static class BuildSQLTooltipAction extends ExtKit.BuildToolTipAction {
        
        @Override
        protected String buildText(JTextComponent target) {
            if (target instanceof JEditorPane) {
                final Point p = target.getMousePosition();
                
                if (p != null) {
                    int offs = target.viewToModel(p);
                    String tooltip = null;
                    String mimeType = ((JEditorPane) target).getContentType();
                    MimePath mimePath = MimePath.parse(mimeType);
                    HyperlinkProviderExt provider = MimeLookup.getLookup(mimePath).lookup(HyperlinkProviderExt.class);
                    if (provider != null) {
                        tooltip = provider.getTooltipText(target.getDocument(), offs, HyperlinkType.GO_TO_DECLARATION);
                    }
                    if (tooltip != null && tooltip.length() > 0) {
                        return tooltip;
                    }
                }
            }
            return super.buildText(target);
        }
    }

}
