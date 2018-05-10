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

package org.radixware.kernel.common.client.tree.nodes;

import java.util.List;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.views.IExplorerItemView;


public interface IExplorerTreeNode {

    public IExplorerTreeNode getParentNode();

    public List<IExplorerTreeNode> getChildNodes();
    
    public boolean isChildNodesInited();

    public List<IExplorerTreeNode> getChildNodesRecursively();

    public long getIndexInExplorerTree();

    public IExplorerTree getExplorerTree();

    public boolean isValid();
    
    public boolean isRemovable();

    public IExplorerItemView getView();

    public Exception getCreationModelException();

    public String getCreationModelExceptionMessage();

    public String getPath();

    public String getName();
    
    public org.radixware.schemas.clientstate.ExplorerTreeNode writeToXml(final org.radixware.schemas.clientstate.ExplorerTreeNode node);
}
