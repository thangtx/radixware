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
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.design.msdleditor.field.VariantFieldPanel;


public class FieldVariantNode extends FieldNode {
    private MsdlVariantField fieldModel;
    public FieldVariantNode(Tree tree, MsdlVariantField fieldModel) {
        super(tree,fieldModel);
        this.fieldModel = fieldModel;
    }
    @Override
    public JPanel createView() {
        VariantFieldPanel panel = new VariantFieldPanel();
        panel.open(fieldModel,null, null, null);
        return panel;
    }
}
