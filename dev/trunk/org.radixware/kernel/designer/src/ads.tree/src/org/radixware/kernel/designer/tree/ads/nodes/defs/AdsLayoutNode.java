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
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.IContextNodeFactory;

public class AdsLayoutNode extends AdsFormObjectNode<AdsLayout> {

    protected AdsLayoutNode(AdsAbstractUIDef rootDef, AdsLayout layout) {
        super(rootDef, layout);
        layout.getNodeUpdateSupport().addEventListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return new Class[]{FormLayoutProvider.class};
    }

    @NodeFactoryRegistration
    public static final class Factory implements IContextNodeFactory<AdsLayout> {

        @Override
        public Node newInstance(AdsLayout layout) {
            return new AdsLayoutNode(null, layout);
        }
        @Override
        public Node newInstance(RadixObject context,AdsLayout layout) {
            return new AdsLayoutNode(context instanceof AdsAbstractUIDef ? (AdsAbstractUIDef) context : null, layout);
        }
    }

}
