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

package org.radixware.kernel.designer.common.dialogs.spellchecker;

import javax.swing.text.JTextComponent;

import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;

public class SpellcheckerHighlightLayerFactory implements HighlightsLayerFactory {

    public SpellcheckerHighlightLayerFactory() {
    }

    @Override
    public HighlightsLayer[] createLayers(Context ctx) {
        OffsetsBag bag = getBag(ctx.getComponent());
        return new HighlightsLayer[]{
                    HighlightsLayer.create(SpellcheckerHighlightLayerFactory.class.getName(), ZOrder.SYNTAX_RACK, false, bag),};
    }

    public static synchronized OffsetsBag getBag(JTextComponent component) {
        OffsetsBag bag = (OffsetsBag) component.getClientProperty(SpellcheckerHighlightLayerFactory.class);
        if (bag == null) {
            component.putClientProperty(SpellcheckerHighlightLayerFactory.class, bag = new OffsetsBag(component.getDocument()));
        }

        return bag;
    }
}
