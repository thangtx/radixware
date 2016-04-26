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

package org.radixware.kernel.designer.debugger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


final class BranchesLoadService {

    private List<Branch> branches = null;
    private ScheduledFuture f;
    private Runnable readyAction;

    BranchesLoadService(Runnable readyAction) {
        this.readyAction = readyAction;
    }

    List<Branch> getBranches() {
        synchronized (this) {
            if (branches == null || branches.isEmpty()) {
                Collection<Branch> loadedBranches = RadixFileUtil.getOpenedBranches();
                if (loadedBranches.isEmpty()) {
                    return Collections.emptyList();
                } else {
                    branches = new ArrayList<Branch>(loadedBranches);

                }
            }
            return branches;
        }
    }

    private void onReady() {
        if (readyAction != null) {
            readyAction.run();
        }
    }

    public void load() {

        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                synchronized (BranchesLoadService.this) {
                    getBranches();
                    if (branches != null && !branches.isEmpty()) {
                        if (f != null) {
                            f.cancel(true);
                        }
                        onReady();
                    }
                }
            }
        };

        f = service.scheduleWithFixedDelay(runnable, 1000, 1000, TimeUnit.MILLISECONDS);
    }
}