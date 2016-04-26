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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import javax.swing.JEditorPane;
import javax.swing.text.Document;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory.Context;
import org.netbeans.spi.editor.highlighting.ZOrder;

/**
 * Factory for producing Highlighters for tags
 */
public class TagHighlighterFactory implements HighlightsLayerFactory {

    public static TagHighlighter getTagHighlighter(Document doc, String mimeType) {
        TagHighlighter highlighter = (TagHighlighter) doc.getProperty(TagHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(TagHighlighter.class, highlighter = new TagHighlighter(doc, mimeType));
        }
        return highlighter;
    }

    public static TagCompletionHighlighter getTagCompletionHighlighter(Document doc, String mimeType) {
        TagCompletionHighlighter highlighter = (TagCompletionHighlighter) doc.getProperty(TagCompletionHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(TagCompletionHighlighter.class, highlighter = new TagCompletionHighlighter(doc, mimeType));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        JEditorPane tc = (JEditorPane) context.getComponent();
        String mimeType = tc.getEditorKit().getContentType();
        return new HighlightsLayer[]{
                    HighlightsLayer.create(
                    TagHighlighter.class.getName(),
                    ZOrder.SYNTAX_RACK.forPosition(200),
                    false,
                    getTagHighlighter(context.getDocument(), mimeType).getHighlightsBag()),
                    HighlightsLayer.create(
                    TagCompletionHighlighter.class.getName(),
                    ZOrder.SHOW_OFF_RACK.forPosition(200),
                    false,
                    getTagCompletionHighlighter(context.getDocument(), mimeType).getHighlightsBag())
                };
    }
}
