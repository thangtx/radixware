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

package org.radixware.kernel.common.client.tree;

import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.actions.Action;


public interface IExplorerTreePresenter {
    IExplorerTree getView();
    void setFocus();
    void removeNode(IExplorerTreeNode node);
    boolean isNodeExists(IExplorerTreeNode node);
    void setCurrent(IExplorerTreeNode node);
    void scrollTo(IExplorerTreeNode node);
    void resizeToContents();
    Action createAction(final Icon icon, final String title);
}
