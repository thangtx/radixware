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

package org.radixware.kernel.designer.common.dialogs.tree;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Node;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;



public interface INavigatorFilter {

    public void addChangeListener(ChangeListener changeListener);

    public IAcceptor<Node> getNodeAcceptor();

    /**
     * this method must always return the same instance
     * @return
     */
    public Component getComponent();

    /**
     * @return position of the component
     * in terms of BorderLayout. It should not return
     * BorderLayout.Center, beacause it's holded by Tree component.<br><br>
     * Resulted code will looks like:<br><br>
     * {@code container.setLayout(new BorderLayout());}<br>
     * {@code container.add(treeView, BorderLayout.CENTER);}<br>
     * {@code container.add(filter.getComponent(), filter.getComponentPosition());}<br>
     */
    public Object getComponentPosition();

    /**
     * Indicates whether or not the tree shall expand.
     */
    boolean expandTree();
}
