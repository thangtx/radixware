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
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeChildren;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;

/**
 * Node of designer tree for {@linkplain DdsTypeDef PL/SQL types} of
 * {@linkplain DdsModelDef}.
 */
public class DdsModelDdsTypesNode extends RadixObjectsNode {

    public DdsModelDdsTypesNode(DdsDefinitions<DdsTypeDef> types) {
        super(types, new RadixObjectsNodeChildren(types));
//        super(types, new RadixObjectsNodeSortedChildren(types));
    }

    private static class DdsTypeCreature extends NamedRadixObjectCreature<DdsTypeDef> {

        public DdsTypeCreature(DdsDefinitions<DdsTypeDef> container) {
            super(container, DdsTypeDef.TYPE_TYPE_TITLE);
        }

        @Override
        public DdsTypeDef createInstance(final String name) {
            final DdsTypeDef typeDef = DdsTypeDef.Factory.newInstance(name);
            return typeDef;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.TYPE;
        }
    }

    public DdsDefinitions<DdsTypeDef> getTypes() {
        return (DdsDefinitions<DdsTypeDef>) getRadixObject();
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        DdsDefinitions<DdsTypeDef> types = getTypes();
        NamedRadixObjectCreature creature = new DdsTypeCreature(types);
        return new DdsCreationSupport(creature);
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsDefinitions<DdsTypeDef>> {

        @Override // Registered in layer.xml
        public Node newInstance(DdsDefinitions<DdsTypeDef> types) {
            return new DdsModelDdsTypesNode(types);
        }
    }
}
