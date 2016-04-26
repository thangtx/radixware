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

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.common.msdl.MsdlVariantFields;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeChildren;

public class MsdlFieldNodeCustomChildren extends RadixObjectsNodeChildren<RadixObject> {

    public MsdlFieldNodeCustomChildren(RadixObjects<RadixObject> obj) {
        super(obj);
    }

    @Override
    protected Node createNodeForObject(RadixObject obj) {
        if (obj instanceof MsdlField) {
            return new MsdlFieldNode((MsdlField) obj);
        } else if (obj instanceof MsdlStructureFields) {
            return new MsdlStructureFieldsNode((MsdlStructureFields) obj);
        } else if (obj instanceof MsdlVariantFields) {
            return new MsdlVariantFieldsNode((MsdlVariantFields) obj);
        }
        Node node = NodesManager.findOrCreateNode(obj);
        if (node != null) {
            return new FilterNode(node);
        } else {
            return null;
        }
    }
}
