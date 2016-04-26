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

package org.radixware.kernel.designer.environment.project;

import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.environment.upload.BranchesUpdater;


public class RadixProjectOpenedHook extends ProjectOpenedHook {

    private final Branch branch;

    public RadixProjectOpenedHook(Branch branch) {
        this.branch = branch;
    }

    @Override
    protected void projectClosed() {
        branch.close();
        BranchesUpdater.getDefault().remove(branch);

    }

    @Override
    protected void projectOpened() {
        BranchesUpdater.getDefault().put(branch);
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                WindowManager.getDefault().getMainWindow().setTitle(WindowManager.getDefault().getMainWindow().getTitle() + " - " + branch.getName());
            }
        });
    }
}
