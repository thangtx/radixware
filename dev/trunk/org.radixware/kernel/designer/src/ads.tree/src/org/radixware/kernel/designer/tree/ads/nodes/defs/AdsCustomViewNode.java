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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.NodeUpdateSupport;


public abstract class AdsCustomViewNode<T extends AdsAbstractUIDef> extends AdsObjectNode<T> {

    protected AdsCustomViewNode(T definition) {
        this(definition, Children.LEAF);
    }
    private final NodeUpdateSupport.NodeUpdateListener listener = new NodeUpdateSupport.NodeUpdateListener() {
        @Override
        public void onEvent(NodeUpdateSupport.NodeUpdateEvent e) {
            update();
        }
    };

    protected AdsCustomViewNode(T definition, Children children) {
        super(definition, children);
        definition.getNodeUpdateSupport().addEventListener(listener);
        update();
    }

    private void update() {
//        RadixObject obj = getRadixObject();
//        PaletteController palette = getLookup().lookup(PaletteController.class);
//        if (obj.isReadOnly()) {
//            if (palette != null) {
//                getLookupContent().remove(palette);
//            }
//        } else {
//            if (palette == null) {
//                palette = Palette.createPalette(obj);
//                if (palette != null) {
//                    getLookupContent().add(palette);
//                }
//            }
//            Palette.updatePalette(obj);
//        }
        setSheet(createSheet());
    }

    @Override
    protected NavigatorLookupHint getNavigatorLookupHint() {
        return FormNavigatorLookupHint.getInstance();
    }
}
