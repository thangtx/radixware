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

package org.radixware.kernel.designer.debugger.impl.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.DebuggerManagerAdapter;
import org.netbeans.api.debugger.Session;
import org.radixware.kernel.designer.debugger.RadixDebugger;
import org.radixware.kernel.designer.debugger.impl.KernelSourcePathProvider;


public class SourcesTreeModel extends AbstractTreeModel {

    public SourcesTreeModel() {
        super(null, 0);
        DebuggerManager.getDebuggerManager().addDebuggerListener(new DebuggerManagerAdapter() {

            @Override
            public void sessionAdded(Session session) {
                fireModelChange();
            }

            @Override
            public void sessionRemoved(Session session) {
                fireModelChange();
            }
        });
        KernelSourcePathProvider.getInstance().addPropertyChangeListener(KernelSourcePathProvider.PROP_NAME_SOURCE_ROOTS, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireModelChange();
            }
        });
    }

    @Override
    protected List<Object> getChildren(Object parent) {
        if (parent == ROOT) {
            KernelSourcePathProvider provider = KernelSourcePathProvider.getInstance();
            return new ArrayList<Object>(provider.getSourceRoots());
        } else {
            return Collections.emptyList();
        }
    }
}
