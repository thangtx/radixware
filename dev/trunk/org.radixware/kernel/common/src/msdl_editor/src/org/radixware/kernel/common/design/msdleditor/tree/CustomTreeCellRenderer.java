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

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

    private ImageIcon rootIcon;
    private ImageIcon fieldIcon;
    private ImageIcon fieldsIcon;

    public CustomTreeCellRenderer() {
        rootIcon = new ImageIcon(CustomTreeCellRenderer.class.getResource("/org/radixware/kernel/common/design/msdleditor/img/msdl_scheme.png"));
        fieldIcon = new ImageIcon(CustomTreeCellRenderer.class.getResource("/org/radixware/kernel/common/design/msdleditor/img/field.png"));
        fieldsIcon = new ImageIcon(CustomTreeCellRenderer.class.getResource("/org/radixware/kernel/common/design/msdleditor/img/fields.png"));

    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
        if (nodeObj instanceof RootStructureNode) {
            tree.setToolTipText(null);
            setIcon(rootIcon);
            return this;
        }
        if (nodeObj instanceof HeaderFieldsNode || nodeObj instanceof FieldsNode || nodeObj instanceof VariantsNode) {
            tree.setToolTipText(null);
            setIcon(fieldsIcon);
            return this;
        }
        if (nodeObj instanceof FieldNode || nodeObj instanceof SequenceItemNode) {
            if (nodeObj instanceof FieldNode) {
                String comm = ((FieldNode) nodeObj).getFieldModel().getModel().getMsdlField().getDescription();
                if (!comm.trim().isEmpty() && !comm.equals("")) {
                    tree.setToolTipText(comm);
                } else {
                    tree.setToolTipText(null);
                }
            }
            setIcon(fieldIcon);
            return this;
        }
        return this;
    }

}
