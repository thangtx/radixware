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
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;


public class AdsEnumClassNode extends AdsDynamicClassNode {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsEnumClassDef> {

        @Override
        public Node newInstance(AdsEnumClassDef field) {
            return new AdsEnumClassNode(field);
        }
    }

    public AdsEnumClassNode(AdsEnumClassDef definition) {
        super(definition);
    }

    @Override
    protected Class[] getChildrenProviders() {
        return MixedNodeChildrenAdapter.enumClassProviders();
    }
}
