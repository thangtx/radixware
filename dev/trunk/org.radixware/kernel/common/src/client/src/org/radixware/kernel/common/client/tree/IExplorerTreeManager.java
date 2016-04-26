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

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public interface IExplorerTreeManager {

    public IExplorerTree openTree(final List<ExplorerRoot> explorerRoots, final IWidget parentWindow) throws ServiceClientException, InterruptedException;

    public IExplorerTree openSubTree(final IExplorerTreeNode node, final IWidget parentWindow);

    public IExplorerTree getCurrentTree();

    public void updateVersion(Collection<Id> changedDefinitions) throws CantUpdateVersionException;

    public void afterEntitiesRemoved(Collection<Pid> removedEntitiesPids, IWidget currentView);//RADIX-2425

    public boolean closeAll(final boolean forced);

    public void translate();
}
