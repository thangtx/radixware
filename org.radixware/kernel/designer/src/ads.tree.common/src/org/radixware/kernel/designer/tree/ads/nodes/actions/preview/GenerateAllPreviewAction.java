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
package org.radixware.kernel.designer.tree.ads.nodes.actions.preview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public class GenerateAllPreviewAction implements ActionListener {

    private void performAction(Collection<Branch> branches, final ProgressHandle progressHandle, final Canceller canceller) {

        for (Branch branch : branches) {
            branch.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    progressHandle.progress("Generate preview for " + radixObject.getQualifiedName());
                    AdsUtils.savePreview(radixObject);
                    progressHandle.progress("");
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return AdsUtils.isEnableHumanReadable(radixObject);
                }

                @Override
                public boolean isCancelled() {
                    return super.isCancelled() || canceller.isCancelled; 
                }
                
                

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    if (radixObject instanceof ModuleDefinitions) {
                        return true;
                    } else if (radixObject instanceof Module) {
                        return radixObject instanceof AdsModule;
                    } else if (radixObject instanceof Segment) {
                        return radixObject instanceof AdsSegment;
                    } else if (radixObject instanceof Layer) {
                        return true;
                    } else if (radixObject instanceof Branch) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
        if (branches.isEmpty()) {
            DialogUtils.messageError("There are no opened branches.");
            return;
        }
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                RadixMutex.readAccess(new Runnable() {

                    @Override
                    public void run() {
                        final Canceller canceller = new Canceller();
                        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Generate preview files...", canceller);
                        progressHandle.start();
                        try {
                            performAction(branches, progressHandle, canceller);
                        } finally {
                            progressHandle.finish();
                        }
                    }
                });
            }
        });
    }

    private class Canceller implements Cancellable {

        private boolean isCancelled = false;

        @Override
        public boolean cancel() {
            isCancelled = true;
            return true;
        }
    }
}
