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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation;

import org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation.FilterParamsProvider;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


public class AdsFilterNode extends AdsMixedNode<AdsFilterDef> {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return new Class[]{FilterParamsProvider.class, CollectionNode.Provider.class,
            ModelClassProvider.class,
            CustomViewProvider.class,};
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsFilterDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(AdsFilterDef ddsPlSqlObject) {
            return new AdsFilterNode(ddsPlSqlObject);
        }
    }

    public AdsFilterNode(AdsFilterDef definition) {
        super(definition);
    }
}
