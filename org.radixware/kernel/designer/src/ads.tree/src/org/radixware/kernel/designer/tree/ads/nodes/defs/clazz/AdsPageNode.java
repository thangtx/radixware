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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz;

import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPage;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.NodeUpdateSupport.NodeUpdateListener;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;


@SuppressWarnings({"rawtypes"})
public class AdsPageNode extends AdsMixedNode implements NodeUpdateListener { // RadixObjectNode - because not required save, rename, etc.

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsPage> {

        @Override
        public Node newInstance(AdsPage object) {
            return new AdsPageNode(object);
        }
    }

    @SuppressWarnings("unchecked")
    public AdsPageNode(AdsPage page) {
        super(page);
        page.getNodeUpdateSupport().addEventListener(this);
        update();
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Class[] getChildrenProviders() {
        return new Class[]{ClassPageProvider.class};
    }

    @Override
    protected final Sheet createSheet() {
        return Sheet.createDefault();
    }

    final void update() {
//        AdsPage obj = (AdsPage)getRadixObject();
//        if (obj.getContainer() != null) {
//            PaletteController palette = getLookup().lookup(PaletteController.class);
//            if (obj.isReadOnly()) {
//                if (palette != null)
//                    getLookupContent().remove(palette);
//            } else {
//                if (palette == null)
//                    getLookupContent().add(Palette.PALETTE);
//                Palette.updatePalette(obj);
//            }
//        }
        setSheet(createSheet());
    }

    @Override
    public void onEvent(NodeUpdateEvent e) {
        update();
    }
}
