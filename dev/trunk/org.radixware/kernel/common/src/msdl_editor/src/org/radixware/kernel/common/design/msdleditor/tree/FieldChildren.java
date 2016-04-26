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

package org.radixware.kernel.common.design.msdleditor.tree;

import java.util.HashMap;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.msdl.fields.SequenceFieldModel;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;


public class FieldChildren extends Children<String> {
    private HashMap<String, DefaultMutableTreeNode> string2node = new HashMap<String, DefaultMutableTreeNode>();
    private MsdlField field;

    public FieldChildren(Tree tree, MsdlField field, DefaultMutableTreeNode parent) {
        super(tree,parent);
        this.field = field;
    }

    @Override
    protected void createKeys(List<String> itemsKeys) {
        switch (field.getType()) {
            case STRUCTURE:
                if (((StructureFieldModel)field.getFieldModel()).getStructure().isSetBitmap())
                    itemsKeys.add("HeaderFields");
                itemsKeys.add("Fields");
                break;
            case CHOICE:
                itemsKeys.add("Variant");
                break;
            case SEQUENCE:
                itemsKeys.add("Item");
                break;
        }
    }

    @Override
    protected DefaultMutableTreeNode createNodeForKey(String key) {
        DefaultMutableTreeNode node = string2node.get(key);
        switch (field.getType()) {
            case STRUCTURE:
                if (key.equals("HeaderFields"))
                    node = new HeaderFieldsNode(tree,((StructureFieldModel)field.getFieldModel()).getHeaderFields());
                if (key.equals("Fields"))
                    node = new FieldsNode(tree,((StructureFieldModel)field.getFieldModel()).getFields());
                break;
            case CHOICE:
                if (key.equals("Variant"))
                    node = new VariantsNode(tree,getParent(),((ChoiceFieldModel)field.getFieldModel()).getFields());
                break;
            case SEQUENCE:
                if (key.equals("Item"))
                    node = new SequenceItemNode(tree,((SequenceFieldModel)field.getFieldModel()).getItem());
                break;
        }
        string2node.put(key, node);
        return node;
    }

}
