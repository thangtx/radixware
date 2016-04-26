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
import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;

/**
 * Node of designer tree for {@linkplain DdsColumnTempalteDef column templates} of {@linkplain DdsModelDef}.
 */
public class DdsModelDdsColumnTemplatesNode extends RadixObjectsNode {

    protected DdsModelDdsColumnTemplatesNode(DdsDefinitions<DdsColumnTemplateDef> columnTemplates) {
        super(columnTemplates, new RadixObjectsNodeSortedChildren(columnTemplates));
    }

    private static class DdsColumnTemplateCreature extends NamedRadixObjectCreature<DdsColumnTemplateDef> {

        public DdsColumnTemplateCreature(DdsDefinitions<DdsColumnTemplateDef> container) {
            super(container, DdsColumnTemplateDef.COLUMN_TEMPLATE_TYPE_TITLE);
        }

        @Override
        public DdsColumnTemplateDef createInstance(final String name) {
            return DdsColumnTemplateDef.Factory.newInstance(name);
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.COLUMN_TEMPLATE;
        }
    }

    public DdsDefinitions<DdsColumnTemplateDef> getColumnTemplates() {
        return (DdsDefinitions<DdsColumnTemplateDef>) getRadixObject();
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        DdsDefinitions<DdsColumnTemplateDef> columnTemplates = getColumnTemplates();
        NamedRadixObjectCreature creature = new DdsColumnTemplateCreature(columnTemplates);
        return new DdsCreationSupport(creature);
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsDefinitions<DdsColumnTemplateDef>> {

        @Override // Registered in layer.xml
        public Node newInstance(DdsDefinitions<DdsColumnTemplateDef> columnTemplates) {
            return new DdsModelDdsColumnTemplatesNode(columnTemplates);
        }
    }
}
