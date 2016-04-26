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

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


public class AdsSelectorPresentationNode extends AdsMixedNode<AdsSelectorPresentationDef> {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return new Class[]{
            ModelClassProvider.class,
            CustomViewProvider.class
        };
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsSelectorPresentationDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(AdsSelectorPresentationDef ddsPlSqlObject) {
            return new AdsSelectorPresentationNode(ddsPlSqlObject);
        }
    }

    public AdsSelectorPresentationNode(AdsSelectorPresentationDef definition) {
        super(definition);
    }
}
