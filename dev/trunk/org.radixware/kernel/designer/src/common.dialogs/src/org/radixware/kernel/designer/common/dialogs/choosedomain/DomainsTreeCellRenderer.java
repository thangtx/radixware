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

package org.radixware.kernel.designer.common.dialogs.choosedomain;

import java.awt.Color;
import java.awt.Component;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.types.Id;


class DomainsTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setBackgroundNonSelectionColor(Color.WHITE);
        setBorderSelectionColor(super.getBackgroundSelectionColor()); //prevents unusable black border when runing under GNOME desktop environment

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        assert (value instanceof DefaultMutableTreeTableNode);
        final DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
        final TreeTableNode parentNode = node.getParent();
        if (parentNode != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof AdsDomainDef) {
                final AdsDomainDef adsDomainDef = (AdsDomainDef) userObject;
                setIcon(adsDomainDef.getIcon().getIcon());
                if (parentNode.equals(tree.getModel().getRoot())) {
                    setText(adsDomainDef.getQualifiedName());
                } else {
                    setText(adsDomainDef.getName());
                }
            } else if(userObject instanceof Id) {
                Id unresolvedId = (Id) userObject;
                setForeground(Color.RED);
                setText(unresolvedId.toString());
            }
        }
        return this;
    }
}
