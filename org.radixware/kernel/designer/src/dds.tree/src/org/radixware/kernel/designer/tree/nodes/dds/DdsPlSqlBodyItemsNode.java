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

package org.radixware.kernel.designer.tree.nodes.dds;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.tree.nodes.dds.DdsPlSqlHeaderItemsNode.DdsCustomTextCreature;
import org.radixware.kernel.designer.tree.nodes.dds.DdsPlSqlHeaderItemsNode.DdsFunctionCreature;
import org.radixware.kernel.designer.tree.nodes.dds.DdsPlSqlHeaderItemsNode.DdsPrototypeCreature;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeChildren;


public class DdsPlSqlBodyItemsNode extends RadixObjectsNode {

    protected DdsPlSqlBodyItemsNode(DdsDefinitions<DdsPlSqlObjectItemDef> items) {
        super(items, new RadixObjectsNodeChildren(items));
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        final DdsDefinitions<DdsPlSqlObjectItemDef> bodyItems = getBodyItems();
        final Creature prototypeCreature = new DdsPrototypeCreature(bodyItems);
        final boolean isPublic = false;
        final Creature functionCreature = new DdsFunctionCreature(bodyItems, isPublic);
        final Creature customTextCreature = new DdsCustomTextCreature(bodyItems);
        return new DdsCreationSupport(functionCreature, prototypeCreature, customTextCreature);
    }

    public DdsDefinitions<DdsPlSqlObjectItemDef> getBodyItems() {
        return (DdsDefinitions<DdsPlSqlObjectItemDef>) getRadixObject();
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsDefinitions<DdsPlSqlObjectItemDef>> {

        @Override // Registered in layer.xml
        public Node newInstance(DdsDefinitions<DdsPlSqlObjectItemDef> bodyItems) {
            return new DdsPlSqlBodyItemsNode(bodyItems);
        }
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return new RadixObjectEditCookie(getBodyItems().getContainer());
    }
}
