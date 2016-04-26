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

package org.radixware.kernel.designer.common.tree.actions;

import org.netbeans.modules.subversion.ui.history.SearchHistoryAction;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class RadixSvnSearchHistoryAction extends RadixSvnAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        SearchHistoryAction searchHistoryAction = SystemAction.get(SearchHistoryAction.class);
        searchHistoryAction.performAction();
    }

    @Override
    public String getName() {
        return "Search History...";
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.SUBVERSION.SEARCH_HOSTORY.getResourceUri();
    }
}
