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
import javax.swing.tree.DefaultMutableTreeNode;


public abstract class ItemNode extends DefaultMutableTreeNode {
    Children children1;
    public ItemNode() {
        setUserObject(this);
    }
    abstract public JPanel createView();
    public void setChildren(Children children) {
        this.children1 = children;
        children.refresh();
    }
    public Children getChildren() {
        return children1;
    }
}
