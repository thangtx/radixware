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

import org.netbeans.spi.palette.PaletteController;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject.Prop;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.NodeUpdateSupport.NodeUpdateListener;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.Palette;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.props.AppPropertySupport;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;


public abstract class AdsAlgoObjectNode extends RadixObjectNode implements NodeUpdateListener { // RadixObjectNode - because not required save, rename, etc.

    public AdsAlgoObjectNode(AdsBaseObject obj) {
        super(obj, Children.LEAF);
        obj.getNodeUpdateSupport().addEventListener(this);
        update();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        RadixObject node = getRadixObject();

        if (!(node instanceof AdsAppObject)) {
            return sheet;
        }

        AdsAppObject obj = (AdsAppObject) node;
        Sheet.Set set = Sheet.createPropertiesSet();

        for (Prop prop : obj.getProps()) {
            try {
                if (prop.getSourceId() == null) {
                    set.put(new AppPropertySupport(prop));
                }
            } catch (Exception ex) {
                AppPropertySupport sup = new AppPropertySupport(prop);
            }
        }

        sheet.put(set);
        return sheet;
    }

    private void update() {
//        AdsBaseObject obj = (AdsBaseObject)getRadixObject();
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
//        setSheet(createSheet());
    }

    @Override
    public void onEvent(NodeUpdateEvent e) {
        update();
    }
}
