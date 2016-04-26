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

import org.openide.nodes.Node;
import org.openide.nodes.Children;

import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsPropEditorModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomWidgetDef;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


class AdsRwtCustomWidgetNode extends AdsCustomViewNode<AdsRwtCustomWidgetDef> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsRwtCustomWidgetDef> {

        @Override
        public Node newInstance(AdsRwtCustomWidgetDef object) {
            return new AdsRwtCustomWidgetNode(object);
        }
    }

    private static class Model extends Children.Keys<AdsCustomWidgetModelClassDef> {

        private Model(AdsRwtCustomWidgetDef definition) {
            super();
            this.setKeys(new AdsCustomWidgetModelClassDef[]{definition.getModelClass()});
        }

        @Override
        protected Node[] createNodes(AdsCustomWidgetModelClassDef key) {
            return new Node[]{NodesManager.findOrCreateNode(key)};
        }
    }

    private AdsRwtCustomWidgetNode(AdsRwtCustomWidgetDef definition) {
        super(definition, new Model(definition));
    }
}
