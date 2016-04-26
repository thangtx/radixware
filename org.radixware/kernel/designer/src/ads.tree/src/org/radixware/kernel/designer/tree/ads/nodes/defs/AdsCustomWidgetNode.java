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
import org.netbeans.spi.palette.PaletteController;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.defs.ads.ui.NodeUpdateSupport.NodeUpdateListener;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Palette;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


class AdsCustomWidgetNode extends AdsObjectNode<AdsCustomWidgetDef> implements NodeUpdateListener {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsCustomWidgetDef> {

        @Override
        public Node newInstance(AdsCustomWidgetDef object) {
            return new AdsCustomWidgetNode(object);
        }
    }

    private static class Model extends Children.Keys<AdsDialogModelClassDef> {

        private Model(AdsCustomWidgetDef definition) {
            super();
            this.setKeys(new AdsDialogModelClassDef[]{definition.getModelClass()});
        }

        @Override
        protected Node[] createNodes(AdsDialogModelClassDef key) {
            return new Node[]{NodesManager.findOrCreateNode(key)};
        }
    }

    private AdsCustomWidgetNode(AdsCustomWidgetDef definition) {
        super(definition, new Model(definition));
        definition.getNodeUpdateSupport().addEventListener(this);
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
//                getLookupContent().add(Palette.DEFAULT_EXPLORER_PALETTE);
//            }
//            Palette.updatePalette(obj);
//        }
        setSheet(createSheet());
    }

    @Override
    public void onEvent(NodeUpdateEvent e) {
        update();
    }

    @Override
    protected NavigatorLookupHint getNavigatorLookupHint() {
        return FormNavigatorLookupHint.getInstance();
    }
}
