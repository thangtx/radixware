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

package org.radixware.kernel.designer.environment.tasks;

import java.util.Collection;
import java.util.Iterator;
import org.netbeans.spi.tasklist.TaskScanningScope;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


class BranchScanningScope extends TaskScanningScope {

    BranchScanningScope() {
        super("All opened branches", "Show tasks for currently opened branches", RadixObjectIcon.BRANCH.getImage());
    }

    public static final BranchScanningScope create() {
        return new BranchScanningScope();
    }

    @Override
    public boolean isInScope(FileObject resource) {
        return false;
    }

    @Override
    public void attach(Callback callback) {
    }

    @Override
    public Lookup getLookup() {
        Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
        Object[] branchArr = branches.toArray(new Object[0]);
        return Lookups.fixed(branchArr);
    }

    @Override
    public Iterator<FileObject> iterator() {
        return new Iterator<FileObject>() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public FileObject next() {
                return null;
            }

            @Override
            public void remove() {
            }
        };
    }
}
