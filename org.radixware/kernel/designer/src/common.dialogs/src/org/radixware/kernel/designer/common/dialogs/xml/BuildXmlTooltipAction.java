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


package org.radixware.kernel.designer.common.dialogs.xml;

import java.awt.Point;
import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.api.editor.EditorActionRegistrations;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.editor.ext.ExtKit;
import static org.netbeans.editor.ext.ExtKit.buildToolTipAction;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;


@EditorActionRegistrations(value = {
    @EditorActionRegistration(name = buildToolTipAction, shortDescription = buildToolTipAction, mimeType = "text/xml")
})
public class BuildXmlTooltipAction extends ExtKit.BuildToolTipAction {

    @Override
    protected String buildText(JTextComponent target) {
        if (target instanceof JEditorPane) {
            final Point p = target.getMousePosition();
            if (p != null) {
                int offs = target.viewToModel(p);
                //special tooltip when ctrl is pressed
                String tooltip = null;
                String mimeType = ((JEditorPane) target).getContentType();
                //Get the TagHyperlinkProvider instance
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
