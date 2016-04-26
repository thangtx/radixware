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

package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.kernel.KernelModule;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public final class SaveAllForcedlyAction implements ActionListener {

    private void performAction(Collection<Branch> branches) {
        for (Branch branch : branches) {
            branch.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject.isSaveable() && !radixObject.isReadOnly() && !(radixObject instanceof KernelModule)) {
                        try {
                            radixObject.save();
                        } catch (IOException error) {
                            DialogUtils.messageError(error);
                        }
                    }
                }
            }, VisitorProviderFactory.createDefaultVisitorProvider());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
        if (branches.isEmpty()) {
            DialogUtils.messageError("There are no opened branches.");
            return;
        }

        if (!DialogUtils.messageConfirmation("Save all forcedly?")) {
            return;
        }

        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                RadixMutex.writeAccess(new Runnable() {
                    @Override
                    public void run() {
                        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Save All Forcedly");
                        progressHandle.start();
                        try {
                            performAction(branches);
                        } finally {
                            progressHandle.finish();
                        }
                    }
                });
            }
        });
    }
}
