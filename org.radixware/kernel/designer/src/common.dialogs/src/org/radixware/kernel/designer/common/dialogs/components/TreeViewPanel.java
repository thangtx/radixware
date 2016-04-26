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

package org.radixware.kernel.designer.common.dialogs.components;

import javax.swing.ActionMap;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup;


public class TreeViewPanel extends JPanel implements ExplorerManager.Provider, Lookup.Provider {
    private final ExplorerManager explorerManager = new ExplorerManager();
    private final BeanTreeView treeView = new BeanTreeView();
    private final Lookup lookup = ExplorerUtils.createLookup(explorerManager, new ActionMap());

    public TreeViewPanel() {
        setLayout(new java.awt.BorderLayout());
        add(treeView, java.awt.BorderLayout.CENTER);
    }

    @Override
    public final ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    @Override
    public final Lookup getLookup() {
        return lookup;
    }

    public final BeanTreeView getTreeView() {
        return treeView;
    }

}
