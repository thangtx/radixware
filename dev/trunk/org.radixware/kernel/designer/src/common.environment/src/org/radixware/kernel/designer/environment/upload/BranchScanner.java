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

package org.radixware.kernel.designer.environment.upload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.editors.EditorsRegistry;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

/**
 * Visited all definitions of all opened branches and visualized the process.
 * Allows to scan branches for quick opening of definitions later.
 */
class BranchScanner {

    final Branch branch;

    public BranchScanner(Branch branch) {
        super();
        this.branch = branch;
    }

    private static final class Cancel implements Cancellable {

        boolean canceled = false;

        @Override
        public boolean cancel() {
            canceled = true;
            return false;
        }
    }

    private Collection<Module> collectModules(Branch branch) {
        final List<Module> modules = new ArrayList<Module>();
        for (Layer layer : branch.getLayers().getInOrder()) {
            final Segment[] segments = {layer.getDds(), layer.getAds(),layer.getUds()};
            for (Segment segment : segments) {
                final List<Module> segmentModules = segment.getModules().list();
                RadixObjectsUtils.sortByName(segmentModules);
                modules.addAll(segmentModules);
            }
        }

        return modules;
    }

    private static final class EmptyVisitor implements IVisitor {

        @Override
        public void accept(RadixObject object) {
            // NOTHING
        }
    }

    private static class OpenEditorsVisitor implements IVisitor {

        @Override
        public void accept(RadixObject radixObject) {
            EditorsRegistry.onScanned(radixObject);
        }
    }

    public void scan() {
        final Cancel cancel = new Cancel();

        ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Scanning branch", cancel);
        final Collection<Module> modules;
        try {
            progressHandle.start();
            modules = collectModules(branch);
        } finally {
            progressHandle.finish();
        }

        progressHandle = ProgressHandleFactory.createHandle("Scanning modules", cancel);
        try {
            final IVisitor emptyVisitor = new EmptyVisitor();
            final IVisitor opedEditorsVisitor = new OpenEditorsVisitor();

            int count = 0;
            progressHandle.start(modules.size());
            for (int step = 0; step < 2; step++) { // 0=scan, 1=open editors
                if (step == 1 && EditorsManager.getDefault().wasOpened()) { // RADIX-3899
                    break;
                }
                for (Module module : modules) {
                    if (cancel.canceled) {
                        break;
                    }
                    if (!RadixFileUtil.getOpenedBranches().contains(branch)) {
                        break;
                    }
                    if (step == 0) {
                        progressHandle.progress(module.getQualifiedName(), count++);
                    }
                    module.visitAll(step == 0 ? emptyVisitor : opedEditorsVisitor);
                }
            }
        } finally {
            progressHandle.finish();
        }
    }
}
