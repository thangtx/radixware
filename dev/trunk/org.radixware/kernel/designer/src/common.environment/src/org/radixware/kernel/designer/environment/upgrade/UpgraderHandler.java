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

package org.radixware.kernel.designer.environment.upgrade;

import java.util.Collection;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.upgrade.Upgrader;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class UpgraderHandler extends Task {

    private volatile boolean posted = false;
    private static final UpgraderHandler INSTANCE = new UpgraderHandler();
    private IRadixEventListener firstStepListener = new IRadixEventListener() {

        @Override
        public void onEvent(RadixEvent e) {
            onFirstStep();
        }
    };

    private void onFirstStep() {
        if (!posted) {
            posted = true;
            RequestProcessor.getDefault().post(this, 220);
        }
    }

    private UpgraderHandler() {
        Upgrader.addFirstStepListener(firstStepListener);
    }

    public static UpgraderHandler getDefault() {
        return INSTANCE;
    }

    private void runOnWriteAccess() {

        final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
        for (Branch branch : branches) {
            Upgrader.upgrade(branch);
        }
        posted = false;
    }

    @Override
    public void run() {
        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Upgrading branch, please wait");
        try {
            progressHandle.start();

            RadixMutex.writeAccess(new Runnable() {

                @Override
                public void run() {
                    runOnWriteAccess();
                }
            });
        } finally {
            progressHandle.finish();
        }
    }
}
