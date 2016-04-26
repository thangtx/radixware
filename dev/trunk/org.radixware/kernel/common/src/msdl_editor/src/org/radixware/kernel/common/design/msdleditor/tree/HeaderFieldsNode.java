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
import org.radixware.kernel.common.msdl.MsdlStructureHeaderFields;


public class HeaderFieldsNode extends ItemNode implements IFieldsOwner {

    private MsdlStructureHeaderFields fields;
    @Override
    public void delField(MsdlField field) {
        //fields.remove(field);
    }

    @Override
    public MsdlField addField() {
        MsdlField f = fields.createChild();
        fields.add(f);
        return f;
    }

    private static final class FieldList extends Children<MsdlField> implements ContainerChangesListener {
        private HashMap<MsdlField, DefaultMutableTreeNode> field2node = new HashMap<MsdlField, DefaultMutableTreeNode>();
        private  MsdlStructureHeaderFields fields;

        private FieldList(Tree tree, DefaultMutableTreeNode parent, MsdlStructureHeaderFields field) {
            super(tree,parent);
            this.fields = field;
            field.getContainerChangesSupport().addEventListener(this);
        }

        @Override
        protected void createKeys(List<MsdlField> itemsKeys) {
            for (MsdlField f : fields)
                itemsKeys.add(f);
        }

        @Override
        public DefaultMutableTreeNode createNodeForKey(MsdlField key) {
            DefaultMutableTreeNode node = field2node.get(key);
            if (node == null) {
                node = new FieldNode(tree,key);
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

   public HeaderFieldsNode(Tree tree, MsdlStructureHeaderFields fieldModel) {
       setChildren(new FieldList(tree,this,fieldModel));
       fields = fieldModel;
   }

    @Override
    public String toString() {
      return "Header Fields";
   }

    @Override
    public JPanel createView() {
        SortPanel panel = new SortPanel();
        panel.open(fields);
        return panel;
    }

    public RadixObject getFields() {
        return fields;
    }
}
