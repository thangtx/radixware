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
package org.radixware.kernel.designer.environment.nodes;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.BuildCookie;
import org.radixware.kernel.designer.ads.build.actions.BuildAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAndBuildAction;
import org.radixware.kernel.designer.ads.localization.OpenMlsEditorAction;
import org.radixware.kernel.designer.ads.localization.OpenMlsEditorAction.OpenMlEditorCookie;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.tree.actions.DictionaryEditAction;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.actions.RadixdocAction;

public class LayerNode extends RadixObjectNode {

    private static final class SegmentList extends ChildFactory<Object> {

        private Layer layer;

        private SegmentList(Layer layer) {
            this.layer = layer;
        }

        @Override
        protected boolean createKeys(final List<Object> itemsKeys) {
            itemsKeys.add(new Object());
            return true;
        }

        @Override
        protected Node[] createNodesForKey(final Object key) {
            final Node ddsNode = NodesManager.findOrCreateNode(layer.getDds());
            final Node adsNode = NodesManager.findOrCreateNode(layer.getAds());
            final Node udsNode = NodesManager.findOrCreateNode(layer.getUds());
            final Node[] result = new Node[]{ddsNode, adsNode, udsNode};
            return result;
        }
    }

    protected LayerNode(final Layer layer) {
        super(layer, layer.isLocalizing() ? Children.LEAF : Children.create(new SegmentList(layer), true));
        addCookie(new BuildCookie(layer));
        addCookie(new OpenMlEditorCookie(layer));
        addCookie(new DictionaryEditAction.EditCookie(layer));
        addCookie(new RadixdocAction.RadixdocCookie(null, null, layer, null));
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<Layer> {

        @Override
        public RadixObjectNode newInstance(final Layer layer) {
            return new LayerNode(layer);
        }
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        actions.add(SystemAction.get(OpenMlsEditorAction.class));

        super.addCustomActions(actions);
        actions.add(SystemAction.get(BuildAction.class));
        actions.add(SystemAction.get(CleanAndBuildAction.class));
        actions.add(SystemAction.get(CleanAction.class));
        actions.add(SystemAction.get(RadixdocAction.class));

        actions.add(null);

        actions.add(SystemAction.get(DictionaryEditAction.class));
    }
}
