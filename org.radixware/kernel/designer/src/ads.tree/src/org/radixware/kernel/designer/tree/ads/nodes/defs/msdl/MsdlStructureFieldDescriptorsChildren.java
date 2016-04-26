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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptorsList;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeAbstractChildren;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeChildren;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;


public class MsdlStructureFieldDescriptorsChildren extends RadixObjectsNodeAbstractChildren<MsdlStructureField> {

    private StructureFieldModel model = null;

    @Override
    protected List<MsdlStructureField> getOrderedList() {        
        List<MsdlStructureField> ret = new ArrayList<>();
        for (MsdlFieldDescriptor d : model.getFieldDescriptorList()) {
            MsdlStructureField toAdd = (MsdlStructureField) d.getMsdlField();
            ret.add(toAdd);
        }
        return ret;
    }

    public MsdlStructureFieldDescriptorsChildren(MsdlStructureFields l) {
        super(l);
        model = (StructureFieldModel)l.getContainer();       
    }
    
    public MsdlStructureFieldDescriptorsChildren(StructureFieldModel sfm) {
        super(sfm.getFields());
        model = sfm;
    }

    @Override
    protected Node createNodeForObject(RadixObject obj) {
     if (obj instanceof MsdlStructureField) {
            return new MsdlFieldNode((MsdlStructureField) obj);
        } else {
            //return NodesManager.findOrCreateNode(obj);
            return null;
        }
    }
}
