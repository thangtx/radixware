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
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeChildren;

/**
 * Node of designer tree for {@linkplain DdsPackageDef PL/QL packages} of {@linkplain DdsModelDef}.
 */
public class DdsModelDdsPackagesNode extends RadixObjectsNode {

    protected DdsModelDdsPackagesNode(DdsDefinitions<DdsPackageDef> packages) {
        super(packages, new RadixObjectsNodeChildren(packages));
    }

    private static class DdsPackageCreature extends NamedRadixObjectCreature<DdsPackageDef> {

        public DdsPackageCreature(DdsDefinitions<DdsPackageDef> container) {
            super(container, DdsPackageDef.PACKAGE_TYPE_TITLE);
        }

        @Override
        public DdsPackageDef createInstance(final String name) {
            return DdsPackageDef.Factory.newInstance(name);
        }
        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.PACKAGE;
        }
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        DdsDefinitions<DdsPackageDef> packages = getPackages();
        NamedRadixObjectCreature creature = new DdsPackageCreature(packages);
        return new DdsCreationSupport(creature);
    }

    public DdsDefinitions<DdsPackageDef> getPackages() {
        return (DdsDefinitions<DdsPackageDef>) getRadixObject();
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsDefinitions<DdsPackageDef>> {

        @Override // Registered in layer.xml
        public Node newInstance(DdsDefinitions<DdsPackageDef> enums) {
            return new DdsModelDdsPackagesNode(enums);
        }
    }
}

