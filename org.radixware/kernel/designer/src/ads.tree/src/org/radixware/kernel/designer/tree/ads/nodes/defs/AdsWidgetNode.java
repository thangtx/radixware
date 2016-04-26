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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.IContextNodeFactory;

public class AdsWidgetNode extends AdsFormObjectNode<AdsWidgetDef> {

    protected AdsWidgetNode(AdsAbstractUIDef rootDef, AdsWidgetDef widget) {
        super(rootDef, widget);
        widget.getNodeUpdateSupport().addEventListener(this);
    }

    @Override
    protected void update() {
        super.update();
        // update listeners, children
        if (children != null && children.getProviders() != null) {
            for (MixedNodeChildrenProvider provider : children.getProviders()) {
                if (provider instanceof FormWidgetProvider) {
                    ((FormWidgetProvider) provider).refresh();
                }
            }
        }
    }

   

    @SuppressWarnings({"rawtypes"})
    @Override
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return new Class[]{FormWidgetProvider.class};
    }

    @NodeFactoryRegistration
    public static final class Factory implements IContextNodeFactory<AdsWidgetDef> {

        @Override
        public Node newInstance(AdsWidgetDef widget) {
            return new AdsWidgetNode(null, widget);
        }

        @Override
        public Node newInstance(RadixObject context, AdsWidgetDef widget) {
            return new AdsWidgetNode(context instanceof AdsAbstractUIDef ? (AdsAbstractUIDef) context : null, widget);
        }
    }
}
