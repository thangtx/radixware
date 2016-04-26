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

import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsObjectNode;


public class AdsSortingNode extends AdsObjectNode<AdsSortingDef> {

    public static class Factory implements INodeFactory<AdsSortingDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(AdsSortingDef ddsPlSqlObject) {
            return new AdsSortingNode(ddsPlSqlObject);
        }
    }

    public AdsSortingNode(AdsSortingDef definition) {
        super(definition, Children.LEAF);
    }
}
