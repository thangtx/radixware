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
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsPropEditorModelClassDef;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


class AdsCustomPropEditorNode extends AdsCustomViewNode<AdsCustomPropEditorDef> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsCustomPropEditorDef> {

        @Override
        public Node newInstance(AdsCustomPropEditorDef object) {
            return new AdsCustomPropEditorNode(object);
        }
    }

    private static class Model extends Children.Keys<AdsPropEditorModelClassDef> {

        private Model(AdsCustomPropEditorDef definition) {
            super();
            this.setKeys(new AdsPropEditorModelClassDef[]{definition.getModelClass()});
        }

        @Override
        protected Node[] createNodes(AdsPropEditorModelClassDef key) {
            return new Node[]{NodesManager.findOrCreateNode(key)};
        }
    }

    private AdsCustomPropEditorNode(AdsCustomPropEditorDef definition) {
        super(definition, new Model(definition));
    }
}
