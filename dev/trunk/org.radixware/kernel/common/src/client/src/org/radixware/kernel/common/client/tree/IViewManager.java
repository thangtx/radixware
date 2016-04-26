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

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.views.IView;


public interface IViewManager {

    public IView openView(final IExplorerTreeNode node, final boolean closeCurrent);

    public IExplorerTreeNode getCurrentNode();

    public boolean isViewOpenedForModel(Model model);

    public boolean canSafetyClose();

    public void closeAll();

    public void closeCurrentView();
}
