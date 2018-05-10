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

package org.radixware.kernel.designer.common.tree;

import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.nodes.hide.HideSettings;
import org.radixware.kernel.designer.common.general.nodes.hide.Restorable;


public class RestoreUnmodifiedChildrenAction extends HideUnmodifiedChildrenAction {

    @Override
    protected void hideNodes(ProgressHandle handle, Branch b, List<RadixObject> list, Canceller canceller) {
        int i = 0;
        for (RadixObject ro : list) {
            if (canceller.isCancele) {
                break;
            }
            Node n = NodesManager.findNode(ro);
            if (canceller.isCancele) {
                break;
            }
            if (n != null) {
                Restorable hidable = n.getLookup().lookup(Restorable.class);
                if (hidable != null) {
                    hidable.restore(true);
                }
            } else {
                HideSettings.restore(true, ro);
            }
            handle.progress(++i);

        }
    }
    
    @Override
    public String getName() {
        return "Restore Unmodified";
    } 
}
