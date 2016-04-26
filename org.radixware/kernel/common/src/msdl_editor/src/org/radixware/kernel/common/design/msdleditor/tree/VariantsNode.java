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
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.design.msdleditor.field.SortPanel;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.MsdlVariantFields;


public class VariantsNode extends ItemNode implements IFieldsOwner{

    private MsdlVariantFields fields;

    @Override
    public void delField(MsdlField field) {
        fields.remove((MsdlVariantField)field);
    }

    @Override
    public MsdlField addField() {
        MsdlVariantField f = fields.createChild();
        fields.add(f);
        return f;
    }

    private static final class FieldList extends Children<MsdlVariantField> implements ContainerChangesListener {
        private HashMap<MsdlVariantField, DefaultMutableTreeNode> field2node = new HashMap<MsdlVariantField, DefaultMutableTreeNode>();
        private MsdlVariantFields field;

        private FieldList(Tree tree, DefaultMutableTreeNode parent, MsdlVariantFields field) {
            super(tree,parent);
            this.field = field;
            field.getContainerChangesSupport().addEventListener(this);
        }

        @Override
        protected void createKeys(List<MsdlVariantField> itemsKeys) {
            for (MsdlVariantField f : field)
                itemsKeys.add(f);
        }

        @Override
        public DefaultMutableTreeNode createNodeForKey(MsdlVariantField key) {
            DefaultMutableTreeNode node = field2node.get(key);
            if (node == null) {
                node = new FieldVariantNode(tree,key);
                field2node.put(key, node);
            }
            return node;
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            refresh();
            structureChanged();
        }

    }

    public VariantsNode(Tree tree, DefaultMutableTreeNode parent, final MsdlVariantFields field) {
        setChildren(new FieldList(tree,this,field));
        fields = field;
    }

    @Override
    public String toString() {
      return "Variants";
    }

    @Override
    public JPanel createView() {
        SortPanel panel = new SortPanel();
        panel.open(fields);
        panel.update();
        return panel;
    }

    public RadixObject getFields() {
        return fields;
    }

}
