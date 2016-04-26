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

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.IContextNodeFactory;

public class AdsRwtWidgetNode extends AdsFormObjectNode<AdsRwtWidgetDef> {

    protected AdsRwtWidgetNode(AdsAbstractUIDef rootDef, AdsRwtWidgetDef widget) {
        super(rootDef, widget);
        widget.getNodeUpdateSupport().addEventListener(this);
    }

    @Override
    protected void update() {
        super.update();

        final MixedNodeChildrenAdapter adapter = children;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (adapter != null && adapter.getProviders() != null) {
                    for (MixedNodeChildrenProvider provider : adapter.getProviders()) {
                        if (provider instanceof FormWidgetProvider) {
                            ((FormWidgetProvider) provider).refresh();
                        }
                    }
                }
            }
        });
        // update listeners, children

    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return new Class[]{FormWidgetProvider.class};
    }

    @NodeFactoryRegistration
    public static final class Factory implements IContextNodeFactory<AdsRwtWidgetDef> {

        @Override
        public Node newInstance(AdsRwtWidgetDef widget) {
            return new AdsRwtWidgetNode(null, widget);
        }

        @Override
        public Node newInstance(RadixObject context, AdsRwtWidgetDef widget) {
            return new AdsRwtWidgetNode(context instanceof AdsAbstractUIDef ? (AdsAbstractUIDef) context : null, widget);
        }
    }
}
