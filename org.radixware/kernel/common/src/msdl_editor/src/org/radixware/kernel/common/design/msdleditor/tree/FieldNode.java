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

import javax.swing.JPanel;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.design.msdleditor.field.FieldPanel;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedEvent;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedListener;

public class FieldNode extends ItemNode  implements MsdlFieldStructureChangedListener{
    private MsdlField fieldModel;
    private Tree tree;
    public FieldNode (Tree tree, MsdlField fieldModel) {
        this.fieldModel = fieldModel;
        this.tree = tree;
        setChildren(new FieldChildren(tree,fieldModel,this));
        fieldModel.getStructureChangedSupport().addEventListener(this);
    }

    @Override
    public JPanel createView() {
        FieldPanel panel = new FieldPanel();
        panel.open(fieldModel,null,null, null);
        return panel;
    }

    @Override
    public String toString() {
      return fieldModel.getTreeItemName();
    }

    public MsdlField getFieldModel() {
        return fieldModel;
    }

    @Override
    public void onEvent(MsdlFieldStructureChangedEvent e) {
        if (e.getType() != MsdlFieldStructureChangedEvent.EType.NAME_ONLY) {
            FieldChildren fc = new FieldChildren(tree,fieldModel,this);
            setChildren(fc);
            fc.structureChanged();
        }
        tree.nameChanged();
    }

}
