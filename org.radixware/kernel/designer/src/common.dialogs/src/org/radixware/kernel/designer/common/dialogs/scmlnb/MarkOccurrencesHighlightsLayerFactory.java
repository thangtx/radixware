/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.common.dialogs.scmlnb;

import javax.swing.text.Document;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;

/**
 *
 * @author akrylov
 */
public class MarkOccurrencesHighlightsLayerFactory implements HighlightsLayerFactory {

    public static MarkOccurrencesHighlighter getMarkOccurrencesHighlighter(Context context) {
        MarkOccurrencesHighlighter highlighter = (MarkOccurrencesHighlighter) context.getDocument().getProperty(MarkOccurrencesHighlighter.class);
        if (highlighter == null) {
            context.getDocument().putProperty(MarkOccurrencesHighlighter.class, highlighter = new MarkOccurrencesHighlighter(context));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        return new HighlightsLayer[]{
            HighlightsLayer.create(
            MarkOccurrencesHighlighter.class.getName(),
            ZOrder.CARET_RACK.forPosition(2000),
            true,
            getMarkOccurrencesHighlighter(context).getHighlightsBag())
        };
    }

}
