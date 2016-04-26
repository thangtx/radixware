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
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.msdl.MsdlVariantFields;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;


public class MsdlVariantFieldDescriptorsChildren extends RadixObjectsNodeSortedChildren<MsdlVariantField> {

    private MsdlVariantFields fields;
    
    @Override
    protected List<MsdlVariantField> getOrderedList() {
        List<MsdlVariantField> ret = new ArrayList<>();
        ChoiceFieldModel cfm = (ChoiceFieldModel) fields.getContainer();
        for(MsdlFieldDescriptor d : cfm.getFieldDescriptorList()) {
            ret.add((MsdlVariantField)d.getMsdlField());
        }
        return ret;
    }
  

    public MsdlVariantFieldDescriptorsChildren(MsdlVariantFields l) {
        super(l);
        fields = l;
    }

    @Override
    protected Node createNodeForObject(RadixObject obj) {
        if (obj instanceof MsdlVariantField) {
            return new MsdlFieldNode((MsdlVariantField) obj);
        } else {
            return NodesManager.findOrCreateNode(obj);
        }
    }
}
