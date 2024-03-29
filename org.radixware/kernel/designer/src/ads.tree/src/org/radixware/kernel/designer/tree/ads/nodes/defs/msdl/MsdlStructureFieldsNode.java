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

package org.radixware.kernel.designer.tree.ads.nodes.defs.msdl;

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.designer.ads.editors.msdl.creation.fieldcreation.MsdlStructureFieldCreature;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;


public class MsdlStructureFieldsNode extends RadixObjectsNode {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<MsdlStructureFields> {

        @Override
        public Node newInstance(MsdlStructureFields object) {
            return new MsdlStructureFieldsNode(object);
        }
    }

    public MsdlStructureFieldsNode(MsdlStructureFields fields) {
        super(fields, new MsdlStructureFieldDescriptorsChildren(fields));
    }
      

    @Override
    protected CreationSupport createNewCreationSupport() {
        return new CreationSupport() {
            @Override
            public ICreatureGroup[] createCreatureGroups(RadixObject object) {
                return new ICreatureGroup[]{
                    new ICreatureGroup() {
                        @Override
                        public List<ICreature> getCreatures() {
                            ArrayList<ICreature> result = new ArrayList<ICreature>();
                            result.add(new MsdlStructureFieldCreature((MsdlStructureFields) getRadixObject()));

                            return result;
                        }

                        @Override
                        public String getDisplayName() {
                            return "Field";
                        }
                    }
                };
            }
        };
    }
}
